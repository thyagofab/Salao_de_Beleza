import java.util.Scanner;

import data.ConexaoDoBanco;
import service.ProcedimentoService;

public class Main {
    public static void main(String[] args) {

        try {
            ConexaoDoBanco.iniciarBanco();
            System.out.println("Banco de dados iniciado com sucesso.");

        } catch (Exception e) {
            System.err.println("Erro ao iniciar o banco de dados: " + e.getMessage());
        }

        // Iniciando os testes na main
        ProcedimentoService procedimentoService = new ProcedimentoService();
        Scanner scanner = new Scanner(System.in);
        int opcao = 0;

        while (opcao != 3) {
            System.out.println("1. Adicionar Procedimento");
            System.out.println("2. Consultar Procedimento");
            System.out.println("3. Sair");
            opcao = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcao) {
                case 1:
                    procedimentoService.criarProcedimento(scanner);
                    break;
                case 2:
                    procedimentoService.consultarProcedimento(scanner);
                    break;
                case 3:
                    System.out.println("Saindo...");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

        }

    }
}