package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import datos.DatosSistema;
import entidad.*;

public class FormCita extends JInternalFrame {
    private static final long serialVersionUID = 1L;

    private JComboBox<Paciente> cboPaciente;
    private JComboBox<Medico> cboMedico;
    private JComboBox<String> cboEspecialidad;
    private JTextField txtFecha, txtHora, txtBuscar;
    private DefaultTableModel modelCitas, modelMedicamentos;

    public FormCita() {
        setTitle("Proceso: Registrar y Buscar Cita Médica");
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        setBounds(40, 25, 820, 650);

        getContentPane().setLayout(new BorderLayout(5, 5));

        JPanel datos = new JPanel(new GridLayout(6, 2, 6, 6));
        datos.setBorder(BorderFactory.createTitledBorder("Agendar Cita"));

        datos.add(new JLabel(" Paciente:"));
        cboPaciente = new JComboBox<>();
        datos.add(cboPaciente);

        datos.add(new JLabel(" Médico:"));
        cboMedico = new JComboBox<>();
        datos.add(cboMedico);

        datos.add(new JLabel(" Especialidad:"));
        cboEspecialidad = new JComboBox<>();
        // Permite seleccionar una especialidad existente o escribir una nueva.
        cboEspecialidad.setEditable(true);
        cboEspecialidad.setToolTipText("Seleccione una especialidad o escriba una nueva");
        datos.add(cboEspecialidad);

        datos.add(new JLabel(" Fecha (AAAA-MM-DD):"));
        txtFecha = new JTextField("2026-10-01");
        datos.add(txtFecha);

        datos.add(new JLabel(" Hora:"));
        txtHora = new JTextField("09:00 AM");
        datos.add(txtHora);

        JButton btnAgendar = new JButton("Agendar Cita");
        btnAgendar.addActionListener(e -> agendar());
        datos.add(btnAgendar);
        datos.add(new JLabel(""));

        cboPaciente.addActionListener(e -> cargarMedicamentosPaciente());
        cboMedico.addActionListener(e -> sincronizarEspecialidadConMedico());

        JPanel medicamentos = new JPanel(new BorderLayout());
        medicamentos.setBorder(BorderFactory.createTitledBorder("Historial de medicamentos del paciente"));
        modelMedicamentos = new DefaultTableModel(
                new Object[]{"ID Receta", "Medicamento", "Indicaciones"}, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tablaMedicamentos = new JTable(modelMedicamentos);
        medicamentos.add(new JScrollPane(tablaMedicamentos), BorderLayout.CENTER);

        JPanel buscar = new JPanel(new BorderLayout());
        buscar.setBorder(BorderFactory.createTitledBorder("Buscar Cita por Paciente o Médico"));
        txtBuscar = new JTextField();
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                actualizarCitas(txtBuscar.getText().trim());
            }
        });
        buscar.add(txtBuscar, BorderLayout.CENTER);

        JPanel superior = new JPanel(new BorderLayout(5, 5));
        superior.add(datos, BorderLayout.NORTH);
        superior.add(medicamentos, BorderLayout.CENTER);
        superior.add(buscar, BorderLayout.SOUTH);
        getContentPane().add(superior, BorderLayout.NORTH);

        modelCitas = new DefaultTableModel(
                new Object[]{"ID", "Fecha", "Hora", "Paciente", "Médico", "Especialidad", "Estado"}, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tablaCitas = new JTable(modelCitas);
        getContentPane().add(new JScrollPane(tablaCitas), BorderLayout.CENTER);
    }

    public void actualizarCitas(String filtro) {
        cargarCombos();
        modelCitas.setRowCount(0);
        String f = filtro == null ? "" : filtro.toLowerCase();

        for (Cita c : DatosSistema.citas) {
            String p = c.getPaciente().getNombre();
            String m = c.getMedico().getNombre();
            String esp = c.getMedico().getEspecialidad();
            if (p.toLowerCase().contains(f) || m.toLowerCase().contains(f) || esp.toLowerCase().contains(f)) {
                modelCitas.addRow(new Object[]{
                        c.getId(), c.getFecha(), c.getHora(), p, m, esp, c.getEstado()
                });
            }
        }
        cargarMedicamentosPaciente();
    }

    private void cargarCombos() {
        Paciente seleccionadoPaciente = (Paciente) cboPaciente.getSelectedItem();
        Medico seleccionadoMedico = (Medico) cboMedico.getSelectedItem();

        cboPaciente.removeAllItems();
        for (Paciente p : DatosSistema.pacientes) cboPaciente.addItem(p);
        if (seleccionadoPaciente != null) seleccionarPacientePorId(seleccionadoPaciente.getId());

        cboMedico.removeAllItems();
        for (Medico m : DatosSistema.medicos) cboMedico.addItem(m);
        if (seleccionadoMedico != null) seleccionarMedicoPorId(seleccionadoMedico.getId());

        cboEspecialidad.removeAllItems();
        for (Especialidad e : DatosSistema.especialidades) cboEspecialidad.addItem(e.getNombre());
        sincronizarEspecialidadConMedico();
    }

    private void seleccionarPacientePorId(String id) {
        for (int i = 0; i < cboPaciente.getItemCount(); i++) {
            Paciente p = cboPaciente.getItemAt(i);
            if (p.getId().equals(id)) { cboPaciente.setSelectedIndex(i); break; }
        }
    }

    private void seleccionarMedicoPorId(String id) {
        for (int i = 0; i < cboMedico.getItemCount(); i++) {
            Medico m = cboMedico.getItemAt(i);
            if (m.getId().equals(id)) { cboMedico.setSelectedIndex(i); break; }
        }
    }

    private void sincronizarEspecialidadConMedico() {
        Medico medico = (Medico) cboMedico.getSelectedItem();
        if (medico == null) return;
        String esp = medico.getEspecialidad();
        for (int i = 0; i < cboEspecialidad.getItemCount(); i++) {
            if (cboEspecialidad.getItemAt(i).equalsIgnoreCase(esp)) {
                cboEspecialidad.setSelectedIndex(i);
                return;
            }
        }
        // Si la especialidad del médico todavía no existe en la lista,
        // se incorpora automáticamente para permitir cualquier especialidad.
        if (esp != null && !esp.trim().isEmpty()) {
            boolean existe = false;
            for (int i = 0; i < cboEspecialidad.getItemCount(); i++) {
                Object item = cboEspecialidad.getItemAt(i);
                if (item != null && item.toString().equalsIgnoreCase(esp.trim())) {
                    cboEspecialidad.setSelectedIndex(i);
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                cboEspecialidad.addItem(esp.trim());
                cboEspecialidad.setSelectedItem(esp.trim());
            }
        }
    }

    private void cargarMedicamentosPaciente() {
        if (modelMedicamentos == null) return;
        modelMedicamentos.setRowCount(0);
        Paciente paciente = (Paciente) cboPaciente.getSelectedItem();
        if (paciente == null) return;

        for (Receta r : DatosSistema.recetas) {
            if (r.getPaciente() != null && r.getPaciente().getId().equals(paciente.getId())) {
                modelMedicamentos.addRow(new Object[]{
                        r.getId(), r.getMedicamento(), r.getIndicaciones()
                });
            }
        }
    }

    private void agendar() {
        Paciente paciente = (Paciente) cboPaciente.getSelectedItem();
        Medico medico = (Medico) cboMedico.getSelectedItem();
        Object valorEspecialidad = cboEspecialidad.getEditor().getItem();
        String especialidad = valorEspecialidad == null
                ? ""
                : valorEspecialidad.toString().trim();

        if (paciente == null || medico == null || especialidad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione paciente y médico e ingrese una especialidad.");
            return;
        }

        // Si el usuario escribió una especialidad nueva, se registra en el
        // catálogo para que quede disponible en futuras citas.
        boolean especialidadExiste = false;
        for (Especialidad e : DatosSistema.especialidades) {
            if (e.getNombre().equalsIgnoreCase(especialidad)) {
                especialidadExiste = true;
                break;
            }
        }
        if (!especialidadExiste) {
            String idEspecialidad = "E" + String.format("%03d", DatosSistema.especialidades.size() + 1);
            DatosSistema.especialidades.add(
                    new Especialidad(idEspecialidad, especialidad, "Especialidad registrada desde Citas")
            );
            cboEspecialidad.addItem(especialidad);
        }

        String id = "C" + String.format("%03d", DatosSistema.citas.size() + 1);
        DatosSistema.citas.add(new Cita(
                id,
                txtFecha.getText().trim(),
                txtHora.getText().trim(),
                paciente,
                medico,
                "Confirmada"
        ));

        actualizarCitas("");
        FrmPrincipal.actualizarContadores();
        JOptionPane.showMessageDialog(this,
                "Cita agendada exitosamente.\nEspecialidad: " + especialidad);
    }
}
