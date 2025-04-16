package com.fu.springbootdemo.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.fu.springbootdemo.entity.User;
import com.fu.springbootdemo.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class UserReadListener implements ReadListener<User> {

    private static final int BATCH_COUNT = 100; //批量处理条数

    /**
     * 缓存的数据
     */
    private List<User> userCachedList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private final UserMapper userMapper;

    //如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
    public UserReadListener(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    //每一条数据解析都会来调用
    @Override
    public void invoke(User user, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(user));
        userCachedList.add(user);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (userCachedList.size() >= BATCH_COUNT) {
            saveData();//调用mapper数据入库
            // 存储完成清理 list
            userCachedList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    //所有数据解析完成了 都会来调用
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //确保最后遗留的数据也存储到数据库
        saveData();//调用mapper数据入库
    }


    //========================================内部方法========================================

    /**
     * 加上存储数据库
     */
    private void saveData() {
//        log.info("{}条数据，开始存储数据库！", userCachedList.size());
        if (!userCachedList.isEmpty()) {
            userCachedList.forEach(user -> {
                user.setIsBan(0);
                user.setIsDelete(0);
                user.setPwd("T4iNIY4/HHJ9iNql/GCm+58GH3Lg1c9omyVvQxoTUwfAgCYW/umNdA9VyRcGLYXdcD0v3XYEHlcDDR5eAt0T4W99xDqph+7UJ+koehTDkbFGF1Sm/oXE/x+mZnWxCo71wVXOLfejjgRnz+nlLX9S64Q2zFHh6lqQfY+KeUE9mPs=");
                user.setCreateTime(LocalDateTime.now());
                user.setUpdateTime(LocalDateTime.now());
            });
            userMapper.importExcel(userCachedList);
        }
    }

}
