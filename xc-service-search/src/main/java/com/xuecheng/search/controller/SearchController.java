package com.xuecheng.search.controller;

import com.xuecheng.api.search.SearchControllerApi;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search/course")
public class SearchController implements SearchControllerApi {
    @Autowired
    SearchService searchService;

    @Override
    @GetMapping(value="/list/{page}/{size}")
    public QueryResponseResult list(
            @PathVariable("page") int page,
            @PathVariable("size") int size,
            CourseSearchParam courseSearchParam) {
        return searchService.list(page,size,courseSearchParam);
    }
}
