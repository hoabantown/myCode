package minhdat.ShopeeAnalysis;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
public class ToolController
{
    @GetMapping("/")
    public String getIndex(Model model)
    {
        String url = "https://shopee.vn/api/v2/search_items/?by=relevancy&keyword=hoa&limit=50&newest=0&order=desc&page_type=search&version=2";

        HttpHeaders headers = new HttpHeaders();
        headers.set("referer", "https://shopee.vn/search?keyword=hoa");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Product> product = restTemplate.exchange(url, HttpMethod.GET, entity, Product.class);

        model.addAttribute("product", product.getBody());

        return "home";
    }

    @GetMapping("/test")
    public String getA(Model model) throws IOException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();

        //get cookie
        HttpEntity<String> loginPage = restTemplate.exchange(
                "https://shopee.vn/api/v0/buyer/login/",
                HttpMethod.GET, null, String.class);
        List<String> cookieLogin = loginPage.getHeaders().getValuesAsList(HttpHeaders.SET_COOKIE);

        //account
        String loginKey = "thenicedream";
        String passwordHash = "b1eaa3bf88da514ddd4613c595f26fffb1483c06233f8e84fae97e53223ce438";

        //header
        HttpHeaders headers = new HttpHeaders();
        headers.set("referer", "https://shopee.vn/api/v0/buyer/login/");
        headers.set("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36");
        headers.set("accept-lan﻿guage", "vi,en-GB;q=0.8,en;q=0.6");
        headers.set("origin﻿", "https://shopee.vn");
        headers.set("x-csrftoken", csrfToken());
        headers.set("cookie", "csrftoken="+csrfToken()+"; "+cookieLogin.toString());

        //params
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("login_key", loginKey);
        params.add("login_type", "username");
        params.add("password_hash", passwordHash);
        params.add("captcha", "");
        params.add("remember_me", "false");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        //send request
        ResponseEntity<String> respone = restTemplate.exchange(
                "https://shopee.vn/api/v0/buyer/login/login_post/",
                HttpMethod.POST, entity, String.class);

        System.out.println(respone.toString());

        //check after login
        HttpEntity<String> test = restTemplate.exchange(
                "https://banhang.shopee.vn/api/v1/login/﻿",
                HttpMethod.GET, null, String.class);

        System.out.println(test.toString());

        return "test";
    }

    public String csrfToken() {
        Random rand = new Random();
        String pattern = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilderCsrf = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            stringBuilderCsrf.append(pattern.charAt(rand.nextInt(pattern.length())));
        }
        return stringBuilderCsrf.toString();
    }
}
