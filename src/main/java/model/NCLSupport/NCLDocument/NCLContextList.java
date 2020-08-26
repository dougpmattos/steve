
package model.NCLSupport.NCLDocument;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.uff.midiacom.ana.NCLBody;
import br.uff.midiacom.ana.NCLDoc;
import br.uff.midiacom.ana.node.NCLContext;
import br.uff.midiacom.ana.node.NCLNode;
import br.uff.midiacom.ana.util.exception.XMLException;

/**
 *
 * @author Douglas
 */
@SuppressWarnings("rawtypes")
public class NCLContextList extends ArrayList<NCLContext> {

	private static final long serialVersionUID = 8079672760739619449L;
	
	private NCLBody body;
	private NCLNode node;
	private NCLContext context;
	private List<String> contextIdList;
    private Iterator iterator, contextListIterator;
    
    public NCLContextList(NCLDoc nclDoc) {
        body = nclDoc.getBody();
        iterator = body.getNodes().iterator();
        while(iterator.hasNext()){
            node = (NCLNode) iterator.next();
            if(node instanceof NCLContext){
                context = (NCLContext) node;
                add(context);
                readContext(context);
            }   
        }
        contextListIterator = iterator();  

        contextIdList = new ArrayList<String>();
        while(contextListIterator.hasNext()){
            node = (NCLNode) contextListIterator.next();
            if(node instanceof NCLContext){
                contextIdList.add(((NCLContext) node).getId());
            }   
        }
    }
    
	private void readContext(NCLContext context){
        Iterator contextIterator = context.getNodes().iterator();
        while(contextIterator.hasNext()){
            node = (NCLNode) contextIterator.next();
            if(node instanceof NCLContext){
                context = (NCLContext) node;
                add(context);
                readContext(context);
            }   
        }
    }
    
    public List<String> getAllNCLContextId() throws XMLException{
        return contextIdList;
    }
    
    public String getNCLContextId(int index) throws XMLException{
        return contextIdList.get(index);
    }
    
    public NCLContext getNCLContext(int index) throws XMLException{
        return get(index);
    }
    
}
