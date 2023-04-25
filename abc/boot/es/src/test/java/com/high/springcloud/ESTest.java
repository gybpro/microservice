package com.high.springcloud;

import com.high.springcloud.domain.Goods;
import com.high.springcloud.repository.GoodsRepository;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * @author high
 * @version 1.0
 * @since 1.0
 */
@SpringBootTest
public class ESTest {
    // 简单查询
    @Resource
    private GoodsRepository goodsRepository;

    // 复杂查询
    @Resource
    private ElasticsearchRestTemplate esRestTemplate;

    @Test
    public void teatES() {
        System.out.println(goodsRepository);
        System.out.println(esRestTemplate);
    }

    @Test
    public void testIndexAndMappings() throws Exception {
        // 得到索引信息
        IndexOperations indexOperations = esRestTemplate.indexOps(Goods.class);
        // 创建mappings
        Document mapping = indexOperations.createMapping(Goods.class);
        // 执行mapping
        indexOperations.putMapping(mapping);

        // 从索引里面获取mappings
        Map<String, Object> mappings = indexOperations.getMapping();
        System.out.println(mappings);
    }

    @Test
    public void testSaveAndQuery() {
        // 新增商品数据100条
        ArrayList<Goods> goods = new ArrayList<>(200);
        for (int i = 1; i <= 100; i++) {
            goods.add(
                    new Goods(i,
                            i % 2 == 0 ? "华为电脑" + i : "联想电脑" + i,
                            i % 2 == 0 ? "轻薄笔记本" + i : "游戏笔记本" + i,
                            4999.9 + i,
                            999L,
                            i % 2 == 0 ? "华为续航强" : "联想性能强",
                            i % 2 == 0 ? 1 : 0,
                            666 + i,
                            i % 2 == 0 ? Arrays.asList("小巧", "轻薄", "续航") : Arrays.asList("炫酷", "畅玩", "游戏"),
                            new Date())
            );
        }
        goodsRepository.saveAll(goods);

        goodsRepository.findAll().forEach(System.out::println);
    }

    @Test
    public void testESSearch() {
        // 复杂查询
        // 创建查询建造者
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // 查询关键字
        String keyword = "华为";

        // 构建查询条件
        // 模糊查询
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("goodsName", keyword);
        // queryBuilder.withQuery(matchQuery);

        // 范围查询
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("goodsStock").from(700).to(750);
        // queryBuilder.withQuery(rangeQuery);

        // 多条件查询：模糊查询+范围查询
        queryBuilder.withQuery(
                QueryBuilders.boolQuery().must(matchQuery).must(rangeQuery)
        );

        // 分页查询
        queryBuilder.withPageable(
                // 第一页从0开始查询
                PageRequest.of(0, 10)
        );

        // 分组查询
        queryBuilder.withHighlightFields(
                new HighlightBuilder.Field("goodsName")
                        .preTags("<i style='color: red'>")
                        .postTags("</i>")
        );

        // 排序查询
        queryBuilder.withSort(
                // 按价格倒序排序
                SortBuilders.fieldSort("goodsPrice").order(SortOrder.DESC)
        );


        // 复杂查询操作
        SearchHits<Goods> search = esRestTemplate.search(queryBuilder.build(), Goods.class);

        // 输出查询结果
        System.out.println("查询总记录数：" + search.getTotalHits());
        search.getSearchHits().forEach(hit -> {
            Goods goods = hit.getContent();

            goods.setGoodsName(hit.getHighlightField("goodsName").get(0));

            System.out.println(goods);
        });
    }

}
