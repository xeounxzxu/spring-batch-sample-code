package io.xeounxzxu.springbatchbigdataflowsample.sample

import io.xeounxzxu.springbatchbigdataflowsample.common.JobTimingLogger
import org.springframework.batch.core.Job
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.core.step.tasklet.TaskletStep
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class SampleJobConfig(
    private val jobTimingLogger: JobTimingLogger,
    private val jobRepository: JobRepository,
    private val transactionManage: PlatformTransactionManager
) {

    @Bean
    fun sampleJob(
        sampleStep: TaskletStep
    ): Job {
        return JobBuilder("sampleJob", jobRepository)
            .start(sampleStep)
            .listener(jobTimingLogger)
            .build()
    }

    @Bean
    fun sampleStep(
        sampleTasklet: Tasklet
    ): TaskletStep {
        return StepBuilder("step", jobRepository)
            .tasklet(sampleTasklet, transactionManage)
            .build()
    }
}
