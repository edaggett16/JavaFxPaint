package Paint;

import java.util.Stack;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
* Tools class is made up of methods specified for editing options available for 
* selection by the user
*
*
* @author   Emanuel Daggett
* @version  4.0
* @since    9/19/2017
* **/

public class Tools {
    //FILLS THE ENTIRE CANVAS WITH DESIRED COLOR CHOSEN BY USER
    public static void fill(Canvas canvas, GraphicsContext gc, StackPane pane, Stack<Image> stack, ColorPicker cp){
        clear(canvas, gc);
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();
        
        canvas.setOnMousePressed((MouseEvent e) -> {
            gc.setFill(cp.getValue());
            gc.fillRect(0,0,canvasWidth, canvasHeight);
            snapshot(pane, stack);
        });  
    }
    
   //ALLOWS USER TO FREE DRAW FROM ONE COORDINATE TO THE NEXT
    public static void scribble(Canvas canvas, GraphicsContext gc, StackPane pane, Stack<Image> stack){
        clear(canvas, gc);
        canvas.setOnMousePressed((MouseEvent e) -> {
           gc.beginPath();
           gc.lineTo(e.getX(), e.getY());
           snapshot(pane, stack);

        });
        
       canvas.setOnMouseDragged((MouseEvent e) -> {
           gc.lineTo(e.getX(), e.getY());
           gc.stroke();
        });
       
       canvas.setOnMouseReleased((MouseEvent e) ->  {
            gc.closePath();
       });
    }
   
    //RESTRICTS USER TO ONLY DRAWING A STRAIGHT LINE
    public static void drawLine(Canvas canvas, GraphicsContext gc, StackPane pane, Stack<Image> stack){
        clear(canvas, gc);
        canvas.setOnMousePressed((MouseEvent e) -> {
           gc.beginPath();
           gc.lineTo(e.getX(), e.getY());
           snapshot(pane, stack);

        });
        
       canvas.setOnMouseDragged((MouseEvent e) -> {
           gc.stroke();
        });
       
       canvas.setOnMouseReleased((MouseEvent e) -> {
           gc.lineTo(e.getX(), e.getY());
           gc.stroke();
           gc.closePath();
        });
    }
    
    public static void drawRect(Canvas canvas, GraphicsContext gc, StackPane pane, Stack<Image> stack){
       clear(canvas, gc);
       Rectangle rect = new Rectangle();
        
        canvas.setOnMousePressed((MouseEvent e) -> {
           gc.beginPath();
           rect.setX(e.getX());
           rect.setY(e.getY());
        });
        
       canvas.setOnMouseDragged((MouseEvent e) -> {
           rect.setWidth(e.getX() - rect.getX());
           rect.setHeight(e.getY() - rect.getY());
        });
       
       canvas.setOnMouseReleased((MouseEvent e) -> {
           gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
           gc.closePath();
           snapshot(pane, stack);
        });
    }
    
    public static void drawSquare(Canvas canvas, GraphicsContext gc, StackPane pane, Stack<Image> stack){
       clear(canvas, gc);
       Rectangle rect = new Rectangle();
        
        canvas.setOnMousePressed((MouseEvent e) -> {
           gc.beginPath();
           rect.setX(e.getX());
           rect.setY(e.getY());
           snapshot(pane, stack);
        });
        
        canvas.setOnMouseDragged((MouseEvent e) -> {
           rect.setWidth(e.getX() - rect.getX());
           rect.setHeight(e.getX() - rect.getX());

        });
        
        canvas.setOnMouseReleased((MouseEvent e) -> {
           gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
           gc.closePath();
           
        });
    }
     
    public static void drawCircle(Canvas canvas, GraphicsContext gc, StackPane pane, Stack<Image> stack){
       clear(canvas, gc); 
       Circle circ = new Circle();
       
        canvas.setOnMousePressed((MouseEvent e) -> {
           gc.beginPath();
           circ.setCenterX(e.getX());
           circ.setCenterY(e.getY());
           snapshot(pane, stack);

        });
        
        canvas.setOnMouseDragged((MouseEvent e) -> {
           circ.setRadius(e.getX());
           
        });
        
        canvas.setOnMouseReleased((MouseEvent e) -> {
           gc.strokeOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());
           gc.closePath();
        });
    }
     
    public static void eraser(Canvas canvas, GraphicsContext gc, StackPane pane, Stack<Image> stack, Slider slider){
        clear(canvas, gc);
        canvas.setOnMousePressed((MouseEvent evt) -> {
           gc.beginPath();
           gc.clearRect(evt.getX(), evt.getY(), slider.getValue(), slider.getValue());
        });
        
        canvas.setOnMouseDragged((MouseEvent evt) -> {
            gc.clearRect(evt.getX(), evt.getY(), slider.getValue(), slider.getValue());
        });
       
        canvas.setOnMouseReleased((MouseEvent e) -> {
           gc.closePath();
           snapshot(pane, stack);
        });
    }
    
    //METHOD THAT CLEARS MOUSE EVENT DATA UPON THE SLECTION OF A NEW TOOL
    public static void clear(Canvas canvas, GraphicsContext gc){
        canvas.setOnMousePressed(null);
        canvas.setOnMouseDragged(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseClicked(null);
    }
    
    //ALLOWS USER TO UTILIZE A COLOR THAT IS ALREADY IN USE OR DISPLAYED
    public static void dropper(Canvas canvas, GraphicsContext gc, StackPane pane, Stack<Image> stack, ColorPicker cp){
        clear(canvas, gc);
        Image image = pane.snapshot(null, null);
        PixelReader reader = image.getPixelReader();
        
        canvas.setOnMousePressed((MouseEvent e) -> {
            cp.setValue(reader.getColor((int)e.getX(), (int)e.getX()));
            gc.setStroke(cp.getValue());
        });  
    }
    
    //ALLOWS USER TO ADD TEXT TO IMAGE OR CANVAS
    public static void textBox(Canvas canvas, GraphicsContext gc, StackPane pane, Stack<Image> stack){
        clear(canvas, gc);
        canvas.setOnMouseClicked((MouseEvent e) -> {
            BorderPane border = new BorderPane();
            Scene scene = new Scene(border, 200, 100);
            Stage stage = new Stage();
            stage.setTitle("Input Text!");
            stage.setScene(scene);
            stage.show();
            
            TextArea textBox = new TextArea();
            textBox.setFont(Font.font("Times New Roman", 15));
            textBox.setPrefHeight(300);
            textBox.setMinHeight(5);
            textBox.setMaxHeight(400);

            textBox.setPrefWidth(400);
            textBox.setMinWidth(5);
            textBox.setMaxWidth(500);

            textBox.setPrefSize(70, 70);
            textBox.setMinSize(30, 10);
            textBox.setMaxSize(80, 80);
            
            Button insert = new Button("Insert Text");
            border.setTop(textBox);
            border.setBottom(insert);
            
            insert.setOnAction((ActionEvent evt) -> {
                gc.setFont( new Font( "Calibri", 48) );
                gc.strokeText(textBox.getText(), e.getX(), e.getY());
                stage.close();
        });
            snapshot(pane,stack);
        });
     }
   
    //METHOD THAT ALLOWS USER TO SAVE CHANGES MADE TO THE CANVAS AS A PICTURE
    public static void snapshot(StackPane pane, Stack<Image> stack){
        Image image = pane.snapshot(null, null);
        stack.push(image);
    } 
    
    //METHOD THAT RESTORES PREVIOUS WORK
    public static void undo(GraphicsContext gc, Stack<Image> stack){
        if(!stack.empty()){
              gc.drawImage(stack.pop(), 0,0);
        }
    } 
}


