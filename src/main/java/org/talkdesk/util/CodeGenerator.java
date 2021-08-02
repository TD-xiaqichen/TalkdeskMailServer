package org.talkdesk.util;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.net.URL;
import java.util.Scanner;

public class CodeGenerator {


    /**
     * <p>
     * 	读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 数据源配置--指定需要生成代码的具体数据库
        DataSourceConfig dsConfig = new DataSourceConfig();
        dsConfig.setUrl("jdbc:postgresql://47.108.233.67:5432/emailToCase");
        // dsc.setSchemaName("public");
        dsConfig.setDriverName("org.postgresql.Driver");
        dsConfig.setUsername("postgres");
        dsConfig.setPassword("123456");
        mpg.setDataSource(dsConfig);

        // 包配置
        PackageConfig pkgConfig = new PackageConfig();
//        pkgConfig.setModuleName(scanner("模块名"));
        pkgConfig.setParent("com.td.eas");	// 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
        pkgConfig.setEntity("model.po");
        pkgConfig.setMapper("dao");
        pkgConfig.setXml("mapper");
        pkgConfig.setService("service");
        pkgConfig.setServiceImpl("service.impl");
        pkgConfig.setController("controller");
        mpg.setPackageInfo(pkgConfig);

        // 数据库表配置 -- 可指定需要生成哪些表或者排除哪些表
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);	// 数据库表映射到实体的命名策略
//        strategy.setColumnNaming(NamingStrategy.underline_to_camel);	// 数据库表字段映射到实体的命名策略, 未指定按照 naming 执行
//        strategy.setTablePrefix(pkgConfig.getModuleName() + "_");
        strategy.setTablePrefix("td_em_");
        strategy.containsTablePrefix("td_em_account");

//        strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");	// 自定义继承的Entity类全称，带包名
//        strategy.setSuperEntityColumns("id");	// 自定义基础的Entity类，公共字段
        strategy.setEntityLombokModel(true);	// 【实体】是否为lombok模型（默认 false）
        strategy.setEntityBooleanColumnRemoveIsPrefix(true); // Boolean类型字段是否移除is前缀（默认 false）
        strategy.entityTableFieldAnnotationEnable(true); // 生成字段注解
        
        strategy.setRestControllerStyle(true);	// 生成 @RestController 控制器
//        strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");
//        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
//        strategy.setExclude("act_,qrtz_");
        strategy.setControllerMappingHyphenStyle(true);
        mpg.setStrategy(strategy);
        /********************************************
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        
//        cfg.setFileCreate(new IFileCreate() {
//            @Override
//            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
//                // 判断自定义文件夹是否需要创建
//                checkDir("调用默认方法创建的目录");
//                return false;
//            }
//        });
        
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        *************************************/

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();
        // templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 全局配置
        GlobalConfig glbConfig = new GlobalConfig();
//        String projectPath = System.getProperty("user.dir");
//        glbConfig.setOutputDir(projectPath + "/src/main/java");






        URL url=
                CodeGenerator.class.getClassLoader().getResource("");

        if(url==null){
            return;
        }

        if(!url.getProtocol().equalsIgnoreCase("file")){
            return;
        }


        String tagDir="target/";

        String path=url.getFile();

        int  index=-1;
        if((index=path.indexOf(tagDir))!=-1){
            path=path.substring(0,index);
        }



        glbConfig
        	.setOutputDir(path.concat("src/main/java"))
        	.setAuthor("eas")
        	.setIdType(IdType.ID_WORKER)
        	.setSwagger2(true) // 实体属性 Swagger2 注解
        	.setFileOverride(false)
        	.setMapperName("I%sDAO")
        	.setBaseResultMap(true)
        	.setBaseColumnList(true);
        mpg.setGlobalConfig(glbConfig);

        mpg.execute();
    }

}
