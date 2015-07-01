package view.temporalViewPane;

import java.util.ArrayList;

import model.common.Media;

public class TemporalChainMediaLine extends ArrayList<Media> {
	
	private static final long serialVersionUID = 5161615912335172004L;
	
	private String id;

	public TemporalChainMediaLine(String id){
		
		this.id = id;
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Media getPreviousMedia(Media media){
		
		Media previousMedia = null;
		
		for(int i = 0; i< size(); i++){
			
			Media currentMedia = get(i);
			if(i == 0 && currentMedia == media){
				previousMedia = null;
			}else if(currentMedia == media){
				previousMedia = get(i-1);
			}
			
		}
		
		return previousMedia;
		
	}
	
}
