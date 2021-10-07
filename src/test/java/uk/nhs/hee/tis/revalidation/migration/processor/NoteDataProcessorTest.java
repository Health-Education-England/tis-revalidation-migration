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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.nhs.hee.tis.revalidation.migration.entity.TargetNotes;
import uk.nhs.hee.tis.revalidation.migration.entity.TraineeNote;


@ExtendWith(MockitoExtension.class)
class NoteDataProcessorTest {

  private final Faker faker = new Faker();

  @InjectMocks
  NoteDataProcessor noteDataProcessor;

  private Integer id;
  private String tisId;
  private String text;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;
  private String gmcNumber;

  private TraineeNote traineeNote = new TraineeNote();


  /**
   * Set up data for testing.
   */
  @BeforeEach
  public void setup() {

    tisId = faker.lorem().characters(8);
    text = faker.lorem().characters(100);
    createdDate = LocalDateTime.now();
    updatedDate = LocalDateTime.now();
    gmcNumber = faker.lorem().characters(8);

    traineeNote.setTisId(tisId);
    traineeNote.setText(text);
    traineeNote.setCreatedDate(createdDate);
    traineeNote.setUpdatedDate(updatedDate);
    traineeNote.setGmcNumber(gmcNumber);
  }

  @Test
  void shouldProcessRevalidationDataToRecommendation() {
    TargetNotes result = noteDataProcessor.process(traineeNote);

    assertThat(result.getGmcId(), is(gmcNumber));
    assertThat(result.getText(), is(text));
    assertThat(result.getCreatedDate(), is(createdDate));
    assertThat(result.getUpdatedDate(), is(updatedDate));
  }
}
