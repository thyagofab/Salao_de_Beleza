package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.Usuario;

public class UsuarioDAO {

    public int criarUsuarioCliente(Usuario usuario, Connection conexao) throws SQLException {
        String instrucaoSql = "INSERT INTO usuarios(nome, cpf, telefone, email, senha, tipo) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement instrucaoPreparada = conexao.prepareStatement(instrucaoSql,
                Statement.RETURN_GENERATED_KEYS)) {
            instrucaoPreparada.setString(1, usuario.getNome());
            instrucaoPreparada.setString(2, usuario.getCpf());
            instrucaoPreparada.setString(3, usuario.getTelefone());
            instrucaoPreparada.setString(4, usuario.getEmail());
            instrucaoPreparada.setString(5, usuario.getSenha());
            instrucaoPreparada.setString(6, "Cliente");

            int linhasAfetadas = instrucaoPreparada.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new SQLException("Falha ao criar usuário, nenhuma linha afetada.");
            }

            try (ResultSet resultado = instrucaoPreparada.getGeneratedKeys()) {
                if (resultado.next()) {
                    return resultado.getInt(1);
                } else {
                    throw new SQLException("Falha ao obter o ID do usuário.");
                }
            }
        }
    }

    public boolean atualizarDados(Usuario usuario, Connection conexao) throws SQLException {
        String instrucaoSql = "UPDATE usuarios SET nome = ?, cpf = ?, telefone = ?, email = ?, senha = ? WHERE idUsuario = ?";

        try (PreparedStatement instrucaoPreparada = conexao.prepareStatement(instrucaoSql)) {
            instrucaoPreparada.setString(1, usuario.getNome());
            instrucaoPreparada.setString(2, usuario.getCpf());
            instrucaoPreparada.setString(3, usuario.getTelefone());
            instrucaoPreparada.setString(4, usuario.getEmail());
            instrucaoPreparada.setString(5, usuario.getSenha());
            instrucaoPreparada.setInt(6, usuario.getIdUsuario());

            return instrucaoPreparada.executeUpdate() > 0;
        }
    }

    public boolean deletarDadosDoUsuario(int idUsuario, Connection conexao) throws SQLException {
        String instrucaoSql = "DELETE FROM usuarios WHERE idUsuario = ?";
        try (PreparedStatement instrucaoPreparada = conexao.prepareStatement(instrucaoSql)) {
            instrucaoPreparada.setInt(1, idUsuario);
            return instrucaoPreparada.executeUpdate() > 0;
        }
    }

    public boolean cpfJaExisteParaOutroUsuario(String cpf, int idUsuarioAtual, Connection conexao) throws SQLException {
        String instrucaoSql = "SELECT idUsuario FROM usuarios WHERE cpf = ? AND idUsuario != ?";
        try (PreparedStatement instrucaoPreparada = conexao.prepareStatement(instrucaoSql)) {
            instrucaoPreparada.setString(1, cpf);
            instrucaoPreparada.setInt(2, idUsuarioAtual);
            try (ResultSet resultado = instrucaoPreparada.executeQuery()) {
                return resultado.next();
            }
        }
    }

}