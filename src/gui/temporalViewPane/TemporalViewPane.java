package gui.temporalViewPane;

import gui.common.Language;

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
import controller.Controller;

@SuppressWarnings("unchecked")
public class TemporalViewPane extends BorderPane implements Observer, gui.common.Observable{

	private Controller controller;
	
	private TemporalView temporalViewModel;
	
	private TabPane temporalChainTabPane;
	private TemporalViewButtonPane temporalViewButtonPane;
	private ArrayList<gui.common.Observer> observers;
	private Media selectedMedia;
	
	public TemporalViewPane(Controller controller, TemporalView temporalViewModel){
		
		setId("temporal-view-pane");
		getStylesheets().add("gui/temporalViewPane/styles/temporalViewPane.css");
		
		observers = new ArrayList<gui.common.Observer>();
		
		this.temporalViewModel = temporalViewModel;
		
		temporalChainTabPane = new TabPane();
		temporalViewButtonPane = new TemporalViewButtonPane(controller, temporalChainTabPane);
	          
	    setCenter(temporalChainTabPane);
	    setBottom(temporalViewButtonPane);
	    
	    temporalViewModel.addObserver(this);
	    
	    this.controller = controller;
	    
	}
	
	public void addTemporalChainPane(TemporalChain temporalChainModel) {
		
		ScrollPane temporalChainScrollPane = new ScrollPane();
		temporalChainScrollPane.setId("temporal-chain-scroll-pane");
		temporalChainScrollPane.setFitToHeight(true);
		temporalChainScrollPane.setFitToWidth(true);
		
		TemporalChainPane temporalChainPane = new TemporalChainPane(controller, temporalViewModel, temporalChainModel, this);
		temporalChainScrollPane.setContent(temporalChainPane);

		Tab mainTemporalChainTab = new Tab();
		
		if(temporalChainModel.getId() == 0){
			mainTemporalChainTab.setText(Language.translate("main.temporal.chain"));
		}else {
			mainTemporalChainTab.setText(temporalChainModel.getId() + "  " + Language.translate("temporal.chain"));
		}
		mainTemporalChainTab.setClosable(false); 
		mainTemporalChainTab.setContent(temporalChainScrollPane);

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

	@Override
	public void addObserver(gui.common.Observer o) {
		observers.add(o);
	}

	@Override
	public void deleteObserver(gui.common.Observer o) {
		
		int i = observers.indexOf(o);
		if(i >= 0){
			observers.remove(o);
		}
		
	}

	@Override
	public void notifyObservers() {

		for(int i = 0; i < observers.size(); i++){
			gui.common.Observer observer = (gui.common.Observer) observers.get(i);
			observer.update(this, selectedMedia);
		}
		
	}
	
	public void  setSelectedMedia(Media selectedMedia){
		this.selectedMedia = selectedMedia;
		notifyObservers();
	}
	
	public Media getSelectedMedia(){
		return selectedMedia;
	}

}
