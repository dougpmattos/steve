package gui.repositoryPanel;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import model.NCLSupport.extendedAna.Media;

public class RepositoryMediaItemContainer extends BorderPane{
	
	private static final int TEXT_LABEL_WITDH = 100;
	
	private Label label;
	private Media media;
	private MediaListPane mediaListPane;

	public RepositoryMediaItemContainer(Media media, MediaListPane mediaListPane){
		
		this.media = media;
		this.mediaListPane = mediaListPane;
		
		setCenter(media.getMediaIcon());
		setId("repo-media-item-container");
		
		label = new Label(media.getName());
		label.setId("label-media-item");
		label.setPrefWidth(TEXT_LABEL_WITDH);
		label.setAlignment(Pos.CENTER);
		setBottom(label);
		
		createListenerEventMediaItem();
		
	}

	private void createListenerEventMediaItem() {
		
		setOnMouseEntered(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	        	boolean itemClicked = getStylesheets().contains("gui/styles/mouseClickedMediaItem.css");
	        	if(!itemClicked){
	        		getStylesheets().add("gui/styles/mouseEnteredMediaItem.css");
	        	}
	        	
	        }
	    });
		
		setOnMouseExited(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	        	getStylesheets().remove("gui/styles/mouseEnteredMediaItem.css");
	        }
	    });
		
		setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	        	RepositoryMediaItemContainer source = (RepositoryMediaItemContainer) e.getSource();
	        	source.getMedia().setSelected(true);
	        	getStylesheets().add("gui/styles/mouseClickedMediaItem.css");
	        	
	        	for(Node media : mediaListPane.getChildren()){
	        		
	        		RepositoryMediaItemContainer repoMediaItemContainer = (RepositoryMediaItemContainer) media;
	        		
	        		if(!source.equals(repoMediaItemContainer)){
	        			repoMediaItemContainer.getMedia().setSelected(false);
	        			repoMediaItemContainer.getStylesheets().remove("gui/styles/mouseClickedMediaItem.css");
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

}
