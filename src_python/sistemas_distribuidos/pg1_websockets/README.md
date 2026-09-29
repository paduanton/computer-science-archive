# Laboratório WebSockets

Antonio de Pádua Santos Júnior — cartão 00318785.

Servidor Python com echo e chat. O echo responde somente ao remetente; o chat distribui cada mensagem a todos os clientes conectados, incluindo quem a enviou. As sessões são identificadas por IP e porta e removidas na desconexão.

## Execução com Docker

Na pasta deste projeto, execute no terminal Linux ou PowerShell:

```sh
docker compose up --build -d servidor
```

Abra http://localhost:8080/ui-echo para o echo ou http://localhost:8080/ui-chat para o chat. No chat, abra duas janelas e envie uma mensagem: ambas a recebem. Os botões **Fechar** e **Reconectar** controlam a conexão.

Um terceiro cliente pode ser aberto no terminal:

```sh
docker compose exec servidor python -m websockets ws://127.0.0.1:8080/chat
```

Digite uma mensagem e pressione Enter. Ela aparece no terminal e nas páginas conectadas. Encerre o cliente com Ctrl+C.

```sh
docker compose logs --tail 30 servidor
docker compose run --rm testes
docker compose down
```

Os logs registram conexão, mensagem e desconexão com IP e porta. Para acompanhar continuamente, use `docker compose logs -f servidor`; Ctrl+C encerra apenas a visualização. Após alterar o código, execute novamente `docker compose up --build -d servidor`.

## Execução direta em Linux

Requer Python 3.12 e suporte a venv:

```sh
python3.12 -m venv .venv
source .venv/bin/activate
python -m pip install -r requirements.txt
python websocket.py
```

Em outro terminal, na mesma pasta:

```sh
source .venv/bin/activate
python -m pytest -v
python -m websockets ws://127.0.0.1:8080/chat
```

## Implementação e resultados

`websocket.py` atende `/echo` e `/chat`, além das páginas `/ui-echo` e `/ui-chat`. As corrotinas usam `async` e `await` para atender conexões concorrentes. O broadcast percorre uma cópia das sessões e trata conexões encerradas individualmente. A remoção ocorre em `finally`.

Os cinco testes passaram em Linux com Python 3.12.14. Eles verificam HTTP, acentos, isolamento do echo, broadcast com três clientes, desconexões, reconexão e rotas inválidas. Os testes abrem servidores em portas livres e usam tempos limite.

- `relatorio.pdf`: descrição, resultados, evidências e código-fonte.
- `evidencias/`: imagens do funcionamento e dos testes.
- `validacao.txt`: resultado da execução dos testes.

## Endereços e problemas comuns

No computador, `localhost:8080` acessa a porta publicada pelo Docker. Dentro do contêiner, `localhost` aponta para o próprio contêiner. O servidor escuta em `0.0.0.0` no Docker para permitir o acesso pela porta publicada.

Se a porta estiver ocupada, encerre o serviço que a utiliza ou escolha outra porta para o computador:

```sh
# Linux
PORTA_HOST=8081 docker compose up --build -d servidor
```

```powershell
# PowerShell
$env:PORTA_HOST = "8081"
docker compose up --build -d servidor
```

Nesse caso, abra http://localhost:8081/ui-chat. Se o Docker estiver indisponível, inicie o Docker Desktop ou o serviço Docker. Para dependências ausentes na execução direta, ative o ambiente virtual e instale `requirements.txt`. Se o cliente estiver desconectado, verifique os logs e clique em **Reconectar**.
