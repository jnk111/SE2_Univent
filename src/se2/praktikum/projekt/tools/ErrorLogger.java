package se2.praktikum.projekt.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import se2.praktikum.projekt.datenimexport.Folders;

public class ErrorLogger {
	
	
	public static synchronized void log(Exception e){
		
		try {
			
			File log = new File(Folders.ERROR_LOG + "/" + "error_log.err");
			FileOutputStream fos = new FileOutputStream(log, true);
			PrintStream logger = new PrintStream(fos);
			logger.println(new Date().toString());
			logger.println("====================================================================");
			e.printStackTrace(logger);
			logger.println();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
