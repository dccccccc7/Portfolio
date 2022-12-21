package com.example.school.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.school.entity.baseEntity.course;
import com.example.school.mapper.TestMapper;
import com.example.school.mapper.TestSessionMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    TestMapper testMapper;

    @GetMapping("/getTestMapper")
    public List<course> testMapper(){
        return testMapper.testList();
    }

    @GetMapping("/getTestFresh")
    public List<course> testRefresh(){
        SqlSessionFactoryBuilder builder=new SqlSessionFactoryBuilder();//xml或注解获得
        List<course> ans = new ArrayList<>();
        try {
            SqlSessionFactory factory1 = builder.build(Resources.getResourceAsStream("mybatis-config.xml"));
            SqlSession session1 = factory1.openSession();
            SqlSession session2 = factory1.openSession();

            TestMapper testMapperSession1 = session1.getMapper(TestMapper.class);
            TestMapper testMapperSession2 = session2.getMapper(TestMapper.class);

            System.out.println("一次查询");
            ans = testMapperSession1.testList();
            session1.commit();

            System.out.println("一次查询");
            ans = testMapperSession1.testList();


            System.out.println("一次查询");
            ans = testMapperSession2.testList();

            System.out.println("一次更新");
            testMapperSession2.testList();
            session2.commit();


            System.out.println("一次查询");
            ans = testMapperSession1.testList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ans;

    }

    @GetMapping("/getTestSession")
    public List<course> test(){
        SqlSessionFactoryBuilder builder=new SqlSessionFactoryBuilder();//xml或注解获得
        List<course> ans = new ArrayList<>();
        try {
            SqlSessionFactory factory1 = builder.build(Resources.getResourceAsStream("mybatis-config.xml"));
            SqlSessionFactory factory2 = builder.build(Resources.getResourceAsStream("mybatis-config.xml"));
            SqlSession session1 = factory1.openSession();
            SqlSession session2 = factory1.openSession();

            TestMapper testMapperSession1 = session1.getMapper(TestMapper.class);
            TestMapper testMapperSession2 = session2.getMapper(TestMapper.class);

            System.out.println("一次查询");
            ans = testMapperSession1.testList();
            session1.commit();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            os.writeObject(ans);
            os.flush();
            byte[] ans1 = bos.toByteArray();

            System.out.println("一次查询");
            session2.commit();
            ans = testMapperSession2.testList2();
            session2.commit();
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            os.writeObject(ans);
            os.flush();
            byte[] ans2 = bos.toByteArray();

            System.out.println("一次查询");
            ans = testMapperSession1.testList();

            System.out.println("一次查询");
            ans = testMapperSession2.testList2();

            System.out.println("一次查询");
            ans = testMapperSession2.testList();

            System.out.println("一次查询");
            testMapperSession2.updateTestData();

            System.out.println("一次查询");
            ans = testMapperSession2.testList();

            System.out.println("一次查询");
            ans = testMapperSession2.testList2();

            session1.commit();
            session2.commit();
            System.out.println("x");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ans;

    }
}
