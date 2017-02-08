package view.stevePane;

import java.util.ArrayList;

import view.common.InputDialog;
import view.common.Language;
import view.temporalViewPane.InteractiveMediaWindow;
import view.temporalViewPane.TemporalViewPane;
import controller.Controller;
import model.common.Media;
import model.temporalView.enums.InteractivityKeyType;
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
        
    	
		
		
        //Fill stage with content
		//StackPane root = new StackPane();
        
        //Scene scene = new Scene(rootGroup, 250, 320); //width x height
        //scene.getStylesheets().add("view/common/styles/preferencesWindow.css");
		//stage.setTitle(Language.translate("preferences"));
		//root.setId("pane");
		//rootGroup.setId("pane");        
       
        //final Text title = new Text(25, 25, "Controle de teclas");
        //title.setFill(Color.BLACK);
        //title.setId("pane");
        //rootGroup.getChildren().add(title);
                
        
        /*
        final Text larr = new Text(25,50,"←");
        larr.setFill(Color.BLACK);
        rootGroup.getChildren().add(larr);
        
        final Text input_larr = new Text(100,50,"◄");
        input_larr.setFill(Color.BLACK);
        rootGroup.getChildren().add(input_larr);
        
        final Text rarr = new Text(25, 75, "→");
        larr.setFill(Color.BLACK);
        rootGroup.getChildren().add(rarr);
        
        final Text input_rarr = new Text(100,75,"►");
        input_rarr.setFill(Color.BLACK);
        rootGroup.getChildren().add(input_rarr);
        
        final Text darr = new Text(25, 100, "↓");
        darr.setFill(Color.BLACK);
        rootGroup.getChildren().add(darr);
        
        final Text input_darr = new Text(100, 100, "▼");
        input_darr.setFill(Color.BLACK);
        rootGroup.getChildren().add(input_darr);
        
        final Text uarr = new Text(25, 125, "↑");
        uarr.setFill(Color.BLACK);
        rootGroup.getChildren().add(uarr);
        
        final Text input_uarr = new Text(100, 125, "▲");
        input_uarr.setFill(Color.BLACK);
        rootGroup.getChildren().add(input_uarr);
        
        final Text enter = new Text(25, 150, "ENTER");
        enter.setFill(Color.BLACK);
        rootGroup.getChildren().add(enter);
        
        final Text input_enter = new Text(100, 150, "ENTER");
        input_enter.setFill(Color.BLACK);
        rootGroup.getChildren().add(input_enter);//
        
        final Text red = new Text(25, 175, "RED");
        red.setFill(Color.RED);
        rootGroup.getChildren().add(red);
        
        final Text input_red = new Text(100, 175, "0");
        input_red.setFill(Color.RED);
        rootGroup.getChildren().add(input_red);
        
        final Text green = new Text(25, 200, "GREEN");
        green.setFill(Color.GREEN);
        rootGroup.getChildren().add(green);
        
        final Text input_green = new Text(100, 200, "1");
        input_green.setFill(Color.GREEN);
        rootGroup.getChildren().add(input_green);
        
        final Text blue = new Text(25, 225, "BLUE");
        blue.setFill(Color.BLUE);
        rootGroup.getChildren().add(blue);
        
        final Text input_blue = new Text(100, 225, "2");
        input_blue.setFill(Color.BLUE);
        rootGroup.getChildren().add(input_blue);
        
        final Text yellow = new Text(25, 250, "YELLOW");
        yellow.setFill(Color.YELLOW);
        rootGroup.getChildren().add(yellow);
        
        final Text input_yellow = new Text(100, 250, "3");
        input_yellow.setFill(Color.YELLOW);
        rootGroup.getChildren().add(input_yellow);
        
        */
        //formGridPane.add(rootGroup, 0, 1);
        //text1.setFont(Font.font(java.awt.Font.SERIF, 18));

        
        return formGridPane;

    	
    }
}
