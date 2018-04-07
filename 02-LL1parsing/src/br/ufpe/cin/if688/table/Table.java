package br.ufpe.cin.if688.table;


import br.ufpe.cin.if688.parsing.analysis.*;
import br.ufpe.cin.if688.parsing.grammar.*;
import java.util.*;


public final class Table {
	private Table() {    }

	public static Map<LL1Key, List<GeneralSymbol>> createTable(Grammar g) throws NotLL1Exception {
        if (g == null) throw new NullPointerException();

        Map<Nonterminal, Set<GeneralSymbol>> first =
            SetGenerator.getFirst(g);
        Map<Nonterminal, Set<GeneralSymbol>> follow =
            SetGenerator.getFollow(g, first);

        Map<LL1Key, List<GeneralSymbol>> parsingTable = 
            new HashMap<LL1Key, List<GeneralSymbol>>();

        /*
         * Implemente aqui o mÃ©todo para retornar a parsing table
        */
        
        Collection<Production> productions = g.getProductions();
        Iterator<GeneralSymbol> it_first, it_follow;
        Nonterminal nonTerminal;
        GeneralSymbol t_first, t_follow;
        LL1Key line;
        
        for(Production p : productions) {
        	nonTerminal = p.getNonterminal();
        	it_first = first.get(nonTerminal).iterator();
        	while(it_first.hasNext()) {
        		t_first = it_first.next();
        		if(!t_first.equals(SpecialSymbol.EPSILON) && !p.getProduction().contains(SpecialSymbol.EPSILON)) {
        			line = new LL1Key(nonTerminal, t_first);
        			parsingTable.put(line, p.getProduction());
        		} else if(t_first.equals(SpecialSymbol.EPSILON) && p.getProduction().contains(SpecialSymbol.EPSILON)) {
        			it_follow = follow.get(nonTerminal).iterator();
        			while(it_follow.hasNext()) {
        				t_follow = it_follow.next();
        				line = new LL1Key(nonTerminal, t_follow);
        				parsingTable.put(line, p.getProduction());
        			}
        		}
        	}
        }
        
        return parsingTable;
    }
}
