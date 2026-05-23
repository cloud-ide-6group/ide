package ru.vsu.front.projects.component.editor

/**
 * Поддерживаемые языки, их правила подсветки и маппинг по расширению файла.
 */
enum class CodeLanguage(val keywords: Set<String>) {
    JAVA17(
        setOf(
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class",
            "const", "continue", "default", "do", "double", "else", "enum", "extends", "final",
            "finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int",
            "interface", "long", "native", "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this",
            "throw", "throws", "transient", "try", "void", "volatile", "while", "true", "false", "null",
            "var", "record", "yield", "sealed", "permits"
        )
    ),
    JAVA21(
        JAVA17.keywords + setOf("when")
    ),
    PYTHON(
        setOf(
            "False", "None", "True", "and", "as", "assert", "async", "await", "break", "class",
            "continue", "def", "del", "elif", "else", "except", "finally", "for", "from", "global",
            "if", "import", "in", "is", "lambda", "nonlocal", "not", "or", "pass", "raise",
            "return", "try", "while", "with", "yield"
        )
    ),
    LUA(
        setOf(
            "and", "break", "do", "else", "elseif", "end", "false", "for", "function", "if",
            "in", "local", "nil", "not", "or", "repeat", "return", "then", "true", "until", "while"
        )
    ),
    JAVASCRIPT(
        setOf(
            "break", "case", "catch", "class", "const", "continue", "debugger", "default", "delete",
            "do", "else", "export", "extends", "false", "finally", "for", "function", "if", "import",
            "in", "instanceof", "new", "null", "return", "super", "switch", "this", "throw", "true",
            "try", "typeof", "var", "void", "while", "with", "let", "yield", "async", "await"
        )
    ),
    DEFAULT(emptySet());

    companion object {
        /**
         * Определяет язык программирования по имени файла.
         */
        fun fromFileName(fileName: String): CodeLanguage {
            val extension = fileName.substringAfterLast('.', "").lowercase()
            return when (extension) {
                "java" -> JAVA21
                "py" -> PYTHON
                "lua" -> LUA
                "js" -> JAVASCRIPT
                else -> DEFAULT
            }
        }
    }
}