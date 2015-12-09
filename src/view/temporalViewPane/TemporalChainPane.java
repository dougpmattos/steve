package view.temporalViewPane;

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
import javafx.scene.chart.XYChart;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.VLineTo;
import model.common.Media;
import model.temporalView.Relation;
import model.temporalView.Synchronous;
import model.temporalView.TemporalChain;
import model.temporalView.TemporalView;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.MediaUtil;
import model.utility.Operation;
import view.repositoryPane.RepositoryPane;
import view.stevePane.StevePane;
import controller.Controller;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TemporalChainPane extends StackPane implements Observer{

	private static final int DIFF_INDICATIVE_LINE_AND_TEMPORAL_NODE = 7;

	private static final double ARROW_DIFF = 7.5;

	private static final double BORDER_DIFF = 0.26;

	private Controller controller;
	
	private TimeLineChart<Number, String> timeLineChart;
	private XYChart.Series<Number, String> serie;
	private TemporalView temporalViewModel;
	private TemporalChain temporalChainModel;
	private TemporalViewPane temporalViewPane;
	private RepositoryPane repositoryPane;
	private ArrayList<String> yAxisCategoryList = new ArrayList<String>();
	private Path indicativeLine;
	private Path playLine;
	private ArrayList<TimeLineXYChartData> timeLineXYChartDataList = new ArrayList<TimeLineXYChartData>();
	private ArrayList<ArrayList<TimeLineXYChartData>> timeLineXYChartDataLineList = new ArrayList<ArrayList<TimeLineXYChartData>>();
	private StevePane stevePane;
	
	NumberAxis xAxis;
	CategoryAxis yAxis;
	
	public TemporalChainPane(Controller controller, TemporalView temporalViewModel, TemporalChain temporalChainModel, TemporalViewPane temporalViewPane, RepositoryPane repositoryPane, StevePane stevePane){
    	
		xAxis = new NumberAxis();
    	xAxis.setAutoRanging(false);
    	xAxis.setUpperBound(50);

    	yAxis = new CategoryAxis();
    	yAxis.setId("axis-y");
    	yAxisCategoryList.addAll(FXCollections.<String>observableArrayList("4", "3", "2", "1", "0"));
    	yAxis.setCategories(FXCollections.<String>observableArrayList(yAxisCategoryList));
    	
		timeLineChart = new TimeLineChart<Number, String>(xAxis, yAxis);
		serie = new XYChart.Series<Number, String>();
		timeLineChart.getData().addAll(serie);
		
		getChildren().add(timeLineChart);
		
    	setId(String.valueOf(temporalChainModel.getId()));
    	setAlignment(Pos.BOTTOM_LEFT);
    	
    	this.temporalViewModel = temporalViewModel;
    	this.temporalChainModel = temporalChainModel;
    	this.temporalViewPane = temporalViewPane;
    	this.repositoryPane = repositoryPane;
    	this.stevePane = stevePane;

    	indicativeLine = new Path();
    	indicativeLine.getElements().addAll(new MoveTo(), new VLineTo());
    	indicativeLine.setId("indicative-line");
    	
    	playLine = new Path();
    	playLine.getElements().addAll(new MoveTo(0, 10), new LineTo(15, 10), new LineTo(7.5, 23), new ClosePath(), new MoveTo(7.5, 23), new VLineTo());
    	playLine.setId("play-line");
    	getChildren().add(playLine);
    	timeLineChart.heightProperty().addListener(new ChangeListener(){
			@Override 
	        public void changed(ObservableValue o,Object oldVal, Object newVal){
				PathElement pathElement = playLine.getElements().get(5);
				if(pathElement instanceof VLineTo){
					((VLineTo) pathElement).setY(timeLineChart.getHeight());
				}
			}
	    });
    	
    	createDragAndDropEvent();
    	createMouseEvent();
    	
    	temporalChainModel.addObserver(this);
    	for(Relation relation: temporalChainModel.getRelationList()){
    		relation.addObserver(this);
    	}
    	
    	this.controller = controller;
    	
     }
	
	private void createDragAndDropEvent() {
		
		setOnDragDropped(new EventHandler<DragEvent>() {
			
			public void handle(DragEvent event) {
				
				Dragboard dragBoard = event.getDragboard();
		        Object[] contentTypes = dragBoard.getContentTypes().toArray();
		        Media droppedMedia = (Media) dragBoard.getContent((DataFormat) contentTypes[0]);
		        int duplicatedMediaCount = getDuplicatedMediaCount(droppedMedia);
		        if(duplicatedMediaCount > 0){
		        	droppedMedia.setName(droppedMedia.getName() + "_" + duplicatedMediaCount++);
		        }

		        try{
		        	
		        	if(temporalChainModel.getMasterMedia() == null){
		        		
		        		Double droppedTime = 0.0;
			        	
			        	droppedMedia.setBegin(droppedTime);
			        	droppedMedia.setEnd(droppedMedia.getDuration());
			        	
			        	controller.setMasterMedia(droppedMedia, temporalChainModel);
		        		
		        	} else{
		        	
		        		Double droppedTime = timeLineChart.getXAxis().getValueForDisplay(event.getX()).doubleValue();
		        		droppedTime = MediaUtil.approximateDouble(droppedTime - BORDER_DIFF);
		        		
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

			private int getDuplicatedMediaCount(Media droppedMedia) {
				
				int i = 0;
				
				for(Media media : temporalChainModel.getMediaAllList()){
					if(media.getName().equalsIgnoreCase(droppedMedia.getName())){
						i++;
					}
				}
				
				return i;
				
			}
		});
		
		setOnDragEntered(new EventHandler<DragEvent>() {
			
				public void handle(DragEvent dragEvent) {
					
					if(temporalChainModel.getMasterMedia() != null){
						
						PathElement pathElement = indicativeLine.getElements().get(1);
						if(pathElement instanceof VLineTo){
							((VLineTo) pathElement).setY(timeLineChart.getHeight());
						}
						getChildren().add(indicativeLine);
				
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
				
				PathElement pathElement = indicativeLine.getElements().get(1);
				if(pathElement instanceof VLineTo){
					((VLineTo) pathElement).setY(timeLineChart.getHeight());
				}
				getChildren().add(indicativeLine);
			
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
		
		setOnMouseDragged(new EventHandler<MouseEvent>() {
			
			public void handle(MouseEvent mouseEvent) {
				indicativeLine.setTranslateX(mouseEvent.getX() + DIFF_INDICATIVE_LINE_AND_TEMPORAL_NODE);
	        }  
	    });
		
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			public void handle(MouseEvent mouseEvent) {
				playLine.setTranslateX(mouseEvent.getX() - ARROW_DIFF);
	        }  
	    });
		
	}
	
	@Override
	public void update(Observable o, Object obj) {
		
		Operation<TemporalViewOperator> operation = (Operation<TemporalViewOperator>) obj;

		Media media;
		int line;
		Synchronous<Media> syncRelation;
		TemporalChain temporalChainModel;
		
		switch(operation.getOperator()){
		
			case ADD_TEMPORAL_CHAIN_MEDIA:
				
				media = (Media) operation.getOperating();
				line = (int) operation.getArg();
	            addTemporalChainMedia(media, line);
	            
	            break;
	            
			case REMOVE_TEMPORAL_CHAIN_MEDIA:
				
				media = (Media) operation.getOperating();
				line = (int) operation.getArg();
	            removeTemporalChainMedia(media, line);
	            
	            break;
	            
			case ADD_SYNC_RELATION:
				
				syncRelation = (Synchronous<Media>) operation.getOperating();
				temporalChainModel = (TemporalChain) operation.getArg();
	            addSyncRelation(syncRelation, temporalChainModel);
				
				break;
				
			case REMOVE_SYNC_RELATION:
				
				syncRelation = (Synchronous<Media>) operation.getOperating();
				temporalChainModel = (TemporalChain) operation.getArg();
	            removeSyncRelation(syncRelation, temporalChainModel);
				
				break;
			
			case REMOVE_SLAVE_MEDIA_OF_SYNC_RELATION:
				
				media = (Media) operation.getOperating();
				syncRelation = (Synchronous<Media>) operation.getArg();
	            removeSlaveMediaOfSyncRelation(media, syncRelation);
				
				break;

        	default:
        	
        		break;
        		
		}
	
	}
	
	private void addTemporalChainMedia(Media media, int line){

		if(line > 4){

			yAxisCategoryList.add(0, String.valueOf(line));
			yAxis.setCategories(FXCollections.<String>observableArrayList(yAxisCategoryList));
	
		}
	
		TimeLineXYChartData timeLineXYChartData = new TimeLineXYChartData(controller, media, temporalChainModel, temporalViewPane, this,repositoryPane, line, stevePane, timeLineChart); 	
		serie.getData().add(timeLineXYChartData.getXYChartData());
		
		timeLineXYChartDataList.add(timeLineXYChartData);
		timeLineXYChartDataLineList.add(line, timeLineXYChartDataList);
		
	}
	
	private void removeTemporalChainMedia(Media media, int line){

		ArrayList<TimeLineXYChartData> timeLineXYChartDataLine = timeLineXYChartDataLineList.get(line);
		int i = 0;
		Boolean removed = false;
		
		while(i < timeLineXYChartDataLine.size() && !removed){
			
			TimeLineXYChartData timeLineXYChartData = timeLineXYChartDataLine.get(i);
			
			if(timeLineXYChartData.getMedia() == media){
				timeLineXYChartDataLine.remove(timeLineXYChartData);
				serie.getData().remove(timeLineXYChartData.getXYChartData());
				removed = true;
			}
			
			i++;
		}

	}
	
	private void addSyncRelation(Synchronous<Media> syncRelation, TemporalChain temporalChainModel){
		
		//TODO fazendo primeiramente para start
//		Double masterBegin = syncRelation.getMasterMedia().getBegin();
//	 	Path arrow = new Path();
//	 	arrow.getElements().addAll(new MoveTo(0, 200), new LineTo(15, 10), new LineTo(7.5, 23), new ClosePath(), new MoveTo(7.5, 23), new VLineTo());
//	 	arrow.setId("relation-arrow");
//    	getChildren().add(arrow);

	}
	
	private void removeSyncRelation(Synchronous<Media> syncRelation, TemporalChain temporalChainModel){
		
		//TODO remover as setas das relações entre as mídias.

	}
	
	private void removeSlaveMediaOfSyncRelation(Media slaveMedia, Synchronous<Media> syncRelation){
		
		//TODO remover as seta que aponta para a mídia escrava removida da relação.

	}
	
	public ObservableList<Node> getChildList() {
        return getChildren();
    }
	
	public XYChart.Series<Number, String> getSerie() {
		return serie;
	}
	
	public ArrayList<ArrayList<TimeLineXYChartData>> getTimeLineXYChartDataLineList(){
		return timeLineXYChartDataLineList;
	}

	public TemporalChain getTemporalChainModel() {
		return temporalChainModel;
	}

	public ArrayList<Media> getMediaListDuringAnother(Media firstSelectedMedia, TemporalChainPane temporalChainPane) {
		
		return getTemporalChainModel().getMediaListDuringAnother(firstSelectedMedia, temporalChainPane);
		
	}
	
	
	
}
