
package pkgfinal.neural.network;

import java.util.ArrayList;
import java.util.Arrays;

public class ExpectedClasses {
    ArrayList< ArrayList<Double> > Expected = new ArrayList<>();
    
    public ExpectedClasses(){
        // 0
        Expected.add(new ArrayList<>(Arrays.asList(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        // 1
        Expected.add(new ArrayList<>(Arrays.asList(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        // 2
        Expected.add(new ArrayList<>(Arrays.asList(0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        // 3
        Expected.add(new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        // 4
        Expected.add(new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        // 5
        Expected.add(new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        // 6
        Expected.add(new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        // 7
        Expected.add(new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        // 8
        Expected.add(new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        // 9
        Expected.add(new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0)));
        // +
        Expected.add(new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0)));
        // -
        Expected.add(new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0)));
        // x
        Expected.add(new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0)));
        // [
        Expected.add(new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0)));
        // ]
        Expected.add(new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0)));
    }
    
    public ArrayList<Double> getExpected(Integer index){
        return Expected.get(index);
    }
    
    public char getExpected(ArrayList<Double> output){
//        for(int i = 0; i < output.size(); i++){
//            if(output.get(i) > 0.8) output.set(i, 1.0);
//            else output.set(i, 0.0);
//        }
        int maxi = 0;
        Double d = -1.0;
        for(int i = 0; i < output.size(); i++){
            if(output.get(i) > d){
                d = output.get(i);
                maxi = i;
            }
        }
        for(int i = 0; i < output.size(); i++){
            if(i == maxi)
                output.set(i, 1.0);
            else output.set(i, 0.0);
        }
        ArrayList<Double> op = output;
//        System.out.println(output.size());
        if(op.equals(Expected.get(0))) return '0';
        else if(op.equals(Expected.get(1))) return '1';
        else if(op.equals(Expected.get(2))) return '2';
        else if(op.equals(Expected.get(3))) return '3';
        else if(op.equals(Expected.get(4))) return '4';
        else if(op.equals(Expected.get(5))) return '5';
        else if(op.equals(Expected.get(6))) return '6';
        else if(op.equals(Expected.get(7))) return '7';
        else if(op.equals(Expected.get(8))) return '8';
        else if(op.equals(Expected.get(9))) return '9';
        else if(op.equals(Expected.get(10))) return '+';
        else if(op.equals(Expected.get(11))) return '-';
        else if(op.equals(Expected.get(12))) return 'x';
        else if(op.equals(Expected.get(13))) return '/';
        else if(op.equals(Expected.get(14))) return '[';
        else if(op.equals(Expected.get(15))) return ']';
        else return 'c';
    }
    
}
