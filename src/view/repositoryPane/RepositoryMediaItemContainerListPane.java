package view.repositoryPane;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import model.common.Media;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import view.common.Observable;
import view.temporalViewPane.TemporalViewPane;

/**
 *
 * @author Douglas
 */

@SuppressWarnings("rawtypes")
public class RepositoryMediaItemContainerListPane extends ScrollPane implements view.common.Observer, view.common.Observable {
	
	final Logger logger = LoggerFactory.getLogger(RepositoryMediaItemContainerListPane.class);
	
	private final String APPLICATION = "Application";
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
	private final FlowPane application = new FlowPane();
	
	private TemporalViewPane temporalViewPane;
	
	private ArrayList<view.common.Observer> observers;
	
	public RepositoryMediaItemContainerListPane(){
		
		observers = new ArrayList<view.common.Observer>();
		
		setLayout();
		setFitToHeight(true);
	    setFitToWidth(true);
	    
		setContent(allTypes);
		
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
		
		application.setPadding(new Insets(5, 0, 5, 0));
		application.setVgap(8);
		application.setHgap(15);
		
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
            
        case APPLICATION:
        	setContent(application);
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
		        
		    case APPLICATION:
		    	application.getChildren().add(createRepositoryMediaItem(media));
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
		        
		    case APPLICATION:
		    	Iterator applicationIt = application.getChildren().iterator();
		    	Boolean mediaApplicationFound = false;
		    	RepositoryMediaItemContainer repositoryApplicationMedia = null;
		    	while(!mediaApplicationFound && applicationIt.hasNext()){
		    		repositoryApplicationMedia = (RepositoryMediaItemContainer) applicationIt.next();
		    		if(repositoryApplicationMedia.getMedia().equals(media)){
		    			mediaApplicationFound = true;
		    		}
		    	}
		    	application.getChildren().remove(repositoryApplicationMedia);
		        break;
		}
		
    	
		
	}

	private RepositoryMediaItemContainer createRepositoryMediaItem(Media media) {
		
		RepositoryMediaItemContainer repositoryMediaItemContainer = new RepositoryMediaItemContainer(media, this, temporalViewPane);
		
		repositoryMediaItemContainer.addObserver(this);
		
		return repositoryMediaItemContainer;
	
	}
	
	public ObservableList<Node> getAllTypes(){
		return allTypes.getChildren();
	}
	
	public ObservableList<Node> getImages(){
		return image.getChildren();
	}
	
	public ObservableList<Node> getVideos(){
		return video.getChildren();
	}
	
	public ObservableList<Node> getAudios(){
		return audio.getChildren();
	}
	
	public ObservableList<Node> getTexts(){
		return text.getChildren();
	}
	
	public ObservableList<Node> getApplications(){
		return application.getChildren();
	}

	public Media getSelectedMedia() {
		
		for(Node node : getAllTypes()){
			RepositoryMediaItemContainer repositoryMediaItemContainer = (RepositoryMediaItemContainer) node;
			if(repositoryMediaItemContainer.getSelected()){
				return repositoryMediaItemContainer.getMedia();
			}
		}
		
		for(Node node : getImages()){
			RepositoryMediaItemContainer repositoryMediaItemContainer = (RepositoryMediaItemContainer) node;
			if(repositoryMediaItemContainer.getSelected()){
				return repositoryMediaItemContainer.getMedia();
			}
		}
		
		for(Node node : getVideos()){
			RepositoryMediaItemContainer repositoryMediaItemContainer = (RepositoryMediaItemContainer) node;
			if(repositoryMediaItemContainer.getSelected()){
				return repositoryMediaItemContainer.getMedia();
			}
		}
		
		for(Node node : getAudios()){
			RepositoryMediaItemContainer repositoryMediaItemContainer = (RepositoryMediaItemContainer) node;
			if(repositoryMediaItemContainer.getSelected()){
				return repositoryMediaItemContainer.getMedia();
			}
		}
		
		for(Node node : getTexts()){
			RepositoryMediaItemContainer repositoryMediaItemContainer = (RepositoryMediaItemContainer) node;
			if(repositoryMediaItemContainer.getSelected()){
				return repositoryMediaItemContainer.getMedia();
			}
		}
		
		for(Node node : getApplications()){
			RepositoryMediaItemContainer repositoryMediaItemContainer = (RepositoryMediaItemContainer) node;
			if(repositoryMediaItemContainer.getSelected()){
				return repositoryMediaItemContainer.getMedia();
			}
		}
		
		return null;
		
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
			observer.update(this, getSelectedMedia(), operator);
		}
		
	}

	@Override
	public void update(Observable o, Object obj, Object operator) {
		
		notifyObservers(operator);

	}
	
	public void setTemporalViewPane(TemporalViewPane temporalViewPane){
		this.temporalViewPane = temporalViewPane;
	}

}
