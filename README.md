# Delta App - API Backend

![Delta APP](https://img.shields.io/badge/Delta%20APP-1.0.0-blue)

[English version](README.en.md) • [Versão em português](README.md)


**Delta App** é uma plataforma educacional desenvolvida especialmente para os estudantes do curso de Engenharia de Computação, com foco inicial no campus UFC - Sobral. Este repositório contém a API backend da aplicação, construída em Java com Spring Boot, para fornecer recursos seguros e escaláveis ao frontend e demais consumidores.

---

## Índice

- [Visão Geral](#visão-geral)
- [Principais Funcionalidades](#principais-funcionalidades)
- [Tecnologias e Dependências](#tecnologias-e-dependências)
- [Como Executar Localmente](#como-executar-localmente)
- [Contribuição](#contribuição)
- [Contato](#contato)
- [Notas de Versão](CHANGELOG.md)

---

## Visão Geral

A API backend do Delta App provê endpoints RESTful para autenticação, gestão de usuários, recursos educacionais, integração com banco de dados e funcionalidades específicas do domínio acadêmico.

---

## Principais Funcionalidades

- **Autenticação e Autorização:** Utiliza Spring Security com suporte a JWT.
- **Gestão de Usuários**: CRUD de usuários, perfis e permissões.
- **Persistência de Dados:** Integração com PostgreSQL via Spring Data JPA.
- **Validação:** Camada robusta de validação para segurança e integridade dos dados.
- **Migração de Banco:** Gerenciamento de versões do banco com Flyway.
- **OpenAPI:** Documentação interativa dos endpoints com Swagger UI.
- **Upload de Arquivos:** Integração com Google Cloud Storage.
- **Testes Automatizados:** Cobertura de testes unitários e integração.
- **HATEOAS:** Suporte à navegação dinâmica de recursos via links.

---

## Tecnologias e Dependências

- **Java 21**
- **Spring Boot 3.4.5**
    - spring-boot-starter-data-jpa
    - spring-boot-starter-security
    - spring-boot-starter-web
    - spring-boot-starter-validation
    - spring-boot-starter-hateoas
    - spring-boot-devtools
    - springdoc-openapi-starter-webmvc-ui
- **PostgreSQL** (driver: `org.postgresql:postgresql`)
- **Flyway** (migração de banco)
- **Google Cloud Storage** (`google-cloud-storage`, `google-cloud-storage-control`)
- **Lombok** (boilerplate-free)
- **MapStruct** (mapeamento DTO/entidades)
- **Mockito** (testes)
- **Junit, Spring Security Test** (testes)
- **Docker** (opcional, para conteinerização do ambiente)

---

## Como Executar Localmente

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/VictorEdu-Dev/delta-app.git
   cd delta-app
   ```

2. **Configure as variáveis de ambiente:**  
   Crie um arquivo `.env` na raiz do projeto e defina as variáveis necessárias criando um arquivo `application.properties-dev` em `src/main/resources/` com o seguinte conteúdo:
    ```ini
    # .env

    # gmail
    GMAIL_CLIENT_ID=ABCD EFGH IJKL MNOP
    
    # === Banco de Dados (PostgreSQL) ===
    PG_URL=your_db_host:5432
    PG_USERNAME=your_db_username
    PG_PASSWORD=your_db_password
    PG_DATABASE=your_db_name
    
    # === Serviço de Storage - PATHS - (Google Cloud Storage) ===
    GCP_PROJECT_ID=your_gcp_project_id
    GCP_BUCKET_NAME=your_gcp_bucket_name
    GCP_FOLDER_PATH=prod/activities/
    GCP_FOLDER_PATH_DOC=doc/
    GCP_FOLDER_PATH_IMG=img/
    GCP_FOLDER_PATH_PROFILE=prod/profile/
    GCP_FOLDER_PATH_TUTOR_REQUEST=prod/tutor_request/
    ```
    ```
    # application.properties
    spring.application.name=delta-app
    
    # Database
    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.datasource.url=jdbc:postgresql://${PG_URL}/${PG_DATABASE}?sslmode=verify-full
    spring.datasource.username=${PG_USERNAME}
    spring.datasource.password=${PG_PASSWORD}
    
    spring.jpa.open-in-view=false
    spring.jpa.properties.hibernate.show_sql=true
    spring.jpa.properties.hibernate.hbm2ddl.auto=none
    spring.jpa.properties.hibernate.transaction.jta.platform=org.hibernate.service.jta.platform.internal.NoJtaPlatform
    
    spring.datasource.hikari.max-lifetime=1800000
    spring.datasource.hikari.idle-timeout=600000
    spring.datasource.hikari.connection-timeout=30000
    spring.datasource.hikari.maximum-pool-size=30
    logging.level.com.zaxxer.hikari=warn
    
    spring.flyway.enabled=true
    spring.flyway.locations=classpath:/db.migration
    spring.flyway.baseline-on-migrate=true
    
    # Swagger Config
    springdoc.swagger-ui.disable-swagger-default-url=true
    
    springdoc.swagger-ui.operationsSorter=alpha
    springdoc.swagger-ui.tagsSorter=alpha
    
    app.api.title=Delta Core API
    app.api.description=API para gerenciamento de monitorias, trilhas e atividades da plataforma Delta.
    app.api.version=0.1.0
    app.api.contact.name=Victor Eduardo
    app.api.contact.email=suporte@delta.ufc.br
    app.api.license.name=UFC
    app.api.license.url=
    app.api.server.prod.url=https://api.delta.ufc.br
    app.api.server.prod.description=Servidor de Produção
    app.api.server.local.url=http://localhost:8080
    app.api.server.local.description=Servidor Local
    
    # Servlet Container
    spring.servlet.multipart.max-file-size=5MB
    spring.servlet.multipart.max-request-size=50MB
    
    # Serviço de Storage
    gcp.project.id=${GCP_PROJECT_ID}
    gcp.bucket.name=${GCP_BUCKET_NAME}
    gcp.folder.path=${GCP_FOLDER_PATH}
    gcp.folder.path.doc=${GCP_FOLDER_PATH_DOC}
    gcp.folder.path.img=${GCP_FOLDER_PATH_IMG}

    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.username=delta.api@email.com
    spring.mail.password=${GMAIL_CLIENT_ID}
    spring.mail.properties.mail.smtp.auth=true
    spring.mail.properties.mail.smtp.starttls.enable=true
    ```    
    - No topo do application.properties existente acrescente a seguinte linha:
        > spring.profiles.active=dev
    - Configure as credenciais e autenticação do Google Cloud Storage caso utilize upload de arquivos.

3. **Construa o projeto:**
   ```bash
   ./mvn clean install -DskipTests
   ```

4. **Execute a aplicação:**
   ```bash
   ./mvnw spring-boot:run
   ```
    - Por padrão, a API estará disponível em `http://localhost:8080/api/v1`.

5. **Acesse a documentação interativa:**
    - Swagger/OpenAPI: `http://localhost:8080/api/v1/swagger-ui.html`.


---

## Contribuição

1. Fork este repositório.
2. Crie uma branch para sua feature ou correção (`git checkout -b minha-feature`).
3. Commit suas alterações (`git commit -am 'Minha contribuição'`).
4. Faça push para a branch (`git push origin minha-feature`).
5. Abra um Pull Request detalhando suas mudanças.

---

## Contato

- **Autor:** Victor Eduardo Pita Campos
- **Email:** victoreduardodev@gmail.com
- **Instituição:** UFC - Engenharia de Computação
- **GitHub:** [VictorEdu-Dev](https://github.com/VictorEdu-Dev)

---

> Plataforma educacional para Engenharia de Computação UFC-Sobral.
