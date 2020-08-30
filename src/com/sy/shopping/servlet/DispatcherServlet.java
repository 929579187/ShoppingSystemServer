package com.sy.shopping.servlet;

import com.alibaba.fastjson.JSON;
import com.sy.shopping.annotations.RequestMapping;
import com.sy.shopping.constant.AppConstant;
import com.sy.shopping.dto.HandlerMethodMappingObject;
import com.sy.shopping.dto.Result;
import com.sy.shopping.exception.BusinessException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个Servlet用于在系统中统一接收所有的请求并统一返回响应
 * 用于替代原来项目中很多很多的Servlet
 */
@WebServlet(urlPatterns = AppConstant.SERVER_APIS_PATTERN, loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // /apis/heroes/add
        // /apis/powers/add
        //1.获取请求过程中，请求路径/apis 后面的一段内容
        String uri = req.getRequestURI()
                .replace(req.getContextPath(), "")
                .replace(AppConstant.SERVER_APIS_PREFIX, "");

        //2.根据/apis 后面的内容来决定调用哪个Handler中的哪个方法来进行处理
        //因为Handler类和Handler类中的方法上面都标注了@RequestMapping注解
        //因此Handler类中的每个方法可以理解为：其对应的访问路径就是：
        // /apis/Handler类上注解中的值/Handler类中方法上注解的值
        //(1)获取在Servlet初始化过程中得到的所有的请求列表对象
        List<HandlerMethodMappingObject> handlerMethodMappingObjectList = (List<HandlerMethodMappingObject>) req.getServletContext().getAttribute(AppConstant.SERVLET_CONTEXT_HANDLER_MAPPING_LIST_ATTRIBUTE_NAME);
        //(2)根据自己实际访问的路径到handlerMethodMappingObjectList中查找有无对应的HandlerMethodMappingObject对象
        HandlerMethodMappingObject findedhandlerMethodMappingObject = null;
        for (HandlerMethodMappingObject handlerMethodMappingObject : handlerMethodMappingObjectList) {
            if (handlerMethodMappingObject.getUrl().equals(uri)) {
                findedhandlerMethodMappingObject = handlerMethodMappingObject;
                break;
            }
        }
        //(3)判断有没有找到匹配的HandlerMethodMappingObject对象
        //(3-1)如果没有找到
        if (null == findedhandlerMethodMappingObject) {
            resp.setContentType("text/html;charset=utf-8");
            PrintWriter pw = resp.getWriter();
            pw.print("访问路径<span style='color:red;'>" + uri + "</span>没有与之对应的处理器及处理器方法");
            return;
        }
        //(3-2)如果找到
        else {
            Result jsonResult;
            try {
                //处理器对象
                Object handlerObj = findedhandlerMethodMappingObject.getHandlerObj();
                //要执行的处理器的方法的方法对象
                Method handlerMethod = findedhandlerMethodMappingObject.getMethod();
                //反射调用Handler中的方法并返回结果
                Object result = handlerMethod.invoke(handlerObj);
                jsonResult = Result.buildSuccess(result);
            } catch (BusinessException e) {
                e.printStackTrace();
                jsonResult = Result.buildFailure(e.getMessage());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                Throwable throwable = e.getTargetException();
                //这里的异常有可能是除了BusinessException以外的其它异常类型
                //统一处理为服务端异常
                Exception realException = (Exception) throwable;
                if (realException instanceof BusinessException) {
                    jsonResult = Result.buildFailure(realException.getMessage());
                } else {
                    jsonResult = Result.buildFailure("服务端异常");
                }
            } catch (Exception e) {
                e.printStackTrace();
                jsonResult = Result.buildFailure("服务端异常");
            }
            //将JSON结果写出给前端
            resp.setContentType("application/json;charset=utf-8");
            PrintWriter pw = resp.getWriter();
            pw.print(JSON.toJSONString(jsonResult, true));

        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //在Servlet初始化的过程中，将项目中所有Handler方法的访问路径以及该访问路径
        //对应的Handler实例以及方法对象统一保存下来，以供后面使用

        //1.获取所有的Handler方法对应的具体信息（Handler方法访问的路径，对应Handler实例及要调用的方法）
        List<HandlerMethodMappingObject> handlerMethodMappingObjectList = new ArrayList<>();

        //2.获取所有带@RequestMapping注解的Handler类
        //假定所有的Handler类都在一个包下，所以只要先去获取指定包下的所有Handler类即可

        try {
            //(1)获取运行时实际的类路径
            String realClasspath =
                    //这里需要解码的原因是因为如果路径中有空格或中文会被转义成%xx的形式，因此需要解码
                    URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource("").getPath(), "UTF-8");
            //(2)将类路径和Handler所在的包的路径拼接起来，得到Handler类实际所在的目录
            String handlerClassesPath = realClasspath + AppConstant.HANDLERS_PACKAGE.replace(".", File.separator);
            //(3)获取Handler包下所有的文件的完整路径(字节码class文件)
            File[] handlerClassesFiles = new File(handlerClassesPath).listFiles();
            if (null != handlerClassesFiles) {
                for (File handlerClassFile : handlerClassesFiles) {
                    //(4)获取到处理器类对应字节码文件的名字（不包含前面的完整路径）
                    String handlerClassFileName = handlerClassFile.getName().replace(".class", "");
                    //(5)根据处理器类的名字获取处理器类的类信息
                    Class<?> handlerClass = Class.forName(AppConstant.HANDLERS_PACKAGE + "." + handlerClassFileName);
                    Object handlerObj = handlerClass.newInstance();
                    //(6)获取类上对应的@RequestMapping注解
                    RequestMapping handlerRequestMappingAnnotation = handlerClass.getDeclaredAnnotation(RequestMapping.class);
                    //(6-1)如果类上没有注解
                    if (null == handlerRequestMappingAnnotation) {
                        throw new IllegalArgumentException(handlerClass.getName() + "没有加@RequestMapping注解");
                    }
                    //(6-2)如果类上有注解
                    else {
                        //获取注解上给定的路径值
                        String classRequestMappingValue = handlerRequestMappingAnnotation.value();

                        //(7)获取Handler中的所有方法
                        Method[] handlerMethods = handlerClass.getDeclaredMethods();

                        //(8)遍历每一个方法
                        if (null != handlerMethods) {
                            for (Method handlerMethod : handlerMethods) {
                                // (9)获取每个方法上的注解中的路径
                                RequestMapping methodRequestMapping = handlerMethod.getDeclaredAnnotation(RequestMapping.class);
                                //(9-1)如果方法上没@RequestMapping注解
                                if (null == methodRequestMapping) {
                                    throw new IllegalArgumentException(handlerClass.getName() + "处理器的" + handlerMethod.getName() + "方法上没有@RequestMapping注解");
                                }
                                //(9-2)如果方法上有@RequestMapping注解
                                else {
                                    //创建HandlerMethodMappingObject对象来描述每个处理器方法的具体信息
                                    HandlerMethodMappingObject handlerMethodMappingObject = new HandlerMethodMappingObject();
                                    //设置Handler方法的访问路径
                                    handlerMethodMappingObject.setUrl("/" + classRequestMappingValue + "/" + methodRequestMapping.value());
                                    //该URL对应的处理器的实例
                                    handlerMethodMappingObject.setHandlerObj(handlerObj);
                                    //该URL对应的处理器中的方法
                                    handlerMethodMappingObject.setMethod(handlerMethod);
                                    handlerMethodMappingObjectList.add(handlerMethodMappingObject);
                                }
                            }
                        }
                    }
                }
            }

            //这里得到的handlerMethodMappingObjectList描述了系统中所有可以访问的路径
            //及其对应的处理器以及处理器方法信息，存储在全局的Servlet上下文作用域中保证整个应用都是共享的
            config.getServletContext().setAttribute(AppConstant.SERVLET_CONTEXT_HANDLER_MAPPING_LIST_ATTRIBUTE_NAME, handlerMethodMappingObjectList);
            System.out.println("当前项目中所有服务端URL访问路径如下：");
            handlerMethodMappingObjectList.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
