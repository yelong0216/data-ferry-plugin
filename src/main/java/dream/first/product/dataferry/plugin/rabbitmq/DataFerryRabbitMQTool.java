/**
 * 
 */
package dream.first.product.dataferry.plugin.rabbitmq;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.FileUtils;
import org.yelong.support.mq.rabbitmq.RabbitMQConnectionProperties;
import org.yelong.support.mq.rabbitmq.RabbitMQUtils;

import com.rabbitmq.client.Connection;

/**
 * 数据摆渡MQ工具
 */
public interface DataFerryRabbitMQTool {

	/**
	 * @return MQ连接配置
	 */
	RabbitMQConnectionProperties getRabbitMQConnectionProperties();

	/**
	 * @param rabbitMQConnectionProperties MQ连接配置
	 * @return
	 */
	void setRabbitMQConnectionProperties(RabbitMQConnectionProperties rabbitMQConnectionProperties);

	/**
	 * @return 数据摆渡对列名称
	 */
	String getDataFerryQueueName();

	/**
	 * @param dataFerryQueueName 数据摆渡对列名称
	 */
	void setDataFerryQueueName(String dataFerryQueueName);

	/**
	 * 创建数据摆渡MQ连接
	 * 
	 * @return MQ连接
	 */
	default Connection createDataFerryRabbitMQConnection() throws IOException, TimeoutException {
		return RabbitMQUtils.createConnection(getRabbitMQConnectionProperties());
	}

	/**
	 * 将数据摆渡的数据添加到MQ对列中
	 * 
	 * @param dataFerryFile 需要进行数据摆渡的数据文件。这应该是一个压缩包
	 */
	default void basicPublishDataFerryFile(File dataFerryFile) throws IOException, TimeoutException {
		basicPublishDataFerryFile(FileUtils.readFileToByteArray(dataFerryFile));
	}
	
	/**
	 * 将数据摆渡的数据添加到MQ对列中
	 * 
	 * @param dataFerryFileBytes 需要进行数据摆渡的数据文件流
	 */
	default void basicPublishDataFerryFile(byte[] dataFerryFileBytes) throws IOException, TimeoutException {
		Connection connection = createDataFerryRabbitMQConnection();
		RabbitMQUtils.basicPublish(connection, getDataFerryQueueName(), dataFerryFileBytes);
		connection.close();
	}

}
