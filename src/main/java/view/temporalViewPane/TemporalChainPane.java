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
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PopupControl;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaView;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.VLineTo;
import javafx.util.Duration;
import model.common.MediaNode;
import model.common.SensoryEffectNode;
import model.common.SpatialTemporalApplication;
import model.common.enums.MediaType;
import model.common.enums.SensoryEffectType;
import model.temporalView.Interactivity;
import model.temporalView.Synchronous;
import model.temporalView.TemporalChain;
import model.temporalView.TemporalRelation;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.MediaUtil;
import model.utility.Operation;
import view.common.Language;
import view.common.dialogs.ReturnMessage;
import view.repositoryPane.RepositoryPane;
import view.spatialViewPane.ControlButtonPane;
import view.spatialViewPane.DisplayPane;
import view.stevePane.SteveScene;
import controller.ApplicationController;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TemporalChainPane extends StackPane implements Observer{

	private static final int DIFF_INDICATIVE_LINE_AND_TEMPORAL_NODE = 7;
	private static final double ARROW_DIFF = 7.5;

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
	private SteveScene steveScene;
	private Tab parentTab;
	private Boolean hasClickedPlayhead = false;
	private DisplayPane displayPane;
	private ControlButtonPane controlButtonPane;
	private ContextMenu arrowContextMenu;
	private MenuItem menuItemDeleteRelation;
	private StackPane screen;
	private NumberAxis xAxis;
	private CategoryAxis yAxis;
	private ReturnMessage currentTimePopup;
	
	public TemporalChainPane(ApplicationController applicationController, SpatialTemporalApplication temporalViewModel, TemporalChain temporalChainModel, TemporalViewPane temporalViewPane, RepositoryPane repositoryPane, SteveScene steveScene){
    	
		xAxis = new NumberAxis();
    	xAxis.setAutoRanging(false);

    	yAxis = new CategoryAxis();
    	yAxis.setId("axis-y");
    	yAxisCategoryList.addAll(FXCollections.<String>observableArrayList("4", "3", "2", "1", "0"));
    	yAxis.setCategories(FXCollections.<String>observableArrayList(yAxisCategoryList));

		timeLineChart = new TimeLineChart<Number, String>(xAxis, yAxis);
		serie = new XYChart.Series<Number, String>();
		timeLineChart.getData().addAll(serie);

		currentTimePopup = new ReturnMessage(55, 30);

		getChildren().add(timeLineChart);
		
    	setId(String.valueOf(temporalChainModel.getId()));
    	setAlignment(Pos.TOP_LEFT);
    	
    	this.temporalViewModel = temporalViewModel;
    	this.temporalChainModel = temporalChainModel;
    	this.temporalViewPane = temporalViewPane;
    	this.repositoryPane = repositoryPane;
    	this.steveScene = steveScene;

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

		displayPane = steveScene.getSpatialViewPane().getDisplayPane();
		controlButtonPane = displayPane.getControlButtonPane();
		screen = displayPane.getScreen();

    	createDragAndDropEvent();
    	createMouseEvent();
    	createArrowPopupMenu(applicationController, temporalChainModel);
    	
    	temporalChainModel.addObserver(this);
    	for(TemporalRelation relation: temporalChainModel.getRelationList()){
    		relation.addObserver(this);
    	}
    	
    	this.applicationController = applicationController;
    	
     }
	
	private void createDragAndDropEvent() {

		setOnDragDropped(new EventHandler<DragEvent>() {
			
			public void handle(DragEvent event) {

				Dragboard dragBoard = event.getDragboard();
		        Object[] contentTypes = dragBoard.getContentTypes().toArray();
		        Object droppedNode = dragBoard.getContent((DataFormat) contentTypes[0]);

		        if(droppedNode instanceof SensoryEffectType) {
		        	
		        	SensoryEffectType sensoryEffectType = (SensoryEffectType) dragBoard.getContent((DataFormat) contentTypes[0]);
					SensoryEffectNode droppedSensoryEffect = new SensoryEffectNode(sensoryEffectType);

		        	int duplicatedEffectCount;

		        	switch(sensoryEffectType){
		        	
			            case WIND:

							droppedSensoryEffect.setParentTemporalChain(temporalChainModel);
			            	droppedSensoryEffect.setType(SensoryEffectType.WIND);

				        	duplicatedEffectCount = getDuplicatedEffectNodeCount(droppedSensoryEffect);
							duplicatedEffectCount++;
					        if(duplicatedEffectCount > 1){
					        	droppedSensoryEffect.setName(SensoryEffectType.WIND.toString() + "_" + duplicatedEffectCount);
					        }else{
								droppedSensoryEffect.setName(SensoryEffectType.WIND.toString());
							}
					        
			                break;
			                
			            case WATER_SPRAYER:

							droppedSensoryEffect.setParentTemporalChain(temporalChainModel);
			            	droppedSensoryEffect.setType(SensoryEffectType.WATER_SPRAYER);
			            	
				        	duplicatedEffectCount = getDuplicatedEffectNodeCount(droppedSensoryEffect);
							duplicatedEffectCount++;
							if(duplicatedEffectCount > 1){
					        	droppedSensoryEffect.setName(SensoryEffectType.WATER_SPRAYER.toString() + "_" + duplicatedEffectCount);
					        }else{
					        	droppedSensoryEffect.setName(SensoryEffectType.WATER_SPRAYER.toString());
					        }
					        
			                break;
			                
			            case FOG:

							droppedSensoryEffect.setParentTemporalChain(temporalChainModel);
			            	droppedSensoryEffect.setType(SensoryEffectType.FOG);
			            	
				        	duplicatedEffectCount = getDuplicatedEffectNodeCount(droppedSensoryEffect);
							duplicatedEffectCount++;
							if(duplicatedEffectCount > 1){
					        	droppedSensoryEffect.setName(SensoryEffectType.FOG.toString() + "_" + duplicatedEffectCount);
					        }else{
					        	droppedSensoryEffect.setName(SensoryEffectType.FOG.toString());
					        }
					        
			                break;
			                
			            case FLASH:

							droppedSensoryEffect.setParentTemporalChain(temporalChainModel);
			            	droppedSensoryEffect.setType(SensoryEffectType.FLASH);
			            	
				        	duplicatedEffectCount = getDuplicatedEffectNodeCount(droppedSensoryEffect);
							duplicatedEffectCount++;
							if(duplicatedEffectCount > 1){
					        	droppedSensoryEffect.setName(SensoryEffectType.FLASH.toString() + "_" + duplicatedEffectCount);
					        }else{
					        	droppedSensoryEffect.setName(SensoryEffectType.FLASH.toString());
					        }
					        
			                break;
			                
			            case SCENT:

							droppedSensoryEffect.setParentTemporalChain(temporalChainModel);
			            	droppedSensoryEffect.setType(SensoryEffectType.SCENT);
			            	
				        	duplicatedEffectCount = getDuplicatedEffectNodeCount(droppedSensoryEffect);
							duplicatedEffectCount++;
							if(duplicatedEffectCount > 1){
					        	droppedSensoryEffect.setName(SensoryEffectType.SCENT.toString() + "_" + duplicatedEffectCount);
					        }else{
					        	droppedSensoryEffect.setName(SensoryEffectType.SCENT.toString());
					        }
					        
			                break;
			                
			            case COLD:

							droppedSensoryEffect.setParentTemporalChain(temporalChainModel);
			            	droppedSensoryEffect.setType(SensoryEffectType.COLD);
			            	
				        	duplicatedEffectCount = getDuplicatedEffectNodeCount(droppedSensoryEffect);
							duplicatedEffectCount++;
					        if(duplicatedEffectCount > 1){
					        	droppedSensoryEffect.setName(SensoryEffectType.COLD.toString() + "_" + duplicatedEffectCount);
					        }else{
					        	droppedSensoryEffect.setName(SensoryEffectType.COLD.toString());
					        }
					        
			                break;

						case HOT:

							droppedSensoryEffect.setParentTemporalChain(temporalChainModel);
							droppedSensoryEffect.setType(SensoryEffectType.HOT);

							duplicatedEffectCount = getDuplicatedEffectNodeCount(droppedSensoryEffect);
							duplicatedEffectCount++;
							if(duplicatedEffectCount > 1){
								droppedSensoryEffect.setName(SensoryEffectType.HOT.toString() + "_" + duplicatedEffectCount);
							}else{
								droppedSensoryEffect.setName(SensoryEffectType.HOT.toString());
							}

							break;
			                
			            case VIBRATION:

							droppedSensoryEffect.setParentTemporalChain(temporalChainModel);
			            	droppedSensoryEffect.setType(SensoryEffectType.VIBRATION);
			            	
				        	duplicatedEffectCount = getDuplicatedEffectNodeCount(droppedSensoryEffect);
							duplicatedEffectCount++;
							if(duplicatedEffectCount > 1){
					        	droppedSensoryEffect.setName(SensoryEffectType.VIBRATION.toString() + "_" + duplicatedEffectCount);
					        }else{
					        	droppedSensoryEffect.setName(SensoryEffectType.VIBRATION.toString());
					        }
					        
			                break;
			                
			            case LIGHT:

							droppedSensoryEffect.setParentTemporalChain(temporalChainModel);
			            	droppedSensoryEffect.setType(SensoryEffectType.LIGHT);
			            	
				        	duplicatedEffectCount = getDuplicatedEffectNodeCount(droppedSensoryEffect);
							duplicatedEffectCount++;
							if(duplicatedEffectCount > 1){
					        	droppedSensoryEffect.setName(SensoryEffectType.LIGHT.toString() + "_" + duplicatedEffectCount);
					        }else{
					        	droppedSensoryEffect.setName(SensoryEffectType.LIGHT.toString());
					        }
					        
			                break;
			                
			            case RAINSTORM:

							droppedSensoryEffect.setParentTemporalChain(temporalChainModel);
			            	droppedSensoryEffect.setType(SensoryEffectType.RAINSTORM);
			            	
				        	duplicatedEffectCount = getDuplicatedEffectNodeCount(droppedSensoryEffect);
							duplicatedEffectCount++;
							if(duplicatedEffectCount > 1){
					        	droppedSensoryEffect.setName(SensoryEffectType.RAINSTORM.toString() + "_" + duplicatedEffectCount);
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
							applicationController.addNodeTemporalChain(droppedSensoryEffect, temporalChainModel);
			        		
			        	} else{
			        	
			        		Double droppedTime = timeLineChart.getXAxis().getValueForDisplay(event.getX()).doubleValue();
			        		droppedTime = MediaUtil.approximateDouble(droppedTime);
			        		
			        		droppedSensoryEffect.setBegin(droppedTime);
			        		droppedSensoryEffect.setEnd(droppedTime + droppedSensoryEffect.getDuration());
				        	
			        		applicationController.addNodeTemporalChain(droppedSensoryEffect, temporalChainModel);
			        		
			        	}
			        	
			        	event.setDropCompleted(true);
			        	event.consume();
			        	
			        } catch (Exception e){
			        	
			        	event.setDropCompleted(false);
			        	Logger.getLogger(MediaNode.class.getName()).log(Level.SEVERE, null, e);
			        	
			        }
		        	 
		        }else {

		        	MediaNode droppedMediaNode = (MediaNode) dragBoard.getContent((DataFormat) contentTypes[0]);
					droppedMediaNode.setParentTemporalChain(temporalChainModel);
					droppedMediaNode.prefetchExecutionObject(applicationController.getScreen());

		        	int duplicatedMediaCount = getDuplicatedMediaNodeCount(droppedMediaNode);
					duplicatedMediaCount++;
			        if(duplicatedMediaCount > 1){
			        	droppedMediaNode.setName(droppedMediaNode.getName() + "_" + duplicatedMediaCount);
			        }

			        try{
			        	
			        	if(temporalChainModel.getMasterNode() == null){
			        		
			        		Double droppedTime = 0.0;
				        	
				        	droppedMediaNode.setBegin(droppedTime);
				        	droppedMediaNode.setEnd(droppedMediaNode.getDuration());
				        	
				        	applicationController.setMasterNode(droppedMediaNode, temporalChainModel);
							applicationController.addNodeTemporalChain(droppedMediaNode, temporalChainModel);
			        		
			        	} else{
			        	
			        		Double droppedTime = timeLineChart.getXAxis().getValueForDisplay(event.getX()).doubleValue();
			        		droppedTime = MediaUtil.approximateDouble(droppedTime);
			        		
				        	droppedMediaNode.setBegin(droppedTime);
				        	droppedMediaNode.setEnd(droppedTime + droppedMediaNode.getDuration());
				        	
			        		applicationController.addNodeTemporalChain(droppedMediaNode, temporalChainModel);
			        		
			        	}
			        	
			        	event.setDropCompleted(true);
			        	event.consume();
			        	
			        } catch (Exception e){
			        	
			        	event.setDropCompleted(false);
			        	Logger.getLogger(MediaNode.class.getName()).log(Level.SEVERE, null, e);
			        	
			        }
		        }
		        
			}

			private int getDuplicatedMediaNodeCount(MediaNode droppedMediaNode){

				int countSameMedia=0;

				for(MediaNode mediaNode : temporalChainModel.getMediaNodeAllList()){
					if(mediaNode.getPath().equalsIgnoreCase(droppedMediaNode.getPath())){
						countSameMedia++;
					}
				}

				return countSameMedia;

			}

			private int getDuplicatedEffectNodeCount(SensoryEffectNode droppedEffectNode) {

				int countSameEffect=0;

				for(SensoryEffectNode sensoryEffectNode : temporalChainModel.getSensoryEffectNodeAllList()){
					if(droppedEffectNode.getType() == sensoryEffectNode.getType()){
						countSameEffect++;
					}
				}

				return countSameEffect;
				
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
				currentTimePopup.close();
				getChildren().remove(indicativeLine);
	        }  
			
		});
		
		setOnDragOver(new EventHandler<DragEvent>() {
			
			public void handle(DragEvent dragEvent) {

				Object[] contentTypes = dragEvent.getDragboard().getContentTypes().toArray();

				indicativeLine.setTranslateX(dragEvent.getX() + DIFF_INDICATIVE_LINE_AND_TEMPORAL_NODE);

				Double currentTimeWhileDragging = timeLineChart.getXAxis().getValueForDisplay(dragEvent.getX()).doubleValue();
				currentTimeWhileDragging = MediaUtil.approximateDouble(currentTimeWhileDragging);

				currentTimePopup.setMessage(String.valueOf(currentTimeWhileDragging));
				currentTimePopup.setCursor(Cursor.H_RESIZE);
				currentTimePopup.setX(dragEvent.getSceneX() + 4);

				if(dragEvent.getDragboard().getContent((DataFormat) contentTypes[0]) instanceof SensoryEffectType) {
					currentTimePopup.setY(dragEvent.getSceneY() - 5);
				}else{
					currentTimePopup.setY(dragEvent.getSceneY() - 15);
				}

				currentTimePopup.show();
				
				if (dragEvent.getDragboard().hasContent((DataFormat) contentTypes[0])) {
                   dragEvent.acceptTransferModes(TransferMode.COPY);
               }
               
               dragEvent.consume();
	        }  
	    });

	}
	
	public void createMouseEvent(){

		setOnMouseReleased(new EventHandler<MouseEvent>() {
			
			public void handle(MouseEvent mouseEvent) {
				currentTimePopup.close();
			}
		});
		
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			
			public void handle(MouseEvent mouseEvent) {
				playhead.setTranslateX(mouseEvent.getX() - ARROW_DIFF);
				hasClickedPlayhead = true;
				updatePreviewScreen(mouseEvent.getSceneX() - ARROW_DIFF);
	        }  
	    });
		
	}

	public void updatePreviewScreen(double xPosition){

		Double currentTimeClicked = timeLineChart.getXAxis().getValueForDisplay(xPosition).doubleValue();

		if(controlButtonPane.getTimerService() != null){
			controlButtonPane.getTimerService().setCurrentTime(currentTimeClicked);
		}

		for(model.common.Node node : temporalChainModel.getNodeAllList()){

			if(node.getBegin() <= currentTimeClicked && currentTimeClicked <= node.getEnd()){

				if(!node.getIsShownInPreview()){

					if(node instanceof MediaNode) {

						Object mediaContent = node.getExecutionObject();

						if(mediaContent instanceof MediaView){

							controlButtonPane.setVideoPresentationProperties((MediaView) mediaContent, (MediaNode) node);
							MediaView mediaView = (MediaView) mediaContent;

							if(!screen.getChildren().contains(mediaView)){
								screen.getChildren().add(mediaView);
							}

						} else if(mediaContent instanceof ImageView){

							controlButtonPane.setImagePresentationProperties((ImageView) mediaContent, (MediaNode) node);
							if(screen.getChildren().isEmpty()){

								if(!screen.getChildren().contains((ImageView) mediaContent)){
									screen.getChildren().add((ImageView) mediaContent);
									node.setIsShownInPreview(true);
								}


							} else{
								boolean inserted = false;
								for(Node executionObject : screen.getChildren()){

									if(((ImageView) mediaContent).getTranslateZ() < executionObject.getTranslateZ()){
										screen.getChildren().add(screen.getChildren().indexOf(executionObject), (ImageView) mediaContent);
										node.setIsShownInPreview(true);
										inserted = true;
										break;
									}

								}
								if(!inserted){
									screen.getChildren().add((ImageView) mediaContent);
									node.setIsShownInPreview(true);
								}
							}

						}

					} else if(node instanceof SensoryEffectNode){

						Object nodeContent = controlButtonPane.getSensoryEffectIcon((SensoryEffectNode) node);
						HBox effectIconsContainer = controlButtonPane.getEffectIconsContainer();
						effectIconsContainer.getChildren().add((ImageView) nodeContent);
						node.setIsShownInPreview(true);

						if(!screen.getChildren().contains(effectIconsContainer)){
							screen.getChildren().add(effectIconsContainer);
						}

					}

				}else if(node.isContinousMedia()){
					//INFO If node is a continuous media, the video frame in preview needed to be updated.

					MediaNode mediaNode = (MediaNode) node;
					Double videoCurrentTime = currentTimeClicked - mediaNode.getBegin();
					Double frameMillisTime = videoCurrentTime*1000;
					Duration duration = new Duration(frameMillisTime.intValue());
					((MediaView) mediaNode.getExecutionObject()).getMediaPlayer().seek(duration);

				}

			}else {

				if(!screen.getChildren().isEmpty()){

					if(node instanceof SensoryEffectNode){
						controlButtonPane.getEffectIconsContainer().getChildren().remove(node.getExecutionObject());
					}else{

						screen.getChildren().remove(node.getExecutionObject());

					}
					node.setIsShownInPreview(false);
					node.setIsContinuousMediaPlaying(false);
				}

			}

		}

	}

	@Override
	public void update(Observable o, Object obj) {
		
		Operation<TemporalViewOperator> operation = (Operation<TemporalViewOperator>) obj;

		model.common.Node node;
		int line;
		Synchronous syncRelation;
		Interactivity<MediaNode> interactivityRelation;
		TemporalChain temporalChainModel = null;
		DisplayPane displayPane = steveScene.getSpatialViewPane().getDisplayPane();
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
	            if(this.temporalChainModel.getRelationList().isEmpty()){
	            	if(temporalViewPane.getTemporalViewButtonPane().getShowNodesLinkedCheckBox().isSelected()){
						temporalViewPane.getTemporalViewButtonPane().getShowNodesLinkedCheckBox().fire();
					}
					temporalViewPane.getTemporalViewButtonPane().getShowNodesLinkedCheckBox().setDisable(true);
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
			
			case REMOVE_SECONDARY_NODE_OF_SYNC_RELATION:
				
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
	
		TimeLineXYChartData timeLineXYChartData = new TimeLineXYChartData(applicationController, node, temporalChainModel, temporalViewPane, this,repositoryPane, line, steveScene, timeLineChart);
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
				timeLineXYChartData.getContainerNode().getChildren().clear();
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
