
package pkgfinal.neural.network;

import java.util.ArrayList;

public class Neuron {
    public ArrayList<Double> weights;
    public Double output, delta;
    
    public Neuron(){ 
        weights = new ArrayList<>();
        output = 0.0;
        delta = 0.0;
    }
}
