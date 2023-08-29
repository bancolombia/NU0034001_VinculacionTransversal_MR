package co.com.bancolombia.expoquestion;

import co.com.bancolombia.commonsvnt.usecase.util.CoreFunctionDate;
import co.com.bancolombia.model.expoquestion.AlertQuestionnaire;
import co.com.bancolombia.model.expoquestion.Question;
import co.com.bancolombia.model.expoquestion.QuestionnaireAlert;
import co.com.bancolombia.model.expoquestion.QuestionnaireQuestion;
import co.com.bancolombia.model.expoquestion.QuestionnaireResponseOption;
import co.com.bancolombia.model.expoquestion.ResponseOption;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RequiredArgsConstructor
public class ExpoQuestionQuestionnaireTransUseCaseTest {
    @InjectMocks
    @Spy
    ExpoQuestionQuestionnaireTransUseCaseImpl questionnaireTUC;

    @Mock
    private CoreFunctionDate coreFD;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void transQuestionnaireAlertSaveTest() {
        AlertQuestionnaire alert = AlertQuestionnaire.builder().build();
        QuestionnaireAlert alertSave = questionnaireTUC.transQuestionnaireAlert(alert);
        assertNotNull(alertSave);
    }

    @Test
    public void transQuestionnaireQuestionTest() {
        List<Question> questions = Collections.singletonList(Question.builder().build());
        List<QuestionnaireQuestion> questionSaves = questionnaireTUC.transQuestionnaireQuestions(questions);
        assertNotNull(questionSaves);
    }

    @Test
    public void transQuestionnaireQuestionNullTest() {
        List<QuestionnaireQuestion> questionSaves = questionnaireTUC.transQuestionnaireQuestions(null);
        assertNotNull(questionSaves);
    }

    @Test
    public void transQuestionnaireResponseOptionTest() {
        List<ResponseOption> responseOptions = Collections.singletonList(ResponseOption.builder().build());
        List<QuestionnaireResponseOption> optionSaves = questionnaireTUC
                .transQuestionnaireResponseOptions(responseOptions);
        assertNotNull(optionSaves);
    }

    @Test
    public void transQuestionnaireResponseOptionNullTest() {
        List<QuestionnaireResponseOption> optionSaves = questionnaireTUC.transQuestionnaireResponseOptions(null);
        assertNotNull(optionSaves);
    }
}
