package kevilnkaito.trivia.repository;

import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import kevilnkaito.trivia.controller.VolleyAppController;
import kevilnkaito.trivia.model.Question;

public class QuestionRepository {

    String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    String myUrl_en = "https://raw.githubusercontent.com/Hieu-iceTea/Learn.FPT.Aptech.SEM4.Android/main/UdemyCourse_Paulo/Trivia/app/src/main/java/hieu_iceTea/trivia/repository/data/statements-data.en.json";
    String myUrl_vi = "https://raw.githubusercontent.com/Hieu-iceTea/Learn.FPT.Aptech.SEM4.Android/main/UdemyCourse_Paulo/Trivia/app/src/main/java/hieu_iceTea/trivia/repository/data/statements-data.vi.json";

    List<Question> questions = new ArrayList<>();

    public List<Question> getQuestions(QuestionAsyncProcess callBackQuestionAsyncProcess) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                myUrl_vi,
                response -> {

                    try {

                        for (int i = 0; i < response.length(); i++) {

                            String answer = response.getJSONArray(i).getString(0);
                            boolean answerTrue = response.getJSONArray(i).getBoolean(1);

                            Question question = new Question(answer, answerTrue);

                            questions.add(question);

                        }

                        if (callBackQuestionAsyncProcess != null) {
                            callBackQuestionAsyncProcess.onFinished(questions);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> {
                    //nothing
                }

        );

        VolleyAppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questions;

    }

}
