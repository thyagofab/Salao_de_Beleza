package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import service.UsuarioService;

public class Entradas {
    private static UsuarioService usuarioService = new UsuarioService();
    private static Scanner scanner = new Scanner(System.in);

    public static void fecharScanner(){
        scanner.close();
    }
    public static String lerString(String texto) {
        String entrada;

        while (true) {
            System.out.print(texto + ": ");

            try {
                entrada = scanner.nextLine().trim();

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

    public static int lerNumero(String texto) {
        String entrada;

        while (true) {
            System.out.print(texto + ": ");
            entrada = scanner.nextLine().trim();

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

    public static double lerNumeroDouble(String texto) {
        String entrada;

        while (true) {
            System.out.print(texto + ": ");
            entrada = scanner.nextLine().trim();

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
                System.out.println(
                        "ERRO: Este campo deve conter apenas números decimais (use . ou , para separar decimais). Tente novamente.");
            }
        }
    }

    public static String lerSenha(String texto) {
        String entrada;

        while (true) {
            System.out.print(texto + ": ");
            entrada = scanner.nextLine().trim();

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


    public static String validarCPF() {
        String cpf;
        boolean cpfValido;
         do{
             cpf = scanner.nextLine().trim();

            // Remover caracteres de pontuação do CPF, se houver
            cpf = cpf.replaceAll("[^0-9]", "");

            // Verificar se o CPF tem 11 dígitos
            if (cpf.length() != 11) {
                System.out.println("CPF inválido. Por favor, digite exatamente 11 números.");
                System.out.print("Digite o CPF (apenas números): ");
                cpfValido = false;
            }else if (usuarioService.verificarCPF(cpf)) {
                System.out.println("Este CPF já está cadastrado no sistema.");
                System.out.print("Digite o CPF (apenas números): ");
                cpfValido = false;

            }else if (!cpf.matches("[0-9]+")) {// Verificar se o CPF contém apenas dígitos
                System.out.println("CPF inválido. Por favor, digite apenas números.");
                System.out.print("Digite o CPF (apenas números): ");
                cpfValido = false;
            } else {
                cpfValido = true;
            }
        }while(!cpfValido);

        return cpf;
    }

    public static String validarNome() {
        String nome;
        boolean nomeValido;
        do {
            nome = scanner.nextLine().trim();

            if (nome.isEmpty()) {
                System.out.println("O nome não pode ser vazio. Por favor, tente novamente.");
                System.out.print("Digite o nome: ");
                nomeValido = false;

                // Verifica se o nome contém apenas letras e espaços
            }else if (!nome.matches("[a-zA-Z\\s]+")) {
                System.out.println("O nome deve conter apenas letras e espaços.");
                System.out.print("Digite o nome: ");
                nomeValido = false;
            }else{
                nomeValido = true;
            }
        }while(!nomeValido);
        return nome;
    }

    public static String validarTelefone() {
        String telefone;
        boolean telefoneValido;
        do{
            telefone = scanner.nextLine().trim();

            telefone = telefone.replaceAll("[^0-9]", "");

            if (telefone.length() == 11) {
                telefoneValido = true;
            } else {
                System.out.println("Telefone inválido. Deve conter 11 dígitos, incluindo o DDD.");
                System.out.print("Digite o telefone com DDD (10 ou 11 dígitos): ");
                telefoneValido = false;
            }
        }while(!telefoneValido);
        return telefone;
    }

    public static String validarEmail() {
        String email;
        boolean validarEmail;
        do{
            email = scanner.nextLine().trim();

            if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                System.out.println("Formato de e-mail inválido.");
                System.out.print("Digite o email: ");
                validarEmail = false;
            }else if (usuarioService.verificarEmail(email)) {
                System.out.println("Este e-mail já pertence a outro usuário.");
                System.out.print("Digite o email: ");
                validarEmail = false;
            }else{
                validarEmail = true;
            }

        }while(!validarEmail);
        
        return email;
    }



    public static String  validarSenha(){
        String senha;
        boolean validarSenha;

        do{
            senha = scanner.nextLine().trim();
            if(senha.length() < 6){
                System.out.println("A senha deve ter pelo menos 6 caracteres");
                System.out.print("Digite senha: ");
                validarSenha = false;
            }else if(senha.contains(" ")){
                System.out.println("A senha não pode conter espaço em branco");
                System.out.print("Digite senha: ");
                validarSenha = false;
            }else{
                validarSenha = true;
            }
        }while(!validarSenha);

        return senha;
    }


    public static String validarEndereco (){
        String endereco;
        boolean validarEndereco; 
        do{
           endereco =  scanner.nextLine();

           if(endereco.isEmpty()){
            System.out.println("O endereço não pode ser vazio.");
            System.out.print("Digite endereço: ");
            validarEndereco = false;
           }else{
            validarEndereco  = true;
           }
            
        }while(!validarEndereco);

        return endereco;
    }


    public static LocalDate validarDataDeNascimento(){
        LocalDate dataDeNascimento = null;
        String dataString;
        boolean validarData;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        do{
            dataString = scanner.nextLine();
            
            try{

                dataDeNascimento = LocalDate.parse(dataString,formatter);

                //não deixar colocar data no futuro ou atual
                if(dataDeNascimento.isAfter(LocalDate.now())){
                    System.out.println("A data de nascimento não pode ser uma data futura.");
                     System.out.print("Data de nascimento (dd/MM/yyyy): ");
                    validarData = false;
                }else{
                    validarData = true;
                }

            }catch(DateTimeParseException e){
                 System.out.println("Formato de data inválido. Use dd/MM/yyyy.");
                  System.out.print("Data de nascimento (dd/MM/yyyy): ");
                validarData = false;
            }


        }while(!validarData);

        return dataDeNascimento;

    }
    




=======
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
