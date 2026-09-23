# Prints do PG1

Execute os comandos na pasta deste projeto, com Docker Desktop em modo Linux.
Use janelas legíveis, sem cortar o comando ou o resultado. Salve PNGs reais nos
nomes abaixo. Não substitua screenshots por imagens de texto montadas.

## 01 ambiente e servidor

```text
docker compose up --build -d servidor
docker compose exec servidor python -c "import platform; print(platform.platform()); print(platform.python_version())"
docker compose logs --tail 15 servidor
```

Mostre Linux, Python e o log de início na mesma captura.
Salve **evidencias/01_ambiente.png**.

## 02 echo

Abra http://localhost:8080/ui-echo. Digite `Teste de echo: olá!`, envie e confirme
que o texto aparece em Mensagens recebidas. Mostre a URL, o estado Conectado e
a resposta. Salve **evidencias/02_echo.png**.

## 03 broadcast

Abra http://localhost:8080/ui-chat em **duas janelas** do navegador, lado a lado.
Em um terminal adicional:

```text
docker compose exec servidor python -m websockets ws://127.0.0.1:8080/chat
```

Espere as três conexões abrirem. Envie `Cliente A: mensagem para todos` na
primeira janela e `Cliente C: olá pelo terminal` no terminal.
Ambas as mensagens devem aparecer nos três clientes. Enquadre as duas janelas e
o terminal na captura, com letras legíveis.
Salve **evidencias/03_broadcast.png**.

## 04 saída de um cliente e continuidade

Mantenha o terminal conectado. Clique em **Fechar** na segunda janela.
Na primeira, envie `Cliente A: continuamos após a saída de B`.
A primeira janela e o terminal devem receber; a segunda deve estar Desconectado
e sem essa nova mensagem. Em outro terminal execute:

```text
docker compose logs --tail 20 servidor
```

Mostre o registro Desconectou com IP/porta e a mensagem posterior nos clientes
ativos. Salve **evidencias/04_desconexao.png**.
Opcionalmente clique Reconectar e envie outra mensagem para observar nova sessão.

## 05 testes

```text
docker compose run --rm testes
```

Mostre os cinco testes e **5 passed**. Salve **evidencias/05_testes.png**.

## Conferência

- [ ] Cada arquivo tem exatamente o nome indicado e abre normalmente.
- [ ] Os textos, IP/porta nos logs, comandos e resultados estão legíveis.
- [ ] O broadcast mostra os três clientes e a mesma mensagem.
- [ ] A desconexão mostra que os demais clientes continuam funcionando.
- [ ] Os prints correspondem à versão atual do código.
- [ ] Identificação preenchida e relatório Word revisado.

No Windows, Win+Shift+S permite selecionar a região a capturar.
Depois salve a imagem como PNG na pasta evidencias. O envio ao Moodle será feito
somente após incorporar essas imagens ao Word e conferir o ZIP final.
