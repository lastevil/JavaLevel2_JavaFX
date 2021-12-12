package com.example.demo.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class APP01Test {
    private APP01 app01;
    @BeforeEach
    public void init(){
        app01=new APP01();
    }
    @Test
    public void massTransformTest(){
        Assertions.assertArrayEquals(new int[]{},app01.massTransform(new int[]{1, 2, 3, 4}));

    }

    @Test
    public void massCheckTest(){
        Assertions.assertEquals(false,app01.massCheck(new int[]{1, 2, 3, 4}));
    }

}