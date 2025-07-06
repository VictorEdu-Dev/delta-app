# Notas de Versão

## [0.2.0-SNAPSHOT] - 2025-07-06
### Destaques

- **Refatoração Estrutural**
    - Reorganização completa dos pacotes do projeto, agora segmentados por domínio: `activity`, `auth`, `profile`, `tracks`, `tutoring` etc.
    - Classes antigas e não utilizadas removidas, reduzindo duplicidade e facilitando manutenção.

- **Autenticação e Segurança**
    - Novo fluxo de autenticação OAuth2 Authorization Server com refresh token persistente e revogável.
    - Endpoints de autenticação remodelados: `/auth/login`, `/auth/refresh`, `/auth/revoke`.
    - DTOs estruturados para respostas de autenticação.
    - Controle de escopo e papéis aprimorado (aluno, tutor, admin).

- **Monitoria/Tutoria**
    - Renomeação de todos os conceitos de “monitoria/monitor” para “tutoria/tutor”.
    - Refatoração dos DTOs, modelos e serviços relacionados.
    - Novos endpoints RESTful para gerenciamento de tutoria, horários, disciplinas, feedback e inscrição.
    - Novas validações e constraints para cadastro de tutoria e horários.
    - Testes unitários e de integração para fluxo de tutoria.

- **Atividades**
    - Refatoração dos DTOs e modelos.
    - Ajuste nos relacionamentos e nomes das entidades.
    - Upload de arquivos aprimorado (documentos e imagens).

- **Infraestrutura**
    - Atualização do `pom.xml`: dependências para OAuth2, Google API, plugins de build/test.
    - Novos scripts de migração Flyway: tabelas de refresh token, blacklist, constraints, etc.
    - Configurações de internacionalização ampliadas.
    - Scripts de migração para alterações em tabelas de monitor, vídeo, tutor e blacklist.

- **Documentação**
    - Atualização dos requisitos funcionais e casos de uso para refletir a nova nomenclatura e regras de negócio.
    - Inclusão dos requisitos do módulo de trilhas (`RF-Tracks.md`).
    - Ajuste nos textos e exemplos para remoção do termo “monitor” em favor de “tutor”.

---

### Upgrade notes

- Para usar os novos fluxos de autenticação, consulte os endpoints `/auth/login`, `/auth/refresh` e `/auth/revoke`. O modelo de token mudou e agora utiliza refresh token persistente.
- Usuários anteriormente cadastrados como “monitor” serão tratados como “tutor”.
- Algumas rotas e payloads mudaram para refletir a nova estrutura RESTful e nomes de entidades.

---