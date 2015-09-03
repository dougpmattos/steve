package view.temporalViewPane;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import model.common.Media;
import model.temporalView.TemporalChain;
import view.repositoryPane.RepositoryMediaItemContainer;
import view.repositoryPane.RepositoryPane;
import controller.Controller;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TemporalMediaNode {

	private static final int SIZE_DIFFERENCE = 15;
	
	private Controller controller;
	private Media media;
	private TemporalMediaNodeList temporalMediaNodeList;
	private TemporalViewPane temporalViewPane;
	private RepositoryPane repositoryPane;
	private TemporalChain temporalChainModel;
	private XYChart.Series<Number, String> beginSerie;
	private XYChart.Data<Number, String> beginData;
	private XYChart.Series<Number, String> endSerie;
	private XYChart.Data<Number, String> endData;
	private HBox containerNode;
	
	private double dragDeltaX;
	private double dragDeltaY;
	
	public TemporalMediaNode(Controller controller, Media media, TemporalChain temporalChainModel, TemporalViewPane temporalViewPane, RepositoryPane repositoryPane, TemporalMediaNodeList temporalMediaNodeList){
		
		this.controller = controller;
		this.media = media;
		this.temporalChainModel = temporalChainModel;
		this.temporalMediaNodeList = temporalMediaNodeList;
		this.temporalViewPane = temporalViewPane;
		this.repositoryPane = repositoryPane;
		
		createBeginSerieData();
		createEndSerieData();	
		
	}

	private void createEndSerieData() {
		
		endSerie = new XYChart.Series<Number, String>();
		endSerie.setName(media.getName());
		endData = new XYChart.Data<Number, String>(media.getDuration(), temporalMediaNodeList.getId());
		endData.setNode(createNode());
		endSerie.getData().add(endData);
		
	}

	private void createBeginSerieData() {
		
		beginSerie = new XYChart.Series<Number, String>();
		BigDecimal bigDecimalBegin = new BigDecimal(Double.toString(media.getBegin()));
	
		Media previousMedia = temporalMediaNodeList.getPreviousMedia(this) != null ? temporalMediaNodeList.getPreviousMedia(this).getMedia(): null;
		
		if(previousMedia != null){
			beginData = new XYChart.Data<Number, String>(bigDecimalBegin.subtract(new BigDecimal(previousMedia.getEnd().toString())), temporalMediaNodeList.getId());
		} else{
			beginData = new XYChart.Data<Number, String>(bigDecimalBegin, temporalMediaNodeList.getId());
		}
		
		BorderPane invisibleNode = new BorderPane();
		invisibleNode.setVisible(false);
		beginData.setNode(invisibleNode);
		beginSerie.getData().add(beginData);
		
	}
	
	private HBox createNode() {
		
		containerNode = new HBox();
		containerNode.setId("temporal-media-container");
	
		Label mediaName = new Label(media.getName());
		mediaName.setId("media-name");

		Rectangle mediaImageClip = new Rectangle();
		mediaImageClip.setId("media-image");
		mediaImageClip.setArcHeight(16);
		mediaImageClip.setArcWidth(16);
		ImageView imageView = media.generateMediaIcon();
		imageView.setClip(mediaImageClip);
		
		containerNode.heightProperty().addListener(new ChangeListener(){
			@Override 
	        public void changed(ObservableValue o,Object oldVal, Object newVal){
					imageView.setFitHeight((double) newVal);
					mediaImageClip.setHeight((double) newVal);
			}
	    });
		containerNode.widthProperty().addListener(new ChangeListener(){
			@Override 
	        public void changed(ObservableValue o,Object oldVal, Object newVal){
				imageView.setFitWidth((double) newVal);
				mediaName.setPrefWidth((double) newVal);
				mediaImageClip.setWidth((double) newVal);
			}
	    });
		
		containerNode.getChildren().add(imageView);
		containerNode.getChildren().add(mediaName);
		
		setListenerEvents(containerNode, repositoryPane);
		
		return containerNode;
		
	}
	
	private void setListenerEvents(HBox node, RepositoryPane repositoryPane){
		
		node.setOnMousePressed(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent mouseEvent) {
				
				for(Node repositoryMediaItemContainer : repositoryPane.getRepositoryMediaItemContainerListPane().getAllTypes()){
					
					RepositoryMediaItemContainer repoMediaItemContainer = (RepositoryMediaItemContainer) repositoryMediaItemContainer;
					repoMediaItemContainer.setSelected(false);
	        		repoMediaItemContainer.getStylesheets().remove("view/repositoryPane/styles/mousePressedRepositoryMedia.css");

				}
				
				temporalViewPane.setSelectedMedia(media);
				
				Label source = (Label) node.getChildren().get(1);

				if(source.getStylesheets().isEmpty()){
					source.getStylesheets().add("view/temporalViewPane/styles/mousePressedTemporalMediaNode.css");
	        	}
				
				for(Tab temporalTab : temporalViewPane.getTemporalChainTabPane().getTabs()){
					
					TemporalChainPane temporalChainPane = (TemporalChainPane) temporalTab.getContent();
					for(TemporalMediaNodeList temporalMediaNodeList : temporalChainPane.getTemporalChainMediaListList()){
						for(TemporalMediaNode temporalMediaNode : temporalMediaNodeList){
							if(temporalMediaNode.getMedia() != media){
								Label labelMediaNode = (Label) temporalMediaNode.getContainerNode().getChildren().get(1);
								labelMediaNode.getStylesheets().remove("view/temporalViewPane/styles/mousePressedTemporalMediaNode.css");
							}
						}
						
					}	
				}
			
			    mouseEvent.consume();
			    
			}
	    	
	    });
		
	    node.setOnMouseDragged(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent mouseEvent) {
	        	node.setTranslateX(mouseEvent.getSceneX() - node.getLayoutX());
	        }
	        
	    });
	    node.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
		        
			}
	    	
	    });
		
	}

	public XYChart.Series<Number, String> getEndSerie() {
		return endSerie;
	}

	public XYChart.Series<Number, String> getBeginSerie() {
		return beginSerie;
	}
	
	public Media getMedia(){
		return media;
	}
	
	public HBox getContainerNode(){
		return containerNode;
	}
	
}
