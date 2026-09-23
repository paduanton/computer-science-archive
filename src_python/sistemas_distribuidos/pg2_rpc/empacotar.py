"""Empacote um laboratório independente; prévias são marcadas explicitamente."""

import argparse
from zipfile import BadZipFile, ZipFile, ZIP_DEFLATED

from entrega_utils import (
    ROOT, config, pending, read_json, report_inputs_hash, sha, source_files,
)

COMMON = [
    "README.md", "CHECKLIST.md", "ROTEIRO_PRINTS.md", "enunciado.pdf",
    "Dockerfile", "Dockerfile.relatorio", "compose.yaml", ".dockerignore", ".gitignore",
    "requirements.txt", "requirements-relatorio.txt", "pytest.ini",
    "entrega_config.json", "entrega_utils.py", "validar.py", "empacotar.py",
    "gerar_relatorio.py", "identificacao.exemplo.json", "evidencias/README.md",
]


def report_problems():
    issues = []
    path = ROOT / "relatorio.docx"
    if not path.is_file():
        return ["Gerar e revisar relatorio.docx."]
    manifest = read_json("relatorio_manifesto.json", {})
    if manifest.get("entradas_sha256") != report_inputs_hash():
        issues.append("Regenerar o Word: fontes, identificação, validação ou capturas mudaram.")
    try:
        with ZipFile(path) as doc:
            import hashlib
            media = {
                hashlib.sha256(doc.read(name)).hexdigest()
                for name in doc.namelist() if name.startswith("word/media/")
            }
        for name, _ in config()["imagens"]:
            if sha(ROOT / "evidencias" / name) not in media:
                issues.append("A captura " + name + " não está incorporada ao Word.")
    except (OSError, ValueError, BadZipFile) as error:
        issues.append("Relatório inválido: " + str(error))
    return issues


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--previa", action="store_true")
    args = parser.parse_args()
    problems = pending()
    if not problems:
        problems += report_problems()
    if problems and not args.previa:
        print("ZIP final não gerado. Pendências:\n- " + "\n- ".join(problems))
        return 1
    details = config()
    names = set(COMMON + source_files())
    if details["projeto"] == "pg2_rpc":
        names.add("demonstrar_geracao.py")
    for optional in ("validacao.json", "validacao.txt", "identificacao.json",
                     "relatorio.docx", "relatorio_manifesto.json"):
        if (ROOT / optional).is_file():
            names.add(optional)
    if args.previa and (ROOT / "relatorio_rascunho.docx").exists():
        names.add("relatorio_rascunho.docx")
    for name, _ in details["imagens"]:
        if (ROOT / "evidencias" / name).is_file():
            names.add("evidencias/" + name)
    missing = [name for name in names if not (ROOT / name).is_file()]
    if missing:
        print("Arquivos ausentes:", ", ".join(sorted(missing)))
        return 1
    for name in names:
        path = ROOT / name
        if path.is_symlink() or not path.resolve().is_relative_to(ROOT):
            raise ValueError("Caminho externo à entrega: " + name)
    output = ROOT / "dist"
    output.mkdir(exist_ok=True)
    base = details["projeto"]
    filename = base + ("_previa" if args.previa else "") + ".zip"
    temporary = output / (filename + ".tmp")
    with ZipFile(temporary, "w", ZIP_DEFLATED) as archive:
        for name in sorted(names):
            archive.write(ROOT / name, arcname=base + "/" + name)
        if args.previa:
            archive.writestr(
                base + "/LEIA_ANTES_DA_ENTREGA.txt",
                "PREVIA PARA TESTE. NAO ENVIAR COMO ENTREGA FINAL.\n"
                + "\n".join(problems or ["Revisar Word e evidências antes do pacote final."]),
            )
    with ZipFile(temporary) as archive:
        bad = archive.testzip()
        if bad:
            raise ValueError("Falha de integridade: " + bad)
    target = output / filename
    temporary.replace(target)
    (output / (filename + ".sha256")).write_text(sha(target) + "  " + filename + "\n", encoding="ascii")
    print("Gerado:", target)
    if args.previa:
        print("Prévia para teste; não representa entrega final.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
