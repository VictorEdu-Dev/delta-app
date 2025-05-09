# Requisitos Funcionais - Módulo de Atividades

#### RF01: O sistema deve permitir que o usuário cadastre uma nova atividade informando obrigatoriamente o título, a descrição detalhada, a data e hora de vencimento (prazo), e a categoria associada (challenge, etc). O sistema deve validar que o prazo seja uma data futura.

#### RF02: O sistema deve listar todas as atividades cadastradas pelo usuário autenticado, exibindo título, status (pendente, concluída, atrasada), categoria e prazo, organizadas por ordem de expiração (mais próximo primeiro).

#### RF03: O sistema deve permitir que o usuário (monitor) edite o título, descrição, prazo e categoria de uma atividade, desde que ela ainda não esteja marcada como concluída. Também deve permitir que o usuário exclua uma atividade em qualquer estado.

#### RF04: O sistema deve permitir que o usuário altere o status de uma atividade para "concluída", registrando a data e hora da conclusão automaticamente.

#### RF05: O sistema deve exibir um painel de progresso com o total de atividades do usuário, o número de concluídas, pendentes e atrasadas, apresentando também a porcentagem de conclusão geral.

#### RF06: O sistema deve permitir que o usuário filtre as atividades por status (pendente, concluída, atrasada), intervalo de datas (prazo inicial e final) e por categoria. O filtro deve funcionar em conjunto com a ordenação por data de vencimento.

#### RF07: O sistema deve gerar uma notificação (push ou interna) para o usuário 24 horas antes do vencimento de uma atividade pendente, e uma notificação imediata quando a data/hora do prazo for ultrapassada sem conclusão.

##### // Isso pode acabar sendo deprecado
#### RF08: O sistema deve permitir a criação de atividades recorrentes (ex: semanal, mensal), com base em uma atividade original, replicando título, descrição e categoria automaticamente para as novas ocorrências.

#### RF09: O sistema deve permitir ao usuário anexar arquivos (PDF, imagens ou documentos) às atividades para suporte ou referência. O tamanho máximo de cada arquivo deve ser de 5 MB.

#### RF10: O sistema deve registrar um histórico de alterações em cada atividade, informando data e hora da modificação, campo alterado e novo valor.

#### RF11: O sistema deve permitir a visualização de detalhes completos de uma atividade, incluindo data de criação, data de modificação, status, anexos e histórico de alterações.

#### RF12: O sistema deve oferecer a opção de busca por palavras-chave no título ou descrição das atividades cadastradas.