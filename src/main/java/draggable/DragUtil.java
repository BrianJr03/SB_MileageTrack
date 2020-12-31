package draggable;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

public abstract class DragUtil {

    private double x;
    private double y;

    public void setDraggable( Node node, Stage stage) {

        node.setOnMousePressed( event -> {
            if (event.getButton() == MouseButton.PRIMARY){
                x = event.getSceneX();
                y = event.getSceneY();
            }
        } );

        node.setOnMouseDragged( event -> {
            if (event.getButton() == MouseButton.PRIMARY){
                stage.setX( event.getScreenX() - x );
                stage.setY( event.getScreenY() - y );
            }
        } );

    }
}
