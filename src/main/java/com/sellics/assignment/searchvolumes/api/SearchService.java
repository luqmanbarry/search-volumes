package com.sellics.assignment.searchvolumes.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.paukov.combinatorics3.Generator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * In this class, are the methods that work together to compute the search score of a given keyword
 * @author Hamidou Barry
 * */
@Service
public class SearchService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${AMAZON_SEARCH_SERVICE_URI}")
    private String amzSearchServiceUri;

    /**
     * This method receives keyword string, determinate some common typos associated with that string,
     * calls the amazon autocomplete api for possible combination,
     * combine the all the api call responses to a single list
     * and then calculate the frequency of the keyword among those returned values
     * @param keyword
     * @return SearchResponse
     * */
    public Optional<SearchResponse> fetchTextScore(String keyword) {
        if(keyword == null && keyword.isEmpty()) {
            return Optional.empty();
        }
        // replace + sign with space
        if (keyword.contains("+")) {
            keyword.replaceAll("/+", " ");
        }
        List<String> apiResultBucket = new ArrayList<>();
        List<String> keywordList = getAllCombinations(keyword);
        // add the keyword to the list as well
        keywordList.add(keyword);
        // call the API for each candidate keyword
        keywordList.forEach(word -> {
            List<String> apiResult = callAPIAndExtractResult(word);
            apiResultBucket.addAll(apiResult);
        });
        // compute the frequencies of the word matching or prefixed the keyword
        SearchResponse searchResponse = new SearchResponse(keyword, calculateKeywordScore(apiResultBucket, keyword));
        return Optional.of(searchResponse);
    }

    /**
     * This method calculates find the frequency of  given string within a list of strings
     * @param apiResultBucket
     * @param keyword
     * @return keywordScore
     * */
    private int calculateKeywordScore(List<String> apiResultBucket, String keyword) {
        int keywordScore = 0;
        for (int i = 0; i < apiResultBucket.size(); i++) {
            String suggestion = apiResultBucket.get(i);

            if (suggestion != null && !suggestion.isEmpty() &&
                    (suggestion.equalsIgnoreCase(keyword) || suggestion.startsWith(keyword) || suggestion.contains(keyword))) {
                keywordScore++;
            }
        }
        // validate keywordScore does not exceeds 100
        if (keywordScore > 100) {
            keywordScore = keywordScore%100;
        }
        return keywordScore;
    }

    /**
     * This method calls the amazon autocomplete API and extract the result into a List<String>
     * @param keyword
     * @Return resultList
     * */
    private List<String> callAPIAndExtractResult(String keyword) {
        String searchUrl = amzSearchServiceUri + keyword;
        String apiResponse = restTemplate.getForObject(searchUrl, String.class);
        List<Object> rawList = JsonParserFactory.getJsonParser().parseList(apiResponse);
        ArrayList<String> resultList = (ArrayList<String>)rawList.get(1);
        return resultList;
    }

    /**
     * This method generate all possible combinations of possible terms using keyword
     * @param keyword
     * @return candidateBucket
     * */
    private List<String> getAllCombinations(String keyword) {
        List<String> candidateBucket = new ArrayList<>();
        String[] array = keyword.split("");
        Generator.combination(array)
                .simple(keyword.length()-1)
                .stream()
                .forEach(can -> {
                    final String aggr = can.stream().map(String::valueOf).collect(Collectors.joining());
                    candidateBucket.add(aggr);
                });

        return candidateBucket;
    }
}
