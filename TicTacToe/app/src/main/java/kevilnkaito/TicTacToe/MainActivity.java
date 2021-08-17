package kevilnkaito.TicTacToe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Tham khảo: https://codinginflow.com/tutorials/android/tic-tac-toe/part-3-finish-game
    //Đề bài: https://www.youtube.com/watch?v=7HzNOrjdh38

    private ImageView[][] imageViews = new ImageView[3][3];

    private TextView lblResult;
    private TextView lblNotification;
    private Button btnPlayAgain;
    private Button btnReset;

    boolean isRed = true;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "imageView_" + i + "_" + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                imageViews[i][j] = findViewById(resID);
                //imageViews[i][j].setOnClickListener(this);
            }
        }

        lblResult = findViewById(R.id.lblResult);
        lblNotification = findViewById(R.id.lblNotification);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnReset = findViewById(R.id.btnReset);

        lblNotification.setText("Next turn: RED");

    }

    public void tap(View view) {

        btnReset.setAlpha(1);

        ImageView imageView = (ImageView) view;

        if (imageView.getDrawable() != null) {
            return;
        }

        //Set image
        if (isRed) {
            imageView.setImageResource(R.drawable.red);
            imageView.setTag("red");
            lblNotification.setText("Next turn: YELLOW");
        } else {
            imageView.setImageResource(R.drawable.yellow);
            imageView.setTag("yellow");
            lblNotification.setText("Next turn: RED");
        }
        isRed = !isRed;

        //Animation:
        imageView.setTranslationY(-1000);
        imageView.animate().translationYBy(1000).setDuration(500);

        if (checkForWin()) {

            lblResult.setAlpha(1);
            lblNotification.setAlpha(0);
            btnPlayAgain.setAlpha(1);
            btnReset.setAlpha(0);

            if (isRed) {
                lblResult.setText("Result: Yellow Win");
            } else {
                lblResult.setText("Result: Red Win");
            }

            setEnabled_imageViews(false);

            return;
        }

        count++;
        if (count == 9) {
            lblResult.setAlpha(1);
            lblNotification.setAlpha(0);
            btnPlayAgain.setAlpha(1);
            btnReset.setAlpha(0);

            setEnabled_imageViews(false);

            lblResult.setText("Result: Draw!!!");
        }

    }

    private boolean checkForWin() {

        // * * *
        // _ _ _
        // _ _ _
        for (int i = 0; i < 3; i++) {
            if (imageViews[i][0].getTag() == imageViews[i][1].getTag()
                    && imageViews[i][0].getTag() == imageViews[i][2].getTag()
                    && imageViews[i][0].getTag() != null) {
                return true;
            }
        }

        // * _ _
        // * _ _
        // * _ _
        for (int i = 0; i < 3; i++) {
            if (imageViews[0][i].getTag() == imageViews[1][i].getTag()
                    && imageViews[0][i].getTag() == imageViews[2][i].getTag()
                    && imageViews[0][i].getTag() != null) {
                return true;
            }
        }

        // * _ _
        // _ * _
        // _ _ *
        if (imageViews[0][0].getTag() == imageViews[1][1].getTag()
                && imageViews[0][0].getTag() == imageViews[2][2].getTag()
                && imageViews[0][0].getTag() != null) {
            return true;
        }

        // _ _ *
        // _ * _
        // * _ _
        if (imageViews[0][2].getTag() == imageViews[1][1].getTag()
                && imageViews[0][2].getTag() == imageViews[2][0].getTag()
                && imageViews[0][2].getTag() != null) {
            return true;
        }

        return false;

    }

    public void btnPlayAgain_onClick(View view) {

        reset();

    }

    public void btnReset_onClick(View view) {

        reset();

    }

    private void reset() {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                imageViews[i][j].setImageResource(0);
                imageViews[i][j].setTag(null);
            }
        }

        lblResult.setAlpha(0);
        lblNotification.setAlpha(1);
        btnPlayAgain.setAlpha(0);
        btnReset.setAlpha(0);

        lblNotification.setText("Next turn: RED");

        setEnabled_imageViews(true);

        count = 0;
        isRed = true;

    }

    private void setEnabled_imageViews(boolean isEnabled) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                imageViews[i][j].setEnabled(isEnabled);
            }
        }
    }

}