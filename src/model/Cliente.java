package model;

import java.time.LocalDate;
import java.util.List;

public class Cliente {
    LocalDate dataNascimento;
    String endereco;
    int quantidadedeAgendamentos;
    List<String> preferenciasDeHorarios;
    LocalDate ultimaVisita;
    
    public Cliente(LocalDate dataNascimento, String endereco, int quantidadedeAgendamentos,
            List<String> preferenciasDeHorarios, LocalDate ultimaVisita) {
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.quantidadedeAgendamentos = quantidadedeAgendamentos;
        this.preferenciasDeHorarios = preferenciasDeHorarios;
        this.ultimaVisita = ultimaVisita;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getEndereco() {
        return endereco;
    }

    public int getQuantidadedeAgendamentos() {
        return quantidadedeAgendamentos;
    }

    public List<String> getPreferenciasDeHorarios() {
        return preferenciasDeHorarios;
    }

    public LocalDate getUltimaVisita() {
        return ultimaVisita;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setQuantidadedeAgendamentos(int quantidadedeAgendamentos) {
        this.quantidadedeAgendamentos = quantidadedeAgendamentos;
    }

    public void setPreferenciasDeHorarios(List<String> preferenciasDeHorarios) {
        this.preferenciasDeHorarios = preferenciasDeHorarios;
    }

    public void setUltimaVisita(LocalDate ultimaVisita) {
        this.ultimaVisita = ultimaVisita;
    }

    @Override
    public String toString() {
        return "Dados Do Cliente: \n Data De Nascimento: " + dataNascimento + "\nEndereço: " + endereco + "\nQuantidade de Agendamento: "
                + quantidadedeAgendamentos + "\nPreferência de Horarios" + preferenciasDeHorarios + "\nÚltima Visita: "
                + ultimaVisita;
    }


    


    
    
}
