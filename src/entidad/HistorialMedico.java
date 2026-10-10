package entidad;

public class HistorialMedico {
	private String id, diagnostico, alergias, fecha;
	private Paciente paciente;

	public HistorialMedico(String id, Paciente paciente, String diagnostico, String alergias, String fecha) {
		this.id = id;
		this.paciente = paciente;
		this.diagnostico = diagnostico;
		this.alergias = alergias;
		this.fecha = fecha;
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

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public String getAlergias() {
		return alergias;
	}

	public void setAlergias(String alergias) {
		this.alergias = alergias;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
}
