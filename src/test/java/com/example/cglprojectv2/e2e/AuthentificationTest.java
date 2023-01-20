package com.example.cglprojectv2.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;


public class AuthentificationTest {
    @BeforeEach
    public void correctPage() {
        open("http://localhost:8080/login");
        $(By.id("username")).should(exist);
        $(By.id("password")).should(exist);
        $(By.xpath("/html/body/div/form/button")).should(exist);
    }

    public static void connect(String user, String password) {
        $(By.id("username")).setValue(user);
        $(By.id("password")).setValue(password);
        $(By.xpath("/html/body/div/form/button")).click();
    }

    @Test
    public void connectionFalse() {
        open("http://localhost:8080/login");
        connect("not", "exist");

        $(By.xpath("/html/body/div/form/div")).should(visible);
    }

    @Test
    public void connectionWithUserRoleTrue() {
        open("http://localhost:8080/login");
        connect("loick.vernet", "user");

        $(By.xpath("//*[@id=\"navbarText\"]/ul/li[1]/a")).shouldHave(href("/apporteur-affaire"));
        $(By.xpath("//*[@id=\"navbarText\"]/ul/li[2]/a")).shouldHave(href("/affaires"));
    }

    @Test
    public void connectionWithAdminRoleTrue() {
        open("http://localhost:8080/login");
        connect("chuck.norris", "admin");

        $(By.xpath("//*[@id=\"navbarText\"]/ul/li[1]/a")).shouldHave(href("/apporteur-affaire"));
        $(By.xpath("//*[@id=\"navbarText\"]/ul/li[2]/a")).shouldHave(href("/affaires"));
        $(By.xpath("//*[@id=\"navbarText\"]/ul/li[3]/a")).shouldHave(href("/parametres"));
        $(By.xpath("//*[@id=\"navbarText\"]/ul/li[4]/a")).shouldHave(href("/statistique"));
    }
}
