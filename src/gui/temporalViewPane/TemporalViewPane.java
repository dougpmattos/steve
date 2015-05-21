package gui.temporalViewPane;

import gui.common.Language;

import java.util.Observable;
import java.util.Observer;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import model.common.Operation;
import model.temporalView.TemporalChain;
import model.temporalView.TemporalView;
import model.temporalView.TemporalViewOperator;
import controller.Controller;

@SuppressWarnings("unchecked")
public class TemporalViewPane extends BorderPane implements Observer{
	
	private Controller controller;
	
	private TemporalView temporalViewModel;
	
	private TabPane temporalChainTabPane;
	private TemporalViewButtonPane temporalViewButtonPane;
	
	public TemporalViewPane(Controller controller, TemporalView temporalViewModel){
		
		setId("temporal-view-pane");
		getStylesheets().add("gui/temporalViewPane/styles/temporalViewPane.css");
		
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
		
		TemporalChainPane temporalChainPane = new TemporalChainPane(controller, temporalViewModel, temporalChainModel);
		temporalChainScrollPane.setContent(temporalChainPane);

		Tab mainTemporalChainTab = new Tab();
		
		if(temporalChainModel.getId() == 0){
			mainTemporalChainTab.setText(Language.translate("main.temporal.chain"));
		}else {
			mainTemporalChainTab.setText(temporalChainModel.getId() + Language.translate("temporal.chain"));
		}
		mainTemporalChainTab.setClosable(false); 
		mainTemporalChainTab.setContent(temporalChainScrollPane);

		temporalChainTabPane.getTabs().add(mainTemporalChainTab);
		
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
		        	
		        default:
		        	
		        	break;
		        	
			}
			
		}
		
	}

}
