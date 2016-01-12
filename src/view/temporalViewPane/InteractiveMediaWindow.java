package view.temporalViewPane;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
import view.common.ReturnMessage;
import view.utility.AnimationUtil;
import controller.Controller;

/**
 *
 * @author Douglas
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class InteractiveMediaWindow extends Stage {
     
	private static final int HEIGHT = 600;
	private static final int WIDTH = 650;
    
	private Controller controller;
	private TemporalViewPane temporalViewPane;
	private Boolean isEdition;
	private Interactivity interactivityRelation;
	
    private Scene scene;
    
    private ChoiceBox<InteractivityKeyType> interactivityKeyTypeField;
	private ChoiceBox interactivityKeyField;
	private CheckBox interactiveMediaWillBeStopped;
    private ChoiceBox<Media> mediaToBeStoppedField;
    private ChoiceBox timelineToBeStartedField;
    private TextField stopDelayField;
    private TextField startDelayField;
    
    private GridPane formGridPane;
    private VBox mediaCloseNewVBoxContainer;
    private VBox timelineCloseNewVBoxContainer;

    private Media interactiveMedia;
	private ArrayList<Media> mediaListDuringInteractivityTime;
	private ObservableList timelineFieldOptions;
    
    public InteractiveMediaWindow(Controller controller, TemporalViewPane temporalViewPane, Media firstSelectedMedia, ArrayList<Media> mediaListDuringInteractivityTime) {

        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);

        this.temporalViewPane = temporalViewPane;
        this.controller = controller;
        isEdition = false;
        
        this.interactiveMedia = firstSelectedMedia;
        this.mediaListDuringInteractivityTime = mediaListDuringInteractivityTime;

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
    
    public InteractiveMediaWindow(Controller controller, TemporalViewPane temporalViewPane, ArrayList<Media> mediaListDuringInteractivityTime, Interactivity<Media, ?> interactivityToLoad){
    	
    	setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);

        this.temporalViewPane = temporalViewPane;
        this.controller = controller;
        isEdition=true;
        this.interactivityRelation = interactivityToLoad;
        
        this.interactiveMedia = interactivityToLoad.getMasterMedia();
        this.mediaListDuringInteractivityTime = mediaListDuringInteractivityTime;
        
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
        
        populateInteractiveMediaWindow(interactivityToLoad);
    	
    }

    private void populateInteractiveMediaWindow(Interactivity<Media, ?> interactivityToLoad){
    	
    	//interactivityKeyTypeField.setValue(interactivityToLoad.getInteractivityKeyType());
        //interactivityKeyField.setValue(interactivityToLoad.getInteractivityKey());
        stopDelayField.setText(interactivityToLoad.getStopDelay().toString());
        startDelayField.setText(interactivityToLoad.getStartDelay().toString());
        
        if(interactivityToLoad.getSlaveMediaList().contains(interactiveMedia)){
        	interactiveMediaWillBeStopped.setSelected(true);
        }else {
        	interactiveMediaWillBeStopped.setSelected(false);
        }
        

//        mediaToBeStoppedField
//        timelineToBeStartedField
    	
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
    	
    	if(isEdition){
    		titleLabe = new Label(Language.translate("edit.interactive.media"));
    	}else {
    		titleLabe = new Label(Language.translate("new.interactive.media"));
    	}
    	
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
    	
		Label interactivityKeySubtitle = new Label(Language.translate("interactivity.key").toUpperCase());
		Label interactivityKeyTypeLabel = new Label(Language.translate("interactivity.key.type"));
		Label interactivityKeyLabel = new Label(Language.translate("interactivity.key"));
		
		Label stopActionSubtitle = new Label(Language.translate("stop.action"));
        Label mediaToBeStoppedLabel = new Label(Language.translate("media.to.be.stopped"));
        Label stopDelayLabel = new Label(Language.translate("delay.to.stop.in.seconds"));
        
        Label startActionSubtitle = new Label(Language.translate("start.action"));
        Label timelineToBeStartedLabel = new Label(Language.translate("timeline.to.be.started"));
        Label startDelayLabel = new Label(Language.translate("delay.to.start.in.seconds"));
        
        interactivityKeySubtitle.setId("subtitle-label");
        stopActionSubtitle.setId("subtitle-label");
        startActionSubtitle.setId("subtitle-label");
        stopDelayLabel.setId("msg-label");
        startDelayLabel.setId("msg-label");
        interactivityKeyTypeLabel.setId("msg-label");
        interactivityKeyLabel.setId("msg-label");
        mediaToBeStoppedLabel.setId("msg-label");
        timelineToBeStartedLabel.setId("msg-label");
        
        interactivityKeyTypeField = new ChoiceBox<InteractivityKeyType>(FXCollections.observableArrayList(InteractivityKeyType.values()));
        interactivityKeyField = new ChoiceBox();
        interactiveMediaWillBeStopped  =new CheckBox(Language.translate("interactive.media.will.be.stopped"));
        mediaToBeStoppedField = new ChoiceBox<Media>(FXCollections.observableArrayList(mediaListDuringInteractivityTime));
        stopDelayField = new TextField();
        startDelayField = new TextField();
        
        timelineFieldOptions = FXCollections.observableArrayList(temporalViewPane.getTemporalViewModel().getTemporalChainList());
        timelineFieldOptions.add(new Separator());
        timelineFieldOptions.add(Language.translate("add.new.timeline") + "...");
        timelineToBeStartedField = new ChoiceBox(timelineFieldOptions);
        
        interactivityKeyTypeField.setValue(InteractivityKeyType.ACTUATION);
        interactivityKeyField.setItems(FXCollections.observableArrayList(ActuationInteractivityKey.values()));
        interactivityKeyField.setValue(ActuationInteractivityKey.OK);
        interactiveMediaWillBeStopped.setSelected(true);
        stopDelayField.setText("0");
        startDelayField.setText("0");
        
        interactivityKeyTypeField.setId("new-interactive-media-field");
        interactivityKeyField.setId("new-interactive-media-field");
        interactiveMediaWillBeStopped.setId("new-interactive-media-field");
        mediaToBeStoppedField.setId("new-interactive-media-field"); 
        timelineToBeStartedField.setId("new-interactive-media-field");
        stopDelayField.setId("input-field");
        startDelayField.setId("input-field");
        
        HBox interactivityKeySeparator = new HBox();
        interactivityKeySeparator.setId("separator");
        VBox interactivityKeySubtitleSeparatorContainer = new VBox();
        interactivityKeySubtitleSeparatorContainer.setId("subtitle-separator-container");
        interactivityKeySubtitleSeparatorContainer.getChildren().add(interactivityKeySubtitle);
        interactivityKeySubtitleSeparatorContainer.getChildren().add(interactivityKeySeparator);
        
        HBox stopActionSeparator = new HBox();
        stopActionSeparator.setId("separator");
        VBox stopActionSubtitleSeparatorContainer = new VBox();
        stopActionSubtitleSeparatorContainer.setId("subtitle-separator-container");
        stopActionSubtitleSeparatorContainer.getChildren().add(stopActionSubtitle);
        stopActionSubtitleSeparatorContainer.getChildren().add(stopActionSeparator);
        
        HBox startActionSeparator = new HBox();
        startActionSeparator.setId("separator");
        VBox startActionSubtitleSeparatorContainer = new VBox();
        startActionSubtitleSeparatorContainer.setId("subtitle-separator-container");
        startActionSubtitleSeparatorContainer.getChildren().add(startActionSubtitle);
        startActionSubtitleSeparatorContainer.getChildren().add(startActionSeparator);
        
        HBox mediaCloseHBoxContainer = new HBox();
        mediaCloseHBoxContainer.setId("media-close-button-hbox-container");
        mediaCloseHBoxContainer.getChildren().add(mediaToBeStoppedField);
        
        HBox timelineCloseHBoxContainer = new HBox();
        timelineCloseHBoxContainer.setId("timeline-close-button-hbox-container");
        timelineCloseHBoxContainer.getChildren().add(timelineToBeStartedField);
        
        mediaCloseNewVBoxContainer = new VBox();
        mediaCloseNewVBoxContainer.setId("media-close-button-vbox-container");
        mediaCloseNewVBoxContainer.getChildren().add(mediaCloseHBoxContainer);
        
        timelineCloseNewVBoxContainer = new VBox();
        timelineCloseNewVBoxContainer.setId("timeline-close-button-vbox-container");
        timelineCloseNewVBoxContainer.getChildren().add(timelineCloseHBoxContainer);
        
        GridPane formGridPane = new GridPane();
        formGridPane.setId("form-grid-pane");
        formGridPane.setValignment(mediaToBeStoppedLabel, VPos.TOP);
        formGridPane.setValignment(timelineToBeStartedLabel, VPos.TOP);
        
        formGridPane.add(interactivityKeySubtitleSeparatorContainer, 0, 1, 5, 1);
        formGridPane.add(interactivityKeyTypeLabel, 0, 2);
        formGridPane.add(interactivityKeyTypeField, 1, 2);
        formGridPane.add(interactivityKeyLabel, 0, 3);
        formGridPane.add(interactivityKeyField, 1, 3);
        
        formGridPane.add(stopActionSubtitleSeparatorContainer, 0, 5, 5, 1);
        formGridPane.add(interactiveMediaWillBeStopped, 0, 6);
        formGridPane.add(mediaToBeStoppedLabel, 0, 7);
        formGridPane.add(mediaCloseNewVBoxContainer, 1, 7);
        formGridPane.add(stopDelayLabel, 0, 8);
        formGridPane.add(stopDelayField, 1, 8);
        
        formGridPane.add(startActionSubtitleSeparatorContainer, 0, 10, 5, 1);
        formGridPane.add(timelineToBeStartedLabel, 0, 11);
        formGridPane.add(timelineCloseNewVBoxContainer, 1, 11);
        formGridPane.add(startDelayLabel, 0, 12);
        formGridPane.add(startDelayField, 1, 12);
        
        createActionEvents(formGridPane, mediaCloseHBoxContainer, mediaCloseNewVBoxContainer, timelineCloseHBoxContainer, timelineCloseNewVBoxContainer);
		
        return formGridPane;
    	
    }

	private void createActionEvents(GridPane formGridPane, HBox mediaCloseHBoxContainer, VBox mediaCloseNewVBoxContainer, HBox timelineCloseHBoxContainer, VBox timelineCloseNewVBoxContainer) {
		
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
				
				if(timelineToBeStartedField.getProperties().get("previousValue") == null){
					
					Button removeMediaButton = new Button();
					removeMediaButton.setId("timeline-to-be-stopped-close-button");
			    	timelineCloseHBoxContainer.getChildren().add(removeMediaButton);
			    	
			    	Button addNewButton = new Button(Language.translate("add.new"));
			    	addNewButton.setId("add-new-button");
			    	timelineCloseNewVBoxContainer.getChildren().add(addNewButton);
			    	
			    	createFormButtonActions(removeMediaButton, addNewButton, timelineCloseHBoxContainer, timelineCloseNewVBoxContainer);
			    	
				}
				
				timelineToBeStartedField.getProperties().put("previousValue", timelineToBeStartedField.getValue());
				
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

    
	private boolean areThereRepeatedMedia(){
	
		for(int i=0; i < mediaCloseNewVBoxContainer.getChildren().size(); i++){
			
			if (mediaCloseNewVBoxContainer.getChildren().get(i) instanceof HBox) {
				
				HBox mediaCloseHBoxContainer = (HBox) mediaCloseNewVBoxContainer.getChildren().get(i);
			    ChoiceBox<Media> mediaToBeStoppedField = (ChoiceBox<Media>) mediaCloseHBoxContainer.getChildren().get(0);
			    
			    if(mediaToBeStoppedField.getValue() != null){
			    	
			    	Media media = mediaToBeStoppedField.getValue();
			    	for(int j=i+1; j < mediaCloseNewVBoxContainer.getChildren().size(); j++){
			    		
			    		if (mediaCloseNewVBoxContainer.getChildren().get(j) instanceof HBox) {
			    			
			    			HBox jMediaCloseHBoxContainer = (HBox) mediaCloseNewVBoxContainer.getChildren().get(j);
						    ChoiceBox<Media> jMediaToBeStoppedField = (ChoiceBox<Media>) jMediaCloseHBoxContainer.getChildren().get(0);
						    
						    if(jMediaToBeStoppedField.getValue() != null){
						    	
						    	Media jMedia = jMediaToBeStoppedField.getValue();
						    	if(media == jMedia){
						    		return true;
						    	}
						    }
			    			
			    		}
					    
			    	}
			    	
			    }
			    
			}
			
		}
		
		return false;
		
	}
	
	private boolean areThereRepeatedTemporalChain(){
		
		for(int i=0; i < timelineCloseNewVBoxContainer.getChildren().size(); i++){
			
			if (timelineCloseNewVBoxContainer.getChildren().get(i) instanceof HBox) {
				
				HBox timelineCloseHBoxContainer = (HBox) timelineCloseNewVBoxContainer.getChildren().get(i);
			    ChoiceBox timelineToBeStartedField = (ChoiceBox) timelineCloseHBoxContainer.getChildren().get(0);
			    
			    if(timelineToBeStartedField.getValue() != null){
			    	
			    	TemporalChain temporalChain = (TemporalChain) timelineToBeStartedField.getValue();
			    	
			    	for(int j=i+1; j < timelineCloseNewVBoxContainer.getChildren().size(); j++){
			    		
			    		if (timelineCloseNewVBoxContainer.getChildren().get(j) instanceof HBox) {
			    			
			    			HBox jTimelineCloseHBoxContainer = (HBox) timelineCloseNewVBoxContainer.getChildren().get(j);
						    ChoiceBox jTimelineToBeStoppedField = (ChoiceBox) jTimelineCloseHBoxContainer.getChildren().get(0);
						    
						    if(jTimelineToBeStoppedField.getValue() != null){
						    	
						    	TemporalChain jTemporalChain = (TemporalChain) jTimelineToBeStoppedField.getValue();
						    	if(temporalChain == jTemporalChain){
						    		return true;
						    	}
						    }
			    			
			    		}
					    
			    	}
			    	
			    }
			    
			}
			
		}
		
		return false;
		
	}
	
    private void createToolBarButtonActions(Button closeButton, Button saveButton) {
		
    	closeButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {
    			
    			InputDialog showContinueQuestionInputDialog;
    			
    			if(isEdition){
    				showContinueQuestionInputDialog = new InputDialog(Language.translate("discard.interactive.media.changes"), null, "no","discard", null, 140);
    			}else {
    				showContinueQuestionInputDialog = new InputDialog(Language.translate("discard.new.interactive.media"), null, "no","discard", null, 140);
    			}
    			
    			String answer = showContinueQuestionInputDialog.showAndWaitAndReturn();
    			
    			if(answer.equalsIgnoreCase("right")){
    				InteractiveMediaWindow.this.close();
    	    	}

    		}
    	});
    	
    	saveButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {
	
    			StringBuilder errorMessage = new StringBuilder();
    			int high = 190;
    			int count = 0;
    			
    			if(mediaToBeStoppedField.getValue() == null && timelineToBeStartedField.getValue() == null && !interactiveMediaWillBeStopped.isSelected()){
    				errorMessage.append(Language.translate("select.at.least.one.media.to.be.stopped.or.a.timeline.to.be.started") +"\n \n");
    				count++;
    			}
    			if(areThereRepeatedMedia()){
    				errorMessage.append(Language.translate("it.is.not.allowed.to.select.a.media.more.than.one.time.to.be.stopped") +"\n \n");
    				count++;
    			}
    			if(areThereRepeatedTemporalChain()){
    				errorMessage.append(Language.translate("it.is.not.allowed.to.select.a.temporal.chain.more.than.one.time.to.be.started") +"\n \n");
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
    				
    				MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.new.interactive.media"), errorMessage.toString(), "OK", high);
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
		    	
		    	switch (interactivityKeyTypeField.getValue()) {
				
					case NUMERIC:
						
						if(!isEdition){
							interactivityRelation = new Interactivity<Media, NumericInteractivityKey>();
						}
						interactivityRelation.setInteractivityKey((NumericInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case ALPHABETICAL:
						
						if(!isEdition){
							interactivityRelation = new Interactivity<Media, AlphabeticalInteractivityKey>();
						}
						interactivityRelation.setInteractivityKey((AlphabeticalInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case PROGRAMMING_GUIDE: 
						
						if(!isEdition){
							interactivityRelation = new Interactivity<Media, ProgrammingGuideInteractivityKey>();
						}
						interactivityRelation.setInteractivityKey((ProgrammingGuideInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case ARROWS: 
						
						if(!isEdition){
							interactivityRelation = new Interactivity<Media, ArrowInteractivityKey>();
						}
						interactivityRelation.setInteractivityKey((ArrowInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case CHANNEL_CHANGE: 

						if(!isEdition){
							interactivityRelation = new Interactivity<Media, ChannelChangeInteractivityKey>();
						}
						interactivityRelation.setInteractivityKey((ChannelChangeInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case VOLUME_CHANGE: 

						if(!isEdition){
							interactivityRelation = new Interactivity<Media, VolumeChangeInteractivityKey>();
						}
						interactivityRelation.setInteractivityKey((VolumeChangeInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case COLORS: 

						if(!isEdition){
							interactivityRelation = new Interactivity<Media, ColorInteractivityKey>();
						}
						interactivityRelation.setInteractivityKey((ColorInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case CONTROL:

						if(!isEdition){
							interactivityRelation = new Interactivity<Media, ControlInteractivityKey>();
						}
						interactivityRelation.setInteractivityKey((ControlInteractivityKey) interactivityKeyField.getValue());
						break;
						
					default:
						
						if(!isEdition){
							interactivityRelation = new Interactivity<Media, ActuationInteractivityKey>();
						}
						interactivityRelation.setInteractivityKey(ActuationInteractivityKey.OK);
						break;

		    	}

		    	interactivityRelation.getTemporalChainList().clear(); //INFO Se for edição, limpar a lista para não duplicar os valores.
		    	
		    	for(int i=0; i < timelineCloseNewVBoxContainer.getChildren().size(); i++){
					
					if (timelineCloseNewVBoxContainer.getChildren().get(i) instanceof HBox) {
						
						HBox timelineCloseHBoxContainer = (HBox) timelineCloseNewVBoxContainer.getChildren().get(i);
					    ChoiceBox timelineToBeStartedField = (ChoiceBox) timelineCloseHBoxContainer.getChildren().get(0);
					    if(timelineToBeStartedField.getValue() != null){
					    	interactivityRelation.addTemporalChain((TemporalChain) timelineToBeStartedField.getValue());
					    }
					    
					}
					
				}
		    	
				interactivityRelation.setExplicit(true);
				
				interactivityRelation.getSlaveMediaList().clear(); //INFO Se for edição, limpar a lista para não duplicar os valores.
				
				if(interactiveMediaWillBeStopped.isSelected()){
					interactivityRelation.addSlaveMedia(interactiveMedia);
				}
				
				for(int i=0; i < mediaCloseNewVBoxContainer.getChildren().size(); i++){
					
					if (mediaCloseNewVBoxContainer.getChildren().get(i) instanceof HBox) {
						
						HBox mediaCloseHBoxContainer = (HBox) mediaCloseNewVBoxContainer.getChildren().get(i);
					    ChoiceBox<Media> mediaToBeStoppedField = (ChoiceBox<Media>) mediaCloseHBoxContainer.getChildren().get(0);
					    if(mediaToBeStoppedField.getValue() != null){
					    	interactivityRelation.addSlaveMedia(mediaToBeStoppedField.getValue());
					    }
					    
					}
					
				}
			
				if(stopDelayField.getText().isEmpty()){
		    		interactivityRelation.setStopDelay(0.0);
		    	}else {
		    		interactivityRelation.setStopDelay(Double.valueOf(stopDelayField.getText()));
		    	}
				
				if(startDelayField.getText().isEmpty()){
		    		interactivityRelation.setStartDelay(0.0);
		    	}else {
		    		interactivityRelation.setStartDelay(Double.valueOf(startDelayField.getText()));
		    	}
				
				interactivityRelation.setMasterMedia(interactiveMedia);
				interactiveMedia.setInteractive(true);
				
				if(isEdition){
					controller.updateInteractivityRelation(temporalChainPane.getTemporalChainModel(), interactivityRelation);
				}else {
					controller.addInteractivityRelation(temporalChainPane.getTemporalChainModel(), interactivityRelation);
				}
				
				InteractiveMediaWindow.this.close();
				
				ReturnMessage returnMessage = new ReturnMessage(Language.translate("interactive.media.saved.successfully"), 350);
				returnMessage.show();
				AnimationUtil.applyFadeInOut(returnMessage);
				
    		}
    		
    	});
		
	}

    private boolean isMediaField(Button removeMediaButton){
    	
    	if(removeMediaButton.getId().indexOf("timeline") != -1){
    		return false;
    	}else {
    		return true;
    	} 
    	
    }
    
	private void createFormButtonActions(Button removeMediaButton, Button addNewButton, HBox fieldCloseHBoxContainer, VBox fieldCloseNewVBoxContainer) {
		
		removeMediaButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {

    			ChoiceBox<Media> field = (ChoiceBox<Media>) fieldCloseHBoxContainer.getChildren().get(0);
				
    			if(fieldCloseNewVBoxContainer.getChildren().size() == 2){
    				
    				field.setValue(null);
    				
    				fieldCloseNewVBoxContainer.getChildren().remove(1);
    				fieldCloseHBoxContainer.getChildren().remove(1);

    			}else {
    				
    				fieldCloseNewVBoxContainer.getChildren().remove(fieldCloseHBoxContainer);
    				
    			}

    		}
    	});
		
		addNewButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {
    			
    			ChoiceBox choiceBoxField;
    			HBox fieldCloseHBoxContainer = new HBox();
    			
    			if(isMediaField(removeMediaButton)){
    				choiceBoxField = new ChoiceBox<Media>(FXCollections.observableArrayList(mediaListDuringInteractivityTime));
    				fieldCloseHBoxContainer.setId("media-close-button-hbox-container");
    			}else {
    				choiceBoxField = new ChoiceBox<Media>(FXCollections.observableArrayList(timelineFieldOptions));
    				fieldCloseHBoxContainer.setId("timeline-close-button-hbox-container");
    			}
    			
    			choiceBoxField.setId("new-interactive-media-field");
    		    fieldCloseHBoxContainer.getChildren().add(choiceBoxField);
    
    			fieldCloseNewVBoxContainer.getChildren().remove(addNewButton);
    			fieldCloseNewVBoxContainer.getChildren().add(fieldCloseHBoxContainer);

    		    choiceBoxField.setOnAction(new EventHandler<ActionEvent>() {

    				@Override
    				public void handle(ActionEvent event) {
    	
    					if(choiceBoxField.getProperties().get("previousValue") == null){
    						
    						Button removeMediaButton = new Button();
        					removeMediaButton.setId("media-to-be-stopped-close-button");
        			    	fieldCloseHBoxContainer.getChildren().add(removeMediaButton);
        			    	
        			    	Button addNewButton = new Button(Language.translate("add.new"));
        			    	addNewButton.setId("add-new-button");
        			    	fieldCloseNewVBoxContainer.getChildren().add(addNewButton);
        			    	
        			    	createFormButtonActions(removeMediaButton, addNewButton, fieldCloseHBoxContainer, fieldCloseNewVBoxContainer);
        			    	
    					}
    					
    					choiceBoxField.getProperties().put("previousValue", choiceBoxField.getValue());

    				}
    				
    			});
    		        
    		}
    	});
		
	}
	
	public VBox getMediaCloseVBoxContainer() {
		return mediaCloseNewVBoxContainer;
	}

}
