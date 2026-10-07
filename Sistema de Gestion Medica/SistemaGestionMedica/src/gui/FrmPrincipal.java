package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import datos.DatosSistema;

public class FrmPrincipal extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JDesktopPane desktopPane;
    private JPanel caratula;
    private JLabel lblReloj;

    private JMenuItem itemInicio;
    private JMenuItem itemPaciente;
    private JMenuItem itemMedico;
    private JMenuItem itemEspecialidad;
    private JMenuItem itemMedicamentos;
    private JMenuItem itemCita;
    private JMenuItem itemReceta;
    private JMenuItem itemHistorial;
    private JMenuItem itemReportes;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DatosSistema.cargarDatosIniciales();
                FrmPrincipal frame = new FrmPrincipal();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public FrmPrincipal() {
        setTitle("Sistema de Gestión Médica");
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1100, 700);

        crearMenu();

        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(contentPane);

        desktopPane = new JDesktopPane();
        desktopPane.setBackground(new Color(235, 243, 250));

        // La carátula NO se agrega al JDesktopPane.
        // Es un JPanel normal que ocupa toda la ventana.
        caratula = crearCaratula();
        contentPane.add(caratula, BorderLayout.CENTER);

        iniciarReloj();
    }

    private void crearMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnInicio = new JMenu("Inicio");
        menuBar.add(mnInicio);

        itemInicio = crearItem("Panel principal");
        mnInicio.add(itemInicio);

        JMenu mnMantenimiento = new JMenu("Mantenimiento");
        menuBar.add(mnMantenimiento);

        itemPaciente = crearItem("Pacientes");
        itemMedico = crearItem("Médicos");
        itemEspecialidad = crearItem("Especialidades");
        itemMedicamentos = crearItem("Medicamentos / Stock");

        mnMantenimiento.add(itemPaciente);
        mnMantenimiento.add(itemMedico);
        mnMantenimiento.add(itemEspecialidad);
        mnMantenimiento.add(itemMedicamentos);

        JMenu mnProcesos = new JMenu("Procesos");
        menuBar.add(mnProcesos);

        itemCita = crearItem("Citas");
        itemReceta = crearItem("Recetas");
        itemHistorial = crearItem("Historial Médico");

        mnProcesos.add(itemCita);
        mnProcesos.add(itemReceta);
        mnProcesos.add(itemHistorial);

        JMenu mnReportes = new JMenu("Consultas y reportes");
        menuBar.add(mnReportes);

        itemReportes = crearItem("Reportes y análisis");
        mnReportes.add(itemReportes);
    }

    private JMenuItem crearItem(String texto) {
        JMenuItem item = new JMenuItem(texto);
        item.addActionListener(this);
        return item;
    }

    private JPanel crearCaratula() {
        JPanel principal = new JPanel(new BorderLayout());

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(new Color(20, 72, 108));
        encabezado.setBorder(new EmptyBorder(16, 30, 16, 30));

        JLabel titulo = new JLabel("SISTEMA DE GESTIÓN MÉDICA");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        encabezado.add(titulo, BorderLayout.WEST);

        lblReloj = new JLabel();
        lblReloj.setForeground(new Color(225, 240, 250));
        lblReloj.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        encabezado.add(lblReloj, BorderLayout.EAST);

        principal.add(encabezado, BorderLayout.NORTH);

        JPanel centro = new FondoCaratula();
        centro.setBorder(new EmptyBorder(35, 55, 35, 55));
        centro.setLayout(new GridLayout(1, 2, 45, 0));

        JPanel izquierda = new JPanel();
        izquierda.setOpaque(false);
        izquierda.setLayout(new BoxLayout(izquierda, BoxLayout.Y_AXIS));

        JLabel icono = new JLabel("✚");
        icono.setForeground(Color.WHITE);
        icono.setFont(new Font("Segoe UI", Font.BOLD, 75));
        icono.setAlignmentX(0.5f);
        izquierda.add(Box.createVerticalGlue());
        izquierda.add(icono);

        JLabel bienvenida = new JLabel("Bienvenido");
        bienvenida.setForeground(Color.WHITE);
        bienvenida.setFont(new Font("Segoe UI", Font.BOLD, 38));
        bienvenida.setAlignmentX(0.5f);
        izquierda.add(bienvenida);

        JLabel descripcion = new JLabel(
                "<html><center>Administre pacientes, médicos,<br>"
                + "citas, recetas e historiales médicos.</center></html>");
        descripcion.setForeground(new Color(225, 240, 248));
        descripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descripcion.setAlignmentX(0.5f);
        izquierda.add(Box.createVerticalStrut(12));
        izquierda.add(descripcion);

        JButton comenzar = new JButton("COMENZAR");
        comenzar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        comenzar.setForeground(new Color(20, 72, 108));
        comenzar.setBackground(Color.WHITE);
        comenzar.setFocusPainted(false);
        comenzar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        comenzar.setBorder(BorderFactory.createEmptyBorder(12, 35, 12, 35));
        comenzar.setAlignmentX(0.5f);
        comenzar.addActionListener(e -> abrirPaciente());

        izquierda.add(Box.createVerticalStrut(25));
        izquierda.add(comenzar);
        izquierda.add(Box.createVerticalGlue());

        centro.add(izquierda);

        JPanel derecha = new JPanel(new BorderLayout(0, 15));
        derecha.setOpaque(false);

        JLabel tituloModulos = new JLabel("MÓDULOS DEL SISTEMA");
        tituloModulos.setForeground(Color.WHITE);
        tituloModulos.setFont(new Font("Segoe UI", Font.BOLD, 20));
        derecha.add(tituloModulos, BorderLayout.NORTH);

        JPanel modulos = new JPanel(new GridLayout(3, 2, 14, 14));
        modulos.setOpaque(false);

        modulos.add(crearModulo("PACIENTES", "Registro y consulta", () -> abrirPaciente()));
        modulos.add(crearModulo("MÉDICOS", "Gestión de profesionales", () -> abrirMedico()));
        modulos.add(crearModulo("ESPECIALIDADES", "Áreas médicas", () -> abrirEspecialidad()));
        modulos.add(crearModulo("CITAS", "Citas médicas", () -> abrirCita()));
        modulos.add(crearModulo("RECETAS", "Tratamientos", () -> abrirReceta()));
        modulos.add(crearModulo("HISTORIAL", "Información médica", () -> abrirHistorial()));

        derecha.add(modulos, BorderLayout.CENTER);
        centro.add(derecha);

        principal.add(centro, BorderLayout.CENTER);

        JPanel pie = new JPanel(new BorderLayout());
        pie.setBackground(new Color(225, 234, 242));
        JLabel textoPie = new JLabel(
                "Sistema de Gestión de Establecimientos Médicos",
                SwingConstants.CENTER);
        textoPie.setForeground(new Color(90, 100, 110));
        textoPie.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pie.add(textoPie, BorderLayout.CENTER);
        principal.add(pie, BorderLayout.SOUTH);

        return principal;
    }

    private JButton crearModulo(String titulo, String descripcion, Runnable accion) {
        JButton boton = new JButton();
        boton.setLayout(new BorderLayout(10, 0));
        boton.setBackground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 230)),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(new Color(20, 95, 135));
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel lblDescripcion = new JLabel(descripcion);
        lblDescripcion.setForeground(new Color(100, 110, 120));
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        textos.add(lblTitulo);
        textos.add(Box.createVerticalStrut(5));
        textos.add(lblDescripcion);

        boton.add(textos, BorderLayout.CENTER);

        JLabel flecha = new JLabel(">", SwingConstants.CENTER);
        flecha.setForeground(new Color(20, 110, 145));
        flecha.setFont(new Font("Segoe UI", Font.BOLD, 25));
        boton.add(flecha, BorderLayout.EAST);

        boton.addActionListener(e -> accion.run());

        return boton;
    }

    private void iniciarReloj() {
        Timer timer = new Timer(1000, e -> {
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formato =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy   HH:mm:ss");
            lblReloj.setText(ahora.format(formato));
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    // =========================================================
    // MOSTRAR CARÁTULA
    // =========================================================

    private void mostrarCaratula() {
        cerrarVentanas();
        contentPane.removeAll();
        contentPane.add(caratula, BorderLayout.CENTER);
        contentPane.revalidate();
        contentPane.repaint();
    }

    // =========================================================
    // ABRIR FORMULARIO
    // =========================================================

    private void abrirVentana(JInternalFrame formulario) {
        cerrarVentanas();

        contentPane.removeAll();
        contentPane.add(desktopPane, BorderLayout.CENTER);

        desktopPane.add(formulario);
        formulario.setVisible(true);

        int x = Math.max(10, (desktopPane.getWidth() - formulario.getWidth()) / 2);
        int y = Math.max(10, (desktopPane.getHeight() - formulario.getHeight()) / 2);
        formulario.setLocation(x, y);

        contentPane.revalidate();
        contentPane.repaint();

        try {
            formulario.setSelected(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cerrarVentanas() {
        JInternalFrame[] ventanas = desktopPane.getAllFrames();
        for (JInternalFrame ventana : ventanas) {
            try {
                ventana.dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        desktopPane.removeAll();
        desktopPane.revalidate();
        desktopPane.repaint();
    }

    private void abrirPaciente() {
        FormPaciente formulario = new FormPaciente();
        formulario.buscar("");
        abrirVentana(formulario);
    }

    private void abrirMedico() {
        FormMedico formulario = new FormMedico();
        formulario.buscar("");
        abrirVentana(formulario);
    }

    private void abrirEspecialidad() {
        FormEspecialidad formulario = new FormEspecialidad();
        formulario.buscar("");
        abrirVentana(formulario);
    }

    private void abrirMedicamentos() {
        FormMedicamentos formulario = new FormMedicamentos();
        formulario.actualizar();
        abrirVentana(formulario);
    }

    private void abrirCita() {
        FormCita formulario = new FormCita();
        formulario.actualizarCitas("");
        abrirVentana(formulario);
    }

    private void abrirReceta() {
        FormReceta formulario = new FormReceta();
        formulario.actualizarRecetas("");
        abrirVentana(formulario);
    }

    private void abrirHistorial() {
        FormHistorial formulario = new FormHistorial();
        formulario.actualizar();
        abrirVentana(formulario);
    }

    private void abrirReportes() {
        FormReportes formulario = new FormReportes();
        formulario.seleccionarPestana(0);
        abrirVentana(formulario);
    }

    public static void actualizarContadores() {
        // Método conservado para compatibilidad con los formularios.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == itemInicio) {
            mostrarCaratula();
        } else if (e.getSource() == itemPaciente) {
            abrirPaciente();
        } else if (e.getSource() == itemMedico) {
            abrirMedico();
        } else if (e.getSource() == itemEspecialidad) {
            abrirEspecialidad();
        } else if (e.getSource() == itemMedicamentos) {
            abrirMedicamentos();
        } else if (e.getSource() == itemCita) {
            abrirCita();
        } else if (e.getSource() == itemReceta) {
            abrirReceta();
        } else if (e.getSource() == itemHistorial) {
            abrirHistorial();
        } else if (e.getSource() == itemReportes) {
            abrirReportes();
        }
    }

    private static class FondoCaratula extends JPanel {
        private static final long serialVersionUID = 1L;

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            GradientPaint gradiente = new GradientPaint(
                    0, 0, new Color(18, 78, 116),
                    getWidth(), getHeight(), new Color(40, 150, 175));

            g2.setPaint(gradiente);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }
}
