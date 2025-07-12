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
        // Tabela base 
        String sqlUsuario = "CREATE TABLE IF NOT EXISTS usuarios ("
                          + "idUsuario INTEGER PRIMARY KEY AUTOINCREMENT,"
                          + "nome TEXT NOT NULL,"
                          + "cpf TEXT NOT NULL UNIQUE,"
                          + "telefone TEXT,"
                          + "email TEXT NOT NULL UNIQUE,"
                          + "senha TEXT NOT NULL,"
                          + "tipo TEXT NOT NULL" 
                          + ");";

        // Tabela de Cliente com os atributos
        String sqlCliente = "CREATE TABLE IF NOT EXISTS clientes ("
                          + "usuario_id INTEGER PRIMARY KEY,"
                          + "data_nascimento TEXT,"
                          + "endereco TEXT,"
                          + "quantidade_agendamentos INTEGER DEFAULT 0,"
                          + "preferencias_horarios TEXT," 
                          + "ultima_visita TEXT,"
                          + "FOREIGN KEY (usuario_id) REFERENCES usuarios(idUsuario)"
                          + ");";

        // Tabela de Cabeleireiro com os atributos
        String sqlCabeleireiro = "CREATE TABLE IF NOT EXISTS cabeleireiros ("
                               + "usuario_id INTEGER PRIMARY KEY,"
                               + "especialidades TEXT,"        
                               + "media_avaliacoes REAL,"
                               + "total_avaliacoes INTEGER,"
                               + "dias_disponiveis TEXT,"      
                               + "horarios_disponiveis TEXT,"  
                               + "tempo_experiencia TEXT,"
                               + "FOREIGN KEY (usuario_id) REFERENCES usuarios(idUsuario)"
                               + ");";
        // Tabela de Procedimento com os atributos
        String sqlProcedimento = "CREATE TABLE IF NOT EXISTS procedimento ("
                             + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                             + "nome TEXT NOT NULL,"
                             + "descricao TEXT,"
                             + "preco REAL NOT NULL"
                             + ");";
        // Tabela de Agendamento com os atributos
        String sqlAgendamento = "CREATE TABLE IF NOT EXISTS agendamentos ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "cliente_id INTEGER NOT NULL,"
                            + "cabeleireiro_id INTEGER NOT NULL,"
                            + "procedimento_id INTEGER NOT NULL,"
                            + "data_hora TEXT NOT NULL,"
                            + "status TEXT NOT NULL,"
                            + "FOREIGN KEY (cliente_id) REFERENCES clientes(usuario_id),"
                            + "FOREIGN KEY (cabeleireiro_id) REFERENCES cabeleireiros(usuario_id),"
                            + "FOREIGN KEY (procedimento_id) REFERENCES procedimento(id)"
                            + ");";
        
        try (Connection c = criarConexao();
             Statement st = c.createStatement()) {
            
            st.execute(sqlUsuario);
            st.execute(sqlCliente);
            st.execute(sqlCabeleireiro);
            st.execute(sqlAgendamento);
            st.execute(sqlProcedimento);
            

            System.out.println("Banco de dados iniciado com sucesso.");
             
        } catch (SQLException e) {
            System.err.println("Erro ao iniciar o banco de dados: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}