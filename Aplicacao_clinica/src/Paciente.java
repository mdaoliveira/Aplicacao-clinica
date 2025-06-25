
import java.io.Serializable;
import java.util.*;

public class Paciente implements Serializable {
    private String nome;
    private long cpf;
    private ArrayList<Consulta> consultas;

    public Paciente (String nome, Long cpf) {
        this.nome = nome;
        this.cpf = cpf;
        consultas = new ArrayList<>();
    }

    public void adicionaConsulta(Consulta consulta) {
        consultas.add(consulta);
    }

    public void editaNome(String nome) { this.nome = nome; }

    public boolean isPaciente(long cpf) {
        return this.cpf == cpf;
    }

    public ArrayList<Consulta> getConsultas(){
        return this.consultas;
    }

    public Long getCpf(){
        return this.cpf;
    }

    public String getNome(){
        return this.nome;
    }

    @Override
    public String toString() {
        return nome + ' '  + cpf;
    }
}
