
package model.NCLSupport.NCLDocument;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.NCLSupport.extendedAna.Body;
import model.NCLSupport.extendedAna.Context;
import model.NCLSupport.extendedAna.Doc;
import model.NCLSupport.extendedAna.Node;
import br.uff.midiacom.ana.util.exception.XMLException;

/**
 *
 * @author Douglas
 */
public class ContextList extends ArrayList<Context> {

	private static final long serialVersionUID = 8079672760739619449L;
	Body body;
    Node node;
    Context context;
    List<String> contextIdList;
    @SuppressWarnings("rawtypes")
	Iterator iterator, contextListIterator;
    
    public ContextList(Doc nclDoc) {
        body = nclDoc.getBody();
        iterator = body.getNodes().iterator();
        while(iterator.hasNext()){
            node = (Node) iterator.next();
            if(node instanceof Context){
                context = (Context) node;
                add(context);
                readContext(context);
            }   
        }
        contextListIterator = iterator();  

        contextIdList = new ArrayList<String>();
        while(contextListIterator.hasNext()){
            node = (Node) contextListIterator.next();
            if(node instanceof Context){
                contextIdList.add(((Context) node).getId());
            }   
        }
    }
    
    @SuppressWarnings("rawtypes")
	private void readContext(Context context){
        Iterator contextIterator = context.getNodes().iterator();
        while(contextIterator.hasNext()){
            node = (Node) contextIterator.next();
            if(node instanceof Context){
                context = (Context) node;
                add(context);
                readContext(context);
            }   
        }
    }
    
    public List<String> getAllContextId() throws XMLException{
        return contextIdList;
    }
    
    public String getContextId(int index) throws XMLException{
        return contextIdList.get(index);
    }
    
    public Context getContext(int index) throws XMLException{
        return get(index);
    }
    
}
