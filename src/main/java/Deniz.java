public abstract class Deniz extends SavasAraclari {

    public Deniz(int seviyePuani) {
        super(seviyePuani);
    }

    public abstract String getAltSinif();
    public abstract void setAltSinif(String altSinif);

    public abstract int getHavaVurusAvantaji();
    public abstract void setHavaVurusAvantaji(int havaVurusAvantaji);
}