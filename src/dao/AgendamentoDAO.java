package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import data.ConexaoDoBanco;
import model.Agendamento;
import model.Cabeleireiro;
import model.Cliente;
import model.Procedimento;
import model.StatusAgendamento;

public class AgendamentoDAO {

    public boolean salvarAgendamento(Agendamento agendamento, List<Integer> idsProcedimentos) {
        String sqlAgendamento = "INSERT INTO agendamentos (cliente_id, cabeleireiro_id, data_hora, status, preco_total) VALUES (?, ?, ?, ?, ?)";
        String sqlAgendamentoProcedimentos = "INSERT INTO agendamento_procedimentos (agendamento_id, procedimento_id) VALUES (?, ?)";
        Connection conexao = null;
        try {
            conexao = ConexaoDoBanco.criarConexao();
            conexao.setAutoCommit(false);

            try (PreparedStatement declaracaoAgendamento = conexao.prepareStatement(sqlAgendamento,
                    Statement.RETURN_GENERATED_KEYS)) {
                declaracaoAgendamento.setInt(1, agendamento.getCliente().getIdUsuario());
                declaracaoAgendamento.setInt(2, agendamento.getCabeleireiro().getIdUsuario());
                declaracaoAgendamento.setString(3, agendamento.getDataHora().toString());
                declaracaoAgendamento.setString(4, agendamento.getStatus().name());
                declaracaoAgendamento.setDouble(5, agendamento.getPrecoTotal());
                declaracaoAgendamento.executeUpdate();

                try (ResultSet chavesGeradas = declaracaoAgendamento.getGeneratedKeys()) {
                    if (chavesGeradas.next()) {
                        agendamento.setId(chavesGeradas.getInt(1));
                    } else {
                        throw new SQLException("Falha ao obter o ID do agendamento.");
                    }
                }
            }

            try (PreparedStatement declaracaoProcedimentos = conexao.prepareStatement(sqlAgendamentoProcedimentos)) {
                for (Integer idProcedimento : idsProcedimentos) {
                    declaracaoProcedimentos.setInt(1, agendamento.getId());
                    declaracaoProcedimentos.setInt(2, idProcedimento);
                    declaracaoProcedimentos.addBatch();
                }
                declaracaoProcedimentos.executeBatch();
            }
            conexao.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao salvar agendamento: " + e.getMessage());
            if (conexao != null) {
                try {
                    conexao.rollback();
                } catch (SQLException ex) {
                    System.err.println("ERRO ROLLBACK: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (conexao != null) {
                try {
                    conexao.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }

    public Agendamento buscarPorId(int idAgendamento) {
        String sql = "SELECT * FROM agendamentos WHERE id = ?";
        try (Connection conexao = ConexaoDoBanco.criarConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, idAgendamento);
            ResultSet resultado = declaracao.executeQuery();
            if (resultado.next()) {
                return montarAgendamento(resultado);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar agendamento por ID: " + e.getMessage());
        }
        return null;
    }

    public List<Agendamento> listarPorCliente(int idCliente) {
        List<Agendamento> agendamentos = new ArrayList<>();
        String sql = "SELECT * FROM agendamentos WHERE cliente_id = ?";
        try (Connection conexao = ConexaoDoBanco.criarConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, idCliente);
            ResultSet resultado = declaracao.executeQuery();
            while (resultado.next()) {
                agendamentos.add(montarAgendamento(resultado));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar agendamentos por cliente: " + e.getMessage());
        }
        return agendamentos;
    }

    public List<Agendamento> listarPorCabeleireiro(int idCabeleireiro) {
        List<Agendamento> agendamentos = new ArrayList<>();
        String sql = "SELECT * FROM agendamentos WHERE cabeleireiro_id = ?";
        try (Connection conexao = ConexaoDoBanco.criarConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, idCabeleireiro);
            ResultSet resultado = declaracao.executeQuery();
            while (resultado.next()) {
                agendamentos.add(montarAgendamento(resultado));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar agendamentos por cabeleireiro: " + e.getMessage());
        }
        return agendamentos;
    }

    public boolean reagendar(int idAgendamento, LocalDateTime novaDataHora) {
        String sql = "UPDATE agendamentos SET data_hora = ?, status = ? WHERE id = ?";
        try (Connection conexao = ConexaoDoBanco.criarConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setString(1, novaDataHora.toString());
            declaracao.setString(2, StatusAgendamento.REAGENDADO.name());
            declaracao.setInt(3, idAgendamento);
            return declaracao.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao reagendar no banco de dados: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizarStatus(int idAgendamento, StatusAgendamento novoStatus) {
        String sql = "UPDATE agendamentos SET status = ? WHERE id = ?";
        try (Connection conexao = ConexaoDoBanco.criarConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setString(1, novoStatus.name());
            declaracao.setInt(2, idAgendamento);
            return declaracao.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status do agendamento: " + e.getMessage());
            return false;
        }
    }

    private Agendamento montarAgendamento(ResultSet resultado) throws SQLException {
        int id = resultado.getInt("id");
        int idCliente = resultado.getInt("cliente_id");
        int idCabeleireiro = resultado.getInt("cabeleireiro_id");
        LocalDateTime dataHora = LocalDateTime.parse(resultado.getString("data_hora"));
        StatusAgendamento status = StatusAgendamento.valueOf(resultado.getString("status"));
        double precoTotal = resultado.getDouble("preco_total");

        Cliente cliente = new ClienteDAO().buscaPorId(idCliente);
        Cabeleireiro cabeleireiro = new CabeleireiroDAO().buscarPorId(idCabeleireiro);
        List<Procedimento> procedimentos = buscarProcedimentosDoAgendamento(id);

        if (cliente != null && cabeleireiro != null) {
            return new Agendamento(id, cliente, cabeleireiro, procedimentos, dataHora, status, precoTotal);
        }
        return null;
    }

    private List<Procedimento> buscarProcedimentosDoAgendamento(int idAgendamento) throws SQLException {
        List<Procedimento> procedimentos = new ArrayList<>();
        String sql = "SELECT procedimento_id FROM agendamento_procedimentos WHERE agendamento_id = ?";
        try (Connection conexao = ConexaoDoBanco.criarConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {
            declaracao.setInt(1, idAgendamento);
            ResultSet resultado = declaracao.executeQuery();
            while (resultado.next()) {
                int idProcedimento = resultado.getInt("procedimento_id");
                Procedimento p = new ProcedimentoDAO().buscarPorId(idProcedimento);
                if (p != null) {
                    procedimentos.add(p);
                }
            }
        }
        return procedimentos;
    }

    public List<LocalTime> listarHorariosOcupados(int idCabeleireiro, LocalDate data) {
        List<LocalTime> horariosOcupados = new ArrayList<>();
        String sql = "SELECT data_hora FROM agendamentos WHERE cabeleireiro_id = ? AND date(data_hora) = ?";

        try (Connection conexao = ConexaoDoBanco.criarConexao();
                PreparedStatement declaracao = conexao.prepareStatement(sql)) {

            declaracao.setInt(1, idCabeleireiro);
            declaracao.setString(2, data.toString()); 

            ResultSet resultado = declaracao.executeQuery();

            while (resultado.next()) {
                LocalDateTime dataHora = LocalDateTime.parse(resultado.getString("data_hora"));
                horariosOcupados.add(dataHora.toLocalTime());
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar horários ocupados: " + e.getMessage());
        }
        return horariosOcupados;
    }
}