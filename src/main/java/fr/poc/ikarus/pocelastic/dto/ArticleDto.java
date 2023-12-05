package fr.poc.ikarus.pocelastic.dto;

import fr.poc.ikarus.pocelastic.entity.Article;
import lombok.*;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class ArticleDto {
    private Article article;
    private Double score;
}
