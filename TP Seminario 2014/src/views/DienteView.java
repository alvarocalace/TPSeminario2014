package views;

import java.util.Collection;

public class DienteView {
	private String posicion;
	private String idProtesis;
	private String idPuente;
	private String estado;
	private Collection<CaraView> caras;

	public DienteView() {

	}

	public DienteView(String posicion, String idProtesis, String idPuente,
			String estado, Collection<CaraView> caras) {

		this.posicion = posicion;
		this.idProtesis = idProtesis;
		this.idPuente = idPuente;
		this.estado = estado;
		this.caras = caras;

	}

	public boolean sosElDiente(String posicion) {
		return this.posicion == posicion;
	}

	public void modificarEstadoCara(String posicionCara, String estadoCara) {

	}

	public CaraView buscarCara(String posicionCara) {
		return null;
	}

	public void setPuenteNuevo() {

	}

	public Collection<String> getSintomas() {
		return null;
	}

	public String getPosicion() {
		return posicion;
	}

	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}

	public String getIdProtesis() {
		return idProtesis;
	}

	public void setIdProtesis(String idProtesis) {
		this.idProtesis = idProtesis;
	}

	public String getIdPuente() {
		return idPuente;
	}

	public void setIdPuente(String idPuente) {
		this.idPuente = idPuente;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Collection<CaraView> getCaras() {
		return caras;
	}

	public void setCaras(Collection<CaraView> caras) {
		this.caras = caras;
	}
}
