package consumer;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import utils.RabbitmqUtils;

import java.io.IOException;

/**
 * @author 袁强
 * @data 2020/10/18 15:24
 * @Description
 */
public class ConsumerMessage1 {
    public static void main(String[] args) throws IOException {
        //获取连接对象
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();
        /**
         * 通道绑定指定交换机
         * 参数1:exchange 交换机名称
         * 参数2:BuiltinExchangeType 交换机类型
         */
        channel.exchangeDeclare("testExchange","fanout");
        //使用临时队列,广播模型使用临时队列即可,有消息时创建,没有则删除队列
        String queue = channel.queueDeclare().getQueue();
        /**
         * 绑定临时队列和交换机
         * Declare表示申明创建,这里使用Bind将交换机和队列进行绑定
         * 参数1:queue 临时队列
         * 参数2:exchange 交换机
         * 参数3:routingKey fanout广播路由参数无意义
         */
        channel.queueBind(queue,"testExchange","");
        /**
         * 消费消息:
         * 参数1:queue 临时队列
         * 参数2:autoAck 是否开启自动确认
         * 参数3:callback 回调函数
         */
        channel.basicConsume(queue,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者1: " + new String(body));
            }
        });

    }
}
