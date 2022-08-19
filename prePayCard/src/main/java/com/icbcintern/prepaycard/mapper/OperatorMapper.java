package com.icbcintern.prepaycard.mapper;

import com.icbcintern.prepaycard.pojo.Operator;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OperatorMapper {
    /**
     * 添加 merchant 信息
     */
    @Insert("insert into operator(operator_name, login_passwd) values (#{name},#{loginPasswd})")
    int insertOperator(Operator operator);

    /**
     * 根据 operator_name 查询 运营方 信息
     */
    @Results(id = "operatorNameResultMap", value = {@Result(property = "name", column = "operator_name")})
//    @ResultMap("operatorNameResultMap")
    @Select("select * from operator where operator_name=#{name}")
    Operator getOperatorByName(String operatorName);
}
