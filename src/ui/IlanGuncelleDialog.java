package ui;

import models.Emlak;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// İlan Güncelleme Dialog Penceresi
public class IlanGuncelleDialog extends JDialog {
    private JTextField adField;
    private JTextField konumField;
    private JTextField fiyatField;
    private JTextField alanField;
    private JComboBox<String> tipCombo;
    private JTextArea aciklamaArea;
    private JButton kaydetButonu;
    private JButton iptalButonu;
    
    private Emlak emlak;
    private boolean kaydedildi;
    
    // Kurucu metot
    public IlanGuncelleDialog(JFrame parent, Emlak emlak) {
        super(parent, "İlan Güncelle", true);
        this.emlak = emlak;
        this.kaydedildi = false;
        
        arayuzuOlustur();
        verileriDoldur();
        pencereAyarla();
    }
    
    // Arayüzü oluştur
    private void arayuzuOlustur() {
        JPanel anaPanel = new JPanel(new BorderLayout(10, 10));
        anaPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Form paneli
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBackground(new Color(240, 245, 250));
        
        // Ad
        formPanel.add(new JLabel("İlan Adı:"));
        adField = new JTextField();
        adField.setFont(new Font("Arial", Font.PLAIN, 12));
        formPanel.add(adField);
        
        // Konum
        formPanel.add(new JLabel("Konum:"));
        konumField = new JTextField();
        konumField.setFont(new Font("Arial", Font.PLAIN, 12));
        formPanel.add(konumField);
        
        // Fiyat
        formPanel.add(new JLabel("Fiyat (₺):"));
        fiyatField = new JTextField();
        fiyatField.setFont(new Font("Arial", Font.PLAIN, 12));
        formPanel.add(fiyatField);
        
        // Alan
        formPanel.add(new JLabel("Alan (m²):"));
        alanField = new JTextField();
        alanField.setFont(new Font("Arial", Font.PLAIN, 12));
        formPanel.add(alanField);
        
        // Tip
        formPanel.add(new JLabel("Emlak Tipi:"));
        tipCombo = new JComboBox<>(new String[]{"Daire", "Villa", "Ev", "Arsa", "İşyeri", "Diğer"});
        tipCombo.setFont(new Font("Arial", Font.PLAIN, 12));
        formPanel.add(tipCombo);
        
        // Açıklama Label
        formPanel.add(new JLabel("Açıklama:"));
        formPanel.add(new JLabel(""));
        
        anaPanel.add(formPanel, BorderLayout.NORTH);
        
        // Açıklama Text Area
        aciklamaArea = new JTextArea(4, 30);
        aciklamaArea.setFont(new Font("Arial", Font.PLAIN, 11));
        aciklamaArea.setLineWrap(true);
        aciklamaArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(aciklamaArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Açıklama"));
        anaPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Buton paneli
        JPanel butonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        butonPanel.setBackground(new Color(235, 240, 245));
        
        kaydetButonu = new JButton("Güncelle");
        kaydetButonu.setFont(new Font("Arial", Font.BOLD, 12));
        kaydetButonu.setBackground(new Color(33, 150, 243));
        kaydetButonu.setForeground(Color.WHITE);
        kaydetButonu.setFocusPainted(false);
        kaydetButonu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        kaydetButonu.addActionListener(this::kaydetIslem);
        
        iptalButonu = new JButton("İptal");
        iptalButonu.setFont(new Font("Arial", Font.BOLD, 12));
        iptalButonu.setBackground(new Color(244, 67, 54));
        iptalButonu.setForeground(Color.WHITE);
        iptalButonu.setFocusPainted(false);
        iptalButonu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        iptalButonu.addActionListener(e -> dispose());
        
        butonPanel.add(kaydetButonu);
        butonPanel.add(iptalButonu);
        
        anaPanel.add(butonPanel, BorderLayout.SOUTH);
        
        add(anaPanel);
    }
    
    // Verileri form alanlarına doldur
    private void verileriDoldur() {
        adField.setText(emlak.getAd());
        konumField.setText(emlak.getKonum());
        fiyatField.setText(String.valueOf(emlak.getFiyat()));
        alanField.setText(String.valueOf(emlak.getAlan()));
        tipCombo.setSelectedItem(emlak.getTip());
        aciklamaArea.setText(emlak.getAciklama());
    }
    
    // Pencere ayarları
    private void pencereAyarla() {
        setSize(450, 500);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    // Kaydet işlemi
    private void kaydetIslem(ActionEvent e) {
        // Girdi doğrulaması
        if (!girdiDogrulaması()) {
            return;
        }
        
        try {
            emlak.setAd(adField.getText().trim());
            emlak.setKonum(konumField.getText().trim());
            emlak.setFiyat(Double.parseDouble(fiyatField.getText().trim()));
            emlak.setAlan(Double.parseDouble(alanField.getText().trim()));
            emlak.setTip((String) tipCombo.getSelectedItem());
            emlak.setAciklama(aciklamaArea.getText().trim());
            
            kaydedildi = true;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Fiyat ve alan sayısal değer olmalıdır!", 
                "Hata", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Girdi doğrulaması
    private boolean girdiDogrulaması() {
        if (adField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "İlan adı boş olamaz!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (konumField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Konum boş olamaz!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        try {
            double fiyat = Double.parseDouble(fiyatField.getText().trim());
            if (fiyat <= 0) {
                JOptionPane.showMessageDialog(this, "Fiyat sıfırdan büyük olmalıdır!", "Uyarı", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Fiyat geçersiz!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        try {
            double alan = Double.parseDouble(alanField.getText().trim());
            if (alan <= 0) {
                JOptionPane.showMessageDialog(this, "Alan sıfırdan büyük olmalıdır!", "Uyarı", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Alan geçersiz!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    // Güncellendi mi kontrolü
    public boolean isGuncellendi() {
        return kaydedildi;
    }
}