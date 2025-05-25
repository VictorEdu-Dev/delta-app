# Autenticação e Autorização nas Rotas de Atividades
ID: CU-RNF02
Nome:
- Verificar Autenticação e Escopo nas Requisições

Ator Principal: 
- Usuário autenticado (aluno ou monitor)

Descrição:
- Define o comportamento esperado para autenticação e autorização de rotas no módulo de atividades. O sistema deverá implementar um mecanismo de autenticação baseado em JWT e controle de escopo por tipo de usuário (aluno ou monitor), restringindo ações conforme o papel.

## Pré-condições:
- A camada de autenticação e geração de tokens JWT ainda será desenvolvida.
- Cada usuário autenticado receberá um token contendo seu papel no sistema.

## Pós-condições:
- Apenas usuários com token válido acessam as rotas protegidas.
- Ações são permitidas com base no escopo (papel) do usuário.

## Fluxo Principal:
- [ ] Usuário realiza login e recebe um token JWT assinado com chave segura.

- [ ] Usuário faz requisição a uma rota protegida, incluindo o token no cabeçalho Authorization: Bearer <token>.

- [ ] Middleware verifica a validade do token (assinatura, expiração).

- [ ] Middleware extrai e interpreta o papel (ex: aluno, monitor) do payload.

- [ ] Middleware verifica se o papel tem permissão para a rota solicitada.

- [ ] Se autorizado, a requisição é processada normalmente.

## Fluxo Alternativo 1: Token ausente ou inválido
- [ ] Se não houver token ou ele for inválido, o sistema retorna HTTP 401 com mensagem "Token inválido ou ausente."

## Fluxo Alternativo 2: Escopo não autorizado
- [ ] Se o token for válido, mas o escopo não permitir a operação, retorna HTTP 403 com mensagem "Permissão negada."

## Exceções:
- [ ] Falha na verificação: retornar "Erro de autenticação" com status 500, sem expor detalhes internos.

## Requisitos Não Funcionais:
- O JWT deve conter ID do usuário, papel e expiração.

- A assinatura do JWT deve usar um algoritmo seguro (ex: HS256 ou RS256).

- Middleware de autenticação e autorização deve ser modular e reutilizável.

- Todas as rotas sensíveis devem aplicar obrigatoriamente os dois middlewares (auth + scope check).

## Notas:
- Alunos não podem criar, editar ou excluir atividades.
- Monitores têm acesso completo às funcionalidades de gestão.
- A verificação de escopo deve ser configurável por rota.