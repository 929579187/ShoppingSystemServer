package com.sy.shopping.handler;

import com.sy.shopping.annotations.RequestMapping;
import com.sy.shopping.dto.IndexPageData;
import com.sy.shopping.service.IndexService;
import com.sy.shopping.service.impl.IndexServiceImpl;

@RequestMapping("index")
public class IndexHandler {
    private IndexService indexService = new IndexServiceImpl();

    @RequestMapping("data")
    public IndexPageData getIndexPageData() {
        return indexService.getIndexPageData();
    }

}
