package controller.repository;

import gui.common.MessageDialog;

import java.io.File;
import java.util.Iterator;

import model.common.Media;
import model.repository.MediaList;

@SuppressWarnings("rawtypes")
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
    	if(media != null && media.getSelected()){
    		mediaList.delete(media);
    	}
    	
//    	Iterator imageIt = mediaList.getImageList().iterator();
//    	Boolean imageMediaSelected = false;
//    	Media imageMedia = null;
//    	while (!imageMediaSelected && imageIt.hasNext()){
//    		imageMedia = (Media) imageIt.next();
//    		imageMediaSelected = imageMedia.getSelected();
//    	}
//    	if(imageMedia != null && imageMedia.getSelected()){
//    		mediaList.delete(imageMedia);
//    	}
//    	
//    	Iterator videoIt = mediaList.getVideoList().iterator();
//    	Boolean videoMediaSelected = false;
//    	Media videoMedia = null;
//    	while (!videoMediaSelected && videoIt.hasNext()){
//    		videoMedia = (Media) videoIt.next();
//    		videoMediaSelected = videoMedia.getSelected();
//    	}
//    	if(videoMedia != null && videoMedia.getSelected()){
//    		mediaList.delete(videoMedia);
//    	}
//    	
//    	Iterator audioIt = mediaList.getAudioList().iterator();
//    	Boolean audioMediaSelected = false;
//    	Media audioMedia = null;
//    	while (!audioMediaSelected && audioIt.hasNext()){
//    		audioMedia = (Media) audioIt.next();
//    		audioMediaSelected = audioMedia.getSelected();
//    	}
//    	if(audioMedia != null && audioMedia.getSelected()){
//    		mediaList.delete(audioMedia);
//    	}
//    	
//    	Iterator textIt = mediaList.getTextList().iterator();
//    	Boolean textMediaSelected = false;
//    	Media textMedia = null;
//    	while (!textMediaSelected && textIt.hasNext()){
//    		textMedia = (Media) textIt.next();
//    		textMediaSelected = textMedia.getSelected();
//    	}
//    	if(textMedia != null && textMedia.getSelected()){
//    		mediaList.delete(textMedia);
//    	}
//    	
//    	Iterator othersIt = mediaList.getOthersList().iterator();
//    	Boolean othersMediaSelected = false;
//    	Media othersMedia = null;
//    	while (!othersMediaSelected && othersIt.hasNext()){
//    		othersMedia = (Media) othersIt.next();
//    		othersMediaSelected = othersMedia.getSelected();
//    	}
//    	if(othersMedia != null && othersMedia.getSelected()){
//    		mediaList.delete(othersMedia);
//    	}
		
	}
	
	public void clearMediaList(){
		mediaList.clear();
	}
	
	public MediaList getMediaList(){
		return mediaList;
	}
	
	
	
}
