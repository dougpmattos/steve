
package model.NCLSupport.NCLDocument;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.NCLSupport.extendedAna.Body;
import model.NCLSupport.extendedAna.Context;
import model.NCLSupport.extendedAna.Doc;
import model.NCLSupport.extendedAna.Media;
import model.NCLSupport.extendedAna.Node;
import br.uff.midiacom.ana.util.exception.XMLException;

/**
 *
 * @author Douglas
 */
public class MediaList extends ArrayList<Media>  {

	private static final long serialVersionUID = 8706897857626660368L;
	Body body;
    Node node;
    List<String> mediaIdList;

    public MediaList(Doc nclDoc, ArrayList<Context> contextList) {
        body = nclDoc.getBody();
        Iterator<Node> iterator = body.getNodes().iterator();
        while(iterator.hasNext()){
        	node = (Node) iterator.next();
            if(node instanceof Media){
                add((Media)node);
            }
        }
        for (Context context : contextList){
        	Iterator<Node> contextIterator = context.getNodes().iterator();
            while(contextIterator.hasNext()){
                node = (Node) contextIterator.next();
                if(node instanceof Media){
                    add((Media)node);
                }
            }
        }
        
        Iterator<Media> mediaListIterator = iterator();  
        mediaIdList = new ArrayList<String>();
        while(mediaListIterator.hasNext()){
            node = (Node) mediaListIterator.next();
            if(node instanceof Media){
                mediaIdList.add(((Media) node).getId());
            }   
        }
    }
    
    public List<String> getAllMediaId() throws XMLException{
        return mediaIdList;
    }
    
    public String getMediaId(int index) throws XMLException{
        return mediaIdList.get(index);
    }
    
    public Media getMedia(int index) throws XMLException{
        return get(index);
    }
    
    public Media getMedia(String anchorId) throws XMLException{
        Iterator<Media> mediaListIterator = iterator();
        while(mediaListIterator.hasNext()){
            node = (Node) mediaListIterator.next();
            if(node instanceof Media){
                Media listMedia = (Media) node;
                if(listMedia.getId().equalsIgnoreCase(anchorId)){
                	return listMedia;
                }
            }   
        }
    	return null;
    }
    
}
