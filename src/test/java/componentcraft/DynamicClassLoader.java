package componentcraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class DynamicClassLoader {

    public static void compileJavaFiles(File file, List<String> imports){


    }



    public static void compileJavaFile(File sourceFile) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("No Compiler found");
        }

        int result = compiler.run(null, null, null, sourceFile.getPath());
        if (result != 0) {
            throw new RuntimeException("Error on compile: " + sourceFile.getPath());
        }

        BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try {
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

        }catch (Exception e){}

        String file = stringBuilder.toString();

    }


    public static void loadClassDynamically(String className, List<String> imports) throws Exception {

        File classesDir = new File("src/main/java/");


        URLClassLoader classLoader = new URLClassLoader(
                new URL[]{classesDir.toURI().toURL()},
                null);

//        for(String importString : imports){
//            classLoader.loadClass(importString);
//        }


        Class<?> compileSignInterface = classLoader.loadClass("component.CompileSign");
        Class<?> test2Class = classLoader.loadClass("component.Test2");
//        Class<?> test2Class = Class.forName("component.Test2", true, classLoader);

        Object classC = test2Class.getDeclaredConstructor().newInstance();

        if (compileSignInterface.isInstance(classC)) {
            Method method = compileSignInterface.getMethod("test");
            method.invoke(classC);
        }
    }

}
