package view.temporalViewPane;

import controller.ApplicationController;
import gateway.SensoryEffectExtractionService;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import model.common.MediaNode;
import model.common.SensoryEffectNode;
import model.common.enums.MediaType;
import model.common.enums.SensoryEffectType;
import model.repository.RepositoryMediaList;
import model.temporalView.*;
import model.temporalView.enums.TemporalViewOperator;
import model.utility.MediaUtil;
import model.utility.Operation;
import view.common.Language;
import view.common.dialogs.InputDialog;
import view.common.dialogs.MessageDialog;
import view.common.dialogs.ReturnMessage;
import view.repositoryPane.RepositoryMediaItemContainer;
import view.repositoryPane.RepositoryPane;
import view.stevePane.SteveScene;
import view.utility.AnimationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TimeLineXYChartData implements Observer {

	private static final double BORDER_DIFF = 0.26;

	private ApplicationController applicationController;
	private model.common.Node node;
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
	private SteveScene steveScene;
	private Rectangle mediaImageClip;
	private ImageView imageView;
	private Boolean wasDragged;
	private Button iButton;
	private HBox containerIButton;
	private ContextMenu contextMenu;
	private MenuItem menuItemDeleteInteractivity;
	private MenuItem menuItemEditInteractivity;
	private MenuItem menuItemAddInteractivity;
	private MenuItem menuItemAutoSensoryEffects;
	private SeparatorMenuItem menuItemSeparator;
	private MenuItem menuItemDeleteNode;
	private SensoryEffectExtractionService sensoryEffectExtractionService;
	private InputDialog effectExtractionLoadingDialog;
	private ReturnMessage currentTimePopup;
	private Boolean isBorderBeingDragged;
	private HBox rightBorderRectangle;
	private Label mediaNameLabel;

	public TimeLineXYChartData(ApplicationController applicationController, model.common.Node node, TemporalChain temporalChainModel,
							   TemporalViewPane temporalViewPane, TemporalChainPane temporalChainPane, RepositoryPane repositoryPane,
							   int line, SteveScene steveScene, TimeLineChart<Number, String> timeLineChart) {

		this.applicationController = applicationController;
		this.node = node;
		this.temporalChainModel = temporalChainModel;
		this.line = line;
		this.temporalViewPane = temporalViewPane;
		this.temporalChainPane = temporalChainPane;
		this.timeLineChart = timeLineChart;
		this.repositoryPane = repositoryPane;
		this.steveScene = steveScene;
		wasDragged = false;
		isBorderBeingDragged = false;
		this.repositoryMediaList = repositoryPane.getRepositoryMediaList();

		currentTimePopup = new ReturnMessage(55, 30);

		temporalChainModel.addObserver(this);

		createPopupMenu(applicationController, node, temporalChainModel);

		createXYChartData();

	}

	private void createPopupMenu(ApplicationController applicationController, model.common.Node node, TemporalChain temporalChainModel) {

		contextMenu = new ContextMenu();

		menuItemDeleteInteractivity = new MenuItem(Language.translate("delete.user.interaction"));
		menuItemEditInteractivity = new MenuItem(Language.translate("edit.user.interaction"));
		menuItemAddInteractivity = new MenuItem(Language.translate("add.user.interaction"));
		menuItemAutoSensoryEffects = new MenuItem(Language.translate("add.autoSensoryEffects"));
		menuItemSeparator = new SeparatorMenuItem();
		menuItemDeleteNode = new MenuItem();

		if(node instanceof MediaNode){

			menuItemDeleteNode.setText(Language.translate("delete.media.context.menu"));

			contextMenu.getItems().addAll(menuItemDeleteNode, menuItemSeparator, menuItemAddInteractivity);

			if(((MediaNode) node).type == MediaType.VIDEO){
				contextMenu.getItems().add(new SeparatorMenuItem());
				contextMenu.getItems().add(menuItemAutoSensoryEffects);
			}

		}else {
			menuItemDeleteNode.setText(Language.translate("delete.effect.context.menu"));
			contextMenu.getItems().addAll(menuItemDeleteNode);
		}

		createMenuItemActions(applicationController, node, temporalChainModel);

	}

	private void createMenuItemActions(ApplicationController applicationController, model.common.Node node,
									   TemporalChain temporalChainModel) {

		menuItemDeleteInteractivity.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				for (TemporalRelation relation : temporalChainModel.getRelationList()) {

					if (relation instanceof Interactivity) {

						Interactivity interactivityRelation = (Interactivity) relation;
						if (interactivityRelation.getPrimaryNode() == node) {
							applicationController.removeInteractivityRelation(temporalChainModel, interactivityRelation);
							break;
						}

					}

				}

			}
		});

		menuItemEditInteractivity.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				InteractiveMediaWindow interactiveMediaWindow;
				ArrayList<model.common.Node> nodeListDuringInteractivityTime = temporalViewPane
						.getNodeListDuringInteractivityTime(node);

				Tab selectedTab = null;
				for (Tab tab : temporalViewPane.getTemporalChainTabPane().getTabs()) {
					if (tab.isSelected()) {
						selectedTab = tab;
						break;
					}
				}
				TemporalChainPane temporalChainPane = null;
				if (selectedTab != null) {
					temporalChainPane = (TemporalChainPane) selectedTab.getContent();
				}
				TemporalChain temporalChain = null;
				if (temporalChainPane != null) {
					temporalChain = temporalChainPane.getTemporalChainModel();
				}

				Interactivity interactivityToLoad = null;
				for (TemporalRelation relation : temporalChain.getRelationList()) {

					if (relation instanceof Interactivity) {

						Interactivity interactivityRelation = (Interactivity) relation;
						if (interactivityRelation.getPrimaryNode() == node) {
							interactivityToLoad = interactivityRelation;
							break;
						}

					}

				}

				interactiveMediaWindow = new InteractiveMediaWindow(applicationController, temporalViewPane,
						nodeListDuringInteractivityTime, interactivityToLoad);

				interactiveMediaWindow.showAndWait();

			}
		});

		menuItemAddInteractivity.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				ArrayList<model.common.Node> nodeListDuringInteractivityTime = temporalViewPane
						.getNodeListDuringInteractivityTime();

				// TODO exibir alerta dizendo que nao pode criar interatividade com efeito
				InteractiveMediaWindow interactiveMediaWindow = new InteractiveMediaWindow(applicationController, temporalViewPane,
						(MediaNode) node, nodeListDuringInteractivityTime);
				interactiveMediaWindow.showAndWait();

			}
		});

		menuItemDeleteNode.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				applicationController.removeMediaTemporalChain(node, temporalChainModel, true);
				applicationController.removeNodeOfSpatialView(node);
			}
		});

		menuItemAutoSensoryEffects.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				if (node.getType() != MediaType.VIDEO) {
					MessageDialog messageDialog = new MessageDialog(Language.translate("media.type.not.supported"),
							"OK", 110);
					messageDialog.showAndWait();
					return;
				}

				ExtractEffectsSelectionWindow extractEffectsSelectionWindow = new ExtractEffectsSelectionWindow(
						applicationController, temporalViewPane, (MediaNode) node);
				extractEffectsSelectionWindow.showAndWait();
				if(!extractEffectsSelectionWindow.getHasDiscarded()){

					effectExtractionLoadingDialog = new InputDialog(Language.translate("extracting.effects"),
							null, null, null, null,
							null, 120, 350);
					effectExtractionLoadingDialog.show();
					effectExtractionLoadingDialog.setProgressIndicator();

					List<SensoryEffectType> selectedSensoryEffects = extractEffectsSelectionWindow.getSelectedSensoryEffects();

					sensoryEffectExtractionService  = new SensoryEffectExtractionService(TimeLineXYChartData.this.node, selectedSensoryEffects);
					Thread effectExtractionServiceThread = new Thread(sensoryEffectExtractionService);
					effectExtractionServiceThread.setDaemon(true);
					effectExtractionServiceThread.start();

					Thread callerbackerThread = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								effectExtractionServiceThread.join();
								Platform.runLater(new Runnable() {
									@Override public void run() {
										event.consume();
										callbackFromEffectExtractionService();
									}
								});
							}
							catch ( InterruptedException e ) {
								e.printStackTrace();
							}
						}
					});

					callerbackerThread.start();

				}

			}

		});

	}

	private void callbackFromEffectExtractionService(){

		addSensoryEffectsToTimeline();

		effectExtractionLoadingDialog.close();

		String message = Language.translate("effect.extracted.successfully");

		ReturnMessage returnMessage = new ReturnMessage(message, 450);
		returnMessage.show();
		AnimationUtil.applyFadeInOut(returnMessage);

	}

	private void addSensoryEffectsToTimeline() {

		SEExtractionServiceResponse sEExtractionServiceResponse = sensoryEffectExtractionService.getSEExtractionServiceResponse();
		int[][] effectActivationMatrix = sEExtractionServiceResponse.getEffectActivationMatrix();
		List<SensoryEffectConcept> sensoryEffectsConceptList = sEExtractionServiceResponse.getSensoryEffectsConceptList();

		final int qtdSegs = node.getDuration().intValue();
		final int qtdSensoryEffects = sensoryEffectsConceptList.size();

		// percorrer cada vetor, encontrar momentos de início e fim e gerar um efeito
		// sensorial

		for (int i = 0; i < qtdSensoryEffects; i++) {

			// busca um momento de apresentação desse efeito sensorial

			int j = 0; // j é nosso indicador de ponto nicial que estamos no vetor
			while (j < qtdSegs) {

				// partindo do ponto inicial, vamos buscando os momentos que aquele efeito está
				// presente (i.e. tem 1 na célula do vetor)
				int end = j;
				while (end < qtdSegs) {
					if (effectActivationMatrix[i][end] == 1)
						end++;
					else
						break;
				}

				if (j > end)
					break;
				else if (j != end) {
					addSensoryEffect((SensoryEffectType) sensoryEffectsConceptList.get(i).getFirst(),
							node.getBegin() + j, 0.00 + (end - j)); // (end - j) me dá a duração do
					// efeito
					System.out.println(sensoryEffectsConceptList.get(i).getFirst() + " inicia em : "
							+ (node.getBegin() + j) + " e tem duracao de:" + (0.00 + (end - j)));

				}
				// anda com j até o próximo momento de início desse efeito.
				j = end;
				while (j < qtdSegs) {
					if (effectActivationMatrix[i][j] == 0)
						j++;
					else
						break;
				}

			}

		}

	}

	private int getDuplicatedNodeCount(model.common.Node droppedNode) {

		int i = 0;
		for (model.common.Node node : temporalChainModel.getNodeAllList()) {
			String[] nodeName = node.getName().split("_");
			if (nodeName[0].equalsIgnoreCase(droppedNode.getName())) {
				i++;
			}
		}

		return i;

	}

	private void addSensoryEffect(SensoryEffectType seType, Double start, Double end) {

		SensoryEffectType NEWsensoryEffectType = seType;
		SensoryEffectNode NEWdroppedSensoryEffectNode = new SensoryEffectNode(NEWsensoryEffectType);
		int NEWduplicatedEffectCount;

		NEWdroppedSensoryEffectNode.setName(NEWsensoryEffectType.toString());
		NEWdroppedSensoryEffectNode.setParentTemporalChain(temporalChainModel);

		NEWduplicatedEffectCount = getDuplicatedNodeCount(NEWdroppedSensoryEffectNode);
		if (NEWduplicatedEffectCount > 0) {
			NEWdroppedSensoryEffectNode.setName(NEWsensoryEffectType.toString() + "_" + NEWduplicatedEffectCount++);
		}

		Double NEWdroppedTime = start;
		NEWdroppedTime = MediaUtil.approximateDouble(start);
		NEWdroppedSensoryEffectNode.setBegin(NEWdroppedTime);
		NEWdroppedSensoryEffectNode.setEnd(NEWdroppedTime + end);

		applicationController.addNodeTemporalChain(NEWdroppedSensoryEffectNode, temporalChainModel);

	}

	private void createXYChartData() {

		xyChartData = new XYChart.Data<Number, String>();
		xyChartData.setExtraValue(node.getBegin());
		xyChartData.setXValue(node.getEnd());
		xyChartData.setYValue(String.valueOf(line));
		xyChartData.setNode(createNode());

	}

	private HBox createNode() {

		rightBorderRectangle = new HBox();
		rightBorderRectangle.setId("right-border-rectangle");
		rightBorderRectangle.setMinWidth(10);
		rightBorderRectangle.setMaxWidth(10);

		createMouseEventInRightBorderForConsuming(rightBorderRectangle);
		createEventForChangingDuration(rightBorderRectangle);

		containerNode = new HBox();
		containerNode.setId("temporal-media-container");
		node.setContainerNode(containerNode);

		nameInteractiveIconContainer = new VBox();
		nameInteractiveIconContainer.setId("name-interactive-icon-container");
		nameInteractiveIconContainer.setAlignment(Pos.CENTER_RIGHT);
		mediaNameLabel = new Label(node.getName());
		mediaNameLabel.setId("media-name-label");

		if (node instanceof MediaNode && ((MediaNode) node).isContinousMedia()) {
			mediaNameLabel.setStyle("-fx-background-radius: 0 8 8 0;");
		}

		nameInteractiveIconContainer.getChildren().add(mediaNameLabel);

		mediaImageClip = new Rectangle();
		mediaImageClip.setArcHeight(16);
		mediaImageClip.setArcWidth(16);

		if (node instanceof MediaNode) {
			imageView = ((MediaNode) node).generateMediaIcon();
		} else {
			imageView = ((SensoryEffectNode) node).generateEffectIcon();
		}

		imageView.setClip(mediaImageClip);

		containerNode.heightProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue o, Object oldVal, Object newVal) {
				imageView.setFitHeight((double) newVal);
				rightBorderRectangle.setPrefHeight((double) newVal);
				mediaImageClip.setHeight((double) newVal);
				mediaNameLabel.setPrefHeight((double) newVal);
			}
		});
		containerNode.widthProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue o, Object oldVal, Object newVal) {
				imageView.setFitWidth((double) newVal);
				nameInteractiveIconContainer.setPrefWidth((double) newVal);
				mediaNameLabel.setPrefWidth((double) newVal);
				mediaImageClip.setWidth((double) newVal);
			}
		});

		containerNode.getChildren().add(imageView);
		containerNode.getChildren().add(nameInteractiveIconContainer);

		containerIButton = new HBox();
		containerIButton.setId("i-button-container");
		iButton = new Button();
		iButton.setId("i-button");
		iButton.setTooltip(new Tooltip(Language.translate("edit.user.interaction")));
		iButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				menuItemEditInteractivity.fire();
			}

		});

		containerNode.getChildren().add(containerIButton);

		if (node.isInteractive()) {
			containerIButton.getChildren().add(iButton);
		}

		if ((node instanceof MediaNode && !((MediaNode) node).isContinousMedia())
				|| node instanceof SensoryEffectNode) {
			containerNode.getChildren().add(rightBorderRectangle);
		}

		setListenerEvents(containerNode, repositoryPane);

		return containerNode;

	}

	private void createMouseEventInRightBorderForConsuming(HBox rightBorderRectangle){

		rightBorderRectangle.setOnMousePressed(mouseEvent -> {
			mouseEvent.consume();
		});

		rightBorderRectangle.setOnMouseClicked(mouseEvent -> {
			mouseEvent.consume();
		});

	}

	private void createEventForChangingDuration(HBox rightBorderRectangle) {

		rightBorderRectangle.setOnMouseEntered(mouseEvent -> {
			ApplicationController.getInstance().getSteveScene().setCursor(Cursor.H_RESIZE);
			mouseEvent.consume();
		});

		rightBorderRectangle.setOnMouseExited(mouseEvent -> {

			if(!isBorderBeingDragged){
				ApplicationController.getInstance().getSteveScene().setCursor(Cursor.DEFAULT);
			}
			mouseEvent.consume();

		});

		rightBorderRectangle.setOnMouseReleased(mouseEvent -> {

			isBorderBeingDragged = false;
			ApplicationController.getInstance().getSteveScene().setCursor(Cursor.DEFAULT);
			currentTimePopup.close();

			Double newValue = timeLineChart.getXAxis().getValueForDisplay(mouseEvent.getSceneX()).doubleValue();
			newValue = MediaUtil.approximateDouble(newValue);

			if(!node.getEnd().equals(newValue)){
				ApplicationController.getInstance().updateNodeEndTime(node, newValue, false);
			}
			temporalViewPane.clearSelectedNodeList();
			temporalViewPane.addSelectedNode(node);

			mouseEvent.consume();

		});

		rightBorderRectangle.setOnMouseDragged(mouseEvent -> {

			if(!isSecondaryNodeEndDefinedByRelation()){

				isBorderBeingDragged = true;
				ApplicationController.getInstance().getSteveScene().setCursor(Cursor.H_RESIZE);

				containerNode.toFront();

				Double currentTimeWhileDragging = timeLineChart.getXAxis().getValueForDisplay(mouseEvent.getSceneX()).doubleValue();
				currentTimeWhileDragging = MediaUtil.approximateDouble(currentTimeWhileDragging);

				xyChartData.setXValue(currentTimeWhileDragging);

				currentTimePopup.setMessage(String.valueOf(currentTimeWhileDragging));
				currentTimePopup.setCursor(Cursor.H_RESIZE);
				currentTimePopup.setX(mouseEvent.getSceneX());
				currentTimePopup.setY(mouseEvent.getSceneY() + currentTimePopup.getHeight());
				currentTimePopup.show();

				mouseEvent.consume();

			}else{
				MessageDialog warningMessageDialog = new MessageDialog(Language.translate("it.is.not.possible.to.edit.end.time"),
						Language.translate("end.has.already.been.defined"), "OK", 135);
				warningMessageDialog.showAndWait();

				mouseEvent.consume();
			}

		});

	}

	private void setListenerEvents(HBox containerNode, RepositoryPane repositoryPane) {

		containerNode.setOnMouseExited(mouseEvent -> {
			if(!wasDragged) {
				ApplicationController.getInstance().getSteveScene().setCursor(Cursor.DEFAULT);
			}

		});
		containerNode.setOnMouseEntered(mouseEvent -> {

			if(!wasDragged) {
				ApplicationController.getInstance().getSteveScene().setCursor(Cursor.OPEN_HAND);
			}

		});
		containerNode.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {

				containerNode.requestFocus();
				ApplicationController.getInstance().getSteveScene().setCursor(Cursor.CLOSED_HAND);

				for (Node repositoryMediaItemContainer : repositoryPane.getRepositoryMediaItemContainerListPane()
						.getAllTypes()) {

					RepositoryMediaItemContainer repoMediaItemContainer = (RepositoryMediaItemContainer) repositoryMediaItemContainer;
					repoMediaItemContainer.setSelected(false);
					repoMediaItemContainer.getStylesheets()
							.remove("styles/repositoryPane/mousePressedRepositoryMedia.css");

				}

				if (steveScene.isMetaDown()) {

					if (!temporalViewPane.getSelectedNodeList().contains(TimeLineXYChartData.this.node)) {
						temporalViewPane.addSelectedNode(TimeLineXYChartData.this.node);
					}

					if (TimeLineXYChartData.this.node == temporalViewPane.getFirstSelectedNode()) {

						if (containerNode.getStylesheets().isEmpty()) {
							containerNode.getStylesheets().add("styles/temporalViewPane/mousePressedTemporalMediaNode.css");
						}

					} else {

						containerNode.getStylesheets().add("styles/temporalViewPane/mousePressedSecondaryTemporalMediaNode.css");

					}

				} else {

					temporalViewPane.clearSelectedNodeList();
					temporalViewPane.addSelectedNode(TimeLineXYChartData.this.node);

					if (containerNode.getStylesheets()
							.remove("styles/temporalViewPane/mousePressedSecondaryTemporalMediaNode.css")
							|| containerNode.getStylesheets()
							.remove("styles/temporalViewPane/mousePressedTemporalMediaNode.css")) {
					}

					containerNode.getStylesheets().add("styles/temporalViewPane/mousePressedTemporalMediaNode.css");

				}

				for (Tab temporalTab : temporalViewPane.getTemporalChainTabPane().getTabs()) {

					TemporalChainPane temporalChainPane = (TemporalChainPane) temporalTab.getContent();
					temporalChainPane.getParentTab().setStyle(null);

					for (ArrayList<TimeLineXYChartData> timeLineXYChartDataList : temporalChainPane
							.getTimeLineXYChartDataLineList()) {
						for (TimeLineXYChartData timeLineXYChartData : timeLineXYChartDataList) {
							if (!temporalViewPane.getSelectedNodeList().contains(timeLineXYChartData.getNode())) {

								boolean styleRemoved = false;
								if (timeLineXYChartData.getContainerNode().getStylesheets().remove(
										"styles/temporalViewPane/mousePressedSecondaryTemporalMediaNode.css")) {
									styleRemoved = true;
								}
								if (timeLineXYChartData.getContainerNode().getStylesheets()
										.remove("styles/temporalViewPane/mousePressedTemporalMediaNode.css")) {
									styleRemoved = true;
								}

							}
						}

					}
				}

				mouseEvent.consume();

			}

		});

		containerNode.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {

				if(!isNodeSecondary()){

					containerNode.setTranslateX(mouseEvent.getSceneX() - containerNode.getLayoutX());
					containerNode.toFront();

					Double currentTimeWhileDragging = timeLineChart.getXAxis().getValueForDisplay(mouseEvent.getSceneX()).doubleValue();
					currentTimeWhileDragging = MediaUtil.approximateDouble(currentTimeWhileDragging);

					currentTimePopup.setMessage(String.valueOf(currentTimeWhileDragging));
					currentTimePopup.setCursor(Cursor.CLOSED_HAND);
					currentTimePopup.setX(mouseEvent.getSceneX() - 52);
					currentTimePopup.setY(mouseEvent.getSceneY() + currentTimePopup.getHeight());
					currentTimePopup.show();

					wasDragged = true;

				}else if(isSecondaryNodeBeginDefinedByRelation() && !isSecondaryNodeEndDefinedByRelation()){
					MessageDialog warningMessageDialog = new MessageDialog(Language.translate("cannot.be.dragged"),
							Language.translate("begin.has.already.been.defined"), "OK", 155);
					warningMessageDialog.showAndWait();
				}else if(!isSecondaryNodeBeginDefinedByRelation() && isSecondaryNodeEndDefinedByRelation()){
					MessageDialog warningMessageDialog = new MessageDialog(Language.translate("cannot.be.dragged"),
							Language.translate("end.has.already.been.defined"), "OK", 155);
					warningMessageDialog.showAndWait();
				}else if(isSecondaryNodeBeginDefinedByRelation() && isSecondaryNodeEndDefinedByRelation()){
					MessageDialog warningMessageDialog = new MessageDialog(Language.translate("cannot.be.dragged"),
							Language.translate("begin.and.end.have.already.been.defined"), "OK", 155);
					warningMessageDialog.showAndWait();
				}

			}

		});
		containerNode.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				currentTimePopup.close();
				ApplicationController.getInstance().getSteveScene().setCursor(Cursor.OPEN_HAND);

				if (wasDragged) {

					Double droppedTime = timeLineChart.getXAxis().getValueForDisplay(event.getSceneX()).doubleValue();
					droppedTime = MediaUtil.approximateDouble(droppedTime);

					applicationController.dragMediaTemporalChain(temporalChainModel, TimeLineXYChartData.this.node, droppedTime);

					wasDragged = false;
				}

			}

		});

		containerNode.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {

					for (int i = 0; i < temporalViewPane.getSelectedNodeList().size(); i++) {

						model.common.Node node = temporalViewPane.getSelectedNodeList().get(i);
						applicationController.removeMediaTemporalChain(node, temporalChainModel, true);
						applicationController.removeNodeOfSpatialView(node);
					}

					temporalViewPane.clearSelectedNodeList();

				}

			}

		});

		containerNode.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {

				if (mouseEvent.getButton() == MouseButton.SECONDARY && !contextMenu.isShowing()) {

					if (node instanceof MediaNode && !node.isInteractive()) {

						contextMenu.getItems().remove(menuItemDeleteInteractivity);
						contextMenu.getItems().remove(menuItemEditInteractivity);

						if(!contextMenu.getItems().contains(menuItemAddInteractivity)){
							contextMenu.getItems().add(menuItemAddInteractivity);
						}

					} else if(node instanceof MediaNode && node.isInteractive()){

						contextMenu.getItems().remove(menuItemAddInteractivity);

						contextMenu.getItems().add(menuItemDeleteInteractivity);
						contextMenu.getItems().add(menuItemEditInteractivity);

					}

					contextMenu.show(TimeLineXYChartData.this.containerNode, mouseEvent.getScreenX(), mouseEvent.getScreenY());

				} else {
					contextMenu.hide();
				}

			}

		});

	}

	private boolean isNodeSecondary(){
		return !temporalChainModel.getListOfRelationsWhereNodeIsSecondary(node).isEmpty();
	}
	public XYChart.Data<Number, String> getXYChartData() {
		return xyChartData;
	}

	public model.common.Node getNode() {
		return node;
	}

	public HBox getContainerNode() {
		return containerNode;
	}

	public Rectangle getMediaImageClip() {
		return mediaImageClip;
	}

	public TimeLineChart<Number, String> geTimeLineChart() {
		return timeLineChart;
	}

	public Label getMediaNameLabel() {
		return mediaNameLabel;
	}

	private boolean isSecondaryNodeBeginDefinedByRelation(){

		javafx.scene.Node content = applicationController.getSteveScene().getTemporalViewPane().getTemporalChainTabPane().getSelectionModel().getSelectedItem().getContent();
		TemporalChainPane temporalChainPane = (TemporalChainPane) content;
		TemporalChain temporalChainModel = temporalChainPane.getTemporalChainModel();

		ArrayList<TemporalRelation> listOfRelationsWhereNodeIsSecondary = temporalChainModel.getListOfRelationsWhereNodeIsSecondary(node);

		for(TemporalRelation temporalRelation : listOfRelationsWhereNodeIsSecondary){

			Synchronous synchronousRelation = (Synchronous) temporalRelation;

			if(temporalChainModel.relationDefinesBegin(synchronousRelation)){
				return true;
			}

		}

		return false;

	}

	private boolean isSecondaryNodeEndDefinedByRelation(){

		javafx.scene.Node content = applicationController.getSteveScene().getTemporalViewPane().getTemporalChainTabPane().getSelectionModel().getSelectedItem().getContent();
		TemporalChainPane temporalChainPane = (TemporalChainPane) content;
		TemporalChain temporalChainModel = temporalChainPane.getTemporalChainModel();

		ArrayList<TemporalRelation> listOfRelationsWhereNodeIsSecondary = temporalChainModel.getListOfRelationsWhereNodeIsSecondary(node);

		for(TemporalRelation temporalRelation : listOfRelationsWhereNodeIsSecondary){

			Synchronous synchronousRelation = (Synchronous) temporalRelation;

			if(temporalChainModel.relationDefinesEnd(synchronousRelation)){
				return true;
			}

		}

		return false;

	}

	@Override
	public void update(Observable observable, Object arg) {

		if (observable instanceof TemporalChain) {

			Operation<TemporalViewOperator> operation = (Operation<TemporalViewOperator>) arg;

			switch (operation.getOperator()) {

				case ADD_INTERACTIVITY_RELATION:

					Interactivity<MediaNode> interactivityRelation = (Interactivity<MediaNode>) operation.getOperating();

					if (interactivityRelation.getPrimaryNode() == node) {
						containerIButton.getChildren().add(iButton);
					}

					break;

				case REMOVE_INTERACTIVITY_RELATION:

					interactivityRelation = (Interactivity<MediaNode>) operation.getOperating();

					if (interactivityRelation.getPrimaryNode().getName().equalsIgnoreCase(node.getName())) {

						containerIButton.getChildren().remove(iButton);

					}

					break;

			}

		}

	}

}
