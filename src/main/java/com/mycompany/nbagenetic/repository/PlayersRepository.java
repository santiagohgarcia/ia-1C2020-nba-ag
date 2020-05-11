/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nbagenetic.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.mycompany.nbagenetic.domain.Player;

/**
 *
 * @author santi
 */
public class PlayersRepository {

	public static Integer MAX_PLAYER_ID = 0;
    private static PlayersRepository instance = new PlayersRepository(); // Eagerly Loading of single ton instance
    private List<Player> players = new ArrayList<>();

    private PlayersRepository() {
    }

    public static PlayersRepository getInstance() {
        return instance;
    }

    public List<Player> getAllPlayersX() { 
    	List<Player> players = new ArrayList<Player>();
    	
    	Player p1 = new Player();
    	p1.setId(1);
    	p1.setHeight(1.83D);
    	p1.setName("JugadorId1");
    	p1.setOverallPoints(90D);
    	p1.setPrimaryPosition("SG");
    	p1.setSecondaryPosition("SF");
    	p1.setTeam("Equipo1");
    	
    	players.add(p1);
    	
    	Player p2 = new Player();
    	p2.setId(2);
    	p2.setHeight(1.90D);
    	p2.setName("JugadorId2");
    	p2.setOverallPoints(80D);
    	p2.setPrimaryPosition("SF");
    	p2.setSecondaryPosition("SG");
    	p2.setTeam("Equipo2");
    	
    	players.add(p2);
    	
    	Player p3 = new Player();
    	p3.setId(3);
    	p3.setHeight(1.99D);
    	p3.setName("JugadorId3");
    	p3.setOverallPoints(70D);
    	p3.setPrimaryPosition("SG");
    	p3.setSecondaryPosition("SF");
    	p3.setTeam("Equipo3");
    	
    	players.add(p3);
    	
    	Player p4 = new Player();
    	p4.setId(4);
    	p4.setHeight(1.75D);
    	p4.setName("JugadorId4");
    	p4.setOverallPoints(86D);
    	p4.setPrimaryPosition("SG");
    	p4.setSecondaryPosition("SF");
    	p4.setTeam("Equipo1");
    	
    	players.add(p4);
    	
    	Player p5 = new Player();
    	p5.setId(5);
    	p5.setHeight(1.93D);
    	p5.setName("JugadorId5");
    	p5.setOverallPoints(56D);
    	p5.setPrimaryPosition("SG");
    	p5.setSecondaryPosition("SF");
    	p5.setTeam("Equipo2");
    	
    	players.add(p5);
    	
    	Player p6 = new Player();
    	p6.setId(6);
    	p6.setHeight(1.77D);
    	p6.setName("JugadorId6");
    	p6.setOverallPoints(82D);
    	p6.setPrimaryPosition("SG");
    	p6.setSecondaryPosition("SF");
    	p6.setTeam("Equipo4");
    	
    	players.add(p6);
    	
    	Player p7 = new Player();
    	p7.setId(7);
    	p7.setHeight(1.85D);
    	p7.setName("JugadorId7");
    	p7.setOverallPoints(94D);
    	p7.setPrimaryPosition("SG");
    	p7.setSecondaryPosition("SF");
    	p7.setTeam("Equipo2");
    	
    	players.add(p7);
    	
    	Player p8 = new Player();
    	p8.setId(8);
    	p8.setHeight(1.69D);
    	p8.setName("JugadorId8");
    	p8.setOverallPoints(97D);
    	p8.setPrimaryPosition("SG");
    	p8.setSecondaryPosition("SF");
    	p8.setTeam("Equipo3");
    	
    	players.add(p8);
    	
    	Player p9 = new Player();
    	p9.setId(9);
    	p9.setHeight(1.80D);
    	p9.setName("JugadorId9");
    	p9.setOverallPoints(91D);
    	p9.setPrimaryPosition("SG");
    	p9.setSecondaryPosition("SF");
    	p9.setTeam("Equipo2");
    	
    	players.add(p9);
    	
    	Player p10 = new Player();
    	p10.setId(10);
    	p10.setHeight(2.00D);
    	p10.setName("JugadorId10");
    	p10.setOverallPoints(99D);
    	p10.setPrimaryPosition("SG");
    	p10.setSecondaryPosition("SF");
    	p10.setTeam("Equipo1");
    	
    	players.add(p10);
    
    	
    	return players;
    }
    
    
    public List<Player> getAllPlayers() {

        Path path = Paths.get("src\\main\\java\\com\\mycompany\\nbagenetic\\repository\\players.csv");

        if (players.size() <= 0) {

            try ( BufferedReader br = Files.newBufferedReader(path, StandardCharsets.US_ASCII)) {

                String line = br.readLine();
                //Skip first line
                line = br.readLine();

                Integer idCount = 1;
                while (line != null) {

                    String[] attributes = line.split(",");

                    Player player = new Player(
                            idCount++, //id
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
        }
        return players;

    }

    public Optional<Player> getPlayerById(int id){
        return this.getAllPlayers().stream().filter(player -> player.getId().equals(id)).findAny();
    }
    
    public HashMap<Integer,Player> getAllPlayersMap() {

        Path path = Paths.get("src\\main\\java\\com\\mycompany\\nbagenetic\\repository\\players.csv");
        Integer idCount = 1;
        
        //id, player
        HashMap<Integer,Player> allPlayers = new HashMap<Integer,Player>(); 
        
        if (players.size() <= 0) {

            try ( BufferedReader br = Files.newBufferedReader(path, StandardCharsets.US_ASCII)) {

                String line = br.readLine();
                //Skip first line
                line = br.readLine();

                
                while (line != null) {

                    String[] attributes = line.split(",");

                    Player player = new Player(
                            idCount++, //id
                            attributes[1], //name
                            attributes[3], //team
                            Double.parseDouble(attributes[4]), //overallPoints
                            attributes[6], //primaryPosition
                            attributes[7], //secondaryPosition
                            Double.parseDouble(attributes[8]) //height
                    );

                    allPlayers.put(player.getId(),player);

                    line = br.readLine();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        MAX_PLAYER_ID = idCount;
        return allPlayers;
    }
}
