package application.controller;

import application.view.auxiliary.Mask;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class MedicineField {

    private TextField txtName = new TextField();

    private HBox paneQuantity = new HBox();
    private TextField txtQuantity = new TextField();
    private ComboBox<String> comboQuantity = new ComboBox<>();

    private HBox paneTime = new HBox();
    private TextField txtTime = new TextField();
    private Label lblTime = new Label("VEZ(ES) A CADA ");
    private TextField txtTime2 = new TextField();
    private ComboBox<String> comboTime = new ComboBox<>();

    private Button btnDelete = new Button();

    public MedicineField(ObservableList<MedicineField> obsMedicine){
        Mask.upperCase(txtName);
        this.txtName.getStyleClass().add("input");
        this.txtName.setMinHeight(35);
        this.txtName.setMaxHeight(35);



        Mask.zeroTo(txtQuantity, 999);
        this.txtQuantity.getStyleClass().add("input");
        this.txtQuantity.setMinSize(50, 35);
        this.txtQuantity.setMaxSize(50, 35);

        ObservableList<String> obsQuantity = FXCollections.observableArrayList();
        obsQuantity.addAll("GOTA(S)", "ML(S)", "COMPRIMIDO(S)", "APLICAÇÃO(ÕES)", "DOSE(S)");
        this.comboQuantity.setItems(obsQuantity);
        this.comboQuantity.setMinHeight(35);
        this.comboQuantity.setMaxHeight(35);
        this.comboQuantity.getStyleClass().add("comboBox");

        this.paneQuantity.getChildren().addAll(txtQuantity, comboQuantity);
        this.paneQuantity.setAlignment(Pos.CENTER_LEFT);
        this.paneQuantity.setSpacing(5);
        this.paneQuantity.setMinHeight(35);
        this.paneQuantity.setMaxHeight(35);



        Mask.zeroTo(txtTime, 99);
        this.txtTime.getStyleClass().add("input");
        this.txtTime.setMinSize(50, 35);
        this.txtTime.setMaxSize(50, 35);

        ObservableList<String> obsTime = FXCollections.observableArrayList();
        obsTime.addAll("HORA(S)", "DIA(S)", "SEMANA(S)", "MES(ES)", "ANO(S)");
        this.comboTime.setItems(obsTime);
        this.comboTime.setMinHeight(35);
        this.comboTime.setMaxHeight(35);
        this.comboTime.getStyleClass().add("comboBox");

        this.lblTime.setStyle("-fx-text-fill: #36434C;");

        Mask.zeroTo(txtTime2, 99);
        this.txtTime2.getStyleClass().add("input");
        this.txtTime2.setMinSize(50, 35);
        this.txtTime2.setMaxSize(50, 35);

        this.paneTime.getChildren().addAll(txtTime, lblTime, txtTime2, comboTime);
        this.paneTime.setAlignment(Pos.CENTER_LEFT);
        this.paneTime.setSpacing(5);
        this.paneTime.setMinHeight(35);
        this.paneTime.setMaxHeight(35);



        ImageView imageRemove = new ImageView(new Image("/view/img/trash.png"));
        imageRemove.setFitHeight(25);
        imageRemove.setFitWidth(25);
        this.btnDelete.setGraphic(imageRemove);
        this.btnDelete.getStyleClass().add("btnRed");
        this.btnDelete.setOnAction( e -> obsMedicine.remove(this));
        this.btnDelete.setMinSize(35, 35);
        this.btnDelete.setMaxSize(35, 35);
    }

    public TextField getTxtName() {
        return txtName;
    }

    public HBox getPaneQuantity() {
        return paneQuantity;
    }

    public TextField getTxtQuantity() {
        return txtQuantity;
    }

    public ComboBox<String> getComboQuantity() {
        return comboQuantity;
    }

    public HBox getPaneTime() {
        return paneTime;
    }

    public Label getLblTime() {
        return lblTime;
    }

    public TextField getTxtTime2() {
        return txtTime2;
    }

    public TextField getTxtTime() {
        return txtTime;
    }

    public ComboBox<String> getComboTime() {
        return comboTime;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }
}
