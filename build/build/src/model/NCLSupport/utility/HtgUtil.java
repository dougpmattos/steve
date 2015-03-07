
package model.NCLSupport.utility;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.NCLSupport.HTG.HTGEdge;
import model.NCLSupport.HTG.HTGVertice;
import model.NCLSupport.HTG.HypermediaTemporalGraph;
import model.NCLSupport.NCLDocument.MediaList;
import br.uff.midiacom.ana.connector.NCLSimpleAction;
import br.uff.midiacom.ana.connector.NCLSimpleCondition;
import br.uff.midiacom.ana.link.NCLBind;
import br.uff.midiacom.ana.link.NCLLink;
import br.uff.midiacom.ana.util.enums.NCLDefaultActionRole;
import br.uff.midiacom.ana.util.enums.NCLEventType;
import br.uff.midiacom.ana.util.exception.XMLException;

/**
 *
 * @author Douglas
 */
public final class HtgUtil {
    
    private HtgUtil(){}
    
    public static String getSimpleActionRole(String verticeAction, String verticeEvent){
        if(verticeEvent.equalsIgnoreCase(NCLEventType.ATTRIBUTION.toString())){
            return NCLDefaultActionRole.SET.toString();
        }else{
            return verticeAction;
        }
    }
    
    public static boolean isMedia(MediaList mediaList, String anchorId) throws XMLException{
        return mediaList.getAllMediaId().contains(anchorId);
   }
    
    @SuppressWarnings("rawtypes")
	public static ArrayList<NCLBind> getReferencedBinds(NCLSimpleAction simpleAction, NCLLink link) {
        ArrayList<NCLBind> referencedBindsList = new ArrayList<NCLBind>();
        Iterator iterator = link.getBinds().iterator();
        NCLBind bind;
        while(iterator.hasNext()){
            bind = (NCLBind) iterator.next();
            String currentRoleName = BindUtil.getRoleName(bind);
            if(currentRoleName.equalsIgnoreCase(simpleAction.getRole().toString())){
                referencedBindsList.add(bind);
            }
        }
        return referencedBindsList;
    }
    
    @SuppressWarnings("rawtypes")
	public static ArrayList<NCLBind> getReferencedBinds(NCLSimpleCondition simpleCondition, NCLLink link) {
        ArrayList<NCLBind> referencedBindsList = new ArrayList<NCLBind>();
        Iterator iterator = link.getBinds().iterator();
        NCLBind bind;
        while(iterator.hasNext()){
            bind = (NCLBind) iterator.next();
            String currentRoleName = BindUtil.getRoleName(bind);
            if(currentRoleName.equalsIgnoreCase(simpleCondition.getRole().toString())){
                referencedBindsList.add(bind);
            }
        }
        return referencedBindsList;
    }
    
    public static HypermediaTemporalGraph constructSubHtg(HTGVertice subInitialVertice) {
        List<HTGVertice> vertices = new ArrayList<HTGVertice>();
        List<HTGEdge> edges = new ArrayList<HTGEdge>();
        for (HTGVertice vert : getListVertices(subInitialVertice)) {
            vert.setVisited(Boolean.FALSE);
        }
        vertices.add(subInitialVertice);
        setVerticesEdges(subInitialVertice, vertices, edges);
        HypermediaTemporalGraph subHtg = new HypermediaTemporalGraph(vertices,edges);
        return subHtg;
    }
    
    public static Double approximateDouble(Double value){
        DecimalFormat approximator = new DecimalFormat( " 0.00 " );
        String aprox = approximator.format(value);
        aprox = aprox.replace(",", ".");
        Double d = Double.parseDouble(aprox);
        return d;
    }
    
    private static void setVerticesEdges(HTGVertice iniVertice, List<HTGVertice> vertices, List<HTGEdge> edges) {
        iniVertice.setVisited(Boolean.TRUE);
        List<HTGEdge> adjList = iniVertice.getAdjacencies();
        for (HTGEdge edge : adjList) {
            edges.add(edge);
            HTGVertice adjVert = edge.getOutput();
            if(!adjVert.getVisited()){
                vertices.add(adjVert);
                setVerticesEdges(adjVert, vertices, edges);
            }
        }
    }
    
    private static List<HTGVertice> getListVertices(HTGVertice iniVertice) {
        List<HTGVertice> vertices = new ArrayList<HTGVertice>();
        vertices.add(iniVertice);
        addVertices(iniVertice, vertices);
        return vertices;
    }
    
    private static void addVertices(HTGVertice iniVertice, List<HTGVertice> vertices) {
        List<HTGEdge> adjList = iniVertice.getAdjacencies();
        for (HTGEdge edge : adjList) {
            HTGVertice adjVert = edge.getOutput();
            vertices.add(adjVert);
            addVertices(adjVert, vertices);
        }
    }
     
}
