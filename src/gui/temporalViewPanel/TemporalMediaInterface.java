package gui.temporalViewPanel;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.nclDocument.extendedAna.Media;

public class TemporalMediaInterface {

	private int THUMBNAIL_WIDTH = 90;
	
	private XYChart.Series<Number, String> beginSerie;
	private XYChart.Series<Number, String> endSerie;
	private XYChart.Data<Number, String> beginData;
	private XYChart.Data<Number, String> endData;
	private VBox node;
	private BorderPane invisibleNode;
	private double begin;
	private double end;
	private String line;
	private String id;
	private Media media;
	private HBox mediaNameConatiner;
	private Logger temporalMediaInterfaceLogger = Logger.getLogger("TemporalMediaInterfaceLogger");
	private VBox channelPane;
	private double channelWidth;
	private double dragDeltaX;
	private double dragDeltaY;
	private double invisibleNodeDragDeltaX;
	private double invisibleNodeDragDeltaY;
	
	public TemporalMediaInterface(double begin, double end, String line, String id, Media media, VBox channelPane){
		
		this.id = id;
		this.begin = begin;
		this.end = end-begin;
		this.line = line;
		this.media = media;
		this.channelPane = channelPane;
		
		initializeComponents();
		
		setListenerEvents();
		
	}

	private void initializeComponents() {
		
		endSerie = new XYChart.Series<Number, String>();
		endSerie.setName(id);
		endData = new XYChart.Data<Number, String>(end, line);
		setBegin();
		node = new VBox();
		mediaNameConatiner = new HBox();
		mediaNameConatiner.setId("media-name-container");
		setNodeLayout();
		endData.setNode(node);
		endSerie.getData().add(endData);
		
		channelPane.widthProperty().addListener(new ChangeListener(){
	           @Override 
	           public void changed(ObservableValue o,Object oldVal, Object newVal){
	        	   channelWidth = (double) newVal;
	           }
	         });
		
		
	}
	
	private void setBegin(){
		
		beginSerie = new XYChart.Series<Number, String>();
		beginData = new XYChart.Data<Number, String>(begin, line);
		invisibleNode = new BorderPane();
		invisibleNode.setVisible(false);
		beginData.setNode(invisibleNode);
		beginSerie.getData().add(beginData);
		
	}
	
	private void setListenerEvents(){

		node.setOnMouseEntered(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	            node.getStylesheets().add("gui/styles/temporalMediaInterfaceEntered.css");
	            mediaNameConatiner.getStylesheets().add("gui/styles/temporalMediaInterfaceEntered.css");
	            node.setCursor(Cursor.HAND);
	        }
	    });
	    node.setOnMouseExited(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	            node.getStylesheets().remove("gui/styles/temporalMediaInterfaceEntered.css");
	            mediaNameConatiner.getStylesheets().remove("gui/styles/temporalMediaInterfaceEntered.css");
	        }
	    });
	    node.setOnMousePressed(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent mouseEvent) {
				dragDeltaX = node.getLayoutX() - mouseEvent.getSceneX();
			    dragDeltaY = node.getLayoutY() - mouseEvent.getSceneY();
			    invisibleNodeDragDeltaX = invisibleNode.getLayoutX() - mouseEvent.getSceneX();
			    invisibleNodeDragDeltaY = invisibleNode.getLayoutY() - mouseEvent.getSceneY();
			    node.setCursor(Cursor.MOVE);
			}
	    	
	    });
	    node.setOnMouseReleased(new EventHandler<MouseEvent>() {
	    	  @Override public void handle(MouseEvent mouseEvent) {
	    		  node.setCursor(Cursor.HAND);
	    	  }
	    	});
	    node.setOnMouseDragged(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent mouseEvent) {
	        	
	        	//node.setTranslateX(mouseEvent.getSceneX() + dragDeltaX);
	        	invisibleNode.setLayoutX(mouseEvent.getSceneX() + invisibleNodeDragDeltaX);
	            node.setLayoutX(mouseEvent.getSceneX() + dragDeltaX);
	        	
//	        	Double relocationValue = mouseEvent.getSceneX()-channelWidth;
//	        	if(relocationValue >= 0){
//	        		//node.relocate(relocationValue, node.getLayoutY());
//	        		//node.setTranslateX(node.getLayoutX());
//	        		node.setLayoutX(());
//	        		node.setCursor(Cursor.MOVE);
//	        	}
	        	
	        }
	        
	    });
		
	}

	@SuppressWarnings("unchecked")
	private void setNodeLayout() {
		
		node.getStylesheets().add("gui/styles/temporalViewPane.css");
		
		final BorderPane temporalMedialayoutContainer = new BorderPane();
		temporalMedialayoutContainer.setId("temporal-media-layout-container");
		temporalMedialayoutContainer.getStylesheets().add("gui/styles/temporalViewPane.css");
		
		media.setName(media.getSrc().toString());
		media.setPath(media.getMediaAbsolutePath());
		media.setImportedMediaType(media.identifyType());
		try {
			media.generateMediaIcon();
		} catch (InterruptedException e) {
			temporalMediaInterfaceLogger.log(Level.WARNING, "Fails to generate media icon."+e.getMessage());
		}
		final ImageView imageView = media.getMediaIcon();
		
		Label mediaName = new Label(media.getName());
		mediaName.setId("media-name");
		mediaName.getStylesheets().add("gui/styles/temporalViewPane.css");
		
		mediaNameConatiner.getStylesheets().add("gui/styles/temporalViewPane.css");
		mediaNameConatiner.getChildren().add(mediaName);
		//mediaNameConatiner.setPrefHeight(5);
		VBox container = new VBox();
		node.heightProperty().addListener(new ChangeListener(){
			@Override 
	        public void changed(ObservableValue o,Object oldVal, Object newVal){
				imageView.setFitHeight((double) newVal);
	        	mediaNameConatiner.setPrefHeight((double) newVal);
	        	//temporalMedialayoutContainer.setPrefHeight((double) newVal);
			}
	    });
		temporalMedialayoutContainer.setCenter(imageView);
		temporalMedialayoutContainer.setBottom(mediaNameConatiner);
		node.getChildren().add(temporalMedialayoutContainer);
		
		node.getChildren().add(imageView);
		node.getChildren().add(mediaNameConatiner);
		
		
	}
	
	public XYChart.Series<Number, String> getBeginSerie() {
		return beginSerie;
	}

	public void setBeginSerie(XYChart.Series<Number, String> beginSerie) {
		this.beginSerie = beginSerie;
	}

	public XYChart.Series<Number, String> getEndSerie() {
		return endSerie;
	}

	public void setEndSerie(XYChart.Series<Number, String> endSerie) {
		this.endSerie = endSerie;
	}

	public XYChart.Data<Number, String> getBeginData() {
		return beginData;
	}

	public void setBeginData(XYChart.Data<Number, String> beginData) {
		this.beginData = beginData;
	}

	public XYChart.Data<Number, String> getEndData() {
		return endData;
	}

	public void setEndData(XYChart.Data<Number, String> endData) {
		this.endData = endData;
	}

	public VBox getNode() {
		return node;
	}

	public void setNode(VBox node) {
		this.node = node;
	}

	public double getBegin() {
		return begin;
	}

	public void setBegin(double begin) {
		this.begin = begin;
	}

	public double getEnd() {
		return end;
	}

	public void setEnd(double end) {
		this.end = end;
	}

	public String getChannel() {
		return line;
	}

	public void setChannel(String channel) {
		this.line = channel;
	}
	
	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}
	
}
