package model.NCLSupport.utility;

import java.io.IOException;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import model.utility.MediaUtil;
import model.utility.OperatingSystemUtil;
import br.uff.midiacom.ana.NCLDoc;
import br.uff.midiacom.ana.descriptor.NCLDescriptor;
import br.uff.midiacom.ana.node.NCLMedia;
import br.uff.midiacom.ana.util.enums.NCLMediaType;
import br.uff.midiacom.ana.util.enums.NCLMimeType;
import br.uff.midiacom.ana.util.exception.XMLException;

@SuppressWarnings("rawtypes")
public class NCLMediaUtil {

	private static final int DIVISOR = 1000000;
	
	public static Double getDuration(NCLMedia nclMedia) throws XMLException, IOException {
		
        if(getExplicitDur(nclMedia)!=null){
            return getExplicitDur(nclMedia);
        }else if(getImplicitDur(nclMedia)!=null){
            return getImplicitDur(nclMedia);
        }else{
            return null;
        }
        
    }
	
	private static Double getImplicitDur(NCLMedia nclMedia) throws XMLException, IOException {

        NCLMediaType mediaType = nclMedia.getMediaType();

        if((mediaType==NCLMediaType.AUDIO)||(mediaType==NCLMediaType.VIDEO)){
            javafx.scene.media.Media javaFXMedia = new javafx.scene.media.Media(getMediaAbsolutePath(nclMedia));
            final Double[] duration = new Double[1];

            MediaPlayer mediaPlayer = new MediaPlayer(javaFXMedia);
            mediaPlayer.setOnReady(new Runnable() {

                @Override
                public void run() {
                    Duration dur = javaFXMedia.getDuration();
                    duration[0] = MediaUtil.approximateDouble(dur.toSeconds());

                }

            });
            return duration[0];
        }else {
            return null;
        }
	       
	}
	
	private static Double getExplicitDur(NCLMedia nclMedia) throws XMLException{
		
        NCLDescriptor desc = (NCLDescriptor) nclMedia.getDescriptor();
        if(desc.getExplicitDur()!=null){
            Double explicitDur = desc.getExplicitDur().getTimeInSeconds();
            return explicitDur;
        }
        else{
            return null;
        }
       
    }
	
	public static String getMediaAbsolutePath(NCLMedia nclMedia){
		
        NCLDoc nclDoc = (NCLDoc) nclMedia.getDoc();
        String mediaAbsolutPath = null;
        String mediaPath = nclMedia.getSrc().toString();
        String nclDocPath = nclDoc.getLocation();
        if(OperatingSystemUtil.isWindows()) {
        	if(!mediaPath.contains(":")){
                mediaAbsolutPath = nclDocPath+"\\"+mediaPath;
                mediaAbsolutPath.replace("\\", "\\\\");
            }else{
                mediaAbsolutPath = mediaPath;
                mediaAbsolutPath.replace("/", "\\\\");
                mediaAbsolutPath = mediaAbsolutPath.split("file:///")[1];
            }
        } else if(OperatingSystemUtil.isMac() || OperatingSystemUtil.isUnix()){
        	if(!mediaPath.contains(":")){
                mediaAbsolutPath = nclDocPath+"/"+mediaPath;
            }else{
                mediaAbsolutPath = mediaPath;
                mediaAbsolutPath.replace("/", "\\\\");
                mediaAbsolutPath = mediaAbsolutPath.split("file:///")[1];
            }
        }
        
        return mediaAbsolutPath;
        
    }
	
	public static boolean hasRefer(NCLMedia nclMedia){
        if(nclMedia.getRefer()!=null){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
	
	public static boolean isApplicationType(NCLMedia nclMedia) {
        if(nclMedia.getType()==NCLMimeType.APPLICATION_X_GINGA_NCL || nclMedia.getType()==NCLMimeType.APPLICATION_X_GINGA_NCLET ||
           nclMedia.getType()==NCLMimeType.APPLICATION_X_GINGA_NCLUA || nclMedia.getType()==NCLMimeType.APPLICATION_X_GINGA_SETTINGS ||
           nclMedia.getType()==NCLMimeType.APPLICATION_X_GINGA_TIME){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
	
}
