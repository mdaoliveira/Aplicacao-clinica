import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Consulta implements Serializable {
    private LocalDate data;
    private LocalTime horario;
    private int codigo;
    private long cpf;

    public Consulta(LocalDate data, LocalTime horario, int codigo, long cpf) {
        this.data = data;
        this.horario = horario;
        this.codigo = codigo;
        this.cpf = cpf;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public int getCodigo() {
        return codigo;
    }
    public long getCpf(){
        return cpf;
    }

    public LocalDateTime getDataEHorario(){
        return LocalDateTime.of(data, horario);
    }

    public String toString() {
        return data+" "+horario+" "+codigo+" "+cpf;
    }
}

