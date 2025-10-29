package org.example.utils;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

  public static long generateRandomNumber() {
    Random random = new Random();
    return random.nextInt(999999 - 111111 + 1) + 111111;
  }

  public static LocalDate generateRandomDate() {
    LocalDate startDate = LocalDate.of(2023, 1, 1);
    LocalDate endDate = LocalDate.of(2024, 9, 24);
    long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
    long randomDays = ThreadLocalRandom.current().nextLong(0, daysBetween + 1);

    return startDate.plusDays(randomDays);
  }


  public static String generateRandomCUIT() {
    int[] possiblePrefixes = {20, 27, 30};
    int prefix = possiblePrefixes[ThreadLocalRandom.current().nextInt(0, possiblePrefixes.length)];
    long dni = ThreadLocalRandom.current().nextLong(10000000L, 99999999L);
    String baseCuit = String.format("%02d%d", prefix, dni);
    int verificador = calculateCheckDigit(baseCuit);

    return baseCuit + verificador;
  }

  private static int calculateCheckDigit(String baseCuit) {
    int[] coefficients = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
    int sum = 0;

    for (int i = 0; i < baseCuit.length(); i++) {
      sum += Character.getNumericValue(baseCuit.charAt(i)) * coefficients[i];
    }

    int remainder = sum % 11;
    int checkDigit = 11 - remainder;

    if (checkDigit == 11) {
      return 0;
    } else if (checkDigit == 10) {
      return 9;
    } else {
      return checkDigit;
    }
  }

  public static String formatLocalDateToString(LocalDate date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    return date.format(formatter);
  }

  public static String getFormatMilDecimal(double valor, int decimal) {
    NumberFormat formatter = NumberFormat.getInstance(Locale.US);

    formatter.setMaximumFractionDigits(decimal);
    formatter.setMinimumFractionDigits(decimal);

    return formatter.format(valor);
  }

  public static LocalDate lessSixMonth(LocalDate fecha) {
    return fecha.minusMonths(6);
  }

  public static String formatterCuit(String cuitSinGuiones) {
    if (cuitSinGuiones != null && cuitSinGuiones.length() == 11)
      return cuitSinGuiones.substring(0, 2) + "-" + cuitSinGuiones.substring(2, 10) + "-"
          + cuitSinGuiones.substring(10, 11);

    return cuitSinGuiones;
  }

  public static boolean isEmpty(String value) {
    return value == null || value.isEmpty() || value.trim().isEmpty();
  }
}
