package pizzaria.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private static final String URL = "jdbc:mysql://localhost:3306/pizzaria?useSSL=false&serverTimezone=UTC";

    private static final String USER = "root";

    private static final String PASS = "Jpss147028@";

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro na conexão com o Banco de Dados: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        try (Connection conn = ConnectionFactory.getConnection()) {
            if (conn != null) {
                System.out.println("certo");
                conn.close();
            } else {
                System.err.println("falhou");
            }
        } catch (SQLException e) {
            System.err.println("erro de conexão ao banco");
            e.printStackTrace();
        }
    }
}