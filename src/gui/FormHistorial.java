package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import datos.DatosSistema;
import entidad.*;

public class FormHistorial extends JInternalFrame {
	private JComboBox<Paciente> cboPaciente;
	private JTextField txtDiagnostico, txtAlergias, txtFecha, txtBuscar;
	private DefaultTableModel model;

	public FormHistorial() {
		setTitle("Historial Médico del Paciente");
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		setBounds(80, 50, 620, 440);
		getContentPane().setLayout(new BorderLayout(5, 5));
		JPanel f = new JPanel(new GridLayout(5, 2, 5, 5));
		f.setBorder(BorderFactory.createTitledBorder("Registrar / Actualizar Historial"));
		f.add(new JLabel(" Paciente:"));
		cboPaciente = new JComboBox<>();
		f.add(cboPaciente);
		f.add(new JLabel(" Diagnóstico Base:"));
		txtDiagnostico = new JTextField();
		f.add(txtDiagnostico);
		f.add(new JLabel(" Alergias:"));
		txtAlergias = new JTextField();
		f.add(txtAlergias);
		f.add(new JLabel(" Fecha Registro (AAAA-MM-DD):"));
		txtFecha = new JTextField("2026-09-26");
		f.add(txtFecha);
		JButton b = new JButton("Guardar Historial");
		b.addActionListener(e -> guardar());
		f.add(b);
		JPanel s = new JPanel(new BorderLayout());
		s.setBorder(BorderFactory.createTitledBorder("Consultar Historial por Paciente"));
		txtBuscar = new JTextField();
		txtBuscar.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				buscar(txtBuscar.getText().trim());
			}
		});
		s.add(txtBuscar);
		JPanel n = new JPanel(new BorderLayout());
		n.add(f, BorderLayout.CENTER);
		n.add(s, BorderLayout.SOUTH);
		getContentPane().add(n, BorderLayout.NORTH);
		model = new DefaultTableModel(new Object[] { "ID", "Paciente", "Diagnóstico", "Alergias", "Fecha" }, 0);
		getContentPane().add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);
	}

	public void actualizar() {
		cboPaciente.removeAllItems();
		for (Paciente p : DatosSistema.pacientes)
			cboPaciente.addItem(p);
		buscar("");
	}

	private void guardar() {
		if (cboPaciente.getSelectedItem() != null && !txtDiagnostico.getText().trim().isEmpty()) {
			String id = "H" + String.format("%03d", DatosSistema.historiales.size() + 1);
			DatosSistema.historiales.add(new HistorialMedico(id, (Paciente) cboPaciente.getSelectedItem(),
					txtDiagnostico.getText().trim(), txtAlergias.getText().trim(), txtFecha.getText().trim()));
			buscar("");
			txtDiagnostico.setText("");
			txtAlergias.setText("");
			JOptionPane.showMessageDialog(this, "Historial registrado correctamente.");
		} else
			JOptionPane.showMessageDialog(this, "Seleccione paciente e ingrese diagnóstico.");
	}

	public void buscar(String filtro) {
		model.setRowCount(0);
		for (HistorialMedico h : DatosSistema.historiales)
			if (h.getPaciente().getNombre().toLowerCase().contains(filtro.toLowerCase()))
				model.addRow(new Object[] { h.getId(), h.getPaciente().getNombre(), h.getDiagnostico(), h.getAlergias(),
						h.getFecha() });
	}
}
