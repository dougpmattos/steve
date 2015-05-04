package controller;

import gui.common.MessageDialog;

import java.io.File;
import java.util.Iterator;

import model.common.Media;
import model.repository.MediaList;

@SuppressWarnings("rawtypes")
public class RepositoryController {

	private static RepositoryController mediaListController = null;
	
	private MediaList mediaList;
	private Media media;
	private String selectedMediaName;
	private Boolean contains;
	
	private RepositoryController(){
		mediaList = new MediaList();
	}
	
	public static RepositoryController getMediaController(){
		
		if (mediaListController==null){
			mediaListController = new RepositoryController();
		}
		return mediaListController;
				
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
    	if(media != null && media.getSelected()){
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
