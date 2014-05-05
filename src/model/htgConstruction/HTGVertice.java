
package model.htgConstruction;

import java.util.ArrayList;
import java.util.List;

import model.nclDocument.extendedAna.Media;

/**
 *
 * @author Douglas
 */
public class HTGVertice {
    
     String eventAction, eventType, anchorId;
     List<HTGEdge> adj;
     Boolean visited;
     int presentPlanPosition;
    
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
      
      public String getAction(){
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
