# PG2 Calculadora RPC

Calculadora gRPC desenvolvida a partir de `grpc.zip`, correspondente à base
descrita no [enunciado](enunciado.pdf) como PG2-RPC.zip. A comunicação é por
chamadas remotas reais, sem interface gráfica.

## Executar no Windows com Docker Linux

No PowerShell, a partir da raiz do repositório:

```powershell
cd src_python/sistemas_distribuidos/pg2_rpc
docker compose up --build -d servidor
docker compose exec -T servidor python calculator_client.py demo
docker compose run --rm testes
```

Se você extraiu o ZIP, entre diretamente na pasta `pg2_rpc`.
A demonstração deve produzir:

```text
Sum(256.5, 128.8) = 385.3
Multiply(6, 7) = 42.0
Maximum(-8, -2, -5) = -2.0
Divide(17, 5): quociente=3, resto=2.0
```

Os testes devem terminar com **38 passed**. Cada chamada tem timeout; a suíte cria
um servidor em porta livre e o encerra automaticamente. Não é preciso subir
o serviço `servidor` para executar os testes.

Outros exemplos e administração:

```text
docker compose exec -T servidor python calculator_client.py sum 10 2.5
docker compose exec -T servidor python calculator_client.py multiply -3 4
docker compose exec -T servidor python calculator_client.py maximum 1 9 3
docker compose exec -T servidor python calculator_client.py divide -17 5
docker compose exec -T servidor python calculator_client.py divide 17 0
docker compose logs -f servidor
docker compose down
```

A divisão por zero imprime `Erro INVALID_ARGUMENT` e retorna código de saída 1.
Isso é o comportamento esperado; o servidor continua disponível.
Ctrl+C interrompe somente o acompanhamento dos logs. Para mudanças no código,
reexecute `docker compose up --build -d servidor`.

## Executar diretamente em Linux

Requer Python 3.12 e venv. Confirme a versão e instale, se necessário, os pacotes
`python3`, `python3-venv` e `python3-pip` de sua distribuição.

```bash
python3 -m venv .venv
source .venv/bin/activate
python -m pip install -r requirements.txt
python -m grpc_tools.protoc -I. --python_out=. --grpc_python_out=. calculator.proto
python -m pytest -v
python calculator_server.py
```

Em outro terminal, ative o mesmo venv e execute
`python calculator_client.py --target 127.0.0.1:50051 demo`.
Ctrl+C encerra o servidor.

## Contrato e resultados

| RPC | Requisição | Resposta |
|---|---|---|
| Sum | `double a, b` | `double s` |
| Multiply | `double a, b` | `double product` |
| Maximum | `double a, b, c` | `double maximum` |
| Divide | `double dividend, divisor` | `int64 quotient; double remainder` |

O contrato original de Sum e seus números de campos foram preservados.
O quociente é truncado em direção a zero: `Divide(-17, 5)` retorna -3 e -2.
O resto é calculado por `dividend - quotient * divisor`, e não pelo operador
`%` do Python, cuja regra para negativos corresponde à divisão arredondada
para baixo. Valores decimais usam a precisão de ponto flutuante de `double`.

Divisor zero, NaN, infinitos, resultado infinito e quociente fora do intervalo
de int64 são rejeitados com `INVALID_ARGUMENT`. Valores omitidos numa mensagem
proto3 têm o padrão zero; portanto omitir o divisor também gera esse erro.

## Como funciona e como gerar as interfaces

`calculator.proto` é a IDL: descreve mensagens e procedimentos. Os números
`= 1`, `= 2` etc. identificam os campos no protocolo; não são índices de
posição em uma lista. Protocol Buffers serializa as mensagens. O cliente utiliza
`CalculatorStub`, e o servidor registra a implementação de `CalculatorServicer`.
A chamada atravessa o canal gRPC e devolve a mensagem de resposta.

`calculator_pb2.py` contém as mensagens e `calculator_pb2_grpc.py` contém
o stub e as interfaces do serviço. Ambos são gerados e não devem ser editados
manualmente. O build Docker os regenera. Para regenerar os arquivos da pasta atual,
este comando funciona no PowerShell e no Bash:

```text
docker compose run --rm -v "${PWD}:/export" testes python -m grpc_tools.protoc -I. --python_out=/export --grpc_python_out=/export calculator.proto
```

Se o .proto foi editado, reconstrua primeiro a imagem. Para observar a falha inicial
prevista no PDF sem apagar os arquivos do projeto:

```text
docker compose run --rm testes python demonstrar_geracao.py
```

O script usa uma cópia temporária, confirma o `ModuleNotFoundError`, gera os
módulos e executa novamente os testes. A primeira falha é intencional; ao final
a suíte deve passar. O arquivo do servidor é `calculator_server.py`; as menções
a `calculator.py` no PDF são inconsistências editoriais.

## Endereços e solução de problemas

O contêiner escuta em `0.0.0.0:50051`; o Docker publica `127.0.0.1:50051`
no computador. O cliente dentro do próprio contêiner usa `127.0.0.1:50051`;
em outro contêiner da rede Compose, deve usar `--target servidor:50051`.

Para liberar uma porta ocupada, pare a instância anterior ou defina
`$env:PORTA_HOST = "50052"` no PowerShell / `export PORTA_HOST=50052` no Bash.
Clientes externos usarão 50052; os internos continuam usando 50051.
Depois remova a variável com `Remove-Item Env:PORTA_HOST` / `unset PORTA_HOST`.

| Sintoma | Correção |
|---|---|
| Docker não conecta | Inicie o Docker Desktop e confira `docker version`. |
| ModuleNotFoundError para pb2 | Instale as dependências e gere novamente os módulos. |
| Versão incompatível de protobuf/grpc | Reinstale o requirements fixado e regenere os módulos. |
| UNAVAILABLE | Confira servidor, endereço e porta em `docker compose ps` e logs. |
| INVALID_ARGUMENT | Corrija os operandos; divisão por zero é rejeitada. |

O canal sem TLS acompanha a proposta didática do enunciado e fica restrito à
máquina local. Referência: [gRPC para Python](https://grpc.io/docs/languages/python/quickstart/).
Veja a [matriz de requisitos](CHECKLIST.md).

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
