import javafx.application.Application;

/**
 * Reflexion T2: Tiempo invertido: 2 horas y media
 */

public class Main {
    public static void main(String[] args) {
        CSV archivo = new CSV();

        Login.cargarCredenciales("./claves.csv");

        Application.launch(Login.class, args);

        // Generar PDF con los datos del CSV
        GenerarPDF pdf = new GenerarPDF();
        pdf.generarPDF("./resultado.csv", "./calificaciones.pdf");
    }
}