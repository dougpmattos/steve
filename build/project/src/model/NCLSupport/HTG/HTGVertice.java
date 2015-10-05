
package model.NCLSupport.HTG;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Douglas
 */
public class HTGVertice {
    
     private String eventAction, eventType, anchorId;
     private List<HTGEdge> adj;
     private Boolean visited;
     private int presentPlanPosition;
    
     public HTGVertice(String eventAction, String anchorId, String eventType) {
    	 
         this.eventAction = eventAction;
         this.eventType = eventType;
         this.anchorId = anchorId;
         adj = new ArrayList<HTGEdge>();
         
     }

      public void addAdj(HTGEdge e) {
          adj.add(e);
      }
      
      public List<HTGEdge> getAdjacencies(){
          return adj;
      }
      
      public String getEventAction(){
          return eventAction;
      }
      
      public String getEventType(){
          return eventType;
      }
      
      public String getAnchorId(){
          return anchorId;
      }
      
      public void setVisited(Boolean value){
          visited = value;
      }
      
      public Boolean getVisited() {
          return visited;
      }
      
      public void setPresentPlanPosition(int i) {
          presentPlanPosition = i;
      }
      
      public int getPresentPlanPosition(){
          return presentPlanPosition;
      }
      
      @Override
      public String toString(){
          return "(" + eventAction + "," + anchorId + "," + eventType + ")";
      }

    
}
