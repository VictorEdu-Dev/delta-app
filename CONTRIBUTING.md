# **IMPORTANTE**
- Lembrar de habilitar spring.profiles.active=dev no application.properties para rodar o projeto facilemente usando um banco de dados remoto para testes.
- Lembrar de solicitar ao DBA o acesso ao banco de dados remoto para rodar o projeto.

## **Arquitetura de Projeto**
- A arquitetura do projeto é baseada em monolito com estrutura principal em camadas:
    - Camada de Apresentação (Controller)
    - Camada de Serviço (Service)
    - Camada de Persistência (Repository)
    - Camada de Domínio de Dados (Model)

## **Nomes de pacotes**:
- O padrão de projeto é `org.deltacore.delta.*` para os pacotes principais.

## **Convenção de nomes**:
- Classes: `StudentDTO`, para classes de transferência de dados entre camadas, `StudentService`, para classes de regras de negócio e processamentos em si, `StudentController`, para classes que contém APIs REST, `StudentDAO`, para classes de repositório, etc.
- Métodos: `findByUsername()`, `save()`, `update()`, `delete()`, etc.

## **Formato de endpoints REST**:
- Use RESTful e nomes no plural para os recursos:
    - `GET /students-list`
    - `POST /students`
    - `GET /students/{id}`
    - `PUT /students/{id}`
    - `DELETE /students/{id}`

## **Uso de DTOs**:
- Use DTOs para transferir dados entre as camadas. Por exemplo, `StudentDTO` para transferir dados do aluno entre o controller e o service.

## **Transações**:
- Sempre utilize transações nas operações que envolvem múltiplas etapas no banco de dados, para permitir as propriedades ACID.
- Use a anotação `@Transactional` do Spring em caso de múltiplas transações num método. A interface `JpaRepository` já possui suporte a transações para cada método. Mas, caso precise de um método customizado que use múltiplas consultas, use `@Transactional` que irá se sobrepôr ao padrão da interface.

## **Regras de commits**:
- Use o formato de mensagens de commit (os commits devem ser preferencialmente em inglês e iniciar com um verbo no imperativo):
    - `feat:` para novas funcionalidades.
    - `fix:` para correções de bugs.
    - `refactor:` para refatoração de código.
    - 'feature/rf<NUMERO DA RF>-activity': ATENÇÃO, USAR ESTE PADRÃO PARA NOMEAR AS BRANCHES PARA NOVAS FUNCIONALIDADES.

# Git e Fluxo de Trabalho

## **Padrão de branches**:
- `feature/nome-da-funcionalidade`
- `bugfix/nome-do-bug`
- `refactor/nome-da-refatoração`

## **Pull Requests**:
- Sempre crie um Pull Request para cada feature ou correção.
- A revisão do código é obrigatória antes de fazer o merge.

## **Code Reviews**:
- Revisarei o código antes de cada merge após um Pull Request para validar as alterações ou acréscimos antes de finalizar a sprint.

# **Testes**

## **Escreva testes**:
- Cada nova funcionalidade deve ter seus testes de unidade, principalmente, e de integração (implementarei depois).
- Use **JUnit** para testes unitários e **Mockito** para mock de dependências.
- Use o padrão de nomenclatura `NomeDaClasseTest` para os testes.
- Use o padrão de nomenclatura `nomeDoCenárioTest()` para os métodos de teste.
- Caminho dos testes: `src/test/java/org/deltacore/delta/` (mesmo caminho do código fonte).

# **Comunicação e Tarefas**

- **Gestão de tarefas**:
- Utilize o **GitHub Projects** para gerenciar as tarefas e o progresso do projeto.

## **Reuniões**:
- A definir

# Exemplos

## **Exemplo de DTO**:
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

## **Exemplo de Controller**:
- Todo controller deve ser anotado com `@RestController` e obrigatoriamente com `@RequestMapping` para definir o caminho base do recurso.


    @RestController()
    public class Home {
    
        private final GeneralUserService generalUserService;
    
        @Autowired
        public Home(GeneralUserService generalUserService) {
            this.generalUserService = generalUserService;
        }
    
        @GetMapping(path = "/register/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<UserDTO> home(@PathVariable String username) {
            Optional<UserDTO> user = Optional.ofNullable(generalUserService.getUserDBByUsername(username));
            return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }
    
        @PostMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<List<UserDTO>> home(@RequestBody UserDTO user) {
            try {
                generalUserService.saveUserDB(user);
                return ResponseEntity.ok(generalUserService.getAllUsers());
            } catch (Exception e) {
                return ResponseEntity
                        .badRequest()
                        .body(null);
            }
        }
    }

## **Exemplo de Service**:
- Todo service deve ser anotado com `@Service`

      @Service
      public class ActivitiesSectionService {
      private static final int DEFAULT_LIMIT = 20;
      
          private final ActivityMapper activityMapper;
          private final ActivityDAO activityDAO;
      
          @Autowired
          public ActivitiesSectionService(ActivityDAO activityDAO, ActivityMapper activityMapper) {
              this.activityMapper = activityMapper;
              this.activityDAO = activityDAO;
          }
      }

## **Exemplo de Repository**:
- Todo repositório deve ser uma interface que estende `CrudRepository<E, ID>` do Spring Data JPA, primordialmente.

      public interface UserDAO extends JpaRepository<User, UUID> {
        User findByUsername(String username);
      }

## **Exemplo de Model**:

        @EqualsAndHashCode(callSuper = true)
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Entity
        public class Activity extends GeneralData {
            @Id
            @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_seq")
            @SequenceGenerator(
            name = "activity_seq",
            sequenceName = "activity_sequence"
            )
            private Long id;
            
            @Column(nullable = false, unique = true, length = 100)
            private String title;
            
            @Column(length = 500, nullable = false)
            private String description;
            
            @Enumerated(EnumType.STRING)
            @Column(nullable = false)
            private ActivityType activityType;
            
            @Column(nullable = false, length = 100)
            private String imageUrl;
            
            @Column(nullable = false)
            private Integer recommendedLevel;
            
            @Column(precision = 19, scale = 4)
            private BigDecimal maxScore;
            
            @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
            private List<VideoLesson> videoUrl;
            
            @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
            @JoinColumn(name = "subject_id", nullable = false)
            private Subject subject;
        }

## **Exemplo de Teste**:

      class ActivitiesSectionServiceTest {
      
          @Mock
          private ActivityDAO activityDAO;
      
          @Mock
          private ActivityMapper activityMapper;
      
          @InjectMocks
          private ActivitiesSectionService activitiesSectionService;
      
          @BeforeEach
          void setUp() {
              MockitoAnnotations.openMocks(this);
          }
      
          @Test
          void shouldSaveActivity() {
              ActivityDTO dto = new ActivityDTO(
                      10L,
                      "Title",
                      "",
                      ActivityType.CHALLENGE,
                      "http://img.com/img.png",
                      1,
                      BigDecimal.TEN
              );
      
              System.out.println("Validating DTO: " + dto);
      
              try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
                  Validator validator = factory.getValidator();
      
                  Set<ConstraintViolation<ActivityDTO>> violations = validator.validate(dto);
                  if (!violations.isEmpty()) {
                      for (ConstraintViolation<ActivityDTO> violation : violations) {
                          System.err.println(violation.getMessage());
                      }
                      return;
                  }
              }
      
      
              Activity entity = new Activity();
              Mockito.when(activityMapper.toEntity(dto)).thenReturn(entity);
              when(activityMapper.toDTO(any(Activity.class))).thenAnswer(invocation -> {
                  Activity a = invocation.getArgument(0);
                  return new ActivityDTO(
                          a.getId(),
                          a.getTitle(),
                          a.getDescription(),
                          a.getActivityType(),
                          a.getImageUrl(),
                          a.getRecommendedLevel(),
                          a.getMaxScore()
                  );
              });
      
              activitiesSectionService.saveActivity(dto);
      
              Mockito.verify(activityDAO).save(entity);
          }
      }

# Automação

## **Build**:
- Use o **Maven** para automação de build.

## **Testes**:
- Use o **JUnit** para automação de testes.
- Use o **Mockito** para mock de dependências.

## **Deploy**:
- Use o **Docker** para automação de deploy.
- Use o **Docker Compose** para automação de deploy em múltiplos containers.

## **Código repetitivo**
- Use o **Lombok** para evitar código repetitivo.
- Use o **MapStruct** para evitar código repetitivo em mapeamentos entre DTOs e entidades.

## **Validações**:
- Use validações automáticas com **Bean Validation** do Spring (`@NotBlank`, `@Valid`, etc).

## Tecnologias principais
- **Java**: 17 ou superior
- **Spring Boot**: 3.0 ou superior
- **PostgreSQL**: 17 ou superior
- **Docker**: 27 ou superior