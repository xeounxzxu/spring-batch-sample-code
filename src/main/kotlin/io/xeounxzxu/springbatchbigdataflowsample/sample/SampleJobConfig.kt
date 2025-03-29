package io.xeounxzxu.springbatchbigdataflowsample.sample

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
class SampleJobConfig {

    @Bean
    fun sampleJob(
        jobRepository: JobRepository,
        sampleStep: TaskletStep
    ): Job {
        return JobBuilder("sampleJob", jobRepository)
            .start(sampleStep)
            .build()
    }

    @Bean
    fun sampleStep(
        jobRepository: JobRepository,
        sampleTasklet: Tasklet,
        transactionManage: PlatformTransactionManager
    ): TaskletStep {
        return StepBuilder("step", jobRepository)
            .tasklet(sampleTasklet, transactionManage)
            .build()
    }
}
