package persistencia;

import implementacion.Odontologo;
import implementacion.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdministradorPersistenciaPaciente extends AdministradorPersistencia {
	
	private static AdministradorPersistenciaPaciente instance;
	
	private AdministradorPersistenciaPaciente() {
		
	}
	
	public static AdministradorPersistenciaPaciente getInstancia() {
		if (instance == null)
			instance = new AdministradorPersistenciaPaciente();
		return instance;
	}

	public void insert(Object o) {
		Paciente paciente = (Paciente) o;
		
		try{
			Connection con = Conexion.connect();
			PreparedStatement ps = con.prepareStatement("INSERT INTO "+super.getDatabase()+".dbo.Pacientes VALUES (?,?,?,?,?,?,?,?,?)");
			ps.setString(1, paciente.getDni());
			ps.setString(2, paciente.getNombre());
			ps.setString(3, paciente.getApellido());
			ps.setString(4, paciente.getTelefono());
			ps.setString(5, paciente.getEmail());
			ps.setDate(6, paciente.getFechaNacimiento());
			ps.setString(7, paciente.getGenero());
			ps.setString(8, paciente.getObraSocial());
			ps.setString(9, paciente.getPlanObraSocial());
			
			ps.execute();
			
			con.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
	}

	public void update(Object o) {
		Paciente paciente = (Paciente) o;
		
		try{
			Connection con = Conexion.connect();
			PreparedStatement ps = con.prepareStatement("UPDATE "+super.getDatabase()+".dbo.Pacientes"
					+ " SET nombre = ?"
					+ ", apellido = ?"
					+ ", telefono = ?"
					+ ", email = ?"
					+ ", fecha_nac = ?"
					+ ", genero = ?"
					+ ", obra_social = ?"
					+ ", plan_obra_social = ?"
					+ " WHERE dni like ?");
			ps.setString(1, paciente.getNombre());
			ps.setString(2, paciente.getApellido());
			ps.setString(3, paciente.getTelefono());
			ps.setString(4, paciente.getEmail());
			ps.setDate(5, paciente.getFechaNacimiento());
			ps.setString(6, paciente.getGenero());
			ps.setString(7, paciente.getObraSocial());
			ps.setString(8, paciente.getPlanObraSocial());
			ps.setString(9, paciente.getDni());
			
			ps.execute();
			
			con.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void delete(Object o) {
		Paciente paciente = (Paciente) o;
		try{
			Connection con = Conexion.connect();
			PreparedStatement ps = con.prepareStatement("DELETE FROM "+super.getDatabase()+".dbo.Pacientes WHERE dni like ?");
			ps.setString(1, paciente.getDni());		
			ps.execute();
			con.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

	public Paciente buscarPaciente(String dni) {
		Paciente paciente = null;
		try{
			Connection con = Conexion.connect();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM "+super.getDatabase()+".dbo.Pacientes WHERE dni like ?");
			ps.setString(1, dni);
			ResultSet rs = ps.executeQuery();
			paciente = new Paciente();
			con.close();
		}
		catch (SQLException e){
			e.printStackTrace();
		}
		return paciente;
	}

}
