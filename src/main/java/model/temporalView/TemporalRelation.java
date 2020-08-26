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
	
	private Node masterNode;
	private ArrayList<Node> slaveNodeList = new ArrayList<Node>();
	private Boolean explicit = false;
	
	public TemporalRelation(){
		
	}
	
	public void setMasterNode(Node masterNode) {
		this.masterNode = masterNode;
	}
	
	public Node getMasterNode() {
		return masterNode;
	}
	
	public void addSlaveNode(Node slaveNode) {
		slaveNodeList.add(slaveNode);
	}
	
	public void removeSlaveNode(Node slaveNode) {
		
		if(slaveNodeList.remove(slaveNode)){
			
			setChanged();
			Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.REMOVE_SLAVE_NODE_OF_SYNC_RELATION, slaveNode, this);
	        notifyObservers(operation);
	        
		}
	}

	public ArrayList<Node> getSlaveNodeList() {
		return slaveNodeList;
	}
	
	public void setExplicit(Boolean explicit){
		this.explicit = explicit;
	}
	
	public Boolean isExplicit(){
		return explicit;
	}
	
}
