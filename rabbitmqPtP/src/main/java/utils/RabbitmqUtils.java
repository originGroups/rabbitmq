package utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.util.StringUtils;

import java.util.concurrent.*;

/**
 * @author 袁强
 * @data 2020/10/15 10:17
 * @Description
 */
public class RabbitmqUtils {

    /**
     * @auther: 袁强
     * @Description: 考虑到资源情况,连接工厂只需要在工具类加载初始化一次即可; 静态代码块只会加载一次
     * @date: 2020/10/15 10:37
     */
    private static ConnectionFactory connectionFactory;
    static {
        //获取rabbitmq连接工厂
        connectionFactory = new ConnectionFactory();
        //设置主机/tcp通信端口
        connectionFactory.setHost("10.15.0.9");
        connectionFactory.setPort(5672);
        //设置要连接的虚拟主机
        connectionFactory.setVirtualHost("/ems");
        //设置虚拟主机访问权限
        connectionFactory.setUsername("ems");
        connectionFactory.setPassword("123456");
        //MQ使用了线程池技术，它们是共享线程的，而不是一个通道一个线程,设置线程池提高效率
        //ali规约不推荐使用Executors创建线程池,规避资源耗尽的风险。
        //ExecutorService threadPool = Executors.newFixedThreadPool(500);
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("providerMessage").build();
        ExecutorService threadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        connectionFactory.setSharedExecutor(threadPool);
        //自动恢复连接
        connectionFactory.setAutomaticRecoveryEnabled(true);
        //30s超时
        connectionFactory.setConnectionTimeout(30000);
        //心跳
        connectionFactory.setRequestedHeartbeat(60);
    }

    public static Connection getConnection(){
        try {
            //获取rabbitmq连接对象
            Connection connection = connectionFactory.newConnection();
            return connection;
        }catch (Exception e){
            System.out.println("获取rabbitmq连接对象失败: " + e.getMessage());
        }
        return null;
    }
    public static void closeChannelAndConnection(Channel channel , Connection connection){
        try {
            if(!StringUtils.isEmpty(channel)){
                channel.close();
            }
            if(!StringUtils.isEmpty(connection)){
                connection.close();
            }
        }catch (Exception e){
            System.out.println("关闭连接失败: " + e.getMessage());
        }
    }
}
