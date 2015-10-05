package model.temporalView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import model.common.Media;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.Operation;
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
    			
    			//INFO Caso use a representação de Meets que coloca a primeira mídia escrava na mesma linha após a mídia mestre da relação.
//    			if ( !((allenRelation.equals(AllenRelation.MEETS)) || (allenRelation.equals(AllenRelation.MET_BY))
//    			|| (allenRelation.equals(AllenRelation.BEFORE)) || (allenRelation.equals(AllenRelation.AFTER)))){
    			
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
		
		//INFO Após definir a relação, é necessário redefinir o início, fim e duração das mídias
		//para permitir dispô-las na time line. Porém, o que define a ordem de apresentação no modelo do editor
		//são as relações, pois o editor se baseia em eventos e não utiliza o paradigma timeline na autoria.
		
		relationList.add(synchronousRelation);
		
		switch(synchronousRelation.getType()){
		
			case STARTS:
	
				for(Media slaveMedia : synchronousRelation.getSlaveMediaList()){
					
					slaveMedia.setBegin(synchronousRelation.getMasterMedia().getBegin());
					slaveMedia.setEnd(synchronousRelation.getMasterMedia().getBegin() + slaveMedia.getDuration());
					removeMedia(slaveMedia);
					addMedia(slaveMedia);
					
				}
				
				break;
	            
			case STARTS_DELAY:
				//TODO
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
			
			case MEETS:
				
				for(Media slaveMedia : synchronousRelation.getSlaveMediaList()){
					
					slaveMedia.setBegin(synchronousRelation.getMasterMedia().getEnd());
					slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
					removeMedia(slaveMedia);
					addMedia(slaveMedia);
					
				}
				
				break;
			
			case MEETS_DELAY:
				//TODO
				break;
			
			case MET_BY:
				
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
						new MessageDialog("Não é possível definir a relação com esta mídia mestre.", MessageDialog.ICON_INFO).showAndWait();
					}else {
						removeMedia(slaveMedia);
						addMedia(slaveMedia);
					}
		
				}
				
				break;
			
			case MET_BY_DELAY:
				//TODO
				break;
			
			case BEFORE:
				//TODO
				break;
	
	    	default:
	    	
	    		break;
    		
		}
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_SYNC_RELATION, synchronousRelation, this);
        notifyObservers(operation);
        
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
