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
import static org.mockito.Mockito.verify;

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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.nhs.hee.tis.revalidation.migration.entity.DeferralReason;
import uk.nhs.hee.tis.revalidation.migration.entity.Recommendation;
import uk.nhs.hee.tis.revalidation.migration.entity.RecommendationGmcOutcome;
import uk.nhs.hee.tis.revalidation.migration.entity.RecommendationStatus;
import uk.nhs.hee.tis.revalidation.migration.entity.RecommendationType;
import uk.nhs.hee.tis.revalidation.migration.entity.Revalidation;
import uk.nhs.hee.tis.revalidation.migration.util.GmcHexStringConverter;


@ExtendWith(MockitoExtension.class)
class BatchDataProcessorTest {

  private final Faker faker = new Faker();

  @InjectMocks
  BatchDataProcessor batchDataProcessor;

  @Mock
  GmcHexStringConverter gmcHexStringConverter;

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
  private String gmcNumber;

  private Revalidation revalidationDto = new Revalidation();


  /**
   * Set up data for testing.
   */
  @BeforeEach
  public void setup() {

    tisId = faker.lorem().characters(8);
    deferralReason = "EXAM_FAILURE";
    deferralComment = faker.lorem().characters(20);
    deferralDate = now();
    proposedOutcomeCode = "non_engagement";
    revalidationStatusCode = "ready_to_review";
    gmcSubmissionDateTime = faker.date().past(10, TimeUnit.DAYS);
    gmcSubmissionReturnCode = faker.lorem().characters(20);
    gmcOutcomeCode = "Approved";
    gmcRecommendationId = faker.lorem().characters(20);
    gmcStatusCheckDateTime = LocalDateTime.now();
    admin = faker.lorem().characters(20);
    submissionDate = now();
    recommendationSubmitter = faker.lorem().characters(20);
    dateAdded = LocalDateTime.now();
    gmcNumber = "1234567";

    revalidationDto.setId(111);
    revalidationDto.setTisId(tisId);
    revalidationDto.setDeferralReason(deferralReason);
    revalidationDto.setDeferralComment(deferralComment);
    revalidationDto.setDeferralDate(deferralDate);
    revalidationDto.setProposedOutcomeCode(proposedOutcomeCode);
    revalidationDto.setRevalidationStatusCode(revalidationStatusCode);
    revalidationDto.setGmcSubmissionDateTime(gmcSubmissionDateTime);
    revalidationDto.setGmcSubmissionReturnCode(gmcSubmissionReturnCode);
    revalidationDto.setGmcOutcomeCode(gmcOutcomeCode);
    revalidationDto.setGmcRecommendationId(gmcRecommendationId);
    revalidationDto.setGmcRecommendationId(gmcRecommendationId);
    revalidationDto.setGmcStatusCheckDateTime(gmcStatusCheckDateTime);
    revalidationDto.setAdmin(admin);
    revalidationDto.setSubmissionDate(submissionDate);
    revalidationDto.setRecommendationSubmitter(recommendationSubmitter);
    revalidationDto.setDateAdded(dateAdded);
    revalidationDto.setGmcNumber(gmcNumber);
  }

  @Test
  void shouldProcessRevalidationDataToRecommendation() {
    Recommendation result = batchDataProcessor.process(revalidationDto);

    assertThat(result.getOutcome(), is(RecommendationGmcOutcome.APPROVED));
    assertThat(result.getRecommendationType(), is(RecommendationType.NON_ENGAGEMENT));
    assertThat(result.getRecommendationStatus(), is(RecommendationStatus.READY_TO_REVIEW));
    assertThat(result.getGmcSubmissionDate(), is(
        LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(gmcSubmissionDateTime))));
    assertThat(result.getActualSubmissionDate(), is(submissionDate));
    assertThat(result.getGmcRevalidationId(), is(gmcRecommendationId));
    assertThat(result.getDeferralDate(), is(deferralDate));
    assertThat(result.getDeferralReason(), is(DeferralReason.EXAM_FAILURE.getCode()));
    assertThat(result.getComments(), is(Arrays.asList(deferralComment)));
    assertThat(result.getAdmin(), is(admin));
  }

  @Test
  void shouldHandleNull() {
    Revalidation nullRevalidationDto = new Revalidation();
    nullRevalidationDto.setId(111);
    nullRevalidationDto.setTisId(null);
    nullRevalidationDto.setDeferralReason(null);
    nullRevalidationDto.setDeferralComment(null);
    nullRevalidationDto.setDeferralDate(null);
    nullRevalidationDto.setProposedOutcomeCode(null);
    nullRevalidationDto.setRevalidationStatusCode(null);
    nullRevalidationDto.setGmcSubmissionDateTime(null);
    nullRevalidationDto.setGmcSubmissionReturnCode(null);
    nullRevalidationDto.setGmcOutcomeCode(null);
    nullRevalidationDto.setGmcRecommendationId(null);
    nullRevalidationDto.setGmcRecommendationId(null);
    nullRevalidationDto.setGmcStatusCheckDateTime(null);
    nullRevalidationDto.setAdmin(null);
    nullRevalidationDto.setSubmissionDate(null);
    nullRevalidationDto.setRecommendationSubmitter(null);
    nullRevalidationDto.setDateAdded(null);

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

  @Test
  void shouldCheckForHexGmcNumbers() {
    Recommendation result = batchDataProcessor.process(revalidationDto);
    verify(gmcHexStringConverter).convertGmcString(gmcNumber);
  }
}
