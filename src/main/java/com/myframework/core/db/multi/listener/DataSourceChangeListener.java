package com.myframework.core.db.multi.listener;

import com.myframework.core.db.multi.MutiDataSourceRouter;
import com.myframework.core.zookeeper.listener.custom.ZkNodeListener;
import com.myframework.core.zookeeper.model.ZkNodeData;
import com.myframework.util.StringUtil;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;/**/

public class DataSourceChangeListener implements ZkNodeListener {
    private static Logger logger = LoggerFactory.getLogger(DataSourceChangeListener.class);

    // 数据源变更
    public static final String DATASOURCE_CHANGE = "/myframework/datasource/change";

    @Override
    public boolean accept(ZkNodeData arg0) {
        return true;
    }

    @Override
    public void childAdded(ZkNodeData arg0) {
        logger.info("监控到数据源节点变更childAdded：{}", getMonitorPath());
    }

    @Override
    public void childDeleted(ZkNodeData arg0) {
        logger.info("监控到数据源节点变更childDeleted：{}", getMonitorPath());

    }

    @Override
    public void childUpdated(ZkNodeData arg0) {
        logger.info("监控到数据源节点变更childUpdated：{}", getMonitorPath());

    }

    @Override
    public String getMonitorPath() {
        return DATASOURCE_CHANGE;
    }

    @Override
    public void nodeUpdated(ZkNodeData value) {
        String dat = value.getValue();
        logger.info("监控到数据源节点变更：{}, {}", getMonitorPath(), dat);
        if (StringUtil.isEmpty(dat)) {
            return;
        }
        JSONArray ja = JSONArray.fromObject(dat);
        if (ja != null) {
            for (int i = 0; i < ja.size(); i++) {
                String dbKey = ja.getString(i);
                logger.info("监控到数据源节点变更,注销数据源：{}", dbKey);
                MutiDataSourceRouter.removeDataSourcesByKey(dbKey);
            }
        }

    }

}
