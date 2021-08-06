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

import static java.time.LocalDate.now;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.github.javafaker.Faker;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.nhs.hee.tis.revalidation.migration.entity.Recommendation;
import uk.nhs.hee.tis.revalidation.migration.entity.Revalidation;


@ExtendWith(MockitoExtension.class)
class BatchDataProcessorTest {

  private final Faker faker = new Faker();

  @InjectMocks
  BatchDataProcessor batchDataProcessor;

  private Integer id;
  private String tisId;
  private String deferralReason;
  private String deferralComment;
  private LocalDate deferralDate;
  private String proposedOutcomeCode;
  private String revalidationStatusCode;
  private Date gmcSubmissionDateTime;
  private String gmcSubmissionReturnCode;
  private String gmcOutcomeCode;
  private String gmcRecommendationId;
  private LocalDateTime gmcStatusCheckDateTime;
  private String admin;
  private LocalDate submissionDate;
  private String recommendationSubmitter;
  private LocalDateTime dateAdded;

  private Revalidation revalidationDto;


  /**
   * Set up data for testing.
   */
  @BeforeEach
  public void setup() {

    tisId = faker.lorem().characters(8);
    deferralReason = faker.lorem().characters(20);
    deferralComment = faker.lorem().characters(20);
    deferralDate = now();
    proposedOutcomeCode = faker.lorem().characters(20);
    revalidationStatusCode = faker.lorem().characters(20);
    gmcSubmissionDateTime = faker.date().past(10, TimeUnit.DAYS);
    gmcSubmissionReturnCode = faker.lorem().characters(20);
    gmcOutcomeCode = faker.lorem().characters(20);
    gmcRecommendationId = faker.lorem().characters(20);
    gmcStatusCheckDateTime = LocalDateTime.now();
    admin = faker.lorem().characters(20);
    submissionDate = now();
    recommendationSubmitter = faker.lorem().characters(20);
    dateAdded = LocalDateTime.now();

    revalidationDto = Revalidation.builder()
        .id(111)
        .tisId(tisId)
        .deferralReason(deferralReason)
        .deferralComment(deferralComment)
        .deferralDate(deferralDate)
        .proposedOutcomeCode(proposedOutcomeCode)
        .revalidationStatusCode(revalidationStatusCode)
        .gmcSubmissionDateTime(gmcSubmissionDateTime)
        .gmcSubmissionReturnCode(gmcSubmissionReturnCode)
        .gmcOutcomeCode(gmcOutcomeCode)
        .gmcRecommendationId(gmcRecommendationId)
        .gmcRecommendationId(gmcRecommendationId)
        .gmcStatusCheckDateTime(gmcStatusCheckDateTime)
        .admin(admin)
        .submissionDate(submissionDate)
        .recommendationSubmitter(recommendationSubmitter)
        .dateAdded(dateAdded)
        .build();
  }

  @Test
  void shouldProcessRevalidationDataToRecommendation() {
    Recommendation result = batchDataProcessor.process(revalidationDto);

    assertThat(result.getOutcome(), is(gmcOutcomeCode));
    assertThat(result.getRecommendationType(), is(proposedOutcomeCode));
    assertThat(result.getRecommendationStatus(), is(revalidationStatusCode));
    assertThat(result.getGmcSubmissionDate(), is(
        LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(gmcSubmissionDateTime))));
    assertThat(result.getActualSubmissionDate(), is(submissionDate));
    assertThat(result.getGmcRevalidationId(), is(gmcRecommendationId));
    assertThat(result.getDeferralDate(), is(deferralDate));
    assertThat(result.getDeferralReason(), is(deferralReason));
    assertThat(result.getComments(), is(Arrays.asList(deferralComment)));
    assertThat(result.getAdmin(), is(admin));
  }

  @Test
  void shouldHandleNull() {
    Revalidation nullRevalidationDto;
    nullRevalidationDto = Revalidation.builder()
        .id(111)
        .tisId(null)
        .deferralReason(null)
        .deferralComment(null)
        .deferralDate(null)
        .proposedOutcomeCode(null)
        .revalidationStatusCode(null)
        .gmcSubmissionDateTime(null)
        .gmcSubmissionReturnCode(null)
        .gmcOutcomeCode(null)
        .gmcRecommendationId(null)
        .gmcRecommendationId(null)
        .gmcStatusCheckDateTime(null)
        .admin(null)
        .submissionDate(null)
        .recommendationSubmitter(null)
        .dateAdded(null)
        .build();
    Recommendation result = batchDataProcessor.process(nullRevalidationDto);

    assertThat(result.getOutcome(), is(nullValue()));
    assertThat(result.getRecommendationType(), is(nullValue()));
    assertThat(result.getRecommendationStatus(), is(nullValue()));
    assertThat(result.getGmcSubmissionDate(), is(nullValue()));
    assertThat(result.getActualSubmissionDate(), is(nullValue()));
    assertThat(result.getGmcRevalidationId(), is(nullValue()));
    assertThat(result.getDeferralDate(), is(nullValue()));
    assertThat(result.getDeferralReason(), is(nullValue()));
    assertThat(result.getComments(), is(nullValue()));
    assertThat(result.getAdmin(), is(nullValue()));
  }
}
