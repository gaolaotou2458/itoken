package com.funtl.itoken.common.hystrix;

import com.funtl.itoken.common.constants.HttpStatusConstans;
import com.funtl.itoken.common.dto.BaseResult;
import com.funtl.itoken.common.utils.MapperUtils;
import com.google.common.collect.Lists;

/**
 *  通用的熔断方法
 */

public class Fallback {

    /**
     * 502
     * @return
     */
    public static String badGateway(){
        BaseResult baseResult = BaseResult.notOk((Lists.newArrayList(new BaseResult.Error(String.valueOf(HttpStatusConstans.Bad_GATEWAY.getStatus()), HttpStatusConstans.Bad_GATEWAY.getContent()))));
        try {
            return MapperUtils.obj2json(baseResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

