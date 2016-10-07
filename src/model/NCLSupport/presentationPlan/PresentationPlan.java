
package model.NCLSupport.presentationPlan;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.NCLSupport.HTG.HTGCondition;
import model.NCLSupport.HTG.HTGEdge;
import model.NCLSupport.HTG.HTGVertice;
import model.NCLSupport.HTG.HypermediaTemporalGraph;
import model.NCLSupport.utility.HTGUtil;
import model.utility.MediaUtil;
import br.uff.midiacom.ana.util.enums.NCLEventAction;
import br.uff.midiacom.ana.util.enums.NCLEventType;

/**
 *
 * @author Douglas
 * 
 *  O vértice é uma tripla composta por uma âncora, tipo de evento e estado do evento.
 	Estes vértices correspondem aos vértices do HTG.
	O presentation plan é criado a partir dessa estrutura HTG na importação de documento NCL.
	Mas iremos criar o presentation plan sem passar pelo HTG
	
 */
public class PresentationPlan {
    
	private static final int TIME_COLUMN = 1;
	private static final String SEPARATOR = "  ----------  ";
	private static final String NEW_LINE = "\r\n";
	private Object[][] presentationPlan;
    private PresentationPlan secPresentationPlan;
    private HypermediaTemporalGraph htg;
    private HTGVertice subInitialVertice;
    private Double time = new Double(0);
    private Double x = null;
    private Boolean isSecPresentationPlan = Boolean.FALSE;
    private Logger presentationPlanLogger = Logger.getLogger("PresentationPlanLogger");
    
    public PresentationPlan(HypermediaTemporalGraph htg){
        this.htg = htg;
        presentationPlan = new Object[htg.getVerticesSize()][2];
        insertVertices();
        insertTimes();
       // sort();
        
    }
    
    private void insertVertices() {
        List<HTGVertice> vertices = htg.getVertices();
        HTGVertice vertice;
        for (int i = 0; i < htg.getVerticesSize(); i++) {
            vertice = vertices.get(i);
            vertice.setPresentPlanPosition(i);
            presentationPlan[i][0]=vertice;
        }
    }
    
    private void insertTimes() {
        HTGVertice initialVertice;
        initialVertice = findInitialVertice();
        presentationPlan[initialVertice.getPresentPlanPosition()][TIME_COLUMN] = time;
        for (HTGVertice vert : htg.getVertices()) {
            vert.setVisited(Boolean.FALSE);
        }
        dephFirstSearchHTG(initialVertice);
    }
    
    @SuppressWarnings("rawtypes")
	private HTGVertice findInitialVertice() {
        Boolean foundInitialVertice = Boolean.FALSE;
        HTGVertice vertice = null, auxVertice;
        Iterator iterator = htg.getVertices().iterator();
        while(iterator.hasNext() && !foundInitialVertice){
            vertice = (HTGVertice) iterator.next();
            Iterator auxIterator = htg.getVertices().iterator();
            Boolean adjListContainsVertice = Boolean.FALSE;
            while(auxIterator.hasNext() && !adjListContainsVertice){
                auxVertice = (HTGVertice) auxIterator.next();
                List<HTGEdge> adjList = auxVertice.getAdjacencies();
                for (HTGEdge htgEdge : adjList) {
                    if(htgEdge.getOutput().equals(vertice)){
                        adjListContainsVertice = Boolean.TRUE;
                    }
                }
            }
            if(adjListContainsVertice == Boolean.FALSE){
                foundInitialVertice = Boolean.TRUE;
            }
        }
        return vertice;
    }
    
    private void dephFirstSearchHTG(HTGVertice iniVertice) {
        Double cumulativeTime;
        iniVertice.setVisited(Boolean.TRUE);
        List<HTGEdge> adjList = iniVertice.getAdjacencies();
        for (HTGEdge edge : adjList) {
            HTGCondition condition = edge.getCondition();
            HTGVertice previousVertice = iniVertice;
            cumulativeTime = (Double) presentationPlan[previousVertice.getPresentPlanPosition()][TIME_COLUMN];
            if(condition.isInteractive()){
                subInitialVertice = edge.getOutput();
                HypermediaTemporalGraph subHtg = HTGUtil.constructSubHtg(subInitialVertice);
                PresentationPlan presentationPlan = new PresentationPlan(subHtg);
//                constructSecPresentationPlan(subHtg);
//                dephFirstSearchHTG(subInitialVertice);
            }else if(condition.getValue()!=null && cumulativeTime!=null){
                time = condition.getValue() + cumulativeTime;
            }else{
                time = condition.getValue();
            }
            HTGVertice adjVert = edge.getOutput();
            if(!adjVert.getEventType().equalsIgnoreCase(NCLEventType.SELECTION.toString())){
                 presentationPlan[adjVert.getPresentPlanPosition()][TIME_COLUMN] = MediaUtil.approximateDouble(time);
            }
            if(!adjVert.getVisited()){
                dephFirstSearchHTG(adjVert);
            }
        }
    }
   
    public void sort() {
        quickSort(presentationPlan,0,presentationPlan.length-1);
        if(secPresentationPlan!=null){
            quickSort(secPresentationPlan.presentationPlan,0,secPresentationPlan.presentationPlan.length-1);
        }
        //joinStartStop();
    }
    
    private void quickSort(Object [][]p, int begin, int end) {
        int middle = partition(p, begin, end);
        if (begin < (middle-1))
            quickSort(p, begin, middle-1);
        if (middle < end)
            quickSort(p, middle, end);
    }
     
    private int partition(Object [][]p, int begin, int end) {
        int i = begin, j = end;
        Double timeTmp;
        HTGVertice verticeTmp;
        Double pivot = (Double) p[(begin+end)/2][1];
        while (i <= j) {
            while( ( (Double) p[i][1]) < pivot )
                i++;
            while( ( (Double) p[j][1]) > pivot )
                j--;
            if(i <= j){
                timeTmp = (Double) p[i][1];
                verticeTmp = (HTGVertice) p[i][0];
                p[i][1] = (Double) p[j][1];
                p[i][0] = (HTGVertice) p[j][0];
                p[j][1] = timeTmp;
                p[j][0] = verticeTmp;
                i++;
                j--;
            }
        }
        return i;
    }
    
    @SuppressWarnings("unused")
	private void joinStartStop() {
        Object[][] auxMatrix;
        auxMatrix = new Object[htg.getVerticesSize()][2];
        int nextPos = 0;
        for(int i = 0; i < presentationPlan.length; i++)   {
            HTGVertice startVertice = (HTGVertice) presentationPlan[i][0];
            if(startVertice.getEventAction().equals("start")){
                auxMatrix[nextPos] =  presentationPlan[i];
                nextPos++;
                for(int j = i+1; j < presentationPlan.length; j++)   {
                    HTGVertice stopVertice = (HTGVertice) presentationPlan[j][0];
                    if(stopVertice.getEventAction().equals("stop") &&
                       stopVertice.getAnchorId().equals(startVertice.getAnchorId()) &&
                       stopVertice.getEventType().equals(startVertice.getEventType())){
                        auxMatrix[nextPos]= presentationPlan[j];
                        nextPos++;
                        break;
                    }
                }
            }
        }
        presentationPlan = auxMatrix;
    }
    
    public Object[][] getMatrix(){
        return presentationPlan;
    }
    
    public void setMatrix(Object[][] mat){
        presentationPlan = mat;
    }
    
    public Object[][] getSecMatrix(){
        if(secPresentationPlan!=null){
            return secPresentationPlan.getMatrix();
        }else{
            return null;
        }  
    }
    
    public Double getX(){
        return x;
    }
    
    public void setX(Double value){
        x = value;
    }
    
    public Double getStartTime(HTGVertice vertice){
       Double startTime = null;
       String searchedVertice = "("+NCLEventAction.START.toString()+","+vertice.getAnchorId()+","+NCLEventType.PRESENTATION.toString()+")";
       HTGVertice currentVertice;
       for (int i = 0; i < presentationPlan.length; i++) {
            currentVertice = (HTGVertice) presentationPlan[i][0];
            if(currentVertice.toString().equalsIgnoreCase(searchedVertice)){
                return startTime = (Double) presentationPlan[i][1];
            }
       }
       if(secPresentationPlan!=null){
           if(secPresentationPlan.getMatrix()!=null){
               for (int i = 0; i < secPresentationPlan.getMatrix().length; i++) {
                   currentVertice = (HTGVertice) secPresentationPlan.getMatrix()[i][0];
                   if(currentVertice.toString().equalsIgnoreCase(searchedVertice)){
                       return startTime = (Double) secPresentationPlan.getMatrix()[i][1];
                   }
               }
           }
       }
       return startTime;
   }
    
    public Double getStopTime(HTGVertice vertice){
       Double stopTime = null;
       String searchedVertice = "("+NCLEventAction.STOP.toString()+","+vertice.getAnchorId()+","+NCLEventType.PRESENTATION.toString()+")";
       HTGVertice currentVertice;
       for (int i = 0; i < presentationPlan.length; i++) {
            currentVertice = (HTGVertice) presentationPlan[i][0];
            if(currentVertice.toString().equalsIgnoreCase(searchedVertice)){
                return stopTime = (Double)presentationPlan[i][1];
            }
       }
        if(secPresentationPlan!=null){
           if(secPresentationPlan.getMatrix()!=null){
               for (int i = 0; i < secPresentationPlan.getMatrix().length; i++) {
                   currentVertice = (HTGVertice) secPresentationPlan.getMatrix()[i][0];
                   if(currentVertice.toString().equalsIgnoreCase(searchedVertice)){
                       return stopTime = (Double)secPresentationPlan.getMatrix()[i][1];
                   }
               }
           }
        }
        return stopTime;
    }
    
    public HTGVertice getInteractiveVertice(){
        return subInitialVertice;
    }
    
    public PresentationPlan getSecPresentationPlan() {
        return secPresentationPlan;
    }
    
    public void generatePresentationPlanTextFile(){

        String presentationPlanResult = toString();
        String[] presentationPlanArray = presentationPlanResult.split("-");
        try {
			FileWriter fileWriter = new FileWriter(new File("presentationPlan.txt"),false);
			for (int i = 0; i < presentationPlanArray.length; i+=2) {
				fileWriter.write(presentationPlanArray[i] + SEPARATOR + presentationPlanArray[i+1] + NEW_LINE);
				
			}
			fileWriter.close();
		} catch (IOException e) {
			presentationPlanLogger.log(Level.WARNING, "Fails to save Presentation Plan txt file."+e.getMessage());
		}
       
    }
    
    @Override
    public String toString(){
        String matrix="";
        for(int i = 0; i < presentationPlan.length; i++) {
            matrix+=((HTGVertice) presentationPlan[i][0]).toString();
            if(isSecPresentationPlan){
                if(((HTGVertice) presentationPlan[i][0]).getEventType().equalsIgnoreCase(NCLEventType.SELECTION.toString())){
                    matrix+="-"+" X "+"-";
                }else{
                    matrix+="-"+" X +"+presentationPlan[i][1]+"-";
                } 
            }else{
                matrix+="-"+presentationPlan[i][1]+"-"; 
            }
        }
        if(secPresentationPlan!=null){
            return matrix + "--" + secPresentationPlan.toString();
        }else{
            return matrix;
        }
    }
    
}
