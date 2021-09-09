package kevilnkaito.NewsReader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> titles = new ArrayList<>();
    List<String> contents = new ArrayList<>();

    SQLiteDatabase articlesSqLiteDatabase;
    ArrayAdapter arrayAdapter;

    ListView listView;
    ProgressBar progressBar;
    ProgressBar progressBarHorizontal;
    CardView cardViewProgressBarStyleHorizontal;
    TextView lblLoading;
    CardView cardViewSetting;
    EditText edtTxtMaxItem;

    int maxItem = 20;

    boolean isEmpty = true;

    boolean onlineMode = true;

    DownloadTask downloadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);
        progressBarHorizontal = findViewById(R.id.progressBarHorizontal);
        cardViewProgressBarStyleHorizontal = findViewById(R.id.cardViewProgressBarStyleHorizontal);
        lblLoading = findViewById(R.id.lblLoading);
        cardViewSetting = findViewById(R.id.cardViewSetting);
        edtTxtMaxItem = findViewById(R.id.edtTxtMaxItem);

        //
        initData();
        loadData();

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titles);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                intent.putExtra("content", contents.get(i));
                intent.putExtra("onlineMode", onlineMode);
                startActivity(intent);
            }
        });

        updateListView();

    }

    private void initData() {

        articlesSqLiteDatabase = this.openOrCreateDatabase("Articles", MODE_PRIVATE, null);

        //articlesSqLiteDatabase.execSQL("DROP TABLE IF EXISTS Articles");

        articlesSqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Articles (id INTEGER PRIMARY KEY, articleId INTEGER, title VARCHAR, content VARCHAR)");

        isEmpty = !isExistsArticles();

    }

    private void loadData() {

        if (isEmpty) {
            downloadTask = new DownloadTask();
            downloadTask.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
        }

    }

    private void updateListView() {

        Cursor cursor = articlesSqLiteDatabase.rawQuery("SELECT * FROM articles", null);

        int titleIndex = cursor.getColumnIndex("title");
        int contentIndex = cursor.getColumnIndex("content");

        if (cursor.moveToFirst()) {
            titles.clear();
            contents.clear();

            do {
                titles.add(cursor.getString(titleIndex));
                contents.add(cursor.getString(contentIndex));
            } while (cursor.moveToNext());

            arrayAdapter.notifyDataSetChanged();

        }

    }

    private boolean isExistsArticles() {
        Cursor cursor = articlesSqLiteDatabase.rawQuery("SELECT * FROM articles", null);
        return cursor.moveToFirst();
    }

    public void onBtnSetMaxItemClick(View view) {

        hideKeyboard();
        cardViewSetting.setVisibility(View.INVISIBLE);

        int newMaxItem = Integer.parseInt(edtTxtMaxItem.getText().toString());

        if (maxItem != newMaxItem) {
            maxItem = newMaxItem;
            refreshData();
        }

    }

    class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            //
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBarHorizontal.setVisibility(View.VISIBLE);
                    cardViewProgressBarStyleHorizontal.setVisibility(View.VISIBLE);
                    lblLoading.setVisibility(View.VISIBLE);
                    progressBarHorizontal.setProgress(0);
                    progressBarHorizontal.setMax(maxItem);
                }
            });


            //
            String result = "";

            String urlString = strings[0]; //coi như chỉ làm việc với phần tử đầu tiên

            String jsonString = readFromUrl(urlString);


            try {

                // Xóa/Reset bảng Database
                articlesSqLiteDatabase.execSQL("DELETE FROM Articles");

                JSONArray articleIdArray = new JSONArray(jsonString);

                if (articleIdArray.length() < maxItem) {
                    maxItem = articleIdArray.length();
                    progressBarHorizontal.setMax(maxItem);
                }

                int count = 0;

                for (int i = 0; i < articleIdArray.length(); i++) {
                    String articleId = articleIdArray.getString(i);
                    String articleItemUrl = "https://hacker-news.firebaseio.com/v0/item/" + articleId + ".json?print=pretty";

                    String articleItemString = readFromUrl(articleItemUrl);

                    JSONObject articleItemObject = new JSONObject(articleItemString);

                    if (!articleItemObject.isNull("title") && !articleItemObject.isNull("url")) {
                        String articleTitle = articleItemObject.getString("title");
                        String articleUrl = articleItemObject.getString("url");

                        //TODO: articleContent đang lấy tạm URL, vì articleContent nặng quá -> máy load chậm + webView không hiện thị được
                        //String articleContent = readFromUrl("https://mbasic.facebook.com");

                        String articleContent = "";
                        if (onlineMode) {
                            articleContent = articleUrl;
                        } else {
                            articleContent = readFromUrl(articleUrl);
                        }

                        insertArticleToDatabase(articleId, articleTitle, articleContent);

                        /*Log.i("articleTitle: ", articleTitle);
                        Log.i("articleUrl: ", articleUrl);
                        Log.i("articleContent: ", articleContent);*/

                        count++;
                        if (count == maxItem) {
                            break;
                        }

                    }

                    //https://stackoverflow.com/questions/5161951/android-only-the-original-thread-that-created-a-view-hierarchy-can-touch-its-vi
                    int finalCount = count;
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            // Stuff that updates the UI

                            progressBarHorizontal.setProgress(finalCount);
                            lblLoading.setText("Loading... " + finalCount + "/" + maxItem + " (item)");

                            updateListView();

                        }
                    });


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return result;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            updateListView();

            //
            progressBar.setVisibility(View.INVISIBLE);
            progressBarHorizontal.setVisibility(View.INVISIBLE);
            cardViewProgressBarStyleHorizontal.setVisibility(View.INVISIBLE);
            lblLoading.setVisibility(View.INVISIBLE);

            isEmpty = false;

        }
    }

    private void insertArticleToDatabase(String articleId, String articleTitle, String articleContent) {

        String sql = "INSERT INTO articles (articleId, title, content) VALUES (?, ?, ?)";

        SQLiteStatement sqLiteStatement = articlesSqLiteDatabase.compileStatement(sql);

        sqLiteStatement.bindString(1, articleId);
        sqLiteStatement.bindString(2, articleTitle);
        sqLiteStatement.bindString(3, articleContent);

        sqLiteStatement.execute();

    }

    private String readFromUrl(String urlString) {

        try {

            String result = "";

            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            int data = inputStreamReader.read();

            while (data != -1) {
                char current = (char) data;
                result += current;
                data = inputStreamReader.read();
            }


            return result;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private void refreshData() {
        lblLoading.setText("Refresh...");

        articlesSqLiteDatabase.execSQL("delete from Articles");

        isEmpty = true;

        if (downloadTask != null) {
            downloadTask.cancel(true);
        }

        loadData();
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(edtTxtMaxItem.getWindowToken(), 0);
    }

    /*private List<String> getArticleIds(String urlString) {
        return null;
    }

    private Article getArticleItem(String articleId) {
        return null;
    }

    private String getArticleContent(String ArticleUrlString) {
        return null;
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.setting_menu:
                cardViewSetting.setVisibility(cardViewSetting.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                return true;
            case R.id.refresh_menu:
                refreshData();
                Toast.makeText(this, "Refresh Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.switch_mode_menu:
                onlineMode = !onlineMode;
                if (onlineMode) {
                    item.setIcon(R.drawable.ic_cloud);
                    Toast.makeText(this, "Online mode Selected", Toast.LENGTH_SHORT).show();
                } else {
                    //item.setIcon(R.drawable.ic_cloud_off);
                    item.setIcon(R.drawable.ic_cloud_download);
                    Toast.makeText(this, "Offline mode Selected", Toast.LENGTH_SHORT).show();
                }
                refreshData();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}