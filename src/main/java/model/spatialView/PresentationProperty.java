package model.spatialView;

import model.common.Fade;

import java.io.Serializable;

public class PresentationProperty<T> implements Serializable {

	private static final long serialVersionUID = -6443982987578515921L;
	
	public T positionProperty;
	private Fade fade;
	
	public PresentationProperty(){
		
	}

	public Fade getFade() {
		return fade;
	}

	public void setFade(Fade fade) {
		this.fade = fade;
	}
	
}
