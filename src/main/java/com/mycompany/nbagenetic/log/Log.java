package com.mycompany.nbagenetic.log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

	
	public static void loguear(String txt) {
		String info = "";
		info = new SimpleDateFormat("hh:mm:ss dd/MM/YYYY").format(new Date());
		info += ">>>";
			info += txt;
		System.out.println(info);
	}
	
	
}
