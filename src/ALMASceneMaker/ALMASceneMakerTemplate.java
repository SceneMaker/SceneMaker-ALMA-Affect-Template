package ALMASceneMaker;


import java.io.IOException;
import java.io.InputStream;



public class ALMASceneMakerTemplate {
    
    static final String commandSceneMaker = "java -cp ./lib/core.jar:./lib/javaGeom-0.11.1.jar:./lib/JCup.jar:./lib/JFlex.jar:./lib/JPL3.jar:./lib/ujmp-complete-0.2.5.jar:./lib/VSM.jar de.dfki.vsm.DefaultEditor true";
    //static final String commandAlma = "java -cp ./lib/processing/jogl-all.jar:./lib/ant.jar:./lib/j3dcore.jar:./lib/j3dutils.jar:./lib/j3dcore-ogl.dll:./lib/processing/gluegen-rt.jar:./lib/processing/gluegen-rt-natives-macosx-universal.jar:./lib/processing/jogl-all-natives-macosx-universal.jar:./lib/affect.jar:./lib/emotionml.jar:./lib/jama.jar:./lib/jsr173_1.0_api.jar:./lib/vecmath.jar:./lib/xbean.jar:./lib/processing/core.jar:./lib/PeasyCam.jar:./lib/geomerative.jar: de.affect.manage.AffectManager ./conf/EvaluationComputation.aml ./conf/EvaluationDefinition.aml true";
    
    public static void main(final String[] args) throws IOException {
        
        SceneMakerLauncher SL = new SceneMakerLauncher();
        AlmaLauncher AL = new AlmaLauncher(SL);
        
        // Run a java app in a separate system process
        Process procSceneMaker = Runtime.getRuntime().exec(commandSceneMaker);
        
       
          
        // Then retreive the process output
        //InputStream inSceneMaker = procSceneMaker.getInputStream();
        //InputStream errSceneMaker = procSceneMaker.getErrorStream();
        
        
        
        //Process procAlma = Runtime.getRuntime().exec(commandAlma);
        //InputStream inAlma = procAlma.getInputStream();
       // InputStream errAlma = procAlma.getErrorStream();
        
                
       

       
    }
    
}

