"""Cliente de terminal para as quatro operações da calculadora."""

import argparse

import grpc

import calculator_pb2 as messages
from calculator_pb2_grpc import CalculatorStub


def execute(stub, operation, values):
    if operation == "sum":
        a, b = values
        result = stub.Sum(messages.SumRequest(a=a, b=b), timeout=5)
        return f"Sum({a}, {b}) = {result.s}"
    if operation == "multiply":
        a, b = values
        result = stub.Multiply(messages.MultiplyRequest(a=a, b=b), timeout=5)
        return f"Multiply({a}, {b}) = {result.product}"
    if operation == "maximum":
        a, b, c = values
        result = stub.Maximum(messages.MaximumRequest(a=a, b=b, c=c), timeout=5)
        return f"Maximum({a}, {b}, {c}) = {result.maximum}"
    dividend, divisor = values
    result = stub.Divide(messages.DivideRequest(dividend=dividend, divisor=divisor), timeout=5)
    return f"Divide({dividend}, {divisor}): quociente={result.quotient}, resto={result.remainder}"


def main():
    parser = argparse.ArgumentParser(description="Cliente da calculadora gRPC")
    parser.add_argument("--target", default="127.0.0.1:50051", help="endereço:porta do servidor")
    commands = parser.add_subparsers(dest="operation", required=True)
    for name, count in (("sum", 2), ("multiply", 2), ("maximum", 3), ("divide", 2)):
        command = commands.add_parser(name)
        command.add_argument("values", type=float, nargs=count)
    commands.add_parser("demo", help="executar as quatro operações com entradas conhecidas")
    args = parser.parse_args()
    examples = [
        ("sum", [256.5, 128.8]),
        ("multiply", [6, 7]),
        ("maximum", [-8, -2, -5]),
        ("divide", [17, 5]),
    ] if args.operation == "demo" else [(args.operation, args.values)]
    try:
        with grpc.insecure_channel(args.target) as channel:
            stub = CalculatorStub(channel)
            for operation, values in examples:
                print(execute(stub, operation, values))
        return 0
    except grpc.RpcError as error:
        print(f"Erro {error.code().name}: {error.details()}")
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
