package io.github.avew.util;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.*;
import com.sun.istack.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
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
            @Nullable String passPdf,
            double signLx,
            double signLy,
            int pageStamp) throws IOException, com.itextpdf.text.DocumentException {

        if (in == null)
            throw new RuntimeException("File in is null");
        if (out == null)
            throw new RuntimeException("File out is null");
        if (pageStamp == 0)
            throw new RuntimeException("Pdf page cannot be zero");

        checkPdf(in, passPdf);

        byte[] bytes = IOUtils.toByteArray(attach);
        attach(bytes, (float) signLx, (float) signLy, passPdf, in, out, pageStamp);

        return out;
    }

    public static File attachBase64Image(
            InputStream in,
            String base64Image,
            File out,
            @Nullable String passPdf,
            double signLx,
            double signLy,
            int pageStamp) throws IOException, com.itextpdf.text.DocumentException {

        if (in == null)
            throw new RuntimeException("File in is null");
        if (out == null)
            throw new RuntimeException("File out is null");
        if (pageStamp == 0)
            throw new RuntimeException("Pdf page cannot be zero");

        checkPdf(in, passPdf);
        byte[] bytes = Base64.decodeBase64(base64Image.getBytes());
        attach(bytes, (float) signLx, (float) signLy, passPdf, in, out, pageStamp);
        return out;
    }

    private static void attach(byte[] bytes, float signLx, float signLy, String passPdf, InputStream in, File out, int pageStamp) throws IOException, DocumentException {
        Image image = Image.getInstance(bytes);
        image.setAbsolutePosition(signLx, signLy);
        PdfReader reader;
        if (isNotEmpty(passPdf)) {
            reader = new PdfReader(in, passPdf.getBytes(StandardCharsets.UTF_8));
        } else {
            reader = new PdfReader(in);
        }
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(out));
        if (isNotEmpty(passPdf)) {
            stamper.setEncryption(passPdf.getBytes(), passPdf.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
        }
        PdfImage pdfImage = new PdfImage(image, "", null);
        pdfImage.put(new PdfName("Signature"), new PdfName("Signature"));

        stamper.getWriter().addToBody(pdfImage);
        stamper.getOverContent(pageStamp).addImage(image);
        stamper.close();

        reader.close();
    }
}
