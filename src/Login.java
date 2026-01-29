import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;

public class Login extends Application {

    static String usuarioCSV;
    static String contrasenaCifradaCSV;

    // ===== CIFRADO CESAR +2 =====
    public static String cifrarCesar(String texto) {
        StringBuilder resultado = new StringBuilder();

        for (char c : texto.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char) ((c - base + 2) % 26 + base);
            } else if (Character.isDigit(c)) {
                c = (char) ((c - '0' + 2) % 10 + '0');
            }
            resultado.append(c);
        }
        return resultado.toString();
    }

    // ===== LECTURA CSV =====
    public static void cargarCredenciales(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {

            br.readLine(); // encabezado
            String linea = br.readLine();
            String[] datos = linea.split(",");

            usuarioCSV = datos[0].trim();
            contrasenaCifradaCSV = datos[1].trim();

        } catch (Exception e) {
            System.out.println("Error al leer CSV");
        }
    }

    @Override
    public void start(Stage stage) {

        cargarCredenciales("./claves.csv");

        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Usuario");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Contraseña");

        Label mensaje = new Label();

        Button btnLogin = new Button("Iniciar sesión");

        btnLogin.setOnAction(e -> {
            String usuarioIngresado = txtUsuario.getText();
            String passwordIngresado = txtPassword.getText();
            String cifrada = cifrarCesar(passwordIngresado);

            if (usuarioIngresado.equals(usuarioCSV) && cifrada.equals(contrasenaCifradaCSV)) {
                CSV csv = new CSV();
                csv.abrirVentanaPrincipal();
                stage.close();

            } else {
                mensaje.setText("Usuario o contraseña incorrectos");
            }
        });

        VBox layout = new VBox(10, txtUsuario, txtPassword, btnLogin, mensaje);
        layout.setPadding(new Insets(20));

        stage.setTitle("Login");
        stage.setScene(new Scene(layout, 300, 220));
        stage.show();
    }
}
