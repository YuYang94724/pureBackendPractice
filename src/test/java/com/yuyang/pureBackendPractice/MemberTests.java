package com.yuyang.pureBackendPractice;

import com.yuyang.pureBackendPractice.member.data.po.MemberPO;
import com.yuyang.pureBackendPractice.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class MemberTests {

    @Autowired
    private MemberService memberService;
    //測試controller打http
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MemberService mockMemberService;

    @Test
    public void test1(){
        MemberPO demo = memberService.query("demo").get();
        Assertions.assertNotNull(demo);
        Assertions.assertEquals("demo", demo.getUsername());
    }

    @Test
    @Transactional
    public void sign(){
        MemberPO memberPO = memberService.login("for", "for").orElse(null);
        Assertions.assertNotNull(memberPO);
        Assertions.assertEquals("for", memberPO.getUsername());
    }

    @Test
    @Transactional
    @DisplayName("測試")
    public void test() throws Exception {
        //登入或註冊
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1.0/members:login")
                .content("{\"username\": \"test\", \"password\": \"test\"}")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, Matchers.notNullValue()))
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, Matchers.containsString("Bearer")))
                .andReturn();
        String token = r.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println(token);
        Assertions.assertEquals("Bearer ", token.substring(0,7));

        //modify about
        RequestBuilder about = MockMvcRequestBuilders
                .patch("/api/v1.0/members:modifyAbout")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"about\": \"testAbout\"}");
        mockMvc.perform(about)
                .andDo(print())
                .andExpect(status().is(204));
    }


    //測試Mockito 不能mock (static、private、final class上述二種方法和final class)
    //Mockito 預設返回的值是原本預設的參數如 int為0, long為0.0, boolean為false
    @Test
    public void markitoTest(){
        MemberPO mockMemberPO = new MemberPO();
        mockMemberPO.setUsername("test");
        mockMemberPO.setPassword("test");
        Mockito.when(mockMemberService.login("test", "test"))
                .thenReturn(Optional.of(mockMemberPO));

        Assertions.assertNotNull(mockMemberPO);
        Assertions.assertEquals("test", mockMemberPO.getUsername());

    }

    @Test
    public void add(){
        Integer v = 1000;
        Integer r = mockMemberService.add(Mockito.anyInt(), Mockito.anyInt());
        Mockito.when(r).thenReturn(v);

        System.out.println(mockMemberService.add(1,2));
        System.out.println(mockMemberService.add(10,21));
        Assertions.assertNotNull(v);
        Assertions.assertEquals(1000, v);
        Assertions.assertTrue(100<mockMemberService.add(1,2));
    }
}
