package view.repositoryPane;

import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;

public class VideoFrame extends MediaListenerAdapter {
	
	private static final int IMAGE_THUMBNAIL_WIDTH = 90;
	private static final int FRAME_NUMBER = 10;
	private String videofilePath;
	private ImageView icon;
	private int cont;
		  
	public VideoFrame(String videofilePath, ImageView icon) {
		
		this.videofilePath = videofilePath;
		this.icon = icon;
		cont = 0;

		IMediaReader reader = ToolFactory.makeReader(this.videofilePath);
		reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
		reader.addListener(this);
		
		while (reader.readPacket() == null)
		do {} while(false);
		    			 
	}

	 public void onVideoPicture(IVideoPictureEvent event) {	
		 
	    try {

	    	  if (cont == FRAME_NUMBER) {
	    		 
	    		 Image image = SwingFXUtils.toFXImage(event.getImage(), null);
	    		 icon.setImage(image);
	    		 icon.setPreserveRatio(true);
	    		 icon.setSmooth(false);
	             icon.setFitWidth(IMAGE_THUMBNAIL_WIDTH);
	    		 
	    	 }
	    	  
	    	  cont++;

	    }
	    
	    catch (Exception e) {
	    	
	      e.printStackTrace();
	      
	    }
	    
	 }
	
}
