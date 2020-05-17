/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nbagenetic.repository;

import com.mycompany.nbagenetic.domain.Player;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.Integer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author santi
 */
public class PlayersRepository {

    private static PlayersRepository instance = new PlayersRepository(); // Eagerly Loading of single ton instance
    private List<Player> players = new ArrayList<>();

    private PlayersRepository() {
    }

    public static PlayersRepository getInstance() {
        return instance;
    }

    public List<Player> getAllPlayers() {

        Path path = Paths.get("src/main/java/com/mycompany/nbagenetic/repository/players.csv");

        try ( BufferedReader br = Files.newBufferedReader(path, StandardCharsets.US_ASCII)) {

            String line = br.readLine();
            //Skip first line
            line = br.readLine();
            while (line != null) {

                String[] attributes = line.split(",");
                
                    Player player = new Player(
                            Integer.parseInt(attributes[0]), //id
                            attributes[1], //name
                            attributes[3], //team
                            Double.parseDouble(attributes[4]), //overallPoints
                            attributes[6], //primaryPosition
                            attributes[7], //secondaryPosition
                            Double.parseDouble(attributes[8]) //height
                    );

                    players.add(player);
                
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return players;

    }

}
