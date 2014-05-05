
package model.nclDocument;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.nclDocument.extendedAna.Body;
import model.nclDocument.extendedAna.Context;
import model.nclDocument.extendedAna.Doc;
import model.nclDocument.extendedAna.Node;
import br.uff.midiacom.ana.link.NCLLink;

/**
 *
 * @author Douglas
 */
@SuppressWarnings("rawtypes")
public class LinkList extends ArrayList<NCLLink> {
    
	private static final long serialVersionUID = -3759983053070484085L;
	Body body;
    Node node;
	NCLLink link;
    List<String> linkIdList;
    Iterator iterator, areaListIterator, contextIterator, nodesIterator;

    public LinkList(Doc nclDoc, ArrayList<Context> contextList) {
        body = nclDoc.getBody();
        iterator = body.getLinks().iterator();
        while(iterator.hasNext()){
            add((NCLLink) iterator.next());   
        }
        for (Context context : contextList) {
            iterator = context.getLinks().iterator();
            while(iterator.hasNext()){
                add((NCLLink) iterator.next());   
            }
        }
        areaListIterator = iterator();  
        
        linkIdList = new ArrayList<String>();
        while(areaListIterator.hasNext()){
            linkIdList.add(((NCLLink) areaListIterator.next()).getId());
        }
    }
    
    public List<String> getAllLinkId() {
        return linkIdList;
    }
    
    public String getLinkId(int index) {
        return linkIdList.get(index);
    }
    
    public NCLLink getLink(int index) {
        return get(index);
    }
    
}
