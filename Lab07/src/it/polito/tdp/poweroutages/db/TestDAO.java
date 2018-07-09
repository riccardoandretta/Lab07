package it.polito.tdp.poweroutages.db;

import it.polito.tdp.poweroutages.model.Nerc;

public class TestDAO {

	public static void main(String[] args) {
	
		PowerOutageDAO podao = new PowerOutageDAO();
		
//		System.out.println("Periodo: \n");
//		podao.getPeriodFromNerc(new Nerc(1, "ERCOT"));
//		System.out.println("Durata: \n");
//		podao.getDurationFromNerc(new Nerc(1, "ERCOT"));
		
		podao.getDateFromNerc(new Nerc(1, "ERCOT"));
		
		System.out.println(podao.getDateAndCustomersFromNerc(new Nerc(1, "ERCOT")));
	}

}
