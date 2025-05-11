- **Arquitetura de Projeto**
- A arquitetura do projeto é baseada em monolito com estrutura principal em camadas:
    - Camada de Apresentação (Controller)
    - Camada de Serviço (Service)
    - Camada de Persistência (Repository)
    - Camada de Domínio de Dados (Model)

- **Nomes de pacotes**:
- O padrão de projeto é `org.deltacore.delta.*` para os pacotes principais.

- **Convenção de nomes**:
- Classes: `StudentDTO`, para classes de transferência de dados entre camadas, `StudentService`, para classes de regras de negócio e processamentos em si, `StudentController`, para classes que contém APIs REST, `StudentDAO`, para classes de repositório, etc.
- Métodos: `findByUsername()`, `save()`, `update()`, `delete()`, etc.

- **Formato de endpoints REST**:
- Use RESTful e nomes no plural para os recursos:
    - `GET /students-list`
    - `POST /students`
    - `GET /students/{id}`
    - `PUT /students/{id}`
    - `DELETE /students/{id}`

- **Uso de DTOs**:
- Use DTOs para transferir dados entre as camadas. Por exemplo, `StudentDTO` para transferir dados do aluno entre o controller e o service.

- **Transações**:
- Sempre utilize transações nas operações que envolvem múltiplas etapas no banco de dados, para permitir as propriedades ACID.
- Use a anotação `@Transactional` do Spring em caso de múltiplas transações num método. A interface `JpaRepository` já possui suporte a transações para cada método. Mas, caso precise de um método customizado que use múltiplas consultas, use `@Transactional` que irá se sobrepôr ao padrão da interface.

- **Regras de commits**:
- Use o formato de mensagens de commit (os commits devem ser preferencialmente em inglês e iniciar com um verbo no imperativo):
    - `feat:` para novas funcionalidades.
    - `fix:` para correções de bugs.
    - `refactor:` para refatoração de código.

## Git e Fluxo de Trabalho

- **Padrão de branches**:
- `feature/nome-da-funcionalidade`
- `bugfix/nome-do-bug`
- `refactor/nome-da-refatoração`

- **Pull Requests**:
- Sempre crie um Pull Request para cada feature ou correção.
- A revisão do código é obrigatória antes de fazer o merge.

- **Code Reviews**:
- Revisarei o código antes de cada merge após um Pull Request para validar as alterações ou acréscimos antes de finalizar a sprint.

## Testes

- **Escreva testes**:
- Cada nova funcionalidade deve ter seus testes de unidade, principalmente, e de integração (implementarei depois).
- Use **JUnit** para testes unitários e **Mockito** para mock de dependências.
- Use o padrão de nomenclatura `NomeDaClasseTest` para os testes.
- Use o padrão de nomenclatura `nomeDoCenárioTest()` para os métodos de teste.
- Caminho dos testes: `src/test/java/org/deltacore/delta/` (mesmo caminho do código fonte).

## Comunicação e Tarefas

- **Gestão de tarefas**:
- Utilize o **GitHub Projects** para gerenciar as tarefas e o progresso do projeto.

- **Reuniões**:
- A definir

## Exemplos

- **Exemplo de DTO**:
- Todo DTO é um record (por isso se faz necessário o Java 17 ou superior).
- Todo DTO deve ser anotado com @Builder para facilitar a construção de objetos.
- Todo DTO está dentro do pacote `org.deltacore.delta.dto`.

        @Builder
        public record ActivityDTO(
        UUID id,
        
        @NotBlank(message = "Title cannot be blank.")
        String title,
        
        @NotBlank(message = "Description cannot be blank.")
        String description,
        
        @NotNull(message = "Activity type cannot be null.")
        ActivityType activityType,
        
        @NotBlank(message = "Image URL cannot be blank.")
        @URL(message = "Image URL must be a valid URL.")
        String imageUrl,
        
        @Min(value = 1, message = "Recommended level must be at least 1.")
        Integer recommendedLevel,
        
        @NotNull(message = "Max score cannot be null.")
        @DecimalMin(value = "0.0", inclusive = false, message = "Max score must be greater than 0.")
        BigDecimal maxScore) {}

- **Exemplo de Controller**:

- **Exemplo de Service**:

- **Exemplo de Repository**:

- **Exemplo de Model**:

- **Exemplo de Teste**:

## Automação

- **Build**:
- Use o **Maven** para automação de build.

- **Testes**:
- Use o **JUnit** para automação de testes.
- Use o **Mockito** para mock de dependências.

- **Deploy**:
- Use o **Docker** para automação de deploy.
- Use o **Docker Compose** para automação de deploy em múltiplos containers.

- **Código repetitivo**
- Use o **Lombok** para evitar código repetitivo.
- Use o **MapStruct** para evitar código repetitivo em mapeamentos entre DTOs e entidades.

- **Validações**:
- Use validações automáticas com **Bean Validation** do Spring (`@NotBlank`, `@Valid`, etc).

## Tecnologias principais
- **Java**: 17 ou superior
- **Spring Boot**: 3.0 ou superior
- **PostgreSQL**: 17 ou superior
- **Docker**: 27 ou superior