package com.malaxg.codegenerator.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.malaxg.codegenerator.FileUtil;
import com.malaxg.codegenerator.Generator;
import com.malaxg.codegenerator.StringUtil;
import com.malaxg.codegenerator.constant.Mapping;
import com.malaxg.codegenerator.constant.SwaggerUI;
import com.malaxg.codegenerator.domain.FieldType;
import com.malaxg.codegenerator.domain.MysqlColumnInfo;
import com.malaxg.codegenerator.generator.MethodName;
import com.malaxg.codegenerator.generator.MethodSignatureGenerator;
import com.malaxg.codegenerator.generator.RemarksGenerator;
import com.malaxg.codegenerator.generator.ReturnGenerator;
import com.malaxg.codegenerator.httpcontroller.GenerateHttpController;
import com.malaxg.codegenerator.service.MysqlColumnInfoJpaService;
import com.malaxg.hardwork.util.DataVerificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * hardwork
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RestController
public class GenerateCodeTemplateController
{
    @Autowired
    private MysqlColumnInfoJpaService mysqlColumnInfoService;
    
    @Autowired
    private GenerateHttpController generateHttpController;
    
    private final Map dataTypeMap = new HashMap<String, String>();

    {
        dataTypeMap.put("bigint", "Long");
        dataTypeMap.put("double", "double");
        dataTypeMap.put("varchar", "String");
        dataTypeMap.put("longtext", "String");
        dataTypeMap.put("int", "int");
        dataTypeMap.put("timestamp", "Date");
        dataTypeMap.put("datetime", "Date");
    }

    @PostMapping("/generateCodeTemplate")
    public void generatorCodeTemplate(@RequestBody Generator generator) throws Exception
    {
        System.out.println(generator.getAuthor() + "-------------");
        String className = generator.getClassName();
        generator.setClassName(StringUtil.toUpperCaseFirstOne(className));
        //https://www.cnblogs.com/huanzi-qch/p/10281773.html
        List<MysqlColumnInfo> columnInfos = mysqlColumnInfoService
                .findMysqlColumnInfo(StringUtil.underscoreName(generator.getClassName()), generator.getDatabase());

        createPojo(generator, columnInfos);
        createVo(generator, columnInfos);
        createController(generator, columnInfos);
        createDao(generator, columnInfos);
        createService(generator, columnInfos);
        createServiceImpl(generator, columnInfos);
    }
    
    private void createPojo(Generator generator, List<MysqlColumnInfo> columnInfos)
    {
        String author = generator.getAuthor();
        String className = generator.getClassName();
        File file = FileUtil.createFile(author, className);
    
        StringBuffer line = new StringBuffer();
        //  设置类注释模板
        line.append("import javax.persistence.*;\n");
        long hasDateType =
            columnInfos.stream().filter(k -> "timestamp".equals(k.getDataType()) || "datetime".equals(k.getDataType()))
                .count();
        if (hasDateType > 0)
        {
            line.append("import java.util.Date;\n");
        }
    
        appendClassRemarksTemplate(line, className, author);
        line.append("@Entity\n" +
            "@Table(name = \"" + StringUtil.underscoreName(className) + "\")\n");
        line.append("public class " + className + "\n{\n");
        // 遍历设置属性
        for (MysqlColumnInfo columnInfo : columnInfos)
        {
            //主键
            if ("PRI".equals(columnInfo.getColumnKey()))
            {
                line.append("    @Id\n");
                //自增
                line.append("    @GeneratedValue(strategy= GenerationType.IDENTITY)\n");
            }
            line.append(
                "    @Column(name = \"" + columnInfo.getColumnName() + "\", columnDefinition = \"" + columnInfo
                    .getColumnType());
            if (columnInfo.getColumnDefault() != null)
            {
                line.append(" default " + columnInfo.getColumnDefault());
            }
    
            if (columnInfo.getColumnComment() != null)
            {
                line.append(" comment '" + columnInfo.getColumnComment() + "'\"");
            }
            if ("UNI".equals(columnInfo.getColumnKey()))
            {
                line.append(", unique = true ");
            }
            line.append(")\n");
            line.append("    private " + dataTypeMap.get(columnInfo.getDataType()) + " " + StringUtil
                .camelCaseName(columnInfo.getColumnName()) + ";\n\n");
        }
        line.append("}");
        FileUtil.fileWriter(file, line);
    }
    
    private void createVo(Generator generator, List<MysqlColumnInfo> columnInfos)
    {
        String author = generator.getAuthor();
        String className = generator.getClassName() + "Vo";
        File file = FileUtil.createFile(author, className);

        StringBuffer line = new StringBuffer();
        //  设置类注释模板
        long hasDateType =
                columnInfos.stream().filter(k -> "timestamp".equals(k.getDataType()) || "datetime".equals(k.getDataType()))
                        .count();
        if (hasDateType > 0) {
            line.append("import java.util.Date;\n");
        }
        appendClassRemarksTemplate(line, className, author);
        line.append("public class " + className + "\n{\n");
        //遍历设置属性
        appendFieldsTemplate(line, columnInfos);
        line.append("}");
        FileUtil.fileWriter(file, line);
    }
    
    private void createController(Generator generator, List<MysqlColumnInfo> columnInfos)
    {
        String author = generator.getAuthor();
        String className = generator.getClassName() + "Controller";
        File file = FileUtil.createFile(author, className);
    
        StringBuffer line = new StringBuffer();
        //  设置导入包
        // import
        line.append("import java.util.List;\n"
            + "\n"
            + "import com.huawei.allinone.web.common.RestResult;\n"
            + "import io.swagger.annotations.ApiOperation;\n"
            + "import org.springframework.beans.factory.annotation.Autowired;\n"
            + "import org.springframework.web.bind.annotation.DeleteMapping;\n"
            + "import org.springframework.web.bind.annotation.GetMapping;\n"
            + "import org.springframework.web.bind.annotation.PostMapping;\n"
            + "import org.springframework.web.bind.annotation.RequestBody;\n"
            + "import org.springframework.web.bind.annotation.RequestMapping;\n"
            + "import org.springframework.web.bind.annotation.RequestParam;\n"
            + "import org.springframework.web.bind.annotation.RestController;");
        //  设置类注释模板
        appendClassRemarksTemplate(line, className, author);
        line.append("@RestController\n");
    
        if (DataVerificationUtil.isEmpty(generator.getRestControllerUrl()))
        {
            line.append("@RequestMapping(\"/" + generator.getClassName().toLowerCase() + "\")\n");
        }
        else
        {
            line.append(
                "@RequestMapping(\"/" + generator.getRestControllerUrl().toLowerCase() + "/" + generator.getClassName()
                    .toLowerCase()
                    + "\")\n");
        }
    
        line.append("public class " + className + "\n{\n");
        line.append("\t@Autowired\n"
            + "    private " + generator.getClassName() + "Service " + StringUtil
            .toLowerCaseFirstOne(generator.getClassName()) + "Service;\n\n");
        //  设置
        setControllerMethod(line, generator, columnInfos);
        line.append("}");
        FileUtil.fileWriter(file, line);
    }
    
    private void createDao(Generator generator, List<MysqlColumnInfo> columnInfos)
    {
        String author = generator.getAuthor();
        String className = generator.getClassName() + "Dao";
        File file = FileUtil.createFile(author, className);
    
        StringBuffer line = new StringBuffer();
        line.append("import org.springframework.data.jpa.repository.JpaRepository;\n");
        line.append("import org.springframework.stereotype.Repository;");
        //  设置类注释模板
        appendClassRemarksTemplate(line, className, author);
        line.append("@Repository\n");
    
        line.append("public interface " + className + " extends JpaRepository<" + generator.getClassName() + ", "
            + getPrimaryType(columnInfos) + ">\n{\n");
        line.append("}");
        FileUtil.fileWriter(file, line);
    }
    
    private void createService(Generator generator, List<MysqlColumnInfo> columnInfos)
    {
        String author = generator.getAuthor();
        String className = generator.getClassName() + "Service";
        File file = FileUtil.createFile(author, className);
        
        StringBuffer line = new StringBuffer();
        line.append("import com.huawei.allinone.service.BaseJpaService;\n");
        //  设置类注释模板
        appendClassRemarksTemplate(line, className, author);
        line.append(
            "public interface " + className + " extends BaseJpaService<" + generator.getClassName() + ", "
                + getPrimaryType(
                columnInfos) + ">\n{\n");
        
        // 设置默认service方法
    
        line.append("}");
        FileUtil.fileWriter(file, line);
    }
    
    private String getPrimaryType(List<MysqlColumnInfo> columnInfos)
    {
        List<MysqlColumnInfo> collect =
            columnInfos.stream().filter(k -> "PRI".equals(k.getColumnKey())).collect(Collectors.toList());
        return dataTypeMap.get(collect.get(0).getDataType()).toString();
    }
    
    private String getPrimaryColumnName(List<MysqlColumnInfo> columnInfos)
    {
        List<MysqlColumnInfo> collect =
                columnInfos.stream().filter(k -> "PRI".equals(k.getColumnKey())).collect(Collectors.toList());
        String columnName = collect.get(0).getColumnName();
        String s = StringUtil.camelCaseName(columnName);
        collect.get(0).setColumnName(s);
        return s;
    }
    
    private void createServiceImpl(Generator generator, List<MysqlColumnInfo> columnInfos)
    {
        String author = generator.getAuthor();
        String className = generator.getClassName() + "ServiceImpl";
        File file = FileUtil.createFile(author, className);
    
        StringBuffer line = new StringBuffer();
        //  设置类注释模板
        line.append("import java.util.ArrayList;\n"
            + "import java.util.List;\n"
            + "\n"
            + "import com.huawei.allinone.utils.ParameterUtil;\n"
            + "import com.huawei.allinone.web.common.RestResult;\n"
            + "import org.springframework.beans.factory.annotation.Autowired;\n"
            + "import org.springframework.stereotype.Service;\n");
        line.append(RemarksGenerator.generateClassRemarks(className, author));
    
        line.append("@Service\n");
        line.append("public class " + className + " implements " + generator.getClassName() + "Service" + "\n{\n");
        line.append("\t@Autowired\n"
            + "    private " + generator.getClassName() + "Dao " + StringUtil
            .toLowerCaseFirstOne(generator.getClassName()) + "Dao;\n\n");
        // 设置默认service方法
        setServiceImplMethod(line, generator, columnInfos);
        line.append("}");
        FileUtil.fileWriter(file, line);
    }
    
    private void setServiceImplMethod(StringBuffer line, Generator generator, List<MysqlColumnInfo> columnInfos)
    {
        line.append("\t@Override\n");
        String primaryType = getPrimaryType(columnInfos);
        String className = generator.getClassName();
        String classNameLowerCaseFirstOneDao = StringUtil.toLowerCaseFirstOne(className) + "Dao";
        String primaryField = null;
        if ("Long".equals(primaryType))
        {
            primaryField = "l";
        }
        else if ("String".equals(primaryType))
        {
            primaryField = "s";
        }
        // findOne 单个查询
        List<FieldType> findOneParams = new ArrayList<>();
        findOneParams.add(new FieldType(primaryType, primaryField));
        line.append(MethodSignatureGenerator.generateContainsReturn(MethodName.FIND_ONE.getMethodName(),
            findOneParams, null));
        line.append(
            "\t\tList<" + className + "> " + StringUtil.toLowerCaseFirstOne(className) + " = new ArrayList<>();\n");
        line.append(
            "\t\t" + StringUtil.toLowerCaseFirstOne(className) + ".add(" + classNameLowerCaseFirstOneDao + "."
                + MethodName.FIND_ONE.getMethodName() + "(" + primaryField + "));\n");
        line.append("\t\treturn RestResult.ok(" + StringUtil.toLowerCaseFirstOne(className) + ");\n\t}\n\n");
    
        // findAll
        line.append("\t@Override\n");
        line.append("\tpublic RestResult findAll()\n\t{\n");
        line.append("\t\treturn RestResult.ok(" + classNameLowerCaseFirstOneDao + ".findAll());\n\t}\n\n");
    
        // deleteByIds
        line.append("\t@Override\n");
        line.append("\tpublic void deleteByIds(List<" + primaryType + "> ids)"
            + "\n\t{\n");
        line.append("\t\tif (ParameterUtil.isEmpty(ids))\n"
            + "\t\t{\n"
            + "\t\t\treturn;\n"
            + "\t\t}\n"
            + "\n"
            + "\t\tids.forEach(k -> " + classNameLowerCaseFirstOneDao + ".delete(k));\n\t}\n\n");
    
        // save
        line.append("\t@Override\n");
        List<FieldType> saveParams = new ArrayList<>();
        saveParams.add(new FieldType(className, "entity"));
        line.append(MethodSignatureGenerator.generateContainsReturn(MethodName.SAVE.getMethodName(),
            saveParams, null));
        line.append(
            "\t\tList<" + className + "> " + StringUtil.toLowerCaseFirstOne(className) + " = new ArrayList<>();\n");
        line.append(
            "\t\t" + StringUtil.toLowerCaseFirstOne(className) + ".add(" + classNameLowerCaseFirstOneDao + "."
                + MethodName.SAVE.getMethodName() + "(entity));\n");
        line.append("\t\treturn RestResult.ok(" + StringUtil.toLowerCaseFirstOne(className) + ");\n\t}\n\n");
    }
    
    private void setControllerMethod(StringBuffer line, Generator generator, List<MysqlColumnInfo> columnInfos)
    {
        //主键类型 Long
        String primaryType = getPrimaryType(columnInfos);
        //主键变量 id
        String primaryField = StringUtil.camelCaseName(getPrimaryColumnName(columnInfos));
        //类名 CooperationProjects
        String className = generator.getClassName();
        //类变量 cooperationProjects
        String classNameLowerCaseFirstOne = StringUtil.toLowerCaseFirstOne(className);
        //类变量 cooperationProjectsService
        String classNameLowerCaseFirstOneService = StringUtil.toLowerCaseFirstOne(className + "Service");
    
        // @GetMapping 单个查询
        List<FieldType> getMappingParams = new ArrayList<>();
        getMappingParams.add(new FieldType(getPrimaryType(columnInfos), getPrimaryColumnName(columnInfos)));
        line.append(
            RemarksGenerator.generateMethodRemarksContainsReturn(MethodName.FIND_ONE.getRemarks(), getMappingParams,
                generator.getAuthor()));
        line.append(Mapping.GetMapping.line());
        line.append(SwaggerUI.ApiOperation.line(MethodName.FIND_ONE.getRemarks()));
        line.append(MethodSignatureGenerator.generateContainsReturn(MethodName.FIND_ONE.getMethodName(),
            getMappingParams, "@RequestParam"));
        line.append(ReturnGenerator
            .generatorContainsReturn(classNameLowerCaseFirstOneService, MethodName.FIND_ONE.getMethodName(),
                getMappingParams));
    
        // @DeleteMapping 根据主键删除
        List<FieldType> deleteMappingParams = new ArrayList<>();
        deleteMappingParams.add(new FieldType("List<" + getPrimaryType(columnInfos) + ">",
            getPrimaryColumnName(columnInfos) + "s"));
        line.append(
            RemarksGenerator.generateMethodRemarks(MethodName.DELETE_BY_ID.getRemarks(), deleteMappingParams,
                generator.getAuthor()));
        line.append(Mapping.DeleteMapping.line());
        line.append(SwaggerUI.ApiOperation.line(MethodName.DELETE_BY_ID.getRemarks()));
        line.append(
            MethodSignatureGenerator.generateVoid(MethodName.DELETE_BY_ID.getMethodName(), deleteMappingParams,
                "@RequestParam"));
        line.append(ReturnGenerator
            .generatorVoid(classNameLowerCaseFirstOneService,
                MethodName.DELETE_BY_ID.getMethodName(), deleteMappingParams));
    
        // @PostMapping 添加
        List<FieldType> postMappingParams = new ArrayList<>();
        postMappingParams.add(new FieldType(className, classNameLowerCaseFirstOne));
        line.append(
            RemarksGenerator.generateMethodRemarksContainsReturn(MethodName.SAVE.getRemarks(), postMappingParams,
                generator.getAuthor()));
        line.append(Mapping.PostMapping.line());
        line.append(SwaggerUI.ApiOperation.line(MethodName.SAVE.getRemarks()));
        line.append(MethodSignatureGenerator.generateContainsReturn(MethodName.SAVE.getMethodName(),
            postMappingParams, "@RequestBody"));
        line.append(ReturnGenerator.generatorContainsReturn(classNameLowerCaseFirstOneService,
            MethodName.SAVE.getMethodName(), postMappingParams));
    }
    
    /**
     * 设置类的注释模板
     */
    private void appendClassRemarksTemplate(StringBuffer line, String className, String author)
    {
        line.append("\n" +
            "/**\n"
            + " * " + className + "\n"
            + " * \n"
            + " * @author " + author + "\n"
            + " * @version [版本号, 2099年12月31日]\n"
            + " * @see [相关类/方法]\n"
            + " * @since [产品/模块版本]\n"
            + " */\n");
    }
    
    
    /**
     * 获取所有属性模板
     */
    private void appendFieldsTemplate(StringBuffer stringBuffer, List<MysqlColumnInfo> columnInfos)
    {
        for (MysqlColumnInfo columnInfo : columnInfos)
        {
            stringBuffer.append("\t/** " + columnInfo.getColumnComment() + " */\n");
            stringBuffer.append("    private " + dataTypeMap.get(columnInfo.getDataType()) + " " + StringUtil
                    .camelCaseName(columnInfo.getColumnName()) + ";\n\n");
        }
    }
}
