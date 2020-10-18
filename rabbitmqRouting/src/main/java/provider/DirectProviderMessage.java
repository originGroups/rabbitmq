package provider;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitmqUtils;

import java.io.IOException;

/**
 * @author 袁强
 * @data 2020/10/18 16:36
 * @Description
 * 在fanout模式中,一条消息,会被所有订阅的队列都消费,但是,在某些场景下,我们希望不同的消息被不同的队列消费,这时就要用到direct类型的exchange,借助不同的routingKey实现
 */
public class DirectProviderMessage {
    public static void main(String[] args) throws IOException {
        //获取连接对象
        Connection connection = RabbitmqUtils.getConnection();
        //获取通道对象
        Channel channel = connection.createChannel();
        /**
         *通过通道申明交换机:
         * 参数1:exchange 交换机名称
         * 参数2:direct 路由模式Routing
         */
        channel.exchangeDeclare("logs_direct","direct");
        //发送消息
        String routingKey = "info";
        channel.basicPublish("logs_direct",routingKey,null,("这是direct模型发布的基于route key: [" + routingKey + "] 发送的消息").getBytes());
        //释放资源
        RabbitmqUtils.closeChannelAndConnection(channel,connection);

    }
}
