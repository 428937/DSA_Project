package yonetici;

import models.*;
import java.util.ArrayList;
import java.util.List;

// Emlak Yönetimi - Tüm işlemleri koordine eden merkez sınıf
public class EmlakYoneticisi {
    private BaglıListe<Emlak> tumEmlaklar;
    private Kuyruk<String> sonIslemler; // Son 10 işlemi takip et
    private GeriyeAlYineleSistemi geriyeAlYinele;
    private IkiliAramaAgaci<Emlak> agac; // Hızlı arama için
    private int sonId;
    private static final int MAX_SON_ISLEM = 10;
    
    // Kurucu metot
    public EmlakYoneticisi() {
        this.tumEmlaklar = new BaglıListe<>();
        this.sonIslemler = new Kuyruk<>();
        this.geriyeAlYinele = new GeriyeAlYineleSistemi();
        this.agac = new IkiliAramaAgaci<>();
        this.sonId = 0;
        
        // Önceki verileri yükle
        yukle();
    }
    
    // Emlak ekle
    public boolean emlakEkle(String ad, String konum, double fiyat, double alan, String tip, String aciklama) {
        if (ad == null || ad.trim().isEmpty() || konum == null || konum.trim().isEmpty()) {
            return false;
        }
        
        if (fiyat <= 0 || alan <= 0) {
            return false;
        }
        
        sonId++;
        Emlak emlak = new Emlak(sonId, ad, konum, fiyat, alan, tip, aciklama);
        tumEmlaklar.sonaEkle(emlak);
        agac.ekle(emlak);
        
        geriyeAlYinele.kayitEkle(GeriyeAlYineleSistemi.IslemTuru.EKLE, emlak);
        islemEkle("Yeni emlak eklendi: " + ad);
        
        kaydet();
        return true;
    }
    
    // Emlak sil
    public boolean emlakSil(int id) {
        Emlak emlak = emlakBul(id);
        if (emlak == null) {
            return false;
        }
        
        tumEmlaklar.sil(emlak);
        agac.sil(emlak);
        
        geriyeAlYinele.kayitEkle(GeriyeAlYineleSistemi.IslemTuru.SIL, emlak);
        islemEkle("Emlak silindi: " + emlak.getAd());
        
        kaydet();
        return true;
    }
    
    // Emlak güncelle
    public boolean emlakGuncelle(int id, String ad, String konum, double fiyat, double alan, String tip, String aciklama) {
        Emlak eskiEmlak = emlakBul(id);
        if (eskiEmlak == null) {
            return false;
        }
        
        // Ağaçtan eski verileri kaldır
        agac.sil(eskiEmlak);
        
        // Eski değerleri sakla (undo için)
        Emlak yedek = new Emlak(
            eskiEmlak.getId(),
            eskiEmlak.getAd(),
            eskiEmlak.getKonum(),
            eskiEmlak.getFiyat(),
            eskiEmlak.getAlan(),
            eskiEmlak.getTip(),
            eskiEmlak.getAciklama()
        );
        yedek.setFavorilerde(eskiEmlak.isFavosilerde());
        yedek.setEklemeTarihi(eskiEmlak.getEklemeTarihi());
        
        // Yeni değerleri ayarla
        eskiEmlak.setAd(ad);
        eskiEmlak.setKonum(konum);
        eskiEmlak.setFiyat(fiyat);
        eskiEmlak.setAlan(alan);
        eskiEmlak.setTip(tip);
        eskiEmlak.setAciklama(aciklama);
        
        // Ağaca yeni verileri ekle
        agac.ekle(eskiEmlak);
        
        geriyeAlYinele.guncellemeKayitEkle(yedek, eskiEmlak);
        islemEkle("Emlak güncellendi: " + ad);
        
        kaydet();
        return true;
    }
    
    // ID'ye göre emlak bul - O(n)
    public Emlak emlakBul(int id) {
        for (Emlak emlak : tumEmlaklar) {
            if (emlak.getId() == id) {
                return emlak;
            }
        }
        return null;
    }
    
    // Ada göre emlak ara (Doğrusal Arama) - O(n)
    public List<Emlak> adaGoreAra(String ad) {
        List<Emlak> sonuclar = new ArrayList<>();
        String adKucuk = ad.toLowerCase();
        
        for (Emlak emlak : tumEmlaklar) {
            if (emlak.getAd().toLowerCase().contains(adKucuk)) {
                sonuclar.add(emlak);
            }
        }
        
        return sonuclar;
    }
    
    // Konuma göre ara
    public List<Emlak> konumaGoreAra(String konum) {
        List<Emlak> sonuclar = new ArrayList<>();
        String konumKucuk = konum.toLowerCase();
        
        for (Emlak emlak : tumEmlaklar) {
            if (emlak.getKonum().toLowerCase().contains(konumKucuk)) {
                sonuclar.add(emlak);
            }
        }
        
        return sonuclar;
    }
    
    // Fiyat aralığında ara
    public List<Emlak> fiyatAraligindaAra(double minFiyat, double maxFiyat) {
        List<Emlak> sonuclar = new ArrayList<>();
        
        for (Emlak emlak : tumEmlaklar) {
            if (emlak.getFiyat() >= minFiyat && emlak.getFiyat() <= maxFiyat) {
                sonuclar.add(emlak);
            }
        }
        
        return sonuclar;
    }
    
    // Alan aralığında ara
    public List<Emlak> alanAraligindaAra(double minAlan, double maxAlan) {
        List<Emlak> sonuclar = new ArrayList<>();
        
        for (Emlak emlak : tumEmlaklar) {
            if (emlak.getAlan() >= minAlan && emlak.getAlan() <= maxAlan) {
                sonuclar.add(emlak);
            }
        }
        
        return sonuclar;
    }
    
    // Tipe göre filtrele
    public List<Emlak> tipeGoreFiltrele(String tip) {
        List<Emlak> sonuclar = new ArrayList<>();
        
        for (Emlak emlak : tumEmlaklar) {
            if (emlak.getTip().equalsIgnoreCase(tip)) {
                sonuclar.add(emlak);
            }
        }
        
        return sonuclar;
    }
    
    // Fiyata göre sırala (Artan)
    public List<Emlak> fiyataSiraArtanHali() {
        List<Emlak> liste = new ArrayList<>();
        for (Emlak emlak : tumEmlaklar) {
            liste.add(emlak);
        }
        
        AramaVeSiralamaAlg.listeSira(liste);
        return liste;
    }
    
    // Fiyata göre sırala (Azalan)
    public List<Emlak> fiyataSiraAzalanHali() {
        List<Emlak> liste = fiyataSiraArtanHali();
        
        // Ters çevir
        for (int i = 0; i < liste.size() / 2; i++) {
            Emlak temp = liste.get(i);
            liste.set(i, liste.get(liste.size() - 1 - i));
            liste.set(liste.size() - 1 - i, temp);
        }
        
        return liste;
    }
    
    // Alan'a göre sırala (Artan)
    public List<Emlak> alanaGoreSiraArtanHali() {
        List<Emlak> liste = new ArrayList<>();
        for (Emlak emlak : tumEmlaklar) {
            liste.add(emlak);
        }
        
        // Alan'a göre manuel sıralama (Bubble Sort)
        for (int i = 0; i < liste.size() - 1; i++) {
            for (int j = 0; j < liste.size() - i - 1; j++) {
                if (liste.get(j).getAlan() > liste.get(j + 1).getAlan()) {
                    Emlak temp = liste.get(j);
                    liste.set(j, liste.get(j + 1));
                    liste.set(j + 1, temp);
                }
            }
        }
        
        return liste;
    }
    
    // Fiyat/Alan oranına göre sırala
    public List<Emlak> fiyatAlanOraninaGoreSirala() {
        List<Emlak> liste = new ArrayList<>();
        for (Emlak emlak : tumEmlaklar) {
            liste.add(emlak);
        }
        
        // Orana göre sıralama
        for (int i = 0; i < liste.size() - 1; i++) {
            for (int j = 0; j < liste.size() - i - 1; j++) {
                if (liste.get(j).getFiyatAlanOrani() > liste.get(j + 1).getFiyatAlanOrani()) {
                    Emlak temp = liste.get(j);
                    liste.set(j, liste.get(j + 1));
                    liste.set(j + 1, temp);
                }
            }
        }
        
        return liste;
    }
    
    // Favorilere ekle
    public void favoriEkle(int id) {
        Emlak emlak = emlakBul(id);
        if (emlak != null && !emlak.isFavosilerde()) {
            emlak.setFavorilerde(true);
            geriyeAlYinele.kayitEkle(GeriyeAlYineleSistemi.IslemTuru.FAVORI_EKLE, emlak);
            islemEkle("Favorilere eklendi: " + emlak.getAd());
            kaydet();
        }
    }
    
    // Favorilerden çıkar
    public void favoriKaldir(int id) {
        Emlak emlak = emlakBul(id);
        if (emlak != null && emlak.isFavosilerde()) {
            emlak.setFavorilerde(false);
            geriyeAlYinele.kayitEkle(GeriyeAlYineleSistemi.IslemTuru.FAVORI_KALDIR, emlak);
            islemEkle("Favorilerden çıkarıldı: " + emlak.getAd());
            kaydet();
        }
    }
    
    // Favorileri getir
    public List<Emlak> favorileriGetir() {
        List<Emlak> favoriler = new ArrayList<>();
        for (Emlak emlak : tumEmlaklar) {
            if (emlak.isFavosilerde()) {
                favoriler.add(emlak);
            }
        }
        return favoriler;
    }
    
    // Geri al
    public boolean gerialYap() {
        GeriyeAlYineleSistemi.IslemBilgisi bilgi = geriyeAlYinele.getIslemBilgisi(geriyeAlYinele.gerialYapılabilirMi());
        
        if (bilgi == null) {
            return false;
        }
        
        switch (bilgi.tur) {
            case EKLE:
                tumEmlaklar.sil(bilgi.emlak);
                agac.sil(bilgi.emlak);
                break;
            case SIL:
                tumEmlaklar.sonaEkle(bilgi.emlak);
                agac.ekle(bilgi.emlak);
                break;
            case GUNCELLE:
                agac.sil(bilgi.emlak);
                tumEmlaklar.sil(bilgi.emlak);
                tumEmlaklar.sonaEkle(bilgi.eskiEmlak);
                agac.ekle(bilgi.eskiEmlak);
                break;
            case FAVORI_EKLE:
                bilgi.emlak.setFavorilerde(false);
                break;
            case FAVORI_KALDIR:
                bilgi.emlak.setFavorilerde(true);
                break;
        }
        
        geriyeAlYinele.gerialYap();
        islemEkle("Geri alındı: " + bilgi.tur.getAciklama());
        kaydet();
        return true;
    }
    
    // Yinele
    public boolean yineleYap() {
        GeriyeAlYineleSistemi.IslemBilgisi bilgi = geriyeAlYinele.getIslemBilgisi(geriyeAlYinele.yineleYapılabilirMi());
        
        if (bilgi == null) {
            return false;
        }
        
        switch (bilgi.tur) {
            case EKLE:
                tumEmlaklar.sonaEkle(bilgi.emlak);
                agac.ekle(bilgi.emlak);
                break;
            case SIL:
                tumEmlaklar.sil(bilgi.emlak);
                agac.sil(bilgi.emlak);
                break;
            case GUNCELLE:
                agac.sil(bilgi.eskiEmlak);
                tumEmlaklar.sil(bilgi.eskiEmlak);
                tumEmlaklar.sonaEkle(bilgi.emlak);
                agac.ekle(bilgi.emlak);
                break;
            case FAVORI_EKLE:
                bilgi.emlak.setFavorilerde(true);
                break;
            case FAVORI_KALDIR:
                bilgi.emlak.setFavorilerde(false);
                break;
        }
        
        geriyeAlYinele.yineleYap();
        islemEkle("Yinelendi: " + bilgi.tur.getAciklama());
        kaydet();
        return true;
    }
    
    // Geri al yapılabilir mi
    public boolean gerialYapilabilir() {
        return geriyeAlYinele.gerialYapilabilir();
    }
    
    // Yinele yapılabilir mi
    public boolean yineleYapilabilir() {
        return geriyeAlYinele.yineleYapilabilir();
    }
    
    // Son işlem açıklaması
    public String sonIslemAciklama() {
        return geriyeAlYinele.sonIslemAciklama();
    }
    
    // Geri alınacak işlem açıklaması
    public String geriAlacakIslemAciklama() {
        return geriyeAlYinele.geriAlacakIslemAciklama();
    }
    
    // Tüm emlakları getir
    public BaglıListe<Emlak> tumEmlaklar() {
        return tumEmlaklar;
    }
    
    // Emlak sayısı
    public int emlakSayisi() {
        return tumEmlaklar.boyut();
    }
    
    // Tüm tip çeşitlerini getir
    public List<String> tumTipCesitleri() {
        List<String> tipler = new ArrayList<>();
        for (Emlak emlak : tumEmlaklar) {
            if (!tipler.contains(emlak.getTip())) {
                tipler.add(emlak.getTip());
            }
        }
        return tipler;
    }
    
    // Tüm konumları getir
    public List<String> tumKonumlar() {
        List<String> konumlar = new ArrayList<>();
        for (Emlak emlak : tumEmlaklar) {
            if (!konumlar.contains(emlak.getKonum())) {
                konumlar.add(emlak.getKonum());
            }
        }
        return konumlar;
    }
    
    // İstatistikleri getir
    public Map getIstatistikler() {
        Map istatistikler = new Map();
        
        if (tumEmlaklar.boyut() == 0) {
            return istatistikler;
        }
        
        double toplamFiyat = 0;
        double toplamAlan = 0;
        double minFiyat = Double.MAX_VALUE;
        double maxFiyat = 0;
        double minAlan = Double.MAX_VALUE;
        double maxAlan = 0;
        int favoriSayisi = 0;
        
        for (Emlak emlak : tumEmlaklar) {
            toplamFiyat += emlak.getFiyat();
            toplamAlan += emlak.getAlan();
            minFiyat = Math.min(minFiyat, emlak.getFiyat());
            maxFiyat = Math.max(maxFiyat, emlak.getFiyat());
            minAlan = Math.min(minAlan, emlak.getAlan());
            maxAlan = Math.max(maxAlan, emlak.getAlan());
            if (emlak.isFavosilerde()) {
                favoriSayisi++;
            }
        }
        
        istatistikler.put("toplam_emlak", tumEmlaklar.boyut());
        istatistikler.put("ortalama_fiyat", toplamFiyat / tumEmlaklar.boyut());
        istatistikler.put("ortalama_alan", toplamAlan / tumEmlaklar.boyut());
        istatistikler.put("min_fiyat", minFiyat);
        istatistikler.put("max_fiyat", maxFiyat);
        istatistikler.put("min_alan", minAlan);
        istatistikler.put("max_alan", maxAlan);
        istatistikler.put("favori_sayisi", favoriSayisi);
        
        return istatistikler;
    }
    
    // İşlem ekle (Kuyruk kullanarak son işlemleri takip et)
    private void islemEkle(String islem) {
        if (sonIslemler.boyut() >= MAX_SON_ISLEM) {
            sonIslemler.cikart();
        }
        sonIslemler.ekle(islem);
    }
    
    // Son işlemleri getir
    public List<String> sonIslemler() {
        List<String> islemler = new ArrayList<>();
        for (String islem : sonIslemler) {
            islemler.add(0, islem); // Tersten ekle (en son en başta)
        }
        return islemler;
    }
    
    // Veri yükle
    private void yukle() {
        tumEmlaklar = VeriYukleme.yükle();
        
        // Ağacı yeniden kur
        for (Emlak emlak : tumEmlaklar) {
            agac.ekle(emlak);
            if (emlak.getId() > sonId) {
                sonId = emlak.getId();
            }
        }
    }
    
    // Veri kaydet
    private void kaydet() {
        VeriYukleme.kaydet(tumEmlaklar);
    }
    
    // Tüm verileri temizle
    public void hepsiTemizle() {
        tumEmlaklar.temizle();
        agac.temizle();
        geriyeAlYinele.temizle();
        sonId = 0;
        kaydet();
    }
    
    // Simple Map sınıfı (HashMap alternatifi)
    public static class Map {
        private List<String> anahtarlar = new ArrayList<>();
        private List<Object> degerler = new ArrayList<>();
        
        public void put(String anahtar, Object deger) {
            int indeks = anahtarlar.indexOf(anahtar);
            if (indeks >= 0) {
                degerler.set(indeks, deger);
            } else {
                anahtarlar.add(anahtar);
                degerler.add(deger);
            }
        }
        
        public Object get(String anahtar) {
            int indeks = anahtarlar.indexOf(anahtar);
            if (indeks >= 0) {
                return degerler.get(indeks);
            }
            return null;
        }
    }
}