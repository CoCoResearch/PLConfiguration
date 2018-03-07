package coco.testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import coco.util.Util;

@RunWith(value = Parameterized.class)
public class Generador_modelos {
	@Parameters
	public static Iterable<Object[]> getData() {
		Check_files files = new Check_files();

		return files.files();
	}

	public Generador_modelos(String archivo, int soluciones,int num_sols ) {
		this.archivo = archivo;
		this.heuristica = soluciones;
		this.num_sols = num_sols;
	}

	private String archivo;
	private int heuristica;
private int num_sols;
	@Test
	public void test() {
		int opc = 2;
		String features = "";
		
			int sols = (num_sols*10);
		if (opc == 1) {
			String data[] = archivo.split("_");
			features = data[1];
			String[] combinacion_1 = data[2].split("\\.");
			int combinacion = Integer.parseInt(combinacion_1[0]) + 10;
			System.out.print(sols+";"+heuristica + ";" + archivo + ";" + features + ";" + combinacion + ";");
		} else {
			String data[] = archivo.split("coco");
			String data2[] =  data[1].split("_");
			features = data2[0];
			System.out.print(sols+";"+heuristica + ";" + archivo + ";" + features + ";" + data2[1] + ";");
		}
		
		MainTransformed mt = new MainTransformed(Util.PATH_PROPERTIES+"/"+archivo,1);
		//MainTransformedDynamic mt = new MainTransformedDynamic(Util.PATH_PROPERTIES + "/" + archivo);
		
		//mt.solveCSP(heuristica,sols);
		mt.solveCSP(heuristica);
	}

	@Before
	public void cleaner() {

		System.gc();
	}

}
