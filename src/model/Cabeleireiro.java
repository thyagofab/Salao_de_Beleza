package model;

import java.time.LocalTime;
import java.util.List;

// classe com os atributos

public class Cabeleireiro {
    private List<String> especialidades;
    private double mediaDeAvaliacoes;
    private int totalDeAvaliacoes;
    private List<String> diasDisponiveis;
    private List<LocalTime> horariosDisponiveis;
    private String tempoDeExperiencia;

    // criacao do construtor

    public Cabeleireiro(List<String> especialidades, double mediaDeAvaliacoes, int totalDeAvaliacoes,
                        List<String> diasDisponiveis, List<LocalTime> horariosDisponiveis, String tempoDeExperiencia){
        this.especialidades = especialidades;
        this.mediaDeAvaliacoes = mediaDeAvaliacoes;
        this.totalDeAvaliacoes = totalDeAvaliacoes;
        this.diasDisponiveis = diasDisponiveis;
        this.horariosDisponiveis = horariosDisponiveis;
        this.tempoDeExperiencia = tempoDeExperiencia;
    }

    // gets e sets

    public List<String> getEspecialidades(){
        return especialidades;
    }

    public void setEspecialidades(List<String> especialidades){
        this.especialidades = especialidades;
    }

    public double getMediaDeAvaliacoes(){
        return mediaDeAvaliacoes;
    }

    public void setMediaDeAvaliacoes(double mediaDeAvaliacoes){
        this.mediaDeAvaliacoes = mediaDeAvaliacoes;
    }

    public int getTotalDeAvaliacoes(){
        return totalDeAvaliacoes;
    }

    public void setTotalDeAvaliacoes(int totalDeAvaliacoes){
        this.totalDeAvaliacoes = totalDeAvaliacoes;
    }

    public List<String> getDiasDisponiveis(){
        return diasDisponiveis;
    }

    public void setDiasDisponiveis(List<String> diasDisponiveis){
        this.diasDisponiveis = diasDisponiveis;
    }

    public List<LocalTime> getHorariosDisponiveis(){
        return horariosDisponiveis;
    }

    public void setHorariosDisponiveis(List<LocalTime> horariosDisponiveis){
        this.horariosDisponiveis = horariosDisponiveis;
    }

    public String getTempoDeExperiencia(){
        return tempoDeExperiencia;
    }

    public void setTempoDeExperiencia(String tempoDeExperiencia){
        this.tempoDeExperiencia = tempoDeExperiencia;
    }

    // toString
    
    @Override
    public String toString(){
        String s = "";
        s += "Especialidades: " + getEspecialidades() + "\n";
        s += "Media de Avaliacao: " + getMediaDeAvaliacoes() + "\n";
        s += "Total de Avaliacoes: " + getTotalDeAvaliacoes() + "\n";
        s += "Dias Disponiveis: " + getDiasDisponiveis() + "\n";
        s += "Horarios Disponiveis: " + getHorariosDisponiveis() + "\n";
        s += "Tempo de Experiencia: " + getTempoDeExperiencia() + "\n";
        return s;
    }
}