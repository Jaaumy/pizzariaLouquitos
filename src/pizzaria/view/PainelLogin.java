package pizzaria.view;

import pizzaria.dao.UsuarioDAO;
import pizzaria.model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PainelLogin extends JPanel {

    private MainFrame mainFrame;
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnCadastrar;

    private UsuarioDAO usuarioDAO;

    public PainelLogin(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.usuarioDAO = new UsuarioDAO();

        setLayout(new GridBagLayout());
        setBackground(TemaLouquitos.VERMELHO_PIZZARIA);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        ImageIcon logoIcon = ImageUtil.loadImage("logo.png");
        JLabel lblLogo = new JLabel(ImageUtil.resizeImage(logoIcon, 150, 150));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblLogo, gbc);

        JLabel lblTitulo = new JLabel("Bem-vindo à Louquitos Pizzaria!");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(TemaLouquitos.BRANCO_PADRAO);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        JLabel lblEmail = new JLabel("E-mail:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 16));
        lblEmail.setForeground(TemaLouquitos.BRANCO_PADRAO);
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(lblEmail, gbc);

        txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 16));
        txtEmail.setBackground(TemaLouquitos.CINZA_CLARO);
        txtEmail.setBorder(BorderFactory.createLineBorder(TemaLouquitos.VERDE_PIZZARIA, 2));
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(txtEmail, gbc);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSenha.setForeground(TemaLouquitos.BRANCO_PADRAO);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(lblSenha, gbc);

        txtSenha = new JPasswordField(20);
        txtSenha.setFont(new Font("Arial", Font.PLAIN, 16));
        txtSenha.setBackground(TemaLouquitos.CINZA_CLARO);
        txtSenha.setBorder(BorderFactory.createLineBorder(TemaLouquitos.VERDE_PIZZARIA, 2));
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(txtSenha, gbc);

        btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 18));
        btnEntrar.setBackground(TemaLouquitos.VERDE_PIZZARIA);
        btnEntrar.setForeground(TemaLouquitos.BRANCO_PADRAO);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorder(BorderFactory.createLineBorder(TemaLouquitos.BRANCO_PADRAO, 2));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnEntrar, gbc);

        btnCadastrar = new JButton("Não tenho cadastro? Cadastre-se!");
        btnCadastrar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnCadastrar.setBackground(TemaLouquitos.CINZA_ESCURO);
        btnCadastrar.setForeground(TemaLouquitos.BRANCO_PADRAO);
        btnCadastrar.setFocusPainted(false);
        btnCadastrar.setBorder(BorderFactory.createLineBorder(TemaLouquitos.BRANCO_PADRAO, 1));
        gbc.gridy = 5;
        add(btnCadastrar, gbc);

        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tentarLogin();
            }
        });

        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showPanel("Cadastro");
            }
        });
    }

    private void tentarLogin() {
        String email = txtEmail.getText();
        String senha = new String(txtSenha.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha E-mail e Senha.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuario = usuarioDAO.validarLogin(email, senha);

        if (usuario != null) {
            mainFrame.setUsuarioLogado(usuario);

            txtEmail.setText("");
            txtSenha.setText("");

            if (usuario.isAdmin()) {
                mainFrame.showPanel("Admin");
            } else {
                mainFrame.showPanel("Cliente");
            }

        } else {
            JOptionPane.showMessageDialog(this, "E-mail ou Senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }
}