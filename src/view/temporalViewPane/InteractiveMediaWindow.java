package view.temporalViewPane;

import java.util.ArrayList;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.common.Media;
import model.temporalView.Interactivity;
import model.temporalView.TemporalChain;
import model.temporalView.enums.ActuationInteractivityKey;
import model.temporalView.enums.AlphabeticalInteractivityKey;
import model.temporalView.enums.ArrowInteractivityKey;
import model.temporalView.enums.ChannelChangeInteractivityKey;
import model.temporalView.enums.ColorInteractivityKey;
import model.temporalView.enums.ControlInteractivityKey;
import model.temporalView.enums.InteractivityKeyType;
import model.temporalView.enums.NumericInteractivityKey;
import model.temporalView.enums.ProgrammingGuideInteractivityKey;
import model.temporalView.enums.VolumeChangeInteractivityKey;
import view.common.InputDialog;
import view.common.Language;
import view.common.MessageDialog;
import controller.Controller;

/**
 *
 * @author Douglas
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class InteractiveMediaWindow extends Stage {
 
	private static final String TITLE = Language.translate("new.interactive.media");    
	private static final int HEIGHT = 350;
	private static final int WIDTH = 500;
    
	private Controller controller;
	private TemporalViewPane temporalViewPane;
	
    private Scene scene;
    
    private ChoiceBox<InteractivityKeyType> interactivityKeyTypeField;
	private ChoiceBox interactivityKeyField;
    private ChoiceBox<Media> mediaToBeStoppedField;
    private ChoiceBox timelineToBeStartedField;
    
    private GridPane formGridPane;
    private VBox mediaCloseNewVBoxContainer;

	private ArrayList<Media> mediaListDuringInteractivityTime;
    private ArrayList<Media> repositoryMediaList;
    
    private int mediaToBeStoppedCount = 0;
    
    public InteractiveMediaWindow(Controller controller, TemporalViewPane temporalViewPane, Media firstSelectedMedia, ArrayList<Media> mediaListDuringInteractivityTime, ArrayList<Media> repositoryMediaList) {

        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);

        this.temporalViewPane = temporalViewPane;
        this.controller = controller;
        
        this.mediaListDuringInteractivityTime = mediaListDuringInteractivityTime;
        this.repositoryMediaList = repositoryMediaList;

        BorderPane containerBorderPane = new BorderPane();
        containerBorderPane.setId("container-border-pane");
        containerBorderPane.getStylesheets().add("view/temporalViewPane/styles/interactivityMediaWindow.css");
        
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
        Label timelineToBeStartedLabel = new Label(Language.translate("timeline.to.be.started"));
        interactivityKeyTypeLabel.setId("msg-label");
        interactivityKeyLabel.setId("msg-label");
        mediaToBeStoppedLabel.setId("msg-label");
        timelineToBeStartedLabel.setId("msg-label");
        
        interactivityKeyTypeField = new ChoiceBox<InteractivityKeyType>(FXCollections.observableArrayList(InteractivityKeyType.values()));
        interactivityKeyField = new ChoiceBox();
        mediaToBeStoppedField = new ChoiceBox<Media>(FXCollections.observableArrayList(mediaListDuringInteractivityTime));
        
        ObservableList timelineFieldOptions = FXCollections.observableArrayList(temporalViewPane.getTemporalViewModel().getTemporalChainList());
        timelineFieldOptions.add(new Separator());
        timelineFieldOptions.add(Language.translate("add.new.timeline") + "...");
        timelineToBeStartedField = new ChoiceBox(timelineFieldOptions);
        
        interactivityKeyTypeField.setValue(InteractivityKeyType.ACTUATION);
        interactivityKeyField.setItems(FXCollections.observableArrayList(ActuationInteractivityKey.values()));
        interactivityKeyField.setValue(ActuationInteractivityKey.OK);
        
        interactivityKeyTypeField.setId("new-interactive-media-field");
        interactivityKeyField.setId("new-interactive-media-field");
        mediaToBeStoppedField.setId("new-interactive-media-field");
        mediaToBeStoppedField.getProperties().put("mediaToBeStoppedCount", mediaToBeStoppedCount); 
        timelineToBeStartedField.setId("new-interactive-media-field");
       
        HBox mediaCloseHBoxContainer = new HBox();
        mediaCloseHBoxContainer.setId("media-close-button-hbox-container");
        mediaCloseHBoxContainer.getChildren().add(mediaToBeStoppedField);
        
        mediaCloseNewVBoxContainer = new VBox();
        mediaCloseNewVBoxContainer.setId("media-close-button-vbox-container");
        mediaCloseNewVBoxContainer.getChildren().add(mediaCloseHBoxContainer);
        
        GridPane formGridPane = new GridPane();
        formGridPane.setId("form-grid-pane");
        formGridPane.setValignment(mediaToBeStoppedLabel, VPos.TOP);
        
        formGridPane.add(interactivityKeyTypeLabel, 0, 2);
        formGridPane.add(interactivityKeyTypeField, 1, 2);
        formGridPane.add(interactivityKeyLabel, 0, 3);
        formGridPane.add(interactivityKeyField, 1, 3);
        formGridPane.add(mediaToBeStoppedLabel, 0, 4);
        formGridPane.add(mediaCloseNewVBoxContainer, 1, 4);
        formGridPane.add(timelineToBeStartedLabel, 0, 5);
        formGridPane.add(timelineToBeStartedField, 1, 5);
        
        createActionEvents(formGridPane, mediaCloseHBoxContainer, mediaCloseNewVBoxContainer);
		
        return formGridPane;
    	
    }

	private void createActionEvents(GridPane formGridPane, HBox mediaCloseHBoxContainer, VBox mediaCloseNewVBoxContainer) {
		
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
					case ACTUATION: 
						interactivityKeyField.setItems(FXCollections.observableArrayList(ActuationInteractivityKey.values()));
						break;

				}
	
			}
			
		});
		
		mediaToBeStoppedField.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
		
				if(mediaToBeStoppedField.getProperties().get("previousValue") == null){
					
					Button removeMediaButton = new Button();
					removeMediaButton.setId("media-to-be-stopped-close-button");
			    	mediaCloseHBoxContainer.getChildren().add(removeMediaButton);
			    	
			    	Button addNewButton = new Button(Language.translate("add.new"));
			    	addNewButton.setId("add-new-button");
			    	mediaCloseNewVBoxContainer.getChildren().add(addNewButton);
			    	
			    	createFormButtonActions(removeMediaButton, addNewButton, mediaCloseHBoxContainer, mediaCloseNewVBoxContainer);
			    	
				}
				
				mediaToBeStoppedField.getProperties().put("previousValue", mediaToBeStoppedField.getValue());
		    	
			}
			
		});
		
		timelineToBeStartedField.setOnAction(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				
				if(timelineToBeStartedField.getValue() != null && timelineToBeStartedField.getValue().toString().equalsIgnoreCase("Add new timeline...")){
					
					timelineToBeStartedField.setValue(null);
					
					InputDialog newTimelineNameDialog = new InputDialog("New timeline", null, "CANCEL", "OK", "Name", 180);
					String name = newTimelineNameDialog.showAndWaitAndReturn();
					
					if(name != null && !name.equalsIgnoreCase("left") && !name.equalsIgnoreCase("close")){
						
						TemporalChain temporalChain = new TemporalChain(name);
						timelineToBeStartedField.getItems().add(0, temporalChain);
						timelineToBeStartedField.setValue(temporalChain);
						
			    	}

	            }
					
			}
			
		});
		
	}

    
    private void createToolBarButtonActions(Button closeButton, Button saveButton) {
		
    	closeButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {
    		
    			InputDialog showContinueQuestionInputDialog = new InputDialog(Language.translate("discard.new.interactive.media"), null, "no","discard", null, 140);
    			
    			String answer = showContinueQuestionInputDialog.showAndWaitAndReturn();
    			
    			if(answer.equalsIgnoreCase("right")){
    				InteractiveMediaWindow.this.close();
    	    	}

    		}
    	});
    	
    	saveButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {
	
    			if(mediaToBeStoppedField.getValue() == null && timelineToBeStartedField.getValue() == null){
    				
    				MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.new.interactive.media"), 
							Language.translate("select.at.least.one.media.to.be.stopped.or.a.timeline.to.be.started"), "OK", 190);
					messageDialog.showAndWait();
					
					return;
					
    			}
    			
				Tab selectedTab = null;
		    	TemporalChainPane temporalChainPane = null;
		    	
		    	for (Tab tab : temporalViewPane.getTemporalChainTabPane().getTabs()){
		    		if(tab.isSelected()){
		    			selectedTab = tab;
		    			break;
		    		}
		    	}
		    	
		    	if(selectedTab != null){
		    		temporalChainPane = (TemporalChainPane) selectedTab.getContent();
		    	}
	
		    	Interactivity interactivityRelation;
		    	
		    	switch (interactivityKeyTypeField.getValue()) {
				
					case NUMERIC:
						
						interactivityRelation = new Interactivity<Media, NumericInteractivityKey>();
						interactivityRelation.setInteractivityKey((NumericInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case ALPHABETICAL:
						
						interactivityRelation = new Interactivity<Media, AlphabeticalInteractivityKey>();
						interactivityRelation.setInteractivityKey((AlphabeticalInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case PROGRAMMING_GUIDE: 
						
						interactivityRelation = new Interactivity<Media, ProgrammingGuideInteractivityKey>();
						interactivityRelation.setInteractivityKey((ProgrammingGuideInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case ARROWS: 
						
						interactivityRelation = new Interactivity<Media, ArrowInteractivityKey>();
						interactivityRelation.setInteractivityKey((ArrowInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case CHANNEL_CHANGE: 

						interactivityRelation = new Interactivity<Media, ChannelChangeInteractivityKey>();
						interactivityRelation.setInteractivityKey((ChannelChangeInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case VOLUME_CHANGE: 

						interactivityRelation = new Interactivity<Media, VolumeChangeInteractivityKey>();
						interactivityRelation.setInteractivityKey((VolumeChangeInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case COLORS: 

						interactivityRelation = new Interactivity<Media, ColorInteractivityKey>();
						interactivityRelation.setInteractivityKey((ColorInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case CONTROL:

						interactivityRelation = new Interactivity<Media, ControlInteractivityKey>();
						interactivityRelation.setInteractivityKey((ControlInteractivityKey) interactivityKeyField.getValue());
						break;
						
					default:
						
						interactivityRelation = new Interactivity<Media, ActuationInteractivityKey>();
						interactivityRelation.setInteractivityKey(ActuationInteractivityKey.OK);
						break;

		    	}
		    	
		    	if(timelineToBeStartedField.getValue() != null){
		    		interactivityRelation.setNewTemporalChain((TemporalChain) timelineToBeStartedField.getValue());
		    	}
		    	
				interactivityRelation.setExplicit(true);
				
				for(int i=0; i < mediaCloseNewVBoxContainer.getChildren().size(); i++){
					
					if (mediaCloseNewVBoxContainer.getChildren().get(i) instanceof HBox) {
						
						HBox mediaCloseHBoxContainer = (HBox) mediaCloseNewVBoxContainer.getChildren().get(i);
					    ChoiceBox<Media> mediaToBeStoppedField = (ChoiceBox<Media>) mediaCloseHBoxContainer.getChildren().get(0);
					    if(mediaToBeStoppedField.getValue() != null){
					    	interactivityRelation.addSlaveMedia(mediaToBeStoppedField.getValue());
					    }
					    
					}
					
				}
		    		
				controller.addInteractivityRelation(temporalChainPane.getTemporalChainModel(), interactivityRelation);
				
				InteractiveMediaWindow.this.close();
				
				final MessageDialog messageDialog = new MessageDialog(Language.translate("no.alignment.was.defined"), 
						Language.translate("the.operation.was.canceled.by.the.user"), "OK", 150);
				messageDialog.show();
				FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), messageDialog.getScene().getRoot());
				fadeIn.setFromValue(0.0);
				fadeIn.setToValue(1.0);
				fadeIn.play();
				fadeIn.setOnFinished(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						
						FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), messageDialog.getScene().getRoot());
						fadeOut.setFromValue(1.0);
						fadeOut.setToValue(0.0);
						fadeOut.play();
						fadeOut.setOnFinished(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								
								messageDialog.close();
								
							}
						});
						
					}
				});

    		}
    		
    	});
		
	}

	private void createFormButtonActions(Button removeMediaButton, Button addNewButton, HBox mediaCloseHBoxContainer, VBox mediaCloseNewVBoxContainer) {
		
		removeMediaButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {

    			ChoiceBox<Media> mediaToBeStoppedField = (ChoiceBox<Media>) mediaCloseHBoxContainer.getChildren().get(0);
				
    			if(mediaCloseNewVBoxContainer.getChildren().size() == 2){
    				
    				mediaToBeStoppedField.setValue(null);
    				
    				mediaCloseNewVBoxContainer.getChildren().remove(1);
    				mediaCloseHBoxContainer.getChildren().remove(1);

    			}else {
    				
    				mediaCloseNewVBoxContainer.getChildren().remove(mediaCloseHBoxContainer);
    				mediaToBeStoppedCount--;
    				
    			}

    		}
    	});
		
		addNewButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {
    			
    			ChoiceBox mediaToBeStoppedField = new ChoiceBox<Media>(FXCollections.observableArrayList(mediaListDuringInteractivityTime));
    			mediaToBeStoppedField.setId("new-interactive-media-field");
    			mediaToBeStoppedCount++;
    			mediaToBeStoppedField.getProperties().put("mediaToBeStoppedCount", mediaToBeStoppedCount);
    			
    			HBox mediaCloseHBoxContainer = new HBox();
    		    mediaCloseHBoxContainer.setId("media-close-button-hbox-container");
    		    mediaCloseHBoxContainer.getChildren().add(mediaToBeStoppedField);
    
    			mediaCloseNewVBoxContainer.getChildren().remove(addNewButton);
    			mediaCloseNewVBoxContainer.getChildren().add(mediaCloseHBoxContainer);

    		    mediaToBeStoppedField.setOnAction(new EventHandler<ActionEvent>() {

    				@Override
    				public void handle(ActionEvent event) {
    	
    					if(mediaToBeStoppedField.getProperties().get("previousValue") == null){
    						
    						Button removeMediaButton = new Button();
        					removeMediaButton.setId("media-to-be-stopped-close-button");
        			    	mediaCloseHBoxContainer.getChildren().add(removeMediaButton);
        			    	
        			    	Button addNewButton = new Button(Language.translate("add.new"));
        			    	addNewButton.setId("add-new-button");
        			    	mediaCloseNewVBoxContainer.getChildren().add(addNewButton);
        			    	
        			    	createFormButtonActions(removeMediaButton, addNewButton, mediaCloseHBoxContainer, mediaCloseNewVBoxContainer);
        			    	
    					}
    					
    					mediaToBeStoppedField.getProperties().put("previousValue", mediaToBeStoppedField.getValue());

    				}
    				
    			});
    		        
    		}
    	});
		
	}
	
	public VBox getMediaCloseVBoxContainer() {
		return mediaCloseNewVBoxContainer;
	}

}
