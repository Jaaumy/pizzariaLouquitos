package pizzaria.dao;

import pizzaria.model.Cliente;
import pizzaria.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class UsuarioDAO {

    public Usuario validarLogin(String email, String senha) {

        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, senha);
            rs = ps.executeQuery();

            if (rs.next()) {
                Usuario usuarioLogado = new Usuario();
                usuarioLogado.setIdUsuario(rs.getInt("id_usuario"));
                usuarioLogado.setNome(rs.getString("nome"));
                usuarioLogado.setEmail(rs.getString("email"));
                usuarioLogado.setAdmin(rs.getBoolean("is_admin"));

                return usuarioLogado;
            } else {
                return null;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao tentar validar login: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void cadastrarUsuario(Usuario novoUsuario) {

        String sqlUsuario = "INSERT INTO usuarios (nome, email, senha, is_admin) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement psUsuario = null;

        try {
            conn = ConnectionFactory.getConnection();

            psUsuario = conn.prepareStatement(sqlUsuario);
            psUsuario.setString(1, novoUsuario.getNome());
            psUsuario.setString(2, novoUsuario.getEmail());
            psUsuario.setString(3, novoUsuario.getSenha());
            psUsuario.setBoolean(4, novoUsuario.isAdmin());
            psUsuario.executeUpdate();

            JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(null, "Erro: O e-mail '" + novoUsuario.getEmail() + "' já está cadastrado.");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao cadastrar usuário: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            try {
                if (psUsuario != null) psUsuario.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int contarTotalClientes() {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE is_admin = false";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public List<Usuario> listarTodosClientes() {
        List<Usuario> clientes = new ArrayList<>();
        String sql = "SELECT * FROM vw_admin_clientes";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));

                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setIdUsuarioRef(rs.getInt("id_usuario_ref"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setDataNascimento(rs.getDate("data_nascimento"));

                usuario.setCliente(cliente);
                clientes.add(usuario);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar clientes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return clientes;
    }
}