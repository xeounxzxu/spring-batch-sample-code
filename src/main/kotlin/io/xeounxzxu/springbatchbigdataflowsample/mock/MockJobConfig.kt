package io.xeounxzxu.springbatchbigdataflowsample.mock

import io.xeounxzxu.springbatchbigdataflowsample.common.JobTimingLogger
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.TaskletStep
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class MockJobConfig(
    private val jobTimingLogger: JobTimingLogger,
    private val jobRepository: JobRepository,
    private val transactionManage: PlatformTransactionManager
) {
    @Bean
    fun mockJob(
        mockStep: Step
    ): Job {
        return JobBuilder("mockJob", jobRepository)
            .start(mockStep)
            .listener(jobTimingLogger)
            .build()
    }

    @Bean
    fun mockStep(
        mockTasklet: MockTasklet
    ): TaskletStep {
        return StepBuilder("step", jobRepository)
            .tasklet(mockTasklet, transactionManage)
            .build()
    }
}
