
package pkgfinal.neural.network;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Training {
    private Double EPS = 0.1;
    public Training(){}
    
    public Training(ArrayList< ArrayList<Double> > TrainingDataSet, Integer K) throws FileNotFoundException{
        CrossValidation cv = new CrossValidation(TrainingDataSet, K);
        ExpectedClasses expect = new ExpectedClasses();
        Double mean = 0.0;
        ArrayList< ArrayList< ArrayList< Double> > > folds = cv.folds;
        for(Integer k = 0; k < cv.K; k++){
            ArrayList< ArrayList<Double> > Test = cv.getFold(k);
            ArrayList< ArrayList<Double> > Actual = new ArrayList<>();
            for(ArrayList<Double> test : Test)
                Actual.add(expect.getExpected(test.get(test.size() - 1).intValue()));
            ArrayList< ArrayList<Double> > Train = new ArrayList<>();
            for(int i = 0; i < cv.K; i++){
                if(i != k){
                    for(ArrayList<Double> d : cv.getFold(i)){
                        Train.add(d);
                    }
                }
            }
            NeuralNetwork NN = new NeuralNetwork(21 * 21, 75, 16, 5000, 0.8, true);
            ArrayList< ArrayList<Double> > predictions = NN.TrainNetwork(k.toString(), Train, Test);
            Double all = 0.0;
            for(int i = 0; i < predictions.size(); i++){
                ArrayList<Double> a = Actual.get(i);
                ArrayList<Double> p = predictions.get(i);
                Double r = 0.0;
                for(int j = 0; j < p.size(); j++){
                    if((a.get(j) == 1.0 && p.get(j) >= 0.999) || (a.get(j) == 0.0 && p.get(j) <= 0.001))
                        r ++;
                }
                if(r.intValue() == p.size())
                    all++;
            }
            System.out.println("Fold 1: " + (k + 1) + " " + (all / predictions.size() * 100.0));
            mean += (all / predictions.size());
        }
        System.out.println("Mean: " + mean / K * 100.0);
    }
}
