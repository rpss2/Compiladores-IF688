package br.ufpe.cin.if688.minijava.visitor;

import br.ufpe.cin.if688.minijava.ast.And;
import br.ufpe.cin.if688.minijava.ast.ArrayAssign;
import br.ufpe.cin.if688.minijava.ast.ArrayLength;
import br.ufpe.cin.if688.minijava.ast.ArrayLookup;
import br.ufpe.cin.if688.minijava.ast.Assign;
import br.ufpe.cin.if688.minijava.ast.Block;
import br.ufpe.cin.if688.minijava.ast.BooleanType;
import br.ufpe.cin.if688.minijava.ast.Call;
import br.ufpe.cin.if688.minijava.ast.ClassDeclExtends;
import br.ufpe.cin.if688.minijava.ast.ClassDeclSimple;
import br.ufpe.cin.if688.minijava.ast.False;
import br.ufpe.cin.if688.minijava.ast.Formal;
import br.ufpe.cin.if688.minijava.ast.Identifier;
import br.ufpe.cin.if688.minijava.ast.IdentifierExp;
import br.ufpe.cin.if688.minijava.ast.IdentifierType;
import br.ufpe.cin.if688.minijava.ast.If;
import br.ufpe.cin.if688.minijava.ast.IntArrayType;
import br.ufpe.cin.if688.minijava.ast.IntegerLiteral;
import br.ufpe.cin.if688.minijava.ast.IntegerType;
import br.ufpe.cin.if688.minijava.ast.LessThan;
import br.ufpe.cin.if688.minijava.ast.MainClass;
import br.ufpe.cin.if688.minijava.ast.MethodDecl;
import br.ufpe.cin.if688.minijava.ast.Minus;
import br.ufpe.cin.if688.minijava.ast.NewArray;
import br.ufpe.cin.if688.minijava.ast.NewObject;
import br.ufpe.cin.if688.minijava.ast.Not;
import br.ufpe.cin.if688.minijava.ast.Plus;
import br.ufpe.cin.if688.minijava.ast.Print;
import br.ufpe.cin.if688.minijava.ast.Program;
import br.ufpe.cin.if688.minijava.ast.This;
import br.ufpe.cin.if688.minijava.ast.Times;
import br.ufpe.cin.if688.minijava.ast.True;
import br.ufpe.cin.if688.minijava.ast.Type;
import br.ufpe.cin.if688.minijava.ast.VarDecl;
import br.ufpe.cin.if688.minijava.ast.While;
import br.ufpe.cin.if688.minijava.symboltable.Method;
import br.ufpe.cin.if688.minijava.symboltable.SymbolTable;
import br.ufpe.cin.if688.minijava.symboltable.Class;

public class TypeCheckVisitor implements IVisitor<Type> {

	private SymbolTable symbolTable;
	private Class currClass;
	private Method currMethod;

	public TypeCheckVisitor(SymbolTable st) {
		symbolTable = st;
	}

	// MainClass m;
	// ClassDeclList cl;
	public Type visit(Program n) {
		n.m.accept(this);
		for (int i = 0; i < n.cl.size(); i++) {
			n.cl.elementAt(i).accept(this);
		}
		return null;
	}

	// Identifier i1,i2;
	// Statement s;
	public Type visit(MainClass n) {
		
		currClass = symbolTable.getClass(n.i1.s);
		currMethod = symbolTable.getMethod("main", currClass.getId());
		
		n.i1.accept(this);
		n.i2.accept(this);
		n.s.accept(this);
		
		currMethod = null;
		currClass = null;
		
		return null;
	}

	// Identifier i;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclSimple n) {
		
		currClass = symbolTable.getClass(n.i.s);
		
		n.i.accept(this);
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.elementAt(i).accept(this);
		}
		
		currClass = null;
		
		return null;
	}

	// Identifier i;
	// Identifier j;
	// VarDeclList vl;
	// MethodDeclList ml;
	public Type visit(ClassDeclExtends n) {
		
		currClass = symbolTable.getClass(n.i.s);
		
		n.i.accept(this);
		n.j.accept(this);
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.elementAt(i).accept(this);
		}
		
		currClass = null;
		
		return null;
	}

	// Type t;
	// Identifier i;
	public Type visit(VarDecl n) {
		n.t.accept(this);
		n.i.accept(this);
		return n.t;
	}

	// Type t;
	// Identifier i;
	// FormalList fl;
	// VarDeclList vl;
	// StatementList sl;
	// Exp e;
	public Type visit(MethodDecl n) {
		n.t.accept(this);
		n.i.accept(this);
		for (int i = 0; i < n.fl.size(); i++) {
			n.fl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.elementAt(i).accept(this);
		}
		n.e.accept(this);
		return null;
	}

	// Type t;
	// Identifier i;
	public Type visit(Formal n) {
		n.t.accept(this);
		n.i.accept(this);
		return n.t;
	}

	public Type visit(IntArrayType n) {
		return new IntArrayType();
	}

	public Type visit(BooleanType n) {
		return new BooleanType();
	}

	public Type visit(IntegerType n) {
		return new IntegerType();
	}

	// String s;
	public Type visit(IdentifierType n) { //Nao sei como fazer esse
		return null;
	}

	// StatementList sl;
	public Type visit(Block n) {
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.elementAt(i).accept(this);
		}
		return null;
	}

	// Exp e;
	// Statement s1,s2;
	public Type visit(If n) {
		
		Type expType = n.e.accept(this);
		
		if(!(expType instanceof BooleanType)) {
			System.out.println("If: Expressão deve ser Booleano!");
			return null;
		}
		
		n.s1.accept(this);
		n.s2.accept(this);
		return null;
	}

	// Exp e;
	// Statement s;
	public Type visit(While n) {
		
		Type expType = n.e.accept(this);
		
		if(!(expType instanceof BooleanType)) {
			System.out.println("While: Expressão deve ser Booleano!");
			return null;
		}
		
		n.s.accept(this);
		return null;
	}

	// Exp e;
	public Type visit(Print n) {
		n.e.accept(this);
		return null;
	}

	// Identifier i;
	// Exp e;
	public Type visit(Assign n) {
		
		Type idType = n.i.accept(this);
		Type expType = n.e.accept(this);
		
		if(!(symbolTable.compareTypes(idType, expType))) {
			System.out.println("Assign: identificador e expressão não são do mesmo tipo!");
		}
		
		return null;
	}

	// Identifier i;
	// Exp e1,e2;
	public Type visit(ArrayAssign n) {
		
		Type idType = n.i.accept(this);
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		
		if(!(expType1 instanceof IntegerType)) {
			System.out.println("ArrayAssign: indice não é inteiro!");
			return null;
		}
		
		if(!(idType instanceof IntArrayType)) {
			System.out.println("ArrayAssign: identificador não é inteiro!");
			return null;
		}
		
		if(!(expType2 instanceof IntArrayType)) {
			System.out.println("ArrayAssign: valor não é inteiro!");
		}
		
		return null;
	}

	// Exp e1,e2;
	public Type visit(And n) {
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		
		if(!(expType1 instanceof BooleanType && expType2 instanceof BooleanType)) {
			System.out.println("And: alguma expressão não é booleana!");
		}
		
		return new BooleanType();
	}

	// Exp e1,e2;
	public Type visit(LessThan n) {
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		
		if(!(expType1 instanceof IntegerType && expType2 instanceof IntegerType)) {
			System.out.println("LessThan: alguma expressão não é inteira!");
		}
		
		return new BooleanType();
	}

	// Exp e1,e2;
	public Type visit(Plus n) {
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		
		if(!(expType1 instanceof IntegerType && expType2 instanceof IntegerType)) {
			System.out.println("Plus: alguma expressão não é inteira!");
		}
		
		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(Minus n) {
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		
		if(!(expType1 instanceof IntegerType && expType2 instanceof IntegerType)) {
			System.out.println("Minus: alguma expressão não é inteira!");
		}
		
		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(Times n) {
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		
		if(!(expType1 instanceof IntegerType && expType2 instanceof IntegerType)) {
			System.out.println("Times: alguma expressão não é inteira!");
		}
		
		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(ArrayLookup n) {
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		
		if(!(expType1 instanceof IntegerType && expType2 instanceof IntegerType)) {
			System.out.println("ArrayLookup: alguma expressão não é inteira!");
		}
		
		return new IntegerType();
	}

	// Exp e;
	public Type visit(ArrayLength n) {
		Type expType = n.e.accept(this);
		
		if(!(expType instanceof IntegerType)) {
			System.out.println("ArrayLength: expressão não é inteira!");
		}
		return null;
	}

	// Exp e;
	// Identifier i;
	// ExpList el;
	public Type visit(Call n) {
		n.e.accept(this);
		n.i.accept(this);
		for (int i = 0; i < n.el.size(); i++) {
			n.el.elementAt(i).accept(this);
		}
		return null;
	}

	// int i;
	public Type visit(IntegerLiteral n) {
		return new IntegerType();
	}

	public Type visit(True n) {
		return new BooleanType();
	}

	public Type visit(False n) {
		return new BooleanType();
	}

	// String s;
	public Type visit(IdentifierExp n) {//Depios faço
		return null;
	}

	public Type visit(This n) {
		return currClass.type();
	}

	// Exp e;
	public Type visit(NewArray n) {
		Type expType = n.e.accept(this);
		
		if(!(expType instanceof IntegerType)) {
			System.out.println("NewArray: expressão do tamanho do array não é inteira!");
			return null;
		}
		
		return new IntArrayType();
	}

	// Identifier i;
	public Type visit(NewObject n) {//Depois tbm
		return null;
	}

	// Exp e;
	public Type visit(Not n) {
		n.e.accept(this);
		return null;
	}

	// String s;
	public Type visit(Identifier n) {
		return null;
	}
}
