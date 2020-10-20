package dream.first.product.dataferry.plugin.configuration.xml;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import dream.first.product.dataferry.core.data.operate.DataObjectSourceOperator;
import dream.first.product.dataferry.core.ferry.DataFerry;
import dream.first.product.dataferry.core.ferry.impl.DefaultDataFerry;
import dream.first.product.dataferry.core.resolve.DataFileResolver;
import dream.first.product.dataferry.plugin.ferry.DataFerryTool;
import dream.first.product.dataferry.plugin.ferry.xml.DefaultXMLDataFerryTool;

public class XMLDataFerryPluginConfiguration {

	// 数据摆渡

	@Bean
	@ConditionalOnMissingBean(ignored = DataFerryTool.class)
	public DataFerry defaultDataFerry(DataFileResolver dataFileResolver,
			DataObjectSourceOperator dataObjectSourceOperator) {
		return new DefaultDataFerry(dataFileResolver, dataObjectSourceOperator);
	}

	@Bean
	@ConditionalOnMissingBean
	public DataFerryTool dataFerryTool(DataFerry dataFerry) {
		return new DefaultXMLDataFerryTool(dataFerry);
	}

}
