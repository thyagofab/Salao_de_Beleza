package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import data.ConexaoDoBanco;
import model.Cabeleireiro;

public class CabeleireiroDAO {

    public boolean salvar(Cabeleireiro cabeleireiro) {
        String sql = "INSERT INTO cabeleireiros (usuario_id, especialidades, media_avaliacoes, total_avaliacoes, dias_disponiveis, horarios_disponiveis, tempo_experiencia) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoDoBanco.criarConexao();
             PreparedStatement pt = conn.prepareStatement(sql)) {

            pt.setInt(1, cabeleireiro.getIdUsuario());
            pt.setString(2, String.join(",", cabeleireiro.getEspecialidades()));
            pt.setDouble(3, cabeleireiro.getMediaDeAvaliacoes());
            pt.setInt(4, cabeleireiro.getTotalDeAvaliacoes());
            pt.setString(5, String.join(",", cabeleireiro.getDiasDisponiveis()));
            pt.setString(6, converterListaLocalTimeParaString(cabeleireiro.getHorariosDisponiveis()));
            pt.setString(7, cabeleireiro.getTempoDeExperiencia());

            return pt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar cabeleireiro: " + e.getMessage());
            return false;
        }
    }

    public Cabeleireiro buscarPorId(int id) {
        String sql = "SELECT u.*, c.* "
                   + "FROM usuarios u JOIN cabeleireiros c ON u.idUsuario = c.usuario_id WHERE u.idUsuario = ?";

        try (Connection conn = ConexaoDoBanco.criarConexao();
             PreparedStatement pt = conn.prepareStatement(sql)) {

            pt.setInt(1, id);
            ResultSet rs = pt.executeQuery();

            if (rs.next()) {
                return montarCabeleireiro(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar cabeleireiro por ID: " + e.getMessage());
        }
        return null;
    }

    public TreeMap<Integer, Cabeleireiro> listarTodos() {
        String sql = "SELECT u.*, c.* "
                   + "FROM usuarios u JOIN cabeleireiros c ON u.idUsuario = c.usuario_id";
        TreeMap<Integer, Cabeleireiro> map = new TreeMap<>();

        try (Connection conn = ConexaoDoBanco.criarConexao();
             PreparedStatement pt = conn.prepareStatement(sql)) {

            ResultSet rs = pt.executeQuery();

            while (rs.next()) {
                Cabeleireiro cab = montarCabeleireiro(rs);
                map.put(cab.getIdUsuario(), cab);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar cabeleireiros: " + e.getMessage());
        }
        return map;
    }

    public boolean atualizar(Cabeleireiro cabeleireiro, int id) {
        String sql = "UPDATE cabeleireiros SET especialidades = ?, media_avaliacoes = ?, total_avaliacoes = ?, "
                   + "dias_disponiveis = ?, horarios_disponiveis = ?, tempo_experiencia = ? "
                   + "WHERE usuario_id = ?";

        try (Connection conn = ConexaoDoBanco.criarConexao();
             PreparedStatement pt = conn.prepareStatement(sql)) {

            pt.setString(1, String.join(",", cabeleireiro.getEspecialidades()));
            pt.setDouble(2, cabeleireiro.getMediaDeAvaliacoes());
            pt.setInt(3, cabeleireiro.getTotalDeAvaliacoes());
            pt.setString(4, String.join(",", cabeleireiro.getDiasDisponiveis()));
            pt.setString(5, converterListaLocalTimeParaString(cabeleireiro.getHorariosDisponiveis()));
            pt.setString(6, cabeleireiro.getTempoDeExperiencia());
            pt.setInt(7, id);

            return pt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar cabeleireiro: " + e.getMessage());
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM cabeleireiros WHERE usuario_id = ?";

        try (Connection conn = ConexaoDoBanco.criarConexao();
             PreparedStatement pt = conn.prepareStatement(sql)) {

            pt.setInt(1, id);
            return pt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar cabeleireiro: " + e.getMessage());
            return false;
        }
    }

    private Cabeleireiro montarCabeleireiro(ResultSet rs) throws SQLException {
        int id = rs.getInt("idUsuario");
        String nome = rs.getString("nome");
        String cpf = rs.getString("cpf");
        String telefone = rs.getString("telefone");
        String email = rs.getString("email");
        String senha = rs.getString("senha");

        List<String> especialidades = Arrays.asList(rs.getString("especialidades").split(","));
        double mediaAvaliacoes = rs.getDouble("media_avaliacoes");
        int totalAvaliacoes = rs.getInt("total_avaliacoes");
        List<String> diasDisponiveis = Arrays.asList(rs.getString("dias_disponiveis").split(","));
        List<LocalTime> horariosDisponiveis = converterStringParaListaLocalTime(rs.getString("horarios_disponiveis"));
        String tempoExperiencia = rs.getString("tempo_experiencia");

        return new Cabeleireiro(id, nome, cpf, telefone, email, senha,
                especialidades, mediaAvaliacoes, totalAvaliacoes, diasDisponiveis, horariosDisponiveis, tempoExperiencia);
    }

    private String converterListaLocalTimeParaString(List<LocalTime> lista) {
        List<String> strList = new ArrayList<>();
        for (LocalTime time : lista) {
            strList.add(time.toString());
        }
        return String.join(",", strList);
    }

    private List<LocalTime> converterStringParaListaLocalTime(String str) {
        List<LocalTime> lista = new ArrayList<>();
        if (str != null && !str.isEmpty()) {
            String[] partes = str.split(",");
            for (String p : partes) {
                lista.add(LocalTime.parse(p));
            }
        }
        return lista;
    }
}