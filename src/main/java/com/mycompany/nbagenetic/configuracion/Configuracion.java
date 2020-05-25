package com.mycompany.nbagenetic.configuracion;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Configuracion {

	private Integer maximoCorridas;
	private Integer tamanioEquipo;
	private Integer tamanioPoblacion;
	private Integer mejorPosibleResultadoTotal;
	private Integer mejorPosibleResultadoPorPuntos;
	private Integer mejorPosibleResultadoPorAltura;
	private Integer peorPosibleResultadoTotal;
	private Integer peorPosibleResultadoPorPuntos;
	private Integer peorPosibleResultadoPorAltura;
	
	
	
	private final String CONFIG_PATH = "src"
			+ File.separator
			+"main" 
			+ File.separator
			+"java" 
			+ File.separator 
			+ "nbaConfig.properties";
		
	public Configuracion(){
	}
	
	public void loadConfig() throws Exception {
		try {
			
			Properties cfgProps = new Properties();
			InputStream is=new FileInputStream(CONFIG_PATH);
			cfgProps.load(is);
			
			maximoCorridas = Integer.parseInt(cfgProps.getProperty("maximoCorridas").trim());
			tamanioPoblacion = Integer.parseInt(cfgProps.getProperty("tamanioPoblacion").trim());
			tamanioEquipo = Integer.parseInt(cfgProps.getProperty("tamanioEquipo").trim());
			//Cotas maximos y minimos aptitud
			mejorPosibleResultadoTotal = Integer.parseInt(cfgProps.getProperty("MEJOR_POSIBLE_RESULTADO_TOTAL").trim());
			mejorPosibleResultadoPorPuntos = Integer.parseInt(cfgProps.getProperty("MEJOR_POSIBLE_RESULTADO_POR_PUNTOS").trim());
			mejorPosibleResultadoPorAltura = Integer.parseInt(cfgProps.getProperty("MEJOR_POSIBLE_RESULTADO_POR_ALTURA").trim());
			
			peorPosibleResultadoTotal = Integer.parseInt(cfgProps.getProperty("PEOR_POSIBLE_RESULTADO_TOTAL").trim());
			peorPosibleResultadoPorPuntos = Integer.parseInt(cfgProps.getProperty("PEOR_POSIBLE_RESULTADO_POR_PUNTOS").trim());
			peorPosibleResultadoPorAltura = Integer.parseInt(cfgProps.getProperty("PEOR_POSIBLE_RESULTADO_POR_ALTURA").trim());
			
			System.out.println("Se cargo la configuracion de '" + CONFIG_PATH + "'");
		}catch(Exception ex) {
			ex.printStackTrace();
			throw new Exception("Fallo la carga de la configuracion en '" + CONFIG_PATH + "'");
		}
		
	}

	public Integer getMaximoCorridas() {
		return maximoCorridas;
	}

	public void setMaximoCorridas(Integer maximoCorridas) {
		this.maximoCorridas = maximoCorridas;
	}

	public Integer getTamanioPoblacion() {
		return tamanioPoblacion;
	}

	public void setTamanioPoblacion(Integer tamanioPoblacion) {
		this.tamanioPoblacion = tamanioPoblacion;
	}

	public Integer getTamanioEquipo() {
		return tamanioEquipo;
	}

	public void setTamanioEquipo(Integer tamanioEquipo) {
		this.tamanioEquipo = tamanioEquipo;
	}

	public Integer getMejorPosibleResultadoTotal() {
		return mejorPosibleResultadoTotal;
	}

	public void setMejorPosibleResultadoTotal(Integer mejorPosibleResultadoTotal) {
		this.mejorPosibleResultadoTotal = mejorPosibleResultadoTotal;
	}

	public Integer getMejorPosibleResultadoPorPuntos() {
		return mejorPosibleResultadoPorPuntos;
	}

	public void setMejorPosibleResultadoPorPuntos(Integer mejorPosibleResultadoPorPuntos) {
		this.mejorPosibleResultadoPorPuntos = mejorPosibleResultadoPorPuntos;
	}

	public Integer getMejorPosibleResultadoPorAltura() {
		return mejorPosibleResultadoPorAltura;
	}

	public void setMejorPosibleResultadoPorAltura(Integer mejorPosibleResultadoPorAltura) {
		this.mejorPosibleResultadoPorAltura = mejorPosibleResultadoPorAltura;
	}

	public Integer getPeorPosibleResultadoTotal() {
		return peorPosibleResultadoTotal;
	}

	public void setPeorPosibleResultadoTotal(Integer peorPosibleResultadoTotal) {
		this.peorPosibleResultadoTotal = peorPosibleResultadoTotal;
	}

	public Integer getPeorPosibleResultadoPorPuntos() {
		return peorPosibleResultadoPorPuntos;
	}

	public void setPeorPosibleResultadoPorPuntos(Integer peorPosibleResultadoPorPuntos) {
		this.peorPosibleResultadoPorPuntos = peorPosibleResultadoPorPuntos;
	}

	public Integer getPeorPosibleResultadoPorAltura() {
		return peorPosibleResultadoPorAltura;
	}

	public void setPeorPosibleResultadoPorAltura(Integer peorPosibleResultadoPorAltura) {
		this.peorPosibleResultadoPorAltura = peorPosibleResultadoPorAltura;
	}
	
}
