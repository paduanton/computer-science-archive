"""Testes da API por chamadas gRPC reais; servidor criado em porta livre."""

import sys

import grpc
import pytest

import calculator_pb2 as messages
from calculator_pb2_grpc import CalculatorStub
from calculator_server import create_server


@pytest.fixture(scope="module")
def grpc_server():
    server, port = create_server(port=0)
    server.start()
    yield server, port
    server.stop(grace=0).wait()


@pytest.fixture(scope="module")
def listen_address(grpc_server):
    return f"127.0.0.1:{grpc_server[1]}"


@pytest.fixture(scope="module")
def channel(listen_address):
    with grpc.insecure_channel(listen_address) as channel:
        grpc.channel_ready_future(channel).result(timeout=5)
        yield channel


@pytest.fixture(scope="module")
def calculator_client(channel):
    return CalculatorStub(channel)


@pytest.mark.parametrize("a,b,expected", [
    (256.5, 128.8, 385.3), (0, 0, 0), (-3, -4, -7), (-2, 5, 3),
    (0.1, 0.2, 0.3),
])
def test_sum(calculator_client, a, b, expected):
    result = calculator_client.Sum(messages.SumRequest(a=a, b=b), timeout=3)
    assert result.s == pytest.approx(expected)


@pytest.mark.parametrize("a,b,expected", [
    (6, 7, 42), (0, 20, 0), (-3, 4, -12), (-2, -5, 10), (2.5, 1.2, 3),
])
def test_multiply(calculator_client, a, b, expected):
    result = calculator_client.Multiply(messages.MultiplyRequest(a=a, b=b), timeout=3)
    assert result.product == pytest.approx(expected)


@pytest.mark.parametrize("a,b,c,expected", [
    (9, 2, 3, 9), (1, 9, 3, 9), (1, 2, 9, 9), (5, 5, 2, 5),
    (-8, -2, -5, -2), (0, 0, 0, 0), (1.5, 1.6, 1.55, 1.6),
])
def test_maximum(calculator_client, a, b, c, expected):
    result = calculator_client.Maximum(messages.MaximumRequest(a=a, b=b, c=c), timeout=3)
    assert result.maximum == pytest.approx(expected)


@pytest.mark.parametrize("dividend,divisor,quotient,remainder", [
    (20, 5, 4, 0), (17, 5, 3, 2), (0, 5, 0, 0),
    (-17, 5, -3, -2), (17, -5, -3, 2), (-17, -5, 3, -2),
    (7.5, 2, 3, 1.5), (0.5, 2, 0, 0.5), (-(2**63), 1, -(2**63), 0),
])
def test_divide(calculator_client, dividend, divisor, quotient, remainder):
    result = calculator_client.Divide(
        messages.DivideRequest(dividend=dividend, divisor=divisor), timeout=3
    )
    assert result.quotient == quotient
    assert result.remainder == pytest.approx(remainder)
    assert dividend == pytest.approx(result.quotient * divisor + result.remainder)


@pytest.mark.parametrize("divisor", [0, -0.0])
def test_divide_by_zero(calculator_client, divisor):
    with pytest.raises(grpc.RpcError) as error:
        calculator_client.Divide(messages.DivideRequest(dividend=17, divisor=divisor), timeout=3)
    assert error.value.code() == grpc.StatusCode.INVALID_ARGUMENT
    assert "zero" in error.value.details()


@pytest.mark.parametrize("method,rpc_request", [
    ("Sum", messages.SumRequest(a=float("inf"), b=1)),
    ("Sum", messages.SumRequest(a=sys.float_info.max, b=sys.float_info.max)),
    ("Multiply", messages.MultiplyRequest(a=1, b=float("nan"))),
    ("Multiply", messages.MultiplyRequest(a=sys.float_info.max, b=2)),
    ("Maximum", messages.MaximumRequest(a=1, b=2, c=float("-inf"))),
    ("Divide", messages.DivideRequest(dividend=float("nan"), divisor=2)),
    ("Divide", messages.DivideRequest(dividend=1, divisor=float("inf"))),
    ("Divide", messages.DivideRequest(dividend=1e308, divisor=1e-308)),
    ("Divide", messages.DivideRequest(dividend=2**63, divisor=1)),
    ("Divide", messages.DivideRequest(dividend=-(2**64), divisor=1)),
])
def test_invalid_numeric_values(calculator_client, method, rpc_request):
    with pytest.raises(grpc.RpcError) as error:
        getattr(calculator_client, method)(rpc_request, timeout=3)
    assert error.value.code() == grpc.StatusCode.INVALID_ARGUMENT
    # Um erro de argumento não deve encerrar o servidor.
    assert calculator_client.Sum(messages.SumRequest(a=1, b=2), timeout=3).s == 3
