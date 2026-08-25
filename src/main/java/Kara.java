public abstract class Kara extends SavasAraclari {

    public Kara(int seviyePuani) {
        super(seviyePuani);
    }

    public abstract String getAltSinif();
    public abstract void setAltSinif(String altSinif);

    public abstract int getDenizVurusAvantaji();
    public abstract void setDenizVurusAvantaji(int denizVurusAvantaji);
}