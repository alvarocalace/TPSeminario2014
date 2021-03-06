package persistencia;

import implementacion.HistoriaClinica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import controlador.Controlador;

public class AdministradorPersistenciaHistoriasClinicas extends AdministradorPersistencia{
	
	private static AdministradorPersistenciaHistoriasClinicas instancia = null;
	
	private AdministradorPersistenciaHistoriasClinicas(){
		
	}
	
	public static AdministradorPersistenciaHistoriasClinicas getInstancia(){
		if (instancia==null)
			instancia = new AdministradorPersistenciaHistoriasClinicas();
		return instancia;
	}

	public void insert(HistoriaClinica historia) {
		try{
			Connection con = Conexion.connect();
			PreparedStatement ps = con.prepareStatement("INSERT INTO "+super.getDatabase()+".dbo.HistoriasClinicas(dni, descripcion) VALUES (?,?)");
			ps.setString(1, historia.getPaciente().getDni());
			ps.setString(2, historia.getDescripcion());
			
			ps.execute();
			
			con.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
	}

	public void update(HistoriaClinica historia) {
		try{
			Connection con = Conexion.connect();
			PreparedStatement ps = con.prepareStatement("UPDATE "+super.getDatabase()+".dbo.HistoriasClinicas SET descripcion = ? WHERE dni like ?");
			ps.setString(1, historia.getDescripcion());
			ps.setString(2, historia.getPaciente().getDni());
			
			ps.execute();
			
			con.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

	public void delete(HistoriaClinica historia) {
		try{
			Connection con = Conexion.connect();
			PreparedStatement ps = con.prepareStatement("UPDATE "+super.getDatabase()+".dbo.HistoriasClinicas SET activo=0 WHERE dni like ?");
			ps.setString(1, historia.getPaciente().getDni());
			
			ps.execute();
			
			con.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public Collection<HistoriaClinica> buscarHistorias(){
		Collection<HistoriaClinica> historias = new ArrayList<HistoriaClinica>(); 
		try{
			Connection con = Conexion.connect();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM "+super.getDatabase()+".dbo.HistoriasClinicas WHERE activo = 1");
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()){
				HistoriaClinica historia = new HistoriaClinica();
				
				historia.setPaciente(Controlador.getInstancia().obtenerPaciente(rs.getString("dni")));
				historia.setDescripcion(rs.getString("descripcion"));
				historia.setFicha(AdministradorPersistenciaFichaPeriodontal.getInstancia().buscarFicha(rs.getString("dni")));
				historia.setOdontogramas(AdministradorPersistenciaOdontograma.getInstancia().buscarOdontogramas(historia));
				historia.setObservaciones(AdministradorPersistenciaObservaciones.getInstancia().buscarObservaciones(historia));
				
				historias.add(historia);
			}
			
			con.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		
		return historias;
	}
	
	public HistoriaClinica buscarHistoria(String dni){
		HistoriaClinica historia = null;
		try{
			Connection con = Conexion.connect();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM "+super.getDatabase()+".dbo.HistoriasClinicas WHERE dni like ?");
			ps.setString(1, dni);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()){
				historia = new HistoriaClinica();
				
				historia.setPaciente(Controlador.getInstancia().obtenerPaciente(rs.getString("dni")));
				historia.setDescripcion(rs.getString("descripcion"));
				historia.setFicha(AdministradorPersistenciaFichaPeriodontal.getInstancia().buscarFicha(rs.getString("dni")));
				historia.setOdontogramas(AdministradorPersistenciaOdontograma.getInstancia().buscarOdontogramas(historia));
				historia.setObservaciones(AdministradorPersistenciaObservaciones.getInstancia().buscarObservaciones(historia));
			}
			
			con.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		
		return historia;
	}
}
