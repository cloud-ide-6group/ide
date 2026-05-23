package ru.vsu.front.projects.component.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vsu.front.designsystem.component.CodeTogetherAnimatedVerticalScrollBar
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Текстовый редактор кода, поддерживающий подсветку синтаксиса, нумерацию строк,
 * кастомные скроллбары и перехват кликов по ссылкам.
 *
 * @param text Текущий отображаемый текст в редакторе.
 * @param modifier Модификатор для настройки.
 * @param language Выбранный язык программирования для применения правил подсветки синтаксиса.
 * @param onContentChanged Коллбек, вызываемый при каждом изменении текста пользователем.
 * @param onLinkClick Коллбек, вызываемый при клике на кастомные ссылки.
 */
@Composable
fun TextFieldCodeEditor(
    text: String,
    modifier: Modifier = Modifier,
    language: CodeLanguage = CodeLanguage.JAVA17,
    onContentChanged: (String) -> Unit,
    onLinkClick: ((String) -> Unit)? = null
) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(text)) }

    LaunchedEffect(text) {
        if (text != textFieldValue.text) {
            val newSelection = TextRange(
                start = textFieldValue.selection.start.coerceIn(0, text.length),
                end = textFieldValue.selection.end.coerceIn(0, text.length)
            )
            textFieldValue = TextFieldValue(text = text, selection = newSelection)
        }
    }

    val vScrollState = rememberScrollState()
    val hScrollState = rememberScrollState()

    val editorLineHeight = 13.sp * 1.2f

    val textStyle = remember {
        TextStyle(
            fontSize = 13.sp,
            color = Color.White,
            lineHeight = editorLineHeight,
            fontFamily = FontFamily.Monospace,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            )
        )
    }

    val gutterStyle = remember {
        TextStyle(
            fontSize = 14.sp,
            lineHeight = editorLineHeight,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.End,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None
            )
        )
    }

    val lineCount = remember(textFieldValue.text) { textFieldValue.text.count { it == '\n' } + 1 }

    val cursorColor = CodeTogetherTheme.colors.primary
    val gutterBgColor = CodeTogetherTheme.colors.secondaryBackground
    val separatorColor = CodeTogetherTheme.colors.primary.copy(alpha = 0.3f)
    val currentLineBgColor = Color.White.copy(alpha = 0.05f)
    val activeGutterTextColor = CodeTogetherTheme.colors.primaryText
    val gutterTextColor = CodeTogetherTheme.colors.secondaryText

    val customTextSelectionColors = remember(cursorColor) {
        TextSelectionColors(
            handleColor = cursorColor,
            backgroundColor = cursorColor.copy(alpha = 0.3f)
        )
    }

    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    val cursorOffset = textFieldValue.selection.start.coerceIn(0, textFieldValue.text.length)

    val activeLineIndex = remember(textFieldValue.text, cursorOffset, textLayoutResult) {
        textLayoutResult?.let { layout ->
            val line = layout.getLineForOffset(cursorOffset)
            if (line > 0 && cursorOffset == layout.getLineStart(line)) {
                val prevChar = textFieldValue.text.getOrNull(cursorOffset - 1)
                if (prevChar != '\n') line - 1 else line
            } else {
                line
            }
        } ?: 0
    }.coerceIn(0, (lineCount - 1).coerceAtLeast(0))

    val gutterAnnotatedText = remember(lineCount, activeLineIndex) {
        buildAnnotatedString {
            for (i in 0 until lineCount) {
                val isCurrent = i == activeLineIndex
                withStyle(SpanStyle(color = if (isCurrent) activeGutterTextColor else gutterTextColor)) {
                    append((i + 1).toString())
                }
                if (i < lineCount - 1) append("\n")
            }
        }
    }

    Box(
        modifier = modifier
            .background(CodeTogetherTheme.colors.secondaryBackground)
    ) {
        Row(
            modifier = Modifier
                .verticalScroll(vScrollState)
        ) {
            Box(
                modifier = Modifier.background(gutterBgColor)
            ) {
                Canvas(Modifier.matchParentSize()) {
                    textLayoutResult?.let { layout ->
                        val top = layout.getLineTop(activeLineIndex) + 4.dp.toPx()
                        val bottom = layout.getLineBottom(activeLineIndex) + 4.dp.toPx()

                        drawRect(
                            color = currentLineBgColor,
                            topLeft = Offset(0f, top),
                            size = Size(size.width, bottom - top)
                        )
                    }

                    drawLine(
                        color = separatorColor,
                        start = Offset(size.width - 1.dp.toPx(), 0f),
                        end = Offset(size.width - 1.dp.toPx(), size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                Text(
                    text = gutterAnnotatedText,
                    style = gutterStyle,
                    modifier = Modifier.padding(start = 8.dp, end = 12.dp, top = 4.dp, bottom = 4.dp)
                )
            }

            CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                BasicTextField(
                    value = textFieldValue,
                    onValueChange = { newValue ->
                        textFieldValue = newValue
                        if (newValue.text != text) {
                            onContentChanged(newValue.text)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(hScrollState)
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent(PointerEventPass.Initial)
                                    val change = event.changes.firstOrNull()
                                    if (change != null) {
                                        if (change.pressed && !change.previousPressed) {
                                            textLayoutResult?.let { layout ->
                                                val clickOffset = layout.getOffsetForPosition(
                                                    Offset(
                                                        change.position.x - 8.dp.toPx(),
                                                        change.position.y - 4.dp.toPx()
                                                    )
                                                )
                                                val newSelection = TextRange(clickOffset)
                                                if (textFieldValue.selection != newSelection) {
                                                    textFieldValue = textFieldValue.copy(selection = newSelection)
                                                }
                                            }
                                        } else if (!change.pressed && change.previousPressed) {
                                            textLayoutResult?.let { layout ->
                                                val clickOffset = layout.getOffsetForPosition(
                                                    Offset(
                                                        change.position.x - 8.dp.toPx(),
                                                        change.position.y - 4.dp.toPx()
                                                    )
                                                )
                                                val linkRegex = Regex("//abc\\d+")
                                                val match = linkRegex.findAll(textFieldValue.text).find { clickOffset in it.range }
                                                if (match != null) {
                                                    onLinkClick?.invoke(match.value)
                                                    change.consume()
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        },
                    textStyle = textStyle,
                    cursorBrush = SolidColor(cursorColor),
                    onTextLayout = { textLayoutResult = it },
                    visualTransformation = remember(language) { CodeVisualTransformation(language, EditorColors()) },
                    decorationBox = { innerTextField ->
                        Box {
                            Canvas(Modifier.matchParentSize()) {
                                textLayoutResult?.let { layout ->
                                    val top = layout.getLineTop(activeLineIndex) + 4.dp.toPx()
                                    val bottom = layout.getLineBottom(activeLineIndex) + 4.dp.toPx()

                                    drawRect(
                                        color = currentLineBgColor,
                                        topLeft = Offset(0f, top),
                                        size = Size(size.width + 2000.dp.toPx(), bottom - top)
                                    )
                                }
                            }
                            Box(Modifier.padding(start = 8.dp, top = 4.dp, bottom = 4.dp, end = 16.dp)) {
                                innerTextField()
                            }
                        }
                    }
                )
            }
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(vScrollState),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            style = LocalScrollbarStyle.current.copy(
                unhoverColor = CodeTogetherTheme.colors.primary.copy(alpha = 0.1f),
                hoverColor = CodeTogetherTheme.colors.primary
            )
        )

        HorizontalScrollbar(
            adapter = rememberScrollbarAdapter(hScrollState),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(end = 14.dp, start = 40.dp),
            style = LocalScrollbarStyle.current.copy(
                unhoverColor = CodeTogetherTheme.colors.primary.copy(alpha = 0.1f),
                hoverColor = CodeTogetherTheme.colors.primary
            )
        )
    }
}