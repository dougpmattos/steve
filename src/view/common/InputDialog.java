package view.common;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class InputDialog extends Stage {

	private static final int HEIGHT = 200;
	private static final int WIDTH = 350;
	private static final int LEFT_BUTTON = 0;
	private static final int RIGHT_BUTTON = 1;
	private static final int CLOSE_BUTTON = 2;
    
    private Scene scene;
    private String title;
    private String msg;
    private String leftButtonText;
    private String rightButtonText;
    private String inputLabelText;
    private Label msgLabel;
    private Label titleLabel;
    private Label inputLabel;
    private TextField inputField;
    private Button leftButton;
    private Button rightButton;
    private BorderPane containerBorderPane;
    private VBox containerInpuFieldButtonTitleMsgVBox;
    private VBox containerTitleMsgVBox;
    private HBox containerButtonHBox;
    private GridPane containerInputFieldGridPane;
    private int buttonClicked;
    private int height;
 
    public InputDialog(String title, String msg, String leftButtonText, String rightButonText, String inputLabelText, int height) {

        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.DECORATED);
        
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
    
    public void createButtons(){
		
		if(leftButtonText != null){
			leftButton = new Button(leftButtonText.toUpperCase());
		}
	    
	    rightButton = new Button(rightButtonText.toUpperCase());
	
	    buttonClicked = CLOSE_BUTTON;
	    
	}
    
    public void setLayout(){
    	
    	containerBorderPane = new BorderPane();
        containerBorderPane.setId("container-border-pane");
        containerBorderPane.getStylesheets().add("view/common/styles/dialog.css"); 
        
        containerInpuFieldButtonTitleMsgVBox = new VBox();
        containerInpuFieldButtonTitleMsgVBox.setId("container-vbox");
        
        containerTitleMsgVBox = new VBox();
        containerTitleMsgVBox.setId( "container-title-msg-vbox");
        if(title != null){
        	containerTitleMsgVBox.getChildren().add(titleLabel);
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
        containerButtonHBox.getChildren().add(rightButton);
        
        
        containerInpuFieldButtonTitleMsgVBox.getChildren().add(containerTitleMsgVBox);
        if(containerInputFieldGridPane != null){
        	containerInpuFieldButtonTitleMsgVBox.getChildren().add(containerInputFieldGridPane);
        }
        containerInpuFieldButtonTitleMsgVBox.getChildren().add(containerButtonHBox);
        
        containerBorderPane.setTop(containerInpuFieldButtonTitleMsgVBox);

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

    	rightButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
    		public void handle(ActionEvent arg0) {
    			InputDialog.this.close();
    			buttonClicked = RIGHT_BUTTON;
        	}
        });
    	  
   }
    
    public String showAndWaitAndReturn(){
    	
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