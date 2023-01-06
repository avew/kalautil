package io.github.avew.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class RupiahUtil {

    private static String[] huruf = {"", "Satu", "Dua", "Tiga", "Empat", "Lima", "Enam", "Tujuh", "Delapan", "Sembilan", "Sepuluh", "Sebelas"};

    private static String angkaToTerbilang(Long angka) {
        if (angka < 12)
            return huruf[angka.intValue()];
        if (angka >= 12 && angka <= 19)
            return huruf[angka.intValue() % 10] + " Belas";
        if (angka >= 20 && angka <= 99)
            return angkaToTerbilang(angka / 10) + " Puluh " + huruf[angka.intValue() % 10];
        if (angka >= 100 && angka <= 199)
            return "Seratus " + angkaToTerbilang(angka % 100);
        if (angka >= 200 && angka <= 999)
            return angkaToTerbilang(angka / 100) + " Ratus " + angkaToTerbilang(angka % 100);
        if (angka >= 1000 && angka <= 1999)
            return "Seribu " + angkaToTerbilang(angka % 1000);
        if (angka >= 2000 && angka <= 999999)
            return angkaToTerbilang(angka / 1000) + " Ribu " + angkaToTerbilang(angka % 1000);
        if (angka >= 1000000 && angka <= 999999999)
            return angkaToTerbilang(angka / 1000000) + " Juta " + angkaToTerbilang(angka % 1000000);
        if (angka >= 1000000000 && angka <= 999999999999L)
            return angkaToTerbilang(angka / 1000000000) + " Milyar " + angkaToTerbilang(angka % 1000000000);
        if (angka >= 1000000000000L && angka <= 999999999999999L)
            return angkaToTerbilang(angka / 1000000000000L) + " Triliun " + angkaToTerbilang(angka % 1000000000000L);
        if (angka >= 1000000000000000L && angka <= 999999999999999999L)
            return angkaToTerbilang(angka / 1000000000000000L) + " Quadrilyun " + angkaToTerbilang(angka % 1000000000000000L);
        return "";
    }

    public static String rupiah(Long value) {
        String rupiah;
        String rupiahTerbilang = angkaToTerbilang(value);
        String lastRupiahTerbilang = rupiahTerbilang.substring(rupiahTerbilang.length() - 1);
        if (lastRupiahTerbilang.equals(" ")) {
            log.debug("LAST RUPIAH IS SPACE TRUE");
            StringBuffer sb = new StringBuffer(rupiahTerbilang);
            sb.deleteCharAt(rupiahTerbilang.length() - 1);
            sb.append(" Rupiah");
            log.debug("RUPIAH TERBILANG IS= {}", sb);
            rupiah = sb.toString();
        } else {
            rupiah = String.join(" ", angkaToTerbilang(value), "Rupiah");
        }
        return StringUtils.normalizeSpace(rupiah);
//        return rupiah.trim().replaceAll("\\s{2,}", " ");
    }
}
