package view.stevePane;


import view.common.InputDialog;
import view.common.Language;
import view.common.MessageDialog;
import view.common.ReturnMessage;
import view.utility.AnimationUtil;
import controller.Controller;
import model.common.InteractivityKeyMapping;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PreferencesWindow extends Stage {
	private static final int HEIGHT = 450;
	private static final int WIDTH = 500;
    
	private Scene scene;
    
    private GridPane formGridPane;
    
    private TextField redKeyField;
    private TextField greenKeyField;
    private TextField yellowKeyField;
    private TextField blueKeyField;     

    
    public PreferencesWindow(Controller controller) {

        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);
        

        BorderPane containerBorderPane = new BorderPane();
        containerBorderPane.setId("container-border-pane");
        containerBorderPane.getStylesheets().add("styles/common/preferencesWindow.css");
        
        formGridPane = createForm(controller);  
        ScrollPane scrollPaneContainer = new ScrollPane();
        scrollPaneContainer.setContent(formGridPane);
        scrollPaneContainer.setId("scroll-pane-container");
        
        containerBorderPane.setTop(createToolBar(controller));
        containerBorderPane.setCenter(scrollPaneContainer);

        scene = new Scene(containerBorderPane, WIDTH, HEIGHT);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);

    }
    
    private void createToolBarButtonActions(Button closeButton, Button saveButton, Controller controller) {
		
    	closeButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {
    			
    			InputDialog showContinueQuestionInputDialog;
    			    			
    			showContinueQuestionInputDialog = new InputDialog(Language.translate("discard.preference.changes"), null, Language.translate("no"), Language.translate("discard"), null, 140);
    			
    			String answer = showContinueQuestionInputDialog.showAndWaitAndReturn();
    			
    			if(answer.equalsIgnoreCase("right")){
    				PreferencesWindow.this.close();
    	    	}

    		}
    	});
    	saveButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {    
    			StringBuilder errorMessage = new StringBuilder();
    			int high = 190;
    			int count = 0;    			

    			if( //Verifica se uma tecla foi mapeada mais de uma vez
    					
    					redKeyField.getText().equals(greenKeyField.getText())||
    					redKeyField.getText().equals(blueKeyField.getText())||
    					redKeyField.getText().equals(yellowKeyField.getText())||
    					blueKeyField.getText().equals(greenKeyField.getText())||
    					blueKeyField.getText().equals(yellowKeyField.getText())||
    					greenKeyField.getText().equals(yellowKeyField.getText())
    					){
    				
    				errorMessage.append(Language.translate("it.is.not.allowed.to.have.same.key.more.than.once") +"\n \n");
    				count++;
    			}
    			
    			if(errorMessage.length() != 0){
    				
    				if(count == 1){
    					high = 190;
    				}else if(count == 2) {
    					high = 250;
    				} else if(count == 3){
    					high = 300;
    				}
    				
    				MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.save.preferences"), errorMessage.toString(), "OK", high);
    				messageDialog.showAndWait();
    				
    				return;
    				
    			}
    			    			
    			
				PreferencesWindow.this.close();
				
				ReturnMessage returnMessage = new ReturnMessage(Language.translate("preference.changes.saved.successfully"), 350);
				returnMessage.show();
				AnimationUtil.applyFadeInOut(returnMessage);
				InteractivityKeyMapping ikm = new InteractivityKeyMapping();
				ikm.setInteractivityKeyMapping(redKeyField.getText(), greenKeyField.getText(), blueKeyField.getText(), yellowKeyField.getText());
								
				controller.setInteractivityKeyMapping(ikm);
				
				controller.setPreferences(redKeyField.getText(), greenKeyField.getText(), blueKeyField.getText(), yellowKeyField.getText());
				
			}
    		
    	});
		
    }

    
	private BorderPane createToolBar(Controller controller){
	    	
    	BorderPane toolBarBorderPane = new BorderPane();
    	toolBarBorderPane.setId("tool-bar-pane");
    	
    	Button closeButton = new Button();
    	Button saveButton = new Button(Language.translate("save").toUpperCase());
    	saveButton.setId("save-button");
    	closeButton.setId("close-button");
    	createToolBarButtonActions(closeButton, saveButton, controller);
    	
    	Label titleLabe;
    	    	
    	titleLabe = new Label(Language.translate("preferences"));
    	titleLabe.setId("title-label");
    	titleLabe.setWrapText(true);

    	HBox titleHBox = new HBox();
    	titleHBox.setId("title-hbox");
    	titleHBox.getChildren().add(titleLabe);
    	
    	toolBarBorderPane.setLeft(closeButton);
    	toolBarBorderPane.setCenter(titleHBox);
    	toolBarBorderPane.setRight(saveButton);
    	
    	return toolBarBorderPane;
    	
    }

    
    private GridPane createForm(Controller controller){
    	              
        Label interactivityKeySubtitle = new Label(Language.translate("interactivity.key.mapping").toUpperCase());
        
        Label remoteControlColumnLabel = new Label(Language.translate("remote.control").toUpperCase());
        Label keyboardColumnLabel = new Label(Language.translate("keyboard").toUpperCase());
        
        Label interactivityKeyRedLabel = new Label(Language.translate("interactivity.key.red"));
        Label interactivityKeyGreenLabel = new Label(Language.translate("interactivity.key.green"));
        Label interactivityKeyBlueLabel = new Label(Language.translate("interactivity.key.blue"));
        Label interactivityKeyYellowLabel = new Label(Language.translate("interactivity.key.yellow"));
		
        interactivityKeySubtitle.setId("subtitle-label");
        
        remoteControlColumnLabel.setId("column-label");
        keyboardColumnLabel.setId("column-label");
        
        interactivityKeyRedLabel.setId("msg-label");
        interactivityKeyGreenLabel.setId("msg-label");
        interactivityKeyBlueLabel.setId("msg-label");
        interactivityKeyYellowLabel.setId("msg-label");
        
        redKeyField = new TextField();
        greenKeyField = new TextField();
        blueKeyField = new TextField();
        yellowKeyField = new TextField();
                
        redKeyField.setMinWidth(70);
        greenKeyField.setMinWidth(70);
        yellowKeyField.setMinWidth(70);
        blueKeyField.setMinWidth(70);
        
        redKeyField.setText(controller.getInteractivityKeyMapping().getInteractivityKeyMapping("red"));
        greenKeyField.setText(controller.getInteractivityKeyMapping().getInteractivityKeyMapping("green"));
        blueKeyField.setText(controller.getInteractivityKeyMapping().getInteractivityKeyMapping("blue"));
        yellowKeyField.setText(controller.getInteractivityKeyMapping().getInteractivityKeyMapping("yellow"));
        
        redKeyField.setId("red-key-field");
        greenKeyField.setId("green-key-field");
        blueKeyField.setId("blue-key-field");
        yellowKeyField.setId("yellow-key-field");
        
        addTextLimiter(redKeyField, 1);
        addTextLimiter(greenKeyField, 1);
        addTextLimiter(blueKeyField, 1);
        addTextLimiter(yellowKeyField, 1);
        
        HBox interactivityKeySeparator = new HBox();
        interactivityKeySeparator.setId("separator");
        VBox interactivityKeySubtitleSeparatorContainer = new VBox();
        interactivityKeySubtitleSeparatorContainer.setId("subtitle-separator-container");
        interactivityKeySubtitleSeparatorContainer.getChildren().add(interactivityKeySubtitle);
        interactivityKeySubtitleSeparatorContainer.getChildren().add(interactivityKeySeparator);
        
        GridPane formGridPane = new GridPane();

        formGridPane.setId("form-grid-pane");
        
        
        formGridPane.add(interactivityKeySubtitleSeparatorContainer, 0, 1, 5, 1);
        formGridPane.add(remoteControlColumnLabel, 0, 2);
        formGridPane.add(keyboardColumnLabel, 1, 2);
        formGridPane.add(interactivityKeyRedLabel, 0, 3);
        formGridPane.add(redKeyField, 1, 3);
        formGridPane.add(interactivityKeyGreenLabel, 0, 4);
        formGridPane.add(greenKeyField, 1, 4);
        formGridPane.add(interactivityKeyBlueLabel, 0, 5);
        formGridPane.add(blueKeyField, 1, 5);
        formGridPane.add(interactivityKeyYellowLabel, 0, 6);
        formGridPane.add(yellowKeyField, 1, 6);
        
        return formGridPane;
    	
    }
    
    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }

}
