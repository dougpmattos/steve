package view.temporalViewPane;

import java.util.ArrayList;

import javafx.application.Platform;
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
import model.common.MediaNode;
import model.common.Node;
import model.repository.RepositoryMediaList;
import model.temporalView.Synchronous;
import model.temporalView.TemporalChain;
import model.temporalView.TemporalRelation;
import model.temporalView.enums.TemporalRelationType;
import view.common.dialogs.InputDialog;
import view.common.Language;
import view.common.dialogs.MessageDialog;
import view.common.customComponents.SliderButton;
import controller.ApplicationController;

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
	private CheckBox showNodesLinkedCheckBox;
	private HBox alignmentButtonPane;
	private TemporalViewPane temporalViewPane;
	private RepositoryMediaList repositoryMediaList;
	private ApplicationController applicationController;
	
	public TemporalViewButtonPane(ApplicationController applicationController, TabPane temporalChainTabPane, TemporalViewPane temporalViewPane, RepositoryMediaList repositoryMediaList){
		
		this.applicationController = applicationController;
		this.temporalViewPane = temporalViewPane;
		this.repositoryMediaList = repositoryMediaList;
		
		setId("button-pane");
	    getStylesheets().add("styles/temporalViewPane/temporalViewButtonPane.css");
	    
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
        showNodesLinkedCheckBox = new CheckBox(Language.translate("show.nodes.linked"));
       
        otherButtonPane = new HBox();
        otherButtonPane.setId("other-button-pane");
        otherButtonPane.setFillHeight(false);
        //otherButtonPane.getChildren().add(zoomButton);
        otherButtonPane.getChildren().add(showNodesLinkedCheckBox);
        zoomButton.setDisable(true);
		showNodesLinkedCheckBox.setDisable(true);
        showNodesLinkedCheckBox.setSelected(false);
	       
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
        alignmentButtonPane.getChildren().add(otherButtonPane);
        
	}
	
	private Boolean masterAndSecondaryHaveBeenDefined(){
		
		ArrayList<Node> selectedSecondaryNodeList = new ArrayList<Node>();
		
		for(Node node : temporalViewPane.getSelectedNodeList()){
    		
    		if(node != temporalViewPane.getFirstSelectedNode()){
    			
    			selectedSecondaryNodeList.add(node);

    		}
    		
    	}
		
		if((temporalViewPane.getFirstSelectedNode() == null) && (selectedSecondaryNodeList.isEmpty())){
			
			MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
					Language.translate("please.select.a.media.to.be.the.primary.and.at.least.one.to.be.secondary"), "OK", 230);
			messageDialog.showAndWait();
			
			return false;
			
    	}else if(temporalViewPane.getFirstSelectedNode() == null){

			MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"),
					Language.translate("please.select.a.media.to.be.the.primary"), "OK", 220);
			messageDialog.showAndWait();

			return false;
    		
    	}else if(selectedSecondaryNodeList.isEmpty()){
    		
    		MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
					Language.translate("please.select.at.least.one.media.to.be.secondary"), "OK", 220);
			messageDialog.showAndWait();
			
    		return false;
    		
    	}
		
		return true;
		
	}
	
	private void createButtonActions(TabPane temporalChainTabPane){
		
		equalsButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				startsButton.fire();
				finishesButton.fire();

			}
			
		});
		
		startsButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    public void handle(ActionEvent t) {

		    	if(masterAndSecondaryHaveBeenDefined()){
		    		
		    		Node relationMasterNode = temporalViewPane.getFirstSelectedNode();
			    	
			    	Synchronous synchronousRelation = new Synchronous();
	    			synchronousRelation.setType(TemporalRelationType.STARTS);
	    			synchronousRelation.setPrimaryNode(relationMasterNode);
	    			synchronousRelation.setExplicit(true);
		
			    	for(Node node : temporalViewPane.getSelectedNodeList()){
			    		
			    		if(node != relationMasterNode){
			    			
			    			synchronousRelation.addSecondaryNode(node);

			    		}
			    		
			    	}
			    	
			    	addSynchronousRelationToModel(synchronousRelation);
		    		
		    	}

		    }
		    
		});
		
		startsDelayButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    public void handle(ActionEvent t) {
		    	
		    	if(masterAndSecondaryHaveBeenDefined()){
		    		
		    		Double delay = showDelayInputDialog();
			    	
			    	if(delay == null){
			    		return;
			    	}

			    	Node relationMasterNode = temporalViewPane.getFirstSelectedNode();
			    	
			    	Synchronous synchronousRelation = new Synchronous();
	    			synchronousRelation.setType(TemporalRelationType.STARTS_DELAY);
	    			synchronousRelation.setPrimaryNode(relationMasterNode);
	    			synchronousRelation.setDelay(delay);
	    			synchronousRelation.setExplicit(true);
		
			    	for(Node node : temporalViewPane.getSelectedNodeList()){
			    		
			    		if(node != relationMasterNode){
			    			
			    			synchronousRelation.addSecondaryNode(node);
			    			
			    		}
			    		
			    	}
			    	
			    	addSynchronousRelationToModel(synchronousRelation);
		    		
		    	}
		    	
		    }
		    
		});
		
		finishesButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				if(masterAndSecondaryHaveBeenDefined()){
					
					Node relationMasterNode = temporalViewPane.getFirstSelectedNode();
			    	
			    	Synchronous synchronousRelation = new Synchronous();
	    			synchronousRelation.setType(TemporalRelationType.FINISHES);
	    			synchronousRelation.setPrimaryNode(relationMasterNode);
	    			synchronousRelation.setExplicit(true);
		
			    	for(Node node : temporalViewPane.getSelectedNodeList()){
			    		
			    		if(node != relationMasterNode){
			    			
			    			synchronousRelation.addSecondaryNode(node);
			    			
			    		}
			    		
			    	}
			    	
			    	addSynchronousRelationToModel(synchronousRelation);
					
				}
				
			}
			
		});
		
		finishesDelayButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				if(masterAndSecondaryHaveBeenDefined()){
					
					Double delay = showDelayInputDialog();
			    	
			    	if(delay == null){
			    		return;
			    	}

					Node relationMasterNode = temporalViewPane.getFirstSelectedNode();
			    	
			    	Synchronous synchronousRelation = new Synchronous();
	    			synchronousRelation.setType(TemporalRelationType.FINISHES_DELAY);
	    			synchronousRelation.setPrimaryNode(relationMasterNode);
	    			synchronousRelation.setDelay(delay);
	    			synchronousRelation.setExplicit(true);
		
			    	for(Node node : temporalViewPane.getSelectedNodeList()){
			    		
			    		if(node != relationMasterNode){
			    			
			    			synchronousRelation.addSecondaryNode(node);
			    			
			    		}
			    		
			    	}
			    	
			    	addSynchronousRelationToModel(synchronousRelation);
					
				}

			}
			
		});
		
		meetsButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    public void handle(ActionEvent t) {
		    	
		    	if(masterAndSecondaryHaveBeenDefined()){
		    		
		    		Node relationMasterNode = temporalViewPane.getFirstSelectedNode();
			    	
			    	Synchronous synchronousRelation = new Synchronous();
	    			synchronousRelation.setType(TemporalRelationType.MEETS);
	    			synchronousRelation.setPrimaryNode(relationMasterNode);
	    			synchronousRelation.setExplicit(true);
		
			    	for(Node node : temporalViewPane.getSelectedNodeList()){
			    		
			    		if(node != relationMasterNode){
			    			
			    			synchronousRelation.addSecondaryNode(node);
			    			
			    		}
			    		
			    	}	
			    	
			    	addSynchronousRelationToModel(synchronousRelation);
			    	
		    	}

		    }
		    
		});
		
		meetsDelayButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    public void handle(ActionEvent t) {
		    	
		    	if(masterAndSecondaryHaveBeenDefined()){
		    		
		        	Double delay = showDelayInputDialog();
			    	
			    	if(delay == null){
			    		return;
			    	}
			    	
			    	Node relationMasterNode = temporalViewPane.getFirstSelectedNode();
			    	
			    	Synchronous synchronousRelation = new Synchronous();
	    			synchronousRelation.setType(TemporalRelationType.MEETS_DELAY);
	    			synchronousRelation.setPrimaryNode(relationMasterNode);
	    			synchronousRelation.setDelay(delay);
	    			synchronousRelation.setExplicit(true);
		
			    	for(Node node : temporalViewPane.getSelectedNodeList()){
			    		
			    		if(node != relationMasterNode){
			    			
			    			synchronousRelation.addSecondaryNode(node);
			    			
			    		}
			    		
			    	}
			    	
			    	addSynchronousRelationToModel(synchronousRelation);
		    		
		    	}

		    }
		    
		});
		
		
		metByButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    public void handle(ActionEvent t) {
		    	
		    	if(masterAndSecondaryHaveBeenDefined()){
		    		
		    		Node relationMasterNode = temporalViewPane.getFirstSelectedNode();
			    	
			    	Synchronous synchronousRelation = new Synchronous();
	    			synchronousRelation.setType(TemporalRelationType.MET_BY);
	    			synchronousRelation.setPrimaryNode(relationMasterNode);
	    			synchronousRelation.setExplicit(true);
		
			    	for(Node node : temporalViewPane.getSelectedNodeList()){
			    		
			    		if(node != relationMasterNode){
			    			
			    			synchronousRelation.addSecondaryNode(node);
			    			
			    		}
			    		
			    	}
			    	
			    	addSynchronousRelationToModel(synchronousRelation);
		    		
		    	}
		    	
		    }
  
		});
		
		metByDelayButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    public void handle(ActionEvent t) {
		    	
		    	if(masterAndSecondaryHaveBeenDefined()){
		    		
		    		Double delay = showDelayInputDialog();
			    	
			    	if(delay == null){
			    		return;
			    	}
			    	
			    	Node relationMasterNode = temporalViewPane.getFirstSelectedNode();
			    	
			    	Synchronous synchronousRelation = new Synchronous();
	    			synchronousRelation.setType(TemporalRelationType.MET_BY_DELAY);
	    			synchronousRelation.setPrimaryNode(relationMasterNode);
	    			synchronousRelation.setDelay(delay);
	    			synchronousRelation.setExplicit(true);
		
			    	for(Node node : temporalViewPane.getSelectedNodeList()){
			    		
			    		if(node != relationMasterNode){
			    			
			    			synchronousRelation.addSecondaryNode(node);
			    			
			    		}
			    		
			    	}
			    	
			    	addSynchronousRelationToModel(synchronousRelation);
		    		
		    	}

		    }
  
		});
		
		duringButton.setOnAction(new EventHandler<ActionEvent>() {
			
			 public void handle(ActionEvent t) {
				
				 if(masterAndSecondaryHaveBeenDefined()){
					 
						Node relationMasterNode = temporalViewPane.getFirstSelectedNode();
						 
						Double startDelay = showInputDialogOfFirstDelay();
					    	
				    	if(startDelay == null){
				    		return;
				    	}
					    	
						Double secondDelay = showInputDialogOfSecondDelay();
						Double metByDelay;
						if(secondDelay == null){
				    		return;
				    	}else {
				    		metByDelay =  relationMasterNode.getDuration() - secondDelay;
				    	}

				    	Synchronous synchronousRelation = new Synchronous();
		    			synchronousRelation.setType(TemporalRelationType.MET_BY_DELAY);
		    			synchronousRelation.setPrimaryNode(relationMasterNode);
		    			synchronousRelation.setDelay(metByDelay);
		    			synchronousRelation.setExplicit(true);
			
				    	for(Node node : temporalViewPane.getSelectedNodeList()){
				    		
				    		if(node != relationMasterNode){
				    			
				    			synchronousRelation.addSecondaryNode(node);
				    			
				    		}
				    		
				    	}
				    	
				    	addSynchronousRelationToModel(synchronousRelation);

				    	Synchronous startDelayRelation = new Synchronous();
		    			startDelayRelation.setType(TemporalRelationType.STARTS_DELAY);
		    			startDelayRelation.setPrimaryNode(relationMasterNode);
		    			startDelayRelation.setDelay(startDelay);
		    			startDelayRelation.setExplicit(true);
			
				    	for(Node node : temporalViewPane.getSelectedNodeList()){
				    		
				    		if(node != relationMasterNode){
				    			
				    			startDelayRelation.addSecondaryNode(node);
				    			
				    		}
				    		
				    	}
				    	
				    	addSynchronousRelationToModel(startDelayRelation);
					 
				 }
				 
			 }
			 
		});
		
		overlapsButton.setOnAction(new EventHandler<ActionEvent>() {
			
			 public void handle(ActionEvent t) {
				 
				 if(masterAndSecondaryHaveBeenDefined()){
					 
					 startsDelayButton.fire();
					 finishesDelayButton.fire();
					 
				 }
				 
			 }
			 
		});
		
		beforeButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    public void handle(ActionEvent t) {
		    	
		    	if(masterAndSecondaryHaveBeenDefined()){
		    		
		    		Double delay = showDelayInputDialog();
			    	
			    	if(delay == null){
			    		return;
			    	}
			    	
			    	Node relationMasterNode = temporalViewPane.getFirstSelectedNode();
			    	
			    	Synchronous synchronousRelation = new Synchronous();
	    			synchronousRelation.setType(TemporalRelationType.BEFORE);
	    			synchronousRelation.setPrimaryNode(relationMasterNode);
	    			synchronousRelation.setDelay(delay);
	    			synchronousRelation.setExplicit(true);
		
			    	for(Node node : temporalViewPane.getSelectedNodeList()){
			    		
			    		if(node != relationMasterNode){
			    			
			    			synchronousRelation.addSecondaryNode(node);
			    			
			    		}
			    		
			    	}
			    	
			    	addSynchronousRelationToModel(synchronousRelation);
		    		
		    	}

		    }
		    
		});
		
		interactivityButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    public void handle(ActionEvent t) {
		    	
		    	Node firstSelectedNode = temporalViewPane.getFirstSelectedNode();
		    	
		    	if(firstSelectedNode != null){
		    		
		    		if(!firstSelectedNode.isInteractive()){
		    			
		    			InteractiveMediaWindow interactiveMediaWindow;
			    		ArrayList<Node> nodeListDuringInteractivityTime = temporalViewPane.getNodeListDuringInteractivityTime();
			    
				    	interactiveMediaWindow = new InteractiveMediaWindow(applicationController, temporalViewPane, (MediaNode) firstSelectedNode, nodeListDuringInteractivityTime);
			    		
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
		
		showNodesLinkedCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue){
					showNodesLinked();
				}else{
					hideNodesLinked();
				}
			}
		});

	}

	private void showNodesLinked() {

		javafx.scene.Node content = applicationController.getSteveScene().getTemporalViewPane().getTemporalChainTabPane().getSelectionModel().getSelectedItem().getContent();
		TemporalChainPane temporalChainPane = (TemporalChainPane) content;
		TemporalChain temporalChainModel = temporalChainPane.getTemporalChainModel();

		ArrayList<Node> nodeList = temporalChainModel.getNodeAllList();

		for(Node node : nodeList){

			ArrayList<TemporalRelation> listOfRelationsWhereNodeIsMaster = temporalChainModel.getListOfMasterRelations(node);
			ArrayList<TemporalRelation> listOfRelationsWhereNodeIsSecondary = temporalChainModel.getListOfRelationsWhereNodeIsSecondary(node);

			boolean isPrimary = false;
			boolean isSecondary = false;

			if(!listOfRelationsWhereNodeIsMaster.isEmpty()){
				isPrimary = true;
			}
			if(!listOfRelationsWhereNodeIsSecondary.isEmpty()){
				isSecondary = true;

			}

			HBox containerNode = node.getContainerNode();
			containerNode.getStylesheets().clear();
			if(containerNode != null){
				if(isPrimary && isSecondary){
					containerNode.getStylesheets().add("styles/temporalViewPane/primarySecondaryNodes.css");
				}else if(isPrimary){
					containerNode.getStylesheets().add("styles/temporalViewPane/primaryNodes.css");
				}else if(isSecondary){
					containerNode.getStylesheets().add("styles/temporalViewPane/secondaryNodes.css");
				}
			}

		}

	}

	private void hideNodesLinked() {

		javafx.scene.Node content = applicationController.getSteveScene().getTemporalViewPane().getTemporalChainTabPane().getSelectionModel().getSelectedItem().getContent();
		TemporalChainPane temporalChainPane = (TemporalChainPane) content;
		TemporalChain temporalChainModel = temporalChainPane.getTemporalChainModel();

		ArrayList<Node> nodeList = temporalChainModel.getNodeAllList();

		for(Node node : nodeList){
			HBox containerNode = node.getContainerNode();
			if(containerNode != null){
				containerNode.getStylesheets().remove("styles/temporalViewPane/primarySecondaryNodes.css");
				containerNode.getStylesheets().remove("styles/temporalViewPane/primaryNodes.css");
				containerNode.getStylesheets().remove("styles/temporalViewPane/secondaryNodes.css");
			}

		}

	}

	private void addSynchronousRelationToModel(Synchronous synchronousRelation) {
		
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
    	
		applicationController.addSynchronousRelation(temporalChainPane.getTemporalChainModel(), synchronousRelation);
    	showNodesLinkedCheckBox.setDisable(false);
	}
	
	private Double showDelayInputDialog() {
		
		InputDialog inputDialog = new InputDialog(Language.translate("type.delay"),null,
				Language.translate("cancel"),"ok",
				Language.translate("delay.in.seconds") + ": ", 180);
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

	public CheckBox getShowNodesLinkedCheckBox() {
		return showNodesLinkedCheckBox;
	}

}
