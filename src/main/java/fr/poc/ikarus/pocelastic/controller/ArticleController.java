package fr.poc.ikarus.pocelastic.controller;

import fr.poc.ikarus.pocelastic.elasticClient.ElasticClient;
import fr.poc.ikarus.pocelastic.entity.Article;
import fr.poc.ikarus.pocelastic.entity.Author;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("/")
    private ResponseEntity<List<Article>> getArticles(@PathParam("titre") String titre) throws IOException {
        Article article = new Article("test","test",new ArrayList<>(),new Author("testo","testtt"));
        elasticClient.addArticle(article);
        System.out.println("HEHOOOO");
//        return new ResponseEntity<>(null,HttpStatus.OK);
        return new ResponseEntity<>(elasticClient.findArticleByTitle(titre), HttpStatus.OK);

    }
}
