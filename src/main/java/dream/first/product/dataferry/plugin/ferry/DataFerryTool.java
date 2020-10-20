package dream.first.product.dataferry.plugin.ferry;

import java.io.File;
import java.io.IOException;

import org.yelong.core.model.service.SqlModelService;

import dream.first.product.dataferry.core.ferry.DataFerry;

/**
 * 数据摆渡工具。 根据数据文件（XMl、ZIP）进行数据摆渡并记录日志
 */
public interface DataFerryTool extends DataFerry {

	/**
	 * 数据摆渡。该方法内部对解析文件等异常均会记录在日志中
	 * 
	 * @param dataZipFile   数据文件压缩包
	 * @param unZipFilePath 解压文件路径
	 * @param modelService  操作的数据库服务
	 * @throws IOException 流异常
	 * @return 数据摆渡结果
	 */
	void ferryByZip(File dataZipFile, String unZipFilePath, SqlModelService modelService) throws IOException;

}
