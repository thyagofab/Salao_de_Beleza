# Sistema de Gerenciamento para Salão de Beleza

## Tópicos:
- [Descrição](#descrição)
- [Organização do Projeto](#organização-do-projeto)
- [Funcionalidades](#funcionalidades)
- [Requisitos](#requisitos)
- [Instalação e execução](#instalação-e-execução)
- [Contribuição](#contribuição)

## Descrição
O sistema desenvolvido tem como finalidade gerenciar de forma eficiente os atendimentos em um salão de beleza, permitindo gerenciar agendamentos, controlar a disponibilidade de horários, registrar o histórico de procedimentos e disponibilizar avaliações de profissionais.

## Organização do projeto
```
thyagofab-salao_de_beleza/
├── README.md
└── src/
    ├── Main.java                     # Classe principal do sistema
    ├── dao/                          # Classes responsáveis pelo acesso ao banco de dados
    │   ├── AgendamentoDAO.java
    │   ├── CabeleireiroDAO.java
    │   ├── ClienteDAO.java
    │   ├── ProcedimentoDAO.java
    │   └── UsuarioDAO.java
    ├── data/                         # Conexão com o banco de dados e scripts SQL
    │   ├── banco.db
    │   ├── ConexaoDoBanco.java
    │   └── Data.sql
    ├── model/                        # Classes de modelo (entidades)
    │   ├── Agendamento.java
    │   ├── Cabeleireiro.java
    │   ├── Cliente.java
    │   ├── Procedimento.java
    │   ├── StatusAgendamento.java
    │   └── Usuario.java
    ├── service/                      # Serviços do negócio
    │   ├── AgendamentoService.java
    │   ├── CabeleireiroService.java
    │   ├── ClienteService.java
    │   ├── ProcedimentoService.java
    │   └── UsuarioService.java
    ├── util/                         # Utilitários auxiliares
    │   └── Entradas.java
    └── view/                         # Interfaces de interação com o usuário
        ├── AgendamentoView.java
        ├── CabeleireiroView.java
        ├── ClienteView.java
        ├── MenuPrincipalView.java
        ├── ProcedimentoView.java
        └── UsuarioView.java
```

## Funcionalidades
- Cadastro e gerenciamento de **Clientes**, **Cabeleireiros** e **Procedimentos**
- Agendamento de horários entre **Clientes** e **Cabeleireiros**
- Controle de **status dos agendamentos** (ex: agendado, reagendado, concluído, cancelado)
- **Clientes** podem optar por adicionar mais de um procedimento ao agendamento
- **Clientes** e **Cabeleireiros** podem visualizar seus históricos de agendamentos
- Atualização automática da última visita feita pelo **Cliente**
- Cálculo do valor total do agendamento com base nos procedimentos selecionados pelo **Cliente**
- Sistema de **login de usuário** que redireciona para o menu principal de acordo com o tipo de usuário (**Cliente** ou **Cabeleireiro**)

## Requisitos
- Java JDK 8 ou superior
- IDE Java (Eclipse, IntelliJ, VS Code ou similar)
- SQLite (já incluso no projeto na pasta `lib`)

## instalação e execução

1. **Clone o repositório**:
   ```bash
   git clone https://github.com/thyagofab/salao_de_beleza.git
    ```  
2. **Abra o projeto em sua IDE Java**

3. **Ajuste as dependências do Banco de dados**:
    - Se estiver utilizando o Visual Studio Code, siga as instruções abaixo para ajustar as dependências do banco de dados SQLite:
        - Dentro da pasta `.vscode` no projeto, crie um arquivo `settings.json` (se já não existir).
        - Adicione o seguinte conteúdo ao arquivo `settings.json` com as versões correspondentes dos arquivos JAR presentes na pasta `lib` do projeto:

        ```json
        {
            "java.project.referencedLibraries": [
            "src/lib/sqlite-jdbc-3.45.2.0.jar",
            "src/lib/slf4j-api-2.0.12.jar"
            ]
        }
        ```

3. **Compile e execute a classe `Main.java`**.

