package view.common;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Douglas
 */
public class MessageDialog extends Stage {
 
    private static final int HEIGHT = 200;
	private static final int WIDTH = 350;
    
    private Scene scene;
    private String title;
    private String msg;
    private String buttonText;
    private Label msgLabel;
    private Label titleLabel;
    private Button button;
    private BorderPane containerBorderPane;
    private VBox containerVBox;
    private VBox containerTitleMsgVBox;
    private HBox containerHBox;
    private int height;
 
    public MessageDialog(String title, String msg, String buttonText, int height) {

        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);
        
        this.title = title;
        this.msg = msg;
        this.buttonText = buttonText;
        this.height = height;
        
        createLabels();
        createButtons();
       
        setLayout();
        
        createButtonActions();

        scene = new Scene(containerBorderPane, WIDTH, height);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);

    }
   
    public MessageDialog(String msg, String rightButonText, int height) {
    	this(null, msg, rightButonText, height);
    }
    
    public void createLabels(){
    	
    	if(title != null){
    		titleLabel = new Label(title);
    		titleLabel.setId("title-label");
        	titleLabel.setWrapText(true);
    	}
    	
        msgLabel = new Label(msg);
        msgLabel.setId("msg-label");
        msgLabel.setWrapText(true);

    }
    
    public void createButtons(){
		
		if(buttonText != null){
			button = new Button(buttonText.toUpperCase());
		}

	}
    
    
    public void setLayout(){
    	
    	containerBorderPane = new BorderPane();
        containerBorderPane.setId("container-border-pane");
        containerBorderPane.getStylesheets().add("view/common/styles/dialog.css"); 
        
        containerVBox = new VBox();
        containerVBox.setId("container-vbox");
        containerTitleMsgVBox = new VBox();
        containerTitleMsgVBox.setId( "container-title-msg-vbox");
        if(title != null){
        	containerTitleMsgVBox.getChildren().add(titleLabel);
        }
        containerTitleMsgVBox.getChildren().add(msgLabel);
        containerVBox.getChildren().add(containerTitleMsgVBox);
        
        containerHBox = new HBox();
        containerHBox.setId("container-hbox");
        if(button != null){
        	containerHBox.getChildren().add(button);
        }
 
        containerVBox.getChildren().add(containerHBox);
        
        containerBorderPane.setTop(containerVBox);

    }
    
    public void createButtonActions(){
    	
    	if(button != null){
    		button.setOnAction(new EventHandler<ActionEvent>(){
        		@Override
                public void handle(ActionEvent arg0) {
        			MessageDialog.this.close();  
        		}
        	});
    	}
    	  
   }
 
}
