package models;

import java.io.Serializable;
import java.util.Objects;

// Emlak (Property) modeli - Temel veri sınıfı
public class Emlak implements Serializable, Comparable<Emlak> {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String ad;
    private String konum;
    private double fiyat;
    private double alan; // m²
    private String tip; // Daire, Villa, Ev, vb.
    private String aciklama;
    private boolean favorilerde;
    private long eklemeTarihi;
    
    // Kurucu metot
    public Emlak(int id, String ad, String konum, double fiyat, double alan, String tip, String aciklama) {
        this.id = id;
        this.ad = ad;
        this.konum = konum;
        this.fiyat = fiyat;
        this.alan = alan;
        this.tip = tip;
        this.aciklama = aciklama;
        this.favorilerde = false;
        this.eklemeTarihi = System.currentTimeMillis();
    }
    
    // Getters ve Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getAd() {
        return ad;
    }
    
    public void setAd(String ad) {
        this.ad = ad;
    }
    
    public String getKonum() {
        return konum;
    }
    
    public void setKonum(String konum) {
        this.konum = konum;
    }
    
    public double getFiyat() {
        return fiyat;
    }
    
    public void setFiyat(double fiyat) {
        this.fiyat = fiyat;
    }
    
    public double getAlan() {
        return alan;
    }
    
    public void setAlan(double alan) {
        this.alan = alan;
    }
    
    public String getTip() {
        return tip;
    }
    
    public void setTip(String tip) {
        this.tip = tip;
    }
    
    public String getAciklama() {
        return aciklama;
    }
    
    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }
    
    public boolean isFavosilerde() {
        return favorilerde;
    }
    
    public void setFavorilerde(boolean favorilerde) {
        this.favorilerde = favorilerde;
    }
    
    public long getEklemeTarihi() {
        return eklemeTarihi;
    }
    
    public void setEklemeTarihi(long eklemeTarihi) {
        this.eklemeTarihi = eklemeTarihi;
    }
    
    // Fiyat/Alan oranını hesapla (analiz için)
    public double getFiyatAlanOrani() {
        return alan > 0 ? fiyat / alan : 0;
    }
    
    // Comparable arayüzü - Fiyata göre karşılaştırma
    @Override
    public int compareTo(Emlak diger) {
        return Double.compare(this.fiyat, diger.fiyat);
    }
    
    // Eşitlik kontrolü
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Emlak emlak = (Emlak) o;
        return id == emlak.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    // String gösterimi
    @Override
    public String toString() {
        return String.format("%s - %s (₺%.0f, %d m²)", ad, konum, fiyat, (int)alan);
    }
}