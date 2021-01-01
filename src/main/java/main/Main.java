package main;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application implements EventHandler <ActionEvent> {

    public static Stage stage;

    public static void main( String[] args )
    { launch( args );  }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource( "/ui/welcome.fxml" ));
        stage.setResizable( false );
        stage.getIcons().removeAll();
        stage.setScene(new Scene(root,615,315));
        stage.show();
    }

    @Override
    public void handle(ActionEvent event) {}
}
