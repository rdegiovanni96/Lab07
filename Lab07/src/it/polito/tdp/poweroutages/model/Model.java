package it.polito.tdp.poweroutages.model;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.db.PowerOutageDAO;

public class Model {

	PowerOutageDAO podao;
	private int bestScore;
	private List<Evento> soluzione;
	
	public Model() {
		podao = new PowerOutageDAO();
		bestScore = 0;
		soluzione = new ArrayList<Evento>();
	}
	
	public List<Nerc> getNercList() {
		return podao.getNercList();
	}
	
	public List<Evento> getEventiNerc(String nerc){
		return podao.getEventiFromNERC(nerc);
	}
	
	public List<String> getNomiNerc(){
		return podao.getNercName();
	}
	
	public String worstCase(String nerc, int maxAnni, int maxOre) {
		int livello = 0;
		List<Evento> parziale = new ArrayList<Evento>();
		List<Evento> lista = this.getEventiNerc(nerc);
		int disservizio = 0;
		String result = "";
		int totCoinvolti = 0;
		long totDisservizio = 0;
		
		recursive(lista, parziale, livello, maxAnni, maxOre, disservizio);
		
		for(Evento e: soluzione) {
			totDisservizio += e.getDisservizio();
			totCoinvolti += e.getUtentiCoinvolti();
			result += e.toString()+"\n";
		}
		
		return "Tot people affected: "+totCoinvolti+"\n"+"Tot hours of outage: "+totDisservizio+"\n"+result;
	}
	
	public boolean keepGoing(int maxAnni, int maxOre, int disservizio, List<Evento> parziale, Evento e) {
		if(parziale.contains(e))
			return false;
		if(parziale.size()==0)
			return true;
		if(parziale.size()>1) {
		if(e.getDataInizio().isBefore(parziale.get(parziale.size()-1).getDataInizio()))
			return false;}
		if(disservizio + e.getDisservizio() > maxOre)
			return false;
		if(parziale.get(0).getDataInizio().until(e.getDataInizio(), ChronoUnit.YEARS) > maxAnni)
			return false;
		return true;
	}
	
	public int calcolaPunteggio(List<Evento> parziale) {
		int result = 0;
		for(Evento e: parziale) {
			result += e.getUtentiCoinvolti();
		}
		
		return result;
	}
	
	
	private void recursive(List<Evento> lista, List<Evento> parziale, int livello, int maxAnni, int maxOre, int disservizio) {
		
		//condizione terminale
		
			int score = calcolaPunteggio(parziale);
			if(score > bestScore) {
				bestScore = score;
				soluzione = new ArrayList<>(parziale);
			}
		
		
		for(Evento e: lista) {
			if(keepGoing(maxAnni, maxOre, disservizio, parziale, e)) {
				parziale.add(e);
				disservizio += e.getDisservizio();
				System.out.println("Prova");
				recursive(lista, parziale, livello+1, maxAnni, maxOre, disservizio);
				parziale.remove(e);
				disservizio -= e.getDisservizio();
			}
		}
		
		
	}

}
