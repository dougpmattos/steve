
package model.nclDocument.extendedAna;

import br.uff.midiacom.ana.NCLDoc;
import br.uff.midiacom.ana.NCLElement;
import br.uff.midiacom.ana.NCLHead;
import br.uff.midiacom.ana.util.exception.XMLException;
import br.uff.midiacom.ana.util.ncl.NCLVariable;

/**
 *
 * @author Douglas
 */
@SuppressWarnings("rawtypes")
public class Doc extends NCLDoc<NCLElement, NCLHead, Body, NCLVariable> {
    
	private static final long serialVersionUID = -150032364981313404L;


	public Doc() throws XMLException {
        super();
    }
    
    
    @Override
    protected Body createBody() throws XMLException {
        return new Body();
    }
    
}
