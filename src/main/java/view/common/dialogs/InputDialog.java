package view.common.dialogs;

import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import view.utility.StageUtil;

public class InputDialog extends Stage {

    private static final int HEIGHT = 200;
    private static final int WIDTH = 450;
    private static final int LEFT_BUTTON = 0;
    private static final int RIGHT_BUTTON = 1;
    private static final int CLOSE_BUTTON = 2;

    private Scene scene;
    private String title;
    private String msg;
    private String leftButtonText;
    private String rightButtonText;
    private String inputLabelText;
    private String checkboxText;
    private Label msgLabel;
    private Label titleLabel;
    private Label inputLabel;
    private TextField inputField;
    private CheckBox checkbox;
    private Button leftButton;
    private Button rightButton;
    private BorderPane containerBorderPane;
    private VBox containerInputFieldButtonTitleMsgVBox;
    private VBox containerTitleMsgVBox;
    private HBox containerButtonHBox;
    private HBox containerTitleProgIndicatorHBox;
    private GridPane containerInputFieldGridPane;
    private int buttonClicked;
    private int height;
    private int width;

    public InputDialog(String title, String msg, String leftButtonText, String rightButonText, String inputLabelText, String checkboxText, int height, int width) {

        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);

        this.title = title;
        this.msg = msg;
        this.inputLabelText = inputLabelText;
        this.checkboxText = checkboxText;
        this.leftButtonText = leftButtonText;
        this.rightButtonText = rightButonText;
        this.height = height;

        createLabels();
        if(inputLabelText != null){
            createInputField();
        }
        if(checkboxText != null){
            createCheckbox();
        }
        createButtons();

        setLayout();

        createButtonActions();

        if(width==0){
            width = WIDTH;
        }
        if(height==0){
            height = HEIGHT;
        }

        scene = new Scene(containerBorderPane, width, height);

        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
        StageUtil.centerStage(this, width, height);

    }

    public InputDialog(String title, String msg, String leftButtonText, String rightButonText, String inputLabelText, int height) {

        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);

        this.title = title;
        this.msg = msg;
        this.inputLabelText = inputLabelText;
        this.leftButtonText = leftButtonText;
        this.rightButtonText = rightButonText;
        this.height = height;

        createLabels();
        if(inputLabelText != null){
            createInputField();
        }
        createButtons();

        setLayout();

        createButtonActions();

        scene = new Scene(containerBorderPane, WIDTH, height);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);

    }

    public void createLabels(){

        titleLabel = new Label(title);
        titleLabel.setId("title-label");
        titleLabel.setWrapText(true);

        if(msg != null){
            msgLabel = new Label(msg);
            msgLabel.setId("msg-label");
            msgLabel.setWrapText(true);
        }

        if(inputLabelText != null){
            inputLabel = new Label(inputLabelText);
            inputLabel.setId("input-text-label");
            inputLabel.setWrapText(true);
        }

    }

    public void createInputField(){

        inputField = new TextField();
        inputField.setId("input-field");

    }

    public void createCheckbox(){

        checkbox = new CheckBox();
        checkbox.setText(checkboxText);

    }

    public void createButtons(){

        if(leftButtonText != null){
            leftButton = new Button(leftButtonText.toUpperCase());
            leftButton.setId("left-button");
        }

        if(rightButtonText != null){
            rightButton = new Button(rightButtonText.toUpperCase());
            rightButton.setId("right-button");
        }

        buttonClicked = CLOSE_BUTTON;

    }

    public Button getRightButton() {
        return rightButton;
    }

    public void setProgressIndicator(){

        ProgressIndicator  progressIndicator = new ProgressIndicator(-1.0);
        progressIndicator.setScaleX(0.5);
        progressIndicator.setScaleY(0.5);
        containerTitleProgIndicatorHBox.getChildren().add(progressIndicator);

    }

    public void setLayout(){

        containerBorderPane = new BorderPane();
        containerBorderPane.setId("container-border-pane");
        containerBorderPane.getStylesheets().add("styles/common/dialog.css");

        containerInputFieldButtonTitleMsgVBox = new VBox();
        containerInputFieldButtonTitleMsgVBox.setId("container-vbox");

        containerTitleMsgVBox = new VBox();
        containerTitleMsgVBox.setId( "container-title-msg-vbox");

        containerTitleProgIndicatorHBox = new HBox();
        containerTitleProgIndicatorHBox.setSpacing(50);
        containerTitleProgIndicatorHBox.getChildren().add(titleLabel);

        if(title != null){
            containerTitleMsgVBox.getChildren().add(containerTitleProgIndicatorHBox);
        }

        if(msg != null){
            containerTitleMsgVBox.getChildren().add(msgLabel);
        }

        if(inputField != null){
            containerInputFieldGridPane = new GridPane();
            containerInputFieldGridPane.setId("input-field-grid-pane");
            containerInputFieldGridPane.add(inputLabel, 0, 0);
            containerInputFieldGridPane.add(inputField, 1, 0);
        }

        containerButtonHBox = new HBox();
        containerButtonHBox.setId("container-hbox");
        if(leftButton != null){
            containerButtonHBox.getChildren().add(leftButton);
        }
        if(rightButton != null){
            containerButtonHBox.getChildren().add(rightButton);
        }

        containerInputFieldButtonTitleMsgVBox.getChildren().add(containerTitleMsgVBox);
        if(containerInputFieldGridPane != null){
            containerInputFieldButtonTitleMsgVBox.getChildren().add(containerInputFieldGridPane);
        }
        if(checkbox != null){
            containerInputFieldButtonTitleMsgVBox.getChildren().add(checkbox);
        }
        containerInputFieldButtonTitleMsgVBox.getChildren().add(containerButtonHBox);

        containerBorderPane.setTop(containerInputFieldButtonTitleMsgVBox);

    }

    public void createButtonActions(){

        if(leftButton != null){
            leftButton.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent arg0) {
                    InputDialog.this.close();
                    buttonClicked = LEFT_BUTTON;
                }
            });
        }

        if(rightButton != null){
            rightButton.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent arg0) {
                    InputDialog.this.close();
                    buttonClicked = RIGHT_BUTTON;
                }
            });
        }

    }

    public CheckBox getCheckbox() {
        return checkbox;
    }

    public String showAndWaitAndReturn(){

        containerBorderPane.requestFocus();

        this.showAndWait();

        switch(buttonClicked){

            case LEFT_BUTTON:
                return "left";

            case RIGHT_BUTTON:
                if(inputField != null && !inputField.getText().isEmpty()){
                    return inputField.getText();
                } else{
                    return "right";
                }

            case CLOSE_BUTTON:
                return "close";

        }

        return null;

    }

}