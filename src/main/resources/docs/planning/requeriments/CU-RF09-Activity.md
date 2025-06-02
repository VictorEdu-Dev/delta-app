## Envio de Arquivos pelo Aluno para Atividades
### ID: CU-RF09
- **Nome:** Enviar Arquivos em Resposta a uma Atividade
- **Ator Principal:** Aluno autenticado — ignorar autenticação no momento

### Descrição:
Permite que o aluno envie arquivos (PDF, imagens ou documentos) em resposta a uma atividade atribuída. Cada arquivo deve ter no máximo 5 MB.

### Pré-condições:
- A atividade foi previamente criada por um monitor.
- O aluno tem acesso à atividade e está apto a interagir com ela.

### Pós-condições:
- O arquivo é salvo e associado à submissão do aluno para aquela atividade.

### Fluxo Principal:
- [x] Aluno acessa uma atividade pendente.
- [x] Aluno seleciona arquivos para envio (PDF, imagem, documento).
- [x] Sistema valida o tipo e tamanho dos arquivos (máx. 5 MB por arquivo).
- [x] Sistema salva os arquivos e os vincula à submissão do aluno.
- [x] Sistema retorna confirmação de sucesso com os dados dos arquivos enviados.

### Fluxo Alternativo 1: Arquivo inválido
- [x] Se o tipo não for permitido ou o tamanho exceder 5 MB, o sistema rejeita o upload e informa o erro.

### Exceções:
- [x] Falha no upload: retorna "Erro ao enviar arquivo. Tente novamente."

### Requisitos Não Funcionais:
- Os arquivos devem ser armazenados de forma segura, com nomes únicos ou hash.
- A API deve validar tipo e tamanho antes de processar o upload.
- O sistema deve suportar múltiplos uploads simultâneos sem perda de performance.
- A vinculação dos arquivos deve ser por ID do aluno e ID da atividade.

### Notas:
- Tipos permitidos: `.pdf`, `.png`, `.jpg`, `.jpeg`, `.doc`, `.docx`.
- Um aluno pode enviar vários arquivos por atividade.
- O sistema deve impedir substituições não autorizadas.
- Monitores podem visualizar os arquivos enviados pelos alunos.
