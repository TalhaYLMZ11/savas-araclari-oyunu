public class Carpisma {

    private final SavasAraclari oyuncuKarti;
    private final SavasAraclari bilgisayarKarti;

    private final int oyuncuSaldirisi;
    private final int bilgisayarSaldirisi;

    private final int oyuncuKalanDayaniklilik;
    private final int bilgisayarKalanDayaniklilik;

    private boolean oyuncuKartiElendi;
    private boolean bilgisayarKartiElendi;

    private int oyuncuKazanci;
    private int bilgisayarKazanci;

    public Carpisma(SavasAraclari oyuncuKarti, SavasAraclari bilgisayarKarti,
                    int oyuncuSaldirisi, int bilgisayarSaldirisi) {
        this.oyuncuKarti = oyuncuKarti;
        this.bilgisayarKarti = bilgisayarKarti;
        this.oyuncuSaldirisi = oyuncuSaldirisi;
        this.bilgisayarSaldirisi = bilgisayarSaldirisi;
        this.oyuncuKalanDayaniklilik = Math.max(0, oyuncuKarti.getDayaniklilik());
        this.bilgisayarKalanDayaniklilik = Math.max(0, bilgisayarKarti.getDayaniklilik());
    }

    public SavasAraclari getOyuncuKarti()          { return oyuncuKarti; }
    public SavasAraclari getBilgisayarKarti()      { return bilgisayarKarti; }
    public int getOyuncuSaldirisi()                { return oyuncuSaldirisi; }
    public int getBilgisayarSaldirisi()            { return bilgisayarSaldirisi; }
    public int getOyuncuKalanDayaniklilik()        { return oyuncuKalanDayaniklilik; }
    public int getBilgisayarKalanDayaniklilik()    { return bilgisayarKalanDayaniklilik; }

    public boolean isOyuncuKartiElendi()           { return oyuncuKartiElendi; }
    public void setOyuncuKartiElendi(boolean v)    { this.oyuncuKartiElendi = v; }

    public boolean isBilgisayarKartiElendi()       { return bilgisayarKartiElendi; }
    public void setBilgisayarKartiElendi(boolean v){ this.bilgisayarKartiElendi = v; }

    public int getOyuncuKazanci()                  { return oyuncuKazanci; }
    public void setOyuncuKazanci(int v)            { this.oyuncuKazanci = v; }

    public int getBilgisayarKazanci()              { return bilgisayarKazanci; }
    public void setBilgisayarKazanci(int v)        { this.bilgisayarKazanci = v; }
}