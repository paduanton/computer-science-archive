"""Calculadora gRPC derivada da implementaÃ§Ã£o-base da disciplina."""

import argparse
import logging
import math
import platform
from concurrent import futures

import grpc

import calculator_pb2 as messages
from calculator_pb2_grpc import CalculatorServicer, add_CalculatorServicer_to_server

LOGGER = logging.getLogger("pg2")


def require_finite(context, *values):
    if not all(math.isfinite(value) for value in values):
        context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Operandos e resultados devem ser finitos.")


class Calculator(CalculatorServicer):
    def Sum(self, request, context):
        require_finite(context, request.a, request.b)
        result = request.a + request.b
        require_finite(context, result)
        LOGGER.info("Sum(%s, %s) = %s", request.a, request.b, result)
        return messages.SumReply(s=result)

    def Multiply(self, request, context):
        require_finite(context, request.a, request.b)
        result = request.a * request.b
        require_finite(context, result)
        LOGGER.info("Multiply(%s, %s) = %s", request.a, request.b, result)
        return messages.MultiplyReply(product=result)

    def Maximum(self, request, context):
        require_finite(context, request.a, request.b, request.c)
        result = max(request.a, request.b, request.c)
        LOGGER.info("Maximum(%s, %s, %s) = %s", request.a, request.b, request.c, result)
        return messages.MaximumReply(maximum=result)

    def Divide(self, request, context):
        dividend, divisor = request.dividend, request.divisor
        require_finite(context, dividend, divisor)
        if divisor == 0:
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "O divisor nÃ£o pode ser zero.")
        ratio = dividend / divisor
        require_finite(context, ratio)
        quotient = math.trunc(ratio)
        if not -(2**63) <= quotient < 2**63:
            context.abort(grpc.StatusCode.INVALID_ARGUMENT, "Quociente fora do intervalo int64.")
        remainder = dividend - quotient * divisor
        require_finite(context, remainder)
        LOGGER.info("Divide(%s, %s): quociente=%d resto=%s", dividend, divisor, quotient, remainder)
        return messages.DivideReply(quotient=quotient, remainder=remainder)


def create_server(host="127.0.0.1", port=50051):
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    add_CalculatorServicer_to_server(Calculator(), server)
    # Portas dinÃ¢micas nos testes; endereÃ§o concreto do cliente, nunca [::].
    address = f"[{host}]:{port}" if ":" in host else f"{host}:{port}"
    bound_port = server.add_insecure_port(address)
    if not bound_port:
        raise OSError(f"NÃ£o foi possÃ­vel escutar em {address}")
    return server, bound_port


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Servidor da calculadora gRPC")
    parser.add_argument("--host", default="127.0.0.1")
    parser.add_argument("--port", type=int, default=50051)
    args = parser.parse_args()
    logging.basicConfig(level=logging.INFO, format="%(asctime)s %(levelname)s %(message)s")
    server, port = create_server(args.host, args.port)
    server.start()
    LOGGER.info("Ambiente=%s Python=%s", platform.system(), platform.python_version())
    LOGGER.info("Calculadora gRPC em %s:%d", args.host, port)
    try:
        server.wait_for_termination()
    except KeyboardInterrupt:
        pass
    finally:
        server.stop(grace=2).wait()
        LOGGER.info("Servidor encerrado")
