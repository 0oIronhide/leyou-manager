package cn.hp.search.controller;

import cn.hp.search.service.SearchService;
import cn.hp.search.utils.SearchRequest;
import cn.hp.search.utils.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 向上
 * @Date: 2020/05/09/19:49
 * @Description:
 */
@RestController

public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("page")
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest searchRequest) {
        SearchResult search = searchService.search(searchRequest);
        if (search != null && search.getItems().size() > 0) {
            return ResponseEntity.ok(search);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
