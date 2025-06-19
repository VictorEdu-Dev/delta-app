# Notas de Versão

## [0.1.1] - 2025-06-18

## Corrigido
- Corrigido bug no envio de arquivos para o bucket do Google Cloud Storage causado pela inicialização estática incorreta de credenciais na classe GcpConfig.
- A configuração foi refatorada para utilizar injeção de dependência adequada pelo Spring, eliminando o uso de campos static e compatibilizando com o ciclo de vida dos beans.

## [0.1.0-SNAPSHOT] - 2025-06-08

### Adicionado
- Estrutura inicial do projeto backend em Java 21 usando Spring Boot 3.4.5.
- Integração com PostgreSQL via Spring Data JPA.
- Configuração de autenticação e autorização com Spring Security.
- Validação de dados com Spring Validation.
- Migração de banco de dados utilizando Flyway.
- Upload e integração com Google Cloud Storage.
- Documentação automática de API utilizando Springdoc OpenAPI (Swagger UI).
- Suporte a HATEOAS para navegação de recursos.
- Testes automatizados com JUnit, Mockito e Spring Security Test.
- Utilização de MapStruct para mapeamento de entidades e DTOs.
- Scripts e configuração básica para Docker e conteinerização na nuvem.
- Gerenciamento de atividades por usuários autenticados e autorizados.
- Endpoints para upload arquivos paralelos à atividades.

### Em desenvolvimento
- Endpoints para gerenciamento avançado de monitorias.
- Melhoria nas permissões e papéis de usuários (admin, professor, aluno).
- Sistema de notificações e envio de e-mails.
- Internacionalização (i18n) da API.
- Mais testes de integração e cobertura de código.