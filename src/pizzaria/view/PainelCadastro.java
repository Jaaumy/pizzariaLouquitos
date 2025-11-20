package pizzaria.view;

import pizzaria.dao.UsuarioDAO;
import pizzaria.model.Cliente;
import pizzaria.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PainelCadastro extends JPanel {

    private MainFrame mainFrame;
    private JTextField txtNome;
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JPasswordField txtConfirmarSenha;
    private JTextField txtTelefone;
    private JButton btnSalvar;
    private JButton btnVoltarLogin;

    private UsuarioDAO usuarioDAO;

    public PainelCadastro(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.usuarioDAO = new UsuarioDAO();

        setLayout(new GridBagLayout());
        setBackground(TemaLouquitos.VERDE_PIZZARIA);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Novo Cadastro na Louquitos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(TemaLouquitos.BRANCO_PADRAO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        JLabel lblNome = new JLabel("Nome Completo:");
        lblNome.setFont(new Font("Arial", Font.PLAIN, 16));
        lblNome.setForeground(TemaLouquitos.BRANCO_PADRAO);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(lblNome, gbc);

        txtNome = new JTextField(20);
        txtNome.setFont(new Font("Arial", Font.PLAIN, 16));
        txtNome.setBackground(TemaLouquitos.CINZA_CLARO);
        txtNome.setBorder(BorderFactory.createLineBorder(TemaLouquitos.VERMELHO_PIZZARIA, 2));
        gbc.gridx = 1;
        add(txtNome, gbc);

        JLabel lblEmail = new JLabel("E-mail:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 16));
        lblEmail.setForeground(TemaLouquitos.BRANCO_PADRAO);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblEmail, gbc);

        txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 16));
        txtEmail.setBackground(TemaLouquitos.CINZA_CLARO);
        txtEmail.setBorder(BorderFactory.createLineBorder(TemaLouquitos.VERMELHO_PIZZARIA, 2));
        gbc.gridx = 1;
        add(txtEmail, gbc);

        JLabel lblTelefone = new JLabel("Telefone (Opcional):");
        lblTelefone.setFont(new Font("Arial", Font.PLAIN, 16));
        lblTelefone.setForeground(TemaLouquitos.BRANCO_PADRAO);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(lblTelefone, gbc);

        txtTelefone = new JTextField(20);
        txtTelefone.setFont(new Font("Arial", Font.PLAIN, 16));
        txtTelefone.setBackground(TemaLouquitos.CINZA_CLARO);
        txtTelefone.setBorder(BorderFactory.createLineBorder(TemaLouquitos.VERMELHO_PIZZARIA, 2));
        gbc.gridx = 1;
        add(txtTelefone, gbc);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSenha.setForeground(TemaLouquitos.BRANCO_PADRAO);
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(lblSenha, gbc);

        txtSenha = new JPasswordField(20);
        txtSenha.setFont(new Font("Arial", Font.PLAIN, 16));
        txtSenha.setBackground(TemaLouquitos.CINZA_CLARO);
        txtSenha.setBorder(BorderFactory.createLineBorder(TemaLouquitos.VERMELHO_PIZZARIA, 2));
        gbc.gridx = 1;
        add(txtSenha, gbc);

        JLabel lblConfirmarSenha = new JLabel("Confirmar Senha:");
        lblConfirmarSenha.setFont(new Font("Arial", Font.PLAIN, 16));
        lblConfirmarSenha.setForeground(TemaLouquitos.BRANCO_PADRAO);
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(lblConfirmarSenha, gbc);

        txtConfirmarSenha = new JPasswordField(20);
        txtConfirmarSenha.setFont(new Font("Arial", Font.PLAIN, 16));
        txtConfirmarSenha.setBackground(TemaLouquitos.CINZA_CLARO);
        txtConfirmarSenha.setBorder(BorderFactory.createLineBorder(TemaLouquitos.VERMELHO_PIZZARIA, 2));
        gbc.gridx = 1;
        add(txtConfirmarSenha, gbc);

        btnSalvar = new JButton("Salvar Cadastro");
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 18));
        btnSalvar.setBackground(TemaLouquitos.VERMELHO_PIZZARIA);
        btnSalvar.setForeground(TemaLouquitos.BRANCO_PADRAO);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorder(BorderFactory.createLineBorder(TemaLouquitos.BRANCO_PADRAO, 2));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(btnSalvar, gbc);

        btnVoltarLogin = new JButton("Voltar para Login");
        btnVoltarLogin.setFont(new Font("Arial", Font.PLAIN, 14));
        btnVoltarLogin.setBackground(TemaLouquitos.CINZA_ESCURO);
        btnVoltarLogin.setForeground(TemaLouquitos.BRANCO_PADRAO);
        btnVoltarLogin.setFocusPainted(false);
        btnVoltarLogin.setBorder(BorderFactory.createLineBorder(TemaLouquitos.BRANCO_PADRAO, 1));
        gbc.gridy = 7;
        add(btnVoltarLogin, gbc);

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarCadastro();
            }
        });

        btnVoltarLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showPanel("Login");
            }
        });
    }

    private void salvarCadastro() {
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String senha = new String(txtSenha.getPassword());
        String confirmarSenha = new String(txtConfirmarSenha.getPassword());

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome, E-mail e Senha são obrigatórios.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            JOptionPane.showMessageDialog(this, "As senhas não coincidem.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario novoUsuario = new Usuario(nome, email, senha, false);

        usuarioDAO.cadastrarUsuario(novoUsuario);

        txtNome.setText("");
        txtEmail.setText("");
        txtSenha.setText("");
        txtConfirmarSenha.setText("");
        txtTelefone.setText("");

        mainFrame.showPanel("Login");
    }
}