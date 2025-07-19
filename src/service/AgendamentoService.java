package service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import dao.AgendamentoDAO;
import dao.CabeleireiroDAO;
import dao.ClienteDAO;
import dao.ProcedimentoDAO;
import model.Agendamento;
import model.Cabeleireiro;
import model.Cliente;
import model.Procedimento;
import model.StatusAgendamento;

public class AgendamentoService {

    private AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private CabeleireiroDAO cabeleireiroDAO = new CabeleireiroDAO();
    private ProcedimentoDAO procedimentoDAO = new ProcedimentoDAO();

    private static final double DESCONTO_DE_ANIVERSARIO = 0.20;

    public Agendamento criarAgendamento(int idCliente, int idCabeleireiro, List<Integer> idsProcedimentos,
            String diaDaSemana, LocalTime horario) {

        Cliente cliente = clienteDAO.buscaPorId(idCliente);
        Cabeleireiro cabeleireiro = cabeleireiroDAO.buscarPorId(idCabeleireiro);

        if (cliente == null || cabeleireiro == null) {
            System.err.println("Erro: Cliente ou Cabeleireiro não encontrado.");
            return null;
        }

        LocalDateTime dataHoraAgendamento = calcularProximaDataHora(diaDaSemana, horario);
        if (dataHoraAgendamento == null) {
            System.err.println("Erro: Dia da semana '" + diaDaSemana + "' é inválido.");
            return null;
        }

        List<LocalTime> horariosOcupados = agendamentoDAO.listarHorariosOcupados(idCabeleireiro,
                dataHoraAgendamento.toLocalDate());
        if (horariosOcupados.contains(horario)) {
            System.err.println("Erro: Este horário está preenchido por outro cliente.");
            return null;
        }

        if (!verificarDisponibilidadeCabeleireiro(cabeleireiro, dataHoraAgendamento)) {
            System.err.println("Erro: O cabeleireiro não está disponível no dia ou horário especificado.");
            return null;
        }

        TreeMap<Integer, Procedimento> procedimentosDisponiveis = procedimentoDAO.listarTodos();
        List<Procedimento> procedimentosSelecionados = new ArrayList<>();
        for (Integer id : idsProcedimentos) {
            if (procedimentosDisponiveis.containsKey(id)) {
                procedimentosSelecionados.add(procedimentosDisponiveis.get(id));
            } else {
                System.err.println("Erro: Procedimento com ID " + id + " não encontrado.");
                return null;
            }
        }

        if (procedimentosSelecionados.isEmpty()) {
            System.err.println("Erro: Nenhum procedimento válido selecionado.");
            return null;
        }

        double precoBase = 0.0;
        for (Procedimento proc : procedimentosSelecionados) {
            precoBase += proc.getPreco();
        }

        double precoFinal = precoBase;
        if (cliente.verificarAnivesario(dataHoraAgendamento.toLocalDate())) {
            precoFinal = precoBase * (1 - DESCONTO_DE_ANIVERSARIO);
            System.out.println("INFO: Desconto de aniversário aplicado!");
        }

        Agendamento novoAgendamento = new Agendamento(cliente, cabeleireiro, procedimentosSelecionados,
                dataHoraAgendamento);
        novoAgendamento.setPrecoTotal(precoFinal);

        boolean concluido = agendamentoDAO.salvarAgendamento(novoAgendamento, idsProcedimentos);

        return concluido ? novoAgendamento : null;
    }

    public Agendamento reagendar(int idAgendamento, String novoDiaDaSemana, LocalTime novoHorario) {
        Agendamento agendamentoAtual = agendamentoDAO.buscarPorId(idAgendamento);

        if (agendamentoAtual == null || agendamentoAtual.getStatus() == StatusAgendamento.CONCLUIDO
                || agendamentoAtual.getStatus() == StatusAgendamento.CANCELADO) {
            System.err.println("Erro: Este agendamento não pode ser reagendado.");
            return null;
        }

        LocalDateTime novaDataHora = calcularProximaDataHora(novoDiaDaSemana, novoHorario);
        if (novaDataHora == null) {
            System.err.println("Erro: Dia da semana inválido.");
            return null;
        }

        Cabeleireiro cabeleireiro = agendamentoAtual.getCabeleireiro();
        if (!verificarDisponibilidadeCabeleireiro(cabeleireiro, novaDataHora)) {
            System.err.println("Erro: O cabeleireiro não está disponível no novo dia/horário solicitado.");
            return null;
        }

        boolean concluido = agendamentoDAO.reagendar(idAgendamento, novaDataHora);
        return concluido ? agendamentoDAO.buscarPorId(idAgendamento) : null;
    }

    public boolean concluirAgendamento(int idAgendamento) {
        Agendamento agendamento = agendamentoDAO.buscarPorId(idAgendamento);

        if (agendamento == null || agendamento.getStatus() == StatusAgendamento.CONCLUIDO
                || agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            System.err.println("Erro: Agendamento não pode ser concluído.");
            return false;
        }

        boolean sucessoStatus = agendamentoDAO.atualizarStatus(idAgendamento, StatusAgendamento.CONCLUIDO);

        if (sucessoStatus) {
            Cliente cliente = agendamento.getCliente();
            LocalDate dataVisita = agendamento.getDataHora().toLocalDate();
            return clienteDAO.atualizarUltimaVisita(cliente.getIdUsuario(), dataVisita);
        }
        return false;
    }

    public boolean cancelarAgendamento(int idAgendamento) {
        Agendamento agendamento = agendamentoDAO.buscarPorId(idAgendamento);
        if (agendamento == null || agendamento.getStatus() == StatusAgendamento.CONCLUIDO
                || agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            System.err.println("Erro: Agendamento não pode ser cancelado.");
            return false;
        }
        return agendamentoDAO.atualizarStatus(idAgendamento, StatusAgendamento.CANCELADO);
    }

    public List<Agendamento> listarAgendamentosPorCliente(int idCliente) {
        return agendamentoDAO.listarPorCliente(idCliente);
    }

    public List<Agendamento> listarAgendamentosPorCabeleireiro(int idCabeleireiro) {
        return agendamentoDAO.listarPorCabeleireiro(idCabeleireiro);
    }

    public Agendamento buscarAgendamentoPorId(int idAgendamento) {
        return agendamentoDAO.buscarPorId(idAgendamento);
    }

    private LocalDateTime calcularProximaDataHora(String diaDaSemana, LocalTime horario) {
        DayOfWeek diaDesejado = converterStringParaDayOfWeek(diaDaSemana);
        if (diaDesejado == null)
            return null;
        LocalDate hoje = LocalDate.now();
        LocalDate proximaData = hoje.with(TemporalAdjusters.nextOrSame(diaDesejado));
        return LocalDateTime.of(proximaData, horario);
    }

    private boolean verificarDisponibilidadeCabeleireiro(Cabeleireiro cabeleireiro, LocalDateTime dataHora) {
        String diaDaSemana = converterDayOfWeekParaString(dataHora.getDayOfWeek());
        if (diaDaSemana == null)
            return false;

        boolean diaDisponivel = false;
        for (String dia : cabeleireiro.getDiasDisponiveis()) {
            if (dia.trim().equalsIgnoreCase(diaDaSemana)) {
                diaDisponivel = true;
                break;
            }
        }

        boolean horarioDisponivel = cabeleireiro.getHorariosDisponiveis().contains(dataHora.toLocalTime());

        return diaDisponivel && horarioDisponivel;
    }

    public List<LocalTime> listarHorariosDisponiveisParaData(int idCabeleireiro, String diaDaSemana) {
        LocalDateTime dataHoraCalculada = calcularProximaDataHora(diaDaSemana, LocalTime.MIDNIGHT);
        if (dataHoraCalculada == null) {
            return new ArrayList<>();
        }
        LocalDate data = dataHoraCalculada.toLocalDate();

        Cabeleireiro cabeleireiro = cabeleireiroDAO.buscarPorId(idCabeleireiro);
        if (cabeleireiro == null) {
            return new ArrayList<>();
        }

        List<LocalTime> todosOsHorarios = new ArrayList<>(cabeleireiro.getHorariosDisponiveis());

        List<LocalTime> horariosOcupados = agendamentoDAO.listarHorariosOcupados(idCabeleireiro, data);

        todosOsHorarios.removeAll(horariosOcupados);

        return todosOsHorarios;
    }

    private DayOfWeek converterStringParaDayOfWeek(String dia) {
        switch (dia.toUpperCase()) {
            case "SEGUNDA":
                return DayOfWeek.MONDAY;
            case "TERCA":
                return DayOfWeek.TUESDAY;
            case "QUARTA":
                return DayOfWeek.WEDNESDAY;
            case "QUINTA":
                return DayOfWeek.THURSDAY;
            case "SEXTA":
                return DayOfWeek.FRIDAY;
            case "SABADO":
                return DayOfWeek.SATURDAY;
            case "DOMINGO":
                return DayOfWeek.SUNDAY;
            default:
                return null;
        }
    }

    private String converterDayOfWeekParaString(DayOfWeek dia) {
        switch (dia) {
            case MONDAY:
                return "SEGUNDA";
            case TUESDAY:
                return "TERCA";
            case WEDNESDAY:
                return "QUARTA";
            case THURSDAY:
                return "QUINTA";
            case FRIDAY:
                return "SEXTA";
            case SATURDAY:
                return "SABADO";
            case SUNDAY:
                return "DOMINGO";
            default:
                return null;
        }
    }
}