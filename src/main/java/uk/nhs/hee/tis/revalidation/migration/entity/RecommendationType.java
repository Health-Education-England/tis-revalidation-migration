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
