import java.time.*;
import java.time.format.DateTimeFormatter;

public class Pessoa {
    public enum GENERO { INDEFINIDO, HOMEM_CIS, MULHER_CIS, NAO_BINARIO, TRANSGENERO };

    private String nome = "INDEFINIDO";
    private LocalDateTime dt_nascimento = LocalDateTime.now();
    private GENERO genero = GENERO.INDEFINIDO;

    public Pessoa() { }

    public Pessoa(final String nome, final LocalDateTime nascimento, final GENERO genero) throws ValidationException {
        setGenero(genero);
        setDtNascimento(nascimento);
        setNome(nome);
    }

    public String toString(){
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDate = this.getDtNascimento().format(myFormatObj);
        return "[Pessoa] Nome: " + this.getNome() + " | Nascimento: " + formattedDate + " | Gênero: " + this.getGenero();
    }

    public String getNome(){ return this.nome; }

    public LocalDateTime getDtNascimento() { return this.dt_nascimento; }

    public GENERO getGenero(){ return this.genero; }

    public void setNome(final String nome) throws ValidationException {
        if (nome == null) throw new ValidationException("Nome não pode ser nulo.");
        String n = nome.trim();
        if (n.isEmpty()) throw new ValidationException("Nome não pode ser vazio.");
        if (!n.contains(" ")) throw new ValidationException("Nome deve ter pelo menos nome e sobrenome.");
        if (n.matches(".*\\d.*")) throw new ValidationException("Nome não pode conter dígitos.");
        this.nome = n.replaceAll("\\s+", " ");
    }

    public void setDtNascimento(final LocalDateTime nascimento) throws ValidationException {
        if (nascimento == null) throw new ValidationException("Data de nascimento não pode ser nula.");
        if (nascimento.isAfter(LocalDateTime.now())) throw new ValidationException("Data de nascimento não pode ser futura.");
        this.dt_nascimento = nascimento;
    }

    public void setGenero(final GENERO genero) throws ValidationException {
        if (genero == null) throw new ValidationException("Gênero inválido.");
        this.genero = genero;
    }
}
