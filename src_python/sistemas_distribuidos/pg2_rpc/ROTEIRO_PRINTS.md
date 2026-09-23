# Prints do PG2

Execute na pasta deste projeto. Capture o terminal com fonte legível e mantenha
visíveis os comandos e resultados. Os screenshots abaixo são os exigidos para
preencher o relatório; os logs de validação não substituem as imagens.

## 01 geração das interfaces

```text
docker compose build servidor
docker compose run --rm -v "${PWD}:/export" testes python -m grpc_tools.protoc -I. --python_out=/export --grpc_python_out=/export calculator.proto
docker compose run --rm testes python -c "import platform, pathlib; print(platform.platform()); print(platform.python_version()); print(*sorted(str(p) for p in pathlib.Path('.').glob('calculator_pb2*.py')), sep='\n')"
```

O protoc normalmente não imprime nada em caso de sucesso. Mostre seu comando
concluído e a listagem dos dois arquivos gerados, junto com o ambiente Linux.
Salve **evidencias/01_geracao.png**.

Para acompanhar os passos didáticos de erro antes da geração e sucesso depois,
execute `docker compose run --rm testes python demonstrar_geracao.py`.
A ausência inicial de módulos é intencional e ocorre somente numa pasta temporária.

## 02 quatro métodos

```text
docker compose run --rm testes python -m pytest -v -k "test_sum or test_multiply or test_maximum or test_divide" --tb=short
```

Devem aparecer os quatro métodos e **28 passed, 10 deselected**.
A seleção inclui também os testes de divisor zero. Ajuste a altura da janela
para mostrar os nomes e o resultado final. Salve **evidencias/02_metodos.png**.

## 03 casos especiais e suíte completa

```text
docker compose run --rm testes python -m pytest -v -k "test_divide or test_invalid_numeric_values" --tb=short
docker compose run --rm testes python -m pytest -q
```

A primeira execução deve terminar com **21 passed, 17 deselected** e mostrar
negativos, restos, divisor zero e valores inválidos. A segunda deve mostrar
**38 passed**. Salve **evidencias/03_casos_especiais.png**.
Se o terminal não comportar tudo, reduza moderadamente a fonte, preservando
legibilidade e mantendo o trecho relevante e o resumo visíveis.

## 04 uso manual

```text
docker compose up -d servidor
docker compose exec -T servidor python calculator_client.py demo
docker compose exec -T servidor python calculator_client.py divide -17 5
docker compose exec -T servidor python calculator_client.py divide 17 0
docker compose exec -T servidor python calculator_client.py sum 1 2
```

A demo retorna 385.3, 42, -2 e divisão 3/resto 2. A divisão negativa retorna
-3/resto -2. A divisão por zero retorna INVALID_ARGUMENT. A última soma retorna 3,
comprovando que o servidor continua ativo. Salve **evidencias/04_cliente.png**.

## Conferência

- [ ] Quatro PNGs salvos com os nomes exatos.
- [ ] Comandos e resultados estão legíveis.
- [ ] Os testes demonstram as quatro operações por gRPC.
- [ ] Os resultados são da versão atual do código.
- [ ] Nome/matrícula preenchidos e relatório Word revisado.

Use Win+Shift+S no Windows e salve cada seleção como PNG em evidencias.
Depois siga a seção Relatório Word e entrega do README.
