package uk.nhs.hee.tis.revalidation.migration.util;

import org.springframework.stereotype.Component;

//Note: this class is intended to check if gmc strings
// (7 digit numbers) are being stored as hexadecimal (6 digits
// with a leading 0) and convert if so
@Component
public class GmcHexStringConverter {
  public String convertGmcString(String gmcNumber) {
    Character leadingChar = gmcNumber.charAt(0);
    if(leadingChar.equals(new Character('0'))) {
      int decimal = Integer.parseInt(gmcNumber, 16);
      return Integer.toString(decimal);
    }
    return gmcNumber;
  }
}
