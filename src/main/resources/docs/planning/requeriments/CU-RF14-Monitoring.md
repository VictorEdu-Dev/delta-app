# Listagem de Monitorias
## ID: CU-RF14
Nome: Listagem de Monitorias

Ator Principal: Estudante

## Descrição:
Este caso de uso permite que o estudante visualize uma lista de todas as monitorias disponíveis. Cada item da lista apresenta informações básicas como disciplina associada, nome do monitor responsável, horários disponíveis e o número de vagas restantes. O objetivo é permitir que o aluno escolha a monitoria mais adequada às suas necessidades acadêmicas e de horário.

## Pré-condições:
Monitorias já foram cadastradas previamente no sistema.

Cada monitoria possui ao menos os campos: disciplina, monitor responsável, horários e vagas totais/preenchidas.

## Pós-condições:
A lista de monitorias é apresentada ao estudante com informações básicas.

O estudante pode escolher visualizar detalhes de uma monitoria específica (fora do escopo deste caso).

O sistema garante que os dados estejam atualizados em tempo real ou via cache configurado.

## Fluxo Principal:
- [ ] O estudante acessa a funcionalidade “Monitorias” no sistema.

- [ ] O sistema exibe uma lista com todas as monitorias disponíveis.

Para cada monitoria, o sistema mostra:

- Nome da disciplina

- Nome do monitor responsável

- Horário(s) da monitoria

- Quantidade de vagas restantes

- [ ] O estudante pode visualizar a lista completa e utilizar filtros ou ordenações (caso disponíveis). — fora do escopo, mas considerar expansão

- [ ] O estudante pode clicar em uma monitoria para ver mais detalhes. — fora do escopo

## Fluxo Alternativo 1: Nenhuma monitoria cadastrada
Se não houver monitorias disponíveis:

- [ ] O sistema exibe a mensagem: “Nenhuma monitoria disponível no momento.”

## Exceções:
- [ ] Se ocorrer erro ao carregar a lista de monitorias, o sistema exibe a mensagem: “Erro ao carregar monitorias. Tente novamente.”

## Requisitos Não Funcionais:
A listagem deve ter resposta em tempo real ou em menos de 1 segundo em condições normais.

Os dados devem ser apresentados de forma responsiva e acessível.

As informações devem ser atualizadas periodicamente ou sob demanda para refletir alterações como vagas ocupadas.

## Notas:
Monitorias esgotadas ainda podem ser exibidas, mas com indicação clara de “vagas esgotadas”.

O backend deve fornecer um endpoint específico para listar monitorias com paginação e ordenação por relevância ou horário.