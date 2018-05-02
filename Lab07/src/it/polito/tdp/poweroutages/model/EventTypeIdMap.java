package it.polito.tdp.poweroutages.model;

import java.util.HashMap;
import java.util.Map;

public class EventTypeIdMap {

	private Map<Integer, EventType> map;
	
	public EventTypeIdMap() {
		map = new HashMap<>();
	}
	
	public EventType get(int id) {
		return map.get(id);
	}
	
	public EventType get(EventType et) {
		EventType old = map.get(et.getId());
		if (old == null) {
			map.put(et.getId(), et);
			return et;
		}
		return old;
	}
	
	public void put(Integer id, EventType et) {
		map.put(id, et);
	}
}
