package uk.nhs.hee.tis.revalidation.migration.entity;

public enum RecommendationGmcOutcome {

  APPROVED("Approved"), REJECTED("Rejected"), UNDER_REVIEW("Under Review");

  private final String outcome;

  RecommendationGmcOutcome(final String outcome) {
    this.outcome = outcome;
  }

  /**
   * Map outcome string value to RecommendationGmcOutcome.
   */
  public static RecommendationGmcOutcome fromString(final String value) {
    if (value != null) {
      for (final RecommendationGmcOutcome gmcOutcome : RecommendationGmcOutcome.values()) {
        if (gmcOutcome.outcome.equalsIgnoreCase(value)) {
          return gmcOutcome;
        }
      }
    }
    return null;
  }
}
