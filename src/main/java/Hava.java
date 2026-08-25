public abstract class Hava extends SavasAraclari {

    public Hava(int seviyePuani) {
        super(seviyePuani);
    }

    public abstract String getAltSinif();
    public abstract void setAltSinif(String altSinif);

    public abstract int getKaraVurusAvantaji();
    public abstract void setKaraVurusAvantaji(int karaVurusAvantaji);
}