package com.codemasterapi.services;

import org.springframework.stereotype.Service;
import org.graalvm.polyglot.*;

@Service
public class CodeExecutor {
    public String execute(String code, String language, String input) {
        if ("javascript".equalsIgnoreCase(language)) {
            try (Context context = Context.newBuilder("js")
                    .allowAllAccess(true)
                    .build()) {

                context.eval("js", code);

                Value solution = context.getBindings("js").getMember("solution");
                if (solution == null) return "ERROR: solution function not found";

                Value result = solution.execute(input);
                return result.toString();
            } catch (Exception e) {
                return "ERROR: " + e.getMessage();
            }
        }

        return "ERROR: Language not supported";
    }
}
