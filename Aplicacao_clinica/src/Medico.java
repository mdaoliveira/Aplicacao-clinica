
import java.io.*;
import java.util.*;

public class Medico implements Serializable {
    private String nome;
    private Integer codigo;
    private ArrayList<Paciente> pacientes;
    private ArrayList<Consulta> consultas;

    public Medico (String nome, Integer codigo) {
        this.nome = nome;
        this.codigo = codigo;
        this.pacientes = new ArrayList<>();
        this.consultas = new ArrayList<>();

    }

    public void editaNome(String nome){
        this.nome = nome;
    }
    public void adicionaPaciente(Paciente paciente) {
        this.pacientes.add(paciente);
    }

    public boolean isMedico(int codigo) {
        return this.codigo == codigo;
    }

    public ArrayList<Paciente> getPacientes(){
        return this.pacientes;
    }

    public void imprimirPacientes() {
        for(Paciente p : pacientes){
            System.out.println(p.toString());
        }
    }

    public void adicionaConsulta(Consulta consulta) {
        consultas.add(consulta);
    }

    public ArrayList<Consulta> getConsultas(){
        return this.consultas;
    }

    public void imprimirConsultas(){
        for(Consulta c : consultas){
            System.out.println(c.getData() + " " + c.getHorario() + " " + c.getCpf());
        }
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome(){
        return nome;
    }

    @Override
    public String toString() {
        return nome + ' '  + codigo;
    }
}
