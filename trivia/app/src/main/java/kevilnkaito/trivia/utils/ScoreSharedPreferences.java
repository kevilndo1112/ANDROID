package kevilnkaito.trivia.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ScoreSharedPreferences {

    public static final String SCORE_DB_TAG = "Score_Database";
    public static final String HIGHEST_SCORE_TAG = "Highest_Score";
    public static final String State_TAG = "State";

    SharedPreferences sharedPreferences;

    public ScoreSharedPreferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SCORE_DB_TAG, Context.MODE_PRIVATE);
    }

    public int getHighestScore() {
        return sharedPreferences
                .getInt(HIGHEST_SCORE_TAG, 0);
    }

    public void setHighestScore(int highestScore) {

        if (highestScore > getHighestScore()) {
            sharedPreferences
                    .edit()
                    .putInt(HIGHEST_SCORE_TAG, highestScore)
                    .apply();
        }

    }

    public int getState() {
        return sharedPreferences
                .getInt(State_TAG, 0);
    }

    public void setState(int State) {
        sharedPreferences
                .edit()
                .putInt(State_TAG, State)
                .apply();
    }

}
