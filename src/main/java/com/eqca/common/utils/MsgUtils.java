package com.eqca.common.utils;

public class MsgUtils {
    public static String format(String template, Object... args) {
        if (template == null || args == null) return template;

        StringBuilder result = new StringBuilder();
        int argIndex = 0;
        int cursor = 0;

        while (cursor < template.length()) {
            int braceIndex = template.indexOf("{}", cursor);
            if (braceIndex == -1 || argIndex >= args.length) {
                result.append(template.substring(cursor));
                break;
            }

            result.append(template, cursor, braceIndex);
            result.append(args[argIndex] != null ? args[argIndex].toString() : "null");

            cursor = braceIndex + 2;
            argIndex++;
        }

        return result.toString();
    }
}
