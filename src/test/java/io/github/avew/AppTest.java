package io.github.avew;

import com.itextpdf.text.DocumentException;
import io.github.avew.util.PdfUtil;
import io.github.avew.util.RupiahUtil;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private static final String BASE_64_IMAGE = "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";
    private static double LX = 378.67;
    private static double LY = 109.30000000000001;

    @Test
    public void testRupiahTerbilang() {
        String seRibuRupiah = RupiahUtil.rupiah(1532L);
        Assert.assertEquals("Seribu Lima Ratus Tiga Puluh Dua Rupiah", seRibuRupiah);
        String seraturRibuRupiah = RupiahUtil.rupiah(100000L);
        Assert.assertEquals("Seratus Ribu Rupiah", seraturRibuRupiah);
        String rupiah = RupiahUtil.rupiah(543423232L);
        Assert.assertEquals("Lima Ratus Empat Puluh Tiga Juta Empat Ratus Dua Puluh Tiga Ribu Dua Ratus Tiga Puluh Dua Rupiah", rupiah);
    }

    @Test
    public void testSrcFileAttachImageWithBase64() throws DocumentException, IOException {
        File src = new File("src/test/resources/BLANK.pdf");
        PdfUtil.attachBase64Image(src, BASE_64_IMAGE, null, LX, LY, 1);
    }

    @Test
    public void testSrcFileAttachImageBase64WithDest() throws DocumentException, IOException {
        File src = new File("src/test/resources/BLANK.pdf");
        File dest = new File("src/test/resources/OUTPUT.pdf");
        PdfUtil.attachBase64ImageWithDest(src, dest, BASE_64_IMAGE, null, LX, LY, 1);
    }

    @Test
    public void testSrcIsAttachImageBase64WithDest() throws DocumentException, IOException {
        File src = new File("src/test/resources/BLANK.pdf");
        File dest = new File("src/test/resources/OUTPUT.pdf");
        PdfUtil.attachBase64ImageWithDest(FileUtils.openInputStream(src), dest, BASE_64_IMAGE, null, LX, LY, 1);
    }

    @Test
    public void testSrcFileAttachImage() throws DocumentException, IOException {
        File src = new File("src/test/resources/BLANK.pdf");
        File stamp = new File("src/test/resources/meterai.png");
        PdfUtil.attachImage(src, FileUtils.openInputStream(stamp), null, LX, LY, 1);
    }

    @Test
    public void testSrcFileAttachImageWithDest() throws DocumentException, IOException {
        File src = new File("src/test/resources/BLANK.pdf");
        File dest = new File("src/test/resources/OUTPUT.pdf");
        File stamp = new File("src/test/resources/meterai.png");
        PdfUtil.attachImageWithDest(src, dest, FileUtils.openInputStream(stamp), null, LX, LY, 1);
    }

    @Test
    public void testSrcIsAttachImageWithDest() throws DocumentException, IOException {
        File src = new File("src/test/resources/BLANK.pdf");
        File dest = new File("src/test/resources/OUTPUT.pdf");
        File stamp = new File("src/test/resources/meterai.png");
        PdfUtil.attachImageWithDest(FileUtils.openInputStream(src), dest, FileUtils.openInputStream(stamp), null, LX, LY, 1);
    }

    @Test
    public void testAttachImageBase64WithDest() throws DocumentException, IOException {
        File src = new File("src/test/resources/SAMPLE.pdf");
        File dest = new File("src/test/resources/OUTPUT.pdf");
        PdfUtil.attachBase64ImageWithDest(src, dest, BASE_64_IMAGE, null, LX, LY, 1);
    }
}
