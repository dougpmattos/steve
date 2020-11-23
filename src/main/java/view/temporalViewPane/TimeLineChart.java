package view.temporalViewPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.ValueAxis;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

@SuppressWarnings({"rawtypes", "unchecked"})
public class TimeLineChart<X, Y> extends StackedBarChart<X, Y>{
	
	private Map<Series, Map<String, List<Data<X, Y>>>> seriesCategoryMap = new HashMap<Series, Map<String, List<Data<X, Y>>>>();
	private CategoryAxis categoryAxis;
	private ValueAxis valueAxis;

	private int seriesDefaultColorIndex = 0;
	private Map<Series<X, Y>, String> seriesDefaultColorMap = new HashMap<Series<X, Y>, String>();
	    
	public TimeLineChart(Axis<X> xAxis, Axis<Y> yAxis) {
		
		super(xAxis, yAxis);
	
        categoryAxis = (CategoryAxis) yAxis;
        valueAxis = (ValueAxis) xAxis;
        
	}
	
	@Override 
	protected void layoutPlotChildren() {
		
        double catSpace = categoryAxis.getCategorySpacing();
        final double availableBarSpace = catSpace - getCategoryGap();
        final double barWidth = availableBarSpace;
        final double barOffset = -((catSpace - getCategoryGap()) / 2);
        
        for (String category : categoryAxis.getCategories()) {
   
            Iterator<Series<X, Y>> seriesIterator = getDisplayedSeriesIterator();
            
            while (seriesIterator.hasNext()) {
            	
                Series<X, Y> series = seriesIterator.next();
                
                for (final Data<X, Y> item : getDataItem(series, category)) {
                	
                    if (item != null) {
                    	
                        final Node bar = item.getNode();
                        final double categoryPos;
                        final double valNumber;
                        final X xValue = getCurrentDisplayedXValue(item);
                        final Y yValue = getCurrentDisplayedYValue(item);
                        
                        final double extraNumber = (double) item.getExtraValue();
                        double bottom;
                        double top;
                        boolean isNegative = bar.getStyleClass().contains("negative");
                        
                        categoryPos = getYAxis().getDisplayPosition(yValue);
                        valNumber = getXAxis().toNumericValue(xValue);

                        if (!isNegative) {
                        	
                            bottom = valueAxis.getDisplayPosition(extraNumber);
                            top = valueAxis.getDisplayPosition(valNumber);
                            
                        } else {
                        	
                            bottom = valueAxis.getDisplayPosition(valNumber);
                            top = valueAxis.getDisplayPosition(extraNumber);
                            
                        }

                        bar.resizeRelocate(bottom, categoryPos + barOffset, top - bottom, barWidth);
                        
                    }
                }
            }
        }
		
	}
	
	private List<Data<X, Y>> getDataItem(Series<X, Y> series, String category) {
		
        Map<String, List<Data<X, Y>>> catmap = seriesCategoryMap.get(series);
        
        return catmap != null ? catmap.get(category) != null ? catmap.get(category) : new ArrayList<Data<X, Y>>() : new ArrayList<Data<X, Y>>();
        
	}
	
	@Override
    protected void dataItemAdded(Series<X, Y> series, int itemIndex, Data<X, Y> item) {
		
        String category = (String) item.getYValue();
        Map<String, List<Data<X, Y>>> categoryMap = seriesCategoryMap.get(series);

        if (categoryMap == null) {
            categoryMap = new HashMap<String, List<Data<X, Y>>>();
            seriesCategoryMap.put(series, categoryMap);
        }

        List<Data<X, Y>> itemList = categoryMap.get(category) != null ? categoryMap.get(category) : new ArrayList<Data<X, Y>>();
        itemList.add(item);
        categoryMap.put(category, itemList);

        Node bar = createBar(series, getData().indexOf(series), item, itemIndex);
        
        if (shouldAnimate()) {
            animateDataAdd(item, bar);
        } else {
            getPlotChildren().add(bar);
        }
        
    }
	
	private Node createBar(Series series, int seriesIndex, final Data item, int itemIndex) {
		
		Node bar = item.getNode();
		
        if (bar == null) {
            bar = new StackPane();
            item.setNode(bar);
        }
        
        String defaultColorStyleClass = seriesDefaultColorMap.get(series);
        bar.getStyleClass().setAll("chart-bar", "series" + seriesIndex, "data" + itemIndex, defaultColorStyleClass);
        
        return bar;
        
    }
	
	private void animateDataAdd(Data<X, Y> item, Node bar) {
		
		double barVal;
	
        barVal = ((Number) item.getXValue()).doubleValue();
        
        if (barVal < 0) {
            bar.getStyleClass().add("negative");
        }
        
        item.setXValue(getXAxis().toRealValue(getXAxis().getZeroPosition()));
        setCurrentDisplayedXValue(item, getXAxis().toRealValue(getXAxis().getZeroPosition()));
        getPlotChildren().add(bar);
        item.setXValue(getXAxis().toRealValue(barVal));
        
        animate(new Timeline( new KeyFrame(Duration.ZERO, new KeyValue(currentDisplayedXValueProperty(item), getCurrentDisplayedXValue(item))),
                    		  new KeyFrame(Duration.millis(700), new KeyValue(currentDisplayedXValueProperty(item), item.getXValue(), Interpolator.EASE_BOTH))));
        
    }
	
	@Override
	protected void seriesAdded(Series<X, Y> series, int seriesIndex) {
		
	        String defaultColorStyleClass = "default-color" + (seriesDefaultColorIndex % 8);
	        seriesDefaultColorMap.put(series, defaultColorStyleClass);
	        seriesDefaultColorIndex++;
	        Map<String, List<Data<X, Y>>> categoryMap = new HashMap<String, List<Data<X, Y>>>();
	        
	        for (int j = 0; j < series.getData().size(); j++) {
	        	
	            Data<X, Y> item = series.getData().get(j);
	            Node bar = createBar(series, seriesIndex, item, j);
	            String category;
	            category = (String) item.getYValue();
	            List<Data<X, Y>> itemList = categoryMap.get(category) != null ? categoryMap.get(category) : new ArrayList<Data<X, Y>>();
	            itemList.add(item);
	            categoryMap.put(category, itemList);
	            
	            if (shouldAnimate()) {
	            	
	                animateDataAdd(item, bar);
	                
	            } else {
	            	
	                double barVal = ((Number)item.getXValue()).doubleValue();
	                
	                if (barVal < 0) {
	                    bar.getStyleClass().add("negative");
	                }
	                getPlotChildren().add(bar);
	            }
	            
	        }
	        
	        if (categoryMap.size() > 0) {
	            seriesCategoryMap.put(series, categoryMap);
	        }
	        
	    }

	    
//	@Override
//	protected void updateAxisRange() {
//
//		boolean categoryIsX = categoryAxis == getXAxis();
//
//        if (categoryAxis.isAutoRanging()) {
//
//            List cData = new ArrayList();
//
//            for (Series<X, Y> series : getData()) {
//                for (Data<X, Y> data : series.getData()) {
//                    if (data != null) cData.add(categoryIsX ? data.getXValue() : data.getYValue());
//                }
//            }
//
//            categoryAxis.invalidateRange(cData);
//
//        }
//
//        if (valueAxis.isAutoRanging()) {
//
//            List<Number> vData = new ArrayList<>();
//            for (String category : categoryAxis.getCategories()) {
//                double totalXN = 0;
//                double totalXP = 0;
//                Iterator<Series<X, Y>> seriesIterator = getDisplayedSeriesIterator();
//                while (seriesIterator.hasNext()) {
//                    Series<X, Y> series = seriesIterator.next();
//                    for (final Data<X, Y> item : getDataItem(series, category)) {
//                        if (item != null) {
//                            boolean isNegative = item.getNode().getStyleClass().contains("negative");
//                            Number value = (Number) (categoryIsX ? item.getYValue() : item.getXValue());
//                            if (!isNegative) {
//                                totalXP += valueAxis.toNumericValue(value);
//                            } else {
//                                totalXN += valueAxis.toNumericValue(value);
//                            }
//                        }
//                    }
//                }
//                vData.add(totalXP);
//                vData.add(totalXN);
//            }
//            valueAxis.invalidateRange(vData);
//
//        }
//
//	}

    @Override
    protected void updateAxisRange() {

        boolean categoryIsX = categoryAxis == getXAxis();

        if (categoryAxis.isAutoRanging()) {

            List cData = new ArrayList();

            for (Series<X, Y> series : getData()) {
                for (Data<X, Y> data : series.getData()) {
                    if (data != null) cData.add(categoryIsX ? data.getXValue() : data.getYValue());
                }
            }

            categoryAxis.invalidateRange(cData);

        }

        if (valueAxis.isAutoRanging()) {

            List<Number> vData = new ArrayList<>();
            for (String category : categoryAxis.getCategories()) {
                double totalXN = 0;
                double totalXP = 0;
                Iterator<Series<X, Y>> seriesIterator = getDisplayedSeriesIterator();
                while (seriesIterator.hasNext()) {
                    Series<X, Y> series = seriesIterator.next();
                    for (final Data<X, Y> item : getDataItem(series, category)) {
                        if (item != null) {
                            boolean isNegative = item.getNode().getStyleClass().contains("negative");
                            Number value = (Number) (categoryIsX ? item.getYValue() : item.getXValue());
                            if (!isNegative) {
                                totalXP += valueAxis.toNumericValue(value);
                            } else {
                                totalXN += valueAxis.toNumericValue(value);
                            }
                        }
                    }
                }
                vData.add(totalXP);
                vData.add(totalXN);
            }
            valueAxis.invalidateRange(vData);

        }

    }

}
