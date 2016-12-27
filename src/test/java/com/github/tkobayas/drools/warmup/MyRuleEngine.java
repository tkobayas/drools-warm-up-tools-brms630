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

    public void init() {

        long initStart = System.currentTimeMillis();

        testKieContainer = createKieContainer("src/test/resources/SpreadSheetTest_50_50_1000_rules.drl");

        System.out.println("init() : " + (System.currentTimeMillis() - initStart) + "ms");
    }

    public void warmUp() {

        long warmUpStart = System.currentTimeMillis();

        KieSession warmUpKieSession = testKieContainer.newKieSession();
        MyPojo myPojo = new MyPojo();
        warmUpKieSession.insert(myPojo);
        warmUpKieSession.fireAllRules();
        warmUpKieSession.dispose();

        //        KieBase kieBase = testKieContainer.getKieBase();        

        System.out.println("warmUp() : " + (System.currentTimeMillis() - warmUpStart) + "ms");
    }

    public void execTest() {

        long execTestStart = System.currentTimeMillis();

        if (getTestKieSession() == null) {
            if (testKieContainer == null) {
                throw new RuntimeException("Call init() first!");
            }
            setTestKieSession(testKieContainer.newKieSession());
        }

        MyPojo myPojo = new MyPojo(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2",
                "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2",
                "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2", "aaa2");

        long actualWorkStart = System.currentTimeMillis();

        getTestKieSession().insert(myPojo);
        getTestKieSession().fireAllRules();
        getTestKieSession().delete(getTestKieSession().getFactHandle(myPojo));

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
