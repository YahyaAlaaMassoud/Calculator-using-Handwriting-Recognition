
package pkgfinal.neural.network;

import static java.awt.PageAttributes.MediaType.C;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.javatuples.Pair;

public class AI_Test {
    
//    public static void main(String[] args) throws IOException{
//        ArrayList< Pair<String, Integer> > input = new ArrayList<>();
//        
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data_bold\\0", 0));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data_bold\\1", 1));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data_bold\\2", 2));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data_bold\\3", 3));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data_bold\\4", 4));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data_bold\\5", 5));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data_bold\\6", 6));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data_bold\\7", 7));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data_bold\\8", 8));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data_bold\\9", 9));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data_bold\\+", 10));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data_bold\\-", 11));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data_bold\\times", 12));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data_bold\\divide", 13));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data_bold\\[", 14));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data_bold\\]", 15));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data\\test yahya\\HandwritingRecognition (2)\\0", 0));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data\\test yahya\\HandwritingRecognition (2)\\1", 1));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data\\test yahya\\HandwritingRecognition (2)\\2", 2));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data\\test yahya\\HandwritingRecognition (2)\\3", 3));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data\\test yahya\\HandwritingRecognition (2)\\4", 4));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data\\test yahya\\HandwritingRecognition (2)\\5", 5));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data\\test yahya\\HandwritingRecognition (2)\\6", 6));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data\\test yahya\\HandwritingRecognition (2)\\7", 7));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data\\test yahya\\HandwritingRecognition (2)\\8", 8));
//        input.add(Pair.with("D:\\Coding\\Java Project\\new_data\\test yahya\\HandwritingRecognition (2)\\9", 9));
//        
//        
//        
//        //fetching data from files and putting it in arraylist to begin process
////        DataSet dataset = new DataSet("D:\\Coding\\Java Project\\8x8 dataset\\digits.txt");
//    //for training data
////        DataSet dataset = new DataSet(input, 3000);
//
//        //for test
////        DataSet dataset = new DataSet(input, 2500);
////        
////        ArrayList< ArrayList<Double> > out = dataset.TrainingDataSet;
//////        
////        Collections.shuffle(out);
////        Collections.shuffle(out);
//
//
////        NeuralNetwork NN = new NeuralNetwork(21 * 21, 75, 16, 0, 0.0, true);
////        ExpectedClasses ec = new ExpectedClasses();
////
//        Segmentation seg = new Segmentation("D:\\Coding\\Java Project\\new_data\\test yahya\\Untitled.jpg");
//        seg.grayscale = seg.toGray(seg.original);
//        seg.binarized = seg.binarize(seg.grayscale);
//        seg.segment();
//        System.out.println(seg.improve(seg.solve()));
//        Calculator cal = new Calculator();
//        System.out.println(cal.cleanandeval(seg.improve(seg.solve()).toString()));
//        ArrayList< ArrayList<Double> > out = seg.outPut;
//        System.out.println(out.size());
//////        
//////        
//////           //predictions
//////        
////////////        
////////////        
//////////////      
////        String answer = "";
////        int all=0;
////        for(ArrayList<Double> row : out){
////            ArrayList<Double> cur = NN.ForwardPropagation(row);
//////            for(Double d : cur)
//////                System.out.print(d + " " );
////            char exp = ec.getExpected(cur);
//////            ArrayList<Double> s = ec.getExpected(row.get(row.size() - 1).intValue());
//////            String exp1 = ec.getExpected(s);
//////            if(exp1 == exp)
//////                all++;
//////            System.out.println();
////            System.out.println(exp);
////        }
////        System.out.println(all + " " + out.size());
//
//        
//    //training part
////        Training Train = new Training(out, 5);
//    }
}
