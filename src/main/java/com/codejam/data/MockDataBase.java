package com.codejam.data;

import com.codejam.model.WordList;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class MockDataBase {
    private WordList randomWords;

    public MockDataBase() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("data.json");
            randomWords = objectMapper.readValue(is, WordList.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<String> getRandomWords(int n){
        List<String> words = randomWords.getData();
        Collections.shuffle(words);
        return words.subList(0, n);
    }
}
