// Copyright (c) 2001-2020 Aspose Pty Ltd. All Rights Reserved.
//
// This file is part of Aspose.Words. The source code in this file
// is only intended as a supplement to the documentation, and is provided
// "as is", without warranty of any kind, either expressed or implied.
//////////////////////////////////////////////////////////////////////////

package ApiExamples;

// ********* THIS FILE IS AUTO PORTED *********

import org.testng.annotations.Test;
import com.aspose.words.Document;
import com.aspose.words.RtfSaveOptions;
import com.aspose.words.SaveFormat;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.Shape;
import com.aspose.ms.NUnit.Framework.msAssert;
import org.testng.Assert;
import com.aspose.words.ImageType;


@Test
public class ExRtfSaveOptions extends ApiExampleBase
{
    @Test
    public void exportImages() throws Exception
    {
        //ExStart
        //ExFor:RtfSaveOptions
        //ExFor:RtfSaveOptions.ExportCompactSize
        //ExFor:RtfSaveOptions.ExportImagesForOldReaders
        //ExFor:RtfSaveOptions.SaveFormat
        //ExSummary:Shows how to save a document to .rtf with custom options.
        // Open a document with images
        Document doc = new Document(getMyDir() + "Rendering.docx");

        // Configure a RtfSaveOptions instance to make our output document more suitable for older devices
        RtfSaveOptions options = new RtfSaveOptions();
        {
            options.setSaveFormat(SaveFormat.RTF);
            options.setExportCompactSize(true);
            options.setExportImagesForOldReaders(true);
        }

        doc.save(getArtifactsDir() + "RtfSaveOptions.ExportImages.rtf", options);
        //ExEnd
    }

    @Test
    public void saveImagesAsWmf() throws Exception
    {
        //ExStart
        //ExFor:RtfSaveOptions.SaveImagesAsWmf
        //ExSummary:Shows how to save all images as Wmf when saving to the Rtf document.
        // Open a document that contains images in the jpeg format
        Document doc = new Document(getMyDir() + "Images.docx");

        NodeCollection shapes = doc.getChildNodes(NodeType.SHAPE, true);
        Shape shapeWithJpg = (Shape)shapes.get(0);
        msAssert.areEqual(ImageType.JPEG, shapeWithJpg.getImageData().getImageType());

        RtfSaveOptions rtfSaveOptions = new RtfSaveOptions();
        rtfSaveOptions.setSaveImagesAsWmf(true);
        doc.save(getArtifactsDir() + "RtfSaveOptions.SaveImagesAsWmf.rtf", rtfSaveOptions);
        
        doc = new Document(getArtifactsDir() + "RtfSaveOptions.SaveImagesAsWmf.rtf");

        shapes = doc.getChildNodes(NodeType.SHAPE, true);
        Shape shapeWithWmf = (Shape)shapes.get(0);
        msAssert.areEqual(ImageType.WMF, shapeWithWmf.getImageData().getImageType());
        //ExEnd
    }
}
