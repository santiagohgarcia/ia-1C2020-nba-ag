/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.nbagenetic;

import com.mycompany.nbagenetic.domain.Player;
import com.mycompany.nbagenetic.repository.PlayersRepository;
import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.jenetics.engine.Limits.bySteadyFitness;

public class App {

    public static final int TEAM_SIZE = 5;
    private static List<Player> allPlayers;

    // 2.) Definition of the fitness function.
    private static int eval(Genotype<IntegerGene> gt) {
        IntegerChromosome chromosome = gt.chromosome()
                .as(IntegerChromosome.class);

        List<Integer> possibleIDs = Arrays.stream(chromosome.toArray()).boxed().collect(Collectors.toList());

        List<Player> players = new ArrayList<>();

        possibleIDs.forEach(possibleID ->
                allPlayers.stream()
                        .filter(player -> player.getId().equals(possibleID))
                        .findFirst()
                        .ifPresent(players::add));

        List<Supplier<Boolean>> conditions = Arrays.asList(
                () -> players.size() == TEAM_SIZE,
                () -> players.stream().map(Player::getTeam).distinct().count() == TEAM_SIZE,
                () -> players.stream().map(Player::getPrimaryPosition).distinct().count() == TEAM_SIZE,
                () -> players.stream().map(Player::getSecondaryPosition).distinct().count() == TEAM_SIZE
                //() -> players.stream().map(Player::getConference).distinct().count() == TEAM_SIZE - 2 // TODO esta condición no la podemos implementar porque falta la columna del excel
        );

        if(!conditions.stream().allMatch(Supplier::get)) return -100000;

        //APTITUD = 0.5 * ALTURA + PUNTAJE_TOTAL
        Function<Player, Integer> playerFitnessValue = (player) -> Double.valueOf(player.getHeight() * 0.5 + player.getOverallPoints()).intValue();

        return players.stream().mapToInt(playerFitnessValue::apply).sum();
    }
 
    public static void main(String[] args) {
        
        PlayersRepository playersRepository = PlayersRepository.getInstance();

        allPlayers = playersRepository.getAllPlayers();


        //1.) Define the genotype (factory) suitable
        //     for the problem.
        Genotype<IntegerGene> gtf =
                Genotype.of(IntegerChromosome.of(1, 1223, TEAM_SIZE));

        //gtf.instances().forEach(genotype -> System.out.println("Original:\n" + genotype));

        EvolutionStatistics<Integer, ?> statistics = EvolutionStatistics.ofNumber();

        // 3.) Create the execution environment.
        Engine<IntegerGene, Integer> engine = Engine
                .builder(App::eval, gtf)
                .alterers(new Mutator<>(0.115), new SinglePointCrossover<>(0.16))
                .build();

        // 4.) Start the execution (evolution) and
        //     collect the result.
        Phenotype<IntegerGene, Integer> result = engine.stream()
                .limit(bySteadyFitness(7))
                .limit(10000)
                .peek(statistics)
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println("Hello World:\n" + result);

        /*// 1.) Define the genotype (factory) suitable
        //     for the problem.
        Factory<Genotype<BitGene>> gtf =
            Genotype.of(BitChromosome.of(10, 0.5));
 
        // 3.) Create the execution environment.
        Engine<BitGene, Integer> engine = Engine
            .builder(HelloWorld::eval, gtf)
            .build();
 
        // 4.) Start the execution (evolution) and
        //     collect the result.
        Genotype<BitGene> result = engine.stream()
            .limit(100)
            .collect(EvolutionResult.toBestGenotype());
 
        System.out.println("Hello World:\n" + result);*/
         //System.out.println("Hello World:\n" + result);
    }
}