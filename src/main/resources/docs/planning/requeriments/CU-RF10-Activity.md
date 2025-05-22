## Registro de Histórico de Alterações em Atividades
### ID: CU-RF10
- Nome: Registro de Histórico de Alterações em Atividades
- Ator Principal: Usuário autenticado (Monitor ou Aluno) — ignorar autenticação no momento
- Descrição: O sistema registra automaticamente um histórico sempre que uma atividade for alterada. O histórico deve conter a data e hora da modificação, o campo alterado e o novo valor atribuído.

## Pré-condições:
- Deve existir ao menos uma atividade cadastrada no sistema.
- A alteração deve ser realizada por meio das funcionalidades existentes no sistema.

## Pós-condições:
- O sistema registra uma nova entrada no histórico da atividade alterada.

## Fluxo Principal:

- [ ] Usuário altera uma atividade por meio da interface do sistema.

- [ ] Sistema identifica os campos modificados.

- [ ] Para cada campo alterado, o sistema:

- [ ] Captura a data e hora da alteração (utilizando o fuso horário do servidor);

- [ ] Registra o nome do campo alterado;

- [ ] Registra o novo valor atribuído.

- [ ] Sistema armazena a entrada no histórico vinculado à atividade correspondente.

## Fluxo Alternativo 1: Nenhum campo foi alterado
- [ ] Se a tentativa de alteração não modificar nenhum valor da atividade, o sistema não cria entrada no histórico.

## Exceções:
- Se o registro de histórico falhar, a alteração da atividade não é comprometida, mas o sistema deve registrar o erro em log interno.

## Requisitos Não Funcionais:

- O armazenamento do histórico deve ser eficiente e escalável, mantendo a integridade das informações mesmo com grande volume de alterações.

- O histórico deve estar acessível apenas para leitura, sem possibilidade de edição ou exclusão.

- Datas devem ser armazenadas em UTC no banco de dados.

- As comparações de data e hora devem utilizar o fuso horário do servidor.

## Notas:

- O histórico é útil para auditoria e rastreamento de mudanças nas atividades.

- Cada modificação deve gerar um ou mais registros, um para cada campo alterado.

- Os dados do histórico podem ser utilizados posteriormente para exibição ou exportação.

- Este recurso pode ser estendido no futuro para incluir o usuário responsável pela alteração.

