package view.temporalViewPane;

import java.math.BigDecimal;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import model.common.Media;
import model.temporalView.TemporalChain;
import model.utility.MediaUtil;
import view.repositoryPane.RepositoryMediaItemContainer;
import view.repositoryPane.RepositoryPane;
import controller.Controller;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TemporalMediaNode {

	private Controller controller;
	private Media media;
	private TemporalMediaNodeList temporalMediaNodeList;
	private TemporalViewPane temporalViewPane;
	private RepositoryPane repositoryPane;
	private TemporalChain temporalChainModel;
	private XYChart.Data<Number, String> invisibleBeginData;
	private XYChart.Data<Number, String> endData;
	private HBox containerNode;
	
	public TemporalMediaNode(Controller controller, Media media, TemporalChain temporalChainModel, TemporalViewPane temporalViewPane, RepositoryPane repositoryPane, TemporalMediaNodeList temporalMediaNodeList){
		
		this.controller = controller;
		this.media = media;
		this.temporalChainModel = temporalChainModel;
		this.temporalMediaNodeList = temporalMediaNodeList;
		this.temporalViewPane = temporalViewPane;
		this.repositoryPane = repositoryPane;
		
		createInvisibleBeginData();
		createEndData();	
		
	}

	private void createEndData() {
		
		endData = new XYChart.Data<Number, String>(media.getDuration(), temporalMediaNodeList.getId());
		endData.setNode(createNode());
		
	}

	private void createInvisibleBeginData() {

		BigDecimal bigDecimalBegin = new BigDecimal(Double.toString(media.getBegin()));
	
		Media previousMedia = temporalMediaNodeList.getPreviousMedia(this) != null ? temporalMediaNodeList.getPreviousMedia(this).getMedia(): null;
		
		if(previousMedia != null){
			invisibleBeginData = new XYChart.Data<Number, String>(MediaUtil.approximateDouble(bigDecimalBegin.subtract(new BigDecimal(previousMedia.getEnd().toString())).doubleValue()), temporalMediaNodeList.getId());
		} else{
			invisibleBeginData = new XYChart.Data<Number, String>(MediaUtil.approximateDouble(bigDecimalBegin.doubleValue()), temporalMediaNodeList.getId());
		}
		
		BorderPane invisibleNode = new BorderPane();
		invisibleNode.setVisible(false);
		invisibleBeginData.setNode(invisibleNode);
		
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

	public XYChart.Data<Number, String> getEndData() {
		return endData;
	}

	public XYChart.Data<Number, String> getInvisibleBeginData() {
		return invisibleBeginData;
	}
	
	public Media getMedia(){
		return media;
	}
	
	public HBox getContainerNode(){
		return containerNode;
	}
	
}
