package com.echecs.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayerSelectionController {

    @FXML
    private ComboBox<Joueur> playerComboBox;

    private ObservableList<Joueur> players;
    private List<Joueur> selectedPlayers;

    @FXML
    public void initialize() {
        loadPlayers();
        playerComboBox.setItems(players);
        selectedPlayers = new ArrayList<>();
    }

    @FXML
    private void deletePlayer() {
        Joueur selectedPlayer = playerComboBox.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            players.remove(selectedPlayer);
            savePlayers();
        }
    }

    @FXML
    private void createNewPlayer() {
        // Code to open a window and create a new player
    }

    @FXML
    private void selectPlayer() {
        Joueur selectedPlayer = playerComboBox.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null && !selectedPlayers.contains(selectedPlayer)) {
            selectedPlayers.add(selectedPlayer);
            closeWindow();
        }
    }

    private void loadPlayers() {
        players = FXCollections.observableArrayList();
        try (Scanner scanner = new Scanner(new File("players.csv"))) {
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                String name = data[0];
                boolean isComputerPlayer = Boolean.parseBoolean(data[1]);
                Joueur player = new Joueur(name, isComputerPlayer);
                players.add(player);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void savePlayers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("players.csv"))) {
            for (Joueur player : players) {
                writer.println(player.getNomJoueur() + "," + player.isOrdinateurPlayer());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) playerComboBox.getScene().getWindow();
        stage.close();
    }
}