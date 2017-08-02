package com.brmayi.cloudrise.core.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.util.Assert;

import com.brmayi.cloudrise.connection.RedisClusterConnection;
import com.brmayi.cloudrise.connection.RedisClusterNode;
import com.brmayi.cloudrise.connection.RedisClusterNode.SlotRange;
import com.brmayi.cloudrise.connection.commands.RedisClusterCommands.AddSlots;
import com.brmayi.cloudrise.connection.commands.RedisServerCommands.MigrateOption;
import com.brmayi.cloudrise.core.ClusterOperations;
import com.brmayi.cloudrise.core.RedisClusterCallback;
import com.brmayi.cloudrise.core.RedisTemplate;

public class DefaultClusterOperations<K, V> extends AbstractOperations<K, V> implements ClusterOperations<K, V> {

	private final RedisTemplate<K, V> template;

	/**
	 * Creates new {@link DefaultClusterOperations} delegating to the given {@link RedisTemplate}.
	 *
	 * @param template must not be {@literal null}.
	 */
	public DefaultClusterOperations(RedisTemplate<K, V> template) {

		super(template);
		this.template = template;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.core.RedisClusterOperations#keys(org.springframework.data.redis.connection.RedisNode, byte[])
	 */
	@Override
	public Set<K> keys(final RedisClusterNode node, final K pattern) {

		Assert.notNull(node, "ClusterNode must not be null.");

		return execute(connection -> deserializeKeys(connection.keys(node, rawKey(pattern))));
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.core.RedisClusterOperations#randomKey(org.springframework.data.redis.connection.RedisNode)
	 */
	@Override
	public K randomKey(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		return execute(connection -> deserializeKey(connection.randomKey(node)));
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.core.RedisClusterOperations#ping(org.springframework.data.redis.connection.RedisNode)
	 */
	@Override
	public String ping(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		return execute(connection -> connection.ping(node));
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.core.RedisClusterOperations#addSlots(org.springframework.data.redis.connection.RedisClusterNode, int[])
	 */
	@Override
	public void addSlots(final RedisClusterNode node, final int... slots) {

		Assert.notNull(node, "ClusterNode must not be null.");

		execute((RedisClusterCallback<Void>) connection -> {
			connection.clusterAddSlots(node, slots);
			return null;
		});
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.core.RedisClusterOperations#addSlots(org.springframework.data.redis.connection.RedisClusterNode, org.springframework.data.redis.connection.RedisClusterNode.SlotRange)
	 */
	@Override
	public void addSlots(RedisClusterNode node, SlotRange range) {

		Assert.notNull(node, "ClusterNode must not be null.");
		Assert.notNull(range, "Range must not be null.");

		addSlots(node, range.getSlotsArray());
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.core.RedisClusterOperations#bgReWriteAof(org.springframework.data.redis.connection.RedisClusterNode)
	 */
	@Override
	public void bgReWriteAof(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		execute((RedisClusterCallback<Void>) connection -> {
			connection.bgReWriteAof(node);
			return null;
		});
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.core.RedisClusterOperations#bgSave(org.springframework.data.redis.connection.RedisClusterNode)
	 */
	@Override
	public void bgSave(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		execute((RedisClusterCallback<Void>) connection -> {
			connection.bgSave(node);
			return null;
		});
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.core.RedisClusterOperations#meet(org.springframework.data.redis.connection.RedisClusterNode)
	 */
	@Override
	public void meet(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		execute((RedisClusterCallback<Void>) connection -> {
			connection.clusterMeet(node);
			return null;
		});
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.core.RedisClusterOperations#forget(org.springframework.data.redis.connection.RedisClusterNode)
	 */
	@Override
	public void forget(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		execute((RedisClusterCallback<Void>) connection -> {
			connection.clusterForget(node);
			return null;
		});
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.core.RedisClusterOperations#flushDb(org.springframework.data.redis.connection.RedisClusterNode)
	 */
	@Override
	public void flushDb(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		execute((RedisClusterCallback<Void>) connection -> {
			connection.flushDb(node);
			return null;
		});
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.core.RedisClusterOperations#getSlaves(org.springframework.data.redis.connection.RedisClusterNode)
	 */
	@Override
	public Collection<RedisClusterNode> getSlaves(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		return execute(connection -> connection.clusterGetSlaves(node));
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.core.RedisClusterOperations#save(org.springframework.data.redis.connection.RedisClusterNode)
	 */
	@Override
	public void save(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		execute((RedisClusterCallback<Void>) connection -> {
			connection.save(node);
			return null;
		});
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.core.RedisClusterOperations#shutdown(org.springframework.data.redis.connection.RedisClusterNode)
	 */
	@Override
	public void shutdown(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		execute((RedisClusterCallback<Void>) connection -> {
			connection.shutdown(node);
			return null;
		});
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.core.ClusterOperations#reshard(org.springframework.data.redis.connection.RedisClusterNode, int, org.springframework.data.redis.connection.RedisClusterNode)
	 */
	@Override
	public void reshard(final RedisClusterNode source, final int slot, final RedisClusterNode target) {

		Assert.notNull(source, "Source node must not be null.");
		Assert.notNull(target, "Target node must not be null.");

		execute((RedisClusterCallback<Void>) connection -> {

			connection.clusterSetSlot(target, slot, AddSlots.IMPORTING);
			connection.clusterSetSlot(source, slot, AddSlots.MIGRATING);
			List<byte[]> keys = connection.clusterGetKeysInSlot(slot, Integer.MAX_VALUE);

			for (byte[] key : keys) {
				connection.migrate(key, source, 0, MigrateOption.COPY);
			}
			connection.clusterSetSlot(target, slot, AddSlots.NODE);
			return null;
		});
	}

	/**
	 * Executed wrapped command upon {@link RedisClusterConnection}.
	 *
	 * @param callback
	 * @return
	 */
	public <T> T execute(RedisClusterCallback<T> callback) {

		Assert.notNull(callback, "ClusterCallback must not be null!");

		RedisClusterConnection connection = template.getConnectionFactory().getClusterConnection();

		try {
			return callback.doInRedis(connection);
		} finally {
			connection.close();
		}
	}
}
