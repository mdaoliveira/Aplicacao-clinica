import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Clinica implements Serializable {
    private ArrayList<Medico> medicos;
    private ArrayList<Paciente> pacientes;
    private ArrayList<Consulta> consultas;

    public Clinica(ArrayList<Medico> medicos, ArrayList<Paciente> pacientes, ArrayList<Consulta> consultas){
        this.medicos = medicos;
        this.pacientes = pacientes;
        this.consultas = consultas;
    }

    public ArrayList<Medico> getMedicos() {
        return medicos;
    }

    public void setMedicos(ArrayList<Medico> medicos) {
        this.medicos = medicos;
    }

    public ArrayList<Paciente> getPacientes() {
        return pacientes;
    }

    public void setPacientes(ArrayList<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    public ArrayList<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(ArrayList<Consulta> consultas) {
        this.consultas = consultas;
    }

    public void imprimir(){
        for(Medico m : medicos){
            System.out.println(m.toString());
        }

        for(Paciente p : pacientes){
            System.out.println(p.toString());
        }

        for(Consulta c : consultas){
            System.out.println(c.toString());
        }
    }

    public void salvar(String nome_arquivo) throws IOException {
        FileOutputStream arquivo = new FileOutputStream(nome_arquivo);
        ObjectOutputStream gravador = new ObjectOutputStream(arquivo);
        gravador.writeObject(this);
        gravador.close();
        arquivo.close();
    }

    public static Clinica abrir(String nome_arquivo) throws IOException, ClassNotFoundException {
        Clinica clinica = null;
        FileInputStream arquivo = new FileInputStream(nome_arquivo);
        ObjectInputStream restaurador = new ObjectInputStream(arquivo);
        clinica = (Clinica) restaurador.readObject();
        restaurador.close();
        arquivo.close();
        return clinica;
    }

    private boolean existe_medico(int codigo) {
        Medico m;
        boolean encontrado = false;
        int i = 0;
        while (!encontrado && i < medicos.size()) {
            m = medicos.get(i);
            if (m.isMedico(codigo)) {
                encontrado = true;
            } else {
                i++;
            }
        }
        return encontrado;
    }

    // encontrar médico com base no código
    private Medico buscar_medico(int codigo){
        Medico m = null;
        boolean encontrado = false;
        int i = 0;
        while (!encontrado && i < medicos.size()){
            m = medicos.get(i);
            if (m.isMedico(codigo)){
                encontrado = true;
            }
            else{
                i++;
            }
        }
        return m;
    }

    private boolean existe_paciente(long cpf){
        Paciente p;
        boolean encontrado = false;
        int i = 0;
        while (!encontrado && i < pacientes.size()){
            p = pacientes.get(i);
            if (p.isPaciente(cpf)){
                encontrado = true;
            }
            else{
                i++;
            }
        }
        return encontrado;
    }

    private Paciente buscar_paciente(long cpf){
        Paciente p = null;
        boolean encontrado = false;
        int i = 0;
        while (!encontrado && i < pacientes.size()){
            p = pacientes.get(i);
            if (p.isPaciente(cpf)){
                encontrado = true;
            }
            else{
                i++;
            }

        }
        return p;
    }

    private void associa_pessoas_e_consultas(){
        for (Consulta consulta : consultas){
            Medico m = buscar_medico(consulta.getCodigo());
            Paciente p = buscar_paciente(consulta.getCpf());

            if (!(m.getPacientes().contains(p))){
                m.adicionaPaciente(p);
            }
            if (!(m.getConsultas().contains(consulta))){
                m.adicionaConsulta(consulta);
            }
            if (!(p.getConsultas().contains(consulta))){
                p.adicionaConsulta(consulta);
            }
        }

    }

    static String nome_arquivo_usuario = "";
    static int codigo_medico;
    static long cpf_paciente;
    static int codigo = 0;
    static Medico m;
    static Paciente p;

    static LocalDate data_inicial;
    static LocalDate data_final;
    static int intervalo_de_tempo;

    static DateTimeFormatter formatoBR = DateTimeFormatter.ofPattern("dd/MM/uuuu");


    public static void main(String[] args) {
        try {

            Clinica clinica = Clinica.abrir("testeclinica.ser");
            System.out.println("Clinica recuperada com sucesso!");
            // clinica.imprimir();
            clinica.associa_pessoas_e_consultas();

            JFrame frame = new JFrame("Clínica");
            frame.setSize(675, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel painelPrincipal = new JPanel(new CardLayout());
            JPanel painelMenu = new JPanel();
            JPanel painelEscolhaSaida = new JPanel();
            JPanel painelNomeArquivo = new JPanel();
            JPanel painelInsercao = new JPanel();
            JPanel painelInsercaoDupla = new JPanel();
            JPanel painelResultado = new JPanel();
            JPanel painelArquivoSalvamento = new JPanel();
            JPanel painelFinalSalvamento = new JPanel();

            String mensagem_inicio = "<html>Bem-vindo!<br>" +
                    "Menu de Operações:<br>" +
                    "1 - Consultar todos os pacientes de um determinado médico<br>" +
                    "2 - Consultas agendadas de um determinado médico em um período<br>" +
                    "3 - Consulta de todos os médicos de um determinado paciente<br>" +
                    "4 - Consultar todas as consultas de um paciente com um determinado médico<br>" +
                    "5 - Consultar todas as consultas agendadas de um determinado paciente<br>" +
                    "6 - Consultar todos os pacientes de um médico que não o consultam há mais do que um período em meses<br>" +
                    "7 - Editar médico<br>" +
                    "8 - Editar paciente<br>" +
                    "9 - Salvar e sair<br>" +
                    "Insira o código da operação desejada: </html>";


            LocalDate hoje = LocalDate.now();
            LocalTime hora = LocalTime.now();

            // Painel Menu
            JLabel rotulo_mensagem = new JLabel(mensagem_inicio);
            JComboBox<Integer> comboBox_opcoes = new JComboBox<>();
            for (int i = 1; i < 10; i++) {
                comboBox_opcoes.addItem(i);
            }
            JButton botao_continuar = new JButton("Continuar");
            JButton botao_encerrar = new JButton("Encerrar");

            painelMenu.add(rotulo_mensagem);
            painelMenu.add(comboBox_opcoes);
            painelMenu.add(botao_encerrar);
            painelMenu.add(botao_continuar);

            // Painel Escolha Saida
            JLabel rotuloEscolhaSaida = new JLabel();
            JButton botao_continuar2 = new JButton("Continuar");
            JRadioButton opcao1 = new JRadioButton("Na interface do programa");
            JRadioButton opcao2 = new JRadioButton("Em arquivo .txt");
            JButton botao_voltar_escolha = new JButton("Voltar");

            ButtonGroup grupo_opcoes = new ButtonGroup();
            grupo_opcoes.add(opcao1);
            grupo_opcoes.add(opcao2);

            painelEscolhaSaida.add(rotuloEscolhaSaida);
            painelEscolhaSaida.add(opcao1);
            painelEscolhaSaida.add(opcao2);
            painelEscolhaSaida.add(botao_continuar2);
            painelEscolhaSaida.add(botao_voltar_escolha);

            //Painel Nome Arquivo
            JLabel rotuloEscolhaNomeArq = new JLabel("Insira o nome do arquivo desejado: ");
            JTextField caixaNomeArquivo = new JTextField(50);
            JButton botao_continuarNome = new JButton("Continuar");
            JButton botao_voltar_arquivo = new JButton("Voltar");

            painelNomeArquivo.add(rotuloEscolhaNomeArq);
            painelNomeArquivo.add(caixaNomeArquivo);
            painelNomeArquivo.add(botao_voltar_arquivo);
            painelNomeArquivo.add(botao_continuarNome);

            // Painel Insercao (código do médico ou CPF do paciente)
            JLabel rotuloInsercao = new JLabel("");
            JTextField caixaInsercao = new JTextField(20);
            JButton botao_confirmarInsercao = new JButton("Confirmar");
            JButton botao_voltar_insercao = new JButton("Voltar");

            painelInsercao.add(rotuloInsercao);
            painelInsercao.add(caixaInsercao);
            painelInsercao.add(botao_confirmarInsercao);
            painelInsercao.add(botao_voltar_insercao);

            // Painel Insercao Dupla
            JLabel rotuloInsercao1 = new JLabel("Insira o CPF do paciente: ");
            JLabel rotuloInsercao2 = new JLabel("Insira o código do médico: ");
            JTextField caixaInsercaoCPF = new JTextField(20);
            JTextField caixaInsercaoCodigo = new JTextField(20);
            JButton botao_confirmarInsercaoDupla = new JButton("Confirmar");
            JButton botao_voltar_insercao_dupla = new JButton("Voltar");

            painelInsercaoDupla.add(rotuloInsercao1);
            painelInsercaoDupla.add(caixaInsercaoCPF);
            painelInsercaoDupla.add(rotuloInsercao2);
            painelInsercaoDupla.add(caixaInsercaoCodigo);
            painelInsercaoDupla.add(botao_confirmarInsercaoDupla);
            painelInsercaoDupla.add(botao_voltar_insercao_dupla);

            // Painel Arquivo Salvamento
            JLabel rotuloEscolhaNomeArqSalvar = new JLabel("Insira o nome do arquivo desejado para salvamento: ");
            JTextField caixaNomeArquivoSalvar = new JTextField(50);
            JButton botao_continuarNomeSalvar = new JButton("Continuar");
            JButton botao_voltar_arquivoSalvar = new JButton("Voltar");

            painelArquivoSalvamento.add(rotuloEscolhaNomeArqSalvar);
            painelArquivoSalvamento.add(caixaNomeArquivoSalvar);
            painelArquivoSalvamento.add(botao_continuarNomeSalvar);
            painelArquivoSalvamento.add(botao_voltar_arquivoSalvar);

            // Painel Resultado
            JLabel rotuloResultado = new JLabel("Arquivo salvo com sucesso.");
            JButton botao_finalizar = new JButton("Finalizar");

            painelResultado.add(rotuloResultado);
            painelResultado.add(botao_finalizar);

            // Painel Final Salvamento
            JLabel rotuloFinalSalvamento = new JLabel();
            JButton botao_finalSalvamento = new JButton("Finalizar");

            painelFinalSalvamento.add(rotuloFinalSalvamento);
            painelFinalSalvamento.add(botao_finalSalvamento);

            // Adiciona os painéis ao painel principal
            painelPrincipal.add(painelMenu, "Menu");
            painelPrincipal.add(painelEscolhaSaida, "Escolha Saida");
            painelPrincipal.add(painelNomeArquivo, "Escolha Nome Arquivo");
            painelPrincipal.add(painelInsercao, "Insercao Codigo/CPF");
            painelPrincipal.add(painelInsercaoDupla, "Insercao Dupla");
            painelPrincipal.add(painelResultado, "Resultado");
            painelPrincipal.add(painelArquivoSalvamento, "Escolha Nome Arquivo Salvamento");
            painelPrincipal.add(painelFinalSalvamento, "Painel Final Salvamento");

            frame.add(painelPrincipal);
            frame.setVisible(true);

            // ActionListener para o botão "Continuar"
            botao_continuar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    codigo = (int) comboBox_opcoes.getSelectedItem();
                    if (codigo <= 6){
                        rotuloEscolhaSaida.setText("<html>Como deseja que a sua saída ocorra?<br></html>");
                        CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                        cl.show(painelPrincipal, "Escolha Saida");
                    } else if (codigo >= 7){
                        switch(codigo){
                            case 7:
                                CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                                cl.show(painelPrincipal, "Insercao Codigo/CPF");

                                rotuloInsercao.setText("Insira o código do médico desejado: ");
                                botao_confirmarInsercao.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        for(ActionListener al : botao_confirmarInsercao.getActionListeners()){
                                            botao_confirmarInsercao.removeActionListener(al);
                                        }

                                        codigo_medico = Integer.parseInt(caixaInsercao.getText());

                                        try {
                                            if (!clinica.existe_medico(codigo_medico)) {
                                                throw new MedicoNaoExistente();

                                            } else {
                                                m = clinica.buscar_medico(codigo_medico);

                                                cl.show(painelPrincipal, "Insercao Codigo/CPF");
                                                rotuloInsercao.setText("Insira o novo nome para o médico selecionado: ");
                                                botao_confirmarInsercao.addActionListener(new ActionListener() {
                                                    public void actionPerformed(ActionEvent e) {
                                                        for (ActionListener al : botao_confirmarInsercao.getActionListeners()) {
                                                            botao_confirmarInsercao.removeActionListener(al);
                                                        }

                                                        m.editaNome(caixaInsercao.getText());

                                                        String exibir = "Nome editado com sucesso!";
                                                        System.out.println("certo");
                                                        rotuloResultado.setText(exibir);
                                                        CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                                                        cl.show(painelPrincipal, "Resultado");
                                                    }
                                                });
                                            }
                                        } catch (MedicoNaoExistente ex) {
                                            JOptionPane.showMessageDialog(null, "Médico nao encontrado");
                                        }
                                    }
                                });
                                break;
                            case 8:
                                cl = (CardLayout) (painelPrincipal.getLayout());
                                cl.show(painelPrincipal, "Insercao Codigo/CPF");

                                rotuloInsercao.setText("Insira o código do paciente desejado: ");
                                botao_confirmarInsercao.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        for(ActionListener al : botao_confirmarInsercao.getActionListeners()){
                                            botao_confirmarInsercao.removeActionListener(al);
                                        }

                                        cpf_paciente = Long.parseLong(caixaInsercao.getText());

                                        try {
                                            if (!clinica.existe_paciente(cpf_paciente)) {
                                                throw new MedicoNaoExistente();

                                            } else {
                                                p = clinica.buscar_paciente(cpf_paciente);

                                                cl.show(painelPrincipal, "Insercao Codigo/CPF");
                                                rotuloInsercao.setText("Insira o novo nome para o paciente selecionado: ");
                                                botao_confirmarInsercao.addActionListener(new ActionListener() {
                                                    public void actionPerformed(ActionEvent e) {
                                                        for (ActionListener al : botao_confirmarInsercao.getActionListeners()) {
                                                            botao_confirmarInsercao.removeActionListener(al);
                                                        }

                                                        p.editaNome(caixaInsercao.getText());

                                                        String exibir = "Nome editado com sucesso!";
                                                        System.out.println("certo");
                                                        rotuloResultado.setText(exibir);
                                                        CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                                                        cl.show(painelPrincipal, "Resultado");
                                                    }
                                                });
                                            }
                                        } catch (MedicoNaoExistente ex) {
                                            JOptionPane.showMessageDialog(null, "Médico nao encontrado");
                                        }
                                    }
                                });
                                break;
                            case 9:
                                cl = (CardLayout) (painelPrincipal.getLayout());
                                cl.show(painelPrincipal, "Escolha Nome Arquivo Salvamento");

                                botao_continuarNomeSalvar.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        String nome_arquivo_salvamento_usuario = caixaNomeArquivoSalvar.getText() + ".ser";

                                        try {
                                            clinica.salvar(nome_arquivo_salvamento_usuario);

                                            String exibir = "Arquivo salvo com sucesso!";
                                            System.out.println("certo");
                                            rotuloFinalSalvamento.setText(exibir);
                                            CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                                            cl.show(painelPrincipal, "Painel Final Salvamento");
                                        } catch (IOException ex) {
                                            throw new RuntimeException(ex);
                                        }

                                    }
                                });

                                break;
                        }
                    }

                }
            });

            botao_continuar2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // ButtonModel opcao = grupo_opcoes.getSelection();

                    if (opcao1.isSelected()){
                        System.out.println("escolhido impressao no programa");

                        switch(codigo){
                            case 1:
                                CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                                cl.show(painelPrincipal, "Insercao Codigo/CPF");

                                rotuloInsercao.setText("Insira o código do médico desejado: ");
                                botao_confirmarInsercao.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        for(ActionListener al : botao_confirmarInsercao.getActionListeners()){
                                            botao_confirmarInsercao.removeActionListener(al);
                                        }

                                        codigo_medico = Integer.parseInt(caixaInsercao.getText());

                                        try {
                                            if (!clinica.existe_medico(codigo_medico)) {
                                                throw new MedicoNaoExistente();

                                            } else {
                                                m = clinica.buscar_medico(codigo_medico);
                                                String exibir = "<html>";
                                                for (Paciente paci : m.getPacientes()) {
                                                    exibir += paci.toString() + "<br>";
                                                }
                                                exibir += "</html>";

                                                System.out.println("certo");
                                                rotuloResultado.setText(exibir);
                                                CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                                                cl.show(painelPrincipal, "Resultado");
                                            }
                                        } catch (MedicoNaoExistente ex) {
                                            JOptionPane.showMessageDialog(null, "Médico nao encontrado");
                                        }
                                    }
                                });
                                break;

                            case 2:
                                cl = (CardLayout) (painelPrincipal.getLayout());
                                cl.show(painelPrincipal, "Insercao Codigo/CPF");

                                rotuloInsercao.setText("Insira o código do médico desejado: ");
                                botao_confirmarInsercao.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        for(ActionListener al : botao_confirmarInsercao.getActionListeners()){
                                            botao_confirmarInsercao.removeActionListener(al);
                                        }

                                        codigo_medico = Integer.parseInt(caixaInsercao.getText());

                                        try{
                                            if(!clinica.existe_medico(codigo_medico)){
                                                throw new MedicoNaoExistente();
                                            } else{
                                                m = clinica.buscar_medico(codigo_medico);

                                                data_inicial = LocalDate.parse(JOptionPane.showInputDialog(null, "Digite a data inicial da pesquisa: "), formatoBR);
                                                data_final = LocalDate.parse(JOptionPane.showInputDialog(null, "Digite a data final da pesquisa: "), formatoBR);

                                                ArrayList<Consulta> consultas_ordenadas = new ArrayList<>();
                                                for(Consulta c: m.getConsultas()){
                                                    if(c.getData().compareTo(data_inicial)>=0 && c.getData().compareTo(data_final)<=0){
                                                        consultas_ordenadas.add(c);

                                                    }
                                                }

                                                for(int i = 0; i < consultas_ordenadas.size(); i++){
                                                    for(int j = i + 1; j < consultas_ordenadas.size(); j++){
                                                        if(consultas_ordenadas.get(i).getDataEHorario().compareTo(consultas_ordenadas.get(j).getDataEHorario()) > 0){
                                                            Consulta aux = consultas_ordenadas.get(i);
                                                            consultas_ordenadas.set(i, consultas_ordenadas.get(j));
                                                            consultas_ordenadas.set(j, aux);
                                                        }
                                                    }
                                                }

                                                String exibir = "<html>";
                                                for(Consulta co : consultas_ordenadas){
                                                    exibir += co.toString() + "<br>";
                                                }
                                                exibir += "</html>";


                                                rotuloResultado.setText(exibir);
                                                CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                                                cl.show(painelPrincipal, "Resultado");
                                            }
                                        } catch (MedicoNaoExistente ex){
                                            JOptionPane.showMessageDialog(null, "Médico nao encontrado");
                                        }
                                    }

                                });
                                break;

                            case 3:
                                cl = (CardLayout) (painelPrincipal.getLayout());
                                cl.show(painelPrincipal, "Insercao Codigo/CPF");

                                rotuloInsercao.setText("Insira o CPF do paciente desejado: ");
                                botao_confirmarInsercao.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        for(ActionListener al : botao_confirmarInsercao.getActionListeners()){
                                            botao_confirmarInsercao.removeActionListener(al);
                                        }

                                        cpf_paciente = Long.parseLong(caixaInsercao.getText());

                                        try{
                                            if(!clinica.existe_paciente(cpf_paciente)){
                                                throw new PacienteNaoExistente();
                                            } else{
                                                p = clinica.buscar_paciente(cpf_paciente);

                                                ArrayList<String> medicos_do_paciente = new ArrayList<>();
                                                String exibir = "<html>";
                                                for(Consulta c : p.getConsultas()) {
                                                    for(Medico med : clinica.medicos) {
                                                        if (med.getCodigo() == c.getCodigo()) {
                                                            if (!medicos_do_paciente.contains(med.getNome())){
                                                                medicos_do_paciente.add(med.getNome());
                                                                exibir += c.getCodigo() + " " + med.getNome() + "<br>";
                                                            }
                                                        }
                                                    }
                                                }
                                                exibir += "</html>";

                                                rotuloResultado.setText(exibir);
                                                CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                                                cl.show(painelPrincipal, "Resultado");
                                            }
                                        } catch(PacienteNaoExistente ex){
                                            JOptionPane.showMessageDialog(null, "Paciente nao encontrado");
                                        }



                                    }
                                });
                                break;
                            case 4:
                                cl = (CardLayout) (painelPrincipal.getLayout());
                                cl.show(painelPrincipal, "Insercao Dupla");

                                botao_confirmarInsercaoDupla.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        for(ActionListener al : botao_confirmarInsercaoDupla.getActionListeners()){
                                            botao_confirmarInsercaoDupla.removeActionListener(al);
                                        }

                                        cpf_paciente = Long.parseLong(caixaInsercaoCPF.getText());
                                        codigo_medico = Integer.parseInt(caixaInsercaoCodigo.getText());

                                        if(clinica.existe_paciente(cpf_paciente)){
                                            if(clinica.existe_medico(codigo_medico)){
                                                p = clinica.buscar_paciente(cpf_paciente);
                                                m = clinica.buscar_medico(codigo_medico);

                                                String exibir = "<html>";
                                                for(Consulta c : p.getConsultas()){
                                                    if(codigo_medico == c.getCodigo()){
                                                        if((c.getData().compareTo(hoje) < 0) ){
                                                            exibir += c.getData() + " " + c.getHorario() + "<br>";
                                                        } else if((c.getData().compareTo(hoje) == 0) && (c.getHorario().compareTo(hora) < 0)){
                                                            exibir += c.getData() + " " + c.getHorario() + "<br>";
                                                        }
                                                    }
                                                }
                                                exibir += "</html>";

                                                rotuloResultado.setText(exibir);
                                                CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                                                cl.show(painelPrincipal, "Resultado");

                                            }
                                            else{
                                                JOptionPane.showMessageDialog(null, "Médico nao encontrado");
                                            }
                                        } else{
                                            JOptionPane.showMessageDialog(null, "Paciente nao encontrado");
                                        }

                                    }
                                });
                                break;
                            case 5:
                                cl = (CardLayout) (painelPrincipal.getLayout());
                                cl.show(painelPrincipal, "Insercao Codigo/CPF");

                                rotuloInsercao.setText("Insira o CPF do paciente desejado: ");
                                botao_confirmarInsercao.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        for(ActionListener al : botao_confirmarInsercao.getActionListeners()){
                                            botao_confirmarInsercao.removeActionListener(al);
                                        }

                                        cpf_paciente = Long.parseLong(caixaInsercao.getText());

                                        try{
                                            if(!clinica.existe_paciente(cpf_paciente)){
                                                throw new PacienteNaoExistente();
                                            } else{
                                                p = clinica.buscar_paciente(cpf_paciente);

                                                String exibir = "<html>";
                                                for(Consulta c : p.getConsultas()){
                                                    if ((c.getData().compareTo(hoje) > 0)){
                                                        exibir += c.getData() + " " + c.getHorario() + "<br>";
                                                    } else if((c.getData().compareTo(hoje) == 0) && (c.getHorario().compareTo(hora) > 0)){
                                                        exibir += c.getData() + " " + c.getHorario() + "<br>";
                                                    }
                                                }
                                                exibir += "</html>";

                                                if(exibir.equals("<html></html>")){
                                                    exibir = "O paciente não possui consultas futuras!";
                                                }

                                                rotuloResultado.setText(exibir);
                                                CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                                                cl.show(painelPrincipal, "Resultado");
                                            }
                                        } catch(PacienteNaoExistente ex){
                                            JOptionPane.showMessageDialog(null, "Paciente nao encontrado");
                                        }


                                    }
                                });
                                break;
                            case 6:
                                cl = (CardLayout) (painelPrincipal.getLayout());
                                cl.show(painelPrincipal, "Insercao Codigo/CPF");

                                rotuloInsercao.setText("Insira o código do médico desejado: ");
                                botao_confirmarInsercao.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        for(ActionListener al : botao_confirmarInsercao.getActionListeners()){
                                            botao_confirmarInsercao.removeActionListener(al);
                                        }

                                        codigo_medico = Integer.parseInt(caixaInsercao.getText());

                                        try{
                                            if(!clinica.existe_medico(codigo_medico)){
                                                throw new MedicoNaoExistente();
                                            } else{
                                                m = clinica.buscar_medico(codigo_medico);

                                                intervalo_de_tempo = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite o intervalo de tempo em meses: "));

                                                String exibir = "<html>";
                                                for(Paciente pac : m.getPacientes()){
                                                    LocalDate consulta_mais_recente_paciente_atual = LocalDate.of(0, 1, 1);
                                                    for(Consulta c : m.getConsultas()){
                                                        if(c.getCpf() == pac.getCpf()){
                                                            if (c.getData().compareTo(consulta_mais_recente_paciente_atual) > 0){
                                                                if (c.getData().compareTo(hoje) < 0 ){
                                                                    consulta_mais_recente_paciente_atual = c.getData();
                                                                }
                                                            }
                                                        }
                                                    }

                                                    if ((consulta_mais_recente_paciente_atual.compareTo(hoje.minusMonths(intervalo_de_tempo)) < 0) && (consulta_mais_recente_paciente_atual.getYear() != 0000)){
                                                        exibir += "Paciente " + pac.getNome() + " " + pac.getCpf() + "<br>";
                                                    }
                                                }
                                                exibir += "</html>";

                                                rotuloResultado.setText(exibir);
                                                CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                                                cl.show(painelPrincipal, "Resultado");
                                            }
                                        } catch(MedicoNaoExistente ex){
                                            JOptionPane.showMessageDialog(null, "Médico nao encontrado");
                                        }

                                    }
                                });
                                break;

                        }
                        System.out.println("okei");

                    } else if(opcao2.isSelected()){
                        System.out.println("escolhido impressao em arquivo txt");

                        CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                        cl.show(painelPrincipal, "Escolha Nome Arquivo");
                    }
                }
            });

            botao_continuarNome.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("botao pressionadoooo");

                    nome_arquivo_usuario = caixaNomeArquivo.getText() + ".txt";
                    System.out.println(nome_arquivo_usuario);

                    switch(codigo){
                        case 1:
                            CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                            cl.show(painelPrincipal, "Insercao Codigo/CPF");

                            rotuloInsercao.setText("Insira o código do médico desejado: ");
                            botao_confirmarInsercao.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    for(ActionListener al : botao_confirmarInsercao.getActionListeners()){
                                        botao_confirmarInsercao.removeActionListener(al);
                                    }

                                    codigo_medico = Integer.parseInt(caixaInsercao.getText());

                                    try{
                                        if(!clinica.existe_medico(codigo_medico)){
                                            throw new MedicoNaoExistente();
                                        } else{
                                            m = clinica.buscar_medico(codigo_medico);
                                            FileWriter arquivo_usuario = null;
                                            try {
                                                arquivo_usuario = new FileWriter(nome_arquivo_usuario);
                                            } catch (IOException ex) {
                                                throw new RuntimeException(ex);
                                            }
                                            for(Paciente paci : m.getPacientes()){
                                                try {
                                                    arquivo_usuario.append(paci.toString()+"\n");
                                                } catch (IOException ex) {
                                                    throw new RuntimeException(ex);
                                                }
                                            }
                                            try {
                                                arquivo_usuario.close();
                                            } catch (IOException ex) {
                                                throw new RuntimeException(ex);
                                            }

                                            String exibir = "Arquivo escrito com sucesso!";
                                            System.out.println("caiu aqui tb");
                                            rotuloResultado.setText(exibir);
                                            CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                                            cl.show(painelPrincipal, "Resultado");
                                        }
                                    } catch(MedicoNaoExistente ex){
                                        JOptionPane.showMessageDialog(null, "Médico nao encontrado");
                                    }
                                }
                            });
                            break;
                        case 2:
                            cl = (CardLayout) (painelPrincipal.getLayout());
                            cl.show(painelPrincipal, "Insercao Codigo/CPF");

                            rotuloInsercao.setText("Insira o código do médico desejado: ");
                            botao_confirmarInsercao.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    for(ActionListener al : botao_confirmarInsercao.getActionListeners()){
                                        botao_confirmarInsercao.removeActionListener(al);
                                    }

                                    codigo_medico = Integer.parseInt(caixaInsercao.getText());

                                    try{
                                        if(!clinica.existe_medico(codigo_medico)){
                                            throw new MedicoNaoExistente();
                                        } else{
                                            m = clinica.buscar_medico(codigo_medico);

                                            data_inicial = LocalDate.parse(JOptionPane.showInputDialog(null, "Digite a data inicial da pesquisa: "), formatoBR);
                                            data_final = LocalDate.parse(JOptionPane.showInputDialog(null, "Digite a data final da pesquisa: "), formatoBR);

                                            ArrayList<Consulta> consultas_ordenadas = new ArrayList<>();
                                            for(Consulta c: m.getConsultas()){
                                                if(c.getData().compareTo(data_inicial)>=0 && c.getData().compareTo(data_final)<=0){
                                                    consultas_ordenadas.add(c);

                                                }
                                            }

                                            for(int i = 0; i < consultas_ordenadas.size(); i++){
                                                for(int j = i + 1; j < consultas_ordenadas.size(); j++){
                                                    if(consultas_ordenadas.get(i).getDataEHorario().compareTo(consultas_ordenadas.get(j).getDataEHorario()) > 0){
                                                        Consulta aux = consultas_ordenadas.get(i);
                                                        consultas_ordenadas.set(i, consultas_ordenadas.get(j));
                                                        consultas_ordenadas.set(j, aux);
                                                    }
                                                }
                                            }

                                            FileWriter arquivo_usuario = null;
                                            try {
                                                arquivo_usuario = new FileWriter(nome_arquivo_usuario);
                                            } catch (IOException ex) {
                                                throw new RuntimeException(ex);
                                            }
                                            for(Consulta co : consultas_ordenadas){
                                                try {
                                                    arquivo_usuario.append(co.toString()+"\n");
                                                } catch (IOException ex) {
                                                    throw new RuntimeException(ex);
                                                }
                                            }
                                            try {
                                                arquivo_usuario.close();
                                            } catch (IOException ex) {
                                                throw new RuntimeException(ex);
                                            }
                                            String exibir = "Arquivo escrito com sucesso!";

                                            rotuloResultado.setText(exibir);
                                            CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                                            cl.show(painelPrincipal, "Resultado");
                                        }
                                    }catch(MedicoNaoExistente ex){
                                        JOptionPane.showMessageDialog(null, "Médico nao encontrado");
                                    }


                                }

                            });
                            break;

                        case 3:
                            cl = (CardLayout) (painelPrincipal.getLayout());
                            cl.show(painelPrincipal, "Insercao Codigo/CPF");

                            rotuloInsercao.setText("Insira o CPF do paciente desejado: ");
                            botao_confirmarInsercao.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    for(ActionListener al : botao_confirmarInsercao.getActionListeners()){
                                        botao_confirmarInsercao.removeActionListener(al);
                                    }

                                    cpf_paciente = Long.parseLong(caixaInsercao.getText());

                                    try{
                                        if(!clinica.existe_paciente(cpf_paciente)){
                                            throw new PacienteNaoExistente();
                                        } else{
                                            p = clinica.buscar_paciente(cpf_paciente);

                                            ArrayList<String> medicos_do_paciente = new ArrayList<>();
                                            FileWriter arquivo_usuario = null;
                                            try {
                                                arquivo_usuario = new FileWriter(nome_arquivo_usuario);
                                            } catch (IOException ex) {
                                                throw new RuntimeException(ex);
                                            }
                                            for(Consulta c : p.getConsultas()) {
                                                for(Medico med : clinica.medicos) {
                                                    if (med.getCodigo() == c.getCodigo()) {
                                                        if (!medicos_do_paciente.contains(med.getNome())){
                                                            medicos_do_paciente.add(med.getNome());
                                                            try {
                                                                arquivo_usuario.append(c.getCodigo()+" "+med.getNome()+"\n");
                                                            } catch (IOException ex) {
                                                                throw new RuntimeException(ex);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            try {
                                                arquivo_usuario.close();
                                            } catch (IOException ex) {
                                                throw new RuntimeException(ex);
                                            }

                                            String exibir = "Arquivo escrito com sucesso!";

                                            rotuloResultado.setText(exibir);
                                            CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                                            cl.show(painelPrincipal, "Resultado");
                                        }
                                    } catch(PacienteNaoExistente ex){
                                        JOptionPane.showMessageDialog(null, "Paciente nao encontrado");
                                    }


                                }
                            });
                            break;
                        case 4:
                            cl = (CardLayout) (painelPrincipal.getLayout());
                            cl.show(painelPrincipal, "Insercao Dupla");

                            botao_confirmarInsercaoDupla.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    for(ActionListener al : botao_confirmarInsercaoDupla.getActionListeners()){
                                        botao_confirmarInsercaoDupla.removeActionListener(al);
                                    }

                                    cpf_paciente = Long.parseLong(caixaInsercaoCPF.getText());
                                    codigo_medico = Integer.parseInt(caixaInsercaoCodigo.getText());

                                    if(clinica.existe_paciente(cpf_paciente)){
                                        if(clinica.existe_medico(codigo_medico)){
                                            p = clinica.buscar_paciente(cpf_paciente);
                                            m = clinica.buscar_medico(codigo_medico);


                                            FileWriter arquivo_usuario = null;
                                            try {
                                                arquivo_usuario = new FileWriter(nome_arquivo_usuario);
                                            } catch (IOException ex) {
                                                throw new RuntimeException(ex);
                                            }

                                            for(Consulta c : p.getConsultas()){
                                                if(codigo_medico == c.getCodigo()){
                                                    if((c.getData().compareTo(hoje) < 0) ){
                                                        try {
                                                            arquivo_usuario.append(c.getData() + " " + c.getHorario()+"\n");
                                                        } catch (IOException ex) {
                                                            throw new RuntimeException(ex);
                                                        }
                                                    } else if((c.getData().compareTo(hoje) == 0) && (c.getHorario().compareTo(hora) < 0)){
                                                        try {
                                                            arquivo_usuario.append(c.getData() + " " + c.getHorario()+"\n");
                                                        } catch (IOException ex) {
                                                            throw new RuntimeException(ex);
                                                        }
                                                    }
                                                }
                                            }
                                            try {
                                                arquivo_usuario.close();
                                            } catch (IOException ex) {
                                                throw new RuntimeException(ex);
                                            }

                                            String exibir = "Arquivo escrito com sucesso!";

                                            rotuloResultado.setText(exibir);
                                            CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                                            cl.show(painelPrincipal, "Resultado");

                                        }
                                        else{
                                            JOptionPane.showMessageDialog(null, "Médico nao encontrado");
                                        }
                                    } else{
                                        JOptionPane.showMessageDialog(null, "Paciente nao encontrado");
                                    }

                                }
                            });
                            break;
                        case 5:
                            cl = (CardLayout) (painelPrincipal.getLayout());
                            cl.show(painelPrincipal, "Insercao Codigo/CPF");

                            rotuloInsercao.setText("Insira o CPF do paciente desejado: ");
                            botao_confirmarInsercao.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    for(ActionListener al : botao_confirmarInsercao.getActionListeners()){
                                        botao_confirmarInsercao.removeActionListener(al);
                                    }

                                    cpf_paciente = Long.parseLong(caixaInsercao.getText());

                                    try{
                                        if(!clinica.existe_paciente(cpf_paciente)){
                                            throw new PacienteNaoExistente();
                                        } else{
                                            p = clinica.buscar_paciente(cpf_paciente);

                                            FileWriter arquivo_usuario = null;
                                            try {
                                                arquivo_usuario = new FileWriter(nome_arquivo_usuario);
                                            } catch (IOException ex) {
                                                throw new RuntimeException(ex);
                                            }
                                            for(Consulta c : p.getConsultas()){
                                                if ((c.getData().compareTo(hoje) > 0)){
                                                    try {
                                                        arquivo_usuario.append(c.getData() + " " + c.getHorario()+"\n");
                                                    } catch (IOException ex) {
                                                        throw new RuntimeException(ex);
                                                    }
                                                } else if((c.getData().compareTo(hoje) == 0) && (c.getHorario().compareTo(hora) > 0)){
                                                    try {
                                                        arquivo_usuario.append(c.getData() + " " + c.getHorario()+"\n");
                                                    } catch (IOException ex) {
                                                        throw new RuntimeException(ex);
                                                    }
                                                }
                                            }
                                            try {
                                                arquivo_usuario.close();
                                            } catch (IOException ex) {
                                                throw new RuntimeException(ex);
                                            }
                                            String exibir = "Arquivo escrito com sucesso!";

                                            rotuloResultado.setText(exibir);
                                            CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                                            cl.show(painelPrincipal, "Resultado");
                                        }
                                    } catch(PacienteNaoExistente ex){
                                        JOptionPane.showMessageDialog(null, "Paciente nao encontrado");
                                    }


                                }
                            });
                            break;
                        case 6:
                            cl = (CardLayout) (painelPrincipal.getLayout());
                            cl.show(painelPrincipal, "Insercao Codigo/CPF");

                            rotuloInsercao.setText("Insira o código do médico desejado: ");
                            botao_confirmarInsercao.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    for(ActionListener al : botao_confirmarInsercao.getActionListeners()){
                                        botao_confirmarInsercao.removeActionListener(al);
                                    }

                                    codigo_medico = Integer.parseInt(caixaInsercao.getText());

                                    try{
                                        if(!clinica.existe_medico(codigo_medico)){
                                            throw new MedicoNaoExistente();
                                        } else{
                                            m = clinica.buscar_medico(codigo_medico);

                                            intervalo_de_tempo = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite o intervalo de tempo em meses: "));

                                            FileWriter arquivo_usuario = null;
                                            try {
                                                arquivo_usuario = new FileWriter(nome_arquivo_usuario);
                                            } catch (IOException ex) {
                                                throw new RuntimeException(ex);
                                            }
                                            for(Paciente pac : m.getPacientes()){
                                                LocalDate consulta_mais_recente_paciente_atual = LocalDate.of(0, 1, 1);
                                                for(Consulta c : m.getConsultas()){
                                                    if(c.getCpf() == pac.getCpf()){
                                                        if (c.getData().compareTo(consulta_mais_recente_paciente_atual) > 0){
                                                            if (c.getData().compareTo(hoje) < 0 ){
                                                                consulta_mais_recente_paciente_atual = c.getData();
                                                            }
                                                        }
                                                    }
                                                }

                                                if ((consulta_mais_recente_paciente_atual.compareTo(hoje.minusMonths(intervalo_de_tempo)) < 0) && (consulta_mais_recente_paciente_atual.getYear() != 0000)){
                                                    try {
                                                        arquivo_usuario.append("Paciente " + pac.getNome() + " " + pac.getCpf()+"\n");
                                                    } catch (IOException ex) {
                                                        throw new RuntimeException(ex);
                                                    }
                                                }
                                            }
                                            try {
                                                arquivo_usuario.close();
                                            } catch (IOException ex) {
                                                throw new RuntimeException(ex);
                                            }

                                            CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                                            cl.show(painelPrincipal, "Resultado");
                                        }
                                    } catch(MedicoNaoExistente ex){
                                        JOptionPane.showMessageDialog(null, "Médico nao encontrado");
                                    }
                                }
                            });
                            break;
                    }
                }
            });

            botao_voltar_escolha.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                    cl.previous(painelPrincipal);
                    grupo_opcoes.clearSelection();
                }
            });

            botao_voltar_arquivo.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                    cl.previous(painelPrincipal);
                    grupo_opcoes.clearSelection();
                }
            });

            botao_voltar_arquivoSalvar.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                    cl.previous(painelPrincipal);
                    grupo_opcoes.clearSelection();
                }
            });

            botao_voltar_insercao.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                    cl.show(painelPrincipal, "Escolha Saida");
                    grupo_opcoes.clearSelection();
                }
            });

            botao_voltar_insercao_dupla.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                    cl.show(painelPrincipal, "Escolha Saida");
                    grupo_opcoes.clearSelection();
                }
            });

            botao_finalizar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    CardLayout cl = (CardLayout) (painelPrincipal.getLayout());
                    cl.show(painelPrincipal, "Menu");
                    grupo_opcoes.clearSelection();
                    comboBox_opcoes.setSelectedIndex(0);
                }
            });

            botao_finalSalvamento.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                }
            });

            botao_encerrar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                }
            });

        } catch (IOException e) {

            System.out.println("Excecao de I/O");
            e.printStackTrace();

        } catch (ClassNotFoundException e) {

            System.out.println("Excecao de classe desconhecida");
            e.printStackTrace();
        }


    }
}