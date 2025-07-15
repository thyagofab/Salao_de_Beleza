package view;

import java.util.Scanner;
import java.util.TreeMap;

import model.Procedimento;
import service.ProcedimentoService;
// import util.Entradas;

public class ProcedimentoView {
    private Scanner sc;
    private ProcedimentoService procedimentoService;
    
    public ProcedimentoView(Scanner sc, ProcedimentoService procedimentoService) {
        this.sc = sc;
        this.procedimentoService = procedimentoService;
    }

    public void menuProcedimento(){
        int opcao = 0;

        while (opcao != 6) {
            System.out.println("\n--- Menu Procedimentos ---");
            System.out.println("1. Adicionar Procedimento");
            System.out.println("2. Consultar Procedimento");
            System.out.println("3. Atualizar Procedimento");
            System.out.println("4. Deletar Procedimento");
            System.out.println("5. Listar Todos os Procedimentos");
            System.out.println("6. Voltar ao Menu Principal");
            
            opcao = sc.nextInt();
            sc.nextLine(); 

            switch (opcao) {
                case 1:
                    adicionarProcedimento(sc);
                    break;
                case 2:
                    buscarProcedimento(sc);
                    break;
                case 3:
                    atualizarProcedimento(sc);
                    break;
                case 4:
                    deletarProcedimento(sc);
                    break;
                case 5:
                    listarProcedimentos();
                    break;
                case 6:
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    public void adicionarProcedimento(Scanner sc) {
        String nome, descricao;
        double preco;

        System.out.println("Digite o nome do procedimento: ");
        nome = sc.nextLine();
        System.out.println("Digite a descrição do procedimento: ");
        descricao = sc.nextLine();
        System.out.println("Digite o preço do procedimento: ");
        preco = sc.nextDouble();

        Procedimento procedimento = new Procedimento(nome, descricao, preco);

        if (procedimentoService.criarProcedimento(procedimento)) {
            System.out.println("Procedimento adicionado com sucesso!");
        } else {
            System.out.println("Erro ao adicionar o procedimento.");
        }
    }

    public void buscarProcedimento(Scanner sc){
        int id;
        
        System.out.println("Digite o ID do procedimento que deseja consultar: ");
        id = sc.nextInt();

        Procedimento procedimento = procedimentoService.consultarProcedimento(id);

        if (procedimento != null) {
            System.out.println("Procedimento encontrado: ");
            System.out.println("Nome: " + procedimento.getNome());
            System.out.println("Descrição: " + procedimento.getDescricao());
            System.out.println("Preço: " + procedimento.getPreco());
        } else {
            System.out.println("Procedimento não encontrado com o ID: " + id);
        }
    }

    public void atualizarProcedimento(Scanner sc) {
        int id;
        String nome, descricao;
        double preco;

        System.out.println("Digite o ID do procedimento que deseja atualizar: ");
        id = sc.nextInt();
        sc.nextLine(); 

        Procedimento procedimento = procedimentoService.consultarProcedimento(id);

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

            if (procedimentoService.atualizarProcedimento(procedimento, id)) {
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

        Procedimento procedimento = procedimentoService.consultarProcedimento(id);

        if (procedimento != null) {
            if (procedimentoService.deletarProcedimento(id)) {
                System.out.println("Procedimento deletado com sucesso!");
            } else {
                System.out.println("Erro ao deletar o procedimento com ID: " + id);
            }

        } else {
            System.out.println("Procedimento não encontrado.");
        }
    }

    public void listarProcedimentos(){
        TreeMap <Integer, Procedimento> procedimentos = procedimentoService.listarProcedimentos();

        if (procedimentos.isEmpty()) {
            System.out.println("Nenhum procedimento cadastrado.");

        } else {
            System.out.println("Lista de procedimentos:");

            for (Integer id : procedimentos.keySet()) {
                Procedimento procedimento = procedimentos.get(id);
                System.out.println("ID: " + id);
                System.out.println(procedimento);
                System.out.println("---------------------------------");
            }
        }
    }
}
