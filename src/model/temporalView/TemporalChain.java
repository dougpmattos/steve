package model.temporalView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import model.common.Media;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.Operation;
import view.temporalViewPane.enums.AllenRelation;

@SuppressWarnings("rawtypes")
public class TemporalChain extends Observable implements Serializable {

	private static final long serialVersionUID = -6154510036093880684L;
	
	private static int temporalChainNumber = 0;
	private static int temporalViewMediaNumber = 0;
	
	private int id;
	private Media masterMedia;
	private ArrayList<Media> mediaAllList = new ArrayList<Media>();
	private ArrayList<Relation> relationList = new ArrayList<Relation>();
	private ArrayList<ArrayList<Media>> mediaLineList = new ArrayList<ArrayList<Media>>();

	public TemporalChain() {
		
		this.id = temporalChainNumber;
		temporalChainNumber++;
		
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setMasterMedia(Media masterMedia){
		
		this.masterMedia = masterMedia;
        
	}
	
	public Media getMasterMedia() {
		return masterMedia;
	}
	
	public void addMedia(Media media){

		mediaAllList.add(media);
		int line = addMediaLineList(media);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_TEMPORAL_CHAIN_MEDIA, media, line);
        notifyObservers(operation);
        
        temporalViewMediaNumber++;
        
	}
	
	public void removeMedia(Media media){
		
		mediaAllList.remove(media);
		int line = removeMediaLineList(media);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_TEMPORAL_CHAIN_MEDIA, media, line);
        notifyObservers(operation);
        
        temporalViewMediaNumber--;
        
	}
	
	private int removeMediaLineList(Media media) {
		// TODO Auto-generated method stub
		return 0;
	}

	public ArrayList<Media> getMediaAllList() {
		return mediaAllList;
	}
	
	private int addMediaLineList(Media media){
		
//		boolean mediaAdded = false;
//    	AllenRelation allenRelation;
//    	int temporalMediaNodeListListIndex = 0;
//    	
//    	while(!mediaAdded && temporalMediaNodeListListIndex < temporalMediaNodeListList.size()){
//    		
//    		boolean isPossibleAdd = false;
//    		TemporalMediaNodeList temporalMediaNodeList = temporalMediaNodeListList.get(temporalMediaNodeListListIndex);
//    		int index = 0;
//    		while(!isPossibleAdd && index < temporalMediaNodeList.size()){
//    			Media currentMedia = temporalMediaNodeList.get(index).getMedia();
//    			allenRelation = identifyAllenRelation(media, currentMedia);
//    			
//    			if( (allenRelation.equals(AllenRelation.MEETS)) || (allenRelation.equals(AllenRelation.MET_BY))
//    			|| (!allenRelation.equals(AllenRelation.BEFORE)) || (!allenRelation.equals(AllenRelation.AFTER))){
//    				
//    				isPossibleAdd = true;
//    				
//    			}
//    			
//    			index++;
//    		}
//    		
//    		if(isPossibleAdd){
//    			
//    			TimeLineXYChartData temporalMediaNode = new TimeLineXYChartData(controller, media, temporalChainModel, temporalViewPane, repositoryPane, temporalMediaNodeList); 
//    			
//    			System.out.println(temporalMediaNode.getInvisibleBeginData().toString() + " - " + temporalMediaNode.getXYChartData().toString());
//    			
//    			temporalMediaNodeList.add(temporalMediaNode);
//    			
//    			serie.getData().addAll(temporalMediaNode.getInvisibleBeginData(), temporalMediaNode.getXYChartData());
//    			
//    			mediaAdded = true;
//    			
//    		}
//    		
//    		temporalMediaNodeListListIndex++;
//    	}
//    	
//    	if(!mediaAdded){
//    		
//    		int newLineIndex = temporalMediaNodeListList.size();
//    		TemporalMediaNodeList temporalMediaNodeList = new TemporalMediaNodeList(String.valueOf(newLineIndex));
//    		
//    		TimeLineXYChartData temporalMediaNode = new TimeLineXYChartData(controller, media, temporalChainModel, temporalViewPane, repositoryPane, temporalMediaNodeList); 
//    		
//    		temporalMediaNodeList.add(temporalMediaNode);
//    		temporalMediaNodeListList.add(newLineIndex, temporalMediaNodeList);
//    		
//    		//TODO somente quando ultrapassar 5 linhas
////    		yAxisCategoryList.add(Integer.toString(newLineIndex));
////    		CategoryAxis yAxis = (CategoryAxis) getYAxis();
////    		yAxis.setCategories(FXCollections.<String>observableArrayList(yAxisCategoryList));
//    		
//    		serie.getData().addAll(temporalMediaNode.getInvisibleBeginData(), temporalMediaNode.getXYChartData());
//    		
//    	}
    	
    	return 0;
		
	}
	
	private AllenRelation identifyAllenRelation(Media media, Media currentMedia) {
		
		double begin = media.getBegin();
		double end = media.getEnd();
		double currentMediaBegin = currentMedia.getBegin();
		double currentMediaEnd = currentMedia.getEnd();
		
		if(end == currentMediaBegin){
			return AllenRelation.MEETS;
		}else if(begin == currentMediaEnd){
			return AllenRelation.MET_BY;
		}else if(begin == currentMediaBegin && end == currentMediaEnd){
			return AllenRelation.EQUALS;
		}else if(begin == currentMediaBegin){
			return AllenRelation.STARTS;
		}else if(end == currentMediaEnd){
			return AllenRelation.FINISHES;
		}else if(end < currentMediaBegin){
			return AllenRelation.BEFORE;
		}else if(begin > currentMediaEnd){
			return AllenRelation.AFTER;
		}else if(end > currentMediaBegin && end < currentMediaEnd){
			return AllenRelation.OVERLAPS;
		}else if(begin > currentMediaBegin && begin < currentMediaEnd){
			return AllenRelation.OVERLAPPED_BY;
		}else if(begin > currentMediaBegin && end < currentMediaEnd){
			return AllenRelation.DURING;
		}else if(begin < currentMediaBegin && end > currentMediaEnd){
			return AllenRelation.CONTAINS;
		}else{
			return null;
		}
				
	}

//	private String getYAxisCategory(){
//		//return yAxisCategoryList.get(yAxisCategoryList.size() - 1);
//	}
	
	public void addSynchronousRelation(Synchronous<Media> synchronousRelation){
		
		relationList.add(synchronousRelation);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_SYNC_RELATION, synchronousRelation, getId());
        notifyObservers(operation);
        
	}
	
	public void removeSynchronousRelation(Synchronous<Media> synchronousRelation){
		
		relationList.remove(synchronousRelation);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_SYNC_RELATION, synchronousRelation, getId());
        notifyObservers(operation);
        
	}

	public ArrayList<Relation> getRelationList() {
		return relationList;
	}
	
	public static int getTemporalViewMediaNumber(){
		return temporalViewMediaNumber;
	}
	
	public ArrayList<ArrayList<Media>> getMediaLineList() {
		return mediaLineList;
	}
	
}
