package com.mycompany.hastane_sistemi;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.apache.derby.shared.common.error.DerbySQLIntegrityConstraintViolationException;

/**
 * FXML Controller class
 *
 * @author omerfaruk
 */
public class sekreter_sceneController implements Initializable {

    String PERSISTENCE_UNIT_NAME;
    EntityManagerFactory factory;
    DoktorJpaController kontrolcuDoktor;
    KlinikJpaController kontrolcuKlinik;
    HastaJpaController kontrolcuHasta;
    RandevuJpaController kontrolcuRandevu;

    @FXML
    TextField hasta_tarih_tf;
    @FXML
    TextField hasta_tc_tf;
    @FXML
    TextField hasta_ad_tf;
    @FXML
    TextField hasta_soyad_tf;
    @FXML
    TextField randevu_arama_tf;

    @FXML
    Button randevu_ekle_btn;
    @FXML
    Button randevu_ara_btn;

    @FXML
    TableView<KlinikTableAdapter> klinik_table_view;
    @FXML
    TableView<DoktorTableAdapter> doktor_table_view;
    @FXML
    TableView<DoktorTableAdapter> doktor_table_view2;
    @FXML
    TableView<RandevuTableAdapter> randevular_table_view;

    @FXML
    public void randevu_guncelle_btn_action(ActionEvent event) throws IOException {

    }

    @FXML
    public void randevu_ekle_btn_action(ActionEvent event) throws IOException {

        Hasta name = kontrolcuHasta.findHasta(hasta_tc_tf.getText());
        Randevu randevu = kontrolcuRandevu.findRandevu(hasta_tarih_tf.getText());

        if (doktor_table_view.getSelectionModel().getSelectedItem() == null) {
            Alert uyari = new Alert(Alert.AlertType.ERROR);
            uyari.setTitle("Doktor secmediniz ");
            uyari.show();
            return;
        }
        String doktor_id = doktor_table_view.getSelectionModel().getSelectedItem().getId();
        Doktor doktor = kontrolcuDoktor.findDoktor(doktor_id);

        if (name == null) {
            // hasta yoksa
            name = new Hasta(hasta_tc_tf.getText(), hasta_ad_tf.getText(), hasta_soyad_tf.getText());
            try {
                kontrolcuHasta.create(name);
            } catch (Exception ex) {
                Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        if (randevu == null) {

            Randevu randevu2 = new Randevu(hasta_tarih_tf.getText());
            randevu2.setHastaId(name);
            randevu2.setDoktorId(doktor);

            try {
                kontrolcuRandevu.create(randevu2);
                Alert uyari = new Alert(Alert.AlertType.INFORMATION);
                uyari.setContentText("Randevu eklendi... ");
                uyari.show();
            } catch (Exception ex) {
                Alert uyari = new Alert(Alert.AlertType.ERROR);
                uyari.setContentText("Randevu eklenemedi... Bu tarihte randevu var");
                uyari.show();
                Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            if (randevu.getHastaId().equals(hasta_tc_tf.getText())) {
                Alert uyari = new Alert(Alert.AlertType.ERROR);
                uyari.setTitle("Bu tarihe zaten randevu var ");
                uyari.show();
            } else {
                Randevu randevu2 = new Randevu(hasta_tarih_tf.getText());
                randevu2.setHastaId(name);
                randevu2.setDoktorId(doktor);

                try {
                    kontrolcuRandevu.create(randevu2);
                    Alert uyari = new Alert(Alert.AlertType.INFORMATION);
                    uyari.setContentText("Randevu eklendi... ");
                    uyari.show();
                } catch (Exception ex) {
                    Alert uyari = new Alert(Alert.AlertType.ERROR);
                    uyari.setContentText("Randevu eklenemdi... Bu tarihte randevu var");
                    uyari.show();
                    Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    @FXML
    public void randevu_ara_btn_action(ActionEvent event) throws IOException {
        randevu_table_view_doldur(randevu_arama_tf.getText());
    }

    public void klinik_table_view_doldur() {
        List<Klinik> sonuclar = kontrolcuKlinik.getEntityManager().createNamedQuery("Klinik.findAll").getResultList();

        final ObservableList<KlinikTableAdapter> data = FXCollections.observableArrayList();

        for (Klinik klinik : sonuclar) {
            data.add(new KlinikTableAdapter(klinik.getId(), klinik.getDetay()));
        }
        klinik_table_view.setItems(data);

    }

    public void randevu_table_view_doldur(String hastaId) {
        Query que = kontrolcuRandevu.getEntityManager().createNamedQuery("Randevu.findByHastaID");
        que.setParameter("hastaId", kontrolcuHasta.findHasta(hastaId));
        List<Randevu> sonuclar = que.getResultList();
        final ObservableList<RandevuTableAdapter> data = FXCollections.observableArrayList();

        for (Randevu randevu : sonuclar) {
            data.add(new RandevuTableAdapter(randevu.getTarih(), randevu.getDoktorId().getId(), randevu.getHastaId().getId()));
        }
        randevular_table_view.setItems(data);

    }

    public void doktor_table_view_doldur(String klinikID) {
        Query que = kontrolcuDoktor.getEntityManager().createNamedQuery("Doktor.findByKlinikId");
        que.setParameter("klinikId", kontrolcuKlinik.findKlinik(klinikID));
        List<Doktor> sonuclar = que.getResultList();
        final ObservableList<DoktorTableAdapter> data = FXCollections.observableArrayList();

        for (Doktor doktor : sonuclar) {
            data.add(new DoktorTableAdapter(doktor.getId(), doktor.getAd(), doktor.getSoyad(), ""));
        }
        doktor_table_view.setItems(data);

    }

    public void doktor_table_view2_doldur(String id) {
        Doktor doktor = kontrolcuDoktor.findDoktor(id);
        final ObservableList<DoktorTableAdapter> data = FXCollections.observableArrayList();
        data.add(new DoktorTableAdapter(doktor.getId(), doktor.getAd(), doktor.getSoyad(), ""));
        doktor_table_view2.setItems(data);
    }

    public void init_klinik_table_view() {
        TableColumn idcol = new TableColumn("Klinik ID");
        TableColumn detaycol = new TableColumn("Klinik Detayi");

        idcol.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        detaycol.setCellValueFactory(
                new PropertyValueFactory<>("detay"));

        klinik_table_view.getColumns().addAll(idcol, detaycol);

        klinik_table_view.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                doktor_table_view_doldur(klinik_table_view.getSelectionModel().getSelectedItem().getId());
            }
        });
    }

    public void init_randevu_table_view() {
        TableColumn tarihcol = new TableColumn("Randevu Tarihi");
        TableColumn doktoridcol = new TableColumn("Doktor TC");
        TableColumn hastaidcol = new TableColumn("Hasta TC");

        tarihcol.setCellValueFactory(
                new PropertyValueFactory<>("tarih"));
        doktoridcol.setCellValueFactory(
                new PropertyValueFactory<>("doktor_id"));
        hastaidcol.setCellValueFactory(
                new PropertyValueFactory<>("hasta_id"));

        randevular_table_view.getColumns().addAll(tarihcol, doktoridcol, hastaidcol);

        randevular_table_view.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                doktor_table_view2_doldur(randevular_table_view.getSelectionModel().getSelectedItem().getDoktor_id());
            }
        });
    }

    public void init_doktor_table_view() {
        TableColumn idcol2 = new TableColumn("Doktor ID");
        TableColumn adcol = new TableColumn("Doktor Adi");
        TableColumn soyadcol = new TableColumn("Doktor Soyad");

        idcol2.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        adcol.setCellValueFactory(
                new PropertyValueFactory<>("ad"));

        soyadcol.setCellValueFactory(
                new PropertyValueFactory<>("soyad"));

        doktor_table_view.getColumns().addAll(idcol2, adcol, soyadcol);
    }

    public void init_doktor_table_view2() {
        TableColumn idcol2 = new TableColumn("Doktor ID");
        TableColumn adcol = new TableColumn("Doktor Adi");
        TableColumn soyadcol = new TableColumn("Doktor Soyad");

        idcol2.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        adcol.setCellValueFactory(
                new PropertyValueFactory<>("ad"));

        soyadcol.setCellValueFactory(
                new PropertyValueFactory<>("soyad"));

        doktor_table_view2.getColumns().addAll(idcol2, adcol, soyadcol);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        System.out.println("sekreter modulu yukleniyor..");

        PERSISTENCE_UNIT_NAME = "com.mycompany_hastane_sistemi_jar_1.0-SNAPSHOTPU";
        factory = factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        kontrolcuKlinik = new KlinikJpaController(factory);
        kontrolcuDoktor = new DoktorJpaController(factory);
        kontrolcuHasta = new HastaJpaController(factory);
        kontrolcuRandevu = new RandevuJpaController(factory);

        init_klinik_table_view();
        init_doktor_table_view();
        init_doktor_table_view2();
        init_randevu_table_view();

        klinik_table_view_doldur();
    }

}
