package com.github.tkobayas.drools.warmup;

import org.junit.Test;

public class SpreadSheetTest {
    @Test
    public void testPerformance() {

        MyRuleEngine ruleEngine = new MyRuleEngine();

        System.out.println("--- init");
        ruleEngine.init();
        
        System.out.println("--- warmUp");
        ruleEngine.warmUp();

        System.out.println("--- 1st run");
        ruleEngine.execTest();

        System.out.println("--- 2nd run");
        ruleEngine.execTest();

        System.out.println("--- 3rd run");
        ruleEngine.execTest();

        ruleEngine.disposeTestKieSession();
    }
}
