package com.biuqu.boot.service;

import java.util.List;
import java.util.Set;

/**
 * Redis封装和扩展类
 * <p>
 * 封装了redis的自动注入逻辑和使用标准化(如：支持<String,Object>结构，支持脚本执行)
 *
 * @author BiuQu
 * @date 2023/1/27 16:25
 */
public interface RedisService
{
    /**
     * 设置过期时间(ms),该key会在设置之后倒计时
     *
     * @param key      String/Hash/Set/SortedSet结构的key
     * @param expireAt 过期时间(ms)
     * @return true表示设置成功
     */
    Boolean expire(String key, long expireAt);

    /**
     * 删除存储数据
     *
     * @param key String/Hash/Set/SortedSet结构的key
     */
    void delete(String key);

    /**
     * 执行脚本，获取结果
     *
     * @param script 脚本名称
     * @param clazz  返回的参数类型Class
     * @param keys   redis脚本中的定义的参数key集合
     * @param param  redis脚本中的参数值集合
     * @param <T>    脚本返回结果的类型
     * @return 返回结果
     */
    <T> T execute(String script, Class<T> clazz, List<String> keys, Object... param);

    /**
     * 获取String结构的对象
     *
     * @param key String结构的key
     * @param <T> String结构的value对象类型
     * @return String结构的value对象
     */
    <T> T get(String key);

    /**
     * 保存String结构的<String,Object>对象
     *
     * @param key   String结构的key
     * @param value String结构的value对象
     * @param <T>   String结构的value对象类型
     */
    <T> void set(String key, T value);

    /**
     * 当不存在时，设置其值，否则啥也不做
     *
     * @param key   String结构的key
     * @param value String结构的value对象
     * @param <T>   String结构的value对象类型
     * @return true表示设置成功
     */
    <T> Boolean setNx(String key, T value);

    /**
     * 获取Hash结构的key中的field对应的value对象
     *
     * @param key   Hash结构的key(类似关系数据库的表名)
     * @param field Hash结构的属性名(类似关系数据库的字段名)
     * @param <T>   Hash结构的属性名对象的属性值对象类型
     * @return Hash结构的属性名对象的属性值对象(类似关系数据库的字段值对象)
     */
    <T> T hGet(String key, Object field);

    /**
     * 设置Hash结构的key中的field对应的value对象
     *
     * @param key   Hash结构的key(类似关系数据库的表名)
     * @param field Hash结构的属性名(类似关系数据库的字段名)
     * @param value Hash结构的属性名对象的属性值对象(类似关系数据库的字段值对象)
     * @param <T>   Hash结构的属性名对象的属性值对象类型
     */
    <T> void hSet(String key, Object field, T value);

    /**
     * 设置Set结构的key对应的value集合
     *
     * @param key    Set结构的key
     * @param values value集合
     * @param <T>    value类型
     * @return 添加成功的元素个数(不包含重复添加的)
     */
    <T> Long sAdd(String key, T... values);

    /**
     * 移除Set结构的key对应的value集合
     *
     * @param key    Set结构的key
     * @param values value集合
     * @param <T>    value类型
     * @return 移除成功的元素个数(不包含重复添加的)
     */
    <T> Long sRem(String key, T... values);

    /**
     * 查询Set结构key对应的值对象集合
     *
     * @param key Set结构的key
     * @param <T> value类型
     * @return 值对象集合
     */
    <T> Set<T> sMembers(String key);

    /**
     * 设置SortedSet结构的key对应的value集合
     *
     * @param key   SortedSet结构的key
     * @param value value对象
     * @param score 打分
     * @param <T>   value类型
     * @return true表示添加成功
     */
    <T> Boolean zAdd(String key, T value, long score);

    /**
     * 移除SortedSet结构的key对应的value集合
     *
     * @param key    SortedSet结构的key
     * @param values value集合
     * @param <T>    value类型
     * @return 移除成功的元素个数(不包含重复添加的)
     */
    <T> Long zRem(String key, T... values);

    /**
     * 查询SortedSet结构key对应的分数介于一定范围的值对象集合
     *
     * @param key      SortedSet结构的key
     * @param maxScore 最大得分(含)
     * @param minScore 最小得分(含)
     * @param <T>      value类型
     * @return 值对象集合
     */
    <T> Set<T> zRangeByScore(String key, long minScore, long maxScore);
}
