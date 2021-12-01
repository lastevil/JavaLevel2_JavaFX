package com.example.demo;

import java.io.*;

public class History{
    private static File file;

    public static void writeFile(String s) {
            try (BufferedWriter out = new BufferedWriter(new FileWriter(file, true))) {
                out.write(s+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static String readFile(){
              int x=1;
        StringBuilder str = new StringBuilder();
        if (file.exists()){
            try (BufferedReader in = new BufferedReader(new FileReader(file))) {
                                while((in.readLine())!=null || in.read()!=-1){
                    x++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (BufferedReader in = new BufferedReader(new FileReader(file))){
                String line;
                while((line=in.readLine())!=null || in.read()!=-1){
                    x--;
                    if(x<=100 && x>0){
                        str.append(line+"\n");
                    }
                }
                str.append("---\\end history\\---");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return str.toString();
        }
       return "";

    }


    public static void setFileName(String fileName){
        file = new File("hist_"+fileName+".log");
    }
    public static String getFileName(){
        return file.getName();
    }
}
