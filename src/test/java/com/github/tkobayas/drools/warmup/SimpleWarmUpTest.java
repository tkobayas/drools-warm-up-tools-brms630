package com.github.tkobayas.drools.warmup;

import org.junit.Test;

import com.sample.MyPojo;

public class SimpleWarmUpTest {
    @Test
    public void testPerformance() {

        MyRuleEngine ruleEngine = new MyRuleEngine("src/test/resources/SpreadSheetTest_50_50_1000_rules.drl");

        System.out.println("--- init");
        ruleEngine.init();
        
        System.out.println("--- simpleWarmUp");
        ruleEngine.simpleWarmUp();
        
        MyPojo myPojo = new MyPojo(
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
                "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", 
                "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", 
                "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", 
                "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", 
                "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2");

        System.out.println("--- 1st run");
        ruleEngine.execTest(myPojo);

        System.out.println("--- 2nd run");
        ruleEngine.execTest(myPojo);

        System.out.println("--- 3rd run");
        ruleEngine.execTest(myPojo);

        ruleEngine.disposeTestKieSession();
    }
}
