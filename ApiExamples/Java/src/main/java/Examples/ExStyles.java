package Examples;

//////////////////////////////////////////////////////////////////////////
// Copyright (c) 2001-2020 Aspose Pty Ltd. All Rights Reserved.
//
// This file is part of Aspose.Words. The source code in this file
// is only intended as a supplement to the documentation, and is provided
// "as is", without warranty of any kind, either expressed or implied.
//////////////////////////////////////////////////////////////////////////

import com.aspose.words.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.Iterator;

public class ExStyles extends ApiExampleBase {
    @Test
    public void getStyles() throws Exception {
        //ExStart
        //ExFor:DocumentBase.Styles
        //ExFor:Style.Document
        //ExFor:Style.Name
        //ExFor:Style.IsHeading
        //ExFor:Style.IsQuickStyle
        //ExFor:Style.NextParagraphStyleName
        //ExFor:Style.Styles
        //ExFor:Style.Type
        //ExFor:StyleCollection.Document
        //ExFor:StyleCollection.GetEnumerator
        //ExSummary:Shows how to get access to the collection of styles defined in the document.
        Document doc = new Document();
       
        Iterator<Style> stylesEnum = doc.getStyles().iterator();
        while (stylesEnum.hasNext()) {
            Style curStyle = stylesEnum.next();
            System.out.println(MessageFormat.format("Style name:\t\"{0}\", of type \"{1}\"", curStyle.getName(), curStyle.getType()));
            System.out.println(MessageFormat.format("\tSubsequent style:\t{0}", curStyle.getNextParagraphStyleName()));
            System.out.println(MessageFormat.format("\tIs heading:\t\t\t{0}", curStyle.isHeading()));
            System.out.println(MessageFormat.format("\tIs QuickStyle:\t\t{0}", curStyle.isQuickStyle()));

            Assert.assertEquals(curStyle.getDocument(), doc);
        }
        //ExEnd
    }

    @Test
    public void styleCollection() throws Exception {
        //ExStart
        //ExFor:StyleCollection.Add(Style)
        //ExFor:StyleCollection.Count
        //ExFor:StyleCollection.DefaultFont
        //ExFor:StyleCollection.DefaultParagraphFormat
        //ExFor:StyleCollection.Item(StyleIdentifier)
        //ExFor:StyleCollection.Item(Int32)
        //ExSummary:Shows how to add a Style to a StyleCollection.
        Document doc = new Document();

        // New documents come with a collection of default styles that can be applied to paragraphs
        StyleCollection styles = doc.getStyles();
        Assert.assertEquals(styles.getCount(), 4);

        // We can set default parameters for new styles that will be added to the collection from now on
        styles.getDefaultFont().setName("Courier New");
        styles.getDefaultParagraphFormat().setFirstLineIndent(15.0);

        styles.add(StyleType.PARAGRAPH, "MyStyle");

        // Styles within the collection can be referenced either by index or name
        Assert.assertEquals(styles.get(4).getFont().getName(), "Courier New");
        Assert.assertEquals(styles.get("MyStyle").getParagraphFormat().getFirstLineIndent(), 15.0);
        //ExEnd
    }

    @Test
    public void setAllStyles() throws Exception {
        //ExStart
        //ExFor:Style.Font
        //ExFor:Style
        //ExSummary:Shows how to change the font formatting of all styles in a document.
        Document doc = new Document();
        for (Style style : doc.getStyles()) {
            if (style.getFont() != null) {
                style.getFont().clearFormatting();
                style.getFont().setSize(20);
                style.getFont().setName("Arial");
            }
        }
        //ExEnd
    }

    @Test
    public void changeStyleOfTocLevel() throws Exception {
        Document doc = new Document();

        // Retrieve the style used for the first level of the TOC and change the formatting of the style
        doc.getStyles().getByStyleIdentifier(StyleIdentifier.TOC_1).getFont().setBold(true);
    }

    @Test
    public void changeTocsTabStops() throws Exception {
        //ExStart
        //ExFor:TabStop
        //ExFor:ParagraphFormat.TabStops
        //ExFor:Style.StyleIdentifier
        //ExFor:TabStopCollection.RemoveByPosition
        //ExFor:TabStop.Alignment
        //ExFor:TabStop.Position
        //ExFor:TabStop.Leader
        //ExSummary:Shows how to modify the position of the right tab stop in TOC related paragraphs.
        Document doc = new Document(getMyDir() + "Table of contents.docx");

        // Iterate through all paragraphs in the document
        for (Paragraph para : (Iterable<Paragraph>) doc.getChildNodes(NodeType.PARAGRAPH, true)) {
            // Check if this paragraph is formatted using the TOC result based styles. This is any style between TOC and TOC9
            if (para.getParagraphFormat().getStyle().getStyleIdentifier() >= StyleIdentifier.TOC_1
                    && para.getParagraphFormat().getStyle().getStyleIdentifier() <= StyleIdentifier.TOC_9) {
                // Get the first tab used in this paragraph, this should be the tab used to align the page numbers
                TabStop tab = para.getParagraphFormat().getTabStops().get(0);
                // Remove the old tab from the collection
                para.getParagraphFormat().getTabStops().removeByPosition(tab.getPosition());
                // Insert a new tab using the same properties but at a modified position
                // We could also change the separators used (dots) by passing a different Leader type
                para.getParagraphFormat().getTabStops().add(tab.getPosition() - 50, tab.getAlignment(), tab.getLeader());
            }
        }

        doc.save(getArtifactsDir() + "Styles.ChangeTocsTabStops.docx");
        //ExEnd
    }

    @Test
    public void copyStyleSameDocument() throws Exception {
        Document doc = new Document(getMyDir() + "Document.docx");

        //ExStart
        //ExFor:StyleCollection.AddCopy
        //ExFor:Style.Name
        //ExSummary:Demonstrates how to copy a style within the same document.
        // The AddCopy method creates a copy of the specified style and automatically generates a new name for the style, such as "Heading 1_0"
        Style newStyle = doc.getStyles().addCopy(doc.getStyles().get("Heading 1"));

        // You can change the new style name if required as the Style.Name property is read-write
        newStyle.setName("My Heading 1");
        //ExEnd

        Assert.assertNotNull(newStyle);
        Assert.assertEquals(newStyle.getName(), "My Heading 1");
        Assert.assertEquals(newStyle.getType(), doc.getStyles().get("Heading 1").getType());
    }

    @Test
    public void copyStyleDifferentDocument() throws Exception {
        Document dstDoc = new Document();
        Document srcDoc = new Document();

        //ExStart
        //ExFor:StyleCollection.AddCopy
        //ExSummary:Demonstrates how to copy style from one document into a different document.
        // This is the style in the source document to copy to the destination document
        Style srcStyle = srcDoc.getStyles().getByStyleIdentifier(StyleIdentifier.HEADING_1);

        // Change the font of the heading style to red
        srcStyle.getFont().setColor(Color.RED);

        // The AddCopy method can be used to copy a style from a different document
        Style newStyle = dstDoc.getStyles().addCopy(srcStyle);
        //ExEnd

        Assert.assertNotNull(newStyle);
        Assert.assertEquals(newStyle.getName(), "Heading 1");
        Assert.assertEquals(newStyle.getFont().getColor().getRGB(), Color.RED.getRGB());
    }

    @Test
    public void overwriteStyleDifferentDocument() throws Exception {
        Document dstDoc = new Document();
        Document srcDoc = new Document();

        //ExStart
        //ExFor:StyleCollection.AddCopy
        //ExSummary:Demonstrates how to copy a style from one document to another and override an existing style in the destination document.
        // This is the style in the source document to copy to the destination document
        Style srcStyle = srcDoc.getStyles().getByStyleIdentifier(StyleIdentifier.HEADING_1);

        // Change the font of the heading style to red
        srcStyle.getFont().setColor(Color.RED);

        // The AddCopy method can be used to copy a style to a different document
        Style newStyle = dstDoc.getStyles().addCopy(srcStyle);

        // The name of the new style can be changed to the name of any existing style. Doing this will override the existing style
        newStyle.setName("Heading 1");
        //ExEnd

        Assert.assertNotNull(newStyle);
        Assert.assertEquals(newStyle.getName(), "Heading 1");
        Assert.assertNull(dstDoc.getStyles().get("Heading 1_0"));
        Assert.assertEquals(newStyle.getFont().getColor().getRGB(), Color.RED.getRGB());
    }

    @Test
    public void defaultStyles() throws Exception {
        Document doc = new Document();

        // Add document-wide defaults parameters
        doc.getStyles().getDefaultFont().setName("PMingLiU");
        doc.getStyles().getDefaultFont().setBold(true);

        doc.getStyles().getDefaultParagraphFormat().setSpaceAfter(20.0);
        doc.getStyles().getDefaultParagraphFormat().setAlignment(ParagraphAlignment.RIGHT);

        ByteArrayOutputStream dstStream = new ByteArrayOutputStream();
        doc.save(dstStream, SaveFormat.RTF);

        Assert.assertTrue(doc.getStyles().getDefaultFont().getBold());
        Assert.assertEquals(doc.getStyles().getDefaultFont().getName(), "PMingLiU");
        Assert.assertEquals(doc.getStyles().getDefaultParagraphFormat().getSpaceAfter(), 20.0);
        Assert.assertEquals(doc.getStyles().getDefaultParagraphFormat().getAlignment(), ParagraphAlignment.RIGHT);
    }

    @Test
    public void removeEx() throws Exception {
        //ExStart
        //ExFor:Style.Remove
        //ExSummary:Shows how to pick a style that is defined in the document and remove it.
        Document doc = new Document();
        doc.getStyles().get("Normal").remove();
        //ExEnd
    }

    @Test
    public void styleAliases() throws Exception {
        //ExStart
        //ExFor:Style.Aliases
        //ExFor:Style.BaseStyleName
        //ExFor:Style.Equals(Aspose.Words.Style)
        //ExFor:Style.LinkedStyleName
        //ExSummary:Shows how to use style aliases.
        // Open a document that had a style inserted with commas in its name which separate the style name and aliases
        Document doc = new Document(getMyDir() + "Style with alias.docx");

        // The aliases, separate from the name can be found here
        Style style = doc.getStyles().get("MyStyle");
        Assert.assertEquals(new String[]{"MyStyle Alias 1", "MyStyle Alias 2"}, style.getAliases());
        Assert.assertEquals("Title", style.getBaseStyleName());
        Assert.assertEquals("MyStyle Char", style.getLinkedStyleName());

        // A style can be referenced by alias as well as name
        Assert.assertTrue(style.equals(doc.getStyles().get("MyStyle Alias 1")));
        //ExEnd
    }
}
