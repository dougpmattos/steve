package view.repositoryPane;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import model.common.Media;

public class RepositoryMediaItemContainer extends BorderPane{
	
	private static final DataFormat dataFormat = new DataFormat("model.common.media");
	
	private Label label;
	private Media media;
	private MediaListPane mediaListPane;
	private Boolean selected;

	public RepositoryMediaItemContainer(Media media, MediaListPane mediaListPane){
		
		setId("repo-media-item-container");
		
		this.media = media;
		this.mediaListPane = mediaListPane;
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
			
		setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	        	RepositoryMediaItemContainer source = (RepositoryMediaItemContainer) e.getSource();
	        	source.setSelected(true);
	        	
	        	getStylesheets().add("view/repositoryPane/styles/mouseClickedRepositoryMedia.css");
	        	
	        	for(Node media : mediaListPane.getAllTypes()){
	        		
	        		RepositoryMediaItemContainer repoMediaItemContainer = (RepositoryMediaItemContainer) media;
	        		
	        		if(!source.getMedia().equals(repoMediaItemContainer.getMedia())){
	        			repoMediaItemContainer.setSelected(false);
	        			repoMediaItemContainer.getStylesheets().remove("view/repositoryPane/styles/mouseClickedRepositoryMedia.css");
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
	}

	public Boolean getSelected(){
		return this.selected;
	}

}
