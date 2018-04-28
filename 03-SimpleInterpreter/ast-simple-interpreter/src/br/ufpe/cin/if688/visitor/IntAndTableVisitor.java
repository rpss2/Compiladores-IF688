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
import br.ufpe.cin.if688.symboltable.IntAndTable;
import br.ufpe.cin.if688.symboltable.Table;

public class IntAndTableVisitor implements IVisitor<IntAndTable> {
	private Table t;

	public IntAndTableVisitor(Table t) {
		this.t = t;
	}

	@Override
	public IntAndTable visit(Stm s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntAndTable visit(AssignStm s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntAndTable visit(CompoundStm s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntAndTable visit(PrintStm s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntAndTable visit(Exp e) {
		return e.accept(this);
	}

	@Override
	public IntAndTable visit(EseqExp e) {
		this.t = new Interpreter(this.t).visit(e.getStm());
		return e.getExp().accept(this);
	}

	@Override
	public IntAndTable visit(IdExp e) {
		String id = e.getId();
		return new IntAndTable(searchId(id, this.t), this.t);
	}
	
	@Override
	public IntAndTable visit(NumExp e) {
		return new IntAndTable(e.getNum(), this.t);
	}
	
	public double searchId(String id, Table t) {
		if(id == t.id) {
			return t.value;
		}
		return searchId(id, t.tail);
	}


	@Override
	public IntAndTable visit(OpExp e) {
		IntAndTable operatingLeft = e.getLeft().accept(this);
		IntAndTable operatingRight = e.getRight().accept(this);
		int operator = e.getOper();
		double res = 0;
		switch (operator) {
		case OpExp.Plus:
			res = operatingLeft.result + operatingRight.result;
			break;
		case OpExp.Minus:
			res = operatingLeft.result - operatingRight.result;		
			break;
		case OpExp.Times:
			res = operatingLeft.result * operatingRight.result;
			break;
		case OpExp.Div:
			res = operatingLeft.result / operatingRight.result;
			break;
		}
		return new IntAndTable(res, this.t);
	}

	@Override
	public IntAndTable visit(ExpList el) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntAndTable visit(PairExpList el) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntAndTable visit(LastExpList el) {
		// TODO Auto-generated method stub
		return null;
	}


}
