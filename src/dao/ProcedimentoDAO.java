package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import data.ConexaoDoBanco;
import model.Procedimento;

public class ProcedimentoDAO {
    
    public void salvar(Procedimento procedimento) {
        String sql = "INSERT INTO procedimento (nome, descricao, preco) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoDoBanco.criarConexao();
             PreparedStatement pt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pt.setString(1, procedimento.getNome());
            pt.setString(2, procedimento.getDescricao());
            pt.setDouble(3, procedimento.getPreco());

            pt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao salvar procedimento: " + e.getMessage());
        
        }
    }

    public Procedimento buscarPorId(int id) {
        String sql = "SELECT * FROM procedimento WHERE id = ?";

        try (Connection conn = ConexaoDoBanco.criarConexao();
             PreparedStatement pt = conn.prepareStatement(sql)) {

            pt.setInt(1, id);
            ResultSet rs = pt.executeQuery();

            if (rs.next()) {
                return new Procedimento(rs.getString("nome"), rs.getString("descricao"), rs.getDouble("preco"));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar procedimento por ID: " + e.getMessage());
        }
        return null;
    }

}
