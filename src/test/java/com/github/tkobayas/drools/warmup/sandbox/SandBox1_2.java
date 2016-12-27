package com.github.tkobayas.drools.warmup.sandbox;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;

import com.github.tkobayas.drools.warmup.WarmUpHelper;

/**
 * Not JUnit TestCase at this moment
 */
public class SandBox1_2 {

    public static final void main(String[] args) {
        try {

            System.setProperty("drools.dump.dir", "/home/tkobayas/tmp");

            KieServices ks = KieServices.Factory.get();
            KieFileSystem kfs = ks.newKieFileSystem();
            kfs.write("src/main/resources/Sample1.drl", ks.getResources().newClassPathResource("Sample1.drl"));
            ks.newKieBuilder( kfs ).buildAll();
            KieContainer kContainer = ks.newKieContainer(ks.getRepository().getDefaultReleaseId());
            KieBase kbase = kContainer.getKieBase();
            
            WarmUpHelper helper = new WarmUpHelper();
            helper.analyze(kbase, true);
            helper.optimizeAlphaNodeConstraints(false, false);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
