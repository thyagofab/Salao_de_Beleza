package view;

import java.util.Scanner;
import service.UsuarioService;
import util.Entradas;

public class UsuarioView {
    private Scanner scanner;
    private UsuarioService usuarioService;

    public UsuarioView(Scanner scanner, UsuarioService usuarioService) {
        this.scanner = scanner;
        this.usuarioService = usuarioService;
    }

    public void efetuarLogin() {
        System.out.println("==== LOGIN ====");
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        String tipo = usuarioService.autenticar(email, senha);
        if (tipo != null) {
            redirecionarParaMenu(email, tipo);
        } else {
            System.out.println("Credenciais inválidas. Tente novamente.");
        }
    }

   private void redirecionarParaMenu(String email, String tipo) {
    Entradas.limparTela();

    // Busca o ID do usuário que acabou de logar
    int idUsuarioLogado = new dao.UsuarioDAO().buscarIdPorEmail(email);
    if (idUsuarioLogado == -1) {
        System.out.println("Erro crítico: não foi possível encontrar o ID do usuário logado.");
        return;
    }

    if ("Cliente".equals(tipo)) {
        ClienteView cView = new ClienteView(new service.ClienteService());
        cView.MenuClientes(idUsuarioLogado); // Passa o ID do cliente logado
    } else if ("Cabeleireiro".equals(tipo)) {
        CabeleireiroView cabView = new CabeleireiroView(scanner, new service.CabeleireiroService(), new service.ProcedimentoService());
        cabView.MenuCabeleireiros(idUsuarioLogado); // Passa o ID do cabeleireiro logado
    } else {
        System.out.println("usuario não identificado.");
    }
}
}
