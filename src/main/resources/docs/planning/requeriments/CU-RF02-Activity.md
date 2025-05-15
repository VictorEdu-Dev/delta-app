## Listagem de Atividades
### ID: CU-RF02
- Nome: Listagem de Atividades
- Ator Principal: Usuário autenticado (Monitor ou Estudante)
- Descrição: Este caso de uso permite que o usuário visualize todas as atividades cadastradas, exibindo informações como título, status (pendente, concluída, atrasada), categoria e prazo. As atividades são organizadas por ordem de expiração, com as mais próximas do prazo de expiração sendo exibidas primeiro.

### Pré-condições:
- O usuário está autenticado no sistema.
- Existem atividades cadastradas no sistema.

### Pós-condições:
- O sistema exibe uma lista de atividades com título, status, categoria e prazo, organizadas por ordem de expiração.
- O usuário consegue visualizar as atividades de forma clara e objetiva.

### Fluxo Principal:
- [x] O usuário acessa a tela de atividades.
- [x] O sistema carrega a lista de atividades cadastradas do usuário.
- [ ] O sistema exibe as atividades com as informações de título, status (pendente, concluída, atrasada), categoria e prazo.
- [ ] O sistema organiza as atividades por ordem de expiração, exibindo as mais próximas primeiro.

### Exceções:
- Se não houver atividades cadastradas, o sistema exibe uma mensagem informando "Nenhuma atividade encontrada".
- Se ocorrer um erro ao carregar as atividades, o sistema exibe uma mensagem de erro "Erro ao carregar as atividades".

### Requisitos Não Funcionais:
- A listagem de atividades deve ser exibida rapidamente, com no máximo 2 segundos de atraso.
- A interface de listagem deve ser responsiva e adaptar-se a diferentes tamanhos de tela.
- A ordenação das atividades deve ser precisa e consistente.

### Notas:
- As atividades "atrasadas" devem ser destacadas de forma visual para facilitar a identificação.
- O status das atividades pode ser filtrado por categorias para facilitar a visualização.
- O sistema deve garantir que a data de expiração de cada atividade seja corretamente calculada e atualizada.