/**
 * Created by corpa on 6/10/17.
 */

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProfileFrame extends Application {

    Group root;

    @Override
    public void start(Stage primaryStage) {

        root = new Group();

        Stage stage = new Stage();
        stage.setTitle("My New Stage Title");
        stage.setScene(new Scene(root, 450, 450));
        stage.show();
    }
}
