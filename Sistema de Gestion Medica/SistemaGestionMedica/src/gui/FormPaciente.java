package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import datos.DatosSistema;
import entidad.Paciente;

public class FormPaciente extends JInternalFrame {
    private JTextField txtDni,txtNombre,txtTelefono,txtBuscar; private DefaultTableModel model;
    public FormPaciente(){
        setTitle("Mantenimiento de Paciente");setClosable(true);setMaximizable(true);setIconifiable(true);setBounds(30,30,600,420);getContentPane().setLayout(new BorderLayout(5,5));
        JPanel f=new JPanel(new GridLayout(4,2,5,5));f.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Paciente"));
        f.add(new JLabel(" DNI:"));txtDni=new JTextField();f.add(txtDni);f.add(new JLabel(" Nombre Completo:"));txtNombre=new JTextField();f.add(txtNombre);f.add(new JLabel(" Teléfono:"));txtTelefono=new JTextField();f.add(txtTelefono);JButton b=new JButton("Guardar Paciente");b.addActionListener(e->guardar());f.add(b);
        JPanel search=new JPanel(new BorderLayout(5,5));search.setBorder(BorderFactory.createTitledBorder("Buscar Paciente (por DNI o Nombre)"));txtBuscar=new JTextField();txtBuscar.addKeyListener(new KeyAdapter(){public void keyReleased(KeyEvent e){buscar(txtBuscar.getText().trim());}});search.add(txtBuscar);
        JPanel north=new JPanel(new BorderLayout());north.add(f,BorderLayout.CENTER);north.add(search,BorderLayout.SOUTH);getContentPane().add(north,BorderLayout.NORTH);model=new DefaultTableModel(new Object[]{"ID","DNI","Nombre","Teléfono"},0);getContentPane().add(new JScrollPane(new JTable(model)),BorderLayout.CENTER);
    }
    private void guardar(){if(!txtDni.getText().trim().isEmpty()&&!txtNombre.getText().trim().isEmpty()){String id="P"+String.format("%03d",DatosSistema.pacientes.size()+1);DatosSistema.pacientes.add(new Paciente(id,txtDni.getText().trim(),txtNombre.getText().trim(),txtTelefono.getText().trim()));buscar("");FrmPrincipal.actualizarContadores();txtDni.setText("");txtNombre.setText("");txtTelefono.setText("");JOptionPane.showMessageDialog(this,"Paciente registrado correctamente.");}else JOptionPane.showMessageDialog(this,"Complete los campos DNI y Nombre.");}
    public void buscar(String t){model.setRowCount(0);for(Paciente p:DatosSistema.pacientes)if(p.getDni().toLowerCase().contains(t.toLowerCase())||p.getNombre().toLowerCase().contains(t.toLowerCase()))model.addRow(new Object[]{p.getId(),p.getDni(),p.getNombre(),p.getTelefono()});}
}
