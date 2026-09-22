"""Integração por HTTP e WebSocket reais, em portas livres."""

import asyncio
import logging
from contextlib import asynccontextmanager
from urllib.error import HTTPError
from urllib.request import urlopen

import pytest
from websockets.asyncio.client import connect
from websockets.exceptions import ConnectionClosed

from websocket import create_server


@asynccontextmanager
async def running_server():
    sessions = {}
    async with create_server(port=0, sessions=sessions) as server:
        port = server.sockets[0].getsockname()[1]
        yield f"127.0.0.1:{port}", sessions


async def receive(client):
    return await asyncio.wait_for(client.recv(), timeout=2)


async def wait_sessions(sessions, count):
    async def changed():
        while len(sessions) != count:
            await asyncio.sleep(0.01)
    await asyncio.wait_for(changed(), timeout=2)


def test_http_pages_and_missing_route():
    def fetch(url):
        with urlopen(url, timeout=2) as response:
            return response.status, response.headers.get_content_type(), response.read().decode()

    async def scenario():
        async with running_server() as (address, _):
            for path, title in (("/ui-chat", "Chat"), ("/ui-echo", "Echo")):
                status, content_type, body = await asyncio.to_thread(fetch, f"http://{address}{path}")
                assert status == 200
                assert content_type == "text/html"
                assert title in body
                assert "Distribu\u00eddos" in body
            with pytest.raises(HTTPError) as error:
                await asyncio.to_thread(fetch, f"http://{address}/inexistente")
            assert error.value.code == 404
            error.value.close()
    asyncio.run(scenario())


def test_echo_isolated_from_other_echo_and_chat():
    async def scenario():
        async with running_server() as (address, _):
            async with (
                connect(f"ws://{address}/echo", proxy=None) as sender,
                connect(f"ws://{address}/echo", proxy=None) as other,
                connect(f"ws://{address}/chat", proxy=None) as chat,
            ):
                await sender.send("Olá, ação! 🌎")
                assert await receive(sender) == "Olá, ação! 🌎"
                for client in (other, chat):
                    with pytest.raises(TimeoutError):
                        await asyncio.wait_for(client.recv(), timeout=0.15)
    asyncio.run(scenario())


def test_broadcast_three_clients_disconnect_and_reconnect(caplog):
    caplog.set_level(logging.INFO, logger="pg1")

    async def scenario():
        async with running_server() as (address, sessions):
            async with (
                connect(f"ws://{address}/chat", proxy=None) as a,
                connect(f"ws://{address}/chat", proxy=None) as b,
                connect(f"ws://{address}/chat", proxy=None) as c,
            ):
                await wait_sessions(sessions, 3)
                remote_c = c.local_address
                for sender, message in ((a, "A: olá!"), (b, "B: ação e comunicação")):
                    await sender.send(message)
                    assert await asyncio.gather(*(receive(x) for x in (a, b, c))) == [message] * 3
                await c.close()
                await wait_sessions(sessions, 2)
                assert remote_c not in sessions
                await a.send("A: ainda conectados")
                assert await receive(a) == await receive(b) == "A: ainda conectados"
                async with connect(f"ws://{address}/chat", proxy=None) as reconnected:
                    await wait_sessions(sessions, 3)
                    await reconnected.send("C: voltei")
                    assert await asyncio.gather(*(receive(x) for x in (a, b, reconnected))) == ["C: voltei"] * 3
            await wait_sessions(sessions, 0)
    asyncio.run(scenario())
    assert "Conectou chat cliente=('127.0.0.1'," in caplog.text
    assert "Desconectou chat cliente=" in caplog.text
    assert "texto='C: voltei'" in caplog.text


def test_abrupt_disconnect_does_not_break_broadcast():
    async def scenario():
        async with running_server() as (address, sessions):
            async with (
                connect(f"ws://{address}/chat", proxy=None) as a,
                connect(f"ws://{address}/chat", proxy=None) as b,
                connect(f"ws://{address}/chat", proxy=None) as c,
            ):
                await wait_sessions(sessions, 3)
                c.transport.abort()
                await a.send("Servidor continua disponível")
                assert await receive(a) == await receive(b) == "Servidor continua disponível"
                await wait_sessions(sessions, 2)
    asyncio.run(scenario())


def test_invalid_websocket_route_and_server_remains_available():
    async def scenario():
        async with running_server() as (address, _):
            async with connect(f"ws://{address}/inexistente", proxy=None) as client:
                with pytest.raises(ConnectionClosed):
                    await receive(client)
                assert client.close_code == 1008
            async with connect(f"ws://{address}/echo", proxy=None) as client:
                await client.send("ok")
                assert await receive(client) == "ok"
    asyncio.run(scenario())
