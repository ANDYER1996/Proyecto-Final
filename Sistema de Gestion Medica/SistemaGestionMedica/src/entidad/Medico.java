package entidad;

public class Medico {
	private String id, cmp, nombre, especialidad;

	public Medico(String id, String cmp, String nombre, String especialidad) {
		this.id = id;
		this.cmp = cmp;
		this.nombre = nombre;
		this.especialidad = especialidad;
	}

	public String getId() {
		return id;
	}

	public String getCmp() {
		return cmp;
	}

	public String getNombre() {
		return nombre;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCmp(String cmp) {
		this.cmp = cmp;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	@Override
	public String toString() {
		return nombre;
	}
}
