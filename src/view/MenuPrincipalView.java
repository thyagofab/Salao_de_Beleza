package view;

import java.util.Scanner;

import service.ClienteService;
import service.ProcedimentoService;
import service.CabeleireiroService;

public class MenuPrincipalView {
    private Scanner scanner;
    private ClienteService clienteService;
    private ProcedimentoService procedimentoService;
    private CabeleireiroService cabeleireiroService;

    public MenuPrincipalView() {
        this.scanner = new Scanner(System.in);
        this.clienteService = new ClienteService();
        this.procedimentoService = new ProcedimentoService();
        this.cabeleireiroService = new CabeleireiroService();
    }

    public void exibirMenuPrincipal() {
        int opcao = 0;
        while (opcao != 5) {
            System.out.println("\n===== Sistema de Gestão do Salão de Beleza =====");
            System.out.println("1. Gerenciar Clientes");
            System.out.println("2. Gerenciar Cabeleireiros");
            System.out.println("3. Gerenciar Procedimentos");
            System.out.println("4. Gerenciar Agendamentos");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine(); 
            switch (opcao) {
                case 1:
                    ClienteView clienteView = new ClienteView(scanner, clienteService);
                    clienteView.MenuClientes();
                    break;
                case 2:
                    CabeleireiroView cabeleireiroView = new CabeleireiroView(scanner, cabeleireiroService);
                    cabeleireiroView.menuCabeleireiro();
                    break;
                case 3:
                    ProcedimentoView procedimentoView = new ProcedimentoView(scanner, procedimentoService);
                    procedimentoView.menuProcedimento();
                    break;
                case 4:
                    System.out.println("Módulo de Agendamentos ainda não implementado.");
                    break;
                case 5:
                    System.out.println("Encerrando o sistema...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
        scanner.close();
    }
}
