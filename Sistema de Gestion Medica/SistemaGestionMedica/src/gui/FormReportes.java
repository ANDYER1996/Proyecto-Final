package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import datos.DatosSistema;
import entidad.*;

public class FormReportes extends JInternalFrame {
    private static final long serialVersionUID = 1L;

    private JTabbedPane tabs;
    private JTextField txtBuscar;
    private JTable tablaPac, tablaHist, tablaCit, tablaRec, tablaMed;
    private DefaultTableModel modelPac, modelHist, modelCit, modelRec, modelMed;

    public FormReportes() {
        setTitle("Módulo de Reportes de Análisis");
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        setBounds(60, 40, 900, 590);
        getContentPane().setLayout(new BorderLayout(5, 5));

        JPanel barra = new JPanel(new BorderLayout(8, 5));
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JButton btnDescargar = new JButton("Descargar PDF");
        JButton btnImprimir = new JButton("Imprimir seleccionado");
        btnDescargar.addActionListener(e -> descargarPdfSeleccionado());
        btnImprimir.addActionListener(e -> imprimirSeleccionado());
        botones.add(btnDescargar);
        botones.add(btnImprimir);

        JPanel buscar = new JPanel(new BorderLayout(5, 0));
        buscar.add(new JLabel("Buscar:"), BorderLayout.WEST);
        txtBuscar = new JTextField();
        txtBuscar.setToolTipText("Buscar en el reporte seleccionado");
        buscar.add(txtBuscar, BorderLayout.CENTER);

        barra.add(botones, BorderLayout.WEST);
        barra.add(buscar, BorderLayout.CENTER);
        getContentPane().add(barra, BorderLayout.NORTH);

        tabs = new JTabbedPane();
        tabs.addChangeListener(e -> {
            txtBuscar.setText("");
            aplicarFiltro();
        });

        modelPac = modelo(new Object[]{"ID Paciente", "DNI", "Nombre", "Teléfono"});
        tablaPac = tabla(modelPac);
        tabs.addTab("Análisis de Pacientes", new JScrollPane(tablaPac));

        modelHist = modelo(new Object[]{"ID", "Paciente", "Diagnóstico", "Alergias", "Fecha"});
        tablaHist = tabla(modelHist);
        tabs.addTab("Análisis de Historial", new JScrollPane(tablaHist));

        modelCit = modelo(new Object[]{"ID Cita", "Fecha", "Hora", "Paciente", "Médico", "Especialidad", "Estado"});
        tablaCit = tabla(modelCit);
        tabs.addTab("Reporte de Citas", new JScrollPane(tablaCit));

        modelRec = modelo(new Object[]{"ID Receta", "Paciente", "Medicamento", "Indicaciones"});
        tablaRec = tabla(modelRec);
        tabs.addTab("Historial de Medicamentos", new JScrollPane(tablaRec));

        modelMed = modelo(new Object[]{"ID", "Medicamento", "Stock disponible", "Vencimiento"});
        tablaMed = tabla(modelMed);
        tabs.addTab("Stock de Medicamentos", new JScrollPane(tablaMed));

        getContentPane().add(tabs, BorderLayout.CENTER);

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            private void cambio() { aplicarFiltro(); }
            public void insertUpdate(DocumentEvent e) { cambio(); }
            public void removeUpdate(DocumentEvent e) { cambio(); }
            public void changedUpdate(DocumentEvent e) { cambio(); }
        });
    }

    private DefaultTableModel modelo(Object[] columnas) {
        return new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    private JTable tabla(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        t.setAutoCreateRowSorter(true);
        return t;
    }

    public void seleccionarPestana(int index) {
        cargarTablas();
        if (index >= 0 && index < tabs.getTabCount()) tabs.setSelectedIndex(index);
    }

    private void cargarTablas() {
        modelPac.setRowCount(0);
        for (Paciente p : DatosSistema.pacientes)
            modelPac.addRow(new Object[]{p.getId(), p.getDni(), p.getNombre(), p.getTelefono()});

        modelHist.setRowCount(0);
        for (HistorialMedico h : DatosSistema.historiales)
            modelHist.addRow(new Object[]{h.getId(), h.getPaciente().getNombre(), h.getDiagnostico(), h.getAlergias(), h.getFecha()});

        modelCit.setRowCount(0);
        for (Cita c : DatosSistema.citas)
            modelCit.addRow(new Object[]{c.getId(), c.getFecha(), c.getHora(), c.getPaciente().getNombre(), c.getMedico().getNombre(), c.getMedico().getEspecialidad(), c.getEstado()});

        modelRec.setRowCount(0);
        for (Receta r : DatosSistema.recetas)
            modelRec.addRow(new Object[]{r.getId(), r.getPaciente().getNombre(), r.getMedicamento(), r.getIndicaciones()});

        modelMed.setRowCount(0);
        for (Medicamento m : DatosSistema.medicamentos)
            modelMed.addRow(new Object[]{m.getId(), m.getNombre(), m.getStockDisponible(), m.getFechaVencimiento()});

        aplicarFiltro();
    }

    private JTable tablaActiva() {
        return switch (tabs.getSelectedIndex()) {
            case 0 -> tablaPac;
            case 1 -> tablaHist;
            case 2 -> tablaCit;
            case 3 -> tablaRec;
            default -> tablaMed;
        };
    }

    private DefaultTableModel modeloActivo() {
        return (DefaultTableModel) tablaActiva().getModel();
    }

    private void aplicarFiltro() {
        JTable tabla = tablaActiva();
        if (tabla == null) return;
        @SuppressWarnings("unchecked")
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tabla.getRowSorter();
        String texto = txtBuscar == null ? "" : txtBuscar.getText().trim();
        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
        }
    }

    private List<String[]> filasSeleccionadas() {
        JTable tabla = tablaActiva();
        DefaultTableModel model = modeloActivo();
        int[] filas = tabla.getSelectedRows();
        List<String[]> datos = new ArrayList<>();
        for (int viewRow : filas) {
            int modelRow = tabla.convertRowIndexToModel(viewRow);
            String[] fila = new String[model.getColumnCount()];
            for (int c = 0; c < model.getColumnCount(); c++) {
                fila[c] = String.valueOf(model.getValueAt(modelRow, c));
            }
            datos.add(fila);
        }
        return datos;
    }

    private String nombreReporteActual() {
        return tabs.getTitleAt(tabs.getSelectedIndex()).replace(' ', '_');
    }

    private void descargarPdfSeleccionado() {
        List<String[]> filas = filasSeleccionadas();
        if (filas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una o más filas de la tabla para generar el PDF.",
                    "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar reporte en PDF");
        chooser.setSelectedFile(new File("Reporte_" + nombreReporteActual() + ".pdf"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        try {
            File archivo = chooser.getSelectedFile();
            if (!archivo.getName().toLowerCase().endsWith(".pdf")) {
                archivo = new File(archivo.getAbsolutePath() + ".pdf");
            }
            crearPdf(archivo, tabs.getTitleAt(tabs.getSelectedIndex()), modeloActivo(), filas);
            JOptionPane.showMessageDialog(this, "PDF generado correctamente:\n" + archivo.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo generar el PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void imprimirSeleccionado() {
        List<String[]> filas = filasSeleccionadas();
        if (filas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una o más filas de la tabla para imprimir.",
                    "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String titulo = tabs.getTitleAt(tabs.getSelectedIndex());
        DefaultTableModel model = modeloActivo();
        String[] columnas = new String[model.getColumnCount()];
        for (int c = 0; c < columnas.length; c++) columnas[c] = model.getColumnName(c);

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Reporte - " + titulo);
        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            int porPagina = 38;
            int inicio = pageIndex * porPagina;
            if (inicio >= filas.size()) return Printable.NO_SUCH_PAGE;
            Graphics2D g = (Graphics2D) graphics;
            g.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            g.drawString("SISTEMA DE GESTIÓN MÉDICA", 0, 15);
            g.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g.drawString(titulo, 0, 32);
            int y = 50;
            g.setFont(new Font("Monospaced", Font.BOLD, 8));
            g.drawString(recortar(String.join(" | ", columnas), 115), 0, y);
            y += 14;
            g.setFont(new Font("Monospaced", Font.PLAIN, 8));
            for (int i = inicio; i < Math.min(inicio + porPagina, filas.size()); i++) {
                g.drawString(recortar(String.join(" | ", filas.get(i)), 115), 0, y);
                y += 13;
            }
            return Printable.PAGE_EXISTS;
        });

        if (job.printDialog()) {
            try { job.print(); }
            catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "No se pudo imprimir: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String recortar(String texto, int max) {
        return texto.length() <= max ? texto : texto.substring(0, max - 3) + "...";
    }

    // PDF simple y autónomo: no requiere librerías externas y genera solo las filas seleccionadas.
    private void crearPdf(File archivo, String titulo, DefaultTableModel model, List<String[]> filas) throws IOException {
        List<String> lineas = new ArrayList<>();
        lineas.add("SISTEMA DE GESTIÓN MÉDICA");
        lineas.add("REPORTE: " + titulo);
        lineas.add("");
        StringBuilder encabezado = new StringBuilder();
        for (int c = 0; c < model.getColumnCount(); c++) {
            if (c > 0) encabezado.append(" | ");
            encabezado.append(model.getColumnName(c));
        }
        lineas.add(recortar(encabezado.toString(), 115));
        lineas.add("---------------------------------------------------------------");
        for (String[] fila : filas) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < fila.length; c++) {
                if (c > 0) sb.append(" | ");
                sb.append(fila[c]);
            }
            lineas.add(recortar(sb.toString(), 115));
        }
        escribirPdf(archivo, lineas);
    }

    private void escribirPdf(File archivo, List<String> lineas) throws IOException {
        final Charset CP1252 = Charset.forName("windows-1252");
        List<byte[]> objetos = new ArrayList<>();
        objetos.add(null); // 0
        objetos.add(bytes("<< /Type /Catalog /Pages 2 0 R >>", CP1252)); // 1

        int porPagina = 42;
        int paginas = Math.max(1, (int) Math.ceil(lineas.size() / (double) porPagina));
        int pagesObj = 2;
        int fontObj = 3;
        List<Integer> pageObjs = new ArrayList<>();
        List<Integer> contentObjs = new ArrayList<>();
        int next = 4;
        for (int p = 0; p < paginas; p++) {
            contentObjs.add(next++);
            pageObjs.add(next++);
        }

        StringBuilder kids = new StringBuilder();
        for (int p = 0; p < paginas; p++) kids.append(5 + p * 2).append(" 0 R ");
        while (objetos.size() <= pagesObj) objetos.add(null);
        objetos.set(pagesObj - 1, null); // placeholder; objects are 1-indexed below
        // Rebuild objects in exact object-number order.
        List<byte[]> objs = new ArrayList<>();
        objs.add(null); // 0
        objs.add(bytes("<< /Type /Catalog /Pages " + pagesObj + " 0 R >>", CP1252)); // 1
        objs.add(bytes("<< /Type /Pages /Kids [ " + kids + "] /Count " + paginas + " >>", CP1252)); // 2
        objs.add(bytes("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica /Encoding /WinAnsiEncoding >>", CP1252)); // 3

        for (int p = 0; p < paginas; p++) {
            int start = p * porPagina;
            int end = Math.min(start + porPagina, lineas.size());
            StringBuilder stream = new StringBuilder();
            stream.append("BT /F1 9 Tf 45 760 Td 11 TL\n");
            for (int i = start; i < end; i++) {
                stream.append("(").append(pdfEscape(lineas.get(i))).append(") Tj T*\n");
            }
            stream.append("ET");
            byte[] data = stream.toString().getBytes(CP1252);
            objs.add(bytes("<< /Length " + data.length + " >>\nstream\n", CP1252));
            // Replace the just-added object with a full stream object marker; handled below.
            objs.set(objs.size()-1, concat(bytes("<< /Length " + data.length + " >>\nstream\n", CP1252), data, bytes("\nendstream", CP1252)));
            objs.add(bytes("<< /Type /Page /Parent " + pagesObj + " 0 R /MediaBox [0 0 612 792] /Resources << /Font << /F1 " + fontObj + " 0 R >> >> /Contents " + (contentObjs.get(p) + 0) + " 0 R >>", CP1252));
        }

        // The loop above appended content then page, but page object numbers need to match.
        // Build a clean final object list explicitly.
        List<byte[]> finalObjs = new ArrayList<>();
        finalObjs.add(null);
        finalObjs.add(bytes("<< /Type /Catalog /Pages 2 0 R >>", CP1252));
        finalObjs.add(bytes("<< /Type /Pages /Kids [ " + kids + "] /Count " + paginas + " >>", CP1252));
        finalObjs.add(bytes("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica /Encoding /WinAnsiEncoding >>", CP1252));
        for (int p = 0; p < paginas; p++) {
            int start = p * porPagina;
            int end = Math.min(start + porPagina, lineas.size());
            StringBuilder stream = new StringBuilder("BT /F1 9 Tf 45 760 Td 11 TL\n");
            for (int i = start; i < end; i++) stream.append("(").append(pdfEscape(lineas.get(i))).append(") Tj T*\n");
            stream.append("ET");
            byte[] data = stream.toString().getBytes(CP1252);
            int contentNumber = 4 + p * 2;
            int pageNumber = contentNumber + 1;
            // We need page object to reference content object and vice versa is not needed.
            finalObjs.add(bytes("<< /Length " + data.length + " >>\nstream\n" + stream + "\nendstream", CP1252));
            finalObjs.add(bytes("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Resources << /Font << /F1 3 0 R >> >> /Contents " + contentNumber + " 0 R >>", CP1252));
        }

        try (FileOutputStream out = new FileOutputStream(archivo)) {
            out.write("%PDF-1.4\n%\u00E2\u00E3\u00CF\u00D3\n".getBytes(CP1252));
            long[] offsets = new long[finalObjs.size()];
            for (int i = 1; i < finalObjs.size(); i++) {
                offsets[i] = out.getChannel().position();
                out.write((i + " 0 obj\n").getBytes(StandardCharsets.US_ASCII));
                out.write(finalObjs.get(i));
                out.write("\nendobj\n".getBytes(StandardCharsets.US_ASCII));
            }
            long xref = out.getChannel().position();
            out.write(("xref\n0 " + finalObjs.size() + "\n").getBytes(StandardCharsets.US_ASCII));
            out.write("0000000000 65535 f \n".getBytes(StandardCharsets.US_ASCII));
            for (int i = 1; i < finalObjs.size(); i++) out.write(String.format("%010d 00000 n \n", offsets[i]).getBytes(StandardCharsets.US_ASCII));
            out.write(("trailer\n<< /Size " + finalObjs.size() + " /Root 1 0 R >>\nstartxref\n" + xref + "\n%%EOF").getBytes(StandardCharsets.US_ASCII));
        }
    }

    private byte[] bytes(String s, Charset cs) { return s.getBytes(cs); }
    private byte[] concat(byte[]... parts) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        for (byte[] p : parts) b.write(p);
        return b.toByteArray();
    }
    private String pdfEscape(String s) {
        return s.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)")
                .replace("\r", " ").replace("\n", " ");
    }
}
