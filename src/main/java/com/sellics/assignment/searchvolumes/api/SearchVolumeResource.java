package com.sellics.assignment.searchvolumes.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@RestController
public class SearchVolumeResource {

    private SearchService searchService;

    public SearchVolumeResource(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/estimate")
    public ResponseEntity<SearchResponse> searchWord(@RequestParam("keyword") @NotBlank String keyword) {
        Optional<SearchResponse> searchResult = searchService.fetchTextScore(keyword);
        if (searchResult.isPresent()) {
            return new ResponseEntity<>(searchResult.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
