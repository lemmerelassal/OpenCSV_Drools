package com.lemmer.csvexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import com.lemmer.csvexample.data.model.InvoiceItem;
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.drools.compiler.compiler.*;
import org.drools.compiler.compiler.PackageBuilder;
import org.drools.core.RuleBase;
import org.drools.core.RuleBaseFactory;
import org.drools.core.WorkingMemory;


@SpringBootApplication
public class CsvexampleApplication {

    public static void main(String[] args) throws IOException {
        
        if(args.length < 2) {
            System.out.println("Usage:\n\tCsvexampleApplication <input.csv> <output.csv>");
            return;
        }
        
    //		SpringApplication.run(CsvexampleApplication.class, args);
        CSVReader reader = new CSVReader(new FileReader(args[0]), ',');
        CSVWriter writer = new CSVWriter(new FileWriter(args[1]), ',', CSVWriter.NO_QUOTE_CHARACTER);
        InvoiceItem temp;
        String[] nextLine;
        BigDecimal subtotal = new BigDecimal(0);
        CsvexampleApplication myCtx = new CsvexampleApplication();
        while ((nextLine = reader.readNext()) != null) {
           if (nextLine != null) {
               temp = new InvoiceItem(nextLine[0], Integer.parseInt(nextLine[1].trim()));
               try {
                   temp = myCtx.executeDrools(temp);
               } catch (DroolsParserException ex) {
                   Logger.getLogger(CsvexampleApplication.class.getName()).log(Level.SEVERE, null, ex);
               }
               writer.writeNext(temp.toCsvStrings());
               subtotal = subtotal.add(temp.getTotal());
           }
         }
        nextLine = new String[1];
        nextLine[0] = subtotal.toString();
        writer.writeNext(nextLine);
        writer.close();
    }

    public InvoiceItem executeDrools(InvoiceItem myItem) throws DroolsParserException, IOException {

        PackageBuilder packageBuilder = new PackageBuilder();

        String ruleFile = "/com/rule/Rules.drl";
        InputStream resourceAsStream = getClass().getResourceAsStream(ruleFile);

        Reader reader = new InputStreamReader(resourceAsStream);
        packageBuilder.addPackageFromDrl(reader);
        org.drools.core.rule.Package rulesPackage = packageBuilder.getPackage();
        RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        ruleBase.addPackage(rulesPackage);

        WorkingMemory workingMemory = ruleBase.newStatefulSession();

        workingMemory.insert(myItem);
        workingMemory.fireAllRules();

        return myItem;
    }
    
    
}

