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

package uk.nhs.hee.tis.revalidation.migration.entity;

public enum DeferralReason {

  INSUFFICIENT_EVIDENCE("1", "Insufficient evidence for a positive recommendation", "INSUFFICIENT_EVIDENCE"),
  ONGOING_PROCESS("2", "The doctor is subject to an ongoing process", "ONGOING_PROCESS"),
  BELOW_1_YEAR_TO_CCT("3", "Below 1 year to CCT", "BELOW_1_YEAR_TO_CCT"),
  SICK_CARERS_LEAVE("4", "Sick carers leave", "SICK_CARERS_LEAVE"),
  PARENTAL_LEAVE("5", "Parental leave", "PARENTAL_LEAVE"),
  EXAM_FAILURE("6", "Exam failure", "EXAM_FAILURE"),
  OTHER("7", "Other", "OTHER"),
  OUT_OF_CLINICAL_TRAINING("8", "Out of Clinical training", "OUT_OF_CLINICAL_TRAINING"),
  BELOW_5_YEARS_FULL_REG("9", "Below 5 years full reg", "BELOW_5_YEARS_FULL_REG");

  final String code;
  final String reason;
  final String abbr;

  DeferralReason(final String code, final String reason, final String abbr) {
    this.code = code;
    this.reason = reason;
    this.abbr = abbr;
  }

  /**
   * Map deferral reason string value to code.
   */
  public static String fromString(final String value) {
    if (value != null) {
      for (final DeferralReason deferralReason : DeferralReason.values()) {
        if (deferralReason.abbr.equalsIgnoreCase(value)) {
          return deferralReason.code;
        }
      }
    }
    return null;
  }

  /**
   * Map deferral reason to code.
   */
  public String getCode() {
    return this.code;
  }
}
