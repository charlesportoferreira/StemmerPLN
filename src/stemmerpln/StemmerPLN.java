/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stemmerpln;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author charles
 */
public class StemmerPLN {

    public static List<String> filePaths = new ArrayList<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        List<String> palavras = new ArrayList<>();
        String diretorio = System.getProperty("user.dir");
        fileTreePrinter(new File(diretorio), 0);
        char[] w = new char[501];
        Stemmer s = new Stemmer();
        File f;
        //for (int i = 0; i < args.length; i++) {
        for (String filePath : filePaths) {
            f = new File(filePath);
            try {
                FileInputStream in = new FileInputStream(f);
                try {
                    while (true) {
                        int ch = in.read();
                        if (Character.isLetter((char) ch)) {
                            int j = 0;
                            while (true) {
                                ch = Character.toLowerCase((char) ch);
                                w[j] = (char) ch;
                                if (j < 500) {
                                    j++;
                                }
                                ch = in.read();
                                if (!Character.isLetter((char) ch)) {
                                    /* to test add(char ch) */
                                    for (int c = 0; c < j; c++) {
                                        s.add(w[c]);
                                    }
                                    /* or, to test add(char[] w, int j) */
 /* s.add(w, j); */
                                    s.stem();
                                    {
                                        String u;
                                        /* and now, to test toString() : */
                                        u = s.toString();
                                        /* to test getResultBuffer(), getResultLength() : */
 /* u = new String(s.getResultBuffer(), 0, s.getResultLength()); */
                                        System.out.print(u);
                                        palavras.add(u);
                                    }
                                    break;
                                }
                            }
                        }
                        if (ch < 0) {
                            break;
                        }
                        //System.out.println("Teste2");
                        System.out.print((char) ch);
                        palavras.add(String.valueOf(ch));

                    }
                } catch (IOException e) {
                    // System.out.println("error reading " + args[i]);
                    //break;
                }
            } catch (FileNotFoundException e) {
                //System.out.println("file " + args[i] + " not found");
                //break;
            }
            //}
            String stopList = insertStopListTag(palavras);
            try {
                printFile(diretorio + "/" + f.getName().replace(".text", "") + ".xml", stopList);
            } catch (IOException ex) {
                // Logger.getLogger(Teste.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static List<String> fileTreePrinter(File initialPath, int initialDepth) {

        int depth = initialDepth++;
        if (initialPath.exists()) {
            File[] contents = initialPath.listFiles();
            for (File content : contents) {
                if (content.isDirectory()) {
                    fileTreePrinter(content, initialDepth + 1);
                } else {
                    char[] dpt = new char[initialDepth];
                    for (int j = 0; j < initialDepth; j++) {
                        dpt[j] = '+';
                    }
                    //System.out.println(new String(dpt) + content.getName() + " " + content.getPath() );
                    //System.out.println(content.toString());
                    if (content.getName().contains(".text")) {
                        filePaths.add(content.toString());
                    }
                }
            }
        }
        return filePaths;
    }

    public static String insertStopListTag(List<String> dados) {
        StringBuilder stopList = new StringBuilder();
        stopList.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\n").append("<stopfile>").append("\n");

        for (String dado : dados) {
            dado = dado.replaceAll("[^a-zA-Z]", "");
            if (!dado.matches("")) {
                stopList.append("\t<stopword>").append(dado).append("</stopword>").append("\n");
            }
        }
        stopList.append("</stopfile>").append("\n");
        return stopList.toString();
    }

    public static void printFile(String fileName, String texto) throws IOException {
        try (FileWriter fw = new FileWriter(fileName); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(texto);
            bw.newLine();
            bw.close();
            fw.close();
        }
    }

}
