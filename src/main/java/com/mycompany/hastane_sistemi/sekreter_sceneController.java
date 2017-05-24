package com.mycompany.hastane_sistemi;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mycompany.hastane_sistemi.exceptions.IllegalOrphanException;
import com.mycompany.hastane_sistemi.exceptions.NonexistentEntityException;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
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

    // jpa elemanlari
    String PERSISTENCE_UNIT_NAME;
    EntityManagerFactory factory;
    DoktorJpaController kontrolcuDoktor;
    KlinikJpaController kontrolcuKlinik;
    HastaJpaController kontrolcuHasta;
    RandevuJpaController kontrolcuRandevu;
    // tab pane
    @FXML
    TabPane tab_pane;
    // text field lar
    @FXML
    TextField hasta_tarih_tf;
    @FXML
    TextField hasta_tc_tf;
    @FXML
    TextField hasta_ad_tf;
    @FXML
    TextField hasta_soyad_tf;
    @FXML
    TextField yeni_hasta_tarih_tf;
    @FXML
    TextField yeni_hasta_tc_tf;
    @FXML
    TextField yeni_hasta_ad_tf;
    @FXML
    TextField yeni_hasta_soyad_tf;
    @FXML
    TextField randevu_arama_tf;
    // view lar
    @FXML
    TableView<KlinikTableAdapter> klinik_table_view;
    @FXML
    TableView<KlinikTableAdapter> klinik_table_view2;
    @FXML
    TableView<DoktorTableAdapter> doktor_table_view;
    @FXML
    TableView<DoktorTableAdapter> doktor_table_view2;
    @FXML
    TableView<DoktorTableAdapter> doktor_table_view3;
    @FXML
    TableView<DoktorTableAdapter> doktor_table_view4;
    @FXML
    TableView<RandevuTableAdapter> randevular_table_view;
    @FXML
    TableView<RandevuTableAdapter> randevular_table_view2;
    @FXML
    TableView<RandevuTableAdapter> randevular_table_view3;
    @FXML
    TableView<HastaTableAdapter> hasta_table_view;

    @FXML
    public void randevu_sil_btn_action(ActionEvent event) {

        RandevuTableAdapter rta = randevular_table_view2.getSelectionModel().getSelectedItem();
        if (rta == null) {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Once silinecek randevuyu seciniz !", ButtonType.OK);
            hata.show();
            return;
        }
        try {
            kontrolcuRandevu.destroy(Integer.parseInt(rta.getId()));
            fill_all_table_views();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void randevu_guncelle_btn_action(ActionEvent event) throws IOException {
        RandevuTableAdapter rta = randevular_table_view2.getSelectionModel().getSelectedItem();
        if (rta == null) {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Once guncellenecek randevuyu seciniz !", ButtonType.OK);
            hata.show();
            return;
        }
        Randevu randevu = kontrolcuRandevu.findRandevu(Integer.parseInt(rta.getId()));
        Hasta hasta = kontrolcuHasta.findHasta(rta.getHasta_id());
        Doktor doktor = kontrolcuDoktor.findDoktor(rta.getDoktor_id());

        if (!yeni_hasta_ad_tf.getText().equals(hasta.getAd())) {
            hasta.setAd(yeni_hasta_ad_tf.getText());
        }
        if (!yeni_hasta_soyad_tf.getText().equals(hasta.getSoyad())) {
            hasta.setSoyad(yeni_hasta_soyad_tf.getText());
        }
        if (!yeni_hasta_tarih_tf.getText().equals(randevu.getTarih())) {
            randevu.setTarih(yeni_hasta_tarih_tf.getText());
        }
        DoktorTableAdapter dta = doktor_table_view3.getSelectionModel().getSelectedItem();
        if (dta != null && !dta.equals(doktor)) {
            Doktor yeniDoktor = kontrolcuDoktor.findDoktor(dta.getId());
            randevu.setDoktorId(yeniDoktor);
        }
        try {
            kontrolcuHasta.edit(hasta);
            kontrolcuRandevu.edit(randevu);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        fill_all_table_views();
    }

    @FXML
    public void randevu_ekle_btn_action(ActionEvent event) throws IOException {

        Hasta name = kontrolcuHasta.findHasta(hasta_tc_tf.getText());

        Query que = kontrolcuRandevu.getEntityManager().createNamedQuery("Randevu.findByTarih");
        que.setParameter("tarih", hasta_tarih_tf.getText());
        List<Randevu> sonuclar = que.getResultList();
        DoktorTableAdapter dta = doktor_table_view.getSelectionModel().getSelectedItem();
        if (dta == null) {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Once doktor seciniz !", ButtonType.OK);
            hata.show();
            return;
        }

        Doktor doktor = kontrolcuDoktor.findDoktor(dta.getId());

        for (Randevu randevu : sonuclar) {
            if (randevu.getDoktorId().equals(doktor)) {
                Alert hata = new Alert(Alert.AlertType.ERROR, "Bu tarihte bu doktora randevu var !", ButtonType.OK);
                hata.show();
                return;
            }
        }
        Boolean test = false;
        if (name == null) {
            name = new Hasta(hasta_tc_tf.getText(), hasta_ad_tf.getText(), hasta_soyad_tf.getText());
            try {
                kontrolcuHasta.create(name);
                test = true;
            } catch (Exception ex) {
                Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            test = true;
        }

        if (test) {
            Randevu yeniRandevu = new Randevu();
            yeniRandevu.setDoktorId(doktor);
            yeniRandevu.setHastaId(name);
            yeniRandevu.setTarih(hasta_tarih_tf.getText());

            try {
                kontrolcuRandevu.create(yeniRandevu);
                Alert mesaj = new Alert(Alert.AlertType.CONFIRMATION, "Randevu eklendi!", ButtonType.OK);
                mesaj.show();
            } catch (Exception ex) {
                Logger.getLogger(sekreter_sceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert hata = new Alert(Alert.AlertType.ERROR, "Hasta eklenemedigi icin randevu da eklenemedi !", ButtonType.OK);
            hata.show();
        }
        fill_all_table_views();
    }

    @FXML
    public void randevu_ara_btn_action(ActionEvent event) throws IOException {
        randevu_table_view_doldur(randevu_arama_tf.getText());
    }

    public void klinik_table_view_doldur(TableView<KlinikTableAdapter> tableView) {
        List<Klinik> sonuclar = kontrolcuKlinik.getEntityManager().createNamedQuery("Klinik.findAll").getResultList();
        final ObservableList<KlinikTableAdapter> data = FXCollections.observableArrayList();
        for (Klinik klinik : sonuclar) {
            data.add(new KlinikTableAdapter(klinik.getId(), klinik.getDetay()));
        }
        tableView.setItems(data);
    }

    public void randevu_table_view_doldur(String hastaId) {
        Query que = kontrolcuRandevu.getEntityManager().createNamedQuery("Randevu.findByHastaId");
        que.setParameter("hastaId", kontrolcuHasta.findHasta(hastaId));
        List<Randevu> sonuclar = que.getResultList();
        final ObservableList<RandevuTableAdapter> data = FXCollections.observableArrayList();

        for (Randevu randevu : sonuclar) {
            data.add(new RandevuTableAdapter(randevu.getId().toString(), randevu.getTarih(), randevu.getDoktorId().getId(), randevu.getHastaId().getId()));
        }
        randevular_table_view.setItems(data);

    }

    public void randevu_table_view2_doldur(TableView<RandevuTableAdapter> tableView) {
        List<Randevu> sonuclar = kontrolcuRandevu.getEntityManager().createNamedQuery("Randevu.findAll").getResultList();
        final ObservableList<RandevuTableAdapter> data = FXCollections.observableArrayList();
        for (Randevu randevu : sonuclar) {
            data.add(new RandevuTableAdapter(randevu.getId().toString(), randevu.getTarih(), randevu.getDoktorId().getId(), randevu.getHastaId().getId()));
        }
        tableView.setItems(data);
    }

    public void doktor_table_view_doldur(TableView<DoktorTableAdapter> tableView, String klinikID) {
        Query que = kontrolcuDoktor.getEntityManager().createNamedQuery("Doktor.findByKlinikId");
        que.setParameter("klinikId", kontrolcuKlinik.findKlinik(klinikID));
        List<Doktor> sonuclar = que.getResultList();
        final ObservableList<DoktorTableAdapter> data = FXCollections.observableArrayList();

        for (Doktor doktor : sonuclar) {
            data.add(new DoktorTableAdapter(doktor.getId(), doktor.getAd(), doktor.getSoyad(), ""));
        }
        tableView.setItems(data);
    }

    public void doktor_table_view2_doldur(TableView<DoktorTableAdapter> tableView, String id) {
        Doktor doktor = kontrolcuDoktor.findDoktor(id);
        final ObservableList<DoktorTableAdapter> data = FXCollections.observableArrayList();
        data.add(new DoktorTableAdapter(doktor.getId(), doktor.getAd(), doktor.getSoyad(), ""));
        tableView.setItems(data);
    }

    public void hasta_table_view_doldur(String id) {
        Hasta hasta = kontrolcuHasta.findHasta(id);
        final ObservableList<HastaTableAdapter> data = FXCollections.observableArrayList();
        data.add(new HastaTableAdapter(hasta.getId(), hasta.getAd(), hasta.getSoyad()));
        hasta_table_view.setItems(data);
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

    public void init_randevu_table_view(TableView<RandevuTableAdapter> tableView) {
        TableColumn idcol = new TableColumn("Randevu ID");
        TableColumn tarihcol = new TableColumn("Randevu Tarihi");
        TableColumn doktoridcol = new TableColumn("Doktor TC");
        TableColumn hastaidcol = new TableColumn("Hasta TC");
        idcol.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        tarihcol.setCellValueFactory(
                new PropertyValueFactory<>("tarih"));
        doktoridcol.setCellValueFactory(
                new PropertyValueFactory<>("doktor_id"));
        hastaidcol.setCellValueFactory(
                new PropertyValueFactory<>("hasta_id"));
        tableView.getColumns().addAll(idcol, tarihcol, doktoridcol, hastaidcol);
    }

    public void init_doktor_table_view(TableView<DoktorTableAdapter> tableView) {
        TableColumn idcol2 = new TableColumn("Doktor TC");
        TableColumn adcol = new TableColumn("Doktor Adi");
        TableColumn soyadcol = new TableColumn("Doktor Soyad");
        idcol2.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        adcol.setCellValueFactory(
                new PropertyValueFactory<>("ad"));
        soyadcol.setCellValueFactory(
                new PropertyValueFactory<>("soyad"));
        tableView.getColumns().addAll(idcol2, adcol, soyadcol);
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

    public void fill_all_table_views() {   
        clear_all_table_views();
        randevu_table_view2_doldur(randevular_table_view2);
        randevu_table_view2_doldur(randevular_table_view3);
        klinik_table_view_doldur(klinik_table_view);
        klinik_table_view_doldur(klinik_table_view2);
    }

    public void clear_all_table_views() {
        klinik_table_view.setItems(null);
        klinik_table_view2.setItems(null);
        doktor_table_view.setItems(null);
        doktor_table_view2.setItems(null);
        doktor_table_view3.setItems(null);
        doktor_table_view4.setItems(null);
        randevular_table_view.setItems(null);
        randevular_table_view2.setItems(null);
        randevular_table_view3.setItems(null);
        hasta_table_view.setItems(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        System.out.println("sekreter modulu yukleniyor..");

        tab_pane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            fill_all_table_views();
        });

        PERSISTENCE_UNIT_NAME = "com.mycompany_hastane_sistemi_jar_1.0-SNAPSHOTPU";
        factory = factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        kontrolcuKlinik = new KlinikJpaController(factory);
        kontrolcuDoktor = new DoktorJpaController(factory);
        kontrolcuHasta = new HastaJpaController(factory);
        kontrolcuRandevu = new RandevuJpaController(factory);

        init_klinik_table_view(klinik_table_view);
        klinik_table_view.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                doktor_table_view_doldur(doktor_table_view, klinik_table_view.getSelectionModel().getSelectedItem().getId());
            }
        });
        init_klinik_table_view(klinik_table_view2);
        klinik_table_view2.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                doktor_table_view_doldur(doktor_table_view3, klinik_table_view2.getSelectionModel().getSelectedItem().getId());
            }
        });

        init_doktor_table_view(doktor_table_view);
        init_doktor_table_view(doktor_table_view2);
        init_doktor_table_view(doktor_table_view3);
        init_doktor_table_view(doktor_table_view4);

        init_randevu_table_view(randevular_table_view);
        randevular_table_view.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                doktor_table_view2_doldur(doktor_table_view2, randevular_table_view.getSelectionModel().getSelectedItem().getDoktor_id());
            }
        });

        init_randevu_table_view(randevular_table_view2);
        randevular_table_view2.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Hasta hasta = kontrolcuHasta.findHasta(randevular_table_view2.getSelectionModel().getSelectedItem().getHasta_id());
                yeni_hasta_ad_tf.setText(hasta.getAd());
                yeni_hasta_soyad_tf.setText(hasta.getSoyad());
                yeni_hasta_tc_tf.setText(hasta.getId());
                yeni_hasta_tarih_tf.setText(randevular_table_view2.getSelectionModel().getSelectedItem().getTarih());
                doktor_table_view2_doldur(doktor_table_view3, randevular_table_view2.getSelectionModel().getSelectedItem().getDoktor_id());

            }
        });

        init_randevu_table_view(randevular_table_view3);
        randevular_table_view3.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                doktor_table_view2_doldur(doktor_table_view4, randevular_table_view3.getSelectionModel().getSelectedItem().getDoktor_id());
                hasta_table_view_doldur(randevular_table_view3.getSelectionModel().getSelectedItem().getHasta_id());
            }
        });

        init_hasta_table_view(hasta_table_view);

        fill_all_table_views();

    }
}
