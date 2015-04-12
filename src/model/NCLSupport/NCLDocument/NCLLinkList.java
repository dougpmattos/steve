
package model.NCLSupport.NCLDocument;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.uff.midiacom.ana.NCLBody;
import br.uff.midiacom.ana.NCLDoc;
import br.uff.midiacom.ana.link.NCLLink;
import br.uff.midiacom.ana.node.NCLContext;

/**
 *
 * @author Douglas
 */
@SuppressWarnings("rawtypes")
public class NCLLinkList extends ArrayList<NCLLink> {
    
	private static final long serialVersionUID = -3759983053070484085L;
	
	private NCLBody body;
    private List<String> linkIdList;
    private Iterator iterator, areaListIterator;

    public NCLLinkList(NCLDoc nclDoc, ArrayList<NCLContext> contextList) {
        body = nclDoc.getBody();
        iterator = body.getLinks().iterator();
        while(iterator.hasNext()){
            add((NCLLink) iterator.next());   
        }
        for (NCLContext context : contextList) {
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
