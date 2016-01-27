package model.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

import model.common.Media;
import model.repository.enums.RepositoryOperator;
import model.utility.Operation;

/**
 *
 * @author Douglas
 */
public class RepositoryMediaList extends Observable implements Serializable{

	private static final long serialVersionUID = 5842489767967822129L;
	
	private ArrayList<Media> images = new ArrayList<Media>();
    private ArrayList<Media> video = new ArrayList<Media>();
    private ArrayList<Media> audio = new ArrayList<Media>();
    private ArrayList<Media> text = new ArrayList<Media>();
    private ArrayList<Media> application = new ArrayList<Media>();
    private ArrayList<Media> allTypes = new ArrayList<Media>();
    
    public RepositoryMediaList(){
    	
    }
    
	public Boolean add(Media media) {
		
		String selectedMediaName = media.getName();
        Boolean contains = false;
		Iterator<Media> mediaListIterator = getAllTypesList().iterator();
        while(mediaListIterator.hasNext() && contains==false) {
            String listMediaName = ((Media) mediaListIterator.next()).getName();
            if(selectedMediaName.equalsIgnoreCase(listMediaName)){
                return false;
            }
        }
		
        allTypes.add(media);
        
        switch(media.getMediaType()){
            case IMAGE:
                images.add(media);
                break;
                
            case VIDEO:
                video.add(media);
                break;
                
            case AUDIO:
                audio.add(media);
                break;
                
            case TEXT:
                text.add(media);
                break;
                
            case APPLICATION:
                application.add(media);  
                break;
                
        }
        
        setChanged();
        Operation<RepositoryOperator> operation = new Operation<RepositoryOperator>(RepositoryOperator.ADD_REPOSITORY_MEDIA, media);
        notifyObservers(operation);
        
        return true;
        
    }
    
    public void delete(Media media){
    	
    	allTypes.remove(media);
    	
    	switch(media.getMediaType()){
        case IMAGE:
            images.remove(media);
            break;
            
        case VIDEO:
            video.remove(media);
            break;
            
        case AUDIO:
            audio.remove(media);
            break;
            
        case TEXT:
            text.remove(media);
            break;
            
        case APPLICATION:
            application.remove(media);  
            break;
            
    	}
    
    	setChanged();
        Operation<RepositoryOperator> operation = new Operation<RepositoryOperator>(RepositoryOperator.REMOVE_REPOSITORY_MEDIA, media);
        notifyObservers(operation);
    	
    }
    
    public void clear(){
        images.clear();
        video.clear();
        audio.clear();
        text.clear();
        application.clear();
        allTypes.clear();
        
        setChanged();
        Operation<RepositoryOperator> operation = new Operation<RepositoryOperator>(RepositoryOperator.CLEAR_SELECTION_REPOSITORY_MEDIA);
        notifyObservers(operation);
        
    }
    
    public Boolean isEmpty(){
    	return allTypes.isEmpty();
    }
    
    public ArrayList<Media> getAllTypesList(){
        return allTypes;
    }
    
    public ArrayList<Media> getImageList(){
        return images;
    }
    
    public ArrayList<Media> getVideoList(){
        return video;
    }
    
    public ArrayList<Media> getAudioList(){
        return audio;
    }
    
    public ArrayList<Media> getTextList(){
        return text;
    }
    
    public ArrayList<Media> getApplicationList(){
        return application;
    }

	public void openExistingRepositoryMediaList(RepositoryMediaList existingRepositoryMediaList) {
		
		clear();
		
		for(Media existentMedia : existingRepositoryMediaList.getAllTypesList()){
			add(existentMedia);
		}
		
	}
    
}
