package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import data.ConexaoDoBanco;
import model.Cabeleireiro;

public class CabeleireiroDAO {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public boolean salvarDadosCabeleireiroNoBanco(Cabeleireiro cabeleireiro) {
        Connection conexao = null;
        String instrucaoSql = "INSERT INTO cabeleireiros (usuario_id, especialidades, media_avaliacoes, total_avaliacoes, dias_disponiveis, horarios_disponiveis, tempo_experiencia) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            conexao = ConexaoDoBanco.criarConexao();
            conexao.setAutoCommit(false);

            int idGerado = usuarioDAO.criarUsuarioCabeleireiro(cabeleireiro, conexao);
            cabeleireiro.setIdUsuario(idGerado);

            try (PreparedStatement instrucaoPreparada = conexao.prepareStatement(instrucaoSql)) {
                instrucaoPreparada.setInt(1, cabeleireiro.getIdUsuario());
                instrucaoPreparada.setString(2, String.join(",", cabeleireiro.getEspecialidades()));
                instrucaoPreparada.setDouble(3, cabeleireiro.getMediaDeAvaliacoes());
                instrucaoPreparada.setInt(4, cabeleireiro.getTotalDeAvaliacoes());
                instrucaoPreparada.setString(5, String.join(",", cabeleireiro.getDiasDisponiveis()));
                instrucaoPreparada.setString(6, converterListaLocalTimeParaString(cabeleireiro.getHorariosDisponiveis()));
                instrucaoPreparada.setString(7, cabeleireiro.getTempoDeExperiencia());

                int linhaAlterada = instrucaoPreparada.executeUpdate();
                if (linhaAlterada == 0) {
                    throw new SQLException("Falha ao salvar dados complementares do cabeleireiro!");
                }
            }

            conexao.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro na transação ao salvar cabeleireiro: " + e.getMessage());
            try {
                if (conexao != null) {
                    conexao.rollback();
                    System.err.println("Rollback de transação devido a erro SQL ao salvar cabeleireiro.");
                }
            } catch (SQLException ex) {
                System.err.println("ERRO ao executar ROLLBACK: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

    public Cabeleireiro buscarPorId(int id) {
        Connection conexao = null;
        String instrucaoSql = "SELECT u.*, c.* "
                + "FROM usuarios u JOIN cabeleireiros c ON u.idUsuario = c.usuario_id WHERE u.idUsuario = ?";

        try {
            conexao = ConexaoDoBanco.criarConexao();
            try (PreparedStatement instrucaoPreparadaSql = conexao.prepareStatement(instrucaoSql)) {
                instrucaoPreparadaSql.setInt(1, id);

                try (ResultSet resultado = instrucaoPreparadaSql.executeQuery()) {
                    if (resultado.next()) {
                        return montarCabeleireiro(resultado);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cabeleireiro por ID: " + e.getMessage(), e);
        } finally {
            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
        return null;
    }

    public TreeMap<Integer, Cabeleireiro> listarTodos() {
        Connection conexao = null;
        String instrucaoSql = "SELECT u.*, c.* "
                + "FROM usuarios u JOIN cabeleireiros c ON u.idUsuario = c.usuario_id";
        TreeMap<Integer, Cabeleireiro> map = new TreeMap<>();

        try {
            conexao = ConexaoDoBanco.criarConexao();
            try (PreparedStatement instrucaoPreparadaSql = conexao.prepareStatement(instrucaoSql);
                 ResultSet resultado = instrucaoPreparadaSql.executeQuery()) {

                while (resultado.next()) {
                    Cabeleireiro cab = montarCabeleireiro(resultado);
                    map.put(cab.getIdUsuario(), cab);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar cabeleireiros: " + e.getMessage());
        } finally {
            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
        return map;
    }

    public boolean atualizarDadosDoCabeleireiro(Cabeleireiro cabeleireiro) {
        Connection conexao = null;
        String instrucaoSqlCabeleireiro = "UPDATE cabeleireiros SET especialidades = ?, media_avaliacoes = ?, total_avaliacoes = ?, "
                                        + "dias_disponiveis = ?, horarios_disponiveis = ?, tempo_experiencia = ? "
                                        + "WHERE usuario_id = ?";

        try {
            conexao = ConexaoDoBanco.criarConexao();
            conexao.setAutoCommit(false);

            boolean sucessoUsuario = usuarioDAO.atualizarDados(cabeleireiro, conexao);

            boolean sucessoCabeleireiro = false;
            try (PreparedStatement instrucaoPreparadaSql = conexao.prepareStatement(instrucaoSqlCabeleireiro)) {
                instrucaoPreparadaSql.setString(1, String.join(",", cabeleireiro.getEspecialidades()));
                instrucaoPreparadaSql.setDouble(2, cabeleireiro.getMediaDeAvaliacoes());
                instrucaoPreparadaSql.setInt(3, cabeleireiro.getTotalDeAvaliacoes());
                instrucaoPreparadaSql.setString(4, String.join(",", cabeleireiro.getDiasDisponiveis()));
                instrucaoPreparadaSql.setString(5, converterListaLocalTimeParaString(cabeleireiro.getHorariosDisponiveis()));
                instrucaoPreparadaSql.setString(6, cabeleireiro.getTempoDeExperiencia());
                instrucaoPreparadaSql.setInt(7, cabeleireiro.getIdUsuario());

                if (instrucaoPreparadaSql.executeUpdate() > 0) {
                    sucessoCabeleireiro = true;
                }
            }

            if (sucessoUsuario && sucessoCabeleireiro) {
                conexao.commit();
                return true;
            } else {
                conexao.rollback();
                System.err.println("Falha na atualização dos dados do cabeleireiro ou usuário. Rollback realizado.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("ERRO DE BANCO DE DADOS ao atualizar cabeleireiro: " + e.getMessage());
            try {
                if (conexao != null) {
                    conexao.rollback();
                    System.err.println("Rollback de transação devido a erro SQL.");
                }
            } catch (SQLException ex) {
                System.err.println("ERRO ao executar ROLLBACK: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar a conexão: " + ex.getMessage());
            }
        }
    }

    public boolean deletarDadosDoCabeleireiro(int id) {
        Connection conexao = null;
        String sqlCabeleireiro = "DELETE FROM cabeleireiros WHERE usuario_id = ?";

        try {
            conexao = ConexaoDoBanco.criarConexao();
            conexao.setAutoCommit(false);

            boolean cabeleireiroDeletado = false;
            try (PreparedStatement pt = conexao.prepareStatement(sqlCabeleireiro)) {
                pt.setInt(1, id);
                if (pt.executeUpdate() > 0) {
                    cabeleireiroDeletado = true;
                }
            }

            boolean usuarioDeletado = usuarioDAO.deletarDadosDoUsuario(id, conexao);

            if (cabeleireiroDeletado && usuarioDeletado) {
                conexao.commit();
                return true;
            } else {
                System.err.println("Falha ao deletar cabeleireiro ou usuário associado. Rollback realizado.");
                conexao.rollback();
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao deletar cabeleireiro: " + e.getMessage());
            try {
                if (conexao != null) {
                    conexao.rollback();
                    System.err.println("Rollback de transação devido a erro SQL ao deletar cabeleireiro.");
                }
            } catch (SQLException ex) {
                System.err.println("ERRO ao executar ROLLBACK: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

    private Cabeleireiro montarCabeleireiro(ResultSet rs) throws SQLException {
        int id = rs.getInt("idUsuario");
        String nome = rs.getString("nome");
        String cpf = rs.getString("cpf");
        String telefone = rs.getString("telefone");
        String email = rs.getString("email");
        String senha = rs.getString("senha");

        List<String> especialidades = new ArrayList<>();
        String especialidadesStr = rs.getString("especialidades");
        if (especialidadesStr != null && !especialidadesStr.isEmpty()) {
            especialidades = Arrays.asList(especialidadesStr.split(","));
        }

        double mediaAvaliacoes = rs.getDouble("media_avaliacoes");
        int totalAvaliacoes = rs.getInt("total_avaliacoes");

        List<String> diasDisponiveis = new ArrayList<>();
        String diasDisponiveisStr = rs.getString("dias_disponiveis");
        if (diasDisponiveisStr != null && !diasDisponiveisStr.isEmpty()) {
            diasDisponiveis = Arrays.asList(diasDisponiveisStr.split(","));
        }

        List<LocalTime> horariosDisponiveis = new ArrayList<>();
        String horariosDisponiveisStr = rs.getString("horarios_disponiveis");
        if (horariosDisponiveisStr != null && !horariosDisponiveisStr.isEmpty()) {
            try {
                horariosDisponiveis = converterStringParaListaLocalTime(horariosDisponiveisStr);
            } catch (DateTimeParseException e) {
                System.err.println("Formato de horários disponíveis inválido para cabeleireiro " + nome + ": " + e.getMessage());
                horariosDisponiveis = new ArrayList<>();
            }
        }

        String tempoExperiencia = rs.getString("tempo_experiencia");

        return new Cabeleireiro(id, nome, cpf, telefone, email, senha,
                especialidades, mediaAvaliacoes, totalAvaliacoes, diasDisponiveis, horariosDisponiveis, tempoExperiencia);
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

    private List<LocalTime> converterStringParaListaLocalTime(String str) throws DateTimeParseException {
        List<LocalTime> lista = new ArrayList<>();
        if (str != null && !str.isEmpty()) {
            String[] partes = str.split(",");
            for (String p : partes) {
                lista.add(LocalTime.parse(p.trim()));
            }
        }
        return lista;
    }
}