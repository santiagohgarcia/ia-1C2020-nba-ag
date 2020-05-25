package com.mycompany.nbagenetic.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class PlainTextUtils {

	private BufferedReader input;
	private FileWriter outFile;
	private String rutaPlainText = "";
	private PrintWriter out;

	public PlainTextUtils(){
	}

	public PlainTextUtils(String rutaPlainText){
		this.rutaPlainText = rutaPlainText;
	}

	//fopen
	//appendar=false => truncar archivo existente
	//appendar=true => agregar a archivo existente
	public boolean fopen(String rutaPlainText,boolean abrirExistente){

		try{
			File f = new File(rutaPlainText);
			if(f.exists()){
				input =  new BufferedReader(new FileReader(rutaPlainText));
				outFile = new FileWriter(rutaPlainText,abrirExistente);
				out = new PrintWriter(outFile);
			}
			else {
				outFile = new FileWriter(rutaPlainText,abrirExistente);
				out = new PrintWriter(outFile);
				input =  new BufferedReader(new FileReader(rutaPlainText));
			}

			return true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}

	//readline
	public String fgets(){
		try{
			return input.readLine();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	//writeline
	public String fputs(String linea){
		try{
			out.println(linea);
			out.flush();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}


	public String readAll(){
		StringBuffer allText = new StringBuffer();
		allText.append("");
		boolean error = false;
		try{
			input =  new BufferedReader(new FileReader(rutaPlainText));
			String linea = "";
			while (( linea = input.readLine()) != null){
				allText.append(linea + System.getProperty("line.separator"));
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
			error = true;
		}finally{
			try{
				input.close();
			}catch(Exception ex){
				ex.printStackTrace();
				error = true;
			}
		}
		if(error)return "";
		else 
			return allText.toString();
	}


	//fclose
	public boolean fclose(){
		try{
			input.close();
			out.close();
			outFile.close();
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}

}

