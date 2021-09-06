package org.example;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest{
    @Test
    public void test01(){
        ClassLoader loader = new MyClassLoader("D:\\soft\\addmavenjar\\bigdata-1.0-SNAPSHOT\\hoau\\com\\cn\\service\\reducer\\");
        try {
            Class<?> c =  loader.loadClass("StationDetailReducer");
            System.out.println(c.getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
