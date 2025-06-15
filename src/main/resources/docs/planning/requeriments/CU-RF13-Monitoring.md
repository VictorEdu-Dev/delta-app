## Cadastro de Monitoria
### ID: CU-RF13
- Nome: Cadastro de Monitoria
- Ator Principal: Estudante Monitor

### Descrição:
Este caso de uso permite que o estudante monitor cadastre uma nova monitoria no sistema, informando os dados necessários como disciplina, descrição, dias da semana, horário, modalidade (presencial ou online) e número de vagas. O objetivo é disponibilizar a monitoria para que outros alunos possam visualizá-la e se inscrever.

### Pré-condições:
O estudante está autenticado como monitor.

A disciplina informada já está registrada no sistema.

### Pós-condições:
Uma nova monitoria é registrada e aparece na listagem de monitorias disponíveis para os alunos.

O sistema armazena todos os dados fornecidos e os torna disponíveis para futuras consultas.

### Fluxo Principal:
- [x] O estudante monitor acessa a funcionalidade “Cadastrar Monitoria”.

- [x] O sistema exibe o formulário de cadastro com os campos obrigatórios:
- Ver classe "Monitoring" e as restrições de integridade e constraints associadas.

- [x] O estudante preenche todos os campos e confirma o cadastro.

- [ ] O sistema valida os dados inseridos.

- [ ] O sistema salva a nova monitoria e exibe a mensagem: “Monitoria cadastrada com sucesso.”

### Fluxo Alternativo 1: Dados inválidos ou incompletos
- [ ] Se o formulário for enviado com dados faltando ou inválidos, o sistema destaca os campos com erro e exibe mensagens apropriadas para correção.

### Exceções:
- [ ] Se ocorrer erro no servidor ao salvar a monitoria, o sistema exibe a mensagem: “Erro ao cadastrar monitoria. Tente novamente.”

### Requisitos Não Funcionais:
- O cadastro deve ser processado em até 2 segundos em condições normais.

- O formulário deve ser responsivo e acessível.

- O backend deve validar os dados e garantir a integridade das informações.

### Notas:
- Após o cadastro, o monitor pode editar ou excluir a monitoria posteriormente (fora do escopo deste caso).

- O sistema deve permitir formatos flexíveis de horário (ex: 14h–16h ou 08:30–10:00).

- Deve haver tratamento adequado para conflito de horários com outras monitorias do mesmo monitor.