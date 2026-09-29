# Laboratório gRPC

Antonio de Pádua Santos Júnior — cartão 00318785.

Calculadora com quatro procedimentos remotos definidos em `calculator.proto`.

| Método | Entrada | Resposta |
|---|---|---|
| Sum | a, b | s |
| Multiply | a, b | product |
| Maximum | a, b, c | maximum |
| Divide | dividend, divisor | quotient, remainder |

Os campos numéricos usam `double`, exceto `quotient`, que usa `int64`. A divisão trunca o quociente em direção a zero e calcula `remainder = dividend - quotient * divisor`. Por exemplo, -17 dividido por 5 retorna quociente -3 e resto -2. Divisor zero, valores não finitos e resultados fora dos tipos definidos retornam `INVALID_ARGUMENT`.

## Execução com Docker

Na pasta deste projeto, execute no terminal Linux ou PowerShell:

```sh
docker compose up --build -d servidor
docker compose exec -T servidor python calculator_client.py demo
docker compose exec -T servidor python calculator_client.py sum 2 3
docker compose exec -T servidor python calculator_client.py multiply 6 7
docker compose exec -T servidor python calculator_client.py maximum -8 -2 -5
docker compose exec -T servidor python calculator_client.py divide -17 5
docker compose exec -T servidor python calculator_client.py divide 17 0
```

As chamadas retornam, respectivamente, soma 5, produto 42, máximo -2, quociente -3 com resto -2 e erro `INVALID_ARGUMENT`. A divisão por zero encerra o cliente com código 1, e o servidor continua disponível.

```sh
docker compose run --rm testes
docker compose logs --tail 30 servidor
docker compose down
```

Para acompanhar os logs, use `docker compose logs -f servidor`; Ctrl+C encerra apenas a visualização. Após alterar os fontes, execute novamente `docker compose up --build -d servidor`.

## Execução direta em Linux

Requer Python 3.12 e suporte a venv:

```sh
python3.12 -m venv .venv
source .venv/bin/activate
python -m pip install -r requirements.txt
python calculator_server.py
```

Em outro terminal, na mesma pasta:

```sh
source .venv/bin/activate
python calculator_client.py demo
python -m pytest -v
```

## Contrato e interfaces

A IDL em `calculator.proto` define as mensagens e o serviço. Protocol Buffers serializa os dados. `calculator_pb2.py` contém as mensagens e `calculator_pb2_grpc.py` contém o stub do cliente e as interfaces do servidor. Ambos acompanham o projeto e são gerados pelo compilador:

```sh
python -m grpc_tools.protoc -I. --python_out=. --grpc_python_out=. calculator.proto
```

Esse comando requer as dependências instaladas. O Docker também gera os módulos durante a construção da imagem. O servidor implementa `Calculator`; o cliente chama seus métodos por meio de `CalculatorStub`.

Para reproduzir o exercício antes e depois da geração dos módulos:

```sh
python demonstrar_geracao.py
```

Ou, no Docker:

```sh
docker compose exec -T servidor python demonstrar_geracao.py
```

A demonstração usa uma cópia temporária: primeiro ocorre `ModuleNotFoundError`, depois o compilador gera os módulos e os testes passam. O arquivo do servidor é `calculator_server.py`, correspondente às referências a `calculator.py` no enunciado.

## Resultados e arquivos

Os 38 testes passaram em Linux com Python 3.12.14. Eles realizam chamadas gRPC reais e verificam as quatro operações, positivos, negativos, zeros, decimais, empates, divisão com resto e entradas inválidas. O servidor de teste usa uma porta livre e as chamadas possuem tempos limite.

- `relatorio.pdf`: descrição, resultados, evidências e código-fonte.
- `evidencias/`: imagens dos métodos, interfaces e testes.
- `validacao.txt`: resultado da execução dos testes.

## Endereços e problemas comuns

No computador, `localhost:50051` acessa a porta publicada pelo Docker. Dentro do contêiner, `localhost` aponta para o próprio contêiner; o cliente executado por `docker compose exec` acessa o servidor nesse endereço. O servidor escuta em `0.0.0.0` no Docker.

Para uma porta ocupada, encerre o serviço que a utiliza ou altere a porta do computador:

```sh
# Linux
PORTA_HOST=50052 docker compose up --build -d servidor
```

```powershell
# PowerShell
$env:PORTA_HOST = "50052"
docker compose up --build -d servidor
```

Um cliente executado no computador usará `python calculator_client.py --target localhost:50052 demo`. Dentro do contêiner a porta permanece 50051. Se o Docker estiver indisponível, inicie o Docker Desktop ou o serviço Docker. Para dependências ausentes, ative o ambiente virtual e instale `requirements.txt`; para módulos gRPC ausentes, execute o comando de geração acima.
