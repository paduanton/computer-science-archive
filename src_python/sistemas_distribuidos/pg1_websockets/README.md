# PG1 WebSockets

Servidor de echo e chat em Python, desenvolvido a partir de `PG1-WS.zip`.
O [enunciado](enunciado.pdf) exige sessões, broadcast e logs de IP/porta,
além de relatório com código e screenshots.

## Executar no Windows com Docker Linux

Abra o Docker Desktop e aguarde o mecanismo Linux ficar disponível. No PowerShell,
a partir da raiz deste repositório:

```powershell
cd src_python/sistemas_distribuidos/pg1_websockets
docker compose up --build -d servidor
docker compose logs -f servidor
```

Se você extraiu o ZIP, entre diretamente na pasta `pg1_websockets`.
O último comando acompanha os logs; Ctrl+C para de acompanhá-los e mantém o servidor.

Abra [Echo](http://localhost:8080/ui-echo) e
[Chat](http://localhost:8080/ui-chat). No echo, cada cliente recebe apenas
sua própria mensagem. No chat, todos os clientes conectados recebem cada mensagem,
inclusive quem enviou. Abra duas janelas de chat e use também um terceiro terminal:

```text
docker compose exec servidor python -m websockets ws://127.0.0.1:8080/chat
```

Digite `Cliente C: olá pelo terminal` e pressione Enter. Ctrl+C sai do cliente.
Para echo no terminal, substitua `/chat` por `/echo`.
O botão **Fechar** encerra a conexão; **Reconectar** abre uma nova sessão.
Mensagens anteriores não são armazenadas no servidor nem enviadas a novos clientes.

## Testar e administrar

```text
docker compose run --rm testes
docker compose exec servidor python -c "import platform; print(platform.platform()); print(platform.python_version())"
docker compose logs --tail 40 servidor
docker compose down
```

Resultado esperado: **5 passed**. Os testes usam HTTP e WebSocket reais, portas livres
e limites de tempo. Cobrem páginas, acentuação, echo isolado, três clientes,
broadcast, saída normal/abrupta, remoção de sessão, reconexão e rota inválida.
Após editar o código, execute novamente `docker compose up --build -d servidor`:
a imagem contém uma cópia dos arquivos, sem recarga automática.

## Executar diretamente em Linux

Requer Python 3.12 e suporte a venv. Em Debian/Ubuntu, os pacotes usuais são
`python3`, `python3-venv` e `python3-pip`; confirme `python3 --version`.

```bash
python3 -m venv .venv
source .venv/bin/activate
python -m pip install -r requirements.txt
python websocket.py
```

Em outro terminal com o mesmo venv ativo, use `python -m pytest -v` ou
`python -m websockets ws://127.0.0.1:8080/chat`. Ctrl+C encerra o servidor.

## Como funciona

HTTP entrega os clientes HTML pelas rotas `/ui-echo` e `/ui-chat`. O navegador
abre uma conexão WebSocket persistente em `/echo` ou `/chat`.
`async def` declara uma corrotina, e `await` permite que o event loop atenda
outras conexões enquanto aguarda uma operação de rede; isso não significa
criar uma thread para cada cliente.

O chat mantém um dicionário de sessões indexado por `remote_address`, a tupla
IP/porta TCP. Ao receber uma mensagem, percorre uma cópia de `sessions.values()`
e a envia a todos. O `finally` remove a sessão quando a conexão termina.
Falhas de envio a uma conexão encerrada não impedem o envio às restantes.
Os logs mostram conexão, mensagem e desconexão, sempre com o endereço do cliente.

A assinatura `chat(websocket, sessions={})` foi mantida conforme o roteiro.
O servidor passa explicitamente um dicionário próprio para não depender do
valor padrão mutável entre instâncias. As sessões existem só em memória.

## Ajustes em relação ao material

- O exemplo usa uma API antiga. A implementação usa `websockets.asyncio.server`,
  o caminho em `websocket.request.path` e a assinatura atual de `process_request`.
- O passo de echo no PDF aponta para `/ui-chat`; a página correta é `/ui-echo`.
- A menção à biblioteca gRPC na instalação do PG1 é um erro editorial; este
  projeto utiliza `websockets`.
- A URL WebSocket no HTML deriva do endereço da página, permitindo alterar a porta.
- O endereço remoto visto no Docker pode ser o gateway virtual. As portas distintas
  ainda identificam as conexões dos clientes.

## Endereços e solução de problemas

O servidor escuta em `0.0.0.0:8080` dentro do contêiner. O Docker publica apenas
`127.0.0.1:8080` no computador. Dentro do contêiner, `localhost` aponta para ele
mesmo; em outro contêiner da mesma rede, use `servidor:8080`.

Se a porta estiver ocupada, no PowerShell use `$env:PORTA_HOST = "8081"` antes de
subir o projeto; no Bash use `export PORTA_HOST=8081`. Abra então a porta 8081
no navegador. A porta interna usada por `docker compose exec` continua 8080.
Remova a variável depois com `Remove-Item Env:PORTA_HOST` ou `unset PORTA_HOST`.

| Sintoma | Correção |
|---|---|
| Docker não conecta | Abra o Docker Desktop; confira `docker version` e o modo Linux. |
| Página não abre | Confira `docker compose ps`, porta publicada e logs. |
| Cliente desconectado | Confira o servidor e clique em Reconectar. |
| Biblioteca ausente no Linux | Ative o venv e instale o requirements. |
| Código editado não aparece | Reconstrua a imagem e recarregue a página. |

Referência: [migração da API websockets](https://websockets.readthedocs.io/en/stable/howto/upgrade.html).
Veja também a [matriz de requisitos](CHECKLIST.md).

## Relatório Word e entrega

Leia [o roteiro de prints](ROTEIRO_PRINTS.md). Salve as capturas reais nos nomes
indicados em `evidencias/`. Copie `identificacao.exemplo.json` para
`identificacao.json` e preencha nome e matrícula; turma e professor são opcionais.

Primeiro registre a validação atual:

```text
docker compose run --build --rm entrega python validar.py
```

A geração do Word usa um contêiner separado, sem acrescentar dependências à aplicação:

```text
docker compose run --build --rm relatorio
```

Esse comando gera `relatorio.docx` e incorpora os prints e o código dos laboratórios.
Sem identificação ou capturas, ele explica o que falta. Para conferir o texto antes
dos prints, use `docker compose run --build --rm relatorio python gerar_relatorio.py --rascunho`.
O rascunho se chama `relatorio_rascunho.docx` e não é a versão para entrega.
Também é possível instalar `requirements-relatorio.txt` em um ambiente virtual
e executar `python gerar_relatorio.py` diretamente.

Revise o Word: identificação, explicações, imagens legíveis, código completo e
ausência de campos pendentes. Alterações nos fontes, na identificação ou nos prints
exigem gerar novamente o relatório. Ajustes de formatação no Word são permitidos.

Depois da revisão do Word, gere o ZIP final:

```text
docker compose run --rm entrega
```

A validação anterior executa os testes na cópia de trabalho e registra o resultado real em
`validacao.json` e `validacao.txt`. O empacotador verifica testes atuais e aprovados,
identificação, capturas e relatório contendo as imagens. Ele não avalia se o
conteúdo dos prints está correto: a revisão visual continua necessária.
Se executar validar.py novamente, regenere o Word para atualizar seu registro de resultados.

Antes disso, `docker compose run --rm entrega python empacotar.py --previa`
gera um ZIP explicitamente marcado como prévia, útil para testar a portabilidade.
Nunca envie o ZIP com sufixo `_previa` como entrega final.

Extraia o ZIP final em uma pasta nova, entre na pasta principal extraída e execute:

```text
docker compose -p conferencia-laboratorio build servidor
docker compose -p conferencia-laboratorio run --rm testes
docker compose -p conferencia-laboratorio up -d servidor
docker compose -p conferencia-laboratorio down
```

Pare primeiro a instância original para liberar a porta. Abra também o Word
extraído e confira as imagens. O professor precisa apenas do ZIP daquele projeto.
