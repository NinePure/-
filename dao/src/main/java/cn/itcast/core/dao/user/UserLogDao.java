package cn.itcast.core.dao.user;

import cn.itcast.core.pojo.user.UserLog;
import cn.itcast.core.pojo.user.UserLogQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserLogDao {
    int countByExample(UserLogQuery example);

    int deleteByExample(UserLogQuery example);

    int insert(UserLog record);

    int insertSelective(UserLog record);

    List<UserLog> selectByExample(UserLogQuery example);

    int updateByExampleSelective(@Param("record") UserLog record, @Param("example") UserLogQuery example);

    int updateByExample(@Param("record") UserLog record, @Param("example") UserLogQuery example);
}