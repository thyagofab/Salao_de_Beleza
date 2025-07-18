package view;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    private AgendamentoService agendamentoService;

   public AgendamentoView() {
        this.agendamentoService = new AgendamentoService();
    }

    public void menuAgendamentoCliente(Cliente clienteLogado) {
        int opcao = 0;
        while (opcao != 4) {
            System.out.println("\n======== MEUS AGENDAMENTOS (Cliente) ========");
            System.out.println("| [1] NOVO AGENDAMENTO                        |");
            System.out.println("| [2] VISUALIZAR MEUS AGENDAMENTOS            |");
            System.out.println("| [3] CANCELAR UM AGENDAMENTO                 |");
            System.out.println("| [4] VOLTAR                                  |");
            System.out.println("===============================================");
            opcao = Entradas.lerNumero("Escolha uma opção");

            switch (opcao) {
                case 1:
                    realizarNovoAgendamento(clienteLogado);
                    break;
                case 2:
                    listarAgendamentosDoCliente(clienteLogado);
                    break;
                case 3:
                    cancelarAgendamentoCliente(clienteLogado);
                    break;
                case 4:
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

   private void realizarNovoAgendamento(Cliente clienteLogado) {
    System.out.println("\n--- NOVO AGENDAMENTO ---");

    // 1. Listar e escolher o cabeleireiro
    CabeleireiroService cabeleireiroService = new CabeleireiroService();
    TreeMap<Integer, Cabeleireiro> cabeleireiros = cabeleireiroService.listarCabeleireiros();
    if (cabeleireiros.isEmpty()) {
        System.out.println("Nenhum cabeleireiro disponível no momento.");
        return;
    }
    System.out.println("Cabeleireiros disponíveis:");
    cabeleireiros.forEach((id, cab) -> System.out.println(id + " - " + cab.getNome()));
    int cabeleireiroId = Entradas.lerNumero("Digite o ID do cabeleireiro desejado: ");

    // Valida a escolha do cabeleireiro
    Cabeleireiro cabeleireiroEscolhido = cabeleireiros.get(cabeleireiroId);
    if (cabeleireiroEscolhido == null) {
        System.out.println("ID de cabeleireiro inválido.");
        return;
    }

    // 2. MOSTRAR a disponibilidade do cabeleireiro escolhido
    System.out.println("\nDisponibilidade para " + cabeleireiroEscolhido.getNome() + ":");
    System.out.println("Dias: " + cabeleireiroEscolhido.getDiasDisponiveis());
    System.out.println("Horários: " + cabeleireiroEscolhido.getHorariosDisponiveis());

    // 3. Pedir DIA DA SEMANA e HORÁRIO
    System.out.print("Digite o dia da semana desejado (ex: TERCA): ");
    String diaSemana = Entradas.lerLinha().toUpperCase();

    System.out.print("Digite o horário desejado (ex: 10:20): ");
    String horarioStr = Entradas.lerLinha();
    LocalTime horario;
    try {
        horario = LocalTime.parse(horarioStr);
    } catch (DateTimeParseException e) {
        System.out.println("Formato de horário inválido.");
        return;
    }

    ProcedimentoService procedimentoService = new ProcedimentoService();
    TreeMap<Integer, Procedimento> procedimentos = procedimentoService.listarProcedimentos();
    if (procedimentos.isEmpty()) {
        System.out.println("Nenhum procedimento disponível no momento.");
        return;
    }
    System.out.println("\nProcedimentos disponíveis:");
    procedimentos.forEach((id, proc) -> System.out.printf("%d - %s (R$ %.2f)\n", id, proc.getNome(), proc.getPreco()));
    
    List<Integer> procedimentoIds = new ArrayList<>();
    System.out.print("Digite os IDs dos procedimentos (separados por vírgula): ");
    String[] idsStr = Entradas.lerLinha().split(",");
    for (String idStr : idsStr) {
        try {
            procedimentoIds.add(Integer.parseInt(idStr.trim()));
        } catch (NumberFormatException e) {
            System.out.println("'" + idStr + "' não é um ID válido. Ignorando.");
        }
    }

    Agendamento agendamentoCriado = agendamentoService.criarAgendamento(
            clienteLogado.getIdUsuario(),
            cabeleireiroId,
            procedimentoIds,
            diaSemana, // Passando o dia da semana
            horario    // Passando o horário
    );
    
    if (agendamentoCriado != null) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        System.out.println("\nAgendamento criado com sucesso!");
        System.out.println("Data calculada: " + agendamentoCriado.getDataHora().format(formatter));
        System.out.printf("Valor Total: R$ %.2f\n", agendamentoCriado.getPrecoTotal());
    } else {
        System.out.println("\nNão foi possível criar o agendamento. Verifique os erros informados.");
    }
}

    private void listarAgendamentosDoCliente(Cliente clienteLogado) {
        List<Agendamento> agendamentos = agendamentoService.listarAgendamentosPorCliente(clienteLogado.getIdUsuario());
        if (agendamentos.isEmpty()) {
            System.out.println("Você não possui agendamentos.");
            return;
        }
        System.out.println("\n--- Meus Agendamentos ---");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        for (Agendamento ag : agendamentos) {
            System.out.println("---------------------------------");
            System.out.println("ID: " + ag.getId());
            System.out.println("Cabeleireiro: " + ag.getCabeleireiro().getNome());
            System.out.println("Data/Hora: " + ag.getDataHora().format(formatter));
            System.out.println("Status: " + ag.getStatus());
            System.out.printf("Valor: R$ %.2f\n", ag.getPrecoTotal());
        }
        System.out.println("---------------------------------");
    }

    private void cancelarAgendamentoCliente(Cliente clienteLogado) {
        listarAgendamentosDoCliente(clienteLogado);
        int idAgendamento = Entradas.lerNumero("Digite o ID do agendamento que deseja cancelar");
        
        if (agendamentoService.cancelarAgendamento(idAgendamento)) {
            System.out.println("Agendamento cancelado com sucesso.");
        } else {
            System.out.println("Falha ao cancelar o agendamento. Verifique se o ID está correto e se o agendamento já não foi concluído/cancelado.");
        }
    }

    public void menuAgendamentoCabeleireiro(Cabeleireiro cabeleireiroLogado) {
        int opcao = 0;
        while (opcao != 3) {
            System.out.println("\n======== MINHA AGENDA (Cabeleireiro) ========");
            System.out.println("| [1] VISUALIZAR MINHA AGENDA                 |");
            System.out.println("| [2] CONCLUIR AGENDAMENTO                    |");
            System.out.println("| [3] VOLTAR                                  |");
            System.out.println("===============================================");
            opcao = Entradas.lerNumero("Escolha uma opção");

            switch (opcao) {
                case 1:
                    listarAgendamentosDoCabeleireiro(cabeleireiroLogado);
                    break;
                case 2:
                    concluirAgendamentoView(cabeleireiroLogado);
                    break;
                case 3:
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
    
    private void listarAgendamentosDoCabeleireiro(Cabeleireiro cabeleireiroLogado) {
        List<Agendamento> agendamentos = agendamentoService.listarAgendamentosPorCabeleireiro(cabeleireiroLogado.getIdUsuario());
        if (agendamentos.isEmpty()) {
            System.out.println("Você não possui agendamentos.");
            return;
        }
        System.out.println("\n--- Minha Agenda ---");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        for (Agendamento ag : agendamentos) {
            System.out.println("---------------------------------");
            System.out.println("ID: " + ag.getId());
            System.out.println("Cliente: " + ag.getCliente().getNome());
            System.out.println("Data/Hora: " + ag.getDataHora().format(formatter));
            System.out.println("Status: " + ag.getStatus());
            System.out.print("Procedimentos: ");
            ag.getProcedimentos().forEach(p -> System.out.print(p.getNome() + "; "));
            System.out.println();
        }
        System.out.println("---------------------------------");
    }

    private void concluirAgendamentoView(Cabeleireiro cabeleireiroLogado) {
        listarAgendamentosDoCabeleireiro(cabeleireiroLogado);
        int idAgendamento = Entradas.lerNumero("Digite o ID do agendamento que deseja concluir");

        if(agendamentoService.concluirAgendamento(idAgendamento)) {
            System.out.println("Agendamento concluído com sucesso! A última visita do cliente foi atualizada.");
        } else {
            System.out.println("Falha ao concluir o agendamento. Verifique o ID.");
        }
    }


    
}