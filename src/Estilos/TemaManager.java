package Estilos;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.github.lgooddatepicker.components.TimePickerSettings.TimeIncrement;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TemaManager {
    private static boolean esModoOscuro = false;

    public static void aplicarOscuro() {
        FlatDarkLaf.setup();
        configurarEstilosGlobales();
        esModoOscuro = true;
    }

    public static void aplicarClaro() {
        FlatLightLaf.setup();
        configurarEstilosGlobales();
        esModoOscuro = false;
    }
    
    public static void inicializarTema() {
        if (esModoOscuro) {
            aplicarOscuro();
        } else {
            aplicarClaro();
        }
    }

    // Método para alternar el tema con un solo clic
    public static void alternarTema(JFrame ventana) {
        if (esModoOscuro) {
            aplicarClaro();
        } else {
            aplicarOscuro();
        }
        // Esta línea actualiza visualmente la ventana y todos sus componentes al instante
        SwingUtilities.updateComponentTreeUI(ventana);
    }

    public static boolean esOscuro() {
        return esModoOscuro;
    }

    private static void configurarEstilosGlobales() {
        // Bordes redondeados
        UIManager.put("Button.arc", 12);        // Redondeo de las esquinas de los botones (12px).
        UIManager.put("Component.arc", 10);     // Redondeo genérico para la mayoría de componentes UI (10px).
        UIManager.put("CheckBox.arc", 8);       // Redondeo de la casilla de verificación de los CheckBox (8px).
        UIManager.put("ProgressBar.arc", 10);   // Redondeo del marco y la barra de progreso (10px).
        UIManager.put("TextComponent.arc", 10); // Redondeo de los campos de texto como JTextField o JTextArea (10px).

        // Tablas
        UIManager.put("Table.rowHeight", 28);            // Altura de cada fila de la tabla en píxeles (28px).
        UIManager.put("Table.showHorizontalLines", true);  // Muestra las líneas divisoras horizontales entre filas (true/false).
        UIManager.put("Table.showVerticalLines", false);  // Oculta las líneas divisoras verticales entre columnas (true/false).
        UIManager.put("TableHeader.height", 32);         // Altura de la cabecera (encabezado) de la tabla (32px).

        // Márgenes / padding interno de componentes
        UIManager.put("Button.margin", new java.awt.Insets(6, 14, 6, 14));        // Espaciado interno del botón: (Arriba, Izquierda, Abajo, Derecha) en px.
        UIManager.put("TextField.margin", new java.awt.Insets(6, 10, 6, 10));     // Espaciado interno para campos de texto simples.
        UIManager.put("PasswordField.margin", new java.awt.Insets(6, 10, 6, 10));  // Espaciado interno para campos de contraseña.
        UIManager.put("ComboBox.padding", new java.awt.Insets(4, 8, 4, 8));        // Padding interno para el menú desplegable (JComboBox).

        // Foco
        UIManager.put("Component.focusWidth", 1);          // Grosor en píxeles del contorno/anillo que indica el foco (1px).
        UIManager.put("Button.focusedBorderColor", null);  // Elimina el color del borde adicional al enfocar botones (evita doble borde).

        // Scrollbars más finas y modernas
        UIManager.put("ScrollBar.width", 12);     // Ancho de la barra de desplazamiento (12px).
        UIManager.put("ScrollBar.thumbArc", 999);  // Redondeo de las esquinas del "pico/controlador" que arrastras (999 = totalmente redondo).
        UIManager.put("ScrollBar.trackArc", 999);  // Redondeo de las esquinas del canal/pista sobre el que se desplaza la barra (999 = totalmente redondo).
        
        UIManager.put("PopupMenu.borderCornerRadius", 10);// Redondeo de los bordes emergentes del calendario (Popup)
    }
    
    public static DatePickerSettings obtenerDatePickerSettings() {
        DatePickerSettings dateSettings = new DatePickerSettings();

        // Tipografías
        dateSettings.setFontValidDate(new Font("Segoe UI", Font.PLAIN, 14));
        dateSettings.setFontCalendarDateLabels(new Font("Segoe UI", Font.PLAIN, 12));
        dateSettings.setFontMonthAndYearMenuLabels(new Font("Segoe UI", Font.BOLD, 13));
        dateSettings.setFontCalendarWeekdayLabels(new Font("Segoe UI", Font.BOLD, 11));

        if (esModoOscuro) {
            // --- PALETA OSCURA ---

            // TextField del picker
            dateSettings.setColor(DatePickerSettings.DateArea.TextFieldBackgroundValidDate, new Color(45, 45, 45));
            dateSettings.setColor(DatePickerSettings.DateArea.TextFieldBackgroundInvalidDate, new Color(110, 30, 30));
            dateSettings.setColor(DatePickerSettings.DateArea.TextFieldBackgroundVetoedDate, new Color(60, 60, 60));
            dateSettings.setColor(DatePickerSettings.DateArea.TextFieldBackgroundDisallowedEmptyDate, new Color(110, 30, 30));
            dateSettings.setColor(DatePickerSettings.DateArea.TextFieldBackgroundDisabled, new Color(35, 35, 35));
            //dateSettings.setColor(DatePickerSettings.DateArea.TextFieldBorder, new Color(70, 70, 70));
            dateSettings.setColor(DatePickerSettings.DateArea.DatePickerTextValidDate, new Color(220, 220, 220));
            dateSettings.setColor(DatePickerSettings.DateArea.DatePickerTextDisabled, new Color(120, 120, 120));

            // Panel del calendario
            dateSettings.setColor(DatePickerSettings.DateArea.BackgroundOverallCalendarPanel, new Color(30, 30, 30));
            dateSettings.setColor(DatePickerSettings.DateArea.CalendarBackgroundNormalDates, new Color(38, 38, 38));
            dateSettings.setColor(DatePickerSettings.DateArea.CalendarTextNormalDates, new Color(210, 210, 210));
            dateSettings.setColor(DatePickerSettings.DateArea.CalendarTextWeekdays, new Color(180, 180, 180));
            dateSettings.setColor(DatePickerSettings.DateArea.CalendarBackgroundVetoedDates, new Color(55, 55, 55));

            // Selección y hover
            dateSettings.setColor(DatePickerSettings.DateArea.CalendarBackgroundSelectedDate, new Color(13, 71, 161));
            dateSettings.setColor(DatePickerSettings.DateArea.CalendarBorderSelectedDate, new Color(13, 110, 253));
            dateSettings.setColor(DatePickerSettings.DateArea.BackgroundCalendarPanelLabelsOnHover, new Color(60, 60, 60));
            dateSettings.setColor(DatePickerSettings.DateArea.TextCalendarPanelLabelsOnHover, new Color(230, 230, 230));

            // Cabecera (mes/año) y botones de navegación
            dateSettings.setColor(DatePickerSettings.DateArea.BackgroundMonthAndYearMenuLabels, new Color(38, 38, 38));
            dateSettings.setColor(DatePickerSettings.DateArea.TextMonthAndYearMenuLabels, new Color(220, 220, 220));
            dateSettings.setColor(DatePickerSettings.DateArea.BackgroundMonthAndYearNavigationButtons, new Color(45, 45, 45));
            dateSettings.setColor(DatePickerSettings.DateArea.TextMonthAndYearNavigationButtons, new Color(220, 220, 220));

            dateSettings.setColorBackgroundWeekdayLabels(new Color(50, 50, 50), true);
        } else {
            // --- PALETA CLARA ---

            dateSettings.setColor(DatePickerSettings.DateArea.TextFieldBackgroundValidDate, Color.WHITE);
            dateSettings.setColor(DatePickerSettings.DateArea.TextFieldBackgroundInvalidDate, new Color(255, 220, 220));
            dateSettings.setColor(DatePickerSettings.DateArea.TextFieldBackgroundVetoedDate, new Color(240, 240, 240));
            dateSettings.setColor(DatePickerSettings.DateArea.TextFieldBackgroundDisallowedEmptyDate, new Color(255, 220, 220));
            dateSettings.setColor(DatePickerSettings.DateArea.TextFieldBackgroundDisabled, new Color(235, 235, 235));
            //dateSettings.setColor(DatePickerSettings.DateArea.TextFieldBorder, new Color(200, 200, 200));
            dateSettings.setColor(DatePickerSettings.DateArea.DatePickerTextValidDate, new Color(30, 30, 30));
            dateSettings.setColor(DatePickerSettings.DateArea.DatePickerTextDisabled, new Color(150, 150, 150));

            dateSettings.setColor(DatePickerSettings.DateArea.BackgroundOverallCalendarPanel, new Color(248, 249, 250));
            dateSettings.setColor(DatePickerSettings.DateArea.CalendarBackgroundNormalDates, Color.WHITE);
            dateSettings.setColor(DatePickerSettings.DateArea.CalendarTextNormalDates, new Color(50, 50, 50));
            dateSettings.setColor(DatePickerSettings.DateArea.CalendarTextWeekdays, new Color(90, 90, 90));
            dateSettings.setColor(DatePickerSettings.DateArea.CalendarBackgroundVetoedDates, new Color(230, 230, 230));

            dateSettings.setColor(DatePickerSettings.DateArea.CalendarBackgroundSelectedDate, new Color(190, 215, 255));
            dateSettings.setColor(DatePickerSettings.DateArea.CalendarBorderSelectedDate, new Color(0, 120, 215));
            dateSettings.setColor(DatePickerSettings.DateArea.BackgroundCalendarPanelLabelsOnHover, new Color(225, 235, 250));
            dateSettings.setColor(DatePickerSettings.DateArea.TextCalendarPanelLabelsOnHover, new Color(20, 20, 20));

            dateSettings.setColor(DatePickerSettings.DateArea.BackgroundMonthAndYearMenuLabels, Color.WHITE);
            dateSettings.setColor(DatePickerSettings.DateArea.TextMonthAndYearMenuLabels, new Color(30, 30, 30));
            dateSettings.setColor(DatePickerSettings.DateArea.BackgroundMonthAndYearNavigationButtons, new Color(245, 245, 245));
            dateSettings.setColor(DatePickerSettings.DateArea.TextMonthAndYearNavigationButtons, new Color(30, 30, 30));

            dateSettings.setColorBackgroundWeekdayLabels(new Color(240, 240, 240), true);
        }

        dateSettings.setVisibleClearButton(false);
        dateSettings.setVisibleTodayButton(false);
        return dateSettings;
    }

    public static TimePickerSettings obtenerTimePickerSettings() {
        TimePickerSettings timeSettings = new TimePickerSettings();
        timeSettings.generatePotentialMenuTimes(TimeIncrement.FiveMinutes, null, null);
        timeSettings.use24HourClockFormat();

        if (esModoOscuro) {
            timeSettings.setColor(TimePickerSettings.TimeArea.TextFieldBackgroundValidTime, new Color(45, 45, 45));
            timeSettings.setColor(TimePickerSettings.TimeArea.TextFieldBackgroundInvalidTime, new Color(110, 30, 30));
            timeSettings.setColor(TimePickerSettings.TimeArea.TextFieldBackgroundVetoedTime, new Color(60, 60, 60));
            timeSettings.setColor(TimePickerSettings.TimeArea.TextFieldBackgroundDisallowedEmptyTime, new Color(110, 30, 30));
            timeSettings.setColor(TimePickerSettings.TimeArea.TextFieldBackgroundDisabled, new Color(35, 35, 35));
            timeSettings.setColor(TimePickerSettings.TimeArea.TimePickerTextValidTime, new Color(220, 220, 220));
            timeSettings.setColor(TimePickerSettings.TimeArea.TimePickerTextDisabled, new Color(120, 120, 120));
        } else {
            timeSettings.setColor(TimePickerSettings.TimeArea.TextFieldBackgroundValidTime, Color.WHITE);
            timeSettings.setColor(TimePickerSettings.TimeArea.TextFieldBackgroundInvalidTime, new Color(255, 220, 220));
            timeSettings.setColor(TimePickerSettings.TimeArea.TextFieldBackgroundVetoedTime, new Color(240, 240, 240));
            timeSettings.setColor(TimePickerSettings.TimeArea.TextFieldBackgroundDisallowedEmptyTime, new Color(255, 220, 220));
            timeSettings.setColor(TimePickerSettings.TimeArea.TextFieldBackgroundDisabled, new Color(235, 235, 235));
            timeSettings.setColor(TimePickerSettings.TimeArea.TimePickerTextValidTime, new Color(30, 30, 30));
            timeSettings.setColor(TimePickerSettings.TimeArea.TimePickerTextDisabled, new Color(150, 150, 150));
        }

        return timeSettings;
    }
}
