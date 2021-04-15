package model.temporalView;

import model.common.MediaNode;
import model.common.Node;
import model.common.SensoryEffectNode;
import model.temporalView.enums.AllenRelation;
import model.temporalView.enums.ConflictType;
import model.temporalView.enums.TemporalRelationType;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.Operation;
import view.common.Language;
import view.common.dialogs.MessageDialog;
import view.temporalViewPane.TemporalChainPane;
import view.temporalViewPane.TimeLineXYChartData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

@SuppressWarnings("rawtypes")
public class TemporalChain extends Observable implements Serializable {

	private static final long serialVersionUID = -6154510036093880684L;
	
	private static int temporalChainNumber = 0;
	private static int temporalViewNodeNumber = 0;

	private int id;
	private String name;
	private Node masterNode;
	private ArrayList<Node> nodeAllList = new ArrayList<Node>();
	private ArrayList<TemporalRelation> relationList = new ArrayList<TemporalRelation>();

	private ArrayList<MediaNode> mediaNodeAllList = new ArrayList<MediaNode>();
	private ArrayList<SensoryEffectNode> sensoryEffectNodeAllList = new ArrayList<SensoryEffectNode>();
	private ArrayList<ArrayList<Node>> nodeLineList = new ArrayList<ArrayList<Node>>();

	public TemporalChain(String name) {
		
		this.id = temporalChainNumber;
		temporalChainNumber++;
		
		this.name = name;
		
	}

	public void clearObject(){

		name = null;
		masterNode = null;
		nodeAllList.clear();
		relationList.clear();

		mediaNodeAllList.clear();
		sensoryEffectNodeAllList.clear();
		nodeLineList.clear();

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
	
	public void setMasterNode(Node masterNode){
		
		this.masterNode = masterNode;
		
	}
	
	public Node getMasterNode() {
		return masterNode;
	}
	
	public void addNode(Node node){

		if(node instanceof MediaNode){
			mediaNodeAllList.add((MediaNode)node);
		}else if(node instanceof SensoryEffectNode){
			sensoryEffectNodeAllList.add((SensoryEffectNode)node);
		}
		
		nodeAllList.add(node);
		
		int line = addNodeLineList(node);
		
		if(masterNode == null){
			masterNode = node;
		} else if(masterNode.getBegin() != 0){
			masterNode = getNodeWithLowestBegin();
		}
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_TEMPORAL_CHAIN_NODE, node, line);
        notifyObservers(operation);
        
        temporalViewNodeNumber++;
        
	}
	
	public void removeNode(Node node, Boolean isDeleteButton){

		mediaNodeAllList.remove(node);
		sensoryEffectNodeAllList.remove(node);
		nodeAllList.remove(node);
		
		int line = removeNodeLineList(node);
		
		if(masterNode == node){
			masterNode = getNodeWithLowestBegin();
		}
		
		if(isDeleteButton){
			removeNodeOfRelations(node);
		}

		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.REMOVE_TEMPORAL_CHAIN_NODE, node, line);
        notifyObservers(operation);
        
        temporalViewNodeNumber--;
        
	}

	/**
	 * Update start time using dragNode method. Remove the node afterwards add it.
	 * Also, update the model (Node and TemporalChain).
	 */
	public void updateNodeStartTimeView(Node node, Double newValue, boolean isDrag) {
		dragNode(node, newValue, isDrag);
	}

	/**
	 * Update end time using dragNode method. Remove the node afterwards add it.
	 * Also, update the model (Node and TemporalChain).
	 */
	public void updateNodeEndTimeView(Node node, Double newValue, boolean isLinked) {

		Double newBeginDerivedFromNewEnd = node.getBegin();

		if(isLinked){
			newBeginDerivedFromNewEnd = newValue - node.getDuration();
		}

		ArrayList<Node> rootNodeList = new ArrayList<Node>();

		node.setBegin(newBeginDerivedFromNewEnd);
		if(isLinked){
			node.setEnd(newBeginDerivedFromNewEnd + node.getDuration());
		}else{
			node.setEnd(newValue);
			node.setDuration(node.getEnd() - node.getBegin());
		}

		removeNode(node, false);
		addNode(node);

		dragChildren(node);


	}

	public void updateNodeDurationTimeView(Node node, Double newValue) {

		ArrayList<Node> rootNodeList = new ArrayList<Node>();

		addElementsInRootNodeList(node, rootNodeList);

		if(rootNodeList.get(0) == node){

			node.setDuration(newValue);
			node.setEnd(node.getBegin() + node.getDuration());

			removeNode(node, false);
			addNode(node);

			dragChildren(node);

		}else {

			//INFO offset(draggedTime) to be applied for all nodes (parents and children of the dragged node)
			// linked to the dragged node by user.
			Double draggedTime = newValue - node.getEnd();

			for(Node rootNode : rootNodeList){

				node.setDuration(newValue);
				rootNode.setEnd(rootNode.getEnd() + draggedTime);

				removeNode(rootNode, false);
				addNode(rootNode);

				dragChildren(rootNode);

			}

		}

	}

	public void dragNode(Node node, Double droppedTime, boolean isDrag) {
		
		ArrayList<Node> rootNodeList = new ArrayList<Node>();
		
		addElementsInRootNodeList(node, rootNodeList);
    		
		if(rootNodeList.get(0) == node){

			node.setBegin(droppedTime);
			if(isDrag){
				node.setEnd(droppedTime + node.getDuration());
			}else{
				node.setDuration(node.getEnd() - node.getBegin());
			}

	    	removeNode(node, false);
	    	addNode(node);
	    	
	    	dragChildren(node);
			
		}else {

			if(isDrag){

				//INFO offset(draggedTime) to be applied for all nodes (parents and children of the dragged node)
				// linked to the dragged node by user.
				Double draggedTime = droppedTime - node.getBegin();

				for(Node rootNode : rootNodeList){

					rootNode.setBegin(rootNode.getBegin() + draggedTime);
					rootNode.setEnd(rootNode.getEnd() + draggedTime);

					removeNode(rootNode, false);
					addNode(rootNode);

					dragChildren(rootNode);

				}

			}else{ //INFO It is not node dragging. It is update of start time.

				node.setBegin(droppedTime);
				node.setDuration(node.getEnd() - node.getBegin());

				removeNode(node, false);
				addNode(node);

				dragChildren(node);

			}

			
		}
		
	}
	
	private void addElementsInRootNodeList(Node node, ArrayList<Node> rootNodeList){
		
		ArrayList<TemporalRelation> listOfRelationsWhereNodeIsSecondary = getListOfRelationsWhereNodeIsSecondary(node);
		
		if(!listOfRelationsWhereNodeIsSecondary.isEmpty()){
			
			for(TemporalRelation relation : listOfRelationsWhereNodeIsSecondary){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				Node masterNode = synchronousRelation.getPrimaryNode();
				addElementsInRootNodeList(masterNode, rootNodeList);
				
			}
			
		}else {
			
			rootNodeList.add(node);
			
		}

	}

	private int getLineToAddNode(Node node){
		
		AllenRelation allenRelation;
    	int line = 0;
    	
    	while(line < nodeLineList.size()){
    		
    		boolean isPossibleAdd = true;
    		ArrayList<Node> mediaList = nodeLineList.get(line);
    		int index = 0;
    		
    		while(isPossibleAdd && index < mediaList.size()){
    			
    			Node currentNode = mediaList.get(index);
    			allenRelation = identifyAllenRelation(node, currentNode);
    			
    			if(node != currentNode){
    				
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

    	int newLineIndex = nodeLineList.size();

    	return newLineIndex;
		
	}
	
	private Node getNodeWithLowestBegin() {
		
		Node nodeWithLowestBegin = null;
		
		if(!nodeAllList.isEmpty()){
			nodeWithLowestBegin = nodeAllList.get(0);
		}
		
		for(int i=1; i < nodeAllList.size(); i++){
			
			Node node = nodeAllList.get(i);
			
			if(node.getBegin() < nodeWithLowestBegin.getBegin()){
				nodeWithLowestBegin = node;
			}
			
		}
		
		return nodeWithLowestBegin;
		
	}
	public Node getNodeWithHighestEnd() {
		
		Node nodeWithHighestEnd = null;
		
		if(!nodeAllList.isEmpty()){
			nodeWithHighestEnd = nodeAllList.get(nodeAllList.size()-1);
		}
		
		for(Node node : nodeAllList){
			
			if(node.getEnd() > nodeWithHighestEnd.getEnd()){
				nodeWithHighestEnd = node;
			}
			
		}
		
		return nodeWithHighestEnd;
		
	}


	private void removeNodeOfRelations(Node node) {

		ArrayList<Synchronous> synchronousRelationsToBeRemoved = new ArrayList<Synchronous>();
		ArrayList<Interactivity> interactivityRelationsToBeRemoved = new ArrayList<Interactivity>();

		for(int i=0; i < relationList.size(); i++){
			
			TemporalRelation relation = relationList.get(i);
			
			if(relation instanceof Synchronous){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				if(synchronousRelation.getPrimaryNode() == node){
					synchronousRelationsToBeRemoved.add(synchronousRelation);
				}else {
					synchronousRelation.removeSlaveNode(node);
					if(synchronousRelation.getSecondaryNodeList().isEmpty()){
						synchronousRelationsToBeRemoved.add(synchronousRelation);
					}
				}
				
			}else if(relation instanceof Interactivity){
				
				Interactivity<MediaNode> interactivityRelation = (Interactivity<MediaNode>) relation;
				if(interactivityRelation.getPrimaryNode() == node){
					interactivityRelationsToBeRemoved.add(interactivityRelation);
				}else {
					interactivityRelation.removeSlaveNode(node);
					if(interactivityRelation.getSecondaryNodeList().isEmpty() && interactivityRelation.getTemporalChainList().isEmpty()){
						interactivityRelationsToBeRemoved.add(interactivityRelation);
					}
				}
				
			}
			
		}

		for(Synchronous synchronousRelation : synchronousRelationsToBeRemoved){
			removeSynchronousRelation(synchronousRelation);
		}

		for(Interactivity interactivityRelation : interactivityRelationsToBeRemoved){
			removeInteractivityRelation(interactivityRelation);
		}
		
	}

	private int removeNodeLineList(Node node) {
		
		int line = 0;
    	Boolean removed = false;
		
    	while(line < nodeLineList.size()){
    		
    		ArrayList<Node> nodeList = nodeLineList.get(line);
    		
    		if(nodeList.remove(node)){
    			return line;
    		}
    		line++;
    		
    	}
    	
    	return -1;
		
		
	}

	public ArrayList<MediaNode> getMediaNodeAllList() {
		return mediaNodeAllList;
	}
	
	public ArrayList<Node> getNodeAllList() {
		return nodeAllList;
	}
	
	private int addNodeLineList(Node node){

    	AllenRelation allenRelation;
    	int line = 0;
    	
    	while(line < nodeLineList.size()){
    		
    		boolean isPossibleAdd = true;
    		ArrayList<Node> nodeList = nodeLineList.get(line);
    		int index = 0;
    		
    		while(isPossibleAdd && index < nodeList.size()){
    			
    			Node currentNode = nodeList.get(index);
    			allenRelation = identifyAllenRelation(node, currentNode);
    			
    			if (!(allenRelation.equals(AllenRelation.BEFORE) || (allenRelation.equals(AllenRelation.AFTER)))){
    				
    				isPossibleAdd = false;
    				
    			}
    			
    			index++;
    			
    		}
    		
    		if(isPossibleAdd){
    			
    			nodeList.add(node);
    			return line;

    		}
    		
    		line++;
    	}

    	int newLineIndex = nodeLineList.size();
    	ArrayList<Node> newNodeList = new ArrayList<Node>();
    	newNodeList.add(node);
    	nodeLineList.add(newLineIndex, newNodeList);

    	return newLineIndex;
		
	}
	
	public AllenRelation identifyAllenRelation(Node node, Node currentNode) {
		
		double begin = node.getBegin();
		double end = node.getEnd();
		double currentMediaBegin = currentNode.getBegin();
		double currentMediaEnd = currentNode.getEnd();
		
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

		Boolean atLeastOneSecondaryMediaWasDefined = false;
		int i;
		ArrayList<Node> listOfSecondaryNodeToBeRemoved = new ArrayList<>();

		for(i  = 0; i < synchronousRelationToBeDefined.getSecondaryNodeList().size(); i++){
			
			Node secondaryNode = synchronousRelationToBeDefined.getSecondaryNodeList().get(i);
			
			Boolean secondaryWasChangedModifyingDuration = false;
			Boolean secondaryBlockedForChanges = false;
			
			ArrayList<TemporalRelation> listOfAllRelations = getListOfAllRelations(secondaryNode);
			ArrayList<TemporalRelation> listOfSecondaryRelations = getListOfRelationsWhereNodeIsSecondary(secondaryNode);
			ArrayList<TemporalRelation> listOfMasterRelations = getListOfMasterRelations(secondaryNode);

			if(!listOfAllRelations.isEmpty()){

				//INFO There is a loop in that case. This prevents the dragChildren method to run in loop.
				// However, this loop can occur in MultiSEM. Its just a limitation of STEVE.
				boolean isThereLoopSameRelation = isThereLoopSameRelationForTheCurrentSecondary(synchronousRelationToBeDefined, listOfMasterRelations);

				if(isThereLoopSameRelation){

					showBlockedRelationMessageDialog(secondaryNode, ConflictType.LOOP);
					secondaryBlockedForChanges = true;

				}else if(listOfSecondaryRelations.size() == 2){//INFO Uma mídia pode ser secundária de no máximo 2 relações

					showBlockedRelationMessageDialog(secondaryNode, ConflictType.BEGIN_END_DEFINED);
					secondaryBlockedForChanges = true;

				} else if(listOfSecondaryRelations.size() == 1) { //INFO Apenas uma relação

					ConflictType conflictType;

					Synchronous synchronousRelationWhereSecondaryNodeIsSecondary = (Synchronous) listOfSecondaryRelations.get(0);

					conflictType = hasBeginOrEndAlreadyBeenDefined(synchronousRelationToBeDefined, synchronousRelationWhereSecondaryNodeIsSecondary);

					if(conflictType != null){

						showBlockedRelationMessageDialog(secondaryNode, conflictType);
						secondaryBlockedForChanges = true;

					}else {

						conflictType = isThereBeginGTEndOrEndLTBeginConflict(synchronousRelationToBeDefined, synchronousRelationWhereSecondaryNodeIsSecondary, secondaryNode);

						if(conflictType != null){

							showBlockedRelationMessageDialog(secondaryNode, conflictType);
							secondaryBlockedForChanges = true;

						}else {

							if(!secondaryNode.isContinousMedia()){

								defineRelationChangingDuration(synchronousRelationToBeDefined, i, secondaryNode);
								secondaryWasChangedModifyingDuration = true;
								atLeastOneSecondaryMediaWasDefined = true;

							}else{

								MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment.for.this.secondary") + ": " + secondaryNode.getName(),
										Language.translate("changing.duration.continuous.media"), "OK", 185);
								messageDialog.showAndWait();

								secondaryBlockedForChanges = true;
							}

						}

					}

				}

				if(!secondaryBlockedForChanges){

					if(!listOfMasterRelations.isEmpty()){

						if(secondaryWasChangedModifyingDuration){

							dragChildren(secondaryNode);

						}else {

							defineRelation(synchronousRelationToBeDefined, i, secondaryNode);
							atLeastOneSecondaryMediaWasDefined = true;
							dragChildren(secondaryNode);

						}

					}

				}else {
					listOfSecondaryNodeToBeRemoved.add(secondaryNode);
				}

			}else {

				defineRelation(synchronousRelationToBeDefined, i, secondaryNode);
				atLeastOneSecondaryMediaWasDefined = true;

			}
			
		}
			
		if(!atLeastOneSecondaryMediaWasDefined && synchronousRelationToBeDefined.getSecondaryNodeList().size()>1){

			MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
					Language.translate("no.selected.media.could.be.secondary.for.the.relation"), "OK", 180);
			messageDialog.showAndWait();
			
		}else if(atLeastOneSecondaryMediaWasDefined) {

			for(Node secondaryNode : listOfSecondaryNodeToBeRemoved){
				synchronousRelationToBeDefined.getSecondaryNodeList().remove(secondaryNode);
			}

			relationList.add(synchronousRelationToBeDefined);

		}
	
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_SYNC_RELATION, synchronousRelationToBeDefined, this);
        notifyObservers(operation);
        
	}

	private boolean isThereLoopSameRelationForTheCurrentSecondary(Synchronous synchronousRelationToBeDefined, ArrayList<TemporalRelation> listOfMasterRelations) {

		boolean primaryIsSecondaryOfSomeOfItsSecondaryInSameRelation = false;

		Node primaryNodeOfRelationToBeDefined = synchronousRelationToBeDefined.getPrimaryNode();

		for(TemporalRelation temporalRelation : listOfMasterRelations){

			if(temporalRelation instanceof Synchronous){

				Synchronous synchronousRelation = (Synchronous) temporalRelation;

					for(Node synchronousRelationSecondary : synchronousRelation.getSecondaryNodeList()){

						if(primaryNodeOfRelationToBeDefined.equals(synchronousRelationSecondary)){
							primaryIsSecondaryOfSomeOfItsSecondaryInSameRelation = true;
							break;
						}
					}

					if(primaryIsSecondaryOfSomeOfItsSecondaryInSameRelation){
						break;
					}

			}
		}
		return primaryIsSecondaryOfSomeOfItsSecondaryInSameRelation;
	}

	private void dragChildren(Node rootNode) {
		
		ArrayList<TemporalRelation> listOfMasterRelations = getListOfMasterRelations(rootNode);
		
		for(TemporalRelation relation : listOfMasterRelations){
			
			Synchronous synchronousRelation = (Synchronous) relation;
			
			for(int i = 0; i < synchronousRelation.getSecondaryNodeList().size(); i++){
				
				Node secondaryNode = synchronousRelation.getSecondaryNodeList().get(i);
				defineRelation(synchronousRelation, i, secondaryNode);
				
				dragChildren(secondaryNode);
				
			}
	
		}

	}

	private void defineRelationChangingDuration(Synchronous synchronousRelationToBeDefined, int i, Node secondaryNode) {
		
		switch(synchronousRelationToBeDefined.getType()){
		
			case STARTS:
				
				secondaryNode.setBegin(synchronousRelationToBeDefined.getPrimaryNode().getBegin());
				removeNode(secondaryNode, false);
				addNode(secondaryNode);
				break;
				
			case STARTS_DELAY:
				
				secondaryNode.setBegin(synchronousRelationToBeDefined.getPrimaryNode().getBegin() + synchronousRelationToBeDefined.getDelay());
				removeNode(secondaryNode, false);
				addNode(secondaryNode);
	
				break;
				
			case FINISHES:

				secondaryNode.setEnd(synchronousRelationToBeDefined.getPrimaryNode().getEnd());
				secondaryNode.setDuration(secondaryNode.getEnd() - secondaryNode.getBegin());
				removeNode(secondaryNode, false);
				addNode(secondaryNode);
	
				break;
			
			case FINISHES_DELAY:
	
				secondaryNode.setEnd(synchronousRelationToBeDefined.getPrimaryNode().getEnd() + synchronousRelationToBeDefined.getDelay());
				secondaryNode.setDuration(secondaryNode.getEnd() - secondaryNode.getBegin());
				removeNode(secondaryNode, false);
				addNode(secondaryNode);
	
				break;
				
			case MEETS:
	
				secondaryNode.setBegin(synchronousRelationToBeDefined.getPrimaryNode().getEnd());
				removeNode(secondaryNode, false);
				addNode(secondaryNode);
	
				break;
			
			case MEETS_DELAY:
	
				secondaryNode.setBegin(synchronousRelationToBeDefined.getPrimaryNode().getEnd() + synchronousRelationToBeDefined.getDelay());
				removeNode(secondaryNode, false);
				addNode(secondaryNode);
	
				break;
			
			case MET_BY:
				
				Boolean isPossibleDefineMetBy = true;
	
				secondaryNode.setEnd(synchronousRelationToBeDefined.getPrimaryNode().getBegin());
				secondaryNode.setDuration(secondaryNode.getEnd() - secondaryNode.getBegin());
				
				if(secondaryNode.getDuration() == 0){
					isPossibleDefineMetBy = false;
					break;
				}else {
					removeNode(secondaryNode, false);
					addNode(secondaryNode);
				}
	
				if(!isPossibleDefineMetBy){
					MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
							Language.translate("select.other.media.to.be.the.primary"), "OK", 190);
					messageDialog.showAndWait();
				}
				
				break;
			
			case MET_BY_DELAY:
				
				Boolean isPossibleDefineMetByDelay = true;
					
				secondaryNode.setEnd(synchronousRelationToBeDefined.getPrimaryNode().getBegin() + synchronousRelationToBeDefined.getDelay());
				secondaryNode.setDuration(secondaryNode.getEnd() - secondaryNode.getBegin());
				
				if(secondaryNode.getDuration() == 0){
					isPossibleDefineMetByDelay = false;
					break;
				}else {
					removeNode(secondaryNode, false);
					addNode(secondaryNode);
				}
	
				if(!isPossibleDefineMetByDelay){
					MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
							Language.translate("select.other.media.to.be.the.primary"), "OK", 190);
					messageDialog.showAndWait();
				}
				
				break;
			
			case BEFORE:
		
				Double beforeRelationBegin;
				
				if(i == 0){
					beforeRelationBegin = synchronousRelationToBeDefined.getPrimaryNode().getEnd() + synchronousRelationToBeDefined.getDelay();
				}else {
					beforeRelationBegin = synchronousRelationToBeDefined.getSecondaryNodeList().get(i-1).getEnd() + synchronousRelationToBeDefined.getDelay();
				}
	
				secondaryNode.setBegin(beforeRelationBegin);
				removeNode(secondaryNode, false);
				addNode(secondaryNode);
				
				break;
			
		}
		
	}

	//INFO Após passar pela validação das relações (e sua inserção no modelo), seta o início e fim das mídias visualmente na cadeia utilizando inicio/fim das mídia mestre e delay conforme o tipo de relação.
	private void defineRelation(Synchronous synchronousRelationToBeDefined, int i, Node secondaryNode) {
		
		switch(synchronousRelationToBeDefined.getType()){
		
			case STARTS:
				
				secondaryNode.setBegin(synchronousRelationToBeDefined.getPrimaryNode().getBegin());
				secondaryNode.setEnd(secondaryNode.getBegin() + secondaryNode.getDuration());
				removeNode(secondaryNode, false);
				addNode(secondaryNode);
				
				break;
				
			case STARTS_DELAY:
				
				secondaryNode.setBegin(synchronousRelationToBeDefined.getPrimaryNode().getBegin() + synchronousRelationToBeDefined.getDelay());
				secondaryNode.setEnd(secondaryNode.getBegin() + secondaryNode.getDuration());
				removeNode(secondaryNode, false);
				addNode(secondaryNode);

				break;
				
			case FINISHES:

                ArrayList<TemporalRelation> listOfRelationsWhereNodeIsSecondary = getListOfRelationsWhereNodeIsSecondary(secondaryNode);

                if(listOfRelationsWhereNodeIsSecondary.size() < 2){

                    Double finishesRelationBegin = synchronousRelationToBeDefined.getPrimaryNode().getEnd() - secondaryNode.getDuration();
                    if(finishesRelationBegin > 0){
                        secondaryNode.setBegin(finishesRelationBegin);
                    }else {
                        secondaryNode.setBegin(0.0);
                    }

                }

				secondaryNode.setEnd(synchronousRelationToBeDefined.getPrimaryNode().getEnd());
				secondaryNode.setDuration(secondaryNode.getEnd() - secondaryNode.getBegin());
				removeNode(secondaryNode, false);
				addNode(secondaryNode);

				break;
			
			case FINISHES_DELAY:

				Double delayFinishesRelationBegin = synchronousRelationToBeDefined.getPrimaryNode().getEnd() + synchronousRelationToBeDefined.getDelay() - secondaryNode.getDuration();
				if(delayFinishesRelationBegin > 0){
					secondaryNode.setBegin(delayFinishesRelationBegin);
				}else {
					secondaryNode.setBegin(0.0);
				}
				
				secondaryNode.setEnd(synchronousRelationToBeDefined.getPrimaryNode().getEnd() + synchronousRelationToBeDefined.getDelay());
				secondaryNode.setDuration(secondaryNode.getEnd() - secondaryNode.getBegin());
				removeNode(secondaryNode, false);
				addNode(secondaryNode);

				break;
				
			case MEETS:

				secondaryNode.setBegin(synchronousRelationToBeDefined.getPrimaryNode().getEnd());
				secondaryNode.setEnd(secondaryNode.getBegin() + secondaryNode.getDuration());
				removeNode(secondaryNode, false);
				addNode(secondaryNode);

				break;
			
			case MEETS_DELAY:

				secondaryNode.setBegin(synchronousRelationToBeDefined.getPrimaryNode().getEnd() + synchronousRelationToBeDefined.getDelay());
				secondaryNode.setEnd(secondaryNode.getBegin() + secondaryNode.getDuration());
				removeNode(secondaryNode, false);
				addNode(secondaryNode);

				break;
			
			case MET_BY:
				
				Boolean isPossibleDefineMetBy = true;

				secondaryNode.setEnd(synchronousRelationToBeDefined.getPrimaryNode().getBegin());
				Double begin = secondaryNode.getEnd() - secondaryNode.getDuration();
				if(begin > 0){
					secondaryNode.setBegin(begin);
				}else {
					secondaryNode.setBegin(0.0);
				}
				
				secondaryNode.setDuration(secondaryNode.getEnd() - secondaryNode.getBegin());
				if(secondaryNode.getDuration() == 0){
					isPossibleDefineMetBy = false;
					break;
				}else {
					removeNode(secondaryNode, false);
					addNode(secondaryNode);
				}

				if(!isPossibleDefineMetBy){
					MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
							Language.translate("select.other.media.to.be.the.primary"), "OK", 190);
					messageDialog.showAndWait();
				}
				
				break;
			
			case MET_BY_DELAY:
				
				Boolean isPossibleDefineMetByDelay = true;
					
				secondaryNode.setEnd(synchronousRelationToBeDefined.getPrimaryNode().getBegin() + synchronousRelationToBeDefined.getDelay());
				Double metByDelayRelationBegin = secondaryNode.getEnd() - secondaryNode.getDuration();
				if(metByDelayRelationBegin > 0){
					secondaryNode.setBegin(metByDelayRelationBegin);
				}else {
					secondaryNode.setBegin(0.0);
				}
				
				secondaryNode.setDuration(secondaryNode.getEnd() - secondaryNode.getBegin());
				if(secondaryNode.getDuration() == 0){
					isPossibleDefineMetByDelay = false;
					break;
				}else {
					removeNode(secondaryNode, false);
					addNode(secondaryNode);
				}

				if(!isPossibleDefineMetByDelay){
					MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
							Language.translate("select.other.media.to.be.the.primary"), "OK", 190);
					messageDialog.showAndWait();
				}
				
				break;
			
			case BEFORE:

				Double beforeRelationBegin;
				
				if(i == 0){
					beforeRelationBegin = synchronousRelationToBeDefined.getPrimaryNode().getEnd() + synchronousRelationToBeDefined.getDelay();
				}else {
					beforeRelationBegin = synchronousRelationToBeDefined.getSecondaryNodeList().get(i-1).getEnd() + synchronousRelationToBeDefined.getDelay();
				}

				secondaryNode.setBegin(beforeRelationBegin);
				secondaryNode.setEnd(secondaryNode.getBegin() + secondaryNode.getDuration());
				removeNode(secondaryNode, false);
				addNode(secondaryNode);
				
				break;
		}
		
	}
	
	/** If the begin/end defined by the new relation is greater/less than the end/begin defined by the existing relation.
	 * 
	 * @param synchronousRelationToBeDefined				The new relation which is being defined.
	 * @param synchronousRelationWhereSlaveNodeIsSlave		The relations where the media selected to be slave belongs to.
	 * @return true If the begin/end defined by the new relation is greater/less than the end/begin defined by the existing relation; false otherwise.
	 */
	private ConflictType isThereBeginGTEndOrEndLTBeginConflict( Synchronous synchronousRelationToBeDefined, 
			Synchronous synchronousRelationWhereSlaveNodeIsSlave, Node newRelationSlaveNode) {
		
		if(relationDefinesBegin(synchronousRelationToBeDefined)){
			
			Double newRelationBegin = 0.0;
			Double existingRelationEnd = 0.0;
			TemporalRelationType newRelationType = synchronousRelationToBeDefined.getType();
			TemporalRelationType existingRelationType = synchronousRelationWhereSlaveNodeIsSlave.getType();
			
			switch (newRelationType) {
			
				case STARTS:
					newRelationBegin = synchronousRelationToBeDefined.getPrimaryNode().getBegin();
					break;
	
				case STARTS_DELAY:
					newRelationBegin = synchronousRelationToBeDefined.getPrimaryNode().getBegin() + synchronousRelationToBeDefined.getDelay();
					break;
					
				case MEETS:
					newRelationBegin = synchronousRelationToBeDefined.getPrimaryNode().getEnd();
					break;
					
				case MEETS_DELAY:
					newRelationBegin = synchronousRelationToBeDefined.getPrimaryNode().getEnd() + synchronousRelationToBeDefined.getDelay();
					break;
					
				case BEFORE:
					 
					ArrayList<Node> auxSlaveMediaList = new ArrayList<Node>(synchronousRelationToBeDefined.getSecondaryNodeList());
					Double begin;
					
					for(int i=0; i < auxSlaveMediaList.size(); i++){
		
						MediaNode auxSlaveMediaNode = (MediaNode) auxSlaveMediaList.get(i);
						
						if(i == 0){
							begin = synchronousRelationToBeDefined.getPrimaryNode().getEnd() + synchronousRelationToBeDefined.getDelay();
							if(auxSlaveMediaNode == newRelationSlaveNode){
								newRelationBegin = begin;
								break;
							}
						}else {
							begin = synchronousRelationToBeDefined.getSecondaryNodeList().get(i-1).getEnd() + synchronousRelationToBeDefined.getDelay();
							if(auxSlaveMediaNode == newRelationSlaveNode){
								newRelationBegin = begin;
								break;
							}
						}
						
						auxSlaveMediaNode.setBegin(begin);
						auxSlaveMediaNode.setEnd(auxSlaveMediaNode.getBegin() + auxSlaveMediaNode.getDuration());
						
					}
					
					break;
				
			}
			
			switch (existingRelationType) {
			
				case FINISHES:
					existingRelationEnd = synchronousRelationWhereSlaveNodeIsSlave.getPrimaryNode().getEnd();
					break;
	
				case FINISHES_DELAY:
					existingRelationEnd = synchronousRelationWhereSlaveNodeIsSlave.getPrimaryNode().getEnd() + synchronousRelationWhereSlaveNodeIsSlave.getDelay();
					break;
					
				case MET_BY:
					existingRelationEnd = synchronousRelationWhereSlaveNodeIsSlave.getPrimaryNode().getBegin();
					break;
					
				case MET_BY_DELAY:
					existingRelationEnd = synchronousRelationWhereSlaveNodeIsSlave.getPrimaryNode().getBegin() + synchronousRelationWhereSlaveNodeIsSlave.getDelay();
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
			TemporalRelationType existingRelationType = synchronousRelationWhereSlaveNodeIsSlave.getType();
			
			switch (newRelationType) {
			
				case FINISHES:
					newRelationEnd = synchronousRelationToBeDefined.getPrimaryNode().getEnd();
					break;
	
				case FINISHES_DELAY:
					newRelationEnd = synchronousRelationToBeDefined.getPrimaryNode().getEnd() + synchronousRelationToBeDefined.getDelay();
					break;
					
				case MET_BY:
					newRelationEnd = synchronousRelationToBeDefined.getPrimaryNode().getBegin();
					break;
					
				case MET_BY_DELAY:
					newRelationEnd = synchronousRelationToBeDefined.getPrimaryNode().getBegin() + synchronousRelationToBeDefined.getDelay();
					break;
				
			}
			
			switch (existingRelationType) {
			
				case STARTS:
					existingRelationBegin = synchronousRelationWhereSlaveNodeIsSlave.getPrimaryNode().getBegin();
					break;
	
				case STARTS_DELAY:
					existingRelationBegin = synchronousRelationWhereSlaveNodeIsSlave.getPrimaryNode().getBegin() + synchronousRelationWhereSlaveNodeIsSlave.getDelay();
					break;
					
				case MEETS:
					existingRelationBegin = synchronousRelationWhereSlaveNodeIsSlave.getPrimaryNode().getEnd();
					break;
					
				case MEETS_DELAY:
					existingRelationBegin = synchronousRelationWhereSlaveNodeIsSlave.getPrimaryNode().getEnd() + synchronousRelationWhereSlaveNodeIsSlave.getDelay();
					break;
					
				case BEFORE:
					 
					ArrayList<Node> auxSlaveMediaList = new ArrayList<Node>(synchronousRelationWhereSlaveNodeIsSlave.getSecondaryNodeList());
					Double begin;
					
					for(int i=0; i < auxSlaveMediaList.size(); i++){
		
						MediaNode auxSlaveMediaNode = (MediaNode) auxSlaveMediaList.get(i);
						
						if(i == 0){
							begin = synchronousRelationWhereSlaveNodeIsSlave.getPrimaryNode().getEnd() + synchronousRelationWhereSlaveNodeIsSlave.getDelay();
							if(auxSlaveMediaNode == newRelationSlaveNode){
								existingRelationBegin = begin;
								break;
							}
						}else {
							begin = synchronousRelationWhereSlaveNodeIsSlave.getSecondaryNodeList().get(i-1).getEnd() + synchronousRelationWhereSlaveNodeIsSlave.getDelay();
							if(auxSlaveMediaNode == newRelationSlaveNode){
								existingRelationBegin = begin;
								break;
							}
						}
						
						auxSlaveMediaNode.setBegin(begin);
						auxSlaveMediaNode.setEnd(auxSlaveMediaNode.getBegin() + auxSlaveMediaNode.getDuration());
						
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

	public boolean relationDefinesEnd(Synchronous synchronousRelation){
		
		TemporalRelationType relationType = synchronousRelation.getType();
		
		if(relationType == TemporalRelationType.FINISHES || relationType == TemporalRelationType.FINISHES_DELAY 
		   ||relationType == TemporalRelationType.MET_BY || relationType == TemporalRelationType.MET_BY_DELAY){
			
			return true;
			
		}else {
			return false;
		}
		
	}

	public boolean relationDefinesBegin(Synchronous synchronousRelation){
		
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
	 * @param synchronousRelationWhereSecondaryMediaIsSecondary		The relations where the media selected to be secondary belongs to.
	 * @return true if the relations define the same; false otherwise.
	 */
	private ConflictType hasBeginOrEndAlreadyBeenDefined(Synchronous synchronousRelationToBeDefined, Synchronous synchronousRelationWhereSecondaryMediaIsSecondary) {

		if(relationDefinesBegin(synchronousRelationToBeDefined) && relationDefinesBegin(synchronousRelationWhereSecondaryMediaIsSecondary)){
			
			return ConflictType.BEGIN_DEFINED;
			
		} else if(relationDefinesEnd(synchronousRelationToBeDefined) && relationDefinesEnd(synchronousRelationWhereSecondaryMediaIsSecondary)){
			
			return ConflictType.END_DEFINED;
			
		} else{
			return null;
		}
		
	}

	public ArrayList<TemporalRelation> getListOfMasterRelations(Node secondaryNode) {
		
		ArrayList<TemporalRelation> listOfMasterRelations = new ArrayList<TemporalRelation>(); 
		
		for(TemporalRelation relation : relationList){
			
			if(relation instanceof Synchronous){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				
				if(secondaryNode.equals(synchronousRelation.getPrimaryNode())){
					listOfMasterRelations.add(synchronousRelation);
				}
				
			}

		}
		
		return listOfMasterRelations;
		
	}

	public ArrayList<TemporalRelation> getListOfRelationsWhereNodeIsSecondary(Node secondaryNode) {
		
		ArrayList<TemporalRelation> listOfSecondaryRelations = new ArrayList<TemporalRelation>();
		
		for(TemporalRelation relation : relationList){
			
			if(relation instanceof Synchronous){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				
				for(Node synchronousRelationSecondary : synchronousRelation.getSecondaryNodeList()){
						
					if(secondaryNode.equals(synchronousRelationSecondary)){
						listOfSecondaryRelations.add(synchronousRelation);
						break;
					}
						
				}
				
			}

		}
		
		return listOfSecondaryRelations;
		
	}

	private ArrayList<TemporalRelation> getListOfAllRelations(Node secondaryNode) {
		
		ArrayList<TemporalRelation> listOfAllRelations = new ArrayList<TemporalRelation>(); 
		
		for(TemporalRelation relation : relationList){
			
			if(relation instanceof Synchronous){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				
				if(secondaryNode.equals(synchronousRelation.getPrimaryNode())){
					listOfAllRelations.add(synchronousRelation);
				}else {
					
					for(Node synchronousRelationSecondary : synchronousRelation.getSecondaryNodeList()){
						
						if(secondaryNode.equals(synchronousRelationSecondary)){
							listOfAllRelations.add(synchronousRelation);
							break;
						}
						
					}
					
				}
				
			}

		}
		
		return listOfAllRelations;
		
	}

	public void showBlockedRelationMessageDialog(Node secondaryNode, ConflictType conflictType){
		
		MessageDialog showBlockedRelationMessageDialog;
		
		switch (conflictType) {
		
			case BEGIN_END_DEFINED:
				
				showBlockedRelationMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment.for.this.secondary") + ": " + secondaryNode.getName(),
						Language.translate("begin.and.end.have.already.been.defined"), "OK", 185);
				showBlockedRelationMessageDialog.showAndWait();
				break;
	
			case BEGIN_DEFINED:
				
				showBlockedRelationMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment.for.this.secondary") + ": " + secondaryNode.getName(),
						Language.translate("begin.has.already.been.defined"), "OK", 185);
				showBlockedRelationMessageDialog.showAndWait();
				break;
				
			case END_DEFINED:
				
				showBlockedRelationMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment.for.this.secondary") + ": " + secondaryNode.getName(),
						Language.translate("end.has.already.been.defined"), "OK", 160);
				showBlockedRelationMessageDialog.showAndWait();
				break;
				
			case NEW_BEGIN_GREATER_THAN_EXISTING_END:
				
				showBlockedRelationMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment.for.this.secondary") + ": " + secondaryNode.getName(),
						Language.translate("new.begin.is.greater.than.the.end.defined.by.another.alignment"), "OK", 180);
				showBlockedRelationMessageDialog.showAndWait();
				break;
				
			case NEW_END_LESS_THAN_EXISTING_BEGIN:
				
				showBlockedRelationMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment.for.this.secondary") + ": " + secondaryNode.getName(),
						Language.translate("new.end.is.less.than.the.begin.defined.by.another.alignment"), "OK", 180);
				showBlockedRelationMessageDialog.showAndWait();
				break;

			case LOOP:

				showBlockedRelationMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment.for.this.secondary") + ": " + secondaryNode.getName(),
						Language.translate("secondary.primary.secondary"), "OK", 180);
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
	
	public void removeInteractivityRelation(Interactivity<MediaNode> interactivityRelation){
		
		relationList.remove(interactivityRelation);
		
		interactivityRelation.getPrimaryNode().setInteractive(false);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.REMOVE_INTERACTIVITY_RELATION, interactivityRelation, this);
        notifyObservers(operation);
        
	}

	public void removeInteractivityRelationFromIterator(Interactivity<MediaNode> interactivityRelation, Iterator<TemporalRelation> iterator){

		iterator.remove();

		interactivityRelation.getPrimaryNode().setInteractive(false);

		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.REMOVE_INTERACTIVITY_RELATION, interactivityRelation, this);
		notifyObservers(operation);

	}

	public ArrayList<TemporalRelation> getRelationList() {
		return relationList;
	}

	public void setRelationList(ArrayList<TemporalRelation> relationList) {
		this.relationList = relationList;
	}
	
	public static int getTemporalViewMediaNumber(){
		return temporalViewNodeNumber;
	}
	
	public ArrayList<ArrayList<Node>> getMediaLineList() {
		return nodeLineList;
	}

	public ArrayList<Node> getNodeListDuringAnother(Node firstSelectedNode, TemporalChainPane temporalChainPane) {
		
		ArrayList<Node> mediaListDuringAnother = new ArrayList<Node>();
		
		for(ArrayList<TimeLineXYChartData> timeLineXYChartDataList : temporalChainPane.getTimeLineXYChartDataLineList()){
			
			for(TimeLineXYChartData timeLineXYChartData : timeLineXYChartDataList){
			
				AllenRelation allenRelation = identifyAllenRelation(firstSelectedNode, timeLineXYChartData.getNode());
				
				if(firstSelectedNode != timeLineXYChartData.getNode()){
					
					if ( !( allenRelation.equals(AllenRelation.BEFORE) || allenRelation.equals(AllenRelation.AFTER) ||
							allenRelation.equals(AllenRelation.MEETS) || allenRelation.equals(AllenRelation.MET_BY) ) ){
						
						mediaListDuringAnother.add(timeLineXYChartData.getNode());
						
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

	public void addInteractivityRelation(Interactivity<MediaNode> interactivityRelation) {
	
		relationList.add(interactivityRelation);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_INTERACTIVITY_RELATION, interactivityRelation, this);
        notifyObservers(operation);
		
	}
	
	public void updateInteractivityRelation(Interactivity<MediaNode> interactivityRelation) {
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.EDIT_INTERACTIVITY_RELATION, interactivityRelation, this);
        notifyObservers(operation);
		
	}

	public ArrayList<SensoryEffectNode> getSensoryEffectNodeAllList() {
		return sensoryEffectNodeAllList;
	}

}
