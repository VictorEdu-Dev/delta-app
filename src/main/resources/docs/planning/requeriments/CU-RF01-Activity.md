## Cadastro de Nova Atividade
### ID: CU-RF01
- Nome: Cadastro de Nova Atividade
- Ator Principal: Usuário autenticado (Administrador ou Estudante com permissão para cadastrar atividades)
- Descrição: Este caso de uso permite que o usuário cadastre uma nova atividade no sistema, fornecendo informações como título, descrição, prazo e categoria da atividade. O sistema valida se o prazo fornecido é uma data futura antes de confirmar o cadastro.


### Pré-condições:
- O usuário deve estar autenticado no sistema.
- O usuário deve ter permissão para cadastrar atividades.

### Pós-condições:
- Uma nova atividade é registrada no sistema com os dados fornecidos.
- A atividade é associada à categoria selecionada e o prazo é validado como sendo uma data futura.
- A atividade aparece na lista de atividades do usuário ou da categoria correspondente.

### Fluxo Principal:
- [x] O usuário acessa a tela de cadastro de atividade.
- [x] O usuário insere o título da atividade no campo apropriado.
- [x] O usuário insere a descrição detalhada da atividade.
- [x] O usuário seleciona a data e hora de vencimento da atividade.
- [x] O usuário escolhe a categoria da atividade (exemplo: "challenge").
- [x] O sistema valida que a data de vencimento fornecida é uma data futura.
- [x] Se o prazo for inválido (não for uma data futura), o sistema exibe uma mensagem de erro e solicita que o usuário insira uma nova data válida.
- [x] O usuário confirma o cadastro da atividade.
- [x] O sistema salva as informações da atividade no banco de dados e a associa à categoria selecionada.
- [x] O sistema exibe uma mensagem de sucesso informando que a atividade foi cadastrada com sucesso.

### Fluxo Alternativo:
#### Fluxo Alternativo 1: Prazo inválido
- [x] O usuário corrige a data e repete o fluxo principal.

### Exceções:
- [x] Se o usuário não preencher todos os campos obrigatórios (título, descrição, prazo e categoria), o sistema exibe uma mensagem informando os campos faltantes.

### Requisitos Não Funcionais:
- [x] O sistema deve garantir que a validação da data de vencimento seja realizada de maneira rápida e sem afetar a performance do cadastro.

##### Notas:
- O prazo de vencimento deve ser validado em tempo real (ao submeter o formulário).
- O campo de categoria pode ser um dropdown com categorias pré-definidas, facilitando a escolha do usuário.