%%writefile AlunoForm.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AlunoForm extends JFrame {
    private final JTextField tfNome = new JTextField(22);
    private final JTextField tfNascimento = new JTextField(16);
    private final JComboBox<Pessoa.GENERO> cbGenero = new JComboBox<>(Pessoa.GENERO.values());
    private final JTextField tfMatricula = new JTextField(10);
    private final JTextField tfDataMatricula = new JTextField(16);
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> listaVisual = new JList<>(listModel);

    private final AlunoController controller = new AlunoController();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public AlunoForm() {
        super("Cadastro de Alunos");
        initLayout();
        initListeners();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initLayout() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6,6,6,6);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = 0; form.add(new JLabel("Nome:"), g);
        g.gridx = 1; form.add(tfNome, g);

        g.gridx = 0; g.gridy = 1; form.add(new JLabel("Nascimento (dd-MM-yyyy HH:mm):"), g);
        g.gridx = 1; form.add(tfNascimento, g);

        g.gridx = 0; g.gridy = 2; form.add(new JLabel("Gênero:"), g);
        g.gridx = 1; form.add(cbGenero, g);

        g.gridx = 0; g.gridy = 3; form.add(new JLabel("Matrícula (8 dígitos):"), g);
        g.gridx = 1; form.add(tfMatricula, g);

        g.gridx = 0; g.gridy = 4; form.add(new JLabel("Data Matrícula (dd-MM-yyyy HH:mm):"), g);
        g.gridx = 1; form.add(tfDataMatricula, g);

        JPanel south = new JPanel(new BorderLayout());
        JButton btnAdd = new JButton("Incluir Aluno");
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pBtn.add(btnAdd);
        south.add(pBtn, BorderLayout.NORTH);
        JScrollPane sp = new JScrollPane(listaVisual);
        sp.setPreferredSize(new Dimension(640, 200));
        south.add(sp, BorderLayout.CENTER);

        c.add(form, BorderLayout.NORTH);
        c.add(south, BorderLayout.CENTER);

        listaVisual.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        btnAdd.addActionListener(e -> onIncluir());
    }

    private void initListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });
    }

    private void onIncluir() {
        try {
            Aluno a = buildAlunoFromForm();
            controller.addAluno(a);
            listModel.addElement(a.toString());
            clearForm();
            JOptionPane.showMessageDialog(this, "Aluno incluído.", "OK", JOptionPane.INFORMATION_MESSAGE);
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro inesperado.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Aluno buildAlunoFromForm() throws ValidationException {
        String nome = tfNome.getText().trim();
        if (nome.isEmpty()) throw new ValidationException("Nome obrigatório.");
        LocalDateTime nasc;
        try { nasc = LocalDateTime.parse(tfNascimento.getText().trim(), fmt); }
        catch (DateTimeParseException ex) { throw new ValidationException("Data de nascimento inválida. Use dd-MM-yyyy HH:mm"); }
        Pessoa.GENERO genero = (Pessoa.GENERO) cbGenero.getSelectedItem();
        String mat = tfMatricula.getText().trim();
        if (!mat.matches("\\d{8}")) throw new ValidationException("Matrícula deve ter 8 dígitos numéricos.");
        LocalDateTime dtMat;
        try { dtMat = LocalDateTime.parse(tfDataMatricula.getText().trim(), fmt); }
        catch (DateTimeParseException ex) { throw new ValidationException("Data de matrícula inválida. Use dd-MM-yyyy HH:mm"); }

        Aluno a = new Aluno(nome, nasc, genero, dtMat, mat);
        return a;
    }

    private void clearForm() {
        tfNome.setText("");
        tfNascimento.setText("");
        tfMatricula.setText("");
        tfDataMatricula.setText("");
        cbGenero.setSelectedIndex(0);
        tfNome.requestFocusInWindow();
    }

    private void onClose() {
        if (!controller.getAlunos().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Alunos cadastrados:\n");
            for (Pessoa p : controller.getAlunos()) sb.append(p.toString()).append("\n");
            System.out.println(sb.toString());
            JTextArea ta = new JTextArea(sb.toString());
            ta.setEditable(false);
            JScrollPane sp = new JScrollPane(ta);
            sp.setPreferredSize(new Dimension(700, 300));
            JOptionPane.showMessageDialog(this, sp, "Resumo ao fechar", JOptionPane.INFORMATION_MESSAGE);
        }
        controller.persist();
        dispose();
        System.exit(0);
    }
}
