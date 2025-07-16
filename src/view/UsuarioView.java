package view;

import java.util.Scanner;

import service.UsuarioService;

public class UsuarioView {
    private Scanner sc;
    private UsuarioService usuarioService;

    public void consultarUsuario(){
        sc = new Scanner(System.in);
        usuarioService = new UsuarioService();

        System.out.println("Consultar Usuário");
        System.out.print("Digite o CPF do usuário: ");

        String cpf = sc.nextLine();

        if (usuarioService.verificarCPF(cpf)) {
            System.out.println("Usuário encontrado.");
        } else {
            System.out.println("Usuário não encontrado.");
        }

        System.out.print("Digite o email do usuário: ");
        String email = sc.nextLine();

        if (usuarioService.verificarEmail(email)) {
            System.out.println("Email encontrado.");
        } else {
            System.out.println("Email não encontrado.");
        }
        
        sc.close();
    }
}
