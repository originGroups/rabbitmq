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
 * @data 2020/10/16 11:32
 * @Description
 */
public class ConsumerMessage2 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitmqUtils.getConnection();
        Channel channel = connection.createChannel();
        //消费者绑定的队列必须与生产一致
        channel.queueDeclare("workQueue",true,false,false,null);
        //消费消息: 队列名称,是否开启自动确认机制,回调函数
        channel.basicConsume("workQueue",true,new DefaultConsumer(channel){
            /**
             * 覆盖 处理消息时的回调方法
             * 参数1:tag标签
             * 参数2:信封
             * 参数3:传递的属性
             * 参数4:消息体
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费的消息为: "+new String(body));
            }
        });
    }
}
