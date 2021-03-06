package implementacion;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import persistencia.AdministradorPersistenciaOdontograma;
import views.DienteView;
import views.OdontogramaView;



public class Odontograma {
	private String idOdontograma;
	private Collection<Diente> dientes;
	private Timestamp fecha;
	private Odontologo odontologo;
	
	public Odontograma(String idOdontograma, Timestamp fecha, Odontologo odontologo, Collection<Diente> dientes, HistoriaClinica historiaClinica) {
		this.idOdontograma=idOdontograma;
		this.fecha=fecha;
		this.odontologo=odontologo;
		
		this.dientes=dientes;
		
		/*
		this.dientes= new ArrayList<Diente>();
		
		int i;
		
		for (i = 11;i<=18;i++)
			this.dientes.add(new Diente(Integer.toString(i)));
		
		for (i = 21;i<=28;i++)
			this.dientes.add(new Diente(Integer.toString(i)));
		
		for (i = 31;i<=38;i++)
			this.dientes.add(new Diente(Integer.toString(i)));
		
		for (i = 51;i<=55;i++)
			this.dientes.add(new Diente(Integer.toString(i)));
		
		for (i = 61;i<=65;i++)
			this.dientes.add(new Diente(Integer.toString(i)));
		
		for (i = 71;i<=75;i++)
			this.dientes.add(new Diente(Integer.toString(i)));
		
		for (i = 81;i<=85;i++)
			this.dientes.add(new Diente(Integer.toString(i)));	
		*/
		
		AdministradorPersistenciaOdontograma.getInstancia().insert(this, historiaClinica);
	}
	
	public Odontograma() {
	}

	public Collection<String> getSintomas() {
		return null;
	}

	public Collection<Diente> getDientes() {
		return dientes;
	}

	public void setDientes(Collection<Diente> dientes) {
		this.dientes = dientes;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public Odontologo getOdontologo() {
		return odontologo;
	}

	public void setOdontologo(Odontologo odontologo) {
		this.odontologo = odontologo;
	}

	public String getIdOdontograma() {
		return idOdontograma;
	}

	public void setIdOdontograma(String idOdontograma) {
		this.idOdontograma = idOdontograma;
	}
	
	public Collection<Observacion> generarObservaciones() {
		Collection<Observacion> observaciones = new ArrayList<Observacion>();
		for (Diente diente : dientes) {
			Observacion o = diente.generarObservacion(this);
			if (!o.getDescripcion().equals("-"))
				observaciones.add(o);
		}
		return observaciones;
	}
	
	public OdontogramaView generarView() {
		OdontogramaView view = new OdontogramaView();
		Collection<DienteView> dientesView = new ArrayList<DienteView>();
		for (Diente diente : dientes) {
			dientesView.add(diente.generarView());
		}
		view.setDientes(dientesView);
		view.setFecha(fecha);
		view.setIdOdontograma(idOdontograma);
		view.setOdontologo(odontologo.generarView());
		return view;
	}

	public boolean sosElOdontograma(String idOdontograma) {
		return this.idOdontograma.equals(idOdontograma);
	}	
}