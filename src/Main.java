public class Main {
    public static void main(String[] args) {
        CSV archivo = new CSV();

        Login.solicitarUsuario();
        Login.solicitarContraseña();

        archivo.generarCSV("Book 1(Sheet1).csv", "prueba");
    }
}