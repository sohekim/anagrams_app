/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 4;
    private static final int MAX_WORD_LENGTH = 7;
    private static int currentLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    private ArrayList<String> wordlist;
    private HashMap<String, ArrayList<String>> lettersToWord;
    private HashMap<Integer, ArrayList<String>> sizeToWord;
    private static final String TAG = "AnagramDictionary";
    private HashSet<String> wordSet;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        lettersToWord = new HashMap<>();
        sizeToWord = new HashMap<>();
        wordSet = new HashSet<>();
        wordlist = new ArrayList<>();
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (lettersToWord.containsKey(sortLetters(word))) {
                lettersToWord.get(sortLetters(word)).add(word);
            } else {
                ArrayList<String> anagram = new ArrayList<String>();
                anagram.add(word);
                lettersToWord.put(sortLetters(word), anagram);
            }

            if (sizeToWord.containsKey(word.length())) {
                sizeToWord.get(word.length()).add(word);
            } else {
                ArrayList<String> anagram2 = new ArrayList<String>();
                anagram2.add(word);
                sizeToWord.put(word.length(), anagram2);
            }
            wordSet.add(word);
            wordlist.add(word);
        }



    }

    public boolean isGoodWord(String word, String base) {
        // why do we have to check if the word contains the wordset?
        if (wordSet.contains(word)) {
            if (!word.contains(base)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getAnagrams(String targetWord) {
//       return  lettersToWord.get(targetWord);
//      Log.d(TAG, getAnagramsWithOneMoreLetter(targetWord).toString());
        ArrayList<String> result = lettersToWord.get(sortLetters(targetWord));
//        ArrayList<String> result = new ArrayList<String>();
//        for (String word : wordlist) {
//            if (sortLetters(word).equals(sortLetters(targetWord)) && word.length() == targetWord.length()) {
//                result.add(word);
//            }
//        }
        return result;
    }


    private String sortLetters (String word) {
        char tempArray[] = word.toCharArray();
        Arrays.sort(tempArray);
        return new String (tempArray);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        char c;
        for(c = 'a'; c <= 'z'; ++c) {
            String subWord;
            subWord = word.concat(String.valueOf(c));
            if (lettersToWord.containsKey(sortLetters(subWord))) {
                ArrayList<String> sub = lettersToWord.get(sortLetters(subWord));
                result.addAll(sub);
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        Random rand = new Random();
        ArrayList<String> wordArray = sizeToWord.get(currentLength);

        int n = rand.nextInt(wordArray.size());
        // do i assume that each letter group has a word with x number of anagrams?
        while (lettersToWord.get(sortLetters(wordArray.get(n))).size() < MIN_NUM_ANAGRAMS) {
            n++;
        }
        if (currentLength < MAX_WORD_LENGTH) {
            currentLength++;
        }

        return wordArray.get(n);
    }
}
