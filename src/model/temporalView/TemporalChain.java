package model.temporalView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import model.common.Media;
import model.temporalView.enums.RelationType;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.Operation;
import view.common.InputDialog;
import view.common.Language;
import view.common.MessageDialog;
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
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.REMOVE_TEMPORAL_CHAIN_MEDIA, media, line);
        notifyObservers(operation);
        
        temporalViewMediaNumber--;
        
	}
	
	private int removeMediaLineList(Media media) {
		
		int line = 0;
    	Boolean removed = false;
		
    	while(line < mediaLineList.size()){
    		
    		ArrayList<Media> mediaList = mediaLineList.get(line);
    		
    		if(mediaList.remove(media)){
    			return line;
    		}
    		line++;
    		
    	}
    	
    	return -1;
		
		
	}

	public ArrayList<Media> getMediaAllList() {
		return mediaAllList;
	}
	
	private int addMediaLineList(Media media){

    	AllenRelation allenRelation;
    	int line = 0;
    	
    	while(line < mediaLineList.size()){
    		
    		boolean isPossibleAdd = true;
    		ArrayList<Media> mediaList = mediaLineList.get(line);
    		int index = 0;
    		
    		while(isPossibleAdd && index < mediaList.size()){
    			
    			Media currentMedia = mediaList.get(index);
    			allenRelation = identifyAllenRelation(media, currentMedia);
    			
    			if (!(allenRelation.equals(AllenRelation.BEFORE) || (allenRelation.equals(AllenRelation.AFTER)))){
    				
    				isPossibleAdd = false;
    				
    			}
    			
    			index++;
    			
    		}
    		
    		if(isPossibleAdd){
    			
    			mediaList.add(media);
    			return line;

    		}
    		
    		line++;
    	}

    	int newLineIndex = mediaLineList.size();
    	ArrayList<Media> newMediaList = new ArrayList<Media>();
    	newMediaList.add(media);
    	mediaLineList.add(newLineIndex, newMediaList);

    	return newLineIndex;
		
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

	public void addSynchronousRelation(Synchronous<Media> synchronousRelation){
		
		relationList.add(synchronousRelation);
		
		switch(synchronousRelation.getType()){
			
			case STARTS:
	
				for(Media slaveMedia : synchronousRelation.getSlaveMediaList()){
					
					ArrayList<Relation> slaveMediaRelationList = getSlaveMediaRelationList();
					
					if(slaveMediaRelationList != null){
						
						for(Relation<Media> slaveMediaRelation : slaveMediaRelationList){
							
							Synchronous<Media> slaveMediaSynchronousRelation = (Synchronous<Media>) slaveMediaRelation;
							
							if(slaveMediaSynchronousRelation.getType() == RelationType.BEFORE){
								
								Boolean keepGoing = showBlockedRelationInputDialog(slaveMedia, slaveMediaSynchronousRelation);
								
						    	
							}else if(){
								
							}else {
								
							}
							
						}
						
					}else {
						
						slaveMedia.setBegin(synchronousRelation.getMasterMedia().getBegin());
						slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
						removeMedia(slaveMedia);
						addMedia(slaveMedia);
						
					}
					
				}
				
				break;
	            
			case STARTS_DELAY:
				
				for(Media slaveMedia : synchronousRelation.getSlaveMediaList()){
					
					slaveMedia.setBegin(synchronousRelation.getMasterMedia().getBegin() + synchronousRelation.getDelay());
					slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
					removeMedia(slaveMedia);
					addMedia(slaveMedia);
					
				}
				
				break;
				
			case FINISHES:
				
				for(Media slaveMedia : synchronousRelation.getSlaveMediaList()){
					
					Double begin = synchronousRelation.getMasterMedia().getEnd() - slaveMedia.getDuration();
					if(begin > 0){
						slaveMedia.setBegin(begin);
					}else {
						slaveMedia.setBegin(0.0);
					}
					
					slaveMedia.setEnd(synchronousRelation.getMasterMedia().getEnd());
					slaveMedia.setDuration(slaveMedia.getEnd() - slaveMedia.getBegin());
					removeMedia(slaveMedia);
					addMedia(slaveMedia);
					
				}
				
				break;
			
			case FINISHES_DELAY:
				
				for(Media slaveMedia : synchronousRelation.getSlaveMediaList()){
					
					Double begin = synchronousRelation.getMasterMedia().getEnd() + synchronousRelation.getDelay() - slaveMedia.getDuration();
					if(begin > 0){
						slaveMedia.setBegin(begin);
					}else {
						slaveMedia.setBegin(0.0);
					}
					
					slaveMedia.setEnd(synchronousRelation.getMasterMedia().getEnd() + synchronousRelation.getDelay());
					slaveMedia.setDuration(slaveMedia.getEnd() - slaveMedia.getBegin());
					removeMedia(slaveMedia);
					addMedia(slaveMedia);
					
				}
				
				break;
				
			case MEETS:
				
				for(Media slaveMedia : synchronousRelation.getSlaveMediaList()){
					
					slaveMedia.setBegin(synchronousRelation.getMasterMedia().getEnd());
					slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
					removeMedia(slaveMedia);
					addMedia(slaveMedia);
					
				}
				
				break;
			
			case MEETS_DELAY:
				
				for(Media slaveMedia : synchronousRelation.getSlaveMediaList()){
					
					slaveMedia.setBegin(synchronousRelation.getMasterMedia().getEnd() + synchronousRelation.getDelay());
					slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
					removeMedia(slaveMedia);
					addMedia(slaveMedia);
					
				}
				
				break;
			
			case MET_BY:
				
				Boolean isPossibleDefineMetBy = true;
				
				for(Media slaveMedia : synchronousRelation.getSlaveMediaList()){
					
					slaveMedia.setEnd(synchronousRelation.getMasterMedia().getBegin());
					Double begin = slaveMedia.getEnd() - slaveMedia.getDuration();
					if(begin > 0){
						slaveMedia.setBegin(begin);
					}else {
						slaveMedia.setBegin(0.0);
					}
					
					slaveMedia.setDuration(slaveMedia.getEnd() - slaveMedia.getBegin());
					if(slaveMedia.getDuration() == 0){
						isPossibleDefineMetBy = false;
						break;
					}else {
						removeMedia(slaveMedia);
						addMedia(slaveMedia);
					}

				}
				
				if(!isPossibleDefineMetBy){
					MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
							Language.translate("select.other.media.to.be.the.master"), "OK", 190);
					messageDialog.showAndWait();
				}
				
				break;
			
			case MET_BY_DELAY:
				
				Boolean isPossibleDefineMetByDelay = true;
				
				for(Media slaveMedia : synchronousRelation.getSlaveMediaList()){
					
					slaveMedia.setEnd(synchronousRelation.getMasterMedia().getBegin() + synchronousRelation.getDelay());
					Double begin = slaveMedia.getEnd() - slaveMedia.getDuration();
					if(begin > 0){
						slaveMedia.setBegin(begin);
					}else {
						slaveMedia.setBegin(0.0);
					}
					
					slaveMedia.setDuration(slaveMedia.getEnd() - slaveMedia.getBegin());
					if(slaveMedia.getDuration() == 0){
						isPossibleDefineMetByDelay = false;
						break;
					}else {
						removeMedia(slaveMedia);
						addMedia(slaveMedia);
					}

				}
				
				if(!isPossibleDefineMetByDelay){
					MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
							Language.translate("select.other.media.to.be.the.master"), "OK", 190);
					messageDialog.showAndWait();
				}
				
				break;
			
			case BEFORE:

				for(int i=0; i < synchronousRelation.getSlaveMediaList().size(); i++){
					
					Double begin;
					
					if(i == 0){
						begin = synchronousRelation.getMasterMedia().getEnd() + synchronousRelation.getDelay();
					}else {
						begin = synchronousRelation.getSlaveMediaList().get(i-1).getEnd() + synchronousRelation.getDelay();
					}
					
					Media slaveMedia = synchronousRelation.getSlaveMediaList().get(i);
					
					slaveMedia.setBegin(begin);
					slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
					removeMedia(slaveMedia);
					addMedia(slaveMedia);
					
				}
				
				break;
				
		}
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_SYNC_RELATION, synchronousRelation, this);
        notifyObservers(operation);
        
	}
	
	public String showBlockedRelationInputDialog(Media slaveMedia, Synchronous<Media> synchronousRelation){
		
		InputDialog showBlockedRelationInputDialog = new InputDialog("It's not possible to define this alignment for this media as slave",null, "cancel","ok", "Delay in seconds: ", 190);
    	System.out.println(showBlockedRelationInputDialog.showAndWaitAndReturn());
		return null;
		
	}
	
	public void removeSynchronousRelation(Synchronous<Media> synchronousRelation){
		
		relationList.remove(synchronousRelation);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_SYNC_RELATION, synchronousRelation, this);
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
