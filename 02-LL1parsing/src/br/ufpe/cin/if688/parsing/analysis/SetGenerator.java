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
        
        Nonterminal startSymbol = g.getStartSymbol();
        Set<GeneralSymbol> currentSet; 
        currentSet = follow.get(startSymbol);
        currentSet.add(SpecialSymbol.EOF);
        
        Collection<Production> productions = g.getProductions();
        Iterator<GeneralSymbol> itSymbols, aux, aux2;
        GeneralSymbol s, b = null, f;
        boolean epsilon = false;
        
        for (Production p : productions) {
        	itSymbols = p.iterator();
        	aux = p.iterator();
        	while(itSymbols.hasNext()) {
        		s = itSymbols.next();
        		if(aux.hasNext())
        			b = aux.next();
        		if(!s.equals(SpecialSymbol.EPSILON) && !((Symbol) s).isTerminal()) {
        			currentSet = follow.get(s);
        			if(s == b) {
        				if(aux.hasNext()) {
        					b = aux.next();
        				} else {
        					epsilon = true;
        				}
        			}
    				if(((Symbol) b).isTerminal()) {
    					currentSet.add(b);
    				} else {
        				aux2 = first.get(b).iterator();
        				while(aux2.hasNext()) {
        					f = aux2.next();
        					if(!f.equals(SpecialSymbol.EPSILON)) {
        						currentSet.add(f);
        					} else {
        						epsilon = true;
        					}
        				}
    				}
    				if(epsilon) {
    					aux2 = follow.get(p.getNonterminal()).iterator();
    					while(aux2.hasNext()) {
    						currentSet.add(aux2.next());
    					}
    					epsilon = false;
    				}
    				follow.put((Nonterminal) s, currentSet);
        		}
        	}
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
