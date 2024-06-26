package com.jabar.app.fitnesscenter.feature.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

public class FilterableByPage {

    public static Pageable getPageable(Integer page, Integer limit){
        Pageable pageable;
        if(Objects.isNull(page) || Objects.isNull(limit)) {
            page = 1;
            limit = 10;
            pageable = PageRequest.of(page, limit);
            return pageable;
        }
        pageable = PageRequest.of(page, limit);

        return pageable;
    }

    public static Pageable getPageableWithSort(Integer page, Integer limit, Sort sort){
        Pageable pageable;
        if(Objects.isNull(page) || Objects.isNull(limit)) {
            page = 1;
            limit = 10;
            pageable = PageRequest.of(page, limit, sort);
            return pageable;
        }
        pageable = PageRequest.of(page, limit, sort);

        return pageable;
    }
}
