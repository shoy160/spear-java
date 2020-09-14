package cn.spear.core.util;

import cn.spear.core.lang.Func;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Type Utils
 *
 * @author shay
 * @date 2020/9/8
 */
public class TypeUtils {
    public static ParameterizedType toParameterizedType(Type type) {
        ParameterizedType result = null;
        if (type instanceof ParameterizedType) {
            result = (ParameterizedType) type;
        } else if (type instanceof Class) {
            Class<?> clazz = (Class) type;
            Type genericSuper = clazz.getGenericSuperclass();
            if (null == genericSuper || Object.class.equals(genericSuper)) {
                Type[] genericInterfaces = clazz.getGenericInterfaces();
                if (genericInterfaces.length > 0) {
                    genericSuper = genericInterfaces[0];
                }
            }

            result = toParameterizedType(genericSuper);
        }
        return result;
    }

    public static <T> Class<T> getGenericClass(Type type, int index) {
        try {
            ParameterizedType parameterizedType = toParameterizedType(type);
            if (parameterizedType == null) {
                return null;
            }
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            if (CommonUtils.isEmpty(typeArguments) || typeArguments.length <= index) {
                return null;
            }
            Type resultType = typeArguments[index];
            return (Class<T>) resultType;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static <T> T createGenericInstance(Class<?> parentClazz) {
        return createGenericInstance(parentClazz, 0);
    }

    public static <T> T createGenericInstance(Class<?> parentClazz, int index) {
        Class<T> clazz = getGenericClass(parentClazz, index);
        if (clazz == null) {
            return null;
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName package
     * @param packagePath path
     * @param recursive   recursive
     * @param classes     classes
     */
    private static void findClassesInPackageByFile(
            String packageName, String packagePath, final boolean recursive,
            Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
        File[] dirFiles = dir.listFiles(file -> (recursive && file.isDirectory()) || (file.getName().endsWith(".class")));
        if (null == dirFiles) {
            return;
        }
        // 循环所有文件
        for (File file : dirFiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                if (CommonUtils.isEmpty(packageName)) {
                    findClassesInPackageByFile(file.getName(), file.getAbsolutePath(), recursive, classes);

                } else {
                    findClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
                }

            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className);
                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 以jar的形式来获取包下的所有Class
     *
     * @param packageName    package
     * @param entries        entries
     * @param packageDirName dir
     * @param recursive      recursive
     * @param classes        classes
     */
    private static void findClassesInPackageByJar(
            String packageName, Enumeration<JarEntry> entries, String packageDirName, final boolean recursive,
            Set<Class<?>> classes) {
        // 同样的进行循环迭代
        while (entries.hasMoreElements()) {
            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            // 如果是以/开头的
            if (name.charAt(0) == '/') {
                // 获取后面的字符串
                name = name.substring(1);
            }
            // 如果前半部分和定义的包名相同
            if (name.startsWith(packageDirName)) {
                int idx = name.lastIndexOf('/');
                // 如果以"/"结尾 是一个包
                if (idx != -1) {
                    // 获取包名 把"/"替换成"."
                    packageName = name.substring(0, idx).replace('/', '.');
                }
                // 如果可以迭代下去 并且是一个包
                if ((idx != -1) || recursive) {
                    // 如果是一个.class文件 而且不是目录
                    if (name.endsWith(".class") && !entry.isDirectory()) {
                        // 去掉后面的".class" 获取真正的类名
                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                        try {
                            // 添加到classes
                            Class<?> clazz = Class.forName(packageName + '.' + className);
                            classes.add(clazz);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private static final Map<String, Set<Class<?>>> clazzCache = new HashMap<>();

    /**
     * 根据包名获取包下面所有的类名
     *
     * @param pack 包名
     * @return set
     */
    public static Set<Class<?>> findClasses(String pack) {
        if (null == pack) {
            pack = "";
        }
        if (clazzCache.containsKey(pack)) {
            LoggerFactory.getLogger(TypeUtils.class).info("load pack[{}] classes from cache", pack);
            return clazzCache.get(pack);
        }
        // 第一个class类的集合
        Set<Class<?>> classes = new LinkedHashSet<>();
        // 获取包的名字 并进行替换
        String packageDirName = pack.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findClassesInPackageByFile(pack, filePath, true, classes);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
                    System.out.println("jar类型的扫描");
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        findClassesInPackageByJar(pack, entries, packageDirName, true, classes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        clazzCache.putIfAbsent(pack, classes);
        return classes;
    }

    public static Set<Class<?>> findClasses(Func<Boolean, Class<?>> filter) {
        return findClasses("", filter);
    }

    public static Set<Class<?>> findClasses(String pack, Func<Boolean, Class<?>> filter) {
        Set<Class<?>> clazzList = findClasses(pack);
        if (CommonUtils.isEmpty(clazzList)) {
            return new LinkedHashSet<>();
        }
        if (null == filter) {
            return new LinkedHashSet<>(clazzList);
        }
        Set<Class<?>> result = new LinkedHashSet<>();
        for (Class<?> clazz : clazzList) {
            if (filter.invoke(clazz)) {
                result.add(clazz);
            }
        }
        return result;
    }
}
