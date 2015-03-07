
package model.NCLSupport.utility;

import br.uff.midiacom.ana.connector.NCLCausalConnector;
import br.uff.midiacom.ana.link.NCLBind;
import br.uff.midiacom.ana.link.NCLLink;
import br.uff.midiacom.ana.link.NCLLinkParam;
import br.uff.midiacom.ana.util.enums.NCLDefaultConditionRole;

import java.util.Iterator;

/**
 *
 * @author Douglas
 */
public final class LinkUtil {
    
    private LinkUtil(){}
    
    @SuppressWarnings("rawtypes")
	public static NCLCausalConnector getConnector(NCLLink link){
        NCLCausalConnector casualConnector = (NCLCausalConnector)link.getXconnector();
        return casualConnector;
    }
    
    @SuppressWarnings("rawtypes")
	public static Boolean hasOnSelectionCondition(NCLLink link){
        NCLBind bind;
        String roleName;
        Iterator iteratorBinds = link.getBinds().iterator();
        while(iteratorBinds.hasNext()){
            bind = (NCLBind) iteratorBinds.next();
            roleName = BindUtil.getRoleName(bind);
            if(roleName.equalsIgnoreCase(NCLDefaultConditionRole.ONSELECTION.toString())){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
    
    @SuppressWarnings("rawtypes")
	public static NCLLinkParam getLinkParam(NCLLink link, String paramName) {
        NCLLinkParam linkParam;
        Iterator iterator = link.getLinkParams().iterator();
        while(iterator.hasNext()){
            linkParam = (NCLLinkParam) iterator.next();
            String currentParamName = linkParam.getName().getName();
            if(paramName.equalsIgnoreCase(currentParamName)){
                return linkParam;
            }
        }
        return null;
    }
    
            
}
