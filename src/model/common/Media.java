package model.common;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import model.common.enums.MediaType;
import model.common.enums.MimeType;
import model.spatialView.PresentationProperty;
import model.temporalView.TimeSegment;
import model.utility.MediaUtil;
import view.common.MessageDialog;
import view.spatialViewPane.TemporalMediaInfoPane;

/**
 *
 * @author Douglas
 */

public class Media implements Serializable{
	
	private static final long serialVersionUID = 2375510094294210628L;
	
	private final int IMAGE_THUMBNAIL_WIDTH = 120;
	private final int ICON_WIDTH = 40;
    
    private File mediaFile;
    private String name;
    private String path;
    private MediaType mediaType;
    private MimeType mimeType;
    private Double duration = 5.0;
    private transient ImageView icon;
    private Double begin;
    private Double end;
    private Boolean interactive = false;
    private PresentationProperty presentationProperty = new PresentationProperty();
    private ArrayList<TimeSegment> timeSegmentList = new ArrayList<TimeSegment>();
    
	public Media(){
       
	}
	
	public void setFile(File mediaFile){
		
		this.mediaFile = mediaFile;
		this.name = mediaFile.getAbsoluteFile().getName();
	    this.path = mediaFile.getAbsolutePath();
	    this.mediaType = getMediaType(mediaFile);
	    this.mimeType = getMimeType(mediaFile);
	    
	    if((mediaType == MediaType.AUDIO)||(mediaType == MediaType.VIDEO)){
	    	   setImplicitDuration();
	    }
	    
	}
	
	public String getName() {
		   return name;
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
	public MediaType getMediaType() {
		return mediaType;
	} 
	
	public void setDuration(Double duration) {
		this.duration = duration;
	}
   
   public Double getDuration() {
	   return duration;
   }
   
   public ImageView generateMediaIcon() {
	   
	   switch(mediaType) {
	   
	   		case IMAGE:
           	   File imageFile = new File(path);
               icon = new ImageView(new Image(imageFile.toURI().toString()));
               icon.setPreserveRatio(true);
               icon.setSmooth(true);
               icon.setFitWidth(IMAGE_THUMBNAIL_WIDTH);
               break;
               
			case VIDEO:
				icon = new ImageView(new Image(getClass().getResourceAsStream("/view/repositoryPane/images/video.png")));
				icon.setPreserveRatio(true);
	            icon.setSmooth(true);
	            icon.setFitWidth(ICON_WIDTH);
				break;
               
			case AUDIO:
				icon = new ImageView(new Image(getClass().getResourceAsStream("/view/repositoryPane/images/audio.png")));
				icon.setPreserveRatio(true);
				icon.setSmooth(true);
				icon.setFitWidth(ICON_WIDTH);
				break; 
           
			case TEXT:
				icon = new ImageView(new Image(getClass().getResourceAsStream("/view/repositoryPane/images/text.png")));
				icon.setPreserveRatio(true);
				icon.setSmooth(true);
				icon.setFitWidth(ICON_WIDTH);
				break;
                   
			case OTHER:
			case PROCEDURAL:
				icon = new ImageView(new Image(getClass().getResourceAsStream("/view/repositoryPane/images/others.png")));
				icon.setPreserveRatio(true);
				icon.setSmooth(true);
				icon.setFitWidth(ICON_WIDTH);
				break;                
       }
       
       return icon;
                  
   }
   
   public void setBegin(Double begin) {
	   this.begin = begin;
   }
	   
   public Double getBegin() {
	   return begin;
   }

   public void setEnd(Double end) {
	   this.end = end;
   }
   
   public Double getEnd() {
	   return end;
   }
   
   public void setInteractive(Boolean interactive) {
	   this.interactive = interactive;
   }
   
   public Boolean getInteractive() {
	   return interactive;
   }
   
   public PresentationProperty getPresentationProperty() {
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
    	   
          return MimeType.getEnumType(getMediaType().toString().toLowerCase() + "/" + subType);
           
       } catch (Exception ex) {
    	   
           Logger.getLogger(Media.class.getName()).log(Level.SEVERE, null, ex);
           new MessageDialog(ex.getMessage(), MessageDialog.ICON_INFO).showAndWait();
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
    	   
           Logger.getLogger(Media.class.getName()).log(Level.SEVERE, null, ex);
           return MediaType.OTHER;
           
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
