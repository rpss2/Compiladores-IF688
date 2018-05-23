package br.ufpe.cin.if688.minijava.main;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import antlr.AntlrParser.ClassDeclarationContext;
import antlr.AntlrParser.ExpressionContext;
import antlr.AntlrParser.GoalContext;
import antlr.AntlrParser.IdentifierContext;
import antlr.AntlrParser.MainClassContext;
import antlr.AntlrParser.MethodDeclarationContext;
import antlr.AntlrParser.StatementContext;
import antlr.AntlrParser.TypeContext;
import antlr.AntlrParser.VarDeclarationContext;
import antlr.AntlrVisitor;
import br.ufpe.cin.if688.minijava.ast.And;
import br.ufpe.cin.if688.minijava.ast.ArrayAssign;
import br.ufpe.cin.if688.minijava.ast.ArrayLength;
import br.ufpe.cin.if688.minijava.ast.ArrayLookup;
import br.ufpe.cin.if688.minijava.ast.Assign;
import br.ufpe.cin.if688.minijava.ast.Block;
import br.ufpe.cin.if688.minijava.ast.BooleanType;
import br.ufpe.cin.if688.minijava.ast.Call;
import br.ufpe.cin.if688.minijava.ast.ClassDecl;
import br.ufpe.cin.if688.minijava.ast.ClassDeclExtends;
import br.ufpe.cin.if688.minijava.ast.ClassDeclList;
import br.ufpe.cin.if688.minijava.ast.ClassDeclSimple;
import br.ufpe.cin.if688.minijava.ast.Exp;
import br.ufpe.cin.if688.minijava.ast.ExpList;
import br.ufpe.cin.if688.minijava.ast.False;
import br.ufpe.cin.if688.minijava.ast.Formal;
import br.ufpe.cin.if688.minijava.ast.FormalList;
import br.ufpe.cin.if688.minijava.ast.Identifier;
import br.ufpe.cin.if688.minijava.ast.IdentifierType;
import br.ufpe.cin.if688.minijava.ast.If;
import br.ufpe.cin.if688.minijava.ast.IntArrayType;
import br.ufpe.cin.if688.minijava.ast.IntegerLiteral;
import br.ufpe.cin.if688.minijava.ast.IntegerType;
import br.ufpe.cin.if688.minijava.ast.LessThan;
import br.ufpe.cin.if688.minijava.ast.MainClass;
import br.ufpe.cin.if688.minijava.ast.MethodDecl;
import br.ufpe.cin.if688.minijava.ast.MethodDeclList;
import br.ufpe.cin.if688.minijava.ast.Minus;
import br.ufpe.cin.if688.minijava.ast.NewArray;
import br.ufpe.cin.if688.minijava.ast.NewObject;
import br.ufpe.cin.if688.minijava.ast.Not;
import br.ufpe.cin.if688.minijava.ast.Plus;
import br.ufpe.cin.if688.minijava.ast.Print;
import br.ufpe.cin.if688.minijava.ast.Program;
import br.ufpe.cin.if688.minijava.ast.Statement;
import br.ufpe.cin.if688.minijava.ast.StatementList;
import br.ufpe.cin.if688.minijava.ast.This;
import br.ufpe.cin.if688.minijava.ast.Times;
import br.ufpe.cin.if688.minijava.ast.True;
import br.ufpe.cin.if688.minijava.ast.Type;
import br.ufpe.cin.if688.minijava.ast.VarDecl;
import br.ufpe.cin.if688.minijava.ast.VarDeclList;
import br.ufpe.cin.if688.minijava.ast.While;

public class ASTVisitor implements AntlrVisitor<Object> {

	@Override
	public Object visit(ParseTree arg0) {
		// TODO Auto-generated method stub
		return arg0.accept(this);
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
		return new Identifier(ctx.getText());
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
		String start = ctx.getStart().getText(); //pegar o primeiro simbolo, pra saber 'new', 'int'...
		int expSize = ctx.expression().size(); //saber qual a qtd de expression
		int qtdChildren = ctx.getChildCount(); //saber o tamanho da expression
		
		//expression '.' identifier '(' ( expression ( ',' expression )* )? ')'
		if(qtdChildren >= 5 && ctx.getChild(1).getText().equals(".")) {
			Exp exp = (Exp) ctx.expression(0).accept(this);
			Identifier id = (Identifier) ctx.identifier().accept(this);
			
			ExpList listExp = new ExpList();
			for(int i = 1; i < expSize; i++) {
				listExp.addElement((Exp) ctx.expression(i).accept(this));
			}
			
			return new Call(exp, id, listExp);
		} else if(expSize == 2) { //expression ( '&&' | '<' | '+' | '-' | '*' ) expression
			Exp ae1 = (Exp) ctx.expression(0).accept(this);
			Exp ae2 = (Exp) ctx.expression(1).accept(this);
			
			switch (ctx.getChild(1).getText()) {
			case "&&":
				return new And(ae1, ae2);
			case "<":
				return new LessThan(ae1, ae2);
			case "+":
				return new Plus(ae1, ae2);
			case "-":
				return new Minus(ae1, ae2);
			case "*":
				return new Times(ae1, ae2);
			default: //expression '[' expression ']'
				return new ArrayLookup(ae1, ae2);
			}
		} else if(expSize == 1) {
			Exp ae = (Exp) ctx.expression(0).accept(this);
			switch (start) {
			case "!": //'!' expression
				return new Not(ae);
			case "(": //'(' expression ')'
				return ae;
			case "new": //'new' 'int' '[' expression ']'
				return new NewArray(ae);
			default: //expression '.' 'length'
				return new ArrayLength(ae);
			}
		} else {
			switch (start) {
				case "true": //'true'
					return new True();
				case "false": //'false'
					return new False();
				case "this": //'this'
					return new This();
				case "new": //'new' identifier '(' ')'
					return new NewObject((Identifier) ctx.identifier().accept(this));
				default: //identifier or INTEGER_LITERAL
					if(start.matches("\\d+")) {
						return new IntegerLiteral(Integer.parseInt(ctx.getStart().getText()));
					} else {
						return (Identifier) ctx.identifier().accept(this);
					}
			}
		}
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
		String start = ctx.getStart().getText();
		
		switch (start) {
		case "{":
			StatementList asl = new StatementList();
			for (StatementContext sc : ctx.statement()) {
				asl.addElement((Statement) sc.accept(this));
			}
			return new Block(asl);
		case "if":
			Exp ae = (Exp) ctx.expression(0).accept(this);
			Statement as1 = (Statement) ctx.statement(0).accept(this);
			Statement as2 = (Statement) ctx.statement(1).accept(this);
			return new If(ae, as1, as2);
		case "while":
			Exp exp = (Exp) ctx.expression(0).accept(this);
			Statement as = (Statement) ctx.statement(0).accept(this);
			return new While(exp, as);
		case "System.out.println":
			Exp printExp = (Exp) ctx.expression(0).accept(this);
			return new Print(printExp);
		default:
			if(ctx.expression().size() == 1) {
				Identifier ai = (Identifier) ctx.identifier().accept(this);
				Exp ae1 = (Exp) ctx.expression(0).accept(this);
				return new Assign(ai, ae1);
			} else {
				Identifier ai = (Identifier) ctx.identifier().accept(this);
				Exp ae1 = (Exp) ctx.expression(0).accept(this);
				Exp ae2 = (Exp) ctx.expression(1).accept(this);
				return new ArrayAssign(ai, ae1, ae2);
			}
		}
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
