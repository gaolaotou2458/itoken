package com.funtl.itoken.service.posts.mapper;

import com.funtl.itoken.common.domain.TbPostsPost;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.MyMapper;

/**
 * 如果需要扩展方法，可以在基类基础上扩展，然后服务用这个mapper,
 * 如果无需扩展，service直接使用基类的mapper即可
 * */

@Repository
public interface TbPostsPostExtendMapper extends MyMapper<TbPostsPost> {
}