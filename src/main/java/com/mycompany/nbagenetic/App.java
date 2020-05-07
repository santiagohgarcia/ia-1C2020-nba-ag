/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nbagenetic;

import java.util.List;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.Factory;

import com.mycompany.nbagenetic.repository.PlayersRepository;
import com.mycompany.nbagenetic.domain.Player;
import io.jenetics.Chromosome;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {

    // 2.) Definition of the fitness function.
    private static int eval(Genotype<BitGene> gt) {

        List<Optional<Player>> players = chromosomeToPlayers(gt);

        if (!players.stream().allMatch(player -> player.isPresent())) {
            return -1;
        }

        int points = 0;
        for (int i = 0; i < players.size(); i++) {
            points += players.get(i).get().getOverallPoints();
        }

        if(points > 0){ 
            return points;
        }else{
        return -1;
        }
       
    }

    public static void main(String[] args) {

        PlayersRepository playersRepository = PlayersRepository.getInstance();

        List<Player> allPlayers = playersRepository.getAllPlayers();

        // 1.) Define the genotype (factory) suitable
        //     for the problem.
        int bitsPerPlayer = getBitCountFor(allPlayers.size());
        Factory<Genotype<BitGene>> gtf
                = Genotype.of(BitChromosome.of(bitsPerPlayer * 5, 0.5));

        // 3.) Create the execution environment.
        Engine<BitGene, Integer> engine = Engine
                .builder(App::eval, gtf)
                .build();

        // 4.) Start the execution (evolution) and
        //     collect the result.
        Genotype<BitGene> result = engine.stream()
                .limit(100)
                .collect(EvolutionResult.toBestGenotype());

        List<Optional<Player>> players = chromosomeToPlayers(result);

        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).isPresent()) {
                var line = players.get(i).get().getName() + " - " + players.get(i).get().getOverallPoints().toString();
                System.out.println(line + "\n");
            }
        }

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

    private static List<Optional<Player>> chromosomeToPlayers(Genotype<BitGene> gt) {

        PlayersRepository playersRepository = PlayersRepository.getInstance();

        BitChromosome chromosome = gt.chromosome().as(BitChromosome.class);

        int chromosomeLength = chromosome.length();
        int bitsPerPlayer = chromosomeLength / 5;
        int index = 0;

        List<Optional<Player>> players = new ArrayList<>();

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

            players.add(playersRepository.getPlayerById(playerId));

            index += bitsPerPlayer;
        }

        return players;
    }

}
