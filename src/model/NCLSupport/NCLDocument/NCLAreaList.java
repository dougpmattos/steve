
package model.NCLSupport.NCLDocument;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.uff.midiacom.ana.NCLBody;
import br.uff.midiacom.ana.NCLDoc;
import br.uff.midiacom.ana.interfaces.NCLArea;
import br.uff.midiacom.ana.node.NCLMedia;
import br.uff.midiacom.ana.node.NCLNode;
import br.uff.midiacom.ana.util.exception.XMLException;

/**
 *
 * @author Douglas
 */
@SuppressWarnings("rawtypes")
public class NCLAreaList extends ArrayList<NCLArea>  {

	private static final long serialVersionUID = 1265204907701387552L;
	
	private NCLBody body;
	private NCLNode node;
	private NCLMedia media;
	private NCLArea area;
	private List<String> areasIdList;
    private Iterator areasListIterator;

    public NCLAreaList(NCLDoc nclDoc, String mediaId) throws XMLException {
    	
        body = (NCLBody) nclDoc.getBody();
        node = body.getNode(mediaId);
        if(node instanceof NCLMedia){
            media = (NCLMedia) node;
            areasListIterator = media.getAreas().iterator();
            while(areasListIterator.hasNext()){
                area = (NCLArea) areasListIterator.next();
                add(area);   
            }
            areasIdList = new ArrayList<String>();
            areasListIterator = media.getAreas().iterator();
            while(areasListIterator.hasNext()){
                area = (NCLArea) areasListIterator.next();
                areasIdList.add(area.getId());
            }
        }
       
    }
    
    public List<String> getAllAreasId() throws XMLException{
        return areasIdList;
    }
    
    public String getAreaId(int index) throws XMLException{
        return areasIdList.get(index);
    }
    
    public NCLArea getArea(int index) throws XMLException{
        return get(index);
    }
    
}
