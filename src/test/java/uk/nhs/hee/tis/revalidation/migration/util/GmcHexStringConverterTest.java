/*
 * The MIT License (MIT)
 *
 * Copyright 2021 Crown Copyright (Health Education England)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package uk.nhs.hee.tis.revalidation.migration.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GmcHexStringConverterTest {

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
