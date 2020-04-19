package com.gmail.yauhen2012.service.util;

import com.gmail.yauhen2012.service.constant.PaginationConstant;

public class PaginationUtil {

    public static int findStartPosition(int page) {
        return (page - 1) * PaginationConstant.ITEMS_BY_PAGE;
    }

}
