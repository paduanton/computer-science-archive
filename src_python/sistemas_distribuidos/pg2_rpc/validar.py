"""Execute a suíte real e registre o resultado e a versão dos fontes."""

import json
import platform
import subprocess
import sys
import xml.etree.ElementTree as ET
from datetime import datetime, timezone

from entrega_utils import ROOT, config, sources_hash


def main():
    (ROOT / ".qa").mkdir(exist_ok=True)
    digest = sources_hash()
    result = {
        "aprovado": False, "sistema": platform.system(),
        "python": platform.python_version(),
        "data_utc": datetime.now(timezone.utc).isoformat(),
        "fontes_sha256": digest,
    }
    target = ROOT / "validacao.json"
    target.write_text(json.dumps(result, indent=2), encoding="utf-8")
    try:
        run = subprocess.run(
            [sys.executable, "-m", "pytest", "-v", "--junitxml=.qa/testes.xml"],
            cwd=ROOT, capture_output=True, text=True, encoding="utf-8",
            errors="replace", timeout=180,
        )
        output = run.stdout + run.stderr
        print(output)
        (ROOT / "validacao.txt").write_text(output, encoding="utf-8")
        suites = ET.parse(ROOT / ".qa/testes.xml").getroot().findall("testsuite")
        result.update({
            key: sum(int(suite.get(key, "0")) for suite in suites)
            for key in ("tests", "failures", "errors", "skipped")
        })
        result["aprovado"] = (
            run.returncode == 0 and result["tests"] >= config()["esperados"]
            and result["failures"] == result["errors"] == result["skipped"] == 0
            and digest == sources_hash()
        )
    except (subprocess.TimeoutExpired, ET.ParseError, OSError) as error:
        result["erro"] = str(error)
        print("Validação interrompida:", error)
    target.write_text(json.dumps(result, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    return 0 if result["aprovado"] else 1


if __name__ == "__main__":
    raise SystemExit(main())
