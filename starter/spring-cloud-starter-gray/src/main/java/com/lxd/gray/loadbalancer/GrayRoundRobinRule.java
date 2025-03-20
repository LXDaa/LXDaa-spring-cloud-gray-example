package com.lxd.gray.loadbalancer;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// 定义 GrayRoundRobinRule 类，继承自 AbstractGrayLoadBalancerRule
public class GrayRoundRobinRule extends AbstractGrayLoadBalancerRule {
    // 原子整数，用于循环计数
    private AtomicInteger nextServerCyclicCounter;
    // 常量，表示只选择可用的服务器
    private static final boolean AVAILABLE_ONLY_SERVERS = true;
    // 常量，表示选择所有服务器
    private static final boolean ALL_SERVERS = false;

    // 日志记录器
    private static Logger log = LoggerFactory.getLogger(RoundRobinRule.class);

    // 构造函数，初始化原子整数
    public GrayRoundRobinRule() {
        nextServerCyclicCounter = new AtomicInteger(0);
    }

    // 带负载均衡器的构造函数
    public GrayRoundRobinRule(ILoadBalancer lb) {
        this();
        setLoadBalancer(lb);
    }

    // 选择服务器的方法
    public Server choose(ILoadBalancer lb, Object key) {
        // 如果负载均衡器为空，记录警告并返回空
        if (lb == null) {
            log.warn("no load balancer");
            return null;
        }
        Server server = null;
        int count = 0;
        // 尝试最多10次选择服务器
        while (server == null && count++ < 10) {
            // 获取可达服务器列表
            List<Server> reachableServers = getReachableServers();
            // 获取所有服务器列表
            List<Server> allServers = getAllServers();
            // 可达服务器数量
            int upCount = reachableServers.size();
            // 所有服务器数量
            int serverCount = allServers.size();

            // 如果没有可用服务器，记录警告并返回空
            if ((upCount == 0) || (serverCount == 0)) {
                log.warn("No up servers available from load balancer: " + lb);
                return null;
            }
            // 获取下一个服务器的索引
            int nextServerIndex = incrementAndGetModulo(serverCount);
            // 根据索引获取服务器
            server = allServers.get(nextServerIndex);
            // 如果服务器为空，让出线程并继续尝试
            if (server == null) {
                /* Transient. */
                Thread.yield();
                continue;
            }
            // 如果服务器可用且准备好服务，返回该服务器
            if (server.isAlive() && (server.isReadyToServe())) {
                return (server);
            }
            // 否则，重置服务器为null，继续下一次尝试
            server = null;
        }
        // 如果尝试超过10次仍未找到可用服务器，记录警告
        if (count >= 10) {
            log.warn("No available alive servers after 10 tries from load balancer: "
                    + lb);
        }
        // 返回最终选择的服务器，可能为空
        return server;
    }

    // 增加计数并取模的方法
    private int incrementAndGetModulo(int modulo) {
        for (; ; ) {
            // 获取当前计数值
            int current = nextServerCyclicCounter.get();
            // 计算下一个计数值
            int next = (current + 1) % modulo;
            // 原子性地更新计数值，成功则返回下一个计数值
            if (nextServerCyclicCounter.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    // 重写选择服务器的方法，使用默认负载均衡器
    @Override
    public Server choose(Object key) {
        Server server = choose(getLoadBalancer(), key);
        System.out.println(">>>>>> 负载均衡:" + server);
        return server;
    }

    // 初始化方法，未实现
    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
    }
}