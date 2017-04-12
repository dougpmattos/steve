package view.temporalViewPane;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.SAXException;

import javafx.animation.*;
import javafx.application.Platform;
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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.VLineTo;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import model.common.Media;
import model.common.SpatialTemporalView;
import model.temporalView.Interactivity;
import model.temporalView.TemporalRelation;
import model.temporalView.Synchronous;
import model.temporalView.TemporalChain;
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
	private Double currentTime = 0.0;
	private boolean stopped = false;
	
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
	
//	private static void myTask() {
//	    System.out.println("Running");
//	}
//	
	private void installEventHandler(final Node keyNode) {
	    // handler for enter key press / release events, other keys are
	    // handled by the parent (keyboard) node handler
	    final EventHandler<KeyEvent> keyEventHandler =
	        new EventHandler<KeyEvent>() {
	            public void handle(final KeyEvent keyEvent) {
//	                if (keyEvent.getCode() == KeyCode.ENTER) {
//	                    setPressed(keyEvent.getEventType()
//	                        == KeyEvent.KEY_PRESSED);
//	 
//	                    keyEvent.consume();
//	                }
	            	//System.out.println("KEY PRESSED\nKEY PRESSED\nKEY PRESSED\nKEY PRESSED\nKEY PRESSED\nKEY PRESSED\nKEY PRESSED\n");
	            	playhead.setVisible(false);
	            }
	        };
	 
	    keyNode.setOnKeyPressed(keyEventHandler);
	    keyNode.setOnKeyReleased(keyEventHandler);
	}

	private void createDisplayPaneButtonActions(){
		
		DisplayPane displayPane = stevePane.getSpatialViewPane().getDisplayPane();
		ControlButtonPane controlButtonPane = displayPane.getControlButtonPane();
		StackPane screen = displayPane.getScreen();
		
		installEventHandler(screen);
		
		controlButtonPane.getPauseButton().setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {	
				
				controlButtonPane.getStopButton().setDisable(false);
				controlButtonPane.getPauseButton().setDisable(true);
				controlButtonPane.getPlayButton().setDisable(false);
				
			}
			
		});
		
		controlButtonPane.getPlayButton().setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {	
				
				isPlaying = true;
				currentTime = 0.0;
				controlButtonPane.getStopButton().setDisable(false);
				controlButtonPane.getPauseButton().setDisable(false);
				controlButtonPane.getPlayButton().setDisable(true);
				
//				---TESTE
				Runnable task = new Runnable()
        		{
        			public void run()
        			{
        				runTask();
        			}
        		};
        		
        		// Run the task in a background thread
        		Thread backgroundThread = new Thread(task);
        		// Terminate the running thread if the application exits
        		backgroundThread.setDaemon(true);
        		// Start the thread
        		backgroundThread.start();
//				----FIM TESTE
		
				/*//controlButtonPane.runPreviewScreen(); // Roda as midias na tela 
				ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
				
				executorService.scheduleAtFixedRate(new Runnable() {
		        @Override
		        	public void run() {
		        
		        	//INFO Dado o tempo, obtenho pixel para transladar com o playhead
		        	playheadPixelPosition = timeLineChart.getXAxis().getDisplayPosition(currentTime); 
		        	playhead.setTranslateX(playheadPixelPosition);
		        	currentTime = currentTime + 0.01;
		        	System.out.println(currentTime);
		     
					DisplayPane displayPane = stevePane.getSpatialViewPane().getDisplayPane();
					StackPane screen = displayPane.getScreen();
					if(!screen.getChildren().isEmpty()){
						screen.getChildren().clear();
					}
			
//					for(Media media : temporalChainModel.getMediaAllList()){
//		
//						if(media.getBegin() <= currentTime && currentTime <= media.getEnd()){
////							//TODO problema esta qui muito pesado a cada milisegindp atualizar: dimiuir a txa do execeute ou pesquisar sobre treah paralelas e desempenho
//							Platform.runLater(new Runnable(){
//
//								@Override
//								public void run() {
//									ImageView mediaContent = getMediaContent(media);
//									//setPresentationProperties(mediaContent);	
//									screen.getChildren().add(mediaContent);
//									
//								}
//								
//							});
//								
//						}
//				
//					}
					
					//INFO quando chega em 10s, para
//					if(currentTime >= 10){
//		        		executorService.shutdownNow();
//		        	}
						
//					----BRUNO-----
		        	//Double playheadTime = timeLineChart.getXAxis().getValueForDisplay(newValue.doubleValue()).doubleValue();
		        	
//			        	playheadPosition+=0.27;
//						playhead.setTranslateX(playheadPosition);
//						Media lastMedia = temporalChainModel.getMediaWithHighestEnd();
//						System.out.println("PlayheadPosition = "+playheadPosition/135 +" > "+lastMedia.getEnd()/5+" last media end");
//						if((playheadPosition/13.5 > lastMedia.getEnd()/0.5)||(stopped==true)){
//							System.out.println("PlayheadPosition = "+playheadPosition/135 +" > "+lastMedia.getEnd()/5+" last media end");
//							executorService.shutdownNow();
//							//controlButtonPane.hideWebView();
//							playheadPosition = 0;
//							stopped = false;
//							playhead.setTranslateX(playheadPosition);
//							playhead.setVisible(true);
//						}
			        }
			}, 0, 10, TimeUnit.MILLISECONDS);*/	
								
			}
			
		});		
	}
	
	public void runTask() 
	{
		while(true){
			try 
			{
				
				//INFO Dado o tempo, obtenho pixel para transladar com o playhead
		    	playheadPixelPosition = timeLineChart.getXAxis().getDisplayPosition(currentTime); 
		    	playhead.setTranslateX(playheadPixelPosition);
		    	currentTime = currentTime + 0.01;
		    	System.out.println(currentTime);
		 
				DisplayPane displayPane = stevePane.getSpatialViewPane().getDisplayPane();
				StackPane screen = displayPane.getScreen();
//				if(!screen.getChildren().isEmpty()){
//					screen.getChildren().clear();
//				}

				for(Media media : temporalChainModel.getMediaAllList()){

					if(media.getBegin() <= currentTime && currentTime <= media.getEnd()){
//						//TODO problema esta qui muito pesado a cada milisegindp atualizar: dimiuir a txa do execeute ou pesquisar sobre treah paralelas e desempenho
						Platform.runLater(new Runnable(){

							@Override
							public void run() {
								ImageView mediaContent = getMediaContent(media);
								//setPresentationProperties(mediaContent);	
								screen.getChildren().add(mediaContent);
								
							}
							
						});
							
					}
			
				}
				Thread.sleep(10);
				
			}
			catch (InterruptedException e) 
			{
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
				
				DisplayPane displayPane = stevePane.getSpatialViewPane().getDisplayPane();
				StackPane screen = displayPane.getScreen();
				screen.getChildren().clear();
				
				Double indicativeLine = timeLineChart.getXAxis().getValueForDisplay(newValue.doubleValue()).doubleValue();
		
				for(Media media : temporalChainModel.getMediaAllList()){
	
					if(media.getBegin() <= indicativeLine && indicativeLine <= media.getEnd()){
						
						ImageView mediaContent = getMediaContent(media);
						setPresentationProperties(mediaContent);	
						screen.getChildren().add(mediaContent);
						
					}
			
				}

			}
			
		});
		
		
	}
	
	private void setPresentationProperties(ImageView mediaContent){
		//TODO formtar conteuco da midia de acordo com as propriedade de apresentação.
		mediaContent.setFitWidth(300);
	}
	
	private ImageView getMediaContent(Media media){
		
		ImageView mediaContent = null;
		
		switch(media.getMediaType()) {
		   
   		case IMAGE:
//   			mediaContent = new ImageView(new Image("view/temporalViewPane/images/interactivity-button-hover.png"));
   		mediaContent = media.generateMediaIcon();
   			break;
           
		case VIDEO:
			//TODO pegar o frame do instante do playhead no vídeo
			break;
           
		case AUDIO:
			//INFO símbolo de áudio apenas. Não tocar.
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
	
				for(XYChart.Data<Number, String> xyChartData : getSerie().getData()){
					
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
					
				}
				
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
	
	public void resetPlayheadPosition(){
		stopped = true;
		isPlaying = false;
		playheadPixelPosition = 0.0;
		playhead.setTranslateX(playheadPixelPosition);
	}
	
	
}
