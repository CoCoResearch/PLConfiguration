package coco.statistics;

import java.io.*; // for File
import java.util.*; // for Scanner

import coco.modifiers.AddFaMaFMModifier;
import coco.modifiers.IModifier;
import coco.testing.MainTransformed;
import coco.testing.MainTransformedDynamic;
import coco.util.Util;

public class FileCrawler {
	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
		System.out.print("Directory to crawl? ");
		String directoryName = console.nextLine();

		File f = new File(directoryName);
		crawl(f);
	}
static int heuristica;
static int tipo;
	public FileCrawler(String directory, int heuristica,int tipo) {
		
		super();
		if(tipo != 1 && tipo != 0) {
			System.out.println("Parametro 'tipo' invalido (1,0)");
			return;
		}
		if(heuristica != 1 && heuristica != 0 && heuristica != 2 && heuristica != 3) {
			System.out.println("Parametro 'heuristica' invalido (0,1,2,3,4)");
			return;
		}
		this.heuristica = heuristica; 
		this.tipo = tipo;
		File f = new File(directory);
		crawl(f);
		// TODO Auto-generated constructor stub
	}

	// Prints the given file/directory and any files/directories inside it,
	// starting with zero indentation.
	// Precondition: f != null and f exists
	public static void crawl(File f) {
		crawl(f, "");
		System.out.println("acabo!!!!!:" + numero);
	}

	static int numero = 0;

	// This recursive "helper" method prints the given file/directory and any
	// files/directories inside it at the given level of indentation.
	// Precondition: f != null and f exists
	private static int crawl(File f, String indent) {
		int salida = 0;
		Transformador tr = new Transformador();

		if (f.isDirectory()) {
			salida = 0;

			// recursive case: directory
			// print everything in the directory
			File[] subFiles = f.listFiles();
			indent += "    ";
			String cmc = "";
			Extractor_cmc extractor = new Extractor_cmc();
			String arch_destino = extraer_ruta(f.getAbsolutePath());
			String ruta_destino = f.getAbsolutePath();
			System.out.println("inicio 123131c proceso para:" + subFiles.length);
			
			for (int i = 0; i < subFiles.length; i++) {//ITERACION 0 PARA GENERAR EL AFM2COCO
				if(subFiles[i].getName().contains(".afm")) {
				IModifier modifier = new AddFaMaFMModifier();
				modifier.modifyFSG("", f.getAbsolutePath() + "/" + subFiles[i].getName(), true);
				}
			}
			for (int i = 0; i < subFiles.length; i++) {// PRIMERA ITERACION PARA ENCONTRAR EL XMI Y EXTRAER LOS CMC
				int sal = crawl(subFiles[i], indent);
				System.out.println("co trol1::" + sal + "________" + arch_destino);
				if (sal == 2 && !subFiles[i].getName().contains(arch_destino)
						&& subFiles[i].getName().contains("coco")) {
					String archivo_xmi = subFiles[i].getName();
					try {
						System.out.println("co trol222::" + f.getAbsolutePath() + "/" + subFiles[i].getName());

						cmc = extractor.extractor_cmc(f.getAbsolutePath() + "/" + subFiles[i].getName());
						System.out.println("co trol333::" + cmc);

						break;
					} catch (Exception e) {
						System.out.println("error en extractor: continuemos por favor:" + ruta_destino);
					}

				}
			}
			// en este punto ya cuenta con los cmc de la carpeta especificada.
			boolean encontro = false;
			subFiles = f.listFiles();
			String ruta_conversion = ruta_destino + "/" + arch_destino;
			for (int i = 0; i < subFiles.length; i++) {// SEGUNDA ITERACION PARA ENCONTRA LOS AFM2COCO Y CONVERTIRLOS
				int sal = crawl(subFiles[i], indent);
				if (sal == 1) {
					// DESDE ACA DEBE LLAMAR A LA CLASE DEL MAIN
					System.out.println("........................" + f.getAbsolutePath());
					Transformador.afm2coco_2_xmi(f.getAbsolutePath(), subFiles[i].getName(), ruta_destino,
							arch_destino);
					encontro = true;

				}

			}
			if (encontro) {
				try {
					Transformador.generador_SolutionC(ruta_destino, arch_destino);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					System.out.println("error en el dsl... continuemos por favor:" + ruta_conversion);
				}
				try {
					extractor.append_cmc(ruta_conversion, cmc);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					System.out.println("error en el append... continuemos por favor:" + ruta_conversion);
				}

				// try {
				String[] arch_destino_props = arch_destino.split("\\.");
				System.out.println(ruta_conversion + "_____________" + arch_destino_props[0]);
				String ruta_pro = Transformador.coco2properties(ruta_conversion, arch_destino_props[0]);
				System.out.println("..."+ruta_pro+".......ok.." + ruta_conversion);
				
				
				try {
				MainTransformed mt = new MainTransformed(ruta_pro,tipo);
				//MainTransformedDynamic mt = new MainTransformedDynamic(Util.PATH_PROPERTIES + "/" + archivo);
				
				//mt.solveCSP(heuristica,sols);
				mt.solveCSP(heuristica);
				} catch (Exception e) {
					MainTransformedDynamic mt = new MainTransformedDynamic(ruta_pro,tipo);
					
					mt.solveCSP(heuristica,10);
					// System.out.println("error en coco2properties... continuemos por favor:" +
				// ruta_conversion);
				}
			}
		} else {
			if (f.getName().contains(".afm2coco")) {
				salida = 1;
			}
			if (f.getName().contains(".xmi")) {
				salida = 2;
			}
		}
		return salida;
	}

	public static HashMap extraer_info(String ruta_conversion) {
		HashMap map = new HashMap();
		String[] doc = ruta_conversion.split("/");
		String pparte = "";
		String sparte = "";
		String[] rutaaaa = doc[0].split("[^a-z0-9]");
		String pnum = "", snum = "";
		for (int w = 0; w < rutaaaa.length; w++) {
			if (w == rutaaaa.length - 2)
				pnum = rutaaaa[w];
			if (w == rutaaaa.length - 1)
				snum = rutaaaa[w];

		}
		for (int w = 0; w < doc.length; w++) {

			if (w < doc.length - 1) {
				pparte += doc[w] + "/";
			} else {
				sparte += doc[w];
			}
		}
		map.put("pparte", pparte);
		map.put("sparte", sparte);
		String[] nomarch = sparte.split("\\.");
		numero++;
		String archivo = numero + "_" + pnum + "_" + snum + "_" + nomarch[0];
		map.put("archivo", archivo);
		return map;

	}

	public static String extraer_ruta(String ruta) {
		String[] doc = ruta.split("/");

		String[] rutaaaa = doc[0].split("[^a-z0-9]");
		String pnum = "", snum = "";
		for (int w = 0; w < rutaaaa.length; w++) {
			if (w == rutaaaa.length - 2)
				pnum = rutaaaa[w];
			if (w == rutaaaa.length - 1)
				snum = rutaaaa[w];

		}
		return "cocoModel_" + pnum + "_" + snum + ".xmi";
	}
}
