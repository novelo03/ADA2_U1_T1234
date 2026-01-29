import java.util.Objects;
import java.util.Scanner;

public class Login {
    private static final String USUARIO = "admin";
    private static final String CONTRASEÑA = "1234";

    static Scanner scanner = new Scanner(System.in);

    public static void solicitarUsuario(){
        System.out.print("Ingrese el usuario: ");
        while (!Objects.equals(scanner.nextLine(), USUARIO)){
            System.out.print("Usuario no registrado, intente de nuevo: ");
        }
    }

    public static void solicitarContraseña(){
        System.out.print("Ingrese la contraseña: ");
        while (!Objects.equals(scanner.nextLine(), CONTRASEÑA)) {
            System.out.print("Contraseña incorrecta, intente de nuevo: ");
        }
    }

}
