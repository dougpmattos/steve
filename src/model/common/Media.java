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
import model.spatialView.PresentationProperty;
import model.temporalView.TimeSegment;
import model.utility.MediaUtil;

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
    private MediaType type;
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
	    this.type = getMediaType();
	    
	    if((type == MediaType.AUDIO)||(type == MediaType.VIDEO)){
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
	
	public MediaType getType() {
		return type;
	} 
	
	public void setDuration(Double duration) {
		this.duration = duration;
	}
   
   public Double getDuration() {
	   return duration;
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
				//icon = new ImageView();
				//VideoFrame videoFrame = new VideoFrame(path, icon);
				icon = new ImageView(new Image(getClass().getResourceAsStream("/gui/repositoryPane/images/video.png")));
				break;
               
			case AUDIO:
				icon = new ImageView(new Image(getClass().getResourceAsStream("/gui/repositoryPane/images/audio.png")));
				icon.setPreserveRatio(true);
				icon.setSmooth(true);
				icon.setFitWidth(ICON_WIDTH);
				break; 
           
			case TEXT:
				icon = new ImageView(new Image(getClass().getResourceAsStream("/gui/repositoryPane/images/text.png")));
				icon.setPreserveRatio(true);
				icon.setSmooth(true);
				icon.setFitWidth(ICON_WIDTH);
				break;
                   
			case OTHER:
			case PROCEDURAL:
				icon = new ImageView(new Image(getClass().getResourceAsStream("/gui/repositoryPane/images/others.png")));
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
   
   private MediaType getMediaType() {
	   
       String ext = "";
       int pos;
       
       if(path != null){
    	   
    	   pos = path.indexOf('.');
           ext = path.substring(pos);
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

}
