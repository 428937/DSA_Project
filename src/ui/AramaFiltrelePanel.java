package ui;

import models.Emlak;
import yonetici.EmlakYoneticisi;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

// Arama ve Filtreleme Paneli
public class AramaFiltrelePanel extends JPanel {
    private JTextField adAraField;
    private JTextField konumAraField;
    private JSpinner minFiyatSpinner;
    private JSpinner maxFiyatSpinner;
    private JSpinner minAlanSpinner;
    private JSpinner maxAlanSpinner;
    private JComboBox<String> tipFilterCombo;
    private JButton araButonu;
    private JButton temizleButonu;
    private JButton fiyatArtiButonu;
    private JButton fiyatAzalanButonu;
    private JButton alanArtiButonu;
    private JButton oranButonu;
    private JList<Emlak> sonucListesi;
    private DefaultListModel<Emlak> sonucModel;
    
    private EmlakYoneticisi yonetici;
    private AnaEkran anaEkran;
    
    // Kurucu metot
    public AramaFiltrelePanel(EmlakYoneticisi yonetici, AnaEkran anaEkran) {
        this.yonetici = yonetici;
        this.anaEkran = anaEkran;
        this.sonucModel = new DefaultListModel<>();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 245, 250));
        
        arayuzuOlustur();
    }
    
    // Arayüzü oluştur
    private void arayuzuOlustur() {
        // Üst panel - Arama alanları
        JPanel aramaPaneli = new JPanel(new GridLayout(3, 4, 8, 8));
        aramaPaneli.setBorder(BorderFactory.createTitledBorder("Arama ve Filtreleme"));
        aramaPaneli.setBackground(new Color(245, 248, 250));
        
        // Ad araması
        aramaPaneli.add(new JLabel("Ada Göre Ara:"));
        adAraField = new JTextField();
        adAraField.setFont(new Font("Arial", Font.PLAIN, 11));
        aramaPaneli.add(adAraField);
        
        // Konum araması
        aramaPaneli.add(new JLabel("Konuma Göre Ara:"));
        konumAraField = new JTextField();
        konumAraField.setFont(new Font("Arial", Font.PLAIN, 11));
        aramaPaneli.add(konumAraField);
        
        // Tip filtresi
        aramaPaneli.add(new JLabel("Tip Filtresi:"));
        tipFilterCombo = new JComboBox<>();
        tipFilterCombo.addItem("Tümü");
        for (String tip : yonetici.tumTipCesitleri()) {
            tipFilterCombo.addItem(tip);
        }
        tipFilterCombo.setFont(new Font("Arial", Font.PLAIN, 11));
        aramaPaneli.add(tipFilterCombo);
        
        // Min Fiyat
        aramaPaneli.add(new JLabel("Min Fiyat (₺):"));
        minFiyatSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, 50000.0));
        aramaPaneli.add(minFiyatSpinner);
        
        // Max Fiyat
        aramaPaneli.add(new JLabel("Max Fiyat (₺):"));
        maxFiyatSpinner = new JSpinner(new SpinnerNumberModel(Double.MAX_VALUE, 0.0, Double.MAX_VALUE, 50000.0));
        aramaPaneli.add(maxFiyatSpinner);
        
        // Min Alan
        aramaPaneli.add(new JLabel("Min Alan (m²):"));
        minAlanSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, Double.MAX_VALUE, 10.0));
        aramaPaneli.add(minAlanSpinner);
        
        // Max Alan
        aramaPaneli.add(new JLabel("Max Alan (m²):"));
        maxAlanSpinner = new JSpinner(new SpinnerNumberModel(Double.MAX_VALUE, 0.0, Double.MAX_VALUE, 10.0));
        aramaPaneli.add(maxAlanSpinner);
        
        // Buton paneli (sağ taraf)
        JPanel butonPaneli = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        butonPaneli.setBackground(new Color(245, 248, 250));
        
        araButonu = new JButton("Ara");
        araButonu.setFont(new Font("Arial", Font.BOLD, 11));
        araButonu.setBackground(new Color(76, 175, 80));
        araButonu.setForeground(Color.WHITE);
        araButonu.setFocusPainted(false);
        araButonu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        araButonu.addActionListener(this::araIslem);
        
        temizleButonu = new JButton("Temizle");
        temizleButonu.setFont(new Font("Arial", Font.BOLD, 11));
        temizleButonu.setBackground(new Color(158, 158, 158));
        temizleButonu.setForeground(Color.WHITE);
        temizleButonu.setFocusPainted(false);
        temizleButonu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        temizleButonu.addActionListener(e -> temizle());
        
        butonPaneli.add(araButonu);
        butonPaneli.add(temizleButonu);
        
        aramaPaneli.add(butonPaneli);
        
        add(aramaPaneli, BorderLayout.NORTH);
        
        // Orta panel - Sıralama butonları
        JPanel siralamaPaneli = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        siralamaPaneli.setBorder(BorderFactory.createTitledBorder("Sıralama Seçenekleri"));
        siralamaPaneli.setBackground(new Color(245, 248, 250));
        
        fiyatArtiButonu = new JButton("Fiyat (Artan)");
        fiyatArtiButonu.setFont(new Font("Arial", Font.BOLD, 11));
        fiyatArtiButonu.setBackground(new Color(33, 150, 243));
        fiyatArtiButonu.setForeground(Color.WHITE);
        fiyatArtiButonu.setFocusPainted(false);
        fiyatArtiButonu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fiyatArtiButonu.addActionListener(e -> siralaFiyatArtanHali());
        
        fiyatAzalanButonu = new JButton("Fiyat (Azalan)");
        fiyatAzalanButonu.setFont(new Font("Arial", Font.BOLD, 11));
        fiyatAzalanButonu.setBackground(new Color(33, 150, 243));
        fiyatAzalanButonu.setForeground(Color.WHITE);
        fiyatAzalanButonu.setFocusPainted(false);
        fiyatAzalanButonu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fiyatAzalanButonu.addActionListener(e -> siralaFiyatAzalanHali());
        
        alanArtiButonu = new JButton("Alan (Artan)");
        alanArtiButonu.setFont(new Font("Arial", Font.BOLD, 11));
        alanArtiButonu.setBackground(new Color(33, 150, 243));
        alanArtiButonu.setForeground(Color.WHITE);
        alanArtiButonu.setFocusPainted(false);
        alanArtiButonu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        alanArtiButonu.addActionListener(e -> sirlalaAlanArtanHali());
        
        oranButonu = new JButton("Fiyat/Alan Oranı");
        oranButonu.setFont(new Font("Arial", Font.BOLD, 11));
        oranButonu.setBackground(new Color(156, 39, 176));
        oranButonu.setForeground(Color.WHITE);
        oranButonu.setFocusPainted(false);
        oranButonu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        oranButonu.addActionListener(e -> siralaOrana());
        
        add(siralamaPaneli, BorderLayout.CENTER);
        
        // Alt panel - Sonuç listesi
        JPanel sonucPaneli = new JPanel(new BorderLayout());
        sonucPaneli.setBorder(BorderFactory.createTitledBorder("Arama Sonuçları"));
        sonucPaneli.setBackground(new Color(245, 248, 250));
        
        sonucListesi = new JList<>(sonucModel);
        sonucListesi.setFont(new Font("Arial", Font.PLAIN, 12));
        sonucListesi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sonucListesi.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && sonucListesi.getSelectedValue() != null) {
                anaEkran.secilenEmlakiGuncelle(sonucListesi.getSelectedValue());
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(sonucListesi);
        scrollPane.setPreferredSize(new Dimension(0, 150));
        sonucPaneli.add(scrollPane, BorderLayout.CENTER);
        
        add(sonucPaneli, BorderLayout.SOUTH);
    }
    
    // Arama işlemi
    private void araIslem(ActionEvent e) {
        sonucModel.clear();
        
        String ad = adAraField.getText().trim();
        String konum = konumAraField.getText().trim();
        double minFiyat = (double) minFiyatSpinner.getValue();
        double maxFiyat = (double) maxFiyatSpinner.getValue();
        double minAlan = (double) minAlanSpinner.getValue();
        double maxAlan = (double) maxAlanSpinner.getValue();
        String tip = (String) tipFilterCombo.getSelectedItem();
        
        List<Emlak> sonuclar = new java.util.ArrayList<>();
        
        // Tüm emlaklar üzerinde filtrele
        for (Emlak emlak : yonetici.tumEmlaklar()) {
            boolean uyuyor = true;
            
            // Ad kontrolü
            if (!ad.isEmpty() && !emlak.getAd().toLowerCase().contains(ad.toLowerCase())) {
                uyuyor = false;
            }
            
            // Konum kontrolü
            if (!konum.isEmpty() && !emlak.getKonum().toLowerCase().contains(konum.toLowerCase())) {
                uyuyor = false;
            }
            
            // Fiyat aralığı kontrolü
            if (emlak.getFiyat() < minFiyat || emlak.getFiyat() > maxFiyat) {
                uyuyor = false;
            }
            
            // Alan aralığı kontrolü
            if (emlak.getAlan() < minAlan || emlak.getAlan() > maxAlan) {
                uyuyor = false;
            }
            
            // Tip kontrolü
            if (!tip.equals("Tümü") && !emlak.getTip().equals(tip)) {
                uyuyor = false;
            }
            
            if (uyuyor) {
                sonuclar.add(emlak);
            }
        }
        
        // Sonuçları listeye ekle
        for (Emlak emlak : sonuclar) {
            sonucModel.addElement(emlak);
        }
        
        if (sonuclar.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Arama kriterlerine uyan emlak bulunamadı.", 
                "Bilgi", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Fiyata göre artan sıralama
    private void siralaFiyatArtanHali() {
        sonucModel.clear();
        List<Emlak> siralı = yonetici.fiyataSiraArtanHali();
        for (Emlak emlak : siralı) {
            sonucModel.addElement(emlak);
        }
    }
    
    // Fiyata göre azalan sıralama
    private void siralaFiyatAzalanHali() {
        sonucModel.clear();
        List<Emlak> siralı = yonetici.fiyataSiraAzalanHali();
        for (Emlak emlak : siralı) {
            sonucModel.addElement(emlak);
        }
    }
    
    // Alan'a göre artan sıralama
    private void sirlalaAlanArtanHali() {
        sonucModel.clear();
        List<Emlak> siralı = yonetici.alanaGoreSiraArtanHali();
        for (Emlak emlak : siralı) {
            sonucModel.addElement(emlak);
        }
    }
    
    // Orana göre sıralama
    private void siralaOrana() {
        sonucModel.clear();
        List<Emlak> siralı = yonetici.fiyatAlanOraninaGoreSirala();
        for (Emlak emlak : siralı) {
            sonucModel.addElement(emlak);
        }
    }
    
    // Temizle
    private void temizle() {
        adAraField.setText("");
        konumAraField.setText("");
        minFiyatSpinner.setValue(0.0);
        maxFiyatSpinner.setValue(Double.MAX_VALUE);
        minAlanSpinner.setValue(0.0);
        maxAlanSpinner.setValue(Double.MAX_VALUE);
        tipFilterCombo.setSelectedIndex(0);
        sonucModel.clear();
    }
    
    // Listeyi yenile
    public void listeyi Yenile() {
        sonucModel.clear();
        for (Emlak emlak : yonetici.tumEmlaklar()) {
            sonucModel.addElement(emlak);
        }
    }
}