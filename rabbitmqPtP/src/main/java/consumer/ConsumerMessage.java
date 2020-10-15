package consumer;

import com.rabbitmq.client.*;
import utils.RabbitmqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerMessage {
    /**
     * 测试rabbitmq点对点模型:
     * 消费消息
     */
    /*@Test
    public void testCM() throws IOException, TimeoutException {

        //获取连接mq的连接工厂对象
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置连接的rabbitmq主机
        connectionFactory.setHost("10.15.0.9");
        //设置tcp通信端口
        connectionFactory.setPort(5672);
        //设置连接的虚拟主机
        connectionFactory.setVirtualHost("/ems");
        //设置访问虚拟主机的权限
        connectionFactory.setUsername("ems");
        connectionFactory.setPassword("123456");
        //获取连接mq的连接对象
        Connection connection = connectionFactory.newConnection();
        //获取连接中的通道
        Channel channel = connection.createChannel();
        //通道绑定队列
        channel.queueDeclare("hello",false,false,false,null);
        //消费消息
        channel.basicConsume("",false,Cons);
        *//**
         * 由于消费者需要时刻监听消息队列是否有未消费的消息,所以需要保持通信状态,不建议关闭,如果关闭会导致消息消费掉但是却来不及执行回调函数;
         * 注意!!!使用junit测试类也会导致消息即使消费掉,但是回调函数却无法执行,原因:@Test测试类也是执行完,主线程就被kill掉,从而来不及执行回调函数,所以不能写测试类进行测试,通过main函数进行验证
         * channel.close();
         * connection.close();
         *//*
    }*/
    public static void  main(String[] args) throws IOException, TimeoutException {
        /*//获取连接mq的连接工厂对象
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置连接的rabbitmq主机
        connectionFactory.setHost("10.15.0.9");
        //设置tcp通信端口
        connectionFactory.setPort(5672);
        //设置连接的虚拟主机
        connectionFactory.setVirtualHost("/ems");
        //设置访问虚拟主机的权限
        connectionFactory.setUsername("ems");
        connectionFactory.setPassword("123456");
        //获取连接mq的连接对象
        Connection connection = connectionFactory.newConnection();*/

        Connection connection = RabbitmqUtils.getConnection();
        //获取连接中的通道
        Channel channel = connection.createChannel();
        /**
         * 通道绑定队列
         * 参数1:queue(队列名称),如果该队列不存在则会自动创建
         * 参数2:定义队列特性是否要持久化,true表示要持久化队列
         * 参数3:exclusive(排除)是否独占队列,true表示该队列只能被当前通道所占用,false表示不独占队列
         * 参数4:autoDelete 是否在消费完后自动删除队列,false表示不自动删除
         * 参数5:额外参数
         */
        channel.queueDeclare("hello",false,false,false,null);
        /**
         * 消费消息
         * 参数1:queue消费哪个队列的消息
         * 参数2:autoAck开启消息的自动确认机制
         * 参数3:消息消息时的回调接口
         */
        channel.basicConsume("hello",true,new DefaultConsumer(channel){

            /**
             * 覆盖 处理消息时的回调方法
             * 参数1:tag标签
             * 参数2:信封
             * 参数3:传递的属性
             * 参数4:消息体
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费的消息为: " + new String(body));
            }
        });
    }
}
