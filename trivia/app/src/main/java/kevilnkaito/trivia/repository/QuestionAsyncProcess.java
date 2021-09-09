package kevilnkaito.trivia.repository;

import java.util.List;

import kevilnkaito.trivia.model.Question;

public interface QuestionAsyncProcess {
    void onFinished(List<Question> questions);
}
