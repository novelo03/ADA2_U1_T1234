import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class CSV {

    private TextField txtAsignatura;
    private TextField txtCalificacion;
    private Label lblAlumno;
    private Button btnSiguiente;

    private BufferedReader lector;
    private BufferedWriter escritor;

    private String lineaActual;

    public void abrirVentanaPrincipal() {
        Stage stage = new Stage();
        stage.setTitle("Captura de calificaciones");

        txtAsignatura = new TextField();
        txtAsignatura.setPromptText("Nombre de la asignatura");

        lblAlumno = new Label("Alumno:");

        txtCalificacion = new TextField();
        txtCalificacion.setPromptText("Calificación (1-100)");

        btnSiguiente = new Button("Guardar / Siguiente");

        btnSiguiente.setOnAction(e -> guardarCalificacion());

        VBox layout = new VBox(10,
                new Label("Asignatura"),
                txtAsignatura,
                lblAlumno,
                txtCalificacion,
                btnSiguiente
        );

        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 400, 250));
        stage.show();

        iniciarLectura();
    }

    private void iniciarLectura() {
        try {
            lector = new BufferedReader(new FileReader("./alumnos.csv"));
            escritor = new BufferedWriter(new FileWriter("./resultado.csv"));

            // Encabezado nuevo CSV
            escritor.write("Matricula,Nombre,Asignatura,Calificacion");
            escritor.newLine();

            // Saltar encabezado original
            lector.readLine();

            // Leer primer alumno
            lineaActual = lector.readLine();
            mostrarAlumnoActual();

        } catch (Exception e) {
            System.out.println("Error al iniciar lectura: " + e.getMessage());
        }
    }

    private void mostrarAlumnoActual() {
        if (lineaActual == null) {
            cerrarArchivos();
            lblAlumno.setText("Proceso terminado ✔");
            btnSiguiente.setDisable(true);
            return;
        }

        String[] partes = lineaActual.split(",");

        String nombreCompleto =
                partes[1] + " " +
                        partes[2] + " " +
                        partes[3];

        lblAlumno.setText("Alumno: " + nombreCompleto);
        txtCalificacion.clear();
    }

    private void guardarCalificacion() {
        try {
            int calificacion = Integer.parseInt(txtCalificacion.getText());

            if (calificacion < 1 || calificacion > 100) {
                mostrarError("La calificación debe estar entre 1 y 100");
                return;
            }

            String asignatura = txtAsignatura.getText();
            if (asignatura.isEmpty()) {
                mostrarError("Ingrese la asignatura");
                return;
            }

            String[] partes = lineaActual.split(",");

            String matricula = partes[0];
            String nombreCompleto =
                    partes[1] + " " +
                            partes[2] + " " +
                            partes[3];

            escritor.write(
                    matricula + "," +
                            nombreCompleto + "," +
                            asignatura + "," +
                            calificacion
            );
            escritor.newLine();

            // Leer siguiente alumno
            lineaActual = lector.readLine();
            mostrarAlumnoActual();

        } catch (NumberFormatException e) {
            mostrarError("Ingrese un número válido");
        } catch (Exception e) {
            mostrarError("Error al guardar");
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cerrarArchivos() {
        try {
            if (lector != null) lector.close();
            if (escritor != null) escritor.close();
        } catch (Exception e) {
            System.out.println("Error al cerrar archivos");
        }
    }
}
