package view;

import java.util.Scanner;

import service.ClienteService;

public class ClienteView {
    private Scanner scanner;
    private ClienteService clienteService;
    
    public ClienteView(Scanner scanner, ClienteService clienteService) {
        this.scanner = scanner;
        this.clienteService = clienteService;
    }

    public void MenuClientes() {
        int opcao = 0;

        while (opcao != 6) {
            System.out.println("\n--- Menu Clientes ---");
            System.out.println("1. Adicionar Cliente");
            System.out.println("2. Consultar Cliente");
            System.out.println("3. Atualizar Cliente");
            System.out.println("4. Deletar Cliente");
            System.out.println("5. Listar Todos os Clientes");
            System.out.println("6. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer do scanner

            switch (opcao) {
                case 1:
                    clienteService.criarCliente(scanner);
                    break;
                case 2:
                    clienteService.consultarCLiente(scanner);
                    break;
                case 3:
                    clienteService.atualizarCliente(scanner);
                    break;
                case 4:
                    clienteService.deletarCliente(scanner);
                    break;
                case 5:
                    clienteService.listarClientes();
                    break;
                case 6:
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}   
