package pkg;

import org.chocosolver.solver.constraints.Propagator;
import org.chocosolver.solver.search.strategy.selectors.VariableEvaluator;
import org.chocosolver.solver.search.strategy.selectors.VariableSelector;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.Variable;

public class FMVarSelectorMorePercInstVars implements VariableSelector<IntVar>, VariableEvaluator<IntVar> {
	
	@Override
	public double evaluate(IntVar variable) {
		return variable.getPropagators().length;
	}

	/**
	 * The method returns the variable with the highest percentage of  
	 * instantiated variables in its involved constraints. Otherwise, 
	 * it returns null.
	 */
	@Override
	public IntVar getVariable(IntVar[] variables) {
		IntVar variable = null;
		
		if(variables.length > 0) {
			int globalMoreInstantiatedPerc = -1;

			for(int i = 0; i < variables.length; i++) {
				if(!variables[i].isInstantiated()) {
					int varsLength = 0;
					int instantVarsLength = 0;
					int localMoreInstantiatedPerc = 0;
					Propagator[] propagators = variables[i].getPropagators();
					
					for(int j = 0; j < propagators.length; j++) {
						varsLength += propagators[j].getVars().length;
						Variable[] vars = propagators[j].getVars();
						
						for(int k = 0; k < vars.length; k++) {
							if(vars[k].isInstantiated()) {
								instantVarsLength++;
							}
						}
					}
					
					if(varsLength > 0) {
						localMoreInstantiatedPerc = instantVarsLength/varsLength;
						if(localMoreInstantiatedPerc > globalMoreInstantiatedPerc) {
							variable = variables[i];
						}
					}
					else {
						variable = variables[i];
					}	
				}
			}	
		}
		return variable;
	}
}
