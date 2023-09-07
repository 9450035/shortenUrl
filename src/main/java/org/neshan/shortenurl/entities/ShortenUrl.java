package org.neshan.shortenurl.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortenUrl {
    @Id
    @GeneratedValue
    private Long id;
    private String realUrl;
    private String shortUrl;
    private Long createTime;
    private Long view;
    @ManyToOne
    private User user;
}
