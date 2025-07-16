package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Cliente extends Usuario {
    LocalDate dataNascimento;
    String endereco;
    int quantidadedeAgendamentos;
    LocalDate ultimaVisita;

    public Cliente(int idUsuario, String nome, String cpf, String telefone, String email, String senha,
            LocalDate dataNascimento, String endereco, int quantidadedeAgendamentos, LocalDate ultimaVisita) {
        super(idUsuario, nome, cpf, telefone, email, senha);
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.quantidadedeAgendamentos = quantidadedeAgendamentos;
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

    public void setUltimaVisita(LocalDate ultimaVisita) {
        this.ultimaVisita = ultimaVisita;
    }

    @Override
    public String toString() {
        return super.toString()
                + "\n| Data De Nascimento: " + this.dataNascimento
                + "\n| Endereço: " + this.endereco
                + "\n| Quantidade De Agendamentos: " + this.quantidadedeAgendamentos
                + "\n| Última Visita: " + (this.ultimaVisita != null ? this.getUltimaVisita().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")): "Nenhuma");
    }

}
