package com.github.tkobayas.drools.warmup.sandbox.testgen;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Simulate SpreadSheet
 */
public class SpreadSheetRuleGen {
    
    static final int INT_COL_NUM = 50;
    static final int STR_COL_NUM = 50;
    
    static final int RULE_NUM = 1000;


    public static void main(String[] args) throws Exception {
        
        StringBuilder builder = new StringBuilder();
        
        builder.append("package com.sample\n");
        builder.append("import com.sample.*\n\n");
        

        
        for (int i = 0; i < RULE_NUM; i++) {
            
            builder.append("rule \"rule" + i + "\"\n");
            builder.append("  when\n");
            builder.append("        pojo : MyPojo(");
            
            for (int intCol = 0; intCol < INT_COL_NUM; intCol++) {
                builder.append("int" + intCol + " <= " + i + ", ");
            }
            
            for (int strCol = 0; strCol < STR_COL_NUM; strCol++) {
                builder.append("str" + strCol + " in ('aaa" + i + "', 'bbb" + i + "', 'ccc" + i + "'), ");
            }
            builder.delete(builder.length() - 2, builder.length() - 1);
            builder.append(")\n");

            builder.append("  then\n");
            builder.append("    System.out.println(\"Rule Hit : \" + kcontext.getRule().getName());\n");
            builder.append("end\n");
            builder.append("\n");
        }
        
        PrintWriter pr = new PrintWriter(new FileWriter(new File("src/test/resources/SpreadSheetTest_"  + INT_COL_NUM + "_" + STR_COL_NUM + "_" + RULE_NUM + "_rules.drl")));
        pr.write(builder.toString());
        pr.close();
        
        System.out.println("finish");
    }
}
