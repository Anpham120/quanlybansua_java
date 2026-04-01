/**
 * Export BAO_CAO_DO_AN.md → .docx
 * Times New Roman, size 13, bordered tables, proper headings
 * Bắt đầu từ phần "LỜI NÓI ĐẦU" đến hết
 */

const fs = require("fs");
const path = require("path");
const {
  Document,
  Packer,
  Paragraph,
  TextRun,
  Table,
  TableRow,
  TableCell,
  WidthType,
  AlignmentType,
  HeadingLevel,
  BorderStyle,
  ShadingType,
  PageBreak,
  convertInchesToTwip,
  TableOfContents,
  LevelFormat,
  Footer,
  PageNumber,
  NumberFormat,
} = require("docx");

// ============ CONFIG ============
const FONT = "Times New Roman";
const SIZE_NORMAL = 26; // 13pt * 2 = 26 half-points
const SIZE_H1 = 32; // 16pt
const SIZE_H2 = 28; // 14pt
const SIZE_H3 = 26; // 13pt
const SIZE_CODE = 22; // 11pt
const LINE_SPACING = 360; // 1.5 lines

const BORDER_STYLE = {
  style: BorderStyle.SINGLE,
  size: 1,
  color: "000000",
};

const TABLE_BORDERS = {
  top: BORDER_STYLE,
  bottom: BORDER_STYLE,
  left: BORDER_STYLE,
  right: BORDER_STYLE,
  insideHorizontal: BORDER_STYLE,
  insideVertical: BORDER_STYLE,
};

// ============ READ MD ============
const mdPath = path.join(__dirname, "BAO_CAO_DO_AN.md");
const mdContent = fs.readFileSync(mdPath, "utf8");

// Start from "LỜI NÓI ĐẦU"
const startIdx = mdContent.indexOf("# LỜI NÓI ĐẦU");
if (startIdx === -1) {
  console.error("Không tìm thấy phần LỜI NÓI ĐẦU!");
  process.exit(1);
}
const content = mdContent.substring(startIdx);
const lines = content.split("\n");

// ============ INLINE PARSING ============
function parseInline(text) {
  const runs = [];
  // Pattern: **bold**, *italic*, `code`, normal text
  const regex = /(\*\*(.+?)\*\*)|(\*(.+?)\*)|(`([^`]+?)`)|([^*`]+)/g;
  let match;
  while ((match = regex.exec(text)) !== null) {
    if (match[2]) {
      // **bold**
      runs.push(
        new TextRun({ text: match[2], bold: true, font: FONT, size: SIZE_NORMAL })
      );
    } else if (match[4]) {
      // *italic*
      runs.push(
        new TextRun({ text: match[4], italics: true, font: FONT, size: SIZE_NORMAL })
      );
    } else if (match[6]) {
      // `code`
      runs.push(
        new TextRun({
          text: match[6],
          font: "Courier New",
          size: SIZE_CODE,
          shading: { type: ShadingType.CLEAR, fill: "F0F0F0" },
        })
      );
    } else if (match[7]) {
      // Normal
      runs.push(
        new TextRun({ text: match[7], font: FONT, size: SIZE_NORMAL })
      );
    }
  }
  if (runs.length === 0) {
    runs.push(new TextRun({ text: text, font: FONT, size: SIZE_NORMAL }));
  }
  return runs;
}

// ============ TABLE PARSING ============
function parseTable(tableLines) {
  // tableLines: array of "|...|" lines
  const rows = [];
  for (let i = 0; i < tableLines.length; i++) {
    const line = tableLines[i].trim();
    // Skip separator line (|---|---|)
    if (/^\|[\s\-:]+\|$/.test(line.replace(/\|/g, (m, offset, str) => {
      return m;
    }))) {
      // Check if it's separator
      const cells = line.split("|").filter((c) => c.trim() !== "");
      const isSep = cells.every((c) => /^[\s\-:]+$/.test(c));
      if (isSep) continue;
    }

    const cells = line
      .split("|")
      .filter((_, idx, arr) => idx > 0 && idx < arr.length - 1)
      .map((c) => c.trim());

    const isHeader = i === 0;

    const tableCells = cells.map(
      (cellText) =>
        new TableCell({
          children: [
            new Paragraph({
              children: parseInline(cellText),
              spacing: { before: 40, after: 40 },
              alignment: AlignmentType.LEFT,
            }),
          ],
          shading: isHeader
            ? { type: ShadingType.CLEAR, fill: "D9E2F3" }
            : undefined,
          borders: TABLE_BORDERS,
        })
    );

    rows.push(
      new TableRow({
        children: tableCells,
        tableHeader: isHeader,
      })
    );
  }

  if (rows.length === 0) return null;

  return new Table({
    rows: rows,
    width: { size: 100, type: WidthType.PERCENTAGE },
    borders: TABLE_BORDERS,
  });
}

// ============ MAIN CONVERSION ============
const docChildren = [];

// Add TOC placeholder
docChildren.push(
  new Paragraph({
    children: [
      new TextRun({ text: "MỤC LỤC", bold: true, font: FONT, size: SIZE_H1 }),
    ],
    heading: HeadingLevel.HEADING_1,
    alignment: AlignmentType.CENTER,
    spacing: { after: 200 },
  })
);
docChildren.push(
  new TableOfContents("Mục lục", {
    hyperlink: true,
    headingStyleRange: "1-3",
  })
);
docChildren.push(
  new Paragraph({
    children: [new PageBreak()],
  })
);

let i = 0;
let tableBuffer = [];
let codeBuffer = [];
let inCodeBlock = false;
let codeLang = "";

function flushTable() {
  if (tableBuffer.length > 0) {
    const tbl = parseTable(tableBuffer);
    if (tbl) docChildren.push(tbl);
    docChildren.push(
      new Paragraph({ spacing: { after: 120 }, children: [] })
    );
    tableBuffer = [];
  }
}

function flushCode() {
  if (codeBuffer.length > 0) {
    // Add code block as paragraphs with monospace font and grey background
    for (const codeLine of codeBuffer) {
      docChildren.push(
        new Paragraph({
          children: [
            new TextRun({
              text: codeLine || " ",
              font: "Courier New",
              size: SIZE_CODE,
            }),
          ],
          spacing: { before: 0, after: 0, line: 240 },
          indent: { left: convertInchesToTwip(0.4) },
          shading: { type: ShadingType.CLEAR, fill: "F5F5F5" },
        })
      );
    }
    docChildren.push(
      new Paragraph({ spacing: { after: 120 }, children: [] })
    );
    codeBuffer = [];
  }
}

while (i < lines.length) {
  const line = lines[i];
  const trimmed = line.trim();

  // Skip MỤC LỤC section in the original (we auto-generate one)
  if (trimmed === "# MỤC LỤC") {
    i++;
    // Skip until next --- or # heading
    while (i < lines.length) {
      const nextTrimmed = lines[i].trim();
      if (nextTrimmed === "---" || (nextTrimmed.startsWith("# ") && !nextTrimmed.startsWith("# MỤC"))) break;
      i++;
    }
    continue;
  }

  // Code block toggle
  if (trimmed.startsWith("```")) {
    if (!inCodeBlock) {
      flushTable();
      inCodeBlock = true;
      codeLang = trimmed.substring(3).trim();
      i++;
      continue;
    } else {
      inCodeBlock = false;
      flushCode();
      i++;
      continue;
    }
  }

  if (inCodeBlock) {
    codeBuffer.push(line);
    i++;
    continue;
  }

  // Horizontal rule / page separator
  if (trimmed === "---") {
    flushTable();
    // Don't add page break for every ---, just a visual separator
    docChildren.push(
      new Paragraph({ spacing: { before: 200, after: 200 }, children: [] })
    );
    i++;
    continue;
  }

  // Empty line
  if (trimmed === "" || trimmed === "&nbsp;") {
    flushTable();
    i++;
    continue;
  }

  // Table line
  if (trimmed.startsWith("|") && trimmed.endsWith("|")) {
    tableBuffer.push(trimmed);
    i++;
    continue;
  } else {
    flushTable();
  }

  // Headings
  if (trimmed.startsWith("# ") && !trimmed.startsWith("##")) {
    const text = trimmed.substring(2).trim();
    // Page break before H1 (except the first one which is LỜI NÓI ĐẦU)
    if (docChildren.length > 3) {
      docChildren.push(new Paragraph({ children: [new PageBreak()] }));
    }
    docChildren.push(
      new Paragraph({
        children: [
          new TextRun({
            text: text,
            bold: true,
            font: FONT,
            size: SIZE_H1,
            allCaps: true,
          }),
        ],
        heading: HeadingLevel.HEADING_1,
        alignment: AlignmentType.CENTER,
        spacing: { before: 360, after: 240 },
      })
    );
    i++;
    continue;
  }

  if (trimmed.startsWith("## ") && !trimmed.startsWith("###")) {
    const text = trimmed.substring(3).trim();
    docChildren.push(
      new Paragraph({
        children: [
          new TextRun({
            text: text,
            bold: true,
            font: FONT,
            size: SIZE_H2,
          }),
        ],
        heading: HeadingLevel.HEADING_2,
        spacing: { before: 280, after: 160 },
      })
    );
    i++;
    continue;
  }

  if (trimmed.startsWith("### ")) {
    const text = trimmed.substring(4).trim();
    docChildren.push(
      new Paragraph({
        children: [
          new TextRun({
            text: text,
            bold: true,
            italics: true,
            font: FONT,
            size: SIZE_H3,
          }),
        ],
        heading: HeadingLevel.HEADING_3,
        spacing: { before: 200, after: 120 },
      })
    );
    i++;
    continue;
  }

  // Blockquote (image placeholder)
  if (trimmed.startsWith("> ")) {
    const quoteText = trimmed.substring(2).replace(/\*\*/g, "");
    docChildren.push(
      new Paragraph({
        children: [
          new TextRun({
            text: quoteText,
            italics: true,
            font: FONT,
            size: SIZE_NORMAL,
            color: "666666",
          }),
        ],
        indent: { left: convertInchesToTwip(0.5) },
        spacing: { before: 80, after: 80 },
        alignment: AlignmentType.CENTER,
      })
    );
    i++;
    continue;
  }

  // Ordered list (1. 2. 3.)
  if (/^\d+\.\s/.test(trimmed)) {
    const listText = trimmed.replace(/^\d+\.\s+/, "");
    docChildren.push(
      new Paragraph({
        children: parseInline(listText),
        spacing: { before: 60, after: 60, line: LINE_SPACING },
        indent: { left: convertInchesToTwip(0.4), hanging: convertInchesToTwip(0.3) },
        numbering: undefined, // Use manual numbering from text
      })
    );
    // Re-add the number prefix
    const numMatch = trimmed.match(/^(\d+)\./);
    if (numMatch) {
      const lastChild = docChildren[docChildren.length - 1];
      lastChild.properties = undefined; // Reset
      docChildren[docChildren.length - 1] = new Paragraph({
        children: [
          new TextRun({
            text: numMatch[1] + ". ",
            font: FONT,
            size: SIZE_NORMAL,
          }),
          ...parseInline(listText),
        ],
        spacing: { before: 60, after: 60, line: LINE_SPACING },
        indent: { left: convertInchesToTwip(0.4), hanging: convertInchesToTwip(0.3) },
      });
    }
    i++;
    continue;
  }

  // Unordered list (- item)
  if (trimmed.startsWith("- ")) {
    const listText = trimmed.substring(2);
    const indentLevel = line.search(/\S/) >= 3 ? 2 : 1;
    docChildren.push(
      new Paragraph({
        children: [
          new TextRun({ text: "• ", font: FONT, size: SIZE_NORMAL }),
          ...parseInline(listText),
        ],
        spacing: { before: 40, after: 40, line: LINE_SPACING },
        indent: {
          left: convertInchesToTwip(0.3 * indentLevel),
          hanging: convertInchesToTwip(0.2),
        },
      })
    );
    i++;
    continue;
  }

  // Sub-item with "   - " indent
  if (/^\s{2,}-\s/.test(line)) {
    const listText = line.replace(/^\s+-\s+/, "");
    docChildren.push(
      new Paragraph({
        children: [
          new TextRun({ text: "  – ", font: FONT, size: SIZE_NORMAL }),
          ...parseInline(listText),
        ],
        spacing: { before: 20, after: 20, line: LINE_SPACING },
        indent: {
          left: convertInchesToTwip(0.7),
          hanging: convertInchesToTwip(0.25),
        },
      })
    );
    i++;
    continue;
  }

  // Continuation indent lines (   →, or lines starting with spaces under a list)
  if (/^\s{3,}/.test(line) && trimmed.length > 0) {
    docChildren.push(
      new Paragraph({
        children: parseInline(trimmed),
        spacing: { before: 20, after: 20, line: LINE_SPACING },
        indent: { left: convertInchesToTwip(0.7) },
      })
    );
    i++;
    continue;
  }

  // Caption-style text (like *(Bảng 1...)*)
  if (/^\*\(.+\)\*$/.test(trimmed)) {
    const captionText = trimmed.replace(/^\*\(/, "(").replace(/\)\*$/, ")");
    docChildren.push(
      new Paragraph({
        children: [
          new TextRun({
            text: captionText,
            italics: true,
            font: FONT,
            size: SIZE_NORMAL,
            color: "333333",
          }),
        ],
        alignment: AlignmentType.CENTER,
        spacing: { before: 80, after: 80 },
      })
    );
    i++;
    continue;
  }

  // Normal paragraph
  docChildren.push(
    new Paragraph({
      children: parseInline(trimmed),
      spacing: { before: 60, after: 60, line: LINE_SPACING },
      indent: { firstLine: convertInchesToTwip(0.5) },
    })
  );
  i++;
}

// Flush remaining
flushTable();
flushCode();

// ============ BUILD DOCUMENT ============
const doc = new Document({
  features: {
    updateFields: true, // Auto-update TOC when opening in Word
  },
  styles: {
    default: {
      document: {
        run: {
          font: FONT,
          size: SIZE_NORMAL,
        },
        paragraph: {
          spacing: { line: LINE_SPACING },
        },
      },
      heading1: {
        run: {
          font: FONT,
          size: SIZE_H1,
          bold: true,
        },
        paragraph: {
          spacing: { before: 360, after: 240 },
        },
      },
      heading2: {
        run: {
          font: FONT,
          size: SIZE_H2,
          bold: true,
        },
        paragraph: {
          spacing: { before: 280, after: 160 },
        },
      },
      heading3: {
        run: {
          font: FONT,
          size: SIZE_H3,
          bold: true,
          italics: true,
        },
        paragraph: {
          spacing: { before: 200, after: 120 },
        },
      },
    },
  },
  numbering: {
    config: [],
  },
  sections: [
    {
      properties: {
        page: {
          margin: {
            top: convertInchesToTwip(1),
            bottom: convertInchesToTwip(1),
            left: convertInchesToTwip(1.2),
            right: convertInchesToTwip(1),
          },
          pageNumbers: {
            start: 1,
            formatType: NumberFormat.DECIMAL,
          },
        },
      },
      footers: {
        default: new Footer({
          children: [
            new Paragraph({
              alignment: AlignmentType.CENTER,
              children: [
                new TextRun({
                  children: [PageNumber.CURRENT],
                  font: FONT,
                  size: 20,
                }),
              ],
            }),
          ],
        }),
      },
      children: docChildren,
    },
  ],
});

// ============ SAVE ============
const outputPath = path.join(__dirname, "BAO_CAO_DO_AN.docx");

Packer.toBuffer(doc).then((buffer) => {
  fs.writeFileSync(outputPath, buffer);
  console.log(`✅ Xuất thành công: ${outputPath}`);
  console.log(`   Kích thước: ${(buffer.length / 1024).toFixed(1)} KB`);
  console.log(`   Nội dung: Từ "LỜI NÓI ĐẦU" đến hết Phụ lục`);
  console.log(`   Font: ${FONT}, Size: 13pt`);
  console.log(`   Bảng: Viền đầy đủ, header tô màu`);
  console.log(`\n💡 Mở file trong Word → nhấn Ctrl+A → F9 để cập nhật Mục lục`);
});
