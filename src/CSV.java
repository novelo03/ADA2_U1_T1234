import javax.swing.*;
import java.io.*;
import java.util.Scanner;

public class CSV {

    public void generarCSV(String archivoEntrada, String archivoSalida) {

        Scanner scanner = new Scanner(System.in);

        BufferedReader lector = null;
        BufferedWriter escritor = null;

        try {
            System.out.print("Ingrese el nombre de la asignatura: ");
            String asignatura = scanner.nextLine();

            lector = new BufferedReader(new FileReader(archivoEntrada));
            escritor = new BufferedWriter(new FileWriter(archivoSalida));

            // Encabezados del nuevo CSV
            escritor.write("Matricula,Nombre,Asignatura,Calificacion");
            escritor.newLine();

            // Ignorar encabezado del CSV original
            lector.readLine();

            String linea;

            while ((linea = lector.readLine()) != null) {

                String[] partes = linea.split(",");

                String matricula = partes[0];
                String nombreCompleto = partes[1] + " " + partes[2] + " " + partes[3];

                System.out.print("Ingrese la calificacion (1-100) para " + nombreCompleto + ":");
                int calificacion = scanner.nextInt();

                escritor.write(matricula + "," + nombreCompleto + "," + asignatura + "," + calificacion);
                escritor.newLine();
            }

            JOptionPane.showMessageDialog(null, "Archivo CSV generado correctamente");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            try {
                if (lector != null) lector.close();
                if (escritor != null) escritor.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }
}
