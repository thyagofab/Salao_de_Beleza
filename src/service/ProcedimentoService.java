package service;

import java.util.Scanner;
import java.util.TreeMap;

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

    public void listarProcedimentos(){
        TreeMap <Integer, Procedimento> procedimentos = procedimentoDAO.listarTodos();

        if (procedimentos.isEmpty()) {
            System.out.println("Nenhum procedimento cadastrado.");

        } else {
            System.out.println("Lista de procedimentos:");

            for (Integer id : procedimentos.keySet()) {
                Procedimento procedimento = procedimentos.get(id);
                System.out.println("ID: " + id + ", Nome: " + procedimento.getNome() + ", Descrição: " + procedimento.getDescricao() + ", Preço: " + procedimento.getPreco());
            }
        }
    }

    public void atualizarProcedimento(Scanner sc) {
        int id;
        String nome, descricao;
        double preco;

        System.out.println("Digite o ID do procedimento que deseja atualizar: ");
        id = sc.nextInt();
        sc.nextLine(); 

        Procedimento procedimento = procedimentoDAO.buscarPorId(id);

        if (procedimento != null) {
           
            System.out.println("Digite o novo nome do procedimento: ");
            nome = sc.nextLine();
            System.out.println("Digite a nova descrição do procedimento: ");
            descricao = sc.nextLine();
            System.out.println("Digite o novo preço do procedimento: ");
            preco = sc.nextDouble();

            procedimento.setNome(nome);
            procedimento.setDescricao(descricao);
            procedimento.setPreco(preco);

            if (procedimentoDAO.atualizar(procedimento, id)) {
                System.out.println("Procedimento atualizado com sucesso!");
            } else {
                System.out.println("Erro ao atualizar o procedimento.");
            }

        } else {
            System.out.println("Procedimento não encontrado.");
        }
    }

    public void deletarProcedimento(Scanner sc){
        int id;

        System.out.println("Digite o ID do procedimento que deseja deletar: ");
        id = sc.nextInt();

        Procedimento procedimento = procedimentoDAO.buscarPorId(id);

        if (procedimento != null) {
            if (procedimentoDAO.deletar(id)) {
                System.out.println("Procedimento deletado com sucesso!");

            } else {
                System.out.println("Erro ao deletar o procedimento.");
            }
        } else {
            System.out.println("Procedimento não encontrado.");
        }
    }
}
