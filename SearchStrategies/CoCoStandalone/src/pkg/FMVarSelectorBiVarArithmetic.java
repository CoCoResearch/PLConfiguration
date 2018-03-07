package pkg;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.search.strategy.selectors.VariableEvaluator;
import org.chocosolver.solver.search.strategy.selectors.VariableSelector;
import org.chocosolver.solver.variables.IntVar;

import coco.testing.Utilities;

public class FMVarSelectorBiVarArithmetic implements VariableSelector<IntVar>, VariableEvaluator<IntVar> {
	
	@Override
	public double evaluate(IntVar variable) {
		return variable.getPropagators().length;
	}

	@Override
	public IntVar getVariable(IntVar[] variables) {
		IntVar variable = null;
		
		if(variables.length > 0) {
			variable = getVarsInMandatoryRequiresConstraints(variables);
		}
		
		return variable;
	}
	
	/**
	 * The method returns the first variable involved in a mandatory, 
	 * requires, or excludes constraint (both in a single or multi-model 
	 * environment) where the other variable is already instantiated.
	 * Otherwise, it returns null.
	 */
	private IntVar getVarsInMandatoryRequiresConstraints(IntVar[] variables){
		IntVar variable = null;
		boolean exists = false;
		
		for(int i = 0; i < variables.length && !exists; i++) {
			if(!variables[i].isInstantiated()) {
				Propagator[] propagators = variables[i].getPropagators();
				
				for(int j = 0; j < propagators.length && !exists; j++) {
					String constraintName = propagators[j].getConstraint().getName();
					
					if(constraintName.equals(Utilities.MANDATORY_TC) || constraintName.equals(Utilities.REQUIRES_CTC) 
						|| constraintName.equals(Utilities.REQUIRES_CMC) || constraintName.equals(Utilities.EXCLUDES_CTC) 
						|| constraintName.equals(Utilities.EXCLUDES_CMC)){
						IntVar[] vars = (IntVar[]) propagators[j].getVars();
						
						for(int k = 0; k < vars.length; k++) {
							if(vars[k].isInstantiated()) {
								variable = variables[i];
								exists = true;
								//System.out.println(constraintName + ": " + vars.length + " - " + vars[k] + " " + variable);
							}
						}
					}		
				}
			}
		}
		
		return variable;
	}
	
}