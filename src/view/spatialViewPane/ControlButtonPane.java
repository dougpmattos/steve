package view.spatialViewPane;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.DepthTest;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import model.common.Media;
import model.common.SpatialTemporalView;
import model.spatialView.PositionProperty;
import model.spatialView.PresentationProperty;
import model.spatialView.enums.AspectRatio;
import model.temporalView.TemporalChain;
import view.common.CommonMethods;
import view.common.Language;
import view.stevePane.SteveMenuBar;
import view.temporalViewPane.TemporalChainPane;
import view.temporalViewPane.TemporalViewPane;

public class ControlButtonPane extends BorderPane{
	
	private Button fullScreen;
	private Button play;
	private Button pause;
	private Button run;
	private Button stop;
	private Button previousScene;
	private Button nextScene;
	private Button refresh;
	private HBox fullButtonPane;
	private HBox centerButtonPane;
	private HBox refreshButtonPane;
	private StackPane screen;
	private TemporalChain temporalChainModel;
	private TemporalChainPane selectedTemporalChainPane;
	private TemporalViewPane temporalViewPane;
	private SpatialTemporalView spatialTemporalView;
	private SteveMenuBar steveMenuBar;
	private WebView webView;
	
	private Object mediaContent;
	private boolean isPlaying = false;
	private Double playheadPixelPosition = 0.0;
	private Double currentTime;
	private boolean itHasMediaView = false;
	private Boolean hasStopped = false;
	private Boolean hasPaused = false;
	
	public ControlButtonPane(StackPane screen, TemporalViewPane temporalViewPane,SteveMenuBar steveMenuBar, SpatialTemporalView spatialTemporalView){
		
		setId("control-button-pane");
		this.steveMenuBar = steveMenuBar;
		this.screen = screen;
		this.screen.setAlignment(Pos.TOP_LEFT);
		this.temporalViewPane = temporalViewPane;
		this.spatialTemporalView = spatialTemporalView;
		this.webView = new WebView();
		
	    createButtons();
	  
	    setLeft(fullButtonPane);
		setCenter(centerButtonPane);
		setRight(refreshButtonPane);
	
		createListeners();
		createButtonActions();
		
		//DragResizerXY.makeResizable(screen);

	}
	private void createMouseEvent(){
		
	}
	private void createListeners(){
		
		temporalViewPane.getTemporalChainTabPane().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				
				stop.fire();
				TemporalChainPane temporalChainPane = (TemporalChainPane) newValue.getContent();
				selectedTemporalChainPane = temporalChainPane;
				temporalChainModel = selectedTemporalChainPane.getTemporalChainModel();
				
			}
			
		
		});
		
		
		
		//	temporalViewPane.addListerner();
		// cria listener de click no linha temporal
		// se o video nao esta playing
		// pega as midias debaixo da indicative line 
		// apresenta na screen

	}
	
	public void createButtons(){
		
		fullScreen = new Button();
		fullScreen.setId("full-button");
		fullScreen.setTooltip(new Tooltip(Language.translate("full.screen")));
		
		run = new Button();
		run.setDisable(true);
		run.setId("run-button");
		run.setTooltip(new Tooltip(Language.translate("run")));
		
		play = new Button();
		play.setDisable(true);
		play.setId("play-button");
		play.setTooltip(new Tooltip(Language.translate("play")));
		
		pause = new Button();
		pause.setDisable(true);
		pause.setId("pause-button");
		pause.setTooltip(new Tooltip(Language.translate("pause")));
		
		stop = new Button();
		stop.setDisable(true);
		stop.setId("stop-button");
		stop.setTooltip(new Tooltip(Language.translate("stop")));
		
		previousScene = new Button();
		previousScene.setId("previous-scene-button");
		previousScene.setTooltip(new Tooltip(Language.translate("previous.scene")));
		
		nextScene = new Button();
		nextScene.setId("next-scene-button");
		nextScene.setTooltip(new Tooltip(Language.translate("next.scene")));
		
		refresh = new Button();
		refresh.setId("refresh-button");
		refresh.setTooltip(new Tooltip(Language.translate("refresh")));
		
		fullButtonPane = new HBox();
	    fullButtonPane.setId("full-pane");
	    //fullButtonPane.getChildren().add(fullScreen);
		
		centerButtonPane = new HBox();
		centerButtonPane.setId("center-button-pane");
		centerButtonPane.getChildren().add(run);
		centerButtonPane.getChildren().add(play);
		centerButtonPane.getChildren().add(pause);
		centerButtonPane.getChildren().add(stop);
		//centerButtonPane.getChildren().add(previousScene);
		//centerButtonPane.getChildren().add(nextScene);
		
		refreshButtonPane = new HBox();
		refreshButtonPane.setId("refresh-pane");
		//refreshButtonPane.getChildren().add(refresh);
		
	}
	
	private void createButtonActions(){

		run.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent t) {
				CommonMethods.runApplication(spatialTemporalView);
		    }
		});
		
		stop.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {	
				
				stop.setDisable(true);
				pause.setDisable(true);
				play.setDisable(false);

				selectedTemporalChainPane.getPlayhead().setTranslateX(selectedTemporalChainPane.getTimeLineChart().getXAxis().getDisplayPosition(0));
	    		hasStopped = true;
	    		hasPaused = false;
	    		isPlaying = false;
	    		for(Media media : temporalChainModel.getMediaAllList()){
	    			media.setIsPLayingInPreview(false);
	    		}
	    		
	    		for(Node executionObject : screen.getChildren()){
	    			if(executionObject instanceof MediaView){
	    				MediaView currentMediaView = (MediaView) executionObject;
	    				currentMediaView.getMediaPlayer().stop();
	    			}
	    		}
	    		
	    		Platform.runLater(new Runnable() {
					@Override
					public void run() {
						screen.getChildren().clear();
					}
				});

			}
			
		});
		

		pause.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {	
				
				stop.setDisable(false);
				pause.setDisable(true);
				play.setDisable(false);
				
	    		hasPaused = true;
	    		isPlaying = false;
	    		selectedTemporalChainPane.setHasClickedPlayhead(false);
	    		
	    		for(Node executionObject : screen.getChildren()){
	    			if(executionObject instanceof MediaView){
	    				MediaView currentMediaView = (MediaView) executionObject;
	    				currentMediaView.getMediaPlayer().pause();
	    			}
	    		}
	    	
			}
			
		});
		
		play.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {	
				
				if(!temporalChainModel.getMediaAllList().isEmpty()){
					
					isPlaying = true;
					hasStopped = false;
					currentTime = selectedTemporalChainPane.getTimeLineChart().getXAxis().getValueForDisplay(selectedTemporalChainPane.getPlayhead().getTranslateX()).doubleValue();
					if(hasPaused && !selectedTemporalChainPane.getHasClickedPlayhead()) {
						for(Node executionObject : screen.getChildren()){
			    			if(executionObject instanceof MediaView){
			    				MediaView currentMediaView = (MediaView) executionObject;
			    				currentMediaView.getMediaPlayer().play();
			    			}
			    		}
					}else if(hasPaused && selectedTemporalChainPane.getHasClickedPlayhead()){
						for(Node executionObject : screen.getChildren()){
			    			if(executionObject instanceof MediaView){
			    				MediaView currentMediaView = (MediaView) executionObject;
			    				Duration currentMillisDuration = new Duration(currentTime*1000);
			    				currentMediaView.getMediaPlayer().setStartTime(currentMillisDuration);
			    				currentMediaView.getMediaPlayer().play();
			    			}
			    		}
					}
					hasPaused = false;

					stop.setDisable(false);
					pause.setDisable(false);
					play.setDisable(true);
					
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
				
			}
			
		});		
	}
	
	public void runTask() {
		
		while(true && !hasStopped && !hasPaused) {
			
			try {
				
		    	playheadPixelPosition = selectedTemporalChainPane.getTimeLineChart().getXAxis().getDisplayPosition(currentTime); 
		    	selectedTemporalChainPane.getPlayhead().setTranslateX(playheadPixelPosition);
		    	currentTime = currentTime + 0.1;
		    	System.out.println(currentTime);
		 
		    	if(currentTime > temporalChainModel.getMediaWithHighestEnd().getEnd()){
		    		stop.fire();
		    		hasStopped = true;
		    		isPlaying = false;
		    		for(Media media : temporalChainModel.getMediaAllList()){
		    			media.setIsPLayingInPreview(false);
		    		}
		    		System.out.println(" termino - remove todos d screen e aciona stop do player");
		    	}
		    	
				for(Media media : temporalChainModel.getMediaAllList()){
					
					if(media.getBegin() <= currentTime && currentTime <= media.getEnd()){
						System.out.println(media.getName() + " - entrou");
						
						if(!media.getIsPLayingInPreview()){
							System.out.println(media.getName() + " - nao esta no pewview ");
							Platform.runLater(new Runnable(){

								@Override
								public void run() {
									
									media.setIsPLayingInPreview(true);
									mediaContent = getMediaContent(media);
									
									if(mediaContent instanceof MediaView){
										
										setVideoPresentationProperties((MediaView) mediaContent, media);
										Double playerStartTime = currentTime - media.getBegin();
										Duration playerStartTimeMillis = new Duration(playerStartTime*1000);
										((MediaView) mediaContent).getMediaPlayer().setStartTime(playerStartTimeMillis);
										screen.getChildren().add((MediaView) mediaContent);
										
									} else if(mediaContent instanceof ImageView){
									
										setImagePresentationProperties((ImageView) mediaContent, media);
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
										System.out.println(media.getName() + " - saiu");
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

	public Object getMediaContent(Media media){

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
		
				final javafx.scene.media.Media video = new javafx.scene.media.Media(media.getFile().toURI().toString());
				final MediaPlayer videoPlayer = new MediaPlayer(video);
				MediaView videoMediaView = new MediaView(videoPlayer);
				videoMediaView.setFitWidth(screen.getWidth());
				videoMediaView.setFitHeight(screen.getHeight()); 
				videoMediaView.setSmooth(true);
				
				if(isPlaying){
					videoPlayer.play();
					mediaContent = videoMediaView;
				} else {
					mediaContent = videoMediaView;
					
					videoPlayer.statusProperty().addListener(new ChangeListener<MediaPlayer.Status>() {
								
					    public void changed(ObservableValue<? extends MediaPlayer.Status> ov,
					            final MediaPlayer.Status oldStatus, final MediaPlayer.Status newStatus) {
					    	
					    	Double frameMillisTime = (media.getDuration()/2)*1000;
					    	Duration duration = new Duration(frameMillisTime.intValue()); 
					    	videoPlayer.seek(duration);
					    }
					});
				}
				media.setExecutionObject(mediaContent);
				break;
	
			case AUDIO:
				
				final javafx.scene.media.Media audio = new javafx.scene.media.Media(media.getFile().toURI().toString());
				final MediaPlayer audioPlayer = new MediaPlayer(audio);
				MediaView audioMediaView = new MediaView(audioPlayer);
				audioMediaView.setFitWidth(screen.getWidth());
				audioMediaView.setFitHeight(screen.getHeight()); 
				audioMediaView.setSmooth(true);
				
				if(isPlaying){
					audioPlayer.play();
					mediaContent = audioMediaView;
				} else {
					//mediaContent = audioMediaView;
					//inserir depois a figura do audio tambem alem de tocar
		//			ImageView imageAudio = new ImageView(new Image(getClass().getResourceAsStream("/view/repositoryPane/images/audioNode.png")));
		//			imageAudio.setPreserveRatio(true);
		//			imageAudio.setSmooth(false);
		//			imageAudio.setFitWidth(ICON_WIDTH);
				}
				media.setExecutionObject(mediaContent);
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
	
	public void setVideoPresentationProperties(MediaView mediaContent, Media media){
		
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
		int boundWidth = (int)mediaContent.getBoundsInLocal().getWidth();
        int boundHeight = (int)mediaContent.getBoundsInLocal().getHeight();
        	        	        
        screenWidth = screen.getWidth();
        screenHeight = screen.getHeight();
        
        
        
        double xZero = (+screenWidth)-boundWidth;
        
		double dXRight = 0; //como definir?
		if(right!=0){
			dXRight = (right/100)*screenWidth; 
			//System.out.println("xZero+dXRight = "+(xZero-dXRight));
			mediaContent.setTranslateX(xZero-dXRight);
		}
        
        xZero = 0;
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
	
	public void setImagePresentationProperties(ImageView mediaContent, Media media){

		double percentageHeight, percentageWidth; 
		String temp;

//	    if(media.getPresentationProperty().getSizeProperty().getAspectRatio()==AspectRatio.SLICE){
//	    	mediaContent.setPreserveRatio(true);
//	    } else {
//	    	mediaContent.setPreserveRatio(false);
//	    }
	    		
		double left = Double.parseDouble(media.getPresentationProperty().getPositionProperty().getLeft().replace("%", ""));
		double right = Double.parseDouble(media.getPresentationProperty().getPositionProperty().getRight().replace("%", ""));
		double top = Double.parseDouble(media.getPresentationProperty().getPositionProperty().getTop().replace("%", ""));
		double bottom = Double.parseDouble(media.getPresentationProperty().getPositionProperty().getBottom().replace("%", ""));		
		
//		Image image = new Image(media.getFile().toURI().toString());
//		ImageView imageView = new ImageView(image);
//		
//		mediaContent = imageView;
//		mediaContent.setFitHeight(screen.getHeight());
//		mediaContent.setFitWidth(screen.getWidth());
		
		
//		if(!((left==0)&&(right!=0))){			
//			moveMediaLeft(mediaContent, media, left);
//		}		
//		
//		if(!((top==0)&&(bottom!=0))){			
//			moveMediaTop(mediaContent, media, top);
//		}
		
		boolean hLock = true;
		boolean vLock = true;
		
		if(left!=0) hLock = true;
		else hLock = false;
		
		if(top!=0) vLock = true;
		else vLock = false;
		
		String ratio = "";
		if(media.getPresentationProperty().getSizeProperty().getAspectRatio()==AspectRatio.SLICE){
			if((top==0)&&(bottom==0)&&(left==0)&&(right==0)){
				System.out.println("e zero");
				slice(mediaContent, spaceAvailable(left, right, top, bottom));
			}
			else {
				slice(mediaContent, spaceAvailable(left, right, top, bottom));
			}
	    	if((top==0)&&(bottom==0)&&(left==0)&&(right==0)){
	    		moveMediaLeft(mediaContent,media,left);
	    		moveMediaTop(mediaContent,media,top);
	    	}
	    	ratio = "fill";
	    	System.out.println("H Lock = "+hLock);
	    	if(!hLock) {
				moveMediaRight(mediaContent, media, right);
			} else {
				moveMediaLeft(mediaContent,media,left);
			}
			if(!vLock) {
				moveMediaBottom(mediaContent, media, bottom);
			} else {
				moveMediaTop(mediaContent,media,top);
			}
			System.out.println("bottom: "+bottom+" top: "+top+" right: "+right+" left: "+left);
			if((!vLock)&&(bottom==0)) {
				System.out.println("e zero vertical");
				moveMediaTop(mediaContent,media,top);
			}
			System.out.println(right==0);
			if((!hLock)&&(right==0)) moveMediaLeft(mediaContent,media,left);

//			slice( mediaContent, left, right, top, bottom);
			ratio = "slice";
			
	    } else {
	    	fill(mediaContent, spaceAvailable(left, right, top, bottom));
	    	if((top==0)&&(bottom==0)&&(left==0)&&(right==0)){
	    		moveMediaLeft(mediaContent,media,left);
	    		moveMediaTop(mediaContent,media,top);
	    	}
	    	ratio = "fill";
	    	System.out.println("H Lock = "+hLock);
	    	if(!hLock) {
				moveMediaRight(mediaContent, media, right);
			} else {
				moveMediaLeft(mediaContent,media,left);
			}
			if(!vLock) {
				moveMediaBottom(mediaContent, media, bottom);
			} else {
				moveMediaTop(mediaContent,media,top);
			}
			System.out.println("bottom: "+bottom+" top: "+top+" right: "+right+" left: "+left);
			if((!vLock)&&(bottom==0)) {
				System.out.println("e zero vertical");
				moveMediaTop(mediaContent,media,top);
			}
			System.out.println(right==0);
			if((!hLock)&&(right==0)) moveMediaLeft(mediaContent,media,left);
			
	    }		
		
//		
//		if(!(left>0)) {
//			String width = media.getPresentationProperty().getSizeProperty().getWidth().replace("%", "");
//			mediaContent.setFitWidth((Double.parseDouble(width)/100)*screen.getWidth());
////			 TO DO TESTAR ESSA MODIFICACAO
//			if(media.getPresentationProperty().getSizeProperty().getAspectRatio()==AspectRatio.FILL){
////				mediaContent = fill(mediaContent, spaceAvailable(0, (100-Double.parseDouble(width)), top, bottom));
//				moveMediaRight(mediaContent, media, right);
//				mediaContent.setFitWidth(screen.getWidth()-(right/100)*screen.getWidth());
//			}
//		}		
//		
//		if(!(top>0)&&(bottom!=0)){
//			String height = media.getPresentationProperty().getSizeProperty().getHeight().replace("%", "");
//			mediaContent.setFitHeight((Double.parseDouble(height)/100)*screen.getHeight());
//			if(media.getPresentationProperty().getSizeProperty().getAspectRatio()==AspectRatio.FILL){
//				moveMediaBottom(mediaContent, media, bottom);
//				mediaContent.setFitHeight(screen.getHeight()-(bottom/100)*screen.getHeight());
//			}
//		}		
		
//		System.out.println(media.getPresentationProperty().getPositionProperty().getOrderZ());
//				System.out.println(mediaContent.getTranslateZ());
		mediaContent.setDepthTest(DepthTest.ENABLE);
		mediaContent.setTranslateZ(media.getPresentationProperty().getPositionProperty().getOrderZ());
		
		
		double opacity = 1-(media.getPresentationProperty().getStyleProperty().getTransparency()/100);
		mediaContent.setOpacity(opacity);
		
//		screen.getChildren().clear();
//		screen.getChildren().add( mediaContent);
	}
	//move media pra esquerda e seta margem esquerda na position property
	public Point2D spaceAvailable(double left, double right, double top, double bottom){
		Point2D space = null;
		double x, y;
		if(left!=0){ // NAO ADIANTA MEXER AQUI PORQUE ELE SO RETORNA OS PONTOS E ELE RECEBE A MEDIA			
//			System.out.println("Width: "+screen.getWidth()+" - left: "+left);
			x = screen.getWidth() - ((left/100)*screen.getWidth());
//			System.out.println(x);
//			moveMediaLeft(mediaContent, media, left);
		}
			
		else {
			
			x = screen.getWidth() - ((right/100)*screen.getWidth());
//			moveMediaRight(mediaContent, media, right);
			
		}
		if(top!=0){
			
			y = screen.getHeight() - ((top/100)*screen.getHeight());
//			moveMediaTop(mediaContent, media, top);
			
		}
			
		else {
			
			y = screen.getHeight() - ((bottom/100)*screen.getHeight());
//			moveMediaBottom(mediaContent, media, bottom);
			
		}
		
		space = new Point2D.Double(x,y);
		
		return space;
	}
	
	public ImageView slice(ImageView imageView, Point2D space){
				
		
		imageView.setPreserveRatio(true);
		imageView.setFitWidth(screen.getWidth());
		imageView.setFitHeight(screen.getHeight());
//		imageView.setFitWidth(space.getX());
//		imageView.setFitHeight(space.getY());		
		imageView.setViewport(new Rectangle2D(0, 0, screen.getWidth(), screen.getHeight()));
		
		
		
		
		return imageView;
	}
	
	
		
	public ImageView fill(ImageView imageView, Point2D space){ //image, available space
		imageView.setViewport(null);
		imageView.setPreserveRatio(false);
		System.out.println("Settando width para: "+space.getX()+" height para: "+space.getY());
		imageView.setFitWidth(space.getX());
		imageView.setFitHeight(space.getY());
		return imageView;
	}
	
	public void changeHeight(ImageView mediaContent, Media media, String height){
		//mediaContent = new ImageView(new Image(media.getFile().toURI().toString()));
		media.getPresentationProperty().getSizeProperty().setHeight(height);			
		//setImagePresentationProperties(mediaContent, media);
		mediaContent.setPreserveRatio(false);
		mediaContent.setFitHeight((Double.parseDouble(height)/100)*screen.getHeight());
		screen.getChildren().clear();
		screen.getChildren().add((ImageView) mediaContent);
		
	}
	public void changeWidth(ImageView mediaContent, Media media, String width){
		//mediaContent = new ImageView(new Image(media.getFile().toURI().toString()));
		media.getPresentationProperty().getSizeProperty().setWidth(width);			
		//setImagePresentationProperties(mediaContent, media);
		mediaContent.setPreserveRatio(false);
		mediaContent.setFitWidth((Double.parseDouble(width)/100)*screen.getWidth());
		screen.getChildren().clear();
		screen.getChildren().add((ImageView) mediaContent);
		
	}
	
	public void moveMediaLeft(ImageView mediaContent, Media media, double left){
		
//		mediaContent.setFitHeight((Double.parseDouble(media.getPresentationProperty().getSizeProperty().getHeight().replace("%",""))/100)*screen.getHeight());
		
		PositionProperty pp = media.getPresentationProperty().getPositionProperty();
		pp.setLeft(Double.toString(left));		
		
		media.getPresentationProperty().setPositionProperty(pp);
        	
		double screenWidth = screen.getWidth();     
		double dXLeft = (left/100)*screen.getWidth();
//		if(media.getPresentationProperty().getSizeProperty().getAspectRatio().compareTo(AspectRatio.FILL)==0){			   			
	        double xZero = 0; //Referencial da tela (borda esquerda)	                 			
			mediaContent.setTranslateX(xZero+dXLeft);
//		}
//		else {
//			mediaContent.resize(screenWidth, screen.getHeight());
//			mediaContent.setFitWidth(screenWidth);
//			mediaContent.setFitHeight(screen.getHeight());
//			mediaContent.setViewport(new Rectangle2D(screenWidth-dXLeft, 0, screenWidth, screen.getHeight()));
//			mediaContent.setTranslateX(dXLeft);
//		}
		
	}
	
	public void moveMediaRight(ImageView mediaContent, Media media, double right){
//		if(media.getPresentationProperty().getSizeProperty().getAspectRatio().compareTo(AspectRatio.FILL)==0){
//			mediaContent.setPreserveRatio(false);
//			Double d = screen.getWidth()-((right/100)*screen.getWidth());			
//			mediaContent.setFitWidth(d);    
//		} else {
//			mediaContent.setPreserveRatio(true);
//			String s = media.getPresentationProperty().getSizeProperty().getHeight().replace("%","");			
//			mediaContent.setFitHeight((Double.parseDouble(s)/100)*screen.getHeight());
//			s = media.getPresentationProperty().getSizeProperty().getWidth().replace("%","");									
//			mediaContent.setFitWidth((Double.parseDouble(s)/100)*screen.getWidth());
//		}

//		mediaContent.setFitHeight((Double.parseDouble(media.getPresentationProperty().getSizeProperty().getHeight().replace("%",""))/100)*screen.getHeight());
		
		PositionProperty pp = media.getPresentationProperty().getPositionProperty();
		pp.setRight(Double.toString(right));		
		
		media.getPresentationProperty().setPositionProperty(pp);        
        	        	        
        double screenWidth = screen.getWidth();		
	                
		double dXRight = ((right/100)*screenWidth);				 
		if(media.getPresentationProperty().getSizeProperty().getAspectRatio()==AspectRatio.SLICE){
			System.out.println("Size of media is: "+mediaContent.getImage().getWidth());
			if(mediaContent.getImage().getWidth()<screenWidth){
				mediaContent.setTranslateX(-mediaContent.getImage().getWidth()-dXRight+screenWidth);
			} else {
				mediaContent.setTranslateX(-dXRight);
			}
			
		} else{
			mediaContent.setTranslateX(0);			
		}
//		mediaContent.setTranslateX(layoutBoundsProperty().get().getMinX());	
	}
	
	public void moveMediaTop(ImageView mediaContent, Media media, double top){
		PresentationProperty presentationProperty = media.getPresentationProperty();
		PositionProperty pp = media.getPresentationProperty().getPositionProperty();
		pp.setTop(Double.toString(top));	
		media.getPresentationProperty().setPositionProperty(pp);
		
		double screenWidth = screen.getWidth();
		double screenHeight = screen.getHeight();
			
		double yZero = 0; //Referencial da tela (borda superior)
		double dYTop = (top/100)*screenHeight; 
			
		mediaContent.setTranslateY(yZero+dYTop);	
	
	}
	
	public void moveMediaBottom(ImageView mediaContent, Media media, double bottom){
//		if(media.getPresentationProperty().getSizeProperty().getAspectRatio().compareTo(AspectRatio.FILL)==0){
//			mediaContent.setPreserveRatio(false);
//			Double d = screen.getHeight()-((bottom/100)*screen.getHeight());			
//			mediaContent.setFitHeight(d);    
//		} else {
//			mediaContent.setPreserveRatio(true);
//			String s = media.getPresentationProperty().getSizeProperty().getWidth().replace("%","");			
//			mediaContent.setFitWidth((Double.parseDouble(s)/100)*screen.getWidth());
//			s = media.getPresentationProperty().getSizeProperty().getHeight().replace("%","");									
//			mediaContent.setFitHeight((Double.parseDouble(s)/100)*screen.getHeight());
//		}
//		Double d = screen.getHeight()-((bottom/100)*screen.getHeight());			
//		mediaContent.setFitHeight(d);    
		PositionProperty pp = media.getPresentationProperty().getPositionProperty();
		pp.setBottom(Double.toString(bottom));		
		
		media.getPresentationProperty().setPositionProperty(pp);
		
        int boundHeight = (int)mediaContent.getBoundsInLocal().getHeight();
        //the x or y variables of a shape, or translateX, 
        //translateY should never be bound to boundsInParent for the purpose of positioning the node.
		System.out.println("esse e o bound height= "+boundHeight);
		double screenHeight = screen.getHeight();
		
		double yZero = (screenHeight)-boundHeight; //Referencial da tela (borda inferior)
		System.out.println("yZero: "+ yZero);		

		double dYDown = (bottom/100)*screenHeight; 
		//System.out.println("yZero-dYDown = "+(yZero-dYDown));
//		mediaContent.setFitWidth((screen.getWidth()));	
		if(media.getPresentationProperty().getSizeProperty().getAspectRatio()==AspectRatio.SLICE){

			if(mediaContent.getImage().getHeight()<screenHeight){
				
				mediaContent.setTranslateY(yZero-dYDown+screenHeight-mediaContent.getImage().getHeight());
				
			} else{
				mediaContent.setTranslateY(yZero-dYDown);
	
			}
		} else {
			mediaContent.setTranslateY(yZero-dYDown);
			
		}
//		setImagePresentationProperties(mediaContent, media);

//
//		screen.getChildren().clear();
//		screen.getChildren().add((ImageView) mediaContent);

	}
	

	public Boolean getHasPaused(){
		return this.hasPaused;
	}
	
	public Boolean getIsPlaying(){
		return this.isPlaying;
	}
	
	public Button getPlayButton(){
		return play;
	}
	public Button getRunButton(){
		return run;
	}
	public Button getPauseButton(){
		return pause;
	}
	public Button getStopButton(){
		return stop;
	}
	public StackPane getScreen(){
		return screen;
	}
	public ControlButtonPane getControlButtonPane(){
		return this;
	}
	
	
	
}