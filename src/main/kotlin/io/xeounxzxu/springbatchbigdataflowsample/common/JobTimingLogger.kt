package io.xeounxzxu.springbatchbigdataflowsample.common

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.ZoneOffset

private val logger = KotlinLogging.logger {}

@Component
class JobTimingLogger : JobExecutionListener {

    override fun beforeJob(jobExecution: JobExecution) {
        logger.info { "🟢 [${jobExecution.jobInstance.jobName}] 시작: ${jobExecution.startTime}" }
    }

    override fun afterJob(jobExecution: JobExecution) {
        val start = jobExecution.startTime?.toInstant(ZoneOffset.UTC)
        val end = jobExecution.endTime?.toInstant(ZoneOffset.UTC)

        if (start != null && end != null) {
            val duration = Duration.between(start, end).toMillis()
            logger.info { "✅ [${jobExecution.jobInstance.jobName}] 완료: ${jobExecution.endTime} (소요시간: ${duration}ms)" }
        }
    }
}
