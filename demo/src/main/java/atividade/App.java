package atividade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class App {

    // Scanner único para toda a aplicação
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        int opcao;

        do {
            exibirMenu();
            try {
                opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 0:
                        salvarProduto();
                        break;
                    case 1:
                        buscarTodosProdutos();
                        break;
                    case 2:
                        buscarProdutoPorId();
                        break;
                    case 3:
                        atualizarProduto();
                        break;
                    case 4:
                        excluirProduto();
                        break;
                    case 5: {
                        System.out.println("Saindo do programa...");
                        // Fechando o scanner antes de sair
                        scanner.close();
                        System.exit(0);
                        break;
                    }
                    default:
                        System.out.println("Opção inválida!");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Por favor, digite um número.");
                opcao = -1; // Define uma opção inválida para continuar o loop
            }
        } while (opcao != 5);
    }

    // conecta com o banco

    private static Connection conectar() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/dbproduto";
        String user = "root";
        String password = "root";
        return DriverManager.getConnection(url, user, password);
    }

    private static void exibirMenu() {
        System.out.println("\n### Menu de Operações ###");
        System.out.println("0. Salvar novo produto");
        System.out.println("1. Buscar todos produtos");
        System.out.println("2. Buscar produto por ID");
        System.out.println("3. Atualizar produto");
        System.out.println("4. Excluir produto");
        System.out.println("5. Sair do programa");
        System.out.print("Escolha uma opção: ");
    }
    
    // 0 salvar novo produto 

    private static void salvarProduto() {
        System.out.println("\n### Criar Novo Produto ###");
        System.out.print("Digite o nome do produto: ");
        String nome = scanner.nextLine();
        System.out.print("Digite a quantidade: ");
        int quantidade = Integer.parseInt(scanner.nextLine());
        System.out.print("Digite o valor: ");
        double valor = Double.parseDouble(scanner.nextLine());

        String sql = "INSERT INTO produto (nome, quantidade, valor) VALUES (?, ?, ?)";

        try (Connection connection = conectar();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setInt(2, quantidade);
            pstmt.setDouble(3, valor);

            int linhasAfetadas = pstmt.executeUpdate();
            System.out.println("Produto inserido com sucesso. Linhas afetadas: " + linhasAfetadas);

        } catch (SQLException e) {
            System.err.println("Erro ao salvar o produto: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Erro: Quantidade e valor devem ser números.");
        }
    }

    // 1 busca todos os produtos

    private static void buscarTodosProdutos() {
        System.out.println("\n### Buscar Todos os Produtos ###");
        String sql = "SELECT * FROM produto";

        try (Connection connection = conectar();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (!rs.isBeforeFirst()) {
                System.out.println("Nenhum produto encontrado.");
            } else {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    int quantidade = rs.getInt("quantidade");
                    double valor = rs.getDouble("valor");
                    System.out.printf("ID: %d | Nome: %s | Quantidade: %d | Valor: %.2f\n", id, nome, quantidade, valor);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os produtos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 2 busca produtos por id

    private static void buscarProdutoPorId() {
        System.out.println("\n### Buscar Produto por ID ###");
        System.out.print("Digite o ID do produto: ");
        try {
            int idBusca = Integer.parseInt(scanner.nextLine());
            String sql = "SELECT * FROM produto WHERE id = ?";

            try (Connection connection = conectar();
                 PreparedStatement pstmt = connection.prepareStatement(sql)) {

                pstmt.setInt(1, idBusca);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        String nome = rs.getString("nome");
                        int quantidade = rs.getInt("quantidade");
                        double valor = rs.getDouble("valor");
                        System.out.println("Produto encontrado:");
                        System.out.printf("ID: %d | Nome: %s | Quantidade: %d | Valor: %.2f\n", id, nome, quantidade, valor);
                    } else {
                        System.out.println("Nenhum produto encontrado com o ID: " + idBusca);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto por ID: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Erro: O ID deve ser um número.");
        }
    }

    // 3 atualiza produtos existentes

    private static void atualizarProduto() {
        System.out.println("\n### Atualizar Produto ###");
        System.out.print("Digite o ID do produto para atualizar: ");
        try {
            int idUpdate = Integer.parseInt(scanner.nextLine());
            System.out.print("Digite o novo nome: ");
            String novoNome = scanner.nextLine();
            System.out.print("Digite a nova quantidade: ");
            int novaQuantidade = Integer.parseInt(scanner.nextLine());
            System.out.print("Digite o novo valor: ");
            double novoValor = Double.parseDouble(scanner.nextLine());

            String sql = "UPDATE produto SET nome = ?, quantidade = ?, valor = ? WHERE id = ?";

            try (Connection connection = conectar();
                 PreparedStatement pstmt = connection.prepareStatement(sql)) {

                pstmt.setString(1, novoNome);
                pstmt.setInt(2, novaQuantidade);
                pstmt.setDouble(3, novoValor);
                pstmt.setInt(4, idUpdate);

                int linhasAfetadas = pstmt.executeUpdate();
                if (linhasAfetadas > 0) {
                    System.out.println("Produto atualizado com sucesso!");
                } else {
                    System.out.println("Nenhum produto encontrado com o ID: " + idUpdate);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar o produto: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Erro: ID, quantidade e valor devem ser números.");
        }
    }

    // 4 exclui produto.

    private static void excluirProduto() {
        System.out.println("\n### Excluir Produto ###");
        System.out.print("Digite o ID do produto para excluir: ");
        try {
            int idDelete = Integer.parseInt(scanner.nextLine());
            String sql = "DELETE FROM produto WHERE id = ?";

            try (Connection connection = conectar();
                 PreparedStatement pstmt = connection.prepareStatement(sql)) {

                pstmt.setInt(1, idDelete);
                int linhasAfetadas = pstmt.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Produto excluído com sucesso!");
                } else {
                    System.out.println("Nenhum produto encontrado com o ID: " + idDelete);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao excluir o produto: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Erro: O ID deve ser um número.");
        }
    }
}