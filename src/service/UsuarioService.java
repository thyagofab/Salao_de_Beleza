package service;

import dao.UsuarioDAO;

public class UsuarioService {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public boolean verificarCPF(String cpf){
        return usuarioDAO.cpfExiste(cpf);
    }

    public boolean verificarEmail(String email){
        return usuarioDAO.emailExiste(email);
    }

    

    
}
