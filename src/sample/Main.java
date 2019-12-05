package sample;

import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        setPrimaryStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        //root.setStyle("-fx-background-image: url('http://getwallpapers.com/wallpaper/full/2/b/6/548462.jpg')");
        //root.setEffect(new GaussianBlur());
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 683, 489));
        primaryStage.show();
    }


    public static void main(String[] args) {
      launch(args);
    }
    private void setPrimaryStage(Stage primaryStage){
        Main.primaryStage = primaryStage;
    }
    public static Stage getPrimaryStage(){
        return primaryStage;
    }
}
