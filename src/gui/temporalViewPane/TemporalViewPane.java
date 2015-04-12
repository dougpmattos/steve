package gui.temporalViewPane;

import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class TemporalViewPane extends BorderPane{
	
	private TabPane temporalChainTabPane;
	private ScrollPane temporalChainScrollPane;
	private VBox temporalChainPane;
	private TemporalViewButtonPane temporalViewButtonPane;
	
	public TemporalViewPane(){
		
		setId("temporal-view-pane");
		getStylesheets().add("gui/temporalViewPane/styles/temporalViewPane.css");
		
		temporalChainTabPane = new TabPane();
		temporalViewButtonPane = new TemporalViewButtonPane();
	          
	    setCenter(temporalChainTabPane);
	    setBottom(temporalViewButtonPane);
	    
	    createTemporalChain();

	    createDragAndDropEvent();
		
	      
	   
//		TemporalChainPane videoTemporalChain = new TemporalChainPane(videoTemporalMediainfoList);       
//		TemporalChainPane audioTemporalChain = new TemporalChainPane(audioTemporalMediainfoList);
//       audioTemporalChain.setXAxisTickLabelsVisible(false);
//       audioTemporalChain.setXAxisTickLength(-1);
//       
//       final StackedBarChart<Number, String> videoTemporalChainChart = videoTemporalChain.getStackedBarChart();
//       final StackedBarChart<Number, String> audioTemporalChainChart = audioTemporalChain.getStackedBarChart();
//
//       
//       if(videoTemporalChain.getLastMediaTime() > audioTemporalChain.getLastMediaTime()){
//    	   videoTemporalChain.setXAxisLength(videoTemporalChain.getLastMediaTime());
//    	   audioTemporalChain.setXAxisLength(videoTemporalChain.getLastMediaTime());
//       }else if(audioTemporalChain.getLastMediaTime() > 0){
//    	   videoTemporalChain.setXAxisLength(audioTemporalChain.getLastMediaTime());
//    	   audioTemporalChain.setXAxisLength(audioTemporalChain.getLastMediaTime());
//       }else {
//    	   videoTemporalChain.setXAxisLength(DEFAULT_AXIS_LENGTH);
//    	   audioTemporalChain.setXAxisLength(DEFAULT_AXIS_LENGTH);
//       }
//
//       HBox audioTemporalChainChartAndChannelTitlePane = new HBox();
//       audioTemporalChainChartAndChannelTitlePane.setId("temporal-chain-audio-pane");
//       audioTemporalChainChartAndChannelTitlePane.getChildren().add(audioTemporalChainChart);
//      
//       HBox videoTemporalChainChartAndChannelTitlePane = new HBox();
//       videoTemporalChainChartAndChannelTitlePane.setId("temporal-chain-video-pane");
//       videoTemporalChainChartAndChannelTitlePane.getChildren().add(videoTemporalChainChart);
//       
//       videoTemporalChainChartAndChannelTitlePane.widthProperty().addListener(new ChangeListener(){
//           @Override 
//           public void changed(ObservableValue o,Object oldVal, Object newVal){
//        	   videoTemporalChainChart.setPrefWidth((double) newVal - 160);
//           }
//         });
//       audioTemporalChainChartAndChannelTitlePane.widthProperty().addListener(new ChangeListener(){
//           @Override 
//           public void changed(ObservableValue o,Object oldVal, Object newVal){
//        	   audioTemporalChainChart.setPrefWidth((double) newVal - 160);
//           }
//         }); 
//      
		
	}

	private void createTemporalChain() {
		
		temporalChainPane = new VBox();
	    temporalChainPane.setId("temporal-chain-pane");
	    //temporalChainPane.getChildren().add(videoTemporalChainChart);
	    //temporalChainPane.getChildren().add(audioTemporalChainChart);

	    temporalChainScrollPane = new ScrollPane();
	    temporalChainScrollPane.setContent(temporalChainPane);
	    
		//TODO colocar um for aqui para diversas temporalChainPane
		Tab temporalChainTab = new Tab();
		temporalChainTab.setId("temporal-chain-tab");
		temporalChainTab.setText("Temporal Chain 1");
		//tab.setId("tab");
		temporalChainTab.setContent(temporalChainScrollPane);
		temporalChainTab.setClosable(false); 
		temporalChainTabPane.getTabs().add(temporalChainTab);
		
	}
	
	private void createDragAndDropEvent() {
		
		temporalChainPane.setOnDragDropped(new EventHandler<DragEvent>() {
			
			public void handle(DragEvent event) {
				
				System.out.println("DROPPED");
//		        Dragboard dragBoard = event.getDragboard();
//		        boolean success = false;
//		        if (dragBoard.hasFiles()) {
//		           //TODO Criar o n� NCL correspondente � m�dia arrastada para a temporal View.
//		        	
//		        	Media media = new Media(dragBoard.getFiles().get(0));
//		        	
//		        	TemporalMediaInfo mediaInfo = new TemporalMediaInfo("teste",0.0,5.0, media);
//				 	temporalView.getMainMediaInfoList().add(mediaInfo);
//				 	temporalChainPane = new TemporalChainPane(temporalView.getMainMediaInfoList());
//				 	sp.setContent(temporalChainPane);   
//				 	
//		           success = true;
//		           
//		        }
//			        
//		        event.setDropCompleted(success);
//		        
//		        event.consume();
			}
		});
		
	}       

}
