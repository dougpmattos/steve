package view.temporalViewPane;

import java.io.File;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.common.Media;
import model.temporalView.enums.AlphabeticalInteractivityKey;
import model.temporalView.enums.ArrowInteractivityKey;
import model.temporalView.enums.ChannelChangeInteractivityKey;
import model.temporalView.enums.ColorInteractivityKey;
import model.temporalView.enums.ControlInteractivityKey;
import model.temporalView.enums.InteractivityKeyType;
import model.temporalView.enums.NumericInteractivityKey;
import model.temporalView.enums.ProgrammingGuideInteractivityKey;
import model.temporalView.enums.VolumeChangeInteractivityKey;
import view.common.Language;

/**
 *
 * @author Douglas
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class InteractiveMediaWindow extends Stage {
 
	private static final String TITLE = Language.translate("new.interactive.media");    
	private static final int HEIGHT = 300;
	private static final int WIDTH = 500;
    
    private Scene scene;
    private ChoiceBox<InteractivityKeyType> interactivityKeyTypeField;
    
	private ChoiceBox interactivityKeyField;
    private ChoiceBox<Media> mediaToBeStoppedField;
    private ChoiceBox firstMediaOfTheNewChainField;
    
    private ArrayList<Media> mediaListDuringInteractivityTime;
    private ArrayList<Media> repositoryMediaList;
    
    public InteractiveMediaWindow(Media firstSelectedMedia, ArrayList<Media> mediaListDuringInteractivityTime, ArrayList<Media> repositoryMediaList) {

        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);

        this.mediaListDuringInteractivityTime = mediaListDuringInteractivityTime;
        this.repositoryMediaList = repositoryMediaList;
        
        BorderPane containerBorderPane = new BorderPane();
        containerBorderPane.setId("container-border-pane");
        containerBorderPane.getStylesheets().add("view/temporalViewPane/styles/interactivityMediaWindow.css");
        
        
        containerBorderPane.setTop(createToolBar());
        containerBorderPane.setCenter(createForm());

        scene = new Scene(containerBorderPane, WIDTH, HEIGHT);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);

    }

	private BorderPane createToolBar(){
    	
    	BorderPane toolBarBorderPane = new BorderPane();
    	toolBarBorderPane.setId("tool-bar-pane");
    	
    	Button closeButton = new Button();
    	Button saveButton = new Button(Language.translate("save").toUpperCase());
    	saveButton.setId("save-button");
    	closeButton.setId("close-button");
    	createToolBarButtonActions(closeButton, saveButton);
    	
    	Label titleLabe = new Label(TITLE);
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
    	
		Label interactivityKeyTypeLabel = new Label(Language.translate("interactivity.key.type"));
		Label interactivityKeyLabel = new Label(Language.translate("interactivity.key"));
        Label mediaToBeStoppedLabel = new Label(Language.translate("media.to.be.stopped"));
        Label firstMediaOfTheNewChainLabel = new Label(Language.translate("first.media.of.the.new.timeline"));
        interactivityKeyTypeLabel.setId("msg-label");
        interactivityKeyLabel.setId("msg-label");
        mediaToBeStoppedLabel.setId("msg-label");
        firstMediaOfTheNewChainLabel.setId("msg-label");
        
        interactivityKeyTypeField = new ChoiceBox<InteractivityKeyType>(FXCollections.observableArrayList(InteractivityKeyType.values()));
        interactivityKeyField = new ChoiceBox();
        mediaToBeStoppedField = new ChoiceBox<Media>(FXCollections.observableArrayList(mediaListDuringInteractivityTime));
        firstMediaOfTheNewChainField = new ChoiceBox();
        firstMediaOfTheNewChainField.setItems(FXCollections.observableArrayList(repositoryMediaList, new Separator(), Language.translate("add.new.media") + "..."));
    
        interactivityKeyTypeField.setId("new-interactive-media-field");
        interactivityKeyField.setId("new-interactive-media-field");
        mediaToBeStoppedField.setId("new-interactive-media-field");
        firstMediaOfTheNewChainField.setId("new-interactive-media-field");
       
        HBox mediaCloseHBoxContainer = new HBox();
        mediaCloseHBoxContainer.setId("media-close-button-hbox-container");
        mediaCloseHBoxContainer.getChildren().add(mediaToBeStoppedField);
        VBox mediaCloseVBoxContainer = new VBox();
        mediaCloseVBoxContainer.setId("media-close-button-vbox-container");
        mediaCloseVBoxContainer.getChildren().add(mediaCloseHBoxContainer);
        
        GridPane formGridPane = new GridPane();
        formGridPane.setId("form-grid-pane");
        
        formGridPane.add(interactivityKeyTypeLabel, 0, 2);
        formGridPane.add(interactivityKeyTypeField, 1, 2);
        formGridPane.add(interactivityKeyLabel, 0, 3);
        formGridPane.add(interactivityKeyField, 1, 3);
        formGridPane.add(mediaToBeStoppedLabel, 0, 4);
        formGridPane.add(mediaCloseVBoxContainer, 1, 4);
        formGridPane.add(firstMediaOfTheNewChainLabel, 0, 5);
        formGridPane.add(firstMediaOfTheNewChainField, 1, 5);
        
        createActionEvents(formGridPane, mediaCloseHBoxContainer, mediaCloseVBoxContainer);
		
        return formGridPane;
    	
    }

	private void createActionEvents(GridPane formGridPane, HBox mediaCloseHBoxContainer, VBox mediaCloseVBoxContainer) {
		
		interactivityKeyTypeField.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				switch (interactivityKeyTypeField.getValue()) {
				
					case NUMERIC: 
						interactivityKeyField.setItems(FXCollections.observableArrayList(NumericInteractivityKey.values()));
						break;
					case ALPHABETICAL: 
						interactivityKeyField.setItems(FXCollections.observableArrayList(AlphabeticalInteractivityKey.values()));
						break;
					case PROGRAMMING_GUIDE: 
						interactivityKeyField.setItems(FXCollections.observableArrayList(ProgrammingGuideInteractivityKey.values()));
						break;
					case ARROWS: 
						interactivityKeyField.setItems(FXCollections.observableArrayList(ArrowInteractivityKey.values()));
						break;
					case CHANNEL_CHANGE: 
						interactivityKeyField.setItems(FXCollections.observableArrayList(ChannelChangeInteractivityKey.values()));
						break;
					case VOLUME_CHANGE: 
						interactivityKeyField.setItems(FXCollections.observableArrayList(VolumeChangeInteractivityKey.values()));
						break;
					case COLORS: 
						interactivityKeyField.setItems(FXCollections.observableArrayList(ColorInteractivityKey.values()));
						break;
					case CONTROL: 
						interactivityKeyField.setItems(FXCollections.observableArrayList(ControlInteractivityKey.values()));
						break;

				}
	
			}
			
		});
		
		mediaToBeStoppedField.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
		
				Button removeMediaButton = new Button();
				removeMediaButton.setId("media-to-be-stopped-close-button");
		    	mediaCloseHBoxContainer.getChildren().add(removeMediaButton);
		    	
		    	Button addNewButton = new Button(Language.translate("add.new"));
		    	addNewButton.setId("add-new-button");
		    	mediaCloseVBoxContainer.getChildren().add(addNewButton);
		    	
		    	createFormButtonActions(removeMediaButton, addNewButton, mediaCloseHBoxContainer, mediaCloseVBoxContainer);
		    	
			}
			
		});
		
		firstMediaOfTheNewChainField.setOnAction(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				
				//TODO chamar botat add do repo. Pqra teste colei o codigo dirto aqui
				if(firstMediaOfTheNewChainField.getValue().toString().equalsIgnoreCase("Add new media...")){
					
					FileChooser fileChooser = new FileChooser();
	                fileChooser.setTitle("Select Media");
	                File file = fileChooser.showOpenDialog(null);
	                
	                if(file != null){
	                	
                		Media media = new Media();
                		media.setFile(file);
//	                	if(!controller.addRepositoryMedia(media)){
//	                		MessageDialog messageDialog = new MessageDialog(Language.translate("media.has.already.imported") + ":          " + media.getName(), 
//	                												Language.translate("select.other.media"), "OK", 150);
//	                	    	messageDialog.showAndWait();
//	                	}
                		
                		firstMediaOfTheNewChainField.setValue(media);
	                    
	                }
					
				}

			}
			
		});
		
	}

    
    private void createToolBarButtonActions(Button closeButton, Button saveButton) {
		
    	closeButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {
    			  InteractiveMediaWindow.this.close();
    		}
    	});
    	
    	saveButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {
    			//firstSelectedMedia
    		}
    	});
		
	}
    
	private void createFormButtonActions(Button removeMediaButton, Button addNewButton, HBox mediaCloseHBoxContainer, VBox mediaCloseVBoxContainer) {
		
		removeMediaButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {
    			
    			mediaToBeStoppedField.setValue(null);
    			mediaCloseHBoxContainer.getChildren().remove(removeMediaButton);
    			mediaCloseVBoxContainer.getChildren().remove(addNewButton);
    			
    		}
    	});
		
		addNewButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {
    			
    			ChoiceBox mediaToBeStoppedField = new ChoiceBox<Media>(FXCollections.observableArrayList(mediaListDuringInteractivityTime));
    			mediaToBeStoppedField.setId("new-interactive-media-field");
    			mediaCloseVBoxContainer.getChildren().remove(addNewButton);
    			mediaCloseVBoxContainer.getChildren().add(mediaToBeStoppedField);
    		        
    		}
    	});
		
	}

}
