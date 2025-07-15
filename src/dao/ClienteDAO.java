package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import data.ConexaoDoBanco;
import model.Cliente;

public class ClienteDAO {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public boolean salvarDadosClienteNoBanco(Cliente cliente) {
        Connection conexao = null;
        String instrucaoSql = "INSERT INTO clientes(usuario_id, data_nascimento, endereco, quantidade_agendamentos, ultima_visita, preferencias_horarios) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            conexao = ConexaoDoBanco.criarConexao();
            conexao.setAutoCommit(false);

            int idGerado = usuarioDAO.criarUsuarioCliente(cliente, conexao);
            cliente.setIdUsuario(idGerado);

            try (PreparedStatement instrucaoPreparada = conexao.prepareStatement(instrucaoSql)) {
                instrucaoPreparada.setInt(1, cliente.getIdUsuario());
                instrucaoPreparada.setString(2, cliente.getDataNascimento().toString());
                instrucaoPreparada.setString(3, cliente.getEndereco());
                instrucaoPreparada.setInt(4, 0);
                instrucaoPreparada.setNull(5, Types.DATE);

                String preferenciasDeHorarios = String.join(",", cliente.getPreferenciasDeHorarios());
                instrucaoPreparada.setString(6, preferenciasDeHorarios);

                int linhaAlterada = instrucaoPreparada.executeUpdate();
                if (linhaAlterada == 0) {
                    throw new SQLException("Falha ao salvar dados complementares do cliente!");
                }
            }

            conexao.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro na transação ao salvar cliente: " + e.getMessage());
            try {
                if (conexao != null) {
                    conexao.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("ERRO ROLLBACK: " + ex.getMessage());
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

    public Cliente buscaPorId(int idUsuario) {
        Connection conexao = null;
        String instrucaoSql = "SELECT u.*, c.data_nascimento, c.endereco, c.quantidade_agendamentos, c.ultima_visita, c.preferencias_horarios "
        + "FROM usuarios u JOIN clientes c ON u.idUsuario = c.usuario_id WHERE u.idUsuario = ?";

        try {
            conexao = ConexaoDoBanco.criarConexao();
            try (PreparedStatement instrucaoPreparadaSql = conexao.prepareStatement(instrucaoSql)) {
                instrucaoPreparadaSql.setInt(1, idUsuario);

                try (ResultSet resultado = instrucaoPreparadaSql.executeQuery()) {
                    if (resultado.next()) {
                        int id = resultado.getInt("idUsuario");
                        String nome = resultado.getString("nome");
                        String cpf = resultado.getString("cpf");
                        String telefone = resultado.getString("telefone");
                        String email = resultado.getString("email");
                        String senha = resultado.getString("senha");

                        String dataNascimentoStr = resultado.getString("data_nascimento");
                        LocalDate dataDeNascimento = null;

                        if (dataNascimentoStr != null && !dataNascimentoStr.isEmpty()) {
                            try {
                                dataDeNascimento = LocalDate.parse(dataNascimentoStr);
                            } catch (DateTimeParseException e) {
                                System.err.println("Formato de data de nascimento inválido");
                            }
                        }

                        String endereco = resultado.getString("endereco");
                        int quantidadeDeAgendamentos = resultado.getInt("quantidade_agendamentos");

                        String ultimaVisitaStr = resultado.getString("ultima_visita");
                        LocalDate ultimaVisita = null;
                        if (ultimaVisitaStr != null && !ultimaVisitaStr.isEmpty()) {
                            try {
                                ultimaVisita = LocalDate.parse(ultimaVisitaStr);
                            } catch (DateTimeParseException e) {
                                System.err.println("Formato de última visita inválido");
                            }
                        }

                        List<String> preferenciasDeHorarios = new ArrayList<>();
                        String prefenciaStr = resultado.getString("preferencias_horarios");
                        
                        if (prefenciaStr != null && !prefenciaStr.isEmpty()) {
                            preferenciasDeHorarios.addAll(Arrays.asList(prefenciaStr.split(",")));
                        }

                        return new Cliente(id, nome, cpf, telefone, email, senha, dataDeNascimento, endereco,
                                quantidadeDeAgendamentos, preferenciasDeHorarios, ultimaVisita);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (conexao != null)
                    conexao.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
        return null;
    }

    public boolean atualizarDadosDoCliente(Cliente clienteParaAtualizar) {
        Connection conexao = null;
        String instrucaoSql = "UPDATE clientes SET data_nascimento = ?, endereco = ?, preferencias_horarios = ? WHERE usuario_id = ?";

        try {
            conexao = ConexaoDoBanco.criarConexao();
            conexao.setAutoCommit(false); 

            boolean sucessoUsuario = usuarioDAO.atualizarDados(clienteParaAtualizar, conexao);

            boolean sucessoCliente = false;

            try (PreparedStatement instrucaoPreparadaSql = conexao.prepareStatement(instrucaoSql)) {
                instrucaoPreparadaSql.setString(1, clienteParaAtualizar.getDataNascimento().toString());
                instrucaoPreparadaSql.setString(2, clienteParaAtualizar.getEndereco());
                String preferenciasDeHorarios = String.join(",", clienteParaAtualizar.getPreferenciasDeHorarios());
                instrucaoPreparadaSql.setString(3, preferenciasDeHorarios);
                instrucaoPreparadaSql.setInt(4, clienteParaAtualizar.getIdUsuario());

                if (instrucaoPreparadaSql.executeUpdate() > 0) {
                    sucessoCliente = true;
                }
            }

            if (sucessoUsuario || sucessoCliente) {
                conexao.commit();
                return true;
            } else {
                conexao.rollback();
                return false;
            }

        } catch (SQLException e) {
            System.err.println("ERRO DE BANCO DE DADOS: " + e.getMessage());
            try {
                if (conexao != null)
                    conexao.rollback();
            } catch (SQLException ex) {
                System.err.println("ERRO ROLLBACK: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conexao != null)
                    conexao.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar a conexão: " + ex.getMessage());
            }
        }
    }

    public boolean deletarDadosDoCliente(int idCliente) {
        Connection conexao = null;
        String instrucaoSqlClientes = "DELETE FROM clientes WHERE usuario_id = ?";

        try {
            conexao = ConexaoDoBanco.criarConexao();
            conexao.setAutoCommit(false); 

            boolean deletarCliente = false;
            try (PreparedStatement instrucaoPreparada = conexao.prepareStatement(instrucaoSqlClientes)) {
                instrucaoPreparada.setInt(1, idCliente);
                if (instrucaoPreparada.executeUpdate() > 0) {
                    deletarCliente = true;
                }
            }

            boolean deletarUsuario = usuarioDAO.deletarDadosDoUsuario(idCliente, conexao);

            if (deletarCliente && deletarUsuario) {
                conexao.commit();
                return true;
            } else {
                System.err.println("Falha ao deletar dados!");
                conexao.rollback();
                return false;
            }

        } catch (SQLException e) {
            System.err.println("ERRO DE BANCO DE DADOS");
            try {
                if (conexao != null)
                    conexao.rollback();
            } catch (SQLException ex) {
                System.err.println("ERRO ROLLBACK: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conexao != null)
                    conexao.close();
            } catch (SQLException ex) {
                System.err.println("Erro ao fechar a conexão: " + ex.getMessage());
            }
        }
    }

    public List<Cliente> listarTodos() {
        List<Cliente> listaDeClientes = new ArrayList<>();
        String instrucaoSql = "SELECT u.*, c.data_nascimento, c.endereco, c.quantidade_agendamentos, c.ultima_visita, c.preferencias_horarios "
                + "FROM usuarios u JOIN clientes c ON u.idUsuario = c.usuario_id";

        try (Connection conexao = ConexaoDoBanco.criarConexao();
                PreparedStatement instrucaoPreparadaSql = conexao.prepareStatement(instrucaoSql);
                ResultSet resultado = instrucaoPreparadaSql.executeQuery()) {

            while (resultado.next()) {
                int id = resultado.getInt("idUsuario");
                String nome = resultado.getString("nome");
                String cpf = resultado.getString("cpf");
                String telefone = resultado.getString("telefone");
                String email = resultado.getString("email");
                String senha = resultado.getString("senha");

                LocalDate dataNascimento = null;
                String dataNascimentoStr = resultado.getString("data_nascimento");
                if (dataNascimentoStr != null) {
                    dataNascimento = LocalDate.parse(dataNascimentoStr);
                }

                String endereco = resultado.getString("endereco");
                int quantidadeAgendamentos = resultado.getInt("quantidade_agendamentos");

                List<String> preferenciasDeHorarios = new ArrayList<>();
                
                String preferenciasStr = resultado.getString("preferencias_horarios");
                
                if (preferenciasStr != null && !preferenciasStr.isEmpty()) {
                    preferenciasDeHorarios.addAll(Arrays.asList(preferenciasStr.split(",")));
                }

                LocalDate ultimaVisita = null;
                String ultimaVisitaStr = resultado.getString("ultima_visita");
                if (ultimaVisitaStr != null) {
                    ultimaVisita = LocalDate.parse(ultimaVisitaStr);
                }

                Cliente cliente = new Cliente(id, nome, cpf, telefone, email, senha, dataNascimento, endereco,
                        quantidadeAgendamentos, preferenciasDeHorarios, ultimaVisita);

                listaDeClientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar clientes: " + e.getMessage());
        }
        return listaDeClientes;
    }

    public boolean atualizarUltimaVisita(int idUsuario, LocalDate dataVisita) {
        String instrucaoSql = "UPDATE clientes SET ultima_visita = ?, quantidade_agendamentos = quantidade_agendamentos + 1 WHERE usuario_id = ?";

        try (Connection conexao = ConexaoDoBanco.criarConexao()) {
            conexao.setAutoCommit(false);

            try (PreparedStatement instrucaoPreparadaSql = conexao.prepareStatement(instrucaoSql)) {

                instrucaoPreparadaSql.setString(1, dataVisita.toString());
                instrucaoPreparadaSql.setInt(2, idUsuario);

                int linhasAlteradas = instrucaoPreparadaSql.executeUpdate();
                conexao.commit();
                return linhasAlteradas > 0;
            }

        } catch (SQLException e) {
            System.err.println("ERRO DE BANCO DE DADOS: " + e.getMessage());
            return false;
        }
    }

}