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
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import model.common.Media;
import model.common.SensoryEffect;
import model.repository.RepositoryMediaList;
import model.temporalView.Interactivity;
import model.temporalView.TemporalChain;
import model.temporalView.TemporalRelation;
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
	private model.common.Node applicationNode;
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
	
	public TimeLineXYChartData(Controller controller, model.common.Node node, TemporalChain temporalChainModel, TemporalViewPane temporalViewPane, 
			TemporalChainPane temporalChainPane, RepositoryPane repositoryPane, int line, StevePane stevePane, TimeLineChart<Number, String> timeLineChart){

		this.controller = controller;
		this.applicationNode = node;
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
		
		createPopupMenu(controller, node, temporalChainModel);
		
		createXYChartData();

	}

	private void createPopupMenu(Controller controller, model.common.Node node, TemporalChain temporalChainModel) {
		
		contextMenu = new ContextMenu();

		menuItemDeleteInteractivity = new MenuItem (Language.translate("delete.interactivity"));
		menuItemEditInteractivity = new MenuItem (Language.translate("edit.interactivity"));
		menuItemAddInteractivity = new MenuItem (Language.translate("add.interactivity"));
		menuItemSeparator = new SeparatorMenuItem();
		menuItemDeleteMedia = new MenuItem (Language.translate("delete.media.context.menu"));
		
		contextMenu.getItems().addAll(menuItemDeleteMedia, menuItemSeparator, menuItemDeleteInteractivity, menuItemEditInteractivity, menuItemAddInteractivity);
		
		createMenuItemActions(controller, node, temporalChainModel);
		
	}

	private void createMenuItemActions(Controller controller, model.common.Node node, TemporalChain temporalChainModel) {
		
		menuItemDeleteInteractivity.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				for(TemporalRelation relation : temporalChainModel.getRelationList()){
					
					if(relation instanceof Interactivity){
						
						Interactivity interactivityRelation = (Interactivity) relation;
						if(interactivityRelation.getMasterNode() == node){
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
	    		
				model.common.Node firstSelectedMedia = temporalViewPane.getFirstSelectedNode();
				InteractiveMediaWindow interactiveMediaWindow;
	    		ArrayList<model.common.Node> nodeListDuringInteractivityTime = temporalViewPane.getNodeListDuringInteractivityTime();
	    		
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
						if(interactivityRelation.getMasterNode() == firstSelectedMedia){
							interactivityToLoad = interactivityRelation;
							break;
						}
						
					}
					
				}
    			
	    		interactiveMediaWindow = new InteractiveMediaWindow(controller, temporalViewPane, nodeListDuringInteractivityTime, interactivityToLoad);
	    		
		    	interactiveMediaWindow.showAndWait();
		    			
			}
		});
		
		menuItemAddInteractivity.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
		    	ArrayList<model.common.Node> nodeListDuringInteractivityTime = temporalViewPane.getNodeListDuringInteractivityTime();
		    	
		    	//TODO exibir alerta dizendo que nao pode criar interatividade com efeito sensrial. Só com midia.
	    		InteractiveMediaWindow interactiveMediaWindow = new InteractiveMediaWindow(controller, temporalViewPane, (Media)node, nodeListDuringInteractivityTime);
		    	interactiveMediaWindow.showAndWait();

			}
		});
		
		menuItemDeleteMedia.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				controller.removeMediaTemporalChain(node, temporalChainModel, true);
			}
		});
		
	}

	private void createXYChartData() {
		
		xyChartData = new XYChart.Data<Number, String>();
		xyChartData.setExtraValue(applicationNode.getBegin());
		xyChartData.setXValue(applicationNode.getEnd());
		xyChartData.setYValue(String.valueOf(line));
		xyChartData.setNode(createNode());
		
	}
	
	private HBox createNode() {
		
		containerNode = new HBox();
		containerNode.setId("temporal-media-container");
	
		nameInteractiveIconContainer = new VBox();
		nameInteractiveIconContainer.setId("name-interactive-icon-container");
		nameInteractiveIconContainer.setAlignment(Pos.CENTER_RIGHT);
		Label mediaName = new Label(applicationNode.getName());
		mediaName.setId("media-name");
		nameInteractiveIconContainer.getChildren().add(mediaName);

		mediaImageClip = new Rectangle();
		mediaImageClip.setId("media-image");
		mediaImageClip.setArcHeight(16);
		mediaImageClip.setArcWidth(16);
		
		if(applicationNode instanceof Media){
			imageView = ((Media) applicationNode).generateMediaIcon();
		}else{
			imageView = ((SensoryEffect) applicationNode).generateEffectIcon();
		}
	
		imageView.setClip(mediaImageClip);
		
		if(applicationNode.isInteractive()){
			iButton = new Button();
    		iButton.setId("i-button");
    		
    		iButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					
					Interactivity<Media> interactivityRelation = null;
					
					for(TemporalRelation relation : temporalChainModel.getRelationList()){
						
						if(relation instanceof Interactivity){
							
							interactivityRelation = (Interactivity) relation;
							if(interactivityRelation.getMasterNode() == applicationNode){
								controller.removeInteractivityRelation(temporalChainModel, interactivityRelation);
								break;
							}
							
						}
						
					}
			
					for(XYChart.Data<Number, String> xyChartData : temporalChainPane.getSerie().getData()){
						
						HBox containerNode = (HBox) xyChartData.getNode();
						VBox nameInteractiveIconContainer = (VBox) containerNode.getChildren().get(1);
						Label mediaLabel = (Label) nameInteractiveIconContainer.getChildren().get(0);
						
						for(model.common.Node media : interactivityRelation.getSlaveNodeList()){
							
							if(mediaLabel.getText().equalsIgnoreCase(media.getName())){
								
								if(!containerNode.getStylesheets().contains("styles/temporalViewPane/borderOfMediaToBeStopped.css")){
									
									containerNode.getStylesheets().add("styles/temporalViewPane/borderOfMediaToBeStopped.css");
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
	        		repoMediaItemContainer.getStylesheets().remove("styles/repositoryPane/mousePressedRepositoryMedia.css");

				}				
				
				if(stevePane.isMetaDown()){
					
					if(!temporalViewPane.getSelectedNodeList().contains(applicationNode)){
						temporalViewPane.addSelectedNode(applicationNode);
					}
					
					if(applicationNode == temporalViewPane.getFirstSelectedNode()){

						if(node.getStylesheets().isEmpty()){
							mediaImageClip.setHeight(containerNode.getHeight()-5);
							node.getStylesheets().add("styles/temporalViewPane/mousePressedTemporalMediaNode.css");
						}
						
					}else {
						
						if(node.getStylesheets().isEmpty()){
							mediaImageClip.setHeight(containerNode.getHeight()-5);
							node.getStylesheets().add("styles/temporalViewPane/mousePressedSlaveTemporalMediaNode.css");
						}

					}
	
				}else {
			
					temporalViewPane.clearSelectedMedia();
					temporalViewPane.addSelectedNode(applicationNode);
					
					if(node.getStylesheets().remove("styles/temporalViewPane/borderOfMediaToBeStopped.css") ||
					   node.getStylesheets().remove("styles/temporalViewPane/mousePressedSlaveTemporalMediaNode.css") ||
					   node.getStylesheets().remove("styles/temporalViewPane/mousePressedTemporalMediaNode.css")){
						
						mediaImageClip.setHeight(mediaImageClip.getHeight()+5);
						
					}
					
					node.getStylesheets().add("styles/temporalViewPane/mousePressedTemporalMediaNode.css");
					mediaImageClip.setHeight(mediaImageClip.getHeight()-5);
					
				}
				
				for(Tab temporalTab : temporalViewPane.getTemporalChainTabPane().getTabs()){
					
					TemporalChainPane temporalChainPane = (TemporalChainPane) temporalTab.getContent();
					temporalChainPane.getParentTab().setStyle(null);
					
					for(ArrayList<TimeLineXYChartData> timeLineXYChartDataList : temporalChainPane.getTimeLineXYChartDataLineList()){
						for(TimeLineXYChartData timeLineXYChartData : timeLineXYChartDataList){
							if(!temporalViewPane.getSelectedNodeList().contains(timeLineXYChartData.getNode())){

								boolean styleRemoved = false;
								if(timeLineXYChartData.getContainerNode().getStylesheets().remove("styles/temporalViewPane/mousePressedSlaveTemporalMediaNode.css")){
									styleRemoved = true;
								}
								if(timeLineXYChartData.getContainerNode().getStylesheets().remove("styles/temporalViewPane/mousePressedTemporalMediaNode.css")){
									styleRemoved = true;
								}
								if(timeLineXYChartData.getContainerNode().getStylesheets().remove("styles/temporalViewPane/borderOfMediaToBeStopped.css")){
									styleRemoved = true;
								}
								if(styleRemoved){
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

	        		controller.dragMediaTemporalChain(temporalChainModel, applicationNode, droppedTime);
					
					wasDragged = false;
				}

			}
	    	
	    });
	    
	    node.setOnKeyReleased(new EventHandler<KeyEvent>() {
	    	
	    	@Override
			public void handle(KeyEvent event) {
	    		
	    		if(event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE){
	    			
	    			for(int i=0; i<temporalViewPane.getSelectedNodeList().size(); i++){
	    				
	    				model.common.Node node = temporalViewPane.getSelectedNodeList().get(i);
		    			controller.removeMediaTemporalChain(node, temporalChainModel, true);
		    			
	    			}
	    	
	    		}

	    	}
	    	
	    });
	    
	    node.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent mouseEvent) {
				
				if (mouseEvent.getButton() == MouseButton.SECONDARY && !contextMenu.isShowing()) {

					if(!applicationNode.isInteractive()){
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
	
	public model.common.Node getNode(){
		return applicationNode;
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
		        	
		        	Interactivity<Media> interactivityRelation = (Interactivity<Media>) operation.getOperating();
	
		        	if(interactivityRelation.getMasterNode() == applicationNode){
		        		
		        		iButton = new Button();
		        		iButton.setId("i-button");
		        		iButton.setTooltip(new Tooltip(Language.translate("edit.interactivity")));
		        		
		        		iButton.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								menuItemEditInteractivity.fire();
								
								//INFO caso decida por mostrar as bordas da interatividade na interface e remover a opcao de editar por essa botao
								/*for(XYChart.Data<Number, String> xyChartData : temporalChainPane.getSerie().getData()){
									
									HBox containerNode = (HBox) xyChartData.getNode();
									VBox nameInteractiveIconContainer = (VBox) containerNode.getChildren().get(1);
									Label mediaLabel = (Label) nameInteractiveIconContainer.getChildren().get(0);
									
									for(Media media : interactivityRelation.getSlaveMediaList()){
									
						        		if(interactivityRelation.getTemporalChainList().contains(temporalChainPane.getTemporalChainModel())){
						        			temporalChainPane.getParentTab().setStyle("-fx-border-color: #00BFA5;-fx-border-width: 2; -fx-border-radius: 8; -fx-padding: -5, -5, -5, -5;");
						        		}
							        	
										if(mediaLabel.getText().equalsIgnoreCase(media.getName())){
											
											if(!containerNode.getStylesheets().contains("view/temporalViewPane/styles/borderOfMediaToBeStopped.css")){
												
												containerNode.getStylesheets().add("view/temporalViewPane/styles/borderOfMediaToBeStopped.css");
												ImageView imageView = (ImageView) containerNode.getChildren().get(0);
												Rectangle mediaImageClip = (Rectangle) imageView.getClip();
												mediaImageClip.setHeight(mediaImageClip.getHeight()-5);
												
											}
											
										}
										
									}
									
								}*/
								
								
							}
							
						});
		        		
		        		nameInteractiveIconContainer.getChildren().add(iButton);
		        		
		        	}
		 
		        	if(interactivityRelation.getSlaveNodeList().contains(applicationNode)){
		        		containerNode.getStylesheets().add("styles/temporalViewPane/borderOfMediaToBeStopped.css");
		        		mediaImageClip.setHeight(mediaImageClip.getHeight()-5);
		        	}
		        	  	
		            break;
		            
		        	case REMOVE_INTERACTIVITY_RELATION:
					
						interactivityRelation = (Interactivity<Media>) operation.getOperating();
						
						if(interactivityRelation.getMasterNode().getName().equalsIgnoreCase(applicationNode.getName())){
							
							nameInteractiveIconContainer.getChildren().remove(iButton);
							
						}
						
					break;
		            	
			}
			
		}
		
	}
	
}
