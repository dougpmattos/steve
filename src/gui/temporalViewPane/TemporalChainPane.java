package gui.temporalViewPane;

import java.math.BigDecimal;

import javafx.event.EventHandler;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import model.common.Media;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TemporalChainPane extends StackedBarChart{
    
	public TemporalChainPane(){
    	
    	super(new NumberAxis(), new CategoryAxis());
    	
    	getYAxis().setId("axis-y");
    	
    	createDragAndDropEvent();
    	
     }
	
	private void createDragAndDropEvent() {
		
		setOnDragDropped(new EventHandler<DragEvent>() {
			
			public void handle(DragEvent event) {
				
				Dragboard dragBoard = event.getDragboard();
		        Object[] contentTypes = dragBoard.getContentTypes().toArray();
		        Media media = (Media) dragBoard.getContent((DataFormat) contentTypes[0]);

		        try{
		        	
		        	//TODO controller para manipular o modelo da visao temporal
		        	//temporalViewController.addMedia();
		        	
		        	XYChart.Series<Number, String> endSerie = new XYChart.Series<Number, String>();
		    		BigDecimal bigDecimalEnd = new BigDecimal(3);
		    		XYChart.Data<Number, String> endData = new XYChart.Data<Number, String>(bigDecimalEnd, "1");
		    		endSerie.getData().add(endData);
		    		getData().addAll(endSerie);
		        	
		        	event.setDropCompleted(true);
		        	event.consume();
		        	
		        } catch (Exception e){
		        	
		        	event.setDropCompleted(false);
		        	
		        }
		        
			}
		});
		
		setOnDragOver(new EventHandler<DragEvent>() {
			
			public void handle(DragEvent dragEvent) {
				
				Object[] contentTypes = dragEvent.getDragboard().getContentTypes().toArray();
				
				if (dragEvent.getDragboard().hasContent((DataFormat) contentTypes[0])) {
                   dragEvent.acceptTransferModes(TransferMode.COPY);
               }
               
               dragEvent.consume();
	        }  
	    });
		
	}   

//	private void addTemporalMediaInterface(TemporalMediaInfo mediaInfo, int lineListIndex, HashMap<Number, List<TemporalMediaInfo>> lineList) {
//		
//		double begin = mediaInfo.getStartTime();
//    	double end = mediaInfo.getStopTime();
//    	String previousMediaEndStringValue = Double.toString(getPreviousMediaEnd(mediaInfo, lineList.get(lineListIndex)));
//    	BigDecimal previousMediaEnd = new BigDecimal(previousMediaEndStringValue);
//    	
//		TemporalMediaInterface temporalMediaInterface = new TemporalMediaInterface(begin, previousMediaEnd, end, lineListIndex + "", mediaInfo.getId(), mediaInfo.getMedia(), this);
//		getData().addAll(temporalMediaInterface.getBeginSerie(), temporalMediaInterface.getEndSerie());
//		
//	}
		
}
