package gui.repositoryPane;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import model.common.Media;
import model.common.Operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.RepositoryController;

/**
 *
 * @author Douglas
 */

@SuppressWarnings("rawtypes")
public class MediaListPane extends ScrollPane implements Observer {
    
	private RepositoryController mediaController = RepositoryController.getMediaController();
	
	final Logger logger = LoggerFactory.getLogger(MediaListPane.class);
	
	private final String OTHERS = "Others";
	private final String TEXT = "Text";
	private final String AUDIO = "Audio";
	private final String VIDEO = "Video";
	private final String IMAGE = "Image";
	private final String MEDIA_FILES = "Media Files";
	
	private final FlowPane allTypes = new FlowPane();
	private final FlowPane image = new FlowPane();
	private final FlowPane video = new FlowPane();
	private final FlowPane audio = new FlowPane();
	private final FlowPane text = new FlowPane();
	private final FlowPane others = new FlowPane();
	
	public MediaListPane(){

		setLayout();
		setFitToHeight(true);
	    setFitToWidth(true);
	    
		setContent(allTypes);
		
	    mediaController.getMediaList().addObserver(this);
		
	}

	private void setLayout() {
		
		allTypes.setPadding(new Insets(5, 0, 5, 0));
		allTypes.setVgap(5);
		allTypes.setHgap(5);
		
		image.setPadding(new Insets(5, 0, 5, 0));
		image.setVgap(8);
		image.setHgap(15);
		
		video.setPadding(new Insets(5, 0, 5, 0));
		video.setVgap(8);
		video.setHgap(15);
		
		audio.setPadding(new Insets(5, 0, 5, 0));
		audio.setVgap(8);
		audio.setHgap(15);
		
		text.setPadding(new Insets(5, 0, 5, 0));
		text.setVgap(8);
		text.setHgap(15);
		
		others.setPadding(new Insets(5, 0, 5, 0));
		others.setVgap(8);
		others.setHgap(15);
		
	}

	public void setSelectPaneToFront(String paneName){
		
		switch(paneName){
		case MEDIA_FILES:
			setContent(allTypes);
			break;
			
		case IMAGE:
			setContent(image);
            break;
            
        case VIDEO:
        	setContent(video);
            break;
            
        case AUDIO:
        	setContent(audio);
            break;
            
        case TEXT:
        	setContent(text);
            break;
            
        case OTHERS:
        	setContent(others);
            break;
		}
		
	}
	
	@Override
	public void update(Observable observable, Object obj) {
	
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
	        	allTypes.getChildren().clear();
	        	image.getChildren().clear();
	        	video.getChildren().clear();
	        	audio.getChildren().clear();
	        	text.getChildren().clear();
	        	others.getChildren().clear();
	        	break;
	        default:
	        	break;
		}
		
	}
	
	public void add(Media media){
		
		allTypes.getChildren().add(createRepositoryMediaItem(media));
		
		switch(media.getType()){
		    case IMAGE:
		        image.getChildren().add(createRepositoryMediaItem(media));
		        break;
		        
		    case VIDEO:
		    	video.getChildren().add(createRepositoryMediaItem(media));
		        break;
		        
		    case AUDIO:
		    	audio.getChildren().add(createRepositoryMediaItem(media));
		        break;
		        
		    case TEXT:
		    	text.getChildren().add(createRepositoryMediaItem(media));
		        break;
		        
		    case OTHER:
		    case PROCEDURAL:
		    	others.getChildren().add(createRepositoryMediaItem(media));
		        break;
		}
		
		
	}
	
	public void remove(Media media){
		
		Iterator it = allTypes.getChildren().iterator();
    	Boolean mediaFound = false;
    	RepositoryMediaItemContainer repositoryMedia = null;
    	while(!mediaFound && it.hasNext()){
    		repositoryMedia = (RepositoryMediaItemContainer) it.next();
    		if(repositoryMedia.getMedia().equals(media)){
    			mediaFound = true;
    		}
    	}
    	allTypes.getChildren().remove(repositoryMedia);
		
		switch(media.getType()){
		    case IMAGE:
		    	Iterator imageIt = image.getChildren().iterator();
		    	Boolean mediaImageFound = false;
		    	RepositoryMediaItemContainer repositoryImageMedia = null;
		    	while(!mediaImageFound && imageIt.hasNext()){
		    		repositoryImageMedia = (RepositoryMediaItemContainer) imageIt.next();
		    		if(repositoryImageMedia.getMedia().equals(media)){
		    			mediaImageFound = true;
		    		}
		    	}
		    	image.getChildren().remove(repositoryImageMedia);
		        break;
		        
		    case VIDEO:
		    	Iterator videoIt = video.getChildren().iterator();
		    	Boolean mediaVideoFound = false;
		    	RepositoryMediaItemContainer repositoryVideoMedia = null;
		    	while(!mediaVideoFound && videoIt.hasNext()){
		    		repositoryVideoMedia = (RepositoryMediaItemContainer) videoIt.next();
		    		if(repositoryVideoMedia.getMedia().equals(media)){
		    			mediaVideoFound = true;
		    		}
		    	}
		    	video.getChildren().remove(repositoryVideoMedia);
		        break;
		        
		    case AUDIO:
		    	Iterator audioIt = audio.getChildren().iterator();
		    	Boolean mediaAudioFound = false;
		    	RepositoryMediaItemContainer repositoryAudioMedia = null;
		    	while(!mediaAudioFound && audioIt.hasNext()){
		    		repositoryAudioMedia = (RepositoryMediaItemContainer) audioIt.next();
		    		if(repositoryAudioMedia.getMedia().equals(media)){
		    			mediaAudioFound = true;
		    		}
		    	}
		    	audio.getChildren().remove(repositoryAudioMedia);
		        break;
		        
		    case TEXT:
		    	Iterator textIt = text.getChildren().iterator();
		    	Boolean mediaTextFound = false;
		    	RepositoryMediaItemContainer repositoryTextMedia = null;
		    	while(!mediaTextFound && textIt.hasNext()){
		    		repositoryTextMedia = (RepositoryMediaItemContainer) textIt.next();
		    		if(repositoryTextMedia.getMedia().equals(media)){
		    			mediaTextFound = true;
		    		}
		    	}
		    	text.getChildren().remove(repositoryTextMedia);
		        break;
		        
		    case OTHER:
		    case PROCEDURAL:
		    	Iterator othersIt = others.getChildren().iterator();
		    	Boolean mediaOthersFound = false;
		    	RepositoryMediaItemContainer repositoryOthersMedia = null;
		    	while(!mediaOthersFound && othersIt.hasNext()){
		    		repositoryOthersMedia = (RepositoryMediaItemContainer) othersIt.next();
		    		if(repositoryOthersMedia.getMedia().equals(media)){
		    			mediaOthersFound = true;
		    		}
		    	}
		    	others.getChildren().remove(repositoryOthersMedia);
		        break;
		}
		
    	
		
	}

	private RepositoryMediaItemContainer createRepositoryMediaItem(Media media) {
		
		RepositoryMediaItemContainer repositoryMediaItemContainer = new RepositoryMediaItemContainer(media, this);
		
		return repositoryMediaItemContainer;
	
	}
	
	public ObservableList<Node> getAllTypes(){
		return allTypes.getChildren();
	}
	
	public ObservableList<Node> getImagePane(){
		return image.getChildren();
	}
	
	public ObservableList<Node> getVideoPane(){
		return video.getChildren();
	}
	
	public ObservableList<Node> getAudioPane(){
		return audio.getChildren();
	}
	
	public ObservableList<Node> getTextPane(){
		return text.getChildren();
	}
	
	public ObservableList<Node> getOthersPane(){
		return others.getChildren();
	}

}
