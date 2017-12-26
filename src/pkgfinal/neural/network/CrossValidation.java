
package pkgfinal.neural.network;

import java.util.ArrayList;

public class CrossValidation {
    public ArrayList< ArrayList< ArrayList<Double> > > folds = new ArrayList<>();
    public int K;
    
    public CrossValidation() {}
    
    //Shuffeled data set
    public CrossValidation(ArrayList< ArrayList<Double> > TrainingDataSet, int K){
        this.K = K;
        int limit = TrainingDataSet.size() / K;
        for(int k = 1; k <= K; k++){
            int start = limit * (k - 1);
            int end = limit * k;
            ArrayList< ArrayList<Double> > data = new ArrayList<>();
            for(int i = start; i < end; i++){
                data.add(TrainingDataSet.get(i));
            }
            folds.add(data);
        }
//        System.out.println(folds.size());
//        for(ArrayList<ArrayList<Double>> arr : folds){
//            System.out.println(arr.size());
//        }
    }
    
    public ArrayList< ArrayList<Double> > getFold(Integer index){
        return folds.get(index);
    }
}
