# Conferência do enunciado PG1

| Passos | Requisito | Implementação e verificação | Evidência |
|---|---|---|---|
| 1 e 2 | Analisar base e executar echo | websocket.py, echo.html; test_echo_isolated_from_other_echo_and_chat | 02_echo.png |
| 3 | Criar handler async de chat | chat e explicação de async/await no README | Código no Word |
| 4 | Sessões por IP/porta e parâmetro sessions | Dicionário por servidor e assinatura do roteiro | Código e 01_ambiente.png |
| 5 | Enviar para todas as sessões | Cópia de sessions.values, incluindo remetente; teste com três clientes | 03_broadcast.png |
| 6 | Remover sessão com finally | Saída normal/abrupta e reconexão testadas | 04_desconexao.png |
| 7 | Rota /chat | web_socket_router | 03_broadcast.png |
| 8 e 9 | Clientes HTML e terminal | chat.html, CLI da biblioteca; roteamento HTTP validado | 03_broadcast.png |
| 10 | Múltiplos clientes | Testes de integração e roteiro com duas janelas + terminal | 03_broadcast.png |
| 11 | Logs de conexão, saída e mensagem com IP/porta | logging em echo/chat; assertions nos logs | 04_desconexao.png |
| 12 e entrega | Corrigir problemas; relatório, fontes e screenshots | 5 testes, gerador Word e empacotador | 05_testes.png e relatorio.docx |

## Fechamento manual

- [ ] Identificação real preenchida.
- [ ] Todas as capturas do roteiro obtidas e conferidas.
- [ ] Word gerado com imagens e código completo.
- [ ] Todas as páginas revisadas visualmente.
- [ ] ZIP final extraído em pasta limpa e testado.
- [ ] Arquivo correto selecionado na atividade correspondente do Moodle.

Os testes aprovados não substituem essas etapas. Uma prévia não é entrega final.

