package br.ufpe.cin.if688.minijava.main;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.IntArrayData;

import br.ufpe.cin.if688.minijava.ast.BooleanType;
import br.ufpe.cin.if688.minijava.ast.ClassDecl;
import br.ufpe.cin.if688.minijava.ast.ClassDeclExtends;
import br.ufpe.cin.if688.minijava.ast.ClassDeclList;
import br.ufpe.cin.if688.minijava.ast.ClassDeclSimple;
import br.ufpe.cin.if688.minijava.ast.Exp;
import br.ufpe.cin.if688.minijava.ast.Formal;
import br.ufpe.cin.if688.minijava.ast.FormalList;
import br.ufpe.cin.if688.minijava.ast.Identifier;
import br.ufpe.cin.if688.minijava.ast.IdentifierType;
import br.ufpe.cin.if688.minijava.ast.IntArrayType;
import br.ufpe.cin.if688.minijava.ast.IntegerType;
import br.ufpe.cin.if688.minijava.ast.MainClass;
import br.ufpe.cin.if688.minijava.ast.MethodDecl;
import br.ufpe.cin.if688.minijava.ast.MethodDeclList;
import br.ufpe.cin.if688.minijava.ast.Program;
import br.ufpe.cin.if688.minijava.ast.Statement;
import br.ufpe.cin.if688.minijava.ast.StatementList;
import br.ufpe.cin.if688.minijava.ast.Type;
import br.ufpe.cin.if688.minijava.ast.VarDecl;
import br.ufpe.cin.if688.minijava.ast.VarDeclList;
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
		Identifier id = new Identifier(ctx.getText());
		return id;
	}

	@Override
	public Object visitMethodDeclaration(MethodDeclarationContext ctx) {
		Type at = (Type) ctx.type(0).accept(this);
		Identifier ai = (Identifier) ctx.identifier(0).accept(this);
		
		FormalList afl = new FormalList();
		for (int i = 1; i < ctx.type().size(); i++) {
			Type t = (Type) ctx.type(i).accept(this);
			Identifier id = (Identifier) ctx.identifier(i).accept(this);
			Formal formal = new Formal(t, id);
			afl.addElement(formal);
		}
		
		VarDeclList avl = new VarDeclList();
		for (VarDeclarationContext vdc : ctx.varDeclaration()) {
			avl.addElement((VarDecl) vdc.accept(this));
		}
		
		StatementList asl = new StatementList();
		for (StatementContext sc : ctx.statement()) {
			asl.addElement((Statement) sc.accept(this));
		}
		
		Exp ae = (Exp) ctx.expression().accept(this);
		
		MethodDecl md = new MethodDecl(at, ai, afl, avl, asl, ae);
		return md;
	}

	@Override
	public Object visitGoal(GoalContext ctx) {
		MainClass am = (MainClass) ctx.mainClass().accept(this);
		
		ClassDeclList acl = new ClassDeclList();
		for(ClassDeclarationContext cdc : ctx.classDeclaration()) {
			acl.addElement((ClassDecl) cdc.accept(this));
		}
		
		Program p = new Program(am, acl);
		return p;
	}

	@Override
	public Object visitExpression(ExpressionContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitMainClass(MainClassContext ctx) {
		Identifier ai1 = (Identifier) ctx.identifier(0).accept(this);
		Identifier ai2 = (Identifier) ctx.identifier(1).accept(this);
		Statement as = (Statement) ctx.statement().accept(this);
		
		MainClass mc = new MainClass(ai1, ai2, as);
		return mc;
	}

	@Override
	public Object visitStatement(StatementContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitType(TypeContext ctx) {
		String type = ctx.getText();
		switch (type) {
		case "int":
			return new IntegerType();
		case "boolean":
			return new BooleanType();
		case "int []":
			return new IntArrayType();
		default:
			return new IdentifierType(type);
		}
	}

	@Override
	public Object visitVarDeclaration(VarDeclarationContext ctx) {
		Type at = (Type) ctx.type().accept(this);
		Identifier ai = (Identifier) ctx.identifier().accept(this);
		
		VarDecl vd = new VarDecl(at, ai);
		return vd;
	}

	@Override
	public Object visitClassDeclaration(ClassDeclarationContext ctx) {
		ClassDecl cd = null;
		
		int sizeList = ctx.identifier().size();
		
		VarDeclList vdl = new VarDeclList();
		for(VarDeclarationContext vdc : ctx.varDeclaration()) {
			vdl.addElement((VarDecl) vdc.accept(this));
		}
		
		MethodDeclList mdl = new MethodDeclList();
		for(MethodDeclarationContext mdc : ctx.methodDeclaration()) {
			mdl.addElement((MethodDecl) mdc.accept(this));
		}
		
		if(sizeList > 1) {
			cd = new ClassDeclExtends((Identifier) ctx.identifier(0).accept(this), (Identifier) ctx.identifier(1).accept(this), vdl, mdl);
		} else {
			cd = new ClassDeclSimple((Identifier) ctx.identifier(0).accept(this), vdl, mdl);
		}
		return cd;
	}

}
