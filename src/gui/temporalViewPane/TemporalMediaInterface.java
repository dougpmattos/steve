package gui.temporalViewPane;

import java.math.BigDecimal;
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
import model.common.Media;

public class TemporalMediaInterface {

	private static final int SIZE_DIFFERENCE = 15;

	private int THUMBNAIL_WIDTH = 90;
	
	private XYChart.Series<Number, String> beginSerie;
	private XYChart.Series<Number, String> endSerie;
	private XYChart.Data<Number, String> beginData;
	private XYChart.Data<Number, String> endData;
	private BorderPane node;
	private BorderPane invisibleNode;
	private double begin;
	private BigDecimal previousMediaEnd;
	private double end;
	private String line;
	private String id;
	private Media media;
	private HBox mediaNameContainer;
	private Logger temporalMediaInterfaceLogger = Logger.getLogger("TemporalMediaInterfaceLogger");
	private TemporalChainPane temporalChainPane;
	private double dragDeltaX;
	private double dragDeltaY;
	private double invisibleNodeDragDeltaX;
	private double invisibleNodeDragDeltaY;
	
	public TemporalMediaInterface(double begin, BigDecimal previousMediaEnd, double end, String line, String id, Media media, TemporalChainPane temporalChainPane){
		
		this.id = id;
		this.begin = begin;
		this.previousMediaEnd = previousMediaEnd;
		this.end = end;
		this.line = line;
		this.media = media;
		this.temporalChainPane = temporalChainPane;
		
		initializeComponents();
		
		setListenerEvents();
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initializeComponents() {
		
		setBegin();
		endSerie = new XYChart.Series<Number, String>();
		endSerie.setName(id);
		BigDecimal bigDecimalEnd = new BigDecimal(Double.toString(end));
		BigDecimal bigDecimalBegin = new BigDecimal(Double.toString(begin));
		endData = new XYChart.Data<Number, String>(bigDecimalEnd.subtract(bigDecimalBegin), line);
		node = new BorderPane();
		mediaNameContainer = new HBox();
		mediaNameContainer.setId("media-name-container");
		setNodeLayout();
		endData.setNode(node);
		endSerie.getData().add(endData);		
		
	}
	
	private void setBegin(){
		
		beginSerie = new XYChart.Series<Number, String>();
		BigDecimal bigDecimalBegin = new BigDecimal(Double.toString(begin));
		beginData = new XYChart.Data<Number, String>(bigDecimalBegin.subtract(previousMediaEnd), line);
		invisibleNode = new BorderPane();
		invisibleNode.setVisible(false);
		beginData.setNode(invisibleNode);
		beginSerie.getData().add(beginData);
		
	}
	
	private void setListenerEvents(){

		node.setOnMouseEntered(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	            node.getStylesheets().add("gui/temporalViewPane/styles/temporalMediaInterfaceEntered.css");
	            mediaNameContainer.getStylesheets().add("gui/temporalViewPane/styles/temporalMediaInterfaceEntered.css");
	            if((node.getCursor() != null && node.getCursor() != Cursor.MOVE) || node.getCursor() == null){
	            	node.setCursor(Cursor.HAND);
	            }
	        }
	    });
	    node.setOnMouseExited(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	            node.getStylesheets().remove("gui/temporalViewPane/styles/temporalMediaInterfaceEntered.css");
	            mediaNameContainer.getStylesheets().remove("gui/temporalViewPane/styles/temporalMediaInterfaceEntered.css");
	        }
	    });
	    node.setOnMousePressed(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent mouseEvent) {
				node.getStylesheets().add("gui/temporalViewPane/styles/temporalMediaInterfacePressed.css");
	            mediaNameContainer.getStylesheets().add("gui/temporalViewPane/styles/temporalMediaInterfacePressed.css");
				dragDeltaX = node.getLayoutX() - mouseEvent.getSceneX();
			    dragDeltaY = node.getLayoutY() - mouseEvent.getSceneY();
			    node.setCursor(Cursor.MOVE);
			    mouseEvent.consume();
			}
	    	
	    });
	    node.setOnMouseDragged(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent mouseEvent) {

	            node.setLayoutX(mouseEvent.getSceneX() + dragDeltaX);
	            node.setLayoutY(mouseEvent.getSceneY() + dragDeltaY);
	            
//	        	Double relocationValue = mouseEvent.getSceneX()-channelWidth;
//	        	if(relocationValue >= 0){
//	        		//node.relocate(relocationValue, node.getLayoutY());
//	        		//node.setTranslateX(node.getLayoutX());
//	        		node.setLayoutX(());
//	        		node.setCursor(Cursor.MOVE);
//	        	}
	        	
	        }
	        
	    });
	    node.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
	
				node.getStylesheets().remove("gui/temporalViewPane/styles/temporalMediaInterfacePressed.css");
				mediaNameContainer.getStylesheets().remove("gui/temporalViewPane/styles/temporalMediaInterfacePressed.css");
	    		node.setCursor(Cursor.HAND);
	    		
		    	double newBegin = 5;
		    	double newEnd = 17.17;
	
				beginData.setXValue(begin);
		        endData.setXValue(end-begin);
		        
			}
	    	
	    });
		
	}

	@SuppressWarnings("unchecked")
	private void setNodeLayout() {
		
		node.getStylesheets().add("gui/temporalViewPane/styles/temporalViewPane.css");

		media.generateMediaIcon();
		final ImageView imageView = media.generateMediaIcon();
		
		Label mediaName = new Label(media.getName());
		mediaName.setId("media-name");
		mediaNameContainer.getChildren().add(mediaName);
		
		node.heightProperty().addListener(new ChangeListener(){
			@Override 
	        public void changed(ObservableValue o,Object oldVal, Object newVal){
				Double heightValue = (double) newVal-SIZE_DIFFERENCE;
				if(heightValue >= 0){
					imageView.setFitHeight(heightValue);					
				}
			}
	    });
		node.widthProperty().addListener(new ChangeListener(){
			@Override 
	        public void changed(ObservableValue o,Object oldVal, Object newVal){
				imageView.setFitWidth((double) newVal);
			}
	    });

		node.setLeft(imageView);
		node.setBottom(mediaNameContainer);
		
		
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

	public BorderPane getNode() {
		return node;
	}

	public void setNode(BorderPane node) {
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
