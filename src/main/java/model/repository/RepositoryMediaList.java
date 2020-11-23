package model.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

import model.common.MediaNode;
import model.repository.enums.RepositoryOperator;
import model.utility.Operation;

/**
 *
 * @author Douglas
 */
public class RepositoryMediaList extends Observable implements Serializable{

	private static final long serialVersionUID = 5842489767967822129L;
	
	private ArrayList<MediaNode> images = new ArrayList<MediaNode>();
    private ArrayList<MediaNode> video = new ArrayList<MediaNode>();
    private ArrayList<MediaNode> audio = new ArrayList<MediaNode>();
    private ArrayList<MediaNode> text = new ArrayList<MediaNode>();
    private ArrayList<MediaNode> application = new ArrayList<MediaNode>();
    private ArrayList<MediaNode> allTypes = new ArrayList<MediaNode>();
    
    public RepositoryMediaList(){
    	
    }
    
	public Boolean add(MediaNode mediaNode) {
		
		String selectedMediaName = mediaNode.getName();
        Boolean contains = false;
		Iterator<MediaNode> mediaListIterator = getAllTypesList().iterator();
        while(mediaListIterator.hasNext() && contains==false) {
            String listMediaName = ((MediaNode) mediaListIterator.next()).getName();
            if(selectedMediaName.equalsIgnoreCase(listMediaName)){
                return false;
            }
        }
		
        allTypes.add(mediaNode);
        
        switch(mediaNode.getType()){
            case IMAGE:
                images.add(mediaNode);
                break;
                
            case VIDEO:
                video.add(mediaNode);
                break;
                
            case AUDIO:
                audio.add(mediaNode);
                break;
                
            case TEXT:
                text.add(mediaNode);
                break;
                
            case APPLICATION:
                application.add(mediaNode);
                break;
                
        }
        
        setChanged();
        Operation<RepositoryOperator> operation = new Operation<RepositoryOperator>(RepositoryOperator.ADD_REPOSITORY_MEDIA, mediaNode);
        notifyObservers(operation);
        
        return true;
        
    }
    
    public void delete(MediaNode mediaNode){
    	
    	allTypes.remove(mediaNode);
    	
    	switch(mediaNode.getType()){
        case IMAGE:
            images.remove(mediaNode);
            break;
            
        case VIDEO:
            video.remove(mediaNode);
            break;
            
        case AUDIO:
            audio.remove(mediaNode);
            break;
            
        case TEXT:
            text.remove(mediaNode);
            break;
            
        case APPLICATION:
            application.remove(mediaNode);
            break;
            
    	}
    
    	setChanged();
        Operation<RepositoryOperator> operation = new Operation<RepositoryOperator>(RepositoryOperator.REMOVE_REPOSITORY_MEDIA, mediaNode);
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
    
    public ArrayList<MediaNode> getAllTypesList(){
        return allTypes;
    }
    
    public ArrayList<MediaNode> getImageList(){
        return images;
    }
    
    public ArrayList<MediaNode> getVideoList(){
        return video;
    }
    
    public ArrayList<MediaNode> getAudioList(){
        return audio;
    }
    
    public ArrayList<MediaNode> getTextList(){
        return text;
    }
    
    public ArrayList<MediaNode> getApplicationList(){
        return application;
    }

	public void openExistingRepositoryMediaList(RepositoryMediaList existingRepositoryMediaList) {
		
		clear();
		
		for(MediaNode existentMediaNode : existingRepositoryMediaList.getAllTypesList()){
			add(existentMediaNode);
		}
		
	}
    
}
