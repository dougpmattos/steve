package gui.temporalViewPane;

import gui.common.Language;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

public class TemporalViewPane extends BorderPane{
	
	private TabPane temporalChainTabPane;
	private TemporalViewButtonPane temporalViewButtonPane;
	
	public TemporalViewPane(){
		
		setId("temporal-view-pane");
		getStylesheets().add("gui/temporalViewPane/styles/temporalViewPane.css");
		
		temporalChainTabPane = new TabPane();
		temporalViewButtonPane = new TemporalViewButtonPane();
	          
	    setCenter(temporalChainTabPane);
	    setBottom(temporalViewButtonPane);
	    
	    createTemporalChain();  
	    createTemporalChain(); 
	    createTemporalChain(); 
		
	}

	private void createTemporalChain() {

		ScrollPane mainTemporalChainScrollPane = new ScrollPane();
		mainTemporalChainScrollPane.setId("temporal-chain-scroll-pane");
		mainTemporalChainScrollPane.setFitToHeight(true);
		mainTemporalChainScrollPane.setFitToWidth(true);
		
		TemporalChainPane temporalChainPane = new TemporalChainPane();
		mainTemporalChainScrollPane.setContent(temporalChainPane);

		Tab mainTemporalChainTab = new Tab();
		mainTemporalChainTab.setText(Language.translate("main.temporal.chain"));
		mainTemporalChainTab.setClosable(false); 
		mainTemporalChainTab.setContent(mainTemporalChainScrollPane);

		temporalChainTabPane.getTabs().add(mainTemporalChainTab);

	}

}
