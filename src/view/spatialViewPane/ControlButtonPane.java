package view.spatialViewPane;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
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
import javafx.scene.input.MouseEvent;
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
import model.spatialView.setImageInterface;
import model.spatialView.FillStyle;
import model.spatialView.HiddenStyle;
import model.spatialView.SliceStyle;
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

	double dragx = 0;
	double dragy = 0;
	// mouse position
	double mousex = 0;
	double mousey = 0;

	boolean dragging = false;
	boolean moveToFront = true;

	Media mediaGlobal;
	ImageView ivGlobal;

	double fullHeight = 1080;
	double fullWidth = 1920;

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



		screen.minWidthProperty().setValue(640);
		screen.maxWidthProperty().setValue(640);

		screen.minHeightProperty().setValue(360);
		screen.maxHeightProperty().setValue(360);

//		setStyle("-fx-border-color: #607D8B; -fx-border: 22px solid;");
		createListeners();
		createButtonActions();

//		DragResizerXY.makeResizable(screen);

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

//		screen.sceneProperty().addListener((obs, oldScene, newScene) -> {
//			if (newScene == null) {
//				// not showing...
//			} else {
//				screen.setStyle("-fx-border-color: #607D8B; -fx-border: 22px solid;");
//
//			}
//		});
		screen.widthProperty().addListener((observable, oldValue, newValue) -> {
			screen.minWidthProperty().bind(screen.heightProperty().multiply(1.77778));
			screen.maxWidthProperty().bind(screen.heightProperty().multiply(1.77778));
			if(temporalChainModel!=null) {


				for (Media media : temporalChainModel.getMediaAllList()) {

					if ((currentTime!=null)&&(media.getBegin() <= currentTime && currentTime <= media.getEnd())) {
						System.out.println(media.getName() + " - entrou");
						media.setIsPLayingInPreview(true);
						mediaContent = getMediaContent(media);

						if (mediaContent instanceof ImageView) {

							setImagePresentationProperties((ImageView) mediaContent, media);


						}

					}
				}
			}


//			screen.minHeightProperty().bind(screen.widthProperty().multiply(1.77778));
//			screen.maxHeightProperty().bind(screen.widthProperty().multiply(1.77778));

			System.out.println(	"Mudou width "+newValue);
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

	public void createClickedAction(){

		screen.setOnMouseClicked(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent mouseEvent) {
				System.out.println("MOUSE CLICOU NA TELA direto da cbp");
			}
		});
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
//										File file = new File("test3.png");
//								        RenderedImage renderedImage = SwingFXUtils.fromFXImage(((ImageView) mediaContent).getImage(), null);
//								        try {
//											ImageIO.write(
//											        renderedImage,
//											        "png",
//											        file);
//										} catch (IOException e) {
//											// TODO Auto-generated catch block
//											e.printStackTrace();
//										}
										if(screen.getChildren().isEmpty()){

											screen.getChildren().add((ImageView) mediaContent);
//											file = new File("test4.png");
//									        renderedImage = SwingFXUtils.fromFXImage(((ImageView) mediaContent).getImage(), null);
//									        try {
//												ImageIO.write(
//												        renderedImage,
//												        "png",
//												        file);
//											} catch (IOException e) {
//												// TODO Auto-generated catch block
//												e.printStackTrace();
//											}

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
				setImagePresentationProperties(image, media);
//				image.setFitWidth(screen.getWidth());
//				image.setFitHeight(screen.getHeight());
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


//	public void setImagePresentationProperties(ImageView mediaContent, Media media){
//
//		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//		int width = gd.getDisplayMode().getWidth();
//		int height = gd.getDisplayMode().getHeight();
////		globalImageView = mediadiaContent;
////		globalMedia = media;
//
//		double rateHeightPerWidth = fullHeight/fullWidth;
//
//		//media.getPresentationProperty().set Mudar apsect ratio da imagem
//		double percentageHeight, percentageWidth;
//		String temp;
//		boolean smlImg = false;
//
//		String ratio = "";
//		if(media.getPresentationProperty().getSizeProperty().getAspectRatio()==AspectRatio.SLICE){
//			ratio="SLICE";
//		} else if (media.getPresentationProperty().getSizeProperty().getAspectRatio()==AspectRatio.FILL){
//			ratio = "FILL";
//		} else {
//			ratio = "HIDDEN";
//		}
//
//		String w = media.getPresentationProperty().getSizeProperty().getWidth();
//		String h = media.getPresentationProperty().getSizeProperty().getHeight();
//
//		double wid;
//		double hei;
//
//		if(mediaContent.getImage().getHeight()>=screen.getHeight()||(ratio.equals("FILL"))){
//			smlImg = false;
//			hei =  Double.parseDouble(h.replace("%", "")) * mediaContent.getFitHeight()/100;
//			System.out.println("Double.parseDouble(h.replace(\"%\", \"\")) = "+Double.parseDouble(h.replace("%", ""))+ ", mediaContent.getFitHeight()  = "+ screen.getHeight());
//		} else {
//			smlImg = true;
//			hei =  Double.parseDouble(h.replace("%", "")) * mediaContent.getImage().getHeight()/100;
//
//		}
//
//		double left = Double.parseDouble(media.getPresentationProperty().getPositionProperty().getLeft().replace("%", ""));
//		double right = Double.parseDouble(media.getPresentationProperty().getPositionProperty().getRight().replace("%", ""));
//		double top = Double.parseDouble(media.getPresentationProperty().getPositionProperty().getTop().replace("%", ""));
//		double bottom = Double.parseDouble(media.getPresentationProperty().getPositionProperty().getBottom().replace("%", ""));
//
//		boolean hLock = true;
//		boolean vLock = true;
//		boolean heightLock = true;
//		boolean widthLock = true;
//
//		if(left!=0) hLock = true;
//		else hLock = false;
//
//		if(top!=0) vLock = true;
//		else vLock = false;
//
//
//		if(Double.parseDouble(w.replace("%", ""))!=100) widthLock = true;
//		else widthLock = false;
//
//		if(Double.parseDouble(h.replace("%", ""))!=100) heightLock = true;
//		else heightLock = false;
//
//
////  se slice apenas uma dimensao toda cabe na tela e o resto é cortado.
//
////  se fill imagem é distorcida para caber no tamanho disponivel.
//
////  - [ ] precendencia left > width > right
////  - [ ] -precendencia top > height > bottom
//
//		if(media.getPresentationProperty().getSizeProperty().getAspectRatio()==AspectRatio.SLICE){
//
//			mediaContent = //slice(mediaContent, media, spaceAvailable(mediaContent, left, right, top, bottom, w, h, ratio), hLock, vLock);
//					slice(mediaContent, spaceAvailable(mediaContent, left, right, top, bottom, w, h), media);
//
//
//		} else if(media.getPresentationProperty().getSizeProperty().getAspectRatio()==AspectRatio.FILL){
//
//			mediaContent = //fill(mediaContent, spaceAvailable(mediaContent, left, right, top, bottom, w, h, ratio), hLock, vLock);
//					fill(mediaContent, spaceAvailable(mediaContent, left, right, top, bottom, w, h));
//
//		} else if(media.getPresentationProperty().getSizeProperty().getAspectRatio()==AspectRatio.HIDDEN){
//
//			mediaContent = //slice(mediaContent, media, spaceAvailable(mediaContent, left, right, top, bottom, w, h, ratio), hLock, vLock);
//					hidden(mediaContent, media, spaceAvailable(mediaContent, left, right, top, bottom, w, h), hLock, vLock, fullHeight, fullWidth);
//
//		}
//
////  se right = 10% coloca a margem direita da imagem numa distancia de 10% do 0
////  nesse caso imagem fica com 10% de largura
//
////  se left = 10% coloca a margem esquerda da imagem numa distancia de 10% do 0
////  nesse caso imagem fica com 90% de largura
//
////  se top = 10% coloca a marge de cima da imagem numa distancia de 10% do 0
////  nesse caso imagem fica com 90% de altura
//
////  se bottom = 10% coloca a marge de baixo da imagem numa distancia de 10% do 0
////  nesse caso imagem fica com 10% de altura
//
//		if((top==0)&&(bottom==0)&&(left==0)&&(right==0)){
//			moveMediaLeft(mediaContent,media,left);
//			moveMediaTop(mediaContent,media,top);
//		}
//
//		System.out.println("H Lock = "+hLock);
//		if((!hLock) && (!widthLock)) { //widthLock: only move to right if width % of image is not 100 else width has precedence over right
//			moveMediaRight(mediaContent, media, right);
//		} else {
//			moveMediaLeft(mediaContent,media,left);
//		}
//		if((!vLock) && (!heightLock) && (ratio!="FILL")){ // heightLock: only move to bottom if height % of image is not 100 else height has precedence over bottom
//			moveMediaBottom(mediaContent, media, bottom);
//		} else {
//			moveMediaTop(mediaContent,media,top);
//		}
//		System.out.println("bottom: "+bottom+" top: "+top+" right: "+right+" left: "+left);
//		if((!vLock)&&(bottom==0)) {
//			System.out.println("e zero vertical");
//			moveMediaTop(mediaContent,media,top);
//		}
//		System.out.println(right==0);
//		if((!hLock)&&(right==0)) moveMediaLeft(mediaContent,media,left);
//
//		mediaContent.setDepthTest(DepthTest.ENABLE);
//		mediaContent.setTranslateZ(media.getPresentationProperty().getPositionProperty().getOrderZ());
//
//		double opacity = 1-(media.getPresentationProperty().getStyleProperty().getTransparency()/100);
//		mediaContent.setOpacity(opacity);
//
//	}

	public ImageView setImagePresentationProperties(ImageView mediaContent, Media media){

		//media.getPresentationProperty().set Mudar apsect ratio da imagem
		double percentageHeight, percentageWidth;
		String temp;
		boolean smlImg = false;
//
//		PresentationProperty pp = media.getPresentationProperty();
//		mediaContent = new ImageView(media);
//
//		String imageFile = media.getFile().toURI().toString();
//
//		Image image = new Image(imageFile);
//
//		mediaContent.setImage(image);

		String ratio = "";
		if(media.getPresentationProperty().getSizeProperty().getAspectRatio()==AspectRatio.SLICE){
			ratio="SLICE";
		} else if (media.getPresentationProperty().getSizeProperty().getAspectRatio()==AspectRatio.FILL){
			ratio = "FILL";
		} else {
			ratio = "HIDDEN";
		}

		String w = media.getPresentationProperty().getSizeProperty().getWidth();
		String h = media.getPresentationProperty().getSizeProperty().getHeight();

		double wid;
		double hei;

		if(mediaContent.getImage().getHeight()>=screen.getHeight()||(ratio.equals("FILL"))){
			smlImg = false;
			hei =  Double.parseDouble(h.replace("%", "")) * mediaContent.getFitHeight()/100;
			System.out.println("Double.parseDouble(h.replace(\"%\", \"\")) = "+Double.parseDouble(h.replace("%", ""))+ ", mediaContent.getFitHeight()  = "+ screen.getHeight());
		} else {
			smlImg = true;
			hei =  Double.parseDouble(h.replace("%", "")) * mediaContent.getImage().getHeight()/100;

		}

		double left = Double.parseDouble(media.getPresentationProperty().getPositionProperty().getLeft().replace("%", ""));
		double right = Double.parseDouble(media.getPresentationProperty().getPositionProperty().getRight().replace("%", ""));
		double top = Double.parseDouble(media.getPresentationProperty().getPositionProperty().getTop().replace("%", ""));
		double bottom = Double.parseDouble(media.getPresentationProperty().getPositionProperty().getBottom().replace("%", ""));

		boolean hLock = true;
		boolean vLock = true;
		boolean heightLock = true;
		boolean widthLock = true;

		if(left!=0) hLock = true;
		else hLock = false;

		if(top!=0) vLock = true;
		else vLock = false;


		if(Double.parseDouble(w.replace("%", ""))!=100) widthLock = true;
		else widthLock = false;

		if(Double.parseDouble(h.replace("%", ""))!=100) heightLock = true;
		else heightLock = false;


//		se slice apenas a maior dimensao toda cabe na tela e o resto é cortado.

//		se fill imagem é distorcida para caber no tamanho disponivel.

//		- [ ] precendencia left > width > right
//		- [ ] -precendencia top > height > bottom

		if(media.getPresentationProperty().getSizeProperty().getAspectRatio()==AspectRatio.SLICE){

			SliceStyle sliceStyle = new SliceStyle();

			mediaContent = sliceStyle.setImageProperties(mediaContent,media, hLock, vLock, top, bottom, left, right, screen, widthLock, heightLock, w, h);




	    } else if(media.getPresentationProperty().getSizeProperty().getAspectRatio()==AspectRatio.FILL){

			FillStyle fillStyle = new FillStyle();

			mediaContent = fillStyle.setImageProperties(mediaContent,media, hLock, vLock, top, bottom, left, right, screen, widthLock, heightLock, w, h);


	    } else if(media.getPresentationProperty().getSizeProperty().getAspectRatio()==AspectRatio.HIDDEN){

	    	HiddenStyle hiddenStyle = new HiddenStyle();

			mediaContent = hiddenStyle.setImageProperties(mediaContent,media, hLock, vLock, top, bottom, left, right, screen, widthLock, heightLock, w, h);//slice(mediaContent, media, spaceAvailable(mediaContent, left, right, top, bottom, w, h, ratio), hLock, vLock);
//					hidden(mediaContent, media, spaceAvailable(mediaContent, left, right, top, bottom, w, h), hLock, vLock);

	    }

//		se right = 10% coloca a margem direita da imagem numa distancia de 10% do 0
//		nesse caso imagem fica com 10% de largura

//		se left = 10% coloca a margem esquerda da imagem numa distancia de 10% do 0
//		nesse caso imagem fica com 90% de largura

//		se top = 10% coloca a marge de cima da imagem numa distancia de 10% do 0
//		nesse caso imagem fica com 90% de altura

//		se bottom = 10% coloca a marge de baixo da imagem numa distancia de 10% do 0
//		nesse caso imagem fica com 10% de altura





		mediaContent.setDepthTest(DepthTest.ENABLE);
		mediaContent.setTranslateZ(media.getPresentationProperty().getPositionProperty().getOrderZ());


		double opacity = 1-(media.getPresentationProperty().getStyleProperty().getTransparency()/100);
		mediaContent.setOpacity(opacity);

		return mediaContent;

	}

//	private ImageView hidden(ImageView imageView, Media media, Point2D space, boolean hLock, boolean vLock, double fullHeight, double fullWidth){
//		// TODO Auto-generated method stub
//
//		imageView.setPreserveRatio(false);
//		double x=0,y=0;
//
//		String imageFile = media.getFile().toURI().toString();
//
//		Image image = new Image(imageFile);
//
//		PixelReader reader = image.getPixelReader();
//
//		System.out.println("X = "+space.getX()+" >= "+image.getWidth());
//		if(space.getX()>=image.getWidth()){ //tamanho da tela e maior que tamanho da imagem na dimensao original dela?
//			x=image.getWidth();
//		}
//
//		else if(space.getX()==0){ //tem espaco suficiente pra exibir a imagem?
//			x=screen.getWidth();
//		}
//
//		else {
//			x=space.getX();
//		}
//
//		if(space.getY()>=image.getHeight()){
//			y=image.getHeight();
//		}
//
//		else if(space.getY()==0){
//			y=screen.getHeight();
//		}
//		else {
//			y=space.getY();
//		}
//		SnapshotParameters parameters = new SnapshotParameters();
//
//		parameters.setFill(Color.TRANSPARENT);
//
//
//		// WritableImage(PixelReader reader, int x, int y, int width, int height)
//		if((x!=0)||(y!=0)){
//			WritableImage newImage = new WritableImage(reader, 0,0,(int) x, (int) y);
//			Image n1 = scale(image, (image.getWidth()/(fullWidth/screen.getWidth())), (image.getHeight()/(fullHeight/screen.getHeight())), true, parameters);
//			// File file = new File("test.png");
//			// RenderedImage renderedImage = SwingFXUtils.fromFXImage(newImage, null);
//			// try {
//			// ImageIO.write(
//			// renderedImage,
//			// "png",
//			// file);
//			// } catch (IOException e) {
//			// // TODO Auto-generated catch block
//			// e.printStackTrace();
//			// }
//			imageView.setImage(n1);
//			imageView.setPreserveRatio(true);
//			if(((x!=screen.getWidth())||(y!=screen.getHeight()))&&((image.getWidth()>screen.getWidth()||(image.getHeight()>screen.getHeight())))){
//				imageView.setFitHeight(360);
//				imageView.setFitWidth(640);
//			}
//			else if((image.getWidth()>=screen.getWidth()) || (image.getHeight()>=screen.getHeight())){
//				if(x>y){
//					imageView.setFitHeight(y);
//				} else {
//					imageView.setFitWidth(x);
//				}
//			}
//
//
//
//		}
//
//		return imageView;
//	}

	private ImageView hidden(ImageView imageView, Media media, Point2D space, boolean hLock, boolean vLock){
		// TODO Auto-generated method stub

		imageView.setPreserveRatio(false);
		double x=0,y=0;

		String imageFile = media.getFile().toURI().toString();

		Image image = new Image(imageFile);

		PixelReader reader = image.getPixelReader();

		System.out.println("X = "+space.getX()+" >= "+image.getWidth());
		if(space.getX()>=image.getWidth()){
			x=image.getWidth();
		}

        else if(space.getX()==0){
        	x=screen.getWidth();
        }

        else {
        	x=space.getX();
        }

        if(space.getY()>=image.getHeight()){
        	y=image.getHeight();
        }

        else if(space.getY()==0){
        	y=screen.getHeight();
        }
        else {
        	y=space.getY();
        }

//	        WritableImage(PixelReader reader, int x, int y, int width, int height)
        if((x!=0)||(y!=0)){
        	WritableImage newImage = new WritableImage(reader, 0,0,(int) x, (int) y);
        	File file = new File("test.png");
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(newImage, null);
            try {
    			ImageIO.write(
    			        renderedImage,
    			        "png",
    			        file);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            imageView.setImage(newImage);

            imageView.setFitHeight(y);
    		imageView.setFitWidth(x);

        }



		System.out.println("Compute area in screen: "+imageView.computeAreaInScreen());
		return imageView;
	}

	public Point2D spaceAvailable(ImageView imageView, double left, double right, double top, double bottom, String w, String h){
		Point2D space = null;

		double x=screen.getWidth(), y=screen.getHeight();
		if(left>100) left=100;
		if(top>100) top=100;
		if(right>100) right=100;
		if(bottom>100) bottom=100;

		//atualizar width e height
		//testar se left + width formam uma combinacao valida
		if(left!=0){
			x=screen.getWidth()-((left/100)*screen.getWidth());

		} else { //testar se right + width formam uma combinacao valida
			if(right!=0){
				x=(right/100)*screen.getWidth();
			}
		}

		if(top!=0){
			y=screen.getHeight()-((top/100)*screen.getHeight());
		} else { //testar se bottom + height formam uma combinacao valida
			if(bottom!=0){
				y=(bottom/100)*screen.getHeight();
			}
		}

		space = new Point2D.Double(x,y);

		return space;
	}

	public ImageView slice(ImageView imageView, Point2D space, Media media){

		imageView.setPreserveRatio(true);

		String imageFile = media.getFile().toURI().toString();

		System.out.println("imageFile: "+imageFile);

		Image image = new Image(imageFile);

		double tx=image.getWidth();

		double ty=image.getHeight();

		PixelReader reader = image.getPixelReader();

		WritableImage newImage = new WritableImage(reader, (int)  image.getWidth(), (int) image.getHeight());

        SnapshotParameters parameters = new SnapshotParameters();

        parameters.setFill(Color.TRANSPARENT);

        ImageView nv = new ImageView(newImage);

        nv.setPreserveRatio(true);

		imageView.setImage(newImage);

		Image smlImg = scale(imageView.getImage(),(int) space.getX(),(int) space.getY(), true, parameters);

//		saveToFile(smlImg,"-2");

		if((tx<screen.getWidth())&&(ty<screen.getHeight())){

			nv.setImage(smlImg);

	        nv.setPreserveRatio(true);

			reader = smlImg.getPixelReader();

//			saveToFile(smlImg,"-2");

			System.out.println("(int) space.getX() "+(int) space.getX()+" space.getY() "+(int) space.getY());

			System.out.println("(img X() "+(int) smlImg.getWidth()+" img Y() "+(int) smlImg.getHeight());

			if(smlImg.getWidth()<space.getX()) space.setLocation((int) smlImg.getWidth(), space.getY());

			if(smlImg.getHeight()<space.getY()) space.setLocation(space.getX(), (int) smlImg.getHeight());


		}


		WritableImage wi = nv.snapshot(parameters, new WritableImage(reader, 0,0,(int) space.getX(),(int) space.getY()));

		if(ty>tx){

			nv.setFitWidth(space.getX());

		}
		else {

			nv.setFitHeight(space.getY());

		}

//		saveToFile(nv.getImage(),"-3");

		System.out.println("Tamanho 1: "+nv.getFitWidth()+"x"+nv.getFitHeight());
		newImage = nv.snapshot(parameters, wi);

//		saveToFile(newImage,"-4");

		imageView.setImage(newImage);
		System.out.println("Tamanho 2: "+imageView.getFitWidth()+"x"+imageView.getFitHeight());
//		saveToFile(smlImg,"-5");

		return imageView;

	}


	public static void saveToFile(Image image, String n) {
	    File outputFile = new File("thisTest"+n+".png");
	    BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
	    try {
	      ImageIO.write(bImage, "png", outputFile);
	    } catch (IOException e) {
	      throw new RuntimeException(e);
	    }
	  }


	public Image scale(Image source, double targetWidth, double targetHeight, boolean preserveRatio, SnapshotParameters parameters) {
	    ImageView imageView = new ImageView(source);
	    imageView.setPreserveRatio(preserveRatio);
	    imageView.setFitWidth(targetWidth);
	    imageView.setFitHeight(targetHeight);
	    return imageView.snapshot(parameters, null);
	}
	//checar se esta lock ou nao para poder redimensionar tamanho.

	public ImageView fill(ImageView imageView, Point2D space){
		imageView.setViewport(null);
		imageView.setPreserveRatio(false);
		imageView.setFitWidth(space.getX());
		imageView.setFitHeight(space.getY());
		return imageView;
	}

	/*public ImageView fill(ImageView imageView, Point2D space, boolean hLock, boolean vLock){ //image, available space

		imageView.setViewport(null);
		imageView.setPreserveRatio(false);
		System.out.println("Settando width para: "+space.getX()+" height para: "+space.getY());

		if(space.getX()!=0) {
			imageView.setFitWidth(space.getX());
		} else {
			//imageView.setFitWidth(221);
			imageView.setFitWidth(screen.getWidth());
		}

		if(space.getY()!=0) {
			imageView.setFitHeight(space.getY());
		} else {
			//imageView.setFitHeight(222);
			imageView.setFitHeight(screen.getHeight());
		}


		return imageView;
	}*/

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

	//	se left = 10% coloca a margem esquerda da imagem numa distancia de 10% do 0
	//	nesse caso imagem fica com 90% de largura
	// 	e ajustar o width que fica ouvindo

	public void moveMediaLeft(ImageView mediaContent, Media media, double left){

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
//
//		mediaContent.setFitHeight((Double.parseDouble(media.getPresentationProperty().getSizeProperty().getHeight().replace("%",""))/100)*screen.getHeight());

		PositionProperty pp = media.getPresentationProperty().getPositionProperty();
		pp.setRight(Double.toString(right));

		media.getPresentationProperty().setPositionProperty(pp);

        double screenWidth = screen.getWidth();
        double iw = mediaContent.getImage().getWidth();
		double dXRight = ((right/100)*screenWidth);

		String imageFile = media.getFile().toURI().toString();

		System.out.println("imageFile: "+imageFile);

		Image image = new Image(imageFile);

		double tx=image.getWidth();

		if(media.getPresentationProperty().getSizeProperty().getAspectRatio()!=AspectRatio.FILL){
			System.out.println("Size of media is: "+mediaContent.getImage().getWidth());
			if(tx<screenWidth){
				if(tx < dXRight){
					mediaContent.setTranslateX(dXRight-tx);
				} else {
					mediaContent.setFitWidth(dXRight);
				}


//				mediaContent.setTranslateX(-mediaContent.getImage().getWidth()-dXRight+screenWidth);
			}
//			else {
//				mediaContent.setTranslateX(-dXRight);
//			}
//
//		} else{
//			mediaContent.setTranslateX(0);
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

        int boundHeight = (int)mediaContent.getLayoutBounds().getHeight();
		int boundHeightParent = (int)mediaContent.getBoundsInParent().getHeight();
		int boundHeightLocal = (int)mediaContent.getBoundsInLocal().getHeight();
        //the x or y variables of a shape, or translateX,
        //translateY should never be bound to boundsInParent for the purpose of positioning the node.
//		System.out.println("esse e o bound height= "+boundHeight);
		double screenHeight = screen.getHeight();

		System.out.println(boundHeight);
		System.out.println(boundHeightLocal);
		System.out.println(boundHeightParent);

		double yZero = (screenHeight)-boundHeight; //Referencial da tela (borda inferior)
		System.out.println("yZero: "+ yZero);

		double dYDown = (bottom/100)*screenHeight;
		//System.out.println("yZero-dYDown = "+(yZero-dYDown));
//		mediaContent.setFitWidth((screen.getWidth()));
		String imageFile = media.getFile().toURI().toString();

		System.out.println("imageFile: "+imageFile);

		Image image = new Image(imageFile);

		double ty=image.getHeight();

		if(media.getPresentationProperty().getSizeProperty().getAspectRatio()!=AspectRatio.FILL){

			if(ty<screenHeight){

				if(ty < dYDown){
					double x = dYDown-ty;
					System.out.println("Move: "+x);
					mediaContent.setTranslateY(dYDown-ty);

				} else {
					mediaContent.setFitHeight(dYDown);
				}

//					mediaContent.setTranslateX(-mediaContent.getImage().getWidth()-dXRight+screenWidth);
//
//
//				System.out.println("yZero= "+yZero);
//				System.out.println("dYDown= "+dYDown);
//				System.out.println("screenHeight= "+screenHeight);
//				System.out.println("mediaContent.getImage().getHeight()= "+mediaContent.getImage().getHeight());
//				double result = (-dYDown+screenHeight-mediaContent.getImage().getHeight());
//				System.out.println("result= "+result);
////				mediaContent.setTranslateY(yZero-dYDown+screenHeight-mediaContent.getImage().getHeight());
//				mediaContent.setTranslateY(result);
//
//
//
//			} else{
//				mediaContent.setTranslateY(yZero-dYDown);
//
			}
		} else {
			mediaContent.setTranslateY(yZero-dYDown);

		}
//		setImagePresentationProperties(mediaContent, media);
//
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