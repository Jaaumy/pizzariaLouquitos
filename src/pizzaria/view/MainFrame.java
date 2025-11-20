package pizzaria.view;

import pizzaria.dao.ClienteDAO;
import pizzaria.model.Cliente;
import pizzaria.model.ItemCarrinho;
import pizzaria.model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private Usuario usuarioLogado;
    private Cliente clienteLogado;
    private ArrayList<ItemCarrinho> carrinho;

    private ClienteDAO clienteDAO;
    private PainelLogin painelLogin;
    private PainelCadastro painelCadastro;
    private PainelCliente painelCliente;
    private PainelCarrinho painelCarrinho;
    private PainelMeusPedidos painelMeusPedidos;
    private PainelAdmin painelAdmin;
    private PainelEnderecos painelEnderecos;

    public MainFrame() {
        setTitle("Louquitos Pizzaria");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        this.carrinho = new ArrayList<>();
        this.clienteDAO = new ClienteDAO();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        painelLogin = new PainelLogin(this);
        painelCadastro = new PainelCadastro(this);
        painelCliente = new PainelCliente(this);
        painelCarrinho = new PainelCarrinho(this);
        painelMeusPedidos = new PainelMeusPedidos(this);
        painelAdmin = new PainelAdmin(this);
        painelEnderecos = new PainelEnderecos(this);

        mainPanel.add(painelLogin, "Login");
        mainPanel.add(painelCadastro, "Cadastro");
        mainPanel.add(painelCliente, "Cliente");
        mainPanel.add(painelCarrinho, "Carrinho");
        mainPanel.add(painelMeusPedidos, "MeusPedidos");
        mainPanel.add(painelAdmin, "Admin");
        mainPanel.add(painelEnderecos, "Enderecos");

        add(mainPanel);

        cardLayout.show(mainPanel, "Login");
    }

    public void showPanel(String panelName) {
        if (panelName.equals("Cliente")) {
            painelCliente.carregarDados();
        } else if (panelName.equals("Carrinho")) {
            painelCarrinho.carregarCarrinho();
        } else if (panelName.equals("MeusPedidos")) {
            painelMeusPedidos.carregarPedidos();
        } else if (panelName.equals("Admin")) {
            painelAdmin.carregarDashboard();
        } else if (panelName.equals("Enderecos")) {
            painelEnderecos.carregarEnderecos();
        }
        cardLayout.show(mainPanel, panelName);
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        if (this.usuarioLogado != null && !this.usuarioLogado.isAdmin()) {
            this.clienteLogado = clienteDAO.getClientePorUsuarioId(this.usuarioLogado.getIdUsuario());

            if (this.clienteLogado == null) {
                Cliente novoCliente = new Cliente();
                novoCliente.setIdUsuarioRef(this.usuarioLogado.getIdUsuario());
                try {
                    clienteDAO.criarClienteSimples(novoCliente);
                    this.clienteLogado = clienteDAO.getClientePorUsuarioId(this.usuarioLogado.getIdUsuario());
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erro ao criar perfil de cliente.");
                }
            }
        } else {
            this.clienteLogado = null;
        }
    }

    public Cliente getClienteLogado() {
        return clienteLogado;
    }

    public ArrayList<ItemCarrinho> getCarrinho() {
        return carrinho;
    }

    public void limparCarrinho() {
        this.carrinho.clear();
    }

    public void adicionarAoCarrinho(ItemCarrinho item) {
        this.carrinho.add(item);
    }

    public int getQuantidadeItensCarrinho() {
        int total = 0;
        for (ItemCarrinho item : this.carrinho) {
            total += item.getQuantidade();
        }
        return total;
    }

    public double getValorTotalCarrinho() {
        double total = 0;
        for (ItemCarrinho item : this.carrinho) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void fazerLogout() {
        setUsuarioLogado(null);
        limparCarrinho();
        cardLayout.show(mainPanel, "Login");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}