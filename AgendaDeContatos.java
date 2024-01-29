import java.io.*;
import java.util.*;

class Contato implements Serializable {
    private static Long contadorIds = 1L;
    private Long id;
    private String nome;
    private String sobreNome;
    private List<Telefone> telefones;

    public Contato() {
        this.id = contadorIds++;
        this.telefones = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobreNome() {
        return sobreNome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }

    public List<Telefone> getTelefones() {
        return telefones;
    }

    public void adicionarTelefone(Telefone telefone) {
        if (!telefoneJaCadastrado(telefone)) {
            telefones.add(telefone);
        }
    }

    private boolean telefoneJaCadastrado(Telefone telefone) {
        for (Telefone t : telefones) {
            if (t.getDdd().equals(telefone.getDdd()) && t.getNumero().equals(telefone.getNumero())) {
                return true;
            }
        }
        return false;
    }
}

class Telefone implements Serializable {
    private String ddd;
    private Long numero;

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }
}

public class AgendaDeContatos {
    private List<Contato> contatos;
    private static final String ARQUIVO_CONTATOS = "contatos.txt";

    public AgendaDeContatos() {
        contatos = new ArrayList<>();
        carregarContatos();
    }

    public static void main(String[] args) {
        AgendaDeContatos agenda = new AgendaDeContatos();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("##################");
            System.out.println("##### AGENDA #####");
            System.out.println("##################");
            System.out.println(">>>> Contatos <<<<");
            System.out.println("Id | Nome");

            for (Contato contato : agenda.contatos) {
                System.out.println(contato.getId() + " | " + contato.getNome() + " " + contato.getSobreNome());
            }

            System.out.println(">>>> Menu <<<<");
            System.out.println("1 - Adicionar Contato");
            System.out.println("2 - Remover Contato");
            System.out.println("3 - Editar Contato");
            System.out.println("4 - Sair");

            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    agenda.adicionarContato(scanner);
                    break;
                case 2:
                    agenda.removerContato(scanner);
                    break;
                case 3:
                    agenda.editarContato(scanner);
                    break;
                case 4:
                    System.out.println("Saindo do programa.");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    public void adicionarContato(Scanner scanner) {
        Contato novoContato = new Contato();
        System.out.print("Informe o nome: ");
        String nome = scanner.next();
        System.out.print("Informe o sobrenome: ");
        String sobreNome = scanner.next();

        novoContato.setNome(nome);
        novoContato.setSobreNome(sobreNome);

        System.out.print("Informe a quantidade de telefones: ");
        int qtdTelefones = scanner.nextInt();

        for (int i = 0; i < qtdTelefones; i++) {
            Telefone telefone = new Telefone();
            System.out.print("Informe o DDD do telefone " + (i + 1) + ": ");
            String ddd = scanner.next();
            System.out.print("Informe o número do telefone " + (i + 1) + ": ");
            Long numero = scanner.nextLong();

            telefone.setDdd(ddd);
            telefone.setNumero(numero);

            novoContato.adicionarTelefone(telefone);
        }

        if (!contatoComMesmoId(novoContato.getId())) {
            contatos.add(novoContato);
            salvarContatos();
            System.out.println("Contato adicionado com sucesso!");
        } else {
            System.out.println("Já existe um contato com o mesmo ID.");
        }
    }

    public void removerContato(Scanner scanner) {
        System.out.print("Informe o ID do contato que deseja remover: ");
        Long id = scanner.nextLong();

        Contato contatoParaRemover = null;

        for (Contato contato : contatos) {
            if (contato.getId().equals(id)) {
                contatoParaRemover = contato;
                break;
            }
        }

        if (contatoParaRemover != null) {
            contatos.remove(contatoParaRemover);
            salvarContatos();
            System.out.println("Contato removido com sucesso!");
        } else {
            System.out.println("Contato não encontrado.");
        }
    }

    public void editarContato(Scanner scanner) {
        System.out.print("Informe o ID do contato que deseja editar: ");
        Long id = scanner.nextLong();

        Contato contatoParaEditar = null;

        for (Contato contato : contatos) {
            if (contato.getId().equals(id)) {
                contatoParaEditar = contato;
                break;
            }
        }

        if (contatoParaEditar != null) {
            System.out.println("Informe os novos detalhes do contato:");
            System.out.print("Nome: ");
            String novoNome = scanner.next();
            System.out.print("Sobrenome: ");
            String novoSobreNome = scanner.next();

            contatoParaEditar.setNome(novoNome);
            contatoParaEditar.setSobreNome(novoSobreNome);

            // Limpa a lista de telefones existentes
            contatoParaEditar.getTelefones().clear();

            System.out.print("Informe a quantidade de telefones: ");
            int qtdTelefones = scanner.nextInt();

            for (int i = 0; i < qtdTelefones; i++) {
                Telefone telefone = new Telefone();
                System.out.print("Informe o DDD do telefone " + (i + 1) + ": ");
                String ddd = scanner.next();
                System.out.print("Informe o número do telefone " + (i + 1) + ": ");
                Long numero = scanner.nextLong();

                telefone.setDdd(ddd);
                telefone.setNumero(numero);

                contatoParaEditar.adicionarTelefone(telefone);
            }

            salvarContatos();
            System.out.println("Contato editado com sucesso!");
        } else {
            System.out.println("Contato não encontrado.");
        }
    }

    private boolean contatoComMesmoId(Long id) {
        for (Contato contato : contatos) {
            if (contato.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private void carregarContatos() {
        try {
            File arquivo = new File(ARQUIVO_CONTATOS);
            if (arquivo.exists()) {
                FileInputStream fis = new FileInputStream(arquivo);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Object objetoLido = ois.readObject();
                if (objetoLido instanceof List<?>) {
                    contatos = (List<Contato>) objetoLido;
                }
                ois.close();
                fis.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void salvarContatos() {
        try {
            File arquivo = new File(ARQUIVO_CONTATOS);
            FileOutputStream fos = new FileOutputStream(arquivo);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(contatos);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
