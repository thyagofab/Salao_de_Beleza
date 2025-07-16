package service;

import java.util.TreeMap;

import dao.CabeleireiroDAO;
import model.Cabeleireiro;

public class CabeleireiroService {
    
    private CabeleireiroDAO cabeleireiroDAO = new CabeleireiroDAO();

    public boolean criarCabeleireiro(Cabeleireiro cabeleireiro){
        return cabeleireiroDAO.salvar(cabeleireiro);
    }

    public Cabeleireiro buscarCabeleireiro(int id){
        return cabeleireiroDAO.buscarPorId(id);
    }

    public boolean atualizarCabeleireiro(Cabeleireiro cabeleireiro, int id){
        return cabeleireiroDAO.atualizar(cabeleireiro, id);
    }

    public boolean deletarCabeleireiro(int id) {
        return cabeleireiroDAO.deletar(id);
    }

    public TreeMap<Integer, Cabeleireiro> listarCabeleireiros(){
        return cabeleireiroDAO.listarTodos();
    }
}