package fr.poc.ikarus.pocelastic.elasticClient;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import fr.poc.ikarus.pocelastic.dto.ArticleDto;
import fr.poc.ikarus.pocelastic.entity.Article;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        System.out.println("indexed with version : "+ indexResponse.version());

    }
    public List<Article> findArticleByMatchingTitle(String titre) throws IOException {
        MatchQuery matchQuery = new MatchQuery.Builder().field("title").query(titre).build();
        SearchResponse<Article> search = elasticsearchClient
                .search(s->s
                .index("article")
                                .query(q->q.match(matchQuery)
                                        )
                        ,Article.class);

        List<Article> listeArticle = new ArrayList<>();
        for (Hit<Article> hit: search.hits().hits()) {
            System.out.println(" élément que j'ai obtenue : "+ hit.source());
            listeArticle.add(hit.source());
        }
        return listeArticle;
    }
    public List<Article> getAllArticles() throws IOException {
        SearchResponse<Article> searchAllTest = elasticsearchClient.search(s->s.index("article"),Article.class);
        List<Article> listeArticle = new ArrayList<>();
        for (Hit<Article> hit: searchAllTest.hits().hits()) {
            System.out.println(" élément que j'ai obtenue : "+ hit.source());
            listeArticle.add(hit.source());
        }
        return listeArticle;
    }
    public List<ArticleDto> findArticleByTitleV2(String titre) throws IOException {
        // il faudrais découper le string avec les espaces
        String[] testList = StringUtils.delimitedListToStringArray(titre," ");

        SearchResponse<Article> search = elasticsearchClient
                .search(s->s
                                .index("article")
                                .query(q->q
                                        .term(t->{
                                            for (String str: testList) {
                                                System.out.println("j'ajoute cette string : "+str);
                                                if(str!=null&&!str.isEmpty()){
                                                    t.field("title").value(str);
                                                }
                                            }
                                            return t;
                                        }))
                        ,Article.class);
        List<ArticleDto> articleDtoList = new ArrayList<>();
        for (Hit<Article> hit: search.hits().hits()) {
            System.out.println(" élément que j'ai obtenue : "+ hit.source());
            ArticleDto articleDto = new ArticleDto(hit.source(),hit.score());
            articleDtoList.add(articleDto );
        }
        return articleDtoList;
    }
    public List<ArticleDto> findArticleByText(String texte) throws IOException {
        // il faudrais découper le string avec les espaces
        String[] testList = StringUtils.delimitedListToStringArray(texte," ");

        SearchResponse<Article> search = elasticsearchClient
                .search(s->s
                                .index("article")
                                .query(q->q
                                        .term(t->{
                                            for (String str: testList) {
                                                System.out.println("j'ajoute cette string : "+str);
                                                if(str!=null&&!str.isEmpty()){
                                                    t.field("texte").value(str);
                                                }
                                            }
                                            return t;
                                        }))
                        ,Article.class);
        System.out.println(" la recherche en toString donne : "+ search.toString());
        TotalHits total = search.hits().total();
        List<ArticleDto> articleDtoList = new ArrayList<>();
        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;
        System.out.println("j'obtiens ce boolean pour exact result : "+isExactResult+" en prime je souhaite print ce que total.relation me renvoie "+ total.relation());
        for (Hit<Article> hit: search.hits().hits()) {
            System.out.println(" élément que j'ai obtenue : "+ hit.source());
            ArticleDto articleDto = new ArticleDto(hit.source(),hit.score());
            articleDtoList.add(articleDto );
        }
        return articleDtoList;
    }

    public List<ArticleDto> findArticleByTextV2(String texte) throws IOException {
        MatchQuery matchQuery = new MatchQuery.Builder().field("texte").query(texte).build();
        SearchResponse<Article> search = elasticsearchClient
                .search(s->s
                                .index("article")
                                .query(q->q.match(matchQuery))
                        ,Article.class);
        List<ArticleDto> articleDtoList = new ArrayList<>();
        for (Hit<Article> hit: search.hits().hits()) {
            System.out.println(" élément que j'ai obtenue : "+ hit.source());
            ArticleDto articleDto = new ArticleDto(hit.source(),hit.score());
            articleDtoList.add(articleDto );
        }
        return articleDtoList;
    }
    public List<ArticleDto> findArticleByTextandTitle(String texte,String title) throws IOException {
        Query matchQueryText = new MatchQuery.Builder().field("texte").query(texte).build()._toQuery();
        Query matchQueryTitle = new MatchQuery.Builder().field("title").query(title).build()._toQuery();
        List<Query> test = new ArrayList<>();
        test.add(matchQueryTitle);
        test.add(matchQueryText);

        SearchResponse<Article> search = elasticsearchClient
                .search(s->s
                                .index("article")
                                .query(q->{
                                    q.bool(b->b.must(test));
                                return q;})
                        ,Article.class);
        TotalHits total = search.hits().total();
        List<ArticleDto> articleDtoList = new ArrayList<>();
        for (Hit<Article> hit: search.hits().hits()) {
            System.out.println(" élément que j'ai obtenue : "+ hit.source());
            ArticleDto articleDto = new ArticleDto(hit.source(),hit.score());
            articleDtoList.add(articleDto );
        }
        return articleDtoList;
    }
    public List<ArticleDto> findArticleByTagandTitle(String texte,String[] title) throws IOException {
        Query matchQueryText = new MatchQuery.Builder().field("texte").query(texte).build()._toQuery();
        List<Query> test = new ArrayList<>();
        for (String str: title
             ) {
            Query matchQueryTags = new MatchQuery.Builder().field("tags").query(str).build()._toQuery();
            test.add(matchQueryTags);
        }
        //Query matchQueryTags = new MatchQuery.Builder().field("tagd").query(title).build()._toQuery();
        //Query matchQueryTitle = new MatchQuery.Builder().field("title").query(title).build()._toQuery();


        test.add(matchQueryText);

        SearchResponse<Article> search = elasticsearchClient
                .search(s->s
                                .index("article")
                                .query(q->{
                                    q.bool(b->b.must(test));
                                    return q;})
                        ,Article.class);
        List<ArticleDto> articleDtoList = new ArrayList<>();
        for (Hit<Article> hit: search.hits().hits()) {
            System.out.println(" élément que j'ai obtenue : "+ hit.source());
            ArticleDto articleDto = new ArticleDto(hit.source(),hit.score());
            articleDtoList.add(articleDto );
        }
        return articleDtoList;
    }

    public void deleteArticle(String titre) throws IOException {
        DeleteRequest deleteRequest = DeleteRequest.of(t-> t.index("article").id(titre));
        elasticsearchClient.delete(deleteRequest);
    }


}
