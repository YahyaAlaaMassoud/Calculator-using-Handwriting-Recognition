

package pkgfinal.neural.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.paint.Color;

public class NeuralNetwork {
    private int inputLayerSize, hiddenLayerSize, outputLayerSize, epoch;
    public ArrayList< ArrayList<Neuron> > Layers = new ArrayList<>();
    private ArrayList<Neuron> hiddenLayer = new ArrayList<>();
    private ArrayList<Neuron> outputLayer = new ArrayList<>();
    private Double learningRate;
    private boolean isTrained;
    private Double EPS = 0.0001;
    
    public NeuralNetwork(){
        inputLayerSize = 2;
        hiddenLayerSize = 2;
        outputLayerSize = 2;
        learningRate = 0.5;
        epoch = 30;
        isTrained = false;
        for(int i = 0; i < hiddenLayerSize; i++){
            ArrayList<Double> cur = new ArrayList<>();
            for(int j = 0; j < inputLayerSize + 1; j++)
                cur.add(randomWithRange(0.0, 0.1));
            Neuron n = new Neuron();
            n.weights = cur;
            hiddenLayer.add(n);
        }
        for(int i = 0; i < outputLayerSize; i++){
            ArrayList<Double> cur = new ArrayList<>();
            for(int j = 0; j < hiddenLayerSize + 1; j++)
                cur.add(randomWithRange(0.0, 0.1));
            Neuron n = new Neuron();
            n.weights = cur;
            outputLayer.add(n);
        }
        Layers.add(hiddenLayer);
        Layers.add(outputLayer);
    }
    
    public NeuralNetwork(int inputLayerSize, int hiddenLayerSize, int outputLayerSize, int epoch, Double learningRate, boolean isTrained) throws FileNotFoundException{
        this.inputLayerSize = inputLayerSize;
        this.hiddenLayerSize = hiddenLayerSize;
        this.outputLayerSize = outputLayerSize;
        this.learningRate = learningRate;
        this.epoch = epoch;
        this.isTrained = isTrained;
        if(isTrained == true){
            File weights = new File("D:\\Coding\\Java Project\\28x28 45 hidden 20 epoch 88%.txt");
            Scanner in = new Scanner(weights);
            for(int i = 0; i < hiddenLayerSize; i++){
                ArrayList<Double> cur = new ArrayList<>();
                for(int j = 0; j < inputLayerSize + 1; j++)
                    cur.add(in.nextDouble());
                Neuron n = new Neuron();
                n.weights = cur;
                hiddenLayer.add(n);
            }
            for(int i = 0; i < outputLayerSize; i++){
                ArrayList<Double> cur = new ArrayList<>();
                if(in.hasNextDouble())
                    for(int j = 0; j < hiddenLayerSize + 1; j++)
                        cur.add(in.nextDouble());
                else
                    for(int j = 0; j < hiddenLayerSize + 1; j++)
                        cur.add(randomWithRange(-1.0, 1.0));
                Neuron n = new Neuron();
                n.weights = cur;
                outputLayer.add(n);
            }
            while(in.hasNext()){
                System.out.println("fi eh");
                Double s = in.nextDouble();
                System.out.println(s);
            }
            Layers.add(hiddenLayer);
            Layers.add(outputLayer);
        }
        else{
            for(int i = 0; i < hiddenLayerSize; i++){
                ArrayList<Double> cur = new ArrayList<>();
                for(int j = 0; j < inputLayerSize + 1; j++)
                    cur.add(randomWithRange(-1.0, 1.0));
                Neuron n = new Neuron();
                n.weights = cur;
                hiddenLayer.add(n);
            }
            for(int i = 0; i < outputLayerSize; i++){
                ArrayList<Double> cur = new ArrayList<>();
                for(int j = 0; j < hiddenLayerSize + 1; j++)
                    cur.add(randomWithRange(-1.0, 1.0));
                Neuron n = new Neuron();
                n.weights = cur;
                outputLayer.add(n);
            }
            Layers.add(hiddenLayer);
            Layers.add(outputLayer);
        }
    }
    
    public Double randomWithRange(Double min, Double max)
    {
       Double range = Math.abs(max - min);     
       return (Math.random() * range) + (min <= max ? min : max);
    }
    
    public ArrayList<Double> ForwardPropagation(ArrayList<Double> input){
        for(ArrayList<Neuron> layer : Layers){
            ArrayList<Double> newIn = new ArrayList<>();
            for(Neuron neuron : layer){
                Double activation = Activate(neuron.weights, input);
                neuron.output = (Sigmoid(activation));
                newIn.add(neuron.output);
//                System.out.println(neuron.output);
            }
            input = newIn;
        }
        return input;
    }
    
    public void BackPropagation(ArrayList<Double> expected){
        int reversed = Layers.size() - 1;
        for(int i = reversed; i >= 0; i--){
            ArrayList<Double> errors = new ArrayList<>();
            if(i == 1){
                ArrayList<Neuron> layer = Layers.get(i);
                for(int j = 0; j < layer.size(); j++){
                    Neuron neuron = layer.get(j);
                    errors.add(expected.get(j) - neuron.output);
                }
            }
            else{
                ArrayList<Neuron> layer = Layers.get(i);
                for(int j = 0; j < layer.size(); j++){
                    Double error = 0.0;
                    for(Neuron neuron : Layers.get(i + 1)){
                        error += (neuron.weights.get(j) * neuron.delta);
                    }
                    errors.add(error);
                }
            }
            ArrayList<Neuron> layer = Layers.get(i);
            for(int j = 0; j < layer.size(); j++){
                Neuron neuron = layer.get(j);
                neuron.delta = errors.get(j) * TransferDerivative(neuron.output);
            }
        }
    }
    
    public void UpdateWeights(ArrayList<Double> row){
        for(int i = 0; i < Layers.size(); i++){
            ArrayList<Double> input = new ArrayList<>();
            for(int j = 0; j < row.size() - 1; j++)
                input.add(row.get(j));
            if(i == 1){
                ArrayList<Double> newIn = new ArrayList<>();
                for(int j = 0; j < Layers.get(i - 1).size(); j++){
                    newIn.add(Layers.get(i - 1).get(j).output);
                }
                input = newIn;
            }
            ArrayList<Neuron> layer = Layers.get(i);
            for(Neuron neuron : layer){
                for(int j = 0; j < input.size(); j++){
                    Double newWeight = neuron.weights.get(j) + (learningRate * input.get(j) * neuron.delta);
                    neuron.weights.set(j, newWeight);
                }
                Double newWeight = neuron.weights.get(neuron.weights.size() - 1) + (learningRate * neuron.delta);
                neuron.weights.set(neuron.weights.size() - 1, newWeight);
            }
        }
    }
    
    public ArrayList< ArrayList<Double> > TrainNetwork(String idx, ArrayList< ArrayList<Double> > TrainingDataSet, ArrayList< ArrayList<Double> > TestDataSet) throws FileNotFoundException{
        ExpectedClasses expect = new ExpectedClasses();
        Double best = 0.0;
        for(int i = 1; i <= epoch; i++){
            Double sumOfErrors = 0.0;
            for(ArrayList<Double> row : TrainingDataSet){
                ArrayList<Double> forwardProp = ForwardPropagation(row);
                ArrayList<Double> expected = expect.getExpected(row.get(row.size() - 1).intValue());
                for(int j = 0; j < forwardProp.size(); j++){
                    sumOfErrors += (expected.get(j) - forwardProp.get(j)) * (expected.get(j) - forwardProp.get(j));
                }
                BackPropagation(expected);
                UpdateWeights(row);
            }
            Integer all = 0;
            System.out.println("Epoch: " + i + " , Learning rate: " + learningRate + " , Error Squared: " + sumOfErrors);
            ArrayList< ArrayList<Double> > predictions = new ArrayList<>();
            for(ArrayList<Double> test : TestDataSet){
                ArrayList<Double> prediction = ForwardPropagation(test);
                int maxi = 0;
                Double d = -1.0;
                for(int s = 0; s < prediction.size(); s++){
                    if(prediction.get(s) > d){
                        d = prediction.get(s);
                        maxi = s;
                    }
                }
                for(int s = 0; s < prediction.size(); s++){
                    if(prediction.get(s) == d)
                        prediction.set(s, 1.0);
                    else prediction.set(s, 0.0);
                }
                predictions.add(prediction);
                ArrayList<Double> ex = expect.getExpected(test.get(test.size() - 1).intValue());
//                for(int s = 0; s < prediction.size(); s++){
//                    System.out.print(ex.get(s) + " " + prediction.get(s) + " :::: ");
//                }
//                System.out.println();
                if(expect.getExpected(ex) == expect.getExpected(prediction))
                    all++;
            }
            
            for(int k = 0; k < predictions.size(); k++){
//                ArrayList<Double> a = expect.getExpected(i);
                ArrayList<Double> p = predictions.get(k);
//                Double r = 0.0;
//                for(int j = 0; j < p.size(); j++){
//                    if((a.get(j) == 1.0 && p.get(j) >= 0.99) || (a.get(j) == 0.0 && p.get(j) <= 0.01))
//                        r ++;
//                }
//                if(r.intValue() == p.size())
//                    all++;
//                if(expect.getExpected(p) == expect.getExpected(a))
//                    all++;
            }
            best = Math.max(best, all.doubleValue() / predictions.size());
            System.out.println("Accuracy: " + all.intValue() + " / " +  predictions.size() + "   " + ((all.doubleValue() /predictions.size()) * 100.0) + "  MAX  " + (best * 100.0));
            if(i % 15 == 0){
                Scanner in = new Scanner(System.in);
                System.out.println("continue?");
                int bool = in.nextInt();
                if(bool == 0){
                    break;
                }
            }
        }
        ArrayList< ArrayList<Double> > predictions = new ArrayList<>();
        for(ArrayList<Double> test : TestDataSet){
            ArrayList<Double> prediction = ForwardPropagation(test);
            predictions.add(prediction);
        }
        File file = new File("D:\\Coding\\Java Project\\" + idx + ".txt");
        PrintWriter pt = new PrintWriter(file);
        Integer i = 1;
        for(ArrayList<Neuron> layer : Layers){
//            pt.println("Layer: " + i.toString());
            Integer j = 1;
            for(Neuron neuron : layer){
//                pt.println("Neuon: " + j.toString());
                for(Double weight : neuron.weights){
                    pt.print(weight + " ");
                }
//                pt.print(neuron.delta + " ");
//                pt.println(neuron.output + " ");
                j++;
            }
            i++;
        }
        pt.close();
        return predictions;
    }
    
    public ArrayList<Double> Predict(ArrayList<Double> input){
        ArrayList<Double> output = ForwardPropagation(input);
        return output;
    }
    
    private Double TransferDerivative(Double output){
        return output * (1.0 - output);
    }
    
    private Double Activate(ArrayList<Double> weights, ArrayList<Double> input){
        //bias
        Double activation = weights.get(weights.size() - 1);
        //summation
        for(int i = 0; i < weights.size() - 1; i++)
            activation += (weights.get(i) * input.get(i));
//        System.out.println(activation + " " + Sigmoid(activation));
        return activation;
    }
//    
    private Double Sigmoid(Double activation){
        return 1 / (1 + Math.exp(-activation));
    }
}
