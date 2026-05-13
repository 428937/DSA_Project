package ui;

import models.Emlak;
import yonetici.EmlakYoneticisi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

// Ana Ekran - Uygulamanın Ana Penceresi
public class AnaEkran extends JFrame {
    private EmlakYoneticisi yonetici;
    private JTable emlakTablosu;
    private DefaultTableModel tabloModel;
    private JLabel toplamBilgiLabel;
    private JLabel secilenLabel;
    private JButton ekleButonu;
    private JButton guncelleButonu;
    private JButton silButonu;
    private JButton favoriButonu;
    private JButton gerialButonu;
    private JButton yineleButonu;
    private JTabbedPane tabPanel;
    private AramaFiltrelePanel aramaPanel;
    private FavoriListesiPanel favoriPanel;
    private Emlak secilenEmlak;
    
    // Kurucu metot
    public AnaEkran() {
        this.yonetici = new EmlakYoneticisi();
        this.secilenEmlak = null;
        
        pencereAyarla();
        arayuzuOlustur();
        verileriBilgisiniGuncelle();
        
        setVisible(true);
    }
    
    // Pencere ayarları
    private void pencereAyarla() {
        setTitle("Emlak Listeleme Uygulaması");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    // Arayüzü oluştur
    private void arayuzuOlustur() {
        // Genel layout
        JPanel anaPanel = new JPanel(new BorderLayout(10, 10));
        anaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Üst panel - Başlık ve toolbar
        JPanel ustPanel = new JPanel(new BorderLayout(10, 10));
        ustPanel.setBackground(new Color(33, 150, 243));
        
        JLabel baslikLabel = new JLabel("Emlak Listeleme Yönetim Sistemi");
        baslikLabel.setFont(new Font("Arial", Font.BOLD, 20));
        baslikLabel.setForeground(Color.WHITE);
        baslikLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        ustPanel.add(baslikLabel, BorderLayout.WEST);
        
        // Toolbar
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        toolbarPanel.setBackground(new Color(33, 150, 243));
        
        ekleButonu = yeniButonOlustur("Yeni İlan Ekle", new Color(76, 175, 80), 
            e -> ilanEkleIslem());
        guncelleButonu = yeniButonOlustur("Güncelle", new Color(33, 150, 243), 
            e -> ilanGuncelleIslem());
        silButonu = yeniButonOlustur("Sil", new Color(244, 67, 54), 
            e -> ilanSilIslem());
        favoriButonu = yeniButonOlustur("Favori Ekle/Çıkar", new Color(255, 152, 0), 
            e -> favoriToggle());
        gerialButonu = yeniButonOlustur("Geri Al (Ctrl+Z)", new Color(158, 158, 158), 
            e -> gerialYap());
        yineleButonu = yeniButonOlustur("Yinele (Ctrl+Y)", new Color(158, 158, 158), 
            e -> yineleYap());
        
        toolbarPanel.add(ekleButonu);
        toolbarPanel.add(guncelleButonu);
        toolbarPanel.add(silButonu);
        toolbarPanel.add(favoriButonu);
        toolbarPanel.add(gerialButonu);
        toolbarPanel.add(yineleButonu);
        
        ustPanel.add(toolbarPanel, BorderLayout.CENTER);
        
        anaPanel.add(ustPanel, BorderLayout.NORTH);
        
        // Orta panel - Tabbed pane
        tabPanel = new JTabbedPane();
        tabPanel.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Tab 1 - Ana Liste
        JPanel anaListePanel = new JPanel(new BorderLayout(10, 10));
        tablo Model = new DefaultTableModel(new String[]{"ID", "Ad", "Konum", "Fiyat (₺)", 
            "Alan (m²)", "Tip", "Oran (₺/m²)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        emlakTablosu = new JTable(tablo Model);
        emlakTablosu.setFont(new Font("Arial", Font.PLAIN, 11));
        emlakTablosu.setRowHeight(25);
        emlakTablosu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        emlakTablosu.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && emlakTablosu.getSelectedRow() >= 0) {
                int id = (int) tablo Model.getValueAt(emlakTablosu.getSelectedRow(), 0);
                secilenEmlak = yonetici.emlakBul(id);
                guncelleSeciliLabel();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(emlakTablosu);
        anaListePanel.add(scrollPane, BorderLayout.CENTER);
        
        tabPanel.addTab("Ana Liste", anaListePanel);
        
        // Tab 2 - Arama ve Filtreleme
        aramaPanel = new AramaFiltrelePanel(yonetici, this);
        tabPanel.addTab("Arama ve Filtreleme", aramaPanel);
        
        // Tab 3 - Favoriler
        favoriPanel = new FavoriListesiPanel(yonetici, this);
        tabPanel.addTab("Favoriler", favoriPanel);
        
        anaPanel.add(tabPanel, BorderLayout.CENTER);
        
        // Alt panel - Bilgi çubuğu
        JPanel altPanel = new JPanel(new BorderLayout(10, 10));
        altPanel.setBackground(new Color(240, 245, 250));
        altPanel.setBorder(BorderFactory.createEtchedBorder());
        
        toplamBilgiLabel = new JLabel("Toplam İlan: 0");
        toplamBilgiLabel.setFont(new Font("Arial", Font.BOLD, 12));
        altPanel.add(toplamBilgiLabel, BorderLayout.WEST);
        
        secilenLabel = new JLabel("Seçili İlan: Yok");
        secilenLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        altPanel.add(secilenLabel, BorderLayout.EAST);
        
        anaPanel.add(altPanel, BorderLayout.SOUTH);
        
        add(anaPanel);
        
        // Tuş kısayolları
        tuşKısayollariniYükle();
        
        // Listeyi başta doldur
        listeyi Yenile();
    }
    
    // Yeni buton oluştur
    private JButton yeniButonOlustur(String metin, Color renk, java.awt.event.ActionListener islem) {
        JButton buton = new JButton(metin);
        buton.setFont(new Font("Arial", Font.BOLD, 11));
        buton.setBackground(renk);
        buton.setForeground(Color.WHITE);
        buton.setFocusPainted(false);
        buton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buton.addActionListener(islem);
        return buton;
    }
    
    // Tuş kısayolları
    private void tuşKısayollariniYükle() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
                    gerialYap();
                    return true;
                }
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Y) {
                    yineleYap();
                    return true;
                }
                if (e.getKeyCode() == KeyEvent.VK_DELETE && secilenEmlak != null) {
                    ilanSilIslem();
                    return true;
                }
            }
            return false;
        });
    }
    
    // İlan ekleme işlemi
    private void ilanEkleIslem() {
        IlanEkleDialog dialog = new IlanEkleDialog(this);
        dialog.setVisible(true);
        
        if (dialog.isKaydedildi()) {
            Emlak yeniEmlak = dialog.getYeniEmlak();
            if (yonetici.emlakEkle(yeniEmlak.getAd(), yeniEmlak.getKonum(), 
                yeniEmlak.getFiyat(), yeniEmlak.getAlan(), 
                yeniEmlak.getTip(), yeniEmlak.getAciklama())) {
                listeyi Yenile();
                verileriBilgisiniGuncelle();
                JOptionPane.showMessageDialog(this, 
                    "İlan başarıyla eklendi.", 
                    "Başarılı", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    // İlan güncelleme işlemi
    private void ilanGuncelleIslem() {
        if (secilenEmlak == null) {
            JOptionPane.showMessageDialog(this, 
                "Lütfen güncellenecek bir ilan seçiniz.", 
                "Uyarı", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Emlak yedek = new Emlak(secilenEmlak.getId(), secilenEmlak.getAd(), 
            secilenEmlak.getKonum(), secilenEmlak.getFiyat(), secilenEmlak.getAlan(), 
            secilenEmlak.getTip(), secilenEmlak.getAciklama());
        
        IlanGuncelleDialog dialog = new IlanGuncelleDialog(this, secilenEmlak);
        dialog.setVisible(true);
        
        if (dialog.isGuncellendi()) {
            yonetici.emlakGuncelle(secilenEmlak.getId(), secilenEmlak.getAd(), 
                secilenEmlak.getKonum(), secilenEmlak.getFiyat(), secilenEmlak.getAlan(), 
                secilenEmlak.getTip(), secilenEmlak.getAciklama());
            listeyi Yenile();
            verileriBilgisiniGuncelle();
            JOptionPane.showMessageDialog(this, 
                "İlan başarıyla güncellendi.", 
                "Başarılı", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // İlan silme işlemi
    private void ilanSilIslem() {
        if (secilenEmlak == null) {
            JOptionPane.showMessageDialog(this, 
                "Lütfen silinecek bir ilan seçiniz.", 
                "Uyarı", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int sonuc = JOptionPane.showConfirmDialog(this, 
            "İlanı silmek istediğinizden emin misiniz?", 
            "Onay", 
            JOptionPane.YES_NO_OPTION);
        
        if (sonuc == JOptionPane.YES_OPTION) {
            yonetici.emlakSil(secilenEmlak.getId());
            secilenEmlak = null;
            listeyi Yenile();
            verileriBilgisiniGuncelle();
            JOptionPane.showMessageDialog(this, 
                "İlan başarıyla silindi.", 
                "Başarılı", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Favori toggle
    private void favoriToggle() {
        if (secilenEmlak == null) {
            JOptionPane.showMessageDialog(this, 
                "Lütfen bir ilan seçiniz.", 
                "Uyarı", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (secilenEmlak.isFavosilerde()) {
            yonetici.favoriKaldir(secilenEmlak.getId());
        } else {
            yonetici.favoriEkle(secilenEmlak.getId());
        }
        
        listeyi Yenile();
        favoriPanel.listeyi Yenile();
        verileriBilgisiniGuncelle();
    }
    
    // Geri al
    private void gerialYap() {
        if (!yonetici.gerialYapilabilir()) {
            JOptionPane.showMessageDialog(this, 
                "Geri alınacak işlem yok.", 
                "Bilgi", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        yonetici.gerialYap();
        listeyi Yenile();
        favoriPanel.listeyi Yenile();
        aramaPanel.listeyi Yenile();
        verileriBilgisiniGuncelle();
    }
    
    // Yinele
    private void yineleYap() {
        if (!yonetici.yineleYapilabilir()) {
            JOptionPane.showMessageDialog(this, 
                "Yinelenecek işlem yok.", 
                "Bilgi", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        yonetici.yineleYap();
        listeyi Yenile();
        favoriPanel.listeyi Yenile();
        aramaPanel.listeyi Yenile();
        verileriBilgisiniGuncelle();
    }
    
    // Listeyi yenile
    public void listeyi Yenile() {
        tablo Model.setRowCount(0);
        
        for (Emlak emlak : yonetici.tumEmlaklar()) {
            tablo Model.addRow(new Object[]{
                emlak.getId(),
                emlak.getAd(),
                emlak.getKonum(),
                String.format("%.2f", emlak.getFiyat()),
                String.format("%.2f", emlak.getAlan()),
                emlak.getTip(),
                String.format("%.2f", emlak.getFiyatAlanOrani())
            });
        }
    }
    
    // Verileri bilgisini güncelle
    private void verileriBilgisiniGuncelle() {
        toplamBilgiLabel.setText("Toplam İlan: " + yonetici.emlakSayisi());
        guncelleSeciliLabel();
    }
    
    // Seçili label güncelle
    private void guncelleSeciliLabel() {
        if (secilenEmlak != null) {
            secilenLabel.setText(String.format("Seçili: %s (%s) - Fiyat: %.0f₺", 
                secilenEmlak.getAd(), secilenEmlak.getKonum(), secilenEmlak.getFiyat()));
        } else {
            secilenLabel.setText("Seçili İlan: Yok");
        }
    }
    
    // Seçilen emlak güncelle (Arama panelinden)
    public void secilenEmlakiGuncelle(Emlak emlak) {
        secilenEmlak = emlak;
        guncelleSeciliLabel();
        
        // Tablodan seçili satırı işaretle
        for (int i = 0; i < tablo Model.getRowCount(); i++) {
            if ((int) tablo Model.getValueAt(i, 0) == emlak.getId()) {
                emlakTablosu.setRowSelectionInterval(i, i);
                break;
            }
        }
    }
}