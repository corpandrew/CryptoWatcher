/**
 * Created by corpa on 6/10/17.
 */

import com.google.common.io.Files;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class WatcherFrame extends Application {

    private VBox watcherVBox;
    private int currentNumberOfRows;
    private Stage primaryStage;
    private Group root;

    private VBox profileVBox;
    private int currentNumberOfProfileRows;
    private Stage profilePageStage;

    private TextField priceField, profitField;


    @Override
    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Hello World");
        AppMain.marketsMap = RESTfulUtils.getTickers();
        this.primaryStage = reloadPrimaryStage(null);
//
//        root = new Group();
//
//        currentNumberOfRows = 0;
//
//        Scene scene = new Scene(root);
//
//        watcherVBox = new VBox();
//        watcherVBox.setPadding(new Insets(15, 12, 15, 12));
//        watcherVBox.setSpacing(10);
//        root.getChildren().add(watcherVBox);
//
//        addElementToVbox(new Label(""));
//        currentNumberOfRows++;
//
//        Label textLabel = new Label("CryptoWatcher");
//        textLabel.setFont(new Font(25));
//
//        addElementToVbox(textLabel);
//        currentNumberOfRows++;
//        watcherVBox.setAlignment(Pos.CENTER);
//
//        createNewRow(null);
//
//        createMenu();
//
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }

    private void addElementToVbox(Control control) {
        watcherVBox.getChildren().add(control);
    }

    private void addPaneToVbox(Pane pane) {
        watcherVBox.getChildren().add(pane);
    }

    private void createNewRow(MarketShare marketShare) {

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 12, 10, 12));
        hBox.setSpacing(10);

        ComboBox<String> marketBox = new ComboBox<>(AppMain.marketStrings);
        hBox.getChildren().add(marketBox);

        if (marketShare != null) {
            for (int i = 0; i < marketBox.getItems().size(); i++) {
                if (marketBox.getItems().get(i).equals(marketShare.getMarket())) {
                    marketBox.getSelectionModel().select(i);
                    break;
                }
            }
        }

        Label priceLabel = new Label("Price: ");
        hBox.getChildren().add(priceLabel);

        priceField = new TextField();
        hBox.getChildren().add(priceField);

        Label profitLabel = new Label("Profit: ");
        hBox.getChildren().add(profitLabel);

        TextField profitField = new TextField();
        hBox.getChildren().add(profitField);

        marketBox.setOnAction(event -> {

            AppMain.marketsMap = RESTfulUtils.getTickers();

            priceField.setText(AppMain.marketsMap.get(marketBox.getValue()).getLast());
        });

        if (marketShare != null) {

            priceField.setText(AppMain.marketsMap.get(marketBox.getValue()).getLast());

            BigDecimal currentPrice = new BigDecimal(priceField.getText());

            BigDecimal profit = (currentPrice.multiply(marketShare.getAmountBought())).subtract((currentPrice.multiply(marketShare.getPriceBoughtAt())));

            profitField.setText(profit.toPlainString());

            try {
                SchedulerFactory sf = new StdSchedulerFactory();
                Scheduler sched = sf.getScheduler();

                JobDetail getPriceJob = JobBuilder.newJob(GetPricesJob.class).build();
                Trigger t = newTrigger()
                        .withIdentity("trigger", "group1")
                        .startNow()
                        .withSchedule(simpleSchedule().withIntervalInMilliseconds(550).repeatForever())
                        .build();

                sched.scheduleJob(getPriceJob, t);

                sched.start();
//
//                try {
//                    Thread.sleep(1000000000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                sched.shutdown(true);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }

        } else {
            profitField.setText("Create Profile First!");
        }

        addPaneToVbox(hBox);
        currentNumberOfRows++;

        if (marketShare == null) {
            HBox buttonHbox = new HBox();
            buttonHbox.setPadding(new Insets(10, 12, 10, 12));
            buttonHbox.setSpacing(10);

            Button newRowButton = new Button("Create New Row");
            buttonHbox.getChildren().add(newRowButton);

            newRowButton.setOnAction(event -> {
                watcherVBox.getChildren().remove(currentNumberOfRows);
                createNewRow(null);
                primaryStage.sizeToScene();
            });

            watcherVBox.getChildren().add(buttonHbox);
        }

    }

    public void createMenu() {
        MenuBar menuBar = new MenuBar();

        // --- Menu File
        Menu menuFile = new Menu("File");

        MenuItem newProfile = new MenuItem("New Profile");
        newProfile.setAccelerator(KeyCombination.keyCombination("Meta+N"));
        newProfile.setOnAction(t -> newProfileStage());

        MenuItem loadProfile = new MenuItem("Load Profile");
        loadProfile.setAccelerator(KeyCombination.keyCombination("Meta+L"));
        loadProfile.setOnAction(t -> {
//            System.out.println(loadProfile().toString());
            primaryStage.close();
            primaryStage = reloadPrimaryStage(loadProfile());
            primaryStage.show();
//            profilePageStage.close();
        });

        menuFile.getItems().add(newProfile);
        menuFile.getItems().add(loadProfile);

        // --- Menu Edit
        Menu menuEdit = new Menu("Edit");

        // --- Menu View
        Menu menuView = new Menu("View");

        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        root.getChildren().add(menuBar);
    }

    private void newProfileStage() {

        Group newProfileGroup = new Group();
        Scene secondScene = new Scene(newProfileGroup);

        currentNumberOfProfileRows = 0;

        profileVBox = new VBox();
        profileVBox.setPadding(new Insets(15, 12, 15, 12));
        profileVBox.setSpacing(10);
        newProfileGroup.getChildren().add(profileVBox);

        profilePageStage = new Stage();
        profilePageStage.setTitle("Second Stage");
        profilePageStage.setScene(secondScene);

        //Set position of second window, related to primary window.
        profilePageStage.setX(primaryStage.getX() + 250);
        profilePageStage.setY(primaryStage.getY() + 100);

        createNewProfileRow();

        profilePageStage.show();
    }

    private void createNewProfileRow() {

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 12, 10, 12));
        hBox.setSpacing(10);

        ComboBox<String> marketBox = new ComboBox<>(AppMain.marketStrings);
        hBox.getChildren().add(marketBox);

        Label boughtAtPrice = new Label("Price Bought At: ");
        hBox.getChildren().add(boughtAtPrice);

        TextField boughtAtField = new TextField();
        hBox.getChildren().add(boughtAtField);

        Label amountBought = new Label("Amount Bought: ");
        hBox.getChildren().add(amountBought);

        TextField amountBoughtField = new TextField();
        hBox.getChildren().add(amountBoughtField);

        profileVBox.getChildren().add(hBox);
        currentNumberOfProfileRows++;

        HBox buttonHbox = new HBox();
        buttonHbox.setPadding(new Insets(10, 12, 10, 12));
        buttonHbox.setSpacing(10);

        Button newRowButton = new Button("Add another row");
        buttonHbox.getChildren().add(newRowButton);

        Button saveButton = new Button("Save Profile");
        buttonHbox.getChildren().add(saveButton);

        newRowButton.setOnAction(event -> {
            profileVBox.getChildren().remove(currentNumberOfProfileRows);
            createNewProfileRow();
            profilePageStage.sizeToScene();
        });

        saveButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DAT files (*.dat)", "*.dat");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(primaryStage);

            if (file != null) {

                Profile currentProfile = new Profile();

                for (Node n : profileVBox.getChildren()) {
                    HBox rowHbox = (HBox) n;

                    try {
                        String comboBoxValue = (String) ((ComboBox) rowHbox.getChildren().get(0)).getValue();

                        String priceValue = ((TextField) rowHbox.getChildren().get(2)).getText();

                        String amountValue = ((TextField) rowHbox.getChildren().get(4)).getText();

                        currentProfile.add(new MarketShare(comboBoxValue, new BigDecimal(priceValue), new BigDecimal(amountValue)));

                    } catch (ClassCastException buttonException) {

                    }

                }

                saveProfile(currentProfile, file);
                profilePageStage.close();
            }
        });

        profileVBox.getChildren().add(buttonHbox);

    }

    private void saveProfile(Profile profile, File file) {
        try {
            FileWriter fileWriter = null;

            fileWriter = new FileWriter(file);
            fileWriter.write(profile.toString());
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Profile loadProfile() {
        Profile profile = new Profile();

        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DAT files (*.dat)", "*.dat");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show open file dialog
        File file = fileChooser.showOpenDialog(primaryStage);

        profile.setName(file.getName().substring(0, file.getName().indexOf(".")));

        System.out.println(profile.getName());

        List<String> lines = null;

        try {
            lines = Files.readLines(file, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] splitLine;
        for (String line : lines) {

            splitLine = line.split(",");

            profile.add(new MarketShare(splitLine[0], new BigDecimal(splitLine[1]), new BigDecimal(splitLine[2])));
        }

        return profile;
    }


    private Stage reloadPrimaryStage(Profile profile) {

        primaryStage = new Stage();

        primaryStage.setTitle("CryptoCurrency Watcher - Made by Andrew Corp");
        root = new Group();

        currentNumberOfRows = 0;

        Scene scene = new Scene(root);

        watcherVBox = new VBox();
        watcherVBox.setPadding(new Insets(15, 12, 15, 12));
        watcherVBox.setSpacing(10);
        root.getChildren().add(watcherVBox);

        addElementToVbox(new Label(""));
        currentNumberOfRows++;

        Label textLabel = new Label("CryptoWatcher");
        textLabel.setFont(new Font(25));

        addElementToVbox(textLabel);
        currentNumberOfRows++;
        watcherVBox.setAlignment(Pos.CENTER);

        if (profile != null) {
            for (MarketShare m : profile.getMarketShares()) {
                createNewRow(m);
            }
        } else {
            createNewRow(null);
        }

        createMenu();

        primaryStage.setScene(scene);
        primaryStage.show();

        return primaryStage;
    }

    private class GetPricesJob implements Job {

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

            System.out.println(RESTfulUtils.getTickers());

        }
    }
}
