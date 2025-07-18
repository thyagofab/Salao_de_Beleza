package view;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
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
    private CabeleireiroService cabeleireiroService;
    private ProcedimentoService procedimentoService;
    private Scanner sc;

    public CabeleireiroView(Scanner sc, CabeleireiroService cabeleireiroService, ProcedimentoService procedimentoService) {
        this.cabeleireiroService = cabeleireiroService;
        this.procedimentoService = procedimentoService;
        this.sc = sc;
    }

    public void MenuCabeleireiros(int idCabeleireiro) {
        Cabeleireiro cabeleireiroLogado = this.cabeleireiroService.buscarCabeleireiro(idCabeleireiro);
        int opcao = 0;

        while (opcao != 7) {
            System.out.println("======== MENU DE CABELEIREIROS ========");
            System.out.println("| [1] CONSULTAR CABELEIREIRO          |");
            System.out.println("| [2] ATUALIZAR DADOS DO CABELEIREIRO |");
            System.out.println("| [3] DELETAR CABELEIREIRO            |");
            System.out.println("| [4] EXIBIR LISTA DE CABELEIREIROS   |");
            System.out.println("| [5] GERENCIAR PROCEDIMENTOS         |");
            System.out.println("| [6] GERENCIAR AGENDAMENTOS          |");
            System.out.println("| [7] VOLTAR AO MENU PRINCIPAL        |");
            System.out.println("=======================================");
            System.out.print("Escolha uma opção: ");

            opcao = Entradas.lerNumero("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    buscarPorIdCabeleireiro();
                    break;
                case 2:
                    atualizarDadosDoCabeleireiro();
                    break;
                case 3:
                    deletarDadosCabeleireiro();
                    break;
                case 4:
                    exibirListaDeCabeleireiros();
                    break;
                case 5:
                    ProcedimentoView procedimentoView = new ProcedimentoView(new Scanner(System.in), procedimentoService);
                    procedimentoView.menuProcedimento();
                    break;
                case 6:
                    AgendamentoView agendamentoView = new AgendamentoView();
                    agendamentoView.menuAgendamentoCabeleireiro(cabeleireiroLogado);
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
        System.out.print("Digite o nome: ");
        String nome = Entradas.validarNome();

        System.out.print("Digite o CPF (apenas números): ");
        String cpf = Entradas.validarCPF();

        System.out.print("Digite o telefone com DDD (11 dígitos): ");
        String telefone = Entradas.validarTelefone();

        System.out.print("Digite o email: ");
        String email = Entradas.validarEmail();

        System.out.print("Digite senha: ");
        String senha = Entradas.validarSenha();

        List<String> especialidades = new ArrayList<>();
        String especialidadesStr = Entradas.lerString("Especialidades (separadas por vírgula)");
        if (!especialidadesStr.isEmpty()) {
            especialidades = Arrays.asList(especialidadesStr.split(","));
        }

        double media = Entradas.lerNumeroDouble("Média de avaliações");

        int total = Entradas.lerNumero("Total de avaliações");

        List<String> dias = new ArrayList<>();
        System.out.println("Digite os dias disponíveis (separados por vírgula, ex: SEGUNDA,TERÇA): ");
        String diasStr = sc.nextLine().toUpperCase();
        
        if (!diasStr.isEmpty()) {
            dias = Arrays.asList(diasStr.split(","));
        }

        System.out.print("Horários disponíveis (HH:mm, separados por vírgula, ex: 09:00,10:00): ");
        String[] horariosStr = Entradas.lerLinha().split(",");
        List<LocalTime> horarios = new ArrayList<>();
        for (String h : horariosStr) {
            try {
                horarios.add(LocalTime.parse(h.trim()));
            } catch (Exception e) {
                System.out.println("Horário '"+ h +"' inválido. Ignorando.");
            }
        }

        System.out.print("Tempo de experiência: ");
        String tempo = sc.nextLine();

        Cabeleireiro cab = new Cabeleireiro(
            0, nome, cpf, telefone, email, senha,
            especialidades, media, total, dias, horarios, tempo
        );

        if (cabeleireiroService.criarCabeleireiro(cab)) {
            System.out.println("Cabeleireiro adicionado com sucesso!");
        } else {
            System.out.println("Erro ao adicionar o cabeleireiro.");
        }
    }

    public void buscarPorIdCabeleireiro() {
        int id = Entradas.lerNumero("Digite o ID do cabeleireiro que deseja consultar: ");

        Cabeleireiro cab = cabeleireiroService.buscarCabeleireiro(id);

        if (cab != null) {
            System.out.println("Cabeleireiro encontrado:");
            System.out.println(cab.toString());
        } else {
            System.out.println("Cabeleireiro não encontrado com o ID: " + id);
        }
    }

    public void atualizarDadosDoCabeleireiro() {
        int id = Entradas.lerNumero("Digite o ID do cabeleireiro que deseja atualizar: ");

        Cabeleireiro existente = cabeleireiroService.buscarCabeleireiro(id);

        if (existente != null) {
            System.out.println("Digite os novos Dados para o cabeleireiro (deixe em branco para manter o atual):");

            System.out.print("Novo nome (" + existente.getNome() + "): ");
            String nome = Entradas.validarNome();
            existente.setNome(nome.isEmpty() ? existente.getNome() : nome);

            System.out.print("Novo CPF (" + existente.getCpf() + "): ");
            String cpf = Entradas.validarCPF();
            existente.setCpf(cpf.isEmpty() ? existente.getCpf() : cpf);

            System.out.print("Novo telefone (" + existente.getTelefone() + "): ");
            String telefone = Entradas.validarTelefone();
            existente.setTelefone(telefone.isEmpty() ? existente.getTelefone() : telefone);

            System.out.print("Novo email (" + existente.getEmail() + "): ");
            String email = Entradas.validarEmail();
            existente.setEmail(email.isEmpty() ? existente.getEmail() : email);

            System.out.print("Nova senha (deixe em branco para manter a atual): ");
            String senha = Entradas.validarSenha();
            existente.setSenha(senha.isEmpty() ? existente.getSenha() : senha);


            List<String> especialidades = new ArrayList<>();
            System.out.print("Novas especialidades (separadas por vírgula, atual: " + String.join(",", existente.getEspecialidades()) + "): ");
            String especialidadesStr = Entradas.lerString(" ");
            if (!especialidadesStr.isEmpty()) {
                especialidades = Arrays.asList(especialidadesStr.split(","));
                existente.setEspecialidades(especialidades);
            } else {
                existente.setEspecialidades(existente.getEspecialidades());
            }

            double media = Entradas.lerNumeroDouble("Nova média de avaliações (" + existente.getMediaDeAvaliacoes() + "): ");
            existente.setMediaDeAvaliacoes(media);

            int total = Entradas.lerNumero("Novo total de avaliações (" + existente.getTotalDeAvaliacoes() + "): ");
            existente.setTotalDeAvaliacoes(total);

            List<String> dias = new ArrayList<>();
            System.out.print("Novos dias disponíveis (ex: SEGUNDA,TERÇA, atual: " + String.join(",", existente.getDiasDisponiveis()) + "): ");
            String diasStr = Entradas.lerString(" ");
            if (!diasStr.isEmpty()) {
                dias = Arrays.asList(diasStr.split(","));
                existente.setDiasDisponiveis(dias);
            } else {
                existente.setDiasDisponiveis(existente.getDiasDisponiveis());
            }

            List<LocalTime> horarios = new ArrayList<>();
            System.out.print("Novos horários disponíveis (ex: 09:00,14:00, atual: " + converterListaLocalTimeParaString(existente.getHorariosDisponiveis()) + "): ");
            String horariosStr = Entradas.lerString(" ");
            if (!horariosStr.isEmpty()) {
                try {
                    String[] partesHorarios = horariosStr.split(",");
                    for (String h : partesHorarios) {
                        horarios.add(LocalTime.parse(h.trim()));
                    }
                    existente.setHorariosDisponiveis(horarios);
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de horário inválido. Horários não serão atualizados.");
                    existente.setHorariosDisponiveis(existente.getHorariosDisponiveis());
                }
            } else {
                existente.setHorariosDisponiveis(existente.getHorariosDisponiveis());
            }

            System.out.print("Novo tempo de experiência (" + existente.getTempoDeExperiencia() + "): ");
            String tempo = Entradas.lerString(" ");
            existente.setTempoDeExperiencia(tempo.isEmpty() ? existente.getTempoDeExperiencia() : tempo);


            if (cabeleireiroService.atualizarCabeleireiro(existente)) {
                System.out.println("Cabeleireiro atualizado com sucesso!");
            } else {
                System.out.println("Erro ao atualizar o cabeleireiro.");
            }
        } else {
            System.out.println("Cabeleireiro não encontrado.");
        }
    }

    public void deletarDadosCabeleireiro() {
        int id = Entradas.lerNumero("Digite o ID do cabeleireiro que deseja deletar: ");

        Cabeleireiro cabeleireiroExistente = cabeleireiroService.buscarCabeleireiro(id);

        if (cabeleireiroExistente != null) {
            if (cabeleireiroService.deletarCabeleireiro(id)) {
                System.out.println("Cabeleireiro deletado com sucesso!");
            } else {
                System.out.println("Erro ao deletar o cabeleireiro.");
            }
        } else {
            System.out.println("Cabeleireiro não encontrado.");
        }
    }

    public void exibirListaDeCabeleireiros() {
        TreeMap<Integer, Cabeleireiro> lista = cabeleireiroService.listarCabeleireiros();

        if (lista.isEmpty()) {
            System.out.println("Nenhum cabeleireiro cadastrado no momento.");
        } else {
            System.out.println("--- Lista de Cabeleireiros ---");
            for (Cabeleireiro cab : lista.values()) {
                System.out.println(cab.toString());
                System.out.println("---------------------------------");
            }
        }
    }

    private String converterListaLocalTimeParaString(List<LocalTime> lista) {
        if (lista == null || lista.isEmpty()) {
            return "";
        }
        List<String> strList = new ArrayList<>();
        for (LocalTime time : lista) {
            strList.add(time.toString());
        }
        return String.join(",", strList);
    }
}