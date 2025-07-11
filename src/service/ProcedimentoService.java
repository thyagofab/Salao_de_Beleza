package service;

import java.util.Scanner;

import dao.ProcedimentoDAO;
import model.Procedimento;

public class ProcedimentoService {
    
    private ProcedimentoDAO procedimentoDAO = new ProcedimentoDAO();

    public void criarProcedimento(Scanner sc) {
        String nome, descricao;
        double preco;

        System.out.println("Digite o nome do procedimento: ");
        nome = sc.nextLine();
        System.out.println("Digite a descrição do procedimento: ");
        descricao = sc.nextLine();
        System.out.println("Digite o preço do procedimento: ");
        preco = sc.nextDouble();

        Procedimento procedimento = new Procedimento(nome, descricao, preco);

        procedimentoDAO.salvar(procedimento);
        System.out.println("Procedimento adicionado com sucesso!");
    }

    public void consultarProcedimento(Scanner sc){
        int id;
        
        System.out.println("Digite o ID do procedimento que deseja consultar: ");
        id = sc.nextInt();

        Procedimento procedimento = procedimentoDAO.buscarPorId(id);

        if (procedimento != null) {
            System.out.println("Procedimento encontrado: ");
            System.out.println("Nome: " + procedimento.getNome());
            System.out.println("Descrição: " + procedimento.getDescricao());
            System.out.println("Preço: " + procedimento.getPreco());
        } else {
            System.out.println("Procedimento não encontrado.");
        }
    }
}
