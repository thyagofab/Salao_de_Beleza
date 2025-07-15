package model;

import java.time.LocalDate;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class Cliente extends Usuario {
    LocalDate dataNascimento;
    String endereco;
    int quantidadedeAgendamentos;
    List<String> preferenciasDeHorarios;
    LocalDate ultimaVisita;

    public Cliente(int idUsuario, String nome, String cpf, String telefone, String email, String senha,
            LocalDate dataNascimento, String endereco, int quantidadedeAgendamentos,
            List<String> preferenciasDeHorarios, LocalDate ultimaVisita) {
        super(idUsuario, nome, cpf, telefone, email, senha);
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.quantidadedeAgendamentos = quantidadedeAgendamentos;
        this.preferenciasDeHorarios = preferenciasDeHorarios;
        this.ultimaVisita = ultimaVisita;
    }

    public Boolean verificarAnivesario(LocalDate dataAtual) {
        if (this.dataNascimento == null) {
            return false;
        }

        int mesNascimento = this.dataNascimento.getMonthValue();
        int diaNascimento = this.dataNascimento.getDayOfMonth();

        int mesAtual = dataAtual.getMonthValue();
        int diaAtual = dataAtual.getDayOfMonth();

        return (mesNascimento == mesAtual) && (diaNascimento == diaAtual);
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
        return super.toString()
                + "\n| Data De Nascimento: " + this.dataNascimento
                + "\n| Endereço: " + this.endereco
                + "\n| Quantidade De Agendamentos: " + this.quantidadedeAgendamentos
                + "\n| Preferência de Horários: " + this.preferenciasDeHorarios
                + "\n| Última Visita: " + (this.ultimaVisita != null ? this.getUltimaVisita().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")): "Nenhuma");
    }

}
