package kevilnkaito.trivia;

import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import kevilnkaito.trivia.databinding.ActivityMainBinding;
import kevilnkaito.trivia.model.Question;
import kevilnkaito.trivia.repository.QuestionRepository;
import kevilnkaito.trivia.utils.ScoreSharedPreferences;

public class MainActivity extends AppCompatActivity {

    private List<Question> questions;
    private ActivityMainBinding binding;

    private int currentQuestionIndex = 0;
    private int currentScore = 0;

    ScoreSharedPreferences scoreSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        scoreSharedPreferences = new ScoreSharedPreferences(this);

        binding.highestScoreText.setText(String.format(getString(R.string.highestScoreText_formatted), scoreSharedPreferences.getHighestScore()));
        binding.currentScoreText.setText(String.format(getString(R.string.currentScoreText_formatted), currentScore));
        currentQuestionIndex = scoreSharedPreferences.getState();

        new QuestionRepository().getQuestions(questions -> {
            this.questions = questions;

            updateQuestionAndCounter();
        });

        binding.buttonNext.setOnClickListener(view -> {
            nextQuestion();
        });

        binding.buttonTrue.setOnClickListener(view -> {
            checkAnswer(true);
        });

        binding.buttonFalse.setOnClickListener(view -> {
            checkAnswer(false);
        });
    }

    private void nextQuestion() {
        currentQuestionIndex++;

        if (currentQuestionIndex >= questions.size()) {
            currentQuestionIndex = 0;
        }

        updateQuestionAndCounter();
    }

    private void updateQuestionAndCounter() {
        binding.questionTextview.setText(questions.get(currentQuestionIndex).getAnswer());
        binding.textViewOutOf.setText(String.format(getString(R.string.textViewOutOf_formatted), currentQuestionIndex + 1, questions.size()));
    }

    private void checkAnswer(boolean answerChose) {

        String result = "";

        if (questions.get(currentQuestionIndex).isAnswerTrue() == answerChose) {
            result = getString(R.string.correct_answer);
            fadeAnimation();
            addPoints();
        } else {
            result = getString(R.string.incorrect_answer);
            shakeAnimation();
            deductPoints();
        }

        Snackbar.make(binding.cardView, result, Snackbar.LENGTH_SHORT).show();

        //updateQuestionAndCounter();
    }

    private void fadeAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        //binding.cardView.setAnimation(alphaAnimation);
        binding.cardView.startAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextview.setTextColor(Color.GREEN);
                setAllButtonEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextview.setTextColor(Color.WHITE);
                nextQuestion();
                setAllButtonEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        //binding.cardView.setAnimation(shake);
        binding.cardView.startAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextview.setTextColor(Color.RED);
                setAllButtonEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextview.setTextColor(Color.WHITE);
                nextQuestion();
                setAllButtonEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void addPoints() {
        currentScore += 10;
        binding.currentScoreText.setText(String.format(getString(R.string.currentScoreText_formatted), currentScore));
    }

    private void deductPoints() {
        currentScore -= 5;

        if (currentScore <= 0) {
            currentScore = 0;
        }

        binding.currentScoreText.setText(String.format(getString(R.string.currentScoreText_formatted), currentScore));
    }

    private void setAllButtonEnabled(boolean enabled) {
        //Ngăn chặn ấn nút liên tiếp khi animation chưa kết thúc -> hàm này gọi khi animation_Start và animation_End
        binding.buttonTrue.setEnabled(enabled);
        binding.buttonFalse.setEnabled(enabled);
        binding.buttonNext.setEnabled(enabled);
    }

    @Override
    protected void onPause() {
        super.onPause();

        scoreSharedPreferences.setHighestScore(currentScore);
        scoreSharedPreferences.setState(currentQuestionIndex);
    }
}