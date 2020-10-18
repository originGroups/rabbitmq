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
        final Channel channel = connection.createChannel();
        //设置通道一次只能消费一个消息[一次只能接受一条为确认的消息]
        channel.basicQos(1);
        //消费者绑定的队列必须与生产一致
        channel.queueDeclare("workQueue",true,false,false,null);
        //消费消息: 队列名称,是否开启自动确认机制,回调函数
       //channel.basicConsume("workQueue",true,new DefaultConsumer(channel){
        //FALSE表示关闭自动确认机制
         channel.basicConsume("workQueue",false,new DefaultConsumer(channel){
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
                /**
                 *手动确认消息:
                 * 参数1:确认队列中具体哪个消息,envelope对象有一个tag标记属性从而标记每一个消息
                 * 参数2:是否开启多个消息同时确认[当前通道每次只消费一个消息,所以设置成false]
                 */
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        });
    }
}
