package fr.poc.ikarus.pocelastic.elasticClient;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

/*
* Cette classe englobe le client d'elasticsearch permettant d'utiliser cette classe comme une sorte de service
*
* */
public class ElasticClient {
    private ElasticsearchClient elasticsearchClient;

    public ElasticClient() {
        String serverUrl = "https://localhost:9200";
        RestClient restClient = RestClient.builder(HttpHost.create(serverUrl)).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        this.elasticsearchClient = new ElasticsearchClient(transport);
    }


}
