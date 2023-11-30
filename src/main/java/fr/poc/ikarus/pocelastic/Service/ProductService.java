package fr.poc.ikarus.pocelastic.Service;

import fr.poc.ikarus.pocelastic.Entity.Product;
import fr.poc.ikarus.pocelastic.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Iterable<Product> getProducts(){
        return productRepository.findAll();
    };

    public Product saveProduct(Product product){
        return productRepository.save(product);
    };
    public Product updateProduct(Product product,int id){
        Product product1 = productRepository.findById(id).get();
        product1.setPrice(product.getPrice());
        //Bizarre j'ai pas besoin de save ?
        return product1;
    }
    public void deleteById(int id){
        productRepository.deleteById(id);
    }
}
