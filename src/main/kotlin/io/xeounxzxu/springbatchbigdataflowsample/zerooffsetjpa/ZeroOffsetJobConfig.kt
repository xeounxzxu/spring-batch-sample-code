package io.xeounxzxu.springbatchbigdataflowsample.zerooffsetjpa

import io.xeounxzxu.springbatchbigdataflowsample.common.JobTimingLogger
import io.xeounxzxu.springbatchbigdataflowsample.domain.ItemEntity
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class ZeroOffsetJobConfig(
    private val jobTimingLogger: JobTimingLogger,
    private val jobRepository: JobRepository,
    private val transactionManage: PlatformTransactionManager
) {

    private val size = 1000

    @Bean
    fun zeroOffsetJob(
        zeroOffsetStep: Step
    ): Job {
        return JobBuilder("zeroOffsetJob", jobRepository)
            .start(zeroOffsetStep)
            .listener(jobTimingLogger)
            .build()
    }

    @Bean
    fun zeroOffsetStep(
        zeroOffsetJpaItemReader: ItemReader<ItemEntity>,
        itemZeroOffsetWriter: ItemWriter<ItemEntity>
    ): Step {
        return StepBuilder("zeroOffsetStep", jobRepository)
            .chunk<ItemEntity, ItemEntity>(size, transactionManage)
            .reader(zeroOffsetJpaItemReader)
            .writer(itemZeroOffsetWriter)
            .build()
    }

    @Bean
    fun itemZeroOffsetWriter(): ItemWriter<ItemEntity> {
        return ItemWriter { items ->
            items.forEach {
                it.complete()
            }
        }
    }
}
