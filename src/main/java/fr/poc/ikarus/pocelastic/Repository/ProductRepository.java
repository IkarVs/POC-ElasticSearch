package fr.poc.ikarus.pocelastic.Repository;

import fr.poc.ikarus.pocelastic.Entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/*
* Cette interface est un repository ElasticSearch<class,type de l'id de la class (ici Integer)
* */
public interface ProductRepository extends ElasticsearchRepository<Product,Integer> {
}
