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
import io.jenetics.AnyGene;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;
import io.jenetics.Mutator;
import io.jenetics.Optimize;
import io.jenetics.RouletteWheelSelector;
import io.jenetics.SinglePointCrossover;
import io.jenetics.TournamentSelector;
import io.jenetics.engine.Constraint;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStatistics;
import io.jenetics.engine.Limits;
import io.jenetics.util.Factory;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.Collectors; 

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
    	
    	List<Player> players = Conversion.chromosomeToPlayers(playersMap,gt);

        if(!this.esValido(players)){
            return  (-1 * (cfg.getMejorPosibleResultadoTotal()));
        }
    	
    	Integer fitnessValue = this.getFitness(players);
        
        Log.loguear("Aptitud: puntos obtenidos: " + fitnessValue + " , ciclo " + cicloCorridaAptitud);
        cicloCorridaAptitud++;
        return fitnessValue;
    }
    
    private Integer getFitness(List<Player> players){
    
        Integer fitnessValue = 0;
        
        for(Player p: players) {
            /********************************************************
            * SITUACIONES DESEABLES, POTENCIALES CANDIDATOS
            ********************************************************/
            fitnessValue += p.getOverallPoints().intValue();
        	
            //EVALUAR ALTURA, evaluada al 50%
            fitnessValue += ( p.getHeight().intValue() / 2 ) ;
        }
        
        return fitnessValue;
    }
        
    private boolean esValido(List<Player> players){
        //Verificar que sean 5 jugadores validos
        for(Player p:players){
           if(p == null || (p != null && p.getId() == null)) {
               return false;
           }
        }
        
        Set<String> posicionesNecesarias = new HashSet<String>();
        posicionesNecesarias.add("C");
        posicionesNecesarias.add("PG");
        posicionesNecesarias.add("SG");
        posicionesNecesarias.add("SF");
        posicionesNecesarias.add("PF");
        
        Set<String> equiposESTE = new HashSet<String>();
        equiposESTE.add("Miami Heat");
        equiposESTE.add("Boston Celtics");
        equiposESTE.add("Philadelphia 76ers");
        equiposESTE.add("Detroit Pistons");
        equiposESTE.add("Atlanta Hawks");
        equiposESTE.add("Chicago Bulls");
        equiposESTE.add("Orlando Magic");
        equiposESTE.add("'01-'02 New Jersey Nets");
        equiposESTE.add("New York Knicks");
        equiposESTE.add("Toronto Raptors");
        equiposESTE.add("Washington Wizards");
        equiposESTE.add("'88-'89 Detroit Pistons");
        equiposESTE.add("Milwaukee Bucks");
        equiposESTE.add("'84-'85 Milwaukee Bucks");
        equiposESTE.add("Cleveland Cavaliers");
        equiposESTE.add("'85-'86 Atlanta Hawks");
        equiposESTE.add("Indiana Pacers");
        equiposESTE.add("'12-'13 Miami Heat");
        equiposESTE.add("'92-'93 Charlotte Hornets");
        equiposESTE.add("'11-'12 New York Knicks");
        equiposESTE.add("Brooklyn Nets");
        equiposESTE.add("'92-'93 Chicago Bulls");
        equiposESTE.add("'07-'08 Boston Celtics");
        equiposESTE.add("'85-'86 Boston Celtics");
        equiposESTE.add("'99-'00 Toronto Raptors");
        equiposESTE.add("'10-'11 Chicago Bulls");
        equiposESTE.add("'76-'77 Philadelphia 76ers");
        equiposESTE.add("'05-'06 Miami Heat");
        equiposESTE.add("'03-'04 Detroit Pistons");
        equiposESTE.add("'94-'95 Orlando Magic");
        equiposESTE.add("'06-'07 Cleveland Cavaliers");
        equiposESTE.add("'95-'96 Chicago Bulls");
        equiposESTE.add("'94-'95 New York Knicks");
        equiposESTE.add("'88-'89 Chicago Bulls");

        //Verificar que se cumplan 5 posiciones distintas, 5 jugadores distintos y 5 equipos distintos
        Set<String> equiposSet = new HashSet<String>();
        Set<String> posicionesSet = new HashSet<String>(); 
        Set<Integer> jugadoresSet = new HashSet<Integer>(); 
        
        players.stream().forEach(p -> {
            equiposSet.add(p.getTeam());
            
            //Armo un Set de Posiciones que pueden ser cubiertas por los jugadores
            if(posicionesSet.contains(p.getPrimaryPosition())){
                if(!p.getSecondaryPosition().equals("")){
                    posicionesSet.add(p.getSecondaryPosition());
                } 
            }else{
               posicionesSet.add(p.getPrimaryPosition());
            }
            
            jugadoresSet.add(p.getId());
        });
        
        //Si los jugadores son menores que 5, o los equipos estan repetidos (son menores que 5)
        if(jugadoresSet.size() < 5 || equiposSet.size() < 5){
            return false;
        }
        
        //Si los jugadores cubren las 5 posiciones, es correcto
        if(!posicionesSet.containsAll(posicionesNecesarias)){
            return false;
        }
        
        //La cantidad de equipos de la conferencia ESTE tiene que ser entre 2 y 3
        //Si existen 4 equipos o mas de la conf ESTE => retornar FALSE
        //Si existen 1 equipos o menos de la conf ESTE => la conf OESTE tiene muchos jugadores, tambien retornamos FALSE
        var equiposDeConfESTE = equiposSet.stream().filter(e -> {
           return equiposESTE.contains(e);
        }).collect(Collectors.toSet());
        
        if( !(equiposDeConfESTE.size() >= 2 && equiposDeConfESTE.size() <= 3 ) ){
            return false;
        }
        
        
        return true;
    }

    public static void main(String[] args) {
        var dateInicio = new Date();
    	
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
                .constraint(Constraint.of(
                        phenotype -> {
                            List<Player> players = Conversion.chromosomeToPlayers(playersMap,phenotype.genotype());
                            return app.esValido(players);
                        }
                ))
                .survivorsSelector(new TournamentSelector<>(3))
                .alterers(new Mutator<>(0.15), new SinglePointCrossover<>(0.5))
                .maximalPhenotypeAge(100)
                .build();
        
        final EvolutionStatistics <Integer , ?>
        statistics = EvolutionStatistics.ofNumber() ;

        
        Log.loguear("Comienza corrida del engine");
        
        // 4.) Start the execution (evolution) and
        //     collect the result.
        Genotype<BitGene> result = engine.stream()
                .limit(Limits.byFitnessThreshold(692))
                //.limit(5000)
                //.limit(cfg.getMaximoCorridas())
                // Update  the  evaluation  statistics  after each  generation
                .peek(statistics)
                .collect(EvolutionResult.toBestGenotype());

        
        Log.loguear("Obteniendo resultados finales");
        
        List<Player> players = Conversion.chromosomeToPlayers(playersMap,result);

        Log.loguear("Cantidad de players de resultados finales: " + (players != null ? players.size():"0"));
        
        System.out.println("+---------------------------------------------------------------------------+");
        System.out.println("PUNTAJE TOTAL: " + app.getFitness(players));
        System.out.println("+---------------------------------------------------------------------------+");
        
        for(Player p:players) {
        	if(p != null && p.getId() != null) {
        		Player realPlayer = playersMap.get(Conversion.enteroBase10AStringBase2(p.getId()));
        		var line = p.getName() 
        				+ " - Puntos: " 
        				+ p.getOverallPoints().toString()
        				+ " - Altura: "
                                        + p.getHeight().toString()
        				+ " - "
        				+ p.getTeam()
                                        + " - Posiciones: "
        				+ p.getPrimaryPosition() + "/" + p.getSecondaryPosition();
                      System.out.println(line + "\n");
             
        	}
        }
        
       
        System.out.println("+---------------------------------------------------------------------------+");
        System.out.println("COMIENZA EL PROCESO: " + new SimpleDateFormat("hh:mm:ss dd/MM/YYYY").format(dateInicio));
        System.out.println("+---------------------------------------------------------------------------+"); 
        var dateFinal = new Date();
        System.out.println("TERMINO EL PROCESO: " + new SimpleDateFormat("hh:mm:ss dd/MM/YYYY").format(dateFinal));
        System.out.println("+---------------------------------------------------------------------------+");
        long seconds = (dateFinal.getTime()-dateInicio.getTime())/1000;
        System.out.println("TIEMPO TOTAL: " + seconds + "s");
        System.out.println("+---------------------------------------------------------------------------+");
        System.out.println(statistics);
        
//        for (int i = 0; i < players.size(); i++) {
//            Player p = playersMap.get(players.get(i));
//        	if (p != null) {
//                var line = p.getName() + " - " + p.getOverallPoints().toString();
//                System.out.println(line + "\n");
//            }
//        }

        
        System.out.println("TERMINO EL PROCESO: " + new SimpleDateFormat("hh:mm:ss dd/MM/YYYY").format(new Date()));
        
    }
    



}
