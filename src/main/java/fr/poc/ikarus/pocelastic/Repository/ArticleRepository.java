package fr.poc.ikarus.pocelastic.Repository;

import org.springframework.data.domain.Page;
import fr.poc.ikarus.pocelastic.Entities.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article,String> {
    Page<Article> findByAuthorsName(String name, Pageable pageable);
    @Query("{\"bool\": {\"must\": [{\"match\": {\"authors.name\": \"?0\"}}]}}")
    Page<Article> findByAuthorsNameUsingCustomQuery(String name, Pageable pageable);
}
