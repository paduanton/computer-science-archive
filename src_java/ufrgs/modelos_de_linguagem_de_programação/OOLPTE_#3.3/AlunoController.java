import java.util.*;
import java.io.*;
import java.time.format.DateTimeFormatter;

public class AlunoController {
    private final List<Pessoa> alunos = new ArrayList<>();
    private final File persistFile = new File("alunos.csv");
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public List<Pessoa> getAlunos() { return alunos; }

    public void addAluno(Pessoa p) throws ValidationException {
        if (!(p instanceof Aluno)) throw new ValidationException("Esperado Aluno.");
        Aluno a = (Aluno) p;
        if (matriculaExists(a.getMatricula())) throw new ValidationException("Matrícula já cadastrada.");
        alunos.add(a);
    }

    public boolean matriculaExists(String mat) {
        if (mat == null) return false;
        for (Pessoa p : alunos) if (p instanceof Aluno && ((Aluno)p).getMatricula().equals(mat)) return true;
        return false;
    }

    public void persist() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(persistFile))) {
            pw.println("nome;nascimento;genero;matricula;dataMatricula");
            for (Pessoa p : alunos) {
                if (p instanceof Aluno) {
                    Aluno a = (Aluno)p;
                    String line = String.join(";", escape(a.getNome()), a.getDtNascimento().format(fmt),
                                              a.getGenero().name(), a.getMatricula(), a.getDtMatricula().format(fmt));
                    pw.println(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao gravar arquivo: " + e.getMessage());
        }
    }

    private String escape(String s) {
        return s.replace(";", ",");
    }
}
