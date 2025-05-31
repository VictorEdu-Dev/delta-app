# Requisitos Não Funcionais - Módulo de Atividades
### RNF01: Apenas monitores autenticados devem conseguir criar, editar, excluir, concluir ou anexar arquivos em atividades.
### RNF02: Todas as rotas protegidas devem exigir autenticação via token JWT válido, com verificação de escopo do usuário.
### RNF03: O sistema deve rejeitar qualquer data de vencimento anterior ao horário atual do servidor no momento da criação ou edição de atividades.
### RNF04: Alterações em atividades com status "concluída" devem ser bloqueadas, retornando erro HTTP 403 com justificativa clara.
### RNF05: Anexos enviados devem ser validados quanto à extensão (.pdf, .jpg, .png, .docx) e tamanho máximo de 5 MB antes do armazenamento.
### RNF06: Toda ação de criação, modificação ou exclusão deve ser registrada com timestamp, ID do usuário responsável e dados alterados, compondo um histórico auditável.
### RNF07: As respostas da API devem seguir o padrão JSON, com códigos HTTP apropriados e mensagens de erro claras para cada violação de regra.
### RNF08: Dados sensíveis de usuários devem ser protegidos em trânsito com HTTPS obrigatório e nunca expostos nas respostas da API.