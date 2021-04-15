package model.common;

import controller.ApplicationController;
import model.temporalView.Interactivity;
import model.temporalView.TemporalChain;
import model.temporalView.TemporalRelation;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.Operation;
import view.common.Language;
import view.common.dialogs.InputDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

public class SpatialTemporalApplication extends Observable implements Serializable{

	private static final long serialVersionUID = 1818548173102220176L;
	
	private ArrayList<TemporalChain> temporalChainList = new ArrayList<TemporalChain>();

	private transient ApplicationController applicationController;
	
	public SpatialTemporalApplication(ApplicationController applicationController){
		this.applicationController = applicationController;
	}

	public void addTemporalChain(TemporalChain temporalChain){
		
		temporalChainList.add(temporalChain);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.ADD_TEMPORAL_CHAIN, temporalChain);
	    notifyObservers(operation);
		
	}
	
	public void removeTemporalChain(TemporalChain temporalChain){
		
		temporalChainList.remove(temporalChain);
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.REMOVE_TEMPORAL_CHAIN, temporalChain);
	    notifyObservers(operation);
	    
	}
	
	public void clearTemporalChainList(){
		
		temporalChainList.clear();
		
		setChanged();
		Operation<TemporalViewOperator> operation = new Operation<TemporalViewOperator>(TemporalViewOperator.CLEAR_TEMPORAL_CHAIN_LIST);
	    notifyObservers(operation);
		
	}
	
	public ArrayList<TemporalChain> getTemporalChainList() {
		return temporalChainList;
		
	}

	public void openExistingSpatialTemporalView(SpatialTemporalApplication existingSpatialTemporalView) {
		
		clearTemporalChainList();
		
		for(TemporalChain existingTemporalChain : existingSpatialTemporalView.getTemporalChainList()){

			TemporalChain temporalChain = new TemporalChain(existingTemporalChain.getName());
			addTemporalChain(temporalChain);

			for(Node existingNode : existingTemporalChain.getNodeAllList()){
				existingNode.setParentTemporalChain(temporalChain);
				applicationController.addNodeTemporalChain(existingNode, temporalChain);
				if(existingNode instanceof MediaNode){
					((MediaNode) existingNode).prefetchExecutionObject(applicationController.getScreen());
				}
			}

			temporalChain.setRelationList(existingTemporalChain.getRelationList());

		}
		
	}

	public Boolean checkInteractivityRelationConsistency(TemporalChain removedTemporalChainModel) {

		Boolean removeTemporalChainAfterCheckConsistency = true;
		
		for(TemporalChain currentTemporalChain : temporalChainList){

			if(currentTemporalChain != removedTemporalChainModel){

				for (Iterator<TemporalRelation> iterator = currentTemporalChain.getRelationList().iterator(); iterator.hasNext();) {

					TemporalRelation currentRelation = iterator.next();

					if (currentRelation instanceof Interactivity) {

						Interactivity currentInteractivityRelation = (Interactivity) currentRelation;
						if(currentInteractivityRelation.getTemporalChainList().contains(removedTemporalChainModel)){

							if(currentInteractivityRelation.getTemporalChainList().size()==1 &&
									currentInteractivityRelation.getSecondaryNodeList().isEmpty()){

								InputDialog showContinueQuestionInputDialog = new InputDialog("The user interaction that starts this timeline will be removed.",
										"Would you like to continue removing " + removedTemporalChainModel.getName() + " anyway?",
										Language.translate("no"), "REMOVE", null, 150);
								String answer = showContinueQuestionInputDialog.showAndWaitAndReturn();
								if(answer.equalsIgnoreCase("right")){
									currentTemporalChain.removeInteractivityRelationFromIterator(currentInteractivityRelation, iterator);
								}else{
									removeTemporalChainAfterCheckConsistency = false;
								}

							}else{

								InputDialog showContinueQuestionInputDialog = new InputDialog("This timeline will be removing from the user interaction it is associated with.",
										"Would you like to continue removing " + removedTemporalChainModel.getName() + " anyway?",
										Language.translate("no"), "REMOVE", null, 160);
								String answer = showContinueQuestionInputDialog.showAndWaitAndReturn();
								if(answer.equalsIgnoreCase("right")){
									currentInteractivityRelation.removeTemporalChain(removedTemporalChainModel);
								}else{
									removeTemporalChainAfterCheckConsistency = false;
								}

							}

						}

					}

				}

			}


		}
		
		return removeTemporalChainAfterCheckConsistency;

	}

}
