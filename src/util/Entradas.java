package util;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Entradas {

    public static String lerString(Scanner sc, String texto) {
        String entrada;

        while (true){
            System.out.print(texto + ": ");

            try {
                entrada = sc.nextLine().trim(); 

                if (entrada == null || entrada.isEmpty()) {
                    System.out.println("ERRO: Entrada não pode ser vazia. Tente novamente.");
                    continue;
                }
                if (entrada.matches("[a-zA-Z\\s]+")) {
                    return entrada; 
                    
                } else {
                    System.out.println("ERRO: Este campo deve conter apenas letras e espaços. Tente novamente.");
                }
                
            } catch (Exception e) {
                System.out.println("Erro ao ler entrada. Tente novamente.");
            }
        }
    }

    public static int lerNumero(Scanner sc, String texto) {
        String entrada;

        while (true) {
            System.out.print(texto + ": ");
            entrada = sc.nextLine().trim();

            if (entrada.isEmpty()) {
                System.out.println("ERRO: A entrada não pode ser vazia. Tente novamente.");
                continue; 
            }

            if (entrada.matches("\\d+")) {
                try {
                    return Integer.parseInt(entrada);

                } catch (NumberFormatException e) {
                    System.out.println("ERRO: O número digitado é muito grande. Tente novamente.");
                }

            } else {
                System.out.println("ERRO: Este campo deve conter apenas números inteiros. Tente novamente.");
            }
        }
    }

    public static boolean validarEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
