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
 * @data 2020/10/18 15:47
 * @Description
 */
public class ConsumerMessage3 {
    public static void main(String[] args) throws IOException {
        //获取连接对象
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();
        //将通道绑定交换机
        channel.exchangeDeclare("testExchange","fanout");
        //申明临时队列
        String queue = channel.queueDeclare().getQueue();
        //将队列和交换机绑定
        channel.queueBind(queue,"testExchange","");
        //消费消息
        channel.basicConsume(queue,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者3: " + new String(body));
            }
        });
    }
}
