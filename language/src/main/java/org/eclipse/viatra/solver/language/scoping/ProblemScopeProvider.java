/*
 * generated by Xtext 2.25.0
 */
package org.eclipse.viatra.solver.language.scoping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.viatra.solver.language.ProblemUtil;
import org.eclipse.viatra.solver.language.model.problem.ClassDeclaration;
import org.eclipse.viatra.solver.language.model.problem.ExistentialQuantifier;
import org.eclipse.viatra.solver.language.model.problem.PredicateDefinition;
import org.eclipse.viatra.solver.language.model.problem.Problem;
import org.eclipse.viatra.solver.language.model.problem.ProblemPackage;
import org.eclipse.viatra.solver.language.model.problem.ReferenceDeclaration;
import org.eclipse.viatra.solver.language.model.problem.Relation;
import org.eclipse.viatra.solver.language.model.problem.Variable;
import org.eclipse.viatra.solver.language.model.problem.VariableOrNodeArgument;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;

/**
 * This class contains custom scoping description.
 * 
 * See
 * https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 */
public class ProblemScopeProvider extends AbstractProblemScopeProvider {

	@Override
	public IScope getScope(EObject context, EReference reference) {
		IScope scope = super.getScope(context, reference);
		if (reference == ProblemPackage.Literals.NODE_ASSERTION_ARGUMENT__NODE
				|| reference == ProblemPackage.Literals.NODE_VALUE_ASSERTION__NODE) {
			return getNodesScope(context, scope);
		}
		if (reference == ProblemPackage.Literals.VARIABLE_OR_NODE_ARGUMENT__VARIABLE_OR_NODE) {
			return getVariableScope(context, scope);
		}
		if (reference == ProblemPackage.Literals.REFERENCE_DECLARATION__OPPOSITE) {
			return getOppositeScope(context, scope);
		}
		return scope;
	}

	protected IScope getNodesScope(EObject context, IScope delegateScope) {
		Problem problem = EcoreUtil2.getContainerOfType(context, Problem.class);
		if (problem == null) {
			return delegateScope;
		}
		return Scopes.scopeFor(problem.getNodes(), delegateScope);
	}

	protected IScope getVariableScope(EObject context, IScope delegateScope) {
		List<Variable> variables = new ArrayList<>();
		EObject currentContext = context;
		if (context instanceof VariableOrNodeArgument) {
			VariableOrNodeArgument argument = (VariableOrNodeArgument) context;
			Variable singletonVariable = argument.getSingletonVariable();
			if (singletonVariable != null) {
				variables.add(singletonVariable);
			}
		}
		while (currentContext != null && !(currentContext instanceof PredicateDefinition)) {
			if (currentContext instanceof ExistentialQuantifier) {
				ExistentialQuantifier quantifier = (ExistentialQuantifier) currentContext;
				variables.addAll(quantifier.getImplicitVariables());
			}
			currentContext = currentContext.eContainer();
		}
		if (currentContext instanceof PredicateDefinition) {
			PredicateDefinition definition = (PredicateDefinition) currentContext;
			variables.addAll(definition.getParameters());
		}
		return Scopes.scopeFor(variables, getNodesScope(context, delegateScope));
	}

	protected IScope getOppositeScope(EObject context, IScope delegateScope) {
		ReferenceDeclaration referenceDeclaration = EcoreUtil2.getContainerOfType(context, ReferenceDeclaration.class);
		if (referenceDeclaration == null) {
			return delegateScope;
		}
		Relation relation = referenceDeclaration.getReferenceType();
		if (!(relation instanceof ClassDeclaration)) {
			return delegateScope;
		}
		ClassDeclaration classDeclaration = (ClassDeclaration) relation;
		Collection<ReferenceDeclaration> referenceDeclarations = ProblemUtil
				.getAllReferenceDeclarations(classDeclaration);
		return Scopes.scopeFor(referenceDeclarations, delegateScope);
	}
}