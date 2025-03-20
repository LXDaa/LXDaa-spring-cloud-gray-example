package com.lxd.gray.loadbalancer;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

// 定义 GrayRandomRule 类，继承自 AbstractGrayLoadBalancerRule
public class GrayRandomRule extends AbstractGrayLoadBalancerRule {

    // 重写 choose 方法，根据负载均衡器和键选择服务器
    public Server choose(ILoadBalancer lb, Object key) {
        // 如果负载均衡器为空，直接返回 null
        if (lb == null) {
            return null;
        }

        Server server = null;

        // 循环直到找到一个可用的服务器
        while (server == null) {
            // 如果当前线程被中断，返回 null
            if (Thread.interrupted()) {
                return null;
            }
            // 获取所有可达的服务器列表
            List<Server> upList = getReachableServers();
            // 获取所有服务器列表
            List<Server> allList = getAllServers();

            // 获取所有服务器的数量
            int serverCount = allList.size();
            // 如果服务器数量为 0，返回 null
            if (serverCount == 0) {
                /*
                 * No servers. End regardless of pass, because subsequent passes
                 * only get more restrictive.
                 */
                return null;
            }

            // 随机选择一个服务器索引
            int index = chooseRandomInt(serverCount);
            // 根据索引获取服务器
            server = upList.get(index);

            // 如果服务器为空，让出 CPU 时间片后继续循环
            if (server == null) {
                /*
                 * The only time this should happen is if the server list were
                 * somehow trimmed. This is a transient condition. Retry after
                 * yielding.
                 */
                Thread.yield();
                continue;
            }

            // 如果服务器可用，返回该服务器
            if (server.isAlive()) {
                return (server);
            }

            // 如果服务器不可用，设置为 null 并让出 CPU 时间片后继续循环
            // Shouldn't actually happen.. but must be transient or a bug.
            server = null;
            Thread.yield();
        }

        // 返回最终选择的服务器
        return server;

    }

    // 随机选择一个整数的方法
    protected int chooseRandomInt(int serverCount) {
        return ThreadLocalRandom.current().nextInt(serverCount);
    }

    // 重写 choose 方法，根据键选择服务器
    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

    // 初始化方法，根据配置初始化
    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        // TODO Auto-generated method stub
        
    }
}