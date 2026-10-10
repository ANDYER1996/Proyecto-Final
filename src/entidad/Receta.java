package entidad;

public class Receta {
	private String id, medicamento, indicaciones;
	private Paciente paciente;

	public Receta(String id, Paciente paciente, String medicamento, String indicaciones) {
		this.id = id;
		this.paciente = paciente;
		this.medicamento = medicamento;
		this.indicaciones = indicaciones;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public String getMedicamento() {
		return medicamento;
	}

	public void setMedicamento(String medicamento) {
		this.medicamento = medicamento;
	}

	public String getIndicaciones() {
		return indicaciones;
	}

	public void setIndicaciones(String indicaciones) {
		this.indicaciones = indicaciones;
	}
}
