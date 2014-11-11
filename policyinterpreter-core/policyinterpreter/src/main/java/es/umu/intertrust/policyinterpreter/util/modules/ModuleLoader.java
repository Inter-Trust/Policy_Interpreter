package es.umu.intertrust.policyinterpreter.util.modules;

import es.umu.intertrust.policyinterpreter.util.collections.ListHashMap;
import es.umu.intertrust.policyinterpreter.util.collections.ListMap;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 * @author Juanma
 */
public class ModuleLoader {

    Collection<Class> classes;
    ListMap<Class, Object> loadedInstances;
    ClassLoader classLoader;

    public ModuleLoader(Class c) {
        this(Arrays.asList(new Class[]{c}));
    }

    public ModuleLoader(Collection<Class> classes) {
        this.classes = classes;
        this.loadedInstances = new ListHashMap<Class, Object>();
    }

    public void loadModules(File modulesDirectory) throws ModuleLoadingExceptions {
        ModuleLoadingExceptions exception = null;
        try {
            Collection<File> jars = getJars(modulesDirectory);
            URL[] urls = new URL[jars.size()];
            int i = 0;
            for (File jar : jars) {
                try {
                    urls[i++] = jar.toURI().toURL();
                } catch (MalformedURLException e) {
                    throw new ModuleLoadingException("Unable to load " + jar.getName() + ".", e);
                }
            }
            classLoader = new URLClassLoader(urls);
            loadedInstances.clear();
            for (File jar : jars) {
                try {
                    loadClasses(new JarFile(jar));
                } catch (IOException e) {
                    throw new ModuleLoadingException("Unable to load " + jar.getName() + ".", e);
                }
            }
        } catch (ModuleLoadingException e) {
            if (exception == null) {
                exception = new ModuleLoadingExceptions("Errors have occurred while loading modules.");
            }
            exception.addCause(e);
        }
        if (exception != null) {
            throw exception;
        }
    }

    public <T> Collection<T> getLoadedInstances(Class<T> c) {
        return (Collection<T>) loadedInstances.getList(c);
    }

    private Collection<File> getJars(File directory) throws ModuleLoadingException {
        if (!directory.canRead()) {
            throw new ModuleLoadingException("Unable to read directory: " + directory.getAbsolutePath());
        }
        Collection<File> jars = new ArrayList<File>();
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                jars.addAll(getJars(file));
            } else if (file.getName().endsWith(".jar")) {
                jars.add(file);
            }
        }
        return jars;
    }

    private void loadClasses(JarFile jar) throws ModuleLoadingException {
        Enumeration<JarEntry> jarEntries = jar.entries();
        while (jarEntries.hasMoreElements()) {
            String entryName = jarEntries.nextElement().getName();
            if (entryName.endsWith(".class")) {
                String className = "";
                StringTokenizer tokenizer = new StringTokenizer(entryName, "/");
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    if (tokenizer.hasMoreTokens()) {
                        className = className + token + '.';
                    } else {
                        className = className + token.substring(0, token.length() - 6);
                    }
                }
                try {
                    loadClass(className);
                } catch (ModuleLoadingException ex) {
                    throw ex;
                } catch (Throwable t) {
                    throw new ModuleLoadingException("Unable to load " + jar.getName() + ".", t);
                }
            }
        }
    }

    private void loadClass(String className) throws InstantiationException, IllegalAccessException, ModuleLoadingException {
        try {
            Class loadedClass = classLoader.loadClass(className);
            if (!loadedClass.isInterface() && !Modifier.isAbstract(loadedClass.getModifiers())) {
                for (Class c : classes) {
                    if (c.isAssignableFrom(loadedClass)) {
                        try {
                            loadedClass.getConstructor();
                        } catch (NoSuchMethodException ex) {
                            throw new ModuleLoadingException("Class should implement constructor without parameters: " + loadedClass.getName());
                        }
                        loadedInstances.addToList(c, loadedClass.newInstance());
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
