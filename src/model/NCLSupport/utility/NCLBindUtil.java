
package model.NCLSupport.utility;

import java.util.Iterator;

import br.uff.midiacom.ana.connector.NCLAttributeAssessment;
import br.uff.midiacom.ana.connector.NCLRoleElement;
import br.uff.midiacom.ana.connector.NCLSimpleAction;
import br.uff.midiacom.ana.connector.NCLSimpleCondition;
import br.uff.midiacom.ana.descriptor.NCLDescriptor;
import br.uff.midiacom.ana.interfaces.NCLArea;
import br.uff.midiacom.ana.interfaces.NCLInterface;
import br.uff.midiacom.ana.interfaces.NCLProperty;
import br.uff.midiacom.ana.link.NCLBind;
import br.uff.midiacom.ana.link.NCLBindParam;
import br.uff.midiacom.ana.link.NCLLink;
import br.uff.midiacom.ana.util.enums.NCLDefaultActionRole;
import br.uff.midiacom.ana.util.enums.NCLDefaultConditionRole;
import br.uff.midiacom.ana.util.enums.NCLEventAction;
import br.uff.midiacom.ana.util.enums.NCLEventType;

/**
 *
 * @author Douglas
 */
public final class NCLBindUtil {
    
    private NCLBindUtil(){}
    
    @SuppressWarnings("rawtypes")
	public static boolean roleIsDefaultAction(NCLBind bind){
        Object role = bind.getRole().getRole();
        if(role instanceof NCLDefaultActionRole){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }        
    }
    
    @SuppressWarnings("rawtypes")
	public static boolean roleIsDefaultCondition(NCLBind bind){
        Object role = bind.getRole().getRole();
        if(role instanceof NCLDefaultConditionRole){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }        
    }
    
    @SuppressWarnings("rawtypes")
	public static String getRoleName(NCLBind bind){
        return bind.getRole().getRole().toString();
    }
    
    @SuppressWarnings("rawtypes")
	public static String getComponentId(NCLBind bind){
        return bind.getComponent().getId();
    }
    
    @SuppressWarnings("rawtypes")
	public static String getInterfaceId(NCLBind bind){
        NCLInterface interf;
        interf = bind.getInterface();
        if(interf instanceof NCLArea){
            return ((NCLArea)bind.getInterface()).getId();
        }else if(interf instanceof NCLProperty){
            return ((NCLProperty)bind.getInterface()).getName().toString();
        }
        return null;
    }
    
    @SuppressWarnings("rawtypes")
	public static String getDescriptorId(NCLBind bind){
        NCLDescriptor nclDescriptor = (NCLDescriptor) bind.getDescriptor();
        if(nclDescriptor != null){
            return nclDescriptor.getId();
        }else{
            return null;
        }
    }
    
    @SuppressWarnings("rawtypes")
	public static String getEventAction(NCLBind bind) { //o enum NCLEventAction foi usado para condição e ação, pois no HTG a ação que
        String nameRole = getRoleName(bind);                //dispara a transição possui como nome somente start, não starts para condição.
        String eventAction = "";
        if(roleIsCondition(bind)){
            if(roleIsDefaultCondition(bind)){
                if(nameRole.equalsIgnoreCase(NCLDefaultConditionRole.ONBEGIN.toString()) || nameRole.equalsIgnoreCase(NCLDefaultConditionRole.ONSELECTION.toString()) || 
                   nameRole.equalsIgnoreCase(NCLDefaultConditionRole.ONBEGINATTRIBUTION.toString()) || nameRole.equalsIgnoreCase(NCLDefaultConditionRole.ONENDATTRIBUTION.toString())){
                    eventAction=NCLEventAction.START.toString();
                }else if(nameRole.equalsIgnoreCase(NCLDefaultConditionRole.ONEND.toString())){
                    eventAction=NCLEventAction.STOP.toString();
                }else if(nameRole.equalsIgnoreCase(NCLDefaultConditionRole.ONABORT.toString())){
                    eventAction=NCLEventAction.ABORT.toString();
                }else if(nameRole.equalsIgnoreCase(NCLDefaultConditionRole.ONPAUSE.toString())){
                    eventAction=NCLEventAction.PAUSE.toString();
                }else if(nameRole.equalsIgnoreCase(NCLDefaultConditionRole.ONRESUME.toString())){
                    eventAction=NCLEventAction.RESUME.toString();
                }
            }else{//papel definido pelo usuário no elemento assessmentStatement.
                eventAction = "Not Defined";
            }
        }else if(roleIsAction(bind)){
            if(roleIsDefaultAction(bind)){
                if(nameRole.equalsIgnoreCase(NCLDefaultActionRole.START.toString()) || nameRole.equalsIgnoreCase(NCLDefaultActionRole.SET.toString())){
                    eventAction=NCLEventAction.START.toString();
                }else if(nameRole.equalsIgnoreCase(NCLDefaultActionRole.STOP.toString())){
                    eventAction=NCLEventAction.STOP.toString();
                }else if(nameRole.equalsIgnoreCase(NCLDefaultActionRole.ABORT.toString())){
                    eventAction=NCLEventAction.ABORT.toString();
                }else if(nameRole.equalsIgnoreCase(NCLDefaultActionRole.PAUSE.toString())){
                    eventAction=NCLEventAction.PAUSE.toString();
                }else if(nameRole.equalsIgnoreCase(NCLDefaultActionRole.RESUME.toString())){
                    eventAction=NCLEventAction.RESUME.toString();
                }
            }
        } 
        return eventAction;
   }

   @SuppressWarnings("rawtypes")
public static String getEventType(NCLBind bind) {
        String nameRole = getRoleName(bind);
        String eventType = "";
        if(roleIsDefaultCondition(bind)){
            if(nameRole.equalsIgnoreCase(NCLDefaultConditionRole.ONBEGIN.toString()) || nameRole.equalsIgnoreCase(NCLDefaultConditionRole.ONEND.toString()) ||
               nameRole.equalsIgnoreCase(NCLDefaultConditionRole.ONABORT.toString()) || nameRole.equalsIgnoreCase(NCLDefaultConditionRole.ONPAUSE.toString()) ||
               nameRole.equalsIgnoreCase(NCLDefaultConditionRole.ONRESUME.toString())){
                eventType = NCLEventType.PRESENTATION.toString();
            }else if(nameRole.equalsIgnoreCase(NCLDefaultConditionRole.ONSELECTION.toString())){
                eventType = NCLEventType.SELECTION.toString();
            }else if(nameRole.equalsIgnoreCase(NCLDefaultConditionRole.ONBEGINATTRIBUTION.toString()) || nameRole.equalsIgnoreCase(NCLDefaultConditionRole.ONENDATTRIBUTION.toString())){
                eventType = NCLEventType.ATTRIBUTION.toString();
            }
        }else if(roleIsDefaultAction(bind)){
            if(nameRole.equalsIgnoreCase(NCLDefaultActionRole.START.toString()) || nameRole.equalsIgnoreCase(NCLDefaultActionRole.STOP.toString()) ||
               nameRole.equalsIgnoreCase(NCLDefaultActionRole.ABORT.toString()) || nameRole.equalsIgnoreCase(NCLDefaultActionRole.PAUSE.toString()) ||
               nameRole.equalsIgnoreCase(NCLDefaultActionRole.RESUME.toString())){
                eventType = NCLEventType.PRESENTATION.toString();
            }else if(nameRole.equalsIgnoreCase(NCLDefaultActionRole.SET.toString())){
                eventType = NCLEventType.ATTRIBUTION.toString();
            }
        }
        return eventType;
   }
   
   @SuppressWarnings("rawtypes")
public static boolean roleIsCondition(NCLBind bind) {
        NCLRoleElement role = bind.getRole();
        if(role instanceof NCLSimpleCondition || role instanceof  NCLAttributeAssessment){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
    
   @SuppressWarnings("rawtypes")
public static boolean roleIsAction(NCLBind bind) {
        NCLRoleElement role = bind.getRole();
        if(role instanceof NCLSimpleAction){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
    
   @SuppressWarnings("rawtypes")
public static boolean hasInterface(NCLBind bind){
        if(bind.getInterface()!=null){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
   }
   
   @SuppressWarnings("rawtypes")
public static NCLBindParam getBindParam(NCLBind bind, String paramName) {
       NCLBindParam bindParam;
       Iterator iterator = bind.getBindParams().iterator();
       while(iterator.hasNext()){
           bindParam = (NCLBindParam) iterator.next();
           String currentParamName = bindParam.getName().getName();
           if(paramName.equalsIgnoreCase(currentParamName)){
               return bindParam;
           }
       }
       return null;
   }
   
   //role do elemento AttributeAssessment não pode ter mais de um bind no link se referenciado a ele, pois
   //o AttributeAssessment não possui os atributos qualifier e max/min como o simpleConditio/Action;
   @SuppressWarnings("rawtypes")
   public static NCLBind getReferencedBind_AttributeAssessment(String roleName, NCLLink link) {
       Iterator iterator = link.getBinds().iterator();
       NCLBind bind;
       while(iterator.hasNext()){
           bind = (NCLBind) iterator.next();
           String currentRoleName = getRoleName(bind);
           if(currentRoleName.equalsIgnoreCase(roleName)){
               return bind;
           }
       }
       return null;
    }
   
}
