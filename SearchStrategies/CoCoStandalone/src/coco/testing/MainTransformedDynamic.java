package coco.testing;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.constraints.LogicalConstraintFactory;
import org.chocosolver.solver.search.loop.monitors.IMonitorSolution;
import org.chocosolver.solver.search.loop.monitors.SearchMonitorFactory;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMin;
import org.chocosolver.solver.search.strategy.strategy.IntStrategy;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Variable;
import org.chocosolver.solver.variables.VariableFactory;
import org.chocosolver.util.objects.queues.LinkedList;

import coco.util.Util;

import org.chocosolver.solver.search.solution.ISolutionRecorder;
import org.chocosolver.solver.search.solution.Solution;
import pkg.FMVarSelectorBiVarArithmetic;
import pkg.FMVarSelectorMorePercInstVars;
import pkg.FMVarSelectorOrAttr0;
import pkg.FMVarSelectorOrAttr1;

//import com.ibm.icu.util.Measure;

public class MainTransformedDynamic {
	private Solver solver;
	private int contFeatures;
	private String[] propOptionalFeatures;
	private String[] propMandatoryFeatures;
	private String[] propAttributeTypes;
	private String[][] propFeatureAttributes;
private int tipo = 0;
	// private String[][] propFeatureAttributes0;
	// private String[][] propFeatureAttributes1;
	private String[][] propMandatoryTCs;
	private String[][] propOptionalTCs;
	private List<String>[] propOrTCs;
	private List<String>[] propAlternativeTCs;
	private String[][] propRequiresCTCs;
	private String[][] propExcludesCTCs;
	private String[][] propForcesCMCs;
	private String[][] propProhibitsCMCs;
	private HashMap<String, IntVar> features;
	// private HashMap<String, IntVar> featureAttrAttributes0;
	// private HashMap<String, IntVar> featureAttrAttributes1;
	private HashMap<String, IntVar> featureAttrAttributes;

	private Properties properties;
	private int limit_solution = 10;

	public MainTransformedDynamic(String path,int tipo) {
		// --------------------------------------------
		// Class Attributes
		// --------------------------------------------
		int N = 100;
		this.tipo = tipo;
		// 1. Modelling part

		solver = new Solver();
		
		features = new HashMap<String, IntVar>();
		// featureAttrAttributes0 = new HashMap<String, IntVar>();
		// featureAttrAttributes1 = new HashMap<String, IntVar>();
		featureAttrAttributes = new HashMap<String, IntVar>();
		try {
			properties = new Properties();
			InputStream stream = new FileInputStream(path);
			properties.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// --------------------------------------------
		// Read properties
		// --------------------------------------------
		readProperties();

		// --------------------------------------------
		// Initialize CSP
		// --------------------------------------------
		initializeCSP();
	}

	public void readProperties() {
		// --------------------------------------------
		// Features
		// --------------------------------------------
		readPropFeatures();
		// --------------------------------------------
		// Feature Attributes
		// Atributes
		// --------------------------------------------
		readAtributes();
		// readPropFeatureAttrsAttributes0();
		// readPropFeatureAttrsAttributes1();
		readPropFeatureAttrsAttributes();

		// --------------------------------------------
		// Tree Constraints
		// --------------------------------------------
		readPropMandatoryOptionalTCs();
		readPropOrTCs();
		readPropAlternativeTCs();

		// --------------------------------------------
		// Cross-Tree Constraints
		// --------------------------------------------
		readPropCTCs();

		// --------------------------------------------
		// Cross-Model Constraints
		// --------------------------------------------
		readPropCMCs();
	}

	public void readPropFeatures() {
		int mandatoryFeatures = Integer.parseInt(properties.getProperty("mandatoryFeaturesNum").trim());
		int optionalFeatures = Integer.parseInt(properties.getProperty("optionalFeaturesNum").trim());

		// contFeatures =
		// Integer.parseInt(properties.getProperty("featuresNum").trim());
		ArrayList validador_mandatoryFeats = new ArrayList();
		ArrayList validador_optionalFeats = new ArrayList();

		for (int i = 0; i < mandatoryFeatures; i++) {
			if (!validador_mandatoryFeats.contains(properties.getProperty("mandatoryFeature" + i).trim())) {
				validador_mandatoryFeats.add(properties.getProperty("mandatoryFeature" + i).trim());

			}
		}

		for (int i = 0; i < optionalFeatures; i++) {
			if (!validador_optionalFeats.contains(properties.getProperty("optionalFeature" + i).trim())) {
				validador_optionalFeats.add(properties.getProperty("optionalFeature" + i).trim());

			}
		}
		propOptionalFeatures = new String[validador_optionalFeats.size()];

		propMandatoryFeatures = new String[validador_mandatoryFeats.size()];
		for (int i = 0; i < validador_mandatoryFeats.size(); i++) {
			propMandatoryFeatures[i] = (String) validador_mandatoryFeats.get(i);
		}

		for (int i = 0; i < validador_optionalFeats.size(); i++) {
			propOptionalFeatures[i] = (String) validador_optionalFeats.get(i);
		}
		contFeatures = propMandatoryFeatures.length + propOptionalFeatures.length;
	}

	public void readAtributes() {
		int attributeTypesNum = Integer.parseInt(properties.getProperty("attributeTypesNum").trim());
		propAttributeTypes = new String[attributeTypesNum];
		for (int i = 0; i < attributeTypesNum; i++) {
			propAttributeTypes[i] = properties.getProperty("attributeType" + i).trim();
		}
	}

	public void readPropFeatureAttrsAttributes() {
		propFeatureAttributes = new String[propMandatoryFeatures.length + propOptionalFeatures.length][4];
		for (int j = 0; j < propAttributeTypes.length; j++) {

			for (int i = 0; i < propMandatoryFeatures.length; i++) {

				String key = propMandatoryFeatures[i] + "." + propAttributeTypes[j];
				try {
					String value = properties.getProperty(key).trim();
					String[] array = value.split(",");

					propFeatureAttributes[i][0] = propMandatoryFeatures[i];
					propFeatureAttributes[i][1] = array[0];
					propFeatureAttributes[i][2] = array[1];
					propFeatureAttributes[i][3] = array[2];
				} catch (Exception e) {

				}
			}
		}
		for (int j = 0; j < propAttributeTypes.length; j++) {

			for (int i = propMandatoryFeatures.length; i < propOptionalFeatures.length
					+ propMandatoryFeatures.length; i++) {

				String key = propOptionalFeatures[i - propMandatoryFeatures.length] + "." + propAttributeTypes[j];
				try {
					String value = properties.getProperty(key).trim();
					String[] array = value.split(",");

					propFeatureAttributes[i][0] = propOptionalFeatures[i - propMandatoryFeatures.length];
					propFeatureAttributes[i][1] = array[0];
					propFeatureAttributes[i][2] = array[1];
					propFeatureAttributes[i][3] = array[2];
				} catch (Exception e) {

				}
			}
		}

	}

	/*
	 * public void readPropFeatureAttrsAttributes0() { propFeatureAttributes0 = new
	 * String[contFeatures][4];
	 * System.out.println("pto control jhagsd82h___"+propMandatoryFeatures[0]+"____"
	 * ); for (int i = 0; i < propMandatoryFeatures.length; i++) { String key =
	 * propMandatoryFeatures[i] + ".Atribute0"; String value =
	 * properties.getProperty(key).trim(); String[] array = value.split(",");
	 * 
	 * propFeatureAttributes0[i][0] = propMandatoryFeatures[i];
	 * propFeatureAttributes0[i][1] = array[0]; propFeatureAttributes0[i][2] =
	 * array[1]; propFeatureAttributes0[i][3] = array[2]; }
	 * 
	 * for (int i = propMandatoryFeatures.length; i < propOptionalFeatures.length +
	 * propMandatoryFeatures.length; i++) { String key = propOptionalFeatures[i -
	 * propMandatoryFeatures.length] + ".Atribute0"; String value =
	 * properties.getProperty(key).trim(); String[] array = value.split(",");
	 * 
	 * propFeatureAttributes0[i][0] = propOptionalFeatures[i -
	 * propMandatoryFeatures.length]; propFeatureAttributes0[i][1] = array[0];
	 * propFeatureAttributes0[i][2] = array[1]; propFeatureAttributes0[i][3] =
	 * array[2]; } }
	 * 
	 * public void readPropFeatureAttrsAttributes1() { propFeatureAttributes1 = new
	 * String[contFeatures][4];
	 * 
	 * for (int i = 0; i < propMandatoryFeatures.length; i++) { String key =
	 * propMandatoryFeatures[i] + ".Atribute1"; String value =
	 * properties.getProperty(key).trim(); String[] array = value.split(",");
	 * 
	 * propFeatureAttributes1[i][0] = propMandatoryFeatures[i];
	 * propFeatureAttributes1[i][1] = array[0]; propFeatureAttributes1[i][2] =
	 * array[1]; propFeatureAttributes1[i][3] = array[2]; }
	 * 
	 * for (int i = propMandatoryFeatures.length; i < propOptionalFeatures.length +
	 * propMandatoryFeatures.length; i++) { String key = propOptionalFeatures[i -
	 * propMandatoryFeatures.length] + ".Atribute1"; String value =
	 * properties.getProperty(key).trim(); String[] array = value.split(",");
	 * 
	 * propFeatureAttributes1[i][0] = propOptionalFeatures[i -
	 * propMandatoryFeatures.length]; propFeatureAttributes1[i][1] = array[0];
	 * propFeatureAttributes1[i][2] = array[1]; propFeatureAttributes1[i][3] =
	 * array[2]; } }
	 */
	public void readPropMandatoryOptionalTCs() {
		int ands = Integer.parseInt(properties.getProperty("andsNum").trim());
		int mandatoryAnds = Integer.parseInt(properties.getProperty("mandatoryAndsNum").trim());
		int optionalAnds = Integer.parseInt(properties.getProperty("optionalAndsNum").trim());
		int countMandatory = 0;
		int countOptional = 0;

		propMandatoryTCs = new String[mandatoryAnds][2];
		propOptionalTCs = new String[optionalAnds][2];

		for (int i = 0; i < ands; i++) {
			String value = properties.getProperty("and" + i).trim();
			boolean isMandatory = value.endsWith("*");
			String[] array = value.split("\\*");

			if (isMandatory) {
				String[] features = array[0].split(":");
				propMandatoryTCs[countMandatory][0] = features[0];
				propMandatoryTCs[countMandatory][1] = features[1];
				countMandatory++;
			} else {
				String[] features = array[0].split(":");
				propOptionalTCs[countOptional][0] = features[0];
				propOptionalTCs[countOptional][1] = features[1];
				countOptional++;
			}
		}
	}

	public void readPropOrTCs() {
		int ors = Integer.parseInt(properties.getProperty("orsNum").trim());
		propOrTCs = new List[ors];

		for (int i = 0; i < ors; i++) {
			String value = properties.getProperty("or" + i).trim();
			String[] array = value.split(":");
			if (array.length > 1) {
				String[] features = array[1].split(",");

				propOrTCs[i] = new ArrayList<String>();
				propOrTCs[i].add(array[0]);

				for (int j = 0; j < features.length; j++) {
					propOrTCs[i].add(features[j]);
				}
			}
		}
	}

	public void readPropAlternativeTCs() {
		int alts = Integer.parseInt(properties.getProperty("altsNum").trim());
		propAlternativeTCs = new List[alts];

		for (int i = 0; i < alts; i++) {
			String value = properties.getProperty("alt" + i).trim();
			String[] array = value.split(":");
			String[] features = array[1].split(",");

			propAlternativeTCs[i] = new ArrayList<String>();
			propAlternativeTCs[i].add(array[0]);

			for (int j = 0; j < features.length; j++) {
				propAlternativeTCs[i].add(features[j]);
			}
		}
	}

	public void readPropCTCs() {
		int ctcs = Integer.parseInt(properties.getProperty("ctcsNum").trim());
		int requiresCtcs = Integer.parseInt(properties.getProperty("requiresCtcsNum").trim());
		int excludesCtcs = Integer.parseInt(properties.getProperty("excludesCtcsNum").trim());
		int countRequires = 0;
		int countExcludes = 0;

		propRequiresCTCs = new String[requiresCtcs][2];
		propExcludesCTCs = new String[excludesCtcs][2];

		for (int i = 0; i < ctcs; i++) {
			String value = properties.getProperty("ctc" + i).trim();
			boolean isMandatory = value.endsWith("*");
			String[] array = value.split("\\*");

			if (isMandatory) {
				String[] features = array[0].split(":");
				propRequiresCTCs[countRequires][0] = features[0];
				propRequiresCTCs[countRequires][1] = features[1];
				countRequires++;
			} else {
				String[] features = array[0].split(":");
				propExcludesCTCs[countExcludes][0] = features[0];
				propExcludesCTCs[countExcludes][1] = features[1];
				countExcludes++;
			}
		}
	}

	public void readPropCMCs() {
		int ctcs = Integer.parseInt(properties.getProperty("cmcsNum").trim());
		int forcesCmcs = Integer.parseInt(properties.getProperty("forcesCmcsNum").trim());
		int prohibitsCmcs = Integer.parseInt(properties.getProperty("prohibitsCmcsNum").trim());
		int countForces = 0;
		int countProhibits = 0;

		propForcesCMCs = new String[forcesCmcs][2];
		propProhibitsCMCs = new String[prohibitsCmcs][2];

		for (int i = 0; i < ctcs; i++) {
			String value = properties.getProperty("cmc" + i).trim();
			boolean isMandatory = value.endsWith("*");
			String[] array = value.split("\\*");

			if (isMandatory) {
				String[] features = array[0].split(":");
				propForcesCMCs[countForces][0] = features[0];
				propForcesCMCs[countForces][1] = features[1];
				countForces++;
			} else {
				String[] features = array[0].split(":");
				propProhibitsCMCs[countProhibits][0] = features[0];
				propProhibitsCMCs[countProhibits][1] = features[1];
				countProhibits++;
			}
		}
	}

	public void initializeCSP() {
		// --------------------------------------------
		// Features
		// --------------------------------------------
		initializeOptionalFeatures();
		initializeMandatoryFeatures();

		// --------------------------------------------
		// Feature Attributes
		// --------------------------------------------
		// initializeFeatureAttributes0();
		// initializeFeatureAttributes1();
		initializeFeatureAttributes();
		// --------------------------------------------
		// Tree Constraints
		// --------------------------------------------
		initializeAllMandatoryTCs();
		initializeAllOptionalTCs();
		initializeAllOrTCs();
		initializeAllAlternativeTCs();

		// --------------------------------------------
		// Cross-Tree Constraints
		// --------------------------------------------
		initializeAllRequiresCTCs();
		initializeAllExcludesCTCs();

		// --------------------------------------------
		// Cross-Model Constraints
		// --------------------------------------------
		initializeAllForcesCMCs();
		initializeAllProhibitsCMCs();
	}

	public void initializeOptionalFeatures() {
		for (int i = 0; i < propOptionalFeatures.length; i++) {
			BoolVar feature = VariableFactory.bool(propOptionalFeatures[i], solver);
			features.put(propOptionalFeatures[i], feature);
			// propOptionalFeatures[i] + "\", solver);");
		}
	}

	public void initializeMandatoryFeatures() {

		for (int i = 0; i < propMandatoryFeatures.length; i++) {
			BoolVar feature = (BoolVar) VariableFactory.fixed(propMandatoryFeatures[i], 1, solver);

			features.put(propMandatoryFeatures[i], feature);
			// System.out.println(propMandatoryFeatures[i] + " = (BoolVar)
			// VariableFactory.fixed(\"" + propMandatoryFeatures[i] + "\", 1, solver);");
		}

	}

	public void initializeFeatureAttributes() {
		@SuppressWarnings("unused")
		int xxx = 0;
		for (int j = 0; j < propAttributeTypes.length; j++) {

			for (int i = 0; i < propFeatureAttributes.length; i++) {

				String featureName = propFeatureAttributes[i][0];
				if (featureName != null) {
					int defaultValue = Integer.parseInt(propFeatureAttributes[i][1]);
					IntVar feature = features.get(featureName);

					if (defaultValue == 0) {
						featureAttrAttributes.put(featureName,
								VariableFactory.fixed(featureName + propAttributeTypes[j], defaultValue, solver));
						// System.out.println("featureAttrAtribute0.put(\"" + featureName + "\",
						// VariableFactory.fixed(\"" + featureName + "Atribute0\", " + defaultValue + ",
						// solver));");
					} else {
						featureAttrAttributes.put(featureName, VariableFactory.enumerated(
								featureName + propAttributeTypes[j], new int[] { 0, defaultValue }, solver));
						LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(feature, "=", 0),
								IntConstraintFactory.arithm(featureAttrAttributes.get(featureName), "=", 0));
						LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(feature, "=", 1),
								IntConstraintFactory.arithm(featureAttrAttributes.get(featureName), ">", 0));

						// System.out.println("featureAttrAtribute0.put(\"" + featureName + "\",
						// VariableFactory.enumerated(\"" + featureName + "Atribute0\", new int[]{0, " +
						// defaultValue + "}, solver));");
						// System.out.println("LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm("
						// + feature.getName() + ", \"=\", 0),
						// IntConstraintFactory.arithm(featureAttrAtribute0.get(\"" + feature.getName()
						// + "\"), \"=\", 0));");
						// System.out.println("LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm("
						// + feature.getName() + ", \"=\", 1),
						// IntConstraintFactory.arithm(featureAttrAtribute0.get(\"" + feature.getName()
						// + "\"), \">\", 0));");
					}
				}
			}
		}
	}

	/*
	 * public void initializeFeatureAttributes0() { for (int i = 0; i <
	 * propFeatureAttributes0.length; i++) { String featureName =
	 * propFeatureAttributes0[i][0]; int defaultValue =
	 * Integer.parseInt(propFeatureAttributes0[i][1]); IntVar feature =
	 * features.get(featureName);
	 * 
	 * if (defaultValue == 0) { featureAttrAttributes0.put(featureName,
	 * VariableFactory.fixed(featureName + "Atribute0", defaultValue, solver)); //
	 * System.out.println("featureAttrAtribute0.put(\"" + featureName + "\", //
	 * VariableFactory.fixed(\"" + featureName + "Atribute0\", " + defaultValue + ",
	 * // solver));"); } else { featureAttrAttributes0.put(featureName,
	 * VariableFactory.enumerated(featureName + "Atribute0", new int[] { 0,
	 * defaultValue }, solver));
	 * LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(feature, "=", 0),
	 * IntConstraintFactory.arithm(featureAttrAttributes0.get(featureName), "=",
	 * 0)); LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(feature,
	 * "=", 1), IntConstraintFactory.arithm(featureAttrAttributes0.get(featureName),
	 * ">", 0));
	 * 
	 * // System.out.println("featureAttrAtribute0.put(\"" + featureName + "\", //
	 * VariableFactory.enumerated(\"" + featureName + "Atribute0\", new int[]{0, " +
	 * // defaultValue + "}, solver));"); // System.out.println(
	 * "LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(" // +
	 * feature.getName() + ", \"=\", 0), //
	 * IntConstraintFactory.arithm(featureAttrAtribute0.get(\"" + feature.getName()
	 * // + "\"), \"=\", 0));"); // System.out.println(
	 * "LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(" // +
	 * feature.getName() + ", \"=\", 1), //
	 * IntConstraintFactory.arithm(featureAttrAtribute0.get(\"" + feature.getName()
	 * // + "\"), \">\", 0));"); } } }
	 * 
	 * public void initializeFeatureAttributes1() { for (int i = 0; i <
	 * contFeatures; i++) { String featureName = propFeatureAttributes1[i][0]; int
	 * defaultValue = Integer.parseInt(propFeatureAttributes1[i][1]); IntVar feature
	 * = features.get(featureName);
	 * 
	 * if (defaultValue == 0) { featureAttrAttributes1.put(featureName,
	 * VariableFactory.fixed(featureName + "Atribute1", defaultValue, solver)); //
	 * System.out.println("featureAttrAtribute1.put(\"" + featureName + "\", //
	 * VariableFactory.fixed(\"" + featureName + "Atribute1\", " + defaultValue + ",
	 * // solver));"); } else { featureAttrAttributes1.put(featureName,
	 * VariableFactory.enumerated(featureName + "Atribute1", new int[] { 0,
	 * defaultValue }, solver));
	 * LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(feature, "=", 0),
	 * IntConstraintFactory.arithm(featureAttrAttributes1.get(featureName), "=",
	 * 0)); LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(feature,
	 * "=", 1), IntConstraintFactory.arithm(featureAttrAttributes1.get(featureName),
	 * ">", 0));
	 * 
	 * // System.out.println("featureAttrAtribute1.put(\"" + featureName + "\", //
	 * VariableFactory.enumerated(\"" + featureName + "Atribute1\", new int[]{0, " +
	 * // defaultValue + "}, solver));"); // System.out.println(
	 * "LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(" // +
	 * feature.getName() + ", \"=\", 0), //
	 * IntConstraintFactory.arithm(featureAttrAtribute1.get(\"" + feature.getName()
	 * // + "\"), \"=\", 0));"); // System.out.println(
	 * "LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(" // +
	 * feature.getName() + ", \"=\", 1), //
	 * IntConstraintFactory.arithm(featureAttrAtribute1.get(\"" + feature.getName()
	 * // + "\"), \">\", 0));"); } } }
	 */
	public void initializeAllMandatoryTCs() {
		for (int i = 0; i < propMandatoryTCs.length; i++) {
			IntVar parent = features.get(propMandatoryTCs[i][0]);
			IntVar child = features.get(propMandatoryTCs[i][1]);
			initializeMandatoryTC(parent, child);
		}
	}

	public void initializeMandatoryTC(IntVar parent, IntVar child) {
		Constraint constraint = IntConstraintFactory.arithm(parent, "=", child);
		constraint.setName(Utilities.MANDATORY_TC);
		//System.out.println("MTC..."+constraint);
		solver.post(constraint);

		// System.out.println("Constraint mandatory" + parent.getName() + "_" +
		// child.getName() + " = IntConstraintFactory.arithm(" + parent.getName() + ",
		// \"=\", " + child.getName() + ");");
		// System.out.println("mandatory" + parent.getName() + "_" + child.getName() +
		// ".setName(Utilities.MANDATORY_TC);");
		// System.out.println("solver.post(mandatory" + parent.getName() + "_" +
		// child.getName() + ");");
	}

	public void initializeAllOptionalTCs() {
		for (int i = 0; i < propOptionalTCs.length; i++) {
			IntVar parent = features.get(propOptionalTCs[i][0]);
			IntVar child = features.get(propOptionalTCs[i][1]);
			initializeOptionalTC(parent, child);
		}
	}

	public void initializeOptionalTC(IntVar parent, IntVar child) {
		//System.out.println("OTC.HH.."+parent+"___"+child);

		Constraint constraint = IntConstraintFactory.arithm(parent, ">=", child);
		constraint.setName(Utilities.OPTIONAL_TC);
		//System.out.println("OTC..."+constraint);
		solver.post(constraint);

		// System.out.println("Constraint optional" + parent.getName() + "_" +
		// child.getName() + " = IntConstraintFactory.arithm(" + parent.getName() + ",
		// \">=\", " + child.getName() + ");");
		// System.out.println("optional" + parent.getName() + "_" + child.getName() +
		// ".setName(Utilities.OPTIONAL_TC);");
		// System.out.println("solver.post(optional" + parent.getName() + "_" +
		// child.getName() + ");");
	}

	public void initializeAllOrTCs() {
		for (int i = 0; i < propOrTCs.length; i++) {
			List<String> involvedFeatures = propOrTCs[i];
			if (involvedFeatures != null) {
				IntVar parent = features.get(involvedFeatures.get(0));
				IntVar[] children = new IntVar[involvedFeatures.size() - 1];

				for (int j = 0; j < children.length; j++) {
					children[j] = features.get(involvedFeatures.get(j + 1));
				}

				initializeOrTC(parent, children);
			}
		}
	}

	public void initializeOrTC(IntVar parent, IntVar[] children) {
		IntVar sumOr = VariableFactory.enumerated("sumOr" + parent.getName() + children.length, 0, children.length,
				solver);
		BoolVar[] varsOr = new BoolVar[children.length];

		// System.out.println("IntVar sumOr = VariableFactory.enumerated(\"sumOr" +
		// parent.getName() + children.length + "\", 0, " + children.length + ",
		// solver); ");
		// System.out.println("BoolVar[] varsOr = new BoolVar[" + children.length +
		// "];");

		for (int i = 0; i < children.length; i++) {
			varsOr[i] = (BoolVar) children[i];
			// System.out.println("varsOr[" + i + "] = (BoolVar) " + children[i].getName() +
			// ";");
		}
		//System.out.println("OTC1..."+varsOr+"_____"+sumOr);

		solver.post(IntConstraintFactory.sum(varsOr, sumOr));

		Constraint constraint1 = IntConstraintFactory.arithm(sumOr, ">=", 1);
		constraint1.setName(Utilities.OR_TC);
		Constraint constraint0 = IntConstraintFactory.arithm(sumOr, "=", 0);
		constraint0.setName(Utilities.OR_TC);

		LogicalConstraintFactory.ifThenElse((BoolVar) parent, constraint1, constraint0);

		// System.out.println("solver.post(IntConstraintFactory.sum(varsOr, sumOr));");
		// System.out.println("Constraint constraint1 =
		// IntConstraintFactory.arithm(sumOr, \">=\", 1);");
		// System.out.println("constraint1.setName(Utilities.OR_TC);");
		// System.out.println("Constraint constraint0 =
		// IntConstraintFactory.arithm(sumOr, \"=\", 0);");
		// System.out.println("constraint0.setName(Utilities.OR_TC);");
		// System.out.println("LogicalConstraintFactory.ifThenElse((BoolVar) " +
		// parent.getName() + ", constraint1, constraint0);");
	}

	public void initializeAllAlternativeTCs() {
		for (int i = 0; i < propAlternativeTCs.length; i++) {
			List<String> involvedFeatures = propAlternativeTCs[i];
			IntVar parent = features.get(involvedFeatures.get(0));
			IntVar[] children = new IntVar[involvedFeatures.size() - 1];

			for (int j = 0; j < children.length; j++) {
				children[j] = features.get(involvedFeatures.get(j + 1));
			}

			initializeAlternativeTC(parent, children);
		}
	}

	public void initializeAlternativeTC(IntVar parent, IntVar[] children) {
		IntVar sumAlt = VariableFactory.enumerated("sumAltroot1_root", 0, 1, solver);
		BoolVar[] varsAlt = new BoolVar[children.length];

		for (int i = 0; i < children.length; i++) {
			varsAlt[i] = (BoolVar) children[i];
		}
		//System.out.println("ATC..."+varsAlt+"_____"+sumAlt);

		solver.post(IntConstraintFactory.sum(varsAlt, sumAlt));

		Constraint constraint1 = IntConstraintFactory.arithm(sumAlt, "=", 1);
		constraint1.setName(Utilities.XOR_TC);
		Constraint constraint0 = IntConstraintFactory.arithm(sumAlt, "=", 0);
		constraint0.setName(Utilities.XOR_TC);

		LogicalConstraintFactory.ifThenElse((BoolVar) parent, constraint1, constraint0);
	}

	public void initializeAllRequiresCTCs() {
		for (int i = 0; i < propRequiresCTCs.length; i++) {
			IntVar feature1 = features.get(propRequiresCTCs[i][0]);
			IntVar feature2 = features.get(propRequiresCTCs[i][1]);
			initializeRequiresCTC(feature1, feature2);
		}
	}

	public void initializeRequiresCTC(IntVar feature1, IntVar feature2) {
		Constraint constraint = IntConstraintFactory.arithm(feature1, "<=", feature2);
		constraint.setName(Utilities.REQUIRES_CTC);
//System.out.println("R_CTC::"+constraint);
		solver.post(constraint);

		// System.out.println("Constraint ctc" + feature1.getName() + "_" +
		// feature2.getName() + " = IntConstraintFactory.arithm(" + feature1.getName() +
		// ", \"<=\", " + feature2.getName() + ");");
		// System.out.println("ctc" + feature1.getName() + "_" + feature2.getName() +
		// ".setName(Utilities.REQUIRES_CTC);");
		// System.out.println("solver.post(ctc" + feature1.getName() + "_" +
		// feature2.getName() + ");");
	}

	public void initializeAllExcludesCTCs() {
		for (int i = 0; i < propExcludesCTCs.length; i++) {
			IntVar feature1 = features.get(propExcludesCTCs[i][0]);
			IntVar feature2 = features.get(propExcludesCTCs[i][1]);
			initializeExcludesCTC(feature1, feature2);
		}
	}

	public void initializeExcludesCTC(IntVar feature1, IntVar feature2) {
		Constraint constraint = IntConstraintFactory.arithm(feature1, "+", feature2, "<=", 1);
		constraint.setName(Utilities.EXCLUDES_CTC);
		//System.out.println("E_CTC::"+constraint);

		solver.post(constraint);
	}

	public void initializeAllForcesCMCs() {
		for (int i = 0; i < propForcesCMCs.length; i++) {
			IntVar feature1 = features.get(propForcesCMCs[i][0]);
			IntVar feature2 = features.get(propForcesCMCs[i][1]);
			initializeRequiresCTC(feature1, feature2);
		}
	}

	public void initializeForcesCMC(IntVar feature1, IntVar feature2) {
		Constraint constraint = IntConstraintFactory.arithm(feature1, "<=", feature2);
		constraint.setName(Utilities.REQUIRES_CMC);
		//System.out.println("F_CMC::"+constraint);

		solver.post(constraint);
	}

	public void initializeAllProhibitsCMCs() {
		for (int i = 0; i < propProhibitsCMCs.length; i++) {
			IntVar feature1 = features.get(propProhibitsCMCs[i][0]);
			IntVar feature2 = features.get(propProhibitsCMCs[i][1]);
			initializeExcludesCTC(feature1, feature2);
		}
	}

	public void initializeProhibitsCMC(IntVar feature1, IntVar feature2) {
		Constraint constraint = IntConstraintFactory.arithm(feature1, "+", feature2, "<=", 1);
		constraint.setName(Utilities.EXCLUDES_CMC);
		//System.out.println("P_CMC::"+constraint);

		solver.post(constraint);
	}
int number_solutions = 10;
	public void solveCSP(int heuristica, int number_solutions1) {
		this.number_solutions = number_solutions1;
		List<IntVar> varsAtributeList = new ArrayList<IntVar>(featureAttrAttributes.values());
		IntVar[] varsAtribute = new IntVar[featureAttrAttributes.values().size()];

		for (int i = 0; i < varsAtributeList.size(); i++) {
			varsAtribute[i] = varsAtributeList.get(i);
		}
		
		// List<IntVar> varsAtribute1List = new
		// ArrayList<IntVar>(featureAttrAttributes.values());
		// IntVar[] varsAtribute1 = new IntVar[featureAttrAttributes.values().size()];

		// for (int i = 0; i < varsAtribute1List.size(); i++) {
		// System.out.print(varsAtribute1List.get(i)+";");
		// varsAtribute1[i] = VariableFactory.minus(varsAtribute1List.get(i));
		// }

		IntVar[] totalVars = new IntVar[propAttributeTypes.length];
		for (int j = 0; j < propAttributeTypes.length; j++) {

			IntVar totalAtribute;
			// if (j == 0) {
			totalAtribute = VariableFactory.bounded(propAttributeTypes[j], 0, 1000000, solver);
			//System.out.println("P_CMC__"+j+":	"+propAttributeTypes[j]+"__"+varsAtribute.length+"___"+totalAtribute);
			// } else {
			// totalAtribute = VariableFactory.bounded(propAttributeTypes[j], -1000000, 0,
			// solver);

			// }
			solver.post(IntConstraintFactory.sum(varsAtribute, totalAtribute));
			// IntVar totalAtribute1 = VariableFactory.bounded("totalAtribute1", -1000000,
			// 0, solver);
			// solver.post(IntConstraintFactory.sum(varsAtribute1, totalAtribute1));
			totalVars[j] = totalAtribute;
		}
		IntVar[] featureVars = getFeatureVars(solver, contFeatures);
		IntVar[] attributeVars = getAttributeVars(solver);
		IntStrategy strategy1, strategy2, strategy3, strategy4, strategy5;

		switch (heuristica) {
		// HEURISTICA 1

		case 1:
			strategy1 = IntStrategyFactory.custom(new FMVarSelectorMorePercInstVars(),
					IntStrategyFactory.max_value_selector(), featureVars);
			strategy2 = IntStrategyFactory.custom(IntStrategyFactory.minDomainSize_var_selector(), new IntDomainMin(),
					attributeVars);
			strategy3 = IntStrategyFactory.custom(IntStrategyFactory.minDomainSize_var_selector(), new IntDomainMin(),
					totalVars);
			solver.set(IntStrategyFactory.sequencer(strategy1, IntStrategyFactory.domOverWDeg(featureVars, 1)),
					strategy2, strategy3);
			break;
		// Chatterbox.showSolutions(solver);
		case 2:
			// HEURISTICA 2
			strategy1 = IntStrategyFactory.custom(new FMVarSelectorBiVarArithmetic(),
					IntStrategyFactory.max_value_selector(), featureVars);
			strategy2 = IntStrategyFactory.custom(IntStrategyFactory.minDomainSize_var_selector(), new IntDomainMin(),
					attributeVars);
			strategy3 = IntStrategyFactory.custom(IntStrategyFactory.minDomainSize_var_selector(), new IntDomainMin(),
					totalVars);
			solver.set(IntStrategyFactory.sequencer(strategy1, IntStrategyFactory.domOverWDeg(featureVars, 1)),
					strategy2, strategy3);
			break;
		// Chatterbox.showSolutions(solver);
		case 3:
			// HEURISTICA 3
			strategy1 = IntStrategyFactory.custom(new FMVarSelectorBiVarArithmetic(),
					IntStrategyFactory.max_value_selector(), featureVars);
			strategy2 = IntStrategyFactory.custom(new FMVarSelectorOrAttr0(), IntStrategyFactory.max_value_selector(),
					featureVars);
			strategy3 = IntStrategyFactory.custom(new FMVarSelectorOrAttr1(), IntStrategyFactory.max_value_selector(),
					featureVars);
			strategy4 = IntStrategyFactory.custom(IntStrategyFactory.minDomainSize_var_selector(), new IntDomainMin(),
					attributeVars);
			strategy5 = IntStrategyFactory.custom(IntStrategyFactory.minDomainSize_var_selector(), new IntDomainMin(),
					totalVars);
			solver.set(IntStrategyFactory.sequencer(strategy1, strategy2, strategy3,
					IntStrategyFactory.domOverWDeg(featureVars, 1)), strategy4, strategy5);
			break;
		//
		}
		//for (int j = 0; j < totalVars.length; j++) {
			//System.out.println("RECUENTOOO::::"+j+"____"+totalVars[j]);
		//}
		switch(tipo) {
		case 0:
			solver.findAllSolutions();
			break;
		case 1:
			SearchMonitorFactory.limitSolution(solver, number_solutions);

			solver.findParetoFront(ResolutionPolicy.MINIMIZE, totalVars[0],totalVars[1]);
			break;
		}



		//System.out.print("" + solver.getNbVars() + ";" + solver.getNbCstrs() + ";");
		//Chatterbox.printCSVStatistics(solver);
		Chatterbox.printStatistics(solver);
		
		
	}

	

	private IntVar[] getFeatureVars(Solver solver, int contFeatures) {
		IntVar[] featureVars = features.values().toArray(new IntVar[contFeatures]);
		return featureVars;
	}

	private IntVar[] getAttributeVars(Solver solver) {
		IntVar[] attributeVars = featureAttrAttributes.values()
				.toArray(new IntVar[featureAttrAttributes.values().size()]);
		// IntVar[] attributeVars1 = featureAttrAttributes.values().toArray(new
		// IntVar[featureAttrAttributes.values().size()]);
		// IntVar[] attributeVars = new IntVar[attributeVars0.length +
		// attributeVars1.length];

		// System.arraycopy(attributeVars0, 0, attributeVars, 0, attributeVars0.length);
		// System.arraycopy(attributeVars1, 0, attributeVars, attributeVars0.length,
		// attributeVars1.length);

		return attributeVars;
	}

	public static void main(String[] args) {
		System.gc();
		//String ruta = "C:\\\\Users\\\\Asistente\\\\Documents\\\\InvestIT_SPL\\\\CoCoStandalone\\\\workflow\\\\cocoModel_3_1.properties";
		 //"C:\\Users\\Asistente\\Documents\\InvestIT_SPL\\CoCoStandalone\\models\\properties_real\\cocoModel_100_1.properties";
		// "C:\\Users\\Asistente\\Documents\\InvestIT_SPL\\CoCoStandalone\\workflow\\cocoModel_modelos_reales.properties");
		 //"C:\\Users\\Asistente\\Documents\\InvestIT_SPL\\CoCoStandalone\\models\\properties\\cocoModel_40_11.properties";// + 
		 //"C:\\Users\\Asistente\\Documents\\InvestIT_SPL\\CoCoStandalone\\workflow/cocoModel_exp4_1.properties";

MainTransformedDynamic mt = new MainTransformedDynamic(Util.PATH_PROPERTIES + "/" + "cocoModel_3_1.properties",1);
		
		mt.solveCSP(0,10);
		/*MainTransformedDynamic mt2 = new MainTransformedDynamic(ruta);
		System.out.println("HEURISTICA 2");
		mt2.solveCSP(2);
		MainTransformedDynamic mt3 = new MainTransformedDynamic(ruta);

		System.out.println("HEURISTICA 3");
		mt3.solveCSP(3);
		MainTransformedDynamic mtd = new MainTransformedDynamic(ruta);

		System.out.println("HEURISTICA DEFAULT");
		mtd.solveCSP(4);
		*/
	}
}
