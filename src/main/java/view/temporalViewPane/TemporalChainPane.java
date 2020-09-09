package view.temporalViewPane;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaView;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.VLineTo;
import model.common.Media;
import model.common.SensoryEffect;
import model.common.SpatialTemporalApplication;
import model.common.enums.SensoryEffectType;
import model.temporalView.Interactivity;
import model.temporalView.Synchronous;
import model.temporalView.TemporalChain;
import model.temporalView.TemporalRelation;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.MediaUtil;
import model.utility.Operation;
import view.common.Language;
import view.repositoryPane.RepositoryPane;
import view.spatialViewPane.ControlButtonPane;
import view.spatialViewPane.DisplayPane;
import view.stevePane.StevePane;
import controller.ApplicationController;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TemporalChainPane extends StackPane implements Observer{

	private static final int DIFF_INDICATIVE_LINE_AND_TEMPORAL_NODE = 7;
	private final int ICON_WIDTH = 40;

	private static final double ARROW_DIFF = 7.5;

	private static final double BORDER_DIFF = 0.26;

	private ApplicationController applicationController;
	
	private TimeLineChart<Number, String> timeLineChart;
	private XYChart.Series<Number, String> serie;
	private SpatialTemporalApplication temporalViewModel;
	private TemporalChain temporalChainModel;
	private TemporalViewPane temporalViewPane;
	private RepositoryPane repositoryPane;
	private ArrayList<String> yAxisCategoryList = new ArrayList<String>();
	private Path indicativeLine;
	private Path playhead;
	private ArrayList<ArrayList<TimeLineXYChartData>> timeLineXYChartDataLineList = new ArrayList<ArrayList<TimeLineXYChartData>>();
	private StevePane stevePane;
	private Tab parentTab;
	private Boolean hasClickedPlayhead = false;
	private DisplayPane displayPane;
	private ControlButtonPane controlButtonPane;
	private ContextMenu arrowContextMenu;
	private MenuItem menuItemDeleteRelation;
	private StackPane screen;
	NumberAxis xAxis;
	CategoryAxis yAxis;
	
	public TemporalChainPane(ApplicationController applicationController, SpatialTemporalApplication temporalViewModel, TemporalChain temporalChainModel, TemporalViewPane temporalViewPane, RepositoryPane repositoryPane, StevePane stevePane){
    	
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
    	setAlignment(Pos.TOP_LEFT);
    	
    	this.temporalViewModel = temporalViewModel;
    	this.temporalChainModel = temporalChainModel;
    	this.temporalViewPane = temporalViewPane;
    	this.repositoryPane = repositoryPane;
    	this.stevePane = stevePane;

    	indicativeLine = new Path();
    	indicativeLine.getElements().addAll(new MoveTo(), new VLineTo());
    	indicativeLine.setId("indicative-line");
    	
    	playhead = new Path();
    	playhead.getElements().addAll(new MoveTo(0, 10), new LineTo(15, 10), new LineTo(7.5, 23), new ClosePath(), new MoveTo(7.5, 23), new VLineTo());
    	playhead.setId("play-line");
    	getChildren().add(playhead);
    	heightProperty().addListener(new ChangeListener(){
			@Override 
	        public void changed(ObservableValue o,Object oldVal, Object newVal){
				PathElement pathElement = playhead.getElements().get(5);
				if(pathElement instanceof VLineTo){
					((VLineTo) pathElement).setY(getHeight());
				}
			}
	    });
    	
    	displayPane = stevePane.getSpatialViewPane().getDisplayPane();
		controlButtonPane = displayPane.getControlButtonPane();
		screen = displayPane.getScreen();

    	createDragAndDropEvent();
    	createMouseEvent();
    	createListeners();
    	createArrowPopupMenu(applicationController, temporalChainModel);
    	
    	temporalChainModel.addObserver(this);
    	for(TemporalRelation relation: temporalChainModel.getRelationList()){
    		relation.addObserver(this);
    	}
    	
    	this.applicationController = applicationController;
    	
     }

	private void createListeners(){
		
		indicativeLine.translateXProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				
				if(!controlButtonPane.getIsPlaying() && !controlButtonPane.getHasPaused()) {
					
					Double indicativeLine = timeLineChart.getXAxis().getValueForDisplay(newValue.doubleValue()).doubleValue();
					
					for(Media media : temporalChainModel.getMediaAllList()){
						
						if(media.getBegin() <= indicativeLine && indicativeLine <= media.getEnd()){
							
							if(!media.getIsPLayingInPreview()){
				
								media.setIsPLayingInPreview(true);
								Object mediaContent = controlButtonPane.getMediaContent(media);
								
								if(mediaContent instanceof MediaView){
									
									controlButtonPane.setVideoPresentationProperties((MediaView) mediaContent, media);
									screen.getChildren().add((MediaView) mediaContent);
									
								} else if(mediaContent instanceof ImageView){
									
									controlButtonPane.setImagePresentationProperties((ImageView) mediaContent, media);
									if(screen.getChildren().isEmpty()){
										
										screen.getChildren().add((ImageView) mediaContent);
										
									} else{
										boolean inserted = false;
										for(Node executionObject : screen.getChildren()){
							    			
											if(((ImageView) mediaContent).getTranslateZ() < executionObject.getTranslateZ()){
												screen.getChildren().add(screen.getChildren().indexOf(executionObject), (ImageView) mediaContent);
												inserted = true;
												break;
											}
											
							    		}
										if(!inserted){
											screen.getChildren().add((ImageView) mediaContent);
										}
									}


								}
								
							} 

						}else {
							
							if(media.getIsPLayingInPreview()){
									
				    			if(!screen.getChildren().isEmpty()){
				    				screen.getChildren().remove(media.getExecutionObject());
									media.setIsPLayingInPreview(false);
				    			}
		
							}	
						}
					}
				}

			}
			
		});
		
		
		
	}
	
	private void createDragAndDropEvent() {
		
		setOnDragDropped(new EventHandler<DragEvent>() {
			
			public void handle(DragEvent event) {
				
				Dragboard dragBoard = event.getDragboard();
		        Object[] contentTypes = dragBoard.getContentTypes().toArray();
		        Object droppedNode = dragBoard.getContent((DataFormat) contentTypes[0]);

		        if(droppedNode instanceof SensoryEffectType) {
		        	
		        	SensoryEffectType sensoryEffectType = (SensoryEffectType) dragBoard.getContent((DataFormat) contentTypes[0]);
		        	SensoryEffect droppedSensoryEffect = new SensoryEffect();
					droppedSensoryEffect.setParentTemporalChain(temporalChainModel);

		        	int duplicatedEffectCount;
		        	
		        	switch(sensoryEffectType){
		        	
			            case WIND:
			            	
			            	droppedSensoryEffect.setType(SensoryEffectType.WIND);
			            	droppedSensoryEffect.setName(SensoryEffectType.WIND.toString());
			            	
				        	duplicatedEffectCount = getDuplicatedNodeCount(droppedSensoryEffect);
					        if(duplicatedEffectCount > 0){
					        	droppedSensoryEffect.setName(SensoryEffectType.WIND.toString() + "_" + duplicatedEffectCount++);
					        }
					        
			                break;
			                
			            case WATER_SPRAYER:
			                
			            	droppedSensoryEffect.setType(SensoryEffectType.WATER_SPRAYER);
			            	
				        	duplicatedEffectCount = getDuplicatedNodeCount(droppedSensoryEffect);
					        if(duplicatedEffectCount > 0){
					        	droppedSensoryEffect.setName(SensoryEffectType.WATER_SPRAYER.toString() + "_" + duplicatedEffectCount++);
					        }else{
					        	droppedSensoryEffect.setName(SensoryEffectType.WATER_SPRAYER.toString());
					        }
					        
			                break;
			                
			            case FOG:
			                
			            	droppedSensoryEffect.setType(SensoryEffectType.FOG);
			            	
				        	duplicatedEffectCount = getDuplicatedNodeCount(droppedSensoryEffect);
					        if(duplicatedEffectCount > 0){
					        	droppedSensoryEffect.setName(SensoryEffectType.FOG.toString() + "_" + duplicatedEffectCount++);
					        }else{
					        	droppedSensoryEffect.setName(SensoryEffectType.FOG.toString());
					        }
					        
			                break;
			                
			            case FLASH_LIGHT:
			            	
			            	droppedSensoryEffect.setType(SensoryEffectType.FLASH_LIGHT);
			            	
				        	duplicatedEffectCount = getDuplicatedNodeCount(droppedSensoryEffect);
					        if(duplicatedEffectCount > 0){
					        	droppedSensoryEffect.setName(SensoryEffectType.FLASH_LIGHT.toString() + "_" + duplicatedEffectCount++);
					        }else{
					        	droppedSensoryEffect.setName(SensoryEffectType.FLASH_LIGHT.toString());
					        }
					        
			                break;
			                
			            case SCENT:
			            	
			            	droppedSensoryEffect.setType(SensoryEffectType.SCENT);
			            	
				        	duplicatedEffectCount = getDuplicatedNodeCount(droppedSensoryEffect);
					        if(duplicatedEffectCount > 0){
					        	droppedSensoryEffect.setName(SensoryEffectType.SCENT.toString() + "_" + duplicatedEffectCount++);
					        }else{
					        	droppedSensoryEffect.setName(SensoryEffectType.SCENT.toString());
					        }
					        
			                break;
			                
			            case COLD:
			            	
			            	droppedSensoryEffect.setType(SensoryEffectType.COLD);
			            	
				        	duplicatedEffectCount = getDuplicatedNodeCount(droppedSensoryEffect);
					        if(duplicatedEffectCount > 0){
					        	droppedSensoryEffect.setName(SensoryEffectType.COLD.toString() + "_" + duplicatedEffectCount++);
					        }else{
					        	droppedSensoryEffect.setName(SensoryEffectType.COLD.toString());
					        }
					        
			                break;

						case HOT:

							droppedSensoryEffect.setType(SensoryEffectType.HOT);

							duplicatedEffectCount = getDuplicatedNodeCount(droppedSensoryEffect);
							if(duplicatedEffectCount > 0){
								droppedSensoryEffect.setName(SensoryEffectType.HOT.toString() + "_" + duplicatedEffectCount++);
							}else{
								droppedSensoryEffect.setName(SensoryEffectType.HOT.toString());
							}

							break;
			                
			            case VIBRATION:
			            	
			            	droppedSensoryEffect.setType(SensoryEffectType.VIBRATION);
			            	
				        	duplicatedEffectCount = getDuplicatedNodeCount(droppedSensoryEffect);
					        if(duplicatedEffectCount > 0){
					        	droppedSensoryEffect.setName(SensoryEffectType.VIBRATION.toString() + "_" + duplicatedEffectCount++);
					        }else{
					        	droppedSensoryEffect.setName(SensoryEffectType.VIBRATION.toString());
					        }
					        
			                break;
			                
			            case LIGHT:

			            	droppedSensoryEffect.setType(SensoryEffectType.LIGHT);
			            	
				        	duplicatedEffectCount = getDuplicatedNodeCount(droppedSensoryEffect);
					        if(duplicatedEffectCount > 0){
					        	droppedSensoryEffect.setName(SensoryEffectType.LIGHT.toString() + "_" + duplicatedEffectCount++);
					        }else{
					        	droppedSensoryEffect.setName(SensoryEffectType.LIGHT.toString());
					        }
					        
			                break;
			                
			            case RAINSTORM:
			            	
			            	droppedSensoryEffect.setType(SensoryEffectType.RAINSTORM);
			            	
				        	duplicatedEffectCount = getDuplicatedNodeCount(droppedSensoryEffect);
					        if(duplicatedEffectCount > 0){
					        	droppedSensoryEffect.setName(SensoryEffectType.RAINSTORM.toString() + "_" + duplicatedEffectCount++);
					        }else{
					        	droppedSensoryEffect.setName(SensoryEffectType.RAINSTORM.toString());
					        }
					        
			                break;
		                
		        	}
		        	
		        	try{
			        	
			        	if(temporalChainModel.getMasterNode() == null){
			        		
			        		Double droppedTime = 0.0;
				        	
			        		droppedSensoryEffect.setBegin(droppedTime);
			        		droppedSensoryEffect.setEnd(droppedSensoryEffect.getDuration());
				        	
				        	applicationController.setMasterNode(droppedSensoryEffect, temporalChainModel);
			        		
			        	} else{
			        	
			        		Double droppedTime = timeLineChart.getXAxis().getValueForDisplay(event.getX()).doubleValue();
			        		droppedTime = MediaUtil.approximateDouble(droppedTime - BORDER_DIFF);
			        		
			        		droppedSensoryEffect.setBegin(droppedTime);
			        		droppedSensoryEffect.setEnd(droppedTime + droppedSensoryEffect.getDuration());
				        	
			        		applicationController.addNodeTemporalChain(droppedSensoryEffect, temporalChainModel);
			        		
			        	}
			        	
			        	event.setDropCompleted(true);
			        	event.consume();
			        	
			        } catch (Exception e){
			        	
			        	event.setDropCompleted(false);
			        	Logger.getLogger(Media.class.getName()).log(Level.SEVERE, null, e);
			        	
			        }
		        	 
		        }else {

		        	Media droppedMedia = (Media) dragBoard.getContent((DataFormat) contentTypes[0]);
					droppedMedia.setParentTemporalChain(temporalChainModel);

		        	int duplicatedMediaCount = getDuplicatedNodeCount(droppedMedia);
			        if(duplicatedMediaCount > 0){
			        	droppedMedia.setName(droppedMedia.getName() + "_" + duplicatedMediaCount++);
			        }

			        try{
			        	
			        	if(temporalChainModel.getMasterNode() == null){
			        		
			        		Double droppedTime = 0.0;
				        	
				        	droppedMedia.setBegin(droppedTime);
				        	droppedMedia.setEnd(droppedMedia.getDuration());
				        	
				        	applicationController.setMasterNode(droppedMedia, temporalChainModel);
			        		
			        	} else{
			        	
			        		Double droppedTime = timeLineChart.getXAxis().getValueForDisplay(event.getX()).doubleValue();
			        		droppedTime = MediaUtil.approximateDouble(droppedTime - BORDER_DIFF);
			        		
				        	droppedMedia.setBegin(droppedTime);
				        	droppedMedia.setEnd(droppedTime + droppedMedia.getDuration());
				        	
			        		applicationController.addNodeTemporalChain(droppedMedia, temporalChainModel);
			        		
			        	}
			        	
			        	event.setDropCompleted(true);
			        	event.consume();
			        	
			        } catch (Exception e){
			        	
			        	event.setDropCompleted(false);
			        	Logger.getLogger(Media.class.getName()).log(Level.SEVERE, null, e);
			        	
			        }
		        }
		        
			}

			private int getDuplicatedNodeCount(model.common.Node droppedNode) {
				
				int i = 0;
				
				for(model.common.Node node : temporalChainModel.getNodeAllList()){
					if(node.getName().equalsIgnoreCase(droppedNode.getName())){
						i++;
					}
				}
				
				return i;
				
			}
			
		});
		
		setOnDragEntered(new EventHandler<DragEvent>() {
			
				public void handle(DragEvent dragEvent) {
					
					if(temporalChainModel.getMasterNode() != null){
						
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
				
				if(!controlButtonPane.getIsPlaying() && !controlButtonPane.getHasPaused()){
					screen.getChildren().clear();
					for(Media media : temporalChainModel.getMediaAllList()){
		    			media.setIsPLayingInPreview(false);
		    		}
				}

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
				playhead.setTranslateX(mouseEvent.getX() - ARROW_DIFF);
				hasClickedPlayhead = true;
	        }  
	    });
		
	}

	public void updateNodeStartTime(model.common.Node node) {



	}

	@Override
	public void update(Observable o, Object obj) {
		
		Operation<TemporalViewOperator> operation = (Operation<TemporalViewOperator>) obj;

		model.common.Node node;
		int line;
		Synchronous syncRelation;
		Interactivity<Media> interactivityRelation;
		TemporalChain temporalChainModel;
		DisplayPane displayPane = stevePane.getSpatialViewPane().getDisplayPane();
		ControlButtonPane controlButtonPane = displayPane.getControlButtonPane();
		
		switch(operation.getOperator()){
		
			case ADD_TEMPORAL_CHAIN_NODE:
				
				controlButtonPane.getPlayButton().setDisable(false);
				controlButtonPane.getRunButton().setDisable(false);
				
				node = (model.common.Node) operation.getOperating();
				
				line = (int) operation.getArg();
	            addTemporalChainNode(node, line);
	            
	            break;
	            
			case REMOVE_TEMPORAL_CHAIN_NODE:
				
				node = (model.common.Node) operation.getOperating();
				line = (int) operation.getArg();
	            removeTemporalChainNode(node, line);
	            
	            if(serie.getData().isEmpty()){
	            	controlButtonPane.getPlayButton().setDisable(true);
	            	controlButtonPane.getRunButton().setDisable(true);
	            }
	            
	            break;
	            
			case ADD_SYNC_RELATION:
				
				syncRelation = (Synchronous) operation.getOperating();
				temporalChainModel = (TemporalChain) operation.getArg();
	            addSyncRelation(syncRelation, temporalChainModel);
				
				break;
				
			case REMOVE_SYNC_RELATION:
				
				syncRelation = (Synchronous) operation.getOperating();
				temporalChainModel = (TemporalChain) operation.getArg();
	            removeSyncRelation(syncRelation, temporalChainModel);
				
				break;
			
			case REMOVE_SLAVE_NODE_OF_SYNC_RELATION:
				
				node = (model.common.Node) operation.getOperating();
				syncRelation = (Synchronous) operation.getArg();
	            removeSlaveMediaOfSyncRelation(node, syncRelation);
				
				break;
	        	
        	default:
        	
        		break;
        		
		}

	}
	
	private void addTemporalChainNode(model.common.Node node, int line){

		if(line > 4){

			yAxisCategoryList.add(0, String.valueOf(line));
			yAxis.setCategories(FXCollections.<String>observableArrayList(yAxisCategoryList));
	
		}
	
		TimeLineXYChartData timeLineXYChartData = new TimeLineXYChartData(applicationController, node, temporalChainModel, temporalViewPane, this,repositoryPane, line, stevePane, timeLineChart);
		serie.getData().add(timeLineXYChartData.getXYChartData());
		
		if(!timeLineXYChartDataLineList.isEmpty() && line < timeLineXYChartDataLineList.size()){
			
			ArrayList<TimeLineXYChartData> timeLineXYChartDataList = timeLineXYChartDataLineList.get(line);
			timeLineXYChartDataList.add(timeLineXYChartData);
			timeLineXYChartDataLineList.set(line, timeLineXYChartDataList);
			
		}else {
			
			ArrayList<TimeLineXYChartData> newTimeLineXYChartDataList = new ArrayList<TimeLineXYChartData>();
			newTimeLineXYChartDataList.add(timeLineXYChartData);
			timeLineXYChartDataLineList.add(line, newTimeLineXYChartDataList);
			
		}
		
	}
	
	private void removeTemporalChainNode(model.common.Node node, int line){

		ArrayList<TimeLineXYChartData> timeLineXYChartDataLine = timeLineXYChartDataLineList.get(line);
		int i = 0;
		Boolean removed = false;
		
		while(i < timeLineXYChartDataLine.size() && !removed){
			
			TimeLineXYChartData timeLineXYChartData = timeLineXYChartDataLine.get(i);
			
			if(timeLineXYChartData.getNode() == node){
				timeLineXYChartDataLine.remove(timeLineXYChartData);
				serie.getData().remove(timeLineXYChartData.getXYChartData());
				removed = true;
			}
			
			i++;
		}

	}
	
	private void addSyncRelation(Synchronous syncRelation, TemporalChain temporalChainModel){

		switch(syncRelation.getType()){
		
			case STARTS:
				
//	EXEMPLO DE USO PATH COM PLAYHEAD ------
				
//		    	heightProperty().addListener(new ChangeListener(){
//					@Override 
//			        public void changed(ObservableValue o,Object oldVal, Object newVal){
//						PathElement pathElement = playhead.getElements().get(5);
//						if(pathElement instanceof VLineTo){
//							((VLineTo) pathElement).setY(getHeight());
//						}
//					}
//			    });
			
//	FIM DO EXEMPLO--------------------------
				
//				Path arrow = new Path();
//				arrow.getElements().addAll(new MoveTo(0, 100), new LineTo(10, 100), new LineTo(5, 110), new ClosePath(), 
//						new MoveTo(5, 100), new LineTo(5, 30));
//				arrow.setId("relation-arrow");
//				getChildren().add(arrow);
//				createArrowListeners(arrow);
				
				
				/*for(XYChart.Data<Number, String> xyChartData : getSerie().getData()){
					
					HBox containerNode = (HBox) xyChartData.getNode();
					VBox nameInteractiveIconContainer = (VBox) containerNode.getChildren().get(1);
					Label mediaLabel = (Label) nameInteractiveIconContainer.getChildren().get(0);
					
					Path arrow = new Path();
					
					if(mediaLabel.getText().equalsIgnoreCase(syncRelation.getMasterMedia().getName())){
					
						VLineTo vline = new VLineTo();
						//arrow.getElements().addAll(new MoveTo(0, 300), new LineTo(15, 300), new LineTo(7.5, 313), new ClosePath(), new MoveTo(7.5, 313), new VLineTo());
//						vline.setY(10);
						arrow.getElements().addAll(new MoveTo(0, 1), new LineTo(15, 1), new LineTo(7.5, 12), new ClosePath(), new MoveTo(7.5, 313), new VLineTo());
						arrow.setId("relation-arrow");
						//getChildren().add(arrow);
				    	
				    	//INFO tamanho do caonteiner node:  BoundingBox [minX:0.0, minY:0.0, minZ:0.0, width:134.8, height:51.0, depth:0.0, maxX:134.8, maxY:51.0, maxZ:0.0]
				    	System.out.println("Posicao da midia (X): " + timeLineChart.getXAxis().getDisplayPosition(xyChartData.getXValue()));
				    	
				    	//INFO tamanho do temporal chain = 1366 326 (BoundingBox [minX:0.0, minY:0.0, minZ:0.0, width:1366.0, height:326.0, depth:0.0, maxX:1366.0, maxY:326.0, maxZ:0.0])
				    	//System.out.println("Tamanho do Temporal Chain Pane (X/Y): " + getLayoutBounds());
				    	
					}
					
					//TODO identificar o mestre
//					for(Media media : syncRelation.getSlaveMediaList()){
//						
//						if(mediaLabel.getText().equalsIgnoreCase(media.getName())){
//							
//							if(!containerNode.getStylesheets().contains("view/temporalViewPane/styles/borderOfMediaToBeStopped.css")){
//								
//								containerNode.getStylesheets().add("view/temporalViewPane/styles/borderOfMediaToBeStopped.css");
//								ImageView imageView = (ImageView) containerNode.getChildren().get(0);
//								Rectangle mediaImageClip = (Rectangle) imageView.getClip();
//								mediaImageClip.setHeight(mediaImageClip.getHeight()-5);
//								
//							}
//							
//						}
//						
//					}
					
				}*/
				
				break;
				
			case STARTS_DELAY:
	
	
				break;
				
			case FINISHES:
	
	
				break;
			
			case FINISHES_DELAY:
	
	
				break;
				
			case MEETS:
	
	
				break;
			
			case MEETS_DELAY:
	
	
				break;
			
			case MET_BY:
				
		
				break;
			
			case MET_BY_DELAY:
				
	
				
				break;
			
			case BEFORE:
	
				
				break;
		}

	}
	
	private void removeSyncRelation(Synchronous syncRelation, TemporalChain temporalChainModel){
		
		//TODO remover as setas das relações entre as mídias.

	}
	
	private void removeSlaveMediaOfSyncRelation(model.common.Node slaveNode, Synchronous syncRelation){
		
		//TODO remover as seta que aponta para a mídia escrava removida da relação.

	}
	
	private void createArrowListeners(Path arrow) {
		
		arrow.setOnKeyReleased(new EventHandler<KeyEvent>() {
	    	
	    	@Override
			public void handle(KeyEvent event) {
	    		
	    		if(event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE){
	    			
	    			System.out.println("Deletar Relação");
	    			//TODO deleta a relação selecionada
	    	
	    		}

	    	}
	    	
	    });
		arrow.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent mouseEvent) {
				
				if (mouseEvent.getButton() == MouseButton.SECONDARY && !arrowContextMenu.isShowing()) {

					System.out.println("Mostra Popub");
					
					menuItemDeleteRelation.setDisable(false); 
					arrowContextMenu.show(arrow, mouseEvent.getScreenX(), mouseEvent.getScreenY());
		
			     }
			}
			
	    });
		
	}
	
	private void createArrowPopupMenu(ApplicationController applicationController, TemporalChain temporalChainModel) {
		
		arrowContextMenu = new ContextMenu();
		menuItemDeleteRelation = new MenuItem (Language.translate("delete.relation"));
		arrowContextMenu.getItems().addAll(menuItemDeleteRelation);
		createArrowMenuItemActions(applicationController, temporalChainModel);
		
	}
	
	private void createArrowMenuItemActions(ApplicationController applicationController, TemporalChain temporalChainModel) {
		
		menuItemDeleteRelation.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				//TODO deletar relação selecionada
				
			}
		});

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

	public ArrayList<model.common.Node> getNodeListDuringAnother(model.common.Node firstSelectedMedia, TemporalChainPane temporalChainPane) {
		
		return getTemporalChainModel().getNodeListDuringAnother(firstSelectedMedia, temporalChainPane);
		
	}

	public TimeLineChart<Number, String> getTimeLineChart(){
		return this.timeLineChart;
	}
	public Path getPlayhead(){
		return this.playhead;
	}
	public Boolean getHasClickedPlayhead(){
		return this.hasClickedPlayhead;
	}
	public void setHasClickedPlayhead(Boolean value){
		this.hasClickedPlayhead = value;
	}
	public void setParentTab(Tab temporalChainTab) {
		parentTab = temporalChainTab;
	}
	
	public Tab getParentTab(){
		return parentTab;
	}

}
