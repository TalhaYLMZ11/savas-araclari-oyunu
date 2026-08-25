public class Firkateyn extends Deniz {

    private int dayaniklilik;
    private String sinif;
    private int vurus;
    private String altSinif;
    private int havaVurusAvantaji;

    public Firkateyn() {
        this(0);
    }

    public Firkateyn(int seviyePuani) {
        super(seviyePuani);
        this.dayaniklilik = 25;
        this.sinif = "Deniz";
        this.altSinif = "Firkateyn";
        this.vurus = 10;
        this.havaVurusAvantaji = 5;
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
    public int getHavaVurusAvantaji() {
        return havaVurusAvantaji;
    }

    @Override
    public void setHavaVurusAvantaji(int havaVurusAvantaji) {
        this.havaVurusAvantaji = havaVurusAvantaji;
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