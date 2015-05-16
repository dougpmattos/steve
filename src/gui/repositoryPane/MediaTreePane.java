package gui.repositoryPane;

import java.util.Observable;
import java.util.Observer;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.common.Media;
import model.common.Operation;
import controller.RepositoryController;

/**
 *
 * @author Douglas
 */
public class MediaTreePane extends TreeView<Object> implements Observer{
    
	private final String OTHERS = "Others";
	private final String TEXT = "Text";
	private final String AUDIO = "Audio";
	private final String VIDEO = "Video";
	private final String IMAGE = "Image";
	private final String MEDIA_FILES = "Media Files";	
	
	private final TreeItem<Object> allTypes = new TreeItem<Object>(MEDIA_FILES);
	private final TreeItem<Object> image = new TreeItem<Object>(IMAGE);
	private final TreeItem<Object> video = new TreeItem<Object>(VIDEO);
	private final TreeItem<Object> audio = new TreeItem<Object>(AUDIO);
	private final TreeItem<Object> text = new TreeItem<Object>(TEXT);
	private final TreeItem<Object> others = new TreeItem<Object>(OTHERS);
	
	private MediaListPane mediaListPane;
	
	private RepositoryController mediaController = RepositoryController.getMediaController();
	
    public MediaTreePane(MediaListPane mediaListPane){
    	
    	this.mediaListPane = mediaListPane;
    	
    	ImageView rootIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/repositoryPane/images/rootNode.png")));
    	ImageView imageIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/repositoryPane/images/imageNode.png")));
    	ImageView videoIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/repositoryPane/images/videoNode.png")));
    	ImageView audioIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/repositoryPane/images/audioNode.png")));
    	ImageView textIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/repositoryPane/images/textNode.png")));
    	ImageView othersIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/repositoryPane/images/othersNode.png")));
    	
    	allTypes.setGraphic(rootIcon);
    	allTypes.setExpanded(true);
    	image.setGraphic(imageIcon);
    	image.setExpanded(true);
    	video.setGraphic(videoIcon);
    	video.setExpanded(true);
    	audio.setGraphic(audioIcon);
    	audio.setExpanded(true);
    	text.setGraphic(textIcon);
    	text.setExpanded(true);
    	others.setGraphic(othersIcon);
    	others.setExpanded(true);
    	
	 	getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	 
	 	allTypes.getChildren().add(image);
        allTypes.getChildren().add(video);
        allTypes.getChildren().add(audio);
        allTypes.getChildren().add(text);
        allTypes.getChildren().add(others);
       
        setRoot(allTypes);
        createEventListners();
        mediaController.getMediaList().addObserver(this);
        
    }
    
	public void add(Media media) {
      
		ImageView importedMediaIcon = new ImageView(); 
    	TreeItem<Object> importedMediaTreeItem;
		
        switch(media.getType()){
            case IMAGE:
            	importedMediaIcon.setImage(new Image(getClass().getResourceAsStream("/gui/repositoryPane/images/imageTreeItem.png")));
            	importedMediaTreeItem = new TreeItem<Object>(media.getName(), importedMediaIcon);
                image.getChildren().add(importedMediaTreeItem);
                break;
                
            case VIDEO:
            	importedMediaIcon.setImage(new Image(getClass().getResourceAsStream("/gui/repositoryPane/images/videoTreeItem.png")));
            	importedMediaTreeItem = new TreeItem<Object>(media.getName(), importedMediaIcon);
                video.getChildren().add(importedMediaTreeItem);
                break;
                
            case AUDIO:
            	importedMediaIcon.setImage(new Image(getClass().getResourceAsStream("/gui/repositoryPane/images/audioTreeItem.png")));
            	importedMediaTreeItem = new TreeItem<Object>(media.getName(), importedMediaIcon);
                audio.getChildren().add(importedMediaTreeItem);
                break;
                
            case TEXT:
            	importedMediaIcon.setImage(new Image(getClass().getResourceAsStream("/gui/repositoryPane/images/textTreeItem.png")));
            	importedMediaTreeItem = new TreeItem<Object>(media.getName(), importedMediaIcon);
                text.getChildren().add(importedMediaTreeItem);
                break;
                
            case OTHER:
            case PROCEDURAL:
            	importedMediaIcon.setImage(new Image(getClass().getResourceAsStream("/gui/repositoryPane/images/othersTreeItem.png")));
            	importedMediaTreeItem = new TreeItem<Object>(media.getName(), importedMediaIcon);
                others.getChildren().add(importedMediaTreeItem);
                break;
                
        }
    }
	
	public void remove(Object media) {
		Media selectedMedia = (Media) media;
        switch(selectedMedia.getType()){
            case IMAGE:
            	ObservableList<TreeItem<Object>> imageList = image.getChildren();
            	for(TreeItem<Object> mediaTreeItem : imageList){
            		if(mediaTreeItem.getValue().toString().equalsIgnoreCase(selectedMedia.getName())){
            			image.getChildren().remove(mediaTreeItem);
            			break;
            		}
            	}
            	break;
                
            case VIDEO:
            	ObservableList<TreeItem<Object>> videoList = video.getChildren();
            	for(TreeItem<Object> mediaTreeItem : videoList){
            		if(mediaTreeItem.getValue().toString().equalsIgnoreCase(selectedMedia.getName())){
            			video.getChildren().remove(mediaTreeItem);
            			break;
            		}
            	}
            	break;
                
            case AUDIO:
            	ObservableList<TreeItem<Object>> audioList = audio.getChildren();
            	for(TreeItem<Object> mediaTreeItem : audioList){
            		if(mediaTreeItem.getValue().toString().equalsIgnoreCase(selectedMedia.getName())){
            			audio.getChildren().remove(mediaTreeItem);
            			break;
            		}
            	}
            	break;
                
            case TEXT:
            	ObservableList<TreeItem<Object>> textList = text.getChildren();
            	for(TreeItem<Object> mediaTreeItem : textList){
            		if(mediaTreeItem.getValue().toString().equalsIgnoreCase(selectedMedia.getName())){
            			text.getChildren().remove(mediaTreeItem);
            			break;
            		}
            	}
            	break;
                
            case OTHER:
            case PROCEDURAL:
            	ObservableList<TreeItem<Object>> othersList = others.getChildren();
            	for(TreeItem<Object> mediaTreeItem : othersList){
            		if(mediaTreeItem.getValue().toString().equalsIgnoreCase(selectedMedia.getName())){
            			others.getChildren().remove(mediaTreeItem);
            			break;
            		}
            	}
            	break;
                
        }
    }
	
	public void clear(){
        image.getChildren().clear();
        video.getChildren().clear();
        audio.getChildren().clear();
        text.getChildren().clear();
        others.getChildren().clear();
	}

	@Override
	public void update(Observable observable, Object obj) {
		
		if (obj instanceof Operation) {
			
			Operation operation = (Operation) obj;
			Media media = (Media) operation.getOperating();
			
			switch(operation.getOperator()){
		        case ADD:
		        	add(media);
		            break;
		            
		        case REMOVE:
		        	remove(media);
		            break;
				
		        case CLEAR:
		        	clear();
		        	break;
			}
	            
		}
		
	}
	
	public void createEventListners(){
		
		setOnKeyReleased(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent keyEvent) {
				
				if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN) {
					updateMediaListPane();
				}
				
			}
			
		});
		
		setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent mouseEvent) {
				updateMediaListPane();	 
			}
			
		});
		
	}
	
	private void updateMediaListPane() {
		TreeItem<Object> selectedTreeItem = getSelectionModel().getSelectedItem();
		String selectedItem = selectedTreeItem.getValue().toString();
		
		if(selectedItem == MEDIA_FILES){
			mediaListPane.setSelectPaneToFront(MEDIA_FILES);
		}else {
		
			String selectedItemParent = selectedTreeItem.getParent().getValue().toString();
			
			if(selectedItemParent == MEDIA_FILES){
				
				switch(selectedItem){
				case IMAGE:
					mediaListPane.setSelectPaneToFront(IMAGE);
	                break;
	                
	            case VIDEO:
	            	mediaListPane.setSelectPaneToFront(VIDEO);
	                break;
	                
	            case AUDIO:
	            	mediaListPane.setSelectPaneToFront(AUDIO);
	                break;
	                
	            case TEXT:
	            	mediaListPane.setSelectPaneToFront(TEXT);
	                break;
	                
	            case OTHERS:
	            	mediaListPane.setSelectPaneToFront(OTHERS);
	                break;
				}
				
			}else {
				
				switch(selectedItemParent){
				case IMAGE:
					mediaListPane.setSelectPaneToFront(IMAGE);
					break;
                
				case VIDEO:
	            	mediaListPane.setSelectPaneToFront(VIDEO);
	                break;
                
				case AUDIO:
	            	mediaListPane.setSelectPaneToFront(AUDIO);
	                break;
                
				case TEXT:
	            	mediaListPane.setSelectPaneToFront(TEXT);
	                break;
                
				case OTHERS:
	            	mediaListPane.setSelectPaneToFront(OTHERS);
	                break;
				}
			}
		}
	}
	
    
}
