package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import datos.DatosSistema;
import entidad.Medico;

public class FormMedico extends JInternalFrame {
	private JTextField txtCmp, txtNombre, txtEspecialidad, txtBuscar;
	private DefaultTableModel model;

	public FormMedico() {
		setTitle("Mantenimiento de Médico");
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		setBounds(50, 50, 600, 420);
		getContentPane().setLayout(new BorderLayout(5, 5));
		JPanel f = new JPanel(new GridLayout(4, 2, 5, 5));
		f.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Médico"));
		f.add(new JLabel(" CMP:"));
		txtCmp = new JTextField();
		f.add(txtCmp);
		f.add(new JLabel(" Nombre:"));
		txtNombre = new JTextField();
		f.add(txtNombre);
		f.add(new JLabel(" Especialidad:"));
		txtEspecialidad = new JTextField();
		f.add(txtEspecialidad);
		JButton b = new JButton("Guardar Médico");
		b.addActionListener(e -> guardar());
		f.add(b);
		JPanel s = new JPanel(new BorderLayout(5, 5));
		s.setBorder(BorderFactory.createTitledBorder("Buscar Médico (CMP, Nombre o Especialidad)"));
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
		model = new DefaultTableModel(new Object[] { "ID", "CMP", "Nombre", "Especialidad" }, 0);
		getContentPane().add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);
	}

	private void guardar() {
		if (!txtCmp.getText().trim().isEmpty() && !txtNombre.getText().trim().isEmpty()) {
			String id = "M" + String.format("%03d", DatosSistema.medicos.size() + 1);
			DatosSistema.medicos.add(new Medico(id, txtCmp.getText().trim(), txtNombre.getText().trim(),
					txtEspecialidad.getText().trim()));
			buscar("");
			FrmPrincipal.actualizarContadores();
			txtCmp.setText("");
			txtNombre.setText("");
			txtEspecialidad.setText("");
			JOptionPane.showMessageDialog(this, "Médico registrado correctamente.");
		} else
			JOptionPane.showMessageDialog(this, "Complete CMP y Nombre.");
	}

	public void buscar(String t) {
		model.setRowCount(0);
		for (Medico m : DatosSistema.medicos)
			if (m.getCmp().toLowerCase().contains(t.toLowerCase())
					|| m.getNombre().toLowerCase().contains(t.toLowerCase())
					|| m.getEspecialidad().toLowerCase().contains(t.toLowerCase()))
				model.addRow(new Object[] { m.getId(), m.getCmp(), m.getNombre(), m.getEspecialidad() });
	}
}
