package view;

import java.util.Scanner;

import service.ClienteService;
import service.CabeleireiroService;
import util.Entradas;

public class MenuPrincipalView {

    private Scanner scanner;
    private ClienteService clienteService;
    private CabeleireiroService cabeleireiroService;

    public MenuPrincipalView() {
        this.scanner = new Scanner(System.in);
        this.clienteService = new ClienteService();
        this.cabeleireiroService = new CabeleireiroService();
    }

    public void exibirMenuPrincipal() {
        int opcao = 0;
        while (opcao != 4) {
            System.out.println("=== DINASHOW SALÃO DE BELEZA ===");
            System.out.println("| [1] REALIZAR LOGIN           |");
            System.out.println("| [2] CADASTRAR CLIENTE        |");
            System.out.println("| [3] CADASTRAR CABELEIREIRO   |");
            System.out.println("| [4] SAIR DO SISTEMA          |");
            System.out.println("================================");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine(); 
            switch (opcao) {
                case 1:
                    Entradas.limparTela();
                    UsuarioView usuarioView = new UsuarioView(scanner, new service.UsuarioService());
                    usuarioView.efetuarLogin();
                    break;
                case 2:
                    Entradas.limparTela();
                    ClienteView clienteView = new ClienteView(clienteService);
                    clienteView.adicionarCliente();
                    clienteView.MenuClientes();
                    break;
                case 3:
                    Entradas.limparTela();
                    CabeleireiroView cabeleireiroView = new CabeleireiroView(scanner, cabeleireiroService);
                    cabeleireiroView.adicionarCabeleireiro();
                    cabeleireiroView.menuCabeleireiro();
                    break;
                case 4:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
        scanner.close();
    }
}
