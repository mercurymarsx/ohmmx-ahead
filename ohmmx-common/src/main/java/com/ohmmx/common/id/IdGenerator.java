package com.ohmmx.common.id;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.ohmmx.common.utils.DateUtils;

/**
 * 生成唯一性ID算法
 */
public class IdGenerator implements IdentifierGenerator {
	private static final long datacenterIdBits = 3L; // 3位Datacenter
	private static final long workerIdBits = 5L; // 5位Worker
	private static final long maxDatacenterId = -1 ^ (-1 << datacenterIdBits);
	private static final long maxWorkerId = -1 ^ (-1 << workerIdBits);
	private static final long sequenceBits = 10L; // 序列10位
	private static final long sequenceMask = -1L ^ (-1L << sequenceBits); // 序列号挡板
	private static final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits; // 时间戳偏移量
	private static final long datacenterIdShift = sequenceBits + workerIdBits; // datacenter偏移量
	private static final long workerIdShift = sequenceBits; // worker偏移量
	private static long lastTimestamp = -1L;
	private static long sequence = 0L;

	private static long datacenter;
	private static long worker;

	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		return flake();
	}

	/**
	 * 生成全局唯一ID
	 *
	 * <pre>
	 * 时间戳 + 数据中心ID + 生产机ID + 序列号
	 * </pre>
	 *
	 * @return 唯一ID
	 */
	private static synchronized String flake() {
		long timestamp = System.currentTimeMillis();
		try {
			if (datacenter < 0 || datacenter > maxDatacenterId) {
				throw new IllegalArgumentException(String.format("datacenter Id can't be more than %d or less than 0", maxDatacenterId));
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("datacenter Id config error, please check [server.cluster.datacenter] in property file.");
		}
		try {
			if (worker < 0 || worker > maxWorkerId) {
				throw new IllegalArgumentException(String.format("worker Id can't be more than %d or less than 0", maxWorkerId));
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("datacenter Id config error, please check [server.cluster.worker] in property file.");
		}
		if (timestamp < lastTimestamp) { // 避免出现时光倒流
			throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}
		if (lastTimestamp == timestamp) { // 同一时间内
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) { // 一个时间点ID序列用完了
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0L;
		}
		// 当前时间设置成前一秒的时间
		lastTimestamp = timestamp;

		String id = (Long.toHexString((timestamp << timestampLeftShift) | (datacenter << datacenterIdShift) | (worker << workerIdShift) | sequence)).toUpperCase();
		return id;
	}

	// 获取当前时间
	private static long timeGen() {
		return System.currentTimeMillis();
	}

	// 取下一个时间点
	private static long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		return flake();
	}

	public static String uuid() {
		return flake();
	}

	public static String explainId(String id) {
		long idL = Long.parseLong(id, 16);
		long millis = idL >> 18;
		long datacenter = (millis << 18 ^ idL) >> 15;
		long worker = ((millis << 18 | datacenter << 15) ^ idL) >> 10;
		long sequence = (millis << 18 | datacenter << 15 | worker << 10) ^ idL;
		Date date = new Date(millis);
		String out = "time:" + DateUtils.yyyy_MM_dd_HH_mm_ss(date) + ", datacenter:" + datacenter //
				+ ", worker:" + worker + ", sequence:" + sequence;
		return out;
	}
}
