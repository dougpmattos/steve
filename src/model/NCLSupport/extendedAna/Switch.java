
package model.NCLSupport.extendedAna;

import br.uff.midiacom.ana.NCLElement;
import br.uff.midiacom.ana.interfaces.NCLInterface;
import br.uff.midiacom.ana.interfaces.NCLSwitchPort;
import br.uff.midiacom.ana.node.NCLSwitch;
import br.uff.midiacom.ana.rule.NCLBindRule;
import br.uff.midiacom.ana.rule.NCLTestRule;
import br.uff.midiacom.ana.util.exception.XMLException;
import br.uff.midiacom.ana.util.reference.ExternalReferenceType;

/**
 *
 * @author Douglas
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Switch extends NCLSwitch <NCLElement, Node, NCLInterface, NCLSwitchPort, NCLTestRule, ExternalReferenceType, 
                                       NCLBindRule<NCLElement, Node, NCLTestRule, ExternalReferenceType>> 
                                       implements Node {

	private static final long serialVersionUID = 8796983270267597186L;

	public Switch(){
        super();
    }
    
    public Switch(String id) throws XMLException {
        super(id);
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
