public abstract class SavasAraclari {

    private int seviyePuani;

    public SavasAraclari(int seviyePuani) {
        this.seviyePuani = seviyePuani;
    }

    public abstract int getDayaniklilik();
    public abstract void setDayaniklilik(int dayaniklilik);

    public abstract String getSinif();
    public abstract void setSinif(String sinif);

    public abstract int getVurus();
    public abstract void setVurus(int vurus);

    public int getSeviyePuani() {
        return seviyePuani;
    }

    public void setSeviyePuani(int seviyePuani) {
        this.seviyePuani = seviyePuani;
    }

    /** Kartin gorunen adi (Ucak, Siha, Obus, ...). */
    public String getKartAdi() {
        return getClass().getSimpleName();
    }

    /** Kartin oyun icindeki anlik durumunu metin olarak dondurur. */
    public String kartPuaniGoster() {
        return String.format("%s | Dayaniklilik: %d | Seviye: %d",
                getKartAdi(), getDayaniklilik(), getSeviyePuani());
    }

    /** Saldiri sonucu dayaniklilik azaltimi ve seviye puani guncellemesi. */
    public abstract void durumGuncelle(int vurusDegeri, int seviyePuaniDegeri);
}