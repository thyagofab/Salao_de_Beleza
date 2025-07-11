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
        
        try (Connection c = criarConexao();
             Statement st = c.createStatement()) {
            
            // Chamadas do método st.execute(sqlNomeDaTabela) para executar o SQL abaixo 
                
        } catch (SQLException e) {
            System.out.println("Erro ao iniciar o banco de dados: " + e.getMessage());
        }
    }
}
