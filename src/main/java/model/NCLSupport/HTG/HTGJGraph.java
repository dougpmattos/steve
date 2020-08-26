package model.NCLSupport.HTG;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.ClassBasedEdgeFactory;

public class HTGJGraph extends AbstractBaseGraph<String, String> implements DirectedGraph<String, String> {

	private static final long serialVersionUID = 1L;

	public HTGJGraph() {
		
		super(new ClassBasedEdgeFactory<String, String>(String.class), true, true);
	    
	}
}
