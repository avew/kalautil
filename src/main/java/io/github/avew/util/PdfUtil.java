package io.github.avew.util;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.*;
import com.sun.istack.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
public class PdfUtil {

    public static int checkPdf(InputStream is, String passDoc) throws IOException {
        /* check if doc have a pass */
        if (passDoc != null) {
            try {
                PdfReader pdfReader = new PdfReader(is, passDoc.getBytes(StandardCharsets.UTF_8));
                boolean signature = verifySignature(pdfReader);
                if (signature) {
                    throw new RuntimeException("Document already Certified, No changes are allowed");
                }
                return pdfReader.getNumberOfPages();
            } catch (BadPasswordException ex) {
                throw new RuntimeException("Password pdf cannot match");
            }
        } else {
            /* check if pdf have a pass */
            try {
                PdfReader pdfReader = new PdfReader(is);
                boolean signature = verifySignature(pdfReader);
                if (signature) {
                    throw new RuntimeException("Document already Certified, No changes are allowed");
                }
                return pdfReader.getNumberOfPages();
            } catch (BadPasswordException e) {
                throw new RuntimeException("Pdf have a password");
            }
        }
    }

    public static boolean verifySignature(PdfReader pdfReader) {
        AcroFields acroFields = pdfReader.getAcroFields();
        List<String> signatureNames = acroFields.getSignatureNames();

        return !signatureNames.isEmpty();
    }

    public static void attachImage(
            File src,
            InputStream attach,
            @Nullable String passPdf,
            double signLx,
            double signLy,
            int pageStamp) throws IOException, DocumentException {

        if (src == null)
            throw new RuntimeException("File src is null");
        if (pageStamp == 0)
            throw new RuntimeException("Pdf page cannot be zero");
        byte[] bytes = IOUtils.toByteArray(attach);
        attach(bytes, (float) signLx, (float) signLy, passPdf, src, pageStamp);
    }

    public static void attachImageWithDest(
            InputStream src,
            @Nullable File dest,
            InputStream attach,
            String passPdf,
            double signLx,
            double signLy,
            int pageStamp) throws IOException, DocumentException {

        if (src == null)
            throw new RuntimeException("File src is null");
        if (dest == null)
            throw new RuntimeException("File dest is null");
        if (pageStamp == 0)
            throw new RuntimeException("Pdf page cannot be zero");
        byte[] bytes = IOUtils.toByteArray(attach);
        attachWithDest(bytes, (float) signLx, (float) signLy, passPdf, src, dest, pageStamp);
    }

    public static void attachImageWithDest(
            File src,
            @Nullable File dest,
            InputStream attach,
            String passPdf,
            double signLx,
            double signLy,
            int pageStamp) throws IOException, DocumentException {

        if (src == null)
            throw new RuntimeException("File src is null");
        if (dest == null)
            throw new RuntimeException("File dest is null");
        if (pageStamp == 0)
            throw new RuntimeException("Pdf page cannot be zero");
        byte[] bytes = IOUtils.toByteArray(attach);
        attachWithDest(bytes, (float) signLx, (float) signLy, passPdf, FileUtils.openInputStream(src), dest, pageStamp);
    }

    public static void attachBase64Image(
            File src,
            String base64Image,
            @Nullable String passPdf,
            double signLx,
            double signLy,
            int pageStamp) throws IOException, DocumentException {


        if (src == null)
            throw new RuntimeException("File src is null");

        if (pageStamp == 0)
            throw new RuntimeException("Pdf page cannot be zero");

        byte[] bytes = Base64.decodeBase64(base64Image.getBytes());
        attach(bytes, (float) signLx, (float) signLy, passPdf, src, pageStamp);
    }

    public static void attachBase64ImageWithDest(
            InputStream src,
            File dest,
            String base64Image,
            @Nullable String passPdf,
            double signLx,
            double signLy,
            int pageStamp) throws IOException, DocumentException {

        if (src == null)
            throw new RuntimeException("File src is null");
        if (dest == null)
            throw new RuntimeException("File dest is null");
        if (pageStamp == 0)
            throw new RuntimeException("Pdf page cannot be zero");

        byte[] bytes = Base64.decodeBase64(base64Image.getBytes());
        attachWithDest(bytes, (float) signLx, (float) signLy, passPdf, src, dest, pageStamp);

    }

    public static void attachBase64ImageWithDest(
            File src,
            File dest,
            String base64Image,
            @Nullable String passPdf,
            double signLx,
            double signLy,
            int pageStamp) throws IOException, DocumentException {

        if (src == null)
            throw new RuntimeException("File src is null");
        if (dest == null)
            throw new RuntimeException("File dest is null");
        if (pageStamp == 0)
            throw new RuntimeException("Pdf page cannot be zero");


        byte[] bytes = Base64.decodeBase64(base64Image.getBytes());
        attachWithDest(bytes, (float) signLx, (float) signLy, passPdf, FileUtils.openInputStream(src), dest, pageStamp);

    }

    private static void attach(byte[] bytes,
                               float signLx,
                               float signLy,
                               String passPdf,
                               File src,
                               int pageStamp) throws IOException, DocumentException {
        FileInputStream in = FileUtils.openInputStream(src);
        Image image = Image.getInstance(bytes);
        image.setAbsolutePosition(signLx, signLy);
        PdfReader reader;
        if (isNotEmpty(passPdf)) {
            reader = new PdfReader(in, passPdf.getBytes(StandardCharsets.UTF_8));
        } else reader = new PdfReader(in);
        pdfStamper(passPdf, src, pageStamp, image, reader);
    }


    private static void attachWithDest(byte[] bytes,
                                       float signLx,
                                       float signLy,
                                       String passPdf,
                                       InputStream in,
                                       File dest,
                                       int pageStamp) throws IOException, DocumentException {
        if (dest == null) throw new RuntimeException("File dest is null");
        Image image = Image.getInstance(bytes);
        image.setAbsolutePosition(signLx, signLy);

        PdfReader reader;
        if (isNotBlank(passPdf)) {
            reader = new PdfReader(in, passPdf.getBytes(StandardCharsets.UTF_8));
        } else reader = new PdfReader(in);
        pdfStamper(passPdf, dest, pageStamp, image, reader);
    }

    private static void pdfStamper(String passPdf, File src, int pageStamp, Image image, PdfReader reader) throws DocumentException, IOException {
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(src));

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
