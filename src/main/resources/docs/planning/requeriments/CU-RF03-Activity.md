## Edição e Exclusão de Atividade
### ID: CU-RF03
- Nome: Edição e Exclusão de Atividade
- Ator Principal: Usuário autenticado (Monitor)
- Descrição: Este caso de uso permite que o usuário edite os dados de uma atividade (título, descrição, prazo e categoria), desde que a atividade ainda não esteja marcada como concluída. Também permite a exclusão da atividade, independentemente de seu estado.

### Pré-condições:
- O usuário está autenticado no sistema.
- O usuário é o criador da atividade ou possui papel de monitor.
- A atividade existe no sistema.

### Pós-condições:
- Se editada, a atividade é atualizada com os novos dados fornecidos.
- Se excluída, a atividade é removida do sistema.
- Em ambos os casos, ações são restritas ao usuário com permissão adequada.

### Fluxo Principal (Edição):
- [ ] O usuário acessa a tela de edição da atividade.
- [ ] O sistema carrega os dados atuais da atividade.
- [ ] O usuário modifica o título, descrição, prazo e/ou categoria.
- [ ] O sistema verifica se a atividade não está marcada como concluída.
- [ ] O sistema valida que o novo prazo é uma data futura.
- [ ] O usuário confirma a edição da atividade.
- [ ] O sistema atualiza os dados da atividade no banco de dados.
- [ ] O sistema exibe uma mensagem de sucesso indicando que a atividade foi atualizada.

### Fluxo Principal (Exclusão):
- [ ] O usuário acessa a tela de visualização da atividade.
- [ ] O usuário aciona a opção "Excluir atividade".
- [ ] O sistema solicita uma confirmação de exclusão.
- [ ] O usuário confirma a exclusão.
- [ ] O sistema remove a atividade do banco de dados.
- [ ] O sistema exibe uma mensagem de sucesso indicando que a atividade foi excluída.

### Fluxo Alternativo 1: Atividade Concluída
- [ ] Se o usuário tentar editar uma atividade marcada como concluída:
- [ ] O sistema bloqueia a edição.
- [ ] Exibe uma mensagem de erro informando que atividades concluídas não podem ser editadas.

### Exceções:
- [ ] Se o usuário tentar editar ou excluir uma atividade que não existe, o sistema exibe uma mensagem de erro "Atividade não encontrada".
- [ ] Se o usuário não for autorizado, o sistema exibe uma mensagem de erro "Acesso negado".
- [ ] Se os campos obrigatórios na edição não forem preenchidos, o sistema informa os campos faltantes.

### Requisitos Não Funcionais:
- A verificação de status da atividade (concluída ou não) deve ser rápida.
- A interface de edição e exclusão deve seguir os padrões de usabilidade definidos para a plataforma.
- A confirmação de exclusão deve ser clara e evitar exclusões acidentais.

### Notas:
- A exclusão de atividades deve ser uma ação irreversível (hard delete).
- A permissão para editar ou excluir deve ser verificada no backend com base no usuário autenticado.
- O status "concluída" deve ser persistido na entidade de atividade e controlado separadamente da edição.

