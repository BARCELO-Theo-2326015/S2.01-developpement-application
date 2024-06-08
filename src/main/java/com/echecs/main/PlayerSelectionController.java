package com.echecs.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PlayerSelectionController {

    @FXML
    private ComboBox<String> playerComboBox;

    private ObservableList<Joueur> players;
    private Joueur selectedPlayer = null;

    @FXML
    private TextField newPlayerTextField;

    mainController main;

    @FXML
    public void initialize() {
        createPlayersFileIfNotExists(); // Crée le fichier "players.csv" s'il n'existe pas
        loadPlayers();
        playerComboBox.setItems(FXCollections.observableArrayList(players.stream().map(Joueur::getNomJoueur).collect(Collectors.toList())));
        selectedPlayer = null;
    }

    public Joueur getSelectedPlayer() {
        return selectedPlayer;
    }

    @FXML
    private void deletePlayer() {
        Joueur selectedPlayer = players.stream().filter(a -> a.getNomJoueur()
                .equals(playerComboBox.getSelectionModel().getSelectedItem())).findFirst().orElse(null);
        if (selectedPlayer != null) {
            players.remove(selectedPlayer);
            playerComboBox.setItems(FXCollections.observableArrayList(players.stream().map(Joueur::getNomJoueur).collect(Collectors.toList())));
            savePlayers();
        }
    }

    @FXML
    private void createNewPlayer() {
        String playerName = newPlayerTextField.getText().trim();
        if (!playerName.isEmpty()) {
            Joueur newPlayer = new Joueur(playerName, 0, 0); // Assuming new player is not a computer player
            players.add(newPlayer);
            playerComboBox.setItems(FXCollections.observableArrayList(players.stream().map(Joueur::getNomJoueur).collect(Collectors.toList())));
            savePlayers();
        }
    }

    @FXML
    private void selectPlayer() {
        Joueur selectedPlayer1 = players.stream().filter(a -> a.getNomJoueur()
                .equals(playerComboBox.getSelectionModel().getSelectedItem())).findFirst().orElse(null);
        if (selectedPlayer1 != null) {
            selectedPlayer = selectedPlayer1;
            main.setPlayer(selectedPlayer);
        }
    }

    void setMain(mainController main) throws IOException {
        this.main = main;
    }

    private void loadPlayers() {
        players = FXCollections.observableArrayList();
        try (Scanner scanner = new Scanner(new File("players.csv"))) {
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                String name = data[0];
                int nbParties = Integer.parseInt(data[1]);
                int nbPartiesGagne = Integer.parseInt(data[2]);
                Joueur player = new Joueur(name, nbParties, nbPartiesGagne);
                players.add(player);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void savePlayers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("players.csv"))) {
            for (Joueur player : players) {
                writer.println(player.getNomJoueur() + "," + player.getNbParties() + "," + player.getNbPartiesGagne());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createPlayersFileIfNotExists() {
        File playersFile = new File("players.csv");
        if (!playersFile.exists()) {
            try {
                playersFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}