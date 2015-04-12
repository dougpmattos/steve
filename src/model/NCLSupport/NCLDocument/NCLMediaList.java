
package model.NCLSupport.NCLDocument;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.uff.midiacom.ana.NCLBody;
import br.uff.midiacom.ana.NCLDoc;
import br.uff.midiacom.ana.node.NCLContext;
import br.uff.midiacom.ana.node.NCLMedia;
import br.uff.midiacom.ana.node.NCLNode;
import br.uff.midiacom.ana.util.exception.XMLException;

/**
 *
 * @author Douglas
 */

@SuppressWarnings({"unchecked", "rawtypes"})
public class NCLMediaList extends ArrayList<NCLMedia>  {

	private static final long serialVersionUID = 8706897857626660368L;
	
	private NCLBody body;
	private NCLNode node;
    private List<String> mediaIdList;

    public NCLMediaList(NCLDoc nclDoc, ArrayList<NCLContext> contextList) {
        body = nclDoc.getBody();
        Iterator<NCLNode> iterator = body.getNodes().iterator();
        while(iterator.hasNext()){
        	node = (NCLNode) iterator.next();
            if(node instanceof NCLMedia){
                add((NCLMedia)node);
            }
        }
        for (NCLContext context : contextList){
			Iterator<NCLNode> contextIterator = context.getNodes().iterator();
            while(contextIterator.hasNext()){
                node = (NCLNode) contextIterator.next();
                if(node instanceof NCLMedia){
                    add((NCLMedia)node);
                }
            }
        }
        
        Iterator<NCLMedia> mediaListIterator = iterator();  
        mediaIdList = new ArrayList<String>();
        while(mediaListIterator.hasNext()){
            node = (NCLNode) mediaListIterator.next();
            if(node instanceof NCLMedia){
                mediaIdList.add(((NCLMedia) node).getId());
            }   
        }
    }
    
    public List<String> getAllMediaId() throws XMLException{
        return mediaIdList;
    }
    
    public String getMediaId(int index) throws XMLException{
        return mediaIdList.get(index);
    }
    
    public NCLMedia getNCLMedia(int index) throws XMLException{
        return get(index);
    }
    
    public NCLMedia getNCLMedia(String anchorId) throws XMLException{
        Iterator<NCLMedia> mediaListIterator = iterator();
        while(mediaListIterator.hasNext()){
            node = (NCLNode) mediaListIterator.next();
            if(node instanceof NCLMedia){
            	NCLMedia listMedia = (NCLMedia) node;
                if(listMedia.getId().equalsIgnoreCase(anchorId)){
                	return listMedia;
                }
            }   
        }
    	return null;
    }
    
}
