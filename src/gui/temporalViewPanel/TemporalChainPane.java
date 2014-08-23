package gui.temporalViewPanel;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.layout.VBox;
import model.nclDocument.extendedAna.Media;
import model.temporalView.TemporalMediaInfo;
import br.uff.midiacom.ana.util.enums.NCLMediaType;

public class TemporalChainPane extends StackedBarChart<Number, String> {
    
	private static CategoryAxis yAxis = new CategoryAxis();
	private static NumberAxis xAxis = new NumberAxis();
	private List<Number> audioLineList;
	private List<Number> videoLineList;
	private List<String> yAxisCategoriesList;
	private List<TemporalMediaInfo> videoTemporalMediainfoList;
	private List<TemporalMediaInfo> audioTemporalMediainfoList;
	private VBox channelPane;
	
	private List<TemporalMediaInfo> mediaInfoList;
    
    public TemporalChainPane(List<TemporalMediaInfo> mediaInfoList, VBox channelPane){
    	
    	super(xAxis, yAxis);
    	
    	audioLineList = new ArrayList<Number>();
    	videoLineList = new ArrayList<Number>();
    	yAxisCategoriesList = new ArrayList<String>();
    	videoTemporalMediainfoList = new ArrayList<TemporalMediaInfo>();
    	audioTemporalMediainfoList = new ArrayList<TemporalMediaInfo>();
    	this.channelPane = channelPane;

        setLegendVisible(false);
        setScaleShape(true);
        
        this.mediaInfoList = mediaInfoList;
        audioLineList.add(0);
        videoLineList.add(0);

        for(int mediaInfoIndex=0; mediaInfoIndex<this.mediaInfoList.size(); mediaInfoIndex++){
        	
        	TemporalMediaInfo temporalMediaInfo = this.mediaInfoList.get(mediaInfoIndex);
        	Media media = temporalMediaInfo.getMedia();
        	media.setPath(media.getMediaAbsolutePath());
        	NCLMediaType mediaType = media.identifyType();
        	
        	if(mediaType == NCLMediaType.AUDIO){
        		audioTemporalMediainfoList.add(temporalMediaInfo);
        	} else {
        		videoTemporalMediainfoList.add(temporalMediaInfo);
        	}
        	
        }
        
        for(TemporalMediaInfo audioTemporalMediaInfo : audioTemporalMediainfoList){
    		addAudioMedia(audioTemporalMediaInfo);
    	}
        
        for(TemporalMediaInfo videoTemporalMediaInfo : videoTemporalMediainfoList){
    		addVideoMedia(videoTemporalMediaInfo);
    	}
        
        configureAxis();

     }

	private void configureAxis() {
		
		yAxis.setTickLabelsVisible(false);
		
        for(int i=0; i<videoLineList.size() + audioLineList.size(); i++){
        	String line = ""+i;
        	yAxisCategoriesList.add(line);
        }
         
        yAxis.setCategories(FXCollections.<String>observableArrayList(yAxisCategoriesList));
        
        xAxis.setSide(Side.TOP);
        
	}
    
    @SuppressWarnings("unchecked")
	private void addAudioMedia(TemporalMediaInfo mediaInfo) {
    	
    	double init = mediaInfo.getStartTime();
    	double end = mediaInfo.getStopTime();
    	String id = mediaInfo.getId();
    	Media media = mediaInfo.getMedia();
    	
    	boolean mediaAdded = false;
    	double lineEnd;
    	int indexLineList = 0;
   
    	while(!mediaAdded && indexLineList < audioLineList.size()){
    		lineEnd = audioLineList.get(indexLineList).doubleValue();
    		if(init >= lineEnd){
    			TemporalMediaInterface temporalMediaInterface = new TemporalMediaInterface(init-lineEnd, end-lineEnd, indexLineList + "", id, media, channelPane);
    			audioLineList.set(indexLineList, end);
    			getData().addAll(temporalMediaInterface.getBeginSerie(), temporalMediaInterface.getEndSerie());
    			mediaAdded = true;
    		}
    		indexLineList++;
    	}
    	if(!mediaAdded){
    		audioLineList.add(end);
    		TemporalMediaInterface temporalMediaInterface = new TemporalMediaInterface(init, end, (audioLineList.size() - 1)+"", id, media, channelPane);
    		getData().addAll(temporalMediaInterface.getBeginSerie(), temporalMediaInterface.getEndSerie());
    	}    
    	
    }
    
    @SuppressWarnings("unchecked")
	private void addVideoMedia(TemporalMediaInfo mediaInfo) {
    	
    	double init = mediaInfo.getStartTime();
    	double end = mediaInfo.getStopTime();
    	String id = mediaInfo.getId();
    	Media media = mediaInfo.getMedia();
    	
    	boolean mediaAdded = false;
    	double lineEnd;
    	int indexLineList = 0;
    	
    	while(!mediaAdded && indexLineList < videoLineList.size()){
    		lineEnd = videoLineList.get(indexLineList).doubleValue();
    		if(init >= lineEnd){
    			TemporalMediaInterface temporalMediaInterface = new TemporalMediaInterface(init-lineEnd, end-lineEnd, indexLineList + audioLineList.size() + "", id, media, channelPane);
    			videoLineList.set(indexLineList, end);
    			getData().addAll(temporalMediaInterface.getBeginSerie(), temporalMediaInterface.getEndSerie());
    			mediaAdded = true;
    		}
    		indexLineList++;
    	}
    	if(!mediaAdded){
    		videoLineList.add(end);
    		TemporalMediaInterface temporalMediaInterface = new TemporalMediaInterface(init, end, (videoLineList.size() + audioLineList.size() - 1)+"", id, media, channelPane);
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

	public int getAudioLineNumber() {
		return audioLineList.size();
	}

	public int getVideoLineNumber() {
		return videoLineList.size();
	}
	
	public double getLineGap(){
		return 50.0;
	}
	
	
}
