package view.temporalViewPane;

import java.util.ArrayList;

public class TemporalMediaNodeList extends ArrayList<TemporalMediaNode> {
	
	private static final long serialVersionUID = 5161615912335172004L;
	
	private String id;

	public TemporalMediaNodeList(String id){
		
		this.id = id;
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public TemporalMediaNode getPreviousMedia(TemporalMediaNode media){
		
		TemporalMediaNode previousMedia = null;
		
		for(int i = 0; i< size(); i++){
			
			TemporalMediaNode currentMedia = get(i);
			if(i == 0 && currentMedia == media){
				previousMedia = null;
				break;
			}else if(currentMedia == media){
				previousMedia = get(i-1);
				break;
			}
			
		}
		
		return previousMedia;
		
	}
	
}
