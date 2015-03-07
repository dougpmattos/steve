
package model.NCLSupport.utility;

import br.uff.midiacom.ana.connector.NCLAction;
import br.uff.midiacom.ana.connector.NCLCausalConnector;
import br.uff.midiacom.ana.connector.NCLCompoundAction;
import br.uff.midiacom.ana.connector.NCLCompoundCondition;
import br.uff.midiacom.ana.connector.NCLCondition;
import br.uff.midiacom.ana.connector.NCLConnectorParam;
import br.uff.midiacom.ana.connector.NCLSimpleAction;
import br.uff.midiacom.ana.connector.NCLSimpleCondition;
import br.uff.midiacom.ana.link.NCLBind;
import br.uff.midiacom.ana.link.NCLBindParam;
import br.uff.midiacom.ana.link.NCLLink;
import br.uff.midiacom.ana.link.NCLLinkParam;

import java.util.Iterator;

/**
 *
 * @author Douglas
 */
public final class ConnectorUtil {
    
     private ConnectorUtil(){}
    
     @SuppressWarnings("rawtypes")
	public static boolean hasCompound(NCLCausalConnector connector){
         NCLCondition condition  = connector.getCondition();
         NCLAction action = connector.getAction();
         if(condition instanceof NCLCompoundCondition || action instanceof NCLCompoundAction){
             return Boolean.TRUE;
         }else{
             return Boolean.FALSE;
         }
     }
     
     @SuppressWarnings("rawtypes")
	public static boolean hasCompoundCondition(NCLCausalConnector causalConnector) {
         NCLCondition condition = causalConnector.getCondition();
         if(condition instanceof NCLCompoundCondition){
             return Boolean.TRUE;
         }else{
             return Boolean.FALSE;
         }
    }
     
    @SuppressWarnings("rawtypes")
	public static boolean hasCompoundAction(NCLCausalConnector causalConnector) {
       NCLAction action = causalConnector.getAction();
         if(action instanceof NCLCompoundAction){
             return Boolean.TRUE;
         }else{
             return Boolean.FALSE;
         }
    }
    
    @SuppressWarnings("rawtypes")
	public static NCLSimpleCondition getSimpleCondition(NCLCausalConnector connector, String roleName) {
        NCLSimpleCondition foundSimpleCondition = null;
        NCLCondition nclCondition = connector.getCondition();
        if(nclCondition instanceof NCLSimpleCondition){
            NCLSimpleCondition simpleCondition = (NCLSimpleCondition) nclCondition;
            foundSimpleCondition = simpleCondition;
        }else if(nclCondition instanceof NCLCompoundCondition){
            NCLCompoundCondition compoundCondition = (NCLCompoundCondition) nclCondition;
            Iterator iterator = compoundCondition.getConditions().iterator();
            while(iterator.hasNext()){
                nclCondition = (NCLCondition) iterator.next();
                if(nclCondition instanceof NCLSimpleCondition){
                    NCLSimpleCondition simpleCondition = (NCLSimpleCondition) nclCondition;
                    String currentroleName = simpleCondition.getRole().toString();
                    if(currentroleName.equalsIgnoreCase(roleName)){
                        foundSimpleCondition = simpleCondition;
                    }
                }else if(nclCondition instanceof NCLCompoundCondition){
                    //tratar compoundCondition dentro de outra.
                }
            }
        }
        return foundSimpleCondition;
   }
    
    @SuppressWarnings("rawtypes")
	public static NCLSimpleAction getSimpleAction(NCLCausalConnector connector, String roleName) {
        NCLSimpleAction foundSimpleAction = null;
        NCLAction nclAction = connector.getAction();
        if(nclAction instanceof NCLSimpleAction){
            NCLSimpleAction simpleAction = (NCLSimpleAction) nclAction;
            foundSimpleAction = simpleAction;
        }else if(nclAction instanceof NCLCompoundAction){
            NCLCompoundAction compoundAction = (NCLCompoundAction) nclAction;
            Iterator iterator = compoundAction.getActions().iterator();
            while(iterator.hasNext()){
                nclAction = (NCLAction) iterator.next();
                if(nclAction instanceof NCLSimpleAction){
                    NCLSimpleAction simpleAction = (NCLSimpleAction) nclAction;
                    String currentRoleName = simpleAction.getRole().toString();
                    if(currentRoleName.equalsIgnoreCase(roleName)){
                        foundSimpleAction = simpleAction;
                    }
                }else if(nclAction instanceof NCLCompoundAction){
                    //tratar compoundAction dentro de outra.
                }
            }
        }
        return foundSimpleAction;
   }
    
    @SuppressWarnings("rawtypes")
	public static String getParamValue(NCLConnectorParam connectorParam, NCLLink link){
        String paramValue = null;
        String paramName = connectorParam.getName();
        NCLLinkParam linkParam = LinkUtil.getLinkParam(link,paramName);
        if(linkParam!=null){
            paramValue = linkParam.getValue().toString();
        }
        Iterator iterator = link.getBinds().iterator();
        Boolean foundBindParam = Boolean.FALSE;
        while(iterator.hasNext() && !foundBindParam){
            NCLBind bind = (NCLBind) iterator.next();
            NCLBindParam bindParam = BindUtil.getBindParam(bind,paramName);
            if(bindParam!=null){
                paramValue = bindParam.getValue().toString();
                foundBindParam = Boolean.TRUE;
            }
        }
        return paramValue;
    }
     
}
