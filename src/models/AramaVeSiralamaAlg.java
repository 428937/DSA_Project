import java.util.ArrayList;
import java.util.List;

// Arama ve Sıralama Algoritmaları
public class AramaVeSiralamaAlg {
    
    // === ARAMA ALGORİTMALARI ===
    
    // Doğrusal Arama (Linear Search) - O(n)
    public static <T extends Comparable<T>> int doğrusalAra(T[] dizi, T aranandeger) {
        for (int i = 0; i < dizi.length; i++) {
            if (dizi[i].equals(aranandeger)) {
                return i;
            }
        }
        return -1;
    }
    
    // Doğrusal Arama - Liste versiyonu
    public static <T extends Comparable<T>> int doğrusalAra(List<T> liste, T aranandeger) {
        for (int i = 0; i < liste.size(); i++) {
            if (liste.get(i).equals(aranandeger)) {
                return i;
            }
        }
        return -1;
    }
    
    // İkili Arama (Binary Search) - O(log n) - Sıralanmış dizi gerektirir
    public static <T extends Comparable<T>> int ikiliAra(T[] dizi, T aranandeger) {
        return ikiliAraYardımcı(dizi, aranandeger, 0, dizi.length - 1);
    }
    
    private static <T extends Comparable<T>> int ikiliAraYardımcı(T[] dizi, T aranandeger, int sol, int sag) {
        if (sol > sag) {
            return -1;
        }
        
        int orta = sol + (sag - sol) / 2;
        int karsilastirma = aranandeger.compareTo(dizi[orta]);
        
        if (karsilastirma == 0) {
            return orta;
        } else if (karsilastirma < 0) {
            return ikiliAraYardımcı(dizi, aranandeger, sol, orta - 1);
        } else {
            return ikiliAraYardımcı(dizi, aranandeger, orta + 1, sag);
        }
    }
    
    // ==================== SIRALAMA ALGORİTMALARI ====================
    
    // Kabarcık Sıralaması (Bubble Sort) - O(n²) ortalama
    public static <T extends Comparable<T>> void kabaçikSira(T[] dizi) {
        int n = dizi.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (dizi[j].compareTo(dizi[j + 1]) > 0) {
                    // Swap (Değiş tokuş)
                    T gecici = dizi[j];
                    dizi[j] = dizi[j + 1];
                    dizi[j + 1] = gecici;
                }
            }
        }
    }
    
    // Hızlı Sıralama (Quick Sort) - O(n log n) ortalama, O(n²) kötü durum
    public static <T extends Comparable<T>> void hizliSira(T[] dizi) {
        if (dizi.length == 0) return;
        hizliSiraYardımcı(dizi, 0, dizi.length - 1);
    }
    
    private static <T extends Comparable<T>> void hizliSiraYardımcı(T[] dizi, int sol, int sag) {
        if (sol < sag) {
            int pivotIndeksi = bolum(dizi, sol, sag);
            hizliSiraYardımcı(dizi, sol, pivotIndeksi - 1);
            hizliSiraYardımcı(dizi, pivotIndeksi + 1, sag);
        }
    }
    
    private static <T extends Comparable<T>> int bolum(T[] dizi, int sol, int sag) {
        T pivot = dizi[sag];
        int i = sol - 1;
        
        for (int j = sol; j < sag; j++) {
            if (dizi[j].compareTo(pivot) < 0) {
                i++;
                // Swap
                T gecici = dizi[i];
                dizi[i] = dizi[j];
                dizi[j] = gecici;
            }
        }
        
        // Pivot'u doğru yere koy
        T gecici = dizi[i + 1];
        dizi[i + 1] = dizi[sag];
        dizi[sag] = gecici;
        
        return i + 1;
    }
    
    // Birleştirme Sıralaması (Merge Sort) - O(n log n) her zaman - Stabil
    public static <T extends Comparable<T>> void birlestirmeSira(T[] dizi) {
        if (dizi.length <= 1) return;
        
        @SuppressWarnings("unchecked")
        T[] yardimci = (T[]) new Comparable[dizi.length];
        birlestirmeSiraYardımcı(dizi, 0, dizi.length - 1, yardimci);
    }
    
    private static <T extends Comparable<T>> void birlestirmeSiraYardımcı(T[] dizi, int sol, int sag, T[] yardimci) {
        if (sol < sag) {
            int orta = sol + (sag - sol) / 2;
            birlestirmeSiraYardımcı(dizi, sol, orta, yardimci);
            birlestirmeSiraYardımcı(dizi, orta + 1, sag, yardimci);
            birlestir(dizi, sol, orta, sag, yardimci);
        }
    }
    
    private static <T extends Comparable<T>> void birlestir(T[] dizi, int sol, int orta, int sag, T[] yardimci) {
        int i = sol;
        int j = orta + 1;
        int k = sol;
        
        while (i <= orta && j <= sag) {
            if (dizi[i].compareTo(dizi[j]) <= 0) {
                yardimci[k++] = dizi[i++];
            } else {
                yardimci[k++] = dizi[j++];
            }
        }
        
        while (i <= orta) {
            yardimci[k++] = dizi[i++];
        }
        
        while (j <= sag) {
            yardimci[k++] = dizi[j++];
        }
        
        // Yardımcı dizisinden orijinal diziye kopyala
        for (int x = sol; x <= sag; x++) {
            dizi[x] = yardimci[x];
        }
    }
    
    // Seçim Sıralaması (Selection Sort) - O(n²)
    public static <T extends Comparable<T>> void secimSira(T[] dizi) {
        int n = dizi.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndeksi = i;
            for (int j = i + 1; j < n; j++) {
                if (dizi[j].compareTo(dizi[minIndeksi]) < 0) {
                    minIndeksi = j;
                }
            }
            
            // Swap
            T gecici = dizi[i];
            dizi[i] = dizi[minIndeksi];
            dizi[minIndeksi] = gecici;
        }
    }
    
    // Ekleme Sıralaması (Insertion Sort) - O(n²) ortalama
    public static <T extends Comparable<T>> void eklemeSira(T[] dizi) {
        for (int i = 1; i < dizi.length; i++) {
            T anahtar = dizi[i];
            int j = i - 1;
            
            while (j >= 0 && dizi[j].compareTo(anahtar) > 0) {
                dizi[j + 1] = dizi[j];
                j--;
            }
            
            dizi[j + 1] = anahtar;
        }
    }
    
    // Liste sıralaması (Merge Sort kullanarak)
    public static <T extends Comparable<T>> void listeSira(List<T> liste) {
        if (liste.size() <= 1) return;
        
        @SuppressWarnings("unchecked")
        T[] dizi = (T[]) new Comparable[liste.size()];
        for (int i = 0; i < liste.size(); i++) {
            dizi[i] = liste.get(i);
        }
        
        birlestirmeSira(dizi);
        
        for (int i = 0; i < liste.size(); i++) {
            liste.set(i, dizi[i]);
        }
    }
    
    // Ters sıralama (Descending)
    public static <T extends Comparable<T>> void tersSira(T[] dizi) {
        hizliSira(dizi);
        // Diziyi ters çevir
        for (int i = 0; i < dizi.length / 2; i++) {
            T gecici = dizi[i];
            dizi[i] = dizi[dizi.length - 1 - i];
            dizi[dizi.length - 1 - i] = gecici;
        }
    }
}