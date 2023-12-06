package fr.poc.ikarus.pocelastic.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import fr.poc.ikarus.pocelastic.dto.ArticleDto;
import fr.poc.ikarus.pocelastic.elasticClient.ElasticClient;
import fr.poc.ikarus.pocelastic.entity.Article;
import fr.poc.ikarus.pocelastic.entity.Author;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.websocket.server.PathParam;
import org.hibernate.validator.constraints.ModCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ElasticClient elasticClient ;
    @GetMapping("/")
    private ResponseEntity<List<Article>> getEveryArticle() throws IOException {
        return new ResponseEntity<>(elasticClient.getAllArticles(),HttpStatus.OK);
    }
    @PostMapping("/")
    private ResponseEntity<String> addArticle(@RequestBody Article article) throws IOException {
        elasticClient.addArticle(article);
        return new ResponseEntity<>("ajout réussi",HttpStatus.OK);
    }
    @PostMapping("/adds")
    private ResponseEntity<String> addArticles(@RequestBody List<Article> articles) throws IOException {

        for (Article article:articles
             ) {
            System.out.println("j'ajoute cette article : "+article.toString());
            elasticClient.addArticle(article);
        }
        return new ResponseEntity<>("ajout des documents réussi",HttpStatus.OK);
    }

    @GetMapping("/{titre}")
    private ResponseEntity<List<Article>> getArticlesByTitle(@PathVariable("titre") String titre) throws IOException {
        return new ResponseEntity<>(elasticClient.findArticleByMatchingTitle(titre), HttpStatus.OK);

    }
    @GetMapping("/2/{titre}")
    private ResponseEntity<List<ArticleDto>> getArticlesByTitle2(@PathVariable("titre") String titre) throws IOException {
        return new ResponseEntity<>(elasticClient.findArticleByTitleV2(titre), HttpStatus.OK);

    }


    @GetMapping("/texte")
    private ResponseEntity<List<ArticleDto>> getArticlesByText(@RequestParam String texte) throws IOException {
        System.out.println("texte = "+texte);
        return new ResponseEntity<>(elasticClient.findArticleByText(texte), HttpStatus.OK);

    }
    @GetMapping("/2/texte")
    private ResponseEntity<List<ArticleDto>> getArticlesByTextV2(@RequestParam String texte) throws IOException {
        System.out.println("texte = "+texte);
        return new ResponseEntity<>(elasticClient.findArticleByTextV2(texte), HttpStatus.OK);

    }

    @GetMapping("/texteandtitle")
    private ResponseEntity<List<ArticleDto>> getArticlesByTextAndTitle(@RequestParam String texte,@RequestParam String titre) throws IOException {
        System.out.println("texte = "+texte);
        return new ResponseEntity<>(elasticClient.findArticleByTextandTitle(texte,titre), HttpStatus.OK);

    }
    @GetMapping("/tagsandtags")
        private ResponseEntity<List<ArticleDto>> getArticlesByTagesAndTitle(@RequestParam String titre,@RequestParam String[] tags) throws IOException {
        return new ResponseEntity<>(elasticClient.findArticleByTagandTitle(titre,tags), HttpStatus.OK);

    }
    @DeleteMapping("/{titre}")
    private ResponseEntity<String> deleteByTitle(@PathVariable String titre) throws IOException {
        elasticClient.deleteArticle(titre);
        return new ResponseEntity<>("document de titre "+titre+" supprimé",HttpStatus.OK);
    }

}
