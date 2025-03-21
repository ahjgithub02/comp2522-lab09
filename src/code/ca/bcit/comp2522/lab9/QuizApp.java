package ca.bcit.comp2522.lab9;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javax.swing.*;
import java.io.File;
import java.nio.file.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Random;
import java.util.ArrayList;

/**
 * The {@code QuizApp} class is a JavaFX application that displays a quiz.
 * The quiz reads questions and answers from a file called quiz.txt and presents them to the user.
 *
 *<p>
 * Features:
 * <ul>
 *   <li>Loads questions and answers from a text file called quiz.txt</li>
 *   <li>Displays questions in a random order (without repetition)</li>
 *   <li>Allows users to submit their answers</li>
 *   <li>Tracks and displays the user's score at the bottom of the screen</li>
 *   <li>Provides a summary of their score at the end of their quiz</li>
 * </ul>
 *
 * How to use:
 * <ol>
 *   <li>Run the application</li>
 *   <li>Click "Start Quiz" to navigate to the quiz window</li>
 *   <li>Click "Start" to begin answering questions</li>
 *   <li>Enter answers and submit them</li>
 *   <li>You will receive feedback and a final score upon completion</li>
 * </ol>
 *
 * Dependencies:
 * <ul>
 *   <li>JavaFX</li>
 *   <li>External text file ("quiz.txt") which contain quiz questions and answers</li>
 *   <li>CSS file ("styles.css") for styling</li>
 * </ul>
 * </p>
 *
 * @author Andrew Hwang
 * @author Brownie Tran
 * @author Yang Li
 * @version 1.0
 */
public class QuizApp extends Application
{
    // Constants
    private final int QUESTION_INDEX = 0;
    private final int ZERO = 0;
    private final int ANSWER_INDEX = 1;
    private final int TEN = 10;
    private final int HEIGHT_PREF = 150;
    private final int MAX_WIDTH = 300;
    private final int BOX_WIDTH = 400;
    private final int BOX_HEIGHT = 400;

    // JavaFX variables
    private final TextField answerField;
    private final Button submitButton;
    private final Button startButton;
    private final Button startQuizButton;
    private final Label instructionLabel;
    private final Label welcomeLabel;
    private final Label questionLabel;
    private final Path path;
    private final List<String> quizData;
    private final Map<String, String> quizMap;
    private final String[] keyArray;
    private final List<Integer> askedQuestions;
    private final Label scoreLabel;

    // Initializing score integer
    private int score = 0;

    /**
     * Sets up the QuizApp by initializing UI components and loading quiz questions from a file.
     *
     * <p>Reads quiz.txt and stores the questions and answers in a HashMap for later use.
     * Also checks if the file exists before reading. If not found, an IOException is thrown.</p>
     *
     * @throws IOException if quiz.txt is missing or cannot be read.
     */
    public QuizApp() throws IOException
    {
        answerField = new TextField();
        answerField.setPromptText("Enter your Answer here");
        answerField.setMaxWidth(MAX_WIDTH);

        submitButton = new Button("Submit");
        startButton = new Button("Start");
        startQuizButton = new Button("Start Quiz");
        instructionLabel = new Label("Press Start to Begin");

        questionLabel = new Label("Question:");
        questionLabel.setWrapText(true);
        questionLabel.setMaxWidth(MAX_WIDTH);

        welcomeLabel = new Label("Welcome to the Andrew, Brownie and Yang Quiz!\n" + "Please press \"Start Quiz\" to begin!");
        welcomeLabel.setWrapText(true);

        scoreLabel = new Label("Score: " + score);

        path = Paths.get("src", "res", "quiz.txt");
        quizData = Files.readAllLines(path);

        if(!Files.exists(path))
        {
            throw new IOException("File not found");
        }

        quizMap = quizData.stream()
                        .map(s -> s.split("\\|"))
                        .collect(Collectors
                        .toMap(s -> s[QUESTION_INDEX], s -> s[ANSWER_INDEX]));
        Set<String> keys = quizMap.keySet();
        keyArray = keys.toArray(new String[ZERO]);
        askedQuestions = new ArrayList<>();

    }

    /**
     * Sets up the JavaFX stage, initializes the UI layout, and handles user interactions.
     *
     * <p>Creates two scenes: a start screen and the quiz screen. The quiz starts when the user
     * clicks "Start," enabling input fields and tracking answers. The quiz ends after 10 questions,
     * displaying the final score.</p>
     *
     * @param primaryStage The main stage for the application.
     */
    @Override
    public void start(final Stage primaryStage)
    {
        final VBox mainTitleBox;
        final VBox buttonBox;
        final VBox answersBox;
        final HBox startSubmitBox;
        final Scene startingScene;
        final Scene quizScene;
        final StackPane startingPane;
        final StackPane quizPane;
        final ScrollPane answersPane;
        final String[] question = {""};
        int[] count = {ZERO};

        mainTitleBox = new VBox(TEN);
        mainTitleBox.getChildren().addAll(welcomeLabel, startQuizButton);
        mainTitleBox.setAlignment(Pos.CENTER);

        startSubmitBox = new HBox(TEN);
        startSubmitBox.getChildren().addAll(submitButton, startButton);
        startSubmitBox.setAlignment(Pos.CENTER);

        answersBox = new VBox(TEN);
        answersBox.setAlignment(Pos.TOP_LEFT);
        answersPane = new ScrollPane(answersBox);
        answersPane.setPrefHeight(HEIGHT_PREF);

        buttonBox = new VBox(TEN);
        buttonBox.getChildren().addAll(questionLabel, answerField, startSubmitBox, instructionLabel, scoreLabel, answersPane);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);

        // Blanks out text field and submit button until you click "start"
        answerField.setDisable(true);
        submitButton.setDisable(true);
        answersPane.setVisible(false);

        // This should send you to a different stack pane if you click start quiz
        startingPane = new StackPane(mainTitleBox);
        startingPane.setAlignment(Pos.CENTER);
        startingScene = new Scene(startingPane, BOX_WIDTH, BOX_HEIGHT);

        quizPane = new StackPane(buttonBox);
        quizPane.setAlignment(Pos.CENTER);
        quizScene = new Scene(quizPane, BOX_WIDTH, BOX_HEIGHT);

        // This gives the start quiz button several actions when you click
        startQuizButton.setOnAction(startQuiz -> {primaryStage.setScene(quizScene);});

        startButton.setOnAction(startQuiz ->
        {
            answerField.setDisable(false);
            submitButton.setDisable(false);
            startButton.setDisable(true);
            answersPane.setVisible(false);
            answersBox.getChildren().clear();
            count[0] = 0;
            question[0] = askQuestion();
            questionLabel.setText("Question: " + question[ZERO]);
        });

        // Checks answer if it is correct and if it reaches 10 count, it will display your score and quiz results
        submitButton.setOnAction(event ->
        {
            handleAnswer(answersBox, question, count);

            if (count[ZERO] >= TEN)
            {
                answerField.setDisable(true);
                submitButton.setDisable(true);
                startButton.setDisable(false);
                answersPane.setVisible(true);
                questionLabel.setText("Quiz complete! Your final score is: " + score + "/10" );
            }

        });

        quizScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && count[ZERO] < TEN)
            {
                handleAnswer(answersBox, question, count);
            }
            else
            {
                answerField.setDisable(true);
                submitButton.setDisable(true);
                startButton.setDisable(false);
                answersPane.setVisible(true);
                questionLabel.setText("Quiz complete! Your final score is: " + score + "/10" );
            }
        });

        primaryStage.setScene(startingScene);
        primaryStage.setTitle("The Andrew, Brownie and Yang Quiz");
        primaryStage.show();
    }

    /**
     * Checks the user's answer, updates the score if correct, and moves to the next question.
     *
     * @param answersBox  VBox that displays past questions and answers.
     * @param question    The current question being asked.
     * @param count       Tracks the number of questions asked so far.
     */
    private void handleAnswer(VBox answersBox, String[] question, int[] count)
    {
        answersBox.getChildren().add(new Label(question[ZERO]));
        answersBox.getChildren().add(new Label("Answer: " + quizMap.get(question[ZERO]) + "\n"));

        count[ZERO]++;
        checkAnswer(answerField.getText(), question[ZERO]);
        answerField.clear();
        question[ZERO] = askQuestion();
        questionLabel.setText("Question: " + question[ZERO]);
    }

    /**
     * Randomly selects a question that hasn't been asked yet.
     *
     * @return A randomly selected quiz question.
     */
    private String askQuestion()
    {
        final String question;
        final Random random;
        int number;

        random = new Random();
        number = random.nextInt(quizData.size());
        for (final int num : askedQuestions)
        {
            if (number == num)
            {
                number = random.nextInt(quizData.size());
            }
        }
        askedQuestions.add(number);
        question = keyArray[number];
        return question;
    }

    /**
     * Compares the user's answer with the correct one and updates the score if correct.
     *
     * @param answer   The user's input.
     * @param question The current question.
     */
    private void checkAnswer(final String answer, final String question)
    {
        if (answer.equals(quizMap.get(question)))
        {
            score++;
            scoreLabel.setText("Score: " + score);
        }
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args Command-line arguments.
     */
    public static void main(final String[] args)
    {
        launch(args);
    }
}
