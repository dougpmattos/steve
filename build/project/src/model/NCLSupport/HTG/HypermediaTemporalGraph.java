
package model.NCLSupport.HTG;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.StringNameProvider;
import org.jgrapht.ext.VertexNameProvider;

/**
 *
 * @author Douglas
 */
public class HypermediaTemporalGraph {
    
    private static final String QUOTATION_MARK = "\"";
	private List<HTGVertice> vertices;
	private List<HTGEdge> edges;
	private int edgeID = 0;
	private Logger htgLogger = Logger.getLogger("HTGLogger");

    private HTGJGraph htgJgraph = new HTGJGraph();
    
    public HypermediaTemporalGraph(){
        vertices = new ArrayList<HTGVertice>();
        edges = new ArrayList<HTGEdge>(); 
    }
    
    public HypermediaTemporalGraph(List<HTGVertice> vertices, List<HTGEdge> edges){
        this.vertices = vertices;
        this.edges = edges; 
    }
    
    public void addVertice(HTGVertice v) {
        vertices.add(v);
        
        htgJgraph.addVertex(QUOTATION_MARK + v.toString() + QUOTATION_MARK);
    }

    public void addEdge(HTGEdge e) {
        HTGVertice v = e.input;
        edges.add(e);
        v.addAdj(e);
        
        htgJgraph.addEdge(QUOTATION_MARK+e.input.toString()+QUOTATION_MARK, QUOTATION_MARK+e.output.toString()+QUOTATION_MARK, "E"+edgeID+": "+e.condition.toString());
        edgeID=edgeID+1;
    }
    
    @SuppressWarnings("rawtypes")
	public HTGVertice getVertice(String eventAction,String anchorId,String eventType){
        HTGVertice mediaVertice = null, vertice;
        Iterator iterator = vertices.iterator();
        while (iterator.hasNext() && mediaVertice==null) {
            vertice = (HTGVertice) iterator.next();
            if(vertice.eventAction.equalsIgnoreCase(eventAction) && vertice.getAnchorId().equalsIgnoreCase(anchorId) && 
               vertice.getEventType().equalsIgnoreCase(eventType)){
                mediaVertice = vertice;
            }      
        }
        return mediaVertice;
    }
    
    public List<HTGVertice> getVertices(){
        return vertices;
    }
    
    public int getVerticesSize(){
        return vertices.size();
    }
    
    public List<HTGEdge> getEdges(){
        return edges;
    }
    
    public int getEdgesSize(){
        return edges.size();
    }
    
    public void generateDOTFile(){
        
        VertexNameProvider<String> vertexNameProvider = new StringNameProvider<String>(); 
        EdgeNameProvider<String> edgeNameProvider = new EdgeNameProvider<String>() {
        	@Override
        	public String getEdgeName(String edge) {
        		return edge;
        	}

        };
        DOTExporter<String, String> export=new DOTExporter<>(vertexNameProvider, null, edgeNameProvider);
        try {
        	FileWriter fileWriter = new FileWriter(new File("htg.dot"),false);
            export.export(fileWriter, htgJgraph);
            fileWriter.close();
        }catch (IOException ex){
        	htgLogger.log(Level.WARNING, "Fails to save dot file."+ex.getMessage());
        }  	
        
    }

    @Override
    public String toString() {
        String htg = "";
        for (HTGVertice u : vertices) {
            htg += "(" + u.eventAction + "," + u.anchorId + "," + u.eventType + ")";
            for (HTGEdge e : u.getAdjacencies()) {
                HTGVertice v = e.output;
                htg += " -- " + e.condition + " -->" + "(" + v.eventAction + "," + v.anchorId + "," + v.eventType + ")" + ", ";
            }

            htg += "\n";
        }
        return htg;
    }
    
}
