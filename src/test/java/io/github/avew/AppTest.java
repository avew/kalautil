package io.github.avew;

import io.github.avew.util.RupiahUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testRupiahTerbilang() {
        String seRibuRupiah = RupiahUtil.rupiah(1532L);
        Assert.assertEquals("Seribu Lima Ratus Tiga Puluh Dua Rupiah", seRibuRupiah);
        String seraturRibuRupiah = RupiahUtil.rupiah(100000L);
        Assert.assertEquals("Seratus Ribu Rupiah", seraturRibuRupiah);
        String rupiah = RupiahUtil.rupiah(543423232L);
        Assert.assertEquals("Lima Ratus Empat Puluh Tiga Juta Empat Ratus Dua Puluh Tiga Ribu Dua Ratus Tiga Puluh Dua Rupiah",rupiah);
    }
}
