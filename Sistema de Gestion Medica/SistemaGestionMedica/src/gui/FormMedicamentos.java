package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import datos.DatosSistema;
import entidad.Medicamento;

public class FormMedicamentos extends JInternalFrame {
    private static final long serialVersionUID = 1L;

    private JTextField txtNombre, txtStock, txtVencimiento, txtBuscar;
    private DefaultTableModel model;
    private JTable tabla;

    public FormMedicamentos() {
        setTitle("Mantenimiento: Stock de Medicamentos");
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        setBounds(70, 45, 760, 500);
        getContentPane().setLayout(new BorderLayout(5, 5));

        JPanel datos = new JPanel(new GridLayout(4, 2, 6, 6));
        datos.setBorder(BorderFactory.createTitledBorder("Registrar medicamento / stock hospitalario"));

        datos.add(new JLabel(" Medicamento:"));
        txtNombre = new JTextField();
        datos.add(txtNombre);

        datos.add(new JLabel(" Stock disponible:"));
        txtStock = new JTextField();
        datos.add(txtStock);

        datos.add(new JLabel(" Fecha vencimiento:"));
        txtVencimiento = new JTextField();
        datos.add(txtVencimiento);

        JButton btnRegistrar = new JButton("Registrar medicamento");
        btnRegistrar.addActionListener(e -> registrar());
        JButton btnActualizar = new JButton("Agregar / retirar stock");
        btnActualizar.addActionListener(e -> actualizarStock());
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        botones.add(btnRegistrar);
        botones.add(btnActualizar);
        datos.add(botones);
        datos.add(new JLabel(""));

        JPanel buscar = new JPanel(new BorderLayout(5, 5));
        buscar.setBorder(BorderFactory.createTitledBorder("Buscar medicamento"));
        txtBuscar = new JTextField();
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { cargarTabla(txtBuscar.getText().trim()); }
        });
        buscar.add(txtBuscar, BorderLayout.CENTER);

        JPanel superior = new JPanel(new BorderLayout(5, 5));
        superior.add(datos, BorderLayout.NORTH);
        superior.add(buscar, BorderLayout.SOUTH);
        getContentPane().add(superior, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"ID", "Medicamento", "Stock disponible", "Vencimiento"}, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(model);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        getContentPane().add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    public void actualizar() { cargarTabla(""); }

    private void cargarTabla(String filtro) {
        model.setRowCount(0);
        String f = filtro == null ? "" : filtro.toLowerCase();
        for (Medicamento m : DatosSistema.medicamentos) {
            if (m.getId().toLowerCase().contains(f) || m.getNombre().toLowerCase().contains(f)) {
                model.addRow(new Object[]{m.getId(), m.getNombre(), m.getStockDisponible(), m.getFechaVencimiento()});
            }
        }
    }

    private void registrar() {
        String nombre = txtNombre.getText().trim();
        String venc = txtVencimiento.getText().trim();
        if (nombre.isEmpty() || venc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese medicamento y fecha de vencimiento.");
            return;
        }
        int stock;
        try { stock = Integer.parseInt(txtStock.getText().trim()); }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El stock debe ser un número entero.");
            return;
        }
        if (stock < 0) {
            JOptionPane.showMessageDialog(this, "El stock no puede ser negativo.");
            return;
        }
        String id = "MED" + String.format("%03d", DatosSistema.medicamentos.size() + 1);
        DatosSistema.medicamentos.add(new Medicamento(id, nombre, stock, venc));
        cargarTabla("");
        limpiar();
        FrmPrincipal.actualizarContadores();
        JOptionPane.showMessageDialog(this, "Medicamento registrado correctamente.");
    }

    private void actualizarStock() {
        int fila = -1;
        fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un medicamento en la tabla.");
            return;
        }
        String id = String.valueOf(model.getValueAt(fila, 0));
        String entrada = JOptionPane.showInputDialog(this, "Cantidad a agregar (+) o retirar (-):", "0");
        if (entrada == null) return;
        try {
            int cantidad = Integer.parseInt(entrada.trim());
            for (Medicamento m : DatosSistema.medicamentos) {
                if (m.getId().equals(id)) {
                    if (m.getStockDisponible() + cantidad < 0) {
                        JOptionPane.showMessageDialog(this, "El stock no puede quedar negativo.");
                        return;
                    }
                    m.actualizarStock(cantidad);
                    break;
                }
            }
            cargarTabla(txtBuscar.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un número entero válido.");
        }
    }

    private void limpiar() {
        txtNombre.setText("");
        txtStock.setText("");
        txtVencimiento.setText("");
    }
}
