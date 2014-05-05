
package model.nclDocument.extendedAna;

import br.uff.midiacom.ana.NCLElement;
import br.uff.midiacom.ana.interfaces.NCLArea;
import br.uff.midiacom.ana.util.TimeType;
import br.uff.midiacom.ana.util.exception.XMLException;

/**
 *
 * @author Douglas
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Area extends NCLArea<NCLElement> {
    
	private static final long serialVersionUID = 1922351697883437711L;

	public Area(){
        super();
    }    
    
    public Area(String id) throws XMLException{
        super(id);
    }
    
    public Double getAreaBegin() throws XMLException{
        if(getBegin()!=null){
            return convertToSeconds(getBegin());
        }else{
            return null;
        }
    }
    
    public Double getAreaEnd() throws XMLException{
        if(getEnd()!=null){
            return convertToSeconds(getEnd());
        }else{
            return null;
        }
        
    }
    
    private Double convertToSeconds(TimeType explicitDur) {
        if(explicitDur.getSecond() == null){
            return null;
        }
        Double seconds=0.0;
        if(explicitDur.getHour() != null){
            seconds = ((explicitDur.getHour().doubleValue())*3600);
        }else if(explicitDur.getMinute() != null){
            seconds+=((explicitDur.getMinute().doubleValue())*60);
        }
        seconds += explicitDur.getSecond();
        return seconds;
    }
    
}
