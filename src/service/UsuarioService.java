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

    public String autenticar(String email, String senha) {
        try {
            return usuarioDAO.autenticarObterTipo(email, senha);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao autenticar usuário", e);
        }
    }

    
}
