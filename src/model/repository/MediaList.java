package model.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import model.NCLSupport.extendedAna.Media;

/**
 *
 * @author Douglas
 */
public class MediaList extends Observable {
    
    private static final int ADD = 1;
    private static final int REMOVE = 2;
    private static final int CLEAR = 3;
    
	public static final ArrayList<Media> images = new ArrayList<Media>();
    public static final ArrayList<Media> video = new ArrayList<Media>();
    public static final ArrayList<Media> audio = new ArrayList<Media>();
    public static final ArrayList<Media> text = new ArrayList<Media>();
    public static final ArrayList<Media> others = new ArrayList<Media>();
    public static final ArrayList<Media> allTypes = new ArrayList<Media>();

	public void add(Media media) {
        allTypes.add(media);
        switch(media.getImportedMediaType()){
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
        ListUpdateOperation listUpdateOperation = new ListUpdateOperation(media, ADD);
        notifyObservers(listUpdateOperation);
        
    }
    
    public void delete(Media media){
    	allTypes.remove(media);
    	switch(media.getImportedMediaType()){
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
         ListUpdateOperation listUpdateOperation = new ListUpdateOperation(media, REMOVE);
         notifyObservers(listUpdateOperation);
    	
    }
    
    public void clear(){
        images.clear();
        video.clear();
        audio.clear();
        text.clear();
        others.clear();
        allTypes.clear();
        
        setChanged();
        ListUpdateOperation listUpdateOperation = new ListUpdateOperation(CLEAR);
        notifyObservers(listUpdateOperation);
        
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
