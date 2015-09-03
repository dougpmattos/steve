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
		
		if(!this.isEmpty()){
			quickSort(this, 0, this.size() - 1);
		}
		
		Double mediaBegin = media.getMedia().getBegin();
		
		for(int i = size()-1; i >= 0; i--){
			
			TemporalMediaNode currentTemporalMediaNode = get(i);
			if(currentTemporalMediaNode.getMedia().getBegin() < mediaBegin){
				return currentTemporalMediaNode;
			}
		}
		
		return null;
		
	}
		
    private void quickSort(ArrayList<TemporalMediaNode> list, int listBegin, int listEnd) {
        int middle = partition(list, listBegin, listEnd);
        if (listBegin < (middle-1))
            quickSort(list, listBegin, middle-1);
        if (middle < listEnd)
            quickSort(list, middle, listEnd);
    }
     
    private int partition(ArrayList<TemporalMediaNode> list, int listBegin, int listEnd) {
        int i = listBegin, j = listEnd;
        TemporalMediaNode mediaTemp;
        TemporalMediaNode pivotMedia = list.get((listBegin+listEnd)/2);
        Double pivot = pivotMedia.getMedia().getBegin();
        while (i <= j) {
            while( ( (Double) list.get(i).getMedia().getBegin()) < pivot )
                i++;
            while( ( (Double) list.get(j).getMedia().getBegin()) > pivot )
                j--;
            if(i <= j){
            	mediaTemp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, mediaTemp);
                i++;
                j--;
            }
        }
        return i;
    }
	
}
