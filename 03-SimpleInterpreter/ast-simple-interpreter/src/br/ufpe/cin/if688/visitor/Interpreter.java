package br.ufpe.cin.if688.visitor;

import br.ufpe.cin.if688.ast.AssignStm;
import br.ufpe.cin.if688.ast.CompoundStm;
import br.ufpe.cin.if688.ast.EseqExp;
import br.ufpe.cin.if688.ast.Exp;
import br.ufpe.cin.if688.ast.ExpList;
import br.ufpe.cin.if688.ast.IdExp;
import br.ufpe.cin.if688.ast.LastExpList;
import br.ufpe.cin.if688.ast.NumExp;
import br.ufpe.cin.if688.ast.OpExp;
import br.ufpe.cin.if688.ast.PairExpList;
import br.ufpe.cin.if688.ast.PrintStm;
import br.ufpe.cin.if688.ast.Stm;
import br.ufpe.cin.if688.symboltable.Table;

public class Interpreter implements IVisitor<Table> {
	
	//a=8;b=80;a=7;
	// a->7 ==> b->80 ==> a->8 ==> NIL
	private Table t;
	
	public Interpreter(Table t) {
		this.t = t;
	}

	@Override
	public Table visit(Stm s) {
		return s.accept(this);
	}

	@Override
	public Table visit(AssignStm s) {
		String id = s.getId();
		Exp exp = s.getExp();
		Table t = exp.accept(this);
		t.id = id;
		this.t = t;
		return this.t;
	}

	@Override
	public Table visit(CompoundStm s) {
		s.getStm1().accept(this);
		s.getStm2().accept(this);
		return this.t;
	}

	@Override
	public Table visit(PrintStm s) {
		s.getExps().accept(this);
		return this.t;
	}

	@Override
	public Table visit(Exp e) {
		return e.accept(this);
	}

	@Override
	public Table visit(EseqExp e) {
		IntAndTableVisitor intAndTableVisitor = new IntAndTableVisitor(this.t);
		double res = intAndTableVisitor.visit(e).result;
		return new Table("eseq", res, this.t);
	}

	@Override
	public Table visit(IdExp e) {
		IntAndTableVisitor intAndTableVisitor = new IntAndTableVisitor(this.t);
		double res = intAndTableVisitor.visit(e).result;
		return new Table("id", res, this.t);
	}

	@Override
	public Table visit(NumExp e) {
		IntAndTableVisitor intAndTableVisitor = new IntAndTableVisitor(this.t);
		double res = intAndTableVisitor.visit(e).result;
		return new Table("num", res, this.t);
	}

	@Override
	public Table visit(OpExp e) {
		IntAndTableVisitor intAndTableVisitor = new IntAndTableVisitor(this.t);
		double res = intAndTableVisitor.visit(e).result;
		return new Table("op", res, this.t);
	}

	@Override
	public Table visit(ExpList el) {
		return el.accept(this);
	}

	@Override
	public Table visit(PairExpList el) {
		double value = el.getHead().accept(this).value;
		System.out.println(value);
		return el.getTail().accept(this);
	}

	@Override
	public Table visit(LastExpList el) {
		Table t = el.getHead().accept(this);
		System.out.println(t.value);
		return t;
	}


}
