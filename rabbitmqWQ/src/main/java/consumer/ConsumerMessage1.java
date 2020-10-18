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
 * 工作模式队列:
 *       区别与点对点模式的一对一,工作模式队列为一对多,即一个生产者对应多个消费者,该模式默认情况,RabbitMQ将按顺序将每个消息发送给下一个消费者,平均而言,每个消费者都会收到相同数量的消息,这种分发消息的方式称为循环.
 *       如果某个消费者执行效率低下,那么分派给它的消息就会执行缓慢,造成消息堆积,所以应该按劳分派,能者多劳,手动确认消息
 *
 *       默认的循环分发消息的方式原理:消费者开启自动确认机制,相当于提前告诉rabbitmq服务器已经消费了消息,这样rabbitmq就会把分发给消费者的消息从队列中删除,但是由于消费者消费消息需要时间,所以消费者通道就存储了rabbitmq服务器分发的全部消息;
 *       注意!!!rabbitmq服务器只有收到了来自消费者的确认,这样服务器才会将该消息从队列中删除
 *       因此改变这种默认模式需要从三方面入手:1.设置通道每次只能消费一个消息;2.关闭自动确认机制;3.手动确认
 */
public class ConsumerMessage1 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitmqUtils.getConnection();
        final Channel channel = connection.createChannel();
        //设置通道每次只能消费一个消息[一次只能接受一条为确认的消息]
        channel.basicQos(1);
        //消费者绑定的队列必须与生产一致
        channel.queueDeclare("workQueue",true,false,false,null);
        //消费消息: 队列名称,是否开启自动确认机制 false表示关闭自动确认机制, 回调函数
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
