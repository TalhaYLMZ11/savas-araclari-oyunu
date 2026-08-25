import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OyunUI extends Application {

    private static final int MAKS_YUVA = 3;

    private static final double EL_KART_G = 108;
    private static final double EL_KART_Y = 142;
    private static final double YUVA_G = 104;
    private static final double YUVA_Y = 132;

    private Stage stage;
    private Oyun oyun;

    private final Integer[] yuvalar = new Integer[MAKS_YUVA];

    private HamleSonucu sonSonuc;

    private Label hamleLabel;
    private Label oyuncuSkorLabel;
    private Label bilgisayarSkorLabel;
    private Label oyuncuKartLabel;
    private Label bilgisayarKartLabel;
    private Label kilitLabel;
    private Label durumLabel;

    private HBox bilgisayarEli;
    private HBox arena;
    private FlowPane oyuncuEli;
    private TextArea logArea;

    private Button anaButton;
    private Button temizleButton;

    private TextField isimField;
    private Spinner<Integer> hamleSpinner;
    private Spinner<Integer> seviyeSpinner;

    private Image arkaPlanResmi;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        arkaPlanResmi = yukleResim("/png/backGround.png");
        showAnaMenu();
        stage.setTitle("Savas Araclari Kart Oyunu");
        stage.setMinWidth(1150);
        stage.setMinHeight(820);
        stage.show();
    }

    private Image yukleResim(String path) {
        try {
            return new Image(Objects.requireNonNull(getClass().getResource(path)).toExternalForm());
        } catch (Exception e) {
            return null;
        }
    }

    private void showAnaMenu() {
        VBox root = new VBox(18);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(36));
        root.setPrefSize(1280, 900);
        applyArkaPlan(root);

        Label title = new Label("SAVAS ARACLARI");
        title.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 52));
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.9), 12, 0.4, 0, 3);");

        Label subtitle = new Label("Kart Oyunu");
        subtitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        subtitle.setTextFill(Color.web("#FFD700"));

        VBox infoBox = new VBox(10);
        infoBox.setPadding(new Insets(20, 26, 20, 26));
        infoBox.setMaxWidth(680);
        infoBox.setStyle(camPanel());

        Label infoTitle = new Label("NASIL OYNANIR");
        infoTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        infoTitle.setTextFill(Color.web("#FFD700"));

        VBox kurallar = new VBox(6);
        String[] satirlar = {
                "Elinde 6 kart var. Her hamlede 3 kartini masadaki yuvalara surukle.",
                "1. yuvadaki kartin bilgisayarin 1. kartiyla carpisir, digerleri de sirayla.",
                "Avantaj zinciri:  Hava > Kara  >  Deniz  >  Hava",
                "Siha denize, KFS havaya, Sida karaya ayrica avantajlidir.",
                "20 seviye puanina ulasinca Siha, Sida ve KFS kartlari dagitima girer.",
                "Bir karti eleyen taraf en az 10 seviye ve skor kazanir."
        };
        for (String s : satirlar) {
            Label l = new Label("\u2022  " + s);
            l.setFont(Font.font("Arial", 13));
            l.setTextFill(Color.web("#DCE6F1"));
            kurallar.getChildren().add(l);
        }
        infoBox.getChildren().addAll(infoTitle, kurallar);

        VBox ayarBox = new VBox(12);
        ayarBox.setAlignment(Pos.CENTER);
        ayarBox.setPadding(new Insets(20, 26, 20, 26));
        ayarBox.setMaxWidth(560);
        ayarBox.setStyle(camPanel());

        Label ayarTitle = new Label("AYARLAR");
        ayarTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        ayarTitle.setTextFill(Color.web("#FFD700"));

        isimField = new TextField();
        isimField.setPromptText("Adiniz");
        isimField.setPrefWidth(210);
        isimField.setStyle(girdiStyle());

        hamleSpinner = new Spinner<>(1, 20, 5);
        hamleSpinner.setPrefWidth(110);
        hamleSpinner.setEditable(true);

        seviyeSpinner = new Spinner<>(0, 100, 0);
        seviyeSpinner.setPrefWidth(110);
        seviyeSpinner.setEditable(true);

        ayarBox.getChildren().addAll(ayarTitle,
                ayarSatiri("Oyuncu adi", isimField),
                ayarSatiri("Maksimum hamle", hamleSpinner),
                ayarSatiri("Baslangic seviye puani", seviyeSpinner));

        Button startButton = new Button("OYUNU BASLAT");
        startButton.setPrefWidth(240);
        startButton.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 15));
        startButton.setStyle(buttonStyle("#FFD700", "#141C26"));
        startButton.setOnAction(e -> oyunuBaslat());

        root.getChildren().addAll(title, subtitle, infoBox, ayarBox, startButton);
        stage.setScene(new Scene(root, 1280, 900));
    }

    private HBox ayarSatiri(String etiket, Node alan) {
        HBox box = new HBox(14);
        box.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label(etiket);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        label.setTextFill(Color.WHITE);
        label.setPrefWidth(200);
        box.getChildren().addAll(label, alan);
        return box;
    }

    private void oyunuBaslat() {
        String isim = isimField.getText() == null ? "" : isimField.getText().trim();
        if (isim.isEmpty()) {
            bilgiKutusu(Alert.AlertType.WARNING, "Uyari", "Lutfen adinizi girin.");
            return;
        }
        oyun = new Oyun(isim, hamleSpinner.getValue(), seviyeSpinner.getValue());
        Arrays.fill(yuvalar, null);
        sonSonuc = null;
        showOyunEkrani();
        turuHazirla();
    }


    private void showOyunEkrani() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(12));
        applyArkaPlan(root);

        root.setTop(hudPaneli());
        root.setCenter(masaPaneli());
        root.setBottom(altPanel());

        stage.setScene(new Scene(root, 1280, 900));
    }

    private VBox hudPaneli() {
        VBox top = new VBox(8);
        top.setPadding(new Insets(10, 14, 10, 14));
        top.setStyle(camPanel());

        HBox satir = new HBox(10);
        satir.setAlignment(Pos.CENTER);

        hamleLabel = rozet("Hamle 1/" + oyun.getMaksimumHamle(), "#3498db");
        oyuncuSkorLabel = rozet(oyun.getOyuncu().getOyuncuAdi() + "  0", "#27ae60");
        bilgisayarSkorLabel = rozet("Bilgisayar  0", "#e74c3c");
        oyuncuKartLabel = rozet("Elin: 6", "#f39c12");
        bilgisayarKartLabel = rozet("Rakip: 6", "#9b59b6");
        kilitLabel = rozet("Kapali kartlar kilitli", "#7f8c8d");

        satir.getChildren().addAll(hamleLabel, oyuncuSkorLabel, bilgisayarSkorLabel,
                oyuncuKartLabel, bilgisayarKartLabel, kilitLabel);

        durumLabel = new Label();
        durumLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        durumLabel.setTextFill(Color.web("#FFD700"));

        HBox durumSatiri = new HBox();
        durumSatiri.setAlignment(Pos.CENTER);
        durumSatiri.getChildren().add(durumLabel);

        top.getChildren().addAll(satir, durumSatiri);
        return top;
    }

    private VBox masaPaneli() {
        VBox masa = new VBox(10);
        masa.setPadding(new Insets(12, 8, 12, 8));
        masa.setAlignment(Pos.CENTER);

        // --- Rakip eli (kapali mini kartlar) ---
        VBox rakipBolge = new VBox(6);
        rakipBolge.setAlignment(Pos.CENTER);
        rakipBolge.getChildren().add(bolgeBasligi("BILGISAYARIN ELI", "#FF8A80"));

        bilgisayarEli = new HBox(7);
        bilgisayarEli.setAlignment(Pos.CENTER);
        bilgisayarEli.setMinHeight(70);
        rakipBolge.getChildren().add(bilgisayarEli);

        // --- Arena ---
        VBox arenaBolge = new VBox(6);
        arenaBolge.setAlignment(Pos.CENTER);
        arenaBolge.setPadding(new Insets(10, 14, 10, 14));
        arenaBolge.setStyle("-fx-background-color: rgba(6, 10, 16, 0.72);"
                + "-fx-background-radius: 18;"
                + "-fx-border-color: rgba(255, 215, 0, 0.35);"
                + "-fx-border-width: 2;"
                + "-fx-border-radius: 18;");

        arenaBolge.getChildren().add(bolgeBasligi("SAVAS ALANI", "#FFD700"));

        arena = new HBox(22);
        arena.setAlignment(Pos.CENTER);
        arena.setMinHeight(330);
        arenaBolge.getChildren().add(arena);

        // --- Oyuncu eli ---
        VBox oyuncuBolge = new VBox(6);
        oyuncuBolge.setAlignment(Pos.CENTER);
        oyuncuBolge.getChildren().add(
                bolgeBasligi("SENIN ELIN  \u2014  karti yukaridaki yuvaya surukle", "#61FF9A"));

        oyuncuEli = new FlowPane(10, 10);
        oyuncuEli.setAlignment(Pos.CENTER);
        oyuncuEli.setPrefWrapLength(1180);
        oyuncuEli.setMinHeight(EL_KART_Y + 10);

        ScrollPane elScroll = new ScrollPane(oyuncuEli);
        elScroll.setFitToWidth(true);
        elScroll.setPrefViewportHeight(EL_KART_Y + 24);
        elScroll.setStyle(scrollStyle());
        oyuncuBolge.getChildren().add(elScroll);

        masa.getChildren().addAll(rakipBolge, arenaBolge, oyuncuBolge);
        return masa;
    }

    private VBox altPanel() {
        VBox alt = new VBox(8);
        alt.setPadding(new Insets(8, 4, 0, 4));

        HBox butonlar = new HBox(12);
        butonlar.setAlignment(Pos.CENTER);

        anaButton = new Button("SAVASI BASLAT");
        anaButton.setPrefWidth(230);
        anaButton.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 14));
        anaButton.setStyle(buttonStyle("#e74c3c", "white"));
        anaButton.setOnAction(e -> anaButonaBasildi());

        temizleButton = new Button("YUVALARI BOSALT");
        temizleButton.setPrefWidth(180);
        temizleButton.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        temizleButton.setStyle(buttonStyle("#34495e", "white"));
        temizleButton.setOnAction(e -> {
            Arrays.fill(yuvalar, null);
            ciz();
        });

        Button menuButton = new Button("ANA MENU");
        menuButton.setPrefWidth(150);
        menuButton.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        menuButton.setStyle(buttonStyle("#7f8c8d", "white"));
        menuButton.setOnAction(e -> showAnaMenu());

        butonlar.getChildren().addAll(anaButton, temizleButton, menuButton);

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setPrefRowCount(4);
        logArea.setStyle(logStyle());

        alt.getChildren().addAll(butonlar, logArea);
        return alt;
    }


    private void turuHazirla() {
        if (oyun.isOyunBitti()) {
            return;
        }
        sonSonuc = null;
        Arrays.fill(yuvalar, null);
        anaButton.setText("SAVASI BASLAT");
        anaButton.setStyle(buttonStyle("#e74c3c", "white"));
        temizleButton.setDisable(false);
        durumLabel.setText("Hamle " + oyun.getHamle() + ": " + oyun.gerekliKartSayisi()
                + " kartini savas alanindaki yuvalara yerlestir.");
        ciz();
        logEkle("Hamle " + oyun.getHamle() + " basladi.");
    }

    private void anaButonaBasildi() {
        if (sonSonuc != null) {
            // Sonuc gosteriliyordu, siradaki hamleye gec.
            if (sonSonuc.isOyunBitti()) {
                return;
            }
            turuHazirla();
            return;
        }
        savasiCalistir();
    }

    private void savasiCalistir() {
        int gereken = oyun.gerekliKartSayisi();
        List<Integer> secim = doluYuvalar();
        if (oyun.isOyunBitti() || secim.size() != gereken) {
            return;
        }

        sonSonuc = oyun.hamleYap(secim);
        sonucuYazdir(sonSonuc);

        temizleButton.setDisable(true);
        if (sonSonuc.isOyunBitti()) {
            anaButton.setText("OYUN BITTI");
            anaButton.setDisable(true);
            durumLabel.setText(sonSonuc.getSonucMesaji());
        } else {
            anaButton.setText("DEVAM ET  \u2192");
            anaButton.setStyle(buttonStyle("#27ae60", "white"));
            durumLabel.setText("Carpisma sonuclari asagida. Devam etmek icin butona bas.");
        }

        ciz();

        if (sonSonuc.isOyunBitti()) {
            bilgiKutusu(Alert.AlertType.INFORMATION, "Oyun Sonu",
                    sonSonuc.getSonucMesaji() + "\n\n"
                            + oyun.getOyuncu().SkorGoster() + "\n"
                            + oyun.getBilgisayar().SkorGoster()
                            + "\n\nTum adimlar SavasAraclari.txt dosyasina yazildi.");
        }
    }

    private List<Integer> doluYuvalar() {
        List<Integer> liste = new ArrayList<>();
        for (Integer index : yuvalar) {
            if (index != null) {
                liste.add(index);
            }
        }
        return liste;
    }


    private void ciz() {
        Oyuncu oyuncu = oyun.getOyuncu();
        Oyuncu bilgisayar = oyun.getBilgisayar();
        int gereken = oyun.gerekliKartSayisi();

        // Rakip eli: kapali mini kartlar
        bilgisayarEli.getChildren().clear();
        for (int i = 0; i < bilgisayar.getKartListesi().size(); i++) {
            bilgisayarEli.getChildren().add(miniKapaliKart());
        }

        // Arena
        arena.getChildren().clear();
        int sutunSayisi = (sonSonuc != null) ? sonSonuc.getCarpismalar().size() : gereken;
        for (int i = 0; i < sutunSayisi; i++) {
            arena.getChildren().add(arenaSutunu(i));
        }

        // Oyuncu eli
        oyuncuEli.getChildren().clear();
        for (int i = 0; i < oyuncu.getKartListesi().size(); i++) {
            SavasAraclari kart = oyuncu.getKartListesi().get(i);
            oyuncuEli.getChildren().add(elKarti(kart, i));
        }

        // HUD
        hamleLabel.setText("Hamle " + oyun.getHamle() + "/" + oyun.getMaksimumHamle());
        oyuncuSkorLabel.setText(oyuncu.getOyuncuAdi() + "  " + oyuncu.getSkor());
        bilgisayarSkorLabel.setText("Bilgisayar  " + bilgisayar.getSkor());
        oyuncuKartLabel.setText("Elin: " + oyuncu.getKartListesi().size());
        bilgisayarKartLabel.setText("Rakip: " + bilgisayar.getKartListesi().size());

        boolean acik = oyun.kapaliKartlarAcikMi(oyuncu);
        kilitLabel.setText(acik
                ? "Siha / Sida / KFS acik"
                : "Kilitli (" + oyuncu.getKazanilanSeviyePuani() + "/" + Oyun.SEVIYE_ESIGI + ")");
        kilitLabel.setStyle(rozetStyle(acik ? "#16a085" : "#7f8c8d"));

        if (sonSonuc == null) {
            anaButton.setDisable(doluYuvalar().size() != gereken);
        }
    }

    private VBox arenaSutunu(int index) {
        VBox sutun = new VBox(6);
        sutun.setAlignment(Pos.CENTER);

        Carpisma c = (sonSonuc != null && index < sonSonuc.getCarpismalar().size())
                ? sonSonuc.getCarpismalar().get(index) : null;

        // Ust: bilgisayar tarafi
        Node ust;
        if (c != null) {
            ust = arenaKarti(c.getBilgisayarKarti(), c.getBilgisayarSaldirisi(),
                    c.isBilgisayarKartiElendi(), false);
        } else {
            ust = kapaliYuva();
        }

        Label orta = new Label(c == null ? "VS" : (c.getOyuncuSaldirisi() + "  VS  " + c.getBilgisayarSaldirisi()));
        orta.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, c == null ? 15 : 13));
        orta.setTextFill(Color.web("#FFD700"));
        orta.setPadding(new Insets(2, 12, 2, 12));
        orta.setStyle("-fx-background-color: rgba(0,0,0,0.55); -fx-background-radius: 999;");

        Node alt;
        if (c != null) {
            alt = arenaKarti(c.getOyuncuKarti(), c.getOyuncuSaldirisi(),
                    c.isOyuncuKartiElendi(), true);
        } else {
            alt = oyuncuYuvasi(index);
        }

        Label yuvaNo = new Label("YUVA " + (index + 1));
        yuvaNo.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        yuvaNo.setTextFill(Color.web("#9FB3C8"));

        sutun.getChildren().addAll(ust, orta, alt, yuvaNo);
        return sutun;
    }

    private StackPane kapaliYuva() {
        StackPane p = new StackPane();
        p.setPrefSize(YUVA_G, YUVA_Y);
        p.setMinSize(YUVA_G, YUVA_Y);
        p.setStyle("-fx-background-color: linear-gradient(to bottom right, #33415C, #1B2436);"
                + "-fx-background-radius: 10;"
                + "-fx-border-color: rgba(255,138,128,0.55);"
                + "-fx-border-width: 2;"
                + "-fx-border-radius: 10;");
        Label q = new Label("?");
        q.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 40));
        q.setTextFill(Color.web("#FF8A80"));
        p.getChildren().add(q);
        return p;
    }

    private StackPane oyuncuYuvasi(int index) {
        StackPane p = new StackPane();
        p.setPrefSize(YUVA_G, YUVA_Y);
        p.setMinSize(YUVA_G, YUVA_Y);

        Integer elIndex = yuvalar[index];
        if (elIndex != null && elIndex < oyun.getOyuncu().getKartListesi().size()) {
            SavasAraclari kart = oyun.getOyuncu().getKartListesi().get(elIndex);
            p.setStyle("-fx-background-color: " + kartRengi(kart) + ";"
                    + "-fx-background-radius: 10;"
                    + "-fx-border-color: #FFD700;"
                    + "-fx-border-width: 3;"
                    + "-fx-border-radius: 10;"
                    + "-fx-cursor: hand;"
                    + "-fx-effect: dropshadow(gaussian, rgba(255,215,0,0.45), 12, 0.3, 0, 0);");
            p.getChildren().add(kartIcerigi(kart, 13, 10, true));
            p.setOnMouseClicked(e -> {
                yuvalar[index] = null;
                ciz();
            });
        } else {
            p.setStyle("-fx-background-color: rgba(97, 255, 154, 0.07);"
                    + "-fx-background-radius: 10;"
                    + "-fx-border-color: rgba(97, 255, 154, 0.55);"
                    + "-fx-border-width: 2;"
                    + "-fx-border-radius: 10;"
                    + "-fx-border-style: segments(8, 6);");
            Label bos = new Label("BURAYA\nBIRAK");
            bos.setFont(Font.font("Arial", FontWeight.BOLD, 11));
            bos.setTextFill(Color.web("#61FF9A"));
            p.getChildren().add(bos);
        }

        yuvayaBirakmaEkle(p, index);
        return p;
    }

    private StackPane arenaKarti(SavasAraclari kart, int saldiri, boolean elendi, boolean oyuncunun) {
        StackPane p = new StackPane();
        p.setPrefSize(YUVA_G, YUVA_Y);
        p.setMinSize(YUVA_G, YUVA_Y);

        String kenar = elendi ? "#FF5252" : (oyuncunun ? "#61FF9A" : "#FF8A80");
        p.setStyle("-fx-background-color: " + kartRengi(kart) + ";"
                + "-fx-background-radius: 10;"
                + "-fx-border-color: " + kenar + ";"
                + "-fx-border-width: 3;"
                + "-fx-border-radius: 10;"
                + (elendi ? "-fx-opacity: 0.55;" : "")
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 8, 0.4, 0, 2);");

        VBox icerik = kartIcerigi(kart, 13, 10, true);

        Label hasar = new Label("\u2694 " + saldiri);
        hasar.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 11));
        hasar.setTextFill(Color.web("#FFD700"));
        icerik.getChildren().add(hasar);

        if (elendi) {
            Label elendiLabel = new Label("ELENDI");
            elendiLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 15));
            elendiLabel.setTextFill(Color.web("#FF5252"));
            elendiLabel.setPadding(new Insets(3, 10, 3, 10));
            elendiLabel.setStyle("-fx-background-color: rgba(0,0,0,0.78); -fx-background-radius: 6;");
            p.getChildren().addAll(icerik, elendiLabel);
        } else {
            p.getChildren().add(icerik);
        }
        return p;
    }

    private StackPane elKarti(SavasAraclari kart, int index) {
        StackPane p = new StackPane();
        p.setPrefSize(EL_KART_G, EL_KART_Y);
        p.setMinSize(EL_KART_G, EL_KART_Y);

        boolean turdaKullanildi = oyun.getOyuncu().secilmisMi(kart);
        boolean yuvada = yuvadaMi(index);
        boolean aktif = !turdaKullanildi && sonSonuc == null && !oyun.isOyunBitti();

        String kenar;
        String ek = "";
        if (turdaKullanildi) {
            kenar = "#607D8B";
            ek = "-fx-opacity: 0.4;";
        } else if (yuvada) {
            kenar = "#FFD700";
            ek = "-fx-opacity: 0.35;";
        } else if (aktif) {
            kenar = "rgba(97, 255, 154, 0.75)";
            ek = "-fx-cursor: hand;";
        } else {
            kenar = "rgba(255,255,255,0.25)";
        }

        p.setStyle("-fx-background-color: " + kartRengi(kart) + ";"
                + "-fx-background-radius: 11;"
                + "-fx-border-color: " + kenar + ";"
                + "-fx-border-width: 2;"
                + "-fx-border-radius: 11;"
                + ek
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.55), 7, 0.4, 0, 2);");

        VBox icerik = kartIcerigi(kart, 14, 11, false);

        Label alt = new Label(turdaKullanildi ? "bu turda oynandi" : (yuvada ? "yuvada" : "surukle \u2191"));
        alt.setFont(Font.font("Arial", FontWeight.BOLD, 9));
        alt.setTextFill(turdaKullanildi ? Color.web("#B0BEC5") : Color.web("#FFD700"));
        icerik.getChildren().add(alt);

        p.getChildren().add(icerik);

        if (aktif && !yuvada) {
            p.setOnDragDetected(event -> {
                Dragboard db = p.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(String.valueOf(index));
                db.setContent(content);
                event.consume();
            });
            p.setOnMouseClicked(event -> {
                ilkBosYuvayaKoy(index);
                event.consume();
            });
        }
        return p;
    }

    private VBox kartIcerigi(SavasAraclari kart, double adBoyut, double kucukBoyut, boolean kompakt) {
        VBox box = new VBox(kompakt ? 1 : 2);
        box.setAlignment(Pos.CENTER);

        Label ad = new Label(kart.getKartAdi());
        ad.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, adBoyut));
        ad.setTextFill(Color.WHITE);

        Label sinif = new Label(kart.getSinif());
        sinif.setFont(Font.font("Arial", FontWeight.BOLD, kucukBoyut - 1));
        sinif.setTextFill(Color.web("#CFE3FF"));

        Label hp = new Label("HP " + Math.max(0, kart.getDayaniklilik()) + "   ATK " + kart.getVurus());
        hp.setFont(Font.font("Arial", kucukBoyut));
        hp.setTextFill(Color.web("#EAF2FF"));

        Label seviye = new Label("Seviye " + kart.getSeviyePuani());
        seviye.setFont(Font.font("Arial", kucukBoyut - 1));
        seviye.setTextFill(Color.web("#FFE082"));

        box.getChildren().addAll(ad, sinif, hp, seviye);
        return box;
    }

    private StackPane miniKapaliKart() {
        StackPane p = new StackPane();
        p.setPrefSize(46, 64);
        p.setMinSize(46, 64);
        p.setStyle("-fx-background-color: linear-gradient(to bottom right, #33415C, #161E2E);"
                + "-fx-background-radius: 7;"
                + "-fx-border-color: rgba(255, 215, 0, 0.45);"
                + "-fx-border-width: 1.5;"
                + "-fx-border-radius: 7;");
        Label q = new Label("?");
        q.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 20));
        q.setTextFill(Color.web("#8FA3BF"));
        p.getChildren().add(q);
        return p;
    }

    private void yuvayaBirakmaEkle(StackPane yuva, int yuvaIndex) {
        yuva.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        yuva.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean tamam = false;
            if (db.hasString()) {
                try {
                    int elIndex = Integer.parseInt(db.getString());
                    yuvayaYerlestir(yuvaIndex, elIndex);
                    tamam = true;
                } catch (NumberFormatException ignored) {
                    tamam = false;
                }
            }
            event.setDropCompleted(tamam);
            event.consume();
        });
    }

    private void yuvayaYerlestir(int yuvaIndex, int elIndex) {
        for (int i = 0; i < yuvalar.length; i++) {
            if (yuvalar[i] != null && yuvalar[i] == elIndex) {
                yuvalar[i] = null;
            }
        }
        yuvalar[yuvaIndex] = elIndex;
        ciz();
    }

    private void ilkBosYuvayaKoy(int elIndex) {
        int gereken = oyun.gerekliKartSayisi();
        for (int i = 0; i < gereken; i++) {
            if (yuvalar[i] == null) {
                yuvayaYerlestir(i, elIndex);
                return;
            }
        }
    }

    private boolean yuvadaMi(int elIndex) {
        for (Integer v : yuvalar) {
            if (v != null && v == elIndex) {
                return true;
            }
        }
        return false;
    }

    private void sonucuYazdir(HamleSonucu sonuc) {
        logEkle("");
        logEkle("--- HAMLE " + sonuc.getHamleNo() + " ---");
        logEkle("Senin secimin : " + kartIsimleri(sonuc.getOyuncuSecimi()));
        logEkle("Bilgisayar    : " + kartIsimleri(sonuc.getBilgisayarSecimi()));

        int no = 1;
        for (Carpisma c : sonuc.getCarpismalar()) {
            logEkle(String.format("Yuva %d: %s (%d hasar) <-> %s (%d hasar)  |  HP %d - %d",
                    no++,
                    c.getOyuncuKarti().getKartAdi(), c.getOyuncuSaldirisi(),
                    c.getBilgisayarKarti().getKartAdi(), c.getBilgisayarSaldirisi(),
                    c.getOyuncuKalanDayaniklilik(), c.getBilgisayarKalanDayaniklilik()));
            if (c.isBilgisayarKartiElendi()) {
                logEkle("   > " + c.getBilgisayarKarti().getKartAdi()
                        + " elendi, +" + c.getOyuncuKazanci() + " puan sana.");
            }
            if (c.isOyuncuKartiElendi()) {
                logEkle("   > " + c.getOyuncuKarti().getKartAdi()
                        + " elendi, +" + c.getBilgisayarKazanci() + " puan bilgisayara.");
            }
        }

        sonuc.getYeniKartlar().forEach((ad, kartlar) ->
                logEkle(ad + " yeni kart aldi: " + kartIsimleri(kartlar)));

        if (sonuc.isOyunBitti()) {
            logEkle("");
            logEkle("=== OYUN BITTI: " + sonuc.getSonucMesaji() + " ===");
            logEkle(oyun.getOyuncu().SkorGoster());
            logEkle(oyun.getBilgisayar().SkorGoster());
        }
    }

    private void logEkle(String satir) {
        logArea.appendText(satir + "\n");
    }

    private String kartIsimleri(List<SavasAraclari> kartlar) {
        if (kartlar == null || kartlar.isEmpty()) {
            return "(bos)";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < kartlar.size(); i++) {
            if (i > 0) {
                sb.append(" + ");
            }
            sb.append(kartlar.get(i).getKartAdi());
        }
        return sb.toString();
    }


    private Label bolgeBasligi(String metin, String renk) {
        Label l = new Label(metin);
        l.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 12));
        l.setTextFill(Color.web(renk));
        return l;
    }

    private Label rozet(String metin, String renk) {
        Label l = new Label(metin);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        l.setTextFill(Color.WHITE);
        l.setPadding(new Insets(6, 14, 6, 14));
        l.setStyle(rozetStyle(renk));
        return l;
    }

    private String rozetStyle(String renk) {
        return "-fx-background-color: " + renk + ";"
                + "-fx-background-radius: 999;"
                + "-fx-border-color: rgba(255,255,255,0.22);"
                + "-fx-border-radius: 999;"
                + "-fx-border-width: 1;"
                + "-fx-text-fill: white;";
    }

    private String kartRengi(SavasAraclari kart) {
        if (kart instanceof Hava) {
            return "linear-gradient(to bottom right, #2E86DE, #17406B)";
        }
        if (kart instanceof Kara) {
            return "linear-gradient(to bottom right, #96603C, #4E3524)";
        }
        return "linear-gradient(to bottom right, #17A2B8, #0C4B60)";
    }

    private String camPanel() {
        return "-fx-background-color: rgba(12, 18, 26, 0.82);"
                + "-fx-background-radius: 16;"
                + "-fx-border-color: rgba(255,255,255,0.14);"
                + "-fx-border-radius: 16;"
                + "-fx-border-width: 1;";
    }

    private String girdiStyle() {
        return "-fx-font-size: 13;"
                + "-fx-padding: 8;"
                + "-fx-background-radius: 8;"
                + "-fx-border-radius: 8;"
                + "-fx-border-color: rgba(255, 215, 0, 0.35);";
    }

    private String logStyle() {
        return "-fx-control-inner-background: rgba(6, 10, 16, 0.94);"
                + "-fx-text-fill: #9EEBA6;"
                + "-fx-font-family: 'Consolas';"
                + "-fx-font-size: 12;"
                + "-fx-background-radius: 10;";
    }

    private String scrollStyle() {
        return "-fx-background: transparent; -fx-background-color: transparent;";
    }

    private String buttonStyle(String bgColor, String textColor) {
        return "-fx-background-color: " + bgColor + ";"
                + "-fx-text-fill: " + textColor + ";"
                + "-fx-background-radius: 9;"
                + "-fx-padding: 11 22 11 22;"
                + "-fx-border-radius: 9;"
                + "-fx-cursor: hand;";
    }

    private void applyArkaPlan(Pane pane) {
        if (arkaPlanResmi == null) {
            pane.setStyle("-fx-background-color: linear-gradient(to bottom right, #0B1320, #172B4D);");
            return;
        }
        BackgroundImage bg = new BackgroundImage(arkaPlanResmi,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true));
        pane.setBackground(new Background(bg));
    }

    private void bilgiKutusu(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}