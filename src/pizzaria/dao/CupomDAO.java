package pizzaria.dao;

import pizzaria.model.Cupom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CupomDAO {

    public Cupom validarCupom(String codigo) {
        String sql = "SELECT * FROM cupons WHERE codigo = ? AND ativo = TRUE AND (data_validade IS NULL OR data_validade >= CURDATE())";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, codigo);
            rs = ps.executeQuery();

            if (rs.next()) {
                Cupom cupom = new Cupom();
                cupom.setIdCupom(rs.getInt("id_cupom"));
                cupom.setCodigo(rs.getString("codigo"));
                cupom.setTipoDesconto(rs.getString("tipo_desconto"));
                cupom.setValor(rs.getDouble("valor"));
                cupom.setDataValidade(rs.getDate("data_validade"));
                cupom.setAtivo(rs.getBoolean("ativo"));
                return cupom;
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
        return null;
    }

    public void adicionarCupom(Cupom cupom) {
        String sql = "INSERT INTO cupons (codigo, tipo_desconto, valor, data_validade, ativo) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, cupom.getCodigo());
            ps.setString(2, cupom.getTipoDesconto());
            ps.setDouble(3, cupom.getValor());
            if (cupom.getDataValidade() != null) {
                ps.setDate(4, new java.sql.Date(cupom.getDataValidade().getTime()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.setBoolean(5, cupom.isAtivo());
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Cupom '" + cupom.getCodigo() + "' criado com sucesso!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao criar cupom: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Cupom> listarTodosCupons() {
        List<Cupom> cupons = new ArrayList<>();
        String sql = "SELECT * FROM cupons ORDER BY ativo DESC, data_validade";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionFactory.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Cupom cupom = new Cupom();
                cupom.setIdCupom(rs.getInt("id_cupom"));
                cupom.setCodigo(rs.getString("codigo"));
                cupom.setTipoDesconto(rs.getString("tipo_desconto"));
                cupom.setValor(rs.getDouble("valor"));
                cupom.setDataValidade(rs.getDate("data_validade"));
                cupom.setAtivo(rs.getBoolean("ativo"));
                cupons.add(cupom);
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
        return cupons;
    }
}