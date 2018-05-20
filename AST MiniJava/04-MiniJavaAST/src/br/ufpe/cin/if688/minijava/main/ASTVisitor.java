package br.ufpe.cin.if688.minijava.main;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import br.ufpe.cin.if688.minijava.main.AntlrParser.ClassDeclarationContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.ExpressionContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.GoalContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.IdentifierContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.MainClassContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.MethodDeclarationContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.StatementContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.TypeContext;
import br.ufpe.cin.if688.minijava.main.AntlrParser.VarDeclarationContext;

public class ASTVisitor implements AntlrVisitor<Object> {

	@Override
	public Object visit(ParseTree arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitChildren(RuleNode arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitErrorNode(ErrorNode arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitTerminal(TerminalNode arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitIdentifier(IdentifierContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitMethodDeclaration(MethodDeclarationContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitGoal(GoalContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpression(ExpressionContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitMainClass(MainClassContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatement(StatementContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitType(TypeContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitVarDeclaration(VarDeclarationContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitClassDeclaration(ClassDeclarationContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

}
