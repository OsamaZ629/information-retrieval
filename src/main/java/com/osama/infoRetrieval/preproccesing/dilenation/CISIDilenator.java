package com.osama.infoRetrieval.preproccesing.dilenation;

import com.osama.infoRetrieval.document.RawDocument;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class CISIDilenator implements Dilenator {
    private final String path;
    private Scanner sc;
    private long nextId;

    public CISIDilenator(String corpusPath){
        this.path = corpusPath;
        File file = new File(corpusPath);
        try {
            this.sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(69);
        }

        String line = this.sc.nextLine();
        String idStr = line.split(" ")[1].strip();
        this.nextId = Long.parseLong(idStr);
    }

    @Override
    public RawDocument next() {
        if (!hasNext())
            return null;

        Map<String, String> map = new TreeMap<>();
        StringBuilder buffer = new StringBuilder();
        String currentKey = "";

        String line;
        while (true){
            if (!hasNext())
                break;
            line = sc.nextLine().strip();

            if (line.matches("\\.[A-Z].*")){
                if (line.contains(".I")){
                    String[] keyStr = line.strip().split(" ");
                    map.put(".I", keyStr[keyStr.length - 1]);
                    nextId = Long.parseLong(keyStr[keyStr.length - 1]);
                    break;
                }
                if (buffer.length() > 0){
                    map.put(currentKey, buffer.toString());
                    buffer = new StringBuilder();
                }
                currentKey = line;
                continue;
            }

            buffer.append(line);
            buffer.append(System.getProperty("line.separator"));
        }

        String content = map.remove(".W");
        return new RawDocument(content, map);
    }


    public boolean hasNext(){
        return this.sc.hasNextLine();
    }
}
