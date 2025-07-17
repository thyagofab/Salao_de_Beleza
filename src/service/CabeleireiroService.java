package service;

import java.util.TreeMap;

import dao.CabeleireiroDAO;
import model.Cabeleireiro;

public class CabeleireiroService {
    
    private CabeleireiroDAO cabeleireiroDAO = new CabeleireiroDAO();

    public boolean criarCabeleireiro(Cabeleireiro cabeleireiro){
        return cabeleireiroDAO.salvarDadosCabeleireiroNoBanco(cabeleireiro);
    }

    public Cabeleireiro buscarCabeleireiro(int id){
        return cabeleireiroDAO.buscarPorId(id);
    }

    public boolean atualizarCabeleireiro(Cabeleireiro cabeleireiro) {
        return cabeleireiroDAO.atualizarDadosDoCabeleireiro(cabeleireiro);
    }

    public boolean deletarCabeleireiro(int id) {
        return cabeleireiroDAO.deletarDadosDoCabeleireiro(id);
    }

    public TreeMap<Integer, Cabeleireiro> listarCabeleireiros(){
        return cabeleireiroDAO.listarTodos();
    }
}