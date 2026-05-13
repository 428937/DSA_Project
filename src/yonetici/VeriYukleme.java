package yonetici;

import models.Emlak;
import models.BaglıListe;
import java.io.*;
import java.util.StringTokenizer;

// Veri Yükleme ve Kaydetme işlemleri
public class VeriYukleme {
    private static final String VERI_DOSYASI = "data/ilanlar.txt";
    
    // Veri dosyasının dizinini oluştur
    static {
        File dizin = new File("data");
        if (!dizin.exists()) {
            dizin.mkdirs();
        }
    }
    
    // Dosyadan emlakları yükle
    public static BaglıListe<Emlak> yükle
            return emlaklar;
        }
        
        try (BufferedReader okuyucu = new BufferedReader(new FileReader(dosya))) {
            String satir;
            while ((satir = okuyucu.readLine()) != null) {
                Emlak emlak = satirdanEmlakOlustur(satir);
                if (emlak != null) {
                    emlaklar.sonaEkle(emlak);
                }
            }
        } catch (IOException e) {
            System.err.println("Dosya okuma hatası: " + e.getMessage());
        }
        
        return emlaklar;
    }
    
    // Emlakları dosyaya kaydet
    public static boolean kaydet(BaglıListe<Emlak> emlaklar) {
        try (PrintWriter yazici = new PrintWriter(new FileWriter(VERI_DOSYASI))) {
            for (Emlak emlak : emlaklar) {
                yazici.println(emlakıSatıraDönüştür(emlak));
            }
            return true;
        } catch (IOException e) {
            System.err.println("Dosya yazma hatası: " + e.getMessage());
            return false;
        }
    }
    
    // Satırdan Emlak nesnesi oluştur (Parsing)
    private static Emlak satirdanEmlakOlustur(String satir) {
        try {
            // Format: id|ad|konum|fiyat|alan|tip|aciklama|favorilerde|eklemeTarihi
            StringTokenizer tokenizer = new StringTokenizer(satir, "|");
            
            if (tokenizer.countTokens() < 8) {
                return null;
            }
            
            int id = Integer.parseInt(tokenizer.nextToken());
            String ad = tokenizer.nextToken();
            String konum = tokenizer.nextToken();
            double fiyat = Double.parseDouble(tokenizer.nextToken());
            double alan = Double.parseDouble(tokenizer.nextToken());
            String tip = tokenizer.nextToken();
            String aciklama = tokenizer.nextToken();
            boolean favorilerde = Boolean.parseBoolean(tokenizer.nextToken());
            long eklemeTarihi = Long.parseLong(tokenizer.nextToken());
            
            Emlak emlak = new Emlak(id, ad, konum, fiyat, alan, tip, aciklama);
            emlak.setFavorilerde(favorilerde);
            emlak.setEklemeTarihi(eklemeTarihi);
            
            return emlak;
        } catch (Exception e) {
            System.err.println("Satır parsing hatası: " + satir);
            return null;
        }
    }
    
    // Emlak nesnesini satıra dönüştür (Serialization)
    private static String emlakıSatıraDönüştür(Emlak emlak) {
        return String.format("%d|%s|%s|%.2f|%.2f|%s|%s|%s|%d",
            emlak.getId(),
            emlak.getAd(),
            emlak.getKonum(),
            emlak.getFiyat(),
            emlak.getAlan(),
            emlak.getTip(),
            emlak.getAciklama(),
            emlak.isFavosilerde(),
            emlak.getEklemeTarihi()
        );
    }
    
    // Dosyayı temizle
    public static boolean temizle() {
        File dosya = new File(VERI_DOSYASI);
        return dosya.delete();
    }
    
    // Dosya var mı kontrolü
    public static boolean dosyaVarMi() {
        return new File(VERI_DOSYASI).exists();
    }
}