# Evidências reais

Capturas reais realizadas no navegador em 23/09/2026 (horário de Brasília). Os terminais usam ttyd em um contêiner Linux temporário com Python 3.12.14. Os comandos Python são executados diretamente nesse contêiner; correspondem às chamadas docker compose descritas no README. Os horários dos logs e terminais estão em UTC. Dois clientes HTML originais foram abertos em painéis independentes lado a lado, conectados ao servidor pela porta local 18080. O cliente C é a CLI oficial de websockets em uma sessão de terminal compartilhada. A mensagem parte de C e retorna aos três clientes. A página do cliente B é fechada antes da segunda mensagem; o log confirma a remoção da sessão e dois clientes ativos.

Os nove PNGs dos dois laboratórios foram capturados diretamente pelo navegador.
Não houve montagem de resultados nem edição dos pixels das capturas.
A apresentação usa os clientes reais e um terminal web com processos reais.
Os scripts temporários de captura não são necessários para executar os laboratórios.

Consulte ../ROTEIRO_PRINTS.md para repetir as demonstrações manualmente.
