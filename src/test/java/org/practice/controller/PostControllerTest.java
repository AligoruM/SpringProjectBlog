package org.practice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.practice.model.PostDao;
import org.practice.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostRepository jdbcPostRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("delete from posts");
        jdbcTemplate.update("insert into posts (title, text, likes) values ('Prepopulated post 1', 'Prepopulated text 1', 1);");
        jdbcTemplate.update("insert into posts (title, text, likes) values ('Prepopulated post 2', 'Prepopulated text 2', 2);");
        jdbcTemplate.update("insert into posts (title, text, likes) values ('Prepopulated post 3', 'Prepopulated text 3', 3);");
    }

    @Test
    void posts() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(xpath("//html/body/table/tr").nodeCount(4))
                .andExpect(xpath("//html/body/table/tr[2]/td[1]/h2/a").string("Prepopulated post 1"))
                .andExpect(xpath("//html/body/table/tr[3]/td[1]/h2/a").string("Prepopulated post 2"))
                .andExpect(xpath("//html/body/table/tr[4]/td[1]/h2/a").string("Prepopulated post 3"));
    }

    @Test
    void addPostPage_AddPost_ShouldShowNewPostPage() throws Exception {
        mockMvc.perform(get("/posts/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-post"))
                .andExpect(xpath("//html/body/form").nodeCount(1));
    }

    @Test
    void createPost_AddPost_ShouldRedirectToNewPost() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/posts")
                        .param("title", "Test Title 4")
                        .param("text", "Test text 4")
                        .param("rawTags", "testTag #testTag2"))
                .andExpect(status().is3xxRedirection());

        PostDao lastPost = getLastPost();

        resultActions
                .andExpect(redirectedUrl("/posts/" + lastPost.getId()))
                .andExpect(view().name("redirect:/posts/" + lastPost.getId()));

        mockMvc.perform(get("/posts/" + lastPost.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(xpath("//html/body/table/tr").nodeCount(5))
                .andExpect(xpath("//html/body/table/tr[2]/td[1]/h2").string("Test Title 4"))
                .andExpect(xpath("//html/body/table/tr[2]/td[1]/p[3]/span").nodeCount(2))
                .andExpect(xpath("//html/body/table/tr[2]/td[1]/p[3]/span[1]").string("#testTag "))
                .andExpect(xpath("//html/body/table/tr[2]/td[1]/p[3]/span[2]").string("#testTag2 "));
    }

    //Workaround to get last post
    private PostDao getLastPost() {
        Long count = jdbcPostRepository.count();
        List<PostDao> pagedPosts = jdbcPostRepository.getPagedPosts(count - 1, 1);
        return pagedPosts.isEmpty() ? null : pagedPosts.getFirst();
    }
}