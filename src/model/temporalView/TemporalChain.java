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

		Boolean keepGoing = true;
		Boolean atLeastOneSlaveMediaWasDefined = false;
		
		for(int i  = 0; i < synchronousRelationToBeDefined.getSlaveMediaList().size(); i++){
			
			Media slaveMedia = synchronousRelationToBeDefined.getSlaveMediaList().get(i);
			
			Boolean slaveChanged = false;
			Boolean slaveBlockedForChanges = false;
			
			ArrayList<Relation> listOfAllRelations = getListOfAllRelations(slaveMedia);
			ArrayList<Relation> listOfSlaveRelations = getListOfSlaveRelations(slaveMedia);
			ArrayList<Relation> listOfMasterRelations = getListOfMasterRelations(slaveMedia);
			
			if(!listOfAllRelations.isEmpty()){
	
				if(listOfSlaveRelations.size() == 2){//INFO Uma mídia pode ser escrava de no máximo 2 relações
					
					keepGoing = showBlockedRelationInputDialog(slaveMedia, null);
					slaveBlockedForChanges = true;
					
				} else if(listOfSlaveRelations.size() == 1) { //INFO Apenas uma relação
					
					Synchronous<Media> synchronousRelationWhereSlaveMediaIsSlave = (Synchronous<Media>) listOfSlaveRelations.get(0);
					
					if(relationsDefineBeginOrEndSimultaneously(synchronousRelationToBeDefined, synchronousRelationWhereSlaveMediaIsSlave)){
						
						keepGoing = showBlockedRelationInputDialog(slaveMedia, synchronousRelationWhereSlaveMediaIsSlave);
						slaveBlockedForChanges = true;
						
					}else if(beginEndDefinedByNewRelationIsGreaterLessThanEndBeginDefinedByExistingRelation(synchronousRelationToBeDefined, synchronousRelationWhereSlaveMediaIsSlave, slaveMedia)){
						
						keepGoing = showBlockedRelationInputDialog(slaveMedia, synchronousRelationWhereSlaveMediaIsSlave);
						slaveBlockedForChanges = true;
						
					}else{
						
						defineRelationChangingDuration(synchronousRelationToBeDefined, i, slaveMedia);
						slaveChanged = true;
						atLeastOneSlaveMediaWasDefined = true;
						
					}
					
				}
	
				if(!slaveBlockedForChanges){
					
					if(!listOfMasterRelations.isEmpty()){
						
						if(slaveChanged){
							
							dragChildren(slaveMedia);
							
						}else {
							
							defineRelation(synchronousRelationToBeDefined, i, slaveMedia);
							atLeastOneSlaveMediaWasDefined = true;
							dragChildren(slaveMedia);

						}
						
					}
					
				}
	
				if(!keepGoing){
					break;
				}
				
			}else {
			
				defineRelation(synchronousRelationToBeDefined, i, slaveMedia);
				atLeastOneSlaveMediaWasDefined = true;
				
			}
			
		}
		
		if(keepGoing){
			
			if(!atLeastOneSlaveMediaWasDefined){
				
				MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
						Language.translate("no.selected.media.could.be.slave.for.the.relation"), "OK", 190);
				messageDialog.showAndWait();
				
			}else {
				relationList.add(synchronousRelationToBeDefined);
			}
			
		}else {
			MessageDialog messageDialog = new MessageDialog(Language.translate("no.alignment.was.defined"), 
					Language.translate("the.operation.was.canceled.by.the.user"), "OK", 150);
			messageDialog.showAndWait();
		}
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_SYNC_RELATION, synchronousRelationToBeDefined, this);
        notifyObservers(operation);
        
	}

	private void dragChildren(Media slaveMedia) {
		
		ArrayList<Relation> listOfMasterRelations = getListOfMasterRelations(slaveMedia);
		
		for(Relation relation : listOfMasterRelations){
			
			Synchronous<Media> synchronousRelation = (Synchronous<Media>) relation;
			
			for(int i = 0; i < synchronousRelation.getSlaveMediaList().size(); i++){
				
				Media slave = synchronousRelation.getSlaveMediaList().get(i);
				defineRelation(synchronousRelation, i, slave);
				
				dragChildren(slave);
				
			}
	
		}

	}

	private void defineRelationChangingDuration(Synchronous<Media> synchronousRelationToBeDefined, int i, Media slaveMedia) {
		
		switch(synchronousRelationToBeDefined.getType()){
		
			case STARTS:
				
				slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterMedia().getBegin());
				removeMedia(slaveMedia);
				addMedia(slaveMedia);
				break;
				
			case STARTS_DELAY:
				
				slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterMedia().getBegin() + synchronousRelationToBeDefined.getDelay());
				removeMedia(slaveMedia);
				addMedia(slaveMedia);
	
				break;
				
			case FINISHES:
	
				slaveMedia.setEnd(synchronousRelationToBeDefined.getMasterMedia().getEnd());
				slaveMedia.setDuration(slaveMedia.getEnd() - slaveMedia.getBegin());
				removeMedia(slaveMedia);
				addMedia(slaveMedia);
	
				break;
			
			case FINISHES_DELAY:
	
				slaveMedia.setEnd(synchronousRelationToBeDefined.getMasterMedia().getEnd() + synchronousRelationToBeDefined.getDelay());
				slaveMedia.setDuration(slaveMedia.getEnd() - slaveMedia.getBegin());
				removeMedia(slaveMedia);
				addMedia(slaveMedia);
	
				break;
				
			case MEETS:
	
				slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterMedia().getEnd());
				removeMedia(slaveMedia);
				addMedia(slaveMedia);
	
				break;
			
			case MEETS_DELAY:
	
				slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterMedia().getEnd() + synchronousRelationToBeDefined.getDelay());
				removeMedia(slaveMedia);
				addMedia(slaveMedia);
	
				break;
			
			case MET_BY:
				
				Boolean isPossibleDefineMetBy = true;
	
				slaveMedia.setEnd(synchronousRelationToBeDefined.getMasterMedia().getBegin());
				slaveMedia.setDuration(slaveMedia.getEnd() - slaveMedia.getBegin());
				
				if(slaveMedia.getDuration() == 0){
					isPossibleDefineMetBy = false;
					break;
				}else {
					removeMedia(slaveMedia);
					addMedia(slaveMedia);
				}
	
				if(!isPossibleDefineMetBy){
					MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
							Language.translate("select.other.media.to.be.the.master"), "OK", 190);
					messageDialog.showAndWait();
				}
				
				break;
			
			case MET_BY_DELAY:
				
				Boolean isPossibleDefineMetByDelay = true;
					
				slaveMedia.setEnd(synchronousRelationToBeDefined.getMasterMedia().getBegin() + synchronousRelationToBeDefined.getDelay());
				slaveMedia.setDuration(slaveMedia.getEnd() - slaveMedia.getBegin());
				
				if(slaveMedia.getDuration() == 0){
					isPossibleDefineMetByDelay = false;
					break;
				}else {
					removeMedia(slaveMedia);
					addMedia(slaveMedia);
				}
	
				if(!isPossibleDefineMetByDelay){
					MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
							Language.translate("select.other.media.to.be.the.master"), "OK", 190);
					messageDialog.showAndWait();
				}
				
				break;
			
			case BEFORE:
		
				Double beforeRelationBegin;
				
				if(i == 0){
					beforeRelationBegin = synchronousRelationToBeDefined.getMasterMedia().getEnd() + synchronousRelationToBeDefined.getDelay();
				}else {
					beforeRelationBegin = synchronousRelationToBeDefined.getSlaveMediaList().get(i-1).getEnd() + synchronousRelationToBeDefined.getDelay();
				}
	
				slaveMedia.setBegin(beforeRelationBegin);
				removeMedia(slaveMedia);
				addMedia(slaveMedia);
				
				break;
			
		}
		
	}

	private void defineRelation(Synchronous<Media> synchronousRelationToBeDefined, int i, Media slaveMedia) {
		
		switch(synchronousRelationToBeDefined.getType()){
		
			case STARTS:
				
				slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterMedia().getBegin());
				slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
				removeMedia(slaveMedia);
				addMedia(slaveMedia);
				
				break;
				
			case STARTS_DELAY:
				
				slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterMedia().getBegin() + synchronousRelationToBeDefined.getDelay());
				slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
				removeMedia(slaveMedia);
				addMedia(slaveMedia);

				break;
				
			case FINISHES:

				Double finishesRelationBegin = synchronousRelationToBeDefined.getMasterMedia().getEnd() - slaveMedia.getDuration();
				if(finishesRelationBegin > 0){
					slaveMedia.setBegin(finishesRelationBegin);
				}else {
					slaveMedia.setBegin(0.0);
				}
				
				slaveMedia.setEnd(synchronousRelationToBeDefined.getMasterMedia().getEnd());
				slaveMedia.setDuration(slaveMedia.getEnd() - slaveMedia.getBegin());
				removeMedia(slaveMedia);
				addMedia(slaveMedia);

				break;
			
			case FINISHES_DELAY:

				Double delayFinishesRelationBegin = synchronousRelationToBeDefined.getMasterMedia().getEnd() + synchronousRelationToBeDefined.getDelay() - slaveMedia.getDuration();
				if(delayFinishesRelationBegin > 0){
					slaveMedia.setBegin(delayFinishesRelationBegin);
				}else {
					slaveMedia.setBegin(0.0);
				}
				
				slaveMedia.setEnd(synchronousRelationToBeDefined.getMasterMedia().getEnd() + synchronousRelationToBeDefined.getDelay());
				slaveMedia.setDuration(slaveMedia.getEnd() - slaveMedia.getBegin());
				removeMedia(slaveMedia);
				addMedia(slaveMedia);

				break;
				
			case MEETS:

				slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterMedia().getEnd());
				slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
				removeMedia(slaveMedia);
				addMedia(slaveMedia);

				break;
			
			case MEETS_DELAY:

				slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterMedia().getEnd() + synchronousRelationToBeDefined.getDelay());
				slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
				removeMedia(slaveMedia);
				addMedia(slaveMedia);

				break;
			
			case MET_BY:
				
				Boolean isPossibleDefineMetBy = true;

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

				if(!isPossibleDefineMetBy){
					MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
							Language.translate("select.other.media.to.be.the.master"), "OK", 190);
					messageDialog.showAndWait();
				}
				
				break;
			
			case MET_BY_DELAY:
				
				Boolean isPossibleDefineMetByDelay = true;
					
				slaveMedia.setEnd(synchronousRelationToBeDefined.getMasterMedia().getBegin() + synchronousRelationToBeDefined.getDelay());
				Double metByDelayRelationBegin = slaveMedia.getEnd() - slaveMedia.getDuration();
				if(metByDelayRelationBegin > 0){
					slaveMedia.setBegin(metByDelayRelationBegin);
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

				if(!isPossibleDefineMetByDelay){
					MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
							Language.translate("select.other.media.to.be.the.master"), "OK", 190);
					messageDialog.showAndWait();
				}
				
				break;
			
			case BEFORE:

				Double beforeRelationBegin;
				
				if(i == 0){
					beforeRelationBegin = synchronousRelationToBeDefined.getMasterMedia().getEnd() + synchronousRelationToBeDefined.getDelay();
				}else {
					beforeRelationBegin = synchronousRelationToBeDefined.getSlaveMediaList().get(i-1).getEnd() + synchronousRelationToBeDefined.getDelay();
				}

				slaveMedia.setBegin(beforeRelationBegin);
				slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
				removeMedia(slaveMedia);
				addMedia(slaveMedia);
				
				break;
		}
		
	}
	
	/** If the begin/end defined by the new relation is greater/less than the end/begin defined by the existing relation.
	 * 
	 * @param synchronousRelationToBeDefined				The new relation which is being defined.
	 * @param synchronousRelationWhereSlaveMediaIsSlave		The relations where the media selected to be slave belongs to.
	 * @return true If the begin/end defined by the new relation is greater/less than the end/begin defined by the existing relation; false otherwise.
	 */
	private boolean beginEndDefinedByNewRelationIsGreaterLessThanEndBeginDefinedByExistingRelation( Synchronous<Media> synchronousRelationToBeDefined, 
			Synchronous<Media> synchronousRelationWhereSlaveMediaIsSlave, Media newRelationSlaveMedia) {
		
		if(relationDefinesBegin(synchronousRelationToBeDefined)){
			
			Double newRelationBegin = 0.0;
			Double existingRelationEnd = 0.0;
			RelationType newRelationType = synchronousRelationToBeDefined.getType();
			RelationType existingRelationType = synchronousRelationWhereSlaveMediaIsSlave.getType();
			
			switch (newRelationType) {
			
				case STARTS:
					newRelationBegin = synchronousRelationToBeDefined.getMasterMedia().getBegin();
					break;
	
				case STARTS_DELAY:
					newRelationBegin = synchronousRelationToBeDefined.getMasterMedia().getBegin() + synchronousRelationToBeDefined.getDelay();
					break;
					
				case MEETS:
					newRelationBegin = synchronousRelationToBeDefined.getMasterMedia().getEnd();
					break;
					
				case MEETS_DELAY:
					newRelationBegin = synchronousRelationToBeDefined.getMasterMedia().getEnd() + synchronousRelationToBeDefined.getDelay();
					break;
					
				case BEFORE:
					 
					ArrayList<Media> auxSlaveMediaList = new ArrayList<Media>(synchronousRelationToBeDefined.getSlaveMediaList());
					Double begin;
					
					for(int i=0; i < auxSlaveMediaList.size(); i++){
		
						Media auxSlaveMedia = auxSlaveMediaList.get(i);
						
						if(i == 0){
							begin = synchronousRelationToBeDefined.getMasterMedia().getEnd() + synchronousRelationToBeDefined.getDelay();
							if(auxSlaveMedia == newRelationSlaveMedia){
								newRelationBegin = begin;
								break;
							}
						}else {
							begin = synchronousRelationToBeDefined.getSlaveMediaList().get(i-1).getEnd() + synchronousRelationToBeDefined.getDelay();
							if(auxSlaveMedia == newRelationSlaveMedia){
								newRelationBegin = begin;
								break;
							}
						}
						
						auxSlaveMedia.setBegin(begin);
						auxSlaveMedia.setEnd(auxSlaveMedia.getBegin() + auxSlaveMedia.getDuration());
						
					}
					
					break;
				
			}
			
			switch (existingRelationType) {
			
				case FINISHES:
					existingRelationEnd = synchronousRelationWhereSlaveMediaIsSlave.getMasterMedia().getEnd();
					break;
	
				case FINISHES_DELAY:
					existingRelationEnd = synchronousRelationWhereSlaveMediaIsSlave.getMasterMedia().getEnd() + synchronousRelationWhereSlaveMediaIsSlave.getDelay();
					break;
					
				case MET_BY:
					existingRelationEnd = synchronousRelationWhereSlaveMediaIsSlave.getMasterMedia().getBegin();
					break;
					
				case MET_BY_DELAY:
					existingRelationEnd = synchronousRelationWhereSlaveMediaIsSlave.getMasterMedia().getBegin() + synchronousRelationWhereSlaveMediaIsSlave.getDelay();
					break;
			
			}

			if(newRelationBegin > existingRelationEnd){
				return true;
			}else {
				return false;
			}
			
		}else {
			
			Double newRelationEnd = 0.0;
			Double existingRelationBegin = 0.0;
			RelationType newRelationType = synchronousRelationToBeDefined.getType();
			RelationType existingRelationType = synchronousRelationWhereSlaveMediaIsSlave.getType();
			
			switch (newRelationType) {
			
				case FINISHES:
					newRelationEnd = synchronousRelationToBeDefined.getMasterMedia().getEnd();
					break;
	
				case FINISHES_DELAY:
					newRelationEnd = synchronousRelationToBeDefined.getMasterMedia().getEnd() + synchronousRelationToBeDefined.getDelay();
					break;
					
				case MET_BY:
					newRelationEnd = synchronousRelationToBeDefined.getMasterMedia().getBegin();
					break;
					
				case MET_BY_DELAY:
					newRelationEnd = synchronousRelationToBeDefined.getMasterMedia().getBegin() + synchronousRelationToBeDefined.getDelay();
					break;
				
			}
			
			switch (existingRelationType) {
			
				case STARTS:
					existingRelationBegin = synchronousRelationWhereSlaveMediaIsSlave.getMasterMedia().getBegin();
					break;
	
				case STARTS_DELAY:
					existingRelationBegin = synchronousRelationWhereSlaveMediaIsSlave.getMasterMedia().getBegin() + synchronousRelationWhereSlaveMediaIsSlave.getDelay();
					break;
					
				case MEETS:
					existingRelationBegin = synchronousRelationWhereSlaveMediaIsSlave.getMasterMedia().getEnd();
					break;
					
				case MEETS_DELAY:
					existingRelationBegin = synchronousRelationWhereSlaveMediaIsSlave.getMasterMedia().getEnd() + synchronousRelationWhereSlaveMediaIsSlave.getDelay();
					break;
					
				case BEFORE:
					 
					ArrayList<Media> auxSlaveMediaList = new ArrayList<Media>(synchronousRelationWhereSlaveMediaIsSlave.getSlaveMediaList());
					Double begin;
					
					for(int i=0; i < auxSlaveMediaList.size(); i++){
		
						Media auxSlaveMedia = auxSlaveMediaList.get(i);
						
						if(i == 0){
							begin = synchronousRelationWhereSlaveMediaIsSlave.getMasterMedia().getEnd() + synchronousRelationWhereSlaveMediaIsSlave.getDelay();
							if(auxSlaveMedia == newRelationSlaveMedia){
								existingRelationBegin = begin;
								break;
							}
						}else {
							begin = synchronousRelationWhereSlaveMediaIsSlave.getSlaveMediaList().get(i-1).getEnd() + synchronousRelationWhereSlaveMediaIsSlave.getDelay();
							if(auxSlaveMedia == newRelationSlaveMedia){
								existingRelationBegin = begin;
								break;
							}
						}
						
						auxSlaveMedia.setBegin(begin);
						auxSlaveMedia.setEnd(auxSlaveMedia.getBegin() + auxSlaveMedia.getDuration());
						
					}
					
					break;
			
			}
			
			if(newRelationEnd < existingRelationBegin){
				return true;
			}else {
				return false;
			}
			
		}
	
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
	
	/** If the relation to be defined specifies the same of the existing relation.
	 * 
	 * @param synchronousRelationToBeDefined				The new relation which is being defined.
	 * @param synchronousRelationWhereSlaveMediaIsSlave		The relations where the media selected to be slave belongs to.
	 * @return true if the relations define the same; false otherwise.
	 */
	private boolean relationsDefineBeginOrEndSimultaneously(Synchronous<Media> synchronousRelationToBeDefined, Synchronous<Media> synchronousRelationWhereSlaveMediaIsSlave) {

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

	public Boolean showBlockedRelationInputDialog(Media slaveMedia, Synchronous<Media> conflictingRelation){
		
		InputDialog showBlockedRelationInputDialog;
		
		if(conflictingRelation != null){
			showBlockedRelationInputDialog = new InputDialog(Language.translate("it.is.not.possible.to.define.alignment") + ": " + slaveMedia.getName(), 
					Language.translate("conflicting.alignment") + conflictingRelation.getType().getDescription() + ". " +
					Language.translate("would.you.like.to.continue.defining.alignment"), "yes","no", null, 220);
		}else {
			
			showBlockedRelationInputDialog = new InputDialog(Language.translate("it.is.not.possible.to.define.alignment") + ": " + slaveMedia.getName(), 
					Language.translate("would.you.like.to.continue.defining.alignment"), "yes","no", null, 210);
			
		}
		
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
