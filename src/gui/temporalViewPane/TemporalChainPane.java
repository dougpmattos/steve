package gui.temporalViewPane;

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
import model.temporalView.TemporalChain;
import model.temporalView.TemporalView;
import model.temporalView.TemporalViewOperator;
import controller.Controller;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TemporalChainPane extends StackedBarChart implements Observer{

	private Controller controller;
	
	private TemporalView temporalViewModel;
	private TemporalChain temporalChainModel;
	
	public TemporalChainPane(Controller controller, TemporalView temporalViewModel, TemporalChain temporalChainModel){
    	
    	super(new NumberAxis(), new CategoryAxis());
    	
    	this.temporalViewModel = temporalViewModel;
    	this.temporalChainModel = temporalChainModel;
    	
    	NumberAxis xAxis = (NumberAxis) getXAxis();
    	xAxis.setAutoRanging(false);
    	xAxis.setUpperBound(50);

    	CategoryAxis yAxis = (CategoryAxis) getYAxis();
    	yAxis.setId("axis-y");
    	yAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList("1","2","3", "4","5")));
    	
    	createDragAndDropEvent();
    	
    	temporalChainModel.addObserver(this);
    	
    	this.controller = controller;
    	
     }
	
	private void createDragAndDropEvent() {
		
		setOnDragDropped(new EventHandler<DragEvent>() {
			
			public void handle(DragEvent event) {
				
				Dragboard dragBoard = event.getDragboard();
		        Object[] contentTypes = dragBoard.getContentTypes().toArray();
		        Media droppedMedia = (Media) dragBoard.getContent((DataFormat) contentTypes[0]);

		        try{
		      
		        	if(temporalChainModel.getMasterMedia() == null){
		        		
		        		controller.setMasterMedia(droppedMedia, temporalChainModel);
		        		
		        	}else {
		        		
//		        		temporalViewController.addMediaTemporalChain(droppedMedia, temporalChainModelId);
//			        	Synchronous<Media> syncRelation = new Synchronous<Media>(temporalChain.getMasterMedia(), RelationType.BEFORE);
//			        	
//			        	Double delay = 0.0;
//			        	for(int i=0; i < temporalChain.getMediaList().size(); i++){
//			        		Media media = temporalChain.getMediaList().get(i);
//			        		delay =+ media.getDuration();
//			        	}
//			        	
//			        	syncRelation.setDelay(delay);
//			        	syncRelation.addSlave(droppedMedia);
//			        	temporalViewController.addSynchronousRelationTemporalChain(syncRelation, temporalChainModelId);
			        	
		        	}
		        	
		        	
		        	
		        	
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

	public TemporalView getTemporalViewModel(){
		return temporalViewModel;
	}
	
	public TemporalChain getTemporalChainModel(){
		return temporalChainModel;
	}
	
	@Override
	public void update(Observable o, Object obj) {
		
		Operation<TemporalViewOperator> operation = (Operation<TemporalViewOperator>) obj;
		Media media = (Media) operation.getOperating();
		
		switch(operation.getOperator()){
		
			case SET_MASTER_MEDIA:
				
	            setMasterMedia(media);
	            
	            break;

        	default:
        		
        		break;
        		
		}
	
	}
	
	private void setMasterMedia(Media masterMedia){
		
		XYChart.Series<Number, String> uniqueSerie = new XYChart.Series<Number, String>();
		XYChart.Data<Number, String> axisValues = new XYChart.Data<Number, String>(masterMedia.getDuration(), "3");
		uniqueSerie.getData().add(axisValues);
		getData().addAll(uniqueSerie);
		
	}
	
}
