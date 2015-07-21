package view.repositoryPane;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import model.common.Media;
import view.temporalViewPane.TemporalChainPane;
import view.temporalViewPane.TemporalMediaNode;
import view.temporalViewPane.TemporalMediaNodeList;
import view.temporalViewPane.TemporalViewPane;

public class RepositoryMediaItemContainer extends BorderPane implements view.common.Observable {
	
	private static final DataFormat dataFormat = new DataFormat("model.common.media");
	
	private Label label;
	private Media media;
	private RepositoryMediaItemContainerListPane repositoryMediaItemContainerListPane;
	private TemporalViewPane temporalViewPane;
	private Boolean selected;
	private ArrayList<view.common.Observer> observers;

	public RepositoryMediaItemContainer(Media media, RepositoryMediaItemContainerListPane repositoryMediaItemContainerList, TemporalViewPane temporalViewPane){
		
		setId("repo-media-item-container");
		
		observers = new ArrayList<view.common.Observer>();
		
		this.media = media;
		this.repositoryMediaItemContainerListPane = repositoryMediaItemContainerList;
		this.temporalViewPane = temporalViewPane;
		label = new Label(media.getName());
		label.setId("label-media-item");
		selected = false;

		setCenter(media.generateMediaIcon());
		setBottom(label);
		
		createListenerEventMediaItem();
		createDragDropEffect();
		
	}
	
	private void createDragDropEffect() {
		
		setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
			
		        Dragboard dragBoard = startDragAndDrop(TransferMode.COPY);
		        ClipboardContent content = new ClipboardContent();
		        RepositoryMediaItemContainer repositoryMediaItemContainerSource = (RepositoryMediaItemContainer) mouseEvent.getSource();

		        content.put(dataFormat, repositoryMediaItemContainerSource.getMedia());
		        
		        dragBoard.setContent(content);
		        
		        mouseEvent.consume();
				
			}
			
       });
				
	}

	private void createListenerEventMediaItem() {
					
		setOnMousePressed(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	        	
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
	        	
	        	RepositoryMediaItemContainer source = (RepositoryMediaItemContainer) e.getSource();
	        	source.setSelected(true);
	        	
	        	if(getStylesheets().isEmpty()){
	        		getStylesheets().add("view/repositoryPane/styles/mousePressedRepositoryMedia.css");
	        	}
	        	
	        	for(Node repositoryMediaItemContainer : repositoryMediaItemContainerListPane.getAllTypes()){
	        		
	        		RepositoryMediaItemContainer repoMediaItemContainer = (RepositoryMediaItemContainer) repositoryMediaItemContainer;
	        		
	        		if(!source.getMedia().equals(repoMediaItemContainer.getMedia())){
	        			repoMediaItemContainer.setSelected(false);
	        			repoMediaItemContainer.getStylesheets().remove("view/repositoryPane/styles/mousePressedRepositoryMedia.css");
	        		}
	        		
	        	}
	        	
	        }
	    });
		
	}
	
	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
		notifyObservers();
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
	public void notifyObservers() {

		for(int i = 0; i < observers.size(); i++){
			view.common.Observer observer = (view.common.Observer) observers.get(i);
			observer.update(this, getMedia());
		}
		
	}

}
