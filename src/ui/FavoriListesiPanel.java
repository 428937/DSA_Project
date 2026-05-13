package ui;

import models.Emlak;
import yonetici.EmlakYoneticisi;
import javax.swing.*;
import java.awt.*;
import java.util.List;

// Favori Listesi Paneli
public class FavoriListesiPanel extends JPanel {
    private DefaultListModel<Emlak> favoriModel;
    private JList<Emlak> favoriListesi;
    private JLabel favoriSayisiLabel;
    private JButton kaldir Butonu;
    private JButton temizleButonu;
    
    private EmlakYoneticisi yonetici;
    private AnaEkran anaEkran;
    
    // Kurucu metot
    public FavoriListesiPanel(EmlakYoneticisi yonetici, AnaEkran anaEkran) {
        this.yonetici = yonetici;
        this.anaEkran = anaEkran;
        this.favoriModel = new DefaultListModel<>();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(255, 250, 240));
        
        arayuzuOlustur();
        listeyi Yenile();
    }
    
    // Arayüzü oluştur
    private void arayuzuOlustur() {
        // Üst panel - Başlık ve bilgi
        JPanel ustPanel = new JPanel(new BorderLayout(10, 10));
        ustPanel.setBackground(new Color(255, 250, 240));
        
        JLabel baslikLabel = new JLabel("Favori İlanlarım");
        baslikLabel.setFont(new Font("Arial", Font.BOLD, 16));
        baslikLabel.setForeground(new Color(255, 152, 0));
        ustPanel.add(baslikLabel, BorderLayout.WEST);
        
        JPanel bilgiPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bilgiPanel.setBackground(new Color(255, 250, 240));
        
        favoriSayisiLabel = new JLabel("Favori Sayısı: 0");
        favoriSayisiLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        bilgiPanel.add(favoriSayisiLabel);
        
        ustPanel.add(bilgiPanel, BorderLayout.EAST);
        
        add(ustPanel, BorderLayout.NORTH);
        
        // Orta panel - Liste
        JPanel ortaPanel = new JPanel(new BorderLayout());
        ortaPanel.setBorder(BorderFactory.createTitledBorder("Favori İlanlar Listesi"));
        ortaPanel.setBackground(new Color(255, 250, 240));
        
        favoriListesi = new JList<>(favoriModel);
        favoriListesi.setFont(new Font("Arial", Font.PLAIN, 12));
        favoriListesi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        favoriListesi.setBackground(new Color(255, 255, 255));
        favoriListesi.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && favoriListesi.getSelectedValue() != null) {
                anaEkran.secilenEmlakiGuncelle(favoriListesi.getSelectedValue());
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(favoriListesi);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        ortaPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(ortaPanel, BorderLayout.CENTER);
        
        // Alt panel - Butonlar
        JPanel altPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        altPanel.setBackground(new Color(255, 250, 240));
        
        kaldir Butonu = new JButton("Favorilerden Çıkar");
        kaldir Butonu.setFont(new Font("Arial", Font.BOLD, 11));
        kaldir Butonu.setBackground(new Color(244, 67, 54));
        kaldir Butonu.setForeground(Color.WHITE);
        kaldir Butonu.setFocusPainted(false);
        kaldir Butonu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        kaldir Butonu.addActionListener(e -> kaldir IslemButonu());
        
        temizleButonu = new JButton("Tümünü Temizle");
        temizleButonu.setFont(new Font("Arial", Font.BOLD, 11));
        temizleButonu.setBackground(new Color(158, 158, 158));
        temizleButonu.setForeground(Color.WHITE);
        temizleButonu.setFocusPainted(false);
        temizleButonu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        temizleButonu.addActionListener(e -> temizleIslem());
        
        altPanel.add(kaldir Butonu);
        altPanel.add(temizleButonu);
        
        add(altPanel, BorderLayout.SOUTH);
    }
    
    // Favoriden çıkar işlemi
    private void kaldir IslemButonu() {
        Emlak secili = favoriListesi.getSelectedValue();
        if (secili != null) {
            yonetici.favoriKaldir(secili.getId());
            listeyi Yenile();
            anaEkran.listeyi Yenile();
            JOptionPane.showMessageDialog(this, 
                "İlan favorilerden çıkarıldı.", 
                "Başarılı", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Lütfen bir ilan seçiniz.", 
                "Uyarı", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    // Tümünü temizle işlemi
    private void temizleIslem() {
        if (favoriModel.getSize() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Zaten favori yoktur.", 
                "Bilgi", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int sonuc = JOptionPane.showConfirmDialog(this, 
            "Tüm favorileri temizlemek istediğinizden emin misiniz?", 
            "Onay", 
            JOptionPane.YES_NO_OPTION);
        
        if (sonuc == JOptionPane.YES_OPTION) {
            for (int i = 0; i < favoriModel.getSize(); i++) {
                yonetici.favoriKaldir(favoriModel.get(i).getId());
            }
            listeyi Yenile();
            anaEkran.listeyi Yenile();
            JOptionPane.showMessageDialog(this, 
                "Tüm favoriler temizlendi.", 
                "Başarılı", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Listeyi yenile
    public void listeyi Yenile() {
        favoriModel.clear();
        List<Emlak> favoriler = yonetici.favorileriGetir();
        
        for (Emlak emlak : favoriler) {
            favoriModel.addElement(emlak);
        }
        
        favoriSayisiLabel.setText("Favori Sayısı: " + favoriler.size());
    }
}