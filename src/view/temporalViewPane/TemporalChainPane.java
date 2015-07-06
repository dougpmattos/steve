package view.temporalViewPane;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import view.temporalViewPane.enums.AllenRelation;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import model.common.Media;
import model.temporalView.TemporalChain;
import model.temporalView.TemporalView;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.MediaUtil;
import model.utility.Operation;
import controller.Controller;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TemporalChainPane extends StackedBarChart implements Observer{

	private static final String BORDER_DIFF = "0.2";

	private Controller controller;
	
	private TemporalView temporalViewModel;
	private TemporalChain temporalChainModel;
	private TemporalViewPane temporalViewPane;
	private ArrayList<String> yAxisCategoryList = new ArrayList<String>();
	private HashMap<Integer,TemporalChainMediaLine> temporalChainMediaLineList = new HashMap<Integer,TemporalChainMediaLine>();
	
	public TemporalChainPane(Controller controller, TemporalView temporalViewModel, TemporalChain temporalChainModel, TemporalViewPane temporalViewPane){
    	
    	super(new NumberAxis(), new CategoryAxis());
    	
    	this.temporalViewModel = temporalViewModel;
    	this.temporalChainModel = temporalChainModel;
    	this.temporalViewPane = temporalViewPane;
    	
    	NumberAxis xAxis = (NumberAxis) getXAxis();
    	xAxis.setAutoRanging(true);
    	xAxis.setUpperBound(1000);

    	CategoryAxis yAxis = (CategoryAxis) getYAxis();
    	yAxis.setId("axis-y");
    	yAxisCategoryList.addAll(FXCollections.<String>observableArrayList("4", "3", "2", "1", "0"));
    	yAxis.setCategories(FXCollections.<String>observableArrayList(yAxisCategoryList));
    	
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
		        		
		        		Double droppedTime = 0.0;
			        	
			        	droppedMedia.setBegin(droppedTime);
			        	droppedMedia.setEnd(droppedMedia.getDuration());
			        	
			        	controller.setMasterMedia(droppedMedia, temporalChainModel);
		        		controller.addMediaTemporalChain(droppedMedia, temporalChainModel);
		        		
		        	} else{
		        	
		        		BigDecimal bigDecimalPxPosition = new BigDecimal(Double.toString(event.getX()));
		        		BigDecimal convertedPosition = bigDecimalPxPosition.multiply(new BigDecimal(Double.toString(((NumberAxis) getXAxis()).getUpperBound())).divide(new BigDecimal(Double.toString(getWidth())), 20, RoundingMode.HALF_UP));
		        		Double droppedTime = convertedPosition.subtract(new BigDecimal(BORDER_DIFF)).doubleValue();
		        		
		        		if(droppedTime < 0){
		        			droppedTime = 0.0;
		        		}
		        		
		        		System.out.println(droppedTime);
			        	droppedMedia.setBegin(droppedTime);
			        	droppedMedia.setEnd(droppedTime + droppedMedia.getDuration());
			        	
		        		controller.addMediaTemporalChain(droppedMedia, temporalChainModel);
		        		
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
	
	@Override
	public void update(Observable o, Object obj) {
		
		Operation<TemporalViewOperator> operation = (Operation<TemporalViewOperator>) obj;
		Media media = (Media) operation.getOperating();
		TemporalChain temporalChainModel = (TemporalChain) operation.getArg();
		
		switch(operation.getOperator()){
		
			case ADD_TEMPORAL_CHAIN_MEDIA:
				
	            addTemporalChainMedia(media, temporalChainModel);
	            
	            break;

        	default:
        	
        		break;
        		
		}
	
	}
	
	private void addTemporalChainMedia(Media media, TemporalChain temporalChainModel){

    	boolean mediaAdded = false;
    	AllenRelation allenRelation;
    	int temporalChainMediaLineIndex = 0;
    	
    	while(!mediaAdded && temporalChainMediaLineIndex < temporalChainMediaLineList.size()){
    		
    		boolean isPossibleAdd = true;
    		TemporalChainMediaLine temporalChainMediaLine = temporalChainMediaLineList.get(temporalChainMediaLineIndex);
    		int index = 0;
    		while(isPossibleAdd && index < temporalChainMediaLine.size()){
    			Media currentMedia = temporalChainMediaLine.get(index);
    			allenRelation = identifyAllenRelation(media, currentMedia);
    			
    			if( (!allenRelation.equals(AllenRelation.MEETS)) &&  (!allenRelation.equals(AllenRelation.MET_BY))
    			&& (!allenRelation.equals(AllenRelation.BEFORE)) && (!allenRelation.equals(AllenRelation.AFTER))){
    				
    				isPossibleAdd = false;
    				
    			}
    			
    			index++;
    		}
    		
    		if(isPossibleAdd){
    			
    			temporalChainMediaLine.add(media);
    			//esta aumentado o numero e nao substituinod a lista
    			temporalChainMediaLineList.put(temporalChainMediaLineIndex, temporalChainMediaLine);
    			
    			TemporalMediaNode temporalMediaNode = new TemporalMediaNode(controller, media, temporalChainModel, temporalViewPane, temporalChainMediaLine); 
    			getData().addAll(temporalMediaNode.getBeginSerie(), temporalMediaNode.getEndSerie());
    			
    			mediaAdded = true;
    			
    		}
    		
    		temporalChainMediaLineIndex++;
    	}
    	
    	if(!mediaAdded){
    		
    		int newLineIndex = temporalChainMediaLineList.size();
    		TemporalChainMediaLine temporalChainMediaLine = new TemporalChainMediaLine(String.valueOf(newLineIndex));
    		temporalChainMediaLine.add(media);
    		temporalChainMediaLineList.put(newLineIndex, temporalChainMediaLine);
    		
    		//TODO somente quando ultrapassar 5 linhas
//    		yAxisCategoryList.add(Integer.toString(newLineIndex));
//    		CategoryAxis yAxis = (CategoryAxis) getYAxis();
//    		yAxis.setCategories(FXCollections.<String>observableArrayList(yAxisCategoryList));
    		
    		TemporalMediaNode temporalMediaNode = new TemporalMediaNode(controller, media, temporalChainModel, temporalViewPane, temporalChainMediaLine); 
    		getData().addAll(temporalMediaNode.getBeginSerie(), temporalMediaNode.getEndSerie());
    		
    	}

	}
	
	private AllenRelation identifyAllenRelation(Media media, Media currentMedia) {
		
		double begin = media.getBegin();
		double end = media.getEnd();
		double currentMediaBegin = currentMedia.getBegin();
		double currentMediaEnd = currentMedia.getEnd();
		
		if(end == currentMediaBegin){
			return AllenRelation.MEETS;
		}else if(begin == currentMediaEnd){
			return AllenRelation.MET_BY;
		}else if(begin == currentMediaBegin && end == currentMediaEnd){
			return AllenRelation.EQUALS;
		}else if(begin == currentMediaBegin){
			return AllenRelation.STARTS;
		}else if(end == currentMediaEnd){
			return AllenRelation.FINISHES;
		}else if(end < currentMediaBegin){
			return AllenRelation.BEFORE;
		}else if(begin > currentMediaEnd){
			return AllenRelation.AFTER;
		}else if(end > currentMediaBegin && end < currentMediaEnd){
			return AllenRelation.OVERLAPS;
		}else if(begin > currentMediaBegin && begin < currentMediaEnd){
			return AllenRelation.OVERLAPPED_BY;
		}else if(begin > currentMediaBegin && end < currentMediaEnd){
			return AllenRelation.DURING;
		}else if(begin < currentMediaBegin && end > currentMediaEnd){
			return AllenRelation.CONTAINS;
		}else{
			return null;
		}
				
	}

	private String getYAxisCategory(){
		return yAxisCategoryList.get(yAxisCategoryList.size() - 1);
	}
	
}
