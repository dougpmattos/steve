package model.temporalView;

import java.io.Serializable;
import java.util.ArrayList;

import model.common.Node;
import model.common.Relation;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.Operation;

@SuppressWarnings("rawtypes")

public abstract class TemporalRelation extends Relation implements Serializable {

	private static final long serialVersionUID = 3044752885230388480L;
	
	private Node primaryNode;
	private ArrayList<Node> secondaryNodeList = new ArrayList<Node>();
	private Boolean explicit = false;
	
	public TemporalRelation(){
		
	}
	
	public void setPrimaryNode(Node primaryNode) {
		this.primaryNode = primaryNode;
	}
	
	public Node getPrimaryNode() {
		return primaryNode;
	}
	
	public void addSecondaryNode(Node secondaryNode) {
		secondaryNodeList.add(secondaryNode);
	}
	
	public void removeSlaveNode(Node secondaryNode) {
		
		if(secondaryNodeList.remove(secondaryNode)){
			
			setChanged();
			Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.REMOVE_SECONDARY_NODE_OF_SYNC_RELATION, secondaryNode, this);
	        notifyObservers(operation);
	        
		}
	}

	public ArrayList<Node> getSecondaryNodeList() {
		return secondaryNodeList;
	}
	
	public void setExplicit(Boolean explicit){
		this.explicit = explicit;
	}
	
	public Boolean isExplicit(){
		return explicit;
	}
	
}
