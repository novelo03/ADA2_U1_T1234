import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Objects;
import java.util.Scanner;

public class Login {

    static Scanner scanner = new Scanner(System.in);

    static String usuarioCSV;
    static String contrasenaCifradaCSV;

    /**
     * Funcion para descifrar las contraseñas guardadas en el CSV: claves.
     * Las claves estan guardadas con el cifrado Cesar +2, por ejemplo, la contraseña 1234;
     * en el CSV estara guardada como: 3456
     *
     * @param texto: la contraseña que ingresa el usuario se le pasa como parametro
     * @return: devolvemos la contraseña ingresada con cifrado para comparar con la del CSV
     */
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

    // ===== LECTURA DEL CSV =====
    public static void cargarCredenciales(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {

            br.readLine(); // ignorar encabezados
            String linea = br.readLine();

            String[] datos = linea.split(",");

            usuarioCSV = datos[0];
            contrasenaCifradaCSV = datos[1];

        } catch (Exception e) {
            System.out.println("Error al leer el archivo CSV");
        }
    }

    // ===== VALIDAR USUARIO =====
    public static void solicitarUsuario() {
        System.out.print("Ingrese el usuario: ");
        while (!Objects.equals(scanner.nextLine(), usuarioCSV)) {
            System.out.print("Usuario no registrado, intente de nuevo: ");
        }
    }

    // ===== VALIDAR CONTRASENA =====
    public static void solicitarContraseña() {
        System.out.print("Ingrese la contraseña: ");
        while (true) {
            String entrada = scanner.nextLine();
            String entradaCifrada = cifrarCesar(entrada);

            if (entradaCifrada.equals(contrasenaCifradaCSV)) {
                break;
            }
            System.out.print("Contraseña incorrecta, intente de nuevo: ");
        }
    }
}
