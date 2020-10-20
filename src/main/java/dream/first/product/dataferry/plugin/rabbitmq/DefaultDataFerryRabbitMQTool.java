package dream.first.product.dataferry.plugin.rabbitmq;

import org.yelong.support.mq.rabbitmq.RabbitMQConnectionProperties;

/**
 * 默认的数据摆渡MQ工具
 */
public class DefaultDataFerryRabbitMQTool implements DataFerryRabbitMQTool {

	private RabbitMQConnectionProperties rabbitMQConnectionProperties;

	private String dataFerryQueueName;

	@Override
	public RabbitMQConnectionProperties getRabbitMQConnectionProperties() {
		return rabbitMQConnectionProperties;
	}

	@Override
	public void setRabbitMQConnectionProperties(RabbitMQConnectionProperties rabbitMQConnectionProperties) {
		this.rabbitMQConnectionProperties = rabbitMQConnectionProperties;
	}

	@Override
	public String getDataFerryQueueName() {
		return dataFerryQueueName;
	}

	@Override
	public void setDataFerryQueueName(String dataFerryQueueName) {
		this.dataFerryQueueName = dataFerryQueueName;
	}

}
