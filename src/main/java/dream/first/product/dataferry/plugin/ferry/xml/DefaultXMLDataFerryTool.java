package dream.first.product.dataferry.plugin.ferry.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yelong.commons.io.FileUtilsE;
import org.yelong.core.model.service.SqlModelService;
import org.yelong.support.tools.zip.ant.ZipUtils;
import org.yelong.util.StopWatch;

import dream.first.product.dataferry.core.data.DataObject;
import dream.first.product.dataferry.core.data.DataObjectSource;
import dream.first.product.dataferry.core.data.operate.DataObjectSourceOperateException;
import dream.first.product.dataferry.core.ferry.DataFerry;
import dream.first.product.dataferry.core.ferry.DataFerryResult;
import dream.first.product.dataferry.core.resolve.DataFileResolveException;
import dream.first.product.dataferry.plugin.ferry.DataFerryTool;

/**
 * 默认的XML数据摆渡工具
 */
public class DefaultXMLDataFerryTool implements DataFerryTool {

	private static final Logger logger = LoggerFactory.getLogger(DefaultXMLDataFerryTool.class);

	private DataFerry dataFerry;

	public DefaultXMLDataFerryTool(DataFerry dataFerry) {
		this.dataFerry = dataFerry;
	}

	@Override
	public DataFerryResult ferry(InputStream inputStream, SqlModelService modelService)
			throws DataFileResolveException, DataObjectSourceOperateException {
		_ferryByInputStream(Arrays.asList(inputStream), modelService);
		return null;
	}

	@Override
	public void ferryByZip(File dataZipFile, String unZipFilePath, SqlModelService modelService) throws IOException {
		ZipUtils.unZipFile(dataZipFile, unZipFilePath);
		File file = FileUtilsE.getFile(unZipFilePath);
		// 获取解压后目录内的所有数据文件。文件是xml格式的即为数据文件
		File[] dataFiles = file.listFiles((x, y) -> {
			return y.endsWith(".xml");
		});
		// 進行同步
		_ferryByFile(Arrays.asList(dataFiles), modelService);
	}

	/**
	 * 数据摆渡。XML文件集合
	 * 
	 * @param dataFiles    XML数据文件集合
	 * @param modelService 模型服务
	 * @throws FileNotFoundException
	 */
	protected void _ferryByFile(List<File> dataFiles, SqlModelService modelService) throws IOException {
		List<InputStream> dataInputStreams = new ArrayList<InputStream>();
		for (File file : dataFiles) {
			dataInputStreams.add(new FileInputStream(file));
		}
		_ferryByInputStream(dataInputStreams, modelService);
	}

	/**
	 * 数据摆渡。XML文件流集合
	 * 
	 * @param dataInputStreams XML数据流集合
	 * @param modelService     模型服务
	 */
	protected void _ferryByInputStream(List<InputStream> dataInputStreams, SqlModelService modelService) {
		StopWatch stopWatch = new StopWatch("数据摆渡");
		for (InputStream dataInputStream : dataInputStreams) {
			// 文件解密
			stopWatch.start();
			StringBuilder logInfo = new StringBuilder();
			String fileName = getFileName(dataInputStream);

			logInfo.append("开始摆渡数据文件“" + fileName + "”...");
			logInfo.append("\n");
			logInfo.append("==========摆渡数据记录统计==========");
			try {
				DataFerryResult dataFerryResult = dataFerry.ferry(dataInputStream, modelService);
				List<DataObjectSource> dataObjectSources = dataFerryResult.getDataObjectSources();
				for (DataObjectSource dataObjectSource : dataObjectSources) {
					logInfo.append("\n");
					logInfo.append(buildLogInfo(dataObjectSource));
				}
				logger.info(logInfo.toString());
			} catch (DataFileResolveException e) {
				logger.error("数据文件解析异常", e);
			} catch (DataObjectSourceOperateException e) {
				logger.error("数据操作(新增、修改)异常", e);
			}
			stopWatch.stop();
			logger.info("数据文件“" + fileName + "”摆渡执行时长：" + stopWatch.getLastTaskTimeSeconds());
		}
		logger.info("数据摆渡结束。此次摆渡运行时长:" + stopWatch.getTotalTimeSeconds());
	}

	protected String getFileName(InputStream inputStream) {
		if (inputStream instanceof FileInputStream) {
			try {
				return (String) FieldUtils.readDeclaredField(inputStream, "path", true);
			} catch (IllegalAccessException e) {
				return "";
			}
		}
		return "";
	}

	/**
	 * 构建日志信息
	 * 
	 * @param dataObjectSource 数据对象源
	 * @return 日志信息
	 */
	protected String buildLogInfo(DataObjectSource dataObjectSource) {
		StringBuilder info = new StringBuilder();
		info.append("tableName:" + dataObjectSource.getTableName() + "\tprimaryKey:" + dataObjectSource.getPrimaryKey()
		+ "\toperationType:" + dataObjectSource.getDataObjectOperationType() + "");
		info.append("\n");
		info.append("操作记录数：" + dataObjectSource.getDataObjects().size());
		List<? extends DataObject> dataObjects = dataObjectSource.getDataObjects();
		for (DataObject dataObject : dataObjects) {
			List<? extends DataObjectSource> dataObjectSourceAttributes = dataObject.getDataObjectSourceAttributes();
			for (DataObjectSource dataObjectSourceAttribute : dataObjectSourceAttributes) {
				info.append("\n");
				info.append(buildLogInfo(dataObjectSourceAttribute));
			}
		}
		return info.toString();
	}

}
