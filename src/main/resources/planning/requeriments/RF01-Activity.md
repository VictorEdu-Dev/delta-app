## Cadastro de Nova Atividade
### ID: RF01
- Nome: Cadastro de Nova Atividade
- Ator Principal: Usuário autenticado (Administrador ou Estudante com permissão para cadastrar atividades)
Descrição: Este caso de uso permite que o usuário cadastre uma nova atividade no sistema, fornecendo informações como título, descrição, prazo e categoria da atividade. O sistema valida se o prazo fornecido é uma data futura antes de confirmar o cadastro.


### Pré-condições:
- O usuário deve estar autenticado no sistema.
- O usuário deve ter permissão para cadastrar atividades.

### Pós-condições:
- Uma nova atividade é registrada no sistema com os dados fornecidos.
- A atividade é associada à categoria selecionada e o prazo é validado como sendo uma data futura.
- A atividade aparece na lista de atividades do usuário ou da categoria correspondente.

### Fluxo Principal:
- [ ] O usuário acessa a tela de cadastro de atividade.
- [ ] O usuário insere o título da atividade no campo apropriado.
- [ ] O usuário insere a descrição detalhada da atividade.
- [ ] O usuário seleciona a data e hora de vencimento da atividade.
- [ ] O usuário escolhe a categoria da atividade (exemplo: "challenge").
- [ ] O sistema valida que a data de vencimento fornecida é uma data futura.
- [ ] Se o prazo for inválido (não for uma data futura), o sistema exibe uma mensagem de erro e solicita que o usuário insira uma nova data válida.
- [ ] O usuário confirma o cadastro da atividade.
- [ ] O sistema salva as informações da atividade no banco de dados e a associa à categoria selecionada.
- [ ] O sistema exibe uma mensagem de sucesso informando que a atividade foi cadastrada com sucesso.

### Fluxo Alternativo:
#### Fluxo Alternativo 1: Prazo inválido
- [ ] Se o usuário inserir uma data que não é uma data futura, o sistema exibe uma mensagem de erro informando que o prazo precisa ser uma data futura.
- [ ] O usuário corrige a data e repete o fluxo principal.

### Exceções:
- [ ] Se o usuário não preencher todos os campos obrigatórios (título, descrição, prazo e categoria), o sistema exibe uma mensagem informando os campos faltantes.

### Requisitos Não Funcionais:
- [ ] O sistema deve garantir que a validação da data de vencimento seja realizada de maneira rápida e sem afetar a performance do cadastro.
- [ ] A interface deve ser intuitiva e fácil de usar para garantir que os dados sejam inseridos corretamente.

##### Notas:
- O prazo de vencimento deve ser validado em tempo real (ao submeter o formulário).
- O campo de categoria pode ser um dropdown com categorias pré-definidas, facilitando a escolha do usuário.