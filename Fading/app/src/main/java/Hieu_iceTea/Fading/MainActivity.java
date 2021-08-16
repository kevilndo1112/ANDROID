package Hieu_iceTea.Fading;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    boolean imageView1_isShowing = true;

    public void fade(View view) {

        ImageView imageView1 = findViewById(R.id.imageView1);
        ImageView imageView2 = findViewById(R.id.imageView2);

        if (imageView1_isShowing) {
            imageView1.animate().alpha(0).setDuration(500);
            imageView2.animate().alpha(1).setDuration(500);
        } else {
            imageView1.animate().alpha(1).setDuration(500);
            imageView2.animate().alpha(0).setDuration(500);
        }

        imageView1_isShowing = !imageView1_isShowing;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}