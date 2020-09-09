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
import view.common.dialogs.MessageDialog;
import view.temporalViewPane.TemporalChainPane;
import view.temporalViewPane.TimeLineXYChartData;

@SuppressWarnings("rawtypes")
public class TemporalChain extends Observable implements Serializable {

	private static final long serialVersionUID = -6154510036093880684L;
	final Logger logger = LoggerFactory.getLogger(TemporalChain.class);
	
	private static int temporalChainNumber = 0;
	private static int temporalViewNodeNumber = 0;

	private ArrayList<Node> nodeAllList = new ArrayList<Node>();
	private ArrayList<TemporalRelation> relationList = new ArrayList<TemporalRelation>();

	private int id;
	private String name;
	private Node masterNode;
	private ArrayList<Media> mediaAllList = new ArrayList<Media>();
	private ArrayList<ArrayList<Node>> nodeLineList = new ArrayList<ArrayList<Node>>();

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
	
	public void setMasterNode(Node masterNode){
		
		this.masterNode = masterNode;
		addNode(masterNode);
		
	}
	
	public Node getMasterNode() {
		return masterNode;
	}
	
	public void addNode(Node node){

		if(node instanceof Media){
			mediaAllList.add((Media)node); //TODO depois pode tirar isso e faazer if pra 
			//ver se é efito ou media para crir lista de efeito e de midia separadamente caso necessario
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
		
		mediaAllList.remove(node);
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
	public void updateNodeStartTimeView(Node node, Double newValue, boolean isLinked) {
		dragNode(node, newValue, isLinked);
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

		addElementsInRootNodeList(node, rootNodeList);

		if(rootNodeList.get(0) == node){

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

		}else {

			//INFO offset(draggedTime) to be applied for all nodes (parents and children of the dragged node)
			// linked to the dragged node by user.
			Double draggedTime = node.getDuration() - node.getEnd();

			for(Node rootNode : rootNodeList){

				rootNode.setBegin(rootNode.getBegin() + draggedTime);
				rootNode.setEnd(rootNode.getEnd() + draggedTime);

				removeNode(rootNode, false);
				addNode(rootNode);

				dragChildren(rootNode);

			}

		}

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

	public void dragNode(Node node, Double droppedTime, boolean isLinked) {
		
		ArrayList<Node> rootNodeList = new ArrayList<Node>();
		
		addElementsInRootNodeList(node, rootNodeList);
    		
		if(rootNodeList.get(0) == node){

			node.setBegin(droppedTime);
			if(isLinked){
				node.setEnd(droppedTime + node.getDuration());
			}else{
				node.setDuration(node.getEnd() - node.getBegin());
			}

	    	removeNode(node, false);
	    	addNode(node);
	    	
	    	dragChildren(node);
			
		}else {

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
			
		}
		
	}
	
	private void addElementsInRootNodeList(Node node, ArrayList<Node> rootNodeList){
		
		ArrayList<TemporalRelation> listOfRelationsWhereNodeIsSecondary = getListOfRelationsWhereNodeIsSecondary(node);
		
		if(!listOfRelationsWhereNodeIsSecondary.isEmpty()){
			
			for(TemporalRelation relation : listOfRelationsWhereNodeIsSecondary){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				Media masterMedia = (Media) synchronousRelation.getMasterNode();
				
				addElementsInRootNodeList(masterMedia, rootNodeList);
				
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
		
		for(int i=0; i < relationList.size(); i++){
			
			TemporalRelation relation = relationList.get(i);
			
			if(relation instanceof Synchronous){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				if(synchronousRelation.getMasterNode() == node){
					removeSynchronousRelation(synchronousRelation);
				}else {
					synchronousRelation.removeSlaveNode(node);
					if(synchronousRelation.getSlaveNodeList().isEmpty()){
						removeSynchronousRelation(synchronousRelation);
					}
				}
				
			}else if(relation instanceof Interactivity){
				
				Interactivity<Media> interactivityRelation = (Interactivity<Media>) relation;
				if(interactivityRelation.getMasterNode() == node){
					removeInteractivityRelation(interactivityRelation);
				}else {
					interactivityRelation.removeSlaveNode(node);
					if(interactivityRelation.getSlaveNodeList().isEmpty() && interactivityRelation.getTemporalChainList().isEmpty()){
						removeInteractivityRelation(interactivityRelation);
					}
				}
				
			}
			
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

	public ArrayList<Media> getMediaAllList() {
		return mediaAllList;
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

		Boolean atLeastOneSlaveMediaWasDefined = false;
		int i;
		
		for(i  = 0; i < synchronousRelationToBeDefined.getSlaveNodeList().size(); i++){
			
			Node slaveNode = synchronousRelationToBeDefined.getSlaveNodeList().get(i);
			
			Boolean slaveWasChangedModifyingDuration = false;
			Boolean slaveBlockedForChanges = false;
			
			ArrayList<TemporalRelation> listOfAllRelations = getListOfAllRelations(slaveNode);
			ArrayList<TemporalRelation> listOfSlaveRelations = getListOfRelationsWhereNodeIsSecondary(slaveNode);
			ArrayList<TemporalRelation> listOfMasterRelations = getListOfMasterRelations(slaveNode);
			
			if(!listOfAllRelations.isEmpty()){
	
				if(listOfSlaveRelations.size() == 2){//INFO Uma mídia pode ser escrava de no máximo 2 relações
					
					showBlockedRelationMessageDialog(slaveNode, ConflictType.BEGIN_END_DEFINED);
					slaveBlockedForChanges = true;
					
				} else if(listOfSlaveRelations.size() == 1) { //INFO Apenas uma relação
					
					ConflictType conflictType;
					
					Synchronous synchronousRelationWhereSlaveNodeIsSlave = (Synchronous) listOfSlaveRelations.get(0);
					
					conflictType = hasBeginOrEndAlreadyBeenDefined(synchronousRelationToBeDefined, synchronousRelationWhereSlaveNodeIsSlave);
					
					if(conflictType != null){
						
						showBlockedRelationMessageDialog(slaveNode, conflictType);
						slaveBlockedForChanges = true;
						
					}else {
						
						conflictType = isThereBeginGTEndOrEndLTBeginConflict(synchronousRelationToBeDefined, synchronousRelationWhereSlaveNodeIsSlave, slaveNode);
						
						if(conflictType != null){
							
							showBlockedRelationMessageDialog(slaveNode, conflictType);
							slaveBlockedForChanges = true;
							
						}else {
							
							defineRelationChangingDuration(synchronousRelationToBeDefined, i, slaveNode);
							slaveWasChangedModifyingDuration = true;
							atLeastOneSlaveMediaWasDefined = true;
							
						}
						
					}
					
				}
	
				if(!slaveBlockedForChanges){
					
					if(!listOfMasterRelations.isEmpty()){
						
						if(slaveWasChangedModifyingDuration){
							
							dragChildren(slaveNode);
							
						}else {
							
							defineRelation(synchronousRelationToBeDefined, i, slaveNode);
							atLeastOneSlaveMediaWasDefined = true;
							dragChildren(slaveNode);

						}
						
					}
					
				}else {
					synchronousRelationToBeDefined.getSlaveNodeList().remove(slaveNode);
				}
				
			}else {
			
				defineRelation(synchronousRelationToBeDefined, i, slaveNode);
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

	private void dragChildren(Node rootNode) {
		
		ArrayList<TemporalRelation> listOfMasterRelations = getListOfMasterRelations(rootNode);
		
		for(TemporalRelation relation : listOfMasterRelations){
			
			Synchronous synchronousRelation = (Synchronous) relation;
			
			for(int i = 0; i < synchronousRelation.getSlaveNodeList().size(); i++){
				
				Media slave = (Media) synchronousRelation.getSlaveNodeList().get(i);
				defineRelation(synchronousRelation, i, slave);
				
				dragChildren(slave);
				
			}
	
		}

	}

	private void defineRelationChangingDuration(Synchronous synchronousRelationToBeDefined, int i, Node slaveNode) {
		
		switch(synchronousRelationToBeDefined.getType()){
		
			case STARTS:
				
				slaveNode.setBegin(synchronousRelationToBeDefined.getMasterNode().getBegin());
				removeNode(slaveNode, false);
				addNode(slaveNode);
				break;
				
			case STARTS_DELAY:
				
				slaveNode.setBegin(synchronousRelationToBeDefined.getMasterNode().getBegin() + synchronousRelationToBeDefined.getDelay());
				removeNode(slaveNode, false);
				addNode(slaveNode);
	
				break;
				
			case FINISHES:
	
				slaveNode.setEnd(synchronousRelationToBeDefined.getMasterNode().getEnd());
				slaveNode.setDuration(slaveNode.getEnd() - slaveNode.getBegin());
				removeNode(slaveNode, false);
				addNode(slaveNode);
	
				break;
			
			case FINISHES_DELAY:
	
				slaveNode.setEnd(synchronousRelationToBeDefined.getMasterNode().getEnd() + synchronousRelationToBeDefined.getDelay());
				slaveNode.setDuration(slaveNode.getEnd() - slaveNode.getBegin());
				removeNode(slaveNode, false);
				addNode(slaveNode);
	
				break;
				
			case MEETS:
	
				slaveNode.setBegin(synchronousRelationToBeDefined.getMasterNode().getEnd());
				removeNode(slaveNode, false);
				addNode(slaveNode);
	
				break;
			
			case MEETS_DELAY:
	
				slaveNode.setBegin(synchronousRelationToBeDefined.getMasterNode().getEnd() + synchronousRelationToBeDefined.getDelay());
				removeNode(slaveNode, false);
				addNode(slaveNode);
	
				break;
			
			case MET_BY:
				
				Boolean isPossibleDefineMetBy = true;
	
				slaveNode.setEnd(synchronousRelationToBeDefined.getMasterNode().getBegin());
				slaveNode.setDuration(slaveNode.getEnd() - slaveNode.getBegin());
				
				if(slaveNode.getDuration() == 0){
					isPossibleDefineMetBy = false;
					break;
				}else {
					removeNode(slaveNode, false);
					addNode(slaveNode);
				}
	
				if(!isPossibleDefineMetBy){
					MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
							Language.translate("select.other.media.to.be.the.master"), "OK", 190);
					messageDialog.showAndWait();
				}
				
				break;
			
			case MET_BY_DELAY:
				
				Boolean isPossibleDefineMetByDelay = true;
					
				slaveNode.setEnd(synchronousRelationToBeDefined.getMasterNode().getBegin() + synchronousRelationToBeDefined.getDelay());
				slaveNode.setDuration(slaveNode.getEnd() - slaveNode.getBegin());
				
				if(slaveNode.getDuration() == 0){
					isPossibleDefineMetByDelay = false;
					break;
				}else {
					removeNode(slaveNode, false);
					addNode(slaveNode);
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
	
				slaveNode.setBegin(beforeRelationBegin);
				removeNode(slaveNode, false);
				addNode(slaveNode);
				
				break;
			
		}
		
	}

	//INFO Após passar pela validação das relações (a relação é adicionada de fato no modelo), seta o início e fim das mídias visualmente na cadeia utilizando inicio/fim das mídia mestre e delay conforme o tipo de relação.
	private void defineRelation(Synchronous synchronousRelationToBeDefined, int i, Node slaveNode) {
		
		switch(synchronousRelationToBeDefined.getType()){
		
			case STARTS:
				
				slaveNode.setBegin(synchronousRelationToBeDefined.getMasterNode().getBegin());
				slaveNode.setEnd(slaveNode.getBegin() + slaveNode.getDuration());
				removeNode(slaveNode, false);
				addNode(slaveNode);
				
				break;
				
			case STARTS_DELAY:
				
				slaveNode.setBegin(synchronousRelationToBeDefined.getMasterNode().getBegin() + synchronousRelationToBeDefined.getDelay());
				slaveNode.setEnd(slaveNode.getBegin() + slaveNode.getDuration());
				removeNode(slaveNode, false);
				addNode(slaveNode);

				break;
				
			case FINISHES:

				Double finishesRelationBegin = synchronousRelationToBeDefined.getMasterNode().getEnd() - slaveNode.getDuration();
				if(finishesRelationBegin > 0){
					slaveNode.setBegin(finishesRelationBegin);
				}else {
					slaveNode.setBegin(0.0);
				}
				
				slaveNode.setEnd(synchronousRelationToBeDefined.getMasterNode().getEnd());
				slaveNode.setDuration(slaveNode.getEnd() - slaveNode.getBegin());
				removeNode(slaveNode, false);
				addNode(slaveNode);

				break;
			
			case FINISHES_DELAY:

				Double delayFinishesRelationBegin = synchronousRelationToBeDefined.getMasterNode().getEnd() + synchronousRelationToBeDefined.getDelay() - slaveNode.getDuration();
				if(delayFinishesRelationBegin > 0){
					slaveNode.setBegin(delayFinishesRelationBegin);
				}else {
					slaveNode.setBegin(0.0);
				}
				
				slaveNode.setEnd(synchronousRelationToBeDefined.getMasterNode().getEnd() + synchronousRelationToBeDefined.getDelay());
				slaveNode.setDuration(slaveNode.getEnd() - slaveNode.getBegin());
				removeNode(slaveNode, false);
				addNode(slaveNode);

				break;
				
			case MEETS:

				slaveNode.setBegin(synchronousRelationToBeDefined.getMasterNode().getEnd());
				slaveNode.setEnd(slaveNode.getBegin() + slaveNode.getDuration());
				removeNode(slaveNode, false);
				addNode(slaveNode);

				break;
			
			case MEETS_DELAY:

				slaveNode.setBegin(synchronousRelationToBeDefined.getMasterNode().getEnd() + synchronousRelationToBeDefined.getDelay());
				slaveNode.setEnd(slaveNode.getBegin() + slaveNode.getDuration());
				removeNode(slaveNode, false);
				addNode(slaveNode);

				break;
			
			case MET_BY:
				
				Boolean isPossibleDefineMetBy = true;

				slaveNode.setEnd(synchronousRelationToBeDefined.getMasterNode().getBegin());
				Double begin = slaveNode.getEnd() - slaveNode.getDuration();
				if(begin > 0){
					slaveNode.setBegin(begin);
				}else {
					slaveNode.setBegin(0.0);
				}
				
				slaveNode.setDuration(slaveNode.getEnd() - slaveNode.getBegin());
				if(slaveNode.getDuration() == 0){
					isPossibleDefineMetBy = false;
					break;
				}else {
					removeNode(slaveNode, false);
					addNode(slaveNode);
				}

				if(!isPossibleDefineMetBy){
					MessageDialog messageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment"), 
							Language.translate("select.other.media.to.be.the.master"), "OK", 190);
					messageDialog.showAndWait();
				}
				
				break;
			
			case MET_BY_DELAY:
				
				Boolean isPossibleDefineMetByDelay = true;
					
				slaveNode.setEnd(synchronousRelationToBeDefined.getMasterNode().getBegin() + synchronousRelationToBeDefined.getDelay());
				Double metByDelayRelationBegin = slaveNode.getEnd() - slaveNode.getDuration();
				if(metByDelayRelationBegin > 0){
					slaveNode.setBegin(metByDelayRelationBegin);
				}else {
					slaveNode.setBegin(0.0);
				}
				
				slaveNode.setDuration(slaveNode.getEnd() - slaveNode.getBegin());
				if(slaveNode.getDuration() == 0){
					isPossibleDefineMetByDelay = false;
					break;
				}else {
					removeNode(slaveNode, false);
					addNode(slaveNode);
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

				slaveNode.setBegin(beforeRelationBegin);
				slaveNode.setEnd(slaveNode.getBegin() + slaveNode.getDuration());
				removeNode(slaveNode, false);
				addNode(slaveNode);
				
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
							if(auxSlaveMedia == newRelationSlaveNode){
								newRelationBegin = begin;
								break;
							}
						}else {
							begin = synchronousRelationToBeDefined.getSlaveNodeList().get(i-1).getEnd() + synchronousRelationToBeDefined.getDelay();
							if(auxSlaveMedia == newRelationSlaveNode){
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
					existingRelationEnd = synchronousRelationWhereSlaveNodeIsSlave.getMasterNode().getEnd();
					break;
	
				case FINISHES_DELAY:
					existingRelationEnd = synchronousRelationWhereSlaveNodeIsSlave.getMasterNode().getEnd() + synchronousRelationWhereSlaveNodeIsSlave.getDelay();
					break;
					
				case MET_BY:
					existingRelationEnd = synchronousRelationWhereSlaveNodeIsSlave.getMasterNode().getBegin();
					break;
					
				case MET_BY_DELAY:
					existingRelationEnd = synchronousRelationWhereSlaveNodeIsSlave.getMasterNode().getBegin() + synchronousRelationWhereSlaveNodeIsSlave.getDelay();
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
					existingRelationBegin = synchronousRelationWhereSlaveNodeIsSlave.getMasterNode().getBegin();
					break;
	
				case STARTS_DELAY:
					existingRelationBegin = synchronousRelationWhereSlaveNodeIsSlave.getMasterNode().getBegin() + synchronousRelationWhereSlaveNodeIsSlave.getDelay();
					break;
					
				case MEETS:
					existingRelationBegin = synchronousRelationWhereSlaveNodeIsSlave.getMasterNode().getEnd();
					break;
					
				case MEETS_DELAY:
					existingRelationBegin = synchronousRelationWhereSlaveNodeIsSlave.getMasterNode().getEnd() + synchronousRelationWhereSlaveNodeIsSlave.getDelay();
					break;
					
				case BEFORE:
					 
					ArrayList<Node> auxSlaveMediaList = new ArrayList<Node>(synchronousRelationWhereSlaveNodeIsSlave.getSlaveNodeList());
					Double begin;
					
					for(int i=0; i < auxSlaveMediaList.size(); i++){
		
						Media auxSlaveMedia = (Media) auxSlaveMediaList.get(i);
						
						if(i == 0){
							begin = synchronousRelationWhereSlaveNodeIsSlave.getMasterNode().getEnd() + synchronousRelationWhereSlaveNodeIsSlave.getDelay();
							if(auxSlaveMedia == newRelationSlaveNode){
								existingRelationBegin = begin;
								break;
							}
						}else {
							begin = synchronousRelationWhereSlaveNodeIsSlave.getSlaveNodeList().get(i-1).getEnd() + synchronousRelationWhereSlaveNodeIsSlave.getDelay();
							if(auxSlaveMedia == newRelationSlaveNode){
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

	private ArrayList<TemporalRelation> getListOfMasterRelations(Node slaveNode) {
		
		ArrayList<TemporalRelation> listOfMasterRelations = new ArrayList<TemporalRelation>(); 
		
		for(TemporalRelation relation : relationList){
			
			if(relation instanceof Synchronous){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				
				if(slaveNode.equals(synchronousRelation.getMasterNode())){
					listOfMasterRelations.add(synchronousRelation);
				}
				
			}

		}
		
		return listOfMasterRelations;
		
	}

	private ArrayList<TemporalRelation> getListOfRelationsWhereNodeIsSecondary(Node slaveNode) {
		
		ArrayList<TemporalRelation> listOfSlaveRelations = new ArrayList<TemporalRelation>(); 
		
		for(TemporalRelation relation : relationList){
			
			if(relation instanceof Synchronous){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				
				for(Node synchronousRelationSlave : synchronousRelation.getSlaveNodeList()){
						
					if(slaveNode.equals(synchronousRelationSlave)){
						listOfSlaveRelations.add(synchronousRelation);
						break;
					}
						
				}
				
			}

		}
		
		return listOfSlaveRelations;
		
	}

	private ArrayList<TemporalRelation> getListOfAllRelations(Node slaveNode) {
		
		ArrayList<TemporalRelation> listOfAllRelations = new ArrayList<TemporalRelation>(); 
		
		for(TemporalRelation relation : relationList){
			
			if(relation instanceof Synchronous){
				
				Synchronous synchronousRelation = (Synchronous) relation;
				
				if(slaveNode.equals(synchronousRelation.getMasterNode())){
					listOfAllRelations.add(synchronousRelation);
				}else {
					
					for(Node synchronousRelationSlave : synchronousRelation.getSlaveNodeList()){
						
						if(slaveNode.equals(synchronousRelationSlave)){
							listOfAllRelations.add(synchronousRelation);
							break;
						}
						
					}
					
				}
				
			}

		}
		
		return listOfAllRelations;
		
	}

	public void showBlockedRelationMessageDialog(Node slaveNode, ConflictType conflictType){
		
		MessageDialog showBlockedRelationMessageDialog;
		
		switch (conflictType) {
		
			case BEGIN_END_DEFINED:
				
				showBlockedRelationMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment.for.this.secondary") + ": " + slaveNode.getName(),
						Language.translate("begin.and.end.have.already.been.defined"), "OK", 160);
				showBlockedRelationMessageDialog.showAndWait();
				break;
	
			case BEGIN_DEFINED:
				
				showBlockedRelationMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment.for.this.secondary") + ": " + slaveNode.getName(),
						Language.translate("begin.has.already.been.defined"), "OK", 160);
				showBlockedRelationMessageDialog.showAndWait();
				break;
				
			case END_DEFINED:
				
				showBlockedRelationMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment.for.this.secondary") + ": " + slaveNode.getName(),
						Language.translate("end.has.already.been.defined"), "OK", 160);
				showBlockedRelationMessageDialog.showAndWait();
				break;
				
			case NEW_BEGIN_GREATER_THAN_EXISTING_END:
				
				showBlockedRelationMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment.for.this.secondary") + ": " + slaveNode.getName(),
						Language.translate("new.begin.is.greater.than.the.end.defined.by.another.alignment"), "OK", 180);
				showBlockedRelationMessageDialog.showAndWait();
				break;
				
			case NEW_END_LESS_THAN_EXISTING_BEGIN:
				
				showBlockedRelationMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.define.alignment.for.this.secondary") + ": " + slaveNode.getName(),
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
