package gui.temporalViewPanel;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.scene.chart.XYChart;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import model.nclDocument.extendedAna.Media;

public class TemporalMediaInterface {

	private int THUMBNAIL_WIDTH = 90;
	
	private XYChart.Series<Number, String> beginSerie;
	private XYChart.Series<Number, String> endSerie;
	private XYChart.Data<Number, String> beginData;
	private XYChart.Data<Number, String> endData;
	private BorderPane node;
	private double begin;
	private double end;
	private String line;
	private String id;
	private Media media;
	private Logger temporalMediaInterfaceLogger = Logger.getLogger("TemporalMediaInterfaceLogger");
	
	public TemporalMediaInterface(double begin, double end, String line, String id, Media media){
		
		this.id = id;
		this.begin = begin;
		this.end = end-begin;
		this.line = line;
		this.media = media;
		
		initializeComponents();
		
		setListenerEvents();
		
	}

	private void initializeComponents() {
		
		endSerie = new XYChart.Series<Number, String>();
		endSerie.setName(id);
		endData = new XYChart.Data<Number, String>(end, line);
		setBegin();
		node = new BorderPane();
		setNodeLayout();
		endData.setNode(node);
		endSerie.getData().add(endData);
		
		
	}
	
	private void setBegin(){
		
		beginSerie = new XYChart.Series<Number, String>();
		beginData = new XYChart.Data<Number, String>(begin, line);
		BorderPane invisibleNode = new BorderPane();
		invisibleNode.setVisible(false);
		beginData.setNode(invisibleNode);
		beginSerie.getData().add(beginData);
		
	}
	
	private void setListenerEvents(){
		final Glow glow = new Glow();
		glow.setLevel(20);
		node.setEffect(null);
		node.setOnMouseEntered(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	            node.setEffect(glow);
	        }
	    });
	    node.setOnMouseExited(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	            node.setEffect(new Glow(0));
	        }
	    });
	    node.setOnMouseDragged(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
//	        	double newXlower=xAxis.getLowerBound(), newXupper=xAxis.getUpperBound();             
//		        double Delta=0.1;
////		        
//			        if(rectinitX.get() < e.getX()){    
//			            newXlower=xAxis.getLowerBound()-Delta;
//			            newXupper=xAxis.getUpperBound()-Delta;
//			        }
//			    else if(rectinitX.get() > e.getX()){    
//			            newXlower=xAxis.getLowerBound()+Delta;
//			            newXupper=xAxis.getUpperBound()+Delta;
//			        }    
//			        xAxis.setLowerBound( newXlower ); 
//			        xAxis.setUpperBound( newXupper ); 
//	        	 
//
//	        	m1.setXValue(5);
	        	node.setTranslateX(60);
		
		        }
	        
	    });
		
	}

	private void setNodeLayout() {
		
		media.setName(media.getSrc().toString());
		media.setPath(media.getMediaAbsolutePath());
		media.setImportedMediaType(media.identifyType());
		try {
			media.generateMediaIcon();
		} catch (InterruptedException e) {
			temporalMediaInterfaceLogger.log(Level.WARNING, "Fails to generate media icon."+e.getMessage());
		}
		ImageView imageView = media.getMediaIcon();
		Image image = imageView.getImage();
		BackgroundImage backgroundImage = new BackgroundImage(image, null, null, null, null);
		Background background = new Background(backgroundImage);
		
//		BorderPane n = new BorderPane();
//		n.setBackground(background);
//		node.setCenter(n);
		
		node.setBackground(background);

		
	}
	
	public XYChart.Series<Number, String> getBeginSerie() {
		return beginSerie;
	}

	public void setBeginSerie(XYChart.Series<Number, String> beginSerie) {
		this.beginSerie = beginSerie;
	}

	public XYChart.Series<Number, String> getEndSerie() {
		return endSerie;
	}

	public void setEndSerie(XYChart.Series<Number, String> endSerie) {
		this.endSerie = endSerie;
	}

	public XYChart.Data<Number, String> getBeginData() {
		return beginData;
	}

	public void setBeginData(XYChart.Data<Number, String> beginData) {
		this.beginData = beginData;
	}

	public XYChart.Data<Number, String> getEndData() {
		return endData;
	}

	public void setEndData(XYChart.Data<Number, String> endData) {
		this.endData = endData;
	}

	public BorderPane getNode() {
		return node;
	}

	public void setNode(BorderPane node) {
		this.node = node;
	}

	public double getBegin() {
		return begin;
	}

	public void setBegin(double begin) {
		this.begin = begin;
	}

	public double getEnd() {
		return end;
	}

	public void setEnd(double end) {
		this.end = end;
	}

	public String getChannel() {
		return line;
	}

	public void setChannel(String channel) {
		this.line = channel;
	}
	
	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}
	
}
