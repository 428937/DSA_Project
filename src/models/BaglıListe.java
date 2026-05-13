package models;

import java.io.Serializable;
import java.util.Iterator;

// Bağlı Liste (Linked List) - Emlak nesnelerini depolamak için
public class BaglıListe<T> implements Serializable, Iterable<T> {
    private static final long serialVersionUID = 1L;
    
    // İç sınıf: Düğüm (Node)
    private class Dugum {
        T veri;
        Dugum sonraki;
        
        Dugum(T veri) {
            this.veri = veri;
            this.sonraki = null;
        }
    }
    
    private Dugum bas;
    private int boyut;
    
    // Kurucu metot
    public BaglıListe() {
        this.bas = null;
        this.boyut = 0;
    }
    
    // Listenin başına eleman ekleme - O(1)
    public void basaEkle(T veri) {
        Dugum yeniDugum = new Dugum(veri);
        yeniDugum.sonraki = bas;
        bas = yeniDugum;
        boyut++;
    }
   
    // Listenin sonuna eleman ekleme - O(n)
    public void sonaEkle(T veri) {
        Dugum yeniDugum = new Dugum(veri);
        
        if (bas == null) {// Liste boşsa
            bas = yeniDugum;
        } else {
            Dugum mevcut = bas;
            while (mevcut.sonraki != null) {
                mevcut = mevcut.sonraki;
            }
            mevcut.sonraki = yeniDugum;
        }
        boyut++;
    }
    
    // İndekse göre eleman ekleme - O(n)
    public void indekseEkle(int indeks, T veri) {
        if (indeks < 0 || indeks > boyut) {
            throw new IndexOutOfBoundsException("Geçersiz indeks: " + indeks);
        }
        
        if (indeks == 0) {
            basaEkle(veri);
            return;
        }
        
        Dugum yeniDugum = new Dugum(veri);
        Dugum onceki = bas;
        
        for (int i = 0; i < indeks - 1; i++) {
            onceki = onceki.sonraki;
        }
        
        yeniDugum.sonraki = onceki.sonraki;
        onceki.sonraki = yeniDugum;
        boyut++;
    }
    
    // İndeksten eleman alma - O(n)
    public T al(int indeks) {
        if (indeks < 0 || indeks >= boyut) {
            throw new IndexOutOfBoundsException("Geçersiz indeks: " + indeks);
        }
        
        Dugum mevcut = bas;
        for (int i = 0; i < indeks; i++) {
            mevcut = mevcut.sonraki;
        }
        
        return mevcut.veri;
    }
    
    // İlk elemanı kaldırma - O(1)
    public T ilkiKaldir() {
        if (bas == null) {
            throw new RuntimeException("Liste boş");
        }
        
        T veri = bas.veri;
        bas = bas.sonraki;
        boyut--;
        return veri;
    }
    
    // İndeksten eleman kaldırma - O(n)
    public T sil(int indeks) {
        if (indeks < 0 || indeks >= boyut) {
            throw new IndexOutOfBoundsException("Geçersiz indeks: " + indeks);
        }
        
        T veri;
        if (indeks == 0) {
            veri = bas.veri;
            bas = bas.sonraki;
        } else {
            Dugum onceki = bas;
            for (int i = 0; i < indeks - 1; i++) {
                onceki = onceki.sonraki;
            }
            veri = onceki.sonraki.veri;
            onceki.sonraki = onceki.sonraki.sonraki;
        }
        
        boyut--;
        return veri;
    }
    
    // Belirtilen veriyi silme - O(n)
    public boolean sil(T veri) {
        if (bas == null) return false;
        
        if (bas.veri.equals(veri)) {
            bas = bas.sonraki;
            boyut--;
            return true;
        }
        
        Dugum mevcut = bas;
        while (mevcut.sonraki != null) {
            if (mevcut.sonraki.veri.equals(veri)) {
                mevcut.sonraki = mevcut.sonraki.sonraki;
                boyut--;
                return true;
            }
            mevcut = mevcut.sonraki;
        }
        
        return false;
    }
    
    // Veri içerir mi kontrolü - O(n)
    public boolean içeriyor(T veri) {
        Dugum mevcut = bas;
        while (mevcut != null) {
            if (mevcut.veri.equals(veri)) {
                return true;
            }
            mevcut = mevcut.sonraki;
        }
        return false;
    }
    
    // Listenin boyutu
    public int boyut() {
        return boyut;
    }
    
    // Liste boş mu
    public boolean bosMu() {
        return boyut == 0;
    }
    
    // Listeyi temizle
    public void temizle() {
        bas = null;
        boyut = 0;
    }
    
    // Iterator desteği
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Dugum mevcut = bas;
            
            @Override
            public boolean hasNext() {
                return mevcut != null;
            }
            
            @Override
            public T next() {
                T veri = mevcut.veri;
                mevcut = mevcut.sonraki;
                return veri;
            }
        };
    }
}