package view.temporalViewPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.DepthTest;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.VLineTo;
import javafx.util.Duration;
import model.common.Media;
import model.common.SpatialTemporalView;
import model.spatialView.enums.AspectRatio;
import model.temporalView.Interactivity;
import model.temporalView.Synchronous;
import model.temporalView.TemporalChain;
import model.temporalView.TemporalRelation;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.MediaUtil;
import model.utility.Operation;
import view.repositoryPane.RepositoryPane;
import view.spatialViewPane.ControlButtonPane;
import view.spatialViewPane.DisplayPane;
import view.stevePane.StevePane;
import controller.Controller;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TemporalChainPane extends StackPane implements Observer{

	private static final int DIFF_INDICATIVE_LINE_AND_TEMPORAL_NODE = 7;
	private final int ICON_WIDTH = 40;

	private static final double ARROW_DIFF = 7.5;

	private static final double BORDER_DIFF = 0.26;

	private Controller controller;
	
	private TimeLineChart<Number, String> timeLineChart;
	private XYChart.Series<Number, String> serie;
	private SpatialTemporalView temporalViewModel;
	private TemporalChain temporalChainModel;
	private TemporalViewPane temporalViewPane;
	private RepositoryPane repositoryPane;
	private ArrayList<String> yAxisCategoryList = new ArrayList<String>();
	private Path indicativeLine;
	private Path playhead;
	private ArrayList<ArrayList<TimeLineXYChartData>> timeLineXYChartDataLineList = new ArrayList<ArrayList<TimeLineXYChartData>>();
	private StevePane stevePane;
	private Tab parentTab;
	private boolean isPlaying = false;
	private Double playheadPixelPosition = 0.0;
	private Double currentTime;
	private boolean itHasMediaView = false;
	private Boolean hasStopped = false;
	private Boolean hasPaused = false;
	private Object mediaContent;
	private DisplayPane displayPane;
	private ControlButtonPane controlButtonPane;
	private StackPane screen;
	NumberAxis xAxis;
	CategoryAxis yAxis;
	
	public TemporalChainPane(Controller controller, SpatialTemporalView temporalViewModel, TemporalChain temporalChainModel, TemporalViewPane temporalViewPane, RepositoryPane repositoryPane, StevePane stevePane){
    	
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
    	createDisplayPaneButtonActions();
    	createListeners();
    	
    	temporalChainModel.addObserver(this);
    	for(TemporalRelation relation: temporalChainModel.getRelationList()){
    		relation.addObserver(this);
    	}
    	
    	this.controller = controller;
    	
     }

	private void createDisplayPaneButtonActions(){
		
		DisplayPane displayPane = stevePane.getSpatialViewPane().getDisplayPane();
		ControlButtonPane controlButtonPane = displayPane.getControlButtonPane();
		
		controlButtonPane.getStopButton().setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {	
				
				controlButtonPane.getStopButton().setDisable(true);
				controlButtonPane.getPauseButton().setDisable(true);
				controlButtonPane.getPlayButton().setDisable(false);

	    		playhead.setTranslateX(timeLineChart.getXAxis().getDisplayPosition(0));
	    		hasStopped = true;
	    		hasPaused = false;
	    		isPlaying = false;
	    		for(Media media : temporalChainModel.getMediaAllList()){
	    			media.setIsPLayingInPreview(false);
	    		}
	    		
	    		Platform.runLater(new Runnable() {
					@Override
					public void run() {
						screen.getChildren().clear();
					}
				});

			}
			
		});
		

		controlButtonPane.getPauseButton().setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {	
				
				controlButtonPane.getStopButton().setDisable(false);
				controlButtonPane.getPauseButton().setDisable(true);
				controlButtonPane.getPlayButton().setDisable(false);
				
	    		hasPaused = true;
	    		isPlaying = false;
	    		
	    		for(Node executionObject : screen.getChildren()){
	    			if(executionObject instanceof MediaView){
	    				MediaView currentMediaView = (MediaView) executionObject;
	    				currentMediaView.getMediaPlayer().pause();
	    			}
	    		}
	    	
			}
			
		});
		
		controlButtonPane.getPlayButton().setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {	
				
				isPlaying = true;
				hasStopped = false;
				currentTime = timeLineChart.getXAxis().getValueForDisplay(playhead.getTranslateX()).doubleValue();
				if(hasPaused) {
					for(Node executionObject : screen.getChildren()){
		    			if(executionObject instanceof MediaView){
		    				MediaView currentMediaView = (MediaView) executionObject;
		    				Duration currentMillisDuration = new Duration(currentTime*1000);
		    				currentMediaView.getMediaPlayer().seek(currentMillisDuration);
		    				currentMediaView.getMediaPlayer().play();
		    			}
		    		}
				}
				hasPaused = false;

				controlButtonPane.getStopButton().setDisable(false);
				controlButtonPane.getPauseButton().setDisable(false);
				controlButtonPane.getPlayButton().setDisable(true);
				
				Runnable task = new Runnable()
        		{
        			public void run()
        			{
        				runTask();
        			}
        		};
        		
        		Thread backgroundThread = new Thread(task);
        		backgroundThread.setDaemon(true);
        		backgroundThread.start();
								
			}
			
		});		
	}
	
	public void runTask() {
		
		while(true && !hasStopped && !hasPaused) {
			
			try {
				
		    	playheadPixelPosition = timeLineChart.getXAxis().getDisplayPosition(currentTime); 
		    	playhead.setTranslateX(playheadPixelPosition);
		    	currentTime = currentTime + 0.1;
		    	System.out.println(currentTime);
		 
		    	if(currentTime > temporalChainModel.getMediaWithHighestEnd().getEnd()){
		    		controlButtonPane.getStopButton().fire();
		    		hasStopped = true;
		    		isPlaying = false;
		    		for(Media media : temporalChainModel.getMediaAllList()){
		    			media.setIsPLayingInPreview(false);
		    		}
		    	}
		    	
		    	DisplayPane displayPane = stevePane.getSpatialViewPane().getDisplayPane();
				StackPane screen = displayPane.getScreen();
				
				for(Media media : temporalChainModel.getMediaAllList()){

					if(media.getBegin() <= currentTime && currentTime <= media.getEnd()){
						
						if(!media.getIsPLayingInPreview()){
							
							Platform.runLater(new Runnable(){

								@Override
								public void run() {
									
									media.setIsPLayingInPreview(true);
									mediaContent = getMediaContent(media);
									
									if(mediaContent instanceof MediaView){
										//setVideoPresentationProperties((MediaView) mediaContent, media);
										screen.getChildren().add((MediaView) mediaContent);
									} else if(mediaContent instanceof ImageView){
										setImagePresentationProperties((ImageView) mediaContent, media);
										screen.getChildren().add((ImageView) mediaContent);
									}								
								}
								
							});
							
						}
							
					} else{
						
						if(media.getIsPLayingInPreview()){
							
							Platform.runLater(new Runnable(){
					    		@Override
								public void run() {
									
					    			if(!screen.getChildren().isEmpty()){
					    				screen.getChildren().remove(media.getExecutionObject());
										media.setIsPLayingInPreview(false);
					    			}
	
					    		}
					    	});
						}
						
					}
			
				}
				Thread.sleep(100);
				
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		}
	}
		
	
	private void createListeners(){
		
//		playhead.translateXProperty().addListener(new ChangeListener<Number>(){
//
//			@Override
//			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
////				
////				if(isPlaying){
////				
////					DisplayPane displayPane = stevePane.getSpatialViewPane().getDisplayPane();
////					StackPane screen = displayPane.getScreen();
//////					if(!screen.getChildren().isEmpty()){
//////						screen.getChildren().clear();
//////					}
////					
////					Double playheadTime = timeLineChart.getXAxis().getValueForDisplay(newValue.doubleValue()).doubleValue();
////			
////					for(Media media : temporalChainModel.getMediaAllList()){
////		
////						if(media.getBegin() <= playheadTime && playheadTime <= media.getEnd()){
////							
////							ImageView mediaContent = getMediaContent(media);
////							setPresentationProperties(mediaContent);	
////							screen.getChildren().add(mediaContent);
////							
////						}
////				
////					}
////					
////				}
//				
//			}
//			
//		});
		
		indicativeLine.translateXProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				
				if(!isPlaying && !hasPaused) {
					
					Double indicativeLine = timeLineChart.getXAxis().getValueForDisplay(newValue.doubleValue()).doubleValue();
					
					DisplayPane displayPane = stevePane.getSpatialViewPane().getDisplayPane();
					StackPane screen = displayPane.getScreen();

					for(Media media : temporalChainModel.getMediaAllList()){
						
						if(media.getBegin() <= indicativeLine && indicativeLine <= media.getEnd()){
							
							if(!media.getIsPLayingInPreview()){
				
								media.setIsPLayingInPreview(true);
								mediaContent = getMediaContent(media);
								
								if(mediaContent instanceof MediaView){
									//setVideoPresentationProperties((MediaView) mediaContent, media);
									screen.getChildren().add((MediaView) mediaContent);
								} else if(mediaContent instanceof ImageView){
									setImagePresentationProperties((ImageView) mediaContent, media);
									screen.getChildren().add((int) ((ImageView) mediaContent).getTranslateZ(),(ImageView) mediaContent);
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
	
	public void setImagePresentationProperties(ImageView mediaContent, Media media){
	
		DisplayPane displayPane = stevePane.getSpatialViewPane().getDisplayPane();
		StackPane screen = displayPane.getScreen();
		
		double percentageHeight, percentageWidth; 
		String temp;

	    if(media.getPresentationProperty().getSizeProperty().getAspectRatio()==AspectRatio.SLICE){
	    	mediaContent.setPreserveRatio(true);
	    } else {
	    	mediaContent.setPreserveRatio(false);
	    }
	    		
		double left = Double.parseDouble(media.getPresentationProperty().getPositionProperty().getLeft().replace("%", ""));
		double right = Double.parseDouble(media.getPresentationProperty().getPositionProperty().getRight().replace("%", ""));
		double top = Double.parseDouble(media.getPresentationProperty().getPositionProperty().getTop().replace("%", ""));
		double bottom = Double.parseDouble(media.getPresentationProperty().getPositionProperty().getBottom().replace("%", ""));		
		
		temp = media.getPresentationProperty().getSizeProperty().getHeight().replace("%", "");
		if (Double.parseDouble(temp)!=100){
			if(((left==0)&&(top==0))&&((right==0)&&(bottom==0))){
				left=0.1;
				top=0.1;
			}
		}
		percentageHeight = Double.parseDouble(temp);
		double screenHeight = screen.getHeight()*(percentageHeight/100);
		mediaContent.setFitHeight(screenHeight);
		
		temp = media.getPresentationProperty().getSizeProperty().getWidth().replace("%", "");			
		if (Double.parseDouble(temp)!=100){
			if(((left==0)&&(top==0))&&((right==0)&&(bottom==0))){
				left=0.1;
				top=0.1;
			}
		}
		percentageWidth = Double.parseDouble(temp);
		double screenWidth = screen.getWidth()*(percentageWidth/100);
		mediaContent.setFitWidth(screenWidth);
	
		//double borderLeft = mediaContent.prefHeight()/2;
		//double borderDown = mediaContent.getFitHeight()/2;
		int boundWidth = (int)mediaContent.getBoundsInParent().getWidth();
        int boundHeight = (int)mediaContent.getBoundsInParent().getHeight();
        	        	        
        screenWidth = screen.getWidth();
        screenHeight = screen.getHeight();
        double xZero = (+screenWidth/2)-boundWidth/2;
        
		double dXRight = 0; //como definir?
		if(right!=0){
			dXRight = (right/100)*screenWidth; 
			//System.out.println("xZero+dXRight = "+(xZero-dXRight));
			mediaContent.setTranslateX(xZero-dXRight);
		}
        
        xZero = (-screenWidth/2)+boundWidth/2;
		double dXLeft = 0; //como definir?
		if(left!=0){
			dXLeft = (left/100)*screenWidth; 
			//System.out.println("xZero+dXLeft = "+(xZero+dXLeft));
			mediaContent.setTranslateX(xZero+dXLeft);
		}
		
		double yZero = (screenHeight/2)-boundHeight/2;		
		
		double dYDown = 0; //como definir?
		if(bottom!=0){
			dYDown = (bottom/100)*screenHeight; 
			//System.out.println("yZero-dYDown = "+(yZero-dYDown));
			mediaContent.setTranslateY(yZero-dYDown);
		}
		
		yZero = (-screenHeight/2)+boundHeight/2;

		double dYTop = 0; //como definir?
		if(top!=0){
			dYTop = (top/100)*screenHeight; 
			//System.out.println("yZero+dYTop = "+(yZero+dYTop));
			mediaContent.setTranslateY(yZero+dYTop);
			
		}

//		System.out.println(media.getPresentationProperty().getPositionProperty().getOrderZ());
//				System.out.println(mediaContent.getTranslateZ());
		mediaContent.setDepthTest(DepthTest.ENABLE);
		mediaContent.setTranslateZ(media.getPresentationProperty().getPositionProperty().getOrderZ());
		
		
		double opacity = 1-(media.getPresentationProperty().getStyleProperty().getTransparency()/100);
		mediaContent.setOpacity(opacity);
		
	
	}

	private Object getMediaContent(Media media){

		mediaContent = null;
		
		switch(media.getMediaType()) {
		   
   		case IMAGE:
   			
   			ImageView image = new ImageView(new Image(media.getFile().toURI().toString()));
   			image.setFitWidth(screen.getWidth());
   			image.setFitHeight(screen.getHeight());
   			image.setSmooth(false);
   			mediaContent = image;
   			media.setExecutionObject(mediaContent);
   			break;
           
		case VIDEO:

			URL mediaUrl = getClass().getResource("Test.mp4");
//			URL mediaUrl = getClass().getResource(media.getFile().getAbsolutePath());
			String mediaStringUrl = mediaUrl.toExternalForm();
			final javafx.scene.media.Media m = new javafx.scene.media.Media(mediaStringUrl);
			final MediaPlayer player = new MediaPlayer(m);
			MediaView mediaView = new MediaView(player);
			mediaView.setFitWidth(screen.getWidth());
			mediaView.setFitHeight(screen.getHeight()); 
			mediaView.setSmooth(true);
			
			if(isPlaying){
				player.play();
				mediaContent = mediaView;
			} else {
				mediaContent = mediaView;
				
				player.statusProperty().addListener(new ChangeListener<MediaPlayer.Status>() {
							
				    public void changed(ObservableValue<? extends MediaPlayer.Status> ov,
				            final MediaPlayer.Status oldStatus, final MediaPlayer.Status newStatus) {
				    	
				    	Double frameMillisTime = (media.getDuration()/2)*1000;
						System.out.println(frameMillisTime.intValue());
				    	Duration duration = new Duration(frameMillisTime.intValue()); 
				    	player.seek(duration);
				    	
				    }
				});
			}
			media.setExecutionObject(mediaContent);
   			break;

		case AUDIO:
			//INFO símbolo de áudio apenas. Não tocar.
			ImageView imageAudio = new ImageView(new Image(getClass().getResourceAsStream("/view/repositoryPane/images/audioNode.png")));
			imageAudio.setPreserveRatio(true);
			imageAudio.setSmooth(true);
			imageAudio.setFitWidth(ICON_WIDTH);
			mediaContent = media.generateMediaIcon(); 
			break; 
       
		case TEXT:
			//TODO pegar o texto.
			break;
               
		case APPLICATION:
			mediaContent = media.generateMediaIcon();
			break;                
		}
		
		
		return mediaContent; 
		
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
		        	Logger.getLogger(Media.class.getName()).log(Level.SEVERE, null, e);
		        	
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
				
				if(hasStopped){
					for(Media media : temporalChainModel.getMediaAllList()){
		    			media.setIsPLayingInPreview(false);
		    		}
				}
				
				if(!isPlaying && !hasPaused){
					screen.getChildren().clear();
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
	        }  
	    });
		
	}
	
	@Override
	public void update(Observable o, Object obj) {
		
		Operation<TemporalViewOperator> operation = (Operation<TemporalViewOperator>) obj;

		Media media;
		int line;
		Synchronous<Media> syncRelation;
		Interactivity<Media, ?> interactivityRelation;
		TemporalChain temporalChainModel;
		DisplayPane displayPane = stevePane.getSpatialViewPane().getDisplayPane();
		ControlButtonPane controlButtonPane = displayPane.getControlButtonPane();
		
		switch(operation.getOperator()){
		
			case ADD_TEMPORAL_CHAIN_MEDIA:
				
				controlButtonPane.getPlayButton().setDisable(false);
				controlButtonPane.getRunButton().setDisable(false);
				
				media = (Media) operation.getOperating();
				line = (int) operation.getArg();
	            addTemporalChainMedia(media, line);
	            
	            break;
	            
			case REMOVE_TEMPORAL_CHAIN_MEDIA:
				
				media = (Media) operation.getOperating();
				line = (int) operation.getArg();
	            removeTemporalChainMedia(media, line);
	            
	            if(serie.getData().isEmpty()){
	            	controlButtonPane.getPlayButton().setDisable(true);
	            	controlButtonPane.getRunButton().setDisable(true);
	            }
	            
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

		switch(syncRelation.getType()){
		
			case STARTS:
	
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

	public void setParentTab(Tab temporalChainTab) {
		parentTab = temporalChainTab;
	}
	
	public Tab getParentTab(){
		return parentTab;
	}
	
}
