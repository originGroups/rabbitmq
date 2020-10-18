package provider;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitmqUtils;

import java.io.IOException;

/**
 * @author 袁强
 * @data 2020/10/18 17:20
 * @Description
 * Topic类型的交换机exchange与direct相比,都可以根据routingKey把消息路由到不同的队列中,只不过topic类型的exchange可以让队列在绑定route key 的时候使用通配符.
 * 这种模型的routingKey一般是由一个或多个单词组成,多个单词之间使用"."分割
 * 这里的通配符有两种:
 * "*"星号表示只匹配一个单词
 * "#"井号匹配一个或多个单词
 */
public class TopicProviderMessage {
    public static void main(String[] args) throws IOException {
        //获取连接对象
        Connection connection = RabbitmqUtils.getConnection();
        //获取通道对象
        Channel channel = connection.createChannel();
        //通过通道申明交换机
        channel.exchangeDeclare("topics","topic");
        String routingKey = "user.save";
        //发送消息
        channel.basicPublish("topics",routingKey,null,("这里是topic动态路由模型,route key: [" + routingKey + "]").getBytes());
        //关闭释放资源
        RabbitmqUtils.closeChannelAndConnection(channel,connection);
    }
}
