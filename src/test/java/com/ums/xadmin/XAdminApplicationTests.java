package com.ums.xadmin;

import com.ums.sys.entity.User;
import com.ums.sys.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest

class XAdminApplicationTests {

	@Resource
	private UserMapper userMapper;
	@Test
	void contextLoads() {
		List<User> users =userMapper.selectList(null);
		users.forEach(System.out::println);
	}

}
