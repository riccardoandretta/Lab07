package it.polito.tdp.poweroutages.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		System.out.println(model.getNercList());
		
		System.out.println(model.getDateFromNerc(new Nerc(3, "MAAC")));
		
		System.out.println(model.getDuration(new Nerc(3, "MAAC")));
		
		System.out.println(model.getPeriod(new Nerc(3, "MAAC")));
		
		
	}
}
