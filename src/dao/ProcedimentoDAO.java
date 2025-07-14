package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;

import data.ConexaoDoBanco;
import model.Procedimento;

public class ProcedimentoDAO {
    
    public boolean salvar(Procedimento procedimento) {
        String sql = "INSERT INTO procedimento (nome, descricao, preco) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoDoBanco.criarConexao();
             PreparedStatement pt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pt.setString(1, procedimento.getNome());
            pt.setString(2, procedimento.getDescricao());
            pt.setDouble(3, procedimento.getPreco());

            return pt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar procedimento: " + e.getMessage());
            return false;
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

    public TreeMap <Integer, Procedimento> listarTodos(){
        String sql = "SELECT * FROM procedimento";
        TreeMap <Integer, Procedimento> procedimentos = new TreeMap<>();

        try (Connection conn = ConexaoDoBanco.criarConexao();
            PreparedStatement pt = conn.prepareStatement(sql)) {
            ResultSet rs = pt.executeQuery();
            
            while (rs.next()) {
                Procedimento procedimento = new Procedimento(rs.getString("nome"), rs.getString("descricao"), rs.getDouble("preco"));
                procedimentos.put(rs.getInt("id"), procedimento);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar procedimentos: " + e.getMessage());
        }
        return procedimentos;
    }   

     public boolean atualizar(Procedimento procedimento, int id) {
        String sql = "UPDATE procedimento SET nome = ?, descricao = ?, preco = ? WHERE id = ?";
        
        try (Connection conn = ConexaoDoBanco.criarConexao();
            PreparedStatement pt = conn.prepareStatement(sql)) {

            pt.setString(1, procedimento.getNome());
            pt.setString(2, procedimento.getDescricao());
            pt.setDouble(3, procedimento.getPreco());
            pt.setInt(4, id);

            return pt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar procedimento: " + e.getMessage());
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM procedimento WHERE id = ?";

        try (Connection conn = ConexaoDoBanco.criarConexao();
            PreparedStatement pt = conn.prepareStatement(sql)) {

            pt.setInt(1, id);
            return pt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir procedimento: " + e.getMessage());
            return false;
        }
    }
}