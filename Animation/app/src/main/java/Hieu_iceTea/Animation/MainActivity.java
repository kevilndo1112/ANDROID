package Hieu_iceTea.Animation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    boolean isLarge = true;

    public void fade(View view) {

        ImageView imageView1 = findViewById(R.id.imageView1);

        if (isLarge) {
            imageView1.animate().scaleX(0.5f).scaleY(0.5f).setDuration(1000 * 2);
        } else {
            imageView1.animate().scaleX(1f).scaleY(1f).setDuration(1000 * 2);
        }

        isLarge = !isLarge;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView1 = findViewById(R.id.imageView1);

        imageView1.setX(-1000);
        imageView1.animate().translationXBy(1000).rotation(360 * 10).setDuration(1000 * 5); //Xoay 10 vòng (* 360 độ) && trong khoảng thời gian 5 giây

    }
}