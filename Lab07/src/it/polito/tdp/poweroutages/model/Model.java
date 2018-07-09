package it.polito.tdp.poweroutages.model;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.polito.tdp.poweroutages.db.PowerOutageDAO;

public class Model {

	PowerOutageDAO podao;
	private NercIdMap nercmap;
	private EventTypeIdMap etmap;

	List<LocalDateTime> soluzione;
	private int massimoCostumers;

	public Model() {
		podao = new PowerOutageDAO();
		nercmap = new NercIdMap();
		etmap = new EventTypeIdMap();
	}

	public List<Nerc> getNercList() {
		return podao.getNercList(nercmap);
	}

	public List<LocalDateTime> calcolaWorstCase(Nerc nerc, int maxAnni, int maxOre) {
		if (maxOre < 1 || maxAnni <= 0)
			return new ArrayList<>();
		if (!this.getNercList().contains(nerc)) {
			return new ArrayList<>();
		}

		massimoCostumers = 0;
		soluzione = new ArrayList<>();

		cerca(0, soluzione, nerc, maxAnni, maxOre);

		return soluzione;

	}

	private void cerca(int livello, List<LocalDateTime> parziale, Nerc nerc, int maxAnni, int maxOre) {
		// Condizione terminazione
		if (livello >= maxOre) { // <-- non è cosi

			// int sommaCostumers = this.calcolaTotaleCostumers(nerc);

			// if (sommaCostumers > massimoCostumers) {
			// massimoCostumers = sommaCostumers;
			// this.soluzione = new ArrayList<>(parziale);
			// }
		}

		// Caso intermedio
		{
			if (soluzioneValida(parziale, nerc, maxAnni, maxOre)) {

			}
		}

	}

	private int calcolaTotaleCostumers(List<LocalDateTime> parziale, Nerc nerc, int maxAnni, int maxOre) {
		int somma = 0;
		for (Integer i : podao.getCostumersAffectedByNerc(nerc)) {
			// if (somma)
			somma += i;
		}
		return somma;
	}

	private boolean soluzioneValida(List<LocalDateTime> parziale, Nerc nerc, int maxAnni, int maxOre) {

		if (parziale == null) { // se null non è valido
			return false;
		}
		if (parziale.size() == 0) { // ma se è vuota potrebbe essere valida
			return true;
		}

		return false;
	}

	public List<LocalDateTime> getDateFromNerc(Nerc nerc) {
		return podao.getDateFromNerc(nerc);
	}

	public List<Long> getDuration(Nerc nerc) {

		List<LocalDateTime> ldtBegan = new ArrayList<>();
		List<LocalDateTime> ldtFinished = new ArrayList<>();
		List<Long> durationList = new ArrayList<>();

		for (int i = 0; i < this.getDateFromNerc(nerc).size(); i++) {
			if (i == 0 || i % 2 == 0) { // se indice pari ho 'began'
				ldtBegan.add(this.getDateFromNerc(nerc).get(i));
			} else { // ho indice dispari quindi 'finished'
				ldtFinished.add(this.getDateFromNerc(nerc).get(i));
			}
		}

		for (int i = 0; i < ldtBegan.size(); i++) {
			for (int j = 0; j < ldtFinished.size(); j++) {
				if (i == j) {
					long d = ldtBegan.get(i).until(ldtFinished.get(j), ChronoUnit.HOURS);
					durationList.add(d);
				}
			}
		}

		return durationList;

	}

	public List<Period> getPeriod(Nerc nerc) {
		List<LocalDateTime> ldtBegan = new ArrayList<>();
		List<LocalDateTime> ldtFinished = new ArrayList<>();

		List<Period> periodList = new ArrayList<>();

		for (int i = 0; i < this.getDateFromNerc(nerc).size(); i++) {
			if (i == 0 || i % 2 == 0) { // se indice pari ho 'began'
				ldtBegan.add(this.getDateFromNerc(nerc).get(i));
			} else { // ho indice dispari quindi 'finished'
				ldtFinished.add(this.getDateFromNerc(nerc).get(i));
			}
		}

		for (int i = 0; i < ldtBegan.size(); i++) {
			for (int j = 0; j < ldtFinished.size(); j++) {
				if (i == j) {
					Period p = ldtBegan.get(i).toLocalDate().until(ldtFinished.get(j).toLocalDate());
					periodList.add(p);
				}
			}
		}

		return periodList;
	}

//	public Map<Period, Integer> getCostumersByPeriod(Nerc nerc) {
//		Map<LocalDateTime, Integer> datcost = podao.getDateAndCustomersFromNerc(nerc);
//		Map<Period, Integer> costumers = new HashMap<>();
//		List<Integer> temp = new ArrayList<>(datcost.values());
//		
//		for (int i = 0; i < temp.size(); i = i + 2) {
//			for (Period p : this.getPeriod(nerc)) {
//				costumers.put(p, temp.get(i));
//			}
//		}
//		return costumers;
//	}
//	
//	public Map<Long, Integer> getCostumersByDuration(Nerc nerc) {
//		Map<LocalDateTime, Integer> datcost = podao.getDateAndCustomersFromNerc(nerc);
//		Map<Long, Integer> costumers = new HashMap<>();
//		List<Integer> temp = new ArrayList<>(datcost.values());
//		
//		for (int i = 0; i < temp.size(); i = i + 2) {
//			for (Long l : this.getDuration(nerc)) {
//				costumers.put(l, temp.get(i));
//			}
//		}
//		return costumers;
//	}

}
