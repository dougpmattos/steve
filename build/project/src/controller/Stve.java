
package controller;

import gui.spatialViewPanel.SpatialViewPane;
import gui.temporalViewPanel.TemporalChainPane;
import gui.temporalViewPanel.ZoomButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import model.NCLSupport.HTG.HTGCondition;
import model.NCLSupport.HTG.HTGEdge;
import model.NCLSupport.HTG.HTGVertice;
import model.NCLSupport.HTG.HypermediaTemporalGraph;
import model.NCLSupport.NCLDocument.AreaList;
import model.NCLSupport.NCLDocument.ContextList;
import model.NCLSupport.NCLDocument.LinkList;
import model.NCLSupport.NCLDocument.MediaList;
import model.NCLSupport.extendedAna.Area;
import model.NCLSupport.extendedAna.Doc;
import model.NCLSupport.extendedAna.Media;
import model.NCLSupport.presentationPlan.PresentationPlan;
import model.NCLSupport.utility.BindUtil;
import model.NCLSupport.utility.ConnectorUtil;
import model.NCLSupport.utility.HtgUtil;
import model.NCLSupport.utility.LinkUtil;
import model.temporalView.TemporalMediaInfo;
import model.temporalView.TemporalView;
import br.uff.midiacom.ana.connector.NCLAction;
import br.uff.midiacom.ana.connector.NCLAssessmentStatement;
import br.uff.midiacom.ana.connector.NCLCausalConnector;
import br.uff.midiacom.ana.connector.NCLCompoundAction;
import br.uff.midiacom.ana.connector.NCLCompoundCondition;
import br.uff.midiacom.ana.connector.NCLCompoundStatement;
import br.uff.midiacom.ana.connector.NCLCondition;
import br.uff.midiacom.ana.connector.NCLConnectorParam;
import br.uff.midiacom.ana.connector.NCLSimpleAction;
import br.uff.midiacom.ana.connector.NCLSimpleCondition;
import br.uff.midiacom.ana.connector.NCLStatement;
import br.uff.midiacom.ana.link.NCLBind;
import br.uff.midiacom.ana.link.NCLLink;
import br.uff.midiacom.ana.util.ElementList;
import br.uff.midiacom.ana.util.enums.NCLDefaultConditionRole;
import br.uff.midiacom.ana.util.enums.NCLEventAction;
import br.uff.midiacom.ana.util.enums.NCLEventType;
import br.uff.midiacom.ana.util.enums.NCLMediaType;
import br.uff.midiacom.ana.util.exception.XMLException;

/**
 *
 * @author Douglas
 */
public class Stve extends Scene {
    
	private static final int DEFAULT_AXIS_LENGTH = 10;
	private static final int CHANNEL_WIDTH = 160;
	private static final long serialVersionUID = 1177049012114416958L;
	private static final String EDITOR_TITLE = "STVEN";
    Doc nclDoc;
    MediaList mediaList;
    AreaList areaList;
    LinkList linkList;
    ContextList contextList;
    HypermediaTemporalGraph htg;
    PresentationPlan presentationPlan;
    TemporalView temporalView;
    HTGVertice inputVertice = null,outputVertice = null;
    BorderPane temporalViewPane;
    TabPane temporalViewTabPane;
    TabPane repoPropAnimTabPane;
    TemporalChainPane videoTemporalChain;
    TemporalChainPane audioTemporalChain;
    VBox temporalChainPane;
    Repository repository;
    SpatialViewPane spatialViewPanel;
    SplitPane splitPaneRepoSpatial, splitPane;
    MenuBar menuBar;
    int i = 0;
    static BorderPane containerBorderPane = new BorderPane();
    
   public Stve() throws XMLException, IOException  {
       
	   super(containerBorderPane);
	   containerBorderPane.setPrefSize(1200, 620);
	   
//       openNCLDocument();
//       collectDataForHTG();
//       constructHTG();
//       generatePresentationPlan();
       
//       createTemporalView();
       createGUI();
       
   }
   
   private void openNCLDocument() throws XMLException {
	   nclDoc = new Doc();
	   nclDoc.loadXML(new File("NCL Documents/musica/musicAd.ncl"));
	   //nclDoc.loadXML(new File("C:\\Users\\Douglas\\Workspace\\STVEN\\stven\\NCL Documents\\apresentacao\\ex.ncl"));
   }
   
   

   private void collectDataForHTG() throws XMLException, IOException {
       contextList = new ContextList(nclDoc); 
       mediaList = new MediaList(nclDoc,contextList);
       linkList = new LinkList(nclDoc, contextList);
   }
   
   
   
   
private void constructHTG() throws XMLException, IOException {
       htg = new HypermediaTemporalGraph();
       constructMediaVertices();
       constructAnchorVertices();
       constructLinkEdges();
       htg.generateDOTFile();
   }
    
   private void constructMediaVertices() throws XMLException, IOException {
       for (Media media : mediaList){
           if(!media.hasRefer() && !media.isApplicationType()){
               HTGVertice v1 = new HTGVertice(NCLEventAction.START.toString(), media.getId(), NCLEventType.PRESENTATION.toString());
               htg.addVertice(v1);
               HTGVertice v2 = new HTGVertice(NCLEventAction.STOP.toString(), media.getId(), NCLEventType.PRESENTATION.toString());
               htg.addVertice(v2);
               if(media.getDuration()!=null){
                   HTGCondition c = new HTGCondition(media);
                   HTGEdge e = new HTGEdge(v1, v2, c);
                   htg.addEdge(e);
               } 
           }  
       }
    }
   
   private void constructAnchorVertices() throws XMLException, IOException {
        for (Media media : mediaList){
            if(!media.hasRefer() && !media.isApplicationType()){
                areaList = new AreaList(nclDoc,media.getId());
                for (Area area : areaList) {
                    HTGVertice ancV1 = new HTGVertice(NCLEventAction.START.toString(), area.getId(), NCLEventType.PRESENTATION.toString());
                    htg.addVertice(ancV1);
                    HTGCondition c1 = new HTGCondition(area,area.getAreaBegin(),Boolean.FALSE);
                    HTGEdge e1 = new HTGEdge(htg.getVertice(NCLEventAction.START.toString(),media.getId(),NCLEventType.PRESENTATION.toString()), ancV1, c1);
                    htg.addEdge(e1);
                    if(area.getAreaEnd()!=null){
                        HTGVertice ancV2 = new HTGVertice(NCLEventAction.STOP.toString(), area.getId(), NCLEventType.PRESENTATION.toString());
                        htg.addVertice(ancV2);
                        HTGCondition c2 = new HTGCondition(area,area.getAreaEnd(),Boolean.TRUE);
                        HTGEdge e2 = new HTGEdge(ancV1, ancV2, c2);
                        htg.addEdge(e2);
                    }
                }
            }
        }
    }
   
@SuppressWarnings("rawtypes")
private void constructLinkEdges() throws XMLException {
       NCLCausalConnector causalConnector;
       for (NCLLink link : linkList) {
           causalConnector = LinkUtil.getConnector(link);
           if(ConnectorUtil.hasCompound(causalConnector)){
               constructCompoundLinkEdges(link);
           }else{
               constructSimpleLinkEdges(link);
           }
       }  
   }
   
@SuppressWarnings("rawtypes")
private void constructSimpleLinkEdges(NCLLink link){
      HTGCondition condition = new HTGCondition(new Double(0.0));
      List<HTGVertice> outputVerticeList = new ArrayList<HTGVertice>();
      HTGCondition noDelayCondition = null;
      HTGEdge edge;
      NCLCausalConnector connector = LinkUtil.getConnector(link);
      constructSimpleCondition(condition,link);
      constructSimpleAction(outputVerticeList,link);
      try {
          noDelayCondition = condition.clone();
      } catch (CloneNotSupportedException ex) {
          Logger.getLogger(Stve.class.getName()).log(Level.SEVERE, null, ex);
      }
      for (int j = 0; j < outputVerticeList.size(); j++) {
          outputVertice = outputVerticeList.get(j);
          String simpleActionRole = outputVertice.getAction();
          NCLSimpleAction outputVerticeSimpleAction = ConnectorUtil.getSimpleAction(connector, simpleActionRole);
          Object objectDelay = outputVerticeSimpleAction.getDelay();
          if(objectDelay!=null){
              condition.addDelay(objectDelay,link);
              edge = new HTGEdge(inputVertice, outputVertice, condition);
          }else{
              edge = new HTGEdge(inputVertice, outputVertice, noDelayCondition);
          }
          htg.addEdge(edge);
      }
   }
   
@SuppressWarnings("rawtypes")
private void constructCompoundLinkEdges(NCLLink link) throws XMLException{
       HTGCondition condition = new HTGCondition(new Double(0.0));
       HTGCondition noDelayCondition = null;
       HTGEdge edge;
       NCLCausalConnector connector = LinkUtil.getConnector(link);
       List<HTGVertice> outputVerticeList = new ArrayList<HTGVertice>();
       if(ConnectorUtil.hasCompoundCondition(connector)){
           constructCompoundCondition(condition,link);
       }else{//condicao simples, sem compoundCondition.
           constructSimpleCondition(condition,link);
       }
       if(ConnectorUtil.hasCompoundAction(connector)){
           constructCompoundAction(outputVerticeList,link);
       }else{//acoes simples, sem compoundAction.
           constructSimpleAction(outputVerticeList,link);
       }
       try {
           noDelayCondition = condition.clone();
       } catch (CloneNotSupportedException ex) {
           Logger.getLogger(Stve.class.getName()).log(Level.SEVERE, null, ex);
       }
       for (int j = 0; j < outputVerticeList.size(); j++) {
          outputVertice = outputVerticeList.get(j);
          String simpleActionRole = HtgUtil.getSimpleActionRole(outputVertice.getAction(),outputVertice.getEventType());
          NCLSimpleAction outputVerticeSimpleAction = ConnectorUtil.getSimpleAction(connector, simpleActionRole);
          Object objectDelay = outputVerticeSimpleAction.getDelay();
          if(objectDelay!=null){
              condition.addDelay(objectDelay,link);
              edge = new HTGEdge(inputVertice, outputVertice, condition);
          }else{
              edge = new HTGEdge(inputVertice, outputVertice, noDelayCondition);
          }
          htg.addEdge(edge);
      }
   }
 
@SuppressWarnings("rawtypes")
private void constructSimpleCondition(HTGCondition condition, NCLLink link){
       NCLSimpleCondition simpleCondition = (NCLSimpleCondition)LinkUtil.getConnector(link).getCondition();
       NCLBind bind;
       ArrayList<NCLBind> referencedBindList = HtgUtil.getReferencedBinds(simpleCondition,link); 
       for (int i = 0; i < referencedBindList.size(); i++) {
          HTGVertice defaultConditionVertice = null;
          bind = (NCLBind) referencedBindList.get(i);
          if(i==0){
              String roleName;
              roleName = BindUtil.getRoleName(bind);
              if(roleName.equalsIgnoreCase(NCLDefaultConditionRole.ONSELECTION.toString())){
                  HTGVertice v  = htg.getVertice(BindUtil.getEventAction(bind), getAnchorId(bind), NCLEventType.PRESENTATION.toString());
                  inputVertice = new HTGVertice(BindUtil.getEventAction(bind), getAnchorId(bind), BindUtil.getEventType(bind));
                  htg.addVertice(inputVertice);
                  HTGCondition selectionCondition = new HTGCondition();
                  selectionCondition.addInteractivity(link, bind);
                  HTGEdge e = new HTGEdge(v, inputVertice, selectionCondition);
                  htg.addEdge(e);
              }else {
                  inputVertice = htg.getVertice(BindUtil.getEventAction(bind),getAnchorId(bind),BindUtil.getEventType(bind));
              }
          }else{//adiciona outros objetos associados a mesma condicao(att max e qual)
              defaultConditionVertice = htg.getVertice(BindUtil.getEventAction(bind),getAnchorId(bind),BindUtil.getEventType(bind));
              if(BindUtil.roleIsDefaultCondition(bind)){
                  if(defaultConditionVertice==null){
                      //TODO Adiciona outros objetos associados a mesma condicao(att max e qual)
                  }
                  condition.addDefaultCondition(defaultConditionVertice, simpleCondition, null);
              }else{
                  //TODO Condicao com papel definido pelo autor.
              }
          }
       }
   }
   
@SuppressWarnings("rawtypes")
private void constructSimpleAction(List<HTGVertice> outputVerticeList, NCLLink link){
      NCLSimpleAction simpleAction = (NCLSimpleAction)LinkUtil.getConnector(link).getAction();
      NCLBind bind;
      ArrayList<NCLBind> referencedBindList = HtgUtil.getReferencedBinds(simpleAction,link); 
      for (int i = 0; i < referencedBindList.size(); i++) {
          bind = (NCLBind) referencedBindList.get(i);
          if(BindUtil.getEventType(bind).equalsIgnoreCase(NCLEventType.PRESENTATION.toString())){
              HTGVertice v = htg.getVertice(BindUtil.getEventAction(bind),getAnchorId(bind),BindUtil.getEventType(bind));
              if(v == null && BindUtil.getEventAction(bind).equalsIgnoreCase(NCLEventAction.ABORT.toString())){
                  v = new HTGVertice(BindUtil.getEventAction(bind),getAnchorId(bind),BindUtil.getEventType(bind));
                  htg.addVertice(v);
              }
              outputVerticeList.add(v);
          }else if(BindUtil.getEventType(bind).equalsIgnoreCase(NCLEventType.ATTRIBUTION.toString())){
              String value = getAttributionValue(link,bind);
              HTGVertice v1 = new HTGVertice(BindUtil.getEventAction(bind),BindUtil.getComponentId(bind)+"."+getAnchorId(bind)+" = "+value,BindUtil.getEventType(bind));
              htg.addVertice(v1);
              outputVerticeList.add(v1);
          }  
      }
   }
   
@SuppressWarnings({ "rawtypes", "unchecked" })
private void constructCompoundCondition(HTGCondition condition, NCLLink link) throws XMLException {
       NCLCompoundCondition compoundCondition = (NCLCompoundCondition)LinkUtil.getConnector(link).getCondition();
       ElementList<NCLCondition> conditionList = compoundCondition.getConditions();
       NCLCondition nclCondition;
       String operator = compoundCondition.getOperator().toString();
       Boolean isFirstCondition = Boolean.TRUE;
       Iterator iterator = conditionList.iterator();
       NCLBind bind;
       while(iterator.hasNext()){
           nclCondition = (NCLCondition) iterator.next();
           HTGVertice defaultConditionVertice = null;
           if(nclCondition instanceof NCLSimpleCondition){
               NCLSimpleCondition simpleCondition = (NCLSimpleCondition) nclCondition;
               ArrayList<NCLBind> referencedBindList = HtgUtil.getReferencedBinds(simpleCondition,link); 
               for (int i = 0; i < referencedBindList.size(); i++) {
                   bind = (NCLBind) referencedBindList.get(i);
                   String roleName;
                   roleName = BindUtil.getRoleName(bind);
                   if(roleName.equalsIgnoreCase(NCLDefaultConditionRole.ONSELECTION.toString())){
                       HTGVertice v  = htg.getVertice(BindUtil.getEventAction(bind), getAnchorId(bind), NCLEventType.PRESENTATION.toString());
                       inputVertice = new HTGVertice(BindUtil.getEventAction(bind), getAnchorId(bind), BindUtil.getEventType(bind));
                       htg.addVertice(inputVertice);
                       HTGCondition selectionCondition = new HTGCondition();
                       selectionCondition.addInteractivity(link, bind);
                       HTGEdge e = new HTGEdge(v, inputVertice, selectionCondition);
                       htg.addEdge(e);
                   }else if(isFirstCondition && !LinkUtil.hasOnSelectionCondition(link)){
                       isFirstCondition = Boolean.FALSE;
                       inputVertice = htg.getVertice(BindUtil.getEventAction(bind),getAnchorId(bind),BindUtil.getEventType(bind));
                   }else{
                       defaultConditionVertice = htg.getVertice(BindUtil.getEventAction(bind),getAnchorId(bind),BindUtil.getEventType(bind));
                       if(BindUtil.roleIsDefaultCondition(bind)){
                           if(defaultConditionVertice!=null){
                               condition.addDefaultCondition(defaultConditionVertice, simpleCondition, operator);
                           }
                       }else{
                          //condição com papel definido pelo autor no simpleCondition.
                       }
                    }
               }
            }else if(nclCondition instanceof NCLCompoundCondition){
                   //tratar composição dentro de outra
            }
       }
       NCLStatement statement;
       ElementList<NCLStatement> statementList = compoundCondition.getStatements();
       iterator = statementList.iterator();
       while(iterator.hasNext()){
           statement = (NCLStatement) iterator.next();
           if(statement instanceof NCLAssessmentStatement){
               condition.addAssessmentStatement((NCLAssessmentStatement)statement,link);
           }else if(statement instanceof NCLCompoundStatement){
               //tratar varias statement.
           }
       }
   }
   
@SuppressWarnings({ "rawtypes", "unchecked" })
private void constructCompoundAction(List<HTGVertice> outputVerticeList, NCLLink link){
       NCLAction nclAction;
       NCLCompoundAction compoundAction = (NCLCompoundAction)LinkUtil.getConnector(link).getAction();
       ElementList<NCLAction> actionList = compoundAction.getActions();
       Iterator iterator = actionList.iterator();
       NCLBind bind;
       while(iterator.hasNext()){
           nclAction = (NCLAction) iterator.next();
           if(nclAction instanceof NCLSimpleAction){
               NCLSimpleAction simpleAction = (NCLSimpleAction) nclAction;
               ArrayList<NCLBind> referencedBindList = HtgUtil.getReferencedBinds(simpleAction,link); 
               for (int i = 0; i < referencedBindList.size(); i++) {
                   bind = (NCLBind) referencedBindList.get(i);
                  if(BindUtil.getEventType(bind).equalsIgnoreCase(NCLEventType.PRESENTATION.toString())){
                      HTGVertice v = htg.getVertice(BindUtil.getEventAction(bind),getAnchorId(bind),BindUtil.getEventType(bind));
                      if(v == null){
                          v = new HTGVertice(BindUtil.getEventAction(bind),getAnchorId(bind),BindUtil.getEventType(bind));
                          htg.addVertice(v);
                      }
                      outputVerticeList.add(v); 
                   }else if(BindUtil.getEventType(bind).equalsIgnoreCase(NCLEventType.ATTRIBUTION.toString())){
                       String value = getAttributionValue(link,bind);
                       HTGVertice v1 = new HTGVertice(BindUtil.getEventAction(bind),BindUtil.getComponentId(bind)+"."+getAnchorId(bind)+" = "+value,BindUtil.getEventType(bind));
                       htg.addVertice(v1);
                       outputVerticeList.add(v1); 
                  }
              }
           }else if(nclAction instanceof NCLCompoundAction){
               //tratar composição dentro de outra.
           }
       }
   }
   
@SuppressWarnings("rawtypes")
private String getAttributionValue(NCLLink link, NCLBind bind) {
       String value = null;
       NCLCausalConnector connector = LinkUtil.getConnector(link);
       NCLAction action = connector.getAction();
       if(action instanceof NCLSimpleAction){
           NCLSimpleAction sAction = (NCLSimpleAction) action;
           Object objectValue = sAction.getValue();
           if(objectValue instanceof NCLConnectorParam){
               NCLConnectorParam connectorParam = (NCLConnectorParam) objectValue;
               value = ConnectorUtil.getParamValue(connectorParam,link);
           }else{
               value = objectValue.toString();
           }
       }else if(action instanceof NCLCompoundAction){
            NCLSimpleAction sAction = ConnectorUtil.getSimpleAction(connector,BindUtil.getRoleName(bind));
            Object objectValue = sAction.getValue();
            if(objectValue instanceof NCLConnectorParam){
                NCLConnectorParam connectorParam = (NCLConnectorParam) objectValue;
                value = ConnectorUtil.getParamValue(connectorParam,link);
            }else{
                value = objectValue.toString();
            }
       }
       return value;
   }

	@SuppressWarnings("rawtypes")
	private String getAnchorId(NCLBind bind){
		
		String anchorId;
		if(BindUtil.hasInterface(bind)){
			anchorId = BindUtil.getInterfaceId(bind);
		}else{
		    Media media = (Media) bind.getComponent();
		    if(media.hasRefer()){
		        anchorId = ((Media) media.getRefer()).getId();
		    }else{
		        anchorId = BindUtil.getComponentId(bind);
		    }
		}
		
		return anchorId;
	
	}

   
   private void generatePresentationPlan() {
        presentationPlan = new PresentationPlan(htg);
        removeInteractiveVertices(presentationPlan);
        //presentationPlan.sort();
        presentationPlan.generatePresentationPlanTextFile();
        if(presentationPlan.getSecMatrix()!=null){
             presentationPlan.setX(insertInteractivityTimeValue());
        }
   }
   
   private void removeInteractiveVertices(PresentationPlan presentationPlan) {
        int count = 0;
        for (int i = 0; i < presentationPlan.getMatrix().length; i++) {
            if(presentationPlan.getMatrix()[i][1] != null){
                count++;
            }
        }
        Object[][] aux = new Object[count][2];
        int j=0;
        for (int i = 0; i < presentationPlan.getMatrix().length; i++) {
            if(presentationPlan.getMatrix()[i][1] != null){
                aux[j][1] = presentationPlan.getMatrix()[i][1];
                aux[j][0] = presentationPlan.getMatrix()[i][0];
                j++;
            }
        }
        presentationPlan.setMatrix(aux);
        
//      PARA TRATAR SEGUNDA INTERATIVIDADE A PARTIR DA PRIMEIRA  
//        int ct = 0;
//        for (int i = 0; i < presentationPlan.getSecMatrix().length; i++) {
//            if(presentationPlan.getSecMatrix()[i][1] != null){
//                ct++;
//            }
//        }
//        Object[][] aux2 = new Object[ct][2];
//        int k=0;
//        for (int i = 0; i < presentationPlan.getSecMatrix().length; i++) {
//            if(presentationPlan.getSecMatrix()[i][1] != null){
//                aux2[k][1] = presentationPlan.getSecMatrix()[i][1];
//                aux2[k][0] = presentationPlan.getSecMatrix()[i][0];
//                k++;
//            }
//        }
//        presentationPlan.getSecPresentationPlan().setMatrix(aux2);
   
   }
   
   private Double insertInteractivityTimeValue(){
        ImageIcon icon = new ImageIcon("C:\\Users\\Douglas\\Documents\\NetBeansProjects\\Temporal\\src\\myPlugin\\images\\interactivity.png");
        String s = (String)JOptionPane.showInputDialog(null,"Insert the Interactivity Time Value:\n"+
                    "The value must be within interactivity range  "+presentationPlan.getStartTime(presentationPlan.getInteractiveVertice())+"  to  "+presentationPlan.getStopTime(presentationPlan.getInteractiveVertice()),
                    "Interactivity Time Value", JOptionPane.PLAIN_MESSAGE, icon, null, null);
        if(s!=null && !s.isEmpty()){
            return Double.parseDouble(s);
        }else{
            return null;
        }
   }
   
   
   
   private void createTemporalView() throws XMLException {
       temporalView = new TemporalView(presentationPlan, mediaList);
       //displayTemporalView();
   }
   
   @Deprecated
   private void displayTemporalView(){
       System.out.println("");
       System.out.println("TEMPORAL VIEW");
       for (int i = 0; i < temporalView.getMainMediaInfoList().size(); i++) {
           System.out.println(temporalView.getMainMediaInfoList().get(i));
       }
       if(!temporalView.getSecMediaInfoList().isEmpty()){
           System.out.println("");
           for (int i = 0; i < temporalView.getSecMediaInfoList().size(); i++) {
               System.out.println(temporalView.getSecMediaInfoList().get(i));
           }
       }
   }
   
   @SuppressWarnings({ "unchecked", "rawtypes" })
private void createTemporalViewPane(){
	    
	   temporalViewPane = new BorderPane();
	   temporalViewPane.setId("temporal-view-pane");
	  
	   //TODO implementar zoom setando valores para scale usando o listener so scrollbar Scale scaleTransform = new Scale(5, 5, 0, 0);
	   //temporalChaiChannelPane.getTransforms().add(scaleTransform);
	    
       List<TemporalMediaInfo> audioTemporalMediainfoList = new ArrayList<TemporalMediaInfo>();
       List<TemporalMediaInfo> videoTemporalMediainfoList = new ArrayList<TemporalMediaInfo>();
       
       if(temporalView != null){
    	   for(int mediaInfoIndex=0; mediaInfoIndex<this.temporalView.getMainMediaInfoList().size(); mediaInfoIndex++){
        	   
        	   TemporalMediaInfo temporalMediaInfo = this.temporalView.getMainMediaInfoList().get(mediaInfoIndex);
        	   Media media = temporalMediaInfo.getMedia();
        	   media.setPath(media.getMediaAbsolutePath());
        	   NCLMediaType mediaType = media.getRepoMediaType();
        	   if(mediaType == NCLMediaType.AUDIO){
        		   audioTemporalMediainfoList.add(temporalMediaInfo);
    		   } else {
    			   videoTemporalMediainfoList.add(temporalMediaInfo);
           	   }
           	
           }
       }
   
       videoTemporalChain = new TemporalChainPane(videoTemporalMediainfoList);       
       audioTemporalChain = new TemporalChainPane(audioTemporalMediainfoList);
       audioTemporalChain.setXAxisTickLabelsVisible(false);
       audioTemporalChain.setXAxisTickLength(-1);
       
       final StackedBarChart<Number, String> videoTemporalChainChart = videoTemporalChain.getStackedBarChart();
       final StackedBarChart<Number, String> audioTemporalChainChart = audioTemporalChain.getStackedBarChart();

       
       if(videoTemporalChain.getLastMediaTime() > audioTemporalChain.getLastMediaTime()){
    	   videoTemporalChain.setXAxisLength(videoTemporalChain.getLastMediaTime());
    	   audioTemporalChain.setXAxisLength(videoTemporalChain.getLastMediaTime());
       }else if(audioTemporalChain.getLastMediaTime() > 0){
    	   videoTemporalChain.setXAxisLength(audioTemporalChain.getLastMediaTime());
    	   audioTemporalChain.setXAxisLength(audioTemporalChain.getLastMediaTime());
       }else {
    	   videoTemporalChain.setXAxisLength(DEFAULT_AXIS_LENGTH);
    	   audioTemporalChain.setXAxisLength(DEFAULT_AXIS_LENGTH);
       }

       HBox audioTemporalChainChartAndChannelTitlePane = new HBox();
       audioTemporalChainChartAndChannelTitlePane.setId("temporal-chain-audio-pane");
       audioTemporalChainChartAndChannelTitlePane.getChildren().add(audioTemporalChainChart);
      
       HBox videoTemporalChainChartAndChannelTitlePane = new HBox();
       videoTemporalChainChartAndChannelTitlePane.setId("temporal-chain-video-pane");
       videoTemporalChainChartAndChannelTitlePane.getChildren().add(videoTemporalChainChart);
       
       videoTemporalChainChartAndChannelTitlePane.widthProperty().addListener(new ChangeListener(){
           @Override 
           public void changed(ObservableValue o,Object oldVal, Object newVal){
        	   videoTemporalChainChart.setPrefWidth((double) newVal - 160);
           }
         });
       audioTemporalChainChartAndChannelTitlePane.widthProperty().addListener(new ChangeListener(){
           @Override 
           public void changed(ObservableValue o,Object oldVal, Object newVal){
        	   audioTemporalChainChart.setPrefWidth((double) newVal - 160);
           }
         }); 
      
       
       temporalChainPane = new VBox();
       temporalChainPane.setId("temporal-chain-pane");
       temporalChainPane.getChildren().add(videoTemporalChainChartAndChannelTitlePane);
       temporalChainPane.getChildren().add(audioTemporalChainChartAndChannelTitlePane);
      
       
       ScrollPane temporalChainScrollPane = new ScrollPane();
       temporalChainScrollPane.setContent(temporalChainPane);
       temporalChainScrollPane.setId("scroll-pane");
       
       temporalChainScrollPane.widthProperty().addListener(new ChangeListener(){
           @Override 
           public void changed(ObservableValue o,Object oldVal, Object newVal){
        	   temporalChainPane.setPrefWidth((double) newVal - 20);
           }
         }); 
       temporalChainScrollPane.heightProperty().addListener(new ChangeListener(){
           @Override 
           public void changed(ObservableValue o,Object oldVal, Object newVal){
        	   temporalChainPane.setPrefHeight((double) newVal - 4);
           }
         }); 
           
       temporalViewTabPane = new TabPane();
       temporalViewTabPane.getStylesheets().add("gui/styles/temporalViewPane.css");
       
       //TODO colocar um for aqui para diversas temporalChainPane
	   Tab tab = new Tab();
       tab.setText("Temporal Chain 1");
       tab.setId("tab");
       tab.setContent(temporalChainScrollPane);
       tab.setClosable(false);
       
       temporalViewTabPane.getTabs().add(tab);
      
       Button meetsButton = new Button();
       meetsButton.setId("meets-button");
       meetsButton.setTooltip(new Tooltip("Right alignment"));
       meetsButton.setScaleX(1);
       meetsButton.setScaleY(1);
       
       Button metByButton = new Button();
       metByButton.setId("met_by-button");
       metByButton.setTooltip(new Tooltip("Left alignment"));
       metByButton.setScaleX(1);
       metByButton.setScaleY(1);
        
       Button startsButton = new Button();
       startsButton.setId("starts-button");
       startsButton.setTooltip(new Tooltip("Slave media begins with the master media start"));
       startsButton.setScaleX(0.8);
       startsButton.setScaleY(0.8);
       
       Button finishesButton = new Button();
       finishesButton.setId("finishes-button");
       finishesButton.setTooltip(new Tooltip("Slave media finishes with the master media end"));
       finishesButton.setScaleX(0.8);
       finishesButton.setScaleY(0.8);
       
       Button beforeButton = new Button();
       beforeButton.setId("before-button");
       beforeButton.setTooltip(new Tooltip("Distributes media sequentially with a value t between them"));
       beforeButton.setScaleX(1);
       beforeButton.setScaleY(1);
       
       Button startsDelayButton = new Button();
       startsDelayButton.setId("starts_delay-button");
       startsDelayButton.setTooltip(new Tooltip("Slave media begins with delay when the master media starts"));
       startsDelayButton.setScaleX(0.8);
       startsDelayButton.setScaleY(0.8);
       
       Button finishesDelayButton = new Button();
       finishesDelayButton.setId("finishes_delay-button");
       finishesDelayButton.setTooltip(new Tooltip("Slave media finishes t of time units before the master media end"));
       finishesDelayButton.setScaleX(0.8);
       finishesDelayButton.setScaleY(0.8);
       
       Button duringButton = new Button();
       duringButton.setId("during-button");
       duringButton.setTooltip(new Tooltip("One of the media contains the other"));
       duringButton.setScaleX(0.8);
       duringButton.setScaleY(0.8);
       
       Button equalsButton = new Button();
       equalsButton.setId("equals-button");
       equalsButton.setTooltip(new Tooltip("All media has the same duration"));
       equalsButton.setScaleX(0.8);
       equalsButton.setScaleY(0.8);
       
       ZoomButton zoomButton = new ZoomButton();
       CheckBox showAnchorsLinksButton = new CheckBox("Show Anchors and Links");
       
       HBox zoomShowLinksButtonPane = new HBox();
       zoomShowLinksButtonPane.setId("button-pane");
       zoomShowLinksButtonPane.setSpacing(20);
       zoomShowLinksButtonPane.setAlignment(Pos.CENTER);
       zoomShowLinksButtonPane.setPadding(new Insets(0, 5, 0, 0));
       zoomShowLinksButtonPane.getChildren().add(zoomButton);
       zoomShowLinksButtonPane.getChildren().add(showAnchorsLinksButton);
       
       HBox alignmentButtonPane = new HBox();
       alignmentButtonPane.setId("button-pane");
       alignmentButtonPane.setAlignment(Pos.CENTER);
       alignmentButtonPane.setPadding(new Insets(0, 0, 0, 5));
       alignmentButtonPane.setSpacing(20);
       alignmentButtonPane.getChildren().add(meetsButton);
       alignmentButtonPane.getChildren().add(metByButton);
       alignmentButtonPane.getChildren().add(startsButton);
       alignmentButtonPane.getChildren().add(finishesButton);
       alignmentButtonPane.getChildren().add(beforeButton);
       alignmentButtonPane.getChildren().add(startsDelayButton);
       alignmentButtonPane.getChildren().add(finishesDelayButton);
       alignmentButtonPane.getChildren().add(duringButton);
       alignmentButtonPane.getChildren().add(equalsButton);

       BorderPane temporalViewButtonPane = new BorderPane();
       temporalViewButtonPane.setId("button-pane");
       temporalViewButtonPane.getStylesheets().add("gui/styles/temporalViewPane.css");
       temporalViewButtonPane.setLeft(alignmentButtonPane);
       temporalViewButtonPane.setRight(zoomShowLinksButtonPane);
       
       temporalViewPane.setCenter(temporalViewTabPane);
       temporalViewPane.setBottom(temporalViewButtonPane);
       
       createDragAndDropEvent();
 
   }

private void createDragAndDropEvent() {
	
	temporalChainPane.setOnDragDropped(new EventHandler<DragEvent>() {
		
		public void handle(DragEvent event) {
			
			System.out.println("DROPPED");
//	        Dragboard dragBoard = event.getDragboard();
//	        boolean success = false;
//	        if (dragBoard.hasFiles()) {
//	           //TODO Criar o n� NCL correspondente � m�dia arrastada para a temporal View.
//	        	
//	        	Media media = new Media(dragBoard.getFiles().get(0));
//	        	
//	        	TemporalMediaInfo mediaInfo = new TemporalMediaInfo("teste",0.0,5.0, media);
//			 	temporalView.getMainMediaInfoList().add(mediaInfo);
//			 	temporalChainPane = new TemporalChainPane(temporalView.getMainMediaInfoList());
//			 	sp.setContent(temporalChainPane);   
//			 	
//	           success = true;
//	           
//	        }
//		        
//	        event.setDropCompleted(success);
//	        
//	        event.consume();
		}
	});
	
}
   
   private void createRepositoryPane(){
       repository = new Repository();
   }
   
   private void createSpatialViewPane() {
        spatialViewPanel = new SpatialViewPane();
   }
   
   private void createGUI(){
	   
	   createRepositoryPane();
	   createTemporalViewPane();
	   createSpatialViewPane();
	   createRepoPropAnimTabPane();
	   
	   splitPaneRepoSpatial = new SplitPane();
	   splitPaneRepoSpatial.setId("splitRepoSpatial");
	   splitPaneRepoSpatial.setOrientation(Orientation.HORIZONTAL);
	   splitPaneRepoSpatial.setDividerPositions(0.6);
	   splitPaneRepoSpatial.getItems().addAll(repoPropAnimTabPane, spatialViewPanel);
	   
       splitPane = new SplitPane();
       splitPane.setId("splitPane");
       splitPane.setOrientation(Orientation.VERTICAL);
       splitPane.getItems().addAll(splitPaneRepoSpatial, temporalViewPane);

	   containerBorderPane.setId("containerBorderPane");
	   containerBorderPane.setTop(createMenu());
       containerBorderPane.setCenter(splitPane);
	   
   }

private void createRepoPropAnimTabPane() {
	   
	   repoPropAnimTabPane = new TabPane();
	   repoPropAnimTabPane.setId("repo-prop-anim-view-tab-pane");
	   repoPropAnimTabPane.getStylesheets().add("gui/styles/spatialViewPane.css");
       
	   Tab mediaTab = new Tab();
	   mediaTab.setText("Media");
	   mediaTab.setId("media-tab");
	   mediaTab.setContent(repository.getRepositoryPanel());
	   mediaTab.setClosable(false);
       
       Tab propTab = new Tab();
       propTab.setText("Properties");
       propTab.setId("prop-tab");
       propTab.setClosable(false);
       //TODO tela de propriedades propTab.setContent(repository.getRepositoryPanel());
       
       Tab animTab = new Tab();
       animTab.setText("Animations");
       animTab.setId("anim-tab");
       animTab.setClosable(false);
       //TODO tela de animcoes animTab.setContent(repository.getRepositoryPanel());
       
       repoPropAnimTabPane.getTabs().addAll(mediaTab, propTab, animTab);
}

private BorderPane createMenu() {
	
	BorderPane menuContainer = new BorderPane();
	BorderPane menuBarProjectTitle = new BorderPane();
	
	ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/gui/images/logo.png")));
    logo.setPreserveRatio(true);
    logo.setSmooth(true);
    logo.setFitWidth(40);
    Label projectTitle = new Label("Untitled Project");
    projectTitle.setPadding(new Insets(0, 0, 0, 15));
    createMenuBar();
    
    menuBarProjectTitle.setLeft(projectTitle);
    menuBarProjectTitle.setBottom(menuBar);
    menuContainer.setLeft(logo);
    menuContainer.setCenter(menuBarProjectTitle);
    
    return menuContainer;
    
}

private void createMenuBar() {
	menuBar = new MenuBar();
	
	Menu menuFile = new Menu("File");
	Menu menuEdit = new Menu("Edit");
	Menu menuView = new Menu("View");
	Menu menuTools = new Menu("Tools");
	Menu menuHelp = new Menu("Help");
	   
	MenuItem menuItemNew = new MenuItem("New Project				");
	menuItemNew.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
	menuItemNew.setOnAction(new EventHandler<ActionEvent>() {
		public void handle(ActionEvent t) {
			   //TODO implmentar bot�o
	           }
	       });
    MenuItem menuItemOpen = new MenuItem("Open Project...");
    menuItemOpen.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
    menuItemOpen.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent t) {
		   //TODO implmentar bot�o
           }
        });
    MenuItem menuItemClose = new MenuItem("Close");
    menuItemClose.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
    menuItemClose.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent t) {
		    //TODO implmentar bot�o
            }
        });
    MenuItem menuItemSave = new MenuItem("Save Project");
    menuItemSave.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
    menuItemSave.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent t) {
		    //TODO implmentar bot�o
            }
        });
    MenuItem menuItemImportNCL = new MenuItem("Import NCL Document...");
    menuItemImportNCL.setAccelerator(KeyCombination.keyCombination("Ctrl+I"));
    menuItemImportNCL.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent t) {
	 	    //TODO implmentar bot�o
            }
        });
    MenuItem menuItemExportNCL = new MenuItem("Export to NCL Document...");
    menuItemExportNCL.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
    menuItemExportNCL.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent t) {
		    //TODO implmentar bot�o
            }
        });
    MenuItem menuItemExit = new MenuItem("Exit");
    menuItemExit.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent t) {
		    //TODO implmentar bot�o
            }
        });
    MenuItem menuItemUndo= new MenuItem("Undo				");
    menuItemUndo.setAccelerator(KeyCombination.keyCombination("Ctrl+Z"));
    menuItemUndo.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent t) {
		    //TODO implmentar bot�o
            }
        });
    MenuItem menuItemRedo = new MenuItem("Redo");
    menuItemRedo.setAccelerator(KeyCombination.keyCombination("Ctrl+Y"));
    menuItemRedo.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent t) {
		   //TODO implmentar bot�o
            }
        });
    MenuItem menuItemCut = new MenuItem("Cut");
    menuItemCut.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
    menuItemCut.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent t) {
	 	    //TODO implmentar bot�o
            }
        });
    MenuItem menuItemCopy = new MenuItem("Copy");
    menuItemCopy.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
    menuItemCopy.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent t) {
	 	    //TODO implmentar bot�o
           }
        });
    MenuItem menuItemPaste = new MenuItem("Paste");
    menuItemPaste.setAccelerator(KeyCombination.keyCombination("Ctrl+V"));
    menuItemPaste.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent t) {
	 	   //TODO implmentar bot�o
            }
        });
    MenuItem menuItemDelete = new MenuItem("Delete");
    menuItemDelete.setAccelerator(KeyCombination.keyCombination("Delete"));
    menuItemDelete.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent t) {
 		   //TODO implmentar bot�o
            }
        });
    MenuItem menuItemSelectAll = new MenuItem("Select All");
    menuItemSelectAll.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
    menuItemSelectAll.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent t) {
		    //TODO implmentar bot�o
            }
        });
   
    CheckMenuItem checkMenuItemMediaView = createMenuItem("Media View				", null);                                                       
    CheckMenuItem checkMenuItemTemporalView = createMenuItem ("Temporal View", null);        
    CheckMenuItem checkMenuItemSpatialView = createMenuItem ("Spatial View", null);        
   
    MenuItem menuItemNCL4WEB= new MenuItem("NCL4WEB				");
    menuItemNCL4WEB.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent t) {
		    //TODO implmentar bot�o
            }
        });
    MenuItem menuItemSimulation= new MenuItem("Simulation");
    menuItemSimulation.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent t) {
		    //TODO implmentar bot�o
            }
        });
   
    MenuItem menuItemHelpContents= new MenuItem("Help Contents				");
    menuItemHelpContents.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent t) {
		    //TODO implmentar bot�o
            }
        });
    MenuItem menuItemAbout= new MenuItem("About");
    menuItemAbout.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent t) {
		    //TODO implmentar bot�o
            }
        });
   
    menuFile.getItems().addAll(menuItemNew, menuItemOpen, new SeparatorMenuItem(), menuItemClose, new SeparatorMenuItem(), menuItemSave, new SeparatorMenuItem(), menuItemImportNCL, menuItemExportNCL, new SeparatorMenuItem(), menuItemExit);
    menuEdit.getItems().addAll(menuItemUndo, menuItemRedo, new SeparatorMenuItem(), menuItemCut, menuItemCopy, menuItemPaste, new SeparatorMenuItem(), menuItemDelete, menuItemSelectAll);
    menuView.getItems().addAll(checkMenuItemMediaView, checkMenuItemTemporalView, checkMenuItemSpatialView);
    menuTools.getItems().addAll(menuItemNCL4WEB, menuItemSimulation);
    menuHelp.getItems().addAll(menuItemHelpContents, new SeparatorMenuItem(), menuItemAbout);
    menuBar.getMenus().addAll(menuFile, menuEdit, menuView, menuTools, menuHelp);
}

   private static CheckMenuItem createMenuItem (String title, final Node node){
	    CheckMenuItem cmi = new CheckMenuItem(title);
	    cmi.setSelected(true);
	    cmi.selectedProperty().addListener(new ChangeListener<Boolean>() {
	        public void changed(ObservableValue ov,
	        Boolean old_val, Boolean new_val) {
	            node.setVisible(new_val);
	        }
	    });
	    return cmi;
   }
   
}
