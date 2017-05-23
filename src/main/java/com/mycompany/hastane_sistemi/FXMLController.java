package com.mycompany.hastane_sistemi;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class FXMLController implements Initializable {

    String PERSISTENCE_UNIT_NAME;
    EntityManagerFactory factory;

    @FXML
    TextField id_tf;

    @FXML
    TextField pw_tf;

    @FXML
    public void yonetici_login_btn_action(ActionEvent event) throws IOException {
        try {
            YoneticiJpaController kontrolcu = new YoneticiJpaController(factory);
            Yonetici name = kontrolcu.findYonetici(id_tf.getText());

            if (name != null && name.getYoneticiModuluPw().equals(pw_tf.getText())) {
                Parent yonetici_fxml = FXMLLoader.load(getClass().getResource("/fxml/yonetici_scene.fxml"));
                Scene yonetici_scene = new Scene(yonetici_fxml);
                yonetici_scene.getStylesheets().add("/styles/yonetici_scene.css");
                Stage yonetici_stage = new Stage();
                yonetici_stage.setScene(yonetici_scene);
                yonetici_stage.show();
            } else {
                Alert uyari = new Alert(Alert.AlertType.ERROR);
                uyari.setTitle("Giris basarisiz");
                uyari.show();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    @FXML
    public void sekreter_login_btn_action(ActionEvent event) throws IOException {
        try {
            SekreterJpaController kontrolcu = new SekreterJpaController(factory);
            Sekreter name = kontrolcu.findSekreter(id_tf.getText());

            if (name != null && name.getSekreterModuluPw().equals(pw_tf.getText())) {
                Parent sekreter_fxml = FXMLLoader.load(getClass().getResource("/fxml/sekreter_scene.fxml"));
                Scene sekreter_scene = new Scene(sekreter_fxml);
                sekreter_scene.getStylesheets().add("/styles/sekreter_scene.css");

                Stage sekreter_stage = new Stage();
                sekreter_stage.setScene(sekreter_scene);
                sekreter_stage.show();
            } else {
                Alert uyari = new Alert(Alert.AlertType.ERROR);
                uyari.setTitle("Giris basarisiz");
                uyari.show();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    @FXML
    public void doktor_login_btn_action(ActionEvent event) throws IOException {
        try {
            DoktorJpaController kontrolcu = new DoktorJpaController(factory);
            Doktor name = kontrolcu.findDoktor(id_tf.getText());

            if (name != null && name.getDoktorModuluPw().equals(pw_tf.getText())) {
                Parent doktor_fxml = FXMLLoader.load(getClass().getResource("/fxml/doktor_scene.fxml"));
                Scene doktor_scene = new Scene(doktor_fxml);
                doktor_scene.getStylesheets().add("/styles/doktor_scene.css");

                Stage doktor_stage = new Stage();
                doktor_stage.setScene(doktor_scene);
                doktor_stage.show();
            } else {
                Alert uyari = new Alert(Alert.AlertType.ERROR);
                uyari.setTitle("Giris basarisiz");
                uyari.show();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("yukleniyor..");
        PERSISTENCE_UNIT_NAME = "com.mycompany_hastane_sistemi_jar_1.0-SNAPSHOTPU";
        factory = factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }
}
