package model;

import java.time.LocalDateTime;
import java.util.List;

public class Agendamento {
    private int id;
    private Cliente cliente;
    private Cabeleireiro cabeleireiro;
    private List<Procedimento> procedimentos;
    private LocalDateTime dataHora;
    private StatusAgendamento status;
    private double precoTotal;

    public Agendamento(Cliente cliente, Cabeleireiro cabeleireiro, List<Procedimento> procedimentos,
            LocalDateTime dataHora) {
        this.cliente = cliente;
        this.cabeleireiro = cabeleireiro;
        this.procedimentos = procedimentos;
        this.dataHora = dataHora;
        this.status = StatusAgendamento.AGENDADO;
    }

    public Agendamento(int id, Cliente cliente, Cabeleireiro cabeleireiro, List<Procedimento> procedimentos,
            LocalDateTime dataHora, StatusAgendamento status, double precoTotal) {
        this.id = id;
        this.cliente = cliente;
        this.cabeleireiro = cabeleireiro;
        this.procedimentos = procedimentos;
        this.dataHora = dataHora;
        this.status = status;
        this.precoTotal = precoTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cabeleireiro getCabeleireiro() {
        return cabeleireiro;
    }

    public void setCabeleireiro(Cabeleireiro cabeleireiro) {
        this.cabeleireiro = cabeleireiro;
    }

    public List<Procedimento> getProcedimentos() {
        return procedimentos;
    }

    public void setProcedimentos(List<Procedimento> procedimentos) {
        this.procedimentos = procedimentos;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    public double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(double precoTotal) {
        this.precoTotal = precoTotal;
    }

    public String toString() {
        return "---------------------------------\n" +
                "| ID do Agendamento: " + id +
                "\n| Cliente: " + cliente.getNome() +
                "\n| Cabeleireiro: " + cabeleireiro.getNome() +
                "\n| Data/Hora: " + dataHora +
                "\n| Status: " + status +
                "\n| Preço Total: R$ " + String.format("%.2f", precoTotal);
    }
}