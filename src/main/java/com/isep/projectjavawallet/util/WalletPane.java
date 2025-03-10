package com.isep.projectjavawallet.util;

import com.isep.projectjavawallet.bean.wallet.Wallet;
import com.isep.projectjavawallet.dao.WalletListDao;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.sql.SQLException;
import java.util.Objects;

public class WalletPane extends Pane {
    Wallet wallet;
    private final int id;
    private final Button addButton;
    private final Button removeButton;
    private final Button infoButton;
    private final Button enterButton;
    private final Label walletName;
    private final Label walletDescription;



    public WalletPane(int id){
        this.id = id;
        addButton = new Button();
        removeButton = new Button();
        infoButton = new Button();
        enterButton = new Button();
        walletName = new Label();
        walletDescription = new Label();

        Image walletIcon = new Image(getClass().getResourceAsStream("/com/isep/projectjavawallet/imgs/walletIcon.png"));
        ImageView walletView = new ImageView(walletIcon);

        // nodes' position

        // walletIcon
        walletView.setFitHeight(70);
        walletView.setFitWidth(75);
        walletView.setLayoutX(40);
        walletView.setLayoutY(5);

        // addButton
        addButton.setLayoutX(700);
        addButton.setLayoutY(15);
        Image addIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/isep/projectjavawallet/imgs/addIcon.png")));
        ImageView addView = new ImageView(addIcon);
        addView.setFitHeight(50);
        addView.setFitWidth(50);
        addButton.setGraphic(addView);

        // removeButton
        removeButton.setLayoutX(700);
        removeButton.setLayoutY(15);
        Image removeIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/isep/projectjavawallet/imgs/delIcon.png")));
        ImageView removeView = new ImageView(removeIcon);
        removeView.setFitHeight(50);
        removeView.setFitWidth(50);
        removeButton.setGraphic(removeView);
        removeButton.setVisible(false);

        // infoButton
        infoButton.setLayoutX(620);
        infoButton.setLayoutY(15);
        Image infoIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/isep/projectjavawallet/imgs/infoIcon.png")));
        ImageView infoView = new ImageView(infoIcon);
        infoView.setFitHeight(50);
        infoView.setFitWidth(50);
        infoButton.setGraphic(infoView);
        infoButton.setVisible(false);

        // enterButton
        enterButton.setLayoutX(540);
        enterButton.setLayoutY(15);
        Image enterIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/isep/projectjavawallet/imgs/enterIcon.png")));
        ImageView enterView = new ImageView(enterIcon);
        enterView.setFitHeight(50);
        enterView.setFitWidth(50);
        enterButton.setGraphic(enterView);
        enterButton.setVisible(false);

        // walletName
        walletName.setLayoutX(150);
        walletName.setLayoutY(22);
        walletName.prefHeight(45);
        walletName.prefWidth(90);
        walletName.setText("walletName");       // MAX 10 letters
        walletName.setFont(new Font("System Bold",36));

        this.getChildren().addAll(addButton,removeButton,infoButton,enterButton,walletName, walletView);

        // Buttons' action
        addButton.setOnAction(e ->
        {
            addButtonAction();
        });


        removeButton.setOnAction(e ->
        {
            removeButtonAction();
        });


        infoButton.setOnAction(e ->
        {
            infoButtonAction();
        });

        enterButton.setOnAction(e ->
        {
            enterButtonAction();
        });

    }



    // Not finished yet
    private void addButtonAction(){
        try {
            // show new_wallet window
            SceneManager.anotherScene("/com/isep/projectjavawallet/WalletsViews/addNewWallet-view.fxml","New wallet");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void removeButtonAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to remove this wallet?");

        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        alert.showAndWait().ifPresent(response -> {

            if (response == ButtonType.OK) {
                String username = UserManager.getHome().getProfile().getAccount().getUsername();

                // remove from database
                try {
                    boolean isSuccessful = new WalletListDao().removeWallet(username, wallet);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                // remove from back-end list (Wallet)
                UserManager.getHome().getWallets().remove(wallet);
                // remove from front-end list (wallet Pane)
                UserManager.getWalletsListController().removeWalletPane(this);
                // remove from back-end list (wallet Pane)
                UserManager.getWalletPanes().remove(this);

            }
        });
    }
    private void infoButtonAction() {
        try {
            // show new_wallet window
            SceneManager.anotherScene_walletPane("/com/isep/projectjavawallet/WalletsViews/walletInfo-view.fxml","wallet Information",id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    private void enterButtonAction() {
        UserManager.setCurrentWallet(wallet);
        SceneManager.changeSceneRightPart("/com/isep/projectjavawallet/WalletsViews/walletData.fxml");

    }




    public void update_ADD(String Wname, String Wdescription){
        wallet = UserManager.getHome().getWallets().get(id);
        addButton.setVisible(false);
        infoButton.setVisible(true);
        removeButton.setVisible(true);
        enterButton.setVisible(true);
        walletName.setText(Wname);
        walletDescription.setText(Wdescription);
    }

    public void update_LOAD(Wallet wallet){
        this.wallet = wallet;
        addButton.setVisible(false);
        infoButton.setVisible(true);
        removeButton.setVisible(true);
        enterButton.setVisible(true);
        walletName.setText(wallet.getWalletName());
        walletDescription.setText(wallet.getDescription());
    }
}
