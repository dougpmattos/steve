package view.repositoryPane;

import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.common.MediaNode;

/**
 *
 * @author Douglas
 */
public class MediaTreePane extends TreeView<Object>{
    
	private final String APPLICATION = "Application";
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
	private final TreeItem<Object> application = new TreeItem<Object>(APPLICATION);
	
    public MediaTreePane(){
    	
    	ImageView rootIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/repositoryPane/rootNode.png")));
    	ImageView imageIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/repositoryPane/imageNode.png")));
    	ImageView videoIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/repositoryPane/videoNode.png")));
    	ImageView audioIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/repositoryPane/audioNode.png")));
    	ImageView textIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/repositoryPane/textNode.png")));
    	ImageView applicationIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/repositoryPane/applicationNode.png")));
    	
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
    	application.setGraphic(applicationIcon);
    	application.setExpanded(true);
    	
	 	getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	 
	 	allTypes.getChildren().add(image);
        allTypes.getChildren().add(video);
        allTypes.getChildren().add(audio);
        allTypes.getChildren().add(text);
        allTypes.getChildren().add(application);
       
        setRoot(allTypes);
        
    }
    
	public void add(MediaNode mediaNode) {
      
		ImageView importedMediaIcon = new ImageView(); 
    	TreeItem<Object> importedMediaTreeItem;
		
        switch(mediaNode.getType()){
            case IMAGE:
            	importedMediaIcon.setImage(new Image(getClass().getResourceAsStream("/images/repositoryPane/imageTreeItem.png")));
            	importedMediaTreeItem = new TreeItem<Object>(mediaNode, importedMediaIcon);
                image.getChildren().add(importedMediaTreeItem);
                break;
                
            case VIDEO:
            	importedMediaIcon.setImage(new Image(getClass().getResourceAsStream("/images/repositoryPane/videoTreeItem.png")));
            	importedMediaTreeItem = new TreeItem<Object>(mediaNode, importedMediaIcon);
                video.getChildren().add(importedMediaTreeItem);
                break;
                
            case AUDIO:
            	importedMediaIcon.setImage(new Image(getClass().getResourceAsStream("/images/repositoryPane/audioTreeItem.png")));
            	importedMediaTreeItem = new TreeItem<Object>(mediaNode, importedMediaIcon);
                audio.getChildren().add(importedMediaTreeItem);
                break;
                
            case TEXT:
            	importedMediaIcon.setImage(new Image(getClass().getResourceAsStream("/images/repositoryPane/textTreeItem.png")));
            	importedMediaTreeItem = new TreeItem<Object>(mediaNode, importedMediaIcon);
                text.getChildren().add(importedMediaTreeItem);
                break;
                
            case APPLICATION:
            	importedMediaIcon.setImage(new Image(getClass().getResourceAsStream("/images/repositoryPane/applicationTreeItem.png")));
            	importedMediaTreeItem = new TreeItem<Object>(mediaNode, importedMediaIcon);
                application.getChildren().add(importedMediaTreeItem);
                break;
                
        }
    }
	
	public void remove(Object media) {
		MediaNode selectedMediaNode = (MediaNode) media;
        switch(selectedMediaNode.type){
            case IMAGE:
            	ObservableList<TreeItem<Object>> imageList = image.getChildren();
            	for(TreeItem<Object> mediaTreeItem : imageList){
            		if(mediaTreeItem.getValue().toString().equalsIgnoreCase(selectedMediaNode.getName())){
            			image.getChildren().remove(mediaTreeItem);
            			break;
            		}
            	}
            	break;
                
            case VIDEO:
            	ObservableList<TreeItem<Object>> videoList = video.getChildren();
            	for(TreeItem<Object> mediaTreeItem : videoList){
            		if(mediaTreeItem.getValue().toString().equalsIgnoreCase(selectedMediaNode.getName())){
            			video.getChildren().remove(mediaTreeItem);
            			break;
            		}
            	}
            	break;
                
            case AUDIO:
            	ObservableList<TreeItem<Object>> audioList = audio.getChildren();
            	for(TreeItem<Object> mediaTreeItem : audioList){
            		if(mediaTreeItem.getValue().toString().equalsIgnoreCase(selectedMediaNode.getName())){
            			audio.getChildren().remove(mediaTreeItem);
            			break;
            		}
            	}
            	break;
                
            case TEXT:
            	ObservableList<TreeItem<Object>> textList = text.getChildren();
            	for(TreeItem<Object> mediaTreeItem : textList){
            		if(mediaTreeItem.getValue().toString().equalsIgnoreCase(selectedMediaNode.getName())){
            			text.getChildren().remove(mediaTreeItem);
            			break;
            		}
            	}
            	break;
                
            case APPLICATION:
            	ObservableList<TreeItem<Object>> othersList = application.getChildren();
            	for(TreeItem<Object> mediaTreeItem : othersList){
            		if(mediaTreeItem.getValue().toString().equalsIgnoreCase(selectedMediaNode.getName())){
            			application.getChildren().remove(mediaTreeItem);
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
        application.getChildren().clear();
	}

	public MediaNode getSelectedMedia() {
		
		if(getSelectionModel().getSelectedItem() != null){
			
			if(getSelectionModel().getSelectedItem().getValue() instanceof MediaNode){
				return (MediaNode) getSelectionModel().getSelectedItem().getValue();
			}else {
				return null;
			}
			
		}else {
			return null;	
		}
		
	}
	
    
}
