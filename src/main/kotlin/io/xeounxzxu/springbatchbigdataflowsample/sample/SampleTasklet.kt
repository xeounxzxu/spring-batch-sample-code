package io.xeounxzxu.springbatchbigdataflowsample.sample

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class SampleTasklet : Tasklet {
    override fun execute(
        contribution: StepContribution,
        chunkContext: ChunkContext
    ): RepeatStatus {

        val jobParameters = chunkContext.stepContext.jobParameters
        val requestDate = jobParameters["requestDate"]

        logger.info { "Work Sample Job ======> Sample job started" }
        logger.info { "JobParameter ==> $requestDate" }
        return RepeatStatus.FINISHED
    }
}
