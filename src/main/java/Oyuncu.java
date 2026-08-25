import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Oyuncu {

    private int oyuncuID;
    private String oyuncuAdi;
    private int skor;

    private ArrayList<SavasAraclari> kartListesi;

    private HashSet<SavasAraclari> secilenKartlar;

    private ArrayList<Integer> secimler;

    private int kazanilanSeviyePuani;

    private final Random random = new Random();

    public Oyuncu() {
        this(0, "Isimsiz", 0);
    }

    public Oyuncu(int oyuncuID, String oyuncuAdi, int skor) {
        this.oyuncuID = oyuncuID;
        this.oyuncuAdi = oyuncuAdi;
        this.skor = skor;
        this.kartListesi = new ArrayList<>();
        this.secilenKartlar = new HashSet<>();
        this.secimler = new ArrayList<>();
        this.kazanilanSeviyePuani = 0;
    }

    public void kartEkle(SavasAraclari kart) {
        this.kartListesi.add(kart);
    }

    public void setKullaniciSecimleri(List<Integer> secimler) {
        this.secimler = new ArrayList<>(secimler);
    }

    public ArrayList<SavasAraclari> kartSec(int sayi) {
        ArrayList<SavasAraclari> sonuc = new ArrayList<>();
        if (sayi <= 0 || kartListesi.isEmpty()) {
            return sonuc;
        }

        // Elde secilebilir kart kalmadiysa tekrar secim hakki acilir.
        if (kullanilabilirKartlar().size() < sayi) {
            secilenKartlar.clear();
        }

        if (this.oyuncuID == 1) {
            for (Integer index : secimler) {
                if (index != null && index >= 0 && index < kartListesi.size()) {
                    SavasAraclari kart = kartListesi.get(index);
                    if (!sonuc.contains(kart)) {
                        sonuc.add(kart);
                        secilenKartlar.add(kart);
                    }
                }
            }
            secimler.clear();
        } else {
            ArrayList<SavasAraclari> havuz = kullanilabilirKartlar();
            for (int i = 0; i < sayi && !havuz.isEmpty(); i++) {
                SavasAraclari kart = havuz.remove(random.nextInt(havuz.size()));
                sonuc.add(kart);
                secilenKartlar.add(kart);
            }
        }
        return sonuc;
    }

    public ArrayList<SavasAraclari> kullanilabilirKartlar() {
        ArrayList<SavasAraclari> liste = new ArrayList<>();
        for (SavasAraclari kart : kartListesi) {
            if (!secilenKartlar.contains(kart)) {
                liste.add(kart);
            }
        }
        return liste;
    }

    public boolean secilmisMi(SavasAraclari kart) {
        return secilenKartlar.contains(kart);
    }

    public void secimGecmisiniTemizle() {
        secilenKartlar.clear();
    }

    public HashSet<SavasAraclari> getSecilenKartlar() {
        return this.secilenKartlar;
    }

    public String SkorGoster() {
        return oyuncuAdi + " | Skor: " + skor + " | Toplam seviye: " + kazanilanSeviyePuani;
    }

    public int getOyuncuID() {
        return this.oyuncuID;
    }

    public void setOyuncuID(int oyuncuID) {
        this.oyuncuID = oyuncuID;
    }

    public String getOyuncuAdi() {
        return this.oyuncuAdi;
    }

    public void setOyuncuAdi(String oyuncuAdi) {
        this.oyuncuAdi = oyuncuAdi;
    }

    public int getSkor() {
        return this.skor;
    }

    public void setSkor(int skor) {
        this.skor = skor;
    }

    public void skorEkle(int puan) {
        this.skor += puan;
    }

    public int getKazanilanSeviyePuani() {
        return this.kazanilanSeviyePuani;
    }

    public void seviyePuaniEkle(int puan) {
        this.kazanilanSeviyePuani += puan;
    }

    public ArrayList<SavasAraclari> getKartListesi() {
        return this.kartListesi;
    }

    public void kartCikar(SavasAraclari kart) {
        kartListesi.remove(kart);
        secilenKartlar.remove(kart);
    }

    public void kartlariSifirla() {
        this.kartListesi = new ArrayList<>();
        this.secilenKartlar.clear();
        this.secimler.clear();
        this.kazanilanSeviyePuani = 0;
    }

    public int getToplamSeviye() {
        int toplam = 0;
        for (SavasAraclari kart : kartListesi) {
            toplam += kart.getSeviyePuani();
        }
        return toplam;
    }

    public int toplamDayaniklilik() {
        int toplam = 0;
        for (SavasAraclari kart : kartListesi) {
            toplam += Math.max(0, kart.getDayaniklilik());
        }
        return toplam;
    }
}