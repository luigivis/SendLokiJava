package loki.utilities;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Labels implements Cloneable {
    public static final String LEVEL = "level";
    public static final String FATAL = "critical";
    public static final String WARN = "warning";
    public static final String INFO = "info";
    public static final String DEBUG = "debug";
    public static final String VERBOSE = "trace";
    public static final String TRACE = "trace";
    public static final String UNKNOWN = "unknown";
    private TreeMap<String, String> map;

    public static void assertLabelIdentifierNotNullOrEmpty(String labelIdentifier) {
        if (labelIdentifier == null) {
            throw new RuntimeException("Label identifier is null.");
        } else if (labelIdentifier.isEmpty()) {
            throw new RuntimeException("Label identifier is empty.");
        }
    }

    public static void validateLabelIdentifierOrThrow(String labelIdentifier) {
        assertLabelIdentifierNotNullOrEmpty(labelIdentifier);
        char firstChar = labelIdentifier.charAt(0);
        if (!Character.isLetter(firstChar)) {
            throw new RuntimeException("Cannot validate given label identifier: [" + labelIdentifier + "]:  First character is not a letter: [" + firstChar + "].");
        } else {
            for(int i = 1; i < labelIdentifier.length(); ++i) {
                char ch = labelIdentifier.charAt(i);
                if (!Character.isLetterOrDigit(ch) && ch != '_') {
                    throw new RuntimeException("Cannot validate given label identifier: [" + labelIdentifier + "]:  Given character is not a letter or digit: [" + ch + "].");
                }
            }

        }
    }

    public static boolean checkLabelIdentifierWhenNotEmpty(String labelIdentifier) {
        char firstChar = labelIdentifier.charAt(0);
        if (!Character.isLetter(firstChar)) {
            return false;
        } else {
            for(int i = 1; i < labelIdentifier.length(); ++i) {
                char ch = labelIdentifier.charAt(i);
                if (!Character.isLetterOrDigit(ch) && ch != '_') {
                    return false;
                }
            }

            return true;
        }
    }

    private static boolean checkLabelIdentifier(String labelIdentifier) {
        assertLabelIdentifierNotNullOrEmpty(labelIdentifier);
        return checkLabelIdentifierWhenNotEmpty(labelIdentifier);
    }

    public static String prettifyLabelIdentifier(String labelIdentifier) {
        assertLabelIdentifierNotNullOrEmpty(labelIdentifier);
        if (checkLabelIdentifierWhenNotEmpty(labelIdentifier)) {
            return labelIdentifier;
        } else {
            char[] stringBytes = labelIdentifier.toCharArray();
            char firstChar = stringBytes[0];
            if (!Character.isLetter(firstChar)) {
                stringBytes[0] = 'A';
            }

            for(int i = 1; i < stringBytes.length; ++i) {
                char ch = stringBytes[i];
                if (!Character.isLetterOrDigit(ch)) {
                    stringBytes[i] = '_';
                }
            }

            return new String(stringBytes);
        }
    }

    public Labels() {
        this.map = new TreeMap();
    }

    public Labels(Labels other) {
        this.map = new TreeMap(other.map);
    }

    public Labels(Map<String, String> map) {
        this.map = new TreeMap(map);
    }

    public Map<String, String> getMap() {
        return this.map;
    }

    public Labels clone() {
        try {
            Labels cloned = (Labels)super.clone();
            cloned.map = new TreeMap(this.map);
            return cloned;
        } catch (CloneNotSupportedException var2) {
            throw new RuntimeException("Failed to clone.", var2);
        }
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (this == other) {
            return true;
        } else if (!this.getClass().equals(other.getClass())) {
            return false;
        } else {
            Labels otherLabels = (Labels)other;
            return this.map.equals(otherLabels.map);
        }
    }

    public Labels l(String labelName, String labelValue) {
        String prettifiedName = prettifyLabelIdentifier(labelName);
        String prettifiedValue = prettifyLabelIdentifier(labelValue);
        this.map.put(prettifiedName, prettifiedValue);
        return this;
    }

    public Labels l(Map<String, String> map) {
        Iterator var2 = map.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var2.next();
            this.l((String)entry.getKey(), (String)entry.getValue());
        }

        return this;
    }

    public Labels l(Labels other) {
        return this.l((Map)other.map);
    }

    public Labels critical() {
        return this.l("level", "critical");
    }

    public Labels fatal() {
        return this.l("level", "critical");
    }

    public Labels warning() {
        return this.l("level", "warning");
    }

    public Labels info() {
        return this.l("level", "info");
    }

    public Labels debug() {
        return this.l("level", "debug");
    }

    public Labels verbose() {
        return this.l("level", "trace");
    }

    public Labels trace() {
        return this.l("level", "trace");
    }

    public Labels unknown() {
        return this.l("level", "unknown");
    }
}

