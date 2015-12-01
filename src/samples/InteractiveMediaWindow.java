package samples;

import java.util.ArrayList;

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
import model.common.Media;

/**
 *
 * @author Douglas
 */
public class InteractiveMediaWindow extends Stage {
 
    private static final int HEIGHT = 200;
	private static final int WIDTH = 350;
    
    private Scene scene;
    private String title;
    private String leftButtonText;
    private String rightButtonText;
    private Label titleLabel;
    private Button leftButton;
    private Button rightButton;
    private BorderPane containerBorderPane;
    private VBox containerVBox;
    private VBox containerTitleMsgVBox;
    private HBox containerHBox;
    private int height;
 
    public InteractiveMediaWindow(Media firstSelectedMedia, ArrayList<Media> mediaListDuringInteractivityTime) {

        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.DECORATED);
        
        this.title = "Set the interactive media properties";
        this.leftButtonText = "Cancel";
        this.rightButtonText = "OK";
        this.height = height;
        
        createLabels();
        createButtons();
       
        setLayout();
        
        createButtonActions();

        scene = new Scene(containerBorderPane, WIDTH, height);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);

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
		
		if(leftButtonText != null){
			leftButton = new Button(leftButtonText.toUpperCase());
		}
	    
	    rightButton = new Button(rightButtonText.toUpperCase());
	
	}
    
    
    public void setLayout(){
    	
    	containerBorderPane = new BorderPane();
        containerBorderPane.setId("container-border-pane");
        containerBorderPane.getStylesheets().add("messageDialog/messageDialog.css"); 
        
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
        if(leftButton != null){
        	containerHBox.getChildren().add(leftButton);
        }
        containerHBox.getChildren().add(rightButton);
        
        containerVBox.getChildren().add(containerHBox);
        
        containerBorderPane.setTop(containerVBox);

    }
    
    public void createButtonActions(){
    	
    	if(leftButton != null){
    		leftButton.setOnAction(new EventHandler<ActionEvent>(){
        		@Override
                public void handle(ActionEvent arg0) {
        			MessageDialog.this.close();  
        		}
        	});
    	}

    	rightButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
    		public void handle(ActionEvent arg0) {
    			MessageDialog.this.close();  
        	}
        });
    	  
   }
 
}