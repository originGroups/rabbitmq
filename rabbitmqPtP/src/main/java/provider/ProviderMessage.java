package provider;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.junit.Test;
import utils.RabbitmqUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * @author 袁强
 * @data 2020/10/14 12:00
 * @Description
 */
public class ProviderMessage {
    /**
     * @auther: 袁强
     * @Description:  rabbitmq不管是生产者还是消费者都是通过 通道channel 传递消息的
     * testPM 使用rabbitmq第一种消息模式:点对点模式,不使用交换机,生产者直接通过通道将消息发送到队列中,而消费者则通过通道从队列中获取消息
     * @date: 2020/10/14 14:51
     */
    @Test
    public void testPM() throws IOException, TimeoutException {

        /**Properties prop = new Properties();
        InputStream inStream = this.getClass().getResourceAsStream("/config.properties");
        prop.load(inStream);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(prop.getProperty("RabbitMQHost"));
        factory.setUsername(prop.getProperty("RabbitMQUserName"));
        factory.setPassword(prop.getProperty("RabbitMQPassword"));
        */

        /*//创建连接mq的连接工厂对象
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置连接rabbitmq主机
        connectionFactory.setHost("10.15.0.9");
        //设置tcp通信端口
        connectionFactory.setPort(5672);
        //设置要连接的虚拟主机,虚拟主机必须以/开头
        connectionFactory.setVirtualHost("/ems");
        //设置访问虚拟主机的权限
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
        //最后一步,获取mq连接对象
        Connection connection = connectionFactory.newConnection();*/

        Connection connection = RabbitmqUtils.getConnection();
        //获取连接中的一个通道
        Channel channel = connection.createChannel();
        //点对点模式,不使用交换机,将通道绑定到指定队列
        //参数1:queue(队列名称),如果该队列不存在则会自动创建
        //参数2:定义队列特性是否要持久化,true表示要持久化队列,服务即使挂掉队列也不会丢失
        //参数3:exclusive(排除)是否独占队列,true表示该队列只能被当前通道所占用,false表示不独占队列,一般都是false,允许其他通道使用该队列
        //参数4:autoDelete 是否在消费完后且通道与队列绑定关系断开后怕[生产者发送完消息,该进程就挂了,与队列的绑定关系也断了,而消费者一直处于监听状态,所以只有主动关闭消费者才消费者通道才与队列断开]自动删除队列,false表示不自动删除
        //参数5:额外参数
        channel.queueDeclare("hello",false,false,false,null);
        //发布消息
        //参数1:exchange(交换机名称),点对点不使用交换机
        //参数2:routingKey(队列名称)
        //参数3:props(传递消息的额外配置)MessageProperties.PERSISTENT_TEXT_PLAIN 表示消息持久化,如果rabbitmq服务挂掉,那么未被消费的消息在服务重启后会自己恢复,不会丢失
        //参数4:body(消息内容,字节)
        //channel.basicPublish("","hello",null,"hello rabbitmq".getBytes());
        channel.basicPublish("","hello", MessageProperties.PERSISTENT_TEXT_PLAIN,"hello rabbitmq".getBytes());
        //关闭通道
        /**channel.close();
        connection.close();*/
        RabbitmqUtils.closeChannelAndConnection(channel,connection);
    }
}
