"""Echo e chat derivados da implementaÃ§Ã£o-base PG1-WS da disciplina."""

import argparse
import asyncio
import logging
import platform
from http import HTTPStatus
from pathlib import Path
from urllib.parse import urlsplit

from websockets.asyncio.server import serve
from websockets.exceptions import ConnectionClosed

LOGGER = logging.getLogger("pg1")
ROOT = Path(__file__).resolve().parent


def http_handler(connection, request):
    """Sirva os clientes HTML sem interferir no handshake WebSocket."""
    path = urlsplit(request.path).path
    pages = {"/ui-chat": "chat.html", "/ui-echo": "echo.html"}
    if path in pages:
        response = connection.respond(
            HTTPStatus.OK, (ROOT / pages[path]).read_text(encoding="utf-8")
        )
        del response.headers["Content-Type"]
        response.headers["Content-Type"] = "text/html; charset=utf-8"
        return response
    if request.headers.get("Upgrade", "").lower() == "websocket":
        return None
    return connection.respond(HTTPStatus.NOT_FOUND, "PÃ¡gina nÃ£o encontrada.\n")


async def echo(websocket):
    remote = websocket.remote_address
    LOGGER.info("Conectou echo cliente=%s", remote)
    try:
        async for message in websocket:
            LOGGER.info("Mensagem echo cliente=%s texto=%r", remote, message)
            await websocket.send(message)
    except ConnectionClosed:
        pass
    finally:
        LOGGER.info("Desconectou echo cliente=%s", remote)


async def chat(websocket, sessions={}):
    # Assinatura solicitada pelo roteiro. create_server passa um dict prÃ³prio
    # explicitamente, evitando compartilhar o valor padrÃ£o entre servidores.
    remote = websocket.remote_address
    sessions[remote] = websocket
    LOGGER.info("Conectou chat cliente=%s ativos=%d", remote, len(sessions))
    try:
        async for message in websocket:
            LOGGER.info("Mensagem chat cliente=%s texto=%r", remote, message)
            # A sessÃ£o pode ser removida durante um await; itere uma cÃ³pia.
            for socket in list(sessions.values()):
                try:
                    await socket.send(message)
                except ConnectionClosed:
                    # O finally do handler desse cliente remove sua sessÃ£o.
                    continue
    except ConnectionClosed:
        pass
    finally:
        if sessions.get(remote) is websocket:
            sessions.pop(remote)
        LOGGER.info("Desconectou chat cliente=%s ativos=%d", remote, len(sessions))


async def web_socket_router(websocket, sessions):
    path = urlsplit(websocket.request.path).path
    if path == "/echo":
        await echo(websocket)
    elif path == "/chat":
        await chat(websocket, sessions)
    else:
        await websocket.close(code=1008, reason="Rota WebSocket inexistente")


def create_server(host="127.0.0.1", port=8080, sessions=None):
    """Uma tabela de sessÃµes por servidor; port=0 permite testes sem colisÃ£o."""
    if sessions is None:
        sessions = {}

    async def router(websocket):
        await web_socket_router(websocket, sessions)

    return serve(router, host, port, process_request=http_handler)


async def main(host, port):
    async with create_server(host, port):
        LOGGER.info("Ambiente=%s Python=%s", platform.system(), platform.python_version())
        LOGGER.info("Servidor em %s:%d; clientes /ui-echo e /ui-chat", host, port)
        await asyncio.Future()


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Servidor de echo e chat WebSocket")
    parser.add_argument("--host", default="127.0.0.1")
    parser.add_argument("--port", type=int, default=8080)
    args = parser.parse_args()
    logging.basicConfig(level=logging.INFO, format="%(asctime)s %(levelname)s %(message)s")
    try:
        asyncio.run(main(args.host, args.port))
    except KeyboardInterrupt:
        LOGGER.info("Servidor encerrado")
