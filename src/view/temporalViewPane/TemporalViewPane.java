package view.temporalViewPane;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import model.common.Media;
import model.temporalView.TemporalChain;
import model.temporalView.TemporalView;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.Operation;
import view.common.Language;
import view.repositoryPane.RepositoryPane;
import view.stevePane.StevePane;
import controller.Controller;

@SuppressWarnings("unchecked")
public class TemporalViewPane extends BorderPane implements Observer, view.common.Observable{

	private Controller controller;
	
	private TemporalView temporalViewModel;
	
	private TabPane temporalChainTabPane;
	private TemporalViewButtonPane temporalViewButtonPane;
	private RepositoryPane repositoryPane;
	private ArrayList<view.common.Observer> observers;
	private Media firstSelectedMedia;
	private ArrayList<Media> selectedMediaList = new ArrayList<Media>();
	private StevePane stevePane;
	
	public TemporalViewPane(Controller controller, TemporalView temporalViewModel, RepositoryPane repositoryPane, StevePane stevePane){
		
		setId("temporal-view-pane");
		getStylesheets().add("view/temporalViewPane/styles/temporalViewPane.css");

		this.temporalViewModel = temporalViewModel;
		this.repositoryPane = repositoryPane;
		this.stevePane = stevePane;
		
		temporalChainTabPane = new TabPane();
		temporalViewButtonPane = new TemporalViewButtonPane(controller, temporalChainTabPane, this);
      		
	    setCenter(temporalChainTabPane);
	    setBottom(temporalViewButtonPane);
	    
	    observers = new ArrayList<view.common.Observer>();
	    
	    temporalViewModel.addObserver(this);
	    
	    this.controller = controller;
	    
	}
	
	public void addTemporalChainPane(TemporalChain temporalChainModel) {
		
		ScrollPane temporalChainScrollPane = new ScrollPane();
		temporalChainScrollPane.setId("temporal-chain-scroll-pane");
		
		TemporalChainPane temporalChainPane = new TemporalChainPane(controller, temporalViewModel, temporalChainModel, this, repositoryPane, stevePane);

		Tab mainTemporalChainTab = new Tab();
		
		if(temporalChainModel.getId() == 0){
			mainTemporalChainTab.setText(Language.translate("main.temporal.chain"));
		}else {
			mainTemporalChainTab.setText(temporalChainModel.getId() + "  " + Language.translate("temporal.chain"));
		}
		mainTemporalChainTab.setClosable(false); 
		mainTemporalChainTab.setContent(temporalChainPane);

		temporalChainTabPane.getTabs().add(mainTemporalChainTab);
		
	}
	
	private void clearTemporalChainTabPane() {
		
		temporalChainTabPane.getTabs().clear();
		
	}
	
	@Override
	public void update(Observable observable, Object arg) {
		
		if(observable instanceof TemporalView){
			
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
		notifyObservers(TemporalViewOperator.CLEAR_SELECTION_TEMPORAL_MEDIA);
		
	}

	public ArrayList<Media> getSelectedMediaList() {
		return selectedMediaList;
	}

	public Media getFirstSelectedMedia() {
		return firstSelectedMedia;
	}
	
	
	
}
