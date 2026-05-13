package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// İkili Arama Ağacı (Binary Search Tree) - Sıralı veri depolama
public class IkiliAramaAgaci<T extends Comparable<T>> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // İç sınıf: Ağaç Düğümü
    private class AracDugum {
        T veri;
        AracDugum sol;
        AracDugum sag;
        
        AracDugum(T veri) {
            this.veri = veri;
            this.sol = null;
            this.sag = null;
        }
    }
    
    private AracDugum kok;
    private int boyut;
    
    // Kurucu metot
    public IkiliAramaAgaci() {
        this.kok = null;
        this.boyut = 0;
    }
    
    // Ağaca eleman ekleme - O(log n) ortalama, O(n) kötü durum
    public void ekle(T veri) {
        kok = ekleYardımcı(kok, veri);
    }
    
    private AracDugum ekleYardımcı(AracDugum dugum, T veri) {
        if (dugum == null) {
            boyut++;
            return new AracDugum(veri);
        }
        
        int karsilastirma = veri.compareTo(dugum.veri);
        
        if (karsilastirma < 0) {
            dugum.sol = ekleYardımcı(dugum.sol, veri);
        } else if (karsilastirma > 0) {
            dugum.sag = ekleYardımcı(dugum.sag, veri);
        }
        // Eşit değerler eklenmez (duplikat önlemek)
        
        return dugum;
    }
    
    // Ağaçta arama - O(log n) ortalama
    public boolean ara(T veri) {
        return araYardımcı(kok, veri);
    }
    
    private boolean araYardımcı(AracDugum dugum, T veri) {
        if (dugum == null) {
            return false;
        }
        
        int karsilastirma = veri.compareTo(dugum.veri);
        
        if (karsilastirma < 0) {
            return araYardımcı(dugum.sol, veri);
        } else if (karsilastirma > 0) {
            return araYardımcı(dugum.sag, veri);
        } else {
            return true;
        }
    }
    
    // Minimum değeri bul - O(log n)
    public T enKucuk() {
        if (kok == null) {
            throw new RuntimeException("Ağaç boş");
        }
        return enKucukYardımcı(kok).veri;
    }
    
    private AracDugum enKucukYardımcı(AracDugum dugum) {
        if (dugum.sol == null) {
            return dugum;
        }
        return enKucukYardımcı(dugum.sol);
    }
    
    // Maksimum değeri bul - O(log n)
    public T enBuyuk() {
        if (kok == null) {
            throw new RuntimeException("Ağaç boş");
        }
        return enBuyukYardımcı(kok).veri;
    }
    
    private AracDugum enBuyukYardımcı(AracDugum dugum) {
        if (dugum.sag == null) {
            return dugum;
        }
        return enBuyukYardımcı(dugum.sag);
    }
    
    // Ağaçtan eleman silme - O(log n) ortalama
    public boolean sil(T veri) {
        int oncekiBoyut = boyut;
        kok = silYardımcı(kok, veri);
        return boyut < oncekiBoyut;
    }
    
    private AracDugum silYardımcı(AracDugum dugum, T veri) {
        if (dugum == null) {
            return null;
        }
        
        int karsilastirma = veri.compareTo(dugum.veri);
        
        if (karsilastirma < 0) {
            dugum.sol = silYardımcı(dugum.sol, veri);
        } else if (karsilastirma > 0) {
            dugum.sag = silYardımcı(dugum.sag, veri);
        } else {
            // Silinecek düğüm bulundu
            boyut--;
            
            // Yaprak düğüm
            if (dugum.sol == null && dugum.sag == null) {
                return null;
            }
            
            // Bir çocuğu var
            if (dugum.sol == null) {
                return dugum.sag;
            }
            if (dugum.sag == null) {
                return dugum.sol;
            }
            
            // İki çocuğu var - ön-düzen halefi bul
            AracDugum halefi = enKucukYardımcı(dugum.sag);
            dugum.veri = halefi.veri;
            dugum.sag = silYardımcı(dugum.sag, halefi.veri);
            boyut++; // Yanlışlıkla düşürülen boyutu geri al
        }
        
        return dugum;
    }
    
    // In-order gezme (sıralı) - O(n)
    public List<T> inOrder() {
        List<T> liste = new ArrayList<>();
        inOrderYardımcı(kok, liste);
        return liste;
    }
    
    private void inOrderYardımcı(AracDugum dugum, List<T> liste) {
        if (dugum != null) {
            inOrderYardımcı(dugum.sol, liste);
            liste.add(dugum.veri);
            inOrderYardımcı(dugum.sag, liste);
        }
    }
    
    // Pre-order gezme - O(n)
    public List<T> preOrder() {
        List<T> liste = new ArrayList<>();
        preOrderYardımcı(kok, liste);
        return liste;
    }
    
    private void preOrderYardımcı(AracDugum dugum, List<T> liste) {
        if (dugum != null) {
            liste.add(dugum.veri);
            preOrderYardımcı(dugum.sol, liste);
            preOrderYardımcı(dugum.sag, liste);
        }
    }
    
    // Post-order gezme - O(n)
    public List<T> postOrder() {
        List<T> liste = new ArrayList<>();
        postOrderYardımcı(kok, liste);
        return liste;
    }
    
    private void postOrderYardımcı(AracDugum dugum, List<T> liste) {
        if (dugum != null) {
            postOrderYardımcı(dugum.sol, liste);
            postOrderYardımcı(dugum.sag, liste);
            liste.add(dugum.veri);
        }
    }
    
    // Ağacın yüksekliği - O(n)
    public int yukseklik() {
        return yukseklikYardımcı(kok);
    }
    
    private int yukseklikYardımcı(AracDugum dugum) {
        if (dugum == null) {
            return 0;
        }
        return 1 + Math.max(yukseklikYardımcı(dugum.sol), yukseklikYardımcı(dugum.sag));
    }
    
    // Ağaçta aralık araması - O(n)
    public List<T> aralikAra(T min, T max) {
        List<T> sonuc = new ArrayList<>();
        aralikAraYardımcı(kok, min, max, sonuc);
        return sonuc;
    }
    
    private void aralikAraYardımcı(AracDugum dugum, T min, T max, List<T> sonuc) {
        if (dugum == null) {
            return;
        }
        
        if (dugum.veri.compareTo(min) > 0) {
            aralikAraYardımcı(dugum.sol, min, max, sonuc);
        }
        
        if (dugum.veri.compareTo(min) >= 0 && dugum.veri.compareTo(max) <= 0) {
            sonuc.add(dugum.veri);
        }
        
        if (dugum.veri.compareTo(max) < 0) {
            aralikAraYardımcı(dugum.sag, min, max, sonuc);
        }
    }
    
    // Ağaç boş mu
    public boolean bosMu() {
        return kok == null;
    }
    
    // Ağacın boyutu
    public int boyut() {
        return boyut;
    }
    
    // Ağacı temizle
    public void temizle() {
        kok = null;
        boyut = 0;
    }
}