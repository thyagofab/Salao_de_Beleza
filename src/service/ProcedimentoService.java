package service;

import java.util.TreeMap;

import dao.ProcedimentoDAO;
import model.Procedimento;

public class ProcedimentoService {
    
    private ProcedimentoDAO procedimentoDAO = new ProcedimentoDAO();

    public boolean criarProcedimento(Procedimento procedimento){
        return procedimentoDAO.salvar(procedimento);
    }

    public Procedimento buscarProcedimento(int id){
        return procedimentoDAO.buscarPorId(id);
    }

    public boolean atualizarProcedimento(Procedimento procedimento, int id){
        return procedimentoDAO.atualizar(procedimento, id);
    }

    public boolean deletarProcedimento(int id) {
        return procedimentoDAO.deletar(id);
    }

    public TreeMap <Integer, Procedimento> listarProcedimentos(){
        return procedimentoDAO.listarTodos();
    }
}
