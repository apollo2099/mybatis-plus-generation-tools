package com.ibase.tools.config;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.io.File;
import java.util.Map;

public class TimerVelocityTemplateEngine extends VelocityTemplateEngine {
    @Override
    protected void outputCustomFile(Map<String, String> customFile, TableInfo tableInfo, Map<String, Object> objectMap) {
        //数据库表映射实体名称
        String entityName = tableInfo.getEntityName();

        String otherPath = this.getPathInfo(OutputFile.other);
//        System.out.println(JsonUtils.toJSONString(tableInfo));

        //数据库表映射实体名称 驼峰命名法
        objectMap.put("humpEentityName",toLowerCaseFirstOne(entityName));

        customFile.forEach((key, value) -> {
            String fileName = String.format(otherPath + File.separator +key,entityName);
            this.outputFile(new File(fileName), objectMap, value);
            System.out.println("fileName="+fileName);
        });
    }

    /**
     * 首字母转为小写
     * @param s
     * @return
     */
    private  String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0))) {
            return s;
        }else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

}

