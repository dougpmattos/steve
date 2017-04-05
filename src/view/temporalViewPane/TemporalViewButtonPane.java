package view.temporalViewPane;

import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import model.common.Media;
import model.repository.RepositoryMediaList;
import model.temporalView.Interactivity;
import model.temporalView.TemporalRelation;
import model.temporalView.Synchronous;
import model.temporalView.TemporalChain;
import model.temporalView.enums.TemporalRelationType;
import view.common.InputDialog;
import view.common.Language;
import view.common.MessageDialog;
import view.common.SliderButton;
import controller.Controller;

public class TemporalViewButtonPane extends BorderPane {

	private Button meetsButton;
	private Button meetsDelayButton;
	private Button metByButton;
	private Button metByDelayButton;
	private Button startsButton;
	private Button finishesButton;
	private Button beforeButton;
	private Button startsDelayButton;
	private Button finishesDelayButton;
	private Button duringButton;
	private Button overlapsButton;
	private Button equalsButton;
	private Button interactivityButton;
	private SliderButton zoomButton;
	private HBox otherButtonPane;
	private CheckBox showAnchorsLinksButton;
	private HBox alignmentButtonPane;
	private TemporalViewPane temporalViewPane;
	private RepositoryMediaList repositoryMediaList;
	private Controller controller;
	
	public TemporalViewButtonPane(Controller controller, TabPane temporalChainTabPane, TemporalViewPane temporalViewPane, RepositoryMediaList repositoryMediaList){
		
		this.controller = controller;
		this.temporalViewPane = temporalViewPane;
		this.repositoryMediaList = repositoryMediaList;
		
		setId("button-pane");
	    getStylesheets().add("view/temporalViewPane/styles/temporalViewButtonPane.css");
	    
	    createButtons();
	    
	    setLeft(alignmentButtonPane);
	    setRight(otherButtonPane);
	    
	    createButtonActions(temporalChainTabPane);
		
	}

	private void createButtons() {
		
		equalsButton = new Button();
        equalsButton.setId("equals-button");
        equalsButton.setTooltip(new Tooltip(Language.translate("equals")));
        
        startsButton = new Button();
        startsButton.setId("starts-button");
        startsButton.setTooltip(new Tooltip(Language.translate("starts")));
        
        startsDelayButton = new Button();
        startsDelayButton.setId("starts-delay-button");
        startsDelayButton.setTooltip(new Tooltip(Language.translate("starts.delay")));
	       
        finishesButton = new Button();
        finishesButton.setId("finishes-button");
        finishesButton.setTooltip(new Tooltip(Language.translate("finishes")));
        
        finishesDelayButton = new Button();
        finishesDelayButton.setId("finishes-delay-button");
        finishesDelayButton.setTooltip(new Tooltip(Language.translate("finishes.delay")));
        
		meetsButton = new Button();
	    meetsButton.setId("meets-button");
	    meetsButton.setTooltip(new Tooltip(Language.translate("meets")));
	    
	    meetsDelayButton = new Button();
	    meetsDelayButton.setId("meets-delay-button");
	    meetsDelayButton.setTooltip(new Tooltip(Language.translate("meets.delay")));
	    			
	    metByButton = new Button();
	    metByButton.setId("met-by-button");
	    metByButton.setTooltip(new Tooltip(Language.translate("met.by")));
	        
	    metByDelayButton= new Button();
        metByDelayButton.setId("met-by-delay-button");
        metByDelayButton.setTooltip(new Tooltip(Language.translate("met.by.delay")));
        
        duringButton = new Button();
        duringButton.setId("during-button");
        duringButton.setTooltip(new Tooltip(Language.translate("during")));
        
        overlapsButton = new Button();
        overlapsButton.setId("overlaps-button");
        overlapsButton.setTooltip(new Tooltip(Language.translate("overlaps")));
 
        beforeButton = new Button();
        beforeButton.setId("before-button");
        beforeButton.setTooltip(new Tooltip(Language.translate("before")));
        
        interactivityButton = new Button(Language.translate("interactivity"));
        interactivityButton.setId("interactivity-button");
        interactivityButton.setTooltip(new Tooltip(Language.translate("interactivity-button")));
        
        Label icon = new Label();
		icon.setId("zoom-icon");
        zoomButton = new SliderButton(0.0, 100.0, 50.0, 150.0, icon, false);
        showAnchorsLinksButton = new CheckBox(Language.translate("show.relations"));
       
        otherButtonPane = new HBox();
        otherButtonPane.setId("other-button-pane");
        otherButtonPane.setFillHeight(false);
        otherButtonPane.getChildren().add(interactivityButton);
        //otherButtonPane.getChildren().add(zoomButton);
        //otherButtonPane.getChildren().add(showAnchorsLinksButton);
        zoomButton.setDisable(true);
        showAnchorsLinksButton.setDisable(true);
	       
        alignmentButtonPane = new HBox();
        alignmentButtonPane.setId("alignment-pane");
        alignmentButtonPane.getChildren().add(equalsButton);
        alignmentButtonPane.getChildren().add(startsButton);
        alignmentButtonPane.getChildren().add(startsDelayButton);
        alignmentButtonPane.getChildren().add(finishesButton);
        alignmentButtonPane.getChildren().add(finishesDelayButton);
        alignmentButtonPane.getChildren().add(meetsButton);
        alignmentButtonPane.getChildren().add(meetsDelayButton);
        alignmentButtonPane.getChildren().add(metByButton);
        alignmentButtonPane.getChildren().add(metByDelayButton);
        alignmentButtonPane.getChildren().add(duringButton);
        alignmentButtonPane.getChildren().add(overlapsButton);
        alignmentButtonPane.getChildren().add(beforeButton);
        
	}
	
	private Boolean masterAndSlaveHaveBeenDefined(){
		
		ArrayList<Media> selectedSlaveMediaList = new ArrayList<Media>();
		
		for(Media media : temporalViewPane.getSelectedMediaList()){
    		
    		if(media != temporalViewPane.getFirstSelectedMedia()){
    			
    			selectedSlaveMediaList.add(media);

    		}
    		
    	}
		
		if((temporalViewPane.getFirstSelectedMedia() == null) && (selectedSlaveMediaList.isEmpty())){
			
			MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
					Language.translate("please.select.a.media.to.be.the.master.and.at.least.one.to.be.slave"), "OK", 190);
			messageDialog.showAndWait();
			
			return false;
			
    	}else if(temporalViewPane.getFirstSelectedMedia() == null){
    		
    		MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
					Language.translate("please.select.a.media.to.be.the.master"), "OK", 190);
			messageDialog.showAndWait();
			
			return false;
    		
    	}else if(selectedSlaveMediaList.isEmpty()){
    		
    		MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
					Language.translate("please.select.at.least.one.media.to.be.slave"), "OK", 190);
			messageDialog.showAndWait();
			
    		return false;
    		
    	}
		
		return true;
		
	}
	
	private void createButtonActions(TabPane temporalChainTabPane){
		
		equalsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				if(masterAndSlaveHaveBeenDefined()){
					
					startsButton.fire();
					finishesButton.fire();
					
				}

			}
			
		});
		
		startsButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    public void handle(ActionEvent t) {

		    	if(masterAndSlaveHaveBeenDefined()){
		    		
		    		Media relationMasterMedia = temporalViewPane.getFirstSelectedMedia();
			    	
			    	Synchronous<Media> synchronousRelation = new Synchronous<Media>();
	    			synchronousRelation.setType(TemporalRelationType.STARTS);
	    			synchronousRelation.setMasterMedia(relationMasterMedia);
	    			synchronousRelation.setExplicit(true);
		
			    	for(Media media : temporalViewPane.getSelectedMediaList()){
			    		
			    		if(media != relationMasterMedia){
			    			
			    			synchronousRelation.addSlaveMedia(media);

			    		}
			    		
			    	}
			    	
			    	addSynchronousRelationToModel(synchronousRelation);
		    		
		    	}

		    }
		    
		});
		
		startsDelayButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    public void handle(ActionEvent t) {
		    	
		    	if(masterAndSlaveHaveBeenDefined()){
		    		
		    		Double delay = showDelayInputDialog();
			    	
			    	if(delay == null){
			    		return;
			    	}

			    	Media relationMasterMedia = temporalViewPane.getFirstSelectedMedia();
			    	
			    	Synchronous<Media> synchronousRelation = new Synchronous<Media>();
	    			synchronousRelation.setType(TemporalRelationType.STARTS_DELAY);
	    			synchronousRelation.setMasterMedia(relationMasterMedia);
	    			synchronousRelation.setDelay(delay);
	    			synchronousRelation.setExplicit(true);
		
			    	for(Media media : temporalViewPane.getSelectedMediaList()){
			    		
			    		if(media != relationMasterMedia){
			    			
			    			synchronousRelation.addSlaveMedia(media);
			    			
			    		}
			    		
			    	}
			    	
			    	addSynchronousRelationToModel(synchronousRelation);
		    		
		    	}
		    	
		    }
		    
		});
		
		finishesButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				if(masterAndSlaveHaveBeenDefined()){
					
					Media relationMasterMedia = temporalViewPane.getFirstSelectedMedia();
			    	
			    	Synchronous<Media> synchronousRelation = new Synchronous<Media>();
	    			synchronousRelation.setType(TemporalRelationType.FINISHES);
	    			synchronousRelation.setMasterMedia(relationMasterMedia);
	    			synchronousRelation.setExplicit(true);
		
			    	for(Media media : temporalViewPane.getSelectedMediaList()){
			    		
			    		if(media != relationMasterMedia){
			    			
			    			synchronousRelation.addSlaveMedia(media);
			    			
			    		}
			    		
			    	}
			    	
			    	addSynchronousRelationToModel(synchronousRelation);
					
				}
				
			}
			
		});
		
		finishesDelayButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				if(masterAndSlaveHaveBeenDefined()){
					
					Double delay = showDelayInputDialog();
			    	
			    	if(delay == null){
			    		return;
			    	}

					Media relationMasterMedia = temporalViewPane.getFirstSelectedMedia();
			    	
			    	Synchronous<Media> synchronousRelation = new Synchronous<Media>();
	    			synchronousRelation.setType(TemporalRelationType.FINISHES_DELAY);
	    			synchronousRelation.setMasterMedia(relationMasterMedia);
	    			synchronousRelation.setDelay(delay);
	    			synchronousRelation.setExplicit(true);
		
			    	for(Media media : temporalViewPane.getSelectedMediaList()){
			    		
			    		if(media != relationMasterMedia){
			    			
			    			synchronousRelation.addSlaveMedia(media);
			    			
			    		}
			    		
			    	}
			    	
			    	addSynchronousRelationToModel(synchronousRelation);
					
				}

			}
			
		});
		
		meetsButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    public void handle(ActionEvent t) {
		    	
		    	if(masterAndSlaveHaveBeenDefined()){
		    		
		    		Media relationMasterMedia = temporalViewPane.getFirstSelectedMedia();
			    	
			    	Synchronous<Media> synchronousRelation = new Synchronous<Media>();
	    			synchronousRelation.setType(TemporalRelationType.MEETS);
	    			synchronousRelation.setMasterMedia(relationMasterMedia);
	    			synchronousRelation.setExplicit(true);
		
			    	for(Media media : temporalViewPane.getSelectedMediaList()){
			    		
			    		if(media != relationMasterMedia){
			    			
			    			synchronousRelation.addSlaveMedia(media);
			    			
			    		}
			    		
			    	}	
			    	
			    	addSynchronousRelationToModel(synchronousRelation);
			    	
		    	}

		    }
		    
		});
		
		meetsDelayButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    public void handle(ActionEvent t) {
		    	
		    	if(masterAndSlaveHaveBeenDefined()){
		    		
		        	Double delay = showDelayInputDialog();
			    	
			    	if(delay == null){
			    		return;
			    	}
			    	
			    	Media relationMasterMedia = temporalViewPane.getFirstSelectedMedia();
			    	
			    	Synchronous<Media> synchronousRelation = new Synchronous<Media>();
	    			synchronousRelation.setType(TemporalRelationType.MEETS_DELAY);
	    			synchronousRelation.setMasterMedia(relationMasterMedia);
	    			synchronousRelation.setDelay(delay);
	    			synchronousRelation.setExplicit(true);
		
			    	for(Media media : temporalViewPane.getSelectedMediaList()){
			    		
			    		if(media != relationMasterMedia){
			    			
			    			synchronousRelation.addSlaveMedia(media);
			    			
			    		}
			    		
			    	}
			    	
			    	addSynchronousRelationToModel(synchronousRelation);
		    		
		    	}

		    }
		    
		});
		
		
		metByButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    public void handle(ActionEvent t) {
		    	
		    	if(masterAndSlaveHaveBeenDefined()){
		    		
		    		Media relationMasterMedia = temporalViewPane.getFirstSelectedMedia();
			    	
			    	Synchronous<Media> synchronousRelation = new Synchronous<Media>();
	    			synchronousRelation.setType(TemporalRelationType.MET_BY);
	    			synchronousRelation.setMasterMedia(relationMasterMedia);
	    			synchronousRelation.setExplicit(true);
		
			    	for(Media media : temporalViewPane.getSelectedMediaList()){
			    		
			    		if(media != relationMasterMedia){
			    			
			    			synchronousRelation.addSlaveMedia(media);
			    			
			    		}
			    		
			    	}
			    	
			    	addSynchronousRelationToModel(synchronousRelation);
		    		
		    	}
		    	
		    }
  
		});
		
		metByDelayButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    public void handle(ActionEvent t) {
		    	
		    	if(masterAndSlaveHaveBeenDefined()){
		    		
		    		Double delay = showDelayInputDialog();
			    	
			    	if(delay == null){
			    		return;
			    	}
			    	
			    	Media relationMasterMedia = temporalViewPane.getFirstSelectedMedia();
			    	
			    	Synchronous<Media> synchronousRelation = new Synchronous<Media>();
	    			synchronousRelation.setType(TemporalRelationType.MET_BY_DELAY);
	    			synchronousRelation.setMasterMedia(relationMasterMedia);
	    			synchronousRelation.setDelay(delay);
	    			synchronousRelation.setExplicit(true);
		
			    	for(Media media : temporalViewPane.getSelectedMediaList()){
			    		
			    		if(media != relationMasterMedia){
			    			
			    			synchronousRelation.addSlaveMedia(media);
			    			
			    		}
			    		
			    	}
			    	
			    	addSynchronousRelationToModel(synchronousRelation);
		    		
		    	}

		    }
  
		});
		
		duringButton.setOnAction(new EventHandler<ActionEvent>() {
			
			 public void handle(ActionEvent t) {
				
				 if(masterAndSlaveHaveBeenDefined()){
					 
						Media relationMasterMedia = temporalViewPane.getFirstSelectedMedia();
						 
						Double startDelay = showInputDialogOfFirstDelay();
					    	
				    	if(startDelay == null){
				    		return;
				    	}
					    	
						Double secondDelay = showInputDialogOfSecondDelay();
						Double metByDelay;
						if(secondDelay == null){
				    		return;
				    	}else {
				    		metByDelay =  relationMasterMedia.getDuration() - secondDelay;
				    	}

				    	Synchronous<Media> synchronousRelation = new Synchronous<Media>();
		    			synchronousRelation.setType(TemporalRelationType.MET_BY_DELAY);
		    			synchronousRelation.setMasterMedia(relationMasterMedia);
		    			synchronousRelation.setDelay(metByDelay);
		    			synchronousRelation.setExplicit(true);
			
				    	for(Media media : temporalViewPane.getSelectedMediaList()){
				    		
				    		if(media != relationMasterMedia){
				    			
				    			synchronousRelation.addSlaveMedia(media);
				    			
				    		}
				    		
				    	}
				    	
				    	addSynchronousRelationToModel(synchronousRelation);

				    	Synchronous<Media> startDelayRelation = new Synchronous<Media>();
		    			startDelayRelation.setType(TemporalRelationType.STARTS_DELAY);
		    			startDelayRelation.setMasterMedia(relationMasterMedia);
		    			startDelayRelation.setDelay(startDelay);
		    			startDelayRelation.setExplicit(true);
			
				    	for(Media media : temporalViewPane.getSelectedMediaList()){
				    		
				    		if(media != relationMasterMedia){
				    			
				    			startDelayRelation.addSlaveMedia(media);
				    			
				    		}
				    		
				    	}
				    	
				    	addSynchronousRelationToModel(startDelayRelation);
					 
				 }
				 
			 }
			 
		});
		
		overlapsButton.setOnAction(new EventHandler<ActionEvent>() {
			
			 public void handle(ActionEvent t) {
				 
				 if(masterAndSlaveHaveBeenDefined()){
					 
					 startsDelayButton.fire();
					 finishesDelayButton.fire();
					 
				 }
				 
			 }
			 
		});
		
		beforeButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    public void handle(ActionEvent t) {
		    	
		    	if(masterAndSlaveHaveBeenDefined()){
		    		
		    		Double delay = showDelayInputDialog();
			    	
			    	if(delay == null){
			    		return;
			    	}
			    	
			    	Media relationMasterMedia = temporalViewPane.getFirstSelectedMedia();
			    	
			    	Synchronous<Media> synchronousRelation = new Synchronous<Media>();
	    			synchronousRelation.setType(TemporalRelationType.BEFORE);
	    			synchronousRelation.setMasterMedia(relationMasterMedia);
	    			synchronousRelation.setDelay(delay);
	    			synchronousRelation.setExplicit(true);
		
			    	for(Media media : temporalViewPane.getSelectedMediaList()){
			    		
			    		if(media != relationMasterMedia){
			    			
			    			synchronousRelation.addSlaveMedia(media);
			    			
			    		}
			    		
			    	}
			    	
			    	addSynchronousRelationToModel(synchronousRelation);
		    		
		    	}

		    }
		    
		});
		
		interactivityButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    public void handle(ActionEvent t) {
		    	
		    	Media firstSelectedMedia = temporalViewPane.getFirstSelectedMedia();
		    	
		    	if(firstSelectedMedia != null){
		    		
		    		if(!firstSelectedMedia.isInteractive()){
		    			
		    			InteractiveMediaWindow interactiveMediaWindow;
			    		ArrayList<Media> mediaListDuringInteractivityTime = temporalViewPane.getMediaListDuringInteractivityTime();
			    
				    	interactiveMediaWindow = new InteractiveMediaWindow(controller, temporalViewPane, firstSelectedMedia, mediaListDuringInteractivityTime);
			    		
				    	interactiveMediaWindow.showAndWait();
				    	
		    		}else {//TODO remover quando suportar diversas relações de interatividade
		    			MessageDialog messageDialog = new MessageDialog(Language.translate("interactivity.relation.has.already.been.defined.for.this.media"), 
								Language.translate("please.select.another.media.in.the.timeline.to.define.a.new.interactivity"), "OK", 190);
						messageDialog.showAndWait();
		    		}
			    	
		    	}else{
		    		
		    		MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.a.new.interactive.media"), 
							Language.translate("please.select.a.media.in.the.timeline"), "OK", 190);
					messageDialog.showAndWait();
		    		
		    	}

		    }
		    
		});
		
		zoomButton.getSlider().valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
//            	ScrollPane teste = (ScrollPane) temporalChainTabPane.getTabs().get(0).getContent();
//            	TemporalChainPane teste2 = (TemporalChainPane) teste.getContent();
//            	((NumberAxis) teste2.getXAxis()).setUpperBound(25);
//            	//new_val.doubleValue()
            }
        });
		
	}
	
	private void addSynchronousRelationToModel(Synchronous<Media> synchronousRelation) {
		
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
    	
		controller.addSynchronousRelation(temporalChainPane.getTemporalChainModel(), synchronousRelation);
	}
	
	private Double showDelayInputDialog() {
		
		InputDialog inputDialog = new InputDialog(Language.translate("type.delay"),null, Language.translate("cancel"),"ok", Language.translate("delay.in.seconds") + ": ", 210);
    	String input = inputDialog.showAndWaitAndReturn();
    	Double delay;
    	
    	if(input == null || input.equals("left") || input.equals("close")){
    		return null;
    	}else if(input.isEmpty()){
    		delay = 0.0;
    	}else {
    		delay = Double.valueOf(input);
    	}
		return delay;
		
	}
	
	private Double showInputDialogOfFirstDelay(){
		
		InputDialog inputDialog = new InputDialog(Language.translate("type.first.delay"),null, Language.translate("cancel"),"ok", Language.translate("type.first.delay") + ": ", 210);
    	String input = inputDialog.showAndWaitAndReturn();
    	Double delay;
    	
    	if(input == null){
    		return null;
    	}else if(input.isEmpty()){
    		delay = 0.0;
    	}else {
    		delay = Double.valueOf(input);
    	}
		return delay;
		
	}
	
	private Double showInputDialogOfSecondDelay(){
		
		InputDialog inputDialog = new InputDialog(Language.translate("type.second.delay"),null, Language.translate("cancel"),"ok", Language.translate("type.first.delay") + ": ", 210);
    	String input = inputDialog.showAndWaitAndReturn();
    	Double delay;
    	
    	if(input == null){
    		return null;
    	}else if(input.isEmpty()){
    		delay = 0.0;
    	}else {
    		delay = Double.valueOf(input);
    	}
		return delay;
		
	}
	
	public Boolean isShowAnchorsLinksButtonSelected(){
		return showAnchorsLinksButton.isSelected();
	}
	
}
