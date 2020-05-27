package com.malaxg.codegenerator.httpcontroller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.malaxg.hardwork.util.DataVerificationUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
public class GenerateHtController
{
    
    /**
     * 判断class是否是基本类型
     *
     * @param o Class
     * @return boolean
     */
    public static boolean isBaseType(Class o)
    {
        String className = o.toString().substring(o.toString().indexOf(" ") + 1);
        return "java.lang.Integer".equals(className) ||
            "java.lang.Byte".equals(className) ||
            "java.lang.Long".equals(className) ||
            "java.lang.Double".equals(className) ||
            "java.lang.Float".equals(className) ||
            "java.lang.Character".equals(className) ||
            "java.lang.Short".equals(className) ||
            "java.lang.Boolean".equals(className) ||
            "java.lang.String".equals(className);
    }
    
    /**
     *
     * @param appointFile 如果传入该参数，则只生成指定Controoller的HTTP文件，多个文件以,隔开
     * @param fileSuffix 需要被处理的Controller文件后缀名，默认为Controller
     */
    @GetMapping("/generatorHttpFile134")
    public void generatorHttpFile(@RequestParam(value = "appointFile") String appointFile,
        @RequestParam(value = "fileSuffix", required = false, defaultValue = "Controller") String fileSuffix)
    {
        generatorFile(appointFile, fileSuffix);
    }
    
    public void generatorFile(String appointFile, String fileSuffix)
    {
        // 获取当前类所在包名
        String packageName = GenerateHtController.class.getPackage().getName();
        // 获取包下所有的fileSuffix（Controller）的java文件
        File file = new File("src\\main\\java\\" + packageName.replace(".", "\\"));
        File[] files = null;
        if(DataVerificationUtil.isEmpty(appointFile)){
            files = file.listFiles(
                    (dir, name) -> name.endsWith(fileSuffix + ".java") && !name.equals("GenerateHttpController.java"));
        }
        else {
            String[] split = appointFile.split(",");
            Set<String> collect = Arrays.asList(split).stream().collect(Collectors.toSet());
            files = file.listFiles(
                (dir, name) -> collect.contains(name));
        }
    
        if (files == null || files.length == 0)
        {
            return;
        }
    
        Arrays.stream(files).forEach(k -> {
            try
            {
                String substring = k.getName().substring(0, k.getName().indexOf("."));
                String className = packageName + "." + substring;
                generatorHttpFile(Class.forName(className), substring);
            }
            catch (ClassNotFoundException | IOException e)
            {
                e.printStackTrace();
            }
        });
    }
    
    private void generatorHttpFile(Class aClass, String controllerFileName)
        throws IOException
    {
        // 准备.http文件
        File file = new File(
            "src\\main\\java\\" + aClass.getPackage().getName().replace(".", "\\") + "\\" +
                controllerFileName + ".http");
        if (!file.exists())
        {
            file.createNewFile();
            
            // 准备写入文件的数据
            FileWriter fileWriter = new FileWriter(file);
            try
            {
                StringBuilder line = new StringBuilder();
                
                // 记着查看AtomicReference的用法，和String到底有什么区别，这里为什么要用它来替换String
                AtomicReference<StringBuilder> requestUrl = new AtomicReference<>(new StringBuilder("{{host}}"));
                RequestMapping classRequestMappingAnnotation = (RequestMapping)aClass
                    .getAnnotation(RequestMapping.class);
                if (classRequestMappingAnnotation != null)
                {
                    requestUrl.get().append(classRequestMappingAnnotation.value()[0]);
                }
                String string = requestUrl.get().toString();
                
                Method[] declaredMethods = aClass.getDeclaredMethods();
                Arrays.stream(declaredMethods).forEach(k -> {
                    String mappingUrl = getMappingUrl(k);
                    String mappingType = getMappingType(k);
                    String methodParamsOfBaseType = getMethodParamsOfBaseType(k);
                    requestUrl.get().append(mappingUrl);
                    ApiOperation annotation = k.getAnnotation(ApiOperation.class);
                    line.append("### ");
                    if (annotation != null)
                    {
                        line.append(annotation.value());
                    }
                    line.append("\n" + mappingType + " " + requestUrl + methodParamsOfBaseType + "\n");
                    line.append("Accept: */*\n"
                        + "Cache-Control: no-cache\n"
                        + "Content-Type: application/json\n"
                        + "CURRENT_USER_ID: wangrong\n"
                        + "productLine: 045969\n\n");
                    
                    // 获取@RequestBody注解对象参数并转换成json对象字符串
                    requestUrl.set(new StringBuilder(string));
                });
                fileWriter.write(line.toString());
            }
            catch (Exception e)
            {
                FileUtils.forceDelete(file);
            }
            finally
            {
                fileWriter.close();
            }
        }
        else
        {
            FileUtils.forceDelete(file);
        }
    }
    
    private String getMappingUrl(Method k)
    {
        PutMapping annotation = k.getAnnotation(PutMapping.class);
        if (annotation != null && annotation.value() != null && annotation.value().length > 0)
        {
            return annotation.value()[0];
        }
        GetMapping annotation1 = k.getAnnotation(GetMapping.class);
        if (annotation1 != null && annotation1.value() != null && annotation1.value().length > 0)
        {
            return annotation1.value()[0];
        }
        PostMapping annotation2 = k.getAnnotation(PostMapping.class);
        if (annotation2 != null && annotation2.value() != null && annotation2.value().length > 0)
        {
            return annotation2.value()[0];
        }
        DeleteMapping annotation3 = k.getAnnotation(DeleteMapping.class);
        if (annotation3 != null && annotation3.value() != null && annotation3.value().length > 0)
        {
            return annotation3.value()[0];
        }
        PatchMapping annotation4 = k.getAnnotation(PatchMapping.class);
        if (annotation4 != null && annotation4.value() != null && annotation4.value().length > 0)
        {
            return annotation4.value()[0];
        }
        //以上都不是的话，说明url是存在@RequestMapping(value = "/export/template", method = RequestMethod.GET)中的
        RequestMapping annotation5 = k.getAnnotation(RequestMapping.class);
        if (annotation5 != null)
        {
            if (annotation5.value() != null && annotation5.value().length > 0)
            {
                String[] value = annotation5.value();
                if (value != null && value.length > 0)
                {
                    return value[0];
                }
            }
            else
            {
                return "";
            }
            
        }
        return "";
    }
    
    private String getRequestBodyParamToJsonStr(Method k)
        throws IllegalAccessException, InstantiationException
    {
        Parameter[] parameters = k.getParameters();
        if (parameters == null || parameters.length == 0)
        {
            return "";
        }
        
        List<Parameter> collect = Arrays.stream(parameters).filter(p -> p.getAnnotation(RequestBody.class) != null)
            .collect(Collectors.toList());
        
        if (collect == null || collect.size() == 0)
        {
            return "";
        }
        Class type = collect.get(0).getType();
        Object o = type.newInstance();
        Gson gson = new Gson();
        String s = gson.toJson(o);
        return s;
    }
    
    /**
     * 获取方法的参数(只获取基本类型参数)并且拼接成URL
     *
     * @param k k
     * @return String
     */
    private String getMethodParamsOfBaseType(Method k)
    {
        StringBuilder s = new StringBuilder();
        Parameter[] parameters = k.getParameters();
        if (parameters == null || parameters.length == 0)
        {
            return "";
        }
        List<Parameter> collect = Arrays.stream(parameters).filter(p -> isBaseType(p.getType()))
            .collect(Collectors.toList());
        if (collect != null && collect.size() > 0)
        {
            for (int i = 0; i < collect.size(); i++)
            {
                if (i == 0)
                {
                    s.append("?");
                }
                else
                {
                    s.append("&");
                }
                s.append(collect.get(i).getName() + "=");
            }
        }
        return s.toString();
    }
    
    private String getMappingType(Method k)
    {
        PutMapping annotation = k.getAnnotation(PutMapping.class);
        if (annotation != null)
        {
            return "PUT";
        }
        GetMapping annotation1 = k.getAnnotation(GetMapping.class);
        if (annotation1 != null)
        {
            return "GET";
        }
        PostMapping annotation2 = k.getAnnotation(PostMapping.class);
        if (annotation2 != null)
        {
            return "POST";
        }
        DeleteMapping annotation3 = k.getAnnotation(DeleteMapping.class);
        if (annotation3 != null)
        {
            return "DELETE";
        }
        PatchMapping annotation4 = k.getAnnotation(PatchMapping.class);
        if (annotation4 != null)
        {
            return "PATCH";
        }
        //以上都不是的话，说明url是存在@RequestMapping(value = "/export/template", method = RequestMethod.GET)中的
        RequestMapping annotation5 = k.getAnnotation(RequestMapping.class);
        if (annotation5 != null)
        {
            RequestMethod[] method = annotation5.method();
            if (method != null && method.length > 0)
            {
                return method[0].name();
            }
        }
        return "POST";
    }
}
