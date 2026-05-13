package models;

import java.io.Serializable;
import java.util.Iterator;

// Kuyruk (Queue) - FIFO (First In First Out) veri yapısı
public class Kuyruk<T> implements Serializable, Iterable<T> {
    private static final long serialVersionUID = 1L;
    
    // İç sınıf: Düğüm
    private class Dugum {
        T veri;
        Dugum sonraki;
        
        Dugum(T veri) {
            this.veri = veri;
            this.sonraki = null;
        }
    }
    
    private Dugum bas; // Çıkarma işlemi buradan yapılır
    private Dugum son;  // Ekleme işlemi buraya yapılır
    private int boyut;
    
    // Kurucu metot
    public Kuyruk() {
        this.bas = null;
        this.son = null;
        this.boyut = 0;
    }
    
    // Kuyruğa eleman ekleme (enqueue) - O(1)
    public void ekle(T veri) {
        Dugum yeniDugum = new Dugum(veri);
        
        if (bosMu()) {
            bas = yeniDugum;
        } else {
            son.sonraki = yeniDugum;
        }
        
        son = yeniDugum;
        boyut++;
    }
    
    // Kuyruktan eleman çıkarma (dequeue) - O(1)
    public T cikart() {
        if (bosMu()) {
            throw new RuntimeException("Kuyruk boş");
        }
        
        T veri = bas.veri;
        bas = bas.sonraki;
        boyut--;
        
        // Kuyruk boşaldıysa son da null olmalı
        if (bosMu()) {
            son = null;
        }
        
        return veri;
    }
    
    // Kuyrukta en önde olan elemanı görme (peek) - O(1)
    public T bas() {
        if (bosMu()) {
            throw new RuntimeException("Kuyruk boş");
        }
        return bas.veri;
    }
    
    // Kuyruk boş mu kontrolü
    public boolean bosMu() {
        return boyut == 0;
    }
    
    // Kuyruğun boyutu
    public int boyut() {
        return boyut;
    }
    
    // Kuyruğu temizle
    public void temizle() {
        bas = null;
        son = null;
        boyut = 0;
    }
    
    // Kuyrukta veri var mı kontrolü - O(n)
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