package com.high.springcloud.repository;

import com.high.springcloud.domain.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}
