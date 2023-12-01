package fr.poc.ikarus.pocelastic.elasticClient;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import fr.poc.ikarus.pocelastic.entity.Article;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
* Cette classe englobe le client d'elasticsearch permettant d'utiliser cette classe comme une sorte de service
*
* */
@Service
public class ElasticClient {
    private ElasticsearchClient elasticsearchClient;

    public ElasticClient() {
        String serverUrl = "http://localhost:9200";
        RestClient restClient = RestClient
                .builder(HttpHost.create(serverUrl))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Athorization","ApiKey test")
                })
                /*.setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + "test")
                })*/
                .build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        this.elasticsearchClient = new ElasticsearchClient(transport);
    }

    public void addArticle(Article article) throws IOException{
        IndexResponse indexResponse =elasticsearchClient.index(i-> i
                .index("article")
                .id(article.getTitle())
                .document(article)
        );

    }
    public List<Article> findArticleByTitle(String titre) throws IOException {
        SearchResponse<Article> search = elasticsearchClient
                .search(s->s
                .index("article")
                                .query(q->q
                                        .term(t->t
                                                .field("titre")
                                                .value(v->v.stringValue("titre"))))
                                                //.value(v->v.stringValue(titre))))
                        ,Article.class);
        List<Article> listeArticle = new ArrayList<>();
        for (Hit<Article> hit: search.hits().hits()) {
            listeArticle.add(hit.source());
        }
        return listeArticle;
    }

}
