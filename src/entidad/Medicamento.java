package entidad;

public class Medicamento {
	private String id;
	private String nombre;
	private int stockDisponible;
	private String fechaVencimiento;

	public Medicamento(String id, String nombre, int stockDisponible, String fechaVencimiento) {
		this.id = id;
		this.nombre = nombre;
		this.stockDisponible = stockDisponible;
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getStockDisponible() {
		return stockDisponible;
	}

	public void setStockDisponible(int stockDisponible) {
		this.stockDisponible = stockDisponible;
	}

	public String getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public void actualizarStock(int cantidad) {
		this.stockDisponible += cantidad;
		if (this.stockDisponible < 0)
			this.stockDisponible = 0;
	}

	public boolean estaVencido() {
		return false; // La fecha se mantiene como texto para seguir la estructura actual del
						// proyecto.
	}

	@Override
	public String toString() {
		return nombre;
	}
}
