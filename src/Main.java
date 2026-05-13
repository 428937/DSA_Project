import ui.AnaEkran;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

// Uygulamanın Giriş Noktası
public class Main {
    public static void main(String[] args) {
        // Swing UI'yi sistem görünümüne ayarla
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Look and Feel ayarı başarısız: " + e.getMessage());
        }
        
        // GUI oluşturma işlemini Event Dispatch Thread'de çalıştır
        SwingUtilities.invokeLater(() -> {
            new AnaEkran();
        });
    }
}