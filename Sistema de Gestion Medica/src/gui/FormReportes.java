package gui;

import java.awt.*;
import java.awt.print.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import datos.DatosSistema;
import entidad.*;

public class FormReportes extends JInternalFrame {
    private static final long serialVersionUID = 1L;

    private JTabbedPane tabs;
    private DefaultTableModel modelPac, modelHist, modelCit, modelRec;

    public FormReportes() {
        setTitle("Módulo de Reportes de Análisis");
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        setBounds(60, 40, 850, 560);
        getContentPane().setLayout(new BorderLayout());

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnDescargar = new JButton("Descargar reporte");
        JButton btnImprimir = new JButton("Imprimir reporte");
        btnDescargar.addActionListener(e -> descargarReporte());
        btnImprimir.addActionListener(e -> imprimirReporte());
        barra.add(btnDescargar);
        barra.add(btnImprimir);
        getContentPane().add(barra, BorderLayout.NORTH);

        tabs = new JTabbedPane();

        modelPac = new DefaultTableModel(new Object[]{"ID Paciente", "DNI", "Nombre", "Teléfono"}, 0);
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(new JTable(modelPac)), BorderLayout.CENTER);
        tabs.addTab("Análisis de Pacientes", p);

        modelHist = new DefaultTableModel(new Object[]{"ID", "Paciente", "Diagnóstico", "Alergias", "Fecha"}, 0);
        JPanel h = new JPanel(new BorderLayout());
        h.add(new JScrollPane(new JTable(modelHist)), BorderLayout.CENTER);
        tabs.addTab("Análisis de Historial", h);

        modelCit = new DefaultTableModel(new Object[]{"ID Cita", "Fecha", "Hora", "Paciente", "Médico", "Especialidad", "Estado"}, 0);
        JPanel c = new JPanel(new BorderLayout());
        c.add(new JScrollPane(new JTable(modelCit)), BorderLayout.CENTER);
        tabs.addTab("Reporte de Citas", c);

        modelRec = new DefaultTableModel(new Object[]{"ID Receta", "Paciente", "Medicamento", "Indicaciones"}, 0);
        JPanel r = new JPanel(new BorderLayout());
        r.add(new JScrollPane(new JTable(modelRec)), BorderLayout.CENTER);
        tabs.addTab("Historial de Medicamentos", r);

        getContentPane().add(tabs, BorderLayout.CENTER);
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
    }

    private String construirReporte() {
        cargarTablas();
        int tab = tabs.getSelectedIndex();
        StringBuilder sb = new StringBuilder();
        sb.append("SISTEMA DE GESTIÓN MÉDICA\n");
        sb.append("REPORTE: ").append(tabs.getTitleAt(tab)).append("\n");
        sb.append("============================================================\n");

        DefaultTableModel model = switch (tab) {
            case 0 -> modelPac;
            case 1 -> modelHist;
            case 2 -> modelCit;
            default -> modelRec;
        };

        for (int c = 0; c < model.getColumnCount(); c++) {
            if (c > 0) sb.append(" | ");
            sb.append(model.getColumnName(c));
        }
        sb.append("\n");
        sb.append("------------------------------------------------------------\n");
        for (int r = 0; r < model.getRowCount(); r++) {
            for (int c = 0; c < model.getColumnCount(); c++) {
                if (c > 0) sb.append(" | ");
                sb.append(String.valueOf(model.getValueAt(r, c)));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void descargarReporte() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar reporte");
        chooser.setSelectedFile(new File("Reporte_" + tabs.getTitleAt(tabs.getSelectedIndex()).replace(' ', '_') + ".html"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File archivo = chooser.getSelectedFile();
        String reporte = construirReporte();
        StringBuilder html = new StringBuilder();
        html.append("<html><head><meta charset='UTF-8'><title>Reporte médico</title>")
            .append("<style>body{font-family:Arial}h1{color:#174b70}table{border-collapse:collapse;width:100%}th,td{border:1px solid #999;padding:6px}th{background:#dbeaf4}</style>")
            .append("</head><body><h1>Sistema de Gestión Médica</h1><h2>")
            .append(escapeHtml(tabs.getTitleAt(tabs.getSelectedIndex())))
            .append("</h2><table><tr>");

        DefaultTableModel model = switch (tabs.getSelectedIndex()) {
            case 0 -> modelPac;
            case 1 -> modelHist;
            case 2 -> modelCit;
            default -> modelRec;
        };
        for (int c = 0; c < model.getColumnCount(); c++) html.append("<th>").append(escapeHtml(model.getColumnName(c))).append("</th>");
        html.append("</tr>");
        for (int r = 0; r < model.getRowCount(); r++) {
            html.append("<tr>");
            for (int c = 0; c < model.getColumnCount(); c++) html.append("<td>").append(escapeHtml(String.valueOf(model.getValueAt(r,c)))).append("</td>");
            html.append("</tr>");
        }
        html.append("</table></body></html>");

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8)) {
            writer.write(html.toString());
            JOptionPane.showMessageDialog(this, "Reporte guardado en:\n" + archivo.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String escapeHtml(String texto) {
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    private void imprimirReporte() {
        String reporte = construirReporte();
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Reporte - Sistema de Gestión Médica");
        job.setPrintable(new Printable() {
            @Override public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                String[] lineas = reporte.split("\\R", -1);
                int lineasPorPagina = 45;
                int inicio = pageIndex * lineasPorPagina;
                if (inicio >= lineas.length) return NO_SUCH_PAGE;
                Graphics2D g2 = (Graphics2D) graphics;
                g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                g2.setFont(new Font("Monospaced", Font.PLAIN, 9));
                int y = 15;
                for (int i = inicio; i < Math.min(inicio + lineasPorPagina, lineas.length); i++) {
                    g2.drawString(lineas[i], 0, y);
                    y += 13;
                }
                return PAGE_EXISTS;
            }
        });

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "No se pudo imprimir: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
