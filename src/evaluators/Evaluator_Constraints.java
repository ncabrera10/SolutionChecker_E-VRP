package evaluators;

import java.io.PrintWriter;

import constraints.Cons_CSCapacity;
import constraints.Cons_CustomersVisited;
import constraints.Cons_routeCharge;
import constraints.Cons_routeDuration;
import constraints.Cons_routeStartEnd;
import dataStructures.InstanceEVRPNLC;
import dataStructures.Solution;

public class Evaluator_Constraints {

	public Evaluator_Constraints() {
		
	}
	
	public boolean evaluateRouteCharge(Solution solution, InstanceEVRPNLC instance, int precision,PrintWriter pw) {
		Cons_routeCharge constraint = new Cons_routeCharge();
		return(constraint.checkConstraint(solution, instance, true, precision,pw));
	}
	
	public boolean evaluateRouteDuration(Solution solution, InstanceEVRPNLC instance, int precision,PrintWriter pw) {
		Cons_routeDuration constraint = new Cons_routeDuration();
		return(constraint.checkConstraint(solution, instance, true, precision,pw));
	}
	
	public boolean evaluateRouteStartEnd(Solution solution, InstanceEVRPNLC instance, int precision,PrintWriter pw) {
		Cons_routeStartEnd constraint = new Cons_routeStartEnd();
		return(constraint.checkConstraint(solution, instance, true, precision,pw));
	}
	
	public boolean evaluateCSCapacity(Solution solution, InstanceEVRPNLC instance, int precision,PrintWriter pw) {
		Cons_CSCapacity constraint = new Cons_CSCapacity();
		return(constraint.checkConstraintDeep(solution, instance, true, precision,pw));
	}
	
	public boolean evaluateCustomersVisited(Solution solution, InstanceEVRPNLC instance, int precision,PrintWriter pw) {
		Cons_CustomersVisited constraint = new Cons_CustomersVisited();
		return(constraint.checkConstraint(solution, instance, true, precision,pw));
	}
}
