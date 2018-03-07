package pkg;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.search.strategy.selectors.VariableEvaluator;
import org.chocosolver.solver.search.strategy.selectors.VariableSelector;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Variable;

import coco.testing.Utilities;

public class FMVarSelectorOrAttr1 implements VariableSelector<IntVar>, VariableEvaluator<IntVar> {
	
	@Override
	public double evaluate(IntVar variable) {
		return variable.getPropagators().length;
	}

	@Override
	public IntVar getVariable(IntVar[] variables) {
		IntVar variable = null;
		
		if(variables.length > 0) {
			variable = getVarsInOrAlternativeConstraints(variables);
		}
		
		return variable;
	}
	
	/**
	 * The method returns the variable involved in the first or relation,
	 * where features have attributes related to the Attribute 1 type. The
	 * variable has the highest upper bound of the group. Otherwise, it 
	 * returns null.
	 */
	private IntVar getVarsInOrAlternativeConstraints(IntVar[] variables){
		IntVar variable = null;
		boolean exists = false;
		
		for(int i = 0; i < variables.length && !exists; i++) {
			if(!variables[i].isInstantiated()) {
				Propagator[] propagators = variables[i].getPropagators();
				boolean isOr = false;
				boolean hasFeatureAttributes = false;
				Variable[] orVars = null;
				
				for(int j = 0; j < propagators.length && !isOr && !hasFeatureAttributes; j++) {
					String constraintName = propagators[j].getConstraint().getName();
					Variable[] vars = propagators[j].getVars();
					
					if(constraintName.equals(Utilities.OR_TC)){
						orVars = vars;
						isOr = true;
					}
					else {
						for(int k = 0; k < vars.length; k++) {
							if(vars[k].getName().endsWith("Atribute1")) {
								hasFeatureAttributes = true;
							}
						}
					}
				}
				
				if(isOr && hasFeatureAttributes){
					variable = (IntVar) getVariableHighestValue(orVars);
					if(variable != null){
						exists = true;
					}
				}
			}
		}
		
		return variable;
	}
	
	/**
	 * Gets the variable with the highest upper bound from the
	 * variables given by parameter.
	 */
	private Variable getVariableHighestValue(Variable[] variables){
		Variable highest = null;
		int ub = -1;
		
		for(int i = 0; i < variables.length; i++){
			Propagator[] propagators = variables[i].getPropagators();
			
			for(int j = 0; j < propagators.length; j++) {
				IntVar[] vars = (IntVar[]) propagators[j].getVars();
				
				for(int k = 0; k < vars.length; k++) {
					if(vars[k].getName().endsWith("Atribute1")) {
						if(ub == -1 || vars[k].getUB() > ub){
							ub = vars[k].getUB();
							highest = vars[k];
						}
					}
				}
			}
		}
		return highest;
	}
}