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

public enum RecommendationType {

  REVALIDATE("1", "Revalidate"), DEFER("2", "Defer"), NON_ENGAGEMENT("3", "Non_Engagement");

  final String code;
  final String type;

  RecommendationType(final String code, final String type) {
    this.code = code;
    this.type = type;
  }

  /**
   * Map recommendation type string value to RecommendationType.
   */
  public static RecommendationType fromString(final String value) {
    if (value != null) {
      for (final RecommendationType recommendationType : RecommendationType.values()) {
        if (recommendationType.type.equalsIgnoreCase(value)) {
          return recommendationType;
        }
      }
    }
    return null;
  }
}
