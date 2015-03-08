package gui.repositoryPanel;

import java.util.Observable;
import java.util.Observer;

import com.sun.javafx.scene.KeyboardShortcutsHandler;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import model.NCLSupport.extendedAna.Media;
import model.repository.ListUpdateOperation;
import controller.repository.MediaController;

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

	private final int ADD = 1;
	private final int REMOVE = 2;
	private final int CLEAR = 3;	
	
	private final TreeItem<Object> allTypes = new TreeItem<Object>(MEDIA_FILES);
	private final TreeItem<Object> image = new TreeItem<Object>(IMAGE);
	private final TreeItem<Object> video = new TreeItem<Object>(VIDEO);
	private final TreeItem<Object> audio = new TreeItem<Object>(AUDIO);
	private final TreeItem<Object> text = new TreeItem<Object>(TEXT);
	private final TreeItem<Object> others = new TreeItem<Object>(OTHERS);
	
	private MediaListPane mediaListPane;
	
	private MediaController mediaController = MediaController.getMediaController();
	
    public MediaTreePane(MediaListPane mediaListPane){
        
    	this.mediaListPane = mediaListPane;
    	
    	ImageView rootIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/rootNode.png")));
    	ImageView imageIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/imageNode.png")));
    	ImageView videoIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/videoNode.png")));
    	ImageView audioIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/audioNode.png")));
    	ImageView textIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/textNode.png")));
    	ImageView othersIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/othersNode.png")));
    	
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
		
        switch(media.getImportedMediaType()){
            case IMAGE:
            	importedMediaIcon.setImage(new Image(getClass().getResourceAsStream("/gui/images/imageTreeItem.png")));
            	importedMediaTreeItem = new TreeItem<Object>(media.getName(), importedMediaIcon);
                image.getChildren().add(importedMediaTreeItem);
                break;
                
            case VIDEO:
            	importedMediaIcon.setImage(new Image(getClass().getResourceAsStream("/gui/images/videoTreeItem.png")));
            	importedMediaTreeItem = new TreeItem<Object>(media.getName(), importedMediaIcon);
                video.getChildren().add(importedMediaTreeItem);
                break;
                
            case AUDIO:
            	importedMediaIcon.setImage(new Image(getClass().getResourceAsStream("/gui/images/audioTreeItem.png")));
            	importedMediaTreeItem = new TreeItem<Object>(media.getName(), importedMediaIcon);
                audio.getChildren().add(importedMediaTreeItem);
                break;
                
            case TEXT:
            	importedMediaIcon.setImage(new Image(getClass().getResourceAsStream("/gui/images/textTreeItem.png")));
            	importedMediaTreeItem = new TreeItem<Object>(media.getName(), importedMediaIcon);
                text.getChildren().add(importedMediaTreeItem);
                break;
                
            case OTHER:
            case PROCEDURAL:
            	importedMediaIcon.setImage(new Image(getClass().getResourceAsStream("/gui/images/othersTreeItem.png")));
            	importedMediaTreeItem = new TreeItem<Object>(media.getName(), importedMediaIcon);
                others.getChildren().add(importedMediaTreeItem);
                break;
                
        }
    }
	
	public void remove(Object media) {
		Media selectedMedia = (Media) media;
        switch(selectedMedia.getImportedMediaType()){
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
		
		if (obj instanceof ListUpdateOperation) {
			
			ListUpdateOperation listUpdateOperation = (ListUpdateOperation) obj;
			
			switch(listUpdateOperation.getOperationType()){
		        case ADD:
		        	add(listUpdateOperation.getMedia());
		            break;
		            
		        case REMOVE:
		        	remove(listUpdateOperation.getMedia());
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
