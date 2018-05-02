package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.db.PowerOutageDAO;

public class Model {

	PowerOutageDAO podao;
	private NercIdMap nercmap;
	private EventTypeIdMap etmap;
	
	List<String> soluzione;
	
	
	public Model() {
		podao = new PowerOutageDAO();
		nercmap = new NercIdMap();
		etmap = new EventTypeIdMap();
	}
	
	public List<Nerc> getNercList() {
		return podao.getNercList(nercmap);
	}
	
	public List<String> calcolaWorstCase(Nerc nerc, int maxAnni, int maxOre) {
		
		soluzione = new ArrayList<>();
		
		cerca (0, soluzione, nerc, maxAnni, maxOre);
		
		return soluzione;
		
	}

	private void cerca(int i, List<String> parziale, Nerc nerc, int maxAnni, int maxOre) {
		// TODO Auto-generated method stub
		
	}

}
