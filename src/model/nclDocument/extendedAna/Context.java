
package model.nclDocument.extendedAna;

import br.uff.midiacom.ana.NCLElement;
import br.uff.midiacom.ana.interfaces.NCLInterface;
import br.uff.midiacom.ana.interfaces.NCLPort;
import br.uff.midiacom.ana.interfaces.NCLProperty;
import br.uff.midiacom.ana.link.NCLLink;
import br.uff.midiacom.ana.meta.NCLMeta;
import br.uff.midiacom.ana.meta.NCLMetadata;
import br.uff.midiacom.ana.node.NCLContext;
import br.uff.midiacom.ana.util.exception.XMLException;
import br.uff.midiacom.ana.util.reference.ExternalReferenceType;

/**
 *
 * @author Douglas
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Context extends NCLContext <NCLElement, NCLPort, NCLProperty, Node, NCLInterface, NCLLink,
                                         NCLMeta, NCLMetadata, ExternalReferenceType> implements Node {

	private static final long serialVersionUID = 2028101406485375770L;

	public Context(){
        super();
    }
    
    public Context(String id) throws XMLException {
        super();
        setId(id);
    }
    
    public boolean isReferenced(){
        return true;
    }
    
    @Override
    protected Node createMedia() throws XMLException {
        return new Media();
    }
    
    @Override
    protected Node createContext() throws XMLException {
        return new Context();
    }
    
    @Override
    protected Node createSwitch() throws XMLException {
        return new Switch();
    }
    
}
