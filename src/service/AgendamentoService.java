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
    
    private static final double FATOR_DESCONTO_ANIVERSARIO = 0.80; // Aplica 20% de desconto (1.0 - 0.20)


     public Agendamento criarAgendamento(int clienteId, int cabeleireiroId, List<Integer> procedimentoIds, String diaDaSemana, LocalTime horario) {
        
        Cliente cliente = clienteDAO.buscaPorId(clienteId);
        Cabeleireiro cabeleireiro = cabeleireiroDAO.buscarPorId(cabeleireiroId);

        if (cliente == null || cabeleireiro == null) {
            System.err.println("Erro: Cliente ou Cabeleireiro não encontrado.");
            return null;
        }

        LocalDateTime dataHoraAgendamento = calcularProximaDataHora(diaDaSemana, horario);
        if (dataHoraAgendamento == null) {
            System.err.println("Erro: Dia da semana '" + diaDaSemana + "' é inválido.");
            return null;
        }

        if (!isDisponivel(cabeleireiro, dataHoraAgendamento)) {
            System.err.println("Erro: O cabeleireiro não está disponível no dia ou horário especificado.");
            return null;
        }

        TreeMap<Integer, Procedimento> procedimentosDisponiveis = procedimentoDAO.listarTodos();
        List<Procedimento> procedimentosSelecionados = new ArrayList<>();
        for (Integer id : procedimentoIds) {
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
        
        double precoBase = procedimentosSelecionados.stream().mapToDouble(Procedimento::getPreco).sum();
        double precoFinal = precoBase;
        
        if (cliente.verificarAnivesario(dataHoraAgendamento.toLocalDate())) {
            precoFinal *= FATOR_DESCONTO_ANIVERSARIO;
            System.out.println("INFO: Desconto de aniversário aplicado!");
        }

        Agendamento novoAgendamento = new Agendamento(cliente, cabeleireiro, procedimentosSelecionados, dataHoraAgendamento);
        novoAgendamento.setPrecoTotal(precoFinal);
        
        boolean sucesso = agendamentoDAO.salvar(novoAgendamento, procedimentoIds);

        return sucesso ? novoAgendamento : null;
    }

    private LocalDateTime calcularProximaDataHora(String diaDaSemana, LocalTime horario) {
        DayOfWeek diaDesejado = converterStringParaDayOfWeek(diaDaSemana);
        if (diaDesejado == null) {
            return null;
        }
        
        LocalDate hoje = LocalDate.now();
        LocalDate proximaData = hoje.with(TemporalAdjusters.nextOrSame(diaDesejado));
        
        return LocalDateTime.of(proximaData, horario);
    }
    
    private DayOfWeek converterStringParaDayOfWeek(String dia) {
        switch (dia.toUpperCase()) {
            case "SEGUNDA": return DayOfWeek.MONDAY;
            case "TERCA": return DayOfWeek.TUESDAY;
            case "QUARTA": return DayOfWeek.WEDNESDAY;
            case "QUINTA": return DayOfWeek.THURSDAY;
            case "SEXTA": return DayOfWeek.FRIDAY;
            case "SABADO": return DayOfWeek.SATURDAY;
            case "DOMINGO": return DayOfWeek.SUNDAY;
            default: return null;
        }
    }


    private boolean isDisponivel(Cabeleireiro cabeleireiro, LocalDateTime dataHora) {
        String diaDaSemana = converterDayOfWeekParaString(dataHora.getDayOfWeek().toString());
        if(diaDaSemana == null) return false;

        boolean diaDisponivel = cabeleireiro.getDiasDisponiveis().stream()
                .anyMatch(d -> d.trim().equalsIgnoreCase(diaDaSemana));
        
        boolean horarioDisponivel = cabeleireiro.getHorariosDisponiveis().contains(dataHora.toLocalTime());

        return diaDisponivel && horarioDisponivel;
    }

    private String converterDayOfWeekParaString(String dayOfWeek) {
        switch (dayOfWeek) {
            case "MONDAY": return "SEGUNDA";
            case "TUESDAY": return "TERCA";
            case "WEDNESDAY": return "QUARTA";
            case "THURSDAY": return "QUINTA";
            case "FRIDAY": return "SEXTA";
            case "SATURDAY": return "SABADO";
            case "SUNDAY": return "DOMINGO";
            default: return null;
        }
    }


    public boolean concluirAgendamento(int agendamentoId) {
        Agendamento agendamento = agendamentoDAO.buscarPorId(agendamentoId);

        if (agendamento == null || agendamento.getStatus() == StatusAgendamento.CONCLUIDO || agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            System.err.println("Erro: Agendamento não pode ser concluído.");
            return false;
        }

        boolean sucessoStatus = agendamentoDAO.atualizarStatus(agendamentoId, StatusAgendamento.CONCLUIDO);
        
        boolean sucessoVisita = false;
        if (sucessoStatus) {
            Cliente cliente = agendamento.getCliente();
            LocalDate dataVisita = agendamento.getDataHora().toLocalDate();
            sucessoVisita = clienteDAO.atualizarUltimaVisita(cliente.getIdUsuario(), dataVisita);
        }
        
        return sucessoStatus && sucessoVisita;
    }
    
    public boolean cancelarAgendamento(int agendamentoId) {
        Agendamento agendamento = agendamentoDAO.buscarPorId(agendamentoId);

        if (agendamento == null || agendamento.getStatus() == StatusAgendamento.CONCLUIDO || agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            System.err.println("Erro: Agendamento não pode ser cancelado.");
            return false;
        }
        return agendamentoDAO.atualizarStatus(agendamentoId, StatusAgendamento.CANCELADO);
    }
    
    public List<Agendamento> listarAgendamentosPorCliente(int clienteId) {
        return agendamentoDAO.listarPorCliente(clienteId);
    }
    
    public List<Agendamento> listarAgendamentosPorCabeleireiro(int cabeleireiroId) {
        return agendamentoDAO.listarPorCabeleireiro(cabeleireiroId);
    }
}