package view.repositoryPane;

import java.awt.*;
import java.util.ArrayList;

import controller.ApplicationController;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import model.common.MediaNode;
import model.repository.enums.RepositoryOperator;
import model.utility.MediaUtil;
import view.temporalViewPane.TemporalChainPane;
import view.temporalViewPane.TemporalViewPane;
import view.temporalViewPane.TimeLineXYChartData;

public class RepositoryMediaItemContainer extends BorderPane implements view.common.Observable {
	
	private static final DataFormat dataFormat = new DataFormat("model.common.media");
	
	private Label label;
	private MediaNode mediaNode;
	private RepositoryMediaItemContainerListPane repositoryMediaItemContainerListPane;
	private TemporalViewPane temporalViewPane;
	private Boolean selected;
	private ArrayList<view.common.Observer> observers;

	public RepositoryMediaItemContainer(MediaNode mediaNode, RepositoryMediaItemContainerListPane repositoryMediaItemContainerList, TemporalViewPane temporalViewPane){
		
		setId("repo-media-item-container");
		
		observers = new ArrayList<view.common.Observer>();
		
		this.mediaNode = mediaNode;
		this.repositoryMediaItemContainerListPane = repositoryMediaItemContainerList;
		this.temporalViewPane = temporalViewPane;
		label = new Label(mediaNode.getName());
		label.setId("label-media-item");
		selected = false;

		ImageView mediaIcon = mediaNode.generateMediaIcon();
		mediaIcon.setStyle("-fx-background-radius: 4;");
		setCenter(mediaIcon);
		setBottom(label);
		
		createListenerEventMediaItem();
		createDragDropEffect();
		
	}
	
	private void createDragDropEffect() {

		setOnMouseEntered(mouseEvent -> {
			ApplicationController.getInstance().getSteveScene().setCursor(Cursor.OPEN_HAND);
		});

		setOnMouseExited(mouseEvent -> {
			ApplicationController.getInstance().getSteveScene().setCursor(Cursor.DEFAULT);
		});

		setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
			
		        Dragboard dragBoard = startDragAndDrop(TransferMode.COPY);
		        ClipboardContent content = new ClipboardContent();
		        RepositoryMediaItemContainer repositoryMediaItemContainerSource = (RepositoryMediaItemContainer) mouseEvent.getSource();

				SnapshotParameters snapshotParameters = new SnapshotParameters();
				snapshotParameters.setFill(Color.TRANSPARENT);
				Scale scale = new Scale(0.5,0.5);
				snapshotParameters.setTransform(scale);

				dragBoard.setDragView(snapshot(snapshotParameters, null));
				dragBoard.setDragViewOffsetX(200);

		        content.put(dataFormat, repositoryMediaItemContainerSource.getMediaNode());
		        
		        dragBoard.setContent(content);
		        
		        mouseEvent.consume();
				
			}
			
       });
				
	}

	private void createListenerEventMediaItem() {

		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				ApplicationController.getInstance().getSteveScene().setCursor(Cursor.CLOSED_HAND);
			}

		});

		setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				ApplicationController.getInstance().getSteveScene().setCursor(Cursor.DEFAULT);
			}

		});

		setOnMousePressed(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {

				ApplicationController.getInstance().getSteveScene().setCursor(Cursor.CLOSED_HAND);

	        	temporalViewPane.clearSelectedNodeList();
	        	
	        	for(Tab temporalTab : temporalViewPane.getTemporalChainTabPane().getTabs()){
	        		
	        		TemporalChainPane temporalChainPane = (TemporalChainPane) temporalTab.getContent();
	        		temporalChainPane.getParentTab().setStyle(null);
	        		
					for(ArrayList<TimeLineXYChartData> timeLineXYChartDataList : temporalChainPane.getTimeLineXYChartDataLineList()){
						for(TimeLineXYChartData timeLineXYChartData : timeLineXYChartDataList){
							boolean styleRemoved = false;
							if(timeLineXYChartData.getContainerNode().getStylesheets().remove("styles/temporalViewPane/mousePressedSecondaryTemporalMediaNode.css")){
								styleRemoved = true;
							}
							if(timeLineXYChartData.getContainerNode().getStylesheets().remove("styles/temporalViewPane/mousePressedTemporalMediaNode.css")){
								styleRemoved = true;
							}
							if(styleRemoved){
								timeLineXYChartData.getMediaImageClip().setHeight(timeLineXYChartData.getMediaImageClip().getHeight()+5);
							}
						}
					}	
				 }
	        	
	        	RepositoryMediaItemContainer source = (RepositoryMediaItemContainer) e.getSource();
	        	source.setSelected(true);
	        	
	        	if(getStylesheets().isEmpty()){
	        		getStylesheets().add("styles/repositoryPane/mousePressedRepositoryMedia.css");
	        	}
	        	
	        	for(Node repositoryMediaItemContainer : repositoryMediaItemContainerListPane.getAllTypes()){
	        		
	        		RepositoryMediaItemContainer repoMediaItemContainer = (RepositoryMediaItemContainer) repositoryMediaItemContainer;
	        		
	        		if(!source.getMediaNode().equals(repoMediaItemContainer.getMediaNode())){
	        			repoMediaItemContainer.setSelected(false);
	        			repoMediaItemContainer.getStylesheets().remove("styles/repositoryPane/mousePressedRepositoryMedia.css");
	        		}
	        		
	        	}
	        	
	        }
	    });
		
	}
	
	public MediaNode getMediaNode() {
		return mediaNode;
	}

	public void setMediaNode(MediaNode mediaNode) {
		this.mediaNode = mediaNode;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
		notifyObservers(RepositoryOperator.SELECT_REPOSITORY_MEDIA);
	}

	public Boolean getSelected(){
		return this.selected;
	}
	
	@Override
	public void addObserver(view.common.Observer o) {
		observers.add(o);
	}

	@Override
	public void deleteObserver(view.common.Observer o) {
		
		int i = observers.indexOf(o);
		if(i >= 0){
			observers.remove(o);
		}
		
	}
	
	@Override
	public void notifyObservers(Object operator) {

		for(int i = 0; i < observers.size(); i++){
			view.common.Observer observer = (view.common.Observer) observers.get(i);
			observer.update(this, getMediaNode(), operator);
		}
		
	}

}
