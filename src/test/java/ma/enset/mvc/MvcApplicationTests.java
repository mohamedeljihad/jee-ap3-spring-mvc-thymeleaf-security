package ma.enset.mvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MvcApplicationTests {
    @Autowired
    MockMvc mvc;

    @Test
    void anonymousIsRedirectedToLogin() throws Exception {
        mvc.perform(get("/user/index")).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCanListProducts() throws Exception {
        mvc.perform(get("/user/index")).andExpect(status().isOk()).andExpect(view().name("products"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCannotAccessAdminPages() throws Exception {
        mvc.perform(get("/admin/newProduct")).andExpect(status().isForbidden());
    }
}
