package datos;

import java.util.ArrayList;
import java.util.List;
import entidad.*;

public class DatosSistema {
    public static List<Paciente> pacientes = new ArrayList<>();
    public static List<Medico> medicos = new ArrayList<>();
    public static List<Especialidad> especialidades = new ArrayList<>();
    public static List<Cita> citas = new ArrayList<>();
    public static List<Receta> recetas = new ArrayList<>();
    public static List<HistorialMedico> historiales = new ArrayList<>();
    public static List<Medicamento> medicamentos = new ArrayList<>();

    public static void cargarDatosIniciales() {
        if (pacientes.isEmpty()) {
            pacientes.add(new Paciente("P001", "71234567", "Juan Pérez", "987654321"));
            pacientes.add(new Paciente("P002", "45678912", "María Delgado", "912345678"));
            pacientes.add(new Paciente("P003", "09876543", "Luis Fernández", "955443322"));
        }
        if (especialidades.isEmpty()) {
            especialidades.add(new Especialidad("E001", "Cardiología", "Enfermedades del corazón"));
            especialidades.add(new Especialidad("E002", "Pediatría", "Atención infantil"));
            especialidades.add(new Especialidad("E003", "Medicina General", "Consulta integral"));
            especialidades.add(new Especialidad("E004", "Endocrinología", "Enfermedades hormonales y metabólicas"));
            especialidades.add(new Especialidad("E005", "Neurología", "Enfermedades del sistema nervioso"));
            especialidades.add(new Especialidad("E006", "Dermatología", "Enfermedades de la piel"));
        }
        if (medicos.isEmpty()) {
            medicos.add(new Medico("M001", "CMP-10293", "Carlos Mendoza", "Cardiología"));
            medicos.add(new Medico("M002", "CMP-49301", "Ana María Ramos", "Pediatría"));
            medicos.add(new Medico("M003", "CMP-84920", "Roberto Gómez", "Medicina General"));
        }
        if (citas.isEmpty()) {
            citas.add(new Cita("C001", "2026-09-28", "09:00 AM", pacientes.get(0), medicos.get(0), "Confirmada"));
            citas.add(new Cita("C002", "2026-09-28", "10:30 AM", pacientes.get(1), medicos.get(1), "Pendiente"));
        }
        if (recetas.isEmpty()) {
            recetas.add(new Receta("R001", pacientes.get(0), "Paracetamol 500mg", "Cada 8 horas por 3 días"));
            recetas.add(new Receta("R002", pacientes.get(1), "Ibuprofeno 400mg", "Cada 12 horas por 5 días"));
            recetas.add(new Receta("R003", pacientes.get(0), "Losartán 50mg", "Una tableta cada 24 horas"));
            recetas.add(new Receta("R004", pacientes.get(1), "Salbutamol", "Según indicación médica"));
        }
        if (historiales.isEmpty()) {
            historiales.add(new HistorialMedico("H001", pacientes.get(0), "Hipertensión Leve", "Alergia a la Penicilina", "2026-01-15"));
            historiales.add(new HistorialMedico("H002", pacientes.get(1), "Asma Bronquial", "Ninguna", "2026-03-20"));
        }
        if (medicamentos.isEmpty()) {
            medicamentos.add(new Medicamento("MED001", "Paracetamol 500mg", 120, "2027-06-30"));
            medicamentos.add(new Medicamento("MED002", "Ibuprofeno 400mg", 80, "2027-04-15"));
            medicamentos.add(new Medicamento("MED003", "Losartán 50mg", 60, "2028-01-20"));
            medicamentos.add(new Medicamento("MED004", "Salbutamol", 40, "2027-09-10"));
            medicamentos.add(new Medicamento("MED005", "Amoxicilina 500mg", 75, "2027-11-05"));
        }
    }
}
