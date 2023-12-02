package fr.poc.ikarus.pocelastic.controller;

import fr.poc.ikarus.pocelastic.elasticClient.ElasticClient;
import fr.poc.ikarus.pocelastic.entity.Article;
import fr.poc.ikarus.pocelastic.entity.Author;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ElasticClient elasticClient ;
    @GetMapping("/{titre}")
    private ResponseEntity<List<Article>> getArticlesByTitle(@PathVariable("titre") String titre) throws IOException {
        System.out.println("titre = "+titre);
        return new ResponseEntity<>(elasticClient.findArticleByTitle(titre), HttpStatus.OK);

    }
    @GetMapping("/")
    private ResponseEntity<List<Article>> getEveryArticle() throws IOException {
        return new ResponseEntity<>(elasticClient.getAllArticles(),HttpStatus.OK);
        }
}
