package com.hnu.fk.controller;

import com.hnu.fk.FkApplication;
import com.hnu.fk.domain.Department;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 11:07 2018/8/1
 * @Modified By:
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FkApplication.class)
@WebAppConfiguration
public class DepartmentControllerTest {

    private MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        // 获取mockMvc实例
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void add() throws Exception {
        String uri = "/department/add";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .param("name", "生产部");
        MvcResult mvcResult = mockMvc.perform(builder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        Assert.assertTrue(String.format("【%s】状态码错误【%s】", uri, status), status == 200);
        System.out.println(String.format("返回值: %s", content));
    }

    @Test
    public void update() throws Exception {
        String uri = "/department/update";

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .param("id", "6")
                .param("name", "营销部");
        MvcResult mvcResult = mockMvc.perform(builder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        Assert.assertTrue(String.format("【%s】状态码错误【%s】", uri, status), status == 200);
        System.out.println(String.format("返回值: %s", content));
    }

    @Test
    public void deleteById() throws Exception {
        String uri = "/department/deleteById";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .param("id", "6");
        MvcResult mvcResult = mockMvc.perform(builder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        Assert.assertTrue(String.format("【%s】状态码错误【%s】", uri, status), status == 200);
        System.out.println(String.format("返回值: %s", content));
    }

    @Test
    public void deleteByIds() throws Exception {
        String uri = "/department/deleteByIds";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .param("ids", "1", "2", "3");
        MvcResult mvcResult = mockMvc.perform(builder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        Assert.assertTrue(String.format("【%s】状态码错误【%s】", uri, status), status == 200);
        System.out.println(String.format("返回值: %s", content));
    }

    @Test
    public void getById() throws Exception {
        String uri = "/department/getById";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .param("id", "5");
        MvcResult mvcResult = mockMvc.perform(builder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        Assert.assertTrue(String.format("【%s】状态码错误【%s】", uri, status), status == 200);
        System.out.println(String.format("返回值: %s", content));
    }

    @Test
    public void getAll() throws Exception {
        String uri = "/department/getAll";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(builder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        Assert.assertTrue(String.format("【%s】状态码错误【%s】", uri, status), status == 200);
        System.out.println(String.format("返回值: %s", content));
    }

    @Test
    public void getAllByPage() throws Exception {
        String uri = "/department/getAllByPage";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .param("name", "生产部");
        MvcResult mvcResult = mockMvc.perform(builder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        Assert.assertTrue(String.format("【%s】状态码错误【%s】", uri, status), status == 200);
        System.out.println(String.format("返回值: %s", content));
    }

    @Test
    public void getByNameLikeByPage() throws Exception {
        String uri = "/department/getByNameLikeByPage";
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .param("name", "生产部");
        MvcResult mvcResult = mockMvc.perform(builder).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        Assert.assertTrue(String.format("【%s】状态码错误【%s】", uri, status), status == 200);
        System.out.println(String.format("返回值: %s", content));
    }
}