package view;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import model.Agendamento;
import model.Cabeleireiro;
import model.Cliente;
import model.Procedimento;
import service.AgendamentoService;
import service.CabeleireiroService;
import service.ProcedimentoService;
import util.Entradas;

public class AgendamentoView {
    private AgendamentoService servicoAgendamento = new AgendamentoService();

    public void menuAgendamentoCliente(Cliente clienteLogado) {
        int opcao = 0;
        while (opcao != 5) {
            System.out.println("\n======== MEUS AGENDAMENTOS (" + clienteLogado.getNome() + ") ========");
            System.out.println("| [1] NOVO AGENDAMENTO                      |");
            System.out.println("| [2] VISUALIZAR MEUS AGENDAMENTOS          |");
            System.out.println("| [3] REAGENDAR                             |");
            System.out.println("| [4] CANCELAR                              |");
            System.out.println("| [5] VOLTAR                                |");
            System.out.println("=============================================");
            opcao = Entradas.lerNumero("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    NovoAgendamento(clienteLogado);
                    break;
                case 2:
                    HistoricoCliente(clienteLogado);
                    break;
                case 3:
                    ReagendarAgendamento(clienteLogado);
                    break;
                case 4:
                    CancelamentoAgendamento(clienteLogado);
                    break;
                case 5:
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    public void menuAgendamentoCabeleireiro(Cabeleireiro cabeleireiroLogado) {
        int opcao = 0;
        while (opcao != 4) {
            System.out.println("\n======== MINHA AGENDA (" + cabeleireiroLogado.getNome() + ") ========");
            System.out.println("| [1] VISUALIZAR MINHA AGENDA               |");
            System.out.println("| [2] CONCLUIR UM AGENDAMENTO               |");
            System.out.println("| [3] REAGENDAR                             |");
            System.out.println("| [4] VOLTAR                                |");
            System.out.println("=============================================");
            opcao = Entradas.lerNumero("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    exibirAgendaDoCabeleireiro(cabeleireiroLogado);
                    break;
                case 2:
                    ConcluirAgendamento(cabeleireiroLogado);
                    break;
                case 3:
                    ReagendarAgendamento(null);
                    break;
                case 4:
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void NovoAgendamento(Cliente cliente) {
        System.out.println("\n=== NOVO AGENDAMENTO ===");
        CabeleireiroService servicoCabeleireiro = new CabeleireiroService();
        TreeMap<Integer, Cabeleireiro> cabeleireiros = servicoCabeleireiro.listarCabeleireiros();
        if (cabeleireiros.isEmpty()) {
            System.out.println("Nenhum cabeleireiro disponível.");
            return;
        }
        System.out.println("Cabeleireiros disponíveis:");
        cabeleireiros.forEach((id, cab) -> System.out.println(id + " - " + cab.getNome()));
        int idCabeleireiro = Entradas.lerNumero("Digite o ID do cabeleireiro: ");

        Cabeleireiro cabeleireiroEscolhido = cabeleireiros.get(idCabeleireiro);
        if (cabeleireiroEscolhido == null) {
            System.out.println("ID de cabeleireiro inválido.");
            return;
        }
        System.out.println("\nDisponibilidade para " + cabeleireiroEscolhido.getNome() + ":");
        System.out.println("Dias: " + cabeleireiroEscolhido.getDiasDisponiveis());
        System.out.println("Horários: " + cabeleireiroEscolhido.getHorariosDisponiveis());

        System.out.print("Digite o dia da semana desejado (ex: TERCA): ");
        String diaSemana = Entradas.lerLinha().toUpperCase();
        List<LocalTime> horariosLivres = servicoAgendamento.listarHorariosDisponiveisParaData(idCabeleireiro,
                diaSemana);

        if (horariosLivres.isEmpty()) {
            System.out.println(
                    "Desculpe, não há horários livres para " + cabeleireiroEscolhido.getNome() + " neste dia.");
            return;
        }

        System.out.println("Horários realmente disponíveis: " + horariosLivres);
        System.out.print("Digite o horário desejado (ex: 10:20): ");
        LocalTime horario;
        try {
            horario = LocalTime.parse(Entradas.lerLinha());
        } catch (DateTimeParseException e) {
            System.out.println("Formato de horário inválido.");
            return;
        }

        ProcedimentoService servicoProcedimento = new ProcedimentoService();
        TreeMap<Integer, Procedimento> procedimentos = servicoProcedimento.listarProcedimentos();
        System.out.println("\nProcedimentos disponíveis:");
        procedimentos
                .forEach((id, proc) -> System.out.printf("%d - %s (R$ %.2f)\n", id, proc.getNome(), proc.getPreco()));

        System.out.print("Digite os IDs dos procedimentos (separados por vírgula): ");
        String[] idsStr = Entradas.lerLinha().split(",");
        List<Integer> idsProcedimentos = new ArrayList<>();
        for (String idStr : idsStr) {
            try {
                idsProcedimentos.add(Integer.parseInt(idStr.trim()));
            } catch (NumberFormatException e) {
                System.out.println("ID inválido: " + idStr);
            }
        }

        Agendamento agendamentoCriado = servicoAgendamento.criarAgendamento(cliente.getIdUsuario(), idCabeleireiro,
                idsProcedimentos, diaSemana, horario);

        if (agendamentoCriado != null) {
            System.out.println("\nAgendamento criado com sucesso!");
            System.out.println(agendamentoCriado);
        } else {
            System.out.println("\nNão foi possível criar o agendamento.");
        }
    }

    private void ReagendarAgendamento(Cliente clienteLogado) {
        if (clienteLogado != null) {
            HistoricoCliente(clienteLogado);
        } else {
            System.out.println("Para reagendar, por favor, visualize sua agenda para saber o ID.");
        }

        System.out.println("\n=== REAGENDAR AGENDAMENTO ===");
        int idAgendamento = Entradas.lerNumero("Digite o ID do agendamento que deseja reagendar: ");

        Agendamento agendamento = servicoAgendamento.buscarAgendamentoPorId(idAgendamento);
        if (agendamento == null) {
            System.out.println("Agendamento não encontrado.");
            return;
        }

        System.out.println("\nDisponibilidade do cabeleireiro " + agendamento.getCabeleireiro().getNome() + ":");
        System.out.println("Dias: " + agendamento.getCabeleireiro().getDiasDisponiveis());
        System.out.println("Horários: " + agendamento.getCabeleireiro().getHorariosDisponiveis());

        System.out.print("Digite o NOVO dia da semana (ex: SEXTA): ");
        String novoDia = Entradas.lerLinha().toUpperCase();
        System.out.print("Digite o NOVO horário (ex: 15:00): ");
        String horarioStr = Entradas.lerLinha();
        LocalTime novoHorario;
        try {
            novoHorario = LocalTime.parse(horarioStr);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de horário inválido.");
            return;
        }

        Agendamento reagendado = servicoAgendamento.reagendar(idAgendamento, novoDia, novoHorario);
        if (reagendado != null) {
            System.out.println("\nSucesso! Agendamento foi reagendado para:");
            System.out.println(reagendado);
        } else {
            System.out.println("\nNão foi possível reagendar.");
        }
    }

    private void ConcluirAgendamento(Cabeleireiro cabeleireiroLogado) {
        exibirAgendaDoCabeleireiro(cabeleireiroLogado);
        int idAgendamento = Entradas.lerNumero("Digite o ID do agendamento para concluir: ");
        if (servicoAgendamento.concluirAgendamento(idAgendamento)) {
            System.out.println("Agendamento concluído com sucesso!");
        } else {
            System.out.println("Falha ao concluir o agendamento.");
        }
    }

    private void CancelamentoAgendamento(Cliente clienteLogado) {
        HistoricoCliente(clienteLogado);
        int idAgendamento = Entradas.lerNumero("Digite o ID do agendamento para cancelar: ");
        if (servicoAgendamento.cancelarAgendamento(idAgendamento)) {
            System.out.println("Agendamento cancelado com sucesso.");
        } else {
            System.out.println("Falha ao cancelar o agendamento.");
        }
    }

    private void HistoricoCliente(Cliente clienteLogado) {
        List<Agendamento> agendamentos = servicoAgendamento.consultarHistoricoDoCliente(clienteLogado.getIdUsuario());
        if (agendamentos.isEmpty()) {
            System.out.println("Você não possui agendamentos.");
            return;
        }
        System.out.println("\n=== Meus Agendamentos ===");
        for (Agendamento ag : agendamentos) {
            System.out.println(ag.toString());
        }
    }

    private void exibirAgendaDoCabeleireiro(Cabeleireiro cabeleireiroLogado) {
        List<Agendamento> agendamentos = servicoAgendamento.consultarHistoricoDoCabeleireiro(cabeleireiroLogado.getIdUsuario());
        if (agendamentos.isEmpty()) {
            System.out.println("Você não possui agendamentos na sua agenda.");
            return;
        }
        System.out.println("\n=== Minha Agenda ===");
        for (Agendamento ag : agendamentos) {
            System.out.println(ag.toString());
        }
    }
}