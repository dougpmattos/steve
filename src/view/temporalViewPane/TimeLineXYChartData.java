package view.temporalViewPane;

import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import model.common.Media;
import model.temporalView.TemporalChain;
import view.repositoryPane.RepositoryMediaItemContainer;
import view.repositoryPane.RepositoryPane;
import view.stevePane.StevePane;
import controller.Controller;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TimeLineXYChartData {

	private Controller controller;
	private Media media;
	private int line;
	private TemporalViewPane temporalViewPane;
	private RepositoryPane repositoryPane;
	private TemporalChain temporalChainModel;
	private XYChart.Data<Number, String> xyChartData;
	private HBox containerNode;
	private TemporalChainPane temporalChainPane;
	private StevePane stevePane;
	private Rectangle mediaImageClip;
	private ImageView imageView;
	
	public TimeLineXYChartData(Controller controller, Media media, TemporalChain temporalChainModel, TemporalViewPane temporalViewPane, TemporalChainPane temporalChainPane, RepositoryPane repositoryPane, int line, StevePane stevePane){
		
		this.controller = controller;
		this.media = media;
		this.temporalChainModel = temporalChainModel;
		this.line = line;
		this.temporalViewPane = temporalViewPane;
		this.temporalChainPane = temporalChainPane;
		this.repositoryPane = repositoryPane;
		this.stevePane = stevePane;

		createXYChartData();	
		
	}

	private void createXYChartData() {
		
		xyChartData = new XYChart.Data<Number, String>();
		xyChartData.setExtraValue(media.getBegin());
		xyChartData.setXValue(media.getEnd());
		xyChartData.setYValue(String.valueOf(line));
		xyChartData.setNode(createNode());
		
	}
	
	private HBox createNode() {
		
		containerNode = new HBox();
		containerNode.setId("temporal-media-container");
	
		Label mediaName = new Label(media.getName());
		mediaName.setId("media-name");

		mediaImageClip = new Rectangle();
		mediaImageClip.setId("media-image");
		mediaImageClip.setArcHeight(16);
		mediaImageClip.setArcWidth(16);
		imageView = media.generateMediaIcon();
		imageView.setClip(mediaImageClip);

		containerNode.heightProperty().addListener(new ChangeListener(){
			@Override 
	        public void changed(ObservableValue o,Object oldVal, Object newVal){
				
					imageView.setFitHeight((double) newVal);

					if(containerNode.getStylesheets().isEmpty()){
						mediaImageClip.setHeight((double) newVal);
					}else {
						
						if(containerNode.getStylesheets().get(0).equalsIgnoreCase("view/temporalViewPane/styles/mousePressedSlaveTemporalMediaNode.css")){
							mediaImageClip.setHeight((double) newVal-5);
						}else {
							mediaImageClip.setHeight((double) newVal);
						}
						
					}
					
					
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
				
				if(stevePane.isMetaDown()){
					
					if(!temporalViewPane.getSelectedMediaList().contains(media)){
						temporalViewPane.addSelectedMedia(media);
					}
					
					if(media == temporalViewPane.getFirstSelectedMedia()){

						if(node.getStylesheets().isEmpty()){
							node.getStylesheets().add("view/temporalViewPane/styles/mousePressedTemporalMediaNode.css");
						}
						
					}else {
						
						if(node.getStylesheets().isEmpty()){
							mediaImageClip.setHeight(containerNode.getHeight()-5);
							node.getStylesheets().add("view/temporalViewPane/styles/mousePressedSlaveTemporalMediaNode.css");
						}

					}
	
				}else {
			
					temporalViewPane.clearSelectedMedia();
					temporalViewPane.addSelectedMedia(media);
					
					if(node.getStylesheets().remove("view/temporalViewPane/styles/mousePressedSlaveTemporalMediaNode.css")){
						mediaImageClip.setHeight(mediaImageClip.getHeight()+5);
					}else {
						node.getStylesheets().remove("view/temporalViewPane/styles/mousePressedTemporalMediaNode.css");
					}

					node.getStylesheets().add("view/temporalViewPane/styles/mousePressedTemporalMediaNode.css");
					
				}
				
				for(Tab temporalTab : temporalViewPane.getTemporalChainTabPane().getTabs()){
					
					TemporalChainPane temporalChainPane = (TemporalChainPane) temporalTab.getContent();

					for(ArrayList<TimeLineXYChartData> timeLineXYChartDataList : temporalChainPane.getTimeLineXYChartDataLineList()){
						for(TimeLineXYChartData timeLineXYChartData : timeLineXYChartDataList){
							if(!temporalViewPane.getSelectedMediaList().contains(timeLineXYChartData.getMedia())){
								if(timeLineXYChartData.getContainerNode().getStylesheets().remove("view/temporalViewPane/styles/mousePressedSlaveTemporalMediaNode.css")){
									timeLineXYChartData.getMediaImageClip().setHeight(timeLineXYChartData.getMediaImageClip().getHeight()+5);
								}else {
									timeLineXYChartData.getContainerNode().getStylesheets().remove("view/temporalViewPane/styles/mousePressedTemporalMediaNode.css");
								}
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
	        	node.toFront();
	        }
	        
	    });
	    node.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
		        
			}
	    	
	    });
		
	}

	public XYChart.Data<Number, String> getXYChartData() {
		return xyChartData;
	}
	
	public Media getMedia(){
		return media;
	}
	
	public HBox getContainerNode(){
		return containerNode;
	}
	
	public Rectangle getMediaImageClip(){
		return mediaImageClip;
	}
	
}
