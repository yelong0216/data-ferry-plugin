package dream.first.product.dataferry.plugin.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.yelong.support.mq.rabbitmq.RabbitMQConnectionProperties;

import dream.first.product.dataferry.plugin.rabbitmq.DataFerryRabbitMQTool;
import dream.first.product.dataferry.plugin.rabbitmq.DefaultDataFerryRabbitMQTool;

public class DataFerryPluginRabbitMQConfiguration {

	@Bean
	public DataFerryRabbitMQTool dataFerryRabbitMQTool(ApplicationContext applicationContext) {
		Environment environment = applicationContext.getEnvironment();
		String dataFerryQueueName = environment.getProperty("dataferry.rabbitmqQueueName");
		RabbitMQConnectionProperties rabbitMQConnectionProperties = new RabbitMQConnectionProperties();
		rabbitMQConnectionProperties.setHost(environment.getProperty("rabbitmq.host"));
		rabbitMQConnectionProperties.setUsername(environment.getProperty("rabbitmq.username"));
		rabbitMQConnectionProperties.setPassword(environment.getProperty("rabbitmq.password"));
		rabbitMQConnectionProperties.setPort(environment.getProperty("rabbitmq.port", Integer.class));
		DataFerryRabbitMQTool dataFerryRabbitMQTool = new DefaultDataFerryRabbitMQTool();
		dataFerryRabbitMQTool.setDataFerryQueueName(dataFerryQueueName);
		dataFerryRabbitMQTool.setRabbitMQConnectionProperties(rabbitMQConnectionProperties);
		return dataFerryRabbitMQTool;
	}

}
