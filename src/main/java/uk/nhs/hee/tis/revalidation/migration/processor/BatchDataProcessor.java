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

package uk.nhs.hee.tis.revalidation.migration.processor;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import uk.nhs.hee.tis.revalidation.migration.entity.Recommendation;
import uk.nhs.hee.tis.revalidation.migration.entity.Revalidation;

@Component
@Slf4j
public class BatchDataProcessor implements ItemProcessor<Revalidation, Recommendation> {

  private AtomicInteger count = new AtomicInteger();

  public BatchDataProcessor() {

  }

  @Override
  public Recommendation process(Revalidation revalidation) {
    Recommendation recommendation = new Recommendation();

    recommendation.setGmcNumber(revalidation.getTisId()); //to be updated to gmcNumber by joining tcs.gmcDetails table with TisId
    recommendation.setOutcome(revalidation.getGmcOutcomeCode()); // to be mapped to `RecommendationGmcOutcome` enum
    recommendation.setRecommendationType(revalidation.getProposedOutcomeCode()); // to be mapped to `RecommendationType` enum
    recommendation.setRecommendationStatus(revalidation.getRevalidationStatusCode()); // to be mapped to `RecommendationStatus` enum
    recommendation.setGmcSubmissionDate(mapGmcSubmissionDate(revalidation.getGmcSubmissionDateTime()));
    recommendation.setActualSubmissionDate(revalidation.getSubmissionDate());
    recommendation.setGmcRevalidationId(revalidation.getGmcRecommendationId());
    recommendation.setDeferralDate(revalidation.getDeferralDate());
    recommendation.setDeferralReason(revalidation.getDeferralReason()); // to be mapped with deferralReason code (some option can't be matched, to be discussed)
    recommendation.setDeferralSubReason(null); // to be mapped with deferralReason code
    recommendation.setComments(mapComments(revalidation.getDeferralComment()));
    recommendation.setAdmin(revalidation.getAdmin());

    log.info("Processing data " + revalidation.getId() + " Record no " + count.incrementAndGet());
    return recommendation;
  }

  private LocalDate mapGmcSubmissionDate(Date gmcSubmissionDate) {
    return (gmcSubmissionDate != null)
       ? LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(gmcSubmissionDate)) : null;
  }

  private List<String> mapComments(String comment) {
    return (comment != null) ? Arrays.asList(comment) : null;
  }

}
