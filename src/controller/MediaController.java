package controller;

import gui.repositoryPanel.MessageDialog;

import java.io.File;
import java.util.Iterator;

import model.NCLSupport.extendedAna.Media;
import model.repository.MediaList;

public class MediaController {

	private static MediaController mediaController = null;
	
	private MediaList mediaList;
	private Media media;
	private String selectedMediaName;
	private Boolean contains;
	
	private MediaController(){
		mediaList = new MediaList();
	}
	
	public static MediaController getMediaController(){
		
		if (mediaController==null){
			mediaController = new MediaController();
		}
		return mediaController;
				
	}
	
	public void addMedia(File file){
		
		media = new Media(file);
        selectedMediaName = media.getName();
        contains = false;
        Iterator mediaListIterator = mediaList.getAllTypesList().iterator();
        while(mediaListIterator.hasNext() && contains==false) {
            String listMediaName = ((Media) mediaListIterator.next()).getName();
            if(selectedMediaName.equalsIgnoreCase(listMediaName)){
                new MessageDialog("Media's already added.", MessageDialog.ICON_INFO).showAndWait();
                contains = true;
            }
        }
        if(!contains) {
            mediaList.add(media);
        }
		
	}
	
	public void deleteMedia(){
		
		Iterator it = mediaList.getAllTypesList().iterator();
    	Boolean mediaSelected = false;
    	Media media = null;
    	while (!mediaSelected && it.hasNext()){
    		media = (Media) it.next();
    		mediaSelected = media.getSelected();
    	}
    	if(media.getSelected()){
    		mediaList.delete(media);
    	}
		
	}
	
	public void clearMediaList(){
		mediaList.clear();
	}
	
	public MediaList getMediaList(){
		return mediaList;
	}
	
	
	
}
