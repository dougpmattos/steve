package view.temporalViewPane;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.VLineTo;
import model.common.Media;
import model.temporalView.TemporalChain;
import model.temporalView.TemporalView;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.Operation;
import view.repositoryPane.RepositoryPane;
import view.temporalViewPane.enums.AllenRelation;
import controller.Controller;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TemporalChainPane extends StackPane implements Observer{

	private static final String BORDER_DIFF = "0.2";

	private Controller controller;
	
	private StackedBarChart<Number, String> stackedBarChart;
	private TemporalView temporalViewModel;
	private TemporalChain temporalChainModel;
	private TemporalViewPane temporalViewPane;
	private RepositoryPane repositoryPane;
	private ArrayList<String> yAxisCategoryList = new ArrayList<String>();
	private ArrayList<TemporalMediaNodeList> temporalMediaNodeListList = new ArrayList<TemporalMediaNodeList>();
	private Line indicativeLine;
	private Path playLine;
	
	public TemporalChainPane(Controller controller, TemporalView temporalViewModel, TemporalChain temporalChainModel, TemporalViewPane temporalViewPane, RepositoryPane repositoryPane){
    	
		NumberAxis xAxis = new NumberAxis();
    	xAxis.setAutoRanging(false);
    	xAxis.setUpperBound(50);

    	CategoryAxis yAxis = new CategoryAxis();
    	yAxis.setId("axis-y");
    	yAxisCategoryList.addAll(FXCollections.<String>observableArrayList("4", "3", "2", "1", "0"));
    	yAxis.setCategories(FXCollections.<String>observableArrayList(yAxisCategoryList));
    	
		stackedBarChart = new StackedBarChart<Number, String>(xAxis, yAxis);
    	
		getChildren().add(stackedBarChart);
		
    	setId(String.valueOf(temporalChainModel.getId()));
    	setAlignment(Pos.CENTER_LEFT);
    	
    	this.temporalViewModel = temporalViewModel;
    	this.temporalChainModel = temporalChainModel;
    	this.temporalViewPane = temporalViewPane;
    	this.repositoryPane = repositoryPane;

    	indicativeLine = new Line();
    	indicativeLine.setId("indicative-line");
    	
    	playLine = new Path();
    	playLine.getElements().addAll(new MoveTo(0, 4), new LineTo(15, 4), new LineTo(7.5, 40), new ClosePath(), new MoveTo(7.5, 40), new VLineTo(38));
    	playLine.setId("play-line");
    	getChildren().add(playLine);
    	heightProperty().addListener(new ChangeListener(){
			@Override 
	        public void changed(ObservableValue o,Object oldVal, Object newVal){
				Double heightValue = (double) newVal;
				PathElement pathElement = playLine.getElements().get(5);
				if(pathElement instanceof VLineTo){
					((VLineTo) pathElement).setY(heightValue);
					System.out.println("stack pane mudou" + getHeight());
				}
			}
	    });
    	
    	createDragAndDropEvent();
    	createMouseEvent();
    	
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
		        		BigDecimal convertedPosition = bigDecimalPxPosition.multiply(new BigDecimal(Double.toString(((NumberAxis) stackedBarChart.getXAxis()).getUpperBound())).divide(new BigDecimal(Double.toString(getWidth())), 20, RoundingMode.HALF_UP));
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
		
		setOnDragEntered(new EventHandler<DragEvent>() {
			
				public void handle(DragEvent dragEvent) {
					
					if(temporalChainModel.getMasterMedia() != null){
						getChildren().add(indicativeLine);
						indicativeLine.setEndY(stackedBarChart.getHeight());
					}
					
		        }  
				
		});
		
		setOnDragExited(new EventHandler<DragEvent>() {
			
			public void handle(DragEvent dragEvent) {
				getChildren().remove(indicativeLine);
	        }  
			
		});
		
		setOnDragOver(new EventHandler<DragEvent>() {
			
			public void handle(DragEvent dragEvent) {

				indicativeLine.setTranslateX(dragEvent.getX());
				
				Object[] contentTypes = dragEvent.getDragboard().getContentTypes().toArray();
				
				if (dragEvent.getDragboard().hasContent((DataFormat) contentTypes[0])) {
                   dragEvent.acceptTransferModes(TransferMode.COPY);
               }
               
               dragEvent.consume();
	        }  
	    });
		
	}
	
	public void createMouseEvent(){
		
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			
			public void handle(MouseEvent mouseEvent) {
				//getChildren().add(indicativeLine);
				indicativeLine.setEndY(stackedBarChart.getHeight());
			}
		});
		
		setOnMouseExited(new EventHandler<MouseEvent>() {
			
			public void handle(MouseEvent mouseEvent) {
				getChildren().remove(indicativeLine);
			}
	    });
		
		setOnMouseMoved(new EventHandler<MouseEvent>() {
			
			public void handle(MouseEvent mouseEvent) {
				indicativeLine.setTranslateX(mouseEvent.getX());
	        }  
	    });
		
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			public void handle(MouseEvent mouseEvent) {
				playLine.setTranslateX(mouseEvent.getX());
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
    	int temporalMediaNodeListListIndex = 0;
    	
    	while(!mediaAdded && temporalMediaNodeListListIndex < temporalMediaNodeListList.size()){
    		
    		boolean isPossibleAdd = true;
    		TemporalMediaNodeList temporalMediaNodeList = temporalMediaNodeListList.get(temporalMediaNodeListListIndex);
    		int index = 0;
    		while(isPossibleAdd && index < temporalMediaNodeList.size()){
    			Media currentMedia = temporalMediaNodeList.get(index).getMedia();
    			allenRelation = identifyAllenRelation(media, currentMedia);
    			
    			if( (!allenRelation.equals(AllenRelation.MEETS)) &&  (!allenRelation.equals(AllenRelation.MET_BY))
    			&& (!allenRelation.equals(AllenRelation.BEFORE)) && (!allenRelation.equals(AllenRelation.AFTER))){
    				
    				isPossibleAdd = false;
    				
    			}
    			
    			index++;
    		}
    		
    		if(isPossibleAdd){
    			
    			TemporalMediaNode temporalMediaNode = new TemporalMediaNode(controller, media, temporalChainModel, temporalViewPane, repositoryPane, temporalMediaNodeList); 
    			
    			temporalMediaNodeList.add(temporalMediaNode);
//    			//TODO esta aumentando o numero e nao substituinod a lista
//    			temporalMediaNodeListList.(temporalMediaNodeListListIndex, temporalMediaNodeList);
    			
    			stackedBarChart.getData().addAll(temporalMediaNode.getBeginSerie(), temporalMediaNode.getEndSerie());
    			
    			mediaAdded = true;
    			
    		}
    		
    		temporalMediaNodeListListIndex++;
    	}
    	
    	if(!mediaAdded){
    		
    		int newLineIndex = temporalMediaNodeListList.size();
    		TemporalMediaNodeList temporalMediaNodeList = new TemporalMediaNodeList(String.valueOf(newLineIndex));
    		
    		TemporalMediaNode temporalMediaNode = new TemporalMediaNode(controller, media, temporalChainModel, temporalViewPane, repositoryPane, temporalMediaNodeList); 
    		
    		temporalMediaNodeList.add(temporalMediaNode);
    		temporalMediaNodeListList.add(newLineIndex, temporalMediaNodeList);
    		
    		//TODO somente quando ultrapassar 5 linhas
//    		yAxisCategoryList.add(Integer.toString(newLineIndex));
//    		CategoryAxis yAxis = (CategoryAxis) getYAxis();
//    		yAxis.setCategories(FXCollections.<String>observableArrayList(yAxisCategoryList));
    		
    		stackedBarChart.getData().addAll(temporalMediaNode.getBeginSerie(), temporalMediaNode.getEndSerie());
    		
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

	public ObservableList<Node> getChildList() {
        return getChildren();
    }
	
	public ArrayList<TemporalMediaNodeList> getTemporalChainMediaListList(){
		return temporalMediaNodeListList;
	}
	
}
