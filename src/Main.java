import data.ConexaoDoBanco;
import view.MenuPrincipalView;

public class Main {
    public static void main(String[] args) {

        ConexaoDoBanco.iniciarBanco();

        MenuPrincipalView menuPrincipalView = new MenuPrincipalView();
        menuPrincipalView.exibirMenuPrincipal();
        
    }
}