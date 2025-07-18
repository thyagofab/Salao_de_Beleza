package model;

public class Procedimento {
    private String nome;
    private String descricao;
    private double preco;

    public Procedimento(String nome, String descricao, double preco){
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
    }

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getDescricao(){
        return descricao;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public double getPreco(){
        return preco;
    }

    public void setPreco(double preco){
        this.preco = preco;
    }

    public String toString(){
        return String.format("| Procedimento: %s \n| Descrição: %s \n| Preço: R$%.2f", nome, descricao, preco);
    }    
}
