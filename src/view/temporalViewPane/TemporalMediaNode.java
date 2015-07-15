package view.temporalViewPane;

import java.math.BigDecimal;

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
import javafx.scene.shape.Rectangle;
import model.common.Media;
import model.temporalView.TemporalChain;
import controller.Controller;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TemporalMediaNode {

	private static final int SIZE_DIFFERENCE = 15;
	
	private Controller controller;
	private Media media;
	private TemporalChainMediaLine temporalChainMediaLine;
	private TemporalViewPane temporalViewPane;
	private TemporalChain temporalChainModel;
	private XYChart.Series<Number, String> beginSerie;
	private XYChart.Data<Number, String> beginData;
	private XYChart.Series<Number, String> endSerie;
	private XYChart.Data<Number, String> endData;
	
	private double dragDeltaX;
	private double dragDeltaY;
	
	public TemporalMediaNode(Controller controller, Media media, TemporalChain temporalChainModel, TemporalViewPane temporalViewPane, TemporalChainMediaLine temporalChainMediaLine){
		
		this.controller = controller;
		this.media = media;
		this.temporalChainModel = temporalChainModel;
		this.temporalChainMediaLine = temporalChainMediaLine;
		this.temporalViewPane = temporalViewPane;
		
		createBeginSerieData();
		createEndSerieData();	
		
	}

	private void createEndSerieData() {
		
		endSerie = new XYChart.Series<Number, String>();
		endSerie.setName(media.getName());
		endData = new XYChart.Data<Number, String>(media.getDuration(), temporalChainMediaLine.getId());
		endData.setNode(createNode());
		endSerie.getData().add(endData);
		
	}

	private void createBeginSerieData() {
		
		beginSerie = new XYChart.Series<Number, String>();
		BigDecimal bigDecimalBegin = new BigDecimal(Double.toString(media.getBegin()));
		Media previousMedia = temporalChainMediaLine.getPreviousMedia(media);
		
		if(previousMedia != null){
			beginData = new XYChart.Data<Number, String>(bigDecimalBegin.subtract(new BigDecimal(previousMedia.getEnd().toString())), temporalChainMediaLine.getId());
		} else{
			beginData = new XYChart.Data<Number, String>(bigDecimalBegin, temporalChainMediaLine.getId());
		}
		
		BorderPane invisibleNode = new BorderPane();
		invisibleNode.setVisible(false);
		beginData.setNode(invisibleNode);
		beginSerie.getData().add(beginData);
		
	}
	
	private HBox createNode() {
		
		HBox container = new HBox();
		container.setId("temporal-media-container");
	
		Label mediaName = new Label(media.getName());
		mediaName.setId("media-name");

		Rectangle mediaImageClip = new Rectangle();
		mediaImageClip.setId("media-image");
		mediaImageClip.setArcHeight(16);
		mediaImageClip.setArcWidth(16);
		ImageView imageView = media.generateMediaIcon();
		imageView.setClip(mediaImageClip);
		
		container.heightProperty().addListener(new ChangeListener(){
			@Override 
	        public void changed(ObservableValue o,Object oldVal, Object newVal){
				Double heightValue = (double) newVal-SIZE_DIFFERENCE;
				if(heightValue >= 0){
					imageView.setFitHeight((double) newVal);
					mediaImageClip.setHeight((double) newVal);
				}
			}
	    });
		container.widthProperty().addListener(new ChangeListener(){
			@Override 
	        public void changed(ObservableValue o,Object oldVal, Object newVal){
				imageView.setFitWidth((double) newVal);
				mediaName.setPrefWidth((double) newVal);
				mediaImageClip.setWidth((double) newVal);
			}
	    });
		
		container.getChildren().add(imageView);
		container.getChildren().add(mediaName);
		
		setListenerEvents(container);
		
		return container;
		
	}
	
	private void setListenerEvents(HBox node){
		
		node.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent mouseEvent) {
			
				((Label) node.getChildren().get(1)).getStylesheets().add("view/temporalViewPane/styles/mouseFocusedTemporalMediaNode.css");

				dragDeltaX = mouseEvent.getSceneX() - node.getLayoutX();
			    dragDeltaY = mouseEvent.getSceneY() - node.getLayoutY();
			    node.setCursor(Cursor.MOVE);
				
				if(temporalViewPane.getSelectedMedia() == null || !temporalViewPane.getSelectedMedia().equals(media)){
					temporalViewPane.setSelectedMedia(media);
				}
			    
			    mouseEvent.consume();
			    
			}
	    	
	    });
		
		node.setOnMousePressed(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent mouseEvent) {
			
				((Label) node.getChildren().get(1)).getStylesheets().add("view/temporalViewPane/styles/mouseFocusedTemporalMediaNode.css");

				dragDeltaX = mouseEvent.getSceneX() - node.getLayoutX();
			    dragDeltaY = mouseEvent.getSceneY() - node.getLayoutY();
			    node.setCursor(Cursor.MOVE);
				
				if(temporalViewPane.getSelectedMedia() == null || !temporalViewPane.getSelectedMedia().equals(media)){
					temporalViewPane.setSelectedMedia(media);
				}
			    
			    mouseEvent.consume();
			    
			}
	    	
	    });
		
	    node.setOnMouseDragged(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent mouseEvent) {

	            node.setLayoutX(mouseEvent.getSceneX() + dragDeltaX);
	            node.setLayoutY(mouseEvent.getSceneY() + dragDeltaY);
	            
	        	Double relocationValue = mouseEvent.getSceneX();
	        	if(relocationValue >= 0){
	        		node.relocate(relocationValue, node.getLayoutY());
	        		node.setTranslateX(node.getLayoutX());
	        		node.setCursor(Cursor.MOVE);
	        	}
	        	
	        }
	        
	    });
	    node.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
	
				node.getStylesheets().remove("view/temporalViewPane/styles/mouseFocusedTemporalMediaNode.css");
//				mediaNameContainer.getStylesheets().remove("view/temporalViewPane/styles/temporalMediaInterfacePressed.css");
//	    		node.setCursor(Cursor.HAND);
//	    		
//		    	double newBegin = 5;
//		    	double newEnd = 17.17;
//	
//				beginData.setXValue(begin);
//		        endData.setXValue(end-begin);
		        
			}
	    	
	    });
		
	}

	public XYChart.Series<Number, String> getEndSerie() {
		return endSerie;
	}

	public XYChart.Series<Number, String> getBeginSerie() {
		return beginSerie;
	}
	
}
