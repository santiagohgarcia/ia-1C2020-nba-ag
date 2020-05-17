/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.nbagenetic;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import io.jenetics.*;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;

import com.mycompany.nbagenetic.repository.PlayersRepository;
import com.mycompany.nbagenetic.domain.Player;

import static io.jenetics.engine.Limits.bySteadyFitness;

public class App {

    private static List<Player> allPlayers;

    // 2.) Definition of the fitness function.
    private static int eval(Genotype<IntegerGene> gt) {
        IntegerChromosome chromosome = gt.chromosome()
                .as(IntegerChromosome.class);

        List<Integer> possibleIDs = Arrays.stream(chromosome.toArray()).boxed().collect(Collectors.toList());

        AtomicReference<Integer> fitnessValueAccumulator = new AtomicReference<>(0);

        //APTITUD = 0.5 * ALTURA + PUNTAJE_TOTAL
        Consumer<Player> addToFitnessValue = (player) ->
                fitnessValueAccumulator.accumulateAndGet(Double.valueOf(player.getHeight() * 0.5 + player.getOverallPoints()).intValue(), Integer::sum);

        Runnable setFitnessToNegative = () -> fitnessValueAccumulator.set(-100000);

        possibleIDs.forEach(possibleID ->
                allPlayers.stream()
                        .filter(player -> player.getId().equals(possibleID))
                        .findFirst()
                        .ifPresentOrElse(addToFitnessValue, setFitnessToNegative));

        return fitnessValueAccumulator.get();
    }
 
    public static void main(String[] args) {
        
        PlayersRepository playersRepository = PlayersRepository.getInstance();

        allPlayers = playersRepository.getAllPlayers();


        //1.) Define the genotype (factory) suitable
        //     for the problem.
        Genotype<IntegerGene> gtf =
                Genotype.of(IntegerChromosome.of(1000, 1200, 6));

        //gtf.instances().forEach(genotype -> System.out.println("Original:\n" + genotype));

        EvolutionStatistics<Integer, ?> statistics = EvolutionStatistics.ofNumber();

        // 3.) Create the execution environment.
        Engine<IntegerGene, Integer> engine = Engine
                .builder(App::eval, gtf)
                .build();

        // 4.) Start the execution (evolution) and
        //     collect the result.
        Phenotype<IntegerGene, Integer> result = engine.stream()
                .limit(bySteadyFitness(7))
                .limit(20)
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

    private static BitChromosome playerChromosome() {
        return BitChromosome.of(7, 0.5);
    }
}

/*

    List<BitChromosome> chromosomes = new ArrayList<>();
        IntStream.rangeClosed(1, 5).forEach(i -> chromosomes.add(playerChromosome()));
                Factory<Genotype<BitGene>> gtf =
        Genotype.of(chromosomes);

        Engine<BitGene, Integer> engine = Engine
        .builder(App::eval, gtf)
        .build();

        Genotype<BitGene> result = engine.stream()
        .limit(100)
        .collect(EvolutionResult.toBestGenotype());

        System.out.println("Hello World:\n" + result);*/
