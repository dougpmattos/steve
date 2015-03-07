
package model.temporalView;

import java.util.ArrayList;
import java.util.List;

import model.NCLSupport.HTG.HTGVertice;
import model.NCLSupport.NCLDocument.MediaList;
import model.NCLSupport.presentationPlan.PresentationPlan;
import model.NCLSupport.utility.HtgUtil;
import br.uff.midiacom.ana.util.enums.NCLEventAction;
import br.uff.midiacom.ana.util.enums.NCLEventType;
import br.uff.midiacom.ana.util.exception.XMLException;

/**
 *
 * @author Douglas
 */
public class TemporalView {
    
    List<TemporalMediaInfo> mainMediaInfoList = new ArrayList<TemporalMediaInfo>();
    List<TemporalMediaInfo> secMediaInfoList = new ArrayList<TemporalMediaInfo>();
    
    public TemporalView(PresentationPlan presentationPlan, MediaList mediaList) throws XMLException{
        HTGVertice vertice;
        for (int i = 0; i < presentationPlan.getMatrix().length; i++) {
            vertice = (HTGVertice) presentationPlan.getMatrix()[i][0];
            if(vertice.getAction().equalsIgnoreCase(NCLEventAction.START.toString()) && HtgUtil.isMedia(mediaList,vertice.getAnchorId()) && vertice.getEventType().equalsIgnoreCase(NCLEventType.PRESENTATION.toString())){
            	TemporalMediaInfo mediaInfo = new TemporalMediaInfo(vertice.getAnchorId(),((Double) presentationPlan.getMatrix()[i][1]),((Double) presentationPlan.getStopTime(vertice)), mediaList.getMedia(vertice.getAnchorId()));
                mainMediaInfoList.add(mediaInfo);
            }
       }
       if(presentationPlan.getSecMatrix()!=null){
           for (int i = 0; i < presentationPlan.getSecMatrix().length; i++) {
        
               vertice = (HTGVertice) presentationPlan.getSecMatrix()[i][0];
               if(HtgUtil.isMedia(mediaList,vertice.getAnchorId()) && vertice.getEventType().equalsIgnoreCase(NCLEventType.PRESENTATION.toString())){
                   if(vertice.getAction().equalsIgnoreCase(NCLEventAction.START.toString())){
                       Double sum = ((Double)presentationPlan.getSecMatrix()[i][1]) + presentationPlan.getX();
                       TemporalMediaInfo mediaInfo = new TemporalMediaInfo(vertice.getAnchorId(), sum, presentationPlan.getStopTime(vertice)+ presentationPlan.getX(), mediaList.getMedia(vertice.getAnchorId()));
                       secMediaInfoList.add(mediaInfo);
                       //temporalViewPanel.addMediaInteractivity(mediaInfo);
                   }else if(vertice.getAction().equalsIgnoreCase(NCLEventAction.STOP.toString()) && isStoppedMainTimelineVertice(vertice, presentationPlan)){
                       TemporalMediaInfo mediaInfo = new TemporalMediaInfo(vertice.getAnchorId(), presentationPlan.getStartTime(vertice), (Double)presentationPlan.getSecMatrix()[i][1]+presentationPlan.getX(), mediaList.getMedia(vertice.getAnchorId()));
                       secMediaInfoList.add(mediaInfo);
                       //temporalViewPanel.addMediaInteractivity(mediaInfo);
                   }
               }
           }
       }
    }
    
    private Boolean isStoppedMainTimelineVertice(HTGVertice vertice, PresentationPlan presentationPlan){
       Boolean found = Boolean.TRUE;
       HTGVertice currentVertice, searchedVertice;
       searchedVertice = vertice;
       for (int i = 0; i < presentationPlan.getSecMatrix().length; i++) {
               currentVertice = (HTGVertice) presentationPlan.getSecMatrix()[i][0];
               if(currentVertice.getAnchorId().equalsIgnoreCase(searchedVertice.getAnchorId()) &&
                       currentVertice.getAction().equalsIgnoreCase(NCLEventAction.START.toString()) &&
                       currentVertice.getEventType().equalsIgnoreCase(searchedVertice.getEventType())){
                   found = Boolean.FALSE;
               }     
       }
       return found;
   }
   
    public List<TemporalMediaInfo> getMainMediaInfoList(){
        return mainMediaInfoList;
    }
    
    public List<TemporalMediaInfo> getSecMediaInfoList(){
        return secMediaInfoList;
    }
    
}
