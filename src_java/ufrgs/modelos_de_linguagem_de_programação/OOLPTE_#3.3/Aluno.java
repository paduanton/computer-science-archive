import java.time.*;

public class Aluno extends Pessoa {
    private String matricula = "INDEFINIDO";
    private LocalDateTime dt_matricula = LocalDateTime.now();

    public Aluno() { super(); }

    public Aluno(final String nome, final LocalDateTime nascimento, final GENERO genero,
                 final LocalDateTime dtMatricula, final String matricula) throws ValidationException {
        super(nome, nascimento, genero);
        setDtMatricula(dtMatricula);
        setMatricula(matricula);
    }

    @Override
    public String toString() {
        return "[Aluno] " + super.toString() + " | Matrícula: " + getMatricula() +
               " | DataMatricula: " + getDtMatricula().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public String getMatricula() { return this.matricula; }

    public LocalDateTime getDtMatricula() { return this.dt_matricula; }

    public void setMatricula(final String matricula) throws ValidationException {
        if (matricula == null) throw new ValidationException("Matrícula não pode ser nula.");
        String m = matricula.trim();
        if (!m.matches("\\d{8}")) throw new ValidationException("Matrícula deve conter exatamente 8 dígitos numéricos.");
        this.matricula = m;
    }

    public void setDtMatricula(final LocalDateTime dtMatricula) throws ValidationException {
        if (dtMatricula == null) throw new ValidationException("Data de matrícula não pode ser nula.");
        if (dtMatricula.isAfter(LocalDateTime.now())) throw new ValidationException("Data de matrícula não pode ser futura.");
        if (dtMatricula.isBefore(getDtNascimento())) throw new ValidationException("Data de matrícula não pode ser anterior ao nascimento.");
        this.dt_matricula = dtMatricula;
    }
}
