package view.temporalViewPane;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import model.common.MediaNode;
import model.common.Node;
import model.common.SpatialTemporalApplication;
import model.repository.RepositoryMediaList;
import model.temporalView.Interactivity;
import model.temporalView.TemporalChain;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.Operation;
import view.common.Language;
import view.repositoryPane.RepositoryPane;
import view.sensoryEffectsPane.SensoryEffectsPane;
import view.stevePane.SteveScene;
import controller.ApplicationController;

@SuppressWarnings("unchecked")
public class TemporalViewPane extends BorderPane implements Observer, view.common.Observable{

	private ApplicationController applicationController;
	
	private SpatialTemporalApplication spatialTemporalApplication;
	
	private TabPane temporalChainTabPane;
	private TemporalViewButtonPane temporalViewButtonPane;
	private RepositoryPane repositoryPane;
	private RepositoryMediaList repositoryMediaList;
	private ArrayList<view.common.Observer> observers;
	private Node firstSelectedNode;
	private ArrayList<Node> selectedNodeList = new ArrayList<Node>();
	private SteveScene steveScene;
	private StackPane tabAddButtonContainer;
	private Button tabAddButton;
	private SensoryEffectsPane sensoryEffectsPane;

	public TemporalViewPane(ApplicationController applicationController, SpatialTemporalApplication spatialTemporalApplication, RepositoryPane repositoryPane, SteveScene steveScene, RepositoryMediaList repositoryMediaList){
		
		setId("temporal-view-pane");
		getStylesheets().add("styles/temporalViewPane/temporalViewPane.css");

		this.spatialTemporalApplication = spatialTemporalApplication;
		this.repositoryPane = repositoryPane;
		this.repositoryMediaList = repositoryMediaList;
		this.steveScene = steveScene;
		
		sensoryEffectsPane = new SensoryEffectsPane(applicationController, temporalChainTabPane, this, repositoryMediaList);
		temporalViewButtonPane = new TemporalViewButtonPane(applicationController, temporalChainTabPane, this, repositoryMediaList);
      	
		createTemporalChainTabPane();

		setTop(sensoryEffectsPane);
	    setCenter(tabAddButtonContainer);
	    setBottom(temporalViewButtonPane);

	    observers = new ArrayList<view.common.Observer>();
	    
	    spatialTemporalApplication.addObserver(this);
	    
	    this.applicationController = applicationController;
	    
	}

	private void createTemporalChainTabPane() {
		
		temporalChainTabPane = new TabPane();
		tabAddButtonContainer = new StackPane();
		tabAddButtonContainer.setAlignment(Pos.TOP_LEFT);
		
		tabAddButtonContainer.getChildren().addAll(temporalChainTabPane);
		
		tabAddButton = new Button();
		tabAddButton.setId("tab-add-button");
		tabAddButton.setTooltip(new Tooltip(Language.translate("create.new.temporal.chain")));
		
		tabAddButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				applicationController.addTemporalChain(new TemporalChain(Language.translate("temporal.chain") + " " + (temporalChainTabPane.getTabs().size() + 1)));
				
			}
			
		});
		
		tabAddButtonContainer.getChildren().add(tabAddButton);

	}

	public void addTemporalChainPane(TemporalChain temporalChainModel) {
		
		TemporalChainPane temporalChainPane = new TemporalChainPane(applicationController, spatialTemporalApplication, temporalChainModel, this, repositoryPane, steveScene);
		temporalChainModel.addObserver(this);

		Tab newTemporalChainTab = new Tab();
		newTemporalChainTab.setText(temporalChainModel.getName());
		newTemporalChainTab.setContent(temporalChainPane);
		temporalChainPane.setParentTab(newTemporalChainTab);
		temporalChainTabPane.getTabs().add(newTemporalChainTab);
		
		if(temporalChainTabPane.getTabs().size() == 1){
			newTemporalChainTab.setClosable(false);
		}

		Double positionX = temporalChainTabPane.getTabMinWidth()*temporalChainTabPane.getTabs().size() + temporalChainTabPane.getTabs().size()*10;
		
		if(!(positionX + 40 > getWidth())){
			tabAddButton.setTranslateX(positionX);	
		}
	
		tabAddButton.setTranslateY(15);

		newTemporalChainTab.setOnCloseRequest(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {

				TemporalChainPane temporalChainPaneToBeRemoved = (TemporalChainPane) newTemporalChainTab.getContent();
				boolean isRemoved = applicationController.removeTemporalChain(temporalChainPaneToBeRemoved.getTemporalChainModel());

				if(isRemoved){

					Double positionX = temporalChainTabPane.getTabMinWidth()*temporalChainTabPane.getTabs().size() + temporalChainTabPane.getTabs().size()*10;

					if(!(positionX + 40 > getWidth())){
						tabAddButton.setTranslateX(positionX);
					}
				}else{
					event.consume();
				}

			}
		});
		newTemporalChainTab.setOnClosed(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {

				Double positionX = temporalChainTabPane.getTabMinWidth()*temporalChainTabPane.getTabs().size() + temporalChainTabPane.getTabs().size()*10;

				if(!(positionX + 40 > getWidth())){
					tabAddButton.setTranslateX(positionX);
				}

			}

		});
		
		newTemporalChainTab.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				
				clearSelectedNodeList();
				applicationController.getScreen().getChildren().clear();
				applicationController.getEffectIconsContainer().getChildren().clear();
				for(model.common.Node node : temporalChainModel.getNodeAllList()){
					node.setIsShownInPreview(false);
					node.setIsContinuousMediaPlaying(false);
				}
	        	
	        	for(Tab temporalTab : getTemporalChainTabPane().getTabs()){
	        		
	        		TemporalChainPane temporalChainPane = (TemporalChainPane) temporalTab.getContent();
	        		temporalChainPane.getParentTab().setStyle(null);
	        		
					for(ArrayList<TimeLineXYChartData> timeLineXYChartDataList : temporalChainPane.getTimeLineXYChartDataLineList()){
						for(TimeLineXYChartData timeLineXYChartData : timeLineXYChartDataList){
							boolean styleRemoved = false;
							if(timeLineXYChartData.getContainerNode().getStylesheets().remove("styles/temporalViewPane/mousePressedSecondaryTemporalMediaNode.css")){
								styleRemoved = true;
							}
							if(timeLineXYChartData.getContainerNode().getStylesheets().remove("styles/temporalViewPane/mousePressedTemporalMediaNode.css")){
								styleRemoved = true;
							}
						}
					}	
				 }
		
			}
			
		});

	}
	
	private void clearTemporalChainTabPane() {
		
		temporalChainTabPane.getTabs().clear();
		
	}
	
	@Override
	public void update(Observable observable, Object arg) {
		
		if(observable instanceof SpatialTemporalApplication){
			
			Operation<TemporalViewOperator> operation = (Operation<TemporalViewOperator>) arg;

			switch(operation.getOperator()){
			
		        case ADD_TEMPORAL_CHAIN:
		        	
		        	TemporalChain temporalChain = (TemporalChain) operation.getOperating();
		        	addTemporalChainPane(temporalChain);
		        	
		            break;
		            
		        case CLEAR_TEMPORAL_CHAIN_LIST:
		        	
		        	clearTemporalChainTabPane();
		        	
		        	break;
		        	
		        default:
		        	
		        	break;
		        	
			}
			
		}else if(observable instanceof TemporalChain){
			
			Operation<TemporalViewOperator> operation = (Operation<TemporalViewOperator>) arg;

			switch(operation.getOperator()){
			
		        case ADD_INTERACTIVITY_RELATION:
		        	
		        	Interactivity<MediaNode> interactivityRelation = (Interactivity<MediaNode>) operation.getOperating();
		        	
		        	for(Tab tab : getTemporalChainTabPane().getTabs()){
		        		TemporalChainPane temporalChainPane = (TemporalChainPane) tab.getContent();
		        		if(interactivityRelation.getTemporalChainList().contains(temporalChainPane.getTemporalChainModel())){
		        			temporalChainPane.getParentTab().setStyle("-fx-border-color: #00BFA5;-fx-border-width: 2; -fx-border-radius: 8; -fx-padding: -5, -5, -5, -5;");
		        		}
		        	}
		        	
		        	break;
			}
			
		}
		
	}

	public TabPane getTemporalChainTabPane (){
		return temporalChainTabPane;
	}
	
	@Override
	public void addObserver(view.common.Observer o) {
		observers.add(o);
	}

	@Override
	public void deleteObserver(view.common.Observer o) {
		
		int i = observers.indexOf(o);
		if(i >= 0){
			observers.remove(o);
		}
		
	}

	@Override
	public void notifyObservers(Object operator) {

		TemporalViewOperator temporalViewOperator = (TemporalViewOperator) operator;
		
		for(int i = 0; i < observers.size(); i++){
			view.common.Observer observer = (view.common.Observer) observers.get(i);
			observer.update(this, firstSelectedNode, temporalViewOperator);
		}
		
	}
	
	public void  addSelectedNode(Node selectedNode){
		
		selectedNodeList.add(selectedNode);
		
		if(selectedNodeList.size() == 1){
			
			this.firstSelectedNode = selectedNode;
			notifyObservers(TemporalViewOperator.SELECT_TEMPORAL_MEDIA);
			
		}
		
	}

	public void clearSelectedNodeList(){
		
		selectedNodeList.clear();
		this.firstSelectedNode = null;
		notifyObservers(TemporalViewOperator.CLEAR_SELECTION_TEMPORAL_NODE);
		
	}

	public ArrayList<Node> getSelectedNodeList() {
		return selectedNodeList;
	}

	public Node getFirstSelectedNode() {
		return firstSelectedNode;
	}

	public ArrayList<Node> getNodeListDuringInteractivityTime(Node node) {

		Tab selectedTab = temporalChainTabPane.getSelectionModel().getSelectedItem();
		TemporalChainPane temporalChainPane = (TemporalChainPane) selectedTab.getContent();

		return temporalChainPane.getNodeListDuringAnother(node, temporalChainPane);

	}

	public ArrayList<Node> getNodeListDuringInteractivityTime() {

		Tab selectedTab = temporalChainTabPane.getSelectionModel().getSelectedItem();
		TemporalChainPane temporalChainPane = (TemporalChainPane) selectedTab.getContent();
		
		return temporalChainPane.getNodeListDuringAnother(getFirstSelectedNode(), temporalChainPane);

	}
	
	public SpatialTemporalApplication getSpatialTemporalApplication() {
		return spatialTemporalApplication;
	}

	public SensoryEffectsPane getSensoryEffectsPane() {
		return sensoryEffectsPane;
	}

	public TemporalViewButtonPane getTemporalViewButtonPane() {
		return temporalViewButtonPane;
	}

}
