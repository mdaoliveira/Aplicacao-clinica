import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Leitura implements Serializable {
    private Clinica clinica;
    private ArrayList<Medico> medicos;
    private ArrayList<Paciente> pacientes;
    private ArrayList<Consulta> consultas;

    public Leitura(Clinica clinica){
        this.clinica = clinica;
        this.medicos = clinica.getMedicos();
        this.pacientes = clinica.getPacientes();
        this.consultas = clinica.getConsultas();
    }

    public void salvar(String nome_arquivo) throws IOException {
        FileOutputStream arquivo = new FileOutputStream(nome_arquivo);
        ObjectOutputStream gravador = new ObjectOutputStream(arquivo);
        gravador.writeObject(this);
        gravador.close();
        arquivo.close();
    }

    public static ArrayList<Medico> leitura_medico() throws IOException
    {
        String NOME_ARQUIVO = "dados\\Medicos.csv";
        String SEPARADOR = ",";

        List<List<String>> registros = new ArrayList<>();
        ArrayList<Medico> lista_medicos = new ArrayList<>();

        File arquivo = new File(NOME_ARQUIVO);
        Scanner scanner_arquivo = new Scanner(arquivo);
        String linha = scanner_arquivo.nextLine(); // lê o cabeçalho
        while (scanner_arquivo.hasNextLine())
        {
            linha = scanner_arquivo.nextLine();
            Scanner scanner_linha = new Scanner(linha);
            scanner_linha.useDelimiter(SEPARADOR);

            String nome = scanner_linha.next();
            int codigo = scanner_linha.nextInt();

            lista_medicos.add(new Medico(nome, codigo));

        }

        return lista_medicos;
    }
    public static ArrayList<Paciente> leitura_pacientes() throws IOException
    {
        String NOME_ARQUIVO = "dados\\Pacientes.csv";
        String SEPARADOR = ",";

        List<List<String>> registros = new ArrayList<>();
        ArrayList<Paciente> lista_pacientes = new ArrayList<>();
        File arquivo = new File(NOME_ARQUIVO);
        Scanner scanner_arquivo = new Scanner(arquivo);
        String linha = scanner_arquivo.nextLine(); // lê o cabeçalho
        while (scanner_arquivo.hasNextLine())
        {
            linha = scanner_arquivo.nextLine();
            Scanner scanner_linha = new Scanner(linha);
            scanner_linha.useDelimiter(SEPARADOR);

            String nome = scanner_linha.next();
            long cpf = scanner_linha.nextLong();

            lista_pacientes.add(new Paciente(nome, cpf));

        }


        return lista_pacientes;

    }
    static DateTimeFormatter formatoBR = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    public static ArrayList<Consulta> leitura_consulta() throws IOException
    {
        String NOME_ARQUIVO = "dados\\Consultas.csv";
        String SEPARADOR = ",";

        List<List<String>> registros = new ArrayList<>();
        ArrayList<Consulta> lista_consultas = new ArrayList<>();
        File arquivo = new File(NOME_ARQUIVO);
        Scanner scanner_arquivo = new Scanner(arquivo);
        String linha = scanner_arquivo.nextLine(); // lê o cabeçalho
        while (scanner_arquivo.hasNextLine())
        {
            linha = scanner_arquivo.nextLine();
            Scanner scanner_linha = new Scanner(linha);
            scanner_linha.useDelimiter(SEPARADOR);



            LocalDate data = LocalDate.parse(scanner_linha.next(), formatoBR);
            LocalTime horario = LocalTime.parse(scanner_linha.next());
            int codigo = scanner_linha.nextInt();
            long cpf = scanner_linha.nextLong();

            lista_consultas.add(new Consulta(data, horario, codigo, cpf));

        }
        return lista_consultas;

    }

    public void leitura_geral() {
        try{
            clinica.setMedicos(leitura_medico());
        } catch (IOException e) {
            System.out.println("Erro na leitura do arquivo de médicos!");
            e.printStackTrace();
        }

        try{
            clinica.setPacientes(leitura_pacientes());
        } catch (IOException e) {
            System.out.println("Erro na leitura do arquivo de pacientes!");
            e.printStackTrace();
        }

        try{
            clinica.setConsultas(leitura_consulta());
        } catch (IOException e) {
            System.out.println("Erro na leitura do arquivo de consultas!");
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        ArrayList<Medico> medicos = null;
        ArrayList<Paciente> pacientes = null;
        ArrayList<Consulta> consultas = null;

        Clinica clinica = new Clinica(medicos, pacientes, consultas);

        Leitura leitura = new Leitura(clinica);
        leitura.leitura_geral();

        try{
            clinica.salvar("testeclinica.ser");
        }
        catch(IOException e){
            System.out.println("Erro de I/O");
            e.printStackTrace();
        }
    }
}
