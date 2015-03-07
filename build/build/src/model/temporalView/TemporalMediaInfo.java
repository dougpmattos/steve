
package model.temporalView;

import model.NCLSupport.extendedAna.Media;

/**
 *
 * @author Douglas
 */
public class TemporalMediaInfo {
    
    private String id;
    private Media media;
	private Double startTime;
    private Double stopTime;
    
    public TemporalMediaInfo(String id, Double startTime, Double stopTime, Media media){
        this.id = id;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.media = media;
    }

    public Double getStopTime() {
        return stopTime;
    }

    public Double getStartTime() {
        return startTime;
    }

    public String getId() {
        return id;
    }
    
    public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}
    
    @Override
    public String toString(){
        return id+": "+startTime+" -----> "+stopTime;
    }
    
}
