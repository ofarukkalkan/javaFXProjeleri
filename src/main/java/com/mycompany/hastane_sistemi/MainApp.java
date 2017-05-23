package com.mycompany.hastane_sistemi;

import java.util.Iterator;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainApp extends Application {

    Stage yonetici_scene;
    Stage sekreter_scene;
    Stage doktor_scene;
    TextField id_tf;
    TextField pw_tf;

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        Parent yonetici_scene_root = FXMLLoader.load(getClass().getResource("/fxml/yonetici_scene.fxml"));
        Parent sekreter_scene_root = FXMLLoader.load(getClass().getResource("/fxml/sekreter_scene.fxml"));
        Parent doktor_scene_root = FXMLLoader.load(getClass().getResource("/fxml/doktor_scene.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        Scene yonetici_scene = new Scene(yonetici_scene_root);
        scene.getStylesheets().add("/styles/yonetici_scene.css");

        Scene sekreter_scene = new Scene(sekreter_scene_root);
        scene.getStylesheets().add("/styles/sekreter_scene.css");

        Scene doktor_scene = new Scene(doktor_scene_root);
        scene.getStylesheets().add("/styles/doktor_scene.css");

        Iterator<Node> it = scene.getRoot().getChildrenUnmodifiable().iterator();
        


        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
