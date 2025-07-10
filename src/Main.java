import data.ConexaoDoBanco; 

public class Main {
    public static void main(String[] args) {

        try {
            ConexaoDoBanco.iniciarBanco();
            System.out.println("Banco de dados iniciado com sucesso.");
            
        } catch (Exception e) {
            System.err.println("Erro ao iniciar o banco de dados: " + e.getMessage());
        }
    }
}