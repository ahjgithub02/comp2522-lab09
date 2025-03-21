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
 * TODO: Add hashmap, finish askQuestion(), finish searchQuizText(), finish checkAnswer(), make javadoc
 */

public class QuizApp extends Application
{
    //    private final Map<String, String> quizData;
    private String currentQuestion;
    private String currentAnswer;
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
    private final int QUESTION_INDEX = 0;
    private final int ANSWER_INDEX = 1;
    private final String[] keyArray;
    private final List<Integer> askedQuestions;
    private int score = 0;
    private final Label scoreLabel;

    public QuizApp() throws IOException
    {
        answerField = new TextField();
        answerField.setPromptText("Enter your Answer here");
        answerField.setMaxWidth(300);

        submitButton = new Button("Submit");
        startButton = new Button("Start");
        startQuizButton = new Button("Start Quiz");
        instructionLabel = new Label("Press Start to Begin");

        questionLabel = new Label("Question:");
        questionLabel.setWrapText(true);
        questionLabel.setMaxWidth(300);

        welcomeLabel = new Label("Welcome to Andrew, Brownie and Yang Quiz!\n" + "Please press \"Start Quiz\" to begin!");
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
        keyArray = keys.toArray(new String[0]);
        askedQuestions = new ArrayList<>();

    }

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
        int[] count = {0};

        mainTitleBox = new VBox(10);
        mainTitleBox.getChildren().addAll(welcomeLabel, startQuizButton);
        mainTitleBox.setAlignment(Pos.CENTER);

        // THIS SHOULD MAKE THE BUTTONS :D
        startSubmitBox = new HBox(10);
        startSubmitBox.getChildren().addAll(submitButton, startButton);
        startSubmitBox.setAlignment(Pos.CENTER);

        answersBox = new VBox(10);
        answersBox.setAlignment(Pos.TOP_LEFT);
        answersPane = new ScrollPane(answersBox);
        answersPane.setPrefHeight(150);

        buttonBox = new VBox(10);
        buttonBox.getChildren().addAll(questionLabel, answerField, startSubmitBox, instructionLabel, scoreLabel, answersPane);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);

        // Blanks out textfield and submit button until you click "start"
        answerField.setDisable(true);
        submitButton.setDisable(true);
        answersPane.setVisible(false);

        // This should send you to a different stack pane if you click start quiz
        startingPane = new StackPane(mainTitleBox);
        startingPane.setAlignment(Pos.CENTER);
        startingScene = new Scene(startingPane, 400, 400);

        quizPane = new StackPane(buttonBox);
        quizPane.setAlignment(Pos.CENTER);
        quizScene = new Scene(quizPane, 400, 400);

        // This gives the start quiz button several actions when you click
        startQuizButton.setOnAction(startQuiz -> {primaryStage.setScene(quizScene);});

        startButton.setOnAction(startQuiz -> {
            answerField.setDisable(false);
            submitButton.setDisable(false);
            startButton.setDisable(true);
            answersPane.setVisible(false);
            answersBox.getChildren().clear();
            count[0] = 0;
            question[0] = askQuestion();
            questionLabel.setText("Question: " + question[0]);
        });

        // Checks answer
        submitButton.setOnAction(event -> {
            handleAnswer(answersBox, question, count);

            if (count[0] >= 10) {
                answerField.setDisable(true);
                submitButton.setDisable(true);
                startButton.setDisable(false);
                answersPane.setVisible(true);
                questionLabel.setText("Quiz complete! Your final score is: " + score + "/10" );
            }

        });

        quizScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && count[0] < 10) {
                handleAnswer(answersBox, question, count);
            } else {
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

    private void handleAnswer(VBox answersBox, String[] question, int[] count)
    {
        answersBox.getChildren().add(new Label(question[0]));
        answersBox.getChildren().add(new Label("Answer: " + quizMap.get(question[0]) + "\n"));

        count[0]++;
        checkAnswer(answerField.getText(), question[0]);
        answerField.clear();
        question[0] = askQuestion();
        questionLabel.setText("Question: " + question[0]);
    }

    private String askQuestion()
    {
        final String question;
        final Random random;
        int number;

        random = new Random();
        number = random.nextInt(quizData.size());
        for (final int num : askedQuestions) {
            if (number == num) {
                number = random.nextInt(quizData.size());
            }
        }
        askedQuestions.add(number);
        question = keyArray[number];
        return question;
    }

    private void checkAnswer(final String answer, final String question)
    {
        if (answer.equals(quizMap.get(question))) {
            score++;
            scoreLabel.setText("Score: " + score);
        }
    }

    public static void main(final String[] args)
    {
        launch(args);
    }
}
