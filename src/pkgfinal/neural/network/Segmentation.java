package pkgfinal.neural.network;

 
import com.sun.scenario.effect.Crop;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import javax.imageio.ImageIO;
import javax.swing.DebugGraphics;
import org.imgscalr.AsyncScalr;
import org.imgscalr.Scalr;
 
public class Segmentation {
 
    public class pair{
        int first; int second;
 
        public pair(int first, int second) {
            this.first = first;
            this.second = second;
        }
 
    }
 
    public Segmentation(String path) throws IOException {
        File sourceimage = new File(path);
        original = ImageIO.read(sourceimage);
    }
    
    public Segmentation(BufferedImage img){
        original = img;
    }
 
    private static final int squareSize = 28;
    public  BufferedImage original, grayscale, binarized;
    private  BufferedImage img;
    private static ArrayList<BufferedImage> imgDebug = new ArrayList<>();
    private static double [][]op = new double[squareSize][squareSize];
    public ArrayList<ArrayList<Double> >outPut = new ArrayList<>();
 
    private int []x1 = new int[3000];//x cordinate of left top corner
    private int []x2 = new int[3000];//x cordinate of right bot corner
    private int []y1 = new int[3000];//y cordinate of left top corner
    private int []y2 = new int[3000];//y cordinate of right bot corner
    private int idx;
//    static final int H = 1400, W = 1400;
    private int [][]visit = new int [2400][2400];
    private int []dx = {0, 0, 1, -1, 1, 1, -1, -1};//dfs x-axis directions
    private int []dy = {1, -1, 0, 0, 1, -1, -1, 1};//dfs y-axis directions
 
    public StringBuffer solve() throws FileNotFoundException{
        Double gh;
        gh = 0.0;
        int kkk = squareSize * squareSize;
        NeuralNetwork asd = new NeuralNetwork(28 * 28, 45, 15, 0, gh, true);
        ExpectedClasses qwe = new ExpectedClasses();
        StringBuffer toCalc = new StringBuffer();
 
        System.out.println("outPut.size() = " + outPut.size());
 
        ArrayList<Double> debug= new ArrayList<>();
        for(int i = 0; i < outPut.size(); i++){
//            System.out.println(outPut.size());
            debug = asd.ForwardPropagation(outPut.get(i));
            
            Double maxi = 0.0;
            for(Double d : debug)
                maxi = Math.max(maxi, d);
//            System.out.println("Iteration " + i);
//            for(int j = 0; j < debug.size(); j++)
//                 System.out.print(debug.get(j) + ", ");
             System.out.println();
             System.out.println("Expected " + i + ": " + qwe.getExpected(debug) + " " + maxi);
 
//             0 1 2 3 4 5 6 7 8 9 + - * / [ ]
            char c = qwe.getExpected(debug);
            toCalc.insert(toCalc.length(), c);
        }
//        System.out.println("Calculator input size: " + toCalc.length());
//        for(int i = 0; i < toCalc.length(); i++)
//            System.out.print(toCalc.charAt(i) + " ");
//        System.out.println();
//        System.out.println("Finished");
        return toCalc;
    }
    private Boolean isDot(int idx){
        return y2[idx] - y1[idx] < 30 && x2[idx] - x1[idx] < 30;
    }
    public StringBuffer improve(StringBuffer input){
        int up = 0, down = 0, idx = 0;
        Boolean start = false, inPower = false;
        for(int i = 0; i < input.length(); i++, idx++){
//            System.out.println(i + " ! " + idx);
            Boolean flag = false;
            if(input.charAt(i) == '['){
                input.replace(i, i + 1, "(");
            }
            else if(input.charAt(i) == ']'){
                input.replace(i, i + 1, ")");
            }
            else if(i + 2 < input.length() && isDot(idx + 1) == true && isDot(idx + 2) == true){
//                System.out.print(i + " ! ");
                input.replace(i, i + 3, "/");
//                System.out.println(i);
                flag = true;
            }
            if(isDot(idx) == false && inPower == false && ((down - up) / 3) + up >= y2[idx]){
                inPower = true;
                input.insert(i, "^(");
                i += 2;
            }else if(isDot(idx) == false && inPower == true && ((y2[idx] - y1[idx]) / 3) + y1[idx] >= down){
                inPower = false;
                input.insert(i, ")");
                i += 1;
            }
            if(isDigit(input.charAt(i))){
                start = true;
                up = y1[idx];
                down = y2[idx];
            }
 
            if(flag == true)
                idx += 2;
        }
        if(inPower == true)
            input.insert(input.length(), ")");
        return input;
    }
    public  void main(String[] args) throws IOException {
        Segmentation xas = new Segmentation("original"+".jpg");
//        File original_f = new File("original"+".jpg");
        String output_f = "original"+"_bin";
//        original = ImageIO.read(original_f);
        grayscale = xas.toGray(original);
        binarized = xas.binarize(grayscale);       
        xas.segment();
        imgDebug.add(binarized);
        xas.writeImage(output_f);   
        StringBuffer toCalc = xas.solve();
        toCalc = xas.improve(toCalc);
    }
    private Boolean isDigit(char c){
        return '0' <= c && c <= '9';
    }
    private Boolean isVisit(int x, int y){
        //check x  = H and y = W or reversed ???
        if(x < 0 || y < 0 || y >= binarized.getHeight() || x >= binarized.getWidth() || visit[x][y] == 1 || !isBlack(binarized, x, y)){
 
            return true;
        }
        return false;
        //out of bound checking
    }
    private Boolean isBlack(BufferedImage input, int x, int y){
        int red = new Color(input.getRGB(x, y)).getRed();
        if(red == 0)
            return true;
        return false;
    }
    private void setBlack(BufferedImage input, int x, int y){
        if(x < 0 || y < 0 || x >= input.getWidth() || y >= input.getHeight())
            return;
        input.setRGB(x, y, colorToRGB(0, 0, 0, 0)); 
    }
    private void bold(BufferedImage input, int x, int y){
        for(int i = 0; i < 8; i++)
            for(int j = 1; j <= 2; j++)
                setBlack(input, x + dx[i] * j, y + dy[i] * j);
    }
    private void bfs(int x, int y, int idx){
        int safety = Math.max(binarized.getWidth(), binarized.getHeight());
        Queue<pair> q= new LinkedList<pair>();
        q.add(new pair(x, y));
        while(!q.isEmpty()){
            int curX = q.peek().first;
            int curY = q.peek().second;
            q.remove();
            if(isVisit(curX, curY))
                continue;
            visit[curX][curY] = 1;
 
 
            x1[idx] = Math.min(curX + safety, x1[idx]);//getting top left corner
            y1[idx] = Math.min(curY + safety, y1[idx]);//getting top left corner
            x2[idx] = Math.max(curX + safety, x2[idx]);//getting bot right corner
            y2[idx] = Math.max(curY + safety, y2[idx]);//getting bot right corner
            if(isBlack(binarized, curX, curY)){
                setBlack(img, curX + safety, curY + safety);
                bold(img, curX + safety, curY + safety);
            }
            for(int i = 0; i < 4; i++)
                for(int j = 1; j <= 1; j++)
                q.add(new pair(curX + dx[i] * j, curY + dy[i] * j));//go throught 4 directions 
        }
    }
    private void clearImage(BufferedImage input){
        Graphics2D    graphics = input.createGraphics();
        graphics.setPaint(new Color ( 255, 255, 255 ));
        graphics.fillRect(0, 0, input.getWidth(), input.getHeight());
    }
    private void clear(int idx){
        x1[idx] = 10000;
        y1[idx] = 10000;
        x2[idx] = -10000;
        y2[idx] = -10000;
        int mx = Math.max(binarized.getWidth(), binarized.getHeight()) * 3;
        img = new BufferedImage(mx, mx, binarized.getType()); //hwa el klam dah 3ady eny 23ml new b3d m3mlt new fel global
 
        clearImage(img);
 
        for(int i = 0; i < squareSize; i++)
            for(int j = 0; j < squareSize; j++){
                op[i][j] = 0;
            }
 
    }
    private void centralize(){
        int left = 50, right = -50, up = 50, down = -50, shiftX, shiftY;
        for(int i = 0; i < squareSize; i++)
            for(int j = 0; j < squareSize; j++)
                if(op[i][j] == 1){
                    up = Math.min(up, i);
                    down = Math.max(down, i);
                    right = Math.max(right, j);
                    left = Math.min(left, j);
                }
        shiftX = (left + squareSize - 1 - right) / 2;
        shiftY = (up + squareSize - 1 - down) / 2;
//        System.out.println("Left Right shiftX: " + left + " " + right + " " + shiftX);
        double [][]temp = new double[squareSize][squareSize];
        for(int i = shiftY; i < squareSize; i++)
            for(int j = shiftX; j < squareSize; j++)
                temp[i][j] = op[i - shiftY][j - shiftX];
        for(int i = 0; i < squareSize; i++)
 
            for(int j = 0; j < squareSize; j++)
                op[i][j] = temp[i][j];
    }
    private void arrayToArrayList(){
        clearImage(img);
        for(int i = 0; i < squareSize; i++)
            for(int j = 0; j < squareSize; j++)
                if(op[j][i] == 1)
                    setBlack(img, i, j);
//        byte[] pix = (byte[])img.getRaster().getDataElements(0, 0, img.getWidth(), img.getHeight(), null);
 
        ArrayList<Double> pixels = new ArrayList<>();
 
        for(int i = 0; i < img.getHeight(); i++){
            for(int j = 0; j < img.getWidth(); j++){
                Integer val;
                if(img.getRGB(j, i) == -1)
                    val = 0;
                else 
                    val = 1;
                pixels.add(val.doubleValue());
            }
        }
        outPut.add(pixels);
 
    }
    private void imageTo2DArray(){
        for(int i = 0; i < img.getWidth(); i++){
            for(int j = 0; j < img.getHeight(); j++){
                int red = new Color(img.getRGB(i, j)).getRed();
                if(red <= 180)
                    op[j][i] = 1;
            }
        }
    }
    private void finalRetouch(int idx) throws IOException{
 
        int mx = Math.max(x2[idx] - x1[idx], y2[idx] - y1[idx]);
//        System.out.println(x1[idx] + " " + x2[idx] + " " + y1[idx] + " " + y2[idx]);
//        System.out.println(img.getWidth() + " " + img.getHeight());
        img = Scalr.crop(img, Math.max(x1[idx], 0), Math.max(y1[idx], 0), mx, mx);// + 10 lazm tt2kd eny mtl3tsh barra el sora
        img = Scalr.resize(img,Scalr.Method.ULTRA_QUALITY, 20, 20, Scalr.OP_ANTIALIAS, Scalr.OP_BRIGHTER);
 
//        ImageIO.write(img, "jpg",new File("D:\\Coding\\Java Project\\New_Dataset\\" + idx + ".jpg"));
        
 
//        img = Scalr.resize(img,Scalr.Method.ULTRA_QUALITY, squareSize, squareSize, Scalr.OP_ANTIALIAS, Scalr.OP_BRIGHTER);
//        img = Scalr.resize(img, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, squareSize, squareSize, Scalr.OP_ANTIALIAS);
        System.out.println("Image after crop & resize: " + img.getWidth() + " " + img.getHeight());
 
        imageTo2DArray();
        centralize();
        img = Scalr.pad(img, 4, Color.WHITE);
        arrayToArrayList();
        imgDebug.add(img);
        
    }
    private void print(){
        for(int j = 0; j < squareSize; j++){
            for(int k = 0; k < squareSize; k++)
                if(op[j][k] > 0.5)
                    System.out.print(1);
                else
                    System.out.print(0);
            System.out.println();
        }
        System.out.println("=======================================");
    }
    public  void segment() throws IOException{
        System.out.println("Image height and width: " + binarized.getHeight() + " " + binarized.getWidth());       
//        int idx = 0;
        for(int i = 0; i < binarized.getWidth(); i++){
            for(int j = 0; j < binarized.getHeight(); j++){
                if(!isVisit(i, j)){
                    System.out.println("Start new box" + i + " " + j);
                    clear(idx);
                    bfs(i, j, idx);
                    finalRetouch(idx);
                    print(); 
//                    if(idx == 10)
//                        return;
                    idx++;
 
                }
 
           }
        }
                System.out.println(x1[0] + " " + y1[0] + " " + x2[0] + " " + y2[0]);
 
    }
    private void writeImage(String output) throws IOException {
//        File file = new File(output + ".jpg");
//        ImageIO.write(img[0], "jpg", file);
//        return;
        System.out.println("Image counter: " + idx);
        Integer x = 0;
        for(int i = 0; i < imgDebug.size(); i++, x++){
            File file = new File(output + x.toString() + ".jpg");
            ImageIO.write(imgDebug.get(i), "jpg", file);
        }
//        ImageIO.write(binarized, "jpg", file);
    }
 
    // Return histogram of grayscale image
    public int[] imageHistogram(BufferedImage input) {
 
        int[] histogram = new int[256];
 
        for(int i=0; i<histogram.length; i++) histogram[i] = 0;
 
        for(int i=0; i<input.getWidth(); i++) {
            for(int j=0; j<input.getHeight(); j++) {
                int red = new Color(input.getRGB (i, j)).getRed();
                histogram[red]++;
            }
        }
 
        return histogram;
 
    }
 
    // The luminance method
    public BufferedImage toGray(BufferedImage original) {
 
        int alpha, red, green, blue;
        int newPixel;
 
        BufferedImage lum = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
 
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
 
                // Get pixels by R, G, B
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();
 
                red = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
                // Return back to original format
                newPixel = colorToRGB(alpha, red, red, red);
 
                // Write pixels into image
                lum.setRGB(i, j, newPixel);
 
            }
        }
 
        return lum;
 
    }
 
    // Get binary treshold using Otsu's method
    private int otsuTreshold(BufferedImage original) {
 
        int[] histogram = imageHistogram(original);
        int total = original.getHeight() * original.getWidth();
 
        float sum = 0;
        for(int i=0; i<256; i++) sum += i * histogram[i];
 
        float sumB = 0;
        int wB = 0;
        int wF = 0;
 
        float varMax = 0;
        int threshold = 0;
 
        for(int i=0 ; i<256 ; i++) {
            wB += histogram[i];
            if(wB == 0) continue;
            wF = total - wB;
 
            if(wF == 0) break;
 
            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;
 
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
 
            if(varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }
 
        return threshold;
 
    }
 
    public BufferedImage binarize(BufferedImage original) {
 
        int red;
        int newPixel;
 
        int threshold = otsuTreshold(original);
 
        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
 
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
 
                // Get pixels
                red = new Color(original.getRGB(i, j)).getRed();
                int alpha = new Color(original.getRGB(i, j)).getAlpha();
                if(red > threshold) {
                    newPixel = 255;
                }
                else {
                    newPixel = 0;
                }
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                binarized.setRGB(i, j, newPixel); 
 
            }
        }
 
        return binarized;
 
    }
 
    // Convert R, G, B, Alpha to standard 8 bit
    private int colorToRGB(int alpha, int red, int green, int blue) {
 
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;
 
        return newPixel;
 
    }
 
}








































































 
//import com.sun.scenario.effect.Crop;
//import java.awt.Color;
//import java.awt.Graphics2D;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.Queue;
//import javax.imageio.ImageIO;
//import javax.swing.DebugGraphics;
//import org.imgscalr.AsyncScalr;
//import org.imgscalr.Scalr;
// 
//public class Segmentation {
// 
//    public class pair{
//        int first; int second;
// 
//        public pair(int first, int second) {
//            this.first = first;
//            this.second = second;
//        }
// 
//    }
// 
//    public Segmentation(String path) throws IOException {
//        File sourceimage = new File(path);
//        original = ImageIO.read(sourceimage);
//    }
//    
//    public Segmentation(BufferedImage img){
//        original = img;
//    }
// 
//    private static final int squareSize = 21;
//    public  BufferedImage original, grayscale, binarized;
//    private  BufferedImage img;
//    private static ArrayList<BufferedImage> imgDebug = new ArrayList<>();
//    private static double [][]op = new double[squareSize][squareSize];
//    public ArrayList<ArrayList<Double> >outPut = new ArrayList<>();
// 
//    private int []x1 = new int[3000];//x cordinate of left top corner
//    private int []x2 = new int[3000];//x cordinate of right bot corner
//    private int []y1 = new int[3000];//y cordinate of left top corner
//    private int []y2 = new int[3000];//y cordinate of right bot corner
//    private int idx;
////    static final int H = 1400, W = 1400;
//    private int [][]visit = new int [2400][2400];
//    private int []dx = {0, 0, 1, -1, 1, 1, -1, -1};//dfs x-axis directions
//    private int []dy = {1, -1, 0, 0, 1, -1, -1, 1};//dfs y-axis directions
// 
//    public StringBuffer solve() throws FileNotFoundException{
//        Double gh;
//        gh = 0.0;
//        int kkk = squareSize * squareSize;
//        NeuralNetwork asd = new NeuralNetwork(21 * 21, 75, 16, 0, gh, true);
//        ExpectedClasses qwe = new ExpectedClasses();
//        StringBuffer toCalc = new StringBuffer();
// 
//        System.out.println("outPut.size() = " + outPut.size());
// 
//        ArrayList<Double> debug= new ArrayList<>();
//        for(int i = 0; i < outPut.size(); i++){
//            debug = asd.ForwardPropagation(outPut.get(i));
// 
////            System.out.println("Iteration " + i);
////            for(int j = 0; j < debug.size(); j++)
////                 System.out.print(debug.get(j) + ", ");
//             System.out.println();
//             System.out.println("Expected " + i + ": " + qwe.getExpected(debug));
// 
////             0 1 2 3 4 5 6 7 8 9 + - * / [ ]
//            char c = qwe.getExpected(debug);
//            toCalc.insert(toCalc.length(), c);
//        }
////        System.out.println("Calculator input size: " + toCalc.length());
////        for(int i = 0; i < toCalc.length(); i++)
////            System.out.print(toCalc.charAt(i) + " ");
////        System.out.println();
////        System.out.println("Finished");
//        return toCalc;
//    }
//    private Boolean isDot(int idx){
//        return y2[idx] - y1[idx] < 30 && x2[idx] - x1[idx] < 30;
//    }
//        public StringBuffer improve(StringBuffer input){
//        int up = 0, down = 0, idx = 0;
//        Boolean start = false, inPower = false;
//        for(int i = 0; i < input.length(); i++, idx++){
////            System.out.println(i + " ! " + idx);
//            Boolean flag = false;
//            if(input.charAt(i) == '['){
//                input.replace(i, i + 1, "(");
//            }
//            else if(input.charAt(i) == ']'){
//                input.replace(i, i + 1, ")");
//            }
//            else if(i + 2 < input.length() && isDot(idx + 1) == true && isDot(idx + 2) == true){
////                System.out.print(i + " ! ");
//                input.replace(i, i + 3, "/");
////                System.out.println(i);
//                flag = true;
//            }
//            if(isDot(idx) == false && inPower == false && ((down - up) / 3) + up >= y2[idx]){
//                inPower = true;
//                input.insert(i, "^(");
//                i += 2;
//            }else if(isDot(idx) == false && inPower == true && ((y2[idx] - y1[idx]) / 3) + y1[idx] >= down){
//                inPower = false;
//                input.insert(i, ")");
//                i += 1;
//            }
//            if(isDigit(input.charAt(i))){
//                start = true;
//                up = y1[idx];
//                down = y2[idx];
//            }
// 
//            if(flag == true)
//                idx += 2;
//        }
//        if(inPower == true)
//            input.insert(input.length(), ")");
//        return input;
//    }
////    public  void main(String[] args) throws IOException {
////        Segmentation xas = new Segmentation("original"+".jpg");
//////        File original_f = new File("original"+".jpg");
////        String output_f = "original"+"_bin";
//////        original = ImageIO.read(original_f);
////        grayscale = xas.toGray(original);
////        binarized = xas.binarize(grayscale);       
////        xas.segment();
////        imgDebug.add(binarized);
////        xas.writeImage(output_f);   
////        StringBuffer toCalc = xas.solve();
////        toCalc = xas.improve(toCalc);
////    }
//    private Boolean isDigit(char c){
//        return '0' <= c && c <= '9';
//    }
//    private Boolean isVisit(int x, int y){
//        //check x  = H and y = W or reversed ???
//        if(x < 0 || y < 0 || y >= binarized.getHeight() || x >= binarized.getWidth() || visit[x][y] == 1 || !isBlack(binarized, x, y)){
// 
//            return true;
//        }
//        return false;
//        //out of bound checking
//    }
//    private Boolean isBlack(BufferedImage input, int x, int y){
//        int red = new Color(input.getRGB(x, y)).getRed();
//        if(red == 0)
//            return true;
//        return false;
//    }
//    private void setBlack(BufferedImage input, int x, int y){
//        if(x < 0 || y < 0 || x >= input.getWidth() || y >= input.getHeight())
//            return;
//        input.setRGB(x, y, colorToRGB(0, 0, 0, 0)); 
//    }
//    private void bold(BufferedImage input, int x, int y){
//        for(int i = 0; i < 8; i++)
//            for(int j = 1; j <= 2; j++)
//                setBlack(input, x + dx[i] * j, y + dy[i] * j);
//    }
//    private void bfs(int x, int y, int idx){
//        int safety = Math.max(binarized.getWidth(), binarized.getHeight());
//        Queue<pair> q= new LinkedList<pair>();
//        q.add(new pair(x, y));
//        while(!q.isEmpty()){
//            int curX = q.peek().first;
//            int curY = q.peek().second;
//            q.remove();
//            if(isVisit(curX, curY))
//                continue;
//            visit[curX][curY] = 1;
// 
// 
//            x1[idx] = Math.min(curX + safety, x1[idx]);//getting top left corner
//            y1[idx] = Math.min(curY + safety, y1[idx]);//getting top left corner
//            x2[idx] = Math.max(curX + safety, x2[idx]);//getting bot right corner
//            y2[idx] = Math.max(curY + safety, y2[idx]);//getting bot right corner
//            if(isBlack(binarized, curX, curY)){
//                setBlack(img, curX + safety, curY + safety);
//                bold(img, curX + safety, curY + safety);
//            }
//            for(int i = 0; i < 4; i++)
//                for(int j = 1; j <= 1; j++)
//                q.add(new pair(curX + dx[i] * j, curY + dy[i] * j));//go throught 4 directions 
//        }
//    }
//    private void clearImage(BufferedImage input){
//        Graphics2D    graphics = input.createGraphics();
//        graphics.setPaint(new Color ( 255, 255, 255 ));
//        graphics.fillRect(0, 0, input.getWidth(), input.getHeight());
//    }
//    private void clear(int idx){
//        x1[idx] = 10000;
//        y1[idx] = 10000;
//        x2[idx] = -10000;
//        y2[idx] = -10000;
//        int mx = Math.max(binarized.getWidth(), binarized.getHeight()) * 3;
//        img = new BufferedImage(mx, mx, binarized.getType()); //hwa el klam dah 3ady eny 23ml new b3d m3mlt new fel global
// 
//        clearImage(img);
// 
//        for(int i = 0; i < squareSize; i++)
//            for(int j = 0; j < squareSize; j++){
//                op[i][j] = 0;
//            }
// 
//    }
//    private void centralize(){
//        int left = 50, right = -50, up = 50, down = -50, shiftX, shiftY;
//        for(int i = 0; i < squareSize; i++)
//            for(int j = 0; j < squareSize; j++)
//                if(op[i][j] == 1){
//                    up = Math.min(up, i);
//                    down = Math.max(down, i);
//                    right = Math.max(right, j);
//                    left = Math.min(left, j);
//                }
//        shiftX = (left + squareSize - 1 - right) / 2;
//        shiftY = (up + squareSize - 1 - down) / 2;
////        System.out.println("Left Right shiftX: " + left + " " + right + " " + shiftX);
//        double [][]temp = new double[squareSize][squareSize];
//        for(int i = shiftY; i < squareSize; i++)
//            for(int j = shiftX; j < squareSize; j++)
//                temp[i][j] = op[i - shiftY][j - shiftX];
//        for(int i = 0; i < squareSize; i++)
// 
//            for(int j = 0; j < squareSize; j++)
//                op[i][j] = temp[i][j];
//    }
//    private void arrayToArrayList(){
//        clearImage(img);
//        for(int i = 0; i < squareSize; i++)
//            for(int j = 0; j < squareSize; j++)
//                if(op[j][i] == 1)
//                    setBlack(img, i, j);
////        byte[] pix = (byte[])img.getRaster().getDataElements(0, 0, img.getWidth(), img.getHeight(), null);
// 
//        ArrayList<Double> pixels = new ArrayList<>();
// 
//        for(int i = 0; i < img.getHeight(); i++){
//            for(int j = 0; j < img.getWidth(); j++){
//                Integer val;
//                if(img.getRGB(j, i) == -1)
//                    val = 0;
//                else 
//                    val = 1;
//                pixels.add(val.doubleValue());
//            }
//        }
//        outPut.add(pixels);
// 
//    }
//    private void imageTo2DArray(){
//        for(int i = 0; i < img.getWidth(); i++){
//            for(int j = 0; j < img.getHeight(); j++){
//                int red = new Color(img.getRGB(i, j)).getRed();
//                if(red <= 180)
//                    op[j][i] = 1;
//            }
//        }
//    }
//    private void finalRetouch(int idx){
// 
//        int mx = Math.max(x2[idx] - x1[idx], y2[idx] - y1[idx]);
////        System.out.println(x1[idx] + " " + x2[idx] + " " + y1[idx] + " " + y2[idx]);
////        System.out.println(img.getWidth() + " " + img.getHeight());
//        img = Scalr.crop(img, Math.max(x1[idx], 0), Math.max(y1[idx], 0), mx, mx);// + 10 lazm tt2kd eny mtl3tsh barra el sora
//        img = Scalr.resize(img,Scalr.Method.ULTRA_QUALITY, squareSize, squareSize, Scalr.OP_ANTIALIAS, Scalr.OP_BRIGHTER);
// 
////        img = Scalr.pad(img, 2, Color.WHITE);
// 
////        img = Scalr.resize(img,Scalr.Method.ULTRA_QUALITY, squareSize, squareSize, Scalr.OP_ANTIALIAS, Scalr.OP_BRIGHTER);
////        img = Scalr.resize(img, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, squareSize, squareSize, Scalr.OP_ANTIALIAS);
//        System.out.println("Image after crop & resize: " + img.getWidth() + " " + img.getHeight());
// 
//        imageTo2DArray();
//        centralize();
//        arrayToArrayList();
//        imgDebug.add(img);
// 
//    }
//    private void print(){
//        for(int j = 0; j < squareSize; j++){
//            for(int k = 0; k < squareSize; k++)
//                if(op[j][k] > 0.5)
//                    System.out.print(1);
//                else
//                    System.out.print(0);
//            System.out.println();
//        }
//        System.out.println("=======================================");
//    }
//    public  void segment(){
//        System.out.println("Image height and width: " + binarized.getHeight() + " " + binarized.getWidth());       
////        int idx = 0;
//        for(int i = 0; i < binarized.getWidth(); i++){
//            for(int j = 0; j < binarized.getHeight(); j++){
//                if(!isVisit(i, j)){
//                    System.out.println("Start new box" + i + " " + j);
//                    clear(idx);
//                    bfs(i, j, idx);
//                    finalRetouch(idx);
//                    print(); 
////                    if(idx == 10)
////                        return;
//                    idx++;
// 
//                }
// 
//           }
//        }
//                System.out.println(x1[0] + " " + y1[0] + " " + x2[0] + " " + y2[0]);
// 
//    }
//    private void writeImage(String output) throws IOException {
////        File file = new File(output + ".jpg");
////        ImageIO.write(img[0], "jpg", file);
////        return;
//        System.out.println("Image counter: " + idx);
//        Integer x = 0;
//        for(int i = 0; i < imgDebug.size(); i++, x++){
//            File file = new File(output + x.toString() + ".jpg");
//            ImageIO.write(imgDebug.get(i), "jpg", file);
//        }
////        ImageIO.write(binarized, "jpg", file);
//    }
// 
//    // Return histogram of grayscale image
//    public int[] imageHistogram(BufferedImage input) {
// 
//        int[] histogram = new int[256];
// 
//        for(int i=0; i<histogram.length; i++) histogram[i] = 0;
// 
//        for(int i=0; i<input.getWidth(); i++) {
//            for(int j=0; j<input.getHeight(); j++) {
//                int red = new Color(input.getRGB (i, j)).getRed();
//                histogram[red]++;
//            }
//        }
// 
//        return histogram;
// 
//    }
// 
//    // The luminance method
//    public BufferedImage toGray(BufferedImage original) {
// 
//        int alpha, red, green, blue;
//        int newPixel;
// 
//        BufferedImage lum = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
// 
//        for(int i=0; i<original.getWidth(); i++) {
//            for(int j=0; j<original.getHeight(); j++) {
// 
//                // Get pixels by R, G, B
//                alpha = new Color(original.getRGB(i, j)).getAlpha();
//                red = new Color(original.getRGB(i, j)).getRed();
//                green = new Color(original.getRGB(i, j)).getGreen();
//                blue = new Color(original.getRGB(i, j)).getBlue();
// 
//                red = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
//                // Return back to original format
//                newPixel = colorToRGB(alpha, red, red, red);
// 
//                // Write pixels into image
//                lum.setRGB(i, j, newPixel);
// 
//            }
//        }
// 
//        return lum;
// 
//    }
// 
//    // Get binary treshold using Otsu's method
//    private int otsuTreshold(BufferedImage original) {
// 
//        int[] histogram = imageHistogram(original);
//        int total = original.getHeight() * original.getWidth();
// 
//        float sum = 0;
//        for(int i=0; i<256; i++) sum += i * histogram[i];
// 
//        float sumB = 0;
//        int wB = 0;
//        int wF = 0;
// 
//        float varMax = 0;
//        int threshold = 0;
// 
//        for(int i=0 ; i<256 ; i++) {
//            wB += histogram[i];
//            if(wB == 0) continue;
//            wF = total - wB;
// 
//            if(wF == 0) break;
// 
//            sumB += (float) (i * histogram[i]);
//            float mB = sumB / wB;
//            float mF = (sum - sumB) / wF;
// 
//            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
// 
//            if(varBetween > varMax) {
//                varMax = varBetween;
//                threshold = i;
//            }
//        }
// 
//        return threshold;
// 
//    }
// 
//    public BufferedImage binarize(BufferedImage original) {
// 
//        int red;
//        int newPixel;
// 
//        int threshold = otsuTreshold(original);
// 
//        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
// 
//        for(int i=0; i<original.getWidth(); i++) {
//            for(int j=0; j<original.getHeight(); j++) {
// 
//                // Get pixels
//                red = new Color(original.getRGB(i, j)).getRed();
//                int alpha = new Color(original.getRGB(i, j)).getAlpha();
//                if(red > threshold) {
//                    newPixel = 255;
//                }
//                else {
//                    newPixel = 0;
//                }
//                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
//                binarized.setRGB(i, j, newPixel); 
// 
//            }
//        }
// 
//        return binarized;
// 
//    }
// 
//    // Convert R, G, B, Alpha to standard 8 bit
//    private int colorToRGB(int alpha, int red, int green, int blue) {
// 
//        int newPixel = 0;
//        newPixel += alpha;
//        newPixel = newPixel << 8;
//        newPixel += red; newPixel = newPixel << 8;
//        newPixel += green; newPixel = newPixel << 8;
//        newPixel += blue;
// 
//        return newPixel;
// 
//    }
// 
//}