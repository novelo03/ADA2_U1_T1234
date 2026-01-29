import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GenerarPDF {

    public void generarPDF(String archivoCSV, String archivoSalidaPDF) {
        PDDocument documento = null;
        
        try {
            documento = new PDDocument();
            PDPage pagina = new PDPage(PDRectangle.A4);
            documento.addPage(pagina);

            PDPageContentStream contenido = new PDPageContentStream(documento, pagina);

            // Configurar fuente y tamaño
            contenido.setFont(PDType1Font.HELVETICA_BOLD, 16);
            float yPosition = 750;

            // Título
            contenido.beginText();
            contenido.newLineAtOffset(50, yPosition);
            contenido.showText("Reporte de Calificaciones");
            contenido.endText();

            yPosition -= 30;

            // Encabezados
            contenido.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contenido.beginText();
            contenido.newLineAtOffset(50, yPosition);
            contenido.showText("Matricula");
            contenido.newLineAtOffset(80, 0);
            contenido.showText("Nombre");
            contenido.newLineAtOffset(200, 0);
            contenido.showText("Asignatura");
            contenido.newLineAtOffset(100, 0);
            contenido.showText("Calificacion");
            contenido.endText();

            yPosition -= 20;

            // Leer y escribir datos del CSV
            BufferedReader lector = new BufferedReader(new FileReader(archivoCSV));
            
            // Ignorar encabezado
            lector.readLine();
            
            String linea;
            contenido.setFont(PDType1Font.HELVETICA, 10);

            while ((linea = lector.readLine()) != null) {
                // Usar -1 para incluir elementos vacíos al final
                String[] datos = linea.split(",", -1);
                
                if (datos.length >= 3) {
                    String matricula = datos[0];
                    String nombre = datos[1];
                    String asignatura = datos[2];
                    String calificacionRaw = datos.length >= 4 ? datos[3] : "";
                    
                    // Si está vacío, mostrar "S/C" (Sin Calificación)
                    String calificacion = calificacionRaw.trim().isEmpty() ? "S/C" : calificacionRaw;

                    contenido.beginText();
                    contenido.newLineAtOffset(50, yPosition);
                    contenido.showText(matricula);
                    contenido.newLineAtOffset(80, 0);
                    contenido.showText(nombre);
                    contenido.newLineAtOffset(200, 0);
                    contenido.showText(asignatura);
                    contenido.newLineAtOffset(100, 0);
                    contenido.showText(calificacion);
                    contenido.endText();

                    yPosition -= 20;

                    // Si la página se llena, crear una nueva
                    if (yPosition < 50) {
                        contenido.close();
                        pagina = new PDPage(PDRectangle.A4);
                        documento.addPage(pagina);
                        contenido = new PDPageContentStream(documento, pagina);
                        yPosition = 750;
                    }
                }
            }

            lector.close();
            contenido.close();

            // Guardar el documento
            documento.save(archivoSalidaPDF);
            System.out.println("PDF generado correctamente: " + archivoSalidaPDF);

        } catch (IOException e) {
            System.out.println("Error al generar PDF: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (documento != null) {
                try {
                    documento.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
