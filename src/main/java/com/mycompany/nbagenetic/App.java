/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nbagenetic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.mycompany.nbagenetic.conversion.Conversion;
import com.mycompany.nbagenetic.domain.Player;
import com.mycompany.nbagenetic.log.Log;
import com.mycompany.nbagenetic.repository.PlayersRepository;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.util.Factory;

public class App {
	
	//15
	// private final static Integer CHROMOSOME_SIZE = "0000110001111000011".length();
	private static Integer CHROMOSOME_SIZE = 1;
	private static final Integer MAX_RUNS = 100;
	private static final Integer TEAM_SIZE = 5;
	private static HashMap<String,Player> playersMap = null;
	private static Integer cicloCorridaAptitud = 0;
	

    // 2.) Definition of the fitness function.
    private static int eval(Genotype<BitGene> gt) {

        
    	Log.loguear("Evaluando funcion de aptitud ciclo " + cicloCorridaAptitud);
    	
    	List<Player> players = Conversion.chromosomeToPlayers(playersMap,gt);

//        if (!players.stream().allMatch(player -> player.isPresent())) {
//            return -1;
//        }
        
        int points = 0;
        for(Player p: players) {
        	if(p == null || (p != null && playersMap.get(Conversion.enteroBase10AStringBase2(p.getId())) == null)) {
        		Log.loguear("Aptitud: descartando por combinacion invalida ciclo " + cicloCorridaAptitud);
        		cicloCorridaAptitud++;
        		return -300;
        	}
        	
        	Player realPlayer = playersMap.get(Conversion.enteroBase10AStringBase2(p.getId()));
        	points += realPlayer.getOverallPoints();
        }
        
        Log.loguear("Aptitud: puntos obtenidos: " + points + " , ciclo " + cicloCorridaAptitud);
        
        if(points > 0) { 
        	cicloCorridaAptitud++;
        	return points;
        }
        
        
        Log.loguear("Aptitud: descartando por puntos < 0 , ciclo " + cicloCorridaAptitud);
        cicloCorridaAptitud++;
        return -300;
    }

    public static void main(String[] args) {

    	System.out.println("COMIENZA EL PROCESO: " + new SimpleDateFormat("hh:mm:ss dd/MM/YYYY").format(new Date()));
    	
    	
    	PlayersRepository playersRepository = PlayersRepository.getInstance();
        playersMap = playersRepository.getAllPlayersMap();

        System.out.println("PlayersRepository.MAX_PLAYER_ID = '"  + PlayersRepository.MAX_PLAYER_ID + "'");
        // 1.) Define the genotype (factory) suitable
        //     for the problem.
        CHROMOSOME_SIZE = Conversion.getBitCountFor(PlayersRepository.MAX_PLAYER_ID);
                
        Factory<Genotype<BitGene>> gtf
                = Genotype.of(BitChromosome.of(CHROMOSOME_SIZE*TEAM_SIZE, 0.5));

        // 3.) Create the execution environment.
        Engine<BitGene, Integer> engine = Engine
                .builder(App::eval, gtf)
                .build();
        
        final EvolutionStatistics <Integer , ?>
        statistics = EvolutionStatistics.ofNumber() ;

        
        Log.loguear("Comienza corrida del engine");
        
        // 4.) Start the execution (evolution) and
        //     collect the result.
        Genotype<BitGene> result = engine.stream()
                .limit(MAX_RUNS)
                // Update  the  evaluation  statistics  after each  generation
                .peek(statistics)
                .collect(EvolutionResult.toBestGenotype());

        
        Log.loguear("Obteniendo resultados finales");
        
        List<Player> players = Conversion.chromosomeToPlayers(playersMap,result);

        Log.loguear("Cantidad de players de resultados finales: " + (players != null ? players.size():"0"));
        
        
        for(Player p:players) {
        	if(p != null && p.getId() != null) {
        		Player realPlayer = playersMap.get(Conversion.enteroBase10AStringBase2(p.getId()));
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

        
        System.out.println("TERMINO EL PROCESO: " + new SimpleDateFormat("hh:mm:ss dd/MM/YYYY").format(new Date()));
        
    }

    private static boolean stopCriteria() {
    	
    	
    	
    	
    	
    	return true;
    }
    
    
    private void runEvolution() {
    	
    }

}
