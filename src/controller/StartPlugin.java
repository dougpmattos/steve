
package controller;

import gui.spatialViewPanel.SpatialViewPane;
import gui.temporalViewPanel.TemporalChainPane;
import gui.temporalViewPanel.ZoomButton;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.StackedBarChart;
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
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import model.htgConstruction.HTGCondition;
import model.htgConstruction.HTGEdge;
import model.htgConstruction.HTGVertice;
import model.htgConstruction.HypermediaTemporalGraph;
import model.nclDocument.AreaList;
import model.nclDocument.ContextList;
import model.nclDocument.LinkList;
import model.nclDocument.MediaList;
import model.nclDocument.extendedAna.Area;
import model.nclDocument.extendedAna.Doc;
import model.nclDocument.extendedAna.Media;
import model.presentationPlan.PresentationPlan;
import model.temporalView.TemporalMediaInfo;
import model.temporalView.TemporalView;
import model.utility.htg.HtgUtil;
import model.utility.ncl.BindUtil;
import model.utility.ncl.ConnectorUtil;
import model.utility.ncl.LinkUtil;
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
public class StartPlugin extends JInternalFrame {
    
	private static final int CHANNEL_WIDTH = 160;
	private static final long serialVersionUID = 1177049012114416958L;
	private static final String EDITOR_TITLE = "STVEN";
	static StartPlugin start;
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
    JFXPanel containerfxPane;
    Scene containerScene;
    int i = 0;
    
   public StartPlugin() throws XMLException, IOException {
       
       super(EDITOR_TITLE, true, true, true, true);
       
       openNCLDocument();
       collectDataForHTG();
       constructHTG();
       generatePresentationPlan();
       createTemporalView();
       createGUI();
       //System.exit(0);
   }
   
   private void openNCLDocument() throws XMLException {
	   nclDoc = new Doc();
	   nclDoc.loadXML(new File("C:\\Users\\Douglas\\Workspace\\STVEN\\stven\\NCL Documents\\musica\\musicAd.ncl"));
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
          Logger.getLogger(StartPlugin.class.getName()).log(Level.SEVERE, null, ex);
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
           Logger.getLogger(StartPlugin.class.getName()).log(Level.SEVERE, null, ex);
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
                          //condiÃ§Ã£o com papel definido pelo autor no simpleCondition.
                       }
                    }
               }
            }else if(nclCondition instanceof NCLCompoundCondition){
                   //tratar composiÃ§Ã£o dentro de outra
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
               //tratar composiÃ§Ã£o dentro de outra.
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
   
       videoTemporalChain = new TemporalChainPane(videoTemporalMediainfoList);       
       audioTemporalChain = new TemporalChainPane(audioTemporalMediainfoList);
       audioTemporalChain.setXAxisTickLabelsVisible(false);
       audioTemporalChain.setXAxisTickLength(-1);
       
       final StackedBarChart<Number, String> videoTemporalChainChart = videoTemporalChain.getStackedBarChart();
       final StackedBarChart<Number, String> audioTemporalChainChart = audioTemporalChain.getStackedBarChart();

       
       if(videoTemporalChain.getLastMediaTime() > audioTemporalChain.getLastMediaTime()){
    	   videoTemporalChain.setXAxisLength(videoTemporalChain.getLastMediaTime());
    	   audioTemporalChain.setXAxisLength(videoTemporalChain.getLastMediaTime());
       }else{
    	   videoTemporalChain.setXAxisLength(audioTemporalChain.getLastMediaTime());
    	   audioTemporalChain.setXAxisLength(audioTemporalChain.getLastMediaTime());
       }

       final Label channelPaneTitle = new Label("Channels");
       final Label videoChannelLabel = new Label("Video");
       final Label audioChannelLabel = new Label("Audio");

       channelPaneTitle.setId("channel-pane-title");
       channelPaneTitle.setPrefWidth(CHANNEL_WIDTH);
       videoChannelLabel.setId("video-channel-label");
       videoChannelLabel.setPrefWidth(CHANNEL_WIDTH);
       VBox videoTitleChannelContainer = new VBox();
       videoTitleChannelContainer.getChildren().add(channelPaneTitle);
       videoTitleChannelContainer.getChildren().add(videoChannelLabel);
       audioChannelLabel.setId("audio-channel-label");
       audioChannelLabel.setPrefWidth(CHANNEL_WIDTH);
       VBox audioTitleChannelContainer = new VBox();
       audioTitleChannelContainer.getChildren().add(audioChannelLabel);
       
       
       videoTemporalChainChart.heightProperty().addListener(new ChangeListener(){
           @Override 
           public void changed(ObservableValue o,Object oldVal, Object newVal){
        	   videoChannelLabel.setPrefHeight((double) newVal);
           }
         }); 
       audioTemporalChainChart.heightProperty().addListener(new ChangeListener(){
           @Override 
           public void changed(ObservableValue o,Object oldVal, Object newVal){
        	   audioChannelLabel.setPrefHeight((double) newVal);
           }
         }); 
      
       HBox audioTemporalChainChartAndChannelTitlePane = new HBox();
       audioTemporalChainChartAndChannelTitlePane.setId("temporal-chain-audio-pane");
       audioTemporalChainChartAndChannelTitlePane.getChildren().add(audioTitleChannelContainer);
       audioTemporalChainChartAndChannelTitlePane.getChildren().add(audioTemporalChainChart);
      
       HBox videoTemporalChainChartAndChannelTitlePane = new HBox();
       videoTemporalChainChartAndChannelTitlePane.setId("temporal-chain-video-pane");
       videoTemporalChainChartAndChannelTitlePane.getChildren().add(videoTitleChannelContainer);
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
      
       HBox temporalViewButtonPane = new HBox();
       temporalViewButtonPane.setId("button-pane");
       temporalViewButtonPane.getStylesheets().add("gui/styles/temporalViewPane.css");
       temporalViewButtonPane.setSpacing(100);
       temporalViewButtonPane.setAlignment(Pos.CENTER_RIGHT);
       
       ZoomButton zoomButton = new ZoomButton();
       CheckBox showAnchorsLinksButton = new CheckBox("Show Anchors and Links");
       
       temporalViewButtonPane.getChildren().add(zoomButton);
       temporalViewButtonPane.getChildren().add(showAnchorsLinksButton);
       
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
//	           //TODO Criar o nó NCL correspondente à mídia arrastada para a temporal View.
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
       JFrame nextFrame;
       final int width;
       final int height;
       
       nextFrame = new JFrame("NEXT");
       width = 1024;
       height = 700;
       nextFrame.setLayout(new BorderLayout());
       nextFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       nextFrame.setSize(width, height);
       
       setLayout(new BorderLayout());
       setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
       setVisible(true);
       
       containerfxPane = new JFXPanel();
       add(containerfxPane,BorderLayout.CENTER);
       
       Platform.runLater(new Runnable() {
           @Override
           public void run() {
        	   createTemporalViewPane();
               createRepositoryPane();
               createSpatialViewPane();
        	   containerScene = createContainerScene();
               containerfxPane.setScene(containerScene);
           }
        });
       
       nextFrame.add(this, BorderLayout.CENTER);
       nextFrame.setVisible(true);
       
   }
   
   private Scene createContainerScene(){
	   
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
    		   //TODO implmentar botão
               }
           });
       MenuItem menuItemOpen = new MenuItem("Open Project...");
       menuItemOpen.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
       menuItemOpen.setOnAction(new EventHandler<ActionEvent>() {
    	   public void handle(ActionEvent t) {
    		   //TODO implmentar botão
               }
           });
       MenuItem menuItemClose = new MenuItem("Close");
       menuItemClose.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
       menuItemClose.setOnAction(new EventHandler<ActionEvent>() {
    	   public void handle(ActionEvent t) {
    		   //TODO implmentar botão
               }
           });
       MenuItem menuItemSave = new MenuItem("Save Project");
       menuItemSave.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
       menuItemSave.setOnAction(new EventHandler<ActionEvent>() {
    	   public void handle(ActionEvent t) {
    		   //TODO implmentar botão
               }
           });
       MenuItem menuItemImportNCL = new MenuItem("Import NCL Document...");
       menuItemImportNCL.setAccelerator(KeyCombination.keyCombination("Ctrl+I"));
       menuItemImportNCL.setOnAction(new EventHandler<ActionEvent>() {
    	   public void handle(ActionEvent t) {
    		   //TODO implmentar botão
               }
           });
       MenuItem menuItemExportNCL = new MenuItem("Export to NCL Document...");
       menuItemExportNCL.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
       menuItemExportNCL.setOnAction(new EventHandler<ActionEvent>() {
    	   public void handle(ActionEvent t) {
    		   //TODO implmentar botão
               }
           });
       MenuItem menuItemExit = new MenuItem("Exit");
       menuItemExit.setOnAction(new EventHandler<ActionEvent>() {
    	   public void handle(ActionEvent t) {
    		   //TODO implmentar botão
               }
           });
       
       MenuItem menuItemUndo= new MenuItem("Undo				");
       menuItemUndo.setAccelerator(KeyCombination.keyCombination("Ctrl+Z"));
       menuItemUndo.setOnAction(new EventHandler<ActionEvent>() {
    	   public void handle(ActionEvent t) {
    		   //TODO implmentar botão
               }
           });
       MenuItem menuItemRedo = new MenuItem("Redo");
       menuItemRedo.setAccelerator(KeyCombination.keyCombination("Ctrl+Y"));
       menuItemRedo.setOnAction(new EventHandler<ActionEvent>() {
    	   public void handle(ActionEvent t) {
    		   //TODO implmentar botão
               }
           });
       MenuItem menuItemCut = new MenuItem("Cut");
       menuItemCut.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
       menuItemCut.setOnAction(new EventHandler<ActionEvent>() {
    	   public void handle(ActionEvent t) {
    		   //TODO implmentar botão
               }
           });
       MenuItem menuItemCopy = new MenuItem("Copy");
       menuItemCopy.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
       menuItemCopy.setOnAction(new EventHandler<ActionEvent>() {
    	   public void handle(ActionEvent t) {
    		   //TODO implmentar botão
               }
           });
       MenuItem menuItemPaste = new MenuItem("Paste");
       menuItemPaste.setAccelerator(KeyCombination.keyCombination("Ctrl+V"));
       menuItemPaste.setOnAction(new EventHandler<ActionEvent>() {
    	   public void handle(ActionEvent t) {
    		   //TODO implmentar botão
               }
           });
       MenuItem menuItemDelete = new MenuItem("Delete");
       menuItemDelete.setAccelerator(KeyCombination.keyCombination("Delete"));
       menuItemDelete.setOnAction(new EventHandler<ActionEvent>() {
    	   public void handle(ActionEvent t) {
    		   //TODO implmentar botão
               }
           });
       MenuItem menuItemSelectAll = new MenuItem("Select All");
       menuItemSelectAll.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
       menuItemSelectAll.setOnAction(new EventHandler<ActionEvent>() {
    	   public void handle(ActionEvent t) {
    		   //TODO implmentar botão
               }
           });
       
       CheckMenuItem checkMenuItemMediaView = createMenuItem("Media View				", null);                                                       
       CheckMenuItem checkMenuItemTemporalView = createMenuItem ("Temporal View", null);        
       CheckMenuItem checkMenuItemSpatialView = createMenuItem ("Spatial View", null);        
       
       MenuItem menuItemNCL4WEB= new MenuItem("NCL4WEB				");
       menuItemNCL4WEB.setOnAction(new EventHandler<ActionEvent>() {
    	   public void handle(ActionEvent t) {
    		   //TODO implmentar botão
               }
           });
       MenuItem menuItemSimulation= new MenuItem("Simulation");
       menuItemSimulation.setOnAction(new EventHandler<ActionEvent>() {
    	   public void handle(ActionEvent t) {
    		   //TODO implmentar botão
               }
           });
       
       MenuItem menuItemHelpContents= new MenuItem("Help Contents				");
       menuItemHelpContents.setOnAction(new EventHandler<ActionEvent>() {
    	   public void handle(ActionEvent t) {
    		   //TODO implmentar botão
               }
           });
       MenuItem menuItemAbout= new MenuItem("About");
       menuItemAbout.setOnAction(new EventHandler<ActionEvent>() {
    	   public void handle(ActionEvent t) {
    		   //TODO implmentar botão
               }
           });
       
       menuFile.getItems().addAll(menuItemNew, menuItemOpen, new SeparatorMenuItem(), menuItemClose, new SeparatorMenuItem(), menuItemSave, new SeparatorMenuItem(), menuItemImportNCL, menuItemExportNCL, new SeparatorMenuItem(), menuItemExit);
       menuEdit.getItems().addAll(menuItemUndo, menuItemRedo, new SeparatorMenuItem(), menuItemCut, menuItemCopy, menuItemPaste, new SeparatorMenuItem(), menuItemDelete, menuItemSelectAll);
       menuView.getItems().addAll(checkMenuItemMediaView, checkMenuItemTemporalView, checkMenuItemSpatialView);
       menuTools.getItems().addAll(menuItemNCL4WEB, menuItemSimulation);
       menuHelp.getItems().addAll(menuItemHelpContents, new SeparatorMenuItem(), menuItemAbout);
       menuBar.getMenus().addAll(menuFile, menuEdit, menuView, menuTools, menuHelp);
	   
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
       //TODO tela de animações animTab.setContent(repository.getRepositoryPanel());
       
       repoPropAnimTabPane.getTabs().addAll(mediaTab, propTab, animTab);
	   
	   splitPaneRepoSpatial = new SplitPane();
	   splitPaneRepoSpatial.setId("splitRepoSpatial");
	   splitPaneRepoSpatial.setOrientation(Orientation.HORIZONTAL);
	   splitPaneRepoSpatial.setDividerPositions(0.6);
	   splitPaneRepoSpatial.getItems().addAll(repoPropAnimTabPane, spatialViewPanel);
	   
       splitPane = new SplitPane();
       splitPane.setId("splitPane");
       splitPane.setOrientation(Orientation.VERTICAL);
       splitPane.getItems().addAll(splitPaneRepoSpatial, temporalViewPane);

	   BorderPane containerBorderPane = new BorderPane();
	   containerBorderPane.setId("containerBorderPane");
	   containerBorderPane.setTop(menuBar);
       containerBorderPane.setCenter(splitPane);
 
       Scene scene = new Scene(containerBorderPane);
       
       return scene;
       
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
   
   public static void main (String[] args) throws XMLException, IOException {
       start = new StartPlugin();
    }


   
}
