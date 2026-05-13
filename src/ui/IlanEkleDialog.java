package ui;

import models.Emlak;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// Yeni İlan Ekleme Dialog Penceresi
public class IlanEkleDialog extends JDialog {
    private JTextField adField;
    private JTextField konumField;
    private JTextField fiyatField;
    private JTextField alanField;
    private JComboBox<String> tipCombo;
    private JTextArea aciklamaArea;
    private JButton kaydetButonu;
    private JButton iptalButonu;
    
    private Emlak yeniEmlak;
    private boolean kaydedildi;
    
    // Kurucu metot
    public IlanEkleDialog(JFrame parent) {
        super(parent, "Yeni İlan Ekle", true);
        this.kaydedildi = false;
        
        arayuzuOlustur();
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
        
        kaydetButonu = new JButton("Kaydet");
        kaydetButonu.setFont(new Font("Arial", Font.BOLD, 12));
        kaydetButonu.setBackground(new Color(76, 175, 80));
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
            String ad = adField.getText().trim();
            String konum = konumField.getText().trim();
            double fiyat = Double.parseDouble(fiyatField.getText().trim());
            double alan = Double.parseDouble(alanField.getText().trim());
            String tip = (String) tipCombo.getSelectedItem();
            String aciklama = aciklamaArea.getText().trim();
            
            // Yeni emlak nesnesi oluştur
            yeniEmlak = new Emlak(0, ad, konum, fiyat, alan, tip, aciklama);
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
    
    // Yeni emlak nesnesini getir
    public Emlak getYeniEmlak() {
        return yeniEmlak;
    }
    
    // Kaydedildi mi kontrolü
    public boolean isKaydedildi() {
        return kaydedildi;
    }
}