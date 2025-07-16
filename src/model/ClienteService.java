package model;

import dao.ClienteDAO;

import java.time.LocalDate;
import java.util.List;

public class ClienteService {

    private ClienteDAO clienteDAO = new ClienteDAO();

    public boolean criarCliente(Cliente cliente) {
        return clienteDAO.salvarDadosClienteNoBanco(cliente);
    }

    public Cliente consultarCliente(int idCliente) {
        return clienteDAO.buscaPorId(idCliente);

    }

    public boolean atualizarCliente(Cliente clienteParaAtualizar) {
        return clienteDAO.atualizarDadosDoCliente(clienteParaAtualizar);
    }

    public Boolean deletarCliente(int idCliente) {
        return clienteDAO.deletarDadosDoCliente(idCliente);
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.listarTodos();
    }

    public boolean registrarUltimaVisitaCliente(int idUsuario) {
        LocalDate dataAtual = LocalDate.now();
        return clienteDAO.atualizarUltimaVisita(idUsuario, dataAtual);
    }

    public String visualizarDadosCliente(){
        List<Cliente> clientes = listarClientes();
        if (clientes.isEmpty()) {
            return "Nenhum cliente cadastrado no momento.";
        }
        String clienteString = "";
        for(Cliente clienteAtual: clientes){
            clienteString += clienteAtual.toString();
        }

        return clienteString;
    }


}
