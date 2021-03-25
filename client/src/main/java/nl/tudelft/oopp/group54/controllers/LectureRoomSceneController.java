package nl.tudelft.oopp.group54.controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import nl.tudelft.oopp.group54.Datastore;
import nl.tudelft.oopp.group54.communication.ServerCommunication;
import nl.tudelft.oopp.group54.models.QuestionModel;
import nl.tudelft.oopp.group54.models.responseentities.EndLectureResponse;
import nl.tudelft.oopp.group54.models.responseentities.GetAllQuestionsResponse;
import nl.tudelft.oopp.group54.models.responseentities.GetLectureMetadataResponse;
import nl.tudelft.oopp.group54.models.responseentities.PostQuestionResponse;
import nl.tudelft.oopp.group54.views.ApplicationScene;
import nl.tudelft.oopp.group54.views.MainView;
import nl.tudelft.oopp.group54.widgets.QuestionView;


public class LectureRoomSceneController extends AbstractApplicationController {

    //  @FXML
    //  ScrollPane questionScroll

    @FXML
    ListView<QuestionView> answeredQuestionView;
    
    @FXML
    ListView<QuestionView> unansweredQuestionView;

    @FXML
    TextField questionField;

    @FXML
    Button askButton;

    @FXML
    Button lecturerModeButton;

    @FXML
    Button endLectureButton;

    @FXML
    Button feedbackPanelButton;

    @FXML
    Accordion feedbackMenu;
    @FXML
    ColumnConstraints feedbackMenuContainer;
    Integer feedbackMenuContainerUnfoldedWidth = 140;

    Datastore ds = Datastore.getInstance();

    private Boolean ended = false;
    private Boolean inLecturerMode = false;


    @Override
    public void performControllerSpecificSetup() {
        answeredQuestionView.setItems(ds.getCurrentAnsweredQuestionViews());
        unansweredQuestionView.setItems(ds.getCurrentUnansweredQuestionViews());

        // lecturer
        if (this.ds.getPrivilegeId().equals(1)) {
            //TODO: GUI elements for the lecturer
        }

        // moderator
        if (this.ds.getPrivilegeId().equals(2)) {
            //TODO: GUI elements for the moderator
            this.endLectureButton.setVisible(false);
            this.lecturerModeButton.setVisible(false);
        }

        // student
        if (this.ds.getPrivilegeId().equals(3)) {
            //TODO: GUI elements for the student
            this.endLectureButton.setVisible(false);
            this.lecturerModeButton.setVisible(false);
        }

        updateOnQuestions(false);
        updateOnMetadata();

        questionField.setOnKeyPressed(event -> {
            keyPressed(event);
        });
    }

    public void askButtonClicked() {
        postQuestion();
        this.refreshButtonClickedAfter();
    }

    /**
     * Lecturer mode button clicked.
     */
    public void lecturerModeButtonClicked() {
        if (inLecturerMode) {
            exitLecturerMode();
        } else {
            enterLecturerMode();
        }
    }

    private void enterLecturerMode() {
        inLecturerMode = true;
        this.lecturerModeButton.setStyle("-fx-background-color: linear-gradient(#cccccc, #aaaaaa);");
        for (QuestionView q : unansweredQuestionView.getItems()) {
            q.toggleLecturerMode(true);
        }
    }

    private void exitLecturerMode() {
        inLecturerMode = false;
        this.lecturerModeButton.setStyle("");
        for (QuestionView q : unansweredQuestionView.getItems()) {
            q.toggleLecturerMode(false);
        }
    }

    public void endLectureButtonClicked() {
        endLecture();
    }

    /**
     * Key pressed.
     *
     * @param event the event
     */
    public void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            postQuestion();
        }
    }

    private void postQuestion() {
        String questionText = questionField.getCharacters().toString();
        PostQuestionResponse response = null;
        try {
            response = ServerCommunication.postQuestion(questionText);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (response.getSuccess()) {
            questionField.clear();
            this.refreshButtonClicked();
        }
    }


    public void refreshButtonClicked() {
        updateOnQuestions(true);
        updateOnMetadata();
    }

    public void refreshButtonClickedAfter() {
        updateOnQuestions(false);
        updateOnMetadata();
    }

    /**
     * Update on questions.
     *
     * @param statusDisplay the status display
     */
    public void updateOnQuestions(Boolean statusDisplay) {
        GetAllQuestionsResponse response = null;

        try {
            response = ServerCommunication.getAllQuestions();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (response.getSuccess()) {
            if (statusDisplay) {
                this.displayStatusMessage("Refreshed succesfully.");
            }
            // The questions are already sorted by time so only sorting by score is required.
            List<QuestionModel> sorted = response.getUnanswered();
            Collections.sort(sorted, new Comparator<QuestionModel>() {
                @Override
                public int compare(QuestionModel o1, QuestionModel o2) {
                    return Integer.compare(o2.getScore(), o1.getScore());
                }
            });
            this.ds.setCurrentUnansweredQuestionViews(null);
            this.ds.setCurrentAnsweredQuestionViews(null);
            for (QuestionModel question : response.getAnswered()) {
                this.ds.addAnsweredQuestion(question, this);
            }
            for (QuestionModel question : sorted) {
                this.ds.addUnansweredQuestion(question, this);
            }
        }
    }

    /**
     * Update on metadata.
     */
    public void updateOnMetadata() {
        GetLectureMetadataResponse metadataResponse = null;

        try {
            metadataResponse = ServerCommunication.getLectureMetadata();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if (metadataResponse != null) {
            if (metadataResponse.getSuccess()) {
                if (!metadataResponse.getLectureOngoing()) {
                    //TODO: stuff when lecture has ended change to main menu view
                    endLectureGui();
                    //TODO: update status bar to ended
                    if (!ended) {
                        ended = true;
                        this.displayStatusMessage("The Lecture has been ended.");
                    }
                    if (ds.getPrivilegeId().equals(3)) {
                        MainView.changeSceneClearHistory(ApplicationScene.MAINVIEW, false, true);
                    }
                }
            }
        }

    }

    /**
     * End lecture.
     */
    public void endLecture() {
        EndLectureResponse response = null;

        try {
            response = ServerCommunication.endLecture();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if (response == null) {
            this.displayStatusMessage("Could not end the lecture!");
            return;
        }

        this.displayStatusMessage(response.getMessage());

        if (response.getSuccess()) {
            endLectureGui();
        }
    }

    /**
     * Toggle feedback panel visibility.
     */
    public void toggleFeedbackPanelVisibility() {
        boolean vis = !this.feedbackMenu.isVisible();
        this.feedbackMenu.setVisible(vis);

        if (vis) {
            this.feedbackMenuContainer.setPrefWidth(feedbackMenuContainerUnfoldedWidth);
        } else {
            this.feedbackMenuContainer.setPrefWidth(0);
        }
    }

    /**
     * sets GUI element to the state of lecture has finished.
     */
    public void endLectureGui() {
        this.questionField.setEditable(false);
        this.questionField.setDisable(true);
        this.askButton.setDisable(true);
        this.endLectureButton.setVisible(false);
    }


    public Datastore getDs() {
        return ds;
    }
    
    public Boolean isInLecturerMode() {
        return inLecturerMode;
    }
    
}
