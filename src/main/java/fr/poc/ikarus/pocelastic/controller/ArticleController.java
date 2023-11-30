package fr.poc.ikarus.pocelastic.controller;

import fr.poc.ikarus.pocelastic.elasticClient.ElasticClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ArticleController {
    @Autowired
    private ElasticClient elasticClient;

}
