package io.xeounxzxu.springbatchbigdataflowsample.jpapage

import io.github.oshai.kotlinlogging.KotlinLogging
import io.xeounxzxu.springbatchbigdataflowsample.common.JobTimingLogger
import io.xeounxzxu.springbatchbigdataflowsample.domain.ItemEntity
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.core.ChunkListener
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

private val log = KotlinLogging.logger { }

@Configuration
class ItemJpaPageJobConfig(
    private val jobTimingLogger: JobTimingLogger,
    private val jobRepository: JobRepository,
    private val transactionManage: PlatformTransactionManager
) {

    // TODO: 페이징 과 청크 사이즈는 동일하게 작성을 한다.
    // chunk and pageable size
    // check to same value ...
    private val size = 10000

    /**
     * NOTE: Data Size : 102510
     * - 1000 의 size 로 돌릴시 3896ms
     * - 10000 의 size 로 돌릴시 2136ms
     */

    /**
     * NOTE: Data Size : 202510
     *  - 1000 ->  240318ms (4.0053 분)
     *  - 10000 -> 233983ms (3.8997166667 분)
     */

    @Bean
    fun itemJpaPageJob(
        itemJpaPageStep: Step
    ): Job {
        return JobBuilder("itemJpaPageJob", jobRepository)
            .start(itemJpaPageStep)
            .listener(jobTimingLogger)
            .build()
    }

    @Bean
    fun itemJpaPageStep(
        itemJpaPageReader: ItemReader<ItemEntity>,
        itemJpaPageWriter: ItemWriter<ItemEntity>
    ): Step {
        return StepBuilder("itemJpaPageStep", jobRepository)
            .chunk<ItemEntity, ItemEntity>(size, transactionManage)
            .reader(itemJpaPageReader)
            .writer(itemJpaPageWriter)
            .listener(chunkListener())
            .build()
    }

    @Bean
    fun chunkListener(): ChunkListener {
        return object : ChunkListener {
            override fun afterChunk(context: ChunkContext) {
                Thread.sleep(500) // chunk 처리 후 지연
            }
        }
    }

    @Bean
    @StepScope
    fun itemJpaPageReader(
        entityManagerFactory: EntityManagerFactory
    ): ItemReader<ItemEntity> {
        return JpaPagingItemReaderBuilder<ItemEntity>()
            .name("itemJpaPageReader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(size)
            .queryString(
                """
                    select i
                    from item i 
                    order by i.id asc
                """.trimIndent()
            )
            .build()
    }

    @Bean
    @StepScope
    fun itemJpaPageWriter(): ItemWriter<ItemEntity> {
        return ItemWriter { items ->
            items.forEach {
                it.complete()
            }
        }
    }
}
