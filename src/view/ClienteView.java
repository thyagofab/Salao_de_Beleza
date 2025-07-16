package view;

import java.time.LocalDate;
import java.util.List;

import model.Cliente;
import model.ClienteService;
import util.Entradas;

public class ClienteView {
    private ClienteService clienteService;

    public ClienteView(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void MenuClientes() {
        int opcao = 0;

        while (opcao != 6) {

            System.out.println("======== MENU DE CLIENTES ========");
            System.out.println("| [1] BUSCAR CLIENTE POR ID      |");
            System.out.println("| [2] ATUALIZAR DADOS DO CLIENTE |");
            System.out.println("| [3] DELETAR CLIENTE            |");
            System.out.println("| [4] EXIBIR LISTA DE CLIENTES   |");
            System.out.println("| [5] AGENDAMENTOS               |");
            System.out.println("| [6] VOLTAR AO MENU PRINCIPAL   |");
            System.out.println("==================================");
            System.out.print("Escolha uma opção: ");

            opcao = Entradas.lerNumero("Escolha uma opção ");

            switch (opcao) {
                case 1:
                    buscarPorId();
                    break;
                case 2:
                    atualizarDadosDoCliente();
                    break;
                case 3:
                    deletarDadosCliente();
                    break;
                case 4:
                    exibirListaDeClientes();
                    break;
                case 5:
                    System.out.println("Gerenciar agendamentos não implementado ainda.");
                    break;
                case 6:
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    public void adicionarCliente() {

        System.out.print("Digite o nome: ");
        String nome = Entradas.validarNome();

        System.out.print("Digite o CPF (apenas números): ");
        String cpf = Entradas.validarCPF();

        System.out.print("Digite o telefone com DDD (10 ou 11 dígitos): ");
        String telefone = Entradas.validarTelefone();

        System.out.print("Digite o email: ");
        String email = Entradas.validarEmail();

        System.out.print("Digite senha: ");
        String senha = Entradas.validarSenha();

        System.out.print("Digite endereço: ");
        String endereco = Entradas.validarEndereco();

        System.out.print("Data de nascimento (dd/MM/yyyy): ");
        LocalDate dataNascimento = Entradas.validarDataDeNascimento();

        Cliente clienteNovo = new Cliente(0, nome, cpf, telefone, email, senha, dataNascimento, endereco, 0,
             null);

        if (this.clienteService.criarCliente(clienteNovo)) {
            System.out.println("Cliente criado com sucesso!");
        } else {
            System.out.println("Erro ao adicionar o cliente.");
        }
    }

    public void buscarPorId() {

        int idCliente = Entradas.lerNumero("Digite o ID do Cliente que deseja consultar: ");

        Cliente clienteEncontrado = clienteService.consultarCliente(idCliente);

        if (clienteEncontrado != null) {
            System.out.println(clienteEncontrado.toString());
        } else {
            System.out.println("Cliente não encontrado com o ID: " + idCliente);
        }

    }

    public void atualizarDadosDoCliente() {
        int idCliente = Entradas.lerNumero("Digite o ID do Cliente que deseja atualizar os dados: ");

        Cliente clienteExistente = clienteService.consultarCliente(idCliente);

        if (clienteExistente != null) {

            System.out.println("Digite os novos Dados: ");

            System.out.print("Digite o novo nome: ");
            String nome = Entradas.validarNome();

            System.out.print("Digite o novo CPF: ");
            String cpf = Entradas.validarCPF();

            System.out.print("Digite o novo telefone: ");
            String telefone = Entradas.validarTelefone();

            System.out.print("Digite o novo email: ");
            String email = Entradas.validarEmail();

            System.out.print("Digite a nova senha: ");
            String senha = Entradas.validarSenha();

            System.out.print("Digite o novo endereço: ");
            String endereco = Entradas.validarEndereco();

            System.out.print("Digite a nova Data de nascimento (dd/MM/yyyy): ");
            LocalDate dataNascimento = Entradas.validarDataDeNascimento();


            clienteExistente.setNome(nome);
            clienteExistente.setCpf(cpf);
            clienteExistente.setTelefone(telefone);
            clienteExistente.setEmail(email);
            clienteExistente.setSenha(senha);
            clienteExistente.setEndereco(endereco);
            clienteExistente.setDataNascimento(dataNascimento);

            if (clienteService.atualizarCliente(clienteExistente)) {
                System.out.println("Cliente atualizado com sucesso!");
            } else {
                System.out.println("Eroo ao atualizar os dados no banco");
            }
        } else {
            System.out.println("Cliente não encontrado.");
            return;
        }

    }

    public void deletarDadosCliente() {
        int idCliente = Entradas.lerNumero("Digite o ID do Cliente que deseja consultar: ");

        Cliente clienteExistente = clienteService.consultarCliente(idCliente);

        if (clienteExistente != null) {
            if (clienteService.deletarCliente(idCliente)) {
                System.out.println("Cliente deletado com sucesso!");
            } else {
                System.out.println("Erro ao deletar o procedimento.");
            }
        } else {
            System.out.println("Cliente não encontrado.");
        }

    }

    public void exibirListaDeClientes() {
        List<Cliente> clientes = clienteService.listarClientes();

        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado no momento.");
        } else {
            System.out.println("--- Lista de Clientes ---");
            for (Cliente cliente : clientes) {
                System.out.println(cliente.toString());
                System.out.println("---------------------------------");
            }
        }
    }

    public boolean atualizarData(int idCliente) {
        return clienteService.registrarUltimaVisitaCliente(idCliente);

    }

    public String exibirDadosCliente() {
        return clienteService.visualizarDadosCliente();
    }

}
