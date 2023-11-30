package fr.poc.ikarus.pocelastic.Controller;

import fr.poc.ikarus.pocelastic.Entity.Product;
import fr.poc.ikarus.pocelastic.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apis")
public class ProductController {
    @Autowired
    private ProductService productService;
    //Mettre des response Entity peut être plus propre :)
    @GetMapping("/products")
    Iterable<Product> findAll(){
        return productService.getProducts();
    }
    @PostMapping("/insert")
    public Product insertProduct(@RequestBody Product product){
        return productService.saveProduct(product);
    }
}
