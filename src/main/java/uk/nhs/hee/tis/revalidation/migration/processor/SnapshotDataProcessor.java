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

import com.google.gson.Gson;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import uk.nhs.hee.tis.revalidation.migration.entity.Snapshot;
import uk.nhs.hee.tis.revalidation.migration.entity.TargetSnapshot;

@Component
@Slf4j
@Data
@NoArgsConstructor
public class SnapshotDataProcessor implements ItemProcessor<Snapshot, TargetSnapshot> {

  @Override
  public TargetSnapshot process(Snapshot snapshot) {
    Gson gson = new Gson();
    TargetSnapshot targetSnapshot = gson.fromJson(snapshot.getData(), TargetSnapshot.class);
    targetSnapshot.setGmcNumber(targetSnapshot.getTraineeProfile().getGmcId());
    targetSnapshot.setLegacyRevalidationId(targetSnapshot.getRevalidation().getId());
    targetSnapshot.setLegacyTisId(targetSnapshot.getRevalidation().getTisId());
    return targetSnapshot;
  }
}
