package com.mycompany.nbagenetic.configuracion;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Configuracion {

	private Integer maximoCorridas;
	
	private final String CONFIG_PATH = System.getProperty(File.separator) + "nbaConfig.properties";
		
	public Configuracion(){
	}
	
	public void loadConfig() throws Exception {
		try {
			
			Properties cfgProps = new Properties();
			InputStream is=new FileInputStream(CONFIG_PATH);
			cfgProps.load(is);
			maximoCorridas = Integer.parseInt(cfgProps.getProperty("maximoCorridas"));
			
		}catch(Exception ex) {
			ex.printStackTrace();
			throw new Exception("Fallo la carga de la configuracion");
		}
		
	}
	
}
