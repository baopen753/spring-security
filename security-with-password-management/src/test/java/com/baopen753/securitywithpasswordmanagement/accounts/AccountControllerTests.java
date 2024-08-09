package com.baopen753.securitywithpasswordmanagement.accounts;

import com.baopen753.securitywithpasswordmanagement.controller.AccountController;
import com.baopen753.securitywithpasswordmanagement.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AccountController.class)
public class AccountControllerTests {

    private static final String ENDPOINT = "/accounts";
    private static final String REQUEST_CONTENT_TYPE = "application/json";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyUserDetailsService userDetailsService;
//
//    @Test
//    public void testLoadUserByUsername() throws Exception {
//        String existedUsername = "baopen753@gmail.com";
//        UserDetails userDetails = userDetailsService.loadUserByUsername(existedUsername);
//
//        Mockito.when(userDetailsService.loadUserByUsername(existedUsername)).thenReturn(userDetails);
//        mockMvc.perform(get(ENDPOINT))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }
}
