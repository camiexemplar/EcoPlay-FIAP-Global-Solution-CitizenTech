[README_EcoPlay_Java_Atualizado.md](https://github.com/user-attachments/files/34775234/README_EcoPlay_Java_Atualizado.md)
<div align="center">

# ♻️ EcoPlay — Backend Java

**Global Solution 2026/2 | FIAP — CitizenTech**

**Descarte certo vira ponto — e renda para quem recicla.**

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.2-brightgreen?style=for-the-badge&logo=springboot&logoColor=white)
![Oracle](https://img.shields.io/badge/Oracle-Database-red?style=for-the-badge&logo=oracle&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-green?style=for-the-badge&logo=springsecurity&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-Migrations-blue?style=for-the-badge)

</div>

## 📖 Sobre o projeto

O **EcoPlay** é uma solução CitizenTech que incentiva a educação ambiental e o descarte correto de materiais recicláveis. A proposta conecta cidadãos e cooperativas, oferece informações sobre os materiais e seus cuidados de preparação, facilita a localização de pontos de descarte e permite agendar entregas.

Este repositório contém a **API REST desenvolvida em Java**, responsável pelo cadastro e autenticação dos usuários, catálogo de materiais, pontos de descarte, entregas, histórico de identificações e persistência dos dados.

A solução completa também prevê um aplicativo mobile, um microsserviço Python para reconhecimento de materiais por imagem e uma API .NET para gamificação. **Esses serviços são componentes separados; o funcionamento de suas integrações deve ser validado independentemente dos testes da API Java.**

## 🎯 Objetivos

- Incentivar a separação e o descarte correto dos resíduos.
- Facilitar o acesso a orientações sobre reciclagem e preparação dos materiais.
- Aproximar cidadãos e cooperativas por meio dos pontos de coleta e agendamentos.
- Permitir o acompanhamento das entregas e do histórico de identificações.
- Preparar a plataforma para reconhecimento por IA e recompensas por ações sustentáveis.

O projeto se relaciona especialmente com os **ODS 4, 10, 11, 12 e 13** da ONU.

## 🛠️ Tecnologias utilizadas no backend

| Tecnologia | Finalidade |
|---|---|
| Java 21 | Linguagem principal |
| Spring Boot 4.1.2 | API REST |
| Spring Web MVC | Controllers e requisições HTTP |
| Spring Data JPA / Hibernate | Persistência de entidades |
| Spring Security | Autenticação e autorização |
| JWT com RSA | Tokens de acesso assinados |
| Bean Validation | Validação dos dados recebidos |
| Oracle Database | Banco de dados relacional |
| Flyway | Versionamento do esquema do banco |
| HikariCP | Pool de conexões |
| Spring AMQP / RabbitMQ | Dependência para mensageria e integração futura |
| Maven Wrapper | Gerenciamento e execução do projeto |
| Postman | Testes manuais dos endpoints |

> **Nota:** ter a dependência do RabbitMQ no projeto não significa que a publicação e o consumo de eventos entre serviços já foram validados. O mesmo vale para a integração com IA e gamificação.

## ✅ Funcionalidades do backend Java

### Autenticação e usuários

- Cadastro público de cidadãos.
- Login por e-mail e senha, com emissão de JWT.
- Senhas armazenadas com hash BCrypt.
- Papéis `CIDADAO` e `COOPERATIVA`.
- Consulta de dados do próprio usuário, com autorização baseada na identidade do token.

### Materiais recicláveis

- Cadastro, listagem, consulta por ID, atualização e exclusão.
- Categoria, reciclabilidade, instruções de preparação e classe utilizada na classificação.
- Consulta pública; operações de alteração protegidas por perfil.

### Pontos de descarte

- Cadastro, listagem, consulta por ID, atualização e exclusão.
- Endereço, bairro, horário, tipo de ponto, cooperativa responsável e materiais aceitos.
- Verificação de permissões para operações de alteração.

### Entregas

- Agendamento de entrega com itens e respectivos pesos.
- Consulta por ID e histórico por cidadão ou cooperativa.
- Reagendamento e exclusão conforme regras de negócio.
- Confirmação, recusa e conclusão pela cooperativa responsável.
- Controle do fluxo por status da entrega.

### Identificações

- Registro de uma identificação associada ao cidadão e ao material.
- Armazenamento do nível de confiança informado.
- Consulta por ID e histórico por usuário.
- Confirmação do resultado pelo cidadão.

> **Importante:** os endpoints Java de identificação registram e consultam resultados. Eles **não comprovam, por si só, o envio de uma imagem para o microsserviço Python**.

## 🔐 Segurança

A API utiliza **Spring Security com JWT assinado por chaves RSA**, sessões stateless e autorização por métodos com `@EnableMethodSecurity` e `@PreAuthorize`.

- **Públicos:** cadastro, login, consulta de materiais e consulta de pontos de descarte.
- **Cidadão autenticado:** acesso às próprias informações, identificações e entregas permitidas.
- **Cooperativa autenticada:** operações administrativas autorizadas e tratamento das entregas destinadas à cooperativa.
- **Autorização por identidade:** verificações relacionam o usuário autenticado aos recursos solicitados, evitando confiar somente nos IDs enviados na URL ou no corpo.

Os retornos HTTP tratados incluem validações (`400`), ausência de autenticação (`401`), acesso negado (`403`), recurso não encontrado (`404`) e conflitos de dados ou estado (`409`), além de falhas internas (`500`).

## 🔗 Endpoints da API

**Base local:** `http://localhost:8080`

### Autenticação e usuários

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/usuarios/cadastro` | Cadastrar cidadão |
| POST | `/api/auth/login` | Autenticar e receber JWT |
| GET | `/api/usuarios/{id}` | Consultar dados do próprio usuário |

### Materiais

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/materiais` | Criar material |
| GET | `/api/materiais` | Listar materiais |
| GET | `/api/materiais/{id}` | Consultar material |
| PUT | `/api/materiais/{id}` | Atualizar material |
| DELETE | `/api/materiais/{id}` | Excluir material |

### Pontos de descarte

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/pontos-descarte` | Criar ponto |
| GET | `/api/pontos-descarte` | Listar pontos |
| GET | `/api/pontos-descarte/{id}` | Consultar ponto |
| PUT | `/api/pontos-descarte/{id}` | Atualizar ponto |
| DELETE | `/api/pontos-descarte/{id}` | Excluir ponto |

### Entregas

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/entregas?cidadaoId={id}` | Agendar entrega |
| GET | `/api/entregas/{id}?cidadaoId={id}` | Consultar entrega |
| GET | `/api/entregas/cidadao/{id}` | Listar entregas do cidadão |
| GET | `/api/entregas/cooperativa/{id}` | Listar entregas da cooperativa |
| PUT | `/api/entregas/{id}/reagendar?cidadaoId={id}` | Reagendar entrega |
| DELETE | `/api/entregas/{id}?cidadaoId={id}` | Excluir agendamento |
| PATCH | `/api/entregas/{id}/confirmar?cooperativaId={id}` | Confirmar entrega |
| PATCH | `/api/entregas/{id}/recusar?cooperativaId={id}` | Recusar entrega |
| PATCH | `/api/entregas/{id}/concluir?cooperativaId={id}` | Concluir entrega |

> A operação de recusa também utiliza parâmetros de motivo e, opcionalmente, observação, de acordo com o controller.

### Identificações

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/identificacoes?usuarioId={id}` | Registrar identificação |
| GET | `/api/identificacoes/{id}?usuarioId={id}` | Consultar identificação |
| GET | `/api/identificacoes/usuario/{id}` | Listar identificações do usuário |
| PATCH | `/api/identificacoes/{id}/confirmar?usuarioId={id}` | Confirmar identificação |

**Total: 26 endpoints REST.** Alguns endpoints requerem corpo JSON e outros parâmetros conforme os DTOs e controllers.

## 🧪 Exemplos de requisições

### Cadastro de cidadão

`POST /api/usuarios/cadastro`

```json
{
  "nome": "Maria Silva",
  "email": "maria.ecoplay@example.com",
  "senha": "SenhaForte123!",
  "role": "CIDADAO",
  "bairro": "Jardins"
}
```

### Login

`POST /api/auth/login`

```json
{
  "email": "maria.ecoplay@example.com",
  "senha": "SenhaForte123!"
}
```

Para rotas privadas, utilize no Postman **Authorization → Bearer Token** e informe o token retornado no login.

### Cadastro de ponto de descarte

`POST /api/pontos-descarte` — exige autenticação de cooperativa autorizada.

```json
{
  "nome": "Ecoponto Central",
  "tipo": "ECOPONTO",
  "endereco": "Avenida Exemplo, 200",
  "bairro": "Jardins",
  "horario": "Sáb 08-12",
  "cooperativaId": 1,
  "materiaisAceitosIds": [1]
}
```

Os IDs acima são apenas exemplos: devem existir no banco, e o `cooperativaId` deve corresponder à cooperativa autenticada.

## ▶️ Como executar localmente

### Pré-requisitos

- JDK 21.
- Acesso a uma instância Oracle compatível.
- IntelliJ IDEA ou outra IDE Java (opcional).
- Git; Maven Wrapper já incluído no repositório.
- OpenSSL, para gerar as chaves RSA caso ainda não existam.

### 1. Clonar o repositório

```bash
git clone https://github.com/camiexemplar/EcoPlay-FIAP-Global-Solution-CitizenTech.git
cd EcoPlay-FIAP-Global-Solution-CitizenTech
```

### 2. Configurar as variáveis de ambiente

O `application.properties` lê estas variáveis:

| Variável | Descrição |
|---|---|
| `DB_URL` | URL JDBC do banco Oracle |
| `DB_USERNAME` | Usuário do Oracle |
| `DB_PASSWORD` | Senha do Oracle |
| `JWT_PRIVATE_KEY_PATH` | Caminho da chave privada RSA em PEM |
| `JWT_PUBLIC_KEY_PATH` | Caminho da chave pública RSA em PEM |

Exemplo de URL JDBC (substitua pelos dados fornecidos para seu ambiente):

```text
jdbc:oracle:thin:@//HOST:1521/SERVICO
```

**Não publique senhas, tokens ou chaves privadas no GitHub.** As chaves RSA podem ser geradas localmente com OpenSSL:

```bash
mkdir -p keys
openssl genrsa -out keys/private_key.pem 2048
openssl rsa -in keys/private_key.pem -pubout -out keys/public_key.pem
```

Os comandos acima utilizam um terminal compatível com Bash. No Windows, crie a pasta `keys` e execute os comandos OpenSSL no terminal de sua preferência.

> O projeto utiliza `file:${JWT_PRIVATE_KEY_PATH:./keys/private_key.pem}` e `file:${JWT_PUBLIC_KEY_PATH:./keys/public_key.pem}`. Ajuste os caminhos se armazenar as chaves em outro diretório.

### 3. Banco de dados e Flyway

O projeto utiliza **Oracle Database** e migrations em:

```text
src/main/resources/db/migration/
```

Na configuração atual, o Hibernate usa `ddl-auto=validate`; a criação e evolução do esquema ficam sob responsabilidade do Flyway. A configuração de baseline foi utilizada para adaptação ao schema Oracle de desenvolvimento já existente. Em um banco novo, revise essa configuração conforme a estratégia de migração adotada.

### 4. Executar a API

No Windows (PowerShell):

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux/macOS:

```bash
./mvnw spring-boot:run
```

Com a aplicação iniciada, utilize a URL local `http://localhost:8080` no Postman.

## 🧪 Testes realizados

Foram realizados **testes manuais pelo Postman**, incluindo fluxos de cadastro e login, autenticação JWT, autorização por perfil e identidade, CRUD de materiais e pontos de descarte, operações de entregas e identificações e tratamento de erros HTTP.

Os cenários executados no ambiente de desenvolvimento foram **confirmados como aprovados pela equipe**. Esses testes não equivalem a uma validação automática de integração ponta a ponta com Python, .NET ou RabbitMQ.

## 🏗️ Arquitetura da solução

```text
Aplicativo Mobile (React Native / Expo)
                 |
                 | HTTP / REST
                 v
       Backend Java / Spring Boot
       |     |       |        |
       |     |       |        +-- Autenticação JWT / RSA
       |     |       +----------- Entregas e identificações
       |     +------------------- Materiais e pontos de descarte
       +------------------------- Oracle / JPA / Flyway

Integrações da solução (serviços separados):
  - Python / visão computacional: classificação de resíduos
  - .NET: pontos, XP, conquistas e recompensas
  - RabbitMQ: comunicação assíncrona entre serviços
```

### Situação das integrações

- **Java:** endpoints e regras de negócio testados manualmente pelo Postman.
- **Python:** reconhecimento de resíduos por imagem previsto na arquitetura; integração ponta a ponta a validar.
- **.NET:** gamificação e recompensas previstas na arquitetura; integração ponta a ponta a validar.
- **RabbitMQ:** dependência presente no backend; publicação/consumo de eventos entre serviços ainda exige validação específica.
- **Mobile:** aplicação cliente prevista para consumir a API REST.

## 📂 Organização do código

```text
src/main/
├── java/br/com/fiap/ecoplay/
│   ├── config/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── enums/
│   ├── exception/
│   ├── repository/
│   ├── security/
│   ├── service/
│   └── EcoPlayApplication.java
└── resources/
    ├── application.properties
    └── db/migration/
```

## 👥 Equipe

**FIAP — Turma 2TDSPX**

| Integrante | RM |
|---|---|
| Eduarda Weiss Ventura | RM564434 |
| Camily Vitoria Pereira Maciel | RM566520 |
| Lucas Nunes Soares | RM566503 |

## 📌 Repositório

[EcoPlay — Global Solution CitizenTech](https://github.com/camiexemplar/EcoPlay-FIAP-Global-Solution-CitizenTech)

---

<div align="center">

**EcoPlay ♻️ — tecnologia, cidadania e reciclagem em uma só solução.**

</div>
