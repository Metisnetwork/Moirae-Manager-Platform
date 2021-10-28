//package com.moirae.rosettaflow.mapper;
//
//import com.moirae.rosettaflow.common.constants.SysConstant;
//import com.moirae.rosettaflow.mapper.domain.Job;
//import com.moirae.rosettaflow.mapper.domain.User;
//import com.moirae.rosettaflow.service.IJobService;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author admin
// * @date 2021/7/20
// */
//@SpringBootTest
//public class UserMapperTest {
//
//    @Resource
//    private UserMapper userMapper;
//
//    @Resource
//    private JobMapper jobMapper;
//
//    @Resource
//    private RedisTemplate<String, Object> redisTemplate;
//
//    @Test
////    @Disabled
//    public void test() {
//        User user = userMapper.selectById(1L);
//        System.out.println(user.getUserName());
//    }
//
//    @Test
//    public void testJob(){
//        List<Long> ids = new ArrayList<>();
//        ids.add(1L);
//        ids.add(2L);
////        List<Job> jobs =jobService.listByIds(ids);
//        List<Job> jobs = new ArrayList<>();
//        Job job =jobMapper.selectById(1L);
//        jobs.add(job);
//
//        for (int i = 0; i < jobs.size(); i++) {
//            redisTemplate.opsForList().leftPush(SysConstant.JOB_ADD_QUEUE,jobs.get(i));
//        }
//
//        List<Job> addJobList = (List<Job>) redisTemplate.opsForList().rightPop(SysConstant.JOB_ADD_QUEUE);
//
//        for (int i = 0; i < addJobList.size(); i++) {
//            System.out.println(addJobList.get(i));
//        }
//
//
//    }
//
//
//
//}
