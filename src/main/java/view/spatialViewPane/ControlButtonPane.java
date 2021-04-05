package view.spatialViewPane;

import com.google.common.util.concurrent.AtomicDouble;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.DepthTest;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import model.common.CommonMethods;
import model.common.MediaNode;
import model.common.SensoryEffectNode;
import model.common.SpatialTemporalApplication;
import model.common.enums.MediaType;
import model.spatialView.media.enums.AspectRatio;
import model.temporalView.TemporalChain;
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
	private SpatialTemporalApplication spatialTemporalApplication;
	private SteveMenuBar steveMenuBar;
	private HBox effectIconsContainer;
	private Object nodeContent;
	private boolean isPlaying = false;
	private Double playheadPixelPosition = 0.0;
	private Double currentTime = 0.0;
	private boolean itHasMediaView = false;
	private Boolean hasStopped = false;
	private Boolean hasPaused = false;
	private TimerService timerService;
	
	public ControlButtonPane(StackPane screen, TemporalViewPane temporalViewPane,SteveMenuBar steveMenuBar, SpatialTemporalApplication spatialTemporalApplication){

		setId("control-button-pane");

		timerService = new TimerService();
		timerService.setPeriod(Duration.millis(100));
		timerService.setDelay(Duration.millis(0.0));

		this.steveMenuBar = steveMenuBar;
		this.screen = screen;
		this.temporalViewPane = temporalViewPane;
		this.spatialTemporalApplication = spatialTemporalApplication;
		this.effectIconsContainer = new HBox();
		effectIconsContainer.setId("effect-icons-container");
		effectIconsContainer.setDepthTest(DepthTest.ENABLE);
		effectIconsContainer.setViewOrder(-1);
		
	    createButtons();

	    setLeft(fullButtonPane);
		setCenter(centerButtonPane);
		setRight(refreshButtonPane);
	
		createListeners();
		createButtonActions();

	}

	private void createListeners(){
		
		temporalViewPane.getTemporalChainTabPane().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				
				stop.fire();
				TemporalChainPane temporalChainPane;
				if(newValue != null){
					temporalChainPane = (TemporalChainPane) newValue.getContent();
				}else{
					temporalChainPane = (TemporalChainPane) oldValue.getContent();
				}

				selectedTemporalChainPane = temporalChainPane;
				temporalChainModel = selectedTemporalChainPane.getTemporalChainModel();
				
			}
			
		});

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
		//centerButtonPane.getChildren().add(run);
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
				CommonMethods.runApplication(spatialTemporalApplication);
		    }
		});
		
		stop.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {

				stop.setDisable(true);
				pause.setDisable(true);
				play.setDisable(false);

				hasStopped = true;
				isPlaying = false;

				if(hasPaused){
					//INFO Stop is pressed when the preview is already paused. In this case, the
					// statement 'else if(hasStopped)' in the service thread is never reached.

					for(Node executionObject : screen.getChildren()){
						if(executionObject instanceof MediaView){
							MediaView currentMediaView = (MediaView) executionObject;
							currentMediaView.getMediaPlayer().stop();
						}
					}

					effectIconsContainer.getChildren().clear();
					screen.getChildren().clear();

				}

				hasPaused = false;

				selectedTemporalChainPane.getPlayhead().setTranslateX(selectedTemporalChainPane.getTimeLineChart().getXAxis().getDisplayPosition(0));

	    		for(model.common.Node node : temporalChainModel.getNodeAllList()){
	    			node.setIsShownInPreview(false);
					node.setIsContinuousMediaPlaying(false);
	    		}

			}
			
		});
		

		pause.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {

				stop.setDisable(false);
				pause.setDisable(true);
				play.setDisable(false);

				hasStopped = false;
				hasPaused = true;
				isPlaying = false;

				selectedTemporalChainPane.setHasClickedPlayhead(false);

				for(model.common.Node node : temporalChainModel.getNodeAllList()){
					node.setIsContinuousMediaPlaying(false);
				}

			}
			
		});

		play.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {

				if(!temporalChainModel.getNodeAllList().isEmpty()){

					stop.setDisable(false);
					pause.setDisable(false);
					play.setDisable(true);

					isPlaying = true;
					hasStopped = false;
					hasPaused = false;

					currentTime = selectedTemporalChainPane.getTimeLineChart().getXAxis().
							getValueForDisplay(selectedTemporalChainPane.getPlayhead().getTranslateX()).doubleValue();

					runTask();

				}
				
			}
			
		});		
	}

	public TimerService getTimerService() {
		return timerService;
	}

	public void runTask() {

		AtomicDouble atomicDouble = new AtomicDouble(currentTime);
		timerService.setCurrentTime(atomicDouble.get());

		timerService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent t) {

				if(!hasPaused && !hasStopped){
					Double currentTime = (Double) t.getSource().getValue();

					playheadPixelPosition = selectedTemporalChainPane.getTimeLineChart().getXAxis().getDisplayPosition(currentTime);
					selectedTemporalChainPane.getPlayhead().setTranslateX(playheadPixelPosition);

					if(currentTime > temporalChainModel.getNodeWithHighestEnd().getEnd()){
						stop.fire();
						hasStopped = true;
						isPlaying = false;
						for(model.common.Node node : temporalChainModel.getNodeAllList()){
							node.setIsShownInPreview(false);
							node.setIsContinuousMediaPlaying(false);
						}

					}

					for(model.common.Node node : temporalChainModel.getNodeAllList()){

						nodeContent = node.getExecutionObject();

						if(node.getBegin() <= currentTime && currentTime <= node.getEnd()){

							if(!node.getIsShownInPreview()){

								node.setIsShownInPreview(true);

								if(node instanceof MediaNode){

									if(nodeContent instanceof MediaView){

										if(((MediaNode) node).getType() == MediaType.VIDEO){
											setVideoPresentationProperties((MediaView) nodeContent, (MediaNode) node);
										}
										screen.getChildren().add((MediaView) nodeContent);
										if(currentTime - node.getBegin() > 0.2){
											Double backTime = node.getBegin() - 1;
											if(backTime < 0){
												getTimerService().setCurrentTime(0.0);
											}else{
												getTimerService().setCurrentTime(backTime);
											}
										}
										((MediaView) nodeContent).getMediaPlayer().play();
										node.setIsContinuousMediaPlaying(true);

									} else if(nodeContent instanceof ImageView){

										setImagePresentationProperties((ImageView) nodeContent, (MediaNode) node);
										if(screen.getChildren().isEmpty()){

											screen.getChildren().add((ImageView) nodeContent);

										} else{
											boolean inserted = false;
											for(Node executionObject : screen.getChildren()){

												if(((ImageView) nodeContent).getTranslateZ() < executionObject.getTranslateZ()){
													screen.getChildren().add(screen.getChildren().indexOf(executionObject), (ImageView) nodeContent);
													inserted = true;
													break;
												}

											}
											if(!inserted){
												screen.getChildren().add((ImageView) nodeContent);
											}
										}

									}

								}else if(node instanceof SensoryEffectNode){

									nodeContent = getSensoryEffectIcon((SensoryEffectNode) node);
									effectIconsContainer.getChildren().add((ImageView) nodeContent);
									if(!screen.getChildren().contains(effectIconsContainer)){
										screen.getChildren().add(effectIconsContainer);
									}

								}
							}else if(node.isContinousMedia() && !node.getIsContinuousMediaPlaying()){

								if(selectedTemporalChainPane.getHasClickedPlayhead()){
									((MediaView) node.getExecutionObject()).getMediaPlayer().stop();

									screen.getChildren().remove(node.getExecutionObject());
									node.setIsShownInPreview(false);
									node.setIsContinuousMediaPlaying(false);

									if(getTimerService() != null){
										Double backTime = node.getBegin() - 1;
										if(backTime < 0){
											getTimerService().setCurrentTime(0.0);
										}else{
											getTimerService().setCurrentTime(backTime);
										}
									}
								}else{
									Double videoCurrentTime = currentTime - node.getBegin();
									Double frameMillisTime = videoCurrentTime*1000;
									Duration duration = new Duration(frameMillisTime.intValue());
									((MediaView) nodeContent).getMediaPlayer().play();
									((MediaView) nodeContent).getMediaPlayer().seek(duration);
									node.setIsContinuousMediaPlaying(true);
								}

							}

						} else{

							if(node.getIsShownInPreview()){

								if(!screen.getChildren().isEmpty()){
									if(node instanceof SensoryEffectNode){
										effectIconsContainer.getChildren().remove(node.getExecutionObject());
									}else{
										screen.getChildren().remove(node.getExecutionObject());
									}
									node.setIsShownInPreview(false);
									node.setIsContinuousMediaPlaying(false);

								}

							}

						}

					}

					atomicDouble.set(currentTime);
				}else{

					if(hasPaused) {

						for (Node executionObject : screen.getChildren()) {
							if (executionObject instanceof MediaView) {
								MediaView currentMediaView = (MediaView) executionObject;
								currentMediaView.getMediaPlayer().pause();
							}
						}

					}else if(hasStopped){

						for(Node executionObject : screen.getChildren()){
							if(executionObject instanceof MediaView){
								MediaView currentMediaView = (MediaView) executionObject;
								currentMediaView.getMediaPlayer().stop();
							}
						}

						effectIconsContainer.getChildren().clear();
						screen.getChildren().clear();

					}

					timerService.cancel();

				}


			}
		});

		if(timerService.getState() == Worker.State.CANCELLED){
			timerService.restart();
		}else{
			timerService.start();
		}

	}

	public ImageView getSensoryEffectIcon(SensoryEffectNode sensoryEffectNode){

		ImageView nodeContent = sensoryEffectNode.generateEffectIcon();
		sensoryEffectNode.setExecutionObject(nodeContent);
		return nodeContent;

	}

	public Object getMediaContent(MediaNode mediaNode){

		nodeContent = null;

		switch(mediaNode.getType()) {

			case IMAGE:

				ImageView image = new ImageView(new Image(mediaNode.getFile().toURI().toString()));
				image.setFitWidth(screen.getWidth());
				image.setFitHeight(screen.getHeight());
				image.setSmooth(false);
				nodeContent = image;
				mediaNode.setExecutionObject(nodeContent);
				break;

			case VIDEO:

				final javafx.scene.media.Media video = new javafx.scene.media.Media(mediaNode.getFile().toURI().toString());
				final MediaPlayer videoPlayer = new MediaPlayer(video);
				MediaView videoMediaView = new MediaView(videoPlayer);
				videoMediaView.setFitWidth(screen.getWidth());
				videoMediaView.setFitHeight(screen.getHeight());
				videoMediaView.setSmooth(true);
				nodeContent = videoMediaView;
				mediaNode.setExecutionObject(nodeContent);
				break;

			case AUDIO:

				final javafx.scene.media.Media audio = new javafx.scene.media.Media(mediaNode.getFile().toURI().toString());
				final MediaPlayer audioPlayer = new MediaPlayer(audio);
				MediaView audioMediaView = new MediaView(audioPlayer);
				audioMediaView.setFitWidth(screen.getWidth());
				audioMediaView.setFitHeight(screen.getHeight());
				audioMediaView.setSmooth(true);
				nodeContent = audioMediaView;
				mediaNode.setExecutionObject(nodeContent);
				break;

			case TEXT:
				//TODO pegar o texto.
				break;

			case APPLICATION:
				nodeContent = mediaNode.generateMediaIcon();
				break;
		}

		return nodeContent;

	}
	
public void setVideoPresentationProperties(MediaView mediaContent, MediaNode mediaNode){
		
		double percentageHeight, percentageWidth; 
		String temp;

	    if(mediaNode.getPresentationProperty().getSizeProperty().getAspectRatio()==AspectRatio.SLICE){
	    	mediaContent.setPreserveRatio(true);
	    } else {
	    	mediaContent.setPreserveRatio(false);
	    }
	    		
		double left = Double.parseDouble(mediaNode.getPresentationProperty().getPositionProperty().getLeft().replace("%", ""));
		double right = Double.parseDouble(mediaNode.getPresentationProperty().getPositionProperty().getRight().replace("%", ""));
		double top = Double.parseDouble(mediaNode.getPresentationProperty().getPositionProperty().getTop().replace("%", ""));
		double bottom = Double.parseDouble(mediaNode.getPresentationProperty().getPositionProperty().getBottom().replace("%", ""));
		
		temp = mediaNode.getPresentationProperty().getSizeProperty().getHeight().replace("%", "");
		if (Double.parseDouble(temp)!=100){
			if(((left==0)&&(top==0))&&((right==0)&&(bottom==0))){
				left=0.1;
				top=0.1;
			}
		}
		percentageHeight = Double.parseDouble(temp);
		double screenHeight = screen.getHeight()*(percentageHeight/100);
		mediaContent.setFitHeight(screenHeight);
		
		temp = mediaNode.getPresentationProperty().getSizeProperty().getWidth().replace("%", "");
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
		mediaContent.setTranslateZ(mediaNode.getPresentationProperty().getPositionProperty().getOrderZ());
		
		
		double opacity = 1-(mediaNode.getPresentationProperty().getStyleProperty().getTransparency()/100);
		mediaContent.setOpacity(opacity);
		
	}
	
	public void setImagePresentationProperties(ImageView mediaContent, MediaNode mediaNode){
	
		double percentageHeight, percentageWidth; 
		String temp;

	    if(mediaNode.getPresentationProperty().getSizeProperty().getAspectRatio()==AspectRatio.SLICE){
	    	mediaContent.setPreserveRatio(true);
	    } else {
	    	mediaContent.setPreserveRatio(false);
	    }
	    		
		double left = Double.parseDouble(mediaNode.getPresentationProperty().getPositionProperty().getLeft().replace("%", ""));
		double right = Double.parseDouble(mediaNode.getPresentationProperty().getPositionProperty().getRight().replace("%", ""));
		double top = Double.parseDouble(mediaNode.getPresentationProperty().getPositionProperty().getTop().replace("%", ""));
		double bottom = Double.parseDouble(mediaNode.getPresentationProperty().getPositionProperty().getBottom().replace("%", ""));
		
		temp = mediaNode.getPresentationProperty().getSizeProperty().getHeight().replace("%", "");
		if (Double.parseDouble(temp)!=100){
			if(((left==0)&&(top==0))&&((right==0)&&(bottom==0))){
				left=0.1;
				top=0.1;
			}
		}
		percentageHeight = Double.parseDouble(temp);
		double screenHeight = screen.getHeight()*(percentageHeight/100);
		mediaContent.setFitHeight(screenHeight);
		
		temp = mediaNode.getPresentationProperty().getSizeProperty().getWidth().replace("%", "");
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
		mediaContent.setTranslateZ(mediaNode.getPresentationProperty().getPositionProperty().getOrderZ());
		
		
		double opacity = 1-(mediaNode.getPresentationProperty().getStyleProperty().getTransparency()/100);
		mediaContent.setOpacity(opacity);
		
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

	public HBox getEffectIconsContainer() {
		return effectIconsContainer;
	}

	public TemporalChainPane getSelectedTemporalChainPane() {
		return selectedTemporalChainPane;
	}
	
}
