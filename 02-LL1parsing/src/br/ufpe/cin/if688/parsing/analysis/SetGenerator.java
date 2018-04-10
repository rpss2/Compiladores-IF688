package br.ufpe.cin.if688.parsing.analysis;

import java.util.*;

import br.ufpe.cin.if688.parsing.grammar.*;


public final class SetGenerator {
    
    public static Map<Nonterminal, Set<GeneralSymbol>> getFirst(Grammar g) {
        
    	if (g == null) throw new NullPointerException("g nao pode ser nula.");
        
    	Map<Nonterminal, Set<GeneralSymbol>> first = initializeNonterminalMapping(g);
    	/*
		 * Implemente aqui o m�todo para retornar o conjunto first
		*/
    	
    	Collection<Production> productions = g.getProductions();
    	Iterator<Production> itProds = productions.iterator();
    	
    	Nonterminal nonTerminal;
    	
    	while(itProds.hasNext()) {
    		nonTerminal = itProds.next().getNonterminal();
    		first.get(nonTerminal).addAll(getFirstSymbol(g, nonTerminal, productions));
    	}

        return first;
    	
    }
    
    public static Set<GeneralSymbol> getFirstSymbol(Grammar g, Nonterminal nonTerminal, Collection<Production> productions) {
    	
    	
    	Set<GeneralSymbol> result = new HashSet<GeneralSymbol>();
    	
    	List<Production> prods_N = new ArrayList<Production>();
    	Iterator<Production> it_prod = productions.iterator();
    	
    	while(it_prod.hasNext()) {
    		Production p = it_prod.next();
    		if(p.getNonterminal().equals(nonTerminal)) {
    			prods_N.add(p);
    		}
    	}
    	
    	Iterator<GeneralSymbol> it_symbols = null;
    	GeneralSymbol s_current;
    	
    	for (Production p : prods_N) {
    		it_symbols = p.iterator();
    		while(it_symbols.hasNext()) {
    			s_current = it_symbols.next();
    			if(s_current.equals(SpecialSymbol.EPSILON)) { //cadeia vazia
    				result.add(SpecialSymbol.EPSILON);
    			} else if(((Symbol) s_current).isTerminal()) { //simbolo terminal
    				result.add(s_current);
    				break;
    			} else { //nao terminal, passo recursivo
    				Set<GeneralSymbol> aux = getFirstSymbol(g, (Nonterminal) s_current, productions);
    				result.addAll(aux);
    				if(!aux.contains(SpecialSymbol.EPSILON))
    					break;
    				else if (it_symbols.hasNext()) {
    					result.remove(SpecialSymbol.EPSILON);
    				}
    			}
    		}
    	}
    	
    	return result;
    }

    
    public static Map<Nonterminal, Set<GeneralSymbol>> getFollow(Grammar g, Map<Nonterminal, Set<GeneralSymbol>> first) {
        
        if (g == null || first == null)
            throw new NullPointerException();
                
        Map<Nonterminal, Set<GeneralSymbol>> follow = initializeNonterminalMapping(g);
        
        /*
         * implemente aqui o método para retornar o conjunto follow
        */
        
        Collection<Production> productions = g.getProductions();
        Nonterminal startSymbol = g.getStartSymbol();
        Set<GeneralSymbol> currentSet;
        follow.get(startSymbol).add(SpecialSymbol.EOF);
        
        Set<GeneralSymbol> trailer;
        Map<Nonterminal, Set<GeneralSymbol>> followAux; // usado para comparar com follow
        boolean changing = true;
        
        while(changing) {
        	followAux = new HashMap<Nonterminal, Set<GeneralSymbol>>();
        	for (Nonterminal n : follow.keySet()) { // copia follow para follow_aux
        		trailer = new HashSet<GeneralSymbol>();
        		trailer.addAll(follow.get(n));
        		followAux.put(n, trailer);
        	}
        	//Baseado no Algoritmo do livro ENGINEERING A COMPILER (page 106)
        	for(Production p : productions) {
        		trailer = new HashSet<GeneralSymbol>();
        		trailer.addAll(follow.get(p.getNonterminal()));
        		for (int i = p.getProduction().size() - 1; i >= 0; i--) {
        			if(p.getProduction().get(i) instanceof Nonterminal) {
        				follow.get(p.getProduction().get(i)).addAll(trailer);
        				if(first.get(p.getProduction().get(i)).contains(SpecialSymbol.EPSILON)) {
        					trailer.addAll(first.get(p.getProduction().get(i)));
        					trailer.remove(SpecialSymbol.EPSILON);
        				} else {
        					trailer = new HashSet<GeneralSymbol>();
        					trailer.addAll(first.get(p.getProduction().get(i)));
        				}
        			} else {
        				trailer = new HashSet<GeneralSymbol>();
        				trailer.add(p.getProduction().get(i));
        			}
        		}
        	}
        	changing = !follow.equals(followAux);
        }
        
        return follow;
    }
    
    //método para inicializar mapeamento nãoterminais -> conjunto de símbolos
    private static Map<Nonterminal, Set<GeneralSymbol>>
    initializeNonterminalMapping(Grammar g) {
    Map<Nonterminal, Set<GeneralSymbol>> result = 
        new HashMap<Nonterminal, Set<GeneralSymbol>>();

    for (Nonterminal nt: g.getNonterminals())
        result.put(nt, new HashSet<GeneralSymbol>());

    return result;
}

} 
