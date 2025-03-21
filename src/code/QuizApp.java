import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * TODO: Add hashmap, finish askQuestion(), finish searchQuizText(), finish checkAnswer(), make javadoc
 */

public class QuizApp extends Application
{
//    private final Map<String, String> quizData;
    private String currentQuestion;
    private String currentAnswer;
    private final TextField textField;
    private final Button submitButton;
    private final Button startQuizButton;
    private final Label questionLabel;

    public QuizApp()
    {
//        quizData = new HashMap<>();
        textField = new TextField();
        submitButton = new Button("Submit");
        startQuizButton = new Button("Start Quiz");
        questionLabel = new Label("Press Start Quiz to Begin");
    }

    @Override
    public void start(final Stage primaryStage)
    {
        final VBox mainTitleBox;
        final VBox buttonBox;
        final Scene startingScene;
        final Scene quizScene;
        final StackPane startingPane;

        mainTitleBox = new VBox(new Label("Welcome to Andrew, Brownie and Yang Quiz!\n" +
                "Please press \"Start Quiz\" to begin!"));

        // THIS SHOULD MAKE THE BUTTONS :D
        buttonBox = new VBox(10);
        buttonBox.getChildren().addAll(submitButton, startQuizButton, textField, questionLabel);

        // Blanks out textfield and submit button until you click "start quiz"
        textField.setDisable(true);
        submitButton.setDisable(true);

        // This should send you to a different stack pane if you click start quiz
        startingPane = new StackPane(startQuizButton);
        startingScene = new Scene(startingPane, 1000, 600);
        quizScene = new Scene(buttonBox, 1000, 600);

        // This gives the start quiz button several actions when you click
        startQuizButton.setOnAction(startQuiz ->
        {
            primaryStage.setScene(quizScene);
            textField.setDisable(false);
            submitButton.setDisable(false);
            searchQuizText();
        });

        // Checks answer
        submitButton.setOnAction(event -> checkAnswer());

        primaryStage.setScene(startingScene);
        primaryStage.setTitle("The Andrew, Brownie and Yang Quiz");
        primaryStage.show();
    }

    private void askQuestion()
    {

    }

    private void searchQuizText()
    {

    }

    private void checkAnswer()
    {

    }

    public static void main(final String[] args)
    {
        launch(args);
    }
}
