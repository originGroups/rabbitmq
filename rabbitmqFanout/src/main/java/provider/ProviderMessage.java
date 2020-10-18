package provider;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import utils.RabbitmqUtils;

import java.io.IOException;

/**
 * @author 袁强
 * @data 2020/10/18 11:30
 * @Description
 * rabbitmq第三种模型fanout(翻译为扇出)广播,相比较前两种生产者直接通过通道绑定队列的模式,广播则是生产者将消息发送到交换机上,然后交换机将消息广播(分发)到绑定的队列,但也不是说前两种没有用到交换机,rabbitmq的交换机是不可缺少的,只是前两种使用的rabbitmq的默认交换机
 */
public class ProviderMessage {

    public static void main(String[] args) throws IOException {
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();
        /**
         * 将通道绑定到指定的交换机
         * 参数1:交换机名称[随意命名]
         * 参数2:交换机类型[特定参数值] fanout 广播类型
         */
        channel.exchangeDeclare("testExchange","fanout");
        /**
         *发送消息
         * 参数1:exchange 交换机名称
         * 参数2:routingKey 路由key,广播模型下该参数没有意义
         * 参数3:pros 传递消息的额外配置[持久化]
         * 参数4:body 消息体
         */
        channel.basicPublish("testExchange","",null,"fanout type message".getBytes());
        //关闭连接,释放资源
        RabbitmqUtils.closeChannelAndConnection(channel,connection);
    }
}
