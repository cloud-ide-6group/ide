package ru.vsu.front.projects.component.editor

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration

/**
 * Отвечает за подсветку синтаксиса и разметку кликабельных зон.
 *
 * @param language Язык программирования.
 * @param themeColors Цвета редактора.
 */
class CodeVisualTransformation(
    private val language: CodeLanguage,
    private val themeColors: EditorColors
) : VisualTransformation {

    private val customLinkRegex = Regex("//abc\\d+")
    private val wordRegex = Regex("\\b\\w+\\b")
    private val stringRegex = Regex("\".*?\"")

    override fun filter(text: AnnotatedString): TransformedText {
        val rawText = text.text
        
        val annotatedString = buildAnnotatedString {
            append(rawText)

            wordRegex.findAll(rawText).forEach { match ->
                if (match.value in language.keywords) {
                    addStyle(
                        style = SpanStyle(color = themeColors.keyword, fontWeight = FontWeight.Bold),
                        start = match.range.first,
                        end = match.range.last + 1
                    )
                }
            }

            stringRegex.findAll(rawText).forEach { match ->
                addStyle(
                    style = SpanStyle(color = themeColors.string),
                    start = match.range.first,
                    end = match.range.last + 1
                )
            }

            customLinkRegex.findAll(rawText).forEach { match ->
                addStyle(
                    style = SpanStyle(
                        color = themeColors.link, 
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    ),
                    start = match.range.first,
                    end = match.range.last + 1
                )

                addStringAnnotation(
                    tag = "CUSTOM_LINK",
                    annotation = match.value,
                    start = match.range.first,
                    end = match.range.last + 1
                )
            }
        }

        return TransformedText(annotatedString, OffsetMapping.Identity)
    }
}

/**
 * Содержит цвета для подсветки определенных сценариев.
 */
data class EditorColors(
    val keyword: Color = Color(0xFFCC7832),
    val string: Color = Color(0xFF6A8759),
    val link: Color = Color(0xFF589DF6),
    val lineNumbers: Color = Color(0xFF606366),
    val tabLines: Color = Color(0xFF43454A)
)