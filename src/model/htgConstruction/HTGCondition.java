
package model.htgConstruction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.nclDocument.extendedAna.Area;
import model.nclDocument.extendedAna.Media;
import model.utility.ncl.BindUtil;
import model.utility.ncl.ConnectorUtil;
import model.utility.ncl.LinkUtil;
import br.uff.midiacom.ana.connector.NCLAssessmentStatement;
import br.uff.midiacom.ana.connector.NCLAttributeAssessment;
import br.uff.midiacom.ana.connector.NCLCausalConnector;
import br.uff.midiacom.ana.connector.NCLCompoundCondition;
import br.uff.midiacom.ana.connector.NCLCondition;
import br.uff.midiacom.ana.connector.NCLConnectorParam;
import br.uff.midiacom.ana.connector.NCLSimpleCondition;
import br.uff.midiacom.ana.link.NCLBind;
import br.uff.midiacom.ana.link.NCLLink;
import br.uff.midiacom.ana.util.enums.NCLConditionOperator;
import br.uff.midiacom.ana.util.enums.NCLOperator;
import br.uff.midiacom.ana.util.exception.XMLException;

/**
 *
 * @author Douglas
 */
public class HTGCondition implements Cloneable{
    
    String durVariable;
    Double value;
    Double processedAreaEndValue = null;
    
    List<HTGVertice> conditionVerticeList = new ArrayList<HTGVertice>();
    String qualifier;
    
    String operator = null;
    String assessmentStatement = null;
    String comparator;
    String assessmentValue;
    String anchorId;
    
    String interactivity = null;
    String key;
    
    String delay = null;
    String actionDelay;
    
    public HTGCondition(){
        
    }
    
    public HTGCondition(Media media) throws XMLException, IOException {
        durVariable = "Dur"+media.getId().substring(0,1).toUpperCase()+media.getId().substring(1);
        value = media.getDuration();
    }
    
    public HTGCondition(Area area, Double dur, Boolean isAreaEnd) throws XMLException, IOException {
        Media mediaParent = (Media) area.getParent(); 
        durVariable = "Dur"+mediaParent.getId().substring(0,1).toUpperCase()+mediaParent.getId().substring(1);
        value = dur;
        if(isAreaEnd){
            processedAreaEndValue = area.getAreaEnd() - area.getAreaBegin();
        }
       
    }
    
    public HTGCondition(Double dur){
        durVariable = null;
        value = dur;
    }
    
    public String getDurVariable(){
        return durVariable;
    }
    
    public Double getValue(){
        if(assessmentStatement!=null){//pois estou ignorando os vertices que testam essas variavéis
            return 0.0;
        }
        if(processedAreaEndValue!=null){
            if(delay!=null){
                return processedAreaEndValue + Double.valueOf(delay);
            }else{
                return processedAreaEndValue;
            }
        }else if(value!=null){
            if(delay!=null){
                return value + Double.valueOf(delay);
            }else{
                return value;
            }
        }else if(interactivity!=null){
            return 0.0;
        }else if(delay!=null){
            return Double.valueOf(delay);
        }
        return null;
    }
    
    @SuppressWarnings("rawtypes")
	public void addDefaultCondition(HTGVertice conditionVertice, NCLSimpleCondition simpleCondition, String compoundConditionOperator){
        
        conditionVerticeList.add(conditionVertice);
        NCLConditionOperator conditionOperator = simpleCondition.getQualifier();
        if(conditionOperator!=null){
            qualifier = conditionOperator.toString();
        }else{
            qualifier="";
        }
        if(compoundConditionOperator!=null){
            operator = compoundConditionOperator;
        }else{
            operator="";
        }
        value = null;
    }
    
    @SuppressWarnings("rawtypes")
	public void addAssessmentStatement(NCLAssessmentStatement nclAssessmentStatement,NCLLink link) throws XMLException {
        NCLBind bind;
        comparator = nclAssessmentStatement.getComparator().toString();
        Object objectValueAssessment = nclAssessmentStatement.getValueAssessment();
        if(objectValueAssessment instanceof NCLConnectorParam){
            NCLConnectorParam connectorParam = (NCLConnectorParam) objectValueAssessment;
            assessmentValue = ConnectorUtil.getParamValue(connectorParam,link);
        }else{
            assessmentValue = objectValueAssessment.toString();
        }
        NCLAttributeAssessment attributeAssessment = (NCLAttributeAssessment) nclAssessmentStatement.getAttributeAssessments().get(0);
        bind = BindUtil.getReferencedBind_AttributeAssessment(attributeAssessment.getRole(), link);
        if(BindUtil.hasInterface(bind)){
            anchorId = BindUtil.getInterfaceId(bind);
        }else{
            anchorId = BindUtil.getComponentId(bind);
        }
        String eventType = attributeAssessment.getEventType().toString();
        assessmentStatement = anchorId+" "+comparator+" "+assessmentValue;
        value = null;
    }
    
    @SuppressWarnings("rawtypes")
	public void addInteractivity(NCLLink link, NCLBind bind) {
        NCLCausalConnector connector = LinkUtil.getConnector(link);
        NCLCondition nclCondition = connector.getCondition();
        if(nclCondition instanceof NCLSimpleCondition){
            NCLSimpleCondition simpleCondition = (NCLSimpleCondition) nclCondition;
            Object objectKey = simpleCondition.getKey();
            if(objectKey instanceof NCLConnectorParam){
                NCLConnectorParam connectorParam = (NCLConnectorParam) objectKey;
                key = ConnectorUtil.getParamValue(connectorParam,link);
            }else{
                key = objectKey.toString();
            }
        }else if(nclCondition instanceof NCLCompoundCondition){
            NCLSimpleCondition simpleCondition = ConnectorUtil.getSimpleCondition(connector,BindUtil.getRoleName(bind));
            Object objectKey = simpleCondition.getKey();
            if(objectKey instanceof NCLConnectorParam){
                NCLConnectorParam connectorParam = (NCLConnectorParam) objectKey;
                key = ConnectorUtil.getParamValue(connectorParam,link);
            }else{
                key = objectKey.toString();
            }
        }
        interactivity = "Interactive Action ( Button "+key+" )";
        value = null;
    }
    
    @SuppressWarnings("rawtypes")
	public void addDelay(Object objectDelay, NCLLink link) {
        if(objectDelay instanceof NCLConnectorParam){
            NCLConnectorParam connectorParam = (NCLConnectorParam) objectDelay;
            this.delay = ConnectorUtil.getParamValue(connectorParam,link);
        }else{
            this.delay = objectDelay.toString();
        }
        if(assessmentStatement!=null){
            actionDelay = " "+NCLOperator.AND+" Delay = "+delay;
        }else{
            actionDelay = "Delay = "+delay;
        }
        value = null;
    }
    
    public boolean isInteractive(){
        if(interactivity != null){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
    
    @Override
    public HTGCondition clone() throws CloneNotSupportedException {  
      HTGCondition clone = (HTGCondition) super.clone();
      clone.conditionVerticeList = conditionVerticeList; 
      return clone;          
   }  
    
    @Override
    public String toString(){
        if(!conditionVerticeList.isEmpty()){
            String conditionVertices = "";
            for (int i = 0; i < conditionVerticeList.size(); i++) {
                if(delay!=null){
                    conditionVertices = operator+qualifier+" "+conditionVerticeList.get(i)+" "+actionDelay;
                }else{
                    conditionVertices = operator+qualifier+" "+conditionVerticeList.get(i);
                }
            }
            return conditionVertices;
        }else if(assessmentStatement!=null){
            if(delay!=null){
                return assessmentStatement+actionDelay;
            }else{
                return assessmentStatement;
            }
        }else if(interactivity != null){
            if(delay!=null){
                return interactivity+actionDelay;
            }else{
                return interactivity;
            }
        }else{
            if(durVariable==null){
                if(value==null){
                    if(delay!=null){
                        return actionDelay;
                    }else{
                        return "";
                    } 
                }else{
                     if(delay!=null){
                         return actionDelay+""+value;
                     }else{
                         return ""+value;
                     }
                }
            }else{
                if(delay!=null){
                    return actionDelay+durVariable+" = "+value;
                }else{
                    return durVariable+" = "+value;
                }
                
            } 
        }
    }
    
}
