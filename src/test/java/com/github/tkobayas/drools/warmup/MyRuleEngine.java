package com.github.tkobayas.drools.warmup;

import java.util.List;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Message.Level;
import org.kie.api.builder.Results;
import org.kie.api.io.KieResources;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.sample.MyPojo;

/**
 * This is a sample class to launch a rule.
 */
public class MyRuleEngine {

    private KieContainer testKieContainer;

    private KieSession testKieSession;
    
    private String ruleFilePath;
    
    public MyRuleEngine(String ruleFilePath) {
        this.ruleFilePath = ruleFilePath;
    }

    public void init() {

        long initStart = System.currentTimeMillis();

        testKieContainer = createKieContainer(ruleFilePath);

        System.out.println("init() : " + (System.currentTimeMillis() - initStart) + "ms");
    }

    public void simpleWarmUp() {

        long simpleWarmUpStart = System.currentTimeMillis();

        KieSession warmUpKieSession = testKieContainer.newKieSession();
        MyPojo myPojo = new MyPojo();
        warmUpKieSession.insert(myPojo);
        warmUpKieSession.fireAllRules();
        warmUpKieSession.dispose();

        System.out.println("simpleWarmUp() : " + (System.currentTimeMillis() - simpleWarmUpStart) + "ms");
    }

    public void alphaWarmUp(boolean forceMvelJit, boolean forceJVMJit) {

        long alphaWarmUpStart = System.currentTimeMillis();
        
        KieBase kieBase = testKieContainer.getKieBase();
        WarmUpHelper helper = new WarmUpHelper();
        helper.analyze(kieBase, false);
        helper.optimizeAlphaNodeConstraints(forceMvelJit, forceJVMJit);

        System.out.println("alphaWarmUp() : " + (System.currentTimeMillis() - alphaWarmUpStart) + "ms");
    }

    
    public void execTest(Object fact) {

        long execTestStart = System.currentTimeMillis();

        if (getTestKieSession() == null) {
            if (testKieContainer == null) {
                throw new RuntimeException("Call init() first!");
            }
            setTestKieSession(testKieContainer.newKieSession());
        }

        long actualWorkStart = System.currentTimeMillis();

        getTestKieSession().insert(fact);
        getTestKieSession().fireAllRules();
        getTestKieSession().delete(getTestKieSession().getFactHandle(fact));

        System.out.println("  insert/fireAllRules/delete : " + (System.currentTimeMillis() - actualWorkStart) + "ms");

        System.out.println("execTest() : " + (System.currentTimeMillis() - execTestStart) + "ms");

    }

    protected KieContainer createKieContainer(String fileName) {

        long createKieContainerStart = System.currentTimeMillis();

        if (fileName == null || fileName.isEmpty())
            return null;

        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        KieResources kieResources = kieServices.getResources();
        Resource resource = kieResources.newFileSystemResource(fileName);
        kieFileSystem.write(resource);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        verifyKieContainer(kieContainer);

        KieBase kieBase = kieContainer.getKieBase(); // just for internal cache

        System.out.println("  createKieContainer() : " + (System.currentTimeMillis() - createKieContainerStart) + "ms");

        return kieContainer;
    }

    protected void verifyKieContainer(KieContainer kieContainer) {
        Results results = kieContainer.verify();
        if (results.hasMessages(Level.ERROR)) {
            List<Message> erros = results.getMessages(Level.ERROR);
            for (Message error : erros) {
                System.err.println(error.toString());
            }
            throw new RuntimeException("Errors in the rule.");
        }
    }

    public KieSession getTestKieSession() {
        return this.testKieSession;
    }

    public void setTestKieSession(KieSession test) {
        this.testKieSession = test;
    }

    public void disposeTestKieSession() {
        testKieSession.dispose();
        testKieSession = null;
    }
}
