public class Ucak extends Hava {

    private int dayaniklilik;
    private String sinif;
    private int vurus;
    private String altSinif;
    private int karaVurusAvantaji;

    public Ucak() {
        this(0);
    }

    public Ucak(int seviyePuani) {
        super(seviyePuani);
        this.dayaniklilik = 20;
        this.sinif = "Hava";
        this.altSinif = "Ucak";
        this.vurus = 10;
        this.karaVurusAvantaji = 10;
    }

    @Override
    public int getDayaniklilik() {
        return dayaniklilik;
    }

    @Override
    public void setDayaniklilik(int dayaniklilik) {
        this.dayaniklilik = dayaniklilik;
    }

    @Override
    public String getSinif() {
        return sinif;
    }

    @Override
    public void setSinif(String sinif) {
        this.sinif = sinif;
    }

    @Override
    public int getVurus() {
        return vurus;
    }

    @Override
    public void setVurus(int vurus) {
        this.vurus = vurus;
    }

    @Override
    public String getAltSinif() {
        return altSinif;
    }

    @Override
    public void setAltSinif(String altSinif) {
        this.altSinif = altSinif;
    }

    @Override
    public int getKaraVurusAvantaji() {
        return karaVurusAvantaji;
    }

    @Override
    public void setKaraVurusAvantaji(int karaVurusAvantaji) {
        this.karaVurusAvantaji = karaVurusAvantaji;
    }

    @Override
    public void durumGuncelle(int vurusDegeri, int seviyePuaniDegeri) {
        if (vurusDegeri != 0) {
            setDayaniklilik(getDayaniklilik() - vurusDegeri);
        }
        if (seviyePuaniDegeri != 0) {
            setSeviyePuani(getSeviyePuani() + seviyePuaniDegeri);
        }
    }

    @Override
    public String toString() {
        return getKartAdi();
    }
}