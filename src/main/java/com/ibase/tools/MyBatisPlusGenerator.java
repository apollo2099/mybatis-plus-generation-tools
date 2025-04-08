package com.ibase.tools;


import cn.hutool.setting.dialect.Props;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.ibase.tools.config.TimerVelocityTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 *
 * https://www.qb5200.com/article/488017.html
 *
 * MyBatisPlus代码生成器
 * 3.5.1
 */
public class MyBatisPlusGenerator {

    public static void main(String[] args) {
        // 获取当前目录路径
        String projectPath = System.getProperty("user.dir");
        String moduleName = "goods";//scanner("模块名");
        String[] tableNames = {};//scanner("表名，多个英文逗号分割").split(",");
        // 代码生成器: 数据源配置
        AutoGenerator autoGenerator = new AutoGenerator(initDataSourceConfig());
        // 全局配置
        autoGenerator.global(initGlobalConfig(projectPath));
        // 包配置，如模块名、实体、mapper、service、controller等
        autoGenerator.packageInfo(initPackageConfig(projectPath, moduleName));
        // 模板配置
        autoGenerator.template(initTemplateConfig());
        // 自定义配置
        autoGenerator.injection(initInjectionConfig(projectPath,moduleName));
        // 策略配置
        autoGenerator.strategy(initStrategyConfig(tableNames));
        // 使用Freemarker引擎模板，默认的是Velocity引擎模板
        //autoGenerator.execute(new VelocityTemplateEngine());
        autoGenerator.execute(new TimerVelocityTemplateEngine());
    }

    /**
     * 读取控制台内容信息
     */
    private static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(("请输入" + tip + "："));
        if (scanner.hasNext()) {
            String next = scanner.next();
            if (StringUtils.isNotEmpty(next)) {
                return next;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    /**
     * 初始化数据源配置
     */
    private static DataSourceConfig initDataSourceConfig() {
        // 读取当前源文件配置
        Props props = new Props("generator.properties");
        // jdbc路径
        String url = props.getStr("dataSource.url");
        // 数据库账号
        String username = props.getStr("dataSource.username");
        // 数据库账号
        String password = props.getStr("dataSource.password");
        return new DataSourceConfig
                .Builder(url, username, password) //设置数据源
                .dbQuery(new MySqlQuery())     //设置数据库查询器
                .build();
    }

    /**
     * 初始化全局配置
     */
    private static GlobalConfig initGlobalConfig(String projectPath) {
        return new GlobalConfig.Builder()
                .outputDir(projectPath + "/src/main/java")//指定生成文件的根目录
                .author("liaoxiongjian")             //Author设置作者
                .disableOpenDir()           //禁止打开输出目录,禁止打开输出目录
                .enableSwagger()            //开启 swagger 模式,开启 swagger 模式
                .fileOverride()             //禁止打开输出目录,默认值：false
                .dateType(DateType.ONLY_DATE)//设置时间类型
                .commentDate("yyyy-MM-dd") //注释日期
                .build();
    }

    /**
     * 初始化包配置
     */
    private static PackageConfig initPackageConfig(String projectPath, String moduleName) {
        Props props = new Props("generator.properties");
        Map<OutputFile, String> pathInfo = new HashMap();
        pathInfo.put(OutputFile.xml, projectPath + "/src/main/resources/mapper/" + moduleName);
        pathInfo.put(OutputFile.other, projectPath + "/src/main/resources/other/" + moduleName);
        return new PackageConfig
                .Builder()
                .parent(props.getStr("package.base"))  // 父包名
                .moduleName(moduleName)      //父包模块名
                .entity("entity")             //实体类 Entity 包名,默认值：entity
                .service("service")          //Service 包名,默认值：entity
                .serviceImpl("service.impl") //实现类 Service Impl 包名	默认值：service.impl
                .mapper("dao")            //Mapper 包名	默认值：mapper
                .xml("mapper.xml")           //Mapper XML 包名	默认值：mapper.xml
                .controller("controller")    //Controller 包名	默认值：controller
                .other("other")              //自定义文件包名	可使用"other"，生产一个other文件目录
                .pathInfo(pathInfo)          //路径配置信息
                .build();
    }

    /**
     * 初始化模板配置
     * 可以对controller、service、entity模板进行配置
     */
    private static TemplateConfig initTemplateConfig() {
        return new TemplateConfig.Builder()
                .entity("/templates/java/entity.java.vm")
                .entityKt("/templates/java/entityEvent.java.vm")
                .service("/templates/java/service.java.vm")
                .serviceImpl("/templates/java/serviceImpl.java.vm")
                .mapper("/templates/java/mapper.java.vm")
                .controller("/templates/java/controller.java.vm")
                .xml("/templates/xml/mapper.xml.vm")
                .build();
    }

    /**
     * 初始化自定义配置
     */
    private static InjectionConfig initInjectionConfig(String projectPath, String moduleName) {
        /**自定义生成模板参数**/
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("moduleName",moduleName);
        paramMap.put("subEntityName","");
        /** 自定义 生成类**/
        Map<String, String> customFileMap = new HashMap();
        //TODO 修改修改模版变量数据
        customFileMap.put("/api/%sApi.js", "templates/js/api.js.vm");
//        customFileMap.put("/%s/index.vue", "templates/vue/index.vue.vm");
        customFileMap.put("/dto/%sEvent.java", "templates/java/entityEvent.java.vm");
        // 自定义配置
        return new InjectionConfig.Builder()
                .beforeOutputFile((tableInfo, objectMap) -> {
                    customFileMap.forEach((key, value) -> {
                        System.out.println(key + " : " + value);
                    });
                    System.out.println("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size());
                    String subEntityName = MyBatisPlusGenerator.toLowerCaseFirstOne(tableInfo.getEntityName());
                    //TODO 模版文件未拿到
                    paramMap.put("subEntityName",subEntityName);
                })
                .customMap(paramMap)
                .customFile(customFileMap)
                .build();
    }

    private static Consumer<InjectionConfig.Builder> getInjectionConfigBuilder() {
        return consumer -> {
        };
    }

    /**
     * 初始化策略配置
     */
    private static StrategyConfig initStrategyConfig(String[] tableNames) {
        StrategyConfig.Builder builder = new StrategyConfig.Builder();
        builder.enableCapitalMode()//开启大写命名,默认值：false
                //.enableSkipView()//开启跳过视图,默认值：false
                //.disableSqlFilter()//禁用sql过滤,默认值：true，语法不能支持使用 sql 过滤表的话，可以考虑关闭此开关
                //.enableSchema()//启用 schema, 默认值：false，多 schema 场景的时候打开
                .addTablePrefix("js_")

                .entityBuilder()
                .naming(NamingStrategy.underline_to_camel)//数据库表映射到实体的命名策略,默认下划线转驼峰命名:NamingStrategy.underline_to_camel
                .columnNaming(NamingStrategy.underline_to_camel)//数据库表字段映射到实体的命名策略,	默认为 null，未指定按照 naming 执行
                //.superClass()//设置父类: BaseEntity.class || com.baomidou.global.BaseEntity
                //.idType(IdType.ASSIGN_ID) //全局主键类型
                .disableSerialVersionUID() // 禁用生成 serialVersionUID,默认值：true
                //.enableColumnConstant()//开启生成字段常量,默认值：false
                //开启链式模型,默认值：false
                .enableChainModel()
                //开启 lombok 模型,默认值：false
                .enableLombok()
                .enableRemoveIsPrefix()//开启 Boolean 类型字段移除 is 前缀,默认值：false
                .enableTableFieldAnnotation()//开启生成实体时生成字段注解,默认值：false
                //.enableActiveRecord()//开启 ActiveRecord 模型,默认值：false
                //.logicDeleteColumnName("deleted")   // 逻辑删除字段名(数据库)
                //.logicDeletePropertyName("deleted") // 逻辑删除属性名(实体)
                //.addTableFills(new Column("create_time", FieldFill.INSERT)) // 自动填充配置  create_time  update_time 两种方式
                //.addTableFills(new Property("updateTime", FieldFill.INSERT_UPDATE))
                //.versionColumnName("version")   // 开启乐观锁
                .formatFileName("%s")//格式化文件名称

                .controllerBuilder()
                //.superClass()//设置父类: BaseController.class || com.baomidou.global.BaseController
                .enableHyphenStyle() //开启驼峰转连字符,默认值：false
                .enableRestStyle()//开启生成@RestController 控制器,默认值：false
                .formatFileName("%sController")//格式化文件名称

                .serviceBuilder()
                //.superServiceClass() //设置 service 接口父类: BaseService.class || com.baomidou.global.BaseService
                //.superServiceImplClass()//设置 service 实现类父类 : BaseServiceImpl.class || com.baomidou.global.BaseServiceImpl
                .formatServiceFileName("%sService")//格式化 service 接口文件名称
                .formatServiceImplFileName("%sServiceImpl")//格式化 service 实现类文件名称


                .mapperBuilder()
                //.superClass()//设置父类: BaseMapper.class || com.baomidou.global.BaseMapper
                .enableMapperAnnotation()//开启 @Mapper 注解,默认值:false
                .enableBaseResultMap()  //启用 BaseResultMap 生成,默认值:false
                .enableBaseColumnList() //启用 BaseColumnList,默认值:false
                //.convertXmlFileName()
                .formatMapperFileName("%sMapper")//格式化 mapper 文件名称
                .formatXmlFileName("%sMapper");//格式化 xml 实现类文件名称


        //当表名中带*号时可以启用通配符模式
        if (tableNames.length == 1 && tableNames[0].contains("*")) {
            String[] likeStr = tableNames[0].split("_");
            String likePrefix = likeStr[0] + "_";
            builder.likeTable(new LikeTable(likePrefix));
        } else {
            builder.addInclude(tableNames);
        }
        return builder.build();
    }


    /**
     * 首字母转为小写
     * @param s
     * @return
     */
    public static String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0))) {
            return s;
        }else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
}

