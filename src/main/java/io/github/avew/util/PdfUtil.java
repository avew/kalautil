package io.github.avew.util;

import com.itextpdf.text.Image;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.*;
import com.sun.istack.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
public class PdfUtil {

    public static int checkPdf(InputStream is, String passDoc) throws IOException {
        /* check if doc have a pass */
        if (passDoc != null) {
            try {
                PdfReader pdfReader = new PdfReader(is, passDoc.getBytes(StandardCharsets.UTF_8));
                return pdfReader.getNumberOfPages();
            } catch (BadPasswordException ex) {
                throw new RuntimeException("Password pdf cannot match");
            }
        } else {
            /* check if pdf have a pass */
            try {
                PdfReader pdfReader = new PdfReader(is);
                return pdfReader.getNumberOfPages();
            } catch (BadPasswordException e) {
                throw new RuntimeException("Pdf have a password");
            }
        }
    }

    public static File attachImage(
            InputStream in,
            InputStream attach,
            File out,
            @Nullable String passDoc,
            double signLx,
            double signLy,
            int pageStamp) throws IOException, com.itextpdf.text.DocumentException {

        if (in == null)
            throw new RuntimeException("File in is null");
        if (out == null)
            throw new RuntimeException("File out is null");
        if (pageStamp == 0)
            throw new RuntimeException("Pdf page cannot be zero");

        byte[] bytes = IOUtils.toByteArray(attach);
        Image image = Image.getInstance(bytes);
        image.setAbsolutePosition((float) signLx, (float) signLy);

        checkPdf(in, passDoc);
        PdfReader reader;
        if (isNotEmpty(passDoc)) {
            reader = new PdfReader(in, passDoc.getBytes(StandardCharsets.UTF_8));
        } else {
            reader = new PdfReader(in);
        }
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(out));
        if (isNotEmpty(passDoc)) {
            stamper.setEncryption(passDoc.getBytes(), passDoc.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
        }
        PdfImage pdfImage = new PdfImage(image, "", null);
        pdfImage.put(new PdfName("Signature"), new PdfName("Signature"));

        stamper.getWriter().addToBody(pdfImage);
        stamper.getOverContent(pageStamp).addImage(image);
        stamper.close();

        reader.close();

        return out;
    }
}
