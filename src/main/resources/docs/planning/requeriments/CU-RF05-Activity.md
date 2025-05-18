## Filtro de Atividades por Status, Intervalo de Datas e Categoria com Ordenação
### ID: CU-RF06
- Nome: Filtro de Atividades por Status, Intervalo de Datas e Categoria com Ordenação por Data de Vencimento
- Ator Principal: Usuário autenticado (Monitor ou Aluno) — ignorar autenticação no momento

### Descrição:
- Permite que o usuário filtre as atividades com base em status (pendente, concluída, atrasada), intervalo de prazo (data inicial e final) e categoria, exibindo os resultados ordenados por data de vencimento de forma crescente.

### Pré-condições:
- Existem atividades cadastradas no sistema.
- Parâmetros de filtro podem estar ausentes ou preenchidos, porém devem ser válidos se informados (exemplo: intervalo de datas correto).

### Pós-condições:
- O sistema retorna uma lista de atividades filtrada e ordenada conforme os critérios passados.

### Fluxo Principal:
- [x] Usuário envia requisição com parâmetros opcionais: status, prazoInicial, prazoFinal, categoria.
- [x] Sistema valida os parâmetros (formato e lógica).
- [x] Sistema executa a consulta aplicando os filtros recebidos:
- [x] Filtra por status, se informado.
- [x] Filtra pelo intervalo de prazo, se informado.
- [x] Filtra pela categoria, se informado.
- [x] Ordena as atividades pelo campo data de vencimento (ascendente).
- [x] Retorna a lista paginada de atividades.

### Fluxo Alternativo 1: Parâmetros Inválidos
- Caso as datas estejam no formato errado ou intervalo seja inválido (data inicial maior que final), o sistema retorna erro de validação.

### Exceções:
- Se a consulta ao banco falhar, retorna erro genérico: "Erro ao buscar atividades. Tente novamente."

### Requisitos Não Funcionais:
- A operação deve ser feita com eficiência, usando paginação para evitar sobrecarga.
- O filtro e ordenação devem ser realizados no banco de dados, não na camada da aplicação.
- O sistema deve utilizar o fuso horário do servidor para as comparações de datas.
- O sistema deve suportar alta carga sem perda significativa de performance.

### Notas:
- Status “atrasada” é derivado da comparação da data atual com a data de vencimento e o status da atividade.
- Atividades concluídas só aparecem se o filtro permitir.
- O filtro deve permitir combinação dos parâmetros, ou seja, múltiplos filtros juntos.
- A ordenação por data de vencimento é fixa e não pode ser alterada pelo usuário neste caso de uso.
- É importante garantir que as datas estejam armazenadas em formato consistente (ex: UTC no banco) para evitar erros no filtro.
- O endpoint deve aceitar parâmetros opcionais para facilitar uso em diferentes cenários (ex: só status, só categoria, etc).
- A paginação pode ser implementada para garantir respostas rápidas e uso eficiente de recursos.