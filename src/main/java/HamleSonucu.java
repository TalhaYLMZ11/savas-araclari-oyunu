import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Bir hamlenin sonucunu tasiyan veri sinifi.
 * Oyun sinifi bunu uretir, OyunUI yalnizca ekrana basar.
 */
public class HamleSonucu {

    private final int hamleNo;

    private List<SavasAraclari> oyuncuSecimi = new ArrayList<>();
    private List<SavasAraclari> bilgisayarSecimi = new ArrayList<>();

    private final List<Carpisma> carpismalar = new ArrayList<>();
    private final Map<String, List<SavasAraclari>> yeniKartlar = new LinkedHashMap<>();

    private boolean oyunBitti;
    private String sonucMesaji = "";

    public HamleSonucu(int hamleNo) {
        this.hamleNo = hamleNo;
    }

    public void carpismaEkle(Carpisma carpisma) {
        carpismalar.add(carpisma);
    }

    public void yeniKartEkle(String oyuncuAdi, SavasAraclari kart) {
        yeniKartlar.computeIfAbsent(oyuncuAdi, k -> new ArrayList<>()).add(kart);
    }

    public int getHamleNo()                            { return hamleNo; }
    public List<Carpisma> getCarpismalar()             { return carpismalar; }
    public Map<String, List<SavasAraclari>> getYeniKartlar() { return yeniKartlar; }

    public List<SavasAraclari> getOyuncuSecimi()       { return oyuncuSecimi; }
    public void setOyuncuSecimi(List<SavasAraclari> v) { this.oyuncuSecimi = v; }

    public List<SavasAraclari> getBilgisayarSecimi()   { return bilgisayarSecimi; }
    public void setBilgisayarSecimi(List<SavasAraclari> v) { this.bilgisayarSecimi = v; }

    public boolean isOyunBitti()                       { return oyunBitti; }
    public void setOyunBitti(boolean v)                { this.oyunBitti = v; }

    public String getSonucMesaji()                     { return sonucMesaji; }
    public void setSonucMesaji(String v)               { this.sonucMesaji = v; }
}