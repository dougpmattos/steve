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

	public void addSynchronousRelation(Synchronous<Media> synchronousRelationToBeDefined){
		
		switch(synchronousRelationToBeDefined.getType()){
			
			case STARTS:
	
				Boolean keepGoing = true;
				
				for(Media slaveMedia : synchronousRelationToBeDefined.getSlaveMediaList()){
					
					Boolean slaveChanged = false;
					Boolean slaveBlockedForChanges = false;
					
					ArrayList<Relation> listOfAllRelations = getListOfAllRelations(slaveMedia);
					ArrayList<Relation> listOfSlaveRelations = getListOfSlaveRelations(slaveMedia);
					ArrayList<Relation> listOfMasterRelations = getListOfMasterRelations(slaveMedia);
					
					if(!listOfAllRelations.isEmpty()){

						if(listOfSlaveRelations.size() == 2){//Uma mídia pode ser escrava de no máximo 2 relações
							
							keepGoing = showBlockedRelationInputDialog(slaveMedia);
							slaveBlockedForChanges = true;
							
						} else if(listOfSlaveRelations.size() == 1) { //Apenas uma relação
							
							Synchronous<Media> synchronousRelationWhereSlaveMediaIsSlave = (Synchronous<Media>) listOfSlaveRelations.get(0);
							
							if(relationsDefineBeginOrEndSimultaneously(synchronousRelationToBeDefined, synchronousRelationWhereSlaveMediaIsSlave)){
								
								keepGoing = showBlockedRelationInputDialog(slaveMedia);
								slaveBlockedForChanges = true;
								
							}else if(Senão Se o inicio/fim definido pela nova relação é maior/menor que fim/início definido pela relação já existente){
								
								keepGoing = showBlockedRelationInputDialog(slaveMedia, currentSynchronousRelation);
								slaveBlockedForChanges = true;
								
							}else{
								Define o inicio/fim aumentando ou dimuindo a duração da mídia escrava
								slaveChanged = true;
							}
							
						}
			
						if(!slaveBlockedForChanges){
							
							for(Relation<Media> masterRelation : listOfMasterRelations){
								
								if(slaveChanged){
									arratsr filhos recursivamete
								}else {
									
									slaveMedia.setBegin(currentSynchronousRelation.getMasterMedia().getBegin());
									slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
									removeMedia(slaveMedia);
									addMedia(slaveMedia);
									
									arratsr filhos recursivamete
								}
								
							}
							
						}

						if(!keepGoing){
							break;
						}
						
					}else {
						
						slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterMedia().getBegin());
						slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
						removeMedia(slaveMedia);
						addMedia(slaveMedia);
						
					}
					
				}
				
				if(keepGoing){
					
					if(nenhumaEscravaDefinida){
						
						MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
								Language.translate("no.selected.media.could.be.slave.for.the.relation"), "OK", 190);
						messageDialog.showAndWait();
						
					}else {
						relationList.add(synchronousRelationToBeDefined);
					}
					
				}else {
					MessageDialog messageDialog = new MessageDialog(Language.translate("no.alignment.was.defined"), 
							Language.translate("the.operation.was.canceled.by.the.user"), "OK", 190);
					messageDialog.showAndWait();
				}

				break;
	            
			case STARTS_DELAY:
				
				for(Media slaveMedia : synchronousRelationToBeDefined.getSlaveMediaList()){
					
					slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterMedia().getBegin() + synchronousRelationToBeDefined.getDelay());
					slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
					removeMedia(slaveMedia);
					addMedia(slaveMedia);
					
				}
				
				break;
				
			case FINISHES:
				
				for(Media slaveMedia : synchronousRelationToBeDefined.getSlaveMediaList()){
					
					Double begin = synchronousRelationToBeDefined.getMasterMedia().getEnd() - slaveMedia.getDuration();
					if(begin > 0){
						slaveMedia.setBegin(begin);
					}else {
						slaveMedia.setBegin(0.0);
					}
					
					slaveMedia.setEnd(synchronousRelationToBeDefined.getMasterMedia().getEnd());
					slaveMedia.setDuration(slaveMedia.getEnd() - slaveMedia.getBegin());
					removeMedia(slaveMedia);
					addMedia(slaveMedia);
					
				}
				
				break;
			
			case FINISHES_DELAY:
				
				for(Media slaveMedia : synchronousRelationToBeDefined.getSlaveMediaList()){
					
					Double begin = synchronousRelationToBeDefined.getMasterMedia().getEnd() + synchronousRelationToBeDefined.getDelay() - slaveMedia.getDuration();
					if(begin > 0){
						slaveMedia.setBegin(begin);
					}else {
						slaveMedia.setBegin(0.0);
					}
					
					slaveMedia.setEnd(synchronousRelationToBeDefined.getMasterMedia().getEnd() + synchronousRelationToBeDefined.getDelay());
					slaveMedia.setDuration(slaveMedia.getEnd() - slaveMedia.getBegin());
					removeMedia(slaveMedia);
					addMedia(slaveMedia);
					
				}
				
				break;
				
			case MEETS:
				
				for(Media slaveMedia : synchronousRelationToBeDefined.getSlaveMediaList()){
					
					slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterMedia().getEnd());
					slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
					removeMedia(slaveMedia);
					addMedia(slaveMedia);
					
				}
				
				break;
			
			case MEETS_DELAY:
				
				for(Media slaveMedia : synchronousRelationToBeDefined.getSlaveMediaList()){
					
					slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterMedia().getEnd() + synchronousRelationToBeDefined.getDelay());
					slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
					removeMedia(slaveMedia);
					addMedia(slaveMedia);
					
				}
				
				break;
			
			case MET_BY:
				
				Boolean isPossibleDefineMetBy = true;
				
				for(Media slaveMedia : synchronousRelationToBeDefined.getSlaveMediaList()){
					
					slaveMedia.setEnd(synchronousRelationToBeDefined.getMasterMedia().getBegin());
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
				
				for(Media slaveMedia : synchronousRelationToBeDefined.getSlaveMediaList()){
					
					slaveMedia.setEnd(synchronousRelationToBeDefined.getMasterMedia().getBegin() + synchronousRelationToBeDefined.getDelay());
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

				for(int i=0; i < synchronousRelationToBeDefined.getSlaveMediaList().size(); i++){
					
					Double begin;
					
					if(i == 0){
						begin = synchronousRelationToBeDefined.getMasterMedia().getEnd() + synchronousRelationToBeDefined.getDelay();
					}else {
						begin = synchronousRelationToBeDefined.getSlaveMediaList().get(i-1).getEnd() + synchronousRelationToBeDefined.getDelay();
					}
					
					Media slaveMedia = synchronousRelationToBeDefined.getSlaveMediaList().get(i);
					
					slaveMedia.setBegin(begin);
					slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
					removeMedia(slaveMedia);
					addMedia(slaveMedia);
					
				}
				
				break;
				
		}
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_SYNC_RELATION, synchronousRelationToBeDefined, this);
        notifyObservers(operation);
        
	}
	
	private boolean relationDefinesEnd(Synchronous<Media> synchronousRelation){
		
		RelationType relationType = synchronousRelation.getType();
		
		if(relationType == RelationType.FINISHES || relationType == RelationType.FINISHES_DELAY 
		   ||relationType == RelationType.MET_BY || relationType == RelationType.MET_BY_DELAY){
			
			return true;
			
		}else {
			return false;
		}
		
	}

	private boolean relationDefinesBegin(Synchronous<Media> synchronousRelation){
		
		RelationType relationType = synchronousRelation.getType();
		
		if(relationType == RelationType.STARTS || relationType == RelationType.STARTS_DELAY ||relationType == RelationType.MEETS
		   || relationType == RelationType.MEETS_DELAY || relationType == RelationType.BEFORE){
			
			return true;
			
		}else {
			return false;
		}
		
	}
	
	private boolean relationsDefineBeginOrEndSimultaneously(Synchronous<Media> synchronousRelationToBeDefined, Synchronous<Media> synchronousRelationWhereSlaveMediaIsSlave) {
		
		//INFO Se  relação que está sendo criada especifica o mesmo que a relação já existente (início-inicio ou fim-fim)
		if(relationDefinesBegin(synchronousRelationToBeDefined) && relationDefinesBegin(synchronousRelationWhereSlaveMediaIsSlave)
		   || relationDefinesEnd(synchronousRelationToBeDefined) && relationDefinesEnd(synchronousRelationWhereSlaveMediaIsSlave)){
			
			return true;
			
		}else {
			return false;
		}
		
	}

	private ArrayList<Relation> getListOfMasterRelations(Media slaveMedia) {
		
		ArrayList<Relation> listOfMasterRelations = new ArrayList<Relation>(); 
		
		for(Relation<Media> relation : relationList){
			
			Synchronous<Media> synchronousRelation = (Synchronous<Media>) relation;
			
			if(slaveMedia.equals(synchronousRelation.getMasterMedia())){
				listOfMasterRelations.add(synchronousRelation);
			}

		}
		
		return listOfMasterRelations;
		
	}

	private ArrayList<Relation> getListOfSlaveRelations(Media slaveMedia) {
		
		ArrayList<Relation> listOfSlaveRelations = new ArrayList<Relation>(); 
		
		for(Relation<Media> relation : relationList){
			
			Synchronous<Media> synchronousRelation = (Synchronous<Media>) relation;
			
			for(Media synchronousRelationSlave : synchronousRelation.getSlaveMediaList()){
					
				if(slaveMedia.equals(synchronousRelationSlave)){
					listOfSlaveRelations.add(synchronousRelation);
					break;
				}
					
			}

		}
		
		return listOfSlaveRelations;
		
	}

	private ArrayList<Relation> getListOfAllRelations(Media slaveMedia) {
		
		ArrayList<Relation> listOfAllRelations = new ArrayList<Relation>(); 
		
		for(Relation<Media> relation : relationList){
			
			Synchronous<Media> synchronousRelation = (Synchronous<Media>) relation;
			
			if(slaveMedia.equals(synchronousRelation.getMasterMedia())){
				listOfAllRelations.add(synchronousRelation);
			}else {
				
				for(Media synchronousRelationSlave : synchronousRelation.getSlaveMediaList()){
					
					if(slaveMedia.equals(synchronousRelationSlave)){
						listOfAllRelations.add(synchronousRelation);
						break;
					}
					
				}
				
			}

		}
		
		return listOfAllRelations;
		
	}

	public Boolean showBlockedRelationInputDialog(Media slaveMedia){
		
		InputDialog showBlockedRelationInputDialog = new InputDialog("It's not possible to define this alignment for this media as slave","Would you like to continue defining this alignment?", "yes","no", null, 190);
		String answer = showBlockedRelationInputDialog.showAndWaitAndReturn();
		if(answer.equalsIgnoreCase("left")){
    		return true;
    	}else {
    		return false;
    	}
		
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
