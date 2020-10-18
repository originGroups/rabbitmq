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
 * @data 2020/10/18 17:36
 * @Description
 */
public class TopicConsumerMessage3 {
    public static void main(String[] args) throws IOException {
        //获取连接对象
        Connection connection = RabbitmqUtils.getConnection();
        //获取通道对象
        Channel channel = connection.createChannel();
        //通过通道申明交换机
        channel.exchangeDeclare("topics","topic");
        //申明临时队列
        String queue = channel.queueDeclare().getQueue();
        /**
         * 通配符:
         * "*"只匹配一个单词
         * "#"匹配一个或多个单词
         * 单词直接使用"."分割
         */
        String routingKey = "*.user.#";
        //基于动态通配符的形式,通过routingKey动态绑定临时队列和交换机
        channel.queueBind(queue,"topics",routingKey);
        //消费消息
        channel.basicConsume(queue,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者3: " + new String(body));
            }
        });
    }
}
