package io.xeounxzxu.springbatchbigdataflowsample.mock

import io.xeounxzxu.springbatchbigdataflowsample.domain.ItemEntity
import io.xeounxzxu.springbatchbigdataflowsample.domain.ItemRepository
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class MockTasklet(
    private val itemRepository: ItemRepository
) : Tasklet {


    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {

        for (i in 1..100_000) {
            val entity = ItemEntity(
                name = "test_$i",
                completeDt = LocalDateTime.now()
            )
            itemRepository.save(entity)
        }


        return RepeatStatus.FINISHED
    }
}
