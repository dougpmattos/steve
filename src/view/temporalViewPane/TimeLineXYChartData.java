package view.temporalViewPane;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import model.common.Media;
import model.repository.RepositoryMediaList;
import model.temporalView.Interactivity;
import model.temporalView.TemporalRelation;
import model.temporalView.TemporalChain;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.MediaUtil;
import model.utility.Operation;
import view.common.Language;
import view.repositoryPane.RepositoryMediaItemContainer;
import view.repositoryPane.RepositoryPane;
import view.stevePane.StevePane;
import controller.Controller;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TimeLineXYChartData implements Observer {

	private static final double BORDER_DIFF = 0.26;
	
	private Controller controller;
	private Media media;
	private int line;
	private TemporalViewPane temporalViewPane;
	private RepositoryPane repositoryPane;
	private TemporalChain temporalChainModel;
	private XYChart.Data<Number, String> xyChartData;
	private TimeLineChart<Number, String> timeLineChart;
	private HBox containerNode;
	private VBox nameInteractiveIconContainer;
	private TemporalChainPane temporalChainPane;
	private RepositoryMediaList repositoryMediaList;
	private StevePane stevePane;
	private Rectangle mediaImageClip;
	private ImageView imageView;
	private Boolean wasDragged;
	private Button iButton;
	private ContextMenu contextMenu;
	private MenuItem menuItemDeleteInteractivity;
	private MenuItem menuItemEditInteractivity;
	private MenuItem menuItemAddInteractivity;
	private SeparatorMenuItem menuItemSeparator;
	private MenuItem menuItemDeleteMedia;
	
	public TimeLineXYChartData(Controller controller, Media media, TemporalChain temporalChainModel, TemporalViewPane temporalViewPane, 
			TemporalChainPane temporalChainPane, RepositoryPane repositoryPane, int line, StevePane stevePane, TimeLineChart<Number, String> timeLineChart){

		this.controller = controller;
		this.media = media;
		this.temporalChainModel = temporalChainModel;
		this.line = line;
		this.temporalViewPane = temporalViewPane;
		this.temporalChainPane = temporalChainPane;
		this.timeLineChart = timeLineChart;
		this.repositoryPane = repositoryPane;
		this.stevePane = stevePane;
		wasDragged = false;
		this.repositoryMediaList = repositoryPane.getRepositoryMediaList();

		temporalChainModel.addObserver(this);
		
		createPopupMenu(controller, media, temporalChainModel);
		
		createXYChartData();

	}

	private void createPopupMenu(Controller controller, Media media, TemporalChain temporalChainModel) {
		
		contextMenu = new ContextMenu();

		menuItemDeleteInteractivity = new MenuItem (Language.translate("delete.interactivity"));
		menuItemEditInteractivity = new MenuItem (Language.translate("edit.interactivity"));
		menuItemAddInteractivity = new MenuItem (Language.translate("add.interactivity"));
		menuItemSeparator = new SeparatorMenuItem();
		menuItemDeleteMedia = new MenuItem (Language.translate("delete.media.context.menu"));
		
		contextMenu.getItems().addAll(menuItemDeleteMedia, menuItemSeparator, menuItemDeleteInteractivity, menuItemEditInteractivity, menuItemAddInteractivity);
		
		createMenuItemActions(controller, media, temporalChainModel);
		
	}

	private void createMenuItemActions(Controller controller, Media media, TemporalChain temporalChainModel) {
		
		menuItemDeleteInteractivity.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				for(TemporalRelation relation : temporalChainModel.getRelationList()){
					
					if(relation instanceof Interactivity){
						
						Interactivity interactivityRelation = (Interactivity) relation;
						if(interactivityRelation.getMasterMedia() == media){
							controller.removeInteractivityRelation(temporalChainModel, interactivityRelation);
							break;
						}
						
					}
					
				}
				
			}
		});
		
		menuItemEditInteractivity.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				//TODO chamar a janela de interatividade populando o form com a info do modelo ja preecnhida
	    		
				Media firstSelectedMedia = temporalViewPane.getFirstSelectedMedia();
				InteractiveMediaWindow interactiveMediaWindow;
	    		ArrayList<Media> mediaListDuringInteractivityTime = temporalViewPane.getMediaListDuringInteractivityTime();
	    		
    			Tab selectedTab = null;
    			for (Tab tab : temporalViewPane.getTemporalChainTabPane().getTabs()){
    	    		if(tab.isSelected()){
    	    			selectedTab = tab;
    	    			break;
    	    		}
    	    	}
    			TemporalChainPane temporalChainPane = null;
    	    	if(selectedTab != null){
    	    		temporalChainPane = (TemporalChainPane) selectedTab.getContent();
    	    	}
    	    	TemporalChain temporalChain = null;
    	    	if(temporalChainPane != null){
    	    		temporalChain = temporalChainPane.getTemporalChainModel();
    	    	}
    	    	
    	    	Interactivity interactivityToLoad = null;
    			for(TemporalRelation relation : temporalChain.getRelationList()){
					
					if(relation instanceof Interactivity){
						
						Interactivity interactivityRelation = (Interactivity) relation;
						if(interactivityRelation.getMasterMedia() == firstSelectedMedia){
							interactivityToLoad = interactivityRelation;
							break;
						}
						
					}
					
				}
    			
	    		interactiveMediaWindow = new InteractiveMediaWindow(controller, temporalViewPane, mediaListDuringInteractivityTime, interactivityToLoad);
	    		
		    	interactiveMediaWindow.showAndWait();
		    			
			}
		});
		
		menuItemAddInteractivity.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
		    	ArrayList<Media> mediaListDuringInteractivityTime = temporalViewPane.getMediaListDuringInteractivityTime();
		    	
	    		InteractiveMediaWindow interactiveMediaWindow = new InteractiveMediaWindow(controller, temporalViewPane, media, mediaListDuringInteractivityTime);
		    	interactiveMediaWindow.showAndWait();

			}
		});
		
		menuItemDeleteMedia.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				controller.removeMediaTemporalChain(media, temporalChainModel, true);
			}
		});
		
	}

	private void createXYChartData() {
		
		xyChartData = new XYChart.Data<Number, String>();
		xyChartData.setExtraValue(media.getBegin());
		xyChartData.setXValue(media.getEnd());
		xyChartData.setYValue(String.valueOf(line));
		xyChartData.setNode(createNode());
		
	}
	
	private HBox createNode() {
		
		containerNode = new HBox();
		containerNode.setId("temporal-media-container");
	
		nameInteractiveIconContainer = new VBox();
		nameInteractiveIconContainer.setId("name-interactive-icon-container");
		nameInteractiveIconContainer.setAlignment(Pos.CENTER_RIGHT);
		Label mediaName = new Label(media.getName());
		mediaName.setId("media-name");
		nameInteractiveIconContainer.getChildren().add(mediaName);

		mediaImageClip = new Rectangle();
		mediaImageClip.setId("media-image");
		mediaImageClip.setArcHeight(16);
		mediaImageClip.setArcWidth(16);
		imageView = media.generateMediaIcon();
		imageView.setClip(mediaImageClip);
		
		if(media.isInteractive()){
			iButton = new Button();
    		iButton.setId("i-button");
    		
    		iButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					
					Interactivity<Media, ?> interactivityRelation = null;
					
					for(TemporalRelation relation : temporalChainModel.getRelationList()){
						
						if(relation instanceof Interactivity){
							
							interactivityRelation = (Interactivity) relation;
							if(interactivityRelation.getMasterMedia() == media){
								controller.removeInteractivityRelation(temporalChainModel, interactivityRelation);
								break;
							}
							
						}
						
					}
			
					for(XYChart.Data<Number, String> xyChartData : temporalChainPane.getSerie().getData()){
						
						HBox containerNode = (HBox) xyChartData.getNode();
						VBox nameInteractiveIconContainer = (VBox) containerNode.getChildren().get(1);
						Label mediaLabel = (Label) nameInteractiveIconContainer.getChildren().get(0);
						
						for(Media media : interactivityRelation.getSlaveMediaList()){
							
							if(mediaLabel.getText().equalsIgnoreCase(media.getName())){
								
								if(!containerNode.getStylesheets().contains("view/temporalViewPane/styles/borderOfMediaToBeStopped.css")){
									
									containerNode.getStylesheets().add("view/temporalViewPane/styles/borderOfMediaToBeStopped.css");
									ImageView imageView = (ImageView) containerNode.getChildren().get(0);
									Rectangle mediaImageClip = (Rectangle) imageView.getClip();
									mediaImageClip.setHeight(mediaImageClip.getHeight()-5);
									
								}
								
							}
							
						}
						
					}
					
					
				}
				
			});
			nameInteractiveIconContainer.getChildren().add(iButton);
		}

		containerNode.heightProperty().addListener(new ChangeListener(){
			@Override 
	        public void changed(ObservableValue o,Object oldVal, Object newVal){
				
					imageView.setFitHeight((double) newVal);

					if(containerNode.getStylesheets().isEmpty()){
						mediaImageClip.setHeight((double) newVal);
					}else {
						mediaImageClip.setHeight((double) newVal-5);
					}	
					
			}
	    });
		containerNode.widthProperty().addListener(new ChangeListener(){
			@Override 
	        public void changed(ObservableValue o,Object oldVal, Object newVal){
				imageView.setFitWidth((double) newVal);
				nameInteractiveIconContainer.setPrefWidth((double) newVal);
				mediaName.setPrefWidth((double) newVal);
				mediaImageClip.setWidth((double) newVal);
			}
	    });
		
		containerNode.getChildren().add(imageView);
		containerNode.getChildren().add(nameInteractiveIconContainer);
		
		setListenerEvents(containerNode, repositoryPane);
		
		return containerNode;
		
	}
	
	private void setListenerEvents(HBox node, RepositoryPane repositoryPane){
		
		node.setOnMousePressed(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent mouseEvent) {

				node.requestFocus();
				
				for(Node repositoryMediaItemContainer : repositoryPane.getRepositoryMediaItemContainerListPane().getAllTypes()){
					
					RepositoryMediaItemContainer repoMediaItemContainer = (RepositoryMediaItemContainer) repositoryMediaItemContainer;
					repoMediaItemContainer.setSelected(false);
	        		repoMediaItemContainer.getStylesheets().remove("view/repositoryPane/styles/mousePressedRepositoryMedia.css");

				}				
				
				if(stevePane.isMetaDown()){
					
					if(!temporalViewPane.getSelectedMediaList().contains(media)){
						temporalViewPane.addSelectedMedia(media);
					}
					
					if(media == temporalViewPane.getFirstSelectedMedia()){

						if(node.getStylesheets().isEmpty()){
							mediaImageClip.setHeight(containerNode.getHeight()-5);
							node.getStylesheets().add("view/temporalViewPane/styles/mousePressedTemporalMediaNode.css");
						}
						
					}else {
						
						if(node.getStylesheets().isEmpty()){
							mediaImageClip.setHeight(containerNode.getHeight()-5);
							node.getStylesheets().add("view/temporalViewPane/styles/mousePressedSlaveTemporalMediaNode.css");
						}

					}
	
				}else {
			
					temporalViewPane.clearSelectedMedia();
					temporalViewPane.addSelectedMedia(media);
					
					if(node.getStylesheets().remove("view/temporalViewPane/styles/borderOfMediaToBeStopped.css") ||
					   node.getStylesheets().remove("view/temporalViewPane/styles/mousePressedSlaveTemporalMediaNode.css") ||
					   node.getStylesheets().remove("view/temporalViewPane/styles/mousePressedTemporalMediaNode.css")){
						
						mediaImageClip.setHeight(mediaImageClip.getHeight()+5);
						
					}
					
					node.getStylesheets().add("view/temporalViewPane/styles/mousePressedTemporalMediaNode.css");
					mediaImageClip.setHeight(mediaImageClip.getHeight()-5);
					
				}
				
				for(Tab temporalTab : temporalViewPane.getTemporalChainTabPane().getTabs()){
					
					TemporalChainPane temporalChainPane = (TemporalChainPane) temporalTab.getContent();
					temporalChainPane.getParentTab().setStyle(null);
					
					for(ArrayList<TimeLineXYChartData> timeLineXYChartDataList : temporalChainPane.getTimeLineXYChartDataLineList()){
						for(TimeLineXYChartData timeLineXYChartData : timeLineXYChartDataList){
							if(!temporalViewPane.getSelectedMediaList().contains(timeLineXYChartData.getMedia())){

								if(timeLineXYChartData.getContainerNode().getStylesheets().remove("view/temporalViewPane/styles/mousePressedSlaveTemporalMediaNode.css")||
								   timeLineXYChartData.getContainerNode().getStylesheets().remove("view/temporalViewPane/styles/mousePressedTemporalMediaNode.css") ||
								   timeLineXYChartData.getContainerNode().getStylesheets().remove("view/temporalViewPane/styles/borderOfMediaToBeStopped.css")){
									
									timeLineXYChartData.getMediaImageClip().setHeight(timeLineXYChartData.getMediaImageClip().getHeight()+5);
									
								}

							}
						}
						
					}	
				}
			
			    mouseEvent.consume();
			    
			}
	    	
	    });
		
	    node.setOnMouseDragged(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent mouseEvent) {
	        	node.setTranslateX(mouseEvent.getSceneX() - node.getLayoutX());
	        	node.toFront();
	        	wasDragged = true;
	        }
	        
	    });
	    node.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				
				if(wasDragged){
					
					Double droppedTime = timeLineChart.getXAxis().getValueForDisplay(event.getSceneX()).doubleValue();
	        		droppedTime = MediaUtil.approximateDouble(droppedTime);

	        		controller.dragMediaTemporalChain(temporalChainModel, media, droppedTime);
					
					wasDragged = false;
				}

			}
	    	
	    });
	    
	    node.setOnKeyReleased(new EventHandler<KeyEvent>() {
	    	
	    	@Override
			public void handle(KeyEvent event) {
	    		
	    		if(event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE){
	    			
	    			for(int i=0; i<temporalViewPane.getSelectedMediaList().size(); i++){
	    				
	    				Media media = temporalViewPane.getSelectedMediaList().get(i);
		    			controller.removeMediaTemporalChain(media, temporalChainModel, true);
		    			
	    			}
	    	
	    		}

	    	}
	    	
	    });
	    
	    node.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent mouseEvent) {
				
				if (mouseEvent.getButton() == MouseButton.SECONDARY && !contextMenu.isShowing()) {

					if(!media.isInteractive()){
						menuItemDeleteInteractivity.setDisable(true); 
						menuItemEditInteractivity.setDisable(true);
						menuItemAddInteractivity.setDisable(false);
					}else{
						menuItemDeleteInteractivity.setDisable(false); 
						menuItemEditInteractivity.setDisable(false);
						menuItemAddInteractivity.setDisable(true);
					}

					contextMenu.show(containerNode, mouseEvent.getScreenX(), mouseEvent.getScreenY());
		
			     }else {
			    	 contextMenu.hide();
			     }
				
			}
			
	    });
		
	}

	public XYChart.Data<Number, String> getXYChartData() {
		return xyChartData;
	}
	
	public Media getMedia(){
		return media;
	}
	
	public HBox getContainerNode(){
		return containerNode;
	}
	
	public Rectangle getMediaImageClip(){
		return mediaImageClip;
	}
	
	public TimeLineChart<Number, String> geTimeLineChart(){
		return timeLineChart;
	}
	
	@Override
	public void update(Observable observable, Object arg) {
		
		if(observable instanceof TemporalChain){
			
			Operation<TemporalViewOperator> operation = (Operation<TemporalViewOperator>) arg;

			switch(operation.getOperator()){
			
		        case ADD_INTERACTIVITY_RELATION:
		        	
		        	Interactivity<Media, ?> interactivityRelation = (Interactivity<Media, ?>) operation.getOperating();
	
		        	if(interactivityRelation.getMasterMedia() == media){
		        		
		        		iButton = new Button();
		        		iButton.setId("i-button");
		        		
		        		iButton.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								
								for(XYChart.Data<Number, String> xyChartData : temporalChainPane.getSerie().getData()){
									
									HBox containerNode = (HBox) xyChartData.getNode();
									VBox nameInteractiveIconContainer = (VBox) containerNode.getChildren().get(1);
									Label mediaLabel = (Label) nameInteractiveIconContainer.getChildren().get(0);
									
									for(Media media : interactivityRelation.getSlaveMediaList()){
										
										if(mediaLabel.getText().equalsIgnoreCase(media.getName())){
											
											if(!containerNode.getStylesheets().contains("view/temporalViewPane/styles/borderOfMediaToBeStopped.css")){
												
												containerNode.getStylesheets().add("view/temporalViewPane/styles/borderOfMediaToBeStopped.css");
												ImageView imageView = (ImageView) containerNode.getChildren().get(0);
												Rectangle mediaImageClip = (Rectangle) imageView.getClip();
												mediaImageClip.setHeight(mediaImageClip.getHeight()-5);
												
											}
											
										}
										
									}
									
								}
								
								
							}
							
						});
		        		
		        		nameInteractiveIconContainer.getChildren().add(iButton);
		        		
		        	}
		 
		        	if(interactivityRelation.getSlaveMediaList().contains(media)){
		        		containerNode.getStylesheets().add("view/temporalViewPane/styles/borderOfMediaToBeStopped.css");
		        		mediaImageClip.setHeight(mediaImageClip.getHeight()-5);
		        	}
		        	  	
		            break;
		            
		        	case REMOVE_INTERACTIVITY_RELATION:
					
						interactivityRelation = (Interactivity<Media, ?>) operation.getOperating();
						
						if(interactivityRelation.getMasterMedia().getName().equalsIgnoreCase(media.getName())){
							
							nameInteractiveIconContainer.getChildren().remove(iButton);
							
						}
						
					break;
		            	
			}
			
		}
		
	}
	
}
