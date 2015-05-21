package model.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

import model.common.Media;
import model.common.Operation;

/**
 *
 * @author Douglas
 */
public class RepositoryMediaList extends Observable {
 	
    private ArrayList<Media> images;
    private ArrayList<Media> video;
    private ArrayList<Media> audio;
    private ArrayList<Media> text;
    private ArrayList<Media> others;
    private ArrayList<Media> allTypes;
    
    public void initialize(){
    	
    	images = new ArrayList<Media>();
        video = new ArrayList<Media>();
        audio = new ArrayList<Media>();
        text = new ArrayList<Media>();
        others = new ArrayList<Media>();
        allTypes = new ArrayList<Media>();
    	
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
        
        switch(media.getType()){
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
                
            case OTHER:
            case PROCEDURAL:
                others.add(media);  
                break;
                
        }
        
        setChanged();
        Operation<RepositoryOperator> operation = new Operation<RepositoryOperator>(RepositoryOperator.ADD_REPOSITORY_MEDIA, media);
        notifyObservers(operation);
        
        return true;
        
    }
    
    public void delete(Media media){
    	
    	allTypes.remove(media);
    	
    	switch(media.getType()){
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
            
        case OTHER:
        case PROCEDURAL:
            others.remove(media);  
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
        others.clear();
        allTypes.clear();
        
        setChanged();
        Operation<RepositoryOperator> operation = new Operation<RepositoryOperator>(RepositoryOperator.CLEAR_REPOSITORY_MEDIA_LIST);
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
    
    public ArrayList<Media> getOthersList(){
        return others;
    }
    
}
