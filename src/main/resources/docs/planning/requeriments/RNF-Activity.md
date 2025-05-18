# Requisitos Não Funcionais - Módulo de Atividades
#### RNF01: A API deve responder em menos de 1 segundo em 95% das requisições.
#### RNF02: As rotas devem seguir o padrão REST, com verbos HTTP apropriados e respostas em JSON.
#### RNF03: A autenticação deve ser feita por token (JWT), com expiração e renovação seguras.
#### RNF04: Todas as entradas devem ser validadas conforme as regras de negócio.
#### RNF05: As operações sobre atividades devem ser transacionais, garantindo consistência dos dados.
#### RNF06: As datas devem ser armazenadas em UTC e tratadas de forma consistente.
#### RNF07: O histórico de alterações deve registrar data, campo alterado, novo valor e autor.
#### RNF08: O sistema deve registrar logs de ações sensíveis, como criação, edição, exclusão e login.
#### RNF09: Arquivos anexados devem ter tipo e tamanho validados (máx. 5 MB) antes do armazenamento.
#### RNF10: As exceções devem ser tratadas com mensagens claras e status HTTP adequados.
#### RNF11: Deve ser possível executar testes automatizados cobrindo as regras de negócio principais.
#### RNF12: A aplicação deve suportar múltiplas instâncias sem quebra de funcionalidade.