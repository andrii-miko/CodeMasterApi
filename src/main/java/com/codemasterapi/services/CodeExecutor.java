package com.codemasterapi.services;

import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.StringWriter;

@Service
public class CodeExecutor {
    public String execute(String code, String language, String input) {
        if ("javascript".equalsIgnoreCase(language)) {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            if (engine == null) {
                return "ERROR: JavaScript engine not available";
            }
            StringWriter output = new StringWriter();
            engine.put("output", output);

            StringBuilder script = new StringBuilder();
            script.append("var inputLines = '").append(input.replace("\n", "\\n").replace("'", "\\'")).append("'.split('\\n');\n");
            script.append("var currentLine = 0;\n");
            script.append("function readline() { return inputLines[currentLine++]; }\n");
            script.append("function print(x) { output.write(String(x) + '\\n'); }\n");
            script.append(code).append("\n");
            script.append("solution();\n");

            try {
                engine.eval(script.toString());
            } catch (Exception e) {
                return "ERROR: " + e.getMessage();
            }
            return output.toString().trim();
        }
        return "ERROR: Language not supported";
    }
}
