# ♻️ EcoPlay — Backend Java

**Global Solution 2026/2 | FIAP — CitizenTech**

**Descarte certo vira ponto — e renda para quem recicla.**

## 📖 Sobre o projeto

O **EcoPlay** é uma solução CitizenTech voltada à educação ambiental e ao descarte correto de resíduos.

O aplicativo busca ajudar cidadãos a identificar materiais recicláveis, aprender como prepará-los para o descarte e encontrar cooperativas e pontos de coleta adequados.

Além disso, o EcoPlay conecta cidadãos às cooperativas, permitindo o agendamento de entregas de materiais. As entregas podem ser confirmadas, recusadas ou concluídas pelas cooperativas responsáveis.

A proposta também inclui inteligência artificial para auxiliar na identificação de resíduos e oferecer orientações sobre descarte correto.

Este repositório contém o **backend principal desenvolvido em Java**, responsável pelas regras de negócio, autenticação, persistência dos dados e integração com os demais serviços da solução.

## 🎯 Objetivo

Promover o descarte correto de resíduos, reduzir a contaminação de materiais recicláveis e contribuir para o trabalho das cooperativas de catadores.

O projeto está relacionado aos Objetivos de Desenvolvimento Sustentável (ODS), especialmente:

- **ODS 4:** Educação de Qualidade
- **ODS 10:** Redução das Desigualdades
- **ODS 11:** Cidades e Comunidades Sustentáveis
- **ODS 13:** Ação Contra a Mudança Global do Clima

## 🚀 Tecnologias

| Tecnologia | Utilização |
|---|---|
| Java 21 | Linguagem principal |
| Spring Boot 3.5 | Desenvolvimento da API REST |
| Spring Data JPA | Persistência e acesso aos dados |
| Spring Security | Autenticação e autorização |
| JWT | Controle de acesso por token |
| Bean Validation | Validação dos dados |
| Spring AI | Integração com IA generativa |
| Spring AMQP | Integração com RabbitMQ |
| RabbitMQ | Comunicação assíncrona |
| Azure SQL Database | Banco de dados relacional |
| Flyway | Versionamento do banco de dados |
| Swagger / OpenAPI | Documentação dos endpoints |
| Maven | Gerenciamento de dependências |
| JUnit | Testes automatizados |
| Microsoft Azure | Hospedagem da aplicação |

## ⚙️ Funcionalidades previstas

### Autenticação e usuários

- Cadastro de cidadãos
- Login com JWT
- Controle de acesso por papéis
- Consulta de informações do perfil

**Papéis:**

- `CIDADAO`: consulta materiais, identifica resíduos e gerencia suas entregas.
- `COOPERATIVA`: gerencia seus pontos de descarte e as entregas recebidas.

### Materiais recicláveis

- Cadastro e consulta de materiais
- Classificação por categoria
- Orientações de preparação e descarte
- Pesquisa e paginação

### Pontos de descarte

- Cadastro de pontos de coleta
- Associação de materiais aceitos
- Consulta por bairro e material
- Atualização e exclusão por cooperativas autorizadas

### Entregas

- Agendamento de entrega pelo cidadão
- Consulta das próprias entregas
- Reagendamento e exclusão de agendamentos
- Confirmação pela cooperativa
- Recusa com justificativa
- Conclusão com registro do peso dos materiais

**Estados da entrega:**

`AGENDADA` → `CONFIRMADA` → `CONCLUIDA`

Uma entrega também poderá ser marcada como `RECUSADA`, conforme as regras de negócio.

### Inteligência artificial

Integração planejada com um serviço de visão computacional para identificação de resíduos por imagem.

O Spring AI será utilizado para gerar orientações de descarte contextualizadas, considerando o material identificado e o guia de descarte do EcoPlay.

### Mensageria

Integração planejada com RabbitMQ para publicação e consumo de eventos relacionados à finalização de entregas.

Esses eventos permitirão a comunicação com a API .NET, responsável pela gamificação, incluindo XP e missões.

## 🏗️ Arquitetura

O EcoPlay será composto por diferentes serviços integrados:

- **API Java:** autenticação, materiais, pontos de descarte, entregas e orientações com IA.
- **API .NET:** XP, níveis, missões, quiz e ranking.
- **Serviço Python:** identificação de resíduos por imagem.
- **Aplicativo Mobile:** interface desenvolvida com React Native e Expo.
- **RabbitMQ:** comunicação assíncrona entre serviços.

O backend Java será consumido pelo aplicativo Mobile por meio de endpoints REST.

## 📁 Estrutura planejada

```text
src/
├── main/
│   ├── java/br/com/fiap/ecoplay/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── enums/
│   │   ├── exception/
│   │   ├── integration/
│   │   ├── messaging/
│   │   ├── repository/
│   │   ├── security/
│   │   ├── service/
│   │   └── EcoPlayApplication.java
│   └── resources/
│       ├── application.yml
│       └── db/migration/
└── test/
```

## 🔗 Endpoints planejados

### Autenticação

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/auth/cadastro` | Cadastro de cidadão |
| POST | `/auth/login` | Autenticação |

### Materiais

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/materiais` | Listagem e filtros de materiais |

### Pontos de descarte

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/pontos` | Listagem de pontos |
| POST | `/pontos` | Cadastro de ponto |
| PUT | `/pontos/{id}` | Atualização de ponto |
| DELETE | `/pontos/{id}` | Exclusão de ponto |

### Entregas

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/entregas` | Agendar entrega |
| GET | `/entregas/minhas` | Listar entregas do cidadão |
| GET | `/entregas/{id}` | Consultar entrega |
| PUT | `/entregas/{id}` | Reagendar entrega |
| DELETE | `/entregas/{id}` | Excluir agendamento |
| GET | `/entregas/recebidas` | Listar entregas da cooperativa |
| POST | `/entregas/{id}/confirmacao` | Confirmar entrega |
| POST | `/entregas/{id}/recusa` | Recusar entrega |
| POST | `/entregas/{id}/conclusao` | Concluir entrega |

### Identificação de resíduos

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/identificacoes` | Identificar resíduo por imagem |
| PATCH | `/identificacoes/{id}/confirmacao` | Confirmar identificação |

Os endpoints poderão receber o prefixo `/api` conforme a configuração final da aplicação.

## 💻 Como executar

### Pré-requisitos

- Java JDK 21
- Maven ou Maven Wrapper
- IntelliJ IDEA ou outra IDE Java
- Banco de dados SQL Server configurado

### 1. Clonar o repositório

```bash
git clone URL_DO_REPOSITORIO
cd ecoplay
```

### 2. Configurar variáveis de ambiente

As configurações de acesso ao banco serão fornecidas por variáveis de ambiente:

```env
DB_URL=jdbc:sqlserver://HOST:1433;databaseName=NOME_BANCO;encrypt=true
DB_USERNAME=USUARIO
DB_PASSWORD=SENHA
```

Outras variáveis, como credenciais de IA, JWT e RabbitMQ, serão documentadas conforme a implementação.

**Não adicionar senhas, tokens ou chaves privadas ao repositório.**

### 3. Compilar

No Windows:

```powershell
.\mvnw.cmd clean compile
```

### 4. Executar

Após configurar as dependências e o banco:

```powershell
.\mvnw.cmd spring-boot:run
```

A porta padrão prevista é `8080`.

## 📚 Documentação da API

A documentação interativa será disponibilizada pelo Swagger/OpenAPI após sua configuração.

Endereço local previsto:

```text
http://localhost:8080/swagger-ui/index.html
```

## 🧪 Testes

O projeto utilizará JUnit para testar as regras de negócio e os comportamentos da API.

Os testes serão integrados ao processo de CI/CD desenvolvido com Azure DevOps.

## ☁️ Deploy

A aplicação terá implantação planejada no Microsoft Azure, com automação por Azure Pipelines.

**URL da API:** a definir.

## 📌 Status do projeto

🚧 **Em desenvolvimento — estrutura inicial.**

As funcionalidades, integrações, testes e configurações de deploy serão adicionados progressivamente.

## 🤖 Uso de inteligência artificial

**Em desenvolvimento**

## 👩‍💻 Equipe

| Integrante | RM |
|---|---|
| Eduarda Weiss Ventura | RM564434 |
| Camily Vitoria Pereira Maciel | RM566520 |
| Lucas Nunes Soares | RM566503 |
| Samara Porto Souza | RM559072 |
| Maria Gabriela Landim Severo | RM565146 |

**FIAP — Global Solution 2026/2**

**CitizenTech | EcoPlay**

---

*Projeto acadêmico desenvolvido para a Global Solution da FIAP.*
