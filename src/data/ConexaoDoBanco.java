package data;

import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoDoBanco {
    private Connection conexao;
    private Statement statement;  // usado para executar os comandos sql 
    
    ConexaoDoBanco(){
        try{

            this.conexao = DriverManager.getConnection("jdbc:sqlite:src/data/banco.db"); //Inicializando o banco de dados , caso não exista esse arquivo ai automaticamente é criado peloa SQLlite

            this.statement = conexao.createStatement(); //Inicializando o Statement que vai servi para executar os comandos SQL

            statement.setQueryTimeout(3); //Espera  só por 3 segundos para conectar


        }catch(SQLException e){
            System.out.println("Erro de inicialização!");
            System.out.println(e);
        }
    }
}
