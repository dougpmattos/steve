package gui.temporalViewPane;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javafx.collections.FXCollections;
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
import model.common.Operation;
import controller.TemporalViewController;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TemporalChainPane extends StackedBarChart implements Observer{
	
	private TemporalViewController temporalViewController = TemporalViewController.getTemporalViewController();
	
	public TemporalChainPane(int id){
    	
    	super(new NumberAxis(), new CategoryAxis());
    	
    	NumberAxis xAxis = (NumberAxis) getXAxis();
    	xAxis.setAutoRanging(false);
    	xAxis.setUpperBound(50);

    	CategoryAxis yAxis = (CategoryAxis) getYAxis();
    	yAxis.setId("axis-y");
    	yAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList("1","2","3", "4","5")));
    	
    	createDragAndDropEvent();
    	
    	temporalViewController.getTemporalView().getTemporalChainList().get(id).addObserver(this);
    	
     }
	
	private void createDragAndDropEvent() {
		
		setOnDragDropped(new EventHandler<DragEvent>() {
			
			public void handle(DragEvent event) {
				
				Dragboard dragBoard = event.getDragboard();
		        Object[] contentTypes = dragBoard.getContentTypes().toArray();
		        Media media = (Media) dragBoard.getContent((DataFormat) contentTypes[0]);

		        try{
		        	
		        	temporalViewController.addMedia(media);
		        	
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

	@Override
	public void update(Observable o, Object obj) {
		
		Operation operation = (Operation) obj;
		Media media = (Media) operation.getOperating();
		
		switch(operation.getOperator()){
			case ADD:
	            add(media);
	            break;
	            
	        case REMOVE:
	        	//remove(temporalChainOperation.getMedia());
	            break;
			
	        case CLEAR:
	        	setData(null);
	        	break;
        	default:
        		break;
		}
	
	}
	
	public void add(Media media){
		XYChart.Series<Number, String> timelineMedia = new XYChart.Series<Number, String>();
		BigDecimal bigDecimalEnd = new BigDecimal(4);
		XYChart.Data<Number, String> axisValues = new XYChart.Data<Number, String>(bigDecimalEnd, "3");
		timelineMedia.getData().add(axisValues);
		getData().addAll(timelineMedia);
	}
		
}
