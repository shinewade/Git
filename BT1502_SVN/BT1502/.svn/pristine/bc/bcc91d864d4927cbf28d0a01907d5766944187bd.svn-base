package BT1502_pro_tml.MEMORY;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import xoc.dta.TestMethod;

public class uart_dyn extends TestMethod {

    public final ArrayList<String[]> getRegFromFile(String filename){
        ArrayList<String[]> regList = new ArrayList<String[]>();
        @SuppressWarnings("resource")
        BufferedReader br = null;
        String line = "";
        String separator = " ";//use comma as column separator
        try {
            br = new BufferedReader(new FileReader(filename));
            long lineCounter = 7;
            while(lineCounter-->0) {
              br.readLine();
            }
            while ((line = br.readLine())!= null) {
                String regNameTemp = line.replaceAll("  ", " ");
//                System.out.println(regNameTemp);
                String[] regName = regNameTemp.split(separator);
                if(regName.length >0) {
                    regList.add(regName);
                }
            }
        }catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("ERROR: filename "+filename+" doesn't exist!");
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(br != null) {
                try {
                    br.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return regList;
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub

    }
}
