
package model.htgConstruction;

/**
 *
 * @author Douglas
 */
public class HTGEdge {
    
    HTGVertice input;
    HTGVertice output;
    HTGCondition condition;
    
    public HTGEdge(HTGVertice input, HTGVertice output, HTGCondition condition) {
        this.input = input;
        this.output = output;
        this.condition = condition;
    }
    
    public HTGVertice getInput(){
        return input;
    }
    
    public HTGVertice getOutput(){
        return output;
    }
    
    public HTGCondition getCondition(){
        return condition;
    }
    
}
