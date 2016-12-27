package com.github.tkobayas.drools.warmup.sandbox.testgen;

public class MyPojoGen {

    public static void main(String[] args) throws Exception {
        
        StringBuilder builder = new StringBuilder();
        
        
        for (int i = 0; i < 50; i++) {
            builder.append("    private Integer int" + i + ";\n");
        }
        builder.append("\n");
        
        for (int i = 0; i < 50; i++) {
            builder.append("    private String str" + i + ";\n");
        }
        builder.append("\n");
        
        System.out.println(builder);
    }
}
