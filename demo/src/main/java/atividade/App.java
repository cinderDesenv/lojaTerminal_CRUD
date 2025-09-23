package atividade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement; // Import adicionado
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            exibirMenu();
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
                    scanner.close();
                    System.exit(0);
                    break;
                }
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (opcao != 5);
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

    private static void salvarProduto() {
        System.out.println("\n### Criar Novo Produto ###");
        
        // conexão com banco
        
        String url = "jdbc:mysql://localhost:3306/dbproduto";

        try (Connection connection = DriverManager.getConnection(url, "root", "root")) {
            System.out.println("banco conectado");

            // inserindo produto na colonua produto
            
            String sql = "insert into produto (nome, quantidade, valor) values ('Macarao', 1, 3.5)";
            try(Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(sql);
            }
            
            // O try-with-resources garante que o Statement será fechado automaticamente.
            try(Statement stmt = connection.createStatement()) {
                int linhasAfetadas = stmt.executeUpdate(sql);
                System.out.println("Produto inserido. Linhas afetadas: " + linhasAfetadas);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados ou executar a query: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void buscarTodosProdutos() {
        System.out.println("\n### Buscar Todos ###");
        // TODO- Implementar buscar todos
    }

    private static void buscarProdutoPorId() {
        System.out.println("\n### Buscar Produto por ID ###");
        // TODO- Implementar busca por id
    }

    private static void atualizarProduto() {
        System.out.println("\n### Atualizar Produto ###");
        // TODO- Implementar atualizar
    }

    private static void excluirProduto() {
        System.out.println("\n### Excluir Produto ###");
        // TODO- Implementar excluir
    }
}