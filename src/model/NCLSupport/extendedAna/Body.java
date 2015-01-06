
package model.NCLSupport.extendedAna;

import br.uff.midiacom.ana.NCLBody;
import br.uff.midiacom.ana.NCLElement;
import br.uff.midiacom.ana.interfaces.NCLInterface;
import br.uff.midiacom.ana.interfaces.NCLPort;
import br.uff.midiacom.ana.interfaces.NCLProperty;
import br.uff.midiacom.ana.link.NCLLink;
import br.uff.midiacom.ana.meta.NCLMeta;
import br.uff.midiacom.ana.meta.NCLMetadata;
import br.uff.midiacom.ana.util.exception.XMLException;

/**
 *
 * @author Douglas
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Body extends NCLBody<NCLElement, NCLPort, NCLProperty, Node, 
                                  NCLInterface, NCLLink, NCLMeta, NCLMetadata> implements Node {
    
	private static final long serialVersionUID = 1440379980266566714L;

	public Body() throws XMLException {
        super();
    }

    @Override
    protected Node createMedia() throws XMLException {
        return new Media();
    }

    @Override
    protected Node createContext() throws XMLException {
        return (Node) new Context();
    }

    @Override
    protected Node createSwitch() throws XMLException {
        return (Node) new Switch();
    }
    
}
