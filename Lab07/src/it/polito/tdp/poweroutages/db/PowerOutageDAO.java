package it.polito.tdp.poweroutages.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.model.Evento;
import it.polito.tdp.poweroutages.model.Nerc;

public class PowerOutageDAO {

	public List<Nerc> getNercList() {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}
	
	public List<String> getNercName() {
		String sql = "SELECT value FROM Nerc";
		List<String> result = new ArrayList<String>();
		

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(res.getString("value"));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return result;
		
	}
	
	public List<Evento> getEventiFromNERC(String nercName){
		
		String sql = "SELECT customers_affected, date_event_began, date_event_finished " + 
				"FROM Nerc as n, PowerOutages as po " + 
				"WHERE n.id = nerc_id AND n.value=? ORDER BY date_event_began";
		
		List<Evento> result = new ArrayList<Evento>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, nercName);
			
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				Evento e = new Evento(res.getInt("customers_affected"), res.getTimestamp("date_event_began").toLocalDateTime(), res.getTimestamp("date_event_finished").toLocalDateTime());
				result.add(e);
			}
			
			conn.close();
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
		
		
		
		return result;
		
		
		
	}

}
