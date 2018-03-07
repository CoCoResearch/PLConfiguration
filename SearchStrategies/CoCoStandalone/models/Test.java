
package generated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.constraints.LogicalConstraintFactory;
import org.chocosolver.solver.constraints.SatFactory;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;
import org.chocosolver.solver.search.loop.monitors.SMF;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.search.strategy.strategy.IntStrategy;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Variable;
import org.chocosolver.solver.variables.VariableFactory;
import generated.util.CSPUtil;

public class CSPModel {
	private static Solver solver;
	private static BoolVar root1_root;
	private static BoolVar root1_F1;
	private static BoolVar root1_F2;
	private static BoolVar root1_F3;
	private static BoolVar root1_F9;
	private static BoolVar root1_F4;
	private static BoolVar root1_F5;
	private static BoolVar root1_F6;
	private static BoolVar root1_F7;
	private static BoolVar root1_F8;
		private static HashMap<String, IntVar> featureAttrAtribute1;
		private static HashMap<String, IntVar> featureAttrAtribute0;
	
	public static void main(String[] args) {
		solver = new Solver();
		
		//--------------------------------------------
		//Features
		//--------------------------------------------
		initializeVars();
		
		//--------------------------------------------
		//Feature Attributes
		//--------------------------------------------
		initializeFeatureAttributes();
		
		//--------------------------------------------
		//Tree Constraints
		//--------------------------------------------
		Constraint mandatoryroot1_root_root1_F1 = IntConstraintFactory.arithm(root1_root, "=", root1_F1);
		mandatoryroot1_root_root1_F1.setName(Utilities.MANDATORY_TC);
		solver.post(mandatoryroot1_root_root1_F1);
		Constraint mandatoryroot1_root_root1_F2 = IntConstraintFactory.arithm(root1_root, "=", root1_F2);
		mandatoryroot1_root_root1_F2.setName(Utilities.MANDATORY_TC);
		solver.post(mandatoryroot1_root_root1_F2);
		Constraint mandatoryroot1_root_root1_F3 = IntConstraintFactory.arithm(root1_root, "=", root1_F3);
		mandatoryroot1_root_root1_F3.setName(Utilities.MANDATORY_TC);
		solver.post(mandatoryroot1_root_root1_F3);
		Constraint optionalroot1_F3_root1_F9 = IntConstraintFactory.arithm(root1_F9, "<=", root1_F3);
		optionalroot1_F3_root1_F9.setName(Utilities.OPTIONAL_TC);
		solver.post(optionalroot1_F3_root1_F9);
		Constraint mandatoryroot1_root_root1_F4 = IntConstraintFactory.arithm(root1_root, "=", root1_F4);
		mandatoryroot1_root_root1_F4.setName(Utilities.MANDATORY_TC);
		solver.post(mandatoryroot1_root_root1_F4);
		Constraint mandatoryroot1_root_root1_F5 = IntConstraintFactory.arithm(root1_root, "=", root1_F5);
		mandatoryroot1_root_root1_F5.setName(Utilities.MANDATORY_TC);
		solver.post(mandatoryroot1_root_root1_F5);
		IntVar sumOrroot1_root_5 = VariableFactory.enumerated("sumOrroot1_root", 0, 2, solver); 
		BoolVar[] varsOrroot1_root_5 = new BoolVar[2];
		varsOrroot1_root_5[0] = root1_F6;
		varsOrroot1_root_5[1] = root1_F7;
		solver.post(IntConstraintFactory.sum(varsOrroot1_root_5, sumOrroot1_root_5));
		Constraint or1root1_root_5 = IntConstraintFactory.arithm(sumOrroot1_root_5, ">=", 1);
		or1root1_root_5.setName(Utilities.OR_TC);
		
		Constraint or0root1_root_5 = IntConstraintFactory.arithm(sumOrroot1_root_5, "=", 0);
		or0root1_root_5.setName(Utilities.OR_TC);
		LogicalConstraintFactory.ifThenElse(root1_root, or1root1_root_5, or0root1_root_5);
			
		Constraint optionalroot1_F7_root1_F8 = IntConstraintFactory.arithm(root1_F8, "<=", root1_F7);
		optionalroot1_F7_root1_F8.setName(Utilities.OPTIONAL_TC);
		solver.post(optionalroot1_F7_root1_F8);
		
		//--------------------------------------------
		//Cross-Tree Constraints
		//--------------------------------------------
		Constraint requiresroot1_F8_root1_F2 = IntConstraintFactory.arithm(root1_F8, "<=", root1_F2);
		requiresroot1_F8_root1_F2.setName(Utilities.REQUIRES_CTC);
		solver.post(requiresroot1_F8_root1_F2);
		
		//--------------------------------------------
		//Cross-Model Constraints
		//--------------------------------------------
		
		//--------------------------------------------
		//Solve
		//--------------------------------------------
		long start = System.currentTimeMillis();
		System.out.println("Started at: " + start);
		
		Chatterbox.showSolutions(solver);
		solver.findAllSolutions();
		
		
		long end = System.currentTimeMillis();
		System.out.println("Ended at: " + end);
		System.out.println("Total time: " + (end - start));
	}
	
	private static void initializeVars(){
		int contFeatures = 10;
			
		root1_root = (BoolVar) VariableFactory.fixed("feature_root1_root", 1, solver);
		root1_F1 = VariableFactory.bool("feature_root1_F1", solver);
		root1_F2 = VariableFactory.bool("feature_root1_F2", solver);
		root1_F3 = VariableFactory.bool("feature_root1_F3", solver);
		root1_F9 = VariableFactory.bool("feature_root1_F9", solver);
		root1_F4 = VariableFactory.bool("feature_root1_F4", solver);
		root1_F5 = VariableFactory.bool("feature_root1_F5", solver);
		root1_F6 = VariableFactory.bool("feature_root1_F6", solver);
		root1_F7 = VariableFactory.bool("feature_root1_F7", solver);
		root1_F8 = VariableFactory.bool("feature_root1_F8", solver);
	}
	
	private static void initializeFeatureAttributes(){
		featureAttrAtribute1 = new HashMap<String, IntVar>();
		featureAttrAtribute0 = new HashMap<String, IntVar>();
		
		featureAttrAtribute1.put("root1_F1", VariableFactory.enumerated("root1_F1Atribute1", new int[]{0, 5}, solver));
		LogicalConstraintFactory.or(IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F1"), ">=", 5), IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F1"), "=", 0));
		LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(root1_F1, "=", 0), IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F1"), "=", 0));
		featureAttrAtribute0.put("root1_F1", VariableFactory.enumerated("root1_F1Atribute0", new int[]{0, 94}, solver));
		LogicalConstraintFactory.or(IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F1"), ">=", 94), IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F1"), "=", 0));
		LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(root1_F1, "=", 0), IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F1"), "=", 0));
		featureAttrAtribute1.put("root1_F2", VariableFactory.enumerated("root1_F2Atribute1", new int[]{0, 31}, solver));
		LogicalConstraintFactory.or(IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F2"), ">=", 31), IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F2"), "=", 0));
		LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(root1_F2, "=", 0), IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F2"), "=", 0));
		featureAttrAtribute0.put("root1_F2", VariableFactory.enumerated("root1_F2Atribute0", new int[]{0, 51}, solver));
		LogicalConstraintFactory.or(IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F2"), ">=", 51), IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F2"), "=", 0));
		LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(root1_F2, "=", 0), IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F2"), "=", 0));
		featureAttrAtribute1.put("root1_F9", VariableFactory.enumerated("root1_F9Atribute1", new int[]{0, 14}, solver));
		LogicalConstraintFactory.or(IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F9"), ">=", 14), IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F9"), "=", 0));
		LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(root1_F9, "=", 0), IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F9"), "=", 0));
		featureAttrAtribute0.put("root1_F9", VariableFactory.enumerated("root1_F9Atribute0", new int[]{0, 41}, solver));
		LogicalConstraintFactory.or(IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F9"), ">=", 41), IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F9"), "=", 0));
		LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(root1_F9, "=", 0), IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F9"), "=", 0));
		featureAttrAtribute1.put("root1_F4", VariableFactory.enumerated("root1_F4Atribute1", new int[]{0, 39}, solver));
		LogicalConstraintFactory.or(IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F4"), ">=", 39), IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F4"), "=", 0));
		LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(root1_F4, "=", 0), IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F4"), "=", 0));
		featureAttrAtribute0.put("root1_F4", VariableFactory.enumerated("root1_F4Atribute0", new int[]{0, 12}, solver));
		LogicalConstraintFactory.or(IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F4"), ">=", 12), IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F4"), "=", 0));
		LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(root1_F4, "=", 0), IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F4"), "=", 0));
		featureAttrAtribute1.put("root1_F5", VariableFactory.enumerated("root1_F5Atribute1", new int[]{0, 27}, solver));
		LogicalConstraintFactory.or(IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F5"), ">=", 27), IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F5"), "=", 0));
		LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(root1_F5, "=", 0), IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F5"), "=", 0));
		featureAttrAtribute0.put("root1_F5", VariableFactory.enumerated("root1_F5Atribute0", new int[]{0, 88}, solver));
		LogicalConstraintFactory.or(IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F5"), ">=", 88), IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F5"), "=", 0));
		LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(root1_F5, "=", 0), IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F5"), "=", 0));
		featureAttrAtribute1.put("root1_F6", VariableFactory.enumerated("root1_F6Atribute1", new int[]{0, 81}, solver));
		LogicalConstraintFactory.or(IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F6"), ">=", 81), IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F6"), "=", 0));
		LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(root1_F6, "=", 0), IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F6"), "=", 0));
		featureAttrAtribute0.put("root1_F6", VariableFactory.enumerated("root1_F6Atribute0", new int[]{0, 91}, solver));
		LogicalConstraintFactory.or(IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F6"), ">=", 91), IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F6"), "=", 0));
		LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(root1_F6, "=", 0), IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F6"), "=", 0));
		featureAttrAtribute1.put("root1_F8", VariableFactory.enumerated("root1_F8Atribute1", new int[]{0, 49}, solver));
		LogicalConstraintFactory.or(IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F8"), ">=", 49), IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F8"), "=", 0));
		LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(root1_F8, "=", 0), IntConstraintFactory.arithm(featureAttrAtribute1.get("root1_F8"), "=", 0));
		featureAttrAtribute0.put("root1_F8", VariableFactory.enumerated("root1_F8Atribute0", new int[]{0, 74}, solver));
		LogicalConstraintFactory.or(IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F8"), ">=", 74), IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F8"), "=", 0));
		LogicalConstraintFactory.ifThen(IntConstraintFactory.arithm(root1_F8, "=", 0), IntConstraintFactory.arithm(featureAttrAtribute0.get("root1_F8"), "=", 0));
	}
	
	private static IntVar[] getFeatureVars(int contFeatures){
		IntVar[] featureVars = new IntVar[contFeatures];
		Variable[] varsSolver = solver.getVars();
		int index = 0;
		for(int i = 0; i < varsSolver.length; i++) {
			Variable current = varsSolver[i];
			if(current.getName().startsWith("feature_")) {
				featureVars[index] = (IntVar) current;
				index++;
			}
		}
		
		return featureVars;
	}
}

