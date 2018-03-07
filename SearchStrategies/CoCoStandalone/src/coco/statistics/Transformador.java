package coco.statistics;

import coco.util.AntExecutor;
import coco.util.Util;
import coco.util.XtextModelManager;

public class Transformador {
	int dato = 0;
public static void main(String args[]) {
	System.out.println("salidaaaaa");
	//String ruta = afm2coco_2_xmi("C:\\Users\\Asistente\\Documents\\InvestIT_SPL\\CoCoStandalone\\models\\modelosGenerados\\experimento1\\1280\\11", "FeatureModel13.afm2coco");
	/*String[] doc = ruta.split("/");
	String pparte = "";
	String sparte = "";
	for(int i = 0;i < doc.length;i++) {
		if(i<doc.length-1) {
			pparte+=doc[i]+"/";
		}else {
			sparte+=doc[i];
		}
	}*/
	//System.out.println("pa:"+pparte+"_____sa:"+sparte);
	//generador_SolutionC(pparte, sparte);
	//coco2properties(ruta,"archivo");
	coco2properties("C:/Users/Asistente/Documents/InvestIT_SPL/CoCoStandalone/models/modelosGenerados/experimento 3/1/cocoModel_3_1.xmi","cocoModel_3_1.xmi");
    //coco2properties("C:\\Users\\Asistente\\Documents\\InvestIT_SPL\\CoCoStandalone\\models\\modelosGenerados\\experimento1\\320\\21\\coco21.xmi");
	
	
	//String ruta = splot_2_xmi("C:\\Users\\Asistente\\Documents\\InvestIT_SPL\\tests", "TEST.splot2coco");
	//String ruta = splot_2_xmi("C:\\Users\\Asistente\\Documents\\InvestIT_SPL\\CoCoStandalone\\models\\test_2plot2cocos","prueba.splot_to_coco");
	//String ruta = splot_2_xmi("C:\\Users\\Asistente\\Documents\\InvestIT_SPL\\CoCoStandalone\\models\\modelosGenerados\\experimento1\\1280\\11", "FeatureModel13.afm2coco");
	
	//String rut = afm2coco_2_xmi("C:\\Users\\Asistente\\Documents\\InvestIT_SPL\\tests", "TEST.afm2coco");

}
	public Transformador() {
		super();
	}
public static String splot_2_xmi(String ruta, String archivo) {
		
	XtextModelManager xtextManager = new XtextModelManager(ruta+"\\"+archivo);
	xtextManager.loadXtextModelAsEcoresplot2CoCo();
		String[][] properties1 = new String[2][2];
		properties1[0][0] = "splotModel";
		properties1[0][1] = ruta+"\\"+archivo;
		properties1[1][0] = "cocoModel";
		String[] new_name = archivo.split("\\.");
		properties1[1][1] = ruta+"/"+new_name[0]+".xmi";
		AntExecutor antExecutor1 = new AntExecutor("workflow/build-splot2cocoM2M.xml", properties1);
		return ruta+"/"+new_name[0]+".xmi";
		
	}
public static String afm2coco_2_xmi(String ruta, String archivo,String ruta_salida, String archivo_salida) {
	System.out.println("lalalalaooookkkiiiioooo........"+ruta);

	XtextModelManager xtextManager = new XtextModelManager(ruta+"\\"+archivo);
	System.out.println("lalalalaooookkkiiiioooo........"+archivo);

	xtextManager.loadXtextModelAsEcoreAfm2CoCo();
	System.out.println("lalalala    o........"+archivo);

	String[][] properties1 = new String[2][2];
	properties1[0][0] = "afm2cocoModel";
	properties1[0][1] = ruta+"\\"+archivo;
	properties1[1][0] = "cocoModel";
	String[] new_name = archivo.split("\\.");
	properties1[1][1] = ruta_salida+"/"+archivo_salida;
	System.out.println("lalalalao4444444oo........"+archivo);

	AntExecutor antExecutor1 = new AntExecutor("workflow/build-fama2cocoM2M.xml", properties1);
	return properties1[1][1];
	
}
	public static String afm2coco_2_xmi(String ruta, String archivo) {
		
		XtextModelManager xtextManager = new XtextModelManager(ruta+"\\"+archivo);
		xtextManager.loadXtextModelAsEcoreAfm2CoCo();
		String[][] properties1 = new String[2][2];
		properties1[0][0] = "afm2cocoModel";
		properties1[0][1] = ruta+"\\"+archivo;
		properties1[1][0] = "cocoModel";
		String[] new_name = archivo.split("\\.");
		properties1[1][1] = ruta+"/"+new_name[0]+".xmi";
		AntExecutor antExecutor1 = new AntExecutor("workflow/build-fama2cocoM2M.xml", properties1);
		return ruta+"/"+new_name[0]+".xmi";
		
	}
	public static void generador_SolutionC(String ruta, String archivo) {
		String ruta_metamodelo = Util.PATH_METAMODELOS + "/configConstraints.test7";
		XtextModelManager xtextManager = new XtextModelManager(ruta_metamodelo);
		xtextManager.loadXtextModelAsEcoreCoCo();
		String[][] properties = new String[2][2];
		properties[0][0] = "cocoModel";
		properties[0][1] = ruta+"\\"+archivo;
		properties[1][0] = "cocoDSLModel";
		properties[1][1] = ruta_metamodelo;
		AntExecutor antExecutor = new AntExecutor("workflow/fama2coco/build-cocoDSL2cocoM2M.xml", properties);
		
	}
	public static String coco2properties(String ruta , String archivo) {
		String[][] properties = new String[2][2];
		properties[0][0] = "cocoModel";
		properties[0][1] = ruta;
		properties[1][0] = "cocoCP";
		properties[1][1] = archivo+".properties";
		System.out.println(ruta+"/"+properties[1][1]);
		if(true) {//entra aca cuando tiene atributos
		AntExecutor antExecutor = new AntExecutor("workflow/build-coco2propertiesM2T.xml", properties);
		}else {
			
//			AntExecutor antExecutor = new AntExecutor("workflow/build-coco2propertiesM2T2_old2.xml", properties);
			AntExecutor antExecutor = new AntExecutor("workflow/build-coco2propertiesM2T2_old3.xml", properties);
			
		}
		return "workflow/"+archivo+".properties";
	}


}
