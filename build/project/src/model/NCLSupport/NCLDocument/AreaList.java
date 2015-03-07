
package model.NCLSupport.NCLDocument;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.NCLSupport.extendedAna.Area;
import model.NCLSupport.extendedAna.Body;
import model.NCLSupport.extendedAna.Doc;
import model.NCLSupport.extendedAna.Media;
import model.NCLSupport.extendedAna.Node;
import br.uff.midiacom.ana.util.exception.XMLException;

/**
 *
 * @author Douglas
 */
public class AreaList extends ArrayList<Area>  {

	private static final long serialVersionUID = 1265204907701387552L;
	Body body;
    Node node;
    Media media;
    Area area;
    List<String> areasIdList;
    @SuppressWarnings("rawtypes")
	Iterator iterator, areasListIterator;

    public AreaList(Doc nclDoc, String mediaId) throws XMLException {
        body = (Body) nclDoc.getBody();
        node = body.getNode(mediaId);
        if(node instanceof Media){
            media = (Media) node;
            areasListIterator = media.getAreas().iterator();
            while(areasListIterator.hasNext()){
                area = (Area) areasListIterator.next();
                add(area);   
            }
            areasIdList = new ArrayList<String>();
            areasListIterator = media.getAreas().iterator();
            while(areasListIterator.hasNext()){
                area = (Area) areasListIterator.next();
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
    
    public Area getArea(int index) throws XMLException{
        return get(index);
    }
    
}
