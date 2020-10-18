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
 * @data 2020/10/18 16:49
 * @Description
 */
public class DirectConsumerMessage2 {
    public static void main(String[] args) throws IOException {
        //获取连接对象
        Connection connection = RabbitmqUtils.getConnection();
        //获取通道对象
        Channel channel = connection.createChannel();
        //通过通道绑定交换机
        channel.exchangeDeclare("logs_direct","direct");
        //申明临时队列
        String queue = channel.queueDeclare().getQueue();
        String routingKey = "errors";
        //基于 routingKey 将交换机与临时队列绑定,表示该队列只会取交换机中route key 为 "errors" 的消息
        channel.queueBind(queue,"logs_direct",routingKey);
        //消费消息
        channel.basicConsume(queue,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者2: " + new String(body));
            }
        });
    }
}
