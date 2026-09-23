# Sistemas Distribuídos

Dois laboratórios independentes: **PG1 WebSockets** (chat e echo) e **PG2 RPC**
(calculadora gRPC). Cada diretório contém seu enunciado e a implementação derivada
do material da disciplina. Os arquivos originais da raiz são preservados.

- [PG1 WebSockets](pg1_websockets/README.md)
- [PG2 RPC](pg2_rpc/README.md)

O ambiente principal é Linux em contêineres Docker, inclusive quando os comandos
são executados pelo PowerShell no Windows. Cada projeto também funciona com
Python 3.12 em um ambiente virtual Linux.

Os relatórios e pacotes finais dependem de identificação e capturas reais do
funcionamento. Siga o roteiro de evidências de cada projeto antes de empacotar.

## Estado da entrega

Verificação realizada em 23/09/2026:

| Item | Resultado |
|---|---|
| PG1: testes com HTTP e WebSockets reais | 5 aprovados em Linux |
| PG2: testes com chamadas gRPC reais | 38 aprovados em Linux |
| Clientes WebSockets no navegador | Echo, broadcast, desconexão e reconexão conferidos |
| ZIPs de prévia extraídos fora do repositório | Imagens construídas, testes aprovados e servidores acessíveis |
| Rascunhos Word | Todas as páginas renderizadas e revisadas; identificação e prints pendentes |
| Entrega final ao Moodle | Pendente de identificação, prints, revisão dos relatórios e nova conferência dos ZIPs finais |

Os registros automatizados ficam em `validacao.json` e `validacao.txt` de cada
projeto. Os arquivos `dist/*_previa.zip` servem para conferir a execução e não
devem ser enviados como entrega final.

## Próxima etapa

1. Siga o [roteiro de prints do PG1](pg1_websockets/ROTEIRO_PRINTS.md) e o
   [roteiro de prints do PG2](pg2_rpc/ROTEIRO_PRINTS.md). Salve cada PNG na pasta
   `evidencias` indicada, com o nome solicitado.
2. Copie `identificacao.exemplo.json` para `identificacao.json` em cada projeto
   e preencha nome e matrícula; turma e professor são opcionais.
3. Siga a seção de relatório e empacotamento do README de cada laboratório:
   validar em Linux, gerar o Word, conferir as páginas e gerar o ZIP final.
4. Extraia e teste novamente cada pacote final antes do envio ao Moodle.

O empacotador verifica as pendências e a presença das imagens no Word. A revisão
visual das capturas e do relatório continua sendo necessária antes da entrega.
