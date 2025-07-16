package view;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import model.Cabeleireiro;
import service.CabeleireiroService;
import util.Entradas;
import service.ProcedimentoService;

public class CabeleireiroView {
    private Scanner sc;
    private CabeleireiroService cabeleireiroService;
    private ProcedimentoService procedimentoService;

    public CabeleireiroView(Scanner sc, CabeleireiroService cabeleireiroService) {
        this.sc = sc;
        this.cabeleireiroService = cabeleireiroService;
    }

    public void menuCabeleireiro() {
        int opcao = 0;
        procedimentoService = new ProcedimentoService();

        while (opcao != 7) {
            System.out.println("======== MENU DE CABELEIREIROS ========");
            System.out.println("| [1] CONSULTAR CABELEIREIRO          |");
            System.out.println("| [2] ATUALIZAR CABELEIREIRO          |");
            System.out.println("| [3] DELETAR CABELEIREIRO            |");
            System.out.println("| [4] LISTAR CABELEIREIROS            |");
            System.out.println("| [5] GERENCIAR PROCEDIMENTOS         |");
            System.out.println("| [6] GERENCIAR AGENDAMENTOS          |");
            System.out.println("| [7] VOLTAR AO MENU PRINCIPAL        |");
            System.out.println("=======================================");
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1: 
                    consultarCabeleireiro(); 
                    break;
                case 2: 
                    atualizarCabeleireiro(); 
                    break;
                case 3: 
                    deletarCabeleireiro(); 
                    break;
                case 4: 
                    listarCabeleireiros(); 
                    break;
                case 5: 
                    Entradas.limparTela();
                    ProcedimentoView procedimentoView = new ProcedimentoView(sc, procedimentoService);
                    procedimentoView.menuProcedimento();
                    break;
                case 6:
                    System.out.println("Gerenciar agendamentos não implementado ainda.");
                    break;
                case 7:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default: 
                    System.out.println("Opção inválida.");
            }
        }
    }

    public void adicionarCabeleireiro() {
        try {
            System.out.print("ID do usuário (já cadastrado na tabela usuarios): ");
            int idUsuario = sc.nextInt(); sc.nextLine();

            System.out.print("Especialidades (separadas por vírgula): ");
            List<String> especialidades = Arrays.asList(sc.nextLine().split(","));

            System.out.print("Média de avaliações: ");
            double media = sc.nextDouble(); sc.nextLine();

            System.out.print("Total de avaliações: ");
            int total = sc.nextInt(); sc.nextLine();

            System.out.print("Dias disponíveis (ex: SEGUNDA,TERÇA): ");
            List<String> dias = Arrays.asList(sc.nextLine().split(","));

            System.out.print("Horários disponíveis (ex: 09:00,14:00): ");
            String[] horariosStr = sc.nextLine().split(",");
            List<LocalTime> horarios = new ArrayList<>();
            for (String h : horariosStr) {
                horarios.add(LocalTime.parse(h.trim()));
            }

            System.out.print("Tempo de experiência: ");
            String tempo = sc.nextLine();

            Cabeleireiro cab = new Cabeleireiro(
                idUsuario, "", "", "", "", "", 
                especialidades, media, total, dias, horarios, tempo
            );

            if (cabeleireiroService.criarCabeleireiro(cab)) {
                System.out.println("Cabeleireiro adicionado com sucesso!");
            } else {
                System.out.println("Erro ao adicionar o cabeleireiro.");
            }
        } catch (Exception e) {
            System.out.println("Erro de entrada: " + e.getMessage());
        }
    }

    public void consultarCabeleireiro() {
        System.out.print("Digite o ID do cabeleireiro que deseja consultar: ");
        int id = sc.nextInt(); sc.nextLine();

        Cabeleireiro cab = cabeleireiroService.buscarCabeleireiro(id);

        if (cab != null) {
            System.out.println("Cabeleireiro encontrado:");
            System.out.println(cab);
        } else {
            System.out.println("Cabeleireiro não encontrado.");
        }
    }

    public void atualizarCabeleireiro() {
        System.out.print("Digite o ID do cabeleireiro que deseja atualizar: ");
        int id = sc.nextInt(); sc.nextLine();

        Cabeleireiro existente = cabeleireiroService.buscarCabeleireiro(id);

        if (existente != null) {
            try {
                System.out.print("Novas especialidades (separadas por vírgula): ");
                List<String> especialidades = Arrays.asList(sc.nextLine().split(","));

                System.out.print("Nova média de avaliações: ");
                double media = sc.nextDouble(); sc.nextLine();

                System.out.print("Novo total de avaliações: ");
                int total = sc.nextInt(); sc.nextLine();

                System.out.print("Novos dias disponíveis: ");
                List<String> dias = Arrays.asList(sc.nextLine().split(","));

                System.out.print("Novos horários disponíveis (ex: 09:00,14:00): ");
                String[] horariosStr = sc.nextLine().split(",");
                List<LocalTime> horarios = new ArrayList<>();
                for (String h : horariosStr) {
                    horarios.add(LocalTime.parse(h.trim()));
                }

                System.out.print("Novo tempo de experiência: ");
                String tempo = sc.nextLine();

                Cabeleireiro atualizado = new Cabeleireiro(
                    existente.getIdUsuario(), existente.getNome(), existente.getCpf(),
                    existente.getTelefone(), existente.getEmail(), existente.getSenha(),
                    especialidades, media, total, dias, horarios, tempo
                );

                if (cabeleireiroService.atualizarCabeleireiro(atualizado, id)) {
                    System.out.println("Cabeleireiro atualizado com sucesso!");
                } else {
                    System.out.println("Erro ao atualizar o cabeleireiro.");
                }

            } catch (Exception e) {
                System.out.println("Erro de entrada: " + e.getMessage());
            }
        } else {
            System.out.println("Cabeleireiro não encontrado.");
        }
    }

    public void deletarCabeleireiro() {
        System.out.print("Digite o ID do cabeleireiro que deseja deletar: ");
        int id = sc.nextInt(); sc.nextLine();

        if (cabeleireiroService.deletarCabeleireiro(id)) {
            System.out.println("Cabeleireiro deletado com sucesso!");
        } else {
            System.out.println("Erro ao deletar o cabeleireiro.");
        }
    }

    public void listarCabeleireiros() {
        TreeMap<Integer, Cabeleireiro> lista = cabeleireiroService.listarCabeleireiros();

        if (lista.isEmpty()) {
            System.out.println("Nenhum cabeleireiro cadastrado.");
        } else {
            System.out.println("Lista de cabeleireiros:");
            for (Integer id : lista.keySet()) {
                System.out.println("ID: " + id);
                System.out.println(lista.get(id));
                System.out.println("---------------------------------");
            }
        }
    }
}
