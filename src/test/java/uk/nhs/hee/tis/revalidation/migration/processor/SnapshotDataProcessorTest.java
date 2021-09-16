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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.hee.tis.revalidation.migration.entity.Snapshot;
import uk.nhs.hee.tis.revalidation.migration.entity.TargetSnapshot;


@ExtendWith(MockitoExtension.class)
class SnapshotDataProcessorTest {

  private final Faker faker = new Faker();

  @InjectMocks
  SnapshotDataProcessor snapshotDataProcessor;

  private String revalidationId;
  private String tisId;
  private String proposedOutcomeCode;
  private String postCode;
  private String gmcId;
  private String submitterEmailAddress;
  private String roEmailAddress;
  private String assessmentsProgrammeName;
  private String placementType;
  private String traineeCardCurriculumCode;
  private Integer notesId;
  private String concernsPerson;

  private Snapshot snapshotDto = new Snapshot();
  private String snapshotData;


  /**
   * Set up data for testing.
   */
  @BeforeEach
  public void setup() {

    revalidationId = faker.lorem().characters(8);;
    tisId = faker.lorem().characters(8);
    proposedOutcomeCode = faker.lorem().characters(20);
    postCode = faker.lorem().characters(8);
    gmcId = faker.lorem().characters(8);
    submitterEmailAddress = faker.lorem().characters(20);
    roEmailAddress = faker.lorem().characters(20);
    assessmentsProgrammeName = faker.lorem().characters(20);
    placementType = faker.lorem().characters(8);
    traineeCardCurriculumCode = faker.lorem().characters(20);
    notesId = 222;
    concernsPerson = faker.lorem().characters(20);

    snapshotData = "{"
        + "\"revalidation\":{"
        + "\"id\":" + revalidationId + ","
        + "\"tisId\":\"" + tisId + "\","
        + "\"proposedOutcomeCode\":\"" + proposedOutcomeCode + "\""
        + "},"
        + "\"contactDetails\":{"
        + "\"postCode\":\"" + postCode + "\""
        + "},"
        + "\"traineeProfile\":{"
        + "\"gmcId\":\"" + gmcId + "\""
        + "},"
        + "\"submitter\":{"
        + "\"emailAddress\":\"" + submitterEmailAddress + "\""
        + "},"
        + "\"ro\":{"
        + "\"emailAddress\":\"" + roEmailAddress + "\""
        + "},"
        + "\"arcps\":{"
        + "\"assessments\":["
        + "{\"programmeName\":\"" + assessmentsProgrammeName + "\"}"
        + "]},"
        + "\"placements\":{"
        + "\"placements\":["
        + "{\"placementType\":\"" + placementType + "\"}"
        + "]},"
        + "\"traineeCard\":{"
        + "\"programmeList\":["
        + "{\"curriculumList\":["
        + "{\"code\":\"" + traineeCardCurriculumCode + "\"}"
        + "]}]},"
        + "\"notes\":["
        + "{\"id\":" + notesId + "}"
        + "],"
        + "\"concerns\":{"
        + "\"concerns\":["
        + "{\"contactPerson\":\"" + concernsPerson + "\"}"
        + "]}"
        + "}";

    snapshotDto.setRevalidationId(revalidationId);
    snapshotDto.setData(snapshotData);
  }

  @Test
  void shouldProcessSnapshotDataToTargetSnapshot() {
    TargetSnapshot result = snapshotDataProcessor.process(snapshotDto);

    assertThat(result.getRevalidation().getTisId(), is(tisId));
    assertThat(result.getRevalidation().getProposedOutcomeCode(), is(proposedOutcomeCode));
    assertThat(result.getContactDetails().getPostCode(), is(postCode));
    assertThat(result.getTraineeProfile().getGmcId(), is(gmcId));
    assertThat(result.getSubmitter().getEmailAddress(), is(submitterEmailAddress));
    assertThat(result.getRo().getEmailAddress(), is(roEmailAddress));
    assertThat(result.getArcps().getAssessments().get(0).getProgrammeName(),
        is(assessmentsProgrammeName));
    assertThat(result.getPlacements().getPlacements().get(0).getPlacementType(), is(placementType));
    assertThat(
        result.getTraineeCard().getProgrammeList().get(0).getCurriculumList().get(0).getCode(),
        is(traineeCardCurriculumCode));
    assertThat(result.getNotes().get(0).getId(), is(notesId));
    assertThat(result.getConcerns().getConcerns().get(0).getContactPerson(), is(concernsPerson));

    assertThat(result.getGmcNumber(), is(gmcId));
    assertThat(result.getLegacyRevalidationId(), is(revalidationId));
    assertThat(result.getLegacyTisId(), is(tisId));
  }
}
