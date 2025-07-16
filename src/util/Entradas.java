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

    public static double lerNumeroDouble(Scanner sc, String texto) {
        String entrada;

        while (true) {
            System.out.print(texto + ": ");
            entrada = sc.nextLine().trim();

            if (entrada.isEmpty()) {
                System.out.println("ERRO: A entrada não pode ser vazia. Tente novamente.");
                continue; 
            }

            if (entrada.matches("\\d*[.,]?\\d+")) {
                try {
                    String entradaNormalizada = entrada.replace(',', '.');
                    return Double.parseDouble(entradaNormalizada);

                } catch (NumberFormatException e) {
                    System.out.println("ERRO: O número digitado é inválido. Tente novamente.");
                }

            } else {
                System.out.println("ERRO: Este campo deve conter apenas números decimais (use . ou , para separar decimais). Tente novamente.");
            }
        }
    }

    public static String lerSenha(Scanner sc, String texto) {
        String entrada;

        while (true) {
            System.out.print(texto + ": ");
            entrada = sc.nextLine().trim();

            if (entrada.isEmpty()) {
                System.out.println("ERRO: A senha não pode ser vazia. Tente novamente.");
                continue;
            }

            if (entrada.length() < 6) {
                System.out.println("ERRO: A senha deve ter pelo menos 6 caracteres. Tente novamente.");
                continue;
            }

            if (entrada.trim().isEmpty()) {
                System.out.println("ERRO: A senha não pode conter apenas espaços. Tente novamente.");
                continue;
            }

            return entrada;
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

    public static void limparTela() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
