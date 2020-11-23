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
import model.common.MediaNode;
import model.common.Node;
import model.common.SensoryEffectNode;
import model.common.enums.SensoryEffectType;
import model.temporalView.Interactivity;
import model.temporalView.TemporalChain;
import model.temporalView.enums.AlphabeticalInteractivityKey;
import model.temporalView.enums.ArrowInteractivityKey;
import model.temporalView.enums.ChannelChangeInteractivityKey;
import model.temporalView.enums.ColorInteractivityKey;
import model.temporalView.enums.ControlInteractivityKey;
import model.temporalView.enums.InteractivityKeyType;
import model.temporalView.enums.NumericInteractivityKey;
import model.temporalView.enums.ProgrammingGuideInteractivityKey;
import model.temporalView.enums.VolumeChangeInteractivityKey;
import view.common.dialogs.InputDialog;
import view.common.Language;
import view.common.dialogs.MessageDialog;
import controller.ApplicationController;

/**
 *
 * @author Douglas
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class InteractiveMediaWindow extends Stage {
     
	private static final int HEIGHT = 610;
	private static final int WIDTH = 650;
    
	private ApplicationController applicationController;
	private TemporalViewPane temporalViewPane;
	private Boolean isEdition;
	private Interactivity interactivityRelation;
	
    private Scene scene;
    
    private ChoiceBox<InteractivityKeyType> interactivityKeyTypeField;
	private ChoiceBox interactivityKeyField;
	private CheckBox interactiveMediaWillBeStopped;
	/*TODO
 	private CheckBox endApplication;
	private CheckBox endTemporalChain;
	*/
    private ChoiceBox<Node> nodeToBeStoppedField;
    private ChoiceBox mediaEffectTimelineToBeStartedField;
    private TextField stopDelayField;
    private TextField startDelayField;
    
    private Button addNewButton;
    private Button removeMediaButton;
    
    private boolean isLastOne;
    
    private GridPane formGridPane;
    private VBox nodeToBeStoppedCloseNewVBoxContainer;
    private VBox mediaEffectTimelineCloseNewVBoxContainer;

    private MediaNode interactiveMediaNode;
	private ArrayList<Node> nodeListDuringInteractivityTime;
	private ObservableList mediaEffectTimelineOptions;
    
    public InteractiveMediaWindow(ApplicationController applicationController, TemporalViewPane temporalViewPane, MediaNode firstSelectedMediaNode, ArrayList<Node> nodeListDuringInteractivityTime) {

        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);

        this.temporalViewPane = temporalViewPane;
        this.applicationController = applicationController;
        isEdition = false;
        
        this.interactiveMediaNode = firstSelectedMediaNode;
        this.nodeListDuringInteractivityTime = nodeListDuringInteractivityTime;

        BorderPane containerBorderPane = new BorderPane();
        containerBorderPane.setId("container-border-pane");
        containerBorderPane.getStylesheets().add("styles/temporalViewPane/popupWindow.css");
        
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
    
    public InteractiveMediaWindow(ApplicationController applicationController, TemporalViewPane temporalViewPane, ArrayList<Node> nodeListDuringInteractivityTime, Interactivity<MediaNode> interactivityToLoad){
    	
    	setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED);

        this.temporalViewPane = temporalViewPane;
        this.applicationController = applicationController;
        isEdition=true;
        this.interactivityRelation = interactivityToLoad;
        
        this.interactiveMediaNode = (MediaNode) interactivityToLoad.getPrimaryNode();
        this.nodeListDuringInteractivityTime = nodeListDuringInteractivityTime;
        
        BorderPane containerBorderPane = new BorderPane();
        containerBorderPane.setId("container-border-pane");
        containerBorderPane.getStylesheets().add("styles/temporalViewPane/popupWindow.css");
        
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

    private InteractivityKeyType getKeyType(Object interactivityKey){
    	
    	if(interactivityKey instanceof NumericInteractivityKey){
    		return InteractivityKeyType.NUMERIC;
    	}else if(interactivityKey instanceof AlphabeticalInteractivityKey){
    		return InteractivityKeyType.ALPHABETICAL; 
    	}else if(interactivityKey instanceof ArrowInteractivityKey){
    		return InteractivityKeyType.ARROWS;
    	}else if(interactivityKey instanceof ChannelChangeInteractivityKey){
    		return InteractivityKeyType.CHANNEL_CHANGE;
    	}else if(interactivityKey instanceof ColorInteractivityKey){
    		return InteractivityKeyType.COLORS;
    	}else if(interactivityKey instanceof ControlInteractivityKey){
    		return InteractivityKeyType.CONTROL;
    	}else if(interactivityKey instanceof ProgrammingGuideInteractivityKey){
    		return InteractivityKeyType.PROGRAMMING_GUIDE;
    	}else if(interactivityKey instanceof VolumeChangeInteractivityKey){
    		return InteractivityKeyType.VOLUME_CHANGE;
    	}else {
    		return InteractivityKeyType.CONTROL; 
    	}
			
    }
    
    private void populateInteractiveMediaWindow(Interactivity<MediaNode> interactivityToLoad){
    	
    	interactivityKeyTypeField.setValue(getKeyType(interactivityToLoad.getInteractivityKey()));
        interactivityKeyField.setValue(interactivityToLoad.getInteractivityKey());
        stopDelayField.setText(interactivityToLoad.getStopDelay().toString());
        startDelayField.setText(interactivityToLoad.getStartDelay().toString());
        
        if(interactivityToLoad.getSecondaryNodeList().contains(interactiveMediaNode)){
        	interactiveMediaWillBeStopped.setSelected(true);
        }else {
        	interactiveMediaWillBeStopped.setSelected(false);
        }
        

        //popula campos mediaToBeStoppedField
        for(int i = 0; i < interactivityToLoad.getSecondaryNodeList().size(); i++){
        	
        	MediaNode stoppedMediaNode = (MediaNode) interactivityToLoad.getSecondaryNodeList().get(i);
        	
        	if(stoppedMediaNode != interactiveMediaNode){
        		
        		ChoiceBox choiceBoxField;
    			HBox fieldCloseHBoxContainer = new HBox();
    
				choiceBoxField = new ChoiceBox<Node>(FXCollections.observableArrayList(nodeListDuringInteractivityTime));
				fieldCloseHBoxContainer.setId("media-close-button-hbox-container");
			
    			choiceBoxField.setId("new-interactive-media-field");
    		    fieldCloseHBoxContainer.getChildren().add(choiceBoxField);
    
    		    if(i < (interactivityToLoad.getSecondaryNodeList().size()-1)){
    		    	nodeToBeStoppedCloseNewVBoxContainer.getChildren().add(fieldCloseHBoxContainer);
    		    }
    			
    		    choiceBoxField.setOnAction(new EventHandler<ActionEvent>() {

    				@Override
    				public void handle(ActionEvent event) {
    	
    					if(choiceBoxField.getProperties().get("previousValue") == null){
    						
    						removeMediaButton = new Button();
        					removeMediaButton.setId("media-to-be-stopped-close-button");
        			    	fieldCloseHBoxContainer.getChildren().add(removeMediaButton);
        			    	
        			    	Button addNewButton = new Button(Language.translate("add.new"));
        			    	addNewButton.setId("add-new-button");
        			    
        			    	createFormButtonActions(removeMediaButton, addNewButton, fieldCloseHBoxContainer, nodeToBeStoppedCloseNewVBoxContainer, true);
        			    	
    					}
    					
    					choiceBoxField.getProperties().put("previousValue", choiceBoxField.getValue());

    				}
    				
    			});
        		
        	}
        
        }
        
        int mediaStoppedRealIndex = 0;
        
        for(int i = 0; i < interactivityToLoad.getSecondaryNodeList().size(); i++){
        	
        	MediaNode stoppedMediaNode = (MediaNode) interactivityToLoad.getSecondaryNodeList().get(i);
        	
        	if(stoppedMediaNode != interactiveMediaNode){
        		
        		for(int j = 0; j < nodeToBeStoppedCloseNewVBoxContainer.getChildren().size(); j++){
        			
        			if (nodeToBeStoppedCloseNewVBoxContainer.getChildren().get(j) instanceof HBox && mediaStoppedRealIndex == j) {
        				
        				HBox mediaCloseHBoxContainer = (HBox) nodeToBeStoppedCloseNewVBoxContainer.getChildren().get(j);
        			    ChoiceBox<MediaNode> mediaToBeStoppedField = (ChoiceBox<MediaNode>) mediaCloseHBoxContainer.getChildren().get(0);
        			    mediaToBeStoppedField.setValue(stoppedMediaNode);
        			    
        			}
        			
        		}
        		
        		mediaStoppedRealIndex++;
        		
        	}
        	
        }
		
        //popula campos timelineToBeStartedField
        for(int i=0; i < interactivityToLoad.getTemporalChainList().size(); i++){
        	
    		ChoiceBox<TemporalChain> choiceBoxField;
			HBox mediaEffectCloseHBoxContainer = new HBox();

			choiceBoxField = new ChoiceBox<TemporalChain>(FXCollections.observableArrayList(mediaEffectTimelineOptions));
			mediaEffectCloseHBoxContainer.setId("timeline-close-button-hbox-container");
			mediaEffectCloseHBoxContainer.getChildren().add(choiceBoxField);
	
		    if(i < (interactivityToLoad.getTemporalChainList().size()-1)){
		    	mediaEffectTimelineCloseNewVBoxContainer.getChildren().add(mediaEffectCloseHBoxContainer);
		    }
			
		    choiceBoxField.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
	
					if(choiceBoxField.getProperties().get("previousValue") == null){
						
						removeMediaButton = new Button();
						removeMediaButton.setId("timeline-to-be-stopped-close-button");
						mediaEffectCloseHBoxContainer.getChildren().add(removeMediaButton);
    			    	
    			    	Button addNewButton = new Button(Language.translate("add.new"));
    			    	addNewButton.setId("add-new-button");
    			    
    			    	createFormButtonActions(removeMediaButton, addNewButton, mediaEffectCloseHBoxContainer, mediaEffectTimelineCloseNewVBoxContainer, false);
    			    
					}
					
					choiceBoxField.getProperties().put("previousValue", choiceBoxField.getValue());

				}
				
			});
        
        }
        
        for(int i=0; i < interactivityToLoad.getTemporalChainList().size(); i++){
        	
        	TemporalChain startedTemporalChain = interactivityToLoad.getTemporalChainList().get(i);
  
    		for(int j = 0; j < mediaEffectTimelineCloseNewVBoxContainer.getChildren().size(); j++){
    			
    			if (mediaEffectTimelineCloseNewVBoxContainer.getChildren().get(j) instanceof HBox && i == j) {
    				
    				HBox timelineCloseHBoxContainer = (HBox) mediaEffectTimelineCloseNewVBoxContainer.getChildren().get(j);
    				if(!timelineCloseHBoxContainer.getChildren().isEmpty()){
    					ChoiceBox timelineToBeStartedField = (ChoiceBox) timelineCloseHBoxContainer.getChildren().get(0);
        			    timelineToBeStartedField.setValue(startedTemporalChain);
    				}

    			}
    			
    		}
   
        }
        
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
    		titleLabe = new Label(Language.translate("new.user.interaction"));
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
		
		Label stopActionSubtitle = new Label(Language.translate("stop.action").toUpperCase());
        Label mediaEffectToBeStoppedLabel = new Label(Language.translate("media.effect.to.be.stopped"));
        Label stopDelayLabel = new Label(Language.translate("delay.to.stop.in.seconds"));
        
        Label startActionSubtitle = new Label(Language.translate("start.action").toUpperCase());
        Label newMediaEffectTimelineToBeStartedLabel = new Label(Language.translate("media.effect.to.be.started"));
        Label startDelayLabel = new Label(Language.translate("delay.to.start.in.seconds"));
        
        interactivityKeySubtitle.setId("subtitle-label");
        stopActionSubtitle.setId("subtitle-label");
        startActionSubtitle.setId("subtitle-label");
        stopDelayLabel.setId("msg-label");
        startDelayLabel.setId("msg-label");
        interactivityKeyTypeLabel.setId("msg-label");
        interactivityKeyLabel.setId("msg-label");
        mediaEffectToBeStoppedLabel.setId("msg-label");
        newMediaEffectTimelineToBeStartedLabel.setId("msg-label");
        
        interactivityKeyTypeField = new ChoiceBox<InteractivityKeyType>(FXCollections.observableArrayList(InteractivityKeyType.values()));
        interactivityKeyField = new ChoiceBox();
        interactiveMediaWillBeStopped  =new CheckBox(Language.translate("interactive.media.will.be.stopped"));
//        endApplication  =new CheckBox(Language.translate("end.application"));
//        endTemporalChain = new CheckBox(Language.translate("end.temporal.chain"));
        nodeToBeStoppedField = new ChoiceBox<Node>(FXCollections.observableArrayList(nodeListDuringInteractivityTime));
        stopDelayField = new TextField();
        startDelayField = new TextField();
        
        mediaEffectTimelineOptions = FXCollections.observableArrayList(applicationController.getRepositoryMediaList().getAllTypesList());
        mediaEffectTimelineOptions.add(new Separator());
		mediaEffectTimelineOptions.addAll((Object[]) SensoryEffectType.values());
		mediaEffectTimelineOptions.add(new Separator());
		mediaEffectTimelineOptions.addAll(applicationController.getAllTemporalChains());
        mediaEffectTimelineToBeStartedField = new ChoiceBox(mediaEffectTimelineOptions);
        
        interactivityKeyTypeField.setValue(InteractivityKeyType.CONTROL);
        interactivityKeyField.setItems(FXCollections.observableArrayList(ControlInteractivityKey.values()));
        interactivityKeyField.setValue(ControlInteractivityKey.ENTER);
        interactiveMediaWillBeStopped.setSelected(true);
        stopDelayField.setText("0");
        startDelayField.setText("0");
        
        interactivityKeyTypeField.setId("new-interactive-media-field");
        interactivityKeyField.setId("new-interactive-media-field");
        interactiveMediaWillBeStopped.setId("new-interactive-media-field");
//        endApplication.setId("new-interactive-media-field");
//        endTemporalChain.setId("end-temporal-chain");
        nodeToBeStoppedField.setId("new-interactive-media-field"); 
        mediaEffectTimelineToBeStartedField.setId("new-interactive-media-field");
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
        
        HBox nodeToBeStoppedCloseHBoxContainer = new HBox();
        nodeToBeStoppedCloseHBoxContainer.setId("media-close-button-hbox-container");
        nodeToBeStoppedCloseHBoxContainer.getChildren().add(nodeToBeStoppedField);
        
        HBox mediaEffectTimelineToBeStartedCloseHBoxContainer = new HBox();
        mediaEffectTimelineToBeStartedCloseHBoxContainer.setId("timeline-close-button-hbox-container");
        mediaEffectTimelineToBeStartedCloseHBoxContainer.getChildren().add(mediaEffectTimelineToBeStartedField);
        
        nodeToBeStoppedCloseNewVBoxContainer = new VBox();
        nodeToBeStoppedCloseNewVBoxContainer.setId("media-close-button-vbox-container");
        nodeToBeStoppedCloseNewVBoxContainer.getChildren().add(nodeToBeStoppedCloseHBoxContainer);
        
        mediaEffectTimelineCloseNewVBoxContainer = new VBox();
        mediaEffectTimelineCloseNewVBoxContainer.setId("timeline-close-button-vbox-container");
        mediaEffectTimelineCloseNewVBoxContainer.getChildren().add(mediaEffectTimelineToBeStartedCloseHBoxContainer);
        
        GridPane formGridPane = new GridPane();
        formGridPane.setId("form-grid-pane");
        formGridPane.setValignment(mediaEffectToBeStoppedLabel, VPos.TOP);
        formGridPane.setValignment(newMediaEffectTimelineToBeStartedLabel, VPos.TOP);
        
        formGridPane.add(interactivityKeySubtitleSeparatorContainer, 0, 1, 5, 1);
        formGridPane.add(interactivityKeyTypeLabel, 0, 2);
        formGridPane.add(interactivityKeyTypeField, 1, 2);
        formGridPane.add(interactivityKeyLabel, 0, 3);
        formGridPane.add(interactivityKeyField, 1, 3);
        
        formGridPane.add(stopActionSubtitleSeparatorContainer, 0, 5, 5, 1);
        formGridPane.add(interactiveMediaWillBeStopped, 0, 6);
//        formGridPane.add(endApplication, 1, 6);
//        formGridPane.add(endTemporalChain, 0, 7);
        formGridPane.add(mediaEffectToBeStoppedLabel, 0, 7);
        formGridPane.add(nodeToBeStoppedCloseNewVBoxContainer, 1, 7);
        formGridPane.add(stopDelayLabel, 0, 8);
        formGridPane.add(stopDelayField, 1, 8);
        
        formGridPane.add(startActionSubtitleSeparatorContainer, 0, 10, 5, 1);
        formGridPane.add(newMediaEffectTimelineToBeStartedLabel, 0, 11);
        formGridPane.add(mediaEffectTimelineCloseNewVBoxContainer, 1, 11);
        formGridPane.add(startDelayLabel, 0, 12);
        formGridPane.add(startDelayField, 1, 12);
        
        createActionEvents(formGridPane, nodeToBeStoppedCloseHBoxContainer, nodeToBeStoppedCloseNewVBoxContainer, mediaEffectTimelineToBeStartedCloseHBoxContainer, mediaEffectTimelineCloseNewVBoxContainer);
		
        return formGridPane;
    	
    }

	private void createActionEvents(GridPane formGridPane, HBox nodeToBeStoppedCloseHBoxContainer, VBox nodeToBeStoppedCloseNewVBoxContainer, HBox mediaEffectTimelineToBeStartedCloseHBoxContainer, VBox mediaEffectTimelineCloseNewVBoxContainer) {
		
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
		
		nodeToBeStoppedField.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
		
				if(nodeToBeStoppedField.getProperties().get("previousValue") == null){
					
					removeMediaButton = new Button();
					removeMediaButton.setId("media-to-be-stopped-close-button");
			    	nodeToBeStoppedCloseHBoxContainer.getChildren().add(removeMediaButton);
			    	
			    	addNewButton = new Button(Language.translate("add.new"));
			    	addNewButton.setId("add-new-button");
			    	nodeToBeStoppedCloseNewVBoxContainer.getChildren().add(addNewButton);
			    	
			    	createFormButtonActions(removeMediaButton, addNewButton, nodeToBeStoppedCloseHBoxContainer, nodeToBeStoppedCloseNewVBoxContainer, true);
			    	
				}
				
				nodeToBeStoppedField.getProperties().put("previousValue", nodeToBeStoppedField.getValue());
		    	
			}
			
		});
		
		mediaEffectTimelineToBeStartedField.setOnAction(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				
				if(mediaEffectTimelineToBeStartedField.getProperties().get("previousValue") == null){
					
					Button removeMediaButton = new Button();
					removeMediaButton.setId("timeline-to-be-stopped-close-button");
			    	mediaEffectTimelineToBeStartedCloseHBoxContainer.getChildren().add(removeMediaButton);
			    	
			    	Button addNewButton = new Button(Language.translate("add.new"));
			    	addNewButton.setId("add-new-button");
			    	mediaEffectTimelineCloseNewVBoxContainer.getChildren().add(addNewButton);
			    	
			    	createFormButtonActions(removeMediaButton, addNewButton, mediaEffectTimelineToBeStartedCloseHBoxContainer, mediaEffectTimelineCloseNewVBoxContainer, false);
			    	
				}
				
				mediaEffectTimelineToBeStartedField.getProperties().put("previousValue", mediaEffectTimelineToBeStartedField.getValue());
				
				if(mediaEffectTimelineToBeStartedField.getValue() != null && mediaEffectTimelineToBeStartedField.getValue().toString().equalsIgnoreCase("Add new timeline...")){
					
					mediaEffectTimelineToBeStartedField.setValue(null);
					
					InputDialog newTimelineNameDialog = new InputDialog("New timeline", null, "CANCEL", "OK", "Name", 180);
					String name = newTimelineNameDialog.showAndWaitAndReturn();
					
					if(name != null && !name.equalsIgnoreCase("left") && !name.equalsIgnoreCase("close")){
						
						TemporalChain temporalChain = new TemporalChain(name);
						mediaEffectTimelineToBeStartedField.getItems().add(0, temporalChain);
						mediaEffectTimelineToBeStartedField.setValue(temporalChain);
						
			    	}

	            }
					
			}
			
		});
		
	}

    
	private boolean areThereRepeatedMediaEffectToBeStopped(){
	
		for(int i = 0; i < nodeToBeStoppedCloseNewVBoxContainer.getChildren().size(); i++){
			
			if (nodeToBeStoppedCloseNewVBoxContainer.getChildren().get(i) instanceof HBox) {
				
				HBox iMediaEffectCloseHBoxContainer = (HBox) nodeToBeStoppedCloseNewVBoxContainer.getChildren().get(i);
			    ChoiceBox<Node> iMediaEffectToBeStoppedField = (ChoiceBox<Node>) iMediaEffectCloseHBoxContainer.getChildren().get(0);
			    
			    if(iMediaEffectToBeStoppedField.getValue() != null){
			    	
			    	Node iNodeToBeStoppedField = (Node) iMediaEffectToBeStoppedField.getValue();

			    	for(int j = i+1; j < nodeToBeStoppedCloseNewVBoxContainer.getChildren().size(); j++){
			    		
			    		if (nodeToBeStoppedCloseNewVBoxContainer.getChildren().get(j) instanceof HBox) {
			    			
			    			HBox jMediaCloseHBoxContainer = (HBox) nodeToBeStoppedCloseNewVBoxContainer.getChildren().get(j);
						    ChoiceBox<MediaNode> jMediaToBeStoppedField = (ChoiceBox<MediaNode>) jMediaCloseHBoxContainer.getChildren().get(0);
						    
						    if(jMediaToBeStoppedField.getValue() != null){

								Node jNodeToBeStoppedField = (Node) jMediaToBeStoppedField.getValue();

						    	if(iNodeToBeStoppedField instanceof MediaNode &&
								jNodeToBeStoppedField instanceof MediaNode){

									String iMediaPath = ((MediaNode) iNodeToBeStoppedField).getPath();
									String jMediaPath = ((MediaNode) jNodeToBeStoppedField).getPath();

									if(iMediaPath.equalsIgnoreCase(jMediaPath)){
										return true;
									}

								}else if (iNodeToBeStoppedField instanceof SensoryEffectNode &&
										jNodeToBeStoppedField instanceof SensoryEffectNode){

									SensoryEffectType iSensoryEffectType = (SensoryEffectType) iNodeToBeStoppedField.getType();
									SensoryEffectType jSensoryEffectType = (SensoryEffectType) jNodeToBeStoppedField.getType();

									if(iSensoryEffectType == jSensoryEffectType){
										return true;
									}

						    	}

						    }

			    		}
					    
			    	}
			    	
			    }
			    
			}
			
		}
		
		return false;
		
	}
	
	private boolean areThereRepeatedTimelineToBeStarted(){

		for(int i = 0; i < mediaEffectTimelineCloseNewVBoxContainer.getChildren().size(); i++){
			
			if (mediaEffectTimelineCloseNewVBoxContainer.getChildren().get(i) instanceof HBox) {
				
				HBox iTimelineCloseHBoxContainer = (HBox) mediaEffectTimelineCloseNewVBoxContainer.getChildren().get(i);
			    ChoiceBox iTimelineToBeStartedField = (ChoiceBox) iTimelineCloseHBoxContainer.getChildren().get(0);
			    
			    if(iTimelineToBeStartedField.getValue() != null){

			    	if(iTimelineToBeStartedField.getValue() instanceof TemporalChain){

						TemporalChain iTemporalChain = (TemporalChain) iTimelineToBeStartedField.getValue();

						for(int j = i+1; j < mediaEffectTimelineCloseNewVBoxContainer.getChildren().size(); j++){

							if (mediaEffectTimelineCloseNewVBoxContainer.getChildren().get(j) instanceof HBox) {

								HBox jTimelineCloseHBoxContainer = (HBox) mediaEffectTimelineCloseNewVBoxContainer.getChildren().get(j);
								ChoiceBox jTimelineToBeStoppedField = (ChoiceBox) jTimelineCloseHBoxContainer.getChildren().get(0);

								if(jTimelineToBeStoppedField.getValue() != null){

									if(jTimelineToBeStoppedField.getValue() instanceof TemporalChain){
										TemporalChain jTemporalChain = (TemporalChain) jTimelineToBeStoppedField.getValue();
										if(iTemporalChain == jTemporalChain){
											return true;
										}
									}

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
    				showContinueQuestionInputDialog = new InputDialog(Language.translate("discard.interactive.media.changes"), null, Language.translate("no"), Language.translate("discard"), null, 140);
    			}else {
    				showContinueQuestionInputDialog = new InputDialog(Language.translate("discard.new.interactive.media"), null, Language.translate("no"), Language.translate("discard"), null, 140);
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

    			if(nodeToBeStoppedField.getValue() == null && mediaEffectTimelineToBeStartedField.getValue() == null && !interactiveMediaWillBeStopped.isSelected()){
    				errorMessage.append(Language.translate("select.at.least.one.media.to.be.stopped.or.a.timeline.to.be.started") +"\n \n");
    				count++;
    			}
    			if(areThereRepeatedMediaEffectToBeStopped()){
    				errorMessage.append(Language.translate("it.is.not.allowed.to.select.a.media.more.than.one.time.to.be.stopped") +"\n \n");
    				count++;
    			}
    			if(areThereRepeatedTimelineToBeStarted()){
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
							interactivityRelation = new Interactivity<NumericInteractivityKey>();
						}
						interactivityRelation.setInteractivityKey((NumericInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case ALPHABETICAL:
						
						if(!isEdition){
							interactivityRelation = new Interactivity<AlphabeticalInteractivityKey>();
						}
						interactivityRelation.setInteractivityKey((AlphabeticalInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case PROGRAMMING_GUIDE: 
						
						if(!isEdition){
							interactivityRelation = new Interactivity<ProgrammingGuideInteractivityKey>();
						}
						interactivityRelation.setInteractivityKey((ProgrammingGuideInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case ARROWS: 
						
						if(!isEdition){
							interactivityRelation = new Interactivity<ArrowInteractivityKey>();
						}
						interactivityRelation.setInteractivityKey((ArrowInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case CHANNEL_CHANGE: 

						if(!isEdition){
							interactivityRelation = new Interactivity<ChannelChangeInteractivityKey>();
						}
						interactivityRelation.setInteractivityKey((ChannelChangeInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case VOLUME_CHANGE: 

						if(!isEdition){
							interactivityRelation = new Interactivity<VolumeChangeInteractivityKey>();
						}
						interactivityRelation.setInteractivityKey((VolumeChangeInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case COLORS: 

						if(!isEdition){
							interactivityRelation = new Interactivity<ColorInteractivityKey>();
						}
						interactivityRelation.setInteractivityKey((ColorInteractivityKey) interactivityKeyField.getValue());
						break;
						
					case CONTROL:

						if(!isEdition){
							interactivityRelation = new Interactivity<ControlInteractivityKey>();
						}
						interactivityRelation.setInteractivityKey((ControlInteractivityKey) interactivityKeyField.getValue());
						break;
						
					default:
						
						if(!isEdition){
							interactivityRelation = new Interactivity<ControlInteractivityKey>();
						}
						interactivityRelation.setInteractivityKey(ControlInteractivityKey.ENTER);
						break;

		    	}

				//INFO Se for edição, limpar a lista para não duplicar os valores.
				interactivityRelation.getTemporalChainList().clear();

				TemporalChain newTemporalChain = null;
				ArrayList<TemporalChain> temporalChainAuxList = new ArrayList<TemporalChain>();

				for(int i = 0; i < mediaEffectTimelineCloseNewVBoxContainer.getChildren().size(); i++){
					
					if (mediaEffectTimelineCloseNewVBoxContainer.getChildren().get(i) instanceof HBox) {
						
						HBox mediaEffectCloseHBoxContainer = (HBox) mediaEffectTimelineCloseNewVBoxContainer.getChildren().get(i);
					    ChoiceBox mediaEffectToBeStartedField = (ChoiceBox) mediaEffectCloseHBoxContainer.getChildren().get(0);

					    if(mediaEffectToBeStartedField.getValue() != null){

							if((mediaEffectToBeStartedField.getValue() instanceof MediaNode)
									|| (mediaEffectToBeStartedField.getValue() instanceof SensoryEffectType)){

								if(newTemporalChain == null){
									newTemporalChain = new TemporalChain(Language.translate("temporal.chain") + " " + String.valueOf(temporalViewPane.getTemporalChainTabPane().getTabs().size() + 1));
									applicationController.addTemporalChain(newTemporalChain);
								}

								Node nodeToBeStarted = createNewNodeSelected(mediaEffectToBeStartedField.getValue(), newTemporalChain, i);
								applicationController.addNodeTemporalChain(nodeToBeStarted, newTemporalChain);
								interactivityRelation.addTemporalChain(newTemporalChain);

							}else{
								interactivityRelation.addTemporalChain((TemporalChain) mediaEffectToBeStartedField.getValue());
								temporalChainAuxList.add((TemporalChain) mediaEffectToBeStartedField.getValue());
							}

					    }
					    
					}
					
				}

				interactivityRelation.setExplicit(true);

				//INFO Se for edição, limpar a lista para não duplicar os valores.
				interactivityRelation.getSecondaryNodeList().clear();

				if(interactiveMediaWillBeStopped.isSelected()){
					interactivityRelation.addSecondaryNode(interactiveMediaNode);
				}							
				
				for(int i = 0; i < nodeToBeStoppedCloseNewVBoxContainer.getChildren().size(); i++){
					
					if (nodeToBeStoppedCloseNewVBoxContainer.getChildren().get(i) instanceof HBox) {
						
						HBox mediaCloseHBoxContainer = (HBox) nodeToBeStoppedCloseNewVBoxContainer.getChildren().get(i);
					    ChoiceBox<MediaNode> mediaToBeStoppedField = (ChoiceBox<MediaNode>) mediaCloseHBoxContainer.getChildren().get(0);
					    if(mediaToBeStoppedField.getValue() != null){
					    	interactivityRelation.addSecondaryNode(mediaToBeStoppedField.getValue());
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
				
				interactivityRelation.setPrimaryNode(interactiveMediaNode);
				interactiveMediaNode.setInteractive(true);
				
				if(isEdition){
					applicationController.updateInteractivityRelation(temporalChainPane.getTemporalChainModel(), interactivityRelation);
				}else {
					applicationController.addInteractivityRelation(temporalChainPane.getTemporalChainModel(), interactivityRelation);
				}
				
				InteractiveMediaWindow.this.close();

				String feedbackTitle = Language.translate("user.interaction.created.successfully");
				String newTimelineCreatedMessage="";
				String nodesToBeStartedMessage="";
				String existingTimelineUsedMessage="";
				String existingTimelineToBeStartedMessage="";

				if(newTemporalChain != null){
					newTimelineCreatedMessage = Language.translate("new.timeline.created");;
					for(Node newNode : newTemporalChain.getNodeAllList()){
						nodesToBeStartedMessage = nodesToBeStartedMessage + newNode.getName() + "\n";
					}
				}

				if(!temporalChainAuxList.isEmpty()){
					existingTimelineUsedMessage = Language.translate("existing.timeline");
					for(TemporalChain temporalChain : temporalChainAuxList){
						existingTimelineToBeStartedMessage =
								existingTimelineToBeStartedMessage + temporalChain.getName() + "\n";
					}
				}

				String finalFeedbackMessage = newTimelineCreatedMessage + "\n\n" + nodesToBeStartedMessage
						+ "\n" + existingTimelineUsedMessage + "\n\n" + existingTimelineToBeStartedMessage;

				InputDialog showOkInputDialog = new InputDialog(feedbackTitle, finalFeedbackMessage, "OK", null, null, 300);
				showOkInputDialog.showAndWait();
				showOkInputDialog.requestFocus();
				
    		}
    		
    	});
		
	}

	private Node createNewNodeSelected(Object selectedMediaEffectOption, TemporalChain newTemporalChain, int i) {

    	Node newNode = null;

    	if(selectedMediaEffectOption instanceof MediaNode){

    		newNode = (MediaNode) selectedMediaEffectOption;

			int duplicatedMediaCount = getDuplicatedMediaNodeCount((MediaNode) newNode, newTemporalChain);
			duplicatedMediaCount++;
			if(duplicatedMediaCount > 1){
				newNode.setName(newNode.getName() + "_" + duplicatedMediaCount);
			}else{
				newNode.setName(newNode.getName());
			}

    		newNode.setBegin(0.0);
    		newNode.setEnd(newNode.getDuration());

		}else if(selectedMediaEffectOption instanceof SensoryEffectType){

			SensoryEffectType sensoryEffectType = (SensoryEffectType) selectedMediaEffectOption;
			newNode = new SensoryEffectNode(sensoryEffectType);

			newNode.setParentTemporalChain(newTemporalChain);

			int duplicatedEffectCount = getDuplicatedEffectNodeCount(sensoryEffectType, newTemporalChain);
			duplicatedEffectCount++;
			if(duplicatedEffectCount > 1){
				newNode.setName(sensoryEffectType.toString() + "_" + duplicatedEffectCount);
			}else{
				newNode.setName(sensoryEffectType.toString());
			}

    		newNode.setBegin(0.0);
			newNode.setEnd(newNode.getDuration());

		}

		if(i == 0){
			applicationController.setMasterNode(newNode, newTemporalChain);
		}

    	return newNode;
	}

	private int getDuplicatedMediaNodeCount(MediaNode droppedMediaNode, TemporalChain newTemporalChain){

		int countSameMedia=0;

		for(MediaNode mediaNode : newTemporalChain.getMediaNodeAllList()){
			if(mediaNode.getPath().equalsIgnoreCase(droppedMediaNode.getPath())){
				countSameMedia++;
			}
		}

		return countSameMedia;

	}

	private int getDuplicatedEffectNodeCount(SensoryEffectType sensoryEffectType, TemporalChain newTemporalChain) {

		int countSameEffect=0;

		for(SensoryEffectNode sensoryEffectNode : newTemporalChain.getSensoryEffectNodeAllList()){
			if(sensoryEffectType == sensoryEffectNode.getType()){
				countSameEffect++;
			}
		}

		return countSameEffect;

	}

	private boolean isNodesToBeStoppedField(Button removeMediaButton){
    	
    	if(removeMediaButton.getId().indexOf("timeline") != -1){
    		return false;
    	}else {
    		return true;
    	} 
    	
    }
    
	private void createFormButtonActions(Button removeMediaButton, Button addNewButton, HBox fieldCloseHBoxContainer, VBox fieldCloseNewVBoxContainer, Boolean isNodeToBeStopped) {
		
		removeMediaButton.setOnAction(new EventHandler<ActionEvent>(){
    		@Override
            public void handle(ActionEvent arg0) {

    			ChoiceBox<MediaNode> field = (ChoiceBox<MediaNode>) fieldCloseHBoxContainer.getChildren().get(0);
				
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

    			if(isNodeToBeStopped){
    				choiceBoxField = new ChoiceBox<Node>(FXCollections.observableArrayList(nodeListDuringInteractivityTime));
    				fieldCloseHBoxContainer.setId("media-close-button-hbox-container");
					removeMediaButton.setId("media-to-be-stopped-close-button");
    			}else {
    				choiceBoxField = new ChoiceBox<MediaNode>(FXCollections.observableArrayList(mediaEffectTimelineOptions));
    				fieldCloseHBoxContainer.setId("timeline-close-button-hbox-container");
					removeMediaButton.setId("timeline-to-be-started-close-button");
    			}
    			
    			choiceBoxField.setId("new-interactive-media-field");
    		    fieldCloseHBoxContainer.getChildren().add(choiceBoxField);
    
    			fieldCloseNewVBoxContainer.getChildren().remove(addNewButton);
    			fieldCloseNewVBoxContainer.getChildren().add(fieldCloseHBoxContainer);

    		    choiceBoxField.setOnAction(new EventHandler<ActionEvent>() {

    				@Override
    				public void handle(ActionEvent event) {
    	
    					if(choiceBoxField.getProperties().get("previousValue") == null){

        			    	fieldCloseHBoxContainer.getChildren().add(removeMediaButton);
        			    	
        			    	Button addNewButton = new Button(Language.translate("add.new"));
        			    	addNewButton.setId("add-new-button");
        			    	fieldCloseNewVBoxContainer.getChildren().add(addNewButton);
        			    	
        			    	createFormButtonActions(removeMediaButton, addNewButton, fieldCloseHBoxContainer, fieldCloseNewVBoxContainer, isNodeToBeStopped);
        			    	
    					}
    					
    					choiceBoxField.getProperties().put("previousValue", choiceBoxField.getValue());

    				}
    				
    			});
    		        
    		}
    	});
		
	}
	
	public VBox getMediaCloseVBoxContainer() {
		return nodeToBeStoppedCloseNewVBoxContainer;
	}

}
