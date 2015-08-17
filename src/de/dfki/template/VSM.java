package de.dfki.template;

//~--- non-JDK imports --------------------------------------------------------

import static de.dfki.template.AlmaMonitor.log;
import de.dfki.vsm.SceneMaker3;
import de.dfki.vsm.editor.EditorInstance;
import de.dfki.vsm.editor.project.EditorProject;
import de.dfki.vsm.runtime.RunTimeInstance;
import de.dfki.vsm.runtime.values.AbstractValue;
import de.dfki.vsm.runtime.values.StructValue;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;

/**
 *
 * @author Sergio Soto
 */
public class VSM {
    SceneMaker3 VSM = null;

    public VSM() {
        try {
          //  VSM = new Editor("res/prj/almaTemplate/"");   
            log.info("Starting Visual SceneMaker");
            
            String[] args = new String[]{};
            VSM = new SceneMaker3(args);      
        
        } catch (Exception e) {
            log.info("Error during VSM initialisation");
            System.exit(-1);
        }
    }

    public void setStructVSM(String name, HashMap<String, AbstractValue> map) {
        try {
            RunTimeInstance     runTime   = RunTimeInstance.getInstance();            
            EditorProject project = EditorInstance.getInstance().getSelectedProjectEditor().getEditorProject();         
            StructValue struct     = new StructValue(map);

            runTime.setVariable(project, name, struct);
        } catch (Exception e) {
           // System.out.println("not running");
        }
    }

    public void setFloatVariableVSM(String name, Float value) {
        try {
            RunTimeInstance     runTime   = RunTimeInstance.getInstance();            
            EditorProject project = EditorInstance.getInstance().getSelectedProjectEditor().getEditorProject();         
            runTime.setVariable(project, name, value);              
        } catch (Exception e) {
           //    System.out.println("not running");
        }
    }
}
