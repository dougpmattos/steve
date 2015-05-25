package model.spatialView;

public class VideoProperty extends PresentationProperty {

	private static final long serialVersionUID = -6052072653126955619L;
	
	private String width;
	private String height;
	private AspectRatio aspectRatio;
	
	public VideoProperty(){
		
		super();
		
		width = "100%";
		height = "100%";
		aspectRatio = AspectRatio.SLICE;
		
	}
	
	public void setWidth(String width){
		this.width = width;
	}
	
	public String getWidth(){
		return width;
	}
	
	public void setHeight(String height){
		this.height = height;
	}
	
	public String getHeight(){
		return height;
	}
	
	public void setAspectRatio(AspectRatio aspectRatio){
		this.aspectRatio = aspectRatio;
	}
	
	public AspectRatio getAspectRatio(){
		return aspectRatio;
	}
	
}
