package gui.repositoryPanel;

import java.util.Observable;
import java.util.Observer;

import controller.MediaController;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.NCLSupport.extendedAna.Media;
import model.repository.ListUpdateOperation;
import model.repository.MediaList;

/**
 *
 * @author Douglas
 */
public class MediaTreePane extends TreeView<Object> implements Observer{
    
	private MediaController mediaController = MediaController.getMediaController();
	
	private final int ADD = 1;
	private final int REMOVE = 2;
	private final int CLEAR = 3;
	
	private static final TreeItem<Object> allTypes = new TreeItem<Object>("Media Files");
	private static final TreeItem<Object> image = new TreeItem<Object>("Image");
	private static final TreeItem<Object> video = new TreeItem<Object>("Video");
	private static final TreeItem<Object> audio = new TreeItem<Object>("Audio");
	private static final TreeItem<Object> text = new TreeItem<Object>("Text");
	private static final TreeItem<Object> others = new TreeItem<Object>("Others");
	
	
    public MediaTreePane(){
        
    	Node rootIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/rootNode.png")));
    	Node imageIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/imageNode.png")));
    	Node videoIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/videoNode.png")));
    	Node audioIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/audioNode.png")));
    	Node textIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/textNode.png")));
    	Node othersIcon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/othersNode.png")));
    	
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
         
         mediaController.getMediaList().addObserver(this);
        
    }
    
	public void add(Media media) {
      
        switch(media.getImportedMediaType()){
            case IMAGE:
                image.getChildren().add(new TreeItem<Object>(media.getName()));
                break;
                
            case VIDEO:
                video.getChildren().add(new TreeItem<Object>(media.getName()));
                break;
                
            case AUDIO:
                audio.getChildren().add(new TreeItem<Object>(media.getName()));
                break;
                
            case TEXT:
                text.getChildren().add(new TreeItem<Object>(media.getName()));
                break;
                
            case OTHER:
            case PROCEDURAL:
                others.getChildren().add(new TreeItem<Object>(media.getName()));
                break;
                
        }
    }
	
	public void remove(Object media) {
		Media selectedMedia = (Media) media;
        switch(selectedMedia.getImportedMediaType()){
            case IMAGE:
                image.getChildren().remove(selectedMedia);
                break;
                
            case VIDEO:
                video.getChildren().remove(selectedMedia);
                break;
                
            case AUDIO:
                audio.getChildren().remove(selectedMedia);
                break;
                
            case TEXT:
                text.getChildren().remove(selectedMedia);
                break;
                
            case OTHER:
            case PROCEDURAL:
                others.getChildren().remove(selectedMedia);
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
    
}
