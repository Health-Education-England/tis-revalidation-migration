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

package uk.nhs.hee.tis.revalidation.migration.reader;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import uk.nhs.hee.tis.revalidation.migration.entity.Recommendation;
import uk.nhs.hee.tis.revalidation.migration.entity.Revalidation;
import uk.nhs.hee.tis.revalidation.migration.entity.Snapshot;
import uk.nhs.hee.tis.revalidation.migration.entity.TargetNotes;
import uk.nhs.hee.tis.revalidation.migration.entity.TargetSnapshot;
import uk.nhs.hee.tis.revalidation.migration.entity.TraineeNote;

@Configuration
@EnableBatchProcessing
public class BatchDataReader {

  @Bean
  public Job job(JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory,
      ItemReader<Revalidation> itemReader,
      ItemReader<Snapshot> snapshotItemReader,
      ItemReader<TraineeNote> noteItemReader,
      ItemProcessor<Revalidation, Recommendation> itemProcessor,
      ItemProcessor<Snapshot, TargetSnapshot> snapshotItemProcessor,
      ItemProcessor<TraineeNote, TargetNotes> noteItemProcessor,
      ItemWriter<Recommendation> itemWriter,
      ItemWriter<TargetSnapshot> snapshotItemWriter,
      ItemWriter<TargetNotes> noteItemWriter
  ) {

    Step recommendationMigrationStep = stepBuilderFactory.get("Recommendation-data-load")
        .<Revalidation, Recommendation>chunk(100)
        .reader(itemReader)
        .processor(itemProcessor)
        .writer(itemWriter)
        .build();

    Step snapshotMigrationStep = stepBuilderFactory.get("Snapshot-data-load")
        .<Snapshot, TargetSnapshot>chunk(100)
        .reader(snapshotItemReader)
        .processor(snapshotItemProcessor)
        .writer(snapshotItemWriter)
        .build();

    Step noteMigrationStep = stepBuilderFactory.get("Note-data-load")
        .<TraineeNote, TargetNotes>chunk(100)
        .reader(noteItemReader)
        .processor(noteItemProcessor)
        .writer(noteItemWriter)
        .build();

    return jobBuilderFactory.get("Revalidation-migration-Load")
        .incrementer(new RunIdIncrementer())
        .start(recommendationMigrationStep)
        .next(snapshotMigrationStep)
        .next(noteMigrationStep)
        .build();
  }

  @Bean
  public ItemReader<Revalidation> itemReader(DataSource dataSource) {
    JdbcPagingItemReader<Revalidation> jdbcPagingItemReader = new JdbcPagingItemReader<>();
    jdbcPagingItemReader.setDataSource(dataSource);
    jdbcPagingItemReader.setPageSize(21000);
    PagingQueryProvider queryProvider = createQuery(
        "FROM revalidation.Revalidation reval "
            + "INNER JOIN auth.TraineeProfile gmc ON reval.tisId = gmc.tisId", "reval.id");
    jdbcPagingItemReader.setQueryProvider(queryProvider);
    jdbcPagingItemReader.setRowMapper(new BeanPropertyRowMapper<>(Revalidation.class));
    return jdbcPagingItemReader;
  }

  @Bean
  public ItemReader<Snapshot> itemReader1(DataSource dataSource) {
    JdbcPagingItemReader<Snapshot> jdbcPagingItemReader = new JdbcPagingItemReader<>();
    jdbcPagingItemReader.setDataSource(dataSource);
    jdbcPagingItemReader.setPageSize(21000);
    PagingQueryProvider queryProvider = createQuery("FROM revalidation.Snapshot",
        "Snapshot.revalidationId");
    jdbcPagingItemReader.setQueryProvider(queryProvider);
    jdbcPagingItemReader.setRowMapper(new BeanPropertyRowMapper<>(Snapshot.class));
    return jdbcPagingItemReader;
  }

  /**
   * Item Reader for reading Trainee Notes data from Revalidation.
   *
   * @param dataSource data of trainee notes
   * @return JdbcPagingItemReader for trainee ntoes data
   */
  @Bean
  public ItemReader<TraineeNote> itemReaderNotes(DataSource dataSource) {
    JdbcPagingItemReader<TraineeNote> jdbcPagingItemReader = new JdbcPagingItemReader<>();
    jdbcPagingItemReader.setDataSource(dataSource);
    jdbcPagingItemReader.setPageSize(21000);
    PagingQueryProvider queryProvider = createQuery("FROM revalidation.TraineeNote reval "
        + "INNER JOIN auth.TraineeProfile gmc ON reval.tisId = gmc.tisId", "reval.id");
    jdbcPagingItemReader.setQueryProvider(queryProvider);
    jdbcPagingItemReader.setRowMapper(new BeanPropertyRowMapper<>(TraineeNote.class));
    return jdbcPagingItemReader;
  }

  private PagingQueryProvider createQuery(String clause, String sortKeys) {
    MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
    queryProvider.setSelectClause("SELECT * ");
    queryProvider.setFromClause(clause);
    queryProvider.setSortKeys(sortByKey(sortKeys));
    return queryProvider;
  }

  private Map<String, Order> sortByKey(String sortKeys) {
    Map<String, Order> stringOrderMap = new LinkedHashMap<>();
    stringOrderMap.put(sortKeys, Order.ASCENDING);
    return stringOrderMap;
  }
}
