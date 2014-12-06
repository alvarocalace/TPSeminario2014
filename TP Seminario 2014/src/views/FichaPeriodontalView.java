package views;

import java.util.Collection;



public class FichaPeriodontalView {
	private PacienteView paciente;
	private Collection<SeccionView> secciones;
	private OdontologoView odontologo;
	
	public FichaPeriodontalView() {
	
	}
	
	public void modificarSeccion(String seccion, String sDiente, boolean sangrado, boolean placa, int margen) {
	
	}
	
	public SeccionView buscarSeccion(int seccion) {
		return null;
	}
	
	public Collection<String> getSintomas() {
		return null;
	}

	public Collection<SeccionView> getSecciones() {
		return secciones;
	}

	public void setSecciones(Collection<SeccionView> secciones) {
		this.secciones = secciones;
	}

	public OdontologoView getOdontologo() {
		return odontologo;
	}

	public void setOdontologo(OdontologoView odontologo) {
		this.odontologo = odontologo;
	}
 
	public void agregarSeccion(SeccionView seccion) {
		this.secciones.add(seccion);
	}

	public PacienteView getPaciente() {
		return paciente;
	}

	public void setPaciente(PacienteView paciente) {
		this.paciente = paciente;
	}
	
	
}