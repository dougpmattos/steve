package view.stevePane;

import java.util.ArrayList;

import view.common.InputDialog;
import view.common.Language;
import view.temporalViewPane.InteractiveMediaWindow;
import view.temporalViewPane.TemporalViewPane;
import controller.Controller;
import model.common.Media;
import model.temporalView.enums.InteractivityKeyType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class PreferencesWindow extends Stage {
	private static final int HEIGHT = 450;
	private static final int WIDTH = 500;
    
	private Scene scene;
    
    private GridPane formGridPane;
    
    private TextField redKeyField;
    private TextField greenKeyField;
    private TextField yellowKeyField;
    private TextField blueKeyField;      

    
    public PreferencesWindow() {

        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);
        

        BorderPane containerBorderPane = new BorderPane();
        containerBorderPane.setId("container-border-pane");
        containerBorderPane.getStylesheets().add("view/common/styles/preferencesWindow.css");
        
        formGridPane = createForm();
        ScrollPane scrollPaneContainer = new ScrollPane();
        scrollPaneContainer.setContent(formGridPane);
        scrollPaneContainer.setId("scroll-pane-container");
        
        containerBorderPane.setTop(createToolBar());
        containerBorderPane.setCenter(scrollPaneContainer);

        scene = new Scene(containerBorderPane, WIDTH, HEIGHT);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);

    }
    
	private void createToolBarButtonActions(Button closeButton, Button saveButton) {
			
	    	closeButton.setOnAction(new EventHandler<ActionEvent>(){
	    		@Override
	            public void handle(ActionEvent arg0) {
	    			
	    			Window window = getScene().getWindow();   

	    	        if (window instanceof Stage){
	    	            ((Stage) window).close();
	    	        }

	
	    		}
	    	});
	}

    
	private BorderPane createToolBar(){
	    	
    	BorderPane toolBarBorderPane = new BorderPane();
    	toolBarBorderPane.setId("tool-bar-pane");
    	
    	Button closeButton = new Button();
    	Button saveButton = new Button(Language.translate("save").toUpperCase());
    	saveButton.setId("save-button");
    	closeButton.setId("close-button");
    	createToolBarButtonActions(closeButton, saveButton);
    	
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

    
    private GridPane createForm(){
    	
                
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
                
        redKeyField.setText("0");
        greenKeyField.setText("1");
        blueKeyField.setText("2");
        yellowKeyField.setText("3");
        
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
