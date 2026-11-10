# ♻️ EcoPlay — Backend Java

**FIAP | Global Solution 2026/2 — CitizenTech**  
**Descarte certo vira ponto — e renda para quem recicla.**

## 📖 Sobre o projeto

O **EcoPlay** é uma proposta CitizenTech para incentivar a educação ambiental e o descarte correto de resíduos, aproximando cidadãos, cooperativas e pontos de coleta. A solução contempla informações sobre materiais recicláveis, pontos de descarte, agendamento e acompanhamento de entregas e registro de identificação de resíduos.

Este repositório contém a **API REST Java**. A gamificação e a identificação automática por imagem fazem parte da proposta de evolução e integração com outros serviços, mas não estão implementadas neste backend como integrações operacionais na versão analisada.

### Objetivos

- Facilitar o acesso a orientações de descarte correto.
- Apoiar a localização de pontos de coleta.
- Organizar entregas de recicláveis para cooperativas.
- Contribuir para os ODS **4, 10, 11 e 13**.

## 🚀 Tecnologias verificadas

| Tecnologia | Utilização |
|---|---|
| Java 21 | Linguagem da API |
| Spring Boot 4.1.2 | Aplicação REST |
| Spring Web MVC | Controllers e endpoints |
| Spring Data JPA / Hibernate | Persistência |
| Oracle Database | Banco configurado no projeto |
| Spring Security | Autenticação e regras de acesso |
| JWT com RSA / RS256 | Emissão e verificação de tokens |
| BCrypt | Hash de senhas |
| Bean Validation | Validação dos dados recebidos |
| Spring Boot Actuator | Monitoramento básico |
| Maven | Build e dependências |
| JUnit / Spring Boot Test | Infraestrutura de testes |

> **Dependências presentes, mas não configuradas como funcionalidades operacionais nesta versão:** Spring AMQP/RabbitMQ, Flyway, componentes OAuth2 Client e bibliotecas de SQL Server. A aplicação está configurada para **Oracle**; Flyway está desativado (`spring.flyway.enabled=false`). Não há integração operacional de IA, mensageria, Swagger ou deploy comprovada nos arquivos analisados. A configuração `management.health.rabbit.enabled=false` desativa o health check do RabbitMQ, não a dependência AMQP.

## ✅ O que está implementado

- Cadastro público de usuários com papel **`CIDADAO`**.
- Login por e-mail e senha com BCrypt.
- Emissão de JWT assinado com **RS256**, contendo `userId`, `role`, emissor e expiração de **2 horas**.
- Leitura e validação dos tokens com chave pública RSA.
- Consulta pública de materiais recicláveis e pontos de descarte.
- Controllers, DTOs, services, repositories e entidades para materiais, pontos, entregas e identificações.
- Regras de negócio para agendar, consultar, reagendar, confirmar, recusar e concluir entregas, além de registrar e confirmar identificações.
- Tratamento global de exceções da camada MVC, com respostas JSON para erros de validação, recursos inexistentes, conflitos e outros erros.

### 🔐 Estado atual das permissões

**Liberados sem autenticação:**

- `POST /api/usuarios/cadastro`
- `POST /api/auth/login`
- `GET /api/materiais` e `GET /api/materiais/{id}`
- `GET /api/pontos-descarte` e `GET /api/pontos-descarte/{id}`

**Demais endpoints:** as rotas estão implementadas em controllers, porém **bloqueadas pelo `SecurityConfig` atual**, que termina com `.anyRequest().denyAll()`. Mesmo com JWT válido, elas retornam `403 Forbidden` até que sejam criadas as políticas de autorização por papel e proprietário. O papel `COOPERATIVA` existe no domínio, mas o cadastro público aceita somente `CIDADAO`.

> O `GlobalExceptionHandler` trata exceções da aplicação. Respostas geradas diretamente pelos filtros do Spring Security, como JWT ausente/inválido ou acesso negado, ainda não têm necessariamente o mesmo formato JSON.

## 🔗 Endpoints existentes

**URL base local:** `http://localhost:8080`

| Método | Endpoint | Descrição | Acesso atual |
|---|---|---|---|
| POST | `/api/usuarios/cadastro` | Cadastrar cidadão | Público |
| POST | `/api/auth/login` | Autenticar e receber JWT | Público |
| GET | `/api/usuarios/{id}` | Consultar usuário | Bloqueado |
| GET | `/api/materiais` | Listar materiais (paginado) | Público |
| GET | `/api/materiais/{id}` | Consultar material | Público |
| POST | `/api/materiais` | Cadastrar material | Bloqueado |
| PUT | `/api/materiais/{id}` | Atualizar material | Bloqueado |
| DELETE | `/api/materiais/{id}` | Excluir material | Bloqueado |
| GET | `/api/pontos-descarte` | Listar pontos (paginado) | Público |
| GET | `/api/pontos-descarte/{id}` | Consultar ponto | Público |
| POST | `/api/pontos-descarte` | Cadastrar ponto | Bloqueado |
| PUT | `/api/pontos-descarte/{id}` | Atualizar ponto | Bloqueado |
| DELETE | `/api/pontos-descarte/{id}` | Excluir ponto | Bloqueado |
| POST | `/api/entregas?cidadaoId={id}` | Agendar entrega | Bloqueado |
| GET | `/api/entregas/{id}?cidadaoId={id}` | Consultar entrega | Bloqueado |
| GET | `/api/entregas/cidadao/{cidadaoId}` | Listar por cidadão | Bloqueado |
| GET | `/api/entregas/cooperativa/{cooperativaId}` | Listar por cooperativa | Bloqueado |
| PUT | `/api/entregas/{id}/reagendar?cidadaoId={id}&novaData={AAAA-MM-DD}` | Reagendar | Bloqueado |
| DELETE | `/api/entregas/{id}?cidadaoId={id}` | Excluir agendamento | Bloqueado |
| PATCH | `/api/entregas/{id}/confirmar?cooperativaId={id}` | Confirmar | Bloqueado |
| PATCH | `/api/entregas/{id}/recusar?cooperativaId={id}&motivo={valor}` | Recusar | Bloqueado |
| PATCH | `/api/entregas/{id}/concluir?cooperativaId={id}` | Concluir | Bloqueado |
| POST | `/api/identificacoes?usuarioId={id}` | Registrar identificação | Bloqueado |
| GET | `/api/identificacoes/{id}?usuarioId={id}` | Consultar identificação | Bloqueado |
| GET | `/api/identificacoes/usuario/{usuarioId}` | Listar identificações | Bloqueado |
| PATCH | `/api/identificacoes/{id}/confirmar?usuarioId={id}` | Confirmar identificação | Bloqueado |

**Observação:** a existência de um endpoint no controller não significa que ele esteja liberado no Spring Security. Os parâmetros de consulta exibidos são os exigidos nos métodos correspondentes; requisições de criação/atualização também podem exigir JSON no corpo.

## 🧪 Exemplos de requisições

### Cadastro público

`POST http://localhost:8080/api/usuarios/cadastro`

```json
{
  "nome": "Camily Teste",
  "email": "camily.teste@ecoplay.com",
  "senha": "Senha123!",
  "role": "CIDADAO",
  "bairro": "Centro"
}
```

**Sucesso:** `201 Created`. E-mail já cadastrado: `409 Conflict`. O cadastro público de `COOPERATIVA` não é permitido.

### Login

`POST http://localhost:8080/api/auth/login`

```json
{
  "email": "camily.teste@ecoplay.com",
  "senha": "Senha123!"
}
```

**Sucesso:** `200 OK`, com `token`, `tipo` (`Bearer`) e dados de `usuario`. Credenciais inválidas: `401 Unauthorized`.

Para futuras rotas autorizadas com JWT, enviar o cabeçalho:

```http
Authorization: Bearer SEU_TOKEN
```

### Listagem pública

```http
GET http://localhost:8080/api/materiais
GET http://localhost:8080/api/pontos-descarte
```

Essas duas consultas foram verificadas manualmente, com e sem token, durante a preparação da branch.

### Exemplo de erro em JSON

```json
{
  "status": 409,
  "erro": "Conflict",
  "mensagem": "E-mail já cadastrado no sistema!",
  "caminho": "/api/usuarios/cadastro"
}
```

## 💻 Como executar localmente

### Pré-requisitos

- JDK 21
- Maven ou Maven Wrapper
- Acesso a uma instância Oracle compatível
- OpenSSL para geração das chaves RSA

### 1. Clonar e entrar no projeto

```bash
git clone https://github.com/camiexemplar/EcoPlay-FIAP-Global-Solution-CitizenTech.git
cd EcoPlay-FIAP-Global-Solution-CitizenTech
git switch Security-JWT
```

### 2. Configurar variáveis de ambiente

A aplicação utiliza as seguintes configurações no `application.properties`:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
app.jwt.private-key=file:${JWT_PRIVATE_KEY_PATH:./keys/private_key.pem}
app.jwt.public-key=file:${JWT_PUBLIC_KEY_PATH:./keys/public_key.pem}
app.jwt.issuer=ecoplay-api
```

No IntelliJ, defina `DB_URL`, `DB_USERNAME` e `DB_PASSWORD` em **Run > Edit Configurations > Environment variables**, além dos caminhos das chaves caso não use os valores padrão.

Exemplo de formato da URL Oracle (substitua pelos dados fornecidos para seu ambiente):

```text
DB_URL=jdbc:oracle:thin:@HOST:1521:SERVICO_OU_SID
DB_USERNAME=SEU_USUARIO
DB_PASSWORD=SUA_SENHA
```

### 3. Gerar as chaves RSA

Na raiz do projeto, com OpenSSL instalado:

```bash
mkdir keys
openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -out keys/private_key.pem
openssl pkey -in keys/private_key.pem -pubout -out keys/public_key.pem
```

A chave privada é **PKCS#8**, formato esperado pelo código do projeto. Os arquivos `.pem` e o diretório `/keys/` estão no `.gitignore`. **Nunca versione senhas, tokens ou chaves privadas.**

### 4. Compilar e executar

**Windows / PowerShell:**

```powershell
.\mvnw.cmd clean package
.\mvnw.cmd spring-boot:run
```

Se não usar Maven Wrapper, substitua por `mvn clean package` e `mvn spring-boot:run`.

A aplicação utiliza a porta **8080**. Para executar os testes separadamente:

```powershell
.\mvnw.cmd test
```

> O comando de testes deve ser executado no ambiente configurado com as dependências necessárias; a existência de testes no repositório não equivale a uma execução bem-sucedida confirmada nesta revisão.

## 📁 Estrutura atual

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
│   │   ├── repository/
│   │   ├── security/
│   │   ├── service/
│   │   └── EcoplayApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/br/com/fiap/ecoplay/
```

## 📌 Próximas etapas

1. Implementar permissões por papel e vínculo com o usuário autenticado para os endpoints privados.
2. Padronizar respostas de erro diretamente no Spring Security (`401` e `403`).
3. Criar testes automatizados de autenticação, autorização e regras de negócio.
4. Revisar dependências ainda não utilizadas e definir a estratégia de migração do banco.
5. Implementar e documentar integrações de IA, gamificação e mensageria conforme o escopo da solução.
6. Configurar documentação OpenAPI e deploy quando essas etapas forem desenvolvidas.

## 👩‍💻 Equipe

| Integrante | RM |
|---|---|
| Eduarda Weiss Ventura | RM564434 |
| Camily Vitoria Pereira Maciel | RM566520 |
| Lucas Nunes Soares | RM566503 |
| Samara Porto Souza | RM559072 |
| Maria Gabriela Landim Severo | RM565146 |

**FIAP — Global Solution 2026/2 | CitizenTech**

---

*Projeto acadêmico desenvolvido para a Global Solution da FIAP.*
