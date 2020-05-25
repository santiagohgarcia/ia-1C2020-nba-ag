package com.mycompany.nbagenetic.limit;

import java.util.function.Predicate;

import io.jenetics.engine.EvolutionResult;

//Clase que se podria usar como criterio de parada
//Implementacion segun
//https://jenetics.io/manual/manual-6.0.0.pdf
//Pagian 66, seccion "Termination"
final class NBAGeneticSteadyFitnessLimit<C extends Comparable<? super C>> 
implements Predicate<EvolutionResult <?, C>>{

	private   final   int _generations ;
	private  boolean _proceed =true;
	private  int _stable = 0;
	private C _fitness ;
	
	public NBAGeneticSteadyFitnessLimit(final int generations) {
		_generations = generations;
	}
	
	@Override
	public  boolean test (final EvolutionResult <?, C> er )  {
		return _proceed;
	}
}


