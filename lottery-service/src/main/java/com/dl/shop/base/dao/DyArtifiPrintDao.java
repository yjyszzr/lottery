package com.dl.shop.base.dao;

import java.util.List;
import com.dl.shop.base.dao.entity.DDyArtifiPrintEntity;

/**
 * 动态人工打印表
 * @author wht
 * @date 2018.10.16
 */
public interface DyArtifiPrintDao {
	public static final String TABLE_NAME = "dl_artifi";

	/**
	 * 该队列是否存在
	 * @param userId
	 * @return
	 */
	public int isDyQueueExist(String userId);
	
	/**
	 * 创建动态队列
	 * @param userId
	 * @return
	 */
	public int createDyQueue(String userId);
	
	/**
	 * 获取该队列所有数据
	 * @param userId
	 * @return
	 */
	public List<DDyArtifiPrintEntity> listAll(String userId);
	
	/**
	 * 清空该队列数据
	 * @return
	 */
	public int clearAll(String userId);

	/**
	 * 删除该动态队列
	 * @param userId
	 * @return
	 */
	public int dropTable(String userId);
	
	/**
	 * 添加打印数据
	 * @param entity
	 * @return
	 */
	public int addDyArtifiPrintInfo(String userId,DDyArtifiPrintEntity entity);
}
