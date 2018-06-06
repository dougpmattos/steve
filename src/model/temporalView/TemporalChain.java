package model.temporalView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import model.common.Media;
import model.common.Node;
import model.temporalView.enums.AllenRelation;
import model.temporalView.enums.ConflictType;
import model.temporalView.enums.TemporalRelationType;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.Operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import view.common.Language;
import view.common.MessageDialog;
import view.temporalViewPane.TemporalChainPane;
import view.temporalViewPane.TimeLineXYChartData;

@SuppressWarnings("rawtypes")
public class TemporalChain extends Observable implements Serializable {

	private static final long serialVersionUID = -6154510036093880684L;
	final Logger logger = LoggerFactory.getLogger(TemporalChain.class);
	
	private static int temporalChainNumber = 0;
	private static int temporalViewMediaNumber = 0;
	
	private int id;
	private String name;
	private Media masterMedia;
	private ArrayList<Media> mediaAllList = new ArrayList<Media>();
	private ArrayList<TemporalRelation> relationList = new ArrayList<TemporalRelation>();
	private ArrayList<ArrayList<Media>> mediaLineList = new ArrayList<ArrayList<Media>>();
	

	public TemporalChain(String name) {
		
		this.id = temporalChainNumber;
		temporalChainNumber++;
		
		this.name = name;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setMasterMedia(Media masterMedia){
		
		this.masterMedia = masterMedia;
		addMedia(masterMedia);
		
	}
	
	public Media getMasterMedia() {
		return masterMedia;
	}
	
	public void addMedia(Media media){

		mediaAllList.add(media);
		int line = addMediaLineList(media);
		
		if(masterMedia == null){
			masterMedia = media;
		} else if(masterMedia.getBegin() != 0){
			masterMedia = getMediaWithLowestBegin();
		}
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_TEMPORAL_CHAIN_MEDIA, media, line);
        notifyObservers(operation);
        
        temporalViewMediaNumber++;
        
	}
	
	public void removeMedia(Media media, Boolean isDeleteButton){
		
		mediaAllList.remove(media);
		int line = removeMediaLineList(media);
		
		if(masterMedia == media){
			masterMedia = getMediaWithLowestBegin();
		}
		
		if(isDeleteButton){
			removeMediaOfRelations(media);
		}

		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.REMOVE_TEMPORAL_CHAIN_MEDIA, media, line);
        notifyObservers(operation);
        
        temporalViewMediaNumber--;
        
	}
	
	public void dragMedia(TemporalChain temporalChain, Media media, Double droppedTime) {
		
		ArrayList<Media> rootMediaList = new ArrayList<Media>();
		
		addElementsInRootMediaList(media, rootMediaList);
    		
		if(rootMediaList.get(0) == media){
			
			media.setBegin(droppedTime);
	    	media.setEnd(droppedTime + media.getDuration());
	    	
	    	removeMedia(media, false);
	    	addMedia(media);
	    	
	    	dragChildren(media);
			
		}else {
			
			Double draggedTime = droppedTime - media.getBegin();
			
			for(Media rootMedia : rootMediaList){
				
				rootMedia.setBegin(rootMedia.getBegin() + draggedTime);
				rootMedia.setEnd(rootMedia.getEnd() + draggedTime);

				removeMedia(rootMedia, false);
				addMedia(rootMedia);
				
				dragChildren(rootMedia);
				
			}
			
		}
		
	}
	
	private void addElementsInRootMediaList(Media media, ArrayList<Media> rootMediaList){
		
		ArrayList<TemporalRelation> listOfSalveRelations = getListOfSlaveRelations(media);
		
		if(!listOfSalveRelations.isEmpty()){
			
			
			for(TemporalRelation relation : listOfSalveRelations){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				Media masterMedia = (Media) synchronousRelation.getMasterNode();
				
				addElementsInRootMediaList(masterMedia, rootMediaList);
				
			}
			
		}else {
			
			rootMediaList.add(media);
			
		}

	}

	private int getLineToAddMedia(Media media){
		
		AllenRelation allenRelation;
    	int line = 0;
    	
    	while(line < mediaLineList.size()){
    		
    		boolean isPossibleAdd = true;
    		ArrayList<Media> mediaList = mediaLineList.get(line);
    		int index = 0;
    		
    		while(isPossibleAdd && index < mediaList.size()){
    			
    			Media currentMedia = mediaList.get(index);
    			allenRelation = identifyAllenRelation(media, currentMedia);
    			
    			if(media != currentMedia){
    				
    				if (!(allenRelation.equals(AllenRelation.BEFORE) || (allenRelation.equals(AllenRelation.AFTER)))){
        				
        				isPossibleAdd = false;
        				
        			}
    				
    			}
    			
    			index++;
    			
    		}
    		
    		if(isPossibleAdd){

    			return line;

    		}
    		
    		line++;
    	}

    	int newLineIndex = mediaLineList.size();

    	return newLineIndex;
		
	}
	
	private Media getMediaWithLowestBegin() {
		
		Media mediaWithLowestBegin = null;
		
		if(!mediaAllList.isEmpty()){
			mediaWithLowestBegin = mediaAllList.get(0);
		}
		
		for(int i=1; i < mediaAllList.size(); i++){
			
			Media media = mediaAllList.get(i);
			
			if(media.getBegin() < mediaWithLowestBegin.getBegin()){
				mediaWithLowestBegin = media;
			}
			
		}
		
		return mediaWithLowestBegin;
		
	}
	public Media getMediaWithHighestEnd() {
		
		Media mediaWithHighestEnd = null;
		
		if(!mediaAllList.isEmpty()){
			mediaWithHighestEnd = mediaAllList.get(mediaAllList.size()-1);
		}
		
		for(Media media : mediaAllList){
			
			if(media.getEnd() > mediaWithHighestEnd.getEnd()){
				mediaWithHighestEnd = media;
			}
			
		}
		
		return mediaWithHighestEnd;
		
	}


	private void removeMediaOfRelations(Media media) {
		
		for(int i=0; i < relationList.size(); i++){
			
			TemporalRelation relation = relationList.get(i);
			
			if(relation instanceof Synchronous){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				if(synchronousRelation.getMasterNode() == media){
					removeSynchronousRelation(synchronousRelation);
				}else {
					synchronousRelation.removeSlaveNode(media);
					if(synchronousRelation.getSlaveNodeList().isEmpty()){
						removeSynchronousRelation(synchronousRelation);
					}
				}
				
			}else if(relation instanceof Interactivity){
				
				Interactivity<Media> interactivityRelation = (Interactivity<Media>) relation;
				if(interactivityRelation.getMasterNode() == media){
					removeInteractivityRelation(interactivityRelation);
				}else {
					interactivityRelation.removeSlaveNode(media);
					if(interactivityRelation.getSlaveNodeList().isEmpty() && interactivityRelation.getTemporalChainList().isEmpty()){
						removeInteractivityRelation(interactivityRelation);
					}
				}
				
			}
			
		}
		
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
	
	public AllenRelation identifyAllenRelation(Media media, Media currentMedia) {
		
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

	public void addSynchronousRelation(Synchronous synchronousRelationToBeDefined){

		Boolean atLeastOneSlaveMediaWasDefined = false;
		int i;
		
		for(i  = 0; i < synchronousRelationToBeDefined.getSlaveNodeList().size(); i++){
			
			Media slaveMedia = (Media) synchronousRelationToBeDefined.getSlaveNodeList().get(i);
			
			Boolean slaveWasChangedModifyingDuration = false;
			Boolean slaveBlockedForChanges = false;
			
			ArrayList<TemporalRelation> listOfAllRelations = getListOfAllRelations(slaveMedia);
			ArrayList<TemporalRelation> listOfSlaveRelations = getListOfSlaveRelations(slaveMedia);
			ArrayList<TemporalRelation> listOfMasterRelations = getListOfMasterRelations(slaveMedia);
			
			if(!listOfAllRelations.isEmpty()){
	
				if(listOfSlaveRelations.size() == 2){//INFO Uma mídia pode ser escrava de no máximo 2 relações
					
					showBlockedRelationMessageDialog(slaveMedia, ConflictType.BEGIN_END_DEFINED);
					slaveBlockedForChanges = true;
					
				} else if(listOfSlaveRelations.size() == 1) { //INFO Apenas uma relação
					
					ConflictType conflictType;
					
					Synchronous synchronousRelationWhereSlaveMediaIsSlave = (Synchronous) listOfSlaveRelations.get(0);
					
					conflictType = hasBeginOrEndAlreadyBeenDefined(synchronousRelationToBeDefined, synchronousRelationWhereSlaveMediaIsSlave);
					
					if(conflictType != null){
						
						showBlockedRelationMessageDialog(slaveMedia, conflictType);
						slaveBlockedForChanges = true;
						
					}else {
						
						conflictType = isThereBeginGTEndOrEndLTBeginConflict(synchronousRelationToBeDefined, synchronousRelationWhereSlaveMediaIsSlave, slaveMedia);
						
						if(conflictType != null){
							
							showBlockedRelationMessageDialog(slaveMedia, conflictType);
							slaveBlockedForChanges = true;
							
						}else {
							
							defineRelationChangingDuration(synchronousRelationToBeDefined, i, slaveMedia);
							slaveWasChangedModifyingDuration = true;
							atLeastOneSlaveMediaWasDefined = true;
							
						}
						
					}
					
				}
	
				if(!slaveBlockedForChanges){
					
					if(!listOfMasterRelations.isEmpty()){
						
						if(slaveWasChangedModifyingDuration){
							
							dragChildren(slaveMedia);
							
						}else {
							
							defineRelation(synchronousRelationToBeDefined, i, slaveMedia);
							atLeastOneSlaveMediaWasDefined = true;
							dragChildren(slaveMedia);

						}
						
					}
					
				}else {
					synchronousRelationToBeDefined.getSlaveNodeList().remove(slaveMedia);
				}
				
			}else {
			
				defineRelation(synchronousRelationToBeDefined, i, slaveMedia);
				atLeastOneSlaveMediaWasDefined = true;
				
			}
			
		}
			
		if(!atLeastOneSlaveMediaWasDefined){
			
			MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
					Language.translate("no.selected.media.could.be.slave.for.the.relation"), "OK", 140);
			messageDialog.showAndWait();
			
		}else {
			relationList.add(synchronousRelationToBeDefined);
		}
	
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_SYNC_RELATION, synchronousRelationToBeDefined, this);
        notifyObservers(operation);
        
	}

	private void dragChildren(Media rootMedia) {
		
		ArrayList<TemporalRelation> listOfMasterRelations = getListOfMasterRelations(rootMedia);
		
		for(TemporalRelation relation : listOfMasterRelations){
			
			Synchronous synchronousRelation = (Synchronous) relation;
			
			for(int i = 0; i < synchronousRelation.getSlaveNodeList().size(); i++){
				
				Media slave = (Media) synchronousRelation.getSlaveNodeList().get(i);
				defineRelation(synchronousRelation, i, slave);
				
				dragChildren(slave);
				
			}
	
		}

	}

	private void defineRelationChangingDuration(Synchronous synchronousRelationToBeDefined, int i, Media slaveMedia) {
		
		switch(synchronousRelationToBeDefined.getType()){
		
			case STARTS:
				
				slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterNode().getBegin());
				removeMedia(slaveMedia, false);
				addMedia(slaveMedia);
				break;
				
			case STARTS_DELAY:
				
				slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterNode().getBegin() + synchronousRelationToBeDefined.getDelay());
				removeMedia(slaveMedia, false);
				addMedia(slaveMedia);
	
				break;
				
			case FINISHES:
	
				slaveMedia.setEnd(synchronousRelationToBeDefined.getMasterNode().getEnd());
				slaveMedia.setDuration(slaveMedia.getEnd() - slaveMedia.getBegin());
				removeMedia(slaveMedia, false);
				addMedia(slaveMedia);
	
				break;
			
			case FINISHES_DELAY:
	
				slaveMedia.setEnd(synchronousRelationToBeDefined.getMasterNode().getEnd() + synchronousRelationToBeDefined.getDelay());
				slaveMedia.setDuration(slaveMedia.getEnd() - slaveMedia.getBegin());
				removeMedia(slaveMedia, false);
				addMedia(slaveMedia);
	
				break;
				
			case MEETS:
	
				slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterNode().getEnd());
				removeMedia(slaveMedia, false);
				addMedia(slaveMedia);
	
				break;
			
			case MEETS_DELAY:
	
				slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterNode().getEnd() + synchronousRelationToBeDefined.getDelay());
				removeMedia(slaveMedia, false);
				addMedia(slaveMedia);
	
				break;
			
			case MET_BY:
				
				Boolean isPossibleDefineMetBy = true;
	
				slaveMedia.setEnd(synchronousRelationToBeDefined.getMasterNode().getBegin());
				slaveMedia.setDuration(slaveMedia.getEnd() - slaveMedia.getBegin());
				
				if(slaveMedia.getDuration() == 0){
					isPossibleDefineMetBy = false;
					break;
				}else {
					removeMedia(slaveMedia, false);
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
					
				slaveMedia.setEnd(synchronousRelationToBeDefined.getMasterNode().getBegin() + synchronousRelationToBeDefined.getDelay());
				slaveMedia.setDuration(slaveMedia.getEnd() - slaveMedia.getBegin());
				
				if(slaveMedia.getDuration() == 0){
					isPossibleDefineMetByDelay = false;
					break;
				}else {
					removeMedia(slaveMedia, false);
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
					beforeRelationBegin = synchronousRelationToBeDefined.getMasterNode().getEnd() + synchronousRelationToBeDefined.getDelay();
				}else {
					beforeRelationBegin = synchronousRelationToBeDefined.getSlaveNodeList().get(i-1).getEnd() + synchronousRelationToBeDefined.getDelay();
				}
	
				slaveMedia.setBegin(beforeRelationBegin);
				removeMedia(slaveMedia, false);
				addMedia(slaveMedia);
				
				break;
			
		}
		
	}

	//INFO Após passar pela validação das relações (a relação é adicionada de fato no modelo), seta o início e fim das mídias visualmente na cadeia utilizando inicio/fim das mídia mestre e delay conforme o tipo de relação.
	private void defineRelation(Synchronous synchronousRelationToBeDefined, int i, Media slaveMedia) {
		
		switch(synchronousRelationToBeDefined.getType()){
		
			case STARTS:
				
				slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterNode().getBegin());
				slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
				removeMedia(slaveMedia, false);
				addMedia(slaveMedia);
				
				break;
				
			case STARTS_DELAY:
				
				slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterNode().getBegin() + synchronousRelationToBeDefined.getDelay());
				slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
				removeMedia(slaveMedia, false);
				addMedia(slaveMedia);

				break;
				
			case FINISHES:

				Double finishesRelationBegin = synchronousRelationToBeDefined.getMasterNode().getEnd() - slaveMedia.getDuration();
				if(finishesRelationBegin > 0){
					slaveMedia.setBegin(finishesRelationBegin);
				}else {
					slaveMedia.setBegin(0.0);
				}
				
				slaveMedia.setEnd(synchronousRelationToBeDefined.getMasterNode().getEnd());
				slaveMedia.setDuration(slaveMedia.getEnd() - slaveMedia.getBegin());
				removeMedia(slaveMedia, false);
				addMedia(slaveMedia);

				break;
			
			case FINISHES_DELAY:

				Double delayFinishesRelationBegin = synchronousRelationToBeDefined.getMasterNode().getEnd() + synchronousRelationToBeDefined.getDelay() - slaveMedia.getDuration();
				if(delayFinishesRelationBegin > 0){
					slaveMedia.setBegin(delayFinishesRelationBegin);
				}else {
					slaveMedia.setBegin(0.0);
				}
				
				slaveMedia.setEnd(synchronousRelationToBeDefined.getMasterNode().getEnd() + synchronousRelationToBeDefined.getDelay());
				slaveMedia.setDuration(slaveMedia.getEnd() - slaveMedia.getBegin());
				removeMedia(slaveMedia, false);
				addMedia(slaveMedia);

				break;
				
			case MEETS:

				slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterNode().getEnd());
				slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
				removeMedia(slaveMedia, false);
				addMedia(slaveMedia);

				break;
			
			case MEETS_DELAY:

				slaveMedia.setBegin(synchronousRelationToBeDefined.getMasterNode().getEnd() + synchronousRelationToBeDefined.getDelay());
				slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
				removeMedia(slaveMedia, false);
				addMedia(slaveMedia);

				break;
			
			case MET_BY:
				
				Boolean isPossibleDefineMetBy = true;

				slaveMedia.setEnd(synchronousRelationToBeDefined.getMasterNode().getBegin());
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
					removeMedia(slaveMedia, false);
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
					
				slaveMedia.setEnd(synchronousRelationToBeDefined.getMasterNode().getBegin() + synchronousRelationToBeDefined.getDelay());
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
					removeMedia(slaveMedia, false);
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
					beforeRelationBegin = synchronousRelationToBeDefined.getMasterNode().getEnd() + synchronousRelationToBeDefined.getDelay();
				}else {
					beforeRelationBegin = synchronousRelationToBeDefined.getSlaveNodeList().get(i-1).getEnd() + synchronousRelationToBeDefined.getDelay();
				}

				slaveMedia.setBegin(beforeRelationBegin);
				slaveMedia.setEnd(slaveMedia.getBegin() + slaveMedia.getDuration());
				removeMedia(slaveMedia, false);
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
	private ConflictType isThereBeginGTEndOrEndLTBeginConflict( Synchronous synchronousRelationToBeDefined, 
			Synchronous synchronousRelationWhereSlaveMediaIsSlave, Media newRelationSlaveMedia) {
		
		if(relationDefinesBegin(synchronousRelationToBeDefined)){
			
			Double newRelationBegin = 0.0;
			Double existingRelationEnd = 0.0;
			TemporalRelationType newRelationType = synchronousRelationToBeDefined.getType();
			TemporalRelationType existingRelationType = synchronousRelationWhereSlaveMediaIsSlave.getType();
			
			switch (newRelationType) {
			
				case STARTS:
					newRelationBegin = synchronousRelationToBeDefined.getMasterNode().getBegin();
					break;
	
				case STARTS_DELAY:
					newRelationBegin = synchronousRelationToBeDefined.getMasterNode().getBegin() + synchronousRelationToBeDefined.getDelay();
					break;
					
				case MEETS:
					newRelationBegin = synchronousRelationToBeDefined.getMasterNode().getEnd();
					break;
					
				case MEETS_DELAY:
					newRelationBegin = synchronousRelationToBeDefined.getMasterNode().getEnd() + synchronousRelationToBeDefined.getDelay();
					break;
					
				case BEFORE:
					 
					ArrayList<Node> auxSlaveMediaList = new ArrayList<Node>(synchronousRelationToBeDefined.getSlaveNodeList());
					Double begin;
					
					for(int i=0; i < auxSlaveMediaList.size(); i++){
		
						Media auxSlaveMedia = (Media) auxSlaveMediaList.get(i);
						
						if(i == 0){
							begin = synchronousRelationToBeDefined.getMasterNode().getEnd() + synchronousRelationToBeDefined.getDelay();
							if(auxSlaveMedia == newRelationSlaveMedia){
								newRelationBegin = begin;
								break;
							}
						}else {
							begin = synchronousRelationToBeDefined.getSlaveNodeList().get(i-1).getEnd() + synchronousRelationToBeDefined.getDelay();
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
					existingRelationEnd = synchronousRelationWhereSlaveMediaIsSlave.getMasterNode().getEnd();
					break;
	
				case FINISHES_DELAY:
					existingRelationEnd = synchronousRelationWhereSlaveMediaIsSlave.getMasterNode().getEnd() + synchronousRelationWhereSlaveMediaIsSlave.getDelay();
					break;
					
				case MET_BY:
					existingRelationEnd = synchronousRelationWhereSlaveMediaIsSlave.getMasterNode().getBegin();
					break;
					
				case MET_BY_DELAY:
					existingRelationEnd = synchronousRelationWhereSlaveMediaIsSlave.getMasterNode().getBegin() + synchronousRelationWhereSlaveMediaIsSlave.getDelay();
					break;
			
			}

			if(newRelationBegin > existingRelationEnd){
				return ConflictType.NEW_BEGIN_GREATER_THAN_EXISTING_END;
			}else {
				return null;
			}
			
		}else {
			
			Double newRelationEnd = 0.0;
			Double existingRelationBegin = 0.0;
			TemporalRelationType newRelationType = synchronousRelationToBeDefined.getType();
			TemporalRelationType existingRelationType = synchronousRelationWhereSlaveMediaIsSlave.getType();
			
			switch (newRelationType) {
			
				case FINISHES:
					newRelationEnd = synchronousRelationToBeDefined.getMasterNode().getEnd();
					break;
	
				case FINISHES_DELAY:
					newRelationEnd = synchronousRelationToBeDefined.getMasterNode().getEnd() + synchronousRelationToBeDefined.getDelay();
					break;
					
				case MET_BY:
					newRelationEnd = synchronousRelationToBeDefined.getMasterNode().getBegin();
					break;
					
				case MET_BY_DELAY:
					newRelationEnd = synchronousRelationToBeDefined.getMasterNode().getBegin() + synchronousRelationToBeDefined.getDelay();
					break;
				
			}
			
			switch (existingRelationType) {
			
				case STARTS:
					existingRelationBegin = synchronousRelationWhereSlaveMediaIsSlave.getMasterNode().getBegin();
					break;
	
				case STARTS_DELAY:
					existingRelationBegin = synchronousRelationWhereSlaveMediaIsSlave.getMasterNode().getBegin() + synchronousRelationWhereSlaveMediaIsSlave.getDelay();
					break;
					
				case MEETS:
					existingRelationBegin = synchronousRelationWhereSlaveMediaIsSlave.getMasterNode().getEnd();
					break;
					
				case MEETS_DELAY:
					existingRelationBegin = synchronousRelationWhereSlaveMediaIsSlave.getMasterNode().getEnd() + synchronousRelationWhereSlaveMediaIsSlave.getDelay();
					break;
					
				case BEFORE:
					 
					ArrayList<Node> auxSlaveMediaList = new ArrayList<Node>(synchronousRelationWhereSlaveMediaIsSlave.getSlaveNodeList());
					Double begin;
					
					for(int i=0; i < auxSlaveMediaList.size(); i++){
		
						Media auxSlaveMedia = (Media) auxSlaveMediaList.get(i);
						
						if(i == 0){
							begin = synchronousRelationWhereSlaveMediaIsSlave.getMasterNode().getEnd() + synchronousRelationWhereSlaveMediaIsSlave.getDelay();
							if(auxSlaveMedia == newRelationSlaveMedia){
								existingRelationBegin = begin;
								break;
							}
						}else {
							begin = synchronousRelationWhereSlaveMediaIsSlave.getSlaveNodeList().get(i-1).getEnd() + synchronousRelationWhereSlaveMediaIsSlave.getDelay();
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
				return ConflictType.NEW_END_LESS_THAN_EXISTING_BEGIN;
			}else {
				return null;
			}
			
		}
	
	}

	private boolean relationDefinesEnd(Synchronous synchronousRelation){
		
		TemporalRelationType relationType = synchronousRelation.getType();
		
		if(relationType == TemporalRelationType.FINISHES || relationType == TemporalRelationType.FINISHES_DELAY 
		   ||relationType == TemporalRelationType.MET_BY || relationType == TemporalRelationType.MET_BY_DELAY){
			
			return true;
			
		}else {
			return false;
		}
		
	}

	private boolean relationDefinesBegin(Synchronous synchronousRelation){
		
		TemporalRelationType relationType = synchronousRelation.getType();
		
		if(relationType == TemporalRelationType.STARTS || relationType == TemporalRelationType.STARTS_DELAY ||relationType == TemporalRelationType.MEETS
		   || relationType == TemporalRelationType.MEETS_DELAY || relationType == TemporalRelationType.BEFORE){
			
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
	private ConflictType hasBeginOrEndAlreadyBeenDefined(Synchronous synchronousRelationToBeDefined, Synchronous synchronousRelationWhereSlaveMediaIsSlave) {

		if(relationDefinesBegin(synchronousRelationToBeDefined) && relationDefinesBegin(synchronousRelationWhereSlaveMediaIsSlave)){
			
			return ConflictType.BEGIN_DEFINED;
			
		} else if(relationDefinesEnd(synchronousRelationToBeDefined) && relationDefinesEnd(synchronousRelationWhereSlaveMediaIsSlave)){
			
			return ConflictType.END_DEFINED;
			
		} else{
			return null;
		}
		
	}

	private ArrayList<TemporalRelation> getListOfMasterRelations(Media slaveMedia) {
		
		ArrayList<TemporalRelation> listOfMasterRelations = new ArrayList<TemporalRelation>(); 
		
		for(TemporalRelation relation : relationList){
			
			if(relation instanceof Synchronous){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				
				if(slaveMedia.equals(synchronousRelation.getMasterNode())){
					listOfMasterRelations.add(synchronousRelation);
				}
				
			}

		}
		
		return listOfMasterRelations;
		
	}

	private ArrayList<TemporalRelation> getListOfSlaveRelations(Media slaveMedia) {
		
		ArrayList<TemporalRelation> listOfSlaveRelations = new ArrayList<TemporalRelation>(); 
		
		for(TemporalRelation relation : relationList){
			
			if(relation instanceof Synchronous){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				
				for(Node synchronousRelationSlave : synchronousRelation.getSlaveNodeList()){
						
					if(slaveMedia.equals((Media)synchronousRelationSlave)){
						listOfSlaveRelations.add(synchronousRelation);
						break;
					}
						
				}
				
			}

		}
		
		return listOfSlaveRelations;
		
	}

	private ArrayList<TemporalRelation> getListOfAllRelations(Media slaveMedia) {
		
		ArrayList<TemporalRelation> listOfAllRelations = new ArrayList<TemporalRelation>(); 
		
		for(TemporalRelation relation : relationList){
			
			if(relation instanceof Synchronous){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				
				if(slaveMedia.equals(synchronousRelation.getMasterNode())){
					listOfAllRelations.add(synchronousRelation);
				}else {
					
					for(Node synchronousRelationSlave : synchronousRelation.getSlaveNodeList()){
						
						if(slaveMedia.equals((Media)synchronousRelationSlave)){
							listOfAllRelations.add(synchronousRelation);
							break;
						}
						
					}
					
				}
				
			}

		}
		
		return listOfAllRelations;
		
	}

	public void showBlockedRelationMessageDialog(Media slaveMedia, ConflictType conflictType){
		
		MessageDialog showBlockedRelationMessageDialog;
		
		switch (conflictType) {
		
			case BEGIN_END_DEFINED:
				
				showBlockedRelationMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment.for.this.slave") + ": " + slaveMedia.getName(), 
						Language.translate("begin.and.end.have.already.been.defined"), "OK", 160);
				showBlockedRelationMessageDialog.showAndWait();
				break;
	
			case BEGIN_DEFINED:
				
				showBlockedRelationMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment.for.this.slave") + ": " + slaveMedia.getName(), 
						Language.translate("begin.has.already.been.defined"), "OK", 160);
				showBlockedRelationMessageDialog.showAndWait();
				break;
				
			case END_DEFINED:
				
				showBlockedRelationMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment.for.this.slave") + ": " + slaveMedia.getName(), 
						Language.translate("end.has.already.been.defined"), "OK", 160);
				showBlockedRelationMessageDialog.showAndWait();
				break;
				
			case NEW_BEGIN_GREATER_THAN_EXISTING_END:
				
				showBlockedRelationMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment.for.this.slave") + ": " + slaveMedia.getName(), 
						Language.translate("new.begin.is.greater.than.the.end.defined.by.another.alignment"), "OK", 180);
				showBlockedRelationMessageDialog.showAndWait();
				break;
				
			case NEW_END_LESS_THAN_EXISTING_BEGIN:
				
				showBlockedRelationMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment.for.this.slave") + ": " + slaveMedia.getName(), 
						Language.translate("new.end.is.less.than.the.begin.defined.by.another.alignment"), "OK", 180);
				showBlockedRelationMessageDialog.showAndWait();
				break;
				
		}

	}
	
	public void removeSynchronousRelation(Synchronous synchronousRelation){
		
		relationList.remove(synchronousRelation);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.REMOVE_SYNC_RELATION, synchronousRelation, this);
        notifyObservers(operation);
        
	}
	
	public void removeInteractivityRelation(Interactivity<Media> interactivityRelation){
		
		relationList.remove(interactivityRelation);
		
		interactivityRelation.getMasterNode().setInteractive(false);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.REMOVE_INTERACTIVITY_RELATION, interactivityRelation, this);
        notifyObservers(operation);
        
	}

	public ArrayList<TemporalRelation> getRelationList() {
		return relationList;
	}
	
	public static int getTemporalViewMediaNumber(){
		return temporalViewMediaNumber;
	}
	
	public ArrayList<ArrayList<Media>> getMediaLineList() {
		return mediaLineList;
	}

	public ArrayList<Media> getMediaListDuringAnother(Media firstSelectedMedia, TemporalChainPane temporalChainPane) {
		
		ArrayList<Media> mediaListDuringAnother = new ArrayList<Media>();
		
		for(ArrayList<TimeLineXYChartData> timeLineXYChartDataList : temporalChainPane.getTimeLineXYChartDataLineList()){
			
			for(TimeLineXYChartData timeLineXYChartData : timeLineXYChartDataList){
			
				AllenRelation allenRelation = identifyAllenRelation(firstSelectedMedia, timeLineXYChartData.getMedia());
				
				if(firstSelectedMedia != timeLineXYChartData.getMedia()){
					
					if ( !( allenRelation.equals(AllenRelation.BEFORE) || allenRelation.equals(AllenRelation.AFTER) ||
							allenRelation.equals(AllenRelation.MEETS) || allenRelation.equals(AllenRelation.MET_BY) ) ){
						
						mediaListDuringAnother.add(timeLineXYChartData.getMedia());
						
					}
					
				}
				
			}
			
		}
		
		return mediaListDuringAnother;
			
	}
	
	 @Override
	 public String toString(){
		 return name;
	 }

	public void addInteractivityRelation(Interactivity<Media> interactivityRelation) {
	
		relationList.add(interactivityRelation);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_INTERACTIVITY_RELATION, interactivityRelation, this);
        notifyObservers(operation);
		
	}
	
	public void updateInteractivityRelation(Interactivity<Media> interactivityRelation) {
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.EDIT_INTERACTIVITY_RELATION, interactivityRelation, this);
        notifyObservers(operation);
		
	}
	
}
