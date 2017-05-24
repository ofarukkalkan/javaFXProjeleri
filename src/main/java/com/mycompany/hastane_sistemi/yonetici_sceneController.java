package com.mycompany.hastane_sistemi;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mycompany.hastane_sistemi.exceptions.IllegalOrphanException;
import com.mycompany.hastane_sistemi.exceptions.NonexistentEntityException;
import java.net.URL;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * FXML Controller class
 *
 * @author omerfaruk
 */
public class yonetici_sceneController implements Initializable {

    // jpa elemanlari
    String PERSISTENCE_UNIT_NAME;
    EntityManagerFactory factory;
    DoktorJpaController kontrolcuDoktor;
    YoneticiJpaController kontrolcuYonetici;
    SekreterJpaController kontrolcuSekreter;
    KlinikJpaController kontrolcuKlinik;
    HastaJpaController kontrolcuHasta;
    // tab pane
    @FXML
    TabPane tab_pane;
    // text fields
    @FXML
    TextField hasta_tc_tf;
    @FXML
    TextField hasta_ad_tf;
    @FXML
    TextField hasta_soyad_tf;
    @FXML
    TextField doktor_tc_tf;
    @FXML
    TextField doktor_ad_tf;
    @FXML
    TextField doktor_soyad_tf;
    @FXML
    TextField doktor_pw_tf;
    @FXML
    TextField sekreter_tc_tf;
    @FXML
    TextField sekreter_ad_tf;
    @FXML
    TextField sekreter_soyad_tf;
    @FXML
    TextField sekreter_pw_tf;
    @FXML
    TextField yonetici_tc_tf;
    @FXML
    TextField yonetici_ad_tf;
    @FXML
    TextField yonetici_soyad_tf;
    @FXML
    TextField yonetici_pw_tf;
    @FXML
    TextField klinik_id_tf;
    @FXML
    TextField klinik_detay_tf;

    // views
    @FXML
    TableView<HastaTableAdapter> hasta_bilgi_view;
    @FXML
    TableView<SekreterTableAdapter> sekreter_bilgi_view;
    @FXML
    TableView<KlinikTableAdapter> sekreter_klinik_bilgi_view;
    @FXML
    TableView<DoktorTableAdapter> doktor_bilgi_view;
    @FXML
    TableView<KlinikTableAdapter> doktor_klinik_bilgi_view;
    @FXML
    TableView<YoneticiTableAdapter> yonetici_bilgi_view;
    @FXML
    TableView<KlinikTableAdapter> yonetici_klinik_bilgi_view;
    @FXML
    TableView<KlinikTableAdapter> klinik_bilgi_view;

    @FXML
    public void hasta_sil_btn_action(ActionEvent event) {
        HastaTableAdapter rta = hasta_bilgi_view.getSelectionModel().getSelectedItem();
        if (rta == null) {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Once silinecek olani seciniz !", ButtonType.OK);
            hata.show();
            return;
        }
        try {
            kontrolcuHasta.destroy(rta.getId());
            fill_all_table_views();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(yonetici_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void sekreter_sil_btn_action(ActionEvent event) {
        SekreterTableAdapter rta = sekreter_bilgi_view.getSelectionModel().getSelectedItem();
        if (rta == null) {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Once silinecek olani seciniz !", ButtonType.OK);
            hata.show();
            return;
        }
        try {
            kontrolcuSekreter.destroy(rta.getId());
            fill_all_table_views();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void doktor_sil_btn_action(ActionEvent event) {
        DoktorTableAdapter rta = doktor_bilgi_view.getSelectionModel().getSelectedItem();
        if (rta == null) {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Once silinecek olani seciniz !", ButtonType.OK);
            hata.show();
            return;
        }
        try {
            kontrolcuDoktor.destroy(rta.getId());
            fill_all_table_views();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(yonetici_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void yonetici_sil_btn_action(ActionEvent event) {
        YoneticiTableAdapter rta = yonetici_bilgi_view.getSelectionModel().getSelectedItem();
        if (rta == null) {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Once silinecek olani seciniz !", ButtonType.OK);
            hata.show();
            return;
        }
        try {
            kontrolcuYonetici.destroy(rta.getId());
            fill_all_table_views();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void klinik_sil_btn_action(ActionEvent event) {
        KlinikTableAdapter rta = klinik_bilgi_view.getSelectionModel().getSelectedItem();
        if (rta == null) {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Once silinecek olani seciniz !", ButtonType.OK);
            hata.show();
            return;
        }
        try {
            kontrolcuKlinik.destroy(rta.getId());
            fill_all_table_views();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(yonetici_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void hasta_ekle_btn_action(ActionEvent event) {
        Hasta name = kontrolcuHasta.findHasta(hasta_tc_tf.getText());

        if (name == null) {
            name = new Hasta(hasta_tc_tf.getText(), hasta_ad_tf.getText(), hasta_soyad_tf.getText());
            try {
                kontrolcuHasta.create(name);
                Alert mesaj = new Alert(Alert.AlertType.CONFIRMATION, "Hasta eklendi!", ButtonType.OK);
                mesaj.show();
            } catch (Exception ex) {
                Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Bu kayit zaten var!", ButtonType.OK);
            hata.show();
            return;
        }

        fill_all_table_views();
    }

    @FXML
    public void sekreter_ekle_btn_action(ActionEvent event) {
        Sekreter name = kontrolcuSekreter.findSekreter(sekreter_tc_tf.getText());

        KlinikTableAdapter dta = sekreter_klinik_bilgi_view.getSelectionModel().getSelectedItem();
        if (dta == null) {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Once klinik seciniz !", ButtonType.OK);
            hata.show();
            return;
        }

        if (name == null) {
            name = new Sekreter(sekreter_tc_tf.getText(), sekreter_ad_tf.getText(), sekreter_soyad_tf.getText(), sekreter_pw_tf.getText());
            name.setKlinikId(kontrolcuKlinik.findKlinik(dta.getId()));
            try {
                kontrolcuSekreter.create(name);
                Alert mesaj = new Alert(Alert.AlertType.CONFIRMATION, "Sekreter eklendi!", ButtonType.OK);
                mesaj.show();
            } catch (Exception ex) {
                Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Bu kayit zaten var!", ButtonType.OK);
            hata.show();
            return;
        }

        fill_all_table_views();
    }

    @FXML
    public void doktor_ekle_btn_action(ActionEvent event) {
        Doktor name = kontrolcuDoktor.findDoktor(doktor_tc_tf.getText());

        KlinikTableAdapter dta = doktor_klinik_bilgi_view.getSelectionModel().getSelectedItem();
        if (dta == null) {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Once klinik seciniz !", ButtonType.OK);
            hata.show();
            return;
        }

        if (name == null) {
            name = new Doktor(doktor_tc_tf.getText(), doktor_ad_tf.getText(), doktor_soyad_tf.getText(), doktor_pw_tf.getText());
            name.setKlinikId(kontrolcuKlinik.findKlinik(dta.getId()));
            try {
                kontrolcuDoktor.create(name);
                Alert mesaj = new Alert(Alert.AlertType.CONFIRMATION, "Doktor eklendi!", ButtonType.OK);
                mesaj.show();
            } catch (Exception ex) {
                Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Bu kayit zaten var!", ButtonType.OK);
            hata.show();
            return;
        }

        fill_all_table_views();
    }

    @FXML
    public void yonetici_ekle_btn_action(ActionEvent event) {
        Yonetici name = kontrolcuYonetici.findYonetici(yonetici_tc_tf.getText());

        KlinikTableAdapter dta = yonetici_klinik_bilgi_view.getSelectionModel().getSelectedItem();
        if (dta == null) {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Once klinik seciniz !", ButtonType.OK);
            hata.show();
            return;
        }

        if (name == null) {
            name = new Yonetici(yonetici_tc_tf.getText(), yonetici_ad_tf.getText(), yonetici_soyad_tf.getText(), yonetici_pw_tf.getText());
            name.setKlinikId(kontrolcuKlinik.findKlinik(dta.getId()));
            try {
                kontrolcuYonetici.create(name);
                Alert mesaj = new Alert(Alert.AlertType.CONFIRMATION, "Yonetici eklendi!", ButtonType.OK);
                mesaj.show();
            } catch (Exception ex) {
                Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Bu kayit zaten var!", ButtonType.OK);
            hata.show();
            return;
        }

        fill_all_table_views();
    }

    @FXML
    public void klinik_ekle_btn_action(ActionEvent event) {
        Klinik name = kontrolcuKlinik.findKlinik(klinik_id_tf.getText());

        if (name == null) {
            name = new Klinik(klinik_id_tf.getText(), klinik_detay_tf.getText());
            try {
                kontrolcuKlinik.create(name);
                Alert mesaj = new Alert(Alert.AlertType.CONFIRMATION, "Klinik eklendi!", ButtonType.OK);
                mesaj.show();
            } catch (Exception ex) {
                Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Bu kayit zaten var!", ButtonType.OK);
            hata.show();
            return;
        }

        fill_all_table_views();
    }

    @FXML
    public void hasta_guncelle_btn_action(ActionEvent event) {
        HastaTableAdapter rta = hasta_bilgi_view.getSelectionModel().getSelectedItem();
        if (rta == null) {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Once guncellenecek randevuyu seciniz !", ButtonType.OK);
            hata.show();
            return;
        }

        Hasta hasta = kontrolcuHasta.findHasta(rta.getId());

        if (!hasta_ad_tf.getText().equals(hasta.getAd())) {
            hasta.setAd(hasta_ad_tf.getText());
        }
        if (!hasta_soyad_tf.getText().equals(hasta.getSoyad())) {
            hasta.setSoyad(hasta_soyad_tf.getText());
        }

        try {
            kontrolcuHasta.edit(hasta);

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        fill_all_table_views();
    }

    @FXML
    public void sekreter_guncelle_btn_action(ActionEvent event) {
        SekreterTableAdapter rta = sekreter_bilgi_view.getSelectionModel().getSelectedItem();
        if (rta == null) {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Once guncellenecek randevuyu seciniz !", ButtonType.OK);
            hata.show();
            return;
        }

        Sekreter sekreter = kontrolcuSekreter.findSekreter(rta.getId());
        Klinik klinik = kontrolcuKlinik.findKlinik(sekreter.getKlinikId().getId());

        if (!sekreter_ad_tf.getText().equals(sekreter.getAd())) {
            sekreter.setAd(sekreter_ad_tf.getText());
        }
        if (!sekreter_soyad_tf.getText().equals(sekreter.getSoyad())) {
            sekreter.setSoyad(sekreter_soyad_tf.getText());
        }
        if (!sekreter_pw_tf.getText().equals(sekreter.getSekreterModuluPw())) {
            sekreter.setSekreterModuluPw(sekreter_pw_tf.getText());
        }

        KlinikTableAdapter dta = sekreter_klinik_bilgi_view.getSelectionModel().getSelectedItem();
        if (dta != null && !dta.equals(klinik)) {
            Klinik yeniKlinik = kontrolcuKlinik.findKlinik(dta.getId());
            sekreter.setKlinikId(yeniKlinik);
        }
        try {
            kontrolcuSekreter.edit(sekreter);

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        fill_all_table_views();
    }

    @FXML
    public void doktor_guncelle_btn_action(ActionEvent event) {
        DoktorTableAdapter rta = doktor_bilgi_view.getSelectionModel().getSelectedItem();
        if (rta == null) {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Once guncellenecek randevuyu seciniz !", ButtonType.OK);
            hata.show();
            return;
        }

        Doktor doktor = kontrolcuDoktor.findDoktor(rta.getId());
        Klinik klinik = kontrolcuKlinik.findKlinik(doktor.getKlinikId().getId());

        if (!doktor_ad_tf.getText().equals(doktor.getAd())) {
            doktor.setAd(doktor_ad_tf.getText());
        }
        if (!doktor_soyad_tf.getText().equals(doktor.getSoyad())) {
            doktor.setSoyad(doktor_soyad_tf.getText());
        }
        if (!doktor_pw_tf.getText().equals(doktor.getDoktorModuluPw())) {
            doktor.setDoktorModuluPw(doktor_pw_tf.getText());
        }

        KlinikTableAdapter dta = doktor_klinik_bilgi_view.getSelectionModel().getSelectedItem();
        if (dta != null && !dta.equals(klinik)) {
            Klinik yeniKlinik = kontrolcuKlinik.findKlinik(dta.getId());
            doktor.setKlinikId(yeniKlinik);
        }
        try {
            kontrolcuDoktor.edit(doktor);

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        fill_all_table_views();
    }

    @FXML
    public void yonetici_guncelle_btn_action(ActionEvent event) {
        YoneticiTableAdapter rta = yonetici_bilgi_view.getSelectionModel().getSelectedItem();
        if (rta == null) {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Once guncellenecek randevuyu seciniz !", ButtonType.OK);
            hata.show();
            return;
        }

        Yonetici yonetici = kontrolcuYonetici.findYonetici(rta.getId());
        Klinik klinik = kontrolcuKlinik.findKlinik(yonetici.getKlinikId().getId());

        if (!yonetici_ad_tf.getText().equals(yonetici.getAd())) {
            yonetici.setAd(yonetici_ad_tf.getText());
        }
        if (!yonetici_soyad_tf.getText().equals(yonetici.getSoyad())) {
            yonetici.setSoyad(yonetici_soyad_tf.getText());
        }
        if (!yonetici_pw_tf.getText().equals(yonetici.getYoneticiModuluPw())) {
            yonetici.setYoneticiModuluPw(yonetici_pw_tf.getText());
        }

        KlinikTableAdapter dta = yonetici_klinik_bilgi_view.getSelectionModel().getSelectedItem();
        if (dta != null && !dta.equals(klinik)) {
            Klinik yeniKlinik = kontrolcuKlinik.findKlinik(dta.getId());
            yonetici.setKlinikId(yeniKlinik);
        }
        try {
            kontrolcuYonetici.edit(yonetici);

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        fill_all_table_views();
    }

    @FXML
    public void klinik_guncelle_btn_action(ActionEvent event) {
        KlinikTableAdapter rta = klinik_bilgi_view.getSelectionModel().getSelectedItem();
        if (rta == null) {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Once guncellenecek randevuyu seciniz !", ButtonType.OK);
            hata.show();
            return;
        }

        Klinik klinik = kontrolcuKlinik.findKlinik(rta.getId());

        if (!klinik_id_tf.getText().equals(klinik.getId())) {
            klinik.setId(klinik_id_tf.getText());
        }
        if (!klinik_detay_tf.getText().equals(klinik.getDetay())) {
            klinik.setDetay(klinik_detay_tf.getText());
        }

        try {
            kontrolcuKlinik.edit(klinik);

        } catch (NonexistentEntityException ex) {
            Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        fill_all_table_views();
    }

    public void fill_all_table_views() {
        clear_all_table_views();
        hasta_table_view_doldur(hasta_bilgi_view);
        klinik_table_view_doldur(sekreter_klinik_bilgi_view);
        klinik_table_view_doldur(doktor_klinik_bilgi_view);
        klinik_table_view_doldur(yonetici_klinik_bilgi_view);
        klinik_table_view_doldur(klinik_bilgi_view);
    }

    public void clear_all_table_views() {
        hasta_bilgi_view.setItems(null);
        sekreter_bilgi_view.setItems(null);
        sekreter_klinik_bilgi_view.setItems(null);
        doktor_bilgi_view.setItems(null);
        doktor_klinik_bilgi_view.setItems(null);
        yonetici_bilgi_view.setItems(null);
        yonetici_klinik_bilgi_view.setItems(null);
        klinik_bilgi_view.setItems(null);
    }

    public void init_klinik_table_view(TableView<KlinikTableAdapter> tableView) {
        TableColumn idcol = new TableColumn("Klinik ID");
        TableColumn detaycol = new TableColumn("Klinik Detayi");
        idcol.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        detaycol.setCellValueFactory(
                new PropertyValueFactory<>("detay"));
        tableView.getColumns().addAll(idcol, detaycol);
    }

    public void init_doktor_table_view(TableView<DoktorTableAdapter> tableView) {
        TableColumn idcol2 = new TableColumn("Doktor TC");
        TableColumn adcol = new TableColumn("Doktor Adi");
        TableColumn soyadcol = new TableColumn("Doktor Soyad");
        TableColumn pwcol = new TableColumn("Doktor Modulu Parola");
        idcol2.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        adcol.setCellValueFactory(
                new PropertyValueFactory<>("ad"));
        soyadcol.setCellValueFactory(
                new PropertyValueFactory<>("soyad"));
        pwcol.setCellValueFactory(
                new PropertyValueFactory<>("doktorModuluPw"));
        tableView.getColumns().addAll(idcol2, adcol, soyadcol, pwcol);

    }

    public void init_hasta_table_view(TableView<HastaTableAdapter> tableView) {
        TableColumn idcol2 = new TableColumn("Hasta TC");
        TableColumn adcol = new TableColumn("Hasta Adi");
        TableColumn soyadcol = new TableColumn("Hasta Soyad");
        idcol2.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        adcol.setCellValueFactory(
                new PropertyValueFactory<>("ad"));
        soyadcol.setCellValueFactory(
                new PropertyValueFactory<>("soyad"));
        tableView.getColumns().addAll(idcol2, adcol, soyadcol);
    }

    public void init_sekreter_table_view(TableView<SekreterTableAdapter> tableView) {
        TableColumn idcol2 = new TableColumn("Sekreter TC");
        TableColumn adcol = new TableColumn("Sekreter Adi");
        TableColumn soyadcol = new TableColumn("Sekreter Soyad");
        TableColumn pwcol = new TableColumn("Sekreter Modulu Parola");
        idcol2.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        adcol.setCellValueFactory(
                new PropertyValueFactory<>("ad"));
        soyadcol.setCellValueFactory(
                new PropertyValueFactory<>("soyad"));
        pwcol.setCellValueFactory(
                new PropertyValueFactory<>("sekreterModuluPw"));
        tableView.getColumns().addAll(idcol2, adcol, soyadcol, pwcol);
    }

    public void init_yonetici_table_view(TableView<YoneticiTableAdapter> tableView) {
        TableColumn idcol2 = new TableColumn("Yonetici TC");
        TableColumn adcol = new TableColumn("Yonetici Adi");
        TableColumn soyadcol = new TableColumn("Yonetici Soyad");
        TableColumn pwcol = new TableColumn("Yonetici Modulu Parola");
        idcol2.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        adcol.setCellValueFactory(
                new PropertyValueFactory<>("ad"));
        soyadcol.setCellValueFactory(
                new PropertyValueFactory<>("soyad"));
        pwcol.setCellValueFactory(
                new PropertyValueFactory<>("yoneticiModuluPw"));
        tableView.getColumns().addAll(idcol2, adcol, soyadcol, pwcol);
    }

    public void klinik_table_view_doldur(TableView<KlinikTableAdapter> tableView) {
        List<Klinik> sonuclar = kontrolcuKlinik.getEntityManager().createNamedQuery("Klinik.findAll").getResultList();
        final ObservableList<KlinikTableAdapter> data = FXCollections.observableArrayList();
        for (Klinik klinik : sonuclar) {
            data.add(new KlinikTableAdapter(klinik.getId(), klinik.getDetay()));
        }
        tableView.setItems(data);
    }

    public void hasta_table_view_doldur(TableView<HastaTableAdapter> tableView) {
        List<Hasta> sonuclar = kontrolcuKlinik.getEntityManager().createNamedQuery("Hasta.findAll").getResultList();
        final ObservableList<HastaTableAdapter> data = FXCollections.observableArrayList();
        for (Hasta hasta : sonuclar) {
            data.add(new HastaTableAdapter(hasta.getId(), hasta.getAd(), hasta.getSoyad()));
        }
        tableView.setItems(data);
    }

    public void doktor_table_view_doldur(TableView<DoktorTableAdapter> tableView, String klinikID) {
        Query que = kontrolcuDoktor.getEntityManager().createNamedQuery("Doktor.findByKlinikId");
        que.setParameter("klinikId", kontrolcuKlinik.findKlinik(klinikID));
        List<Doktor> sonuclar = que.getResultList();
        final ObservableList<DoktorTableAdapter> data = FXCollections.observableArrayList();

        for (Doktor doktor : sonuclar) {
            data.add(new DoktorTableAdapter(doktor.getId(), doktor.getAd(), doktor.getSoyad(), doktor.getDoktorModuluPw()));
        }
        tableView.setItems(data);
    }

    public void yonetici_table_view_doldur(TableView<YoneticiTableAdapter> tableView, String klinikID) {
        Query que = kontrolcuDoktor.getEntityManager().createNamedQuery("Yonetici.findByKlinikId");
        que.setParameter("klinikId", kontrolcuKlinik.findKlinik(klinikID));
        List<Yonetici> sonuclar = que.getResultList();
        final ObservableList<YoneticiTableAdapter> data = FXCollections.observableArrayList();

        for (Yonetici yonetici : sonuclar) {
            data.add(new YoneticiTableAdapter(yonetici.getId(), yonetici.getAd(), yonetici.getSoyad(), yonetici.getYoneticiModuluPw()));
        }
        tableView.setItems(data);
    }

    public void sekreter_table_view_doldur(TableView<SekreterTableAdapter> tableView, String klinikID) {
        Query que = kontrolcuDoktor.getEntityManager().createNamedQuery("Sekreter.findByKlinikId");
        que.setParameter("klinikId", kontrolcuKlinik.findKlinik(klinikID));
        List<Sekreter> sonuclar = que.getResultList();
        final ObservableList<SekreterTableAdapter> data = FXCollections.observableArrayList();

        for (Sekreter sekreter : sonuclar) {
            data.add(new SekreterTableAdapter(sekreter.getId(), sekreter.getAd(), sekreter.getSoyad(), sekreter.getSekreterModuluPw()));
        }
        tableView.setItems(data);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("yonetici modulu yukleniyor..");

        PERSISTENCE_UNIT_NAME = "com.mycompany_hastane_sistemi_jar_1.0-SNAPSHOTPU";
        factory = factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        kontrolcuKlinik = new KlinikJpaController(factory);
        kontrolcuDoktor = new DoktorJpaController(factory);
        kontrolcuHasta = new HastaJpaController(factory);
        kontrolcuYonetici = new YoneticiJpaController(factory);
        kontrolcuSekreter = new SekreterJpaController(factory);

        init_doktor_table_view(doktor_bilgi_view);
        init_sekreter_table_view(sekreter_bilgi_view);
        init_yonetici_table_view(yonetici_bilgi_view);
        init_hasta_table_view(hasta_bilgi_view);
        init_klinik_table_view(sekreter_klinik_bilgi_view);
        init_klinik_table_view(doktor_klinik_bilgi_view);
        init_klinik_table_view(yonetici_klinik_bilgi_view);
        init_klinik_table_view(klinik_bilgi_view);

        tab_pane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            fill_all_table_views();
        });

        doktor_klinik_bilgi_view.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                doktor_table_view_doldur(doktor_bilgi_view, doktor_klinik_bilgi_view.getSelectionModel().getSelectedItem().getId());
            }
        });
        sekreter_klinik_bilgi_view.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                sekreter_table_view_doldur(sekreter_bilgi_view, sekreter_klinik_bilgi_view.getSelectionModel().getSelectedItem().getId());
            }
        });
        yonetici_klinik_bilgi_view.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                yonetici_table_view_doldur(yonetici_bilgi_view, yonetici_klinik_bilgi_view.getSelectionModel().getSelectedItem().getId());
            }
        });

        hasta_bilgi_view.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Hasta hasta = kontrolcuHasta.findHasta(hasta_bilgi_view.getSelectionModel().getSelectedItem().getId());
                hasta_ad_tf.setText(hasta.getAd());
                hasta_soyad_tf.setText(hasta.getSoyad());
                hasta_tc_tf.setText(hasta.getId());

            }
        });

        sekreter_bilgi_view.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Sekreter sekreter = kontrolcuSekreter.findSekreter(sekreter_bilgi_view.getSelectionModel().getSelectedItem().getId());
                sekreter_ad_tf.setText(sekreter.getAd());
                sekreter_soyad_tf.setText(sekreter.getSoyad());
                sekreter_tc_tf.setText(sekreter.getId());
                sekreter_pw_tf.setText(sekreter.getSekreterModuluPw());
            }
        });

        doktor_bilgi_view.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Doktor doktor = kontrolcuDoktor.findDoktor(doktor_bilgi_view.getSelectionModel().getSelectedItem().getId());
                doktor_ad_tf.setText(doktor.getAd());
                doktor_soyad_tf.setText(doktor.getSoyad());
                doktor_tc_tf.setText(doktor.getId());
                doktor_pw_tf.setText(doktor.getDoktorModuluPw());
            }
        });

        yonetici_bilgi_view.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Yonetici yonetici = kontrolcuYonetici.findYonetici(yonetici_bilgi_view.getSelectionModel().getSelectedItem().getId());
                yonetici_ad_tf.setText(yonetici.getAd());
                yonetici_soyad_tf.setText(yonetici.getSoyad());
                yonetici_tc_tf.setText(yonetici.getId());
                yonetici_pw_tf.setText(yonetici.getYoneticiModuluPw());
            }
        });

        klinik_bilgi_view.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Klinik klinik = kontrolcuKlinik.findKlinik(klinik_bilgi_view.getSelectionModel().getSelectedItem().getId());
                klinik_id_tf.setText(klinik.getId());
                klinik_detay_tf.setText(klinik.getDetay());

            }
        });

    }

}
