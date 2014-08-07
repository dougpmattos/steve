
package model.nclDocument.extendedAna;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.repository.VideoFrame;
import model.utility.htg.HtgUtil;
import br.uff.midiacom.ana.NCLElement;
import br.uff.midiacom.ana.descriptor.NCLDescriptor;
import br.uff.midiacom.ana.descriptor.NCLLayoutDescriptor;
import br.uff.midiacom.ana.interfaces.NCLInterface;
import br.uff.midiacom.ana.interfaces.NCLProperty;
import br.uff.midiacom.ana.node.NCLMedia;
import br.uff.midiacom.ana.util.TimeType;
import br.uff.midiacom.ana.util.enums.NCLMediaType;
import br.uff.midiacom.ana.util.enums.NCLMimeType;
import br.uff.midiacom.ana.util.exception.NCLParsingException;
import br.uff.midiacom.ana.util.exception.XMLException;
import br.uff.midiacom.ana.util.reference.ExternalReferenceType;

import com.xuggle.xuggler.IContainer;

/**
 *
 * @author Douglas
 */

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Media extends NCLMedia<NCLElement, Area, NCLProperty, NCLLayoutDescriptor, 
                                    Node, NCLInterface, ExternalReferenceType> implements Node {
   
	private static final long serialVersionUID = -5591863977937952316L;
	private static final int DIVISOR = 1000000;
	private static int IMAGE_THUMBNAIL_WIDTH = 90;
    
    private String name, path;
    private NCLMediaType type;
    private ImageView icon;
    private File mediaFile;
    
   public Media(String id) throws XMLException {
        super(id);
   }

   public Media() throws XMLException {
        super();
   }
   
   public Media(File mediaFile) {
	   super();
	   this.mediaFile = mediaFile;
       name = mediaFile.getAbsoluteFile().getName();
       path = mediaFile.getAbsolutePath();
       type = identifyType();
       try {
           icon = generateMediaIcon();
       } catch (InterruptedException ex) {
           Logger.getLogger(Media.class.getName()).log(Level.SEVERE, null, ex);
       }
       
   }
   
   public File getMediaFile() {
	   return mediaFile;
   }
   
   public void setMediaFile(File mediaFile) {
	   this.mediaFile = mediaFile;
   }

public ImageView generateMediaIcon() throws InterruptedException {
       
       switch(type) { 
           
           case IMAGE:
           	   File imageFile = new File(path);
               icon = new ImageView(new Image(imageFile.toURI().toString()));
               icon.setPreserveRatio(true);
               icon.setFitWidth(IMAGE_THUMBNAIL_WIDTH);
               break;
               
           case VIDEO:
//        	   icon = new ImageView();
//        	   VideoFrame videoFrame = new VideoFrame(path, icon);
        	   icon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/video.png")));
        	   break;
               
           case AUDIO:
               icon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/audio.png")));
               break; 
           
           case TEXT:
               icon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/text.png")));
               break;
                   
           case OTHER:
           case PROCEDURAL:
               icon = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/others.png")));
               break;                
       }
       
       return icon;
                  
   }
   
   public NCLMediaType identifyType() {
       String ext = "";
       int pos;
       
       if(path != null){
    	   
    	   pos = path.indexOf('.');
           ext = path.substring(pos);
           ext = ext.toLowerCase();
       }
       
       try {
           return NCLMediaType.getEnumType(ext);
       
       } catch (NCLParsingException ex) {
           Logger.getLogger(Media.class.getName()).log(Level.SEVERE, null, ex);
           return NCLMediaType.OTHER;
       }
   }
   
   private Double getExplicitDur() throws XMLException{
        NCLDescriptor desc = (NCLDescriptor) this.getDescriptor();
        if(desc.getExplicitDur()!=null){
            Double explicitDur = convertToSeconds(desc.getExplicitDur());
            return explicitDur;
        }
        else{
            return null;
        }
       
    }
   
    //TODO Se importar documento, comparo com o caminho do doc NCL
    /*para verificar de o src da midia é relativo e monto o src absoluto da midia.
    Se for doc NCL novo, pego caminho absoluto da mídia no next.*/
    private Double getImplicitDur() throws XMLException, IOException {
       NCLMediaType mediaType = this.getMediaType();
       if((mediaType==NCLMediaType.AUDIO)||(mediaType==NCLMediaType.VIDEO)){
           IContainer container = IContainer.make();
           if (container.open(getMediaAbsolutePath(), IContainer.Type.READ, null) < 0){
               throw new RuntimeException("Failed to open.");
           }
           Double mediaDuration = Double.valueOf((double)container.getDuration()/DIVISOR);
           container.close();
           return HtgUtil.approximateDouble(mediaDuration);
       }else{
           return null;
       }
    }
    
    public String getMediaAbsolutePath(){
        Doc nclDoc = (Doc) getDoc();
        String mediaAbsolutPath;
        String mediaPath = this.getSrc().toString();
        String nclDocPath = nclDoc.getLocation();
        if(!mediaPath.contains(":")){
            mediaAbsolutPath = nclDocPath+"\\"+mediaPath;
            mediaAbsolutPath.replace("\\", "\\\\");
        }else{
            mediaAbsolutPath = mediaPath;
            mediaAbsolutPath.replace("/", "\\\\");
            mediaAbsolutPath = mediaAbsolutPath.split("file:///")[1];
        }
        return mediaAbsolutPath;
    }
    
    public Double getDuration() throws XMLException, IOException {
        if(getExplicitDur()!=null){
            return getExplicitDur();
        }else if(getImplicitDur()!=null){
            return getImplicitDur();
        }else{
            return null;
        }
    }

    private Double convertToSeconds(TimeType explicitDur) {
        if(explicitDur.getSecond() == null){
            return null;
        }
        Double seconds=0.0;
        if(explicitDur.getHour() != null){
            seconds = ((explicitDur.getHour().doubleValue())*3600);
        }else if(explicitDur.getMinute() != null){
            seconds+=((explicitDur.getMinute().doubleValue())*60);
        }
        seconds += explicitDur.getSecond();
        return seconds;
    }
    
    public boolean hasRefer(){
        if(getRefer()!=null){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
    
    public boolean isApplicationType() {
        if(getType()==NCLMimeType.APPLICATION_X_GINGA_NCL || getType()==NCLMimeType.APPLICATION_X_GINGA_NCLET ||
           getType()==NCLMimeType.APPLICATION_X_GINGA_NCLUA || getType()==NCLMimeType.APPLICATION_X_GINGA_SETTINGS ||
           getType()==NCLMimeType.APPLICATION_X_GINGA_TIME){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
    
    @Override
    protected Area createArea() throws XMLException {
        return new Area();
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getPath(){
        return path;
    }
    
    public void setPath(String path){
        this.path = path;
    }
    
    public NCLMediaType getImportedMediaType() {
        return type;
    }
    
    public void setImportedMediaType(NCLMediaType type) {
        this.type = type;
    }
    
    public ImageView getMediaIcon(){
        return icon;
    }
    
}
