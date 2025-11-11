package server;

import java.util.*;

public class SimpleJSON {

    public static class JSONObject {
        private Map<String, String> data;

        public JSONObject(Map<String, String> data) {
            this.data = data;
        }

        public String getString(String key) {
            return data.get(key);
        }

        public boolean has(String key) {
            return data.containsKey(key);
        }
    }

    public static JSONObject parseObject(String json) {
        Map<String, String> result = new HashMap<>();
        json = json.trim();

        // Remove outer braces
        if (json.startsWith("{")) {
            json = json.substring(1);
        }
        if (json.endsWith("}")) {
            json = json.substring(0, json.length() - 1);
        }

        // Parse key-value pairs
        boolean inQuotes = false;
        boolean inArray = false;
        int braceDepth = 0;
        StringBuilder currentPair = new StringBuilder();

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inQuotes = !inQuotes;
            }

            if (!inQuotes) {
                if (c == '[')
                    inArray = true;
                if (c == ']')
                    inArray = false;
                if (c == '{')
                    braceDepth++;
                if (c == '}')
                    braceDepth--;

                if (c == ',' && !inArray && braceDepth == 0) {
                    parsePair(currentPair.toString(), result);
                    currentPair = new StringBuilder();
                    continue;
                }
            }

            currentPair.append(c);
        }

        // Parse last pair
        if (currentPair.length() > 0) {
            parsePair(currentPair.toString(), result);
        }

        return new JSONObject(result);
    }

    private static void parsePair(String pair, Map<String, String> result) {
        int colonIndex = pair.indexOf(':');
        if (colonIndex > 0) {
            String key = pair.substring(0, colonIndex).trim().replace("\"", "");
            String value = pair.substring(colonIndex + 1).trim();

            // Remove quotes from string values but keep arrays and numbers as is
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }

            result.put(key, value);
        }
    }

    public static String createObject(Map<String, Object> data) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (!first)
                json.append(",");
            json.append("\"").append(entry.getKey()).append("\":");

            Object value = entry.getValue();
            if (value instanceof String) {
                json.append("\"").append(value).append("\"");
            } else if (value instanceof Number) {
                json.append(value);
            } else if (value instanceof List) {
                json.append(createArray((List<?>) value));
            }
            first = false;
        }
        json.append("}");
        return json.toString();
    }

    public static String createArray(List<?> list) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0)
                json.append(",");
            Object item = list.get(i);
            if (item instanceof String) {
                json.append("\"").append(item).append("\"");
            } else {
                json.append(item);
            }
        }
        json.append("]");
        return json.toString();
    }
}
