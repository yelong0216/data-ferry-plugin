package dream.first.product.dataferry.plugin.configuration;

import org.springframework.context.annotation.Bean;
import org.yelong.commons.util.ResourcesUtils;
import org.yelong.support.mq.rabbitmq.RabbitMQConnectionProperties;
import org.yelong.support.yaml.YamlProperties;
import org.yelong.support.yaml.YamlWrapper;

import dream.first.product.dataferry.plugin.rabbitmq.DataFerryRabbitMQTool;
import dream.first.product.dataferry.plugin.rabbitmq.DefaultDataFerryRabbitMQTool;

public class DataFerryPluginRabbitMQConfiguration {

	@Bean
	public DataFerryRabbitMQTool dataFerryRabbitMQTool() {
		YamlWrapper yamlWrapper = new YamlWrapper();
		YamlProperties yamlProperties = yamlWrapper.load(ResourcesUtils.getResourceAsStream("dataferry.yml"));
		String dataFerryQueueName = yamlProperties.getProperty("dataferry.rabbitmqQueueName");
		RabbitMQConnectionProperties rabbitMQConnectionProperties = yamlProperties.as("rabbitmq",
				RabbitMQConnectionProperties.class);
		DataFerryRabbitMQTool dataFerryRabbitMQTool = new DefaultDataFerryRabbitMQTool();
		dataFerryRabbitMQTool.setDataFerryQueueName(dataFerryQueueName);
		dataFerryRabbitMQTool.setRabbitMQConnectionProperties(rabbitMQConnectionProperties);
		return dataFerryRabbitMQTool;
	}

}
