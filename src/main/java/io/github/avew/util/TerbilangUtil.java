package io.github.avew.util;

/**
 * Created by heru on 4/26/16.
 */

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

/**
 * This class will convert numeric values into an english representation
 * <p>
 * For units, see : http://www.jimloy.com/math/Milyar.htm
 *
 * @author yanick.rochon@gmail.com
 */

public class TerbilangUtil {

	public static String terbilang(Long number) {
		return processTerbilang(number.toString());
	}

	public static String terbilang(BigDecimal number) {
		return processTerbilang(number.toString());
	}

	private static String processTerbilang(String number) {
		String result = "";
		String decimal = "";
		String[] rev;
		String[] revDecimal = new String[0];
		if (number.contains(".")) {
			int length = number.length();
			decimal = number.substring(length - 3, length);
			revDecimal = decimal.split("");

			String angka = number.substring(0, length - 3);
			rev = angka.split("");

			Collections.reverse(Arrays.asList(rev));
			Collections.reverse(Arrays.asList(revDecimal));
		} else {
			rev = number.split("");
			Collections.reverse(Arrays.asList(rev));
		}

		// handle decimal
		if (revDecimal.length > 0) {
			String thousands = "";
			for (int b = 0; b < revDecimal.length; b++) {
				if (b == 3) {
					thousands = "ribu ";
				} else if (b == 6) {
					thousands = "juta ";
				} else if (b == 9) {
					thousands = "miliar ";
				} else if (b == 12) {
					thousands = "triliun ";
				} else if (b == 15) {
					thousands = "billiun ";
				}

				if (!Objects.equals(revDecimal[b], "0")) {
					if ((b % 3) == 0) {
						if ((b + 1) < revDecimal.length) {
							if (Objects.equals(revDecimal[b + 1], "1")) {
								result = toWords(revDecimal, b, false) + "belas " + thousands + result;
								b++;
							} else {
								result = toWords(revDecimal, b, false) + thousands + result;
							}
						} else {
							result = toWords(revDecimal, b, false) + thousands + result;
						}
						thousands = "";
					} else if ((b % 3) == 2) {
						result = toWords(revDecimal, b, false) + "koma " + thousands + result;
						thousands = "";
					} else if ((b % 3) == 1) {
						result = toWords(revDecimal, b, false) + "puluh " + thousands + result;
						thousands = "";
					}
				}
			}
		}

		// handle non-decimal
		String thousands = "";
		for (int b = 0; b < rev.length; b++) {
			if (b == 3) {
				thousands = "ribu ";
			} else if (b == 6) {
				thousands = "juta ";
			} else if (b == 9) {
				thousands = "miliar ";
			} else if (b == 12) {
				thousands = "triliun ";
			} else if (b == 15) {
				thousands = "billiun ";
			}

			if (!Objects.equals(rev[b], "0")) {
				if ((b % 3) == 0) {
					if ((b + 1) < rev.length) {
						if (Objects.equals(rev[b + 1], "1")) {
							result = toWords(rev, b, false) + "belas " + thousands + result;
							b++;
						} else {
							result = toWords(rev, b, false) + thousands + result;
						}
					} else {
						result = toWords(rev, b, false) + thousands + result;
					}
					thousands = "";
				} else if ((b % 3) == 2) {
					result = toWords(rev, b, false) + "ratus " + thousands + result;
					thousands = "";
				} else if ((b % 3) == 1) {
					result = toWords(rev, b, false) + "puluh " + thousands + result;
					thousands = "";
				}
			}
		}

		return result.toUpperCase() + " RUPIAH";
	}

	private static String toWords(String[] arr, int index, boolean decimal) {
		String number = arr[index];

		String result = "";

		switch (number) {
			case ".":
				if (decimal) {
					result = "koma ";
				}
				break;
			case "0":
				if (decimal) {
					result = "nol ";
					break;
				} else {
					result = "";
					break;
				}
			case "1":
				if ((index + 1) < arr.length) {
					if (!decimal && (index == 1 || index == 2 || Objects.equals(arr[index + 1], "1") || (index % 3) == 1 || (index % 3) == 2)) {
						result = "se";
						break;
					} else {
						result = "satu ";
						break;
					}
				} else {
					if (!decimal && (index == 1 || index == 2 || index == 3 || (index % 3) == 1 || (index % 3) == 2)) {
						result = "se";
						break;
					} else {
						result = "satu ";
						break;
					}
				}
			case "2":
				result = "dua ";
				break;
			case "3":
				result = "tiga ";
				break;
			case "4":
				result = "empat ";
				break;
			case "5":
				result = "lima ";
				break;
			case "6":
				result = "enam ";
				break;
			case "7":
				result = "tujuh ";
				break;
			case "8":
				result = "delapan ";
				break;
			case "9":
				result = "sembilan ";
				break;
		}

		return result;
	}
}
