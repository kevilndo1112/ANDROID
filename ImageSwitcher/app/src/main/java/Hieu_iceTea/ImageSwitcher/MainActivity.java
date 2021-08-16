package Hieu_iceTea.ImageSwitcher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    Boolean isCat1 = true;

    public void onClick_btnChangeCat(View view) {

        ImageView imageView = findViewById(R.id.imageView);

        imageView.animate().alpha(0).setDuration(500);

        if (isCat1) {
            new Handler().postDelayed(() -> {
                imageView.setImageResource(R.drawable.cat2);
            }, 500);
        } else {
            new Handler().postDelayed(() -> {
                imageView.setImageResource(R.drawable.cat1);
            }, 500);
        }
        isCat1 = !isCat1;

        new Handler().postDelayed(() -> {
            imageView.animate().alpha(1).setDuration(500);
        }, 500);


        //Old version:
        /*ImageView imageView = findViewById(R.id.imageView);
        if (isCat1) {
            imageView.setImageResource(R.drawable.cat2);
        } else {
            imageView.setImageResource(R.drawable.cat1);
        }
        isCat1 = !isCat1;*/

    }

    public void onClick_btnHotFace(View view) {

        ImageView imageView = findViewById(R.id.imageView);

        int randomNumber = getRandomNumber(1, 6);

        int resId = getResources().getIdentifier("chibi" + randomNumber, "drawable", getPackageName());

        imageView.setImageResource(resId);

    }

    public int getRandomNumber(int min, int max) {

        //https://www.baeldung.com/java-generating-random-numbers-in-range
        //return (int) ((Math.random() * (max - min)) + min);

        //https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
        //return ThreadLocalRandom.current().nextInt(min, max + 1);

        //https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min; //nextInt as provided by Random is exclusive of the top value so you need to add 1

    }

}