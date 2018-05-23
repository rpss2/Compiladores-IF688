package br.ufpe.cin.if688.minijava.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import antlr.AntlrLexer;
import antlr.AntlrParser;
import br.ufpe.cin.if688.minijava.ast.Program;
import br.ufpe.cin.if688.minijava.visitor.PrettyPrintVisitor;

public class Main {

	public static void main(String[] args) throws IOException {
		InputStream stream = new FileInputStream("src/test/resources/LinkedList.java");
		ANTLRInputStream input = new ANTLRInputStream(stream);
		AntlrLexer lexer = new AntlrLexer(input);
		CommonTokenStream token = new CommonTokenStream(lexer);
		
		AntlrParser parser = new AntlrParser(token);	

		Program prog = (Program) new ASTVisitor().visit(parser.goal());
		//PrettyPrintVisitor ppv = new PrettyPrintVisitor();
		//ppv.visit(prog);
		
		prog.accept(new PrettyPrintVisitor());
	}

}
