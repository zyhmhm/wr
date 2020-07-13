package com.wr.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {

    /**
     * 实现redis集群
     */

    @Value("${redis.nodes}")
    private String hostAndPort;

    //将set集合交给容器管理
    @Bean("redisSet")
    public Set<HostAndPort> redisSet(){
        Set<HostAndPort> nodes = new HashSet<>();
        getHostAndPorts(nodes);
        return nodes;
    }

    @Bean
    @Scope("prototype")
    public JedisCluster jedisCluster(@Qualifier("redisSet") Set<HostAndPort> redisSet){
        return new JedisCluster(redisSet);
    }

    private void getHostAndPorts(Set<HostAndPort> nodes) {
        String[] hostAndPorts = hostAndPort.split(",");
        for (String hostPort: hostAndPorts) {
            String host = hostPort.split(":")[0];
            int port = Integer.parseInt(hostPort.split(":")[1]);
            nodes.add(new HostAndPort(host,port));
        }
    }


}












