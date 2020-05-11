/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nbagenetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mycompany.nbagenetic.domain.Player;
import com.mycompany.nbagenetic.repository.PlayersRepository;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;

public class App {
	
	//15
	// private final static Integer CHROMOSOME_SIZE = "0000110001111000011".length();
	private static Integer CHROMOSOME_SIZE = 1;
	private static final Integer MAX_RUNS = 100;
	private static final Integer TEAM_SIZE = 5;
	private static HashMap<Integer,Player> playersMap = null;
	

    // 2.) Definition of the fitness function.
    private static int eval(Genotype<BitGene> gt) {

        List<Player> players = chromosomeToPlayers(gt);

//        if (!players.stream().allMatch(player -> player.isPresent())) {
//            return -1;
//        }
        
        int points = 0;
        for(Player p: players) {
        	if(p == null || (p != null && playersMap.get(p.getId()) == null))
        		return -1;
        	
        	Player realPlayer = playersMap.get(p.getId());
        	points += realPlayer.getOverallPoints();
        }
        
        if(points > 0) 
            return points;
        
        return -1;
    }

    public static void main(String[] args) {

        PlayersRepository playersRepository = PlayersRepository.getInstance();

        playersMap = playersRepository.getAllPlayersMap();

//        for(Player p:allPlayers) {
//        	System.out.println(">jugador: " 
//        			+ p.getName() + ", altura: "
//        			+ p.getHeight() + ", puntos: "
//        			+ p.getOverallPoints() + ", equipo: "
//        			+ p.getTeam()
//        			);
//        }
        
        
        /*************************************************************************************************
         * GENOTIPO = CROMOSOMA
         * GEN1 | GEN2     			|    GEN3
         * 	id	| overallPoints  	|	height
         * 	1	| 99  				|	195
         * 	4bit	| 110 0011			|	1100 0011
         * Cromosoma total: ? + 110001111000011
         * 4 + 15 posiciones = 4 + 15 bits = 19 bit
         *************************************************************************************************/
        
        /*private Double overallPoints;
    private String primaryPosition;
    private String secondaryPosition;
    private Double height;*/
        
        System.out.println("PlayersRepository.MAX_PLAYER_ID = '"  + PlayersRepository.MAX_PLAYER_ID + "'");
        
        // 1.) Define the genotype (factory) suitable
        //     for the problem.
        CHROMOSOME_SIZE = getBitCountFor(PlayersRepository.MAX_PLAYER_ID);
                
        Factory<Genotype<BitGene>> gtf
                = Genotype.of(BitChromosome.of(CHROMOSOME_SIZE*TEAM_SIZE, 0.5));

        // 3.) Create the execution environment.
        Engine<BitGene, Integer> engine = Engine
                .builder(App::eval, gtf)
                .build();

        // 4.) Start the execution (evolution) and
        //     collect the result.
        Genotype<BitGene> result = engine.stream()
                .limit(MAX_RUNS)
                .collect(EvolutionResult.toBestGenotype());

        List<Player> players = chromosomeToPlayers(result);

        for(Player p:players) {
        	if(p != null && p.getId() != null) {
        		Player realPlayer = playersMap.get(p.getId());
        		var line = realPlayer.getName() + " - " + realPlayer.getOverallPoints().toString();
                System.out.println(line + "\n");
        	}
        }
        
//        for (int i = 0; i < players.size(); i++) {
//            Player p = playersMap.get(players.get(i));
//        	if (p != null) {
//                var line = p.getName() + " - " + p.getOverallPoints().toString();
//                System.out.println(line + "\n");
//            }
//        }

    }

    private static int getBitCountFor(int number) {
        int bits = 0;
        double capacity = 0;
        while (capacity < number) {
            capacity = Math.pow(2, bits);
            bits++;
        }

        return bits - 1;
    }

    private static List<Player> chromosomeToPlayers(Genotype<BitGene> gt) {

        //PlayersRepository playersRepository = PlayersRepository.getInstance();

        BitChromosome chromosome = gt.chromosome().as(BitChromosome.class);

        int chromosomeLength = chromosome.length();
        int bitsPerPlayer = chromosomeLength / 5;
        int index = 0;

        List<Player> players = new ArrayList<>();

        while (index <= chromosomeLength) {

            var playerBits = chromosome.instances().skip(index).limit(bitsPerPlayer).collect(Collectors.toList());

            var playersBinaryString = "";

            for (int i = 0; i < playerBits.size(); i++) {
                if (playerBits.get(i).gene().bit()) {
                    playersBinaryString += "1";
                } else {
                    playersBinaryString += "0";
                }
            }

            int playerId = Integer.parseInt(playersBinaryString, 2);

            //players.add(playersRepository.getPlayerById(playerId));
            players.add(playersMap.get(playerId));
             

            index += bitsPerPlayer;
        }

        return players;
    }

}
