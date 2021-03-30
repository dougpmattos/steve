package model.common;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import model.common.enums.MediaType;
import model.common.enums.MimeType;
import model.spatialView.media.MediaPresentationProperty;
import model.temporalView.TimeSegment;
import model.utility.MediaUtil;
import view.common.dialogs.MessageDialog;
import view.spatialViewPane.TemporalMediaInfoPane;

/**
 *
 * @author Douglas
 */

public class MediaNode extends Node<MediaType> implements Serializable{
	
	private static final long serialVersionUID = 2375510094294210628L;
	
	private final int IMAGE_THUMBNAIL_WIDTH = 120;
	private final int ICON_WIDTH = 40;
    
    private File mediaFile;
    private String path;
    private MediaPresentationProperty presentationProperty = new MediaPresentationProperty();
    private ArrayList<TimeSegment> timeSegmentList = new ArrayList<TimeSegment>();
    private MimeType mimeType;

	public MediaNode(){
       
	}
	
	public void setFile(File mediaFile){
		
		this.mediaFile = mediaFile;
		this.name = mediaFile.getAbsoluteFile().getName().substring(0, mediaFile.getAbsoluteFile().getName().indexOf("."));
	    this.path = mediaFile.getAbsolutePath();
	    this.type = getMediaType(mediaFile);
	    this.mimeType = getMimeType(mediaFile);

	    if((type == MediaType.AUDIO)||(type == MediaType.VIDEO)){
	    	   setImplicitDuration();
	    }
	    
	}

	public void prefetchExecutionObject(StackPane screen){

		Object nodeContent = null;

		switch(getType()) {

			case IMAGE:

				ImageView image = new ImageView(new Image(getFile().toURI().toString()));
				image.setFitWidth(screen.getWidth());
				image.setFitHeight(screen.getHeight());
				image.setSmooth(false);
				nodeContent = image;
				break;

			case VIDEO:

				final javafx.scene.media.Media video = new javafx.scene.media.Media(getFile().toURI().toString());
				final MediaPlayer videoPlayer = new MediaPlayer(video);
				MediaView videoMediaView = new MediaView(videoPlayer);
				videoMediaView.setFitWidth(screen.getWidth());
				videoMediaView.setFitHeight(screen.getHeight());
				videoMediaView.setSmooth(true);
				nodeContent = videoMediaView;
				break;

			case AUDIO:

				final javafx.scene.media.Media audio = new javafx.scene.media.Media(getFile().toURI().toString());
				final MediaPlayer audioPlayer = new MediaPlayer(audio);
				MediaView audioMediaView = new MediaView(audioPlayer);
				audioMediaView.setFitWidth(screen.getWidth());
				audioMediaView.setFitHeight(screen.getHeight());
				audioMediaView.setSmooth(true);
				nodeContent = audioMediaView;
				break;

			case TEXT:
				//TODO pegar o texto.
				break;

			case APPLICATION:
				nodeContent = generateMediaIcon();
				break;
		}

		setExecutionObject(nodeContent);

	}

	public boolean isContinousMedia(){

		if((type == MediaType.AUDIO)||(type == MediaType.VIDEO)){
			return true;
		}else{
			return false;
		}

	}
	
	public String getNCLName(){
		return name.replaceAll("\\s+", "");
	}
	
	public File getFile(){
		return mediaFile;
	}
	
	public String getPath() {
		return path;
	}
	public MimeType getMimeType() {
		return mimeType;
	}
	
	public ImageView generateMediaIcon() {
		   
		   switch(type) {
		   
		   		case IMAGE:
	           	   File imageFile = new File(path);
	               icon = new ImageView(new Image(imageFile.toURI().toString()));
	               icon.setPreserveRatio(true);
	               icon.setSmooth(true);
	               icon.setFitWidth(IMAGE_THUMBNAIL_WIDTH);
	               break;
	               
				case VIDEO:
					icon = new ImageView(new Image(getClass().getResourceAsStream("/images/repositoryPane/video.png")));
					icon.setPreserveRatio(true);
		            icon.setSmooth(true);
		            icon.setFitWidth(ICON_WIDTH);
					break;
	               
				case AUDIO:
					icon = new ImageView(new Image(getClass().getResourceAsStream("/images/repositoryPane/audio.png")));
					icon.setPreserveRatio(true);
					icon.setSmooth(true);
					icon.setFitWidth(ICON_WIDTH);
					break; 
	           
				case TEXT:
					icon = new ImageView(new Image(getClass().getResourceAsStream("/images/repositoryPane/text.png")));
					icon.setPreserveRatio(true);
					icon.setSmooth(true);
					icon.setFitWidth(ICON_WIDTH);
					break;
	                   
				case APPLICATION:
					icon = new ImageView(new Image(getClass().getResourceAsStream("/images/repositoryPane/application.png")));
					icon.setPreserveRatio(true);
					icon.setSmooth(true);
					icon.setFitWidth(ICON_WIDTH);
					break;                
	       }
	       
	       return icon;
	                  
	}
	
	 public MediaPresentationProperty getPresentationProperty() {
		   return presentationProperty;
	 }
	   
	 public void addTimeSegment(TimeSegment timeSegment){
		   timeSegmentList.add(timeSegment);
	 }
	 
	 public void removeTimeSegment(TimeSegment timeSegment){
		   timeSegmentList.remove(timeSegment);
	 }
	   
	 public ArrayList<TimeSegment> getTimeSegmentList() {
			return timeSegmentList;
	 }
	   
	 private MimeType getMimeType(File mediaFile) {
		   
	       String ext = "";
	       int pos;
	       
	       if(mediaFile.getAbsolutePath() != null){
	    	   
	    	   pos = mediaFile.getAbsolutePath().indexOf('.');
	           ext = mediaFile.getAbsolutePath().substring(pos);
	           ext = ext.toLowerCase();
	           ext = ext.substring(ext.indexOf('.') + 1, ext.length());
	       }
	       
	       try {
	    	   
	    	  String subType = getSubType(ext);
	    	   
	    	  if(this.type != null){
	    		  return MimeType.getEnumType(this.type.toString().toLowerCase() + "/" + subType);  
	    	  }else {
	    		  return null;
	    	  }
	           
	       } catch (Exception ex) {
	    	   
	           Logger.getLogger(MediaNode.class.getName()).log(Level.SEVERE, null, ex);
	           MessageDialog messageDialog = new MessageDialog(ex.getMessage(), "OK", 150);
	           messageDialog.showAndWait();
	           return null;
	           
	       }
	       
	 }
	   
	 private String getSubType(String ext){
		   
		   ArrayList<String> imageExt = new ArrayList<String>();
		   imageExt.add("jpg");
		   imageExt.add("jpeg");
		   
		   if(imageExt.contains(ext)){
			   return "jpeg";
		   } else{
			  return ext; 
		   }
		   
	 }

   private MediaType getMediaType(File mediaFile) {
	   
       String ext = "";
       int pos;
       
       if(mediaFile.getAbsolutePath() != null){
    	   
    	   pos = mediaFile.getAbsolutePath().indexOf('.');
           ext = mediaFile.getAbsolutePath().substring(pos);
           ext = ext.toLowerCase();
       }
       
       try {
    	   
           return MediaType.getEnumType(ext);
           
       } catch (Exception ex) {
    	   
           Logger.getLogger(MediaNode.class.getName()).log(Level.SEVERE, null, ex);
           return null;
           
       }
       
   }
   
   private void setImplicitDuration() throws RuntimeException {
		   
	   javafx.scene.media.Media javaFXMedia = new javafx.scene.media.Media(mediaFile.toURI().toString());
		
	   MediaPlayer mediaPlayer = new MediaPlayer(javaFXMedia);
	   mediaPlayer.setOnReady(new Runnable() {
		   
	        @Override
	        public void run() {
	        	Duration dur = javaFXMedia.getDuration();
				duration = MediaUtil.approximateDouble(dur.toSeconds());
	        }
	        
	    });
    
   }

   @Override
   public String toString(){
	   return name;
   }

   public void populateTemporalInfoPropertyJavaBean(TemporalMediaInfoPane infoPane) {
	   
		setBegin(Double.parseDouble(infoPane.getStartTimeValue()));
		setEnd(Double.parseDouble(infoPane.getEndTimeValue()));
		setDuration(Double.parseDouble(infoPane.getDurationValue()));
		setInteractive(infoPane.getInteractiveValue());
		
   }
   

}
