package uk.nhs.hee.tis.revalidation.migration.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GmcHexStringConverterTest {

  @InjectMocks
  private GmcHexStringConverter hexStringConverter;

  //Note: the class under test is intended to check if gmc strings
  // (7 digit numbers) are being stored as hexadecimal (6 digits
  // with a leading 0) and convert if so
  private String testHexString = "0abc123";
  private String expectedTestHexResult = "11256099";
  private String testNonHexString = "1234567";

  @Test
  void shouldConvertValidHexStringWithLeadingZeroToDecimal() {
    assertThat(hexStringConverter.convertGmcString(testHexString), is(expectedTestHexResult));
  }

  @Test
  void shouldNotConvertStringWithoutLeadingZeroToDecimal() {
    assertThat(hexStringConverter.convertGmcString(testNonHexString), is(testNonHexString));
  }
}
