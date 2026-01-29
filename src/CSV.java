import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
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
        
        // Verificar si es el último alumno
        try {
            lector.mark(1000); // Marcar posición actual
            String siguienteLinea = lector.readLine();
            lector.reset(); // Volver a la posición marcada
            
            if (siguienteLinea == null) {
                btnSiguiente.setText("Guardar CSV");
            } else {
                btnSiguiente.setText("Guardar / Siguiente");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void guardarCalificacion() {
        try {
            String calificacionTexto = txtCalificacion.getText().trim();
            String calificacion = "";

            // Si no está vacío, validar que sea un número entre 1-100
            if (!calificacionTexto.isEmpty()) {
                try {
                    int cal = Integer.parseInt(calificacionTexto);
                    if (cal < 1 || cal > 100) {
                        mostrarError("La calificación debe estar entre 1 y 100");
                        return;
                    }
                    calificacion = String.valueOf(cal);
                } catch (NumberFormatException e) {
                    mostrarError("Ingrese un número válido o déjelo en blanco");
                    return;
                }
            }
            // Si está vacío, se guarda como vacío (S/C en PDF)

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
            
            if (lineaActual == null) {
                escritor.close();
                lector.close();
                mostrarVentanaFinal();
            } else {
                mostrarAlumnoActual();
            }

        } catch (Exception e) {
            mostrarError("Error al guardar: " + e.getMessage());
        }
    }

    private void mostrarVentanaFinal() {
        Stage stageFinal = new Stage();
        stageFinal.setTitle("Operaciones finales");

        Label lbl = new Label("Captura de datos completada.\n¿Deseas generar PDF?");
        Button btnGenerarPDF = new Button("Generar PDF");
        Button btnCerrar = new Button("Cerrar");

        btnGenerarPDF.setStyle("-fx-font-size: 12px; -fx-padding: 10px;");
        btnCerrar.setStyle("-fx-font-size: 12px; -fx-padding: 10px;");

        Label lblEstado = new Label("");
        lblEstado.setStyle("-fx-text-fill: green;");

        btnGenerarPDF.setOnAction(e -> {
            GenerarPDF pdf = new GenerarPDF();
            pdf.generarPDF("./resultado.csv", "./calificaciones.pdf");
            
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Éxito");
            alerta.setContentText("PDF generado correctamente en: ./calificaciones.pdf");
            alerta.showAndWait();
            lblEstado.setText("✓ PDF generado");
        });

        btnCerrar.setOnAction(e -> stageFinal.close());

        VBox botonesLayout = new VBox(10, btnGenerarPDF, btnCerrar);
        botonesLayout.setStyle("-fx-alignment: center;");
        
        VBox layout = new VBox(15, lbl, botonesLayout, lblEstado);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        stageFinal.setScene(new Scene(layout, 350, 220));
        stageFinal.show();
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
