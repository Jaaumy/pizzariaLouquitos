package pizzaria.view;

import pizzaria.dao.AvaliacaoDAO;
import pizzaria.dao.PedidoDAO;
import pizzaria.dao.PizzaDAO;
import pizzaria.model.ItemCarrinho;
import pizzaria.model.Pedido;
import pizzaria.model.Pizza;
import pizzaria.model.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PainelCliente extends JPanel {

    private MainFrame mainFrame;
    private PizzaDAO pizzaDAO;
    private AvaliacaoDAO avaliacaoDAO;
    private PedidoDAO pedidoDAO;

    private JLabel lblNomeUsuario;
    private JButton btnVerCarrinho;
    private JPanel painelCatalogo;

    private Timer timerStatus;
    private JPanel painelStatusPedido;
    private JLabel lblStatusPedido;

    public PainelCliente(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.pizzaDAO = new PizzaDAO();
        this.avaliacaoDAO = new AvaliacaoDAO();
        this.pedidoDAO = new PedidoDAO();

        setLayout(new BorderLayout());
        setBackground(TemaLouquitos.CINZA_CLARO);

        criarHeader();
        criarCatalogo();
        criarStatusPedido();

        timerStatus = new Timer(30000, e -> atualizarStatusPedido());
    }

    private void criarHeader() {
        JPanel painelHeader = new JPanel();
        painelHeader.setLayout(new BoxLayout(painelHeader, BoxLayout.X_AXIS));
        painelHeader.setBackground(TemaLouquitos.VERDE_PIZZARIA);
        painelHeader.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel("Pizzas Louquitos - Catálogo");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(TemaLouquitos.BRANCO_PADRAO);

        JPanel painelUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelUsuario.setOpaque(false);

        lblNomeUsuario = new JLabel("Olá, Cliente");
        lblNomeUsuario.setFont(new Font("Arial", Font.BOLD, 16));
        lblNomeUsuario.setForeground(TemaLouquitos.BRANCO_PADRAO);
        lblNomeUsuario.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnMeusEnderecos = new JButton("Endereços");
        btnMeusEnderecos.setFont(new Font("Arial", Font.BOLD, 14));
        btnMeusEnderecos.setBackground(TemaLouquitos.CINZA_CLARO);
        btnMeusEnderecos.setForeground(TemaLouquitos.CINZA_ESCURO);
        btnMeusEnderecos.setFocusPainted(false);

        JButton btnMeusPedidos = new JButton("Meus Pedidos");
        btnMeusPedidos.setFont(new Font("Arial", Font.BOLD, 14));
        btnMeusPedidos.setBackground(TemaLouquitos.CINZA_CLARO);
        btnMeusPedidos.setForeground(TemaLouquitos.CINZA_ESCURO);
        btnMeusPedidos.setFocusPainted(false);

        btnVerCarrinho = new JButton("Carrinho (0)");
        btnVerCarrinho.setFont(new Font("Arial", Font.BOLD, 14));
        btnVerCarrinho.setBackground(TemaLouquitos.VERMELHO_PIZZARIA);
        btnVerCarrinho.setForeground(TemaLouquitos.BRANCO_PADRAO);
        btnVerCarrinho.setFocusPainted(false);

        JButton btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Arial", Font.BOLD, 14));
        btnSair.setBackground(TemaLouquitos.CINZA_ESCURO);
        btnSair.setForeground(TemaLouquitos.BRANCO_PADRAO);
        btnSair.setFocusPainted(false);

        painelUsuario.add(lblNomeUsuario);
        painelUsuario.add(new JSeparator(SwingConstants.VERTICAL));
        painelUsuario.add(btnMeusEnderecos);
        painelUsuario.add(btnMeusPedidos);
        painelUsuario.add(btnVerCarrinho);
        painelUsuario.add(btnSair);

        painelHeader.add(lblTitulo);
        painelHeader.add(Box.createHorizontalGlue());
        painelHeader.add(painelUsuario);

        add(painelHeader, BorderLayout.NORTH);

        lblNomeUsuario.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                exibirDadosCliente();
            }
        });

        btnMeusEnderecos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerStatus.stop();
                mainFrame.showPanel("Enderecos");
            }
        });

        btnMeusPedidos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerStatus.stop();
                mainFrame.showPanel("MeusPedidos");
            }
        });

        btnVerCarrinho.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerStatus.stop();
                mainFrame.showPanel("Carrinho");
            }
        });

        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerStatus.stop();
                mainFrame.fazerLogout();
            }
        });
    }

    private void criarCatalogo() {
        painelCatalogo = new JPanel();
        painelCatalogo.setLayout(new BoxLayout(painelCatalogo, BoxLayout.Y_AXIS));
        painelCatalogo.setBackground(TemaLouquitos.BRANCO_PADRAO);

        JScrollPane scrollPane = new JScrollPane(painelCatalogo);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void criarStatusPedido() {
        painelStatusPedido = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelStatusPedido.setBackground(TemaLouquitos.CINZA_ESCURO);
        painelStatusPedido.setBorder(new EmptyBorder(5, 5, 5, 5));

        lblStatusPedido = new JLabel("Você não tem pedidos ativos no momento.");
        lblStatusPedido.setFont(new Font("Arial", Font.BOLD, 14));
        lblStatusPedido.setForeground(TemaLouquitos.BRANCO_PADRAO);

        painelStatusPedido.add(lblStatusPedido);
        painelStatusPedido.setVisible(false);

        add(painelStatusPedido, BorderLayout.SOUTH);
    }

    public void carregarDados() {
        if (mainFrame.getUsuarioLogado() != null) {
            lblNomeUsuario.setText("Olá, " + mainFrame.getUsuarioLogado().getNome().split(" ")[0]);

            atualizarStatusPedido();
            if (!timerStatus.isRunning()) {
                timerStatus.start();
            }
        }

        atualizarBotaoCarrinho();

        painelCatalogo.removeAll();

        List<Pizza> pizzas = pizzaDAO.listarPizzas();

        for (Pizza pizza : pizzas) {
            painelCatalogo.add(criarItemPizza(pizza));
            painelCatalogo.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        painelCatalogo.revalidate();
        painelCatalogo.repaint();
    }

    private void atualizarStatusPedido() {
        if (mainFrame.getUsuarioLogado() == null) {
            timerStatus.stop();
            return;
        }

        Pedido pedidoAtivo = pedidoDAO.getPedidoAtivo(mainFrame.getUsuarioLogado().getIdUsuario());

        if (pedidoAtivo != null) {
            lblStatusPedido.setText(String.format("Acompanhe seu Pedido (#%d): Status: %s",
                    pedidoAtivo.getIdPedido(),
                    pedidoAtivo.getStatusPedido()));
            painelStatusPedido.setVisible(true);
        } else {
            painelStatusPedido.setVisible(false);
        }
        revalidate();
        repaint();
    }

    private String getEstrelasAvaliacao(double media) {
        if (media == 0) {
            return " (Nenhuma avaliação)";
        }
        long roundMedia = Math.round(media);
        String estrelas = "";
        for (int i = 0; i < 5; i++) {
            estrelas += (i < roundMedia) ? "★" : "☆";
        }
        return String.format(" %s (%.1f)", estrelas, media);
    }

    private JPanel criarItemPizza(Pizza pizza) {
        JPanel painelItem = new JPanel(new BorderLayout(15, 15));
        painelItem.setBackground(TemaLouquitos.CINZA_CLARO);
        painelItem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaLouquitos.VERDE_PIZZARIA, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        painelItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        painelItem.setPreferredSize(new Dimension(600, 130));

        ImageIcon icon = ImageUtil.loadImage(pizza.getImagem());
        ImageIcon resizedIcon = ImageUtil.resizeImage(icon, 100, 100);
        JLabel lblImagem = new JLabel(resizedIcon);
        lblImagem.setBorder(BorderFactory.createLineBorder(TemaLouquitos.VERDE_PIZZARIA, 1));
        painelItem.add(lblImagem, BorderLayout.WEST);

        JPanel painelDescricao = new JPanel();
        painelDescricao.setLayout(new BoxLayout(painelDescricao, BoxLayout.Y_AXIS));
        painelDescricao.setOpaque(false);

        JLabel lblNomePizza = new JLabel(pizza.getNome());
        lblNomePizza.setFont(new Font("Arial", Font.BOLD, 20));
        lblNomePizza.setForeground(TemaLouquitos.VERMELHO_PIZZARIA);

        double media = avaliacaoDAO.getMediaAvaliacaoPorPizza(pizza.getIdPizza());
        JLabel lblAvaliacao = new JLabel(getEstrelasAvaliacao(media));
        lblAvaliacao.setFont(new Font("Arial", Font.PLAIN, 12));
        lblAvaliacao.setForeground(Color.GRAY);

        JPanel painelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        painelTitulo.setOpaque(false);
        painelTitulo.add(lblNomePizza);
        painelTitulo.add(lblAvaliacao);

        JLabel lblDescricaoPizza = new JLabel("<html><p>" + pizza.getDescricao() + "</p></html>");
        lblDescricaoPizza.setFont(new Font("Arial", Font.PLAIN, 14));
        lblDescricaoPizza.setForeground(TemaLouquitos.CINZA_ESCURO);

        JLabel lblPrecoPizza = new JLabel(String.format("R$ %.2f", pizza.getPreco()));
        lblPrecoPizza.setFont(new Font("Arial", Font.BOLD, 18));
        lblPrecoPizza.setForeground(TemaLouquitos.VERDE_PIZZARIA);

        painelDescricao.add(painelTitulo);
        painelDescricao.add(Box.createRigidArea(new Dimension(0, 5)));
        painelDescricao.add(lblDescricaoPizza);
        painelDescricao.add(Box.createVerticalGlue());
        painelDescricao.add(lblPrecoPizza);

        painelItem.add(painelDescricao, BorderLayout.CENTER);

        JButton btnAdicionar = new JButton("Adicionar");
        btnAdicionar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdicionar.setBackground(TemaLouquitos.VERDE_PIZZARIA);
        btnAdicionar.setForeground(TemaLouquitos.BRANCO_PADRAO);
        btnAdicionar.setFocusPainted(false);

        painelItem.add(btnAdicionar, BorderLayout.EAST);

        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarPizzaAoCarrinho(pizza);
            }
        });

        return painelItem;
    }

    private void adicionarPizzaAoCarrinho(Pizza pizza) {
        String qtdeStr = JOptionPane.showInputDialog(
                mainFrame,
                "Qual a quantidade de " + pizza.getNome() + "?",
                "Adicionar ao Carrinho",
                JOptionPane.PLAIN_MESSAGE
        );

        try {
            int quantidade = Integer.parseInt(qtdeStr);
            if (quantidade > 0) {
                ItemCarrinho item = new ItemCarrinho(pizza, quantidade);
                mainFrame.adicionarAoCarrinho(item);
                atualizarBotaoCarrinho();
                JOptionPane.showMessageDialog(mainFrame,
                        quantidade + "x " + pizza.getNome() + " adicionado(s) ao carrinho!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainFrame, "A quantidade deve ser positiva.", "Erro", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            if (qtdeStr != null) {
                JOptionPane.showMessageDialog(mainFrame, "Por favor, digite um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void atualizarBotaoCarrinho() {
        int qtde = mainFrame.getQuantidadeItensCarrinho();
        btnVerCarrinho.setText("Carrinho (" + qtde + ")");
    }

    private void exibirDadosCliente() {
        Usuario user = mainFrame.getUsuarioLogado();
        if (user != null) {
            String dados = "Nome: " + user.getNome() + "\n" +
                    "E-mail: " + user.getEmail() + "\n" +
                    "Tipo: " + (user.isAdmin() ? "Admin" : "Cliente");
            JOptionPane.showMessageDialog(mainFrame, dados, "Dados do Cliente", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}