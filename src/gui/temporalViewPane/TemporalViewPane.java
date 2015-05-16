package gui.temporalViewPane;

import controller.TemporalViewController;
import gui.common.Language;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

public class TemporalViewPane extends BorderPane{
	
	private TemporalViewController temporalViewController = TemporalViewController.getTemporalViewController();
	
	private TabPane temporalChainTabPane;
	private TemporalViewButtonPane temporalViewButtonPane;
	
	public TemporalViewPane(){
		
		setId("temporal-view-pane");
		getStylesheets().add("gui/temporalViewPane/styles/temporalViewPane.css");
		
		temporalChainTabPane = new TabPane();
		temporalViewButtonPane = new TemporalViewButtonPane(temporalChainTabPane);
	          
	    setCenter(temporalChainTabPane);
	    setBottom(temporalViewButtonPane);
	    
	}
	
	public void createTemporalChain(int id) {
		
		ScrollPane temporalChainScrollPane = new ScrollPane();
		temporalChainScrollPane.setId("temporal-chain-scroll-pane");
		temporalChainScrollPane.setFitToHeight(true);
		temporalChainScrollPane.setFitToWidth(true);
		
		TemporalChainPane temporalChainPane = new TemporalChainPane(id);
		temporalChainScrollPane.setContent(temporalChainPane);

		Tab mainTemporalChainTab = new Tab();
		
		if(id == 0){
			mainTemporalChainTab.setText(Language.translate("main.temporal.chain"));
		}else {
			mainTemporalChainTab.setText(id + Language.translate("temporal.chain"));
		}
		mainTemporalChainTab.setClosable(false); 
		mainTemporalChainTab.setContent(temporalChainScrollPane);

		temporalChainTabPane.getTabs().add(mainTemporalChainTab);
		
	}

}
