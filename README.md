PIZZARIA LOUQUITOS
Sistema Completo de Pedidos, Clientes e Administração

SUMÁRIO
1. Visão Geral
2. Funcionalidades
3. Arquitetura e Tecnologias
4. Interface Gráfica
5. Banco de Dados
6. Como Rodar o Projeto
7. Solução de Problemas Comuns
8. Autor

---

1. VISÃO GERAL

O Pizzaria Louquitos é um sistema desktop completo desenvolvido em Java para o gerenciamento de uma pizzaria. A aplicação cobre desde o cadastro de clientes e realização de pedidos até a administração de produtos e usuários.

O projeto foi construído seguindo a arquitetura MVC (Model-View-Controller), garantindo um código organizado, escalável e de fácil manutenção. A persistência de dados é feita via JDBC com banco de dados MySQL.

2. FUNCIONALIDADES

Módulo de Clientes:
- Cadastro, login e atualização de perfil.
- Gerenciamento de múltiplos endereços de entrega.
- Histórico visual de pedidos anteriores.
- Sistema de avaliação de serviços.

Módulo de Produtos:
- Cadastro e edição de pizzas (Nome, Descrição, Preço).
- Sistema de Cupons de Desconto.
- Gerenciamento dinâmico de preços.

Módulo de Pedidos:
- Carrinho de compras interativo.
- Cálculo automático de totais (com aplicação de cupons).
- Associação inteligente de itens, cliente e endereço.

Administração:
- Dashboard administrativo.
- Controle total de usuários (Admin/Funcionários).
- Gestão unificada de pedidos e cardápio.

3. ARQUITETURA E TECNOLOGIAS

A aplicação segue estritamente o padrão MVC com DAO para isolamento do banco de dados.

Estrutura de Pastas:
src/
 - model/ (Entidades)
 - dao/ (Camada de acesso ao Banco de Dados)
 - view/ (Interfaces Gráficas)
 - ConnectionFactory.java (Configuração do Banco)

Modelos Principais:
- Cliente: Dados pessoais, login e lista de endereços.
- Pedido: Composição de cliente, itens, endereço e valores.
- Pizza: Definição do produto base.
- Cupom: Lógica de descontos.
- Usuario: Controle de acesso administrativo.

4. INTERFACE GRÁFICA

A interface foi desenvolvida 100% em Java Swing, utilizando um gerenciador de fluxo centralizado no MainFrame.

- Fluxo Contínuo: Navegação fluida entre Login, Cardápio, Carrinho e Pagamento.
- Tema Personalizado: Classe TemaLouquitos.java garante consistência visual.
- Painéis: PainelLogin, PainelCarrinho, PainelAdmin, etc.

5. BANCO DE DADOS

O sistema utiliza MySQL.

Configuração JDBC (ConnectionFactory.java):
URL: jdbc:mysql://localhost:3306/pizzaria
Usuário: root
Senha: sua_senha

Tabelas Principais:
- clientes
- enderecos
- pizzas
- pedidos / itens_pedido
- cupons
- usuarios

6. COMO RODAR O PROJETO

Pré-requisitos:
- Java JDK 8 ou superior.
- MySQL Server instalado.
- IDE (IntelliJ IDEA, Eclipse ou NetBeans).

Passo a Passo:
1. Clone o repositório do GitHub.
2. Crie o banco de dados "pizzaria" no MySQL (use o script disponível na pasta database).
3. Atualize a classe ConnectionFactory.java com sua senha do MySQL.
4. Execute a classe MainFrame.java ou Main.java.

7. SOLUÇÃO DE PROBLEMAS COMUNS

Erro: Access denied for user
Causa: Senha do banco incorreta.
Solução: Verifique o ConnectionFactory ou resete a senha root do MySQL.

Erro: Driver JDBC não encontrado
Causa: Falta do arquivo .jar do conector.
Solução: Adicione o mysql-connector-j às bibliotecas do projeto.

Erro: NullPointer em telas
Causa: Componente não inicializado.
Solução: Verifique se os componentes Swing foram instanciados antes de serem exibidos.

8. AUTOR

Desenvolvido por João Pedro.

Projeto criado para fins acadêmicos, demonstrando domínio em:
- Estruturas de Dados e Orientação a Objetos.
- Integração Java e Banco de Dados Relacional.
- Desenvolvimento de Interfaces Desktop.
