package yonetici;

import models.Emlak;
import models.Yıgın;

// Geri Al (Undo) ve Yinele (Redo) sistemi
public class GeriyeAlYineleSistemi {
    
    // İşlem türleri
    public enum IslemTuru {
        EKLE("Emlak Eklendi"),
        SIL("Emlak Silindi"),
        GUNCELLE("Emlak Güncellendi"),
        FAVORI_EKLE("Favorilere Eklendi"),
        FAVORI_KALDIR("Favorilerden Çıkarıldı");
        
        private final String aciklama;
        
        IslemTuru(String aciklama) {
            this.aciklama = aciklama;
        }
        
        public String getAciklama() {
            return aciklama;
        }
    }
    
    // İşlem sınıfı
    private class Islem {
        IslemTuru tur;
        Emlak emlak;
        Emlak eskiEmlak; // Güncelleme için eski halini kaydet
        long zaman;
        
        Islem(IslemTuru tur, Emlak emlak) {
            this.tur = tur;
            this.emlak = emlak;
            this.zaman = System.currentTimeMillis();
        }
        
        Islem(IslemTuru tur, Emlak emlak, Emlak eskiEmlak) {
            this(tur, emlak);
            this.eskiEmlak = eskiEmlak;
        }
    }
    
    private Yıgın<Islem> gerialStack;  // Geri alma yığını
    private Yıgın<Islem> yineleStack; // Yineleme yığını
    private static final int MAX_ISLEM = 50; // Maksimum hafızada tutulacak işlem
    
    // Kurucu metot
    public GeriyeAlYineleSistemi() {
        this.gerialStack = new Yıgın<>();
        this.yineleStack = new Yıgın<>();
    }
    
    // İşlem kaydı
    public void kayitEkle(IslemTuru tur, Emlak emlak) {
        // Yeni işlem yapıldığında redo yığını temizle
        yineleStack.temizle();
        
        Islem islem = new Islem(tur, emlak);
        gerialStack.ekle(islem);
        
        // Maksimum işlem sayısını aş
        if (gerialStack.boyut() > MAX_ISLEM) {
            gerialStack.cikart(); // En eski işlemi çıkar
        }
    }
    
    // Güncelleme işlemini kaydet (Eski ve yeni veri gerekli)
    public void guncellemeKayitEkle(Emlak eskiEmlak, Emlak yeniEmlak) {
        yineleStack.temizle();
        
        Islem islem = new Islem(IslemTuru.GUNCELLE, yeniEmlak, eskiEmlak);
        gerialStack.ekle(islem);
        
        if (gerialStack.boyut() > MAX_ISLEM) {
            gerialStack.cikart();
        }
    }
    
    // Geri alma işlemini al
    public Islem gerialYapılabilirMi() {
        if (!gerialStack.bosMu()) {
            return gerialStack.tepe();
        }
        return null;
    }
    
    // Yinele işlemini al
    public Islem yineleYapılabilirMi() {
        if (!yineleStack.bosMu()) {
            return yineleStack.tepe();
        }
        return null;
    }
    
    // Geri al
    public Islem gerialYap() {
        if (!gerialStack.bosMu()) {
            Islem islem = gerialStack.cikart();
            yineleStack.ekle(islem); // Redo yığınına ekle
            return islem;
        }
        return null;
    }
    
    // Yinele
    public Islem yineleYap() {
        if (!yineleStack.bosMu()) {
            Islem islem = yineleStack.cikart();
            gerialStack.ekle(islem); // Geri al yığınına ekle
            return islem;
        }
        return null;
    }
    
    // Son işlemin tanımını al
    public String sonIslemAciklama() {
        if (!gerialStack.bosMu()) {
            return gerialStack.tepe().tur.getAciklama();
        }
        return "İşlem yok";
    }
    
    // Geri alınabilecek son işlemin açıklaması
    public String geriAlacakIslemAciklama() {
        if (!yineleStack.bosMu()) {
            return yineleStack.tepe().tur.getAciklama();
        }
        return "İşlem yok";
    }
    
    // İşlem geçmişini temizle
    public void temizle() {
        gerialStack.temizle();
        yineleStack.temizle();
    }
    
    // Geri alınabilir işlem var mı
    public boolean gerialYapilabilir() {
        return !gerialStack.bosMu();
    }
    
    // Yinelenebilir işlem var mı
    public boolean yineleYapilabilir() {
        return !yineleStack.bosMu();
    }
    
    // İşlem bilgilerini al
    public IslemBilgisi getIslemBilgisi(Islem islem) {
        if (islem == null) {
            return null;
        }
        return new IslemBilgisi(islem.tur, islem.emlak, islem.eskiEmlak, islem.zaman);
    }
    
    // İşlem bilgisi inner class
    public static class IslemBilgisi {
        public IslemTuru tur;
        public Emlak emlak;
        public Emlak eskiEmlak;
        public long zaman;
        
        public IslemBilgisi(IslemTuru tur, Emlak emlak, Emlak eskiEmlak, long zaman) {
            this.tur = tur;
            this.emlak = emlak;
            this.eskiEmlak = eskiEmlak;
            this.zaman = zaman;
        }
    }
}