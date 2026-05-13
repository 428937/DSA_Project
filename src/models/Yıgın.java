package models;

import java.io.Serializable;
import java.util.Iterator;

// Yıgın (Stack) - LIFO (Last In First Out) veri yapısı
public class Yıgın<T> implements Serializable, Iterable<T> {
    private static final long serialVersionUID = 1L;
    
    // İç sınıf: Düğüm
    private class Dugum {
        T veri;
        Dugum onceki;
        
        Dugum(T veri) {
            this.veri = veri;
            this.onceki = null;
        }
    }
    
    private Dugum tepe; // Yığının tepesi (en üst eleman)
    private int boyut;
    
    // Kurucu metot
    public Yıgın() {
        this.tepe = null;
        this.boyut = 0;
    }
    
    // Yığına eleman ekleme (push) - O(1)
    public void ekle(T veri) {
        Dugum yeniDugum = new Dugum(veri);
        yeniDugum.onceki = tepe;
        tepe = yeniDugum;
        boyut++;
    }
    
    // Yığından eleman çıkarma (pop) - O(1)
    public T cikart() {
        if (bosMu()) {
            throw new RuntimeException("Yıgın boş");
        }
        
        T veri = tepe.veri;
        tepe = tepe.onceki;
        boyut--;
        return veri;
    }
    
    // Yığının en üstündeki elemanı görme (peek) - O(1)
    public T tepe() {
        if (bosMu()) {
            throw new RuntimeException("Yıgın boş");
        }
        return tepe.veri;
    }
    
    // Yıgın boş mu kontrolü
    public boolean bosMu() {
         mı kontrolü - O(n)
    public boolean içeriyor(T veri) {
        Dugum mevcut = tepe;
        while (mevcut != null) {
            if (mevcut.veri.equals(veri)) {
                return true;
            }
            mevcut = mevcut.onceki;
        }
        return false;
    }
    
    // Yıgını kopyala
    public Yıgın<T> kopyala() {
        Yıgın<T> kopya = new Yıgın<>();
        Yıgın<T> gecici = new Yıgın<>();
        
        // Ters sırada al
        while (!this.bosMu()) {
            T veri = this.cikart();
            gecici.ekle(veri);
        }
        
        // Orijinal yıgını geri doldur
        while (!gecici.bosMu()) {
            T veri = gecici.cikart();
            this.ekle(veri);
            kopya.ekle(veri);
        }
        
        return kopya;
    }
    
    // Iterator desteği - tepeden başlayarak
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Dugum mevcut = tepe;
            
            @Override
            public boolean hasNext() {
                return mevcut != null;
            }
            
            @Override
            public T next() {
                T veri = mevcut.veri;
                mevcut = mevcut.onceki;
                return veri;
            }
        };
    }
}