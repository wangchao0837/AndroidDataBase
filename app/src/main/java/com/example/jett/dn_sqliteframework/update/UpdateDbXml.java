package com.example.jett.dn_sqliteframework.update;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: UpdateDbXml
 * @Description: 升级更新数据库
 *
 */
public class UpdateDbXml {
    /**
     * 升级脚本列表
     */
    private List<UpdateStep> updateSteps;
    /**
     * 升级版本
     */
    private List<CreateVersion> createVersions;

    public UpdateDbXml(Document document) {
        {
            // 获取升级脚本
            NodeList updateSteps = document.getElementsByTagName("updateStep");
            this.updateSteps = new ArrayList<UpdateStep>();
            for (int i = 0; i < updateSteps.getLength(); i++) {
                Element ele = (Element) (updateSteps.item(i));
                UpdateStep step = new UpdateStep(ele);
                this.updateSteps.add(step);
            }
        }
        {
            /**
             * 获取各升级版本
             */
            NodeList createVersions = document.getElementsByTagName("createVersion");
            this.createVersions = new ArrayList<CreateVersion>();
            for (int i = 0; i < createVersions.getLength(); i++) {
                Element ele = (Element) (createVersions.item(i));
                Log.i("jett","各个升级的版本:"+ele.toString());
                CreateVersion cv = new CreateVersion(ele);
                this.createVersions.add(cv);
            }
        }
    }

    public List<UpdateStep> getUpdateSteps() {
        return updateSteps;
    }

    public void setUpdateSteps(List<UpdateStep> updateSteps) {
        this.updateSteps = updateSteps;
    }

    public List<CreateVersion> getCreateVersions() {
        return createVersions;
    }

    public void setCreateVersions(List<CreateVersion> createVersions) {
        this.createVersions = createVersions;
    }

}
