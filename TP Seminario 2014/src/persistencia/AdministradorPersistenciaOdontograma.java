package persistencia;

import implementacion.Diente;
import implementacion.HistoriaClinica;
import implementacion.Odontograma;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import controlador.Controlador;

public class AdministradorPersistenciaOdontograma extends AdministradorPersistencia{
	
	private static AdministradorPersistenciaOdontograma instancia = null;
	
	private AdministradorPersistenciaOdontograma(){
		
	}
	
	public static AdministradorPersistenciaOdontograma getInstancia(){
		if (instancia==null)
			instancia = new AdministradorPersistenciaOdontograma();
		return instancia;
	}

	public void insert(Odontograma odontograma, HistoriaClinica historia) {		
		try{
			Connection con = Conexion.connect();
			PreparedStatement ps = con.prepareStatement("INSERT INTO "+super.getDatabase()+".dbo.Odontogramas(dni, id_odontograma, fecha, matricula) VALUES (?,?,?,?)");
			ps.setString(1, historia.getPaciente().getDni());
			ps.setString(2, odontograma.getIdOdontograma());
			ps.setTimestamp(3, odontograma.getFecha());
			ps.setString(4, odontograma.getOdontologo().getMatricula());
			
			ps.execute();			
			
			for (Diente diente : odontograma.getDientes()){
				AdministradorPersistenciaDiente.getInstancia().insert(diente,odontograma);
			}
			
			con.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
	}

	public void update(Odontograma odontograma) {
		try{
			Connection con = Conexion.connect();
			PreparedStatement ps = con.prepareStatement("UPDATE "+super.getDatabase()+".dbo.Odontogramas SET fecha = ?, matricula = ? WHERE id_odontograma like ?");
			ps.setTimestamp(1, odontograma.getFecha());
			ps.setString(2, odontograma.getOdontologo().getMatricula());
			ps.setString(3, odontograma.getIdOdontograma());
			
			ps.execute();
			
			for (Diente diente : odontograma.getDientes()){
				AdministradorPersistenciaDiente.getInstancia().update(diente, odontograma);
			}			
			
			con.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
	}

	public void delete(Odontograma odontograma) {
		try{
			Connection con = Conexion.connect();
			PreparedStatement ps = con.prepareStatement("UPDATE "+super.getDatabase()+"dbo.Odontogramas SET activo = 0 WHERE id_odontograma = ?");
			ps.setString(1,odontograma.getIdOdontograma());
			
			for (Diente diente : odontograma.getDientes()){
				AdministradorPersistenciaDiente.getInstancia().delete(diente, odontograma);
			}
			
			ps.execute();
			
			con.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

	public Odontograma buscarOdontograma(){
		Odontograma odontograma = null;
		try{
			Connection con = Conexion.connect();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM "+super.getDatabase()+".dbo.Odontogramas WHERE activo=1");
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()){
				odontograma = new Odontograma();
				
				odontograma.setFecha(rs.getTimestamp("fecha"));
				odontograma.setOdontologo(Controlador.getInstancia().obtenerOdontologo(rs.getString("matricula")));
				odontograma.setIdOdontograma(rs.getString("id_odontograma"));
				odontograma.setDientes(AdministradorPersistenciaDiente.getInstancia().buscarDientes(odontograma));
			}
			
			con.close();			
		}
		catch (SQLException e){
			e.printStackTrace();
		}

		return odontograma;
	}
	
	public Collection<Odontograma> buscarOdontogramas(HistoriaClinica historia){
		Collection<Odontograma> odontogramas = new ArrayList<Odontograma>();
		Odontograma odontograma;
		try{
			Connection con = Conexion.connect();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM "+super.getDatabase()+".dbo.Odontogramas WHERE dni = ? AND activo=1");
			ps.setString(1, historia.getPaciente().getDni());
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()){
				odontograma = new Odontograma();
				
				odontograma.setFecha(rs.getTimestamp("fecha"));
				odontograma.setOdontologo(Controlador.getInstancia().obtenerOdontologo(rs.getString("matricula")));
				odontograma.setIdOdontograma(rs.getString("id_odontograma"));
				odontograma.setDientes(AdministradorPersistenciaDiente.getInstancia().buscarDientes(odontograma));
				
				odontogramas.add(odontograma);
			}
			
			con.close();			
		}
		catch (SQLException e){
			e.printStackTrace();
		}

		return odontogramas;
	}
	
	public int conteoOdontogramas() {
		int conteo = 0;
		
		try {
			Connection con = Conexion.connect();
			PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM "+super.getDatabase()+".dbo.Odontogramas");

			ResultSet rs = ps.executeQuery();
			
			while (rs.next()){
				conteo = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conteo;
	}
	
}
