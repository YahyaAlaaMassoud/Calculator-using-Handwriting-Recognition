
package pkgfinal.neural.network;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import static org.imgscalr.Scalr.pad;
import org.javatuples.Pair;

public class DataSet {
    
    public ArrayList< ArrayList<Double> > TrainingDataSet = new ArrayList<>();
    private int maxNumOfDataPerClass;
    private boolean[][] vis = new boolean[21][21];
    int[] dx = {0, 0, 1, 1, 1, -1 , -1, -1};
    int[] dy = {1, -1, 0, 1, -1, 0, 1, -1};
    
    boolean valid(int idx, int jdx){
        if(idx >= 0 && idx < 21 && jdx >= 0 && jdx < 21 && !vis[idx][jdx])
             return true;
        return false;
    }
    
    public ArrayList<Double> ImageToArrayList(String path, Integer ClassNumber, String idx) throws IOException{
        BufferedImage image = ImageIO.read(new File(path));
        
//        BufferedImage thumbnail = Scalr.resize(image,  Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH,
//                                       40, 40, Scalr.OP_ANTIALIAS);
        
        image = OtsuBinarize.binarize(image);

//        ImageIO.write(image, "jpg",new File("D:\\new_data\\out" + idx + ".jpg"));

        
//        for(int i = 0; i < image.getHeight(); i++){
//            for(int j = 0; j < image.getWidth(); j++){
//                if(image.getRGB(i, j) != -1 && valid(i, j)){
//                    for(int k = 0; k < 8; k++){
//                        if(valid(i + dx[k], j + dy[k])){
//                            image.setRGB(i + dx[k], j + dy[k], 255);
//                            vis[i + dx[k]][j + dy[k]] = true;
//                        }
//                    }
//                }
//            }
//        }
//        
//        for(int i = 0; i < 21; i++)
//            for(int j = 0; j < 21; j++)
//                vis[i][j] = false;
        
//        BufferedImage thumbnail = Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, 20, 20, 
//                Scalr.OP_ANTIALIAS, Scalr.OP_BRIGHTER);
        
        
//        thumbnail = Scalr.pad(thumbnail, 2, Color.WHITE);
        
//        
//        ImageIO.write(thumbnail, "jpg",new File("D:\\Coding\\Java Project\\new_data_pad_bold\\]\\out" + idx + ".jpg"));
        

        //convert image to arraylist
        ArrayList<Double> pixels = new ArrayList<>();
////
        for(int i = 0; i < image.getHeight(); i++){
            for(int j = 0; j < image.getWidth(); j++){
                Integer val;
                if(image.getRGB(j, i) == -1)
                    val = 0;
                else 
                    val = 1;
                pixels.add(val.doubleValue());
//                System.out.print(val);
            }
//            System.out.println();
        }
//        System.out.println();
        pixels.add(ClassNumber.doubleValue());

//        System.out.println(pixels.size());
//        
//        int j = 0;
//        for(int i = 0; i < pixels.size(); i++, j++){
//            if(j == image.getWidth()){
//                j = 0;
//                System.out.println();
//            }
//            System.out.print(pixels.get(i).intValue());
//        }
//        System.out.println(pixels.size());
        return pixels;
    }
    
    public void ReadMultipleImages(String path, Integer ClassNumber) throws IOException{
        File parent = new File(path);
        File[] files = parent.listFiles();
        //main
//        for(Integer i = 0; i < Math.min(files.length, maxNumOfDataPerClass); i++){
//            if(files[i].isFile()){
//                TrainingDataSet.add(ImageToArrayList(files[i].getPath(), ClassNumber, i.toString()));
//            }
//        }
        //test
        for(Integer i = Math.min(files.length, maxNumOfDataPerClass) - 1; i >= 0 ; i--){
            if(files[i].isFile()){
                TrainingDataSet.add(ImageToArrayList(files[i].getPath(), ClassNumber, i.toString()));
            }
        }
    }
    
    public void ReadFromFile(String path) throws IOException{
        File file = new File(path);
        Scanner in = new Scanner(file);
        ArrayList< ArrayList<Double> > input = new ArrayList<>();
        while(in.hasNextInt()){
            ArrayList<Double> arr = new ArrayList<>();
            for(int i = 0; i < 64 && in.hasNextInt(); i++){
                Integer val = in.nextInt();
                arr.add(val.doubleValue());
            }
            if(in.hasNextInt()){
                Integer val = in.nextInt();
                arr.add(val.doubleValue());
            }
            input.add(arr);
        }
        for(ArrayList<Double> arr : input){
            for(int i = 0; i < arr.size() - 1; i++){
                if(arr.get(i) > 6.0)
                    arr.set(i, 1.0);
                else 
                    arr.set(i, 0.0);
            }
        }
        for(ArrayList<Double> arr : input){
            if(arr.size() != 65)
                continue;
//            int j = 0;
//            for(int i = 0; i < arr.size(); i++){
//                if(j == 8){
//                    j = 0;
//                    System.out.println();
//                }
//                System.out.print(arr.get(i).intValue());
//                j++;
//            }
//            System.out.println("\n");
            TrainingDataSet.add(arr);
        }
//        System.out.println(TrainingDataSet.size());
//        for(ArrayList<Double> arr : TrainingDataSet){
//            System.out.print(arr.size() + " ");
//            for(Double d : arr){
//                System.out.print(d + " ");
//            }
//            System.out.println();
//        }
    }
    
    public DataSet(){ }
    
    public DataSet(ArrayList< Pair<String, Integer> > paths, int maxNumOfDataPerClass) throws IOException{
        this.maxNumOfDataPerClass = maxNumOfDataPerClass;
        for(Pair<String, Integer> path : paths){
            ReadMultipleImages(path.getValue0(), path.getValue1());
        }
    }
    
    public DataSet(String path) throws IOException{
        ReadFromFile(path);
    }
    
//    public static void main(String[] args) throws IOException{
//        DataSet ds = new DataSet("D:\\Coding\\Java Project\\8x8 dataset\\digitss.txt");
//    }
    
//    public static void main(String[] args) throws IOException{
//        ArrayList< Pair<String, Integer> > input = new ArrayList<>();
//        
//        input.add(Pair.with("D:\\Coding\\Java Project\\Math Dataset\\0", 0));
//        input.add(Pair.with("D:\\Coding\\Java Project\\Math Dataset\\1", 1));
//        input.add(Pair.with("D:\\Coding\\Java Project\\Math Dataset\\2", 2));
//        input.add(Pair.with("D:\\Coding\\Java Project\\Math Dataset\\3", 3));
//        input.add(Pair.with("D:\\Coding\\Java Project\\Math Dataset\\4", 4));
//        input.add(Pair.with("D:\\Coding\\Java Project\\Math Dataset\\5", 5));
//        input.add(Pair.with("D:\\Coding\\Java Project\\Math Dataset\\6", 6));
//        input.add(Pair.with("D:\\Coding\\Java Project\\Math Dataset\\7", 7));
//        input.add(Pair.with("D:\\Coding\\Java Project\\Math Dataset\\8", 8));
//        input.add(Pair.with("D:\\Coding\\Java Project\\Math Dataset\\9", 9));
//        input.add(Pair.with("D:\\Coding\\Java Project\\Math Dataset\\+", 10));
//        input.add(Pair.with("D:\\Coding\\Java Project\\Math Dataset\\-", 11));
//        input.add(Pair.with("D:\\Coding\\Java Project\\Math Dataset\\times", 12));
//        input.add(Pair.with("D:\\Coding\\Java Project\\Math Dataset\\divide", 13));
//        
//        
//        //fetching data from files and putting it in arraylist to begin process
//        DataSet ds = new DataSet(input, 500);
//        ArrayList< ArrayList<Double> > out = new ArrayList<>();
//        out = ds.TrainingDataSet;
//        
//        //Suffle all the data to make 8 folds
//        Collections.shuffle(out);
//        
//        CrossValidation cv = new CrossValidation(out, 7);
//        
//        
////        System.out.println(out.size());
////        for(ArrayList<Double> arr : out){
////            System.out.println(arr.get(arr.size() - 1));
////            System.out.print(arr.size() + " ");
////            for(Double d : arr){
////                System.out.print(d + " ");
////            }
////            System.out.println();
////        }
////        ExpectedClasses ec = new ExpectedClasses();
////        for(Integer i = 0; i < 14; i++){
////            ArrayList<Double> arr = ec.getExpected(i);
////            for(Double d : arr){
////                System.out.print(d.intValue() + " ");
////            }
////            System.out.println();
////        }
//    }
    
}
