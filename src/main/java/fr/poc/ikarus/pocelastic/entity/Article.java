package fr.poc.ikarus.pocelastic.entity;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Article {
    private String title;
    private String texte;
    private List<String> tags;

    private Author author;

}
