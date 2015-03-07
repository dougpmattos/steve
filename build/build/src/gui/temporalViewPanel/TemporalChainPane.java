package gui.temporalViewPanel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import model.temporalView.AllenRelation;
import model.temporalView.TemporalMediaInfo;

public class TemporalChainPane {
    
	private final int CATEGORY_GAP = 3;
	private CategoryAxis yAxis = new CategoryAxis();
	private NumberAxis xAxis = new NumberAxis();
	private HashMap<Number, List<TemporalMediaInfo>> lineList;
	private List<String> yAxisCategoriesList;
	private List<TemporalMediaInfo> mediaInfoList;
	private StackedBarChart<Number, String> stackedBarChart;;
    
    public TemporalChainPane(List<TemporalMediaInfo> mediaInfoList){
    	
    	stackedBarChart = new StackedBarChart<Number, String>(xAxis, yAxis);
    	
    	yAxis.setTickMarkVisible(false);
        yAxis.setTickLabelsVisible(false);
        xAxis.setSide(Side.TOP);
        xAxis.setAutoRanging(false);

        lineList = new  HashMap<Number, List<TemporalMediaInfo>>();
    	yAxisCategoriesList = new ArrayList<String>();
    	
    	stackedBarChart.setHorizontalGridLinesVisible(false);
    	stackedBarChart.setLegendVisible(false);
    	stackedBarChart.setScaleShape(true);
    	stackedBarChart.setCategoryGap(CATEGORY_GAP);
        
        this.mediaInfoList = mediaInfoList;

        loadTemporalMedia();

     }

	private void loadTemporalMedia() {
		
		if(!mediaInfoList.isEmpty()){
			quickSort(mediaInfoList,0,mediaInfoList.size() - 1);

	        for(TemporalMediaInfo videoTemporalMediaInfo : mediaInfoList){
	    		addMedia(videoTemporalMediaInfo);
	    	}
		}
  
	}
        
    private void addMedia(TemporalMediaInfo mediaInfo) {

    	boolean mediaAdded = false;
    	AllenRelation allenRelation;
    	int lineListIndex = 0;
    	
    	while(!mediaAdded && lineListIndex < lineList.size()){
    		boolean isPossibleAdd = true;
    		List<TemporalMediaInfo> mediaList = lineList.get(lineListIndex);
    		int mediaListIndex = 0;
    		while(isPossibleAdd && mediaListIndex < mediaList.size()){
    			TemporalMediaInfo currentMediaInfo = mediaList.get(mediaListIndex);
    			allenRelation = identifyAllenRelation(mediaInfo, currentMediaInfo);
    			
    			if( (!allenRelation.equals(AllenRelation.MEETS)) &&  (!allenRelation.equals(AllenRelation.MET_BY))
    			&& (!allenRelation.equals(AllenRelation.BEFORE)) && (!allenRelation.equals(AllenRelation.AFTER))){
    				
    				isPossibleAdd = false;
    				
    			}
    			
    			mediaListIndex++;
    		}
    		
    		if(isPossibleAdd){
    			
    			List<TemporalMediaInfo> temporalMediaInfoList = lineList.get(lineListIndex);
    			temporalMediaInfoList.add(mediaInfo);
    			lineList.put(lineListIndex, temporalMediaInfoList);
    			
    			addTemporalMediaInterface(mediaInfo, lineListIndex, lineList);
    			
    			mediaAdded = true;
    			
    		}
    		
    		lineListIndex++;
    	}
    	
    	if(!mediaAdded){
    		
    		int newLineIndex = createNewLine(lineList);
    		List<TemporalMediaInfo> temporalMediaInfo = new ArrayList<TemporalMediaInfo>();
    		temporalMediaInfo.add(mediaInfo);
    		lineList.put(newLineIndex, temporalMediaInfo);
    		yAxisCategoriesList.add(Integer.toString(newLineIndex));
    		yAxis.setCategories(FXCollections.<String>observableArrayList(yAxisCategoriesList));
    		
    		addTemporalMediaInterface(mediaInfo, newLineIndex, lineList);
    		
    	}
    	    	
    }
    
    private int createNewLine(HashMap<Number, List<TemporalMediaInfo>>lineList) {
		return lineList.size();
	}

	@SuppressWarnings("unchecked")
	private void addTemporalMediaInterface(TemporalMediaInfo mediaInfo, int lineListIndex, HashMap<Number, List<TemporalMediaInfo>> lineList) {
		
		double begin = mediaInfo.getStartTime();
    	double end = mediaInfo.getStopTime();
    	String previousMediaEndStringValue = Double.toString(getPreviousMediaEnd(mediaInfo, lineList.get(lineListIndex)));
    	BigDecimal previousMediaEnd = new BigDecimal(previousMediaEndStringValue);
    	
		TemporalMediaInterface temporalMediaInterface = new TemporalMediaInterface(begin, previousMediaEnd, end, lineListIndex + "", mediaInfo.getId(), mediaInfo.getMedia(), this);
		stackedBarChart.getData().addAll(temporalMediaInterface.getBeginSerie(), temporalMediaInterface.getEndSerie());
		
	}
    
    private void quickSort(List<TemporalMediaInfo> list, int begin, int end) {
        int middle = partition(list, begin, end);
        if (begin < (middle-1))
            quickSort(list, begin, middle-1);
        if (middle < end)
            quickSort(list, middle, end);
    }
     
    private int partition(List<TemporalMediaInfo> list, int begin, int end) {
        int i = begin, j = end;
        TemporalMediaInfo mediaTemp;
        TemporalMediaInfo pivotMedia = list.get((begin+end)/2);
        Double pivot = pivotMedia.getStartTime();
        while (i <= j) {
            while( ( (Double) list.get(i).getStartTime()) < pivot )
                i++;
            while( ( (Double) list.get(j).getStartTime()) > pivot )
                j--;
            if(i <= j){
            	mediaTemp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, mediaTemp);
                i++;
                j--;
            }
        }
        return i;
    }
	
	private double getPreviousMediaEnd(TemporalMediaInfo mediaInfo, List<TemporalMediaInfo> temporalMediaInfoList) {
		
		TemporalMediaInfo currentMediaInfo;
		TemporalMediaInfo previousMediaInfo = null;
		for(int i=0; i<temporalMediaInfoList.size(); i++){
			currentMediaInfo = temporalMediaInfoList.get(i);
			if(i == 0 && currentMediaInfo == mediaInfo){
				return 0;
			}else if(currentMediaInfo == mediaInfo){
				previousMediaInfo = temporalMediaInfoList.get(i-1);
			}
		}
		
		return previousMediaInfo.getStopTime();
		
	}

	private AllenRelation identifyAllenRelation(TemporalMediaInfo mediaInfo, TemporalMediaInfo currentMediaInfo) {
		
		double begin = mediaInfo.getStartTime();
		double end = mediaInfo.getStopTime();
		double currentMediaBegin = currentMediaInfo.getStartTime();
		double currentMediaEnd = currentMediaInfo.getStopTime();
		
		if(end == currentMediaBegin){
			return AllenRelation.MEETS;
		}else if(begin == currentMediaEnd){
			return AllenRelation.MET_BY;
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
		}else if(begin == currentMediaBegin && end == currentMediaEnd){
			return AllenRelation.EQUALS;
		}else{
			return null;
		}
				
	}

	public StackedBarChart<Number, String> getStackedBarChart() {
		return stackedBarChart;
	}

	public void setStackedBarChart(StackedBarChart<Number, String> stackedBarChart) {
		this.stackedBarChart = stackedBarChart;
	}
	
	public HashMap<Number, List<TemporalMediaInfo>> getLineList() {
		return lineList;
	}

	public void setLineList(HashMap<Number, List<TemporalMediaInfo>> videoLineList) {
		this.lineList = videoLineList;
	}

	public List<TemporalMediaInfo> getMediaInfoList() {
		return mediaInfoList;
	}

	public void setMediaInfoList(List<TemporalMediaInfo> mediaInfoList) {
		this.mediaInfoList = mediaInfoList;
	}

	public int getVideoLineNumber() {
		return lineList.size();
	}
	
	public double getLineGap(){
		return 50.0;
	}
	
	public void setXAxisLength(double value){
		xAxis.setUpperBound(value);
	}
	
	public double getXAxisLength(){
		return xAxis.getUpperBound();
	}
	
	public double getLastMediaTime(){
		
		double lastMediaTime = 0;
		
		for(int i=0; i < lineList.size(); i++){
			List<TemporalMediaInfo> mediaList = lineList.get(i);
			for(TemporalMediaInfo media : mediaList){
				double currentMediaTime = media.getStopTime();
				if(currentMediaTime > lastMediaTime){
					lastMediaTime = currentMediaTime;
				}
			}
		}
		
		return lastMediaTime;
		
	}

	public void setXAxisTickLabelsVisible(boolean value) {
		
		xAxis.setTickLabelsVisible(value);
		
	}


	public void setXAxisTickLength(int value) {
		
		xAxis.setTickLength(value);
		
	}
	
	
}
