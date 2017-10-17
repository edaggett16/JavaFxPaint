package Paint;

import java.awt.image.RenderedImage;
import javafx.scene.paint.Color;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.WindowEvent;

/**
 * Paint Application.
 * 
 * PaintApp is the main and executable file for the entire Paint Application
 * This file is responsible for running and displaying the application to the 
 * user once the program has been initiated 
 *
 * @author  Emanuel Daggett
 * @version 5.0
 * @since   9/10/17
 **/

public class PaintApp extends Application {
    File file;
    Image image;
   
    @Override
    public void start(Stage primaryStage) {
       //MAIN LAYOUT CONFIGURATION IN A BORDERPANE
       BorderPane root = new BorderPane();
       
       Scene scene = new Scene(root, 750, 600);
       
       //ADDS MENUBAR AND OPTIONS TO THE SCENE
       MenuBar menuBar = new MenuBar();
       Menu menuFile = new Menu("File");
       Menu menuEdit = new Menu("Edit");
       Menu menuView = new Menu("View");
       Menu menuHelp = new Menu("Help");
       menuBar.getMenus().addAll(menuFile, menuEdit, menuView, menuHelp);
       MenuItem OpenImage = new MenuItem("Open");
       MenuItem fileSaveAs = new MenuItem("Save As");
       MenuItem fileSave = new MenuItem("Save");
       MenuItem newFile = new Menu("New");
       menuFile.getItems().addAll(newFile, OpenImage, fileSaveAs, fileSave);
       
       root.setTop(menuBar);
       
       Canvas canvas = new Canvas(600, 500);
       
       //ALLOWS USER TO EDIT CANVAS OBJECT
       GraphicsContext gc = canvas.getGraphicsContext2D();
       double canvasWidth = gc.getCanvas().getWidth();
       double canvasHeight = gc.getCanvas().getHeight();
       
       StackPane pane = new StackPane();
       
       //BLANK PAGE IMAGE ADDED FROM GOOGLE.COM
       Image blankImage = new Image("http://www.globalartmaterials.com/images/quattro/quattro-blank-paper.jpg");
       ImageView blankImageView = new ImageView(blankImage);
       blankImageView.setFitHeight(canvasHeight);
       blankImageView.setFitWidth(canvasWidth);
       
       newFile.setOnAction((ActionEvent e) -> {
           //ADDING BLANK IMAGE UNDER CANVAS SO IT CAN BE SAVED AND KEPT ON FILE
           pane.getChildren().addAll(blankImageView, canvas);
       });
       
       root.setBottom(pane);
       
       //GC ALLOWS FOR MORE OPTIONS i.e, COLOR AND WIDTH OF EDITING TOOLS 
       gc.setStroke(Color.BLACK);
       gc.setLineWidth(5);
       
       //ALLOWS USER TO PICK WHAT COLOR THEY WANT TO EDIT THEIR PAINTING
       ColorPicker cp = new ColorPicker();
       cp.setValue(Color.BLACK);
       
       //COMBOBOX HOLDS THE DIFFERENT DRAW TOOLS FOR USER SELECTION
       ComboBox<String> drawOptions = new ComboBox();
       drawOptions.setPromptText("Select Tool");
       drawOptions.getItems().addAll("Fill","Scribble", "Line", "Rectangle", "Square", "Circle","Dropper", "Eraser", "TextBox");
        
       Button selectTool = new Button("Select");
       Button undo = new Button("Undo");
       Button clear = new Button("Clear");
       
       //LINKS THE GC DRAW TOOL TO THE COLORPICKER
       cp.setOnAction((ActionEvent e) -> {
           gc.setStroke(cp.getValue());
        });    
            
       Stack<Image> stack = new Stack();
       
       //SLIDER ALLOWS USER TO CHANGE LINE WIDTH, LABEL DISPLAYS THE WIDTH VALUE
       Slider slider = new Slider();
       slider.setMin(5);
       slider.setMax(45);
       slider.setShowTickLabels(true);
       slider.setShowTickMarks(true);
       slider.setMajorTickUnit(5f);
       
       //GridPane USED TO NICELY FORMAT CANVAS EDITING OPTIONS FOR USER (TOOLBAR)
       GridPane grid = new GridPane();
       grid.addRow(0, cp, slider, drawOptions, selectTool, undo, clear);
       grid.setHgap(20);
       grid.setPadding(new Insets(20,0,0,40));
       root.setCenter(grid);
       
       //THIS LISTENER LINKS THE SLIDER W/ THE LABEL AND THE SLIDER w/ LINE WIDTH
       slider.valueProperty().addListener(e ->{
           double value = (int) slider.getValue();
           gc.setLineWidth(value);
       });
     
       undo.setOnAction((ActionEvent e) -> {
          Tools.undo(gc, stack);
       });
       
       //IMPLEMENTATION OF CLEAR FUNCTION FOR ENTIRE CANVAS
       clear.setOnAction((ActionEvent e) -> {
           gc.clearRect(0,0,canvasWidth,canvasHeight);
       });
       
       //ALLOWS USER TO SPECIFY DRAW TOOL
       selectTool.setOnAction((ActionEvent e) -> {
           String value = drawOptions.getValue();
           //COMPARES THE SELECTED DRAWOPTION TO THE SAME STRING
           if ("Fill".equals(value)){
               Tools.fill(canvas,gc,pane,stack,cp);
           }
           else;
           if ("Scribble".equals(value)){
               Tools.scribble(canvas,gc,pane,stack);
           }
           else;
           if("Line".equals(value)){
               Tools.drawLine(canvas, gc,pane,stack);
           }
           else;
           if("Rectangle".equals(value)){
               Tools.drawRect(canvas, gc,pane,stack);
           }
           else;
           if("Square".equals(value)){
               Tools.drawSquare(canvas, gc,pane,stack);
           }
           else;
           if("Circle".equals(value)){
               Tools.drawCircle(canvas, gc,pane,stack);
           }
           if("Dropper".equals(value)){
               Tools.dropper(canvas, gc,pane,stack,cp);
           }
           else;
           if("Eraser".equals(value)){
               Tools.eraser(canvas, gc, pane, stack, slider);
           }
           else;
           if("TextBox".equals(value)){
               Tools.textBox(canvas, gc,pane,stack);
           }
           else;
       });
      
       //SMARTSAVE IMPLEMENTAITION MUST CONFIRM CLOSE
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("SmartSave");
            alert.setContentText("Would you like to save your work?");
        
            ButtonType dontButton = new ButtonType("Don't Save"); 
            ButtonType saveButton = new ButtonType("Save"); 
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE); 
        
            alert.getButtonTypes().setAll(dontButton, saveButton, cancelButton);
            alert.showAndWait();
        
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == dontButton){
                System.exit(0);
            }
            else if((result.get() == saveButton)){
                FileChooser fileChooser = new FileChooser();
                setExtFilters(fileChooser);
                file = fileChooser.showSaveDialog(primaryStage);
                if (file != null) {
                    try {
                        WritableImage writableImage = new WritableImage((int) canvasWidth,(int) canvasHeight);
                        pane.snapshot(null, writableImage);
                        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                        ImageIO.write(renderedImage, "png", file);
                    }
                    catch (IOException ex) {
                        Logger.getLogger(PaintApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            if(result.get() == cancelButton){
               event.consume();
            }
            else;
        });   
        
        //ALLOWS USER TO PICK AN IMAGE FROM FILE THAT WILL REPLACE THE blankImageView 
        OpenImage.setOnAction((ActionEvent e) -> {
            FileChooser fileChooser = new FileChooser();
            setExtFilters(fileChooser);
            file = fileChooser.showOpenDialog(primaryStage);
            Image openedImage = new Image(file.toURI().toString());
            blankImageView.setFitWidth((int) canvasWidth);
            blankImageView.setFitHeight((int) canvasHeight);
            blankImageView.setImage(openedImage);
        });
        
        //ALLOWS USER TO SAVE WORK WHEREVER
        fileSaveAs.setOnAction((ActionEvent e) -> {
            FileChooser fileChooser = new FileChooser();
            setExtFilters(fileChooser);
            file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    WritableImage writableImage = new WritableImage((int) canvasWidth,(int) canvasHeight);
                    pane.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                }
                catch (IOException ex) {
                    Logger.getLogger(PaintApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        //ALLOW USER TO SAVE CHANGES TO OPENED FILE
        fileSave.setOnAction((ActionEvent e) -> {
            FileChooser fileChooser1 = new FileChooser();
            setExtFilters(fileChooser1);
                try {
                    WritableImage writableImage = new WritableImage((int) canvasWidth,(int) canvasHeight);
                    pane.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                }
                catch (IOException ex) {
                    Logger.getLogger(PaintApp.class.getName()).log(Level.SEVERE, null, ex);
                }
        });
        
        primaryStage.setTitle("Paint");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    //ALL THE DIFFERENT FILE YPES A USER CAN SAVE IMAGE AS
    private void setExtFilters(FileChooser chooser){
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpg"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp")
        );
    }
  
    public static void main(String[] args) {
        launch(args);
    }
}
