#include

#ifdef _WIN32
#define PATH_SEPARATOR ';'
#else /* UNIX */
#define PATH_SEPARATOR ':'
#endif

#define USER_CLASSPATH "." /* where Prog.class is */

#define MAIN_CLASS  "MyApp2"
#pragma comment(lib,"jvm.lib")
int main()
{
    JNIEnv *env;
    JavaVM *jvm;
    jint res;
    jclass cls;
    jmethodID mid;
    jstring jstr;
    jobjectArray args;
    char classpath[1024];


    JavaVMInitArgs vm_args;
    JavaVMOption options[4];

    /* disable JIT */
    options[0].optionString = "-Djava.compiler=NONE";
    /* user classes */
    options[1].optionString = "-Djava.library.path=.";
    /*native library path*/
    options[2].optionString = "-Djava.class.path=D:\\idea\\gui\\out\\artifacts\\app\\app.jar";
    /* print JNI-related messages */
    //options[3].optionString = "-verbose:jni";
    options[3].optionString = "-verbose";
    vm_args.version = JNI_VERSION_1_6;
    vm_args.options = options;
    vm_args.nOptions = 4;
    vm_args.ignoreUnrecognized = JNI_TRUE;

    /* Note that in the Java 2 SDK, there is no longer any need to call
     * JNI_GetDefaultJavaVMInitArgs.
     */

    /* Append USER_CLASSPATH to the end of default system class path */
    /*      sprintf(classpath, "%s%c%s", */
    /*              vm_args.classpath, PATH_SEPARATOR, USER_CLASSPATH); */
    /*      vm_args.classpath = classpath; */

    /* Create the Java VM */
    res = JNI_CreateJavaVM(&jvm, (void **)&env, &vm_args);
    if (res < 0){
        fprintf(stderr,"ERROR creat JVM failed resultcode=%d\n",res);
        exit(1);
        //D:\Java\jdk1.7.0_03\jre\bin\client
    }

    cls = (*env)->FindClass(env, MAIN_CLASS);
    if (cls == 0) {
        fprintf(stderr, "Can't find Prog class\n");
        exit(1);
    }

    mid = (*env)->GetStaticMethodID(env, cls, "main", "([Ljava/lang/String;)V");
    if (mid == 0) {
        fprintf(stderr, "Can't find Prog.main\n");
        exit(1);
    }

    jstr = (*env)->NewStringUTF(env, " from C!");
    if (jstr == 0) {
        fprintf(stderr, "Out of memory\n");
        exit(1);
    }
    args = (*env)->NewObjectArray(env, 1,
                                  (*env)->FindClass(env, "java/lang/String"), jstr);
    if (args == 0) {
        fprintf(stderr, "Out of memory\n");
        exit(1);
    }
    fprintf(stderr,"calling java...\n");
    (*env)->CallStaticVoidMethod(env, cls, mid, args);
    fprintf(stderr,"returned from java. killing VM.\n");

    (*jvm)->DestroyJavaVM(jvm);
}