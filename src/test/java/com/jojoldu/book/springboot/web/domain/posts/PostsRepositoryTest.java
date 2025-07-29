package com.jojoldu.book.springboot.web.domain.posts;

import com.jojoldu.book.springboot.domain.posts.Posts;
import com.jojoldu.book.springboot.domain.posts.PostsRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @AfterEach
    public void cleanup(){
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기(){
        // given
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("jojoldu@gmail.com")
                .build());

        // when
        List<Posts> postsList = postsRepository.findAll();

        // then
        Posts posts = postsList.getFirst();
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

    @Transactional
    @Test
    public void BaseTimeEntity_등록() throws Exception{
        // given
        LocalDateTime now = LocalDateTime.of(2025,7,28,0,0,0);
        Posts posts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        System.out.println(">>>>>>>>>>>> createDate=" + posts.getCreateDate() + " modifiedDate=" + posts.getModifiedDateTime());
        Thread.sleep(1000);
        posts.update("test", "test");


        // when
        List<Posts> postsList = postsRepository.findAll();

        // then
        Posts posts1 = postsList.getFirst();

        System.out.println(">>>>>>>>>>>> createDate=" + posts1.getCreateDate() + " modifiedDate=" + posts1.getModifiedDateTime());

        assertThat(posts1.getCreateDate()).isAfter(now);
        assertThat(posts1.getModifiedDateTime()).isAfter(now);
    }
}
