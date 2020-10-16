package provider;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import org.junit.Test;
import utils.RabbitmqUtils;

import java.io.IOException;

/**
 * @author 袁强
 * @data 2020/10/16 11:16
 * @Description
 */
public class ProviderMessage {

    @Test
    public void testPM() throws IOException {
        //获取连接对象
        Connection connection = RabbitmqUtils.getConnection();
        //获取通道
        Channel channel = connection.createChannel();
        //通道绑定队列: 队列名称,是否持久化,是否独占,是否自动删除,额外参数
        channel.queueDeclare("workQueue",true,false,false,null);
        for(int i = 1; i <= 10; i++){
            //生产消息: 交换机,队列名称,消息额外参数[MessageProperties.PERSISTENT_TEXT_PLAIN持久化],消息体
            channel.basicPublish("","workQueue", MessageProperties.PERSISTENT_TEXT_PLAIN, (i + "--->工作队列模式").getBytes());
        }
        RabbitmqUtils.closeChannelAndConnection(channel,connection);
    }
}
