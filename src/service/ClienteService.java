package service;

import dao.ClienteDAO;
import dao.UsuarioDAO;
import data.ConexaoDoBanco;
import model.Cliente;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClienteService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void criarCliente(Scanner leitor) {
        System.out.print("Digite o Nome: ");
        String nome = leitor.nextLine();
        System.out.print("Digite o CPF: ");
        String cpf = leitor.nextLine();
        System.out.print("Digite o Telefone: ");
        String telefone = leitor.nextLine();
        System.out.print("Digite o Email: ");
        String email = leitor.nextLine();
        System.out.print("Digite a Senha: ");
        String senha = leitor.nextLine();
        System.out.print("Digite o Endereço: ");
        String endereco = leitor.nextLine();
        System.out.print("Data de Nascimento (dd/mm/aaaa): ");
        LocalDate dataNascimento = LocalDate.parse(leitor.nextLine(), dateFormatter);
        System.out.print("Preferências de Horários (ex: Manhã - 10:00,Tarde - 14:00): ");
        List<String> preferenciasDeHorarios = new ArrayList<>(Arrays.asList(leitor.nextLine().split(",")));

        Connection conexao = null;
        try {
            conexao = ConexaoDoBanco.criarConexao();
            conexao.setAutoCommit(false);
            Cliente novoCliente = new Cliente(0, nome, cpf, telefone, email, senha, dataNascimento, endereco, 0,
                    preferenciasDeHorarios, null);

            //crio novo usuario e gero um id
            int idGerado = usuarioDAO.criarUsuarioCliente(novoCliente, conexao);


            //passo por objeto
            novoCliente.setIdUsuario(idGerado);

            clienteDAO.criaCliente(novoCliente, conexao);

            conexao.commit();
            System.err.println("Cliente cadastrado com sucesso!");

        } catch (SQLException e) {
            System.err.println("ERRO DE BANCO DE DADOS");
            try {
                if (conexao != null)
                    conexao.rollback();
            } catch (SQLException ex) {
                System.err.println("Erro grave no sistema. O cadastro pode estar incompleto");

            }

        } finally {
            try {
                if (conexao != null)
                    conexao.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

    public void consultarCLiente(Scanner leitor) {
        System.out.print("Digite o ID do cliente que deseja consultar: ");
        int id = leitor.nextInt();
        leitor.nextLine();

        Connection conexao = null;

        try {
            conexao = ConexaoDoBanco.criarConexao();

            Cliente clienteEncontrado = clienteDAO.buscaPorId(id, conexao);

            if (clienteEncontrado != null) {

                System.out.println(clienteEncontrado.toString());

            } else {
                System.out.println("Nenhum cliente encontrado com o ID " + id);
            }
        } catch (SQLException e) {
            System.err.println("ERRO DE BANCO DE DADOS");
        } finally {
            try {
                if (conexao != null)
                    conexao.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

    public void atualizarCliente(Scanner leitor) {
        System.out.print("Digite o ID do cliente que deseja atualizar: ");
        int id = leitor.nextInt();
        leitor.nextLine();

        Connection conexao = null;
        try {
            conexao = ConexaoDoBanco.criarConexao();
            Cliente clienteExistente = clienteDAO.buscaPorId(id, conexao);

            if (clienteExistente == null) {
                System.out.println("Cliente não encontrado.");
                return;
            }

            System.out.println("Cliente encontrado: " + clienteExistente.getNome() + ". Digite os novos dados:");

            System.out.print("Digite o nome: ");
            String nome = leitor.nextLine();
            if (!nome.isEmpty()) {
                clienteExistente.setNome(nome);
            }
            System.out.print("Digite o CPF: ");
            String cpf = leitor.nextLine();

            if (!cpf.isEmpty()) {
                if (usuarioDAO.cpfJaExisteParaOutroUsuario(cpf, id, conexao)) {
                    System.out.println("Erro: Este CPF já está cadastrado para outro cliente. A atualização do CPF foi ignorada.");
                } else {
                    clienteExistente.setCpf(cpf);
                }
            }

            System.out.print("Digite o Telefone: ");
            String telefone = leitor.nextLine();
            if (!telefone.isEmpty()) {
                clienteExistente.setTelefone(telefone);
            }

            System.out.print("Digite o Email: ");
            String email = leitor.nextLine();
            if (!email.isEmpty()) {
                clienteExistente.setEmail(email);
            }

            System.out.print("Digite a Senha: ");
            String senha = leitor.nextLine();

            if (!senha.isEmpty()) {
                clienteExistente.setSenha(senha);
            }

            System.out.print("Digite o Endereço: ");
            String endereco = leitor.nextLine();

            if (!endereco.isEmpty()) {
                clienteExistente.setEndereco(endereco);
            }

            System.out.print("Data de Nascimento (dd/mm/aaaa): ");
            String dataNascimentoStr = leitor.nextLine();
            if (!dataNascimentoStr.isEmpty()) {
                try {
                    LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr, dateFormatter);
                    clienteExistente.setDataNascimento(dataNascimento);
                } catch (Exception e) {
                    System.err.println("Formato de data inválido");
                }
            }

            System.out.print("Preferências de Horários (ex: Manhã - 10:00,Tarde - 14:00): ");
            String preferenciasStr = leitor.nextLine();

            if (!preferenciasStr.isEmpty()) {
                List<String> preferencias = new ArrayList<>(Arrays.asList(preferenciasStr.split(",")));
                clienteExistente.setPreferenciasDeHorarios(preferencias);
            }

            conexao.setAutoCommit(false);

            boolean sucessoUsuario = usuarioDAO.atualizarDados(clienteExistente, conexao);
            boolean sucessoCliente = clienteDAO.atualizarDadosDoCliente(clienteExistente, conexao);

            if (sucessoUsuario || sucessoCliente) {
                conexao.commit();
                System.out.println("Cliente atualizado com sucesso!");
            } else {
                System.out.println("Nenhum dado foi alterado.");
            }

        } catch (SQLException e) {
            System.err.println("ERRO DE BANCO DE DADOS");
            try {
                if (conexao != null)
                    conexao.rollback();
            } catch (SQLException ex) {
                 System.err.println("Erro grave no sistema. O cadastro pode estar incompleto");
            }
            e.printStackTrace();
        } finally {
            try {
                if (conexao != null)
                    conexao.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

    public void deletarCliente(Scanner leitor) {
        System.out.println("Digite o ID do cliente que deseja Deletar: ");
        int id = leitor.nextInt();
        leitor.nextLine();

        Connection conexao = null;

        try {
            conexao = ConexaoDoBanco.criarConexao();
            if (clienteDAO.buscaPorId(id, conexao) == null) {
                System.out.println("Cliente não encontrado");
                return;
            }

            conexao.setAutoCommit(false);

            boolean deletarCliente = clienteDAO.delatarDadosDoCliente(id, conexao);
            boolean deletarUsuario = usuarioDAO.deletarDadosDoUsuario(id, conexao);

            if (deletarCliente && deletarUsuario) {
                conexao.commit();
                System.out.println("Cliente Deletado com Sucesso!");
            } else {
                System.out.println("Falha ao deletar o cliente.");
                conexao.rollback(); //reverto tudo que foi feito e não salvando no banco os dados
            }
        } catch (SQLException e) {
            System.err.println("ERRO DE BANCO DE DADOS");

            try {
                if (conexao != null)
                    conexao.rollback();
            } catch (SQLException ex) {
                 System.err.println("Erro grave no sistema. O cadastro pode estar incompleto");
            }
            e.printStackTrace();
        } finally {
            try {
                if (conexao != null)
                    conexao.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

    public String visualizarDadosCliente() {

        Connection conexao = null;

        try {
            conexao = ConexaoDoBanco.criarConexao();
            List<Cliente> clientes = clienteDAO.listarTodosClientes(conexao);
            if (clientes.isEmpty()) {
                return "Nenhum cliente cadastrado no momento!";
            }

            String resultado = null;

            for (Cliente cliente : clientes) {
                resultado += cliente.toString() + "\n";
            }

            return resultado;

        } catch (SQLException e) {
            System.err.println("ERRO DE BANCO DE DADOS");
            return "Erro ao acessar os dados.";
        } finally {
            try {
                if (conexao != null)
                    conexao.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }

    }

    public List<Cliente> listarClientes() {
        Connection conexao = null;
        try {
            conexao = ConexaoDoBanco.criarConexao();
            return clienteDAO.listarTodosClientes(conexao);

        } catch (SQLException e) {
            System.err.println("ERRO DE BANCO DE DADOS!");
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            try {
                if (conexao != null)
                    conexao.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

}