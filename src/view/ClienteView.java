package view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import model.Cliente;
import service.ClienteService;

public class ClienteView {
    private Scanner scanner;
    private ClienteService clienteService;

    public ClienteView(Scanner scanner, ClienteService clienteService) {
        this.scanner = scanner;
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

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    buscarPorId(scanner);
                    break;
                case 2:
                    atualizarDadosDoCliente(scanner);
                    break;
                case 3:
                    deletarDadosCliente(scanner);
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

    public void adicionarCliente(Scanner scanner) {
        System.out.print("Digite o novo nome: ");
        String nome = scanner.nextLine();

        System.out.print("Digite o novo CPF: ");
        String cpf = scanner.nextLine();

        System.out.print("Digite o novo telefone: ");
        String telefone = scanner.nextLine();

        System.out.print("Digite o novo email: ");
        String email = scanner.nextLine();

        System.out.print("Digite a novo senha: ");
        String senha = scanner.nextLine();

        System.out.print("Digite o novo endereço: ");
        String endereco = scanner.nextLine();

        System.out.print("Data de nascimento (dd/MM/yyyy): ");
        String dataNascimentoStr = scanner.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr, formatter);

        System.out.print("Preferências de horários (ex: Manhã,Tarde): ");
        String preferenciasStr = scanner.nextLine();
        List<String> preferencias = new ArrayList<>();
        if (!preferenciasStr.isEmpty()) {
            preferencias.addAll(Arrays.asList(preferenciasStr.split(",")));
        }

        Cliente clienteNovo = new Cliente(0, nome, cpf, telefone, email, senha, dataNascimento, endereco, 0,
                preferencias, null);

        if (this.clienteService.criarCliente(clienteNovo)) {
            System.out.println("Cliente criado com sucesso!");
        } else {
            System.out.println("Erro ao adicionar o cliente.");
        }
    }

    public void buscarPorId(Scanner scanner) {
        int idCliente;

        System.out.printf("Digite o ID do Cliente que deseja consultar: ");
        idCliente = scanner.nextInt();
        scanner.nextLine();

        Cliente clienteEncontrado = clienteService.consultarCliente(idCliente);

        if (clienteEncontrado != null) {
            System.out.println(clienteEncontrado.toString());
        } else {
            System.out.println("Cliente não encontrado com o ID: " + idCliente);
        }

    }

    public void atualizarDadosDoCliente(Scanner scanner) {
        System.out.print("Digite o ID do Cliente que deseja Atualizar:  ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Cliente clienteExistente = clienteService.consultarCliente(id);

        if (clienteExistente != null) {

            System.out.print("Digite o novo nome: ");
            String nome = scanner.nextLine();

            System.out.print("Digite o novo CPF: ");
            String cpf = scanner.nextLine();

            System.out.print("Digite o novo telefone: ");
            String telefone = scanner.nextLine();

            System.out.print("Digite o novo email: ");
            String email = scanner.nextLine();

            System.out.print("Digite a nova senha: ");
            String senha = scanner.nextLine();

            System.out.print("Digite o novo endereço: ");
            String endereco = scanner.nextLine();

            System.out.print("Digite a nova Data de nascimento (dd/MM/yyyy): ");
            String dataNascimentoStr = scanner.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr, formatter);

            System.out.print("Digite as novas Preferências de horários (ex: Manhã,Tarde): ");
            String preferenciasStr = scanner.nextLine();
            List<String> preferencias = new ArrayList<>(Arrays.asList(preferenciasStr.split(",")));

            clienteExistente.setNome(nome);
            clienteExistente.setCpf(cpf);
            clienteExistente.setTelefone(telefone);
            clienteExistente.setEmail(email);
            clienteExistente.setSenha(senha);
            clienteExistente.setEndereco(endereco);
            clienteExistente.setDataNascimento(dataNascimento);
            clienteExistente.setPreferenciasDeHorarios(preferencias);

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

    public void deletarDadosCliente(Scanner scanner) {
        System.out.print("Digite o ID do Cliente que deseja Atualizar:  ");
        int idCliente = scanner.nextInt();
        scanner.nextLine();

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

    public boolean atualizarData(int idCliente){
        return clienteService.registrarUltimaVisitaCliente(idCliente);

    }
    
    public String exibirDadosCliente(){
        return clienteService.visualizarDadosCliente();
    }


}
