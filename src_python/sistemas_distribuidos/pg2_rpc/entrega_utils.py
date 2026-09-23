"""Verificações comuns da entrega; usa apenas a biblioteca padrão."""

import hashlib
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parent


def read_json(name, default=None):
    path = ROOT / name
    return json.loads(path.read_text(encoding="utf-8")) if path.exists() else default


def config():
    return read_json("entrega_config.json")


def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def source_files():
    return config()["codigo"] + config()["gerados"] + [
        "requirements.txt", "pytest.ini", "Dockerfile", "compose.yaml"
    ]


def sources_hash():
    values = {name: sha(ROOT / name) for name in source_files()}
    return hashlib.sha256(json.dumps(values, sort_keys=True).encode()).hexdigest()


def report_inputs_hash():
    names = source_files() + ["entrega_config.json", "identificacao.json", "validacao.json"]
    names += ["evidencias/" + name for name, _ in config()["imagens"]]
    values = {name: sha(ROOT / name) for name in names}
    return hashlib.sha256(json.dumps(values, sort_keys=True).encode()).hexdigest()


def pending():
    problems = []
    identity = read_json("identificacao.json", {})
    for key in ("nome", "matricula"):
        if not str(identity.get(key, "")).strip():
            problems.append("Preencher " + key + " em identificacao.json.")
    for name, _ in config()["imagens"]:
        if not (ROOT / "evidencias" / name).is_file():
            problems.append("Capturar evidencias/" + name + ".")
    validation = read_json("validacao.json", {})
    if (not validation.get("aprovado") or validation.get("sistema") != "Linux"
            or validation.get("fontes_sha256") != sources_hash()):
        problems.append("Executar validar.py em Linux com esta versão dos fontes.")
    return problems
