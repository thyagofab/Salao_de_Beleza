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

import model.Cliente;

public class ClienteDAO {

    public void criaCliente(Cliente cliente, Connection conexao) throws SQLException {
        String instrucaoSql = "INSERT INTO clientes(usuario_id, data_nascimento, endereco, quantidade_agendamentos, ultima_visita, preferencias_horarios) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement instrucaoPreparadaDoSql = conexao.prepareStatement(instrucaoSql)) {
            instrucaoPreparadaDoSql.setInt(1, cliente.getIdUsuario());
            instrucaoPreparadaDoSql.setString(2, cliente.getDataNascimento().toString());
            instrucaoPreparadaDoSql.setString(3, cliente.getEndereco());
            instrucaoPreparadaDoSql.setInt(4, cliente.getQuantidadedeAgendamentos());

            if (cliente.getUltimaVisita() != null) {
                instrucaoPreparadaDoSql.setString(5, cliente.getUltimaVisita().toString());
            } else {
                instrucaoPreparadaDoSql.setNull(5, Types.VARCHAR);
            }

            String preferencias = String.join(",", cliente.getPreferenciasDeHorarios());
            instrucaoPreparadaDoSql.setString(6, preferencias);
            instrucaoPreparadaDoSql.executeUpdate();
        }
    }


    public Cliente buscaPorId(int idUsuario, Connection conexao) throws SQLException {
        String instrucaoSql = "SELECT u.*, c.data_nascimento, c.endereco, c.quantidade_agendamentos, c.ultima_visita, c.preferencias_horarios "
                + "FROM usuarios u JOIN clientes c ON u.idUsuario = c.usuario_id WHERE u.idUsuario = ?";

        try (PreparedStatement instrucaoPreparada = conexao.prepareStatement(instrucaoSql)) {
            instrucaoPreparada.setInt(1, idUsuario);
            try (ResultSet resultado = instrucaoPreparada.executeQuery()) {
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
                            System.err.println("Formato de data de nascimento inválido para o cliente ID " + id);
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
                            System.err.println("Aviso: Formato de última visita inválido para o cliente ID " + id
                                    + ". Valor: '" + ultimaVisitaStr + "'");
                        }
                    }

                    List<String> preferencias = new ArrayList<>();
                    String prefenciaStr = resultado.getString("preferencias_horarios");
                    if (prefenciaStr != null && !prefenciaStr.isEmpty()) {
                        preferencias.addAll(Arrays.asList(prefenciaStr.split(",")));
                    }

                    return new Cliente(id, nome, cpf, telefone, email, senha, dataDeNascimento, endereco,
                            quantidadeDeAgendamentos, preferencias, ultimaVisita);
                }
            }
        }

        return null;
    }

    public boolean atualizarDadosDoCliente(Cliente cliente, Connection conexao) throws SQLException {
        String instrucaoSql = "UPDATE clientes SET data_nascimento = ?, endereco = ?, preferencias_horarios = ? WHERE usuario_id = ?";

        try (PreparedStatement instrucaoPreparada = conexao.prepareStatement(instrucaoSql)) {
            instrucaoPreparada.setString(1, cliente.getDataNascimento().toString());
            instrucaoPreparada.setString(2, cliente.getEndereco());

            //transformo um lista horario e uma só String, assim usando a virgula de separador da possiao da lista
            String preferenciasDeHorarios = String.join(",", cliente.getPreferenciasDeHorarios());

            instrucaoPreparada.setString(3, preferenciasDeHorarios);
            instrucaoPreparada.setInt(4, cliente.getIdUsuario());

            return instrucaoPreparada.executeUpdate() > 0;
        }
    }


    public boolean delatarDadosDoCliente(int idUsuario, Connection conexao) throws SQLException {
        String instrucaoSql = "DELETE FROM clientes WHERE usuario_id = ?";

        try (PreparedStatement instrucaoPreparada = conexao.prepareStatement(instrucaoSql)) {
            instrucaoPreparada.setInt(1, idUsuario);

            return instrucaoPreparada.executeUpdate() > 0;
        }

    }


    public List<Cliente> listarTodosClientes(Connection conexao) throws SQLException {
        List<Cliente> listaDeClientes = new ArrayList<>();
        String instrucaoSql = "SELECT u.*, c.data_nascimento, c.endereco, c.quantidade_agendamentos, c.ultima_visita, c.preferencias_horarios " +
        "FROM usuarios u JOIN clientes c ON u.idUsuario = c.usuario_id";

        try (PreparedStatement instrucaoPreparada = conexao.prepareStatement(instrucaoSql)) {
            ResultSet resultado = instrucaoPreparada.executeQuery();

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
                
                if (preferenciasStr != null) {
                    preferenciasDeHorarios.addAll(Arrays.asList(preferenciasStr.split(",")));
                }

                LocalDate ultimaVisita = null;
                String ultimaVisitaStr = resultado.getString("ultima_visita");
                if (ultimaVisitaStr != null) {
                    ultimaVisita = LocalDate.parse(ultimaVisitaStr);
                }
                Cliente cliente = new Cliente(id, nome, cpf, telefone, email, senha, dataNascimento, endereco, quantidadeAgendamentos, preferenciasDeHorarios, ultimaVisita);

                listaDeClientes.add(cliente);
            }
        }
        return listaDeClientes;
    }

}