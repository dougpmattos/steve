package gui.temporalViewPanel;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import model.nclDocument.extendedAna.Media;
import model.temporalView.TemporalMediaInfo;

public class TemporalChainPane extends StackedBarChart<Number, String>{

	private static final String MEDIA = "Media";
	private static final String TIME = "Time";
    
	private static final CategoryAxis yAxis = new CategoryAxis();
	private static final NumberAxis xAxis = new NumberAxis();
	private static List<Number> lineList = new ArrayList<Number>();
	private static List<String> yAxisCategoriesList = new ArrayList<String>();
	
	private List<TemporalMediaInfo> mediaInfoList;
    
    public TemporalChainPane(List<TemporalMediaInfo> mediaInfoList){
    	
    	super(xAxis, yAxis);
     
        setLegendVisible(false);
        setScaleShape(true);
        
        this.mediaInfoList = mediaInfoList;
        lineList.add(0);
        
        for(int mediaInfoIndex=0; mediaInfoIndex<this.mediaInfoList.size(); mediaInfoIndex++){
     	   addMedia(this.mediaInfoList.get(mediaInfoIndex), mediaInfoIndex);
        }
        
        configureAxis();
        
     }

	private void configureAxis() {
		
		yAxis.setLabel(MEDIA);
        for(int i=0; i<lineList.size(); i++){
        	String line = ""+i;
        	yAxisCategoriesList.add(line);
        }
        yAxis.setCategories(FXCollections.<String>observableArrayList(yAxisCategoriesList));
        
        xAxis.setLabel(TIME);
        
	}
    
    @SuppressWarnings("unchecked")
	private void addMedia(TemporalMediaInfo mediaInfo, int mediaInfoIndex) {
    	
    	double init = mediaInfo.getStartTime();
    	double end = mediaInfo.getStopTime();
    	String id = mediaInfo.getId();
    	Media media = mediaInfo.getMedia();
    	
    	boolean mediaAdded = false;
    	double lineEnd;
    	int indexLineList = 0;
    	
    	while(!mediaAdded && indexLineList < lineList.size()){
    		lineEnd = lineList.get(indexLineList).doubleValue();
    		if(init >= lineEnd){
    			TemporalMediaInterface temporalMediaInterface = new TemporalMediaInterface(init-lineEnd, end-lineEnd, indexLineList+"", id, media);
    			lineList.set(indexLineList, end);
    			getData().addAll(temporalMediaInterface.getBeginSerie(), temporalMediaInterface.getEndSerie());
    			mediaAdded = true;
    		}
    		indexLineList++;
    	}
    	if(!mediaAdded){
    		lineList.add(end);
    		TemporalMediaInterface temporalMediaInterface = new TemporalMediaInterface(init, end, (lineList.size()-1)+"", id, media);
    		getData().addAll(temporalMediaInterface.getBeginSerie(), temporalMediaInterface.getEndSerie());
    	}    
    	
    }
    
    public List<TemporalMediaInfo> getMediaInfoList() {
		return mediaInfoList;
	}

	public void setMediaInfoList(List<TemporalMediaInfo> mediaInfoList) {
		this.mediaInfoList = mediaInfoList;
	}

	public static CategoryAxis getYaxis() {
		return yAxis;
	}

	public static NumberAxis getXaxis() {
		return xAxis;
	}

}
