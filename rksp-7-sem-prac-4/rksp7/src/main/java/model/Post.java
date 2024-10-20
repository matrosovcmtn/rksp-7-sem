package model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("posts")
public class Post {
    @Id
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime date;
}
