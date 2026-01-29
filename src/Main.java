/**
 * Reflexion T2: Tiempo invertido: 2 horas y media
 */

public class Main {
    public static void main(String[] args) {
        CSV archivo = new CSV();

        Login.cargarCredenciales("claves(Sheet1).csv");
        Login.solicitarUsuario();
        Login.solicitarContraseña();

        archivo.generarCSV("Book 1(Sheet1).csv", "prueba");
    }
}