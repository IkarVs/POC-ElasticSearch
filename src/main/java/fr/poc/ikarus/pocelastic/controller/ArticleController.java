package fr.poc.ikarus.pocelastic.controller;

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

    @GetMapping("/{titre}")
    private ResponseEntity<List<Article>> getArticlesByTitle(@PathVariable("titre") String titre) throws IOException {
        System.out.println("titre = "+titre);
        return new ResponseEntity<>(elasticClient.findArticleByTitle(titre), HttpStatus.OK);

    }
    @GetMapping("/")
    private ResponseEntity<List<Article>> getEveryArticle() throws IOException {
        return new ResponseEntity<>(elasticClient.getAllArticles(),HttpStatus.OK);
    }
    @GetMapping("/2/{titre}")
    private ResponseEntity<List<ArticleDto>> getArticlesByTitle2(@PathVariable("titre") String titre) throws IOException {
        System.out.println("titre = "+titre);
        return new ResponseEntity<>(elasticClient.findArticleByTitleV2(titre), HttpStatus.OK);

    }
    @PostMapping("/")
    private ResponseEntity<String> addArticle(@RequestBody Article article) throws IOException {
        elasticClient.addArticle(article);
        return new ResponseEntity<>("ajout réussi",HttpStatus.OK);
    }

    @GetMapping("/texte")
    private ResponseEntity<List<ArticleDto>> getArticlesByText(@RequestParam String texte) throws IOException {
        System.out.println("texte = "+texte);
        return new ResponseEntity<>(elasticClient.findArticleByText(texte), HttpStatus.OK);

    }
}
