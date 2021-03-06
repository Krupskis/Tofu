package nl.tudelft.oopp.group54.controllers;

import com.sun.tools.javac.Main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import nl.tudelft.oopp.group54.communication.ServerCommunication;
import nl.tudelft.oopp.group54.controllers.AbstractApplicationController;
import nl.tudelft.oopp.group54.views.ApplicationScene;
import nl.tudelft.oopp.group54.views.MainView;

public class MainSceneController extends AbstractApplicationController {

    @FXML
    private Button createButton;

    @FXML
    private Button joinButton;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button settingsButton;

    public void settingsButtonClicked() {
        MainView.changeScene(ApplicationScene.SETTINGSVIEW, true);
    }

    public void createButtonClicked() {
        MainView.changeScene(ApplicationScene.DATETIME, true);
    }
    
    public void joinButtonClicked() {
        MainView.changeScene(ApplicationScene.JOINLECTURE, true);
    }

    @Override
    public void performControllerSpecificSetup() {
        System.out.println("Main controller config");
        MainView.clearHistory();
    }

    /**
     * display the message that lecture has been ended when students gets kicked out of the room.
     */
    @Override
    public void switchToMainFromLecture() {
        super.switchToMainFromLecture();
    }
}
