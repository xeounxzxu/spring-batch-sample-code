package io.xeounxzxu.springbatchbigdataflowsample.runner

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.configuration.JobLocator
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

@Component
class JobRunner(
    private val jobLauncher: JobLauncher,
    private val jobLocator: JobLocator
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        // 프로그램 아규먼트에 다음와 같이 (`--job.name=sampleJob`) 넣어준다.
        val jobName = args.getOptionValues("job.name").firstOrNull()
            ?: throw RuntimeException("job name is required")

        logger.info { "============ BOOT_RUN ============" }
        logger.info { "====== JOB_NAME: $jobName ========" }
        logger.info { "============ BOOT_RUN ============" }

        val job = jobLocator.getJob(jobName)

        val params = JobParametersBuilder()
            .addString("requestDate", LocalDateTime.now().toString())
            .toJobParameters()

        jobLauncher.run(job, params)
    }
}
