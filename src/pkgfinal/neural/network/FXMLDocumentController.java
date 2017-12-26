/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgfinal.neural.network;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 *
 * @author Administrator
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label2;
    @FXML
    private Button buttonLoad;
    @FXML
    private Button buttonCalculate;
    @FXML
    private ImageView myImageView;
    
    BufferedImage bufferedImage;
    @FXML
    private Label label1;
    @FXML
    private Label label21;
    @FXML
    private Label label11;
    
    private void handleButtonAction(ActionEvent event) {
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void handleButtonActionLoad(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
             
            //Set extension filter
            FileChooser.ExtensionFilter extFilterJPG = 
                    new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
            FileChooser.ExtensionFilter extFilterjpg = 
                    new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
            FileChooser.ExtensionFilter extFilterPNG = 
                    new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
            FileChooser.ExtensionFilter extFilterpng = 
                    new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            fileChooser.getExtensionFilters()
                    .addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);

            //Show open file dialog
            File file = fileChooser.showOpenDialog(null);
            
            try {
                bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                myImageView.setImage(image);
                myImageView.fitWidthProperty();
            } catch (IOException ex) {
                Logger.getLogger(FinalNeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @FXML
    private void handleButtonActionCalc(ActionEvent event) throws FileNotFoundException, IOException {
        Segmentation seg = new Segmentation(bufferedImage);
        seg.grayscale = seg.toGray(seg.original);
        seg.binarized = seg.binarize(seg.grayscale);
        seg.segment();
//        System.out.println(seg.improve(seg.solve()));
        Calculator cal = new Calculator(seg.improve(seg.solve()).toString(), Calculator.EXPRESSIONTYPE.Infix);
//        System.out.println(cal.cleanandeval(seg.improve(seg.solve()).toString()));
        label2.setText(seg.improve(seg.solve()).toString());
        Double answer = cal.GetValue();
        label1.setText(answer.toString());
    }
    
}
