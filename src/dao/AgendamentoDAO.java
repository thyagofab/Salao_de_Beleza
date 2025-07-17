package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import data.ConexaoDoBanco;
import model.Agendamento;
import model.Cabeleireiro;
import model.Cliente;
import model.Procedimento;
import model.StatusAgendamento;

public class AgendamentoDAO {

    /**
     * Salva um agendamento completo, incluindo seus procedimentos, de forma transacional.
     */
    public boolean salvar(Agendamento agendamento, List<Integer> procedimentoIds) {
        String sqlAgendamento = "INSERT INTO agendamentos (cliente_id, cabeleireiro_id, data_hora, status, preco_total) VALUES (?, ?, ?, ?, ?)";
        String sqlAgendamentoProcedimentos = "INSERT INTO agendamento_procedimentos (agendamento_id, procedimento_id) VALUES (?, ?)";
        
        Connection conn = null;
        try {
            conn = ConexaoDoBanco.criarConexao();
            conn.setAutoCommit(false); 

            // 1. Inserir na tabela 'agendamentos'
            try (PreparedStatement ptAgendamento = conn.prepareStatement(sqlAgendamento, Statement.RETURN_GENERATED_KEYS)) {
                // ... (código interno do try continua igual)
                ptAgendamento.setInt(1, agendamento.getCliente().getIdUsuario());
                ptAgendamento.setInt(2, agendamento.getCabeleireiro().getIdUsuario());
                ptAgendamento.setString(3, agendamento.getDataHora().toString());
                ptAgendamento.setString(4, agendamento.getStatus().name());
                ptAgendamento.setDouble(5, agendamento.getPrecoTotal());
                ptAgendamento.executeUpdate();

                try (ResultSet generatedKeys = ptAgendamento.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        agendamento.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Falha ao obter o ID do agendamento.");
                    }
                }
            }

            // 2. Inserir na tabela de ligação usando a lista de IDs recebida
            try (PreparedStatement ptProcedimentos = conn.prepareStatement(sqlAgendamentoProcedimentos)) {
                for (Integer procId : procedimentoIds) { // Usa a lista de IDs
                    ptProcedimentos.setInt(1, agendamento.getId());
                    ptProcedimentos.setInt(2, procId);
                    ptProcedimentos.addBatch();
                }
                ptProcedimentos.executeBatch();
            }
            
            conn.commit();
            return true;

        } catch (SQLException e) {
            // ... (bloco catch e finally continuam iguais)
            System.err.println("Erro ao salvar agendamento: " + e.getMessage());
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { System.err.println("Erro ao fazer rollback: " + ex.getMessage()); } }
            return false;
        } finally {
            if (conn != null) { try { conn.close(); } catch (SQLException e) { System.err.println("Erro ao fechar conexão: " + e.getMessage()); } }
        }
    }

    /**
     * Atualiza o status de um agendamento existente.
     */
    public boolean atualizarStatus(int agendamentoId, StatusAgendamento novoStatus) {
        String sql = "UPDATE agendamentos SET status = ? WHERE id = ?";
        try (Connection conn = ConexaoDoBanco.criarConexao();
             PreparedStatement pt = conn.prepareStatement(sql)) {

            pt.setString(1, novoStatus.name());
            pt.setInt(2, agendamentoId);
            return pt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status do agendamento: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista todos os agendamentos de um cliente específico.
     */
    public List<Agendamento> listarPorCliente(int clienteId) {
        ClienteDAO clienteDAO = new ClienteDAO();
        CabeleireiroDAO cabeleireiroDAO = new CabeleireiroDAO();

        List<Agendamento> agendamentos = new ArrayList<>();
        String sql = "SELECT * FROM agendamentos WHERE cliente_id = ?";

        try (Connection conn = ConexaoDoBanco.criarConexao();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            
            pt.setInt(1, clienteId);
            ResultSet rs = pt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int cabeleireiroId = rs.getInt("cabeleireiro_id");
                
                // --- CORREÇÃO APLICADA AQUI ---
                LocalDateTime dataHora = LocalDateTime.parse(rs.getString("data_hora"));
                // ---------------------------------

                StatusAgendamento status = StatusAgendamento.valueOf(rs.getString("status"));
                double precoTotal = rs.getDouble("preco_total");

                Cliente cliente = clienteDAO.buscaPorId(clienteId);
                Cabeleireiro cabeleireiro = cabeleireiroDAO.buscarPorId(cabeleireiroId);
                List<Procedimento> procedimentos = buscarProcedimentosPorAgendamentoId(id);

                if (cliente != null && cabeleireiro != null) {
                    agendamentos.add(new Agendamento(id, cliente, cabeleireiro, procedimentos, dataHora, status, precoTotal));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar agendamentos por cliente: " + e.getMessage());
        }
        return agendamentos;
    }
    
    /**
     * Busca a lista de procedimentos associados a um agendamento.
     * Este método auxiliar é chamado por outros métodos de listagem.
     */
    private List<Procedimento> buscarProcedimentosPorAgendamentoId(int agendamentoId) throws SQLException {
        ProcedimentoDAO procedimentoDAO = new ProcedimentoDAO();
        List<Procedimento> procedimentos = new ArrayList<>();
        String sql = "SELECT procedimento_id FROM agendamento_procedimentos WHERE agendamento_id = ?";

        try (Connection conn = ConexaoDoBanco.criarConexao();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            
            pt.setInt(1, agendamentoId);
            ResultSet rs = pt.executeQuery();

            while(rs.next()) {
                int procedimentoId = rs.getInt("procedimento_id");
                Procedimento p = procedimentoDAO.buscarPorId(procedimentoId);
                if (p != null) {
                    procedimentos.add(p);
                }
            }
        }
        return procedimentos;
    }

    public List<Agendamento> listarPorCabeleireiro(int cabeleireiroId) {
        ClienteDAO clienteDAO = new ClienteDAO();
        CabeleireiroDAO cabeleireiroDAO = new CabeleireiroDAO();

        List<Agendamento> agendamentos = new ArrayList<>();
        String sql = "SELECT * FROM agendamentos WHERE cabeleireiro_id = ?";

        try (Connection conn = ConexaoDoBanco.criarConexao();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            
            pt.setInt(1, cabeleireiroId);
            ResultSet rs = pt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int clienteId = rs.getInt("cliente_id");
                
                // --- CORREÇÃO APLICADA AQUI ---
                LocalDateTime dataHora = LocalDateTime.parse(rs.getString("data_hora"));
                // ---------------------------------

                StatusAgendamento status = StatusAgendamento.valueOf(rs.getString("status"));
                double precoTotal = rs.getDouble("preco_total");

                Cliente cliente = clienteDAO.buscaPorId(clienteId);
                Cabeleireiro cabeleireiro = cabeleireiroDAO.buscarPorId(cabeleireiroId);
                List<Procedimento> procedimentos = buscarProcedimentosPorAgendamentoId(id);

                if (cliente != null && cabeleireiro != null) {
                    agendamentos.add(new Agendamento(id, cliente, cabeleireiro, procedimentos, dataHora, status, precoTotal));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar agendamentos por cabeleireiro: " + e.getMessage());
        }
        return agendamentos;
    }

     public Agendamento buscarPorId(int agendamentoId) {
        String sql = "SELECT * FROM agendamentos WHERE id = ?";

        try (Connection conn = ConexaoDoBanco.criarConexao();
             PreparedStatement pt = conn.prepareStatement(sql)) {
            
            pt.setInt(1, agendamentoId);
            ResultSet rs = pt.executeQuery();

            if (rs.next()) {
                int clienteId = rs.getInt("cliente_id");
                int cabeleireiroId = rs.getInt("cabeleireiro_id");
                
                // --- CORREÇÃO APLICADA AQUI ---
                
                LocalDateTime dataHora = LocalDateTime.parse(rs.getString("data_hora"));
                // ---------------------------------
                
                StatusAgendamento status = StatusAgendamento.valueOf(rs.getString("status"));
                double precoTotal = rs.getDouble("preco_total");

                ClienteDAO clienteDAO = new ClienteDAO();
                CabeleireiroDAO cabeleireiroDAO = new CabeleireiroDAO();

                Cliente cliente = clienteDAO.buscaPorId(clienteId);
                Cabeleireiro cabeleireiro = cabeleireiroDAO.buscarPorId(cabeleireiroId);
                List<Procedimento> procedimentos = buscarProcedimentosPorAgendamentoId(agendamentoId);

                if (cliente != null && cabeleireiro != null) {
                    return new Agendamento(agendamentoId, cliente, cabeleireiro, procedimentos, dataHora, status, precoTotal);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar agendamento por ID: " + e.getMessage());
        }
        return null;
    }
}