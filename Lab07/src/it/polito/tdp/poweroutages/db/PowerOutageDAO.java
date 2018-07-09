package it.polito.tdp.poweroutages.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.time.*;

import it.polito.tdp.poweroutages.model.EventType;
import it.polito.tdp.poweroutages.model.EventTypeIdMap;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.NercIdMap;

public class PowerOutageDAO {

	public List<Nerc> getNercList(NercIdMap nercmap) {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(nercmap.get(n));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}

	public List<EventType> getEventTypeList(EventTypeIdMap etmap) {

		String sql = "SELECT id, value FROM eventtype";
		List<EventType> eventList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				EventType et = new EventType(res.getInt("id"), res.getString("value"));
				eventList.add(etmap.get(et));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return eventList;

	}

	public List<Integer> getCostumersAffectedByNerc(Nerc nerc) {

		String sql = "SELECT customers_affected FROM poweroutages WHERE nerc_id = ?";
		List<Integer> costumers = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, nerc.getId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				costumers.add(res.getInt("customers_affected"));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return costumers;
	}

	// public List<Period> getPeriodFromNerc(Nerc nerc) {
	//
	// String sql = "SELECT date_event_began, date_event_finished FROM poweroutages
	// WHERE nerc_id = ?";
	// List<Period> years = new ArrayList<>();
	//
	// try {
	// Connection conn = ConnectDB.getConnection();
	// PreparedStatement st = conn.prepareStatement(sql);
	//
	// st.setInt(1, nerc.getId());
	// ResultSet res = st.executeQuery();
	//
	// while (res.next()) {
	// LocalDate began = res.getDate("date_event_began").toLocalDate();
	// LocalDate finished = res.getDate("date_event_finished").toLocalDate();
	//
	// System.out.println(began);
	// System.out.println(finished);
	//
	// Period p = began.until(finished);
	// System.out.println(p);
	//
	// years.add(p);
	// }
	//
	// conn.close();
	//
	// } catch (SQLException e) {
	// throw new RuntimeException(e);
	// }
	// return years;
	// }
	//
	// public List<Long> getDurationFromNerc(Nerc nerc) {
	// // getDate mi perde le informazioni sulle ore e i minuti
	//
	// String sql = "SELECT Time(date_event_began), Time(date_event_finished) FROM
	// poweroutages WHERE nerc_id = ?";
	// List<Long> hours = new ArrayList<>();
	//
	// try {
	// Connection conn = ConnectDB.getConnection();
	// PreparedStatement st = conn.prepareStatement(sql);
	//
	// st.setInt(1, nerc.getId());
	// ResultSet res = st.executeQuery();
	//
	// while (res.next()) {
	// Time began = res.getTime("Time(date_event_began)"); // getDate mi perde l'
	// Time finished = res.getTime("Time(date_event_finished)");
	//
	// System.out.println(began);
	// System.out.println(finished);
	//
	// long d = (finished.getTime() - began.getTime()) / (1000 * 60 * 60); //
	// trasformo da ms ad ore --> così
	// // restituisce la differenza in ore
	// // dei giorni (non le ore totali
	// // però, ad esempio 30 ore diventa
	// // 24 ore)
	// System.out.println(d);
	// hours.add(d);
	// }
	//
	// conn.close();
	//
	// } catch (SQLException e) {
	// throw new RuntimeException(e);
	// }
	// return hours;
	// }

	public List<LocalDateTime> getDateFromNerc(Nerc nerc) {

		String sql = "SELECT Date(date_event_began), Time(date_event_began), Date(date_event_finished), Time(date_event_finished) FROM poweroutages WHERE nerc_id = ? GROUP BY Date(date_event_began)";
		List<LocalDateTime> date = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, nerc.getId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				LocalDateTime began = res.getDate("Date(date_event_began)").toLocalDate()
						.atTime(res.getTime("Time(date_event_began)").toLocalTime());

				LocalDateTime finished = res.getDate("Date(date_event_finished)").toLocalDate()
						.atTime(res.getTime("Time(date_event_finished)").toLocalTime());

				// System.out.println(began);
				// System.out.println(finished);

				date.add(began);
				date.add(finished);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return date;
	}
	
	public Map<LocalDateTime, Integer> getDateAndCustomersFromNerc(Nerc nerc) {

		String sql = "SELECT customers_affected, Date(date_event_began), Time(date_event_began), Date(date_event_finished), Time(date_event_finished) FROM poweroutages WHERE nerc_id = ? GROUP BY Date(date_event_began)";
		Map<LocalDateTime, Integer> date = new TreeMap<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, nerc.getId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				LocalDateTime began = res.getDate("Date(date_event_began)").toLocalDate()
						.atTime(res.getTime("Time(date_event_began)").toLocalTime());

				LocalDateTime finished = res.getDate("Date(date_event_finished)").toLocalDate()
						.atTime(res.getTime("Time(date_event_finished)").toLocalTime());
				
				int cust = res.getInt("customers_affected");

				// System.out.println(began);
				// System.out.println(finished);

				date.put(began, cust);
				date.put(finished, cust);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return date;
	}


}
