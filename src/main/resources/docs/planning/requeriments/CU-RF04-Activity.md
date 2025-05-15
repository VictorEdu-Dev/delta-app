## Alteração de Status para Concluída
### ID: CU-RF04
- Nome: Alteração de Status para Concluída
- Ator Principal: Usuário autenticado (Monitor ou Aluno)
### Descrição:
- Este caso de uso permite que o usuário altere o status de uma atividade para "concluída". Ao realizar essa ação, o sistema registra automaticamente a data e a hora da conclusão. A alteração só é permitida se o usuário estiver logado como monitor ou aluno. Esta autenticação e autorização pode ser ignorada no início. O sistema atualmente ainda não possui autenticação e autorização englobados, por decisão de projeto.

### Pré-condições:
- O usuário está autenticado no sistema. - pode ignorar
- O usuário é aluno ou possui papel de monitor. - ignorar
- A atividade existe no sistema.
- A atividade ainda não está marcada como concluída.

### Pós-condições:
- A atividade tem seu status alterado para "concluída".
- Apoós a conclusão, a atividade não pode ser editada ou excluída, exceto por usuários com permissões especiais (como o monitor).
- A data e hora da conclusão são registradas automaticamente.
- O sistema impede futuras edições da atividade (exceto por usuários com permissões especiais, como o monitor).

### Fluxo Principal:

- [ ] O usuário acessa a tela de visualização da atividade.

- [ ] O sistema exibe o status atual da atividade.

- [ ] O usuário aciona a opção "Marcar como concluída".

- [ ] O sistema solicita confirmação da ação.

- [ ] O usuário confirma a alteração.

- [ ] O sistema verifica se o usuário possui permissão. - ignorar, porém atentar-se a práticas para escalabilidade futura

- [ ] O sistema verifica se a atividade ainda não está concluída.

- [ ] O sistema altera o status da atividade para "concluída".

- [ ] O sistema registra a data e hora atuais como data de conclusão.

- [ ] O sistema exibe uma mensagem de sucesso indicando que a atividade foi marcada como concluída.

### Fluxo Alternativo 1: Atividade já concluída

- Se o usuário tentar marcar uma atividade já concluída como concluída novamente:

- [ ] O sistema bloqueia a ação.

- [ ] Exibe uma mensagem informando que a atividade já foi concluída anteriormente.

### Exceções:

- [ ] Se a atividade não existir, o sistema exibe a mensagem de erro: "Atividade não encontrada".

- [ ] Se o usuário não tiver permissão, o sistema exibe a mensagem de erro: "Acesso negado". - ignorar

- [ ] Se ocorrer erro ao registrar a data e hora de conclusão, o sistema exibe a mensagem: "Erro ao registrar conclusão. Tente novamente."

### Requisitos Não Funcionais:
- O registro da data e hora deve utilizar o fuso horário do servidor.
- A operação de alteração de status deve ser executada em tempo real.
- A interface deve seguir os padrões de feedback visual da plataforma, indicando sucesso ou falha da operação. - ignorar
- O botão ou ação de conclusão deve estar claramente identificado e separado de ações destrutivas como "excluir". - vander

### Notas:

- A data de conclusão registrada não deve ser editável.
- O status "concluída" deve ser persistido na entidade de atividade como um campo booleano ou enum, com campo adicional de timestamp para a data/hora da conclusão.
- Atividades concluídas não devem aparecer em listas de atividades pendentes, salvo se o filtro "mostrar todas" estiver ativo. (verificar se outros RFs complementam isto)
- Puta que pariu futebol clube

