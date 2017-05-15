package view.temporalViewPane;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import model.common.Media;
import model.common.SpatialTemporalView;
import model.repository.RepositoryMediaList;
import model.temporalView.Interactivity;
import model.temporalView.TemporalChain;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.Operation;
import view.common.Language;
import view.repositoryPane.RepositoryPane;
import view.stevePane.StevePane;
import controller.Controller;

@SuppressWarnings("unchecked")
public class TemporalViewPane extends BorderPane implements Observer, view.common.Observable{

	private Controller controller;
	
	private SpatialTemporalView temporalViewModel;
	
	private TabPane temporalChainTabPane;
	private TemporalViewButtonPane temporalViewButtonPane;
	private RepositoryPane repositoryPane;
	private RepositoryMediaList repositoryMediaList;
	private ArrayList<view.common.Observer> observers;
	private Media firstSelectedMedia;
	private ArrayList<Media> selectedMediaList = new ArrayList<Media>();
	private StevePane stevePane;
	private StackPane tabAddButtonContainer;
	private Button tabAddButton;
	
	public TemporalViewPane(Controller controller, SpatialTemporalView temporalViewModel, RepositoryPane repositoryPane, StevePane stevePane, RepositoryMediaList repositoryMediaList){
		
		setId("temporal-view-pane");
		getStylesheets().add("view/temporalViewPane/styles/temporalViewPane.css");

		this.temporalViewModel = temporalViewModel;
		this.repositoryPane = repositoryPane;
		this.repositoryMediaList = repositoryMediaList;
		this.stevePane = stevePane;
		
		
		temporalViewButtonPane = new TemporalViewButtonPane(controller, temporalChainTabPane, this, repositoryMediaList);
      	
		createTemporalChainTabPane();

	    setCenter(tabAddButtonContainer);
	    setBottom(temporalViewButtonPane);
	    
	    observers = new ArrayList<view.common.Observer>();
	    
	    temporalViewModel.addObserver(this);
	    
	    this.controller = controller;
	    
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
				
				controller.addTemporalChain(new TemporalChain(Language.translate("temporal.chain") + " " + (temporalChainTabPane.getTabs().size() + 1)));
				
			}
			
		});
		
		tabAddButtonContainer.getChildren().add(tabAddButton);

	}

	public void addTemporalChainPane(TemporalChain temporalChainModel) {
		
		TemporalChainPane temporalChainPane = new TemporalChainPane(controller, temporalViewModel, temporalChainModel, this, repositoryPane, stevePane);
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
		
		newTemporalChainTab.setOnClosed(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				
				Double positionX = temporalChainTabPane.getTabMinWidth()*temporalChainTabPane.getTabs().size() + temporalChainTabPane.getTabs().size()*10;
				
				if(!(positionX + 40 > getWidth())){
					tabAddButton.setTranslateX(positionX);	
				}
				
				TemporalChainPane temporalChainPaneToBeRemoved = (TemporalChainPane) newTemporalChainTab.getContent();
				controller.removeTemporalChain(temporalChainPaneToBeRemoved.getTemporalChainModel());
			
			}
			
		});
		
		newTemporalChainTab.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				
				clearSelectedMedia();
	        	
	        	for(Tab temporalTab : getTemporalChainTabPane().getTabs()){
	        		
	        		TemporalChainPane temporalChainPane = (TemporalChainPane) temporalTab.getContent();
	        		temporalChainPane.getParentTab().setStyle(null);
	        		
					for(ArrayList<TimeLineXYChartData> timeLineXYChartDataList : temporalChainPane.getTimeLineXYChartDataLineList()){
						for(TimeLineXYChartData timeLineXYChartData : timeLineXYChartDataList){
							boolean styleRemoved = false;
							if(timeLineXYChartData.getContainerNode().getStylesheets().remove("view/temporalViewPane/styles/mousePressedSlaveTemporalMediaNode.css")){
								styleRemoved = true;
							}
							if(timeLineXYChartData.getContainerNode().getStylesheets().remove("view/temporalViewPane/styles/mousePressedTemporalMediaNode.css")){
								styleRemoved = true;
							}
							if(timeLineXYChartData.getContainerNode().getStylesheets().remove("view/temporalViewPane/styles/borderOfMediaToBeStopped.css")){
								styleRemoved = true;
							}
							if(styleRemoved){
								timeLineXYChartData.getMediaImageClip().setHeight(timeLineXYChartData.getMediaImageClip().getHeight()+5);
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
		
		if(observable instanceof SpatialTemporalView){
			
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
		        	
		        	Interactivity<Media, ?> interactivityRelation = (Interactivity<Media, ?>) operation.getOperating();
		        	
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
			observer.update(this, firstSelectedMedia, temporalViewOperator);
		}
		
	}
	
	public void  addSelectedMedia(Media selectedMedia){
		
		selectedMediaList.add(selectedMedia);
		
		if(selectedMediaList.size() == 1){
			
			this.firstSelectedMedia = selectedMedia;
			notifyObservers(TemporalViewOperator.SELECT_TEMPORAL_MEDIA);
			
		}
		
	}

	public void  clearSelectedMedia(){
		
		selectedMediaList.clear();
		this.firstSelectedMedia = null;
		notifyObservers(TemporalViewOperator.CLEAR_SELECTION_TEMPORAL_MEDIA);
		
	}

	public ArrayList<Media> getSelectedMediaList() {
		return selectedMediaList;
	}

	public Media getFirstSelectedMedia() {
		return firstSelectedMedia;
	}

	public ArrayList<Media> getMediaListDuringInteractivityTime() {

		Tab selectedTab = temporalChainTabPane.getSelectionModel().getSelectedItem();
		TemporalChainPane temporalChainPane = (TemporalChainPane) selectedTab.getContent();
		
		return temporalChainPane.getMediaListDuringAnother(getFirstSelectedMedia(), temporalChainPane);

	}
	
	public SpatialTemporalView getTemporalViewModel() {
		return temporalViewModel;
	}

}
