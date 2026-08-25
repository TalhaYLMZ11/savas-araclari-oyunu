import javafx.application.Application;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Oyun {

    public static final int SEVIYE_ESIGI = 20;

    public static final int MIN_SEVIYE_ARTISI = 10;

    private static final int BASLANGIC_KART_SAYISI = 6;
    private static final int HAMLE_BASINA_KART = 3;

    private final Oyuncu oyuncu;
    private final Oyuncu bilgisayar;

    private final int maksimumHamle;
    private final int baslangicSeviyePuani;

    private int hamle = 1;
    private boolean oyunBitti = false;
    private String sonucMesaji = "";

    private final Random random = new Random();
    private final List<String> kayit = new ArrayList<>();

    public Oyun(String oyuncuAdi) {
        this(oyuncuAdi, 5, 0);
    }

    public Oyun(String oyuncuAdi, int maksimumHamle, int baslangicSeviyePuani) {
        this.maksimumHamle = Math.max(1, maksimumHamle);
        this.baslangicSeviyePuani = Math.max(0, baslangicSeviyePuani);

        this.oyuncu = new Oyuncu(1, oyuncuAdi, 0);
        this.bilgisayar = new Oyuncu(2, "Bilgisayar", 0);

        for (int i = 0; i < BASLANGIC_KART_SAYISI; i++) {
            oyuncu.kartEkle(baslangicKartiUret());
            bilgisayar.kartEkle(baslangicKartiUret());
        }

        yaz("=== OYUN BASLADI ===");
        yaz("Maksimum hamle: " + this.maksimumHamle
                + " | Baslangic seviye puani: " + this.baslangicSeviyePuani);
        yaz(oyuncu.getOyuncuAdi() + " eli: " + kartlariYaz(oyuncu.getKartListesi()));
        yaz("Bilgisayar eli: " + kartlariYaz(bilgisayar.getKartListesi()));
    }

    public static int[] SaldiriHesapla(SavasAraclari birinci, SavasAraclari ikinci) {
        return new int[]{
                saldiriDegeri(birinci, ikinci),
                saldiriDegeri(ikinci, birinci)
        };
    }

    private static int saldiriDegeri(SavasAraclari saldiran, SavasAraclari hedef) {
        int deger = saldiran.getVurus();

        if (saldiran instanceof Siha && hedef instanceof Deniz) {
            deger += ((Siha) saldiran).getDenizVurusAvantaji();
        } else if (saldiran instanceof KFS && hedef instanceof Hava) {
            deger += ((KFS) saldiran).getHavaVurusAvantaji();
        } else if (saldiran instanceof Sida && hedef instanceof Kara) {
            deger += ((Sida) saldiran).getKaraVurusAvantaji();
        } else if (saldiran instanceof Hava && hedef instanceof Kara) {
            deger += ((Hava) saldiran).getKaraVurusAvantaji();
        } else if (saldiran instanceof Kara && hedef instanceof Deniz) {
            deger += ((Kara) saldiran).getDenizVurusAvantaji();
        } else if (saldiran instanceof Deniz && hedef instanceof Hava) {
            deger += ((Deniz) saldiran).getHavaVurusAvantaji();
        }

        return deger;
    }

    public int gerekliKartSayisi() {
        int mevcut = Math.min(oyuncu.getKartListesi().size(), bilgisayar.getKartListesi().size());
        return Math.min(HAMLE_BASINA_KART, mevcut);
    }


    public HamleSonucu hamleYap(List<Integer> oyuncuSecimIndeksleri) {
        HamleSonucu sonuc = new HamleSonucu(hamle);
        if (oyunBitti) {
            return sonuc;
        }

        int adet = gerekliKartSayisi();
        oyuncu.setKullaniciSecimleri(oyuncuSecimIndeksleri);

        List<SavasAraclari> oyuncuKartlari = oyuncu.kartSec(adet);
        List<SavasAraclari> bilgisayarKartlari = bilgisayar.kartSec(adet);

        sonuc.setOyuncuSecimi(oyuncuKartlari);
        sonuc.setBilgisayarSecimi(bilgisayarKartlari);

        yaz("");
        yaz("--- HAMLE " + hamle + " ---");
        yaz(oyuncu.getOyuncuAdi() + " secimi : " + kartlariYaz(oyuncuKartlari));
        yaz("Bilgisayar secimi: " + kartlariYaz(bilgisayarKartlari));

        int eslesme = Math.min(oyuncuKartlari.size(), bilgisayarKartlari.size());
        for (int i = 0; i < eslesme; i++) {
            SavasAraclari ok = oyuncuKartlari.get(i);
            SavasAraclari bk = bilgisayarKartlari.get(i);

            int[] saldirilar = SaldiriHesapla(ok, bk);
            int oyuncuSaldirisi = saldirilar[0];
            int bilgisayarSaldirisi = saldirilar[1];

            bk.durumGuncelle(oyuncuSaldirisi, 0);
            ok.durumGuncelle(bilgisayarSaldirisi, 0);

            Carpisma carpisma = new Carpisma(ok, bk, oyuncuSaldirisi, bilgisayarSaldirisi);
            yaz(String.format("%s (%d hasar) <-> %s (%d hasar)",
                    ok.getKartAdi(), oyuncuSaldirisi, bk.getKartAdi(), bilgisayarSaldirisi));

            if (bk.getDayaniklilik() <= 0) {
                int artis = seviyeArtisi(bk);
                ok.durumGuncelle(0, artis);
                oyuncu.seviyePuaniEkle(artis);
                oyuncu.skorEkle(artis);
                bilgisayar.kartCikar(bk);
                carpisma.setBilgisayarKartiElendi(true);
                carpisma.setOyuncuKazanci(artis);
                yaz("  > " + bk.getKartAdi() + " elendi. "
                        + oyuncu.getOyuncuAdi() + " +" + artis + " seviye/skor");
            }

            if (ok.getDayaniklilik() <= 0) {
                int artis = seviyeArtisi(ok);
                bk.durumGuncelle(0, artis);
                bilgisayar.seviyePuaniEkle(artis);
                bilgisayar.skorEkle(artis);
                oyuncu.kartCikar(ok);
                carpisma.setOyuncuKartiElendi(true);
                carpisma.setBilgisayarKazanci(artis);
                yaz("  > " + ok.getKartAdi() + " elendi. Bilgisayar +" + artis + " seviye/skor");
            }

            yaz("  " + ok.kartPuaniGoster());
            yaz("  " + bk.kartPuaniGoster());
            sonuc.carpismaEkle(carpisma);
        }

        yaz(oyuncu.SkorGoster());
        yaz(bilgisayar.SkorGoster());

        if (oyuncu.getKartListesi().isEmpty() || bilgisayar.getKartListesi().isEmpty()) {
            oyunuBitir(sonuc);
            return sonuc;
        }

        if (hamle >= maksimumHamle) {
            oyunuBitir(sonuc);
            return sonuc;
        }

        kartDagit(sonuc);
        hamle++;
        return sonuc;
    }

    private int seviyeArtisi(SavasAraclari elenenKart) {
        return Math.max(MIN_SEVIYE_ARTISI, elenenKart.getSeviyePuani());
    }


    private void kartDagit(HamleSonucu sonuc) {
        for (Oyuncu taraf : new Oyuncu[]{oyuncu, bilgisayar}) {
            int verilecek = (taraf.getKartListesi().size() == 1) ? 2 : 1;
            for (int i = 0; i < verilecek; i++) {
                SavasAraclari yeni = hamleKartiUret(taraf);
                taraf.kartEkle(yeni);
                sonuc.yeniKartEkle(taraf.getOyuncuAdi(), yeni);
                yaz(taraf.getOyuncuAdi() + " yeni kart: " + yeni.getKartAdi());
            }
        }
        oyuncu.secimGecmisiniTemizle();
        bilgisayar.secimGecmisiniTemizle();
    }

    private void oyunuBitir(HamleSonucu sonuc) {
        oyunBitti = true;

        int oyuncuSkor = oyuncu.getSkor();
        int bilgisayarSkor = bilgisayar.getSkor();

        if (oyuncu.getKartListesi().isEmpty() && !bilgisayar.getKartListesi().isEmpty()) {
            sonucMesaji = "Bilgisayar kazandi (rakibin kartlari tukendi).";
        } else if (bilgisayar.getKartListesi().isEmpty() && !oyuncu.getKartListesi().isEmpty()) {
            sonucMesaji = oyuncu.getOyuncuAdi() + " kazandi (rakibin kartlari tukendi).";
        } else if (oyuncuSkor > bilgisayarSkor) {
            sonucMesaji = oyuncu.getOyuncuAdi() + " kazandi (skor ustunlugu).";
        } else if (bilgisayarSkor > oyuncuSkor) {
            sonucMesaji = "Bilgisayar kazandi (skor ustunlugu).";
        } else {

            int od = oyuncu.toplamDayaniklilik();
            int bd = bilgisayar.toplamDayaniklilik();
            int fark = Math.abs(od - bd);
            if (od > bd) {
                oyuncu.skorEkle(fark);
                sonucMesaji = oyuncu.getOyuncuAdi() + " kazandi (dayaniklilik farki +" + fark + ").";
            } else if (bd > od) {
                bilgisayar.skorEkle(fark);
                sonucMesaji = "Bilgisayar kazandi (dayaniklilik farki +" + fark + ").";
            } else {
                sonucMesaji = "Berabere.";
            }
        }

        sonuc.setOyunBitti(true);
        sonuc.setSonucMesaji(sonucMesaji);

        yaz("");
        yaz("=== OYUN BITTI ===");
        yaz(sonucMesaji);
        yaz(oyuncu.SkorGoster());
        yaz(bilgisayar.SkorGoster());
        kaydiDosyayaYaz("SavasAraclari.txt");
    }

    private SavasAraclari baslangicKartiUret() {
        switch (random.nextInt(3)) {
            case 0:  return new Ucak(baslangicSeviyePuani);
            case 1:  return new Obus(baslangicSeviyePuani);
            default: return new Firkateyn(baslangicSeviyePuani);
        }
    }

    private SavasAraclari hamleKartiUret(Oyuncu taraf) {
        if (!kapaliKartlarAcikMi(taraf)) {
            return baslangicKartiUret();
        }
        switch (random.nextInt(6)) {
            case 0:  return new Ucak(baslangicSeviyePuani);
            case 1:  return new Obus(baslangicSeviyePuani);
            case 2:  return new Firkateyn(baslangicSeviyePuani);
            case 3:  return new Siha(baslangicSeviyePuani);
            case 4:  return new Sida(baslangicSeviyePuani);
            default: return new KFS(baslangicSeviyePuani);
        }
    }

    public boolean kapaliKartlarAcikMi(Oyuncu taraf) {
        return taraf.getKazanilanSeviyePuani() >= SEVIYE_ESIGI;
    }
    private void yaz(String satir) {
        kayit.add(satir);
    }

    public List<String> getKayit() {
        return kayit;
    }

    public void kaydiDosyayaYaz(String dosyaAdi) {
        Path yol = Paths.get(dosyaAdi);
        try (PrintWriter yazici = new PrintWriter(
                Files.newBufferedWriter(yol, StandardCharsets.UTF_8))) {
            for (String satir : kayit) {
                yazici.println(satir);
            }
        } catch (IOException e) {
            System.err.println("Kayit dosyasi yazilamadi: " + e.getMessage());
        }
    }

    private static String kartlariYaz(List<SavasAraclari> kartlar) {
        if (kartlar == null || kartlar.isEmpty()) {
            return "(bos)";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < kartlar.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(kartlar.get(i).getKartAdi());
        }
        return sb.toString();
    }

    public Oyuncu getOyuncu()        { return oyuncu; }
    public Oyuncu getBilgisayar()    { return bilgisayar; }
    public int getHamle()            { return hamle; }
    public int getMaksimumHamle()    { return maksimumHamle; }
    public boolean isOyunBitti()     { return oyunBitti; }
    public String getSonucMesaji()   { return sonucMesaji; }

    public static void main(String[] args) {
        Application.launch(OyunUI.class, args);
    }
}