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

public class App {
    
    // 2.) Definition of the fitness function.
    private static int eval(Genotype<BitGene> gt) {
        return gt.chromosome()
            .as(BitChromosome.class)
            .bitCount();
    }
 
    public static void main(String[] args) {
        
        PlayersRepository playersRepository = PlayersRepository.getInstance();
        
        List<Player> players = playersRepository.getAllPlayers();
        
        
         String a = "a";
        
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