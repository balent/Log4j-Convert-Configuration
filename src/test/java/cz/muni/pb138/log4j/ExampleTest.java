package cz.muni.pb138.log4j;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExampleTest {

    @BeforeClass
    public static void beforeClassExample() {
        System.out.println("Before Class");
    }

    @AfterClass
    public static void afterClassExample() {
        System.out.println("After Class");
    }

    @Before
    public void setUp() {
        System.out.println("Before Unit Test");
    }

    @After
    public void tearDown() {
        System.out.println("After Unit Test");
    }

    @Test
    public void exampleTest1() {
        assertTrue(true);
        System.out.println("Example Unit Test 1");
    }

    @Test
    public void exampleTest2() {
        assertTrue(true);
        System.out.println("Example Unit Test 2");
    }
}
