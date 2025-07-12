package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoDoBanco {
    private static final String endereco = "jdbc:sqlite:src/data/banco.db"; 

    public static Connection criarConexao() throws SQLException {
        return DriverManager.getConnection(endereco);
    }

    public static void iniciarBanco() {
       
        // Declaração das variáveis para criar as tabelas. String sqlNomeDaTabela = "..."
        String sqlProcedimento = "CREATE TABLE IF NOT EXISTS procedimento (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "descricao TEXT, " +
                "preco REAL NOT NULL);";
            
        try (Connection c = criarConexao();
             Statement st = c.createStatement()) {
            
            // Chamadas do método st.execute(sqlNomeDaTabela) para executar o SQL abaixo 
            st.execute(sqlProcedimento);

        } catch (SQLException e) {
            System.out.println("Erro ao iniciar o banco de dados: " + e.getMessage());
        }
    }
}
