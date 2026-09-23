"""Reproduza os passos 5 a 8 do roteiro apenas numa cópia temporária."""

import shutil
import subprocess
import sys
import tempfile
from pathlib import Path


def main():
    root = Path(__file__).resolve().parent
    with tempfile.TemporaryDirectory(prefix="pg2-geracao-") as directory:
        target = Path(directory)
        for name in ("calculator.proto", "calculator_server.py", "calculator_integration_test.py", "pytest.ini"):
            shutil.copy2(root / name, target / name)
        print("1. Testes antes de gerar as mensagens (falha esperada)", flush=True)
        before = subprocess.run(
            [sys.executable, "-m", "pytest", "-q"], cwd=target,
            capture_output=True, text=True, timeout=60,
        )
        print(before.stdout, flush=True)
        if before.returncode == 0 or "ModuleNotFoundError" not in before.stdout:
            raise RuntimeError("A execução não apresentou a ausência esperada dos módulos.")
        print("2. Gerando calculator_pb2.py e calculator_pb2_grpc.py", flush=True)
        subprocess.run(
            [sys.executable, "-m", "grpc_tools.protoc", "-I.", "--python_out=.",
             "--grpc_python_out=.", "calculator.proto"], cwd=target, check=True, timeout=60,
        )
        print("3. Testes após a geração", flush=True)
        subprocess.run([sys.executable, "-m", "pytest", "-v"], cwd=target, check=True, timeout=120)


if __name__ == "__main__":
    main()
