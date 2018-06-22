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
import br.ufpe.cin.if688.minijava.symboltable.Variable;

public class TypeCheckVisitor implements IVisitor<Type> {
	/*Checklist of what is yest incomplete:
	 * 	- Identifier
	 * */
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
	
	//here we need to check if the method returns something which matching type
	public Type visit(MethodDecl n) {
		this.currMethod = symbolTable.getMethod(n.i.s, this.currClass.getId());
		
		Type methodType = n.t.accept(this);
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
		
		Type returnType = n.e.accept(this);
		
		if(!symbolTable.compareTypes(methodType, returnType)) {
			System.out.println("MethodDecl: Metodo declarado tem um retorno de outro tipo");
		}
		this.currMethod = null;
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
	public Type visit(IdentifierType n) {
		return n;
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
			System.out.println("If: Expressao deve ser Booleano!");
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
			System.out.println("While: Expressao deve ser Booleano!");
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
			System.out.println("Assign: identificador e expressao nao sao do mesmo tipo!");
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
			System.out.println("ArrayAssign: indice nao e inteiro!");
			return null;
		}
		
		if(!(idType instanceof IntArrayType)) {
			System.out.println("ArrayAssign: identificador nao e inteiro!");
			return null;
		}
		
		if(!(expType2 instanceof IntArrayType)) {
			System.out.println("ArrayAssign: valor nao e inteiro!");
		}
		
		return null;
	}

	// Exp e1,e2;
	public Type visit(And n) {
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		
		if(!(expType1 instanceof BooleanType && expType2 instanceof BooleanType)) {
			System.out.println("And: alguma expressão nao e booleana!");
		}
		
		return new BooleanType();
	}

	// Exp e1,e2;
	public Type visit(LessThan n) {
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		
		if(!(expType1 instanceof IntegerType && expType2 instanceof IntegerType)) {
			System.out.println("LessThan: alguma expressao nao e inteira!");
		}
		
		return new BooleanType();
	}

	// Exp e1,e2;
	public Type visit(Plus n) {
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		
		if(!(expType1 instanceof IntegerType && expType2 instanceof IntegerType)) {
			System.out.println("Plus: alguma expressao nao e inteira!");
		}
		
		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(Minus n) {
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		
		if(!(expType1 instanceof IntegerType && expType2 instanceof IntegerType)) {
			System.out.println("Minus: alguma expressao nao e inteira!");
		}
		
		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(Times n) {
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		
		if(!(expType1 instanceof IntegerType && expType2 instanceof IntegerType)) {
			System.out.println("Times: alguma expressao nao e inteira!");
		}
		
		return new IntegerType();
	}

	// Exp e1,e2;
	public Type visit(ArrayLookup n) {
		Type expType1 = n.e1.accept(this);
		Type expType2 = n.e2.accept(this);
		
		if(!(expType1 instanceof IntegerType && expType2 instanceof IntegerType)) {
			System.out.println("ArrayLookup: alguma expressao nao e inteira!");
		}
		
		return new IntegerType();
	}

	// Exp e;
	public Type visit(ArrayLength n) {
		Type expType = n.e.accept(this);
		
		if(!(expType instanceof IntegerType)) {
			System.out.println("ArrayLength: expressão nao e inteira!");
		}
		return null;
	}

	// Exp e;
	// Identifier i;
	// ExpList el;
	//here we need to check if all parameters are proper, in number and type.
	public Type visit(Call n) {
		//teremos que retornar o tipo no fim
		Type typeReturned = null;
		
		Type aux = n.e.accept(this);
		//n.i.accept(this);
		if(n.e instanceof This) {
			//se recursivo
			typeReturned = currClass.getMethod(n.i.s).type();			
		} else if (aux instanceof IdentifierType) {
			Class callClass = this.symbolTable.getClass(((IdentifierType) aux).s);
			Method callMethod = this.symbolTable.getMethod(n.i.toString(), callClass.getId());
			
			Class auxClass = this.currClass;
			this.currClass = callClass;
			
			int c = 0;
			for(; c < n.el.size(); c++) {
				Type pamTypes = n.el.elementAt(c).accept(this);
				Variable pamsDeclared = callMethod.getParamAt(c);
				
				//se nao houver parametros
				if(pamsDeclared == null) {
					System.out.println("Nao foram passados parametros!");
					return null;
				}
				Type pamsDeclaredTypes = callMethod.getParamAt(c).type();
				
				//se os parametros passados forem do tipo errado
				if(!(symbolTable.compareTypes(pamTypes, pamsDeclaredTypes))) {
					System.out.println("Tipos dos parametros passados nao correspondem com os argumentos da funcao!");
					return null;
				}
			}
			if(callMethod.getParamAt(c) != null) {
				System.out.println("Faltou parametros na chamada da funcao!");
				return null;
			}
			
			Type idType = n.i.accept(this);
			this.currClass = auxClass;
			return idType;
		}
		
		
		return typeReturned;
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
	public Type visit(IdentifierExp n) {
		Type type = symbolTable.getVarType(currMethod, currClass, n.s);
		return type;
	}

	public Type visit(This n) {
		return currClass.type();
	}

	// Exp e;
	public Type visit(NewArray n) {
		Type expType = n.e.accept(this);
		
		if(!(expType instanceof IntegerType)) {
			System.out.println("NewArray: expressao do tamanho do array nao e inteira!");
			return null;
		}
		
		return new IntArrayType();
	}

	// Identifier i;
	public Type visit(NewObject n) {
		return n.i.accept(this);
	}

	// Exp e;
	public Type visit(Not n) {
		n.e.accept(this);
		return null;
	}

	// String s;
	public Type visit(Identifier n) {
		if(this.currClass.containsVar(n.s)) {
			return symbolTable.getVarType(currMethod, currClass, n.s);
		}
		if(this.currClass.containsMethod(n.s)) {
			return symbolTable.getMethodType(n.s, currClass.getId());
		}
		if(this.currMethod != null && this.currMethod.containsVar(n.s)) {
			return currMethod.getVar(n.s).type();
		}
		if(this.currMethod != null && this.currMethod.containsParam(n.s)) {
			return this.currMethod.getParam(n.s).type();
		} else {
			Class classe = this.symbolTable.getClass(n.s);
			if (classe == null) {
				System.out.println("Varivavel nao foi encontrada!");
			}
			return classe.type();
		}
	}
}
