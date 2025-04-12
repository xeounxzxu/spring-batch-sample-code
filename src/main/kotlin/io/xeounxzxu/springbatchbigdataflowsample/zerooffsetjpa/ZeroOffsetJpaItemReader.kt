package io.xeounxzxu.springbatchbigdataflowsample.zerooffsetjpa

import io.xeounxzxu.springbatchbigdataflowsample.domain.ItemEntity
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Component

@Component
class ZeroOffsetJpaItemReader(
    private val entityManagerFactory: EntityManagerFactory,
) : ItemReader<ItemEntity> {

    private var lastSeenId: Long = 0L

    override fun read(): ItemEntity? {

        val em = entityManagerFactory.createEntityManager()
        val query = em.createQuery(
            """
            SELECT i FROM item i 
            WHERE i.id > :lastId 
            ORDER BY i.id ASC
            """.trimIndent(),
            ItemEntity::class.java
        )
        query.setParameter("lastId", lastSeenId)
        query.maxResults = 1

        val resultList = query.resultList
        em.close()

        if (resultList.isEmpty()) return null

        val item = resultList[0]
        lastSeenId = item.id!! // 다음 기준점 업데이트

        return item
    }
}
