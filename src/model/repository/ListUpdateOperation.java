package model.repository;

import model.nclDocument.extendedAna.Media;

public class ListUpdateOperation {
	
	Media media;
	int operationType;
	
	public ListUpdateOperation(Media media, int operationType){
		
		this.media = media;
		this.operationType = operationType;
		
	}
	
	public ListUpdateOperation(int operationType){
		
		this.operationType = operationType;
		
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}

}
