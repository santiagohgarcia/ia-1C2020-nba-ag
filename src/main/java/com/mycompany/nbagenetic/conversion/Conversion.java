package com.mycompany.nbagenetic.conversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.mycompany.nbagenetic.domain.Player;

import io.jenetics.BitChromosome;
import io.jenetics.BitGene;
import io.jenetics.Genotype;

public class Conversion {
	//1224 = 10011000111 
	//SELECT LENGTH(BIN(COUNT(*))) FROM player
	private final static Integer MAX_BITS_PLAYER_ID = 11;
	
	
	public static int getBitCountFor(int number) {
        int bits = 0;
        double capacity = 0;
        while (capacity < number) {
            capacity = Math.pow(2, bits);
            bits++;
        }

        return bits - 1;
    }

    public static List<Player> chromosomeToPlayers(HashMap<String,Player> playersMap,Genotype<BitGene> gt) {

        //PlayersRepository playersRepository = PlayersRepository.getInstance();

        BitChromosome chromosome = gt.chromosome().as(BitChromosome.class);

        int chromosomeLength = chromosome.length();
        int bitsPerPlayer = chromosomeLength / 5;
        int index = 0;

        List<Player> players = new ArrayList<>();
        
        var chromosomeBitString = chromosome.toString().replace("|", "").substring(1);
        var playersBitStrings = splitInParts(chromosomeBitString, bitsPerPlayer);
        
        for(String playerBitString:playersBitStrings){
         players.add(playersMap.get(playerBitString));
        }

        return players;
    }
    
    public static String[] splitInParts(String s, int partLength)
    {
        int len = s.length();

        // Number of parts
        int nparts = (len + partLength - 1) / partLength;
        String parts[] = new String[nparts];

        // Break into parts
        int offset= 0;
        int i = 0;
        while (i < nparts)
        {
            parts[i] = s.substring(offset, Math.min(offset + partLength, len));
            offset += partLength;
            i++;
        }

        return parts;
    }
	
    public static String enteroBase10AStringBase2(Integer entero) {
    	return rellenarCeros(Integer.toBinaryString(entero)); 
    }
    
    public static String rellenarCeros(String binario) {
    	int longitud = MAX_BITS_PLAYER_ID - binario.length();
    	String relleno = "";
    	
    	for(int a = 0;a<longitud;a++) 
    		relleno += "0";
    	
    	return relleno + binario;
    	
    } 
    
    public static Integer stringBase2AEnteroBase10(String binario) {
    	return Integer.parseInt(binario,2);
    }
    
}
