package coco.testing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class arreglo_logs {
public static void main (String ar[]) throws Exception {
	String archivo = "C:\\Users\\Asistente\\Documents/logs.txt";
	String cadena;
	String salida;
    FileReader f = new FileReader(archivo);
    BufferedReader b = new BufferedReader(f);
    while((cadena = b.readLine())!=null) {
    	if(cadena.contains("1;coco") || cadena.contains("2;coco") || cadena.contains("3;coco") || cadena.contains("0;coco")) {
        System.out.print(cadena);
    	}else 	if(!cadena.contains("EXCLUDES") && !cadena.contains("REQUIRES")) {
            System.out.println(cadena);

    	}
    }
    b.close();
}
}
