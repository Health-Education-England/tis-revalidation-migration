package uk.nhs.hee.tis.revalidation.migration.entity;

public enum RecommendationStatus {

  NOT_STARTED("not_started"), STARTED("started"), READY_TO_REVIEW("ready_to_review"),
  READY_TO_SUBMIT("ready_to_submit"), SUBMITTED_TO_GMC("submitted_to_gmc");

  final String status;

  RecommendationStatus(final String status) {
    this.status = status;
  }

  /**
   * Map recommendation status string value to RecommendationStatus.
   */
  public static RecommendationStatus fromString(final String value) {
    if (value != null) {
      for (final RecommendationStatus recommendationStatus : RecommendationStatus.values()) {
        if (recommendationStatus.status.equalsIgnoreCase(value)) {
          return recommendationStatus;
        }
      }
    }
    return null;
  }
}
