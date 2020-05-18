/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nbagenetic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.mycompany.nbagenetic.configuracion.Configuracion;
import com.mycompany.nbagenetic.conversion.Conversion;
import com.mycompany.nbagenetic.domain.Player;
import com.mycompany.nbagenetic.log.Log;
import com.mycompany.nbagenetic.repository.PlayersRepository;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;
import io.jenetics.Mutator;
import io.jenetics.RouletteWheelSelector;
import io.jenetics.SinglePointCrossover;
import io.jenetics.TournamentSelector;
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
	//es static y se comparte en eval, mientras sea readonly
	private static Configuracion cfg = null;

    // 2.) Definition of the fitness function.
    private int eval(Genotype<BitGene> gt) {
    	Log.loguear("Evaluando funcion de aptitud ciclo " + cicloCorridaAptitud);
    	
    	List<Player> players = Conversion.chromosomeToPlayers(playersMap,gt);
//        if (!players.stream().allMatch(player -> player.isPresent())) {
//            return -1;
//        }
    	System.out.println("Longitud lista players: " + players.size());
    	
    	Player[] vecJugadores = new Player[5];
    	
    	Iterator it = players.iterator();
    	
    	for(int i=0;i<cfg.getTamanioEquipo() && it.hasNext();i++) { 
    		
    		Player p = (Player)it.next();
    		
    		/********************************************************
        	 * SITUACIONES INDESEABLES DESCARTARLAS POR COMPLETO
        	 ********************************************************/
        	//PENALIZAR POR EQUIPO INCOMPLETO
    		if(p == null || (p != null && p.getId() == null)) {
    			Log.loguear("Aptitud: descartando por combinacion invalida ciclo " + cicloCorridaAptitud);
    			cicloCorridaAptitud++;
    			//tirarlo abajo totalmente
    			//Seria el opuesto del mejor resultado
    			return  (-1 * (cfg.getMejorPosibleResultadoTotal()));
    		}
    		
    		vecJugadores[i] = playersMap.get(Conversion.enteroBase10AStringBase2(p.getId()));
    		
        	//PENALIZAR POR EQUIPO INCOMPLETO
    		if(vecJugadores[i] == null) {
    			Log.loguear("Aptitud: descartando por combinacion invalida ciclo " + cicloCorridaAptitud);
    			cicloCorridaAptitud++;
    			//tirarlo abajo totalmente
    			//Seria el opuesto del mejor resultado
    			return  (-1 * (cfg.getMejorPosibleResultadoTotal()));
    		}
    		i++;
    	}
    	
    	//PENALIZAR POR REPETICION DE EQUIPO
    	Player rp1 = playersMap.get(Conversion.enteroBase10AStringBase2(vecJugadores[0].getId())); 
    	Player rp2 = playersMap.get(Conversion.enteroBase10AStringBase2(vecJugadores[1].getId()));
    	Player rp3 = playersMap.get(Conversion.enteroBase10AStringBase2(vecJugadores[2].getId()));
    	Player rp4 = playersMap.get(Conversion.enteroBase10AStringBase2(vecJugadores[3].getId()));
    	Player rp5 = playersMap.get(Conversion.enteroBase10AStringBase2(vecJugadores[4].getId()));
    	
    	Boolean mismoEquipo = rp1.getTeam().equals(rp2.getTeam())
    	&& rp2.getTeam().equals(rp3.getTeam())
    	&& rp3.getTeam().equals(rp4.getTeam())
    	&& rp4.getTeam().equals(rp5.getTeam());
    	
    	if(!mismoEquipo){
    		Log.loguear("Aptitud: descartando por combinacion invalida ciclo " + cicloCorridaAptitud);
    		cicloCorridaAptitud++;
    		//tirarlo abajo totalmente
    		//Seria el opuesto del mejor resultado
    		return  (-1 * (cfg.getMejorPosibleResultadoTotal()));
    	}
    	
    	Integer points = 0;
        
        for(Player p: players) {
        	/********************************************************
        	 * SITUACIONES DESEABLES, POTENCIALES CANDIDATOS
        	 ********************************************************/
        	//EVALUAR PUNTOS
        	Player realPlayer = playersMap.get(Conversion.enteroBase10AStringBase2(p.getId()));
            points += realPlayer.getOverallPoints().intValue();
        	
            //EVALUAR ALTURA
            points += realPlayer.getHeight().intValue();
        }
        
        Log.loguear("Aptitud: puntos obtenidos: " + points + " , ciclo " + cicloCorridaAptitud);
        cicloCorridaAptitud++;
        return points;
    }

    public static void main(String[] args) {

    	System.out.println("COMIENZA EL PROCESO: " + new SimpleDateFormat("hh:mm:ss dd/MM/YYYY").format(new Date()));
    	
    	cfg = new Configuracion();
    	try {
    		cfg.loadConfig();
    	}catch(Exception ex) {
    		ex.printStackTrace();
    		return;
    	}
    	
    	PlayersRepository playersRepository = PlayersRepository.getInstance();
        playersMap = playersRepository.getAllPlayersMap();

        System.out.println("PlayersRepository.MAX_PLAYER_ID = '"  + PlayersRepository.MAX_PLAYER_ID + "'");
        // 1.) Define the genotype (factory) suitable
        //     for the problem.
        CHROMOSOME_SIZE = Conversion.getBitCountFor(PlayersRepository.MAX_PLAYER_ID);
                
        Factory<Genotype<BitGene>> gtf
                = Genotype.of(BitChromosome.of(CHROMOSOME_SIZE*cfg.getTamanioEquipo(), 0.5));

        App app = new App();
        
        // 3.) Create the execution environment.
        Engine<BitGene, Integer> engine = Engine
                .builder(app::eval, gtf)
              //Experimental 1
                .populationSize(cfg.getTamanioPoblacion())
                //Experimental 2
                .survivorsSelector(new RouletteWheelSelector <>())
                .offspringSelector(new TournamentSelector <>())
                .alterers(new Mutator <>(0.55) ,new SinglePointCrossover <>(0.06) )
                //probar este
                //.optimize(Optimize.MAXIMUM)
                .build();
        
        final EvolutionStatistics <Integer , ?>
        statistics = EvolutionStatistics.ofNumber() ;

        
        Log.loguear("Comienza corrida del engine");
        
        // 4.) Start the execution (evolution) and
        //     collect the result.
        Genotype<BitGene> result = engine.stream()
                .limit(cfg.getMaximoCorridas())
                // Update  the  evaluation  statistics  after each  generation
                .peek(statistics)
                .collect(EvolutionResult.toBestGenotype());

        
        Log.loguear("Obteniendo resultados finales");
        
        List<Player> players = Conversion.chromosomeToPlayers(playersMap,result);

        Log.loguear("Cantidad de players de resultados finales: " + (players != null ? players.size():"0"));
        
        
        for(Player p:players) {
        	if(p != null && p.getId() != null) {
        		Player realPlayer = playersMap.get(Conversion.enteroBase10AStringBase2(p.getId()));
        		var line = realPlayer.getName() 
        				+ " - " 
        				+ realPlayer.getOverallPoints().toString()
        				+ " - "
        				+ realPlayer.getTeam();
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
